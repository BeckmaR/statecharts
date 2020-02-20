package com.yakindu.sct.ui.editor.tests;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.ConnectionRouter;
import org.eclipse.draw2d.RelativeBendpoint;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gmf.runtime.draw2d.ui.internal.routers.RectilinearRouter;
import org.yakindu.base.gmf.runtime.router.RelativeBendpointUtil;

public class ConnMock extends FigureMock implements Connection {

	/**
	 * Map from RoutingInput to connection.setPoints(points) determines how to
	 * route.
	 * <p>
	 * Unregistered RoutingInput is rejected.
	 * <p>
	 * Routing data needs to be assembled from running SCT application.
	 */
	public static class SimulatedRouter {
	}

//	public static class AnchorMock implements ConnectionAnchor {
//
//		private Rectangle bounds;
//
//		public AnchorMock(Rectangle bounds) {
//			this.bounds = bounds;
//		}
//
//		@Override
//		public void addAnchorListener(AnchorListener listener) {
//			throw new UnsupportedOperationException();
//		}
//
//		@Override
//		public Point getLocation(Point reference) {
//			return getReferencePoint();
//		}
//
//		@Override
//		public IFigure getOwner() {
//			return new FigureMock("", bounds);
//		}
//
//		@Override
//		public Point getReferencePoint() {
//			return bounds.getCenter();
//		}
//
//		@Override
//		public void removeAnchorListener(AnchorListener listener) {
//			throw new UnsupportedOperationException();
//		}
//	}

	private Rectangle sourceBox;
	private Rectangle targetBox;
	private List<Point> points;

	public ConnMock(String name, Rectangle sourceBox, Rectangle targetBox, List<Point> points) {
		super(name, null);
		this.sourceBox = sourceBox;
		this.targetBox = targetBox;
		this.points = new ArrayList<>(points);
	}

	@SuppressWarnings("restriction")
	@Override
	public ConnectionRouter getConnectionRouter() {
		return new RectilinearRouter();
	}

	@Override
	public PointList getPoints() {
		PointList pl = new PointList();
		for (Point p : points) {
			pl.addPoint(p.getCopy());
		}
		return pl;
	}

	@Override
	public Object getRoutingConstraint() {
		List<RelativeBendpoint> bps = new ArrayList<>();
		for (Point p : points) {
			RelativeBendpoint bp = new RelativeBendpoint();
			bp.setConnection(this);
			new RelativeBendpointUtil().forceLocation(this, bp, p.x, p.y);
			bps.add(bp);
		}
		return bps;
	}

	@Override
	public ConnectionAnchor getSourceAnchor() {
		return new ChopboxAnchor(new FigureMock(name + ":source", sourceBox));
	}

	@Override
	public ConnectionAnchor getTargetAnchor() {
		return new ChopboxAnchor(new FigureMock(name + ":target", targetBox));
	}

	@Override
	public void setConnectionRouter(ConnectionRouter router) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setPoints(PointList list) {
		points.clear();
		for (int i = 0; i < list.size(); i++) {
			points.add(list.getPoint(i).getCopy());
		}
	}

	@Override
	public void setRoutingConstraint(Object cons) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setSourceAnchor(ConnectionAnchor anchor) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setTargetAnchor(ConnectionAnchor anchor) {
		throw new UnsupportedOperationException();
	}
}
