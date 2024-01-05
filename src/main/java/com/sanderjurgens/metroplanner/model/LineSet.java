package com.sanderjurgens.metroplanner.model;

import java.util.ArrayList;

/**
 * A set of all lines.
 *
 * @author sanderjurgens
 */
public class LineSet extends ArrayList<Line> {

    /** The number of lines in the set */
    private int count;

    /**
     * Constructs an empty set of lines.
     */
    public LineSet() {
        count = 0;
    }

    /**
     * Returns the number of lines in this set.
     *
     * @return the number of lines in this set
     */
    public int getCount() {
        return count;
    }

    /**
     * Returns whether this set is empty.
     *
     * @return whether this set is empty
     */
    @Override
    public boolean isEmpty() {
        return count == 0;
    }

    /**
     * Returns the line in this set with given code.
     *
     * @param code a given code
     * @return the line with the given code
     */
    public Line getLine(String code) {
        for (Line line : this) {
            if (line.getCode().equals(code)) {
                return line;
            }
        }
        return null;
    }

    /**
     * Indicates whether it is possible to add the given line to this set. The
     * line has to be unique.
     *
     * @param line a given line
     * @return whether the line is unique to this set and non-null
     */
    public boolean canAdd(Line line) {
        if (line == null) {
            return false;
        }
        for (Line l : this) {
            if (l.getCode().equals(line.getCode())) {
                return false;
            }
        }
        return true;
    }

    /**
     * Adds a line to the set.
     *
     * @param line a given line
     * @throws IllegalRequestException if the line cannot be added
     */
    @Override
    public boolean add(Line line) throws IllegalRequestException {
        if (!canAdd(line)) {
            throw new IllegalRequestException("LineSet.add: cannot add line");
        }
        super.add(line);
        count = count + 1;
        return true;
    }

    /**
     * Returns a textual representation of all lines.
     *
     * @return a textual representation of all lines
     */
    @Override
    public String toString() {
        String output = "";
        for (Line line : this) {
            String circular = "0";
            String oneWay = "0";
            if (line.isCircular()) {
                circular = "1";
            }
            if (line.isOneWay()) {
                oneWay = "1";
            }
            output = output + "line:" + line.getCode() + ":" + circular + ":" + oneWay + ":";
            if (!line.isEmpty()) {
                output = output + line.getStop(0).getCode();
                for (int i = 1; i < line.getCount(); i = i + 1) {
                    output = output + "-" + line.getStop(i).getCode();
                }
            }
            output = output + "\n";
        }
        return output;
    }
}
