package dev.runefox.input;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

class StreamInput implements Input {
    private final InputStream stream;

    private final byte[] buf = new byte[4096];

    StreamInput(InputStream stream) {
        this.stream = stream;
    }

    @Override
    public int next() throws IOException {
        return stream.read();
    }

    @Override
    public int[] next(int n) throws IOException {
        int[] arr = new int[n];
        next(arr);
        return arr;
    }

    @Override
    public void next(int[] arr) throws IOException {
        next(arr, 0, arr.length);
    }

    @Override
    public void next(int[] arr, int n) throws IOException {
        next(arr, 0, n);
    }

    @Override
    public void next(int[] arr, int off, int n) throws IOException {
        int i = 0;
        while (i < n) {
            int r = stream.read(buf);

            if (r < 0) {
                Arrays.fill(arr, off + i, off + n, -1);
                return;
            }

            int x = 0;
            while (x < r) {
                arr[off + i] = buf[x];
                x++;
                i++;
            }
        }
    }

    @Override
    public void skip(int n) throws IOException {
        try {
            stream.skipNBytes(n);
        } catch (EOFException ignored) {
            // We don't need this
        }
    }

    @Override
    public void close() throws IOException {
        stream.close();
    }
}
