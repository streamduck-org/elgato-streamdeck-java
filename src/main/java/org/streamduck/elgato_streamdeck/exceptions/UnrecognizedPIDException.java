package org.streamduck.elgato_streamdeck.exceptions;

public class UnrecognizedPIDException extends RuntimeException {
    public UnrecognizedPIDException(String message) {
        super(message);
    }
}
