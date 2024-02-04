package io.github.riicarus.common.ast;

import io.github.riicarus.front.semantic.Checker;
import io.github.riicarus.front.semantic.types.Scope;
import io.github.riicarus.front.semantic.types.Type;
import io.github.riicarus.front.semantic.types.type.Basic;

import java.util.List;

/**
 * [ Stmt ]
 *
 * @author Riicarus
 * @create 2024-1-16 13:40
 * @since 1.0.0
 */
public class CodeFile extends ASTNode {

    private List<Stmt> stmts;
    private final Scope scope = new Scope();

    public CodeFile() {
        scope.setName("CodeFile");
    }

    @Override
    public Type doCheckType(Checker checker, Type outerCheck) {
        stmts.forEach(s -> s.checkType(checker, outerCheck));
        return Basic.VOID;
    }

    @Override
    public String toTreeString(int level, String prefix) {
        StringBuilder sb = new StringBuilder();
        String t = "\t".repeat(Math.max(0, level - 1));
        String link = level == 0 ? "" : "|--- ";

        if (level != 0) sb.append("\r\n");

        sb.append(prefix).append(t).append(link).append("Program");
        if (stmts != null) stmts.forEach(s -> sb.append(s.toTreeString(level + 1, prefix)));
        return sb.toString();
    }

    public List<Stmt> getStmts() {
        return stmts;
    }

    public void setStmts(List<Stmt> stmts) {
        this.stmts = stmts;
    }

    public Scope getScope() {
        return scope;
    }
}
