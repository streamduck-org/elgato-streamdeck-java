package org.streamduck.elgato_streamdeck;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;
import org.streamduck.elgato_streamdeck.nativelib.HidApi;

@Execution(ExecutionMode.SAME_THREAD)
public class TestLib {
    static final Logger logger = LoggerFactory.getLogger(TestLib.class);

    @Test
    void testHidApi() {
        HidApi.loadLibrary();
        logger.info(() -> "owo pointer - ");
        try(HidApi hidApi = new HidApi()) {
            logger.info(() -> "owo pointer - " + hidApi);
        }
    }
}
