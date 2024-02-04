package io.github.riicarus.common.ast.expr.lit;

import io.github.riicarus.common.ast.Expr;
import io.github.riicarus.common.ast.Stmt;
import io.github.riicarus.common.ast.stmt.decl.FieldDecl;
import io.github.riicarus.common.ast.stmt.decl.TypeDecl;
import io.github.riicarus.front.semantic.Checker;
import io.github.riicarus.front.semantic.types.Type;
import io.github.riicarus.front.semantic.types.type.Signature;

import java.util.List;
import java.util.stream.Collectors;

/**
 * RetType "(" ParamDecls ")" "->" CodeBlock
 *
 * @author Riicarus
 * @create 2024-1-15 21:15
 * @since 1.0.0
 */
public final class FuncLit extends Expr {
    private TypeDecl retType;
    private List<FieldDecl> paramDecls;
    private Stmt body;

    @Override
    public Signature doCheckType(Checker checker, Type outerType) {
        final Signature s = new Signature();
        s.setRetType(retType.type());
        s.setParamTypes(paramDecls.stream().map(p -> p.getType().type()).collect(Collectors.toList()));

        body.checkType(checker, null);

        return s;

        // final Type rt = body.chechType(checker, null);
        // if (s.getRetType().equals(rt)) return s;

        // throw new IllegalStateException(String.format("Type error: function return type need: %s, but get %s", s.getRetType(), rt));
    }

    @Override
    public String toTreeString(int level, String prefix) {
        StringBuilder sb = new StringBuilder();
        String t = "\t".repeat(Math.max(0, level - 1));
        String link = level == 0 ? "" : "|--- ";

        if (level != 0) sb.append("\r\n");

        sb.append(prefix).append(t).append(link).append("FuncLit  ")
                .append(retType.toTreeString(level + 1, prefix));
        if (paramDecls != null) paramDecls.forEach(p -> sb.append(p.toTreeString(level + 1, prefix)));
        sb.append(body == null ? "" : body.toTreeString(level + 1, prefix));

        return sb.toString();
    }

    /* **************************************************************
     * Getters and Setters
     *************************************************************** */

    public TypeDecl getRetType() {
        return retType;
    }

    public void setRetType(TypeDecl retType) {
        this.retType = retType;
    }

    public List<FieldDecl> getParamDecls() {
        return paramDecls;
    }

    public void setParamDecls(List<FieldDecl> paramDecls) {
        this.paramDecls = paramDecls;
    }

    public Stmt getBody() {
        return body;
    }

    public void setBody(Stmt body) {
        this.body = body;
    }
}
