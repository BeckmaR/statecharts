package org.yakindu.sct.model.sruntime

import org.eclipse.emf.common.notify.impl.AdapterImpl
import org.eclipse.xtend.lib.annotations.Accessors

class StatechartExecutionAdapter extends AdapterImpl {
	
	@Accessors protected IEventProcessor eventProcessor
	
	new (IEventProcessor processor) {
		this.eventProcessor = processor
	}
	 
}