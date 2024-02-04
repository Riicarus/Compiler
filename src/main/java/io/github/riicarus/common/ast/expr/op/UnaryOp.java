package io.github.riicarus.common.ast.expr.op;

import io.github.riicarus.common.ast.Expr;
import io.github.riicarus.front.lex.LexSymbol;
import io.github.riicarus.front.semantic.Checker;
import io.github.riicarus.front.semantic.types.Type;
import io.github.riicarus.front.semantic.types.type.Basic;

import java.util.Map;

/**
 * Unary operators
 *
 * @author Riicarus
 * @create 2024-1-16 14:03
 * @since 1.0.0
 */
public enum UnaryOp implements Operator {

    NOT(LexSymbol.NOT.getName(), 11),
    LNOT(LexSymbol.LNOT.getName(), 11);

    private final String op;
    private final int priority;

    UnaryOp(String op, int priority) {
        this.op = op;
        this.priority = priority;
    }


    @Override
    public String getOp() {
        return op;
    }

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public Type checkType(Checker checker, Expr x, Expr y) {
        Type xt = x.checkType(checker, null);

        if (!xt.equals(Basic.BOOL))
            throw new IllegalStateException(String.format("Type error: illegal type for operation: %s %s", op, xt));

        return Basic.BOOL;
    }

    public static final Map<LexSymbol, UnaryOp> OP_MAP = Map.of(
            LexSymbol.NOT, NOT,
            LexSymbol.LNOT, LNOT
    );
}
