package io.github.riicarus.common.ast.stmt.decl.type;

import java.util.ArrayList;
import java.util.List;

/**
 * RetType "func" "(" ParamTypeDecls ")"
 *
 * @author Riicarus
 * @create 2024-1-15 21:53
 * @since 1.0.0
 */
public final class FuncType extends TypeDecl {
    private TypeDecl retType;
    private final List<TypeDecl> paramTypeDecls = new ArrayList<>();

    public TypeDecl getRetType() {
        return retType;
    }

    public void setRetType(TypeDecl retType) {
        this.retType = retType;
    }

    public List<TypeDecl> getParamTypeDecls() {
        return paramTypeDecls;
    }

    public void setParamTypeDecls(List<TypeDecl> paramTypeDecls) {
        this.paramTypeDecls.clear();
        this.paramTypeDecls.addAll(paramTypeDecls);
    }
}
