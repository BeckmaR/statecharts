/**
 * Copyright (c) 2015 committers of YAKINDU and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * Contributors:
 * 	committers of YAKINDU - initial API and implementation
 *
 */
package org.yakindu.base.gmf.runtime.editparts;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.NonResizableEditPolicyEx;
import org.eclipse.gmf.runtime.diagram.ui.requests.RequestConstants;

/**
 *
 * @author andreas muelder - Initial contribution and API
 *
 */
public class LiveFeedbackNonResizableEditPolicy extends NonResizableEditPolicyEx implements ILiveFeedbackPolicy {

	private ChangeBoundsAspectNonResizable changeBoundsAspect;

	@Override
	protected void eraseChangeBoundsFeedback(ChangeBoundsRequest request) {
		getChangeBoundsAspect().eraseChangeBoundsFeedback(request);
	}

	protected ChangeBoundsAspect getChangeBoundsAspect() {
		if (changeBoundsAspect == null) {
			changeBoundsAspect = new ChangeBoundsAspectNonResizable(this);
		}
		return changeBoundsAspect;
	}

	@Override
	protected Command getMoveCommand(ChangeBoundsRequest request) {
		if (RequestConstants.REQ_DROP.equals(request.getType())) {
			return super.getMoveCommand(request);
		}
		return getChangeBoundsAspect().getMoveCommand(request);
	}

	@Override
	public void originalEraseChangeBoundsFeedback(ChangeBoundsRequest request) {
		super.eraseChangeBoundsFeedback(request);
	}

	@Override
	public void originalShowChangeBoundsFeedback(ChangeBoundsRequest request) {
		super.showChangeBoundsFeedback(request);
	}

	@Override
	protected void showChangeBoundsFeedback(ChangeBoundsRequest request) {
		getChangeBoundsAspect().showChangeBoundsFeedback(request);
	}
}
