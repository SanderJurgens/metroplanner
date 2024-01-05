package com.sanderjurgens.metroplanner.planner;

import com.sanderjurgens.metroplanner.model.Network;
import com.sanderjurgens.metroplanner.model.Station;

/**
 *
 * @author sanderjurgens
 */
public abstract class Planner {
    
    /** The network for which routes can be planned */
    protected final Network network; 

    /**
     * Constructs a planner for a given network.
     *
     * @param network a given network
     */
    public Planner(Network network) {
        this.network = network;
    }

    /**
     * Finds a route from origin to destination.
     *
     * @param from origin
     * @param to destination
     * @return a route from origin to destination
     */
    public abstract Route findRoute(Station from, Station to);
}
