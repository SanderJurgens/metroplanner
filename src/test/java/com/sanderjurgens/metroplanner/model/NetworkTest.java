package com.sanderjurgens.metroplanner.model;

import java.io.IOException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * A set of unit tests for the Network class.
 *
 * @author sanderjurgens
 */
public class NetworkTest {

    /**
     * Unit test for a Network.
     */
    @Test
    public void testNetwork() {    
        // Empty network
        try {
            Network network = new Network(getClass().getResource("empty.network"));
            Assertions.assertEquals("", network.getName(), 
                    "The name is not correct");
            Assertions.assertEquals(0, network.getStationSet().getCount(), 
                    "A station was added");   
            Assertions.assertEquals(0, network.getLineSet().getCount(), 
                    "A line was added");
                 
        } catch (IOException e) {
            Assertions.fail("Exception " + e + " should not be thrown");
        }
        
        // Minimal network
        try {
            Network network = new Network(getClass().getResource("minimal.network"));
            Assertions.assertEquals("Minimal network", network.getName(), 
                    "The name is not correct");
            Assertions.assertEquals(1, network.getStationSet().getCount(),
                    "Station was not added");
            Assertions.assertEquals("A", network.getStationSet().getStation("VBA").getName(),
                    "Name or code of the station is not correct");
            Assertions.assertEquals(1, network.getLineSet().getCount(),
                    "Line was not added");           
            Assertions.assertTrue(!network.getLineSet().getLine("Green").isCircular(),
                    "Line is not circular");
            Assertions.assertTrue(network.getLineSet().getLine("Green").isOneWay(),
                    "Line is one way");           
            Assertions.assertEquals(1, network.getLineSet().getLine("Green").getCount(),
                    "Station was not added to the line");            
        } catch (IOException e) {
            Assertions.fail("Exception " + e + " should not be thrown");
        }
        
        // Paris network
        try {
            Network network = new Network(getClass().getResource("paris.network"));
            Assertions.assertEquals("Paris", network.getName(), 
                    "The name is not correct");
            Assertions.assertEquals(182, network.getStationSet().getCount(), 
                    "Not all stations were added");
            int[] numStations = {25, 25, 25, 4, 26, 22, 27, 28, 18, 5, 9};
            int i = 0;
            for (Line line : network.getLineSet()) {
                Assertions.assertEquals(numStations[i], line.getCount(),
                        "Not enough stations on line " + line.getCode());
                i = i + 1;
            }
        } catch (IOException e) {
            Assertions.fail("Exception " + e + " should not be thrown");
        }        
    }

    /**
     * Unit test of toString methods, of class Network.
     */
    @Test
    public void testToString() {
        // Empty network
        try {
            Network network = new Network(getClass().getResource("empty.network"));
            String expected = "name:\n";
            Assertions.assertEquals(expected, network.toString(), 
                    "Empty network string was not correct");                 
        } catch (IOException e) {
            Assertions.fail("Exception " + e + " should not be thrown");
        }
        
        // Minimal network
        try {
            Network network = new Network(getClass().getResource("minimal.network"));
            String expected = "name:" + network.getName() + "\n";
            expected = expected + "station:VBA:A" + "\n";
            expected = expected + "line:Green:0:1:VBA" + "\n";
            Assertions.assertEquals(expected, network.toString(), 
                    "Minimal network string was not correct");                 
        } catch (IOException e) {
            Assertions.fail("Exception " + e + " should not be thrown");
        }
        
        // Paris network
        try {
            Network network = new Network(getClass().getResource("paris.network"));
            String expected = "name:" + network.getName() + "\n";
            for (Station station : network.getStationSet()) {
                expected = expected + "station:" + station.getCode() + ":"
                    + station.getName() + "\n";
            }
            for (Line line : network.getLineSet()) {
                String circular = "0";
                String oneway = "0";
                if (line.isCircular()) {
                    circular = "1";
                }
                if (line.isOneWay()) {
                    oneway = "1";
                }
                expected = expected + "line:" + line.getCode() + ":" + circular + ":" + oneway + ":";
                if (!line.isEmpty()) {
                    expected = expected + line.getStop(0).getCode();
                    for (int i=1; i<line.getCount(); i=i+1) {
                        expected = expected + "-" + line.getStop(i).getCode();
                    }
                }
                expected = expected + "\n";
            }
            Assertions.assertEquals(expected, network.toString(), 
                    "Paris network string was not correct");                 
        } catch (IOException e) {
            Assertions.fail("Exception " + e + " should not be thrown");
        }
    }
}
