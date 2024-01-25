package io.github.riicarus.common.ast;

import java.util.ArrayList;
import java.util.List;

/**
 * Stmt | e
 *
 * @author Riicarus
 * @create 2024-1-16 13:40
 * @since 1.0.0
 */
public class Program extends ASTNode {

    private final List<Stmt> stmts = new ArrayList<>();

    public void setStmts(List<Stmt> stmts) {
        this.stmts.clear();
        this.stmts.addAll(stmts);
    }

    public List<Stmt> getStmts() {
        return stmts;
    }
}
