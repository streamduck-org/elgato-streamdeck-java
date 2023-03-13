package org.streamduck.elgato_streamdeck.exceptions;

public class DeviceClosedException extends RuntimeException {
    public DeviceClosedException(String message) {
        super(message);
    }
}
