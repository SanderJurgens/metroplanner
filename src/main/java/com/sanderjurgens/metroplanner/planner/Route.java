package com.sanderjurgens.metroplanner.planner;

import com.sanderjurgens.metroplanner.model.IllegalRequestException;
import java.util.ArrayList;

/**
 * A planned route consisting of segments.
 *
 * @author sanderjurgens
 */
public class Route extends ArrayList<RouteSegment> {

    /** The number of segments in the route */
    private int count;

    /**
     * Constructs an empty route.
     */
    public Route() {
        count = 0;
    }

    /**
     * Returns the number of segments in this route.
     *
     * @return the number of segments in this route
     */
    public int getCount() {
        return count;
    }

    /**
     * Returns whether this route is empty.
     *
     * @return whether this route is empty
     */
    @Override
    public boolean isEmpty() {
        return count == 0;
    }

    /**
     * Indicates whether it is possible to add the segment to this route. The
     * segment has to be unique.
     *
     * @param segment a given segment
     * @return whether the segment is unique to this route and non-null
     */
    public Boolean canAdd(RouteSegment segment) {
        if (segment == null) {
            return false;
        }
        for (RouteSegment s : this) {
            if (s.equals(segment)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Adds a segment to the end of this route.
     *
     * @param segment a given segment
     * @throws IllegalRequestException if the segment cannot be added
     */
    @Override
    public boolean add(RouteSegment segment) throws IllegalRequestException {
        if (!canAdd(segment)) {
            throw new IllegalRequestException("Route.add: cannot add segment");
        }
        super.add(segment);
        count = count + 1;
        return true;
    }
}
