package dev.runefox.input;

import java.io.Closeable;
import java.io.IOException;

public interface Meter extends Closeable {
    void measure(int c) throws IOException;
}
