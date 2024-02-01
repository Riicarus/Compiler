package io.github.riicarus.common.ast;

import io.github.riicarus.common.Position;

/**
 * Node of AST.
 *
 * @author Riicarus
 * @create 2024-1-15 20:18
 * @since 1.0.0
 */
public abstract class ASTNode {

    protected Position position;

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public abstract String toTreeString(int level, String prefix);

    public final void print(String prefix) {
        System.out.println(toTreeString(0, prefix));
    }

    @Override
    public String toString() {
        return toTreeString(0, "");
    }
}
