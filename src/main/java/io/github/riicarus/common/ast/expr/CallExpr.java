package io.github.riicarus.common.ast.expr;

import io.github.riicarus.common.ast.Expr;
import io.github.riicarus.front.semantic.Checker;
import io.github.riicarus.front.semantic.types.Type;
import io.github.riicarus.front.semantic.types.type.Signature;

import java.util.List;

import static java.util.stream.Collectors.joining;

/**
 * FuncName "(" Params ")"
 *
 * @author Riicarus
 * @create 2024-1-16 14:27
 * @since 1.0.0
 */
public final class CallExpr extends Expr {
    private Expr func;
    private List<Expr> params;

    @Override
    public Type doCheckType(Checker checker, Type outerType) {
        Type ft = func.checkType(checker, null);

        if (ft instanceof Signature s) {
            if (params != null)
                if (params.size() != s.getParamTypes().size())
                    throw new IllegalStateException(String.format("Type error: function params error: need (%s), but get (%s)",
                            s.getParamTypes().stream().map(Type::naming).collect(joining(", ")),
                            params.stream().map(p -> p.checkType(checker, null).naming()).collect(joining(", "))));


            for (int i = 0; i < s.getParamTypes().size(); i++)
                if (!s.getParamTypes().get(i).equals(params.get(i).checkType(checker, null)))
                    throw new IllegalStateException(String.format("Type error: function params error: need (%s), but get (%s)",
                            s.getParamTypes().stream().map(Type::naming).collect(joining(", ")),
                            params.stream().map(p -> p.checkType(checker, null).naming()).collect(joining(", "))));

            return s.getRetType();
        }

        throw new IllegalStateException("Type error: not a function type");
    }

    @Override
    public String toTreeString(int level, String prefix) {
        StringBuilder sb = new StringBuilder();
        String t = "\t".repeat(Math.max(0, level - 1));
        String link = level == 0 ? "" : "|--- ";

        if (level != 0) sb.append("\r\n");

        sb.append(prefix).append(t).append(link).append("Call")
                .append(func.toTreeString(level + 1, prefix)).append("  (func)");
        if (params != null) params.forEach(p -> sb.append(p.toTreeString(level + 1, prefix)).append("  (param)"));

        return sb.toString();
    }

    /* **************************************************************
     * Getters and Setters
     *************************************************************** */

    public Expr getFunc() {
        return func;
    }

    public void setFunc(Expr func) {
        this.func = func;
    }

    public List<Expr> getParams() {
        return params;
    }

    public void setParams(List<Expr> params) {
        this.params = params;
    }
}
