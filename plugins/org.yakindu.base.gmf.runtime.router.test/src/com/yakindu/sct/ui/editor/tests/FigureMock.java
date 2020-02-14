package com.yakindu.sct.ui.editor.tests;

import java.beans.PropertyChangeListener;
import java.util.Collection;
import java.util.List;

import org.eclipse.draw2d.AncestorListener;
import org.eclipse.draw2d.Border;
import org.eclipse.draw2d.CoordinateListener;
import org.eclipse.draw2d.EventDispatcher;
import org.eclipse.draw2d.FigureListener;
import org.eclipse.draw2d.FocusEvent;
import org.eclipse.draw2d.FocusListener;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IClippingStrategy;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.KeyEvent;
import org.eclipse.draw2d.KeyListener;
import org.eclipse.draw2d.LayoutListener;
import org.eclipse.draw2d.LayoutManager;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.draw2d.MouseMotionListener;
import org.eclipse.draw2d.TreeSearch;
import org.eclipse.draw2d.UpdateManager;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.geometry.Translatable;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Font;

public class FigureMock implements IFigure {

	private Rectangle bounds;

	public FigureMock(Rectangle bounds) {
		this.bounds = bounds;
	}

	@Override
	public void add(IFigure figure) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void add(IFigure figure, int index) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void add(IFigure figure, Object constraint) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void add(IFigure figure, Object constraint, int index) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void addAncestorListener(AncestorListener listener) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void addCoordinateListener(CoordinateListener listener) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void addFigureListener(FigureListener listener) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void addFocusListener(FocusListener listener) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void addKeyListener(KeyListener listener) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void addLayoutListener(LayoutListener listener) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void addMouseListener(MouseListener listener) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void addMouseMotionListener(MouseMotionListener listener) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void addNotify() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void addPropertyChangeListener(String property, PropertyChangeListener listener) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean containsPoint(int x, int y) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean containsPoint(Point p) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void erase() {
		throw new UnsupportedOperationException();
	}

	@Override
	public IFigure findFigureAt(int x, int y) {
		throw new UnsupportedOperationException();
	}

	@Override
	public IFigure findFigureAt(int x, int y, TreeSearch search) {
		throw new UnsupportedOperationException();
	}

	@Override
	public IFigure findFigureAt(Point p) {
		throw new UnsupportedOperationException();
	}

	@Override
	public IFigure findFigureAtExcluding(int x, int y, Collection collection) {
		throw new UnsupportedOperationException();
	}

	@Override
	public IFigure findMouseEventTargetAt(int x, int y) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Color getBackgroundColor() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Border getBorder() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Rectangle getBounds() {
		return bounds.getCopy();
	}

	@Override
	public List getChildren() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Rectangle getClientArea() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Rectangle getClientArea(Rectangle rect) {
		throw new UnsupportedOperationException();
	}

	@Override
	public IClippingStrategy getClippingStrategy() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Cursor getCursor() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Font getFont() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Color getForegroundColor() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Insets getInsets() {
		throw new UnsupportedOperationException();
	}

	@Override
	public LayoutManager getLayoutManager() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Color getLocalBackgroundColor() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Color getLocalForegroundColor() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Dimension getMaximumSize() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Dimension getMinimumSize() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Dimension getMinimumSize(int wHint, int hHint) {
		throw new UnsupportedOperationException();
	}

	@Override
	public IFigure getParent() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Dimension getPreferredSize() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Dimension getPreferredSize(int wHint, int hHint) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Dimension getSize() {
		throw new UnsupportedOperationException();
	}

	@Override
	public IFigure getToolTip() {
		throw new UnsupportedOperationException();
	}

	@Override
	public UpdateManager getUpdateManager() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void handleFocusGained(FocusEvent event) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void handleFocusLost(FocusEvent event) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void handleKeyPressed(KeyEvent event) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void handleKeyReleased(KeyEvent event) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void handleMouseDoubleClicked(MouseEvent event) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void handleMouseDragged(MouseEvent event) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void handleMouseEntered(MouseEvent event) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void handleMouseExited(MouseEvent event) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void handleMouseHover(MouseEvent event) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void handleMouseMoved(MouseEvent event) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void handleMousePressed(MouseEvent event) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void handleMouseReleased(MouseEvent event) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean hasFocus() {
		throw new UnsupportedOperationException();
	}

	@Override
	public EventDispatcher internalGetEventDispatcher() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean intersects(Rectangle rect) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void invalidate() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void invalidateTree() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isCoordinateSystem() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isEnabled() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isFocusTraversable() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isMirrored() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isOpaque() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isRequestFocusEnabled() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isShowing() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isVisible() {
		return true;
	}

	@Override
	public void paint(Graphics graphics) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void remove(IFigure figure) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void removeAncestorListener(AncestorListener listener) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void removeCoordinateListener(CoordinateListener listener) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void removeFigureListener(FigureListener listener) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void removeFocusListener(FocusListener listener) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void removeKeyListener(KeyListener listener) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void removeLayoutListener(LayoutListener listener) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void removeMouseListener(MouseListener listener) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void removeMouseMotionListener(MouseMotionListener listener) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void removeNotify() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void removePropertyChangeListener(String property, PropertyChangeListener listener) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void repaint() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void repaint(int x, int y, int w, int h) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void repaint(Rectangle rect) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void requestFocus() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void revalidate() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setBackgroundColor(Color c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setBorder(Border b) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setBounds(Rectangle rect) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setClippingStrategy(IClippingStrategy clippingStrategy) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setConstraint(IFigure child, Object constraint) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setCursor(Cursor cursor) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setEnabled(boolean value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setFocusTraversable(boolean value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setFont(Font f) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setForegroundColor(Color c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setLayoutManager(LayoutManager lm) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setLocation(Point p) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setMaximumSize(Dimension size) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setMinimumSize(Dimension size) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setOpaque(boolean isOpaque) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setParent(IFigure parent) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setPreferredSize(Dimension size) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setRequestFocusEnabled(boolean requestFocusEnabled) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setSize(Dimension d) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setSize(int w, int h) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setToolTip(IFigure figure) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setVisible(boolean visible) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void translate(int x, int y) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void translateFromParent(Translatable t) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void translateToAbsolute(Translatable t) {
	}

	@Override
	public void translateToParent(Translatable t) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void translateToRelative(Translatable t) {
	}

	@Override
	public void validate() {
		throw new UnsupportedOperationException();
	}
}
