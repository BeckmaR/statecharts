package org.yakindu.sct.model.sruntime

import org.eclipse.emf.ecore.EObject
import org.yakindu.sct.model.sruntime.ExecutionContext
import org.yakindu.sct.model.sruntime.ExecutionEvent


class StatechartBehavior {
	
	def raise(ExecutionEvent it, Object value) {
		adapter.eventProcessor.raise(it, value)
	}
	
	def StatechartExecutionAdapter getAdapter(EObject it) {
		executionContext.eAdapters.filter(StatechartExecutionAdapter).head		
	} 
	
	protected def ExecutionContext executionContext(EObject it) {

		if (it instanceof ExecutionContext) return it
		if (it.eContainer !== null ) return it.eContainer.executionContext			
		return null
	}
	
}