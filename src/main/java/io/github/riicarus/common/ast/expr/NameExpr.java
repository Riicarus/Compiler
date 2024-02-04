package io.github.riicarus.common.ast.expr;

import io.github.riicarus.common.ast.Expr;
import io.github.riicarus.front.semantic.Checker;
import io.github.riicarus.front.semantic.types.Element;
import io.github.riicarus.front.semantic.types.Type;

/**
 * identifier
 *
 * @author Riicarus
 * @create 2024-1-15 21:15
 * @since 1.0.0
 */
public final class NameExpr extends Expr {
    private String value;

    @Override
    public Type doCheckType(Checker checker, Type outerType) {
        final Element e = checker.getCurScope().lookupAll(value);
        if (e == null) throw new IllegalStateException(String.format("Can not find variable %s", value));
        return e.getType();
    }

    @Override
    public String toTreeString(int level, String prefix) {
        StringBuilder sb = new StringBuilder();
        String t = "\t".repeat(Math.max(0, level - 1));
        String link = level == 0 ? "" : "|--- ";

        if (level != 0) sb.append("\r\n");

        sb.append(prefix).append(t).append(link).append("Name  ").append(value);
        return sb.toString();
    }

    /* **************************************************************
     * Getters and Setters
     *************************************************************** */

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
