package io.github.riicarus.common.ast.stmt.decl.type;

import io.github.riicarus.common.ast.stmt.decl.TypeDecl;
import io.github.riicarus.front.semantic.types.type.Array;

/**
 * Type "[" "]"
 *
 * @author Riicarus
 * @create 2024-1-15 21:57
 * @since 1.0.0
 */
public final class ArrayType extends TypeDecl {
    private TypeDecl eleType;

    @Override
    public Array type() {
        final Array t = new Array();
        t.setEleType(eleType.type());
        return t;
    }

    @Override
    public String toTreeString(int level, String prefix) {
        StringBuilder sb = new StringBuilder();
        String t = "\t".repeat(Math.max(0, level - 1));
        String link = level == 0 ? "" : "|--- ";

        if (level != 0) sb.append("\r\n");

        sb.append(prefix).append(t).append(link).append("ArrayType")
                .append(eleType.toTreeString(level + 1, prefix));
        return sb.toString();
    }

    public TypeDecl getEleType() {
        return eleType;
    }

    public void setEleType(TypeDecl eleType) {
        this.eleType = eleType;
    }
}
