package io.github.riicarus.common.ast.stmt;

import io.github.riicarus.common.ast.Stmt;
import io.github.riicarus.front.semantic.Checker;
import io.github.riicarus.front.semantic.types.Type;
import io.github.riicarus.front.semantic.types.type.Basic;

import java.util.List;

/**
 * "{" Stmts "}"
 *
 * @author Riicarus
 * @create 2024-1-15 20:20
 * @since 1.0.0
 */
public final class CodeBlock extends Stmt {
    private List<Stmt> stmts;

    @Override
    public Type doCheckType(Checker checker, Type outerType) {
        if (stmts != null) stmts.forEach(s -> s.checkType(checker, outerType));
        return Basic.VOID;
    }

    @Override
    public String toTreeString(int level, String prefix) {
        StringBuilder sb = new StringBuilder();
        String t = "\t".repeat(Math.max(0, level - 1));
        String link = level == 0 ? "" : "|--- ";

        if (level != 0) sb.append("\r\n");

        sb.append(prefix).append(t).append(link).append("CodeBlock");
        if (stmts != null) stmts.forEach(s -> sb.append(s.toTreeString(level + 1, prefix)));
        return sb.toString();
    }

    public List<Stmt> getStmts() {
        return stmts;
    }

    public void setStmts(List<Stmt> stmts) {
        this.stmts = stmts;
    }
}
