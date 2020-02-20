package org.yakindu.base.gmf.runtime.editparts;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.requests.ChangeBoundsRequest;

public class ChangeBoundsAspectNonResizable extends ChangeBoundsAspect {

	public ChangeBoundsAspectNonResizable(EditPolicy policy) {
		super(policy);
	}

	@Override
	protected void enforceConstraintForMove(ChangeBoundsRequest request) {
		Rectangle relativeBounds = getOriginalBounds();
		Rectangle transformed = request.getTransformedRectangle(relativeBounds);
		getHostFigure().getParent().translateToRelative(transformed);
		if (transformed.x < 0) {
			Point moveDelta = request.getMoveDelta();
			moveDelta.x -= transformed.x;
		}
		if (transformed.y < 0) {
			Point moveDelta = request.getMoveDelta();
			moveDelta.y -= transformed.y;
		}
	}
}
