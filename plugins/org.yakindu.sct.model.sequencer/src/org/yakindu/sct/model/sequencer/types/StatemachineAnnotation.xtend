package org.yakindu.sct.model.sequencer.types

import org.yakindu.base.types.AnnotatableElement
import org.yakindu.base.types.Package
import org.yakindu.base.types.TypesFactory
import com.google.inject.Singleton

@Singleton class StatemachineAnnotation {

	extension TypesFactory factory = TypesFactory.eINSTANCE

	static val STATEMACHINE_ANNOTATION = "Statemachine"

	def isStatemachine(AnnotatableElement it) {
		getAnnotationOfType(STATEMACHINE_ANNOTATION) !== null
	}

	def create createAnnotationType createStatemachineAnnotationType(Package p) {
		it.name = STATEMACHINE_ANNOTATION
		p.member += it
	}
}
