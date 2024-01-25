package io.github.riicarus.common.ast.expr.lit;

import io.github.riicarus.common.ast.Expr;
import io.github.riicarus.common.ast.stmt.decl.type.TypeDecl;

import java.util.ArrayList;
import java.util.List;

/**
 * Type { EleList[0], EleList[1] ...}
 *
 * @author Riicarus
 * @create 2024-1-16 14:33
 * @since 1.0.0
 */
public final class CompositeLit extends Expr {
    private TypeDecl type;
    private final List<Expr> elements = new ArrayList<>();

    public TypeDecl getType() {
        return type;
    }

    public void setType(TypeDecl type) {
        this.type = type;
    }

    public List<Expr> getElements() {
        return elements;
    }

    public void setElements(List<Expr> elements) {
        this.elements.clear();
        this.elements.addAll(elements);
    }
}
