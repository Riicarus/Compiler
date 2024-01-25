package io.github.riicarus.common.ast.stmt.decl;

import io.github.riicarus.common.ast.Decl;
import io.github.riicarus.common.ast.SimpleStmt;
import io.github.riicarus.common.ast.stmt.decl.type.TypeDecl;
import io.github.riicarus.common.ast.expr.AssignExpr;
import io.github.riicarus.common.ast.expr.NameExpr;

/**
 * Type Id [ "=" Expr ]
 *
 * @author Riicarus
 * @create 2024-1-15 22:18
 * @since 1.0.0
 */
public final class FieldDecl extends Decl implements SimpleStmt {
    private TypeDecl type;
    private NameExpr name;
    private AssignExpr assign;

    public TypeDecl getType() {
        return type;
    }

    public void setType(TypeDecl type) {
        this.type = type;
    }

    public NameExpr getName() {
        return name;
    }

    public void setName(NameExpr name) {
        this.name = name;
    }

    public AssignExpr getAssign() {
        return assign;
    }

    public void setAssign(AssignExpr assign) {
        this.assign = assign;
    }
}
