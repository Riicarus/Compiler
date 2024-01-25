package io.github.riicarus.common.ast.expr.lit;

import io.github.riicarus.common.ast.Expr;
import io.github.riicarus.common.ast.Stmt;
import io.github.riicarus.common.ast.stmt.decl.FieldDecl;
import io.github.riicarus.common.ast.stmt.decl.type.TypeDecl;

import java.util.ArrayList;
import java.util.List;

/**
 * RetType "(" ParamDecls ")" "->" CodeBlock
 *
 * @author Riicarus
 * @create 2024-1-15 21:15
 * @since 1.0.0
 */
public final class FuncLit extends Expr {
    private TypeDecl retType;
    private final List<FieldDecl> paramDecls = new ArrayList<>();
    private Stmt body;

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
        this.paramDecls.clear();
        this.paramDecls.addAll(paramDecls);
    }

    public Stmt getBody() {
        return body;
    }

    public void setBody(Stmt body) {
        this.body = body;
    }
}
