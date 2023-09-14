package dev.runefox.input;

import java.io.IOException;

/**
 * A line meter cuts lines on common line breaks and keeps track of the position in readable content (i.e. the line and
 * column). It breaks lines when encountering the following line breaks: LF ({@code \n}), CR ({@code \r}) or CRLF
 * ({@code \r\n}). It also counts the last line of input between a line break and the EOF.
 */
public class LineMeter implements Meter {
    private int line, col;
    private State state = State.NONE;

    private State newline(State s) {
        line++;
        col = 0;
        return s;
    }

    private State skip(State s) {
        return s;
    }

    private State count(State s) {
        col++;
        return s;
    }

    @Override
    public void measure(int c) throws IOException {
        state = switch (state) {
            case NONE, CRLF, LF -> switch (c) {
                case '\n' -> newline(State.LF);
                case '\r' -> newline(State.CR);
                case Input.EOF -> newline(State.EOF);
                default -> count(State.NONE);
            };

            case CR -> switch (c) {
                case '\n' -> skip(State.CRLF);
                case '\r' -> newline(State.CR);
                case Input.EOF -> newline(State.EOF);
                default -> count(State.NONE);
            };

            case EOF -> switch (c) {
                case Input.EOF -> skip(State.EOF);
                default -> throw new IOException("Input after EOF");
            };
        };
    }

    /**
     * Returns the line number, the first line being 0.
     *
     * @return The line number.
     */
    public int line() {
        return line;
    }

    /**
     * Returns the column number, the first column being 0.
     *
     * @return The column number.
     */
    public int col() {
        return col;
    }

    @Override
    public void close() {
        if (state != State.EOF) {
            state = newline(State.EOF);
        }
    }

    private enum State {
        NONE,
        LF,
        CR,
        CRLF,
        EOF
    }
}
