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
    private final List<Expr> params = new ArrayList<>();

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
        this.params.clear();
        this.params.addAll(params);
    }
}
