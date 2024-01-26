package io.github.riicarus.common.ast.stmt.ctrl;

import io.github.riicarus.common.ast.Ctrl;

/**
 * break;
 *
 * @author Riicarus
 * @create 2024-1-19 11:53
 * @since 1.0.0
 */
public final class BreakStmt extends Ctrl {

    @Override
    public String toTreeString(int level, String prefix) {
        StringBuilder sb = new StringBuilder();
        String t = "\t".repeat(Math.max(0, level - 1));
        String link = level == 0 ? "" : "|--- ";

        if (level != 0) sb.append("\r\n");

        sb.append(prefix).append(t).append(link).append("Break");
        return sb.toString();
    }

}
