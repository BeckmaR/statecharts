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
package org.yakindu.sct.model.sequencer

import com.google.inject.Inject
import java.util.List
import org.yakindu.base.types.Package
import org.yakindu.base.types.validation.IValidationIssueAcceptor
import org.yakindu.base.types.validation.IValidationIssueAcceptor.ListBasedValidationIssueAcceptor
import org.yakindu.sct.model.sequencer.cyclebased.InEventModification
import org.yakindu.sct.model.sequencer.cyclebased.LocalEventModification
import org.yakindu.sct.model.sequencer.cyclebased.OutEventModification
import org.yakindu.sct.model.sequencer.eventdriven.EventDrivenAnnotation
import org.yakindu.sct.model.sequencer.expressions.ExpressionOptimizer
import org.yakindu.sct.model.sequencer.expressions.RetargetReferences
import org.yakindu.sct.model.sequencer.operations.EnterDeepOperation
import org.yakindu.sct.model.sequencer.operations.EnterOperation
import org.yakindu.sct.model.sequencer.operations.EnterShallowOperation
import org.yakindu.sct.model.sequencer.operations.EntryActionOperation
import org.yakindu.sct.model.sequencer.operations.EntryReactOperation
import org.yakindu.sct.model.sequencer.operations.ExitActionOperation
import org.yakindu.sct.model.sequencer.operations.ExitOperation
import org.yakindu.sct.model.sequencer.operations.ExitReactOperation
import org.yakindu.sct.model.sequencer.operations.ReactOperation
import org.yakindu.sct.model.sequencer.types.StatemachineMethods
import org.yakindu.sct.model.sequencer.types.StatemachineProperties
import org.yakindu.sct.model.sequencer.types.StatemachinePublic
import org.yakindu.sct.model.sgraph.Statechart
import org.yakindu.slang.modification.IModification
import org.yakindu.sct.model.sequencer.cyclebased.CycleBasedModification

class ModelSequencer implements IModelSequencer {
	 
	@Inject extension RetargetReferences
	
	@Inject extension ReactOperation
	@Inject extension EntryReactOperation
	@Inject extension ExitReactOperation
	@Inject extension EnterOperation
	@Inject extension EnterShallowOperation
	@Inject extension EnterDeepOperation
	@Inject extension ExitOperation
	@Inject extension EntryActionOperation
	@Inject extension ExitActionOperation
	
	@Inject extension StatemachinePublic
	@Inject extension StatemachineMethods
	@Inject	extension StatemachineProperties
	
	@Inject extension ExpressionOptimizer
	
	@Inject extension EventDrivenAnnotation
	
	// TODO: use multibindings and ModificationExecutor
	@Inject InEventModification inEventMod
	@Inject OutEventModification outEventMod
	@Inject LocalEventModification localEventMod
	@Inject CycleBasedModification cycleBasedMod
	
	/* ==========================================================================
	 * TRANSFORMATION ROOT
	 */ 
	override Package transform(Statechart sc) {
		transform(sc, new ListBasedValidationIssueAcceptor)
	}
	
	override Package transform(Statechart sc, IValidationIssueAcceptor acceptor) {
		val pkg = sc.makePackage
		
		for (mod : sc.modifications) {
			mod.modify(#[pkg])
		}
		
		pkg
	}

	protected def create pkg:sc.statemachinePackage makePackage(Statechart sc) {

		val sctype = sc.statemachineType
		
		sctype.defineProperties(sc)

		sc.declareEntryActionOperations
		sc.declareExitActionOperations
		sc.declareEntryReactOperations
		sc.declareExitReactOperations
		sc.declareEnterOperations
		sc.declareEnterShallowOperations
		sc.declareEnterDeepOperations
		sc.declareExitOperations
		sc.declareReactMethods
		
		sc.defineEntryActionOperations
		sc.defineExitActionOperations
		sc.defineExitReactOperations
		sc.defineEntryReactOperations
		sc.defineExitReactOperations
		sc.defineEnterOperations
		sc.defineEnterShallowOperations
		sc.defineEnterDeepOperations
		sc.defineExitOperations
		sc.defineReactMethods
		
		sctype.defineEnterMethod(sc)
		sctype.defineExitMethod(sc)
		sctype.defineInitMethod(sc)
		sctype.defineIsActiveMethod(sc)
		sctype.defineIsFinalMethod(sc)
		sctype.defineIsStateActiveMethod(sc)
		
		sctype.defineRtcMethod(sc)
		sctype.defineClearOutEventsMethod(sc)
		sctype.defineClearEventsMethod(sc)
		
		pkg.retargetReferences
		
		pkg.optimize
	}
	
	protected def List<IModification> getModifications(Statechart sc) {
		if (sc.isEventDriven) {
			
			return #[
				// add localQ
				// add inQ
				
				// create raise method for events (@trigger on raise method)
				
				// resolve @trigger to processEvents call
				
			]
		} else {
			
			return #[
				
				// create raise method for events
				inEventMod,
				outEventMod,
				localEventMod,

				// add ICycleBased & runCycle method with @trigger annotation
				cycleBasedMod
				
				// resolve @trigger to processEvents call
	
			]
		}
		
		
	}
	
}
