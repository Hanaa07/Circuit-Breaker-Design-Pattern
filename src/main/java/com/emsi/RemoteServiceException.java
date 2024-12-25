package com.emsi;

/**
 * Exception thrown when {@link RemoteService} does not respond successfully.
 */
public class RemoteServiceException extends Exception {

    public RemoteServiceException(String message) {
        super(message);
    }
}