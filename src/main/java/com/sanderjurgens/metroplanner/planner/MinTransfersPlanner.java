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
 * transfers.
 *
 * @author sanderjurgens
 */
public class MinTransfersPlanner extends Planner {

    /** Additional network data needed to perform the algorithm */
    private HashMap<Line, LineData> dataMap;

    /**
     * Constructs a minimum transfer planner for a given network.
     *
     * @param network a given network
     */
    public MinTransfersPlanner(Network network) {
        super(network);
        dataMap = new HashMap<>();
    }

    /**
     * Finds a route from origin to destination with a minimum number of
     * transfers.
     *
     * @param from origin
     * @param to destination
     * @return a route from origin to destination with a minimum number of
     * transfers
     * @throws IllegalRequestException if the origin or destination is equal to
     * null
     */
    @Override
    public Route findRoute(Station from, Station to) throws IllegalRequestException {
        if (from == null || to == null) {
            throw new IllegalRequestException("Line.getIndex: stop is null");
        }

        // Initialization of Breadth-First Search data
        Queue<Line> queue = new LinkedList<>();
        Line targetLine = null;
        for (Line line : network.getLineSet()) {
            // Set all lines as undiscovered
            dataMap.put(line, new LineData(Color.WHITE, null, null));
            // Mark line if origin occurs on it 
            int fromIndex = line.getIndex(from);
            if (fromIndex != -1) {
                dataMap.get(line).setColor(Color.GRAY);
                dataMap.get(line).setEntry(from);
                queue.offer(line);
                // Store line if it also contains the destination (within reach)
                int toIndex = line.getIndex(to);
                if (toIndex != -1 && !(line.isOneWay() && !line.isCircular() && toIndex < fromIndex)) {
                    targetLine = line;
                }
            }
        }

        // While a line containing the destination is not reached, keep expanding
        while (!queue.isEmpty() && targetLine == null) {
            // Take the next line from the queue and expand along all its connected lines
            Line line = queue.poll();
            boolean belowRange = false;
            boolean aboveRange = false;            
            for (int i = 1; i < line.getCount(); i++) { 
                // Only expand from stations that are reachable, in outward pattern
                int entryIndex = line.getIndex(dataMap.get(line).getEntry());
                int checkIndex;
                if (line.isOneWay()) {
                    // For oneway in a line
                    checkIndex = Math.floorMod((entryIndex + i), line.getCount());
                    // Skip if station is unreachable
                    if (!line.isCircular() && (checkIndex < entryIndex)) {
                        continue;
                    }
                } else {
                    // For circular in both direction simultaneously
                    int checkModifier = (int)(Math.pow(-1, i) * Math.ceil((double)i/2));
                    checkIndex = Math.floorMod(entryIndex + checkModifier, line.getCount());
                    // For non-circular, once edge is reached, continue in a line the other way
                    if ((!line.isCircular() && (entryIndex + checkModifier) < 0)
                            || belowRange) {
                        checkIndex = i;
                        belowRange = true;
                    } else if ((!line.isCircular() && (entryIndex + checkModifier) >= line.getCount())
                            || aboveRange) {
                        checkIndex = (line.getCount()-1) - i;
                        aboveRange = true;
                    }
                }   
                // Explore all lines connecting to the station
                Station station = line.get(checkIndex);
                for (Line l : network.getLineSet()) {
                    // Update data of unvisited connected lines and add to queue
                    int stationIndex = l.getIndex(station);
                    if ((stationIndex != -1) && dataMap.get(l).getColor() == Color.WHITE) {
                        dataMap.get(l).setColor(Color.GRAY);
                        dataMap.get(l).setParent(line);
                        dataMap.get(l).setEntry(station);
                        queue.offer(l);
                         
                        // Also store line if it contains the destination (within reach)
                        int toIndex = l.getIndex(to);
                        if (toIndex != -1 && !(l.isOneWay() && !l.isCircular() && toIndex < stationIndex)) {
                            targetLine = l;
                            break;
                        }
                    }
                }
                // Stop once the target is found
                if (targetLine != null) {
                    break;
                }
            }
            // The current line is now explored and therefore set to black
            dataMap.get(line).setColor(Color.BLACK);
        }

        // Path reconstruction if a route is found
        Route route = new Route();
        if (targetLine != null && from != to) {
            // Reconstruct path in reverse by following parent pointers
            Stack<Line> stack = new Stack<>();
            Line line = targetLine;
            while (line != null) {
                stack.push(line);
                line = dataMap.get(line).getParent();
            }
            // Reverse the path by using the stack
            RouteSegment segment = null;
            while (!stack.isEmpty()) {
                line = stack.pop();
                if (segment == null) {
                    // Create first segment if none exists yet
                    segment = new RouteSegment(line, from, from, from);
                } else {
                    // Add direction to previous segment using entry point of new line                    
                    Line l = segment.getLine();
                    Station entry = dataMap.get(line).getEntry();
                    // Determine terminal
                    int f = l.getIndex(segment.getFromStation());
                    int t = l.getIndex(entry);
                    // Complex logic due to circular lines
                    Station terminal = l.getTerminalA();
                    if (l.isOneWay()) {
                        terminal = l.getTerminalB();
                        if (t < f) {
                            segment.setCircular(true);
                        }
                    } else if (l.isCircular()) {
                        if ((l.getCount() - Math.abs(t - f)) < Math.abs(t - f)) {
                           segment.setCircular(true);
                           if (t < f) {
                               terminal = l.getTerminalB();
                           }
                        } else {
                            if (f < t) {
                                terminal = l.getTerminalB();
                            }
                        }
                    } else if (f < t) {
                        terminal = l.getTerminalB();
                    }
                    // Update segment with information from new line
                    segment.setToStation(entry);
                    segment.setDirection(terminal);
                    // Add finished segment to route and create new segment from entry point
                    route.add(segment);
                    segment = new RouteSegment(line, entry, entry, entry);
                }
                // If no new line is on the stack then complete the last segment
                if (stack.isEmpty()) {
                    Line l = segment.getLine();
                    // Determine terminal
                    int f = l.getIndex(segment.getFromStation());
                    int t = l.getIndex(to);
                    // Complex logic due to circular lines
                    Station terminal = l.getTerminalA();
                    if (l.isOneWay()) {
                        terminal = l.getTerminalB();
                        if (t < f) {
                            segment.setCircular(true);
                        }
                    } else if (l.isCircular()) {
                        if ((l.getCount() - Math.abs(t - f)) < Math.abs(t - f)) {
                           segment.setCircular(true);
                           if (t < f) {
                               terminal = l.getTerminalB();
                           }
                        } else {
                            if (f < t) {
                                terminal = l.getTerminalB();
                            }
                        }
                    } else if (f < t) {
                        terminal = l.getTerminalB();
                    }
                    // Add finished segment to route
                    segment.setToStation(to);
                    segment.setDirection(terminal);
                    route.add(segment);
                }
            }
        }
        return route;
    }
}
