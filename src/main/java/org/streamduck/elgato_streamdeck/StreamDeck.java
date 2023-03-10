package org.streamduck.elgato_streamdeck;

import org.streamduck.elgato_streamdeck.nativelib.HidApi;

/**
 * Main class of the library, holds the HIDAPI instance, only one instance should exist
 */
public class StreamDeck {
    private static StreamDeck instance = null;

    /**
     * Retrieves existing instance, or creates new one if it doesn't exist yet
     * @return Object that can be used to interact with Stream Deck devices
     * @throws RuntimeException If HIDAPI couldn't get initialized
     */
    public static StreamDeck instance() throws RuntimeException {
        if (instance == null) {
            instance = new StreamDeck();
        }

        return instance;
    }

    private final HidApi hidApi;

    private StreamDeck() throws RuntimeException {
        hidApi = new HidApi();
    }

//    public List<DeviceDescriptor> listDevices() {
//
//    }
}
