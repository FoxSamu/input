package dev.runefox.input;

import java.util.Arrays;

enum NullInput implements Input {
    NULL;

    @Override
    public int next() {
        return -1;
    }

    @Override
    public int[] next(int n) {
        int[] arr = new int[n];
        Arrays.fill(arr, -1);
        return arr;
    }

    @Override
    public void next(int[] arr) {
        Arrays.fill(arr, -1);
    }

    @Override
    public void next(int[] arr, int n) {
        Arrays.fill(arr, 0, n, -1);
    }

    @Override
    public void next(int[] arr, int off, int n) {
        Arrays.fill(arr, off, off + n, -1);
    }

    @Override
    public void skip(int n) {
    }

    @Override
    public void close() {
    }
}
