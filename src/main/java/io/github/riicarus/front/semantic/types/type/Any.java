package io.github.riicarus.front.semantic.types.type;

import io.github.riicarus.front.semantic.types.Type;

/**
 * Any type
 *
 * @author Riicarus
 * @create 2024-2-4 17:54
 * @since 1.0.0
 */
public class Any implements Type {

    public static final Any ANY = new Any();

    private Any() {}

    @Override
    public Type underlying() {
        return this;
    }

    @Override
    public String naming() {
        return "any";
    }

    @Override
    public String toString() {
        return naming();
    }
}
