package dev.runefox.input;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * An ordered stream of nonnegative integers of arbitrary length. Values from this stream can be interpreted as bytes,
 * chars or code points, but are not limited to these formats. The only requirement is that values should be 0 or
 * positive. The value -1 is specifically treated as the end of the stream (EOF).
 */
public interface Input extends Closeable {
    static int EOF = -1;

    int next() throws IOException;
    int[] next(int n) throws IOException;
    void next(int[] arr) throws IOException;
    void next(int[] arr, int n) throws IOException;
    void next(int[] arr, int off, int n) throws IOException;
    void skip(int n) throws IOException;

    default Input measure(Meter... meters) {
        if (meters.length == 0)
            return this;
        return new MeasuredInput(this, meters);
    }

    default Input codePoints() {
        return new CodePointInput(this);
    }

    static Input open(InputStream stream) {
        return new StreamInput(stream);
    }

    static Input read(Reader stream) {
        return new ReaderInput(stream);
    }

    static Input bytes(byte[] array) {
        return new ArrayInput(array, 0, array.length);
    }

    static Input bytes(byte[] array, int off, int len) {
        return new ArrayInput(array, off, len);
    }

    static Input chars(char[] array) {
        return new ArrayInput(array, 0, array.length);
    }

    static Input chars(char[] array, int off, int len) {
        return new ArrayInput(array, off, len);
    }

    static Input chars(CharSequence array) {
        return new ArrayInput(array, 0, array.length());
    }

    static Input chars(CharSequence array, int off, int len) {
        return new ArrayInput(array, off, len);
    }

    static Input codePoints(CharSequence array) {
        int[] cps = array.codePoints().toArray();
        return new ArrayInput(cps, 0, cps.length);
    }

    static Input codePoints(CharSequence array, int off, int len) {
        int[] cps = array.codePoints().toArray();
        return new ArrayInput(cps, off, len);
    }

    static Input open(URL url) throws IOException {
        return Input.open(url.openStream());
    }

    static Input open(Path path) throws IOException {
        return Input.open(Files.newInputStream(path));
    }

    static Input open(File file) throws FileNotFoundException {
        return Input.open(new FileInputStream(file));
    }

    static Input open(String file) throws FileNotFoundException {
        return Input.open(new FileInputStream(file));
    }

    static Input read(URL url) throws IOException {
        return Input.read(new BufferedReader(new InputStreamReader(url.openStream())));
    }

    static Input read(Path path) throws IOException {
        return Input.read(Files.newBufferedReader(path));
    }

    static Input read(File file) throws FileNotFoundException {
        return Input.read(new BufferedReader(new FileReader(file)));
    }

    static Input read(String file) throws FileNotFoundException {
        return Input.read(new BufferedReader(new FileReader(file)));
    }

    static Input read(URL url, Charset charset) throws IOException {
        return Input.read(new BufferedReader(new InputStreamReader(url.openStream(), charset)));
    }

    static Input read(Path path, Charset charset) throws IOException {
        return Input.read(Files.newBufferedReader(path, charset));
    }

    static Input read(File file, Charset charset) throws IOException {
        return Input.read(new BufferedReader(new FileReader(file, charset)));
    }

    static Input read(String file, Charset charset) throws IOException {
        return Input.read(new BufferedReader(new FileReader(file, charset)));
    }

    static Input empty() {
        return NullInput.NULL;
    }
}
