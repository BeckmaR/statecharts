/**
* Copyright (c) 2019 itemis AG - All rights Reserved
* Unauthorized copying of this file, via any medium is strictly prohibited
*
* Contributors:
* 	matthias wienand - itemis AG
*
*/
package org.yakindu.base.gmf.runtime.router;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.ConnectionRouter;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.RelativeBendpoint;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.PrecisionPoint;
import org.eclipse.draw2d.geometry.Rectangle;
import org.yakindu.base.gmf.runtime.router.GeometryUtil.Line;

public class RubberBandRoutingSupport {

	protected RelativeBendpointUtil relbpUtil = new RelativeBendpointUtil();
	protected GeometryUtil geom = new GeometryUtil();
	protected Map<Connection, ConnData> conn = new HashMap<>();
	private Rectangle origDraggedBoundsAbs;
	private MaxMoveDelta[] mmds = new MaxMoveDelta[4];
	private double[] prevDeltas = null;

//	public void _initBoxDrag(IGraphicalEditPart host, ChangeBoundsRequest request) {
//		List<Connection> sourceConnections = getSourceConnections(host, request);
//		List<Connection> targetConnections = getTargetConnections(host, request);
//		getBoundsAbs(host, request);
//		dd();
//	}

	public void abortBoxDrag() {
		// force locations only
		for (ConnData cd : conn.values()) {
			forceInitialLocations(cd);
		}
		clearState();
	}

	private double clamp(boolean isVertical, PrecisionPoint p, double dv, Rectangle box, double sideDistance) {
		if (isVertical) {
			double c = p.preciseX() + dv;
			double x = box.preciseX();
			double left = x + sideDistance;
			if (c < left) {
				return left;
			}
			double right = (x + box.preciseWidth()) - sideDistance;
			if (c > right) {
				return right;
			}
			return c;
		} else {
			double c = p.preciseY() + dv;
			double y = box.preciseY();
			double top = y + sideDistance;
			if (c < top) {
				return top;
			}
			double bot = (y + box.preciseHeight()) - sideDistance;
			if (c > bot) {
				return bot;
			}
			return c;
		}
	}

	protected void clearState() {
		conn.clear();
	}

	public void commitBoxDrag() {
		clearState();
	}

	protected List<RelativeBendpoint> createConstraint(Connection conn, List<PrecisionPoint> list) {
		List<RelativeBendpoint> constraint = new ArrayList<>();
		for (Point p : list) {
			RelativeBendpoint relbp = new RelativeBendpoint();
			relbp.setConnection(conn);
			relbpUtil.forceLocation(conn, relbp, p.preciseX(), p.preciseY());
			constraint.add(relbp);
		}
		return constraint;
	}

	/**
	 * Find first intersection with target box, walking from start to end.
	 *
	 * @param points
	 * @param rect
	 * @param isSelfAssoc
	 */
	protected void cutOffEnd(List<PrecisionPoint> points, Rectangle rect, boolean isSelfAssoc) {
		// determine top, right, bottom, and left sides
		final List<Line> segs = geom.getOutlineSegments(geom.toRectangle(rect));

		// need four points for self-associations
		int startIndex = isSelfAssoc ? 3 : 1;
		if (startIndex >= points.size()) {
			return;
		}

		// walk from start to end
		PrecisionPoint p1 = geom.toPP(points.get(startIndex - 1));
		for (int i = startIndex; i < points.size(); i++) {
			final PrecisionPoint p2 = geom.toPP(points.get(i));
			final Line line = new Line(p1, p2);
			final PrecisionPoint poi = geom.findNearestIntersection(line, segs, p1);
			if (poi != null) {
				for (int j = points.size() - 1; j >= i; j--) {
					points.remove(j);
				}
				if (p1.preciseX() == p2.preciseX()) {
					poi.setPreciseX(p1.preciseX());
				} else {
					poi.setPreciseY(p1.preciseY());
				}
				points.add(poi);
				return;
			}
			p1 = p2;
		}
	}

	/**
	 * Find first intersection with source box, walking from end to start.
	 *
	 * @param points
	 * @param rect
	 * @param isSelfAssoc
	 */
	protected void cutOffStart(List<PrecisionPoint> points, Rectangle rect, boolean isSelfAssoc) {
		// determine top, right, bottom, and left sides
		final List<Line> segs = geom.getOutlineSegments(geom.toRectangle(rect));

		// need at least four points for self-assocs
		int startIndex = isSelfAssoc ? points.size() - 4 : points.size() - 2;
		if ((startIndex < 0) || ((startIndex + 1) >= points.size())) {
			return;
		}

		// walk from end to start
		PrecisionPoint p2 = geom.toPP(points.get(startIndex + 1));
		for (int i = startIndex; i >= 0; i--) {
			final PrecisionPoint p1 = geom.toPP(points.get(i));
			final Line line = new Line(p1, p2);
			final PrecisionPoint poi = geom.findNearestIntersection(line, segs, p2);
			if (poi != null) {
				for (int j = i; j >= 0; j--) {
					points.remove(j);
				}
				if (p1.preciseX() == p2.preciseX()) {
					poi.setPreciseX(p1.preciseX());
				} else {
					poi.setPreciseY(p1.preciseY());
				}
				points.add(0, poi);
				return;
			}
			p2 = p1;
		}
	}

	protected void cutOutLoops(List<PrecisionPoint> points) {
		// loops are only possible with at least five points
		final int minLoopSize = 5;
		if (points.size() < minLoopSize) {
			return;
		}

		// search from start so that removals can happen without having to adjust
		// indices
		for (int i = points.size() - 1; i >= (minLoopSize - 1); i--) {
			final Line rearLine = new Line(geom.toPP(points.get(i - 1)), geom.toPP(points.get(i)));
			for (int j = 0; j <= ((i - minLoopSize) + 1); j++) {
				final Line frontLine = new Line(geom.toPP(points.get(j)), geom.toPP(points.get(j + 1)));
				final PrecisionPoint poi = geom.getIntersection(rearLine, frontLine);
//				System.out.println("intersect " + rearLine + " with " + frontLine + " yields " + poi);
				if (poi != null) {
					// replace all points from j + 1 ... i - 1 with the point of intersection
					for (int k = i - 1; k >= (j + 1); k--) {
						points.remove(k);
					}
					points.add(j + 1, poi);
					// XXX: due to continuous loop-removal, there should always be at most one loop
					return;
				}
			}
		}
	}

//	private void dd() {
//		System.out.println("[RB] conns=" + conn.size());
//		System.out.println("[RB] ibox=");
//		Set<Connection> keys = conn.keySet();
//
//		int i = 1;
//
//		for (Connection c : keys) {
//			String pre = String.format("[%2d] ", i);
//			ConnData d = conn.get(c);
//			String srcSide = d.isSourceVertical ? "|" : "/";
//			String tgtSide = d.isTargetVertical ? "|" : "/";
//			String ctype = d.isReflexive ? "<->" : d.isSource ? "<--" : "-->";
//			String cshort = srcSide + ctype + tgtSide;
//			String initialPts = "";
//			for (Point p : d.initialVisualPoints) {
//				initialPts += String.format("(%3d,%3d) ", p.x, p.y);
//			}
//			String currentPts = "";
//			for (Point p : d.getVisualPoints()) {
//				currentPts += "(" + p.x + "," + p.y + "), ";
//			}
//			String spaces = String.format("%" + cshort.length() + "s", "");
//			System.out.println(pre + cshort + " ips: " + initialPts);
//			System.out.println(pre + spaces + " cps: " + currentPts);
//			IFigure srcOwner = d.conn.getSourceAnchor().getOwner();
//			IFigure tgtOwner = d.conn.getTargetAnchor().getOwner();
//			Rectangle srcBounds = getBounds(d.conn, srcOwner);
//			Rectangle tgtBounds = getBounds(d.conn, tgtOwner);
//			System.out.println(pre + spaces + " srcbox=" + srcBounds);
//			System.out.println(pre + spaces + " tgtbox=" + tgtBounds);
//
//			i++;
//		}
//	}

	private String debugBox(Rectangle r) {
		return "new Rectangle(" + r.x + ", " + r.y + ", " + r.width + ", " + r.height + ")";
	}

	private void debugConnections(String var, List<Connection> sourceConnections) {
		for (Connection c : sourceConnections) {
			double[] tx = determineTransform(c);
			Rectangle srcBox = getBounds(c, c.getSourceAnchor().getOwner());
			Rectangle tgtBox = getBounds(c, c.getTargetAnchor().getOwner());
			System.out.println("    {");
			System.out.println("        List<Point> points = new ArrayList<>();");
			PointList points = c.getPoints();
			for (int i = 0; i < points.size(); i++) {
				Point p = points.getPoint(i);
				System.out.println("        points.add(new Point(" + p.x + "," + p.y + ")" + ");");
			}
			String tstr = (tx[0] == 0) && (tx[1] == 0) && (tx[2] == 1) ? ""
					: "s=" + tx[2] + ",t=(" + tx[0] + "," + tx[1] + "), ";
			System.out.println("        " + var + ".add(new ConnMock(" + tstr + debugBox(srcBox) + ", "
					+ debugBox(tgtBox) + ", points));");
			System.out.println("    }");
		}
	}

	private void debugInit(Rectangle originalAbs, List<Connection> sourceConnections,
			List<Connection> targetConnections) {
		System.out.println("{");
		System.out.println("    Rectangle boundsAbs = " + debugBox(originalAbs) + ";");
		System.out.println("    List<Connection> srcConns = new ArrayList<>();");
		debugConnections("srcConns", sourceConnections);
		System.out.println("    List<Connection> tgtConns = new ArrayList<>();");
		debugConnections("tgtConns", targetConnections);
		System.out.println("    rb.initBoxDrag(boundsAbs, srcConns, tgtConns);");
		System.out.println("}");
	}

	private void debugUpdate() {
		List<Connection> srcConns = new ArrayList<>();
		List<Connection> tgtConns = new ArrayList<>();
		for (ConnData cd : conn.values()) {
			if (cd.isReflexive) {
				srcConns.add(cd.conn);
				tgtConns.add(cd.conn);
			} else if (cd.isSource) {
				srcConns.add(cd.conn);
			} else {
				tgtConns.add(cd.conn);
			}
		}

		System.out.println("    List<Connection> srcConns = new ArrayList<>();");
		debugConnections("srcConns", srcConns);
		System.out.println("    List<Connection> tgtConns = new ArrayList<>();");
		debugConnections("tgtConns", tgtConns);
//		dd();
	}

	private double[] determineTransform(Connection c) {
		PrecisionPoint origin = new PrecisionPoint(0, 0);
		PrecisionPoint oneZero = new PrecisionPoint(1, 0);
		c.translateToRelative(origin);
		c.translateToRelative(oneZero);
		return new double[] { origin.preciseX(), origin.preciseY(), oneZero.preciseX() };
	}

	private List<PrecisionPoint> dragAnchoredSegments(ConnData cd, Rectangle sourceBox, Rectangle targetBox,
			double localDx, double localDy, double localDw, double localDh) {
		List<PrecisionPoint> pointsCopy = cd.getInitialVisualPointsCopy();
		if (cd.isReflexive) {
			double dx = localDx + localDw;
			double dy = localDy + localDh;
			if (cd.isSourceVertical) {
				double x = clamp(true, pointsCopy.get(cd.sourceAnchorIndex), dx, targetBox, cd.SideDistance);
				PrecisionPoint ap = pointsCopy.get(cd.sourceAnchorIndex);
				ap.setPreciseX(x);
				ap.setPreciseY(ap.preciseY() + dy);
				PrecisionPoint np = pointsCopy.get(cd.sourceNeighborIndex);
				np.setPreciseX(x);
				np.setPreciseY(np.preciseY() + dy);
			} else {
				double y = clamp(false, pointsCopy.get(cd.sourceAnchorIndex), dy, targetBox, cd.SideDistance);
				PrecisionPoint ap = pointsCopy.get(cd.sourceAnchorIndex);
				ap.setPreciseY(y);
				ap.setPreciseX(ap.preciseX() + dx);
				PrecisionPoint np = pointsCopy.get(cd.sourceNeighborIndex);
				np.setPreciseY(y);
				np.setPreciseX(np.preciseX() + dx);
			}
			if (cd.isTargetVertical) {
				double x = clamp(true, pointsCopy.get(cd.targetAnchorIndex), dx, targetBox, cd.SideDistance);
				PrecisionPoint ap = pointsCopy.get(cd.targetAnchorIndex);
				ap.setPreciseX(x);
				ap.setPreciseY(ap.preciseY() + dy);
				PrecisionPoint np = pointsCopy.get(cd.targetNeighborIndex);
				np.setPreciseX(x);
				np.setPreciseY(np.preciseY() + dy);
			} else {
				double y = clamp(false, pointsCopy.get(cd.targetAnchorIndex), dy, targetBox, cd.SideDistance);
				PrecisionPoint ap = pointsCopy.get(cd.targetAnchorIndex);
				ap.setPreciseY(y);
				ap.setPreciseX(ap.preciseX() + dx);
				PrecisionPoint np = pointsCopy.get(cd.targetNeighborIndex);
				np.setPreciseY(y);
				np.setPreciseX(np.preciseX() + dx);
			}
			if (pointsCopy.size() > 4) {
				for (PrecisionPoint p : pointsCopy.subList(2, pointsCopy.size() - 2)) {
					p.setPreciseLocation(geom.getAdded(p, dx, dy));
				}
			}
		} else {
			if (cd.isSource) {
				if (cd.isSourceVertical) {
					double reqDx = mmds[cd.sourceSideIndex].reqDelta(localDx, localDw);
					if (reqDx != 0) {
						PrecisionPoint ap = pointsCopy.get(cd.sourceAnchorIndex);
						ap.setPreciseX(ap.preciseX() + reqDx);
						PrecisionPoint np = pointsCopy.get(cd.sourceNeighborIndex);
						np.setPreciseX(np.preciseX() + reqDx);
					}
				} else {
					double reqDy = mmds[cd.sourceSideIndex].reqDelta(localDy, localDh);
					if (reqDy != 0) {
						PrecisionPoint ap = pointsCopy.get(cd.sourceAnchorIndex);
						ap.setPreciseY(ap.preciseY() + reqDy);
						PrecisionPoint np = pointsCopy.get(cd.sourceNeighborIndex);
						np.setPreciseY(np.preciseY() + reqDy);
					}
				}
				if (cd.isSourceVertical) {
					double x = clamp(true, pointsCopy.get(cd.sourceAnchorIndex), 0, sourceBox, cd.SideDistance);
					PrecisionPoint ap = pointsCopy.get(cd.sourceAnchorIndex);
					ap.setPreciseX(x);
					PrecisionPoint np = pointsCopy.get(cd.sourceNeighborIndex);
					np.setPreciseX(x);
				} else {
					double y = clamp(false, pointsCopy.get(cd.sourceAnchorIndex), 0, sourceBox, cd.SideDistance);
					PrecisionPoint ap = pointsCopy.get(cd.sourceAnchorIndex);
					ap.setPreciseY(y);
					PrecisionPoint np = pointsCopy.get(cd.sourceNeighborIndex);
					np.setPreciseY(y);
				}
			}
			if (cd.isTarget) {
				if (cd.isTargetVertical) {
					double reqDx = mmds[cd.targetSideIndex].reqDelta(localDx, localDw);
					if (reqDx != 0) {
						PrecisionPoint ap = pointsCopy.get(cd.targetAnchorIndex);
						ap.setPreciseX(ap.preciseX() + reqDx);
						PrecisionPoint np = pointsCopy.get(cd.targetNeighborIndex);
						np.setPreciseX(np.preciseX() + reqDx);
					}
				} else {
					double reqDy = mmds[cd.targetSideIndex].reqDelta(localDy, localDh);
					if (reqDy != 0) {
						PrecisionPoint ap = pointsCopy.get(cd.targetAnchorIndex);
						ap.setPreciseY(ap.preciseY() + reqDy);
						PrecisionPoint np = pointsCopy.get(cd.targetNeighborIndex);
						np.setPreciseY(np.preciseY() + reqDy);
					}
				}
				if (cd.isTargetVertical) {
					double x = clamp(true, pointsCopy.get(cd.targetAnchorIndex), 0, targetBox, cd.SideDistance);
					PrecisionPoint ap = pointsCopy.get(cd.targetAnchorIndex);
					ap.setPreciseX(x);
					PrecisionPoint np = pointsCopy.get(cd.targetNeighborIndex);
					np.setPreciseX(x);
				} else {
					double y = clamp(false, pointsCopy.get(cd.targetAnchorIndex), 0, targetBox, cd.SideDistance);
					PrecisionPoint ap = pointsCopy.get(cd.targetAnchorIndex);
					ap.setPreciseY(y);
					PrecisionPoint np = pointsCopy.get(cd.targetNeighborIndex);
					np.setPreciseY(y);
				}
			}
		}
		return pointsCopy;
	}

//	private List<ConnectionEditPart> filter(List<ConnectionEditPart> allConnectionParts, ChangeBoundsRequest request) {
//		if (request.getEditParts() == null) {
//			return allConnectionParts;
//		}
//		List<ConnectionEditPart> result = new ArrayList<>();
//		for (ConnectionEditPart input : allConnectionParts) {
//			if (!(request.getEditParts().contains(input.getTarget())
//					&& request.getEditParts().contains(input.getSource()))) {
//				result.add(input);
//			}
//		}
//		return result;
//	}

	protected void forceInitialLocations(ConnData cd) {
		List<RelativeBendpoint> constraint = createConstraint(cd.conn, cd.initialVisualPoints);
		ConnectionRouter router = cd.conn.getConnectionRouter();
		router.setConstraint(cd.conn, constraint);
		router.route(cd.conn);
	}

	private Rectangle getBounds(Connection conn, IFigure sourceOwner) {
		Rectangle sourceBox = sourceOwner.getBounds().getCopy();
		sourceOwner.translateToAbsolute(sourceBox);
		conn.translateToRelative(sourceBox);
		return sourceBox;
	}

//	private void getBoundsAbs(IGraphicalEditPart host, ChangeBoundsRequest request) {
//		IFigure figure = host.getFigure();
//		Rectangle boundsAbs = figure.getBounds().getCopy();
//		figure.translateToAbsolute(boundsAbs);
//		boundsAbs.translate(request.getMoveDelta().getNegated()).resize(request.getSizeDelta().getNegated());
//	}

	public ConnData getCD(IFigure figure) {
		return conn.get(figure);
	}

//	private List<Connection> getSourceConnections(IGraphicalEditPart host, ChangeBoundsRequest request) {
//		List<Connection> result = new ArrayList<>();
//		@SuppressWarnings("unchecked")
//		List<IGraphicalEditPart> sourceConnections = filter(host.getSourceConnections(), request);
//		for (IGraphicalEditPart iGraphicalEditPart : sourceConnections) {
//			Connection connection = (Connection) iGraphicalEditPart.getFigure();
//			result.add(connection);
//		}
//		return result;
//	}
//
//	private List<Connection> getTargetConnections(IGraphicalEditPart host, ChangeBoundsRequest request) {
//		List<Connection> result = new ArrayList<>();
//		@SuppressWarnings("unchecked")
//		List<IGraphicalEditPart> targetConnections = filter(host.getTargetConnections(), request);
//		for (IGraphicalEditPart iGraphicalEditPart : targetConnections) {
//			Connection connection = (Connection) iGraphicalEditPart.getFigure();
//			result.add(connection);
//		}
//		return result;
//	}

	public List<Connection> getSourceConnections() {
		List<Connection> srcConns = new ArrayList<>();
		List<Connection> tgtConns = new ArrayList<>();
		for (ConnData cd : conn.values()) {
			if (cd.isReflexive) {
				srcConns.add(cd.conn);
				tgtConns.add(cd.conn);
			} else if (cd.isSource) {
				srcConns.add(cd.conn);
			} else {
				tgtConns.add(cd.conn);
			}
		}
		return srcConns;
	}

	public List<Connection> getTargetConnections() {
		List<Connection> srcConns = new ArrayList<>();
		List<Connection> tgtConns = new ArrayList<>();
		for (ConnData cd : conn.values()) {
			if (cd.isReflexive) {
				srcConns.add(cd.conn);
				tgtConns.add(cd.conn);
			} else if (cd.isSource) {
				srcConns.add(cd.conn);
			} else {
				tgtConns.add(cd.conn);
			}
		}
		return tgtConns;
	}

	public void initBoxDrag(Rectangle originalAbs, List<Connection> sourceConnections,
			List<Connection> targetConnections) {
		debugInit(originalAbs, sourceConnections, targetConnections);

		// save original bounds in asbolute coordinates
		origDraggedBoundsAbs = originalAbs.getCopy();

		// clear connection data (FIXME: do this at the end of interaction, not at the
		// start)
		conn.clear();
		prevDeltas = null;

		for (int i = 0; i < mmds.length; i++) {
			mmds[i] = new MaxMoveDelta((i % 2) != 0);
		}

		Map<Connection, Boolean> isSource = new IdentityHashMap<>();
		for (Connection c : sourceConnections) {
			isSource.put(c, true);
		}
		Map<Connection, Boolean> isTarget = new IdentityHashMap<>();
		for (Connection c : targetConnections) {
			isTarget.put(c, true);
		}
		List<Connection> pureSource = new ArrayList<>();
		List<Connection> reflexive = new ArrayList<>();
		for (Connection c : sourceConnections) {
			if (!isTarget.containsKey(c)) {
				pureSource.add(c);
			} else {
				reflexive.add(c);
			}
		}
		List<Connection> pureTarget = new ArrayList<>();
		for (Connection c : targetConnections) {
			if (!isSource.containsKey(c)) {
				pureTarget.add(c);
			}
		}
		initDrag(originalAbs, pureSource, true, false);
		initDrag(originalAbs, pureTarget, false, true);
		initDrag(originalAbs, reflexive, true, true);
	}

	private void initDrag(Rectangle originalAbs, List<Connection> connections, boolean isSource, boolean isTarget) {
		for (Connection connection : connections) {
			// disable repaint so that first drag does not let connection jump
//			((TransitionFigure) connection).disableRepaint();

			// save connection points
			ConnData cd = new ConnData(connection, isSource, isTarget);
			conn.put(connection, cd);

			// compute max move deltas
			MaxMoveDelta[] mmds = cd.getMaxMoveDeltas(originalAbs);

			// merge with current mmds
			for (int i = 0; i < this.mmds.length; i++) {
				this.mmds[i].merge(mmds[i]);
			}
		}
	}

	public void updateBoxDrag(Rectangle newBoundsAbs) {
		double dx = newBoundsAbs.preciseX() - origDraggedBoundsAbs.preciseX();
		double dy = newBoundsAbs.preciseY() - origDraggedBoundsAbs.preciseY();
		double dw = newBoundsAbs.preciseWidth() - origDraggedBoundsAbs.preciseWidth();
		double dh = newBoundsAbs.preciseHeight() - origDraggedBoundsAbs.preciseHeight();

		if (prevDeltas != null) {
			if ((dx == prevDeltas[0]) && (dy == prevDeltas[1]) && (dw == prevDeltas[2]) && (dh == prevDeltas[3])) {
				// we did this already...
				return;
			}
		}
		prevDeltas = new double[] { dx, dy, dw, dh };

		if ((dx == 0) && (dy == 0) && (dw == 0) && (dh == 0)) {
			// force locations only
			for (ConnData cd : conn.values()) {
				forceInitialLocations(cd);
			}
			return;
		} else {
			System.out.println("rb.updateBoxDrag(" + debugBox(newBoundsAbs) + ");");
		}

		for (ConnData cd : conn.values()) {
			System.out.println("CONN");
			cd.printVisualPoints();

			// compute deltas in local coordinate system
			Rectangle brel = origDraggedBoundsAbs.getCopy();
			cd.conn.translateToRelative(brel);
			Rectangle bnrel = newBoundsAbs.getCopy();
			cd.conn.translateToRelative(bnrel);
			double localDx = bnrel.preciseX() - brel.preciseX();
			double localDy = bnrel.preciseY() - brel.preciseY();
			double localDw = bnrel.preciseWidth() - brel.preciseWidth();
			double localDh = bnrel.preciseHeight() - brel.preciseHeight();
			System.out.println("local deltas = " + localDx + ", " + localDy + ", " + localDw + ", " + localDh);

			// drag anchored segment if necessary
			IFigure targetOwner = cd.conn.getTargetAnchor().getOwner();
			Rectangle targetBox = getBounds(cd.conn, targetOwner);
			IFigure sourceOwner = cd.conn.getSourceAnchor().getOwner();
			boolean isSelfAssoc = targetOwner == sourceOwner;
			Rectangle sourceBox = isSelfAssoc ? targetBox : getBounds(cd.conn, sourceOwner);
			System.out.println("sourceBox = " + debugBox(sourceBox) + ", owner = " + sourceOwner);
			System.out.println("targetBox = " + debugBox(targetBox) + ", owner = " + targetOwner);
			List<PrecisionPoint> pointsCopy = dragAnchoredSegments(cd, sourceBox, targetBox, localDx, localDy, localDw,
					localDh);

//			cd.printVisualPoints();
//			System.out.println("dragged:");
//			cd.printPoints(pointsCopy);

			List<RelativeBendpoint> constraint = createConstraint(cd.conn, pointsCopy);
			ConnectionRouter router = cd.conn.getConnectionRouter();

			// disable repaint to prevent flickering when restoring original constraint
			// (hard to reproduce but happens)
//			((TransitionFigure) cd.conn).disableRepaint();
			router.setConstraint(cd.conn, constraint);
			router.route(cd.conn);

			System.out.println("after route:");
			cd.printVisualPoints();

			// cut source, target, and loops
			List<PrecisionPoint> list = cd.getVisualPoints();

			if (!targetBox.contains(sourceBox)) {
				cutOffEnd(list, targetBox, isSelfAssoc);
			}
			if (!sourceBox.contains(targetBox)) {
				cutOffStart(list, sourceBox, isSelfAssoc);
			}

			cutOutLoops(list);

			List<RelativeBendpoint> updatedConstraint = createConstraint(cd.conn, list);

//			((TransitionFigure) cd.conn).enableRepaint();
			router.setConstraint(cd.conn, updatedConstraint);
			cd.printBendpointLocations();
			router.route(cd.conn);

//			System.out.println("after all");
//			cd.printVisualPoints();
//			System.out.println();
		}

		debugUpdate();
	}
}
