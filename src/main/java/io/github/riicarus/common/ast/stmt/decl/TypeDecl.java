package io.github.riicarus.common.ast.stmt.decl;

import io.github.riicarus.common.ast.Decl;
import io.github.riicarus.front.semantic.types.Type;

/**
 * Type
 *
 * @author Riicarus
 * @create 2024-1-15 21:46
 * @since 1.0.0
 */
public abstract class TypeDecl extends Decl {

    /**
     * Get type info from decl.
     *
     * @return Type
     */
    public abstract Type type();

}
