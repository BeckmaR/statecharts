/**
 * Copyright (c) 2019 itemis AG - All rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * 
 * Contributors:
 * 	itemis AG
 * 
 */
package org.yakindu.sct.types.generator.cpp.modifications

import com.google.inject.Inject
import java.util.Collection
import org.yakindu.base.expressions.util.ExpressionBuilder
import org.yakindu.base.expressions.util.ExpressionsHelper
import org.yakindu.base.types.ComplexType
import org.yakindu.base.types.EnumerationType
import org.yakindu.base.types.Package
import org.yakindu.base.types.Property
import org.yakindu.base.types.TypeBuilder
import org.yakindu.base.types.TypesFactory
import org.yakindu.base.types.Visibility
import org.yakindu.sct.types.generator.c.typesystem.CTypeSystem
import org.yakindu.sct.types.modification.IModification

class InterfaceGetterModification implements IModification {

	protected TypesFactory typesFactory = TypesFactory.eINSTANCE
	@Inject protected extension ExpressionsHelper
	@Inject protected extension CTypeSystem cts
	@Inject protected extension ExpressionBuilder
	@Inject protected extension TypeBuilder

	override modify(Collection<Package> packages) {
		packages.forEach[modify]
		packages
	}

	def modify(Package p) {
		val annotationType = typesFactory.createAnnotationType => [ at |
			at.name = "PointerEleRef"
		]
		val outerClass = p.member.filter(ComplexType).head
		val properties = p.eAllContents.filter(Property).filter[eContainer instanceof ComplexType].toList
		val interfaces = outerClass.eAllContents.filter(ComplexType).filter[!(it instanceof EnumerationType)].filter [
			visibility == Visibility.PUBLIC
		].toList

		interfaces.forEach [ cT |
			properties.forEach [ prop |
				if (prop.type == cT) {
					outerClass.features += typesFactory.createOperation => [ op |
						op.name = "get" + cT.name
						op.typeSpecifier = cT.createPointerType
						prop._annotateWith(annotationType)
						op.body = _block(_return(prop._ref))
					]
				}
			]
		]
		return p
	}

	def protected createPointerType(ComplexType cT) {
		typesFactory.createTypeSpecifier => [ ts |
			ts.type = cts.getType(CTypeSystem.POINTER)
			ts.typeArguments += cT.typeSpecifier
		]
	}
}
