package io.github.riicarus.common.ast;

import io.github.riicarus.front.semantic.Checker;
import io.github.riicarus.front.semantic.types.Type;

/**
 * Expr node of AST.
 *
 * @author Riicarus
 * @create 2024-1-15 20:19
 * @since 1.0.0
 */
public abstract class Expr extends Stmt implements SimpleStmt {

    @Override
    public void checkStatement(Checker checker, Type retType) {
    }
}

