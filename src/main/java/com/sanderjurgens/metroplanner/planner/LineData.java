package com.sanderjurgens.metroplanner.planner;

import com.sanderjurgens.metroplanner.model.IllegalRequestException;
import com.sanderjurgens.metroplanner.model.Line;
import com.sanderjurgens.metroplanner.model.Station;
import java.awt.Color;

/**
 * Data structure for a line, needed for the planning algorithm.
 *
 * @author sanderjurgens
 */
public class LineData {

    /**
     * The state of this (line) node in the search tree; White = undiscovered,
     * Gray = discovered but unvisited, Black = explored
     */
    private Color color;

    /** The line from which we came to enter onto this line */
    private Line parent;

    /** The station that we used to enter onto this line */
    private Station entry;

    /**
     * Constructs the data structure for a line.
     *
     * @param color a given color
     * @param parent a given parent
     * @param entry a given entry
     * @throws IllegalRequestException if color is not white, gray or black
     */
    public LineData(Color color, Line parent, Station entry) throws IllegalRequestException {
        if (color != Color.WHITE && color != Color.GRAY && color != Color.BLACK) {
            throw new IllegalRequestException("LineData: color not white, gray or black");
        }
        this.color = color;
        this.parent = parent;
        this.entry = entry;
    }

    /**
     * Returns the color/state of the data structure for this line.
     *
     * White = undiscovered, Gray = discovered but unvisited, Black = explored.
     *
     * @return the color/state of the data structure for this line
     */
    public Color getColor() {
        return color;
    }

    /**
     * Returns the line from which we came to enter onto this line.
     *
     * @return the line from which we came to enter onto this line
     */
    public Line getParent() {
        return parent;
    }

    /**
     * Returns the station that we used to enter onto this line.
     *
     * @return the station that we used to enter onto this line
     */
    public Station getEntry() {
        return entry;
    }

    /**
     * Sets the color/state of the data structure for this line.
     *
     * White = undiscovered, Gray = discovered but unvisited, Black = explored.
     *
     * @param color a given color
     * @throws IllegalRequestException if color is not white, gray or black
     */
    public void setColor(Color color) throws IllegalRequestException {
        if (color != Color.WHITE && color != Color.GRAY && color != Color.BLACK) {
            throw new IllegalRequestException("LineData.setColor: color not white, gray or black");
        }
        this.color = color;
    }

    /**
     * Sets the line from which we came to enter onto this line.
     *
     * @param parent a given parent
     */
    public void setParent(Line parent) {
        this.parent = parent;
    }

    /**
     * Sets the station that we used to enter onto this line.
     *
     * @param entry a given entry
     */
    public void setEntry(Station entry) {
        this.entry = entry;
    }
}
