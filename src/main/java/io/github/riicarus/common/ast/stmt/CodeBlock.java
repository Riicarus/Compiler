package io.github.riicarus.common.ast.stmt;

import io.github.riicarus.common.ast.Stmt;

import java.util.ArrayList;
import java.util.List;

/**
 * "{" Stmts "}"
 *
 * @author Riicarus
 * @create 2024-1-15 20:20
 * @since 1.0.0
 */
public final class CodeBlock extends Stmt {
    private final List<Stmt> stmts = new ArrayList<>();

    public List<Stmt> getStmts() {
        return stmts;
    }

    public void setStmts(List<Stmt> stmts) {
        this.stmts.clear();
        this.stmts.addAll(stmts);
    }
}
