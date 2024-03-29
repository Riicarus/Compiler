package io.github.riicarus.common.ast;

import io.github.riicarus.common.Position;
import io.github.riicarus.front.semantic.Checker;
import io.github.riicarus.front.semantic.types.Type;

/**
 * Node of AST.
 *
 * @author Riicarus
 * @create 2024-1-15 20:18
 * @since 1.0.0
 */
public abstract class ASTNode {

    protected Position position;
    protected boolean reachable;

    /**
     * Check node's type
     *
     * @param checker Check
     * @param outerType Type, the outer type, need check the inner type
     * @return Type, the inner type
     */
    public abstract Type doCheckType(Checker checker, Type outerType);

    /**
     * Check if statement needs return (value)
     *
     * @param checker Checker
     * @param retType Type, return value type
     */
    public abstract void checkStatement(Checker checker, Type retType);

    public final Type checkType(Checker checker, Type outerType) {
        if (this instanceof Stmt s && s.getScope() != null) checker.enter(s.getScope().getName());
        Type t = doCheckType(checker, outerType);
        if (this instanceof Stmt s && s.getScope() != null) checker.exit();

        return t;
    }

    public abstract String toTreeString(int level, String prefix);

    public final void print(String prefix) {
        System.out.println(toTreeString(0, prefix));
    }

    @Override
    public String toString() {
        return toTreeString(0, "");
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public boolean isReachable() {
        return reachable;
    }

    public void setReachable(boolean reachable) {
        this.reachable = reachable;
    }
}
