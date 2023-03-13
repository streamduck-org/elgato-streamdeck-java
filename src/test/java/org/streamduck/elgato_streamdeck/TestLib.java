package org.streamduck.elgato_streamdeck;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;

import java.util.List;
import java.util.Optional;

@Execution(ExecutionMode.SAME_THREAD)
public class TestLib {
    static final Logger logger = LoggerFactory.getLogger(TestLib.class);

    @Test
    void testHidApi() {
        StreamDeck streamDeck = new StreamDeck();

        List<DeviceDescriptor> devices = streamDeck.listDevices();

        Optional<DeviceDescriptor> first = devices.stream().findFirst();

        if(first.isPresent()) {
            DeviceDescriptor deviceDescriptor = first.get();

            StreamDeckDevice device = streamDeck.connectToDevice(deviceDescriptor);

            device.reset();
            device.setBrightness(30);
        }
    }
}
