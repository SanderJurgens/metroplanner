package com.sanderjurgens.metroplanner.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * A set of unit tests for the LineSet class.
 *
 * @author sanderjurgens
 */
public class LineSetTest {

    /**
     * Unit test of add and canAdd methods, of class LineSet.
     */
    @Test
    public void testCanAdd() {
        // Check count of new LineSet
        LineSet ls = new LineSet();
        Assertions.assertEquals(0, ls.getCount(), "The count was not correct");
        Assertions.assertTrue(ls.isEmpty(), "The LineSet was not empty");
        
        // Test adding null station
        Assertions.assertTrue(!ls.canAdd(null), "Null line could be added");

        // Add regular station
        Line l = new Line("1", false, false);
        Assertions.assertTrue(ls.canAdd(l), "The line could not be added");
        ls.add(l);
        Assertions.assertEquals(1, ls.getCount(), "The line was not added");

        // Add same station again
        Assertions.assertTrue(!ls.canAdd(l), "The line could be added twice");
        Assertions.assertThrows(IllegalRequestException.class,
                () -> {
                    ls.add(l);
                },
                "IllegalRequestException was not thrown whilst adding line that could not be added"
        );
        Assertions.assertEquals(1, ls.getCount(), "The line was still added"); 
    }

    /**
     * Unit test of getLine method, of class LineSet.
     */
    @Test
    public void testGetLine() {
        // Set-up LineSet
        LineSet ls = new LineSet();
        Line[] lines = new Line[]{
            new Line("1", false, false),
            new Line("2", false, false),
            new Line("3", false, false)
        };
        for (Line l : lines) {
            ls.add(l);
        }

        // All indices
        for (Line l : lines) {
            Assertions.assertEquals(l, ls.getLine(l.getCode()), 
                    "Line " + l.getCode() + "not found in the LineSet");
        }

        // Index of non-existent line
        Assertions.assertEquals(null, ls.getLine("4"),
                "Non-existant line found in the LineSet");
    }

    /**
     * Unit test of toString method, of class LineSet.
     */
    @Test
    public void testToString() {
        // Empty set
        LineSet ls = new LineSet();
        Assertions.assertEquals("", ls.toString(), "Empty string was not correct");
        
        // Single Line        
        Station[] stations = new Station[]{
            new Station("a", "a"),
            new Station("b", "b"),
            new Station("c", "c")
        };
        Line line1 = new Line("1", false, false);        
        String expected = "line:1:0:0:";
        for (int i=0; i<stations.length; i++) {
            line1.add(stations[i]);
            expected = expected + stations[i].getCode();
            if (i < stations.length-1) {
                expected = expected + "-";
            }
        }        
        ls.add(line1);
        expected = expected + "\n";
        Assertions.assertEquals(expected, ls.toString(), "Single lines string was not correct");
        
        // Two lines
        Line line2 = new Line("2", true, true);        
        expected = expected + "line:2:1:1:";
        for (int i=stations.length-1; i>=0; i--) {
            line2.add(stations[i]);
            expected = expected + stations[i].getCode();
            if (i > 0) {
                expected = expected + "-";
            }
        }        
        ls.add(line2);
        expected = expected + "\n";
        Assertions.assertEquals(expected, ls.toString(), "Multiple lines string was not correct");     
    }
}
