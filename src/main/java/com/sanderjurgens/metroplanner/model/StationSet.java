package com.sanderjurgens.metroplanner.model;

import java.util.ArrayList;

/**
 * A set of all stations.
 *
 * @author sanderjurgens
 */
public class StationSet extends ArrayList<Station> {

    /** The number of stations in the set */
    private int count;

    /**
     * Constructs an empty set of stations.
     */
    public StationSet() {
        count = 0;
    }

    /**
     * Returns the number of stations in this set.
     *
     * @return the number of stations in this set
     */
    public int getCount() {
        return count;
    }

    /**
     * Returns whether this set is empty.
     *
     * @return whether this set is empty
     */
    @Override
    public boolean isEmpty() {
        return count == 0;
    }

    /**
     * Returns the station in this set with the given code.
     *
     * @param code a given code
     * @return the station with the given code
     */
    public Station getStation(String code) {
        for (Station stop : this) {
            if (stop.getCode().equals(code)) {
                return stop;
            }
        }
        return null;
    }

    /**
     * Indicates whether it is possible to add the station to this line. The
     * station has to be unique.
     *
     * @param station a given station
     * @return whether the station is unique to this set and non-null
     */
    public boolean canAdd(Station station) {
        if (station == null) {
            return false;
        }
        for (Station s : this) {
            if (s.equals(station)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Adds a station to the set.
     *
     * @param station a given station
     * @throws IllegalRequestException if the station cannot be added
     */
    @Override
    public boolean add(Station station) throws IllegalRequestException {
        if (!canAdd(station)) {
            throw new IllegalRequestException("StationSet.add: cannot add station");
        }
        super.add(station);
        count = count + 1;
        return true;
    }

    /**
     * Returns a textual representation of all stations.
     *
     * @return a textual representation of all stations
     */
    @Override
    public String toString() {
        String output = "";
        for (Station stop : this) {
            output = output + "station:" + stop.getCode() + ":" + stop.getName() + "\n";
        }
        return output;
    }
}
