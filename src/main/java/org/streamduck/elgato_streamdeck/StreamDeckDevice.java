package org.streamduck.elgato_streamdeck;

import org.streamduck.elgato_streamdeck.exceptions.DeviceClosedException;
import org.streamduck.elgato_streamdeck.exceptions.HIDException;

public class StreamDeckDevice {
    private final StreamDeckInterface device;

    private boolean deviceAlive = true;

    protected StreamDeckDevice(StreamDeckInterface device) {
        this.device = device;
    }

    private void checkIfClosed() throws DeviceClosedException {
        if(!deviceAlive) {
            throw new DeviceClosedException("Device is already closed");
        }
    }

    private void closeDevice() throws DeviceClosedException {
        deviceAlive = false;
        device.close();
        throw new DeviceClosedException("Device is closed");
    }

    public void setBrightness(int brightness) throws RuntimeException {
        checkIfClosed();

        try {
            StreamDeckInterface.setBrightness(device.getStreamDeckPointer(), (byte) brightness);
        } catch (HIDException e) {
            closeDevice();
        }
    }

    public void reset() throws RuntimeException {
        checkIfClosed();

        try {
            StreamDeckInterface.reset(device.getStreamDeckPointer());
        } catch (HIDException e) {
            closeDevice();
        }
    }

    public void close() throws DeviceClosedException {
        checkIfClosed();
        deviceAlive = false;
        device.close();
    }
}
