/**
 * Copyright (c) 2010 committers of YAKINDU and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * Contributors:
 * 	committers of YAKINDU - initial API and implementation
 *
 */
package org.yakindu.sct.ui.editor.editor.figures;

import java.lang.reflect.Field;

import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.RotatableDecoration;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gmf.runtime.draw2d.ui.figures.PolylineConnectionEx;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.IMapMode;

/**
 *
 * @author muelder
 *
 */
public class TransitionFigure extends PolylineConnectionEx {

	protected static final int TOLERANCE = 4;

	private final IMapMode mapMode;

	public TransitionFigure(IMapMode mapMode) {
		this(mapMode, false);
	}

	public TransitionFigure(IMapMode mapMode, boolean reversed) {
		this.mapMode = mapMode;
		setTolerance();
		setLineWidth(mapMode.DPtoLP(1));
		if (reversed) {
			setSourceDecoration(createTargetDecoration());
		} else {
			setTargetDecoration(createTargetDecoration());
		}
	}

	private RotatableDecoration createTargetDecoration() {
		PolygonDecoration df = new PolygonDecoration();
		df.setFill(true);
		df.setLineWidth(getMapMode().DPtoLP(1));
		df.setTemplate(PolygonDecoration.TRIANGLE_TIP);
		return df;
	}

	protected IMapMode getMapMode() {
		return mapMode;
	}

	@Override
	public void layout() {
		// TODO Auto-generated method stub
		super.layout();
		Rectangle srcBox = getSourceAnchor().getOwner().getBounds().getCopy();
		getSourceAnchor().getOwner().translateToAbsolute(srcBox);
		Rectangle tgtBox = getTargetAnchor().getOwner().getBounds().getCopy();
		getTargetAnchor().getOwner().translateToAbsolute(tgtBox);
		System.out.println("Did layout " + srcBox + ", " + tgtBox);
	}

	protected void setTolerance() {
		// Have to use reflection here, PolylineConnectionEx#calculateTolerance() is
		// private....
		try {
			Field declaredField = PolylineConnectionEx.class.getDeclaredField("TOLERANCE");
			declaredField.setAccessible(true);
			declaredField.set(null, TOLERANCE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
