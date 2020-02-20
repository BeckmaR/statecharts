/**
* Copyright (c) 2019 itemis AG - All rights Reserved
* Unauthorized copying of this file, via any medium is strictly prohibited
*
* Contributors:
* 	andreas muelder - itemis AG
*
*/
package org.yakindu.sct.ui.editor.editparts;

import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.GraphicalEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;

public class FixedBendpointEditPolicy extends GraphicalEditPolicy {

	public static final String ROLE = "Fixed_Bendpoints";

	private FixedBendpointPolicySupport routingAspect;

	@Override
	public void eraseSourceFeedback(Request request) {
		getRoutingAspect().eraseSourceFeedback(request);
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

	protected FixedBendpointPolicySupport getRoutingAspect() {
		if (routingAspect == null) {
			routingAspect = new FixedBendpointPolicySupport(this);
		}
		return routingAspect;
	}

	@Override
	public void showSourceFeedback(Request request) {
		getRoutingAspect().showSourceFeedback(request);
	}
}
