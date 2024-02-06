package io.github.riicarus.common.ast.expr.lit;

import io.github.riicarus.common.ast.Expr;
import io.github.riicarus.front.lex.LitKind;
import io.github.riicarus.front.semantic.Checker;
import io.github.riicarus.front.semantic.types.Type;
import io.github.riicarus.front.semantic.types.type.Any;
import io.github.riicarus.front.semantic.types.type.Basic;

/**
 * int_lit | float_lit | char_lit | string_lit | "true" | "false" | "null"
 *
 * @author Riicarus
 * @create 2024-1-15 21:15
 * @since 1.0.0
 */
public final class BasicLit extends Expr {
    private String value;
    private LitKind kind;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public LitKind getKind() {
        return kind;
    }

    public void setKind(LitKind kind) {
        this.kind = kind;
    }

    @Override
    public Type doCheckType(Checker checker, Type outerType) {
        return switch (kind) {
            case INT -> Basic.INT;
            case FLOAT -> Basic.FLOAT;
            case TRUE, FALSE -> Basic.BOOL;
            case CHAR -> Basic.CHAR;
            case STRING -> Basic.STRING;
            case NULL -> Any.NULL;
        };
    }

    @Override
    public String toTreeString(int level, String prefix) {
        StringBuilder sb = new StringBuilder();
        String t = "\t".repeat(Math.max(0, level - 1));
        String link = level == 0 ? "" : "|--- ";

        if (level != 0) sb.append("\r\n");

        sb.append(prefix).append(t).append(link).append("BasicLit  ")
                .append("\"").append(value).append("\"").append("(").append(kind).append(")");

        return sb.toString();
    }
}
