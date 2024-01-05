package com.sanderjurgens.metroplanner.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.StringTokenizer;

/**
 * An overall network that contains and regulates all lines and stations. It
 * also reads and imports a network from a file.
 *
 * @author sanderjurgens
 */
public class Network {

    /** The URL of the resource that the network is based on */
    private final URL url;

    /** The name of the network */
    private String name;

    /** The sets of stations in the network */
    private StationSet staSet;
    /** The sets of lines in the network */
    private LineSet lineSet;    

    /**
     * Constructs a network based on a file
     *
     * @param url the URL of the resource containing the network
     * @throws IOException if an error occurs while reading the file
     */
    public Network(final URL url) throws IOException {
        this.url = url;
        name = "";        
        staSet = new StationSet();
        lineSet = new LineSet();

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
                    case "name" -> {
                        // Add the name to the network
                        name = tokenizer.nextToken();
                    }
                    case "station" -> {
                        // Add a station to the network
                        staSet.add(new Station(tokenizer.nextToken(), tokenizer.nextToken()));
                    }
                    case "line" -> {
                        // Add a line to the network
                        String code = tokenizer.nextToken();
                        String circular = tokenizer.nextToken();
                        String oneWay = tokenizer.nextToken();

                        // Convert String to Boolean
                        boolean isCircular = false;
                        boolean isOneWay = false;
                        if (circular.equals("1")) {
                            isCircular = true;
                        }
                        if (oneWay.equals("1")) {
                            isOneWay = true;
                        }
                        Line aLine = new Line(code, isCircular, isOneWay);

                        // Add stations to the new line
                        String lineRest = tokenizer.nextToken();
                        StringTokenizer coordinates = new StringTokenizer(lineRest, "-");
                        while (coordinates.hasMoreTokens()) {
                            aLine.add(staSet.getStation(coordinates.nextToken()));
                        }
                        lineSet.add(aLine);
                    }
                    default -> {
                        // Skip line if any other identifier is found
                    }
                }
            }
        }
    }

    /**
     * Returns the name of the network.
     *
     * @return the name of the network
     */
    public String getName() {
        return name;
    }
    
    /**
     * Returns the set of all stations.
     *
     * @return the set of all stations
     */
    public StationSet getStationSet() {
        return staSet;
    }

    /**
     * Returns the set of all lines.
     *
     * @return the set of all lines
     */
    public LineSet getLineSet() {
        return lineSet;
    }

    /**
     * Returns the station with the given code.
     *
     * @param code a given code
     * @return the station with the given code
     */
    public Station getStation(String code) {
        return staSet.getStation(code);
    }
    
    /**
     * Returns the line with the given code.
     *
     * @param code a given code
     * @return the line with the given code
     */
    public Line getLine(String code) {
        return lineSet.getLine(code);
    }

    /**
     * Returns a textual representation of this network.
     *
     * @return a textual representation of this network
     */
    @Override
    public String toString() {
        return "name:" + name + "\n" + staSet.toString() + lineSet.toString();
    }
}
