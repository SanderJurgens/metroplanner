package com.sanderjurgens.metroplanner.gui;

import com.sanderjurgens.metroplanner.model.Line;
import com.sanderjurgens.metroplanner.model.Station;
import com.sanderjurgens.metroplanner.planner.RouteSegment;
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Visual representation of one complete metro line.
 *
 * Note that this class is not a (Vis)Component, instead it is explicitly drawn
 * onto the MapPanel.
 *
 * @author sanderjurgens
 */
public class VisLine {

    /** The actual line that is being visualized */
    private final Line line;

    /** The color this line has on the map */
    private final Color color;

    /** The list of visual stations on the line in the same order as the model line,
    including dummy stations representing bends on the line. */
    private final List<VisStation> stations;

    /** Whether the entire line is visible */
    private boolean visible;

    /** SegmentStations are all the stations where the line changes from visible to non-visible. */
    private final Set<Station> segmentStations;

    /**
     * Constructs a new metro line.
     *
     * @param line the line that this visual object represents
     * @param color the color of this line on the map
     */
    public VisLine(Line line, Color color) {
        this.line = line;
        this.color = color;
        stations = new ArrayList<>();
        visible = true;
        segmentStations = new HashSet<>();
        this.showAll();
    }

    /**
     * Return the visual representation of a line corresponding to a line.
     *
     * @return the line that this visual object represents
     */
    public Line getLine() {
        return line;
    }

    /**
     * Append a station visualization to this line visualization.
     *
     * @param station a visual represents of a given station
     */
    public void addStation(VisStation station) {
        stations.add(station);
    }

    /**
     * Add a list of stations between two existing adjacent stations. This is
     * handy for adding in the bends (i.e. dummy stations).
     *
     * @param from the first station of the segment
     * @param to the last station of the segment
     * @param betweenStations the stations to add between from and to
     */
    public void addBetweenStations(VisStation from, VisStation to, List<VisStation> betweenStations) {

        // Find if there's a spot to add the stations
        for (int i = 1; i < stations.size(); i++) {
            if (stations.get(i - 1) == from && stations.get(i) == to) {

                // Add all stations
                stations.addAll(i, betweenStations);
            }
        }
        
        // Add stations to the end for circular lines
        if (!stations.isEmpty()) {
            if (stations.get(stations.size() - 1) == from && stations.get(0) == to) {
                stations.addAll(betweenStations);            
            }
        }
    }

    /**
     * Don't show any part of this line.
     */
    public void showNone() {
        visible = false;
        segmentStations.clear();
    }

    /**
     * Show some part of the line (on top of what was already visible). Note
     * that this function assumes the given segment does not overlap an already
     * visible part of the line.
     *
     * @param segment the segment to be shown in the visualization
     */
    public void showSegment(RouteSegment segment) {
        // Special rules if the segment uses the circular part of a line
        if (segment.usesCircular()) {
            // 
            if (segment.getFromStation() == line.getTerminalA()) {
                // No need to start drawing immediately, circular part at the end
                segmentStations.add(segment.getToStation());  
            } else if (segment.getToStation() == line.getTerminalA()) {
                // No need to start drawing immediately, circular part at the end
                segmentStations.add(segment.getFromStation());  
            } else {
                // Also add terminal to start drawing immediately
                segmentStations.add(line.getTerminalA());
                segmentStations.add(segment.getFromStation());
                segmentStations.add(segment.getToStation());
                
            }         
        } else {
            segmentStations.add(segment.getFromStation());
            segmentStations.add(segment.getToStation());
        }
        visible = true;        
    }

    /**
     * Show every part of the line.
     */
    public void showAll() {
        segmentStations.clear();
        segmentStations.add(line.getTerminalA());
        visible = true;
    }

    /**
     * Draws all visible parts of the line onto a Graphics object.
     *
     * @param graphics the Graphics context in which to draw
     */
    protected void draw(Graphics graphics) {
        
        // Draw nothing if the line is set to invisible
        if (!visible) {
            return;
        }

        // Set the color of the drawn line.
        graphics.setColor(color);

        // Draw is a boolean that determines whether the next line segment should be drawn
        // It switches everytime we hit upon a station in segmentStations
        boolean draw = false;
        
        // Determine if first line segment should be drawn
        if (segmentStations.contains(line.getTerminalA())) {
            draw = true;
        }

        // Iterate over subsequent station pairs on the line
        for (int i = 1; i < stations.size(); i++) {

            // Draw line between stations
            VisStation visfrom = stations.get(i - 1);
            VisStation visto = stations.get(i);

            if (draw) {
                graphics.drawLine(visfrom.x, visfrom.y, visto.x, visto.y);
            }

            // Toggle drawing if the destination is the start or end of a visible segment
            if (segmentStations.contains(visto.getStation())) {
                draw = !draw;
            }
        }
        
        // Finish drawing circular line if needed
        if (draw && line.isCircular()) {
            VisStation end = stations.get(stations.size()-1);
            VisStation start = stations.get(0);            
            graphics.drawLine(end.x, end.y, start.x, start.y);
        }
    }
}
