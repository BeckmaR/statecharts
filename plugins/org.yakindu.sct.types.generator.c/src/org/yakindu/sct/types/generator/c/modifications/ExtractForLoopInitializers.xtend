/**
 * Copyright (c) 2019 committers of YAKINDU and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     committers of YAKINDU - initial API and implementation
 */
package org.yakindu.sct.types.generator.c.modifications

import com.google.inject.Inject
import java.util.Collection
import org.eclipse.emf.ecore.util.EcoreUtil
import org.yakindu.base.expressions.expressions.AssignmentExpression
import org.yakindu.base.expressions.expressions.BlockExpression
import org.yakindu.base.expressions.expressions.ForExpression
import org.yakindu.base.expressions.util.ExpressionBuilder
import org.yakindu.base.types.Package
import org.yakindu.base.types.Property
import org.yakindu.sct.types.modification.IModification

class ExtractForLoopInitializers implements IModification {
	@Inject protected extension ExpressionBuilder

	override modify(Collection<Package> p) {
		p.forEach[modify]
		p
	}

	def protected modify(Package p) {
		p.eAllContents.filter(ForExpression).forEach[modify]
	}

	def protected modify(ForExpression it) {
		val block = eContainer
		if (block instanceof BlockExpression) {
			varInits.filter(Property).filter[initialValue !== null].toList.forEach [ prop |
				val newProp = EcoreUtil.copy(prop) => [
					initialValue = null
				]
				addDeclarationToBlock(block, newProp)
				EcoreUtil.replace(prop, prop.createAssignmentFromDeclaration)
			]
		}
	}

	def protected AssignmentExpression createAssignmentFromDeclaration(Property it) {
		_assign(it._ref, EcoreUtil.copy(it.initialValue))
	}

	def protected addDeclarationToBlock(BlockExpression block, Property it) {
		if (block.expressions.exists[exp|EcoreUtil.equals(exp, it)]) {
			return
		}
		block.expressions.add(0, it)
	}

}
