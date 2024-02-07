package io.github.riicarus.common.ast.stmt.ctrl;

import io.github.riicarus.common.ast.Stmt;
import io.github.riicarus.front.semantic.Checker;
import io.github.riicarus.front.semantic.types.Type;
import io.github.riicarus.front.semantic.types.type.Basic;

/**
 * ";"
 *
 * @author Riicarus
 * @create 2024-1-25 17:09
 * @since 1.0.0
 */
public class EmptyStmt extends Stmt {

    @Override
    public Type doCheckType(Checker checker, Type outerType) {
        return Basic.VOID;
    }

    @Override
    public void checkStatement(Checker checker, Type retType) {
    }

    @Override
    public String toTreeString(int level, String prefix) {
        StringBuilder sb = new StringBuilder();
        String t = "\t".repeat(Math.max(0, level - 1));
        String link = level == 0 ? "" : "|--- ";

        if (level != 0) sb.append("\r\n");

        sb.append(prefix).append(t).append(link).append("Empty");
        return sb.toString();
    }

}
