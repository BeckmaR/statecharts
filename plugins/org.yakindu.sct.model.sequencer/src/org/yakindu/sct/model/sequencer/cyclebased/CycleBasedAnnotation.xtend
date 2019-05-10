package org.yakindu.sct.model.sequencer.cyclebased

import com.google.inject.Singleton
import org.yakindu.base.types.AnnotatableElement
import org.yakindu.base.types.Package
import org.yakindu.base.types.TypesFactory

import static org.yakindu.sct.model.stext.lib.StatechartAnnotations.CYCLE_BASED_ANNOTATION

@Singleton class CycleBasedAnnotation {
	
	extension TypesFactory factory = TypesFactory.eINSTANCE
	
	def isCycleBased(AnnotatableElement it) {
		getAnnotationOfType(CYCLE_BASED_ANNOTATION) !== null
	}
	
	def create createAnnotationType createCycleBasedAnnotationType(Package p) {
		it.name = CYCLE_BASED_ANNOTATION
		p.member += it
	}
}