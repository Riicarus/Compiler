package io.github.riicarus.common.ast.stmt.decl.type;

/**
 * "int" | "float" | "bool" | "char" | "string" | "void"
 *
 * @author Riicarus
 * @create 2024-1-15 21:49
 * @since 1.0.0
 */
public final class BasicType extends TypeDecl {

    public static final BasicType INT = new BasicType();
    public static final BasicType FLOAT = new BasicType();
    public static final BasicType BOOL = new BasicType();
    public static final BasicType CHAR = new BasicType();
    public static final BasicType STRING = new BasicType();
    public static final BasicType VOID = new BasicType();



}
