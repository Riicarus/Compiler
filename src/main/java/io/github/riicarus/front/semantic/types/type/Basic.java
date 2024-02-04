package io.github.riicarus.front.semantic.types.type;

import io.github.riicarus.front.semantic.types.Type;

import java.util.Map;
import java.util.Set;

/**
 * BasicType
 *
 * @author Riicarus
 * @create 2024-1-26 17:30
 * @since 1.0.0
 */
public enum Basic implements Type {
    INT("int"),
    FLOAT("float"),
    BOOL("bool"),
    CHAR("char"),
    STRING("string"),
    VOID("void");

    private final String name;

    Basic(String name) {
        this.name = name;
    }

    @Override
    public Type underlying() {
        return this;
    }

    @Override
    public String naming() {
        return name;
    }

    public static final Map<Basic, Set<Basic>> CAST_MAP = Map.of(
            INT, Set.of(INT, FLOAT, BOOL, CHAR),
            FLOAT, Set.of(INT, FLOAT, BOOL),
            BOOL, Set.of(INT, FLOAT, BOOL, STRING),
            CHAR, Set.of(INT, FLOAT, CHAR, STRING),
            STRING, Set.of(STRING),
            VOID, Set.of(VOID)
    );

    public static boolean canCast(Type from, Type to) {
        if (from instanceof Basic && to instanceof Basic) {
            return CAST_MAP.get(from).contains(to);
        }

        return false;
    }


    @Override
    public String toString() {
        return naming();
    }
}
