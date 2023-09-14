package dev.runefox.input;

import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;

class ReaderInput implements Input {
    private final Reader stream;

    private final char[] buf = new char[4096];

    ReaderInput(Reader stream) {
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
            int max = Math.min(n - i, 4096);
            int r = stream.read(buf, 0, max);

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
        int i = 0;
        while (i < n) {
            int max = Math.min(n - i, 4096);
            int r = stream.read(buf, 0, max);

            if (r < 0) {
                return;
            }
            i += r;
        }
    }

    @Override
    public void close() throws IOException {
        stream.close();
    }
}
