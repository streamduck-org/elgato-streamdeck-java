package org.streamduck.elgato_streamdeck;

import org.streamduck.elgato_streamdeck.info.DeviceKind;

import java.util.List;

class StreamDeckInterface implements AutoCloseable {
    protected native static List<DeviceDescriptor> listDevices(long hidApi);

    private native static long connectStreamDeck(long hidApi, DeviceKind kind, String serial) throws RuntimeException;

    private native static void freeStreamDeck(long streamDeck);

    protected native static void setBrightness(long streamDeck, byte brightness) throws RuntimeException;

    protected native static void reset(long streamDeck) throws RuntimeException;

    private final long streamDeckPointer;

    protected synchronized long getStreamDeckPointer() {
        return streamDeckPointer;
    }

    public StreamDeckInterface(long hidApi, DeviceKind kind, String serial) throws RuntimeException {
        HidApi.loadLibrary();
        streamDeckPointer = connectStreamDeck(hidApi, kind, serial);
    }

    @Override
    public void close() {
        freeStreamDeck(streamDeckPointer);
    }
}
