package com.sanderjurgens.metroplanner.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * A set of unit tests for the StationSet class.
 *
 * @author sanderjurgens
 */
public class StationSetTest {
    
    /**
     * Unit test of add and canAdd methods, of class StationSet.
     */
    @Test
    public void testCanAdd() {
        // Check count of new StationSet
        StationSet ss = new StationSet();
        Assertions.assertEquals(0, ss.getCount(), "The count was not correct");
        Assertions.assertTrue(ss.isEmpty(), "The StationSet was not empty");
        
        // Test adding null station
        Assertions.assertTrue(!ss.canAdd(null), "Null station could be added");

        // Add regular station
        Station s = new Station("a", "a");
        Assertions.assertTrue(ss.canAdd(s), "The station could not be added");
        ss.add(s);
        Assertions.assertEquals(1, ss.getCount(), "The station was not added");

        // Add same station again
        Assertions.assertTrue(!ss.canAdd(s), "The station could be added twice");
        Assertions.assertThrows(IllegalRequestException.class,
                () -> {
                    ss.add(s);
                },
                "IllegalRequestException was not thrown whilst adding station that could not be added"
        );
        Assertions.assertEquals(1, ss.getCount(), "The station was still added"); 
    }

    /**
     * Unit test of getStation method, of class StationSet.
     */
    @Test
    public void testGetStation() {
        // Set-up StationSet
        StationSet ss = new StationSet();
        Station[] stations = new Station[]{
            new Station("a", "a"),
            new Station("b", "b"),
            new Station("c", "c")
        };
        for (Station s : stations) {
            ss.add(s);
        }

        // All indices
        for (Station s : stations) {
            Assertions.assertEquals(s, ss.getStation(s.getCode()), 
                    "Station " + s.getCode() + "not found in the StationSet");
        }

        // Index of non-existent station
        Assertions.assertEquals(null, ss.getStation("d"),
                "Non-existant station found in the StationSet");
    }
    
    /**
     * Unit test of toString method, of class StationSet.
     */
    @Test
    public void testToString() {
        // Empty set
        StationSet ss = new StationSet();
        Assertions.assertEquals("", ss.toString(), "Empty string was not correct");
        
        // Single station
        ss.add(new Station("1", "S1"));
        String expected = "station:1:S1\n";
        Assertions.assertEquals(expected, ss.toString(), "Single station string was not correct");

        // Multiple stations
        for (int i=1; i<=3; i++) {
            ss.add(new Station(String.valueOf(i), "S" + i));
            expected = expected + "station:" + i + ":S" + i + "\n";
        }
        Assertions.assertEquals(expected, ss.toString(), "Multiple stations string was not correct");
    }
}
