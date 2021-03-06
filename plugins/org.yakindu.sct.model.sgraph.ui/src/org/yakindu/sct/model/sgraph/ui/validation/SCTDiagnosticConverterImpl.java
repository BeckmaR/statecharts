/**
 * Copyright (c) 2012 committers of YAKINDU and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * Contributors:
 * 	committers of YAKINDU - initial API and implementation
 *
 */
package org.yakindu.sct.model.sgraph.ui.validation;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.nodemodel.util.NodeModelUtils;
import org.eclipse.xtext.util.IAcceptor;
import org.eclipse.xtext.validation.DiagnosticConverterImpl;
import org.eclipse.xtext.validation.Issue;
import org.yakindu.sct.model.sgraph.SpecificationElement;

import com.google.inject.Inject;

/**
 *
 * @author andreas muelder - Initial contribution and API
 *
 */
public class SCTDiagnosticConverterImpl extends DiagnosticConverterImpl {

	@Inject
	ISctIssueCreator issueCreator;

	@Override
	public void convertValidatorDiagnostic(final Diagnostic diagnostic, final IAcceptor<Issue> acceptor) {
		super.convertValidatorDiagnostic(diagnostic, new IAcceptor<Issue>() {
			@Override
			public void accept(Issue t) {
				boolean notAccepted = true;
				if (diagnostic.getData().get(0) instanceof EObject) {
					EObject eObject = (EObject) diagnostic.getData().get(0);
					if (eObject != null && eObject.eResource() != null) {
						if (NodeModelUtils.getNode(eObject) != null) {
							eObject = EcoreUtil2.getContainerOfType(eObject, SpecificationElement.class);
						}
						if (eObject != null && eObject.eResource() != null) {
							acceptor.accept(issueCreator.create(t, eObject.eResource().getURIFragment(eObject)));
							notAccepted = false;
						}
					}
				}
				if (notAccepted) {
					acceptor.accept(t);
				}
			}
		});
	}

}
