package com.sanderjurgens.metroplanner.planner;

import com.sanderjurgens.metroplanner.model.IllegalRequestException;
import com.sanderjurgens.metroplanner.model.Line;
import com.sanderjurgens.metroplanner.model.Station;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * A set of unit tests for the Route class.
 *
 * @author sanderjurgens
 */
public class RouteTest {

    /**
     * Unit test of add and canAdd methods, of class Route.
     */
    @Test 
    public void testCanAdd() {
        // Check count of new Route
        Route r = new Route();
        Assertions.assertEquals(0, r.getCount(), "The count was not correct");
        Assertions.assertTrue(r.isEmpty(), "The Route was not empty");
        
        
        // Test adding null RouteSegment
        Assertions.assertTrue(!r.canAdd(null), "Null RouteSegment could be added");

        // Add regular RouteSegment
        Line l = new Line("1", false, false);
        Station[] s = new Station[]{
            new Station("a", "a"),
            new Station("b", "b")
        };       
        RouteSegment rs = new RouteSegment(l, s[0], s[1], s[1]);
        Assertions.assertTrue(r.canAdd(rs), "The RouteSegment could not be added");
        r.add(rs);
        Assertions.assertEquals(1, r.getCount(), "The RouteSegment was not added");

        // Add same RouteSegment again
        Assertions.assertTrue(!r.canAdd(rs), "The RouteSegment could be added twice");
        Assertions.assertThrows(IllegalRequestException.class,
                () -> {
                    r.add(rs);
                },
                "IllegalRequestException was not thrown whilst adding RouteSegment that could not be added"
        );
        Assertions.assertEquals(1, r.getCount(), "The RouteSegment was still added"); 
    }
}
