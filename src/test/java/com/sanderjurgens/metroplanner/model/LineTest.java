package com.sanderjurgens.metroplanner.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * A set of unit tests for the line class.
 *
 * @author sanderjurgens
 */
public class LineTest {

    /**
     * Unit test for a line.
     */
    @Test
    public void testLine() {
        // Normal situation
        Line line1 = new Line("1", true, true);
        Assertions.assertEquals("1", line1.getCode(), "The code was not correct");
        Assertions.assertTrue(line1.isCircular(), "The circular attribute was not correct");
        Assertions.assertTrue(line1.isOneWay(), "The oneway attribute was not correct");
        Assertions.assertTrue(line1.isEmpty(), "The line was not empty");
        Line line2 = new Line("2", false, false);
        Assertions.assertTrue(!line2.isCircular(), "The circular attribute was not correct");
        Assertions.assertTrue(!line2.isOneWay(), "The oneway attribute was not correct");

        // No code
        Assertions.assertThrows(IllegalRequestException.class,
                () -> {
                    Line l = new Line("", true, true);
                },
                "IllegalRequestException was not thrown in absence of code"
        );
    }

    /**
     * Unit test of add and canAdd methods, of class Line.
     */
    @Test
    public void testCanAdd() {
        // Check count of new line
        Line line = new Line("1", false, false);
        Assertions.assertEquals(0, line.getCount(), "The count was not correct");

        // Test adding null station
        Assertions.assertTrue(!line.canAdd(null), "Null station could be added");

        // Add regular station
        Station s = new Station("a", "a");
        Assertions.assertTrue(line.canAdd(s), "The station could not be added");
        line.add(s);
        Assertions.assertEquals(1, line.getCount(), "The station was not added");

        // Add same station again
        Assertions.assertTrue(!line.canAdd(s), "The station could be added twice");
        Assertions.assertThrows(IllegalRequestException.class,
                () -> {
                    line.add(s);
                },
                "IllegalRequestException was not thrown whilst adding station that could not be added"
        );
        Assertions.assertEquals(1, line.getCount(), "The station was still added");
    }

    /**
     * Unit test of getStop, getIndex, getTerminalA and getTerminalB methods, of
     * class Line.
     */
    @Test
    public void testGetStop() {
        // Set-up line
        Line line = new Line("1", false, false);
        Station[] stations = new Station[]{
            new Station("a", "a"),
            new Station("b", "b"),
            new Station("c", "c")
        };
        for (Station s : stations) {
            line.add(s);
        }

        // All stops
        for (int i = 0; i < stations.length; i++) {
            Assertions.assertEquals(stations[i], line.getStop(i), "Not the same station: " + i);
        }

        // All indices
        for (int i = 0; i < stations.length; i++) {
            Assertions.assertEquals(i, line.getIndex(stations[i]),
                    "Station " + i + "not found on the line");
        }

        // Terminals
        Assertions.assertEquals(stations[0], line.getTerminalA(), "Not the same station (A)");
        Assertions.assertEquals(stations[stations.length - 1], line.getTerminalB(), 
                "Not the same station (B)");

        // Out of bounds
        Assertions.assertThrows(IndexOutOfBoundsException.class,
                () -> {
                    line.getStop(stations.length);
                },
                "IndexOutOfBoundsException was not thrown when retrieving out of bounds station"
        );

        // Index of non-existent station
        Assertions.assertEquals(-1, line.getIndex(new Station("d", "d")),
                "Non-existant station found on the line");
    }
}
