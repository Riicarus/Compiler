package io.github.riicarus.common.ast.stmt;

import io.github.riicarus.common.ast.Stmt;
import io.github.riicarus.common.ast.stmt.ctrl.BreakStmt;
import io.github.riicarus.common.ast.stmt.ctrl.ContinueStmt;
import io.github.riicarus.common.ast.stmt.ctrl.RetStmt;
import io.github.riicarus.front.semantic.Checker;
import io.github.riicarus.front.semantic.types.Type;
import io.github.riicarus.front.semantic.types.type.Any;
import io.github.riicarus.front.semantic.types.type.Basic;

import java.util.List;

/**
 * "{" Stmts "}"
 *
 * @author Riicarus
 * @create 2024-1-15 20:20
 * @since 1.0.0
 */
public final class CodeBlock extends Stmt {
    private List<Stmt> stmts;

    @Override
    public Type doCheckType(Checker checker, Type retType) {
        checkStatement(checker, retType);
        return Basic.VOID;
    }

    @Override
    public void checkStatement(Checker checker, Type retType) {
        if (stmts == null || stmts.isEmpty())
            if (retType == null || retType.equals(Basic.VOID)) return;
            else throw new IllegalStateException("Missing return statement");
        boolean reachable = true;
        for (Stmt stmt : stmts) {
            if (stmt instanceof RetStmt) {
                Type retValType = stmt.checkType(checker, retType);
                if (retValType.equals(retType) || retValType.equals(Any.NULL)) {
                    reachable = false;
                    continue;
                } else throw new IllegalStateException("Returns wrong type");
            }

            if (stmt instanceof ContinueStmt || stmt instanceof BreakStmt)
                if (checker.getLoopCnt() > 0) {  // clean unreachable code
                    reachable = false;
                    continue;
                } else throw new IllegalStateException("Illegal statement here");

            if (!reachable) stmt.setReachable(false);
            stmt.checkType(checker, retType);
        }

        // loop statement's return statement is not a must part, but not-loop must have return statement if the outer statement return a non-void value type.
        if (checker.getLoopCnt() > 0) checker.setLoopCnt(checker.getLoopCnt() - 1);
        // if not in a loop and still reachable, meaning that there's no return statement
        else if (reachable && !Basic.VOID.equals(retType)) throw new IllegalStateException("Missing return statement");
    }

    @Override
    public String toTreeString(int level, String prefix) {
        StringBuilder sb = new StringBuilder();
        String t = "\t".repeat(Math.max(0, level - 1));
        String link = level == 0 ? "" : "|--- ";

        if (level != 0) sb.append("\r\n");

        sb.append(prefix).append(t).append(link).append("CodeBlock");
        if (stmts != null) stmts.forEach(s -> sb.append(s.toTreeString(level + 1, prefix)));
        return sb.toString();
    }

    public List<Stmt> getStmts() {
        return stmts;
    }

    public void setStmts(List<Stmt> stmts) {
        this.stmts = stmts;
    }
}
