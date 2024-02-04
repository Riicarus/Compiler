package io.github.riicarus.common.ast.expr.op;

import io.github.riicarus.common.ast.Expr;
import io.github.riicarus.front.semantic.Checker;
import io.github.riicarus.front.semantic.types.Type;

/**
 * Operator
 *
 * @author Riicarus
 * @create 2024-1-16 14:33
 * @since 1.0.0
 */
public interface Operator {

    String getOp();

    int getPriority();

    Type checkType(Checker checker, Expr x, Expr y);
}
