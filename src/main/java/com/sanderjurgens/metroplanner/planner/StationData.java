package com.sanderjurgens.metroplanner.planner;


import com.sanderjurgens.metroplanner.model.IllegalRequestException;
import com.sanderjurgens.metroplanner.model.Line;
import com.sanderjurgens.metroplanner.model.Station;
import java.awt.Color;

/**
 * Data structure for a station, needed for the planning algorithm.
 *
 * @author sanderjurgens
 */
public class StationData {

    /**
     * The state of this (line) node in the search tree; white = undiscovered,
     * gray = discovered but unvisited, black = explored
     */
    private Color color;

    /** The station from which we came to reach this station */
    private Station parent;

    /** The distance we traveled to reach this station (from the origin) */
    private int distance;

    /** The line we used to reach this station */
    private Line line;

    /**
     * Constructs the data structure for a station.
     *
     * @param color a given color
     * @param parent a given parent
     * @param distance a given distance
     * @param line a given line
     * @throws IllegalRequestException if color is not white, gray or black, or
     * the distance is smaller than 0
     */
    public StationData(Color color, Station parent, int distance, Line line) throws IllegalRequestException {
        if ((color != Color.WHITE && color != Color.GRAY && color != Color.BLACK)) {
            throw new IllegalRequestException("StationData: color not white, gray or black");
        } else if (distance < 0) {
            throw new IllegalRequestException("StationData: distance smaller than 0");
        }
        this.color = color;
        this.parent = parent;
        this.distance = distance;
        this.line = line;
    }

    /**
     * Returns the color/state of the data structure for this station.
     *
     * White = undiscovered, Gray = discovered but unvisited, Black = explored.
     *
     * @return the color of the data structure for this station
     */
    public Color getColor() {
        return color;
    }

    /**
     * Returns the station from which we came to reach this station.
     *
     * @return the station from which we came to reach this station
     */
    public Station getParent() {
        return parent;
    }

    /**
     * Returns the distance we traveled to reach this station (from the origin).
     *
     * @return the distance we traveled to reach this station (from the origin)
     */
    public int getDistance() {
        return distance;
    }

    /**
     * Returns the line we used to reach this station.
     *
     * @return the line we used to reach this station.
     */
    public Line getLine() {
        return line;
    }

    /**
     * Sets the color/state of the data structure for this station.
     *
     * @param color a given color
     * @throws IllegalRequestException if color is not white, gray or black
     */
    public void setColor(Color color) throws IllegalRequestException {
        if (color != Color.WHITE && color != Color.GRAY && color != Color.BLACK) {
            throw new IllegalRequestException("StationData.setColor: color not white, gray or black");
        }
        this.color = color;
    }

    /**
     * Sets the station from which we came to reach this station.
     *
     * @param parent a given parent
     */
    public void setParent(Station parent) {
        this.parent = parent;
    }

    /**
     * Sets the distance we traveled to reach this station (from the origin).
     *
     * @param distance a given distance
     * @throws IllegalRequestException  the distance is smaller than 0
     */
    public void setDistance(int distance) throws IllegalRequestException {
        if (distance < 0) {
            throw new IllegalRequestException("StationData.setDistance: pre failed");
        }
        this.distance = distance;
    }

    /**
     * Sets the line we used to reach this station.
     *
     * @param line a given line
     */
    public void setLine(Line line) {
        this.line = line;
    }
}
