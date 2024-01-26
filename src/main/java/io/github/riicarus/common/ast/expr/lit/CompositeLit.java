package io.github.riicarus.common.ast.expr.lit;

import io.github.riicarus.common.ast.Expr;

import java.util.ArrayList;
import java.util.List;

/**
 * { EleList[0], EleList[1] ...}
 *
 * @author Riicarus
 * @create 2024-1-16 14:33
 * @since 1.0.0
 */
public final class CompositeLit extends Expr {
    private List<Expr> elements = new ArrayList<>();

    public List<Expr> getElements() {
        return elements;
    }

    public void setElements(List<Expr> elements) {
        this.elements = elements;
    }

    @Override
    public String toTreeString(int level, String prefix) {
        StringBuilder sb = new StringBuilder();
        String t = "\t".repeat(Math.max(0, level - 1));
        String link = level == 0 ? "" : "|--- ";

        if (level != 0) sb.append("\r\n");

        sb.append(prefix).append(t).append(link).append("CompositeLit");
        elements.forEach(e -> sb.append(e.toTreeString(level + 1, prefix)));

        return sb.toString();
    }
}
