package io.github.riicarus.common.ast.stmt.decl;

import io.github.riicarus.common.ast.Decl;
import io.github.riicarus.common.ast.expr.NameExpr;
import io.github.riicarus.common.ast.expr.lit.FuncLit;

/**
 * RetType "func" FuncName "(" ParamDecls ")" CodeBlock <br/>
 *
 * @author Riicarus
 * @create 2024-1-16 13:44
 * @since 1.0.0
 */
public final class FuncDecl extends Decl {
    private NameExpr funcName;
    private FuncLit funcLit;

    public NameExpr getFuncName() {
        return funcName;
    }

    public void setFuncName(NameExpr funcName) {
        this.funcName = funcName;
    }

    public FuncLit getFuncLit() {
        return funcLit;
    }

    public void setFuncLit(FuncLit funcLit) {
        this.funcLit = funcLit;
    }

    @Override
    public String toTreeString(int level, String prefix) {
        StringBuilder sb = new StringBuilder();
        String t = "\t".repeat(Math.max(0, level - 1));
        String link = level == 0 ? "" : "|--- ";

        if (level != 0) sb.append("\r\n");

        sb.append(prefix).append(t).append(link).append("FuncDecl")
                .append(funcName.toTreeString(level + 1, prefix))
                .append(funcLit.toTreeString(level + 1, prefix));
        return sb.toString();
    }
}
