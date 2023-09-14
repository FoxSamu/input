package dev.runefox.input;

import java.util.Arrays;

class ArrayInput implements Input {
    private final int[] array;
    private final int limit;
    private int pos;

    ArrayInput(byte[] array, int off, int len) {
        int[] arr = this.array = new int[len];
        for (int i = 0; i < len; i++) {
            arr[i] = array[i + off] & 0xFF;
        }
        this.limit = len;
    }

    ArrayInput(char[] array, int off, int len) {
        int[] arr = this.array = new int[len];
        for (int i = 0; i < len; i++) {
            arr[i] = array[i + off] & 0xFFFF;
        }
        this.limit = len;
    }

    ArrayInput(int[] array, int off, int len) {
        this.array = array;
        this.pos = off;
        this.limit = len;
    }

    ArrayInput(CharSequence array, int off, int len) {
        int[] arr = this.array = new int[len];
        for (int i = 0; i < len; i++) {
            arr[i] = array.charAt(i + off) & 0xFFFF;
        }
        this.limit = len;
    }

    @Override
    public int next() {
        if (pos >= limit) return EOF;
        return array[pos++];
    }

    @Override
    public int[] next(int n) {
        int[] arr = new int[n];
        next(arr);
        return arr;
    }

    @Override
    public void next(int[] arr) {
        next(arr, 0, arr.length);
    }

    @Override
    public void next(int[] arr, int n) {
        next(arr, 0, n);
    }

    @Override
    public void next(int[] arr, int off, int n) {
        int rem = limit - pos;
        if (rem <= 0) {
            Arrays.fill(arr, off, off + n, -1);
            return;
        }

        int copy = Math.min(rem, n);
        System.arraycopy(array, pos, arr, off, copy);
        if (rem < n) {
            Arrays.fill(arr, off + rem, off + n, -1);
        }

        skip(n);
    }

    @Override
    public void skip(int n) {
        pos += n;
        if (pos > limit) pos = limit;
    }

    @Override
    public void close() {
        // N/A
    }
}
