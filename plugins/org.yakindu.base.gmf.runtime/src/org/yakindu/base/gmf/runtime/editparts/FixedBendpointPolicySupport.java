package org.yakindu.base.gmf.runtime.editparts;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.PrecisionRectangle;
import org.eclipse.draw2d.geometry.Vector;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gmf.runtime.diagram.ui.commands.ICommandProxy;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ConnectionEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.requests.RequestConstants;
import org.eclipse.gmf.runtime.emf.core.util.EObjectAdapter;
import org.yakindu.base.gmf.runtime.commands.SetConnectionBendpointsAndLabelCommmand;
import org.yakindu.base.gmf.runtime.router.ConnData;
import org.yakindu.base.gmf.runtime.router.RelativeBendpointUtil;
import org.yakindu.base.gmf.runtime.router.RubberBandRoutingSupport;
import org.yakindu.base.xtext.utils.gmf.commands.SetLabelsOffsetOperation;
import org.yakindu.base.xtext.utils.gmf.directedit.ExternalXtextLabelEditPart;
import org.yakindu.base.xtext.utils.gmf.routing.EdgeLabelLocator;

public class FixedBendpointPolicySupport {

	private boolean isWaitingForInitialRequest = true;
	private PrecisionRectangle originalBounds = null;
	private RubberBandRoutingSupport router = new RubberBandRoutingSupport();
	private RelativeBendpointUtil relbpUtil = new RelativeBendpointUtil();
	private ConnectionEditPart host;

	public FixedBendpointPolicySupport(EditPolicy policy) {
		host = (ConnectionEditPart) policy.getHost();
	}

	private Command createUpdateAllBendpointsCommand(ChangeBoundsRequest request) {
		CompoundCommand result = new CompoundCommand();
		for (ConnectionEditPart part : getAllConnectionParts(request)) {
			result.add(getBendpointsChangedCommand(part));
		}
		if (result.size() == 0) {
			return null;
		}
		return result;
	}

	private void eraseChangeBoundsFeedback(ChangeBoundsRequest request) {
		isWaitingForInitialRequest = true;
		router.commitBoxDrag();
		for (ConnectionEditPart connectionEditPart : getAllConnectionParts(request)) {
			List<?> children = connectionEditPart.getChildren();
			Connection connection = connectionEditPart.getConnectionFigure();
			for (Object child : children) {
				if (child instanceof ExternalXtextLabelEditPart) {
					IFigure figure = ((ExternalXtextLabelEditPart) child).getFigure();
					Object currentConstraint = connection.getLayoutManager().getConstraint(figure);
					if (currentConstraint instanceof EdgeLabelLocator) {
						EdgeLabelLocator edgeLabelLocator = (EdgeLabelLocator) currentConstraint;
						edgeLabelLocator.eraseFeedbackData();
					}
				}
			}
		}

	}

	public void eraseSourceFeedback(Request request) {
		if (request instanceof ChangeBoundsRequest) {
			eraseChangeBoundsFeedback((ChangeBoundsRequest) request);
		}
	}

	private List<ConnectionEditPart> filter(List<ConnectionEditPart> allConnectionParts, ChangeBoundsRequest request) {
		if (request.getEditParts() == null) {
			return allConnectionParts;
		}
		List<ConnectionEditPart> result = new ArrayList<>();
		for (ConnectionEditPart input : allConnectionParts) {
			if (!(request.getEditParts().contains(input.getTarget())
					&& request.getEditParts().contains(input.getSource()))) {
				result.add(input);
			}
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	private List<ConnectionEditPart> getAllConnectionParts(ChangeBoundsRequest request) {
		List<ConnectionEditPart> conns = new ArrayList<>();
		conns.addAll(host.getSourceConnections());
		conns.addAll(host.getTargetConnections());
		return filter(conns, request);
	}

	@SuppressWarnings("restriction")
	private Command getBendpointsChangedCommand(ConnectionEditPart part) {
		Connection connection = part.getConnectionFigure();
		Point ptRef1 = connection.getSourceAnchor().getReferencePoint();
		connection.translateToRelative(ptRef1);

		Point ptRef2 = connection.getTargetAnchor().getReferencePoint();
		connection.translateToRelative(ptRef2);

		TransactionalEditingDomain editingDomain = host.getEditingDomain();

		SetConnectionBendpointsAndLabelCommmand sbbCommand = new SetConnectionBendpointsAndLabelCommmand(editingDomain);
		sbbCommand.setEdgeAdapter(new EObjectAdapter((EObject) part.getModel()));
		sbbCommand.setNewPointList(connection.getPoints(), ptRef1, ptRef2);
		sbbCommand.setLabelsToUpdate(part, getInitialPoints(connection));

		return new ICommandProxy(sbbCommand);
	}

	public Command getCommand(Request request) {
		if (request instanceof ChangeBoundsRequest) {
			return createUpdateAllBendpointsCommand((ChangeBoundsRequest) request);
		}
		return null;
	}

	private IFigure getHostFigure() {
		return host.getFigure();
	}

	private PointList getInitialPoints(Connection connection) {
		ConnData cd = router.getCD(connection);
		if (cd != null) {
			return relbpUtil.convertToPointList(cd.initialVisualPoints);
		}
		return connection.getPoints();
	}

	private List<Connection> getSourceConnections(ChangeBoundsRequest request) {
		List<Connection> result = new ArrayList<>();
		@SuppressWarnings("unchecked")
		List<IGraphicalEditPart> sourceConnections = filter(host.getSourceConnections(), request);
		for (IGraphicalEditPart iGraphicalEditPart : sourceConnections) {
			Connection connection = (Connection) iGraphicalEditPart.getFigure();
			result.add(connection);
		}
		return result;
	}

	private List<Connection> getTargetConnections(ChangeBoundsRequest request) {
		List<Connection> result = new ArrayList<>();
		@SuppressWarnings("unchecked")
		List<IGraphicalEditPart> targetConnections = filter(host.getTargetConnections(), request);
		for (IGraphicalEditPart iGraphicalEditPart : targetConnections) {
			Connection connection = (Connection) iGraphicalEditPart.getFigure();
			result.add(connection);
		}
		return result;
	}

	private void justKeepConnectionsInPlace(ChangeBoundsRequest request) {
		if (isWaitingForInitialRequest) {
			IFigure figure = getHostFigure();
			originalBounds = new PrecisionRectangle(getHostFigure().getBounds());
			figure.translateToAbsolute(originalBounds);
			originalBounds.translate(request.getMoveDelta().getNegated()).resize(request.getSizeDelta().getNegated());
			router.initBoxDrag(originalBounds, getSourceConnections(request), getTargetConnections(request));
			isWaitingForInitialRequest = false;
		}
		router.restoreInitialLocations();
	}

	private void routeInResponseToBoxDrag(ChangeBoundsRequest request) {
		if (isWaitingForInitialRequest) {
			IFigure figure = getHostFigure();
			originalBounds = new PrecisionRectangle(getHostFigure().getBounds());
			figure.translateToAbsolute(originalBounds);
			originalBounds.translate(request.getMoveDelta().getNegated()).resize(request.getSizeDelta().getNegated());
			router.initBoxDrag(originalBounds, getSourceConnections(request), getTargetConnections(request));
			isWaitingForInitialRequest = false;
		}
		router.updateBoxDrag(request.getTransformedRectangle(originalBounds));
	}

	private void showChangeBoundsFeedback(ChangeBoundsRequest request) {
		System.out.println("BEND POINTS");
		if (request.getEditParts().get(0) != host) {
			// XXX: policy is also called for composites
			justKeepConnectionsInPlace(request);
		} else {
			routeInResponseToBoxDrag(request);
		}
	}

	private void showLineFeedback(ConnectionEditPart connectionEditPart) {
		// XXX: copied from InitialPointsOfRequestDataManager
		List<?> children = connectionEditPart.getChildren();
		Connection connection = connectionEditPart.getConnectionFigure();
		for (Object child : children) {
			if (child instanceof ExternalXtextLabelEditPart) {
				IFigure figure = ((ExternalXtextLabelEditPart) child).getFigure();
				Object currentConstraint = connection.getLayoutManager().getConstraint(figure);
				if (currentConstraint instanceof EdgeLabelLocator) {
					EdgeLabelLocator edgeLabelLocator = (EdgeLabelLocator) currentConstraint;
					edgeLabelLocator.setFeedbackData(getInitialPoints(connection),
							new Vector(edgeLabelLocator.getOffset().x, edgeLabelLocator.getOffset().y),
							SetLabelsOffsetOperation.isEdgeWithObliqueRoutingStyle(connectionEditPart));
				}
			}
		}
	}

	public void showSourceFeedback(Request request) {
		if (RequestConstants.REQ_DROP.equals(request.getType())) {
			return;
		}
		if (request instanceof ChangeBoundsRequest) {
			showChangeBoundsFeedback((ChangeBoundsRequest) request);
			for (ConnectionEditPart cep : getAllConnectionParts((ChangeBoundsRequest) request)) {
				showLineFeedback(cep);
			}
		}
	}
}
