package com.sanderjurgens.metroplanner.gui;

import java.awt.Point;
import javax.swing.JComponent;

/**
 * Generic visual component to be placed on a map. Only has a position.
 *
 * @author sanderjurgens
 */
abstract class VisComponent extends JComponent {

    /** Position of the component */
    protected int x, y;

    /**
     * Give the component a position on the map. It is up to the component how
     * it draws itself around this position.
     *
     * Subclasses must call the setBounds() method to correctly set the size of
     * the component when this method is called.
     *
     * @param x the horizontal coordinate of the component
     * @param y the vertical coordinate of the component
     */
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Give the component a position on the map. It is up to the component how
     * it draws itself around this position.
     *
     * Subclasses must call the setBounds() method to correctly set the size of
     * the component when this method is called.
     *
     * @param p the coordinates of the component
     */
    public void setPosition(Point p) {
        setPosition(p.x, p.y);
    }

    /**
     * Get the position of this component.
     *
     * @return Point(x, y)
     */
    public Point getPosition() {
        return new Point(x, y);
    }
}
