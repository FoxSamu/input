package dev.runefox.input;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class MeasuredInput implements Input {
    private final Input source;
    private final List<Meter> meters;


    private int[] skipBuf;

    MeasuredInput(Input source, List<Meter> meters) {
        this.source = source;
        this.meters = meters;
    }

    MeasuredInput(Input source, Meter... meters) {
        this.source = source;
        this.meters = List.of(meters);
    }

    @Override
    public Input measure(Meter... meters) {
        if (meters.length == 0)
            return this;
        List<Meter> m = new ArrayList<>(this.meters);
        m.addAll(Arrays.asList(meters));
        return new MeasuredInput(source, List.copyOf(m));
    }

    private int measure(int c) throws IOException {
        for (Meter meter : meters) {
            meter.measure(c);
        }
        return c;
    }

    private void measure(int[] cs, int o, int n) throws IOException {
        for (int i = o, e = o + n; i < e; i++) {
            for (Meter meter : meters) {
                meter.measure(cs[i]);
            }
        }
    }

    @Override
    public int next() throws IOException {
        return measure(source.next());
    }

    @Override
    public int[] next(int n) throws IOException {
        int[] res = source.next(n);
        measure(res, 0, res.length);
        return res;
    }

    @Override
    public void next(int[] arr) throws IOException {
        source.next(arr);
        measure(arr, 0, arr.length);
    }

    @Override
    public void next(int[] arr, int n) throws IOException {
        source.next(arr, n);
        measure(arr, 0, n);
    }

    @Override
    public void next(int[] arr, int off, int n) throws IOException {
        source.next(arr, off, n);
        measure(arr, off, n);
    }

    @Override
    public void skip(int n) throws IOException {
        while (n > 0) {
            int skip = Math.min(n, 4096);

            if (skipBuf == null || skipBuf.length < skip) {
                skipBuf = new int[skip];
            }

            next(skipBuf);
            n -= skip;
        }
    }

    @Override
    public void close() throws IOException {
        List<Throwable> exceptions = new ArrayList<>();

        for (Meter meter : meters) {
            try {
                meter.close();
            } catch (Throwable exc) {
                exceptions.add(exc);
            }
        }

        try {
            source.close();
        } catch (Throwable exc) {
            exceptions.add(exc);
        }

        // Only now that everything is closed, we can throw again
        if (exceptions.size() > 0) {
            Throwable exc;
            if (exceptions.size() == 1) {
                exc = exceptions.get(0);
            } else {
                exc = null;
                for (Throwable e : exceptions) {
                    if (exc == null) exc = e;
                    else exc.addSuppressed(exc);
                }

                assert exc != null;
            }

            if (exc instanceof IOException ioe)
                throw ioe;
            if (exc instanceof RuntimeException re)
                throw re;
            if (exc instanceof Error err)
                throw err;

            // This can't happen because whatever gets thrown is either not
            // checked (RuntimeException or Error) or it's an IOException
            throw new AssertionError(exc);
        }
    }
}
