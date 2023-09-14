package dev.runefox.input;

import org.junit.jupiter.api.AssertionFailureBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;

public class InputTests {
    private static void assertStream(Input input, int... chars) throws IOException {
        int[] read = input.next(chars.length);

        Assertions.assertEquals(chars.length, read.length, "Required length differs");

        for (int i = 0, l = chars.length; i < l; i++) {
            int exp = chars[i];
            int act = read[i];

            if (exp != act) {
                throw AssertionFailureBuilder.assertionFailure()
                                             .reason("stream contents differ at index [" + i + "]")
                                             .expected(exp + (exp >= 0 ? " ('" + (char) exp + "')" : ""))
                                             .actual(act + (act >= 0 ? " ('" + (char) act + "')" : ""))
                                             .build();
            }
        }
    }

    private static void assertStream(Input input, String str) throws IOException {
        int[] chars = new int[str.length()];
        for (int i = 0, l = chars.length; i < l; i++) {
            chars[i] = str.charAt(i);
        }
        assertStream(input, chars);
    }

    @Test
    public void testString() throws IOException {
        try (var i = Input.chars("Hello world")) {
            assertStream(i, "Hello world");
            assertStream(i, -1);
        }
    }

    @Test
    public void testChars() throws IOException {
        try (var i = Input.chars("Hello world".toCharArray())) {
            assertStream(i, "Hello world");
            assertStream(i, -1);
        }
    }

    @Test
    public void testCodePointsRegular() throws IOException {
        try (var i = Input.codePoints("Hello world")) {
            assertStream(i, "Hello world");
            assertStream(i, -1);
        }
    }

    @Test
    public void testCodePointsWeird() throws IOException {
        try (var i = Input.codePoints(new String(new int[] {0x38192, 2, 0, 0x88888}, 0, 4))) {
            assertStream(i, 0x38192, 2, 0, 0x88888);
            assertStream(i, -1);
        }
    }

    @Test
    public void testCodePointsManual() throws IOException {
        try (var i = Input.chars(new String(new int[] {0x38192, 2, 0, 0x88888}, 0, 4)).codePoints()) {
            assertStream(i, 0x38192, 2, 0, 0x88888);
            assertStream(i, -1);
        }
    }

    @Test
    public void testBytes() throws IOException {
        try (var i = Input.bytes("Hello world".getBytes(StandardCharsets.UTF_8))) {
            assertStream(i, "Hello world");
            assertStream(i, -1);
        }
    }

    @Test
    public void testStream() throws IOException {
        try (var i = Input.open(new ByteArrayInputStream("Hello world".getBytes(StandardCharsets.UTF_8)))) {
            assertStream(i, "Hello world");
            assertStream(i, -1);
        }
    }

    @Test
    public void testReader() throws IOException {
        try (var i = Input.read(new StringReader("Hello world"))) {
            assertStream(i, "Hello world");
            assertStream(i, -1);
        }
    }

    @Test
    public void testNull() throws IOException {
        try (var i = Input.empty()) {
            assertStream(i, -1);
        }
    }
}
