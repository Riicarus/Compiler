package io.github.riicarus.common.ast.stmt.decl;

import io.github.riicarus.common.ast.Decl;
import io.github.riicarus.common.ast.SimpleStmt;
import io.github.riicarus.common.ast.expr.AssignExpr;
import io.github.riicarus.common.ast.expr.NameExpr;
import io.github.riicarus.front.semantic.Checker;
import io.github.riicarus.front.semantic.types.Type;

/**
 * Type Id [ "=" Expr ]
 *
 * @author Riicarus
 * @create 2024-1-15 22:18
 * @since 1.0.0
 */
public final class FieldDecl extends Decl implements SimpleStmt {
    private boolean isConst = false;
    private TypeDecl type;
    private NameExpr name;
    private AssignExpr assign;

    @Override
    public Type doCheckType(Checker checker, Type outer) {
        if (assign != null) {
            assign.checkType(checker, outer);
        }

        return type.checkType(checker, outer);
    }

    @Override
    public String toTreeString(int level, String prefix) {
        StringBuilder sb = new StringBuilder();
        String t = "\t".repeat(Math.max(0, level - 1));
        String link = level == 0 ? "" : "|--- ";

        if (level != 0) sb.append("\r\n");

        sb.append(prefix).append(t).append(link).append("FieldDecl")
                .append(isConst ? "(const)" : "")
                .append(type.toTreeString(level + 1, prefix))
                .append(name.toTreeString(level + 1, prefix))
                .append(assign == null ? "" : assign.toTreeString(level + 1, prefix));
        return sb.toString();
    }

    /* **************************************************************
     * Getters and Setters
     *************************************************************** */

    public boolean isConst() {
        return isConst;
    }

    public void setConst(boolean isConst) {
        this.isConst = isConst;
    }

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
