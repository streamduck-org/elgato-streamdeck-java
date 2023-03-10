package org.streamduck.elgato_streamdeck.nativelib;

import com.fizzed.jne.JNE;
import com.fizzed.jne.MemoizedRunner;

public class HidApi implements AutoCloseable {
    private static final MemoizedRunner loader = new MemoizedRunner();

    public static void loadLibrary() {
        loader.once(() -> JNE.loadLibrary("rust_module"));
    }

    private native static long newHidApi() throws RuntimeException;

    private native static void freeHidApi(long pointer);


    /** This field is being set from native library */
    protected final long hidApiPointer;

    public HidApi() throws RuntimeException {
        loadLibrary();
        hidApiPointer = newHidApi();
    }

    @Override
    public String toString() {
        return "HidApi: " + hidApiPointer;
    }

    @Override
    public void close() {
        freeHidApi(hidApiPointer);
    }
}
