package org.yakindu.base.gmf.runtime.editparts;

import java.util.function.Supplier;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PrecisionRectangle;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.tools.ResizeTracker;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.requests.RequestConstants;
import org.yakindu.base.gmf.runtime.editpolicies.SetPreferredSizeRequest;

public class ChangeBoundsAspect {

	public static class LiveFeedbackResizeTracker extends org.yakindu.base.gmf.runtime.tracker.ResizeTracker {

		Supplier<Rectangle> originalBoundsProvider;
		Supplier<IFigure> hostFigureProvider;

		public LiveFeedbackResizeTracker(GraphicalEditPart owner, int direction, Supplier<Rectangle> originalBounds,
				Supplier<IFigure> hostFigure) {
			super(owner, direction);
			originalBoundsProvider = originalBounds;
			hostFigureProvider = hostFigure;
		}

		@Override
		protected void enforceConstraintsForResize(ChangeBoundsRequest request) {
			super.enforceConstraintsForResize(request);
			final IFigure figure = hostFigureProvider.get();
			Dimension prefSize = figure.getPreferredSize().getCopy();
			figure.translateToAbsolute(prefSize);
			Rectangle bounds = getOriginalBounds();
			bounds = request.getTransformedRectangle(bounds);
			if (bounds.width < prefSize.width) {
				request.getSizeDelta().width = request.getSizeDelta().width + (prefSize.width - bounds.width);
			}
			if (bounds.height < prefSize.height) {
				request.getSizeDelta().height = request.getSizeDelta().height + (prefSize.height - bounds.height);
			}
			request.setSizeDelta(request.getSizeDelta());
		}

		@Override
		protected Rectangle getOriginalBounds() {
			return originalBoundsProvider.get();
		}
	}

	private boolean connectionStart = true;
	private PrecisionRectangle originalBounds = null;
	private final ChangeBoundsRequest NULL_REQUEST = new ChangeBoundsRequest(RequestConstants.REQ_MOVE_CHILDREN);
	private String lastRequest = "";
	private IGraphicalEditPart host;
	private ILiveFeedbackPolicy policy;

	public ChangeBoundsAspect(EditPolicy policy) {
		this.policy = (ILiveFeedbackPolicy) policy;
		host = (IGraphicalEditPart) policy.getHost();
	}

	protected void enforceConstraintForMove(ChangeBoundsRequest request) {
		Rectangle relativeBounds = getOriginalBounds();
		PrecisionRectangle manipulatedConstraint = new PrecisionRectangle(
				request.getTransformedRectangle(relativeBounds));
		getHostFigure().translateToRelative(manipulatedConstraint);

		manipulatedConstraint.setX(Math.max(0, manipulatedConstraint.x));
		manipulatedConstraint.setY(Math.max(0, manipulatedConstraint.y));

		getHostFigure().translateToAbsolute(manipulatedConstraint);

		Dimension difference = manipulatedConstraint.getLocation().getDifference(originalBounds.getLocation());
		request.setMoveDelta(new Point(difference.width, difference.height));
	}

	public void eraseChangeBoundsFeedback(ChangeBoundsRequest request) {
		connectionStart = true;
		policy.originalEraseChangeBoundsFeedback(request);
	}

	public IGraphicalEditPart getHost() {
		return host;
	}

	public IFigure getHostFigure() {
		return host.getFigure();
	}

	public Command getMoveCommand(ChangeBoundsRequest request) {
		if (RequestConstants.REQ_DROP.equals(request.getType())) {
			return null;
		}
		NULL_REQUEST.setEditParts(host);
		return host.getParent().getCommand(NULL_REQUEST);
	}

	public Rectangle getOriginalBounds() {
		if (originalBounds == null) {
			updateOriginalBounds();
		}
		return originalBounds.getCopy();
	}

	public Command getResizeCommand(ChangeBoundsRequest request) {
		if (RequestConstants.REQ_DROP.equals(request.getType())) {
			return null;
		}

		if (request instanceof SetPreferredSizeRequest) {
			SetPreferredSizeRequest req = new SetPreferredSizeRequest(
					org.eclipse.gef.RequestConstants.REQ_RESIZE_CHILDREN);
			req.setEditParts(host);
			req.setCenteredResize(request.isCenteredResize());
			req.setConstrainedMove(request.isConstrainedMove());
			req.setConstrainedResize(request.isConstrainedResize());
			req.setSnapToEnabled(request.isSnapToEnabled());
			req.setMoveDelta(request.getMoveDelta());
			req.setSizeDelta(request.getSizeDelta());
			req.setLocation(request.getLocation());
			req.setExtendedData(request.getExtendedData());
			req.setResizeDirection(request.getResizeDirection());
			return host.getParent().getCommand(req);
		}
		NULL_REQUEST.setEditParts(host);
		return host.getParent().getCommand(NULL_REQUEST);
	}

	public ResizeTracker getResizeTracker(int direction) {
		LiveFeedbackResizeTracker liveFeedbackResizeTracker = new LiveFeedbackResizeTracker(host, direction,
				this::getOriginalBounds, this::getHostFigure);
		// TODO: why set bounds when they are queried via getOriginalBounds() anyway?
		liveFeedbackResizeTracker.setOriginalBounds(getOriginalBounds());
		return liveFeedbackResizeTracker;
	}

	public void showChangeBoundsFeedback(ChangeBoundsRequest request) {
		System.out.println("LIVE FEEDBACK " + request.getMoveDelta());
		Rectangle hostBoundsAbs = getHostFigure().getBounds().getCopy();
		getHostFigure().translateToAbsolute(hostBoundsAbs);
		System.out.println("host figure = " + getHostFigure() + ", bounds = " + hostBoundsAbs);
		// If REQ_DROP is delivered 2 times in a row it is a "real" drop and not only a
		// hover over existing elements in the same region
		if (RequestConstants.REQ_DROP.equals(request.getType()) && RequestConstants.REQ_DROP.equals(lastRequest)) {
			Rectangle rect = getOriginalBounds();
			getHostFigure().getParent().translateToRelative(rect);
			getHostFigure().setBounds(rect);
			policy.originalShowChangeBoundsFeedback(request);
			lastRequest = (String) request.getType();
			return;
		}
		policy.originalEraseChangeBoundsFeedback(request);
		enforceConstraintForMove(request);
		if (connectionStart) {
			updateOriginalBounds();
			connectionStart = false;
		}
		Rectangle rect = request.getTransformedRectangle(getOriginalBounds());
		getHostFigure().getParent().translateToRelative(rect);
		getHostFigure().setBounds(rect);
		hostBoundsAbs = getHostFigure().getBounds().getCopy();
		getHostFigure().translateToAbsolute(hostBoundsAbs);
		System.out.println("host figure = " + getHostFigure() + ", bounds = " + hostBoundsAbs);
		getHostFigure().getParent().setConstraint(getHostFigure(), rect);
		lastRequest = (String) request.getType();
	}

	private void updateOriginalBounds() {
		originalBounds = new PrecisionRectangle(getHostFigure().getBounds().getCopy());
		getHostFigure().translateToAbsolute(originalBounds);
	}
}
