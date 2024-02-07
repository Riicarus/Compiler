package io.github.riicarus.common.ast;

import io.github.riicarus.front.semantic.Checker;
import io.github.riicarus.front.semantic.types.Type;

/**
 * Decl stmt
 *
 * @author Riicarus
 * @create 2024-1-16 13:57
 * @since 1.0.0
 */
public abstract class Decl extends Stmt {

    @Override
    public void checkStatement(Checker checker, Type retType) {
    }
}
