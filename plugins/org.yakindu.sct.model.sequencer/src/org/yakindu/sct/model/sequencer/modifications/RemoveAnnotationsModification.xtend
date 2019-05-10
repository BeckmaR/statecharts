package org.yakindu.sct.model.sequencer.modifications

import org.yakindu.slang.modification.IModification
import java.util.Collection
import org.yakindu.base.types.Package
import org.yakindu.base.types.AnnotatableElement
import org.yakindu.base.types.AnnotationType
import org.eclipse.emf.ecore.util.EcoreUtil

class RemoveAnnotationsModification implements IModification {
	
	override modify(Collection<Package> p) {
		p.forEach[
			it.eAllContents.filter(AnnotatableElement).toList.forEach[ ae |
				ae.annotationInfo?.annotations?.clear
			]
			it.eAllContents.filter(AnnotationType).toList.forEach[ at |
				EcoreUtil.remove(at)
			]
		]
		p
	}
	
}