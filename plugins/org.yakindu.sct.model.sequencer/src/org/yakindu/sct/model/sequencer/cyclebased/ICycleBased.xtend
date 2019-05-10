package org.yakindu.sct.model.sequencer.cyclebased

import com.google.inject.Inject
import org.yakindu.base.types.TypeBuilder
import org.yakindu.base.types.TypesFactory
import org.yakindu.base.types.typesystem.ITypeSystem

class ICycleBased {
	
 	public static val String NAME = "ICycleBased"

	extension TypesFactory factory = TypesFactory.eINSTANCE
	
 	@Inject extension TypeBuilder
 
 	def create createComplexType cycleBasedInterfaceType() {
		it => [
			name = NAME
			features += createRunCycleMethod
		]
		abstract = true
	}
	
	def createRunCycleMethod() {
		_op("runCycle", ITypeSystem.VOID)
	}
}