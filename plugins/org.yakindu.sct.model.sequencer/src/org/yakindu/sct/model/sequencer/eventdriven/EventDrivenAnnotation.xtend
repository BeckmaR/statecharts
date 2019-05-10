package org.yakindu.sct.model.sequencer.eventdriven

import com.google.inject.Singleton
import org.yakindu.base.types.AnnotatableElement
import org.yakindu.base.types.Package
import org.yakindu.base.types.TypesFactory

import static org.yakindu.sct.model.stext.lib.StatechartAnnotations.EVENT_DRIVEN_ANNOTATION

@Singleton class EventDrivenAnnotation {

	extension TypesFactory factory = TypesFactory.eINSTANCE

	def isEventDriven(AnnotatableElement it) {
		getAnnotationOfType(EVENT_DRIVEN_ANNOTATION) !== null
	}

	def create createAnnotationType createEventDrivenAnnotationType(Package p) {
		it.name = EVENT_DRIVEN_ANNOTATION
		p.member += it
	}

}
