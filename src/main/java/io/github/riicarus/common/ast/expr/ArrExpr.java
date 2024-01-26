package io.github.riicarus.common.ast.expr;

import io.github.riicarus.common.ast.Expr;
import io.github.riicarus.common.ast.stmt.decl.type.ArrayType;
import io.github.riicarus.common.ast.stmt.decl.type.TypeDecl;
import io.github.riicarus.common.ast.expr.lit.CompositeLit;

/**
 * @author Riicarus
 * @create 2024-1-19 11:51
 * @since 1.0.0
 */
public final class ArrExpr extends Expr {

    private final ArrayType type = new ArrayType();
    private Expr size;
    private CompositeLit lit;

    public ArrayType getType() {
        return type;
    }

    public void setBaseType(TypeDecl type) {
        this.type.setEleType(type);
    }

    public Expr getSize() {
        return size;
    }

    public void setSize(Expr size) {
        this.size = size;
    }

    public CompositeLit getLit() {
        return lit;
    }

    public void setLit(CompositeLit lit) {
        this.lit = lit;
    }

    @Override
    public String toTreeString(int level, String prefix) {
        StringBuilder sb = new StringBuilder();
        String t = "\t".repeat(Math.max(0, level - 1));
        String link = level == 0 ? "" : "|--- ";

        if (level != 0) sb.append("\r\n");

        sb.append(prefix).append(t).append(link).append("Array")
                .append(type.toTreeString(level + 1, prefix))
                .append(size.toTreeString(level + 1, prefix))
                .append(lit == null ? "" : lit.toTreeString(level + 1, prefix));

        return sb.toString();
    }
}
