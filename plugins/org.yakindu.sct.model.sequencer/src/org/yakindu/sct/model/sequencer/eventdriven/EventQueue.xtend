package org.yakindu.sct.model.sequencer.eventdriven

import com.google.inject.Inject
import com.google.inject.Singleton
import org.yakindu.base.types.TypeBuilder
import org.yakindu.base.types.TypesFactory
import org.yakindu.base.types.typesystem.ITypeSystem

@Singleton class EventQueue {
	
	public static val NAME = "EventQueue"
	
	extension TypesFactory factory = TypesFactory.eINSTANCE
	
	@Inject extension TypeBuilder
	
	def create createComplexType eventQueueType() {
		
		name = NAME
		
		features += _op("push", ITypeSystem.VOID)._param("element", ITypeSystem.ANY)
		features += _op("pop", ITypeSystem.ANY)
		features += _op("init", ITypeSystem.VOID)
		features += _op("size", ITypeSystem.INTEGER)
	}
	
}
