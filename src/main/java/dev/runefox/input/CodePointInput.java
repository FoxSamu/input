package dev.runefox.input;

import java.io.IOException;

class CodePointInput implements Input {
    private final Input source;
    private int lookahead = -2;

    CodePointInput(Input source) {
        this.source = source;
    }

    @Override
    public Input codePoints() {
        return this;
    }

    private int peek() throws IOException {
        if (lookahead == -2)
            lookahead = source.next();
        return lookahead;
    }

    private int shift() throws IOException {
        int o = peek();
        lookahead = source.next();
        return o;
    }

    @Override
    public int next() throws IOException {
        int hi = shift();
        if (hi < 0 || !Character.isHighSurrogate((char) hi))
            return hi;

        int lo = peek();
        if (lo < 0 || !Character.isLowSurrogate((char) lo))
            return hi;

        return Character.toCodePoint((char) hi, (char) shift());
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
        for (int i = 0; i < n; i++) {
            arr[off + i] = next();
        }
    }

    @Override
    public void skip(int n) throws IOException {
        while (n > 0) {
            next();
            n--;
        }
    }

    @Override
    public void close() throws IOException {
        source.close();
    }
}
