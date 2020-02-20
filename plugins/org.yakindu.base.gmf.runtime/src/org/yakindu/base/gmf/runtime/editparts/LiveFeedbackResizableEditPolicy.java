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

import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.tools.ResizeTracker;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.ResizableEditPolicyEx;
import org.eclipse.gmf.runtime.diagram.ui.requests.RequestConstants;

/**
 *
 * @author andreas muelder - Initial contribution and API
 *
 */
public class LiveFeedbackResizableEditPolicy extends ResizableEditPolicyEx implements ILiveFeedbackPolicy {

	private ChangeBoundsAspect changeBoundsAspect;
	private FixedBendpointPolicySupport routingAspect;

	protected ChangeBoundsAspect createChangeBoundsAspect() {
		return new ChangeBoundsAspect(this);
	}

	@Override
	protected void eraseChangeBoundsFeedback(ChangeBoundsRequest request) {
		getChangeBoundsAspect().eraseChangeBoundsFeedback(request);
	}

	@Override
	public void eraseSourceFeedback(Request request) {
		if (RequestConstants.REQ_DROP.equals(request.getType())) {
			eraseChangeBoundsFeedback((ChangeBoundsRequest) request);
		} else {
			super.eraseSourceFeedback(request);
		}
		getRoutingAspect().eraseSourceFeedback(request);
	}

	protected ChangeBoundsAspect getChangeBoundsAspect() {
		if (changeBoundsAspect == null) {
			changeBoundsAspect = createChangeBoundsAspect();
		}
		return changeBoundsAspect;
	}

	@Override
	public Command getCommand(Request request) {
		Command command = getRoutingAspect().getCommand(request);
		return command != null ? command : super.getCommand(request);
	}

	@Override
	public IGraphicalEditPart getHost() {
		return (IGraphicalEditPart) super.getHost();
	}

	@Override
	protected Command getMoveCommand(ChangeBoundsRequest request) {
		if (RequestConstants.REQ_DROP.equals(request.getType())) {
			return super.getMoveCommand(request);
		}
		return getChangeBoundsAspect().getMoveCommand(request);
	}

	@Override
	protected Command getResizeCommand(ChangeBoundsRequest request) {
		if (RequestConstants.REQ_DROP.equals(request.getType())) {
			return super.getMoveCommand(request);
		}
		return getChangeBoundsAspect().getResizeCommand(request);
	}

	@Override
	protected ResizeTracker getResizeTracker(int direction) {
		return getChangeBoundsAspect().getResizeTracker(direction);
	}

	protected FixedBendpointPolicySupport getRoutingAspect() {
		if (routingAspect == null) {
			routingAspect = new FixedBendpointPolicySupport(this);
		}
		return routingAspect;
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

	@Override
	public void showSourceFeedback(Request request) {
		if (RequestConstants.REQ_DROP.equals(request.getType())) {
			showChangeBoundsFeedback((ChangeBoundsRequest) request);
		} else {
			super.showSourceFeedback(request);
		}
		getRoutingAspect().showSourceFeedback(request);
	}
}
