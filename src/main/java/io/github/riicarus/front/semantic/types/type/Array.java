package io.github.riicarus.front.semantic.types.type;

import io.github.riicarus.front.semantic.types.Type;

/**
 * Array type
 *
 * @author Riicarus
 * @create 2024-1-26 17:41
 * @since 1.0.0
 */
public class Array implements Type {

    private Type eleType;
    private int size;

    @Override
    public Type underlying() {
        return eleType;
    }

    @Override
    public String naming() {
        return String.format("Array[%d]", size);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Array array)) return false;

        return getEleType() != null ? getEleType().equals(array.getEleType()) : array.getEleType() == null;
    }

    @Override
    public int hashCode() {
        return getEleType() != null ? getEleType().hashCode() : 0;
    }

    @Override
    public String toString() {
        return String.format("{%s}[%d]", eleType, size);
    }

    public Type getEleType() {
        return eleType;
    }

    public void setEleType(Type eleType) {
        this.eleType = eleType;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
