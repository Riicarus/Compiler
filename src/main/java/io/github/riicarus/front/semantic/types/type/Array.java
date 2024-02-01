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

    public Array(Type eleType) {
        this.eleType = eleType;
    }

    @Override
    public Type underlying() {
        return this;
    }

    @Override
    public String naming() {
        return "Array";
    }

    public Type getEleType() {
        return eleType;
    }

    public void setEleType(Type eleType) {
        this.eleType = eleType;
    }
}
