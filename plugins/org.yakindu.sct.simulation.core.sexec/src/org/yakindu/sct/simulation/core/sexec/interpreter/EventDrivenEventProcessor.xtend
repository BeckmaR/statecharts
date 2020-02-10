package org.yakindu.sct.simulation.core.sexec.interpreter

import org.yakindu.sct.model.sruntime.ExecutionEvent
import java.util.Queue
import org.yakindu.base.types.Event
import java.util.LinkedList
import org.yakindu.sct.model.sruntime.IEventProcessor

class EventDrivenEventProcessor implements IEventProcessor {
	
	protected Queue<Event> inEventQueue = new LinkedList<Event>()
	protected Queue<Event> internalEventQueue = new LinkedList<Event>()
	
	
	override defer(ExecutionEvent ev) {
		throw new UnsupportedOperationException("TODO: auto-generated method stub")
	}
	
	override raise(ExecutionEvent ev, Object value) {
		throw new UnsupportedOperationException("TODO: auto-generated method stub")
	}
	
	
	
}