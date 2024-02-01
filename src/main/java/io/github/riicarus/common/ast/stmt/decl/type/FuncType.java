package io.github.riicarus.common.ast.stmt.decl.type;

import io.github.riicarus.common.ast.stmt.decl.TypeDecl;

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
    private List<TypeDecl> paramTypeDecls;

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
        this.paramTypeDecls = paramTypeDecls;
    }

    @Override
    public String toTreeString(int level, String prefix) {
        StringBuilder sb = new StringBuilder();
        String t = "\t".repeat(Math.max(0, level - 1));
        String link = level == 0 ? "" : "|--- ";

        if (level != 0) sb.append("\r\n");

        sb.append(prefix).append(t).append(link).append("FuncType")
                .append(retType.toTreeString(level + 1, prefix)).append("  (RetType)");
        if (paramTypeDecls != null)
            paramTypeDecls.forEach(p -> sb.append(p.toTreeString(level + 1, prefix)).append("  (param)"));
        return sb.toString();
    }
}
