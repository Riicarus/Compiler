package io.github.riicarus.common.ast.stmt.decl.type;

import io.github.riicarus.common.ast.stmt.decl.TypeDecl;
import io.github.riicarus.front.semantic.types.Type;
import io.github.riicarus.front.semantic.types.type.Basic;

/**
 * "int" | "float" | "bool" | "char" | "string" | "void"
 *
 * @author Riicarus
 * @create 2024-1-15 21:49
 * @since 1.0.0
 */
public final class BasicType extends TypeDecl {

    private final String name;
    private final Basic type;

    public BasicType(String name, Basic type) {
        this.name = name;
        this.type = type;
    }

    public static BasicType INT() {
        return new BasicType("int", Basic.INT);
    }

    public static BasicType FLOAT() {
        return new BasicType("float", Basic.FLOAT);
    }

    public static BasicType BOOL() {
        return new BasicType("bool", Basic.BOOL);
    }

    public static BasicType CHAR() {
        return new BasicType("char", Basic.CHAR);
    }

    public static BasicType STRING() {
        return new BasicType("string", Basic.STRING);
    }

    public static BasicType VOID() {
        return new BasicType("void", Basic.VOID);
    }

    @Override
    public Basic type() {
        return type;
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

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }
}
