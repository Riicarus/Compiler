package io.github.riicarus.front.semantic.types;

/**
 * Element's type
 *
 * @author Riicarus
 * @create 2024-1-26 17:23
 * @since 1.0.0
 */
public interface Type {
    /**
     * Get the underlying type of current type
     *
     * @return Type
     */
    Type underlying();

    /**
     * Returns name of type
     *
     * @return String
     */
    String naming();

    static Type under(Type t) {
        return t.underlying();
    }
}
