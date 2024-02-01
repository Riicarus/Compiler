package io.github.riicarus.common.ast.stmt.decl.type;

import io.github.riicarus.common.ast.stmt.decl.TypeDecl;

/**
 * "int" | "float" | "bool" | "char" | "string" | "void"
 *
 * @author Riicarus
 * @create 2024-1-15 21:49
 * @since 1.0.0
 */
public final class BasicType extends TypeDecl {

    private final String name;

    public BasicType(String name) {
        this.name = name;
    }

    public static BasicType INT() {
        return new BasicType("int");
    }

    public static BasicType FLOAT() {
        return new BasicType("float");
    }

    public static BasicType BOOL() {
        return new BasicType("bool");
    }

    public static BasicType CHAR() {
        return new BasicType("char");
    }

    public static BasicType STRING() {
        return new BasicType("string");
    }

    public static BasicType VOID() {
        return new BasicType("void");
    }

    @Override
    public String toTreeString(int level, String prefix) {
        StringBuilder sb = new StringBuilder();
        String t = "\t".repeat(Math.max(0, level - 1));
        String link = level == 0 ? "" : "|--- ";

        if (level != 0) sb.append("\r\n");

        sb.append(prefix).append(t).append(link).append("BasicType  ").append(name);
        return sb.toString();
    }
}
