package com.sanderjurgens.metroplanner.gui;

import java.awt.Graphics;

/**
 * VisStationDummy is a station that doesn't actually represent any station in
 * the network, it just exists for the purpose of creating bends on the line.
 *
 * It therefore has no station to represent and nothing to paint, it's only a
 * location on the map.
 *
 * @author sanderjurgens
 */
public class VisStationDummy extends VisStation {

    /**
     * Constructs a dummy station. Calls VisStation constructor, but without a
     * station.
     *
     * @param x the horizontal coordinate of the dummy station
     * @param y the vertical coordinate of the dummy station
     */
    public VisStationDummy(int x, int y) {
        super(null, false, x, y);
    }

    /**
     * Draws nothing.
     *
     * @param gr the JPanel graphics
     */
    @Override
    protected void paintComponent(Graphics gr) {
        // Nothing to draw
    }
}
