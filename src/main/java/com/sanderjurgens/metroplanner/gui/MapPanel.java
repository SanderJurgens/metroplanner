package com.sanderjurgens.metroplanner.gui;

import com.sanderjurgens.metroplanner.model.Line;
import com.sanderjurgens.metroplanner.model.Network;
import com.sanderjurgens.metroplanner.model.Station;
import com.sanderjurgens.metroplanner.planner.Route;
import com.sanderjurgens.metroplanner.planner.RouteSegment;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import javax.swing.ImageIcon;

/**
 * The MapPanel is a Swing JPanel designed to show of a Network in a graphical
 * way.
 *
 * To do this it takes a Network object and a File that contains additional
 * graphical information about the network such as Station locations and Line
 * colors.
 *
 * The MapPanel is drawn in two layers: 
 * 1) The background layer, this is drawn in the paintComponent() method. 
 * It draws the background image and all lines.
 * 2) The component layer, this is drawn by Swing itself. It consists of all
 * stations and the flags. These objects are genuine Swing components like
 * JButtons.
 *
 * Mouse event handlers are handled within the components, not in this panel.
 *
 * @author sanderjurgens
 */
public class MapPanel extends javax.swing.JPanel {

    /** Background image */
    private ImageIcon background;
    /** Whether the background is visible */
    private boolean backgroundVisible;

    /** The network that is visualized */
    private Network network;

    /** Whether the lines are visible */
    private boolean linesVisible;
    /** The set of visual representations for the lines in the network */
    private Set<VisLine> lines;
    /** The set of visual representations for the stations in the network */
    private Set<VisStation> stations;

    /** Flag marker for start of route */
    private VisMarker startMarker;
    /** Flag marker for end of route */
    private VisMarker endMarker;

    /**
     * Constructs a new MapPanel.
     */
    public MapPanel() {
        initComponents();

        // Set background layer variables 
        backgroundVisible = true;
        linesVisible = true;
        lines = new HashSet<>();

        // Set component layer variables 
        stations = new HashSet<>();
        startMarker = new VisMarker(getClass().getResource("flag_green.png"));
        endMarker = new VisMarker(getClass().getResource("flag_red.png"));

        // Add markers to the panel
        this.add(startMarker);
        this.add(endMarker);
    }

    /**
     * Load all GUI information from a resource given a network.
     *
     * @param url the URL of the resource containing the GUI information
     * @param network the network to which the GUI data is applied
     * @throws IOException if an error occurs while reading the file
     */
    public void loadResource(URL url, Network network) throws IOException {
        // Remove/hide old components
        for (VisStation vs : stations) {
            this.remove(vs);
        }
        hideMarkers();
        
        // Reset network variables
        this.network = network;
        this.background = null;
        this.lines = new HashSet<>();
        this.stations = new HashSet<>(); 
        
        // Iterate over all lines in the file
        try (BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();

                // Ignore empty lines and comments
                if (line.equals("") || line.startsWith("#")) {
                    continue;
                }

                // Split line by delimiter ":"
                StringTokenizer tokenizer = new StringTokenizer(line, ":");
                String id = tokenizer.nextToken();

                // Apply logic based on the line identifier
                switch (id) {
                    case "background" -> {
                        String bg = tokenizer.nextToken();
                        URL resource = getClass().getResource(bg);
                        // If internal resource doesn't exist, create from external file instead
                        if (resource == null) {
                            // With background path relative to given url
                            resource = new URL(url, bg);
                        }
                        background = new ImageIcon(resource);
                    }
                    case "station_gui" -> {
                        // Add VisStations to this panel
                        Station station = network.getStation(tokenizer.nextToken());
                        boolean transfer = tokenizer.nextToken().equals("transfer");
                        int x = Integer.parseInt(tokenizer.nextToken());
                        int y = Integer.parseInt(tokenizer.nextToken());
                        VisStation gui_station = new VisStation(station, transfer, x, y);
                        stations.add(gui_station);
                        this.add(gui_station);
                    }
                    case "line_gui" -> {
                        // Add VisLines
                        Line networkline = network.getLine(tokenizer.nextToken());
                        Color color = Color.decode(tokenizer.nextToken());
                        VisLine visline = new VisLine(networkline, color);
                        lines.add(visline);

                        // Add VisStations to VisLine
                        for (Station station : networkline) {
                            VisStation visstation = getVisStation(station);
                            visline.addStation(visstation);
                        }
                    }
                    case "bend" -> {
                        // Add bends in the form of dummy stations between the real stations
                        VisStation from = getVisStation(tokenizer.nextToken());
                        VisStation to = getVisStation(tokenizer.nextToken());
                        StringTokenizer coordinates = new StringTokenizer(tokenizer.nextToken(), "-");

                        // Make a list of all stations in the bend
                        List<VisStation> dummyStations = new ArrayList<>();
                        while (coordinates.hasMoreTokens()) {
                            String coordinate = coordinates.nextToken();
                            int x = Integer.parseInt(coordinate.substring(0, coordinate.indexOf(',')));
                            int y = Integer.parseInt(coordinate.substring(coordinate.indexOf(',') + 1));
                            dummyStations.add(new VisStationDummy(x, y));
                        }

                        // Give all lines the opportunity to include this bend
                        for (VisLine visline : lines) {
                            visline.addBetweenStations(from, to, dummyStations);
                        }
                    }
                    default -> {
                        // Skip line if any other identifier is found
                    }
                }
            }
        }

        repaint();
    }

    /**
     * Return the visual representation corresponding to the given line.
     *
     * @param line the line for which to return its visual representation
     * @return the visual representation corresponding to the given line
     */
    private VisLine getVisLine(Line line) {
        for (VisLine visline : lines) {
            if (visline.getLine() == line) {
                return visline;
            }
        }
        return null;
    }

    /**
     * Return the visual representation corresponding to the given station.
     *
     * @param station the station for which to return its visual representation
     * @return the visual representation corresponding to the given station
     */
    private VisStation getVisStation(Station station) {
        for (VisStation visStations : stations) {
            if (visStations.getStation() == station) {
                return visStations;
            }
        }
        return null;
    }

    /**
     * Get the visual representation of a station by its code.
     *
     * @param code the code of the station for which to return its visual
     * representation
     * @return the visual representation corresponding to the given station's
     * code
     */
    private VisStation getVisStation(String code) {
        for (VisStation station : stations) {
            if (station.getStation().getCode().equals(code)) {
                return station;
            }
        }
        return null;
    }

    /**
     * Set the visibility of the background.
     *
     * @param visible a boolean representing the visibility of the background
     */
    void setBackgroundVisible(boolean visible) {
        backgroundVisible = visible;
        repaint();
    }

    /**
     * Set the visibility of the lines (in general).
     *
     * Does not impact the hidden or visible status of single lines, only
     * prevents all lines from being drawn all together.
     *
     * @param visible a boolean representing the visibility of all the lines
     */
    void setLinesVisible(boolean visible) {
        linesVisible = visible;
        repaint();
    }

    /**
     * Set the visibility of all stations.
     *
     * @param visible a boolean representing the visibility of all the stations
     */
    void setStationsVisible(boolean visible) {
        for (VisStation station : stations) {
            station.setVisible(visible);
        }
    }

    /**
     * Hide all lines, by setting their individual visibility.
     */
    void hideAllLines() {
        for (VisLine line : lines) {
            line.showNone();
        }
        repaint();
    }

    /**
     * Show a specific line (doesn't hide other lines).
     *
     * @param line the line that should be shown
     */
    void showLine(Line line) {
        VisLine visline = getVisLine(line);
        if (visline != null) {
            visline.showAll();
            repaint();
        }
    }

    /**
     * Hide all lines except the parts that are part of the planned Route.
     *
     * @param route the planned Route
     */
    void showRoute(Route route) {
        hideAllLines();

        for (RouteSegment segment : route) {
            VisLine line = getVisLine(segment.getLine());
            if (line != null) {
                line.showSegment(segment);
            }
        }

        repaint();
    }

    /**
     * Hide both start and end flags.
     */
    void hideMarkers() {
        startMarker.setVisible(false);
        endMarker.setVisible(false);
    }

    /**
     * Put a start marker above a specific station.
     *
     * @param fromStation the Start station (origin) to be marked
     */
    void markStartStation(Station fromStation) {
        VisStation visstation = getVisStation(fromStation);
        if (visstation != null) {
            startMarker.setPosition(visstation.getPosition().x, visstation.getPosition().y);
            startMarker.setVisible(true);
            repaint();
        }
    }

    /**
     * Put an end marker above a specific station.
     *
     * @param toStation the end station (destination) to be marked
     */
    void markEndStation(Station toStation) {
        VisStation visstation = getVisStation(toStation);
        if (visstation != null) {
            endMarker.setPosition(visstation.getPosition().x, visstation.getPosition().y);
            endMarker.setVisible(true);
            repaint();
        }
    }

    /**
     * Return the dimensions of the background image.
     *
     * Used to make this Panel as large as the background. The ScrollPane uses
     * this method to determine the size of the scrollbars.
     *
     * @return The dimensions of the background, or the parent if no background
     * is defined
     */
    @Override
    public Dimension getPreferredSize() {
        if (background != null) {
            return new Dimension(background.getIconWidth(), background.getIconHeight());
        } else {
            return super.getPreferredSize();
        }
    }

    /**
     * Draw the background and all the lines of the network.
     *
     * @param graphics the Graphics context in which to draw
     */
    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D gr = (Graphics2D) graphics;

        // Draw the background
        if (backgroundVisible && background != null) {
            gr.drawImage(background.getImage(), 0, 0, this);
        }

        //Draw all the lines
        if (linesVisible) {

            // Adjust stroke to give the lines a smooth look
            Stroke oldstroke = gr.getStroke();
            gr.setStroke(new BasicStroke(4.0f, BasicStroke.CAP_ROUND,
                    BasicStroke.JOIN_ROUND));
            gr.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            // Draw lines
            for (VisLine line : lines) {
                line.draw(graphics);
            }

            // Reset stroke
            gr.setStroke(oldstroke);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setLayout(null);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
