package com.sanderjurgens.metroplanner.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * A set of unit tests for the station class.
 * 
 * @author sanderjurgens
 */
public class StationTest {

    /**
     * Unit test for a station.
     */
    @Test
    public void testStation() {
        // Normal situation
        Station station = new Station("a","b");
        Assertions.assertEquals("a", station.getCode(), "The code was not correct");
        Assertions.assertEquals("b", station.getName(), "The name was not correct");
        
        // No code
        Assertions.assertThrows(IllegalRequestException.class, 
                () -> {Station s = new Station("","b");},
                "IllegalRequestException was not thrown in absence of code"
        );
        
        // No name
        Assertions.assertThrows(IllegalRequestException.class,
                () -> {Station s = new Station("a","");},
                "IllegalRequestException was not thrown in absence of name"
        );
        
        // No code and name
        Assertions.assertThrows(IllegalRequestException.class, 
                () -> {Station s = new Station("","");},
                "IllegalRequestException was not thrown in absence of name and code"
        );
    }
}
