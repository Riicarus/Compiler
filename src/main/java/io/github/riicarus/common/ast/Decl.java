package io.github.riicarus.common.ast;

/**
 * Decl stmt
 *
 * @author Riicarus
 * @create 2024-1-16 13:57
 * @since 1.0.0
 */
public abstract class Decl extends Stmt {

    protected boolean isConst = false;

    public boolean isConst() {
        return isConst;
    }

    public void setConst(boolean isConst) {
        this.isConst = isConst;
    }
}
