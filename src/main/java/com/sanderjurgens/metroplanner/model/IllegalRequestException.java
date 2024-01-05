package com.sanderjurgens.metroplanner.model;

/**
 * An unchecked exception to signal an illegal request.
 *
 * @author sanderjurgens
 */
public class IllegalRequestException extends RuntimeException {

    /**
     * Default constructor.
     */
    public IllegalRequestException() {
        super();
    }

    /**
     * A constructor that takes a message.
     *
     * @param s message
     */
    public IllegalRequestException(String s) {
        super(s);
    }
}