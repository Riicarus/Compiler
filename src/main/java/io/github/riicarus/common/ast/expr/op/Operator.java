package io.github.riicarus.common.ast.expr.op;

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

}
