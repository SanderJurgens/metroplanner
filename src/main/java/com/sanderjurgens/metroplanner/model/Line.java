package com.sanderjurgens.metroplanner.model;

import java.util.ArrayList;

/**
 * A line with a code and a number of stops, it may be circular or one way.
 *
 * @author sanderjurgens
 */
public class Line extends ArrayList<Station> {

    /** Identification of the line */
    private final String code;

    /** Whether the line is circular; a connection from last to first station exists */
    private final boolean isCircular;
    
    /** Whether the line is one way, in the direction of index increments */
    private final boolean isOneWay;

    /** The number of stops on the line */
    private int count;

    /**
     * Constructs a line with given code.
     *
     * @param code code of the line
     * @param circular whether the line is circular
     * @param oneway whether the line is one way
     * @throws IllegalRequestException if empty code is provided
     */
    public Line(String code, boolean circular, boolean oneway)
            throws IllegalRequestException {
        if (code.length() == 0) {
            throw new IllegalRequestException("Line: code is empty");
        }
        this.code = code;
        isCircular = circular;
        isOneWay = oneway;
        count = 0;
    }

    /**
     * Returns the code of this line.
     *
     * @return code of the line
     */
    public String getCode() {
        return code;
    }

    /**
     * Returns whether the line is circular.
     *
     * @return whether the line is circular
     */
    public boolean isCircular() {
        return isCircular;
    }

    /**
     * Returns whether the line is one way.
     *
     * @return whether the line is one way
     */
    public Boolean isOneWay() {
        return isOneWay;
    }

    /**
     * Returns the number of stops on this line.
     *
     * @return the number of stops on this line
     */
    public int getCount() {
        return count;
    }

    /**
     * Returns whether this line is empty.
     *
     * @return whether this line is empty
     */
    @Override
    public boolean isEmpty() {
        return count == 0;
    }

    /**
     * Returns the first stop on this line.
     *
     * @return the first stop on this line
     */
    public Station getTerminalA() {
        if (isEmpty()) {
            return null;
        } else {
            return this.get(0);
        }
    }

    /**
     * Returns the last stop on this line.
     *
     * @return the last stop on this line
     */
    public Station getTerminalB() {
        if (isEmpty()) {
            return null;
        } else {
            return this.get(count - 1);
        }
    }

    /**
     * Returns the stop at given index i
     *
     * @param i a given index
     * @return the stop at given index i
     * @throws IndexOutOfBoundsException if the index is out of bounds
     */
    public Station getStop(int i) throws IndexOutOfBoundsException {
        if (i < 0 || count <= i) {
            throw new IndexOutOfBoundsException("Line.getStop: index out of bounds");
        }
        return this.get(i);
    }

    /**
     * Returns the index of the given stop.
     *
     * @param stop a given stop
     * @return the index of the given stop, or -1 if it isn't on the line
     * @throws IllegalRequestException if the given stop is equal to null
     */
    public int getIndex(Station stop) throws IllegalRequestException {
        if (stop == null) {
            throw new IllegalRequestException("Line.getIndex: stop is null");
        }
        int iterator = 0;
        for (Station s : this) {
            if (s.equals(stop)) {
                return iterator;
            }
            iterator = iterator + 1;
        }
        return -1;
    }

    /**
     * Indicates whether it is possible to add the given stop to this line. The
     * stop has to be unique.
     *
     * @param stop a given stop
     * @return whether the stop is unique to this line and non-null
     */
    public boolean canAdd(Station stop) {
        if (stop == null) {
            return false;
        }
        for (Station s : this) {
            if (s.equals(stop)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Adds a stop to the end of this line.
     *
     * @param stop a given stop
     * @throws IllegalRequestException if the stop cannot be added
     */
    @Override
    public boolean add(Station stop) throws IllegalRequestException {
        if (!canAdd(stop)) {
            throw new IllegalRequestException("Line.add: cannot add stop");
        }
        super.add(stop);
        count = count + 1;
        return true;
    }

    /**
     * Returns the code of the line.
     *
     * @return the code of the line
     */
    @Override
    public String toString() {
        return code;
    }
}
