package com.sanderjurgens.metroplanner.planner;

import com.sanderjurgens.metroplanner.model.IllegalRequestException;
import com.sanderjurgens.metroplanner.model.Line;
import com.sanderjurgens.metroplanner.model.Station;

/**
 * A segment of a Route.
 *
 * @author sanderjurgens
 */
public class RouteSegment {

    /** The line which this segment is in */
    private Line line;

    /** The stations defining the beginning of the segment */
    private Station from;
    /** The stations defining the ending of the segment */
    private Station to;

    /** The direction traveled along the line, towards this terminal station */
    private Station direction;
    /** Whether it utilizes the circular nature of the line */
    private boolean circular;

    /**
     * Constructs a segment of a line with a beginning, end and a direction.
     *
     * @param line a given line
     * @param from a given beginning station of a segment
     * @param to a given ending station of a segment
     * @param direction a given terminal station
     * @throws IllegalRequestException if a given parameter is null
     */
    public RouteSegment(Line line, Station from, Station to, Station direction)
            throws IllegalRequestException {
        if (line == null || from == null || to == null || direction == null) {
            throw new IllegalRequestException("RouteSegment: parameter is null");
        }
        this.line = line;
        this.from = from;
        this.to = to;
        this.direction = direction;
        circular = false;
    }

    /**
     * Returns the line in which the segment lies.
     *
     * @return the line in which the segment lies
     */
    public Line getLine() {
        return line;
    }

    /**
     * Returns the station where the segment begins.
     *
     * @return the station where the segment begins
     */
    public Station getFromStation() {
        return from;
    }

    /**
     * Returns the station where the segment ends.
     *
     * @return the station where the segment ends
     */
    public Station getToStation() {
        return to;
    }

    /**
     * Returns the terminal in the direction which is traveled.
     *
     * @return the terminal in the direction which is traveled
     */
    public Station getDirection() {
        return direction;
    }

    /**
     * Returns whether the segment utilizes the circular nature of the line.
     *
     * @return whether the segment utilizes the circular nature of the line
     */
    public boolean usesCircular() {
        return circular;
    }

    /**
     * Sets the line of this segment.
     *
     * @param line a given line
     * @throws IllegalRequestException if the given line is null
     */
    public void setLine(Line line) throws IllegalRequestException {
        if (line == null) {
            throw new IllegalRequestException("RouteSegment.setLine: pre failed");
        }
        this.line = line;
    }

    /**
     * Sets the station where this segment begins.
     *
     * @param from a given station
     * @throws IllegalRequestException if the given station is null
     */
    public void setFromStation(Station from) throws IllegalRequestException {
        if (from == null) {
            throw new IllegalRequestException("RouteSegment.setFromStation: pre failed");
        }
        this.from = from;
    }

    /**
     * Sets the station where this segment ends.
     *
     * @param to a given station
     * @throws IllegalRequestException if the given station is null
     */
    public void setToStation(Station to) throws IllegalRequestException {
        if (to == null) {
            throw new IllegalRequestException("RouteSegment.setToStation: pre failed");
        }
        this.to = to;
    }

    /**
     * Sets the direction of this segment.
     *
     * @param direction a given terminal
     * @throws IllegalRequestException if the given station is null
     */
    public void setDirection(Station direction) throws IllegalRequestException {
        if (direction == null) {
            throw new IllegalRequestException("RouteSegment.setDirection: pre failed");
        }
        this.direction = direction;
    }
    
     /**
     * Sets whether the segment utilizes the circular nature of the line.
     *
     * @param circular whether the segment utilizes the circular nature of the line
     */
    public void setCircular(boolean circular) throws IllegalRequestException {
        this.circular = circular;
    }
}
