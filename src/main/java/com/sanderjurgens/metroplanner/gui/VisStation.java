package com.sanderjurgens.metroplanner.gui;

import com.sanderjurgens.metroplanner.model.Station;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * The visual representation of a Station.
 *
 * @author sanderjurgens
 */
public class VisStation extends VisComponent implements MouseListener {

    /** The station this visual object represents */
    private final Station station;

    /** Whether this is a large transfer station or a small stopping station */
    private boolean transfer = false;
    /** The size of the circle for the station */
    private final int size;

    /** Graphical size of a stop station in pixels */
    private static final int SIZE_STOP = 5;    
    /** Graphical size of a transfer station in pixels */
    private static final int SIZE_TRANSFER = 11;

    /**
     * Constructs a new station.
     *
     * @param station the station this visual object represents
     * @param transfer whether is a large transfer station or a small stopping
     * station
     * @param x the horizontal coordinate of the station
     * @param y the vertical coordinate of the station
     */
    public VisStation(Station station, boolean transfer, int x, int y) {
        size = transfer ? SIZE_TRANSFER : SIZE_STOP;
        this.station = station;
        this.transfer = transfer;

        this.setPosition(x, y);
        this.addMouseListener(this);
    }

    /**
     * Draw a white circle with a black border to represent a station.
     *
     * @param graphics the Graphics context in which to draw
     */
    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        // Draw white back
        graphics.setColor(Color.white);
        graphics.fillOval(0, 0, size - 1, size - 1);

        // Draw black border
        graphics.setColor(Color.black);
        graphics.drawOval(0, 0, size - 1, size - 1);
    }

    /**
     * Set the position of the Station by specifying its center.
     *
     * @param x the horizontal coordinate of the station's center
     * @param y the vertical coordinate of the station's center
     */
    @Override
    public void setPosition(int x, int y) {
        super.setPosition(x, y);

        this.setBounds(x - size / 2, y - size / 2, size, size);
    }

    /**
     * Get the station that this visual object represents.
     *
     * @return The station this visual object represents
     */
    Station getStation() {
        return station;
    }

    /**
     * Mouse handler for when the station is clicked.
     *
     * Delegate this back to the MainFrame with a reference of which station was
     * clicked and whether it was a left or right mouse click.
     *
     * @param me the mouse event
     */
    @Override
    public void mousePressed(MouseEvent me) {
        if (me.getButton() == MouseEvent.BUTTON1) {
            MainFrame.stationClicked(station, true);
        } else {
            MainFrame.stationClicked(station, false);
        }
    }

    /**
     * Stub.
     *
     * @param me the mouse event
     */
    @Override
    public void mouseClicked(MouseEvent me) {
    }

    /**
     * Stub.
     *
     * @param me the mouse event
     */
    @Override
    public void mouseReleased(MouseEvent me) {
    }

    /**
     * Stub.
     *
     * @param me the mouse event
     */
    @Override
    public void mouseEntered(MouseEvent me) {
    }

    /**
     * Stub.
     *
     * @param me the mouse event
     */
    @Override
    public void mouseExited(MouseEvent me) {
    }
}
