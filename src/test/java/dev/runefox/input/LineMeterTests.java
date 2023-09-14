package dev.runefox.input;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class LineMeterTests {
    private LineMeter meter;

    @BeforeEach
    public void beforeEach() {
        meter = new LineMeter();
    }

    @Test
    public void testLF() throws IOException {
        try (var i = Input.chars("Hello\nWorld").measure(meter)) {
            i.skip(100);
        }

        assertEquals(2, meter.line());
        assertEquals(0, meter.col());
    }

    @Test
    public void testCR() throws IOException {
        try (var i = Input.chars("Hello\rWorld").measure(meter)) {
            i.skip(100);
        }

        assertEquals(2, meter.line());
        assertEquals(0, meter.col());
    }

    @Test
    public void testCRLF() throws IOException {
        try (var i = Input.chars("Hello\r\nWorld").measure(meter)) {
            i.skip(100);
        }

        assertEquals(2, meter.line());
        assertEquals(0, meter.col());
    }

    @Test
    public void testMixed() throws IOException {
        try (var i = Input.chars("Hello\r\nWorld\nLines\rAre\n\rFun").measure(meter)) {
            i.skip(100);
        }

        assertEquals(6, meter.line());
        assertEquals(0, meter.col());
    }

    @Test
    public void testFineTunedLF() throws IOException {
        try (var i = Input.chars("aa\na").measure(meter)) {
            assertEquals(0, meter.line());
            assertEquals(0, meter.col());
            i.skip(2);
            assertEquals(0, meter.line());
            assertEquals(2, meter.col());
            i.skip(2);
            assertEquals(1, meter.line());
            assertEquals(1, meter.col());
        }
    }

    @Test
    public void testFineTunedCRLF() throws IOException {
        try (var i = Input.chars("aa\r\na").measure(meter)) {
            assertEquals(0, meter.line());
            assertEquals(0, meter.col());
            i.skip(2);
            assertEquals(0, meter.line());
            assertEquals(2, meter.col());
            i.skip(2);
            assertEquals(1, meter.line());
            assertEquals(0, meter.col());
        }
    }
}
