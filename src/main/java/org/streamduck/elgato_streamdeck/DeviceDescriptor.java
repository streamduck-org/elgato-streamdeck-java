package org.streamduck.elgato_streamdeck;

import org.streamduck.elgato_streamdeck.info.DeviceKind;

/**
 * Describes the Stream Deck device
 * @param kind Kind of the device
 * @param serial Serial number of the device
 */
public record DeviceDescriptor(DeviceKind kind, String serial) {}
