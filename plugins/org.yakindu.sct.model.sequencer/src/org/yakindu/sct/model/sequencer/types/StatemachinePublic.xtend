/** 
 * Copyright (c) 2019 committers of YAKINDU and others. 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html 
 * Contributors:
 * committers of YAKINDU - initial API and implementation
 *
*/
package org.yakindu.sct.model.sequencer.types

import com.google.inject.Inject
import com.google.inject.Singleton
import org.eclipse.emf.ecore.util.EcoreUtil
import org.yakindu.base.expressions.util.ExpressionBuilder
import org.yakindu.base.expressions.util.PackageNavigationExtensions
import org.yakindu.base.types.ComplexType
import org.yakindu.base.types.Declaration
import org.yakindu.base.types.Property
import org.yakindu.base.types.TypeBuilder
import org.yakindu.base.types.TypesFactory
import org.yakindu.base.types.Visibility
import org.yakindu.sct.model.sequencer.ModelSequencerNaming
import org.yakindu.sct.model.sequencer.util.SequencerAnnotationLibrary
import org.yakindu.sct.model.sgraph.RegularState
import org.yakindu.sct.model.sgraph.Statechart
import org.yakindu.sct.model.stext.stext.InterfaceScope
import org.yakindu.sct.model.stext.stext.InternalScope

/**
 * This class implements a transformation that creates the state machine type
 * from a statechart. It covers only all publicly visible elements which are provided
 * to users. It creates no internal declarations and no implementations.
 *  
 * @author andreas muelder - Initial contribution and API
 * @author thomas kutz
 * @author axel terfloth
 * 
 */
@Singleton class StatemachinePublic {

	extension TypesFactory factory = TypesFactory.eINSTANCE
	
	@Inject extension IStatemachine 
	@Inject extension ExpressionBuilder
	
	@Inject protected extension ModelSequencerNaming
	@Inject protected extension StatemachineInterfaceMethods
	@Inject protected extension SequencerAnnotationLibrary
	@Inject protected extension TypeBuilder
	@Inject protected extension PackageNavigationExtensions
	
	def create createPackage statemachinePackage(Statechart sc) {
 		it => [
 			name = sc.statemachinePackageName
 		]	
 	}

	def create createComplexType statemachineType(Statechart sc) {

		it => [
			
			name = sc.name
			superTypes += createTypeSpecifier => [ type = statemachineInterfaceType ]
			sc.statemachinePackage.member += statemachineInterfaceType
			sc.statemachinePackage.member += it

			features += interfaceGroupAnnotation

			features += statesEnumeration(sc)
			
			features += sc.scopes.filter(InterfaceScope).map [
				createInterfaceType
			]
			features += sc.scopes.filter(InternalScope).map [
				createInternalType
			]

			declareMembers(sc)
			
			if(sc.isEventDriven) {
				_annotateWith(eventDrivenAnnotation)
				containingPackage.member += eventDrivenAnnotation
			} else {
				_annotateWith(cycleBasedAnnotation)
				containingPackage.member += cycleBasedAnnotation
			}
		]
	}
	
	def create createEnumerationType statesEnumeration(Statechart sc) {
		it => [
			name = sc.statesEnumerationName
			enumerator += sc.noState
			sc.eAllContents.filter(RegularState).forEach [ state |
				enumerator += state.enumerator
			]
		]
	}
	
	def create createEnumerator enumerator(RegularState state) {
		name = state.enumeratorName
	}
	
	def create createEnumerator noState(Statechart sc) {
		it.name = noStateName
	}

	protected def declareMembers(ComplexType scType, Statechart sc) {

		// Named interfaces
		sc.scopes.filter(InterfaceScope).filter[name !== null].forEach [ iface |
			scType.features += iface.property
		]
		
		// Unnamed interface
		sc.scopes.filter(InterfaceScope).filter[name === null].forEach [ iface |
			scType.features += iface.property
		]
		
		// Internal variables
		sc.scopes.filter(InternalScope).forEach [ internal |
			scType.features += internal.property
		]
	}
	
	def create createProperty property(InterfaceScope iface) {
		typeSpecifier = createTypeSpecifier => [
			type = createInterfaceType(iface)
		]
		name = type.name.toFirstLower
		visibility = Visibility.PUBLIC
		initialValue = (type as ComplexType)._new
		
	}
	
	def create createProperty property(InternalScope internal) {
		it.typeSpecifier = createTypeSpecifier => [
			type = createInternalType(internal)
		]
		name = type.name.toFirstLower
		visibility = Visibility.PROTECTED
		initialValue = (type as ComplexType)._new
	}

	protected def create createComplexType createInterfaceType(InterfaceScope iface) {
		it.name = iface.interfaceTypeName
		it.features += createConstructor
		iface.declarations.forEach[decl|features += decl.feature]
		features.filter(Property).filter[!const].forEach[prop|prop.initialValue = null]
		
		features += clearOutEvents
		features += clearEvents
		
		it._annotateWith(interfaceGroupAnnotation)
	}
	
	protected def create createComplexType createInternalType(InternalScope internal) {
		it.name = internalTypeName
		it.features += createConstructor
		internal.declarations.forEach[decl|features += decl.feature => [visibility = Visibility.PROTECTED]]
		features.filter(Property).filter[!const].forEach[prop|prop.initialValue = null]
		
		it._annotateWith(interfaceGroupAnnotation)
		it._annotateWith(internalScopeAnnotation)
		it.visibility = Visibility.PROTECTED
	}
	
	def create EcoreUtil.copy(decl) feature(Declaration decl) {}
}
