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
    private List<Stmt> stmts = new ArrayList<>();

    public List<Stmt> getStmts() {
        return stmts;
    }

    public void setStmts(List<Stmt> stmts) {
        this.stmts = stmts;
    }

    @Override
    public String toTreeString(int level, String prefix) {
        StringBuilder sb = new StringBuilder();
        String t = "\t".repeat(Math.max(0, level - 1));
        String link = level == 0 ? "" : "|--- ";

        if (level != 0) sb.append("\r\n");

        sb.append(prefix).append(t).append(link).append("CodeBlock");
        stmts.forEach(s -> sb.append(s.toTreeString(level + 1, prefix)));
        return sb.toString();
    }
}
