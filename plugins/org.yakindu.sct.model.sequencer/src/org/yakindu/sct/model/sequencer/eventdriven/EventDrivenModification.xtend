package org.yakindu.sct.model.sequencer.eventdriven

import com.google.inject.Inject
import org.yakindu.base.types.ComplexType
import org.yakindu.base.types.Package
import org.yakindu.slang.modification.IModification
import java.util.Collection

class EventDrivenModification implements IModification {
	
	@Inject extension EventDrivenAnnotation
	@Inject extension EventQueue
	
	def Package modify(Package p) {
		val eventDrivenMachines = p.member.filter(ComplexType).filter[isEventDriven]
		
		if (!eventDrivenMachines.nullOrEmpty) {
			p.member += eventQueueType
			
			// 
		}
		
		
		p
	}
	
	override modify(Collection<Package> p) {
		p.forEach[modify]
		p
	}
	
}