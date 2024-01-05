package com.sanderjurgens.metroplanner.planner;

import com.sanderjurgens.metroplanner.model.IllegalRequestException;
import com.sanderjurgens.metroplanner.model.Line;
import com.sanderjurgens.metroplanner.model.Network;
import com.sanderjurgens.metroplanner.model.Station;
import java.awt.Color;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

/**
 * Returns the route from origin to destination with the minimum number of
 * stops.
 *
 * @author sanderjurgens
 */
public class MinStopsPlanner extends Planner {

    /** Additional network data needed to perform the algorithm */
    private HashMap<Station, StationData> dataMap;

    /**
     * Constructs a minimum stops planner for a given network.
     *
     * @param network a given network
     */
    public MinStopsPlanner(Network network) {
        super(network);
        dataMap = new HashMap<>();
    }

    /**
     * Finds a route from origin to destination with a minimum number of stops.
     *
     * @param from origin
     * @param to destination
     * @return a route from origin to destination with a minimum number of stops
     * @throws IllegalRequestException if the origin or destination is equal to
     * null
     */
    @Override
    public Route findRoute(Station from, Station to) throws IllegalRequestException {
        if (from == null || to == null) {
            throw new IllegalRequestException("Line.getIndex: stop is null");
        }

        // Initialization of Breadth-First Search data
        Queue<Station> queue = new LinkedList<>();
        for (Station station : network.getStationSet()) {
            // Set all stations as undiscovered and at maximum distance from the origin
            dataMap.put(station, new StationData(Color.WHITE, null,
                    Integer.MAX_VALUE, null));
        }
        // Set the origin as discovered with a distance of 0 and add it to the queue
        dataMap.get(from).setColor(Color.GRAY);
        dataMap.get(from).setDistance(0);
        queue.offer(from);

        // While the destination is not reached, keep expanding through the network
        while (!queue.isEmpty() && dataMap.get(to).getColor() != Color.GRAY) {
            // Take the next station from the queue and expand along all its lines
            Station stop = queue.poll();
            for (Line line : network.getLineSet()) {
                // Check if the current station is on this line
                int index = line.getIndex(stop);
                if (index != -1) {
                    // If the station is on this line, we find the neighboring stations
                    Station prevStation, nextStation;
                    if (index == 0) {
                        prevStation = line.isCircular() ? line.getTerminalB() : null;
                        nextStation = line.getStop(index + 1);
                    } else if (index == (line.getCount() - 1)) {
                        prevStation = line.getStop(index - 1);
                        nextStation = line.isCircular() ? line.getTerminalA() : null;
                    } else {
                        prevStation = line.getStop(index - 1);
                        nextStation = line.getStop(index + 1);
                    }
                    // Update data of unvisited reachable neighbors and add to queue
                    if (prevStation != null
                            && dataMap.get(prevStation).getColor() == Color.WHITE
                            && !line.isOneWay()) {
                        dataMap.get(prevStation).setColor(Color.GRAY);
                        dataMap.get(prevStation).setParent(stop);
                        dataMap.get(prevStation).setDistance(dataMap.get(stop).getDistance() + 1);
                        dataMap.get(prevStation).setLine(line);
                        queue.offer(prevStation);
                    }
                    if (nextStation != null
                            && dataMap.get(nextStation).getColor() == Color.WHITE) {
                        dataMap.get(nextStation).setColor(Color.GRAY);
                        dataMap.get(nextStation).setParent(stop);
                        dataMap.get(nextStation).setDistance(dataMap.get(stop).getDistance() + 1);
                        dataMap.get(nextStation).setLine(line);
                        queue.offer(nextStation);
                    }
                }
            }
            // The current station is now explored and therefore set to black
            dataMap.get(stop).setColor(Color.BLACK);
        }

        // Path reconstruction if a route is found
        Route route = new Route();
        if (dataMap.get(to).getColor() == Color.GRAY && from != to) {
            // Reconstruct path in reverse by following parent pointers
            Stack<Station> stack = new Stack<>();
            Station stop = to;
            while (stop != null) {
                stack.push(stop);
                // If the next parent is the origin, set its departing line
                if (dataMap.get(stop).getParent() == from) {
                    dataMap.get(from).setLine(dataMap.get(stop).getLine());
                }
                stop = dataMap.get(stop).getParent();
            }
            // Reverse the path by using the stack            
            stop = stack.pop();
            RouteSegment segment = new RouteSegment(dataMap.get(stop).getLine(),
                    from, stop, stop);            
            while (!stack.isEmpty()) {
                stop = stack.pop();
                Station segmentEnd = segment.getToStation();
                Line line = dataMap.get(stop).getLine();
                // Determine terminal
                int f = line.getIndex(segmentEnd);
                int t = line.getIndex(stop);                
                Station terminal = line.getTerminalA();
                // Complex logic due to circular lines
                if ((f + 1) == t || (t == 0 && f == (line.getCount()-1))) {
                    terminal = line.getTerminalB();
                }
                // Expand current segment if stop is on the same line as the segment
                if (line == segment.getLine()) {
                    segment.setDirection(terminal);
                    segment.setToStation(stop);
                } else {
                    // Add previous segment to route and start a new one if stop is on another line
                    route.add(segment);
                    segment = new RouteSegment(line, segmentEnd, stop, terminal);
                }
                // Check for usage of circular lines
                if (Math.abs(t - f) > 1) {
                    segment.setCircular(true);
                }
            }
            route.add(segment);
        }

        return route;
    }
}
