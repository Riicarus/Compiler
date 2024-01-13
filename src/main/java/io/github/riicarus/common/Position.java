package io.github.riicarus.common;

/**
 * Position of symbol
 *
 * @author Riicarus
 * @create 2024-1-12 14:04
 * @since 1.0.0
 */
public record Position(String filename, int offset, int row, int col) {
    @Override
    public String toString() {
        return filename + ':' + row + ':' + col;
    }
}
