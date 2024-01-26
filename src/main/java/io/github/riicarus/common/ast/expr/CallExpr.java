package io.github.riicarus.common.ast.expr;

import io.github.riicarus.common.ast.Expr;

import java.util.ArrayList;
import java.util.List;

/**
 * FuncName "(" Params ")"
 *
 * @author Riicarus
 * @create 2024-1-16 14:27
 * @since 1.0.0
 */
public final class CallExpr extends Expr {
    private Expr func;
    private List<Expr> params = new ArrayList<>();

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

    @Override
    public String toTreeString(int level, String prefix) {
        StringBuilder sb = new StringBuilder();
        String t = "\t".repeat(Math.max(0, level - 1));
        String link = level == 0 ? "" : "|--- ";

        if (level != 0) sb.append("\r\n");

        sb.append(prefix).append(t).append(link).append("Call")
                .append(func.toTreeString(level + 1, prefix)).append("  (func)");
        params.forEach(p -> sb.append(p.toTreeString(level + 1, prefix)).append("  (param)"));

        return sb.toString();
    }
}
