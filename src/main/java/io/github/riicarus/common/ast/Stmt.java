package io.github.riicarus.common.ast;

import io.github.riicarus.front.semantic.types.Scope;

/**
 * Statement
 *
 * @author Riicarus
 * @create 2024-1-15 22:08
 * @since 1.0.0
 */
public abstract class Stmt extends ASTNode {

    protected Scope scope;

    public Scope getScope() {
        return scope;
    }

    public void setScope(Scope scope) {
        this.scope = scope;
    }
}
