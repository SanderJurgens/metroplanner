package com.sanderjurgens.metroplanner.model;

/**
 * A standard station with a name and a code.
 *
 * @author sanderjurgens
 */
public class Station {

    /** Identification of the station */
    private final String name;
    /** The name of the station */
    private final String code;

    /**
     * Constructs a station with given name and code.
     *
     * @param code a given code
     * @param name a given name
     * @throws IllegalRequestException if empty code or name is provided
     */
    public Station(final String code, final String name) throws IllegalRequestException {
        if (name.length() == 0 || code.length() == 0) {
            throw new IllegalRequestException("Station: name or code is empty");
        }
        this.code = code;
        this.name = name;        
    }

    /**
     * Returns the name of this station.
     *
     * @return name of the line
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the code of this station.
     *
     * @return code of the line
     */
    public String getCode() {
        return code;
    }

    /**
     * Returns the name of this station.
     *
     * @return the name of this station
     */
    @Override
    public String toString() {
        return name;
    }
}
