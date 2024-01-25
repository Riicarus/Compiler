package io.github.riicarus.common.ast.stmt.decl.type;

/**
 * Type "[" "]"
 *
 * @author Riicarus
 * @create 2024-1-15 21:57
 * @since 1.0.0
 */
public final class ArrayType extends TypeDecl {
    private TypeDecl eleType;

    public TypeDecl getEleType() {
        return eleType;
    }

    public void setEleType(TypeDecl eleType) {
        this.eleType = eleType;
    }

}
