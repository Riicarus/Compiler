package io.github.riicarus.common.ast.expr.lit;

import io.github.riicarus.common.ast.Expr;
import io.github.riicarus.front.semantic.Checker;
import io.github.riicarus.front.semantic.types.Type;
import io.github.riicarus.front.semantic.types.type.Basic;

import java.util.List;

/**
 * { EleList[0], EleList[1] ...}
 *
 * @author Riicarus
 * @create 2024-1-16 14:33
 * @since 1.0.0
 */
public final class CompositeLit extends Expr {
    private List<Expr> elements;

    public List<Expr> getElements() {
        return elements;
    }

    public void setElements(List<Expr> elements) {
        this.elements = elements;
    }

    @Override
    public Type doCheckType(Checker checker, Type outerType) {
        if (outerType == null)
            throw new IllegalStateException("Type error: composite lit type check need outer type");

        for (Expr e : elements) {
            if (e instanceof CompositeLit) {
                e.checkType(checker, outerType.underlying());
                continue;
            }

            Type inner = e.checkType(checker, outerType.underlying());
            if (!outerType.underlying().equals(inner))
                throw new IllegalStateException(String.format("Type error: need %s, but get %s", outerType.underlying(), inner));
        }

        return Basic.VOID;
    }

    @Override
    public String toTreeString(int level, String prefix) {
        StringBuilder sb = new StringBuilder();
        String t = "\t".repeat(Math.max(0, level - 1));
        String link = level == 0 ? "" : "|--- ";

        if (level != 0) sb.append("\r\n");

        sb.append(prefix).append(t).append(link).append("CompositeLit");
        if (elements != null) elements.forEach(e -> sb.append(e.toTreeString(level + 1, prefix)));

        return sb.toString();
    }
}
