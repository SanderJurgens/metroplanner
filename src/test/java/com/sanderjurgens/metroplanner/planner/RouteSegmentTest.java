package com.sanderjurgens.metroplanner.planner;

import com.sanderjurgens.metroplanner.model.IllegalRequestException;
import com.sanderjurgens.metroplanner.model.Line;
import com.sanderjurgens.metroplanner.model.Station;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * A set of unit tests for the RouteSegment class.
 *
 * @author sanderjurgens
 */
public class RouteSegmentTest {

    // Testing variables
    private Line line;
    private Station[] stations;
    private RouteSegment segment;
    
    /**
     * Set-up RouteSegment for testing.
     */
    @BeforeEach
    public void setupRouteSegment() {
        line = new Line("1", false, false);
        stations = new Station[]{
            new Station("a", "a"),
            new Station("b", "b"),
            new Station("c", "c")
        };
        for (Station s : stations) {
            line.add(s);
        }
        segment = new RouteSegment(line, stations[0], stations[stations.length - 1],
                stations[stations.length - 1]);
    }
    
    /**
     * Unit test of the constructor and all queries, of class RouteSegment.
     */
    @Test
    public void testRouteSegmentQueries() {
        // Check queries
        Assertions.assertEquals(line, segment.getLine(), 
                "The line is not correct");
        Assertions.assertEquals(stations[0], segment.getFromStation(), 
                "The begin station is not correct");
        Assertions.assertEquals(stations[stations.length - 1], segment.getToStation(),
                "The end station is not correct");
        Assertions.assertEquals(stations[stations.length - 1], segment.getDirection(),
                "The direction is not correct");

        // Null parameters in constructor
        Assertions.assertThrows(IllegalRequestException.class,
                () -> {
                    RouteSegment rs = new RouteSegment(null, stations[0], 
                            stations[stations.length - 1], stations[stations.length - 1]);
                },
                "IllegalRequestException was not thrown whilst line parameter was null"
        );
        Assertions.assertThrows(IllegalRequestException.class,
                () -> {
                    RouteSegment rs = new RouteSegment(line, null, 
                            stations[stations.length - 1], stations[stations.length - 1]);
                },
                "IllegalRequestException was not thrown whilst from parameter was null"
        );
        Assertions.assertThrows(IllegalRequestException.class,
                () -> {
                    RouteSegment rs = new RouteSegment(line, stations[0], null, 
                            stations[stations.length - 1]);
                },
                "IllegalRequestException was not thrown whilst to parameter was null"
        );
        Assertions.assertThrows(IllegalRequestException.class,
                () -> {
                    RouteSegment rs = new RouteSegment(line, stations[0], 
                            stations[stations.length - 1], null);
                },
                "IllegalRequestException was not thrown whilst direction parameter was null"
        );
    }

    /**
     * Unit test of the all commands, of class RouteSegment.
     */
    @Test
    public void testRouteSegmentCommands() {
        // Null parameters in commands 
        Assertions.assertThrows(IllegalRequestException.class,
                () -> {
                    segment.setLine(null);
                },
                "IllegalRequestException was not thrown whilst line parameter was null"
        );
        Assertions.assertThrows(IllegalRequestException.class,
                () -> {
                    segment.setFromStation(null);
                },
                "IllegalRequestException was not thrown whilst line parameter was null"
        );
        Assertions.assertThrows(IllegalRequestException.class,
                () -> {
                    segment.setToStation(null);
                },
                "IllegalRequestException was not thrown whilst line parameter was null"
        );
        Assertions.assertThrows(IllegalRequestException.class,
                () -> {
                    segment.setDirection(null);
                },
                "IllegalRequestException was not thrown whilst line parameter was null"
        );
        
        // Regular command usage (setLine)
        Line l = new Line("2", true, true);
        segment.setLine(l);
        Assertions.assertEquals(l, segment.getLine(), "New line not set");
        // Regular command usage (setFromStation)
        Station s = new Station("d", "d");
        segment.setFromStation(s);        
        Assertions.assertEquals(s, segment.getFromStation(), "New from station not set");
        // Regular command usage (setToStation)
        segment.setToStation(s);        
        Assertions.assertEquals(s, segment.getToStation(), "New to station not set");
        // Regular command usage (setDirection)
        segment.setDirection(s);
        Assertions.assertEquals(s, segment.getDirection(), "New direction not set");
        
        // Circular Test
        Assertions.assertTrue(!segment.usesCircular(), "RouteSegment should not use circular route");
        segment.setCircular(true);
        Assertions.assertTrue(segment.usesCircular(), "RouteSegment should use circular route");
    }
}
