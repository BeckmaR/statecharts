package com.yakindu.sct.ui.editor.tests;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;
import org.junit.Test;
import org.yakindu.base.gmf.runtime.router.RubberBandRoutingSupport;

public class SimpleConnectionStabilityTests {

	RubberBandRoutingSupport rb = new RubberBandRoutingSupport();

	@Test
	public void test_01() {
		{
			Rectangle boundsAbs = new Rectangle(44, 106, 101, 89);
			List<Connection> srcConns = new ArrayList<>();
			{
				List<Point> points = new ArrayList<>();
				points.add(new Point(149, 195));
				points.add(new Point(206, 195));
				srcConns.add(new ConnMock(new Rectangle(44, 106, 101, 89), new Rectangle(206, 168, 69, 53), points));
			}
			List<Connection> tgtConns = new ArrayList<>();
			rb.initBoxDrag(boundsAbs, srcConns, tgtConns);
		}
		rb.updateBoxDrag(new Rectangle(40, 96, 101, 89));
	}

	@Test
	public void test_two_horizontal_simple_move_left_state_upwards() {
		Rectangle boundsAbs = new Rectangle(48, 116, 101, 89);
		List<Connection> initialsrcConns = new ArrayList<>();
		{
			List<Point> points = new ArrayList<>();
			points.add(new Point(74, 205));
			points.add(new Point(74, 244));
			initialsrcConns.add(new ConnMock(new Rectangle(54, 106, 101, 89), new Rectangle(54, 244, 56, 53), points));
		}
		{
			List<Point> points = new ArrayList<>();
			points.add(new Point(149, 195));
			points.add(new Point(206, 195));
			initialsrcConns.add(new ConnMock(new Rectangle(54, 106, 101, 89), new Rectangle(206, 168, 69, 53), points));
		}
		List<Connection> initialtgtConns = new ArrayList<>();
		{
			List<Point> points = new ArrayList<>();
			points.add(new Point(95, 48));
			points.add(new Point(95, 116));
			initialtgtConns.add(new ConnMock(new Rectangle(87, 35, 15, 15), new Rectangle(54, 106, 101, 89), points));
		}
		{
			List<Point> points = new ArrayList<>();
			points.add(new Point(87, 244));
			points.add(new Point(87, 205));
			initialtgtConns.add(new ConnMock(new Rectangle(54, 244, 56, 53), new Rectangle(54, 106, 101, 89), points));
		}
		{
			List<Point> points = new ArrayList<>();
			points.add(new Point(206, 182));
			points.add(new Point(149, 182));
			initialtgtConns.add(new ConnMock(new Rectangle(206, 168, 69, 53), new Rectangle(54, 106, 101, 89), points));
		}
		rb.initBoxDrag(boundsAbs, initialsrcConns, initialtgtConns);

		rb.updateBoxDrag(new Rectangle(54, 106, 101, 89));
		{
			List<Connection> srcConns = new ArrayList<>();
			{
				List<Point> points = new ArrayList<>();
				points.add(new Point(155, 185));
				points.add(new Point(206, 185));
				srcConns.add(new ConnMock(new Rectangle(54, 106, 101, 89), new Rectangle(206, 168, 69, 53), points));
			}
			{
				List<Point> points = new ArrayList<>();
				points.add(new Point(74, 195));
				points.add(new Point(74, 244));
				srcConns.add(new ConnMock(new Rectangle(54, 106, 101, 89), new Rectangle(54, 244, 56, 53), points));
			}
			List<Connection> tgtConns = new ArrayList<>();
			{
				List<Point> points = new ArrayList<>();
				points.add(new Point(87, 244));
				points.add(new Point(87, 195));
				tgtConns.add(new ConnMock(new Rectangle(54, 244, 56, 53), new Rectangle(54, 106, 101, 89), points));
			}
			{
				List<Point> points = new ArrayList<>();
				points.add(new Point(206, 173));
				points.add(new Point(155, 173));
				tgtConns.add(new ConnMock(new Rectangle(206, 168, 69, 53), new Rectangle(54, 106, 101, 89), points));
			}
			{
				List<Point> points = new ArrayList<>();
				points.add(new Point(95, 48));
				points.add(new Point(95, 106));
				tgtConns.add(new ConnMock(new Rectangle(87, 35, 15, 15), new Rectangle(54, 106, 101, 89), points));
			}

			List<Connection> actualSrcConns = initialsrcConns;
			assertEquals(srcConns.size(), actualSrcConns.size());
			for (int i = 0; i < srcConns.size(); i++) {
				Connection exp = srcConns.get(i);
				Connection act = actualSrcConns.get(i);
				PointList expPts = exp.getPoints();
				PointList actPts = act.getPoints();
				assertEquals(expPts.size(), actPts.size());
				for (int j = 0; j < expPts.size(); j++) {
					assertEquals(expPts.getPoint(j), actPts.getPoint(j));
				}
			}
		}

		rb.updateBoxDrag(new Rectangle(48, 106, 101, 89));
//		List<Connection> srcConns = new ArrayList<>();
//		{
//			List<Point> points = new ArrayList<>();
//			points.add(new Point(149, 185));
//			points.add(new Point(206, 185));
//			srcConns.add(new ConnMock(new Rectangle(48, 106, 101, 89), new Rectangle(206, 168, 69, 53), points));
//		}
//		{
//			List<Point> points = new ArrayList<>();
//			points.add(new Point(74, 195));
//			points.add(new Point(74, 244));
//			srcConns.add(new ConnMock(new Rectangle(48, 106, 101, 89), new Rectangle(54, 244, 56, 53), points));
//		}
//		List<Connection> tgtConns = new ArrayList<>();
//		{
//			List<Point> points = new ArrayList<>();
//			points.add(new Point(87, 244));
//			points.add(new Point(87, 195));
//			tgtConns.add(new ConnMock(new Rectangle(54, 244, 56, 53), new Rectangle(48, 106, 101, 89), points));
//		}
//		{
//			List<Point> points = new ArrayList<>();
//			points.add(new Point(206, 173));
//			points.add(new Point(149, 173));
//			tgtConns.add(new ConnMock(new Rectangle(206, 168, 69, 53), new Rectangle(48, 106, 101, 89), points));
//		}
//		{
//			List<Point> points = new ArrayList<>();
//			points.add(new Point(95, 48));
//			points.add(new Point(95, 106));
//			tgtConns.add(new ConnMock(new Rectangle(87, 35, 15, 15), new Rectangle(48, 106, 101, 89), points));
//		}

		rb.updateBoxDrag(new Rectangle(48, 100, 101, 89));
		List<Connection> srcConns = new ArrayList<>();
		{
			List<Point> points = new ArrayList<>();
			points.add(new Point(149, 179));
			points.add(new Point(206, 179));
			srcConns.add(new ConnMock(new Rectangle(48, 100, 101, 89), new Rectangle(206, 168, 69, 53), points));
		}
		{
			List<Point> points = new ArrayList<>();
			points.add(new Point(74, 189));
			points.add(new Point(74, 244));
			srcConns.add(new ConnMock(new Rectangle(48, 100, 101, 89), new Rectangle(54, 244, 56, 53), points));
		}
		List<Connection> tgtConns = new ArrayList<>();
		{
			List<Point> points = new ArrayList<>();
			points.add(new Point(87, 244));
			points.add(new Point(87, 189));
			tgtConns.add(new ConnMock(new Rectangle(54, 244, 56, 53), new Rectangle(48, 100, 101, 89), points));
		}
		{
			List<Point> points = new ArrayList<>();
			points.add(new Point(206, 188));
			points.add(new Point(178, 188));
			points.add(new Point(178, 167));
			points.add(new Point(149, 167));
			tgtConns.add(new ConnMock(new Rectangle(206, 168, 69, 53), new Rectangle(48, 100, 101, 89), points));
		}
		{
			List<Point> points = new ArrayList<>();
			points.add(new Point(95, 48));
			points.add(new Point(95, 100));
			tgtConns.add(new ConnMock(new Rectangle(87, 35, 15, 15), new Rectangle(48, 100, 101, 89), points));
		}
	}
}
