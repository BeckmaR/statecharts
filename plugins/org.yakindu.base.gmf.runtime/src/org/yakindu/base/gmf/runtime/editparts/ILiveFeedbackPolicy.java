package org.yakindu.base.gmf.runtime.editparts;

import org.eclipse.gef.requests.ChangeBoundsRequest;

public interface ILiveFeedbackPolicy {

	void originalEraseChangeBoundsFeedback(ChangeBoundsRequest request);

	void originalShowChangeBoundsFeedback(ChangeBoundsRequest request);
}
