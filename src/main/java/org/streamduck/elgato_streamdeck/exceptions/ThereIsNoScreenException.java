package org.streamduck.elgato_streamdeck.exceptions;

public class ThereIsNoScreenException extends RuntimeException {
    public ThereIsNoScreenException(String message) {
        super(message);
    }
}
