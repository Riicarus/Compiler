package io.github.riicarus.common.ast.expr.op;

import io.github.riicarus.common.ast.Expr;
import io.github.riicarus.front.lex.LexSymbol;
import io.github.riicarus.front.semantic.Checker;
import io.github.riicarus.front.semantic.types.Type;
import io.github.riicarus.front.semantic.types.type.Array;
import io.github.riicarus.front.semantic.types.type.Basic;
import io.github.riicarus.front.semantic.types.type.Signature;

import java.util.HashMap;
import java.util.Map;

/**
 * Binary operators
 *
 * @author Riicarus
 * @create 2024-1-16 14:15
 * @since 1.0.0
 */
public enum BinaryOp implements Operator {

    // ArithOp
    ADD("+", 9),
    SUB("-", 9),
    MUL("*", 10),
    QUO("/", 10),
    REM("%", 10),
    AND("&", 5),
    OR("|", 3),
    XOR("^", 4),
    SHL("<<", 8),
    SHR(">>", 8),

    // AssignOp
    ASSIGN("=", 0),
    ADD_ASSIGN("+=", 0),
    SUB_ASSIGN("-=", 0),
    MUL_ASSIGN("*=", 0),
    QUO_ASSIGN("/=", 0),
    REM_ASSIGN("%=", 0),
    AND_ASSIGN("&=", 0),
    OR_ASSIGN("|=", 0),
    XOR_ASSIGN("^=", 0),
    SHL_ASSIGN("<<=", 0),
    SHR_ASSIGN(">>=", 0),

    // RelOp
    EQ("==", 6),
    NE("!=", 6),
    LT("<", 7),
    LE("<=", 7),
    GT(">", 7),
    GE(">=", 7),

    // LogicOp
    LAND("&&", 2),
    LOR("||", 1);

    private final String op;
    private final int priority;

    BinaryOp(String op, int priority) {
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
        Type yt = y.checkType(checker, null);

        if (BinaryOp.ASSIGN.equals(this)) {
            if (xt instanceof Signature xs && yt instanceof Signature ys) {
                if (!xs.getRetType().equals(ys.getRetType()))
                    throw new IllegalStateException(String.format("Type error: illegal type for operation: %s %s %s", xt, op, yt));
                if (xs.getParamTypes().size() != ys.getParamTypes().size())
                    throw new IllegalStateException(String.format("Type error: illegal type for operation: %s %s %s", xt, op, yt));
                for (int i = 0; i < xs.getParamTypes().size(); i++)
                    if (!xs.getParamTypes().get(i).equals(ys.getParamTypes().get(i)))
                        throw new IllegalStateException(String.format("Type error: illegal type for operation: %s %s %s", xt, op, yt));

                return xs;
            }

            if (xt instanceof Array xa && yt instanceof Array ya) {
                if (!xa.equals(ya))
                    throw new IllegalStateException(String.format("Type error: illegal type for operation: %s %s %s", xt, op, yt));

                xa.setSize(ya.getSize());
                xa.setEleType(ya.getEleType());

                return xa;
            }
        }

        if (BinaryOp.NE.equals(this)) return Basic.BOOL;

        if (!xt.equals(yt))
            throw new IllegalStateException(String.format("Type error: illegal type for operation: %s %s %s", xt, op, yt));

        if (xt instanceof Basic xb && yt instanceof Basic yb)
            return switch (this) {
                case ASSIGN -> xt;
                case ADD, ADD_ASSIGN -> {
                    switch (xb) {
                        case INT -> {
                            yield Basic.INT;
                        }
                        case FLOAT -> {
                            yield Basic.FLOAT;
                        }
                        case CHAR, STRING -> {
                            yield Basic.STRING;
                        }
                        default ->
                                throw new IllegalStateException(String.format("Type error: illegal type for operation: %s %s %s", xt, op, yt));
                    }

                }
                case SUB, SUB_ASSIGN, MUL, MUL_ASSIGN, QUO, QUO_ASSIGN, REM, REM_ASSIGN -> {
                    switch (xb) {
                        case INT -> {
                            yield Basic.INT;
                        }
                        case FLOAT -> {
                            yield Basic.FLOAT;
                        }
                        default ->
                                throw new IllegalStateException(String.format("Type error: illegal type for operation: %s %s %s", xt, op, yt));
                    }
                }
                case EQ, LT, LE, GT, GE -> {
                    if (xt.equals(Basic.INT) || xt.equals(Basic.FLOAT)) yield Basic.BOOL;
                    throw new IllegalStateException(String.format("Type error: illegal type for operation: %s %s %s", xt, op, yt));
                }
                case AND, AND_ASSIGN, OR, OR_ASSIGN, LAND, LOR -> {
                    if (xt.equals(Basic.BOOL)) yield Basic.BOOL;
                    throw new IllegalStateException(String.format("Type error: illegal type for operation: %s %s %s", xt, op, yt));
                }
                case XOR, XOR_ASSIGN, SHL, SHL_ASSIGN, SHR, SHR_ASSIGN -> {
                    if (xt.equals(Basic.INT)) yield Basic.INT;
                    throw new IllegalStateException(String.format("Type error: illegal type for operation: %s %s %s", xt, op, yt));
                }
                default -> throw new IllegalStateException("Unexpected value: " + this);
            };

        throw new IllegalStateException(String.format("Type error: illegal type for operation: %s %s %s", xt, op, yt));
    }


    public static final Map<LexSymbol, BinaryOp> ARITH_OP_MAP = new HashMap<>();

    static {
        ARITH_OP_MAP.put(LexSymbol.ADD, ADD);
        ARITH_OP_MAP.put(LexSymbol.SUB, SUB);
        ARITH_OP_MAP.put(LexSymbol.MUL, MUL);
        ARITH_OP_MAP.put(LexSymbol.QUO, QUO);
        ARITH_OP_MAP.put(LexSymbol.REM, REM);
        ARITH_OP_MAP.put(LexSymbol.AND, AND);
        ARITH_OP_MAP.put(LexSymbol.OR, OR);
        ARITH_OP_MAP.put(LexSymbol.XOR, XOR);
        ARITH_OP_MAP.put(LexSymbol.SHL, SHL);
        ARITH_OP_MAP.put(LexSymbol.SHR, SHR);
    }

    public static final Map<LexSymbol, BinaryOp> ASSIGN_OP_MAP = new HashMap<>();

    static {
        ASSIGN_OP_MAP.put(LexSymbol.ASSIGN, ASSIGN);
        ASSIGN_OP_MAP.put(LexSymbol.ADD_ASSIGN, ADD_ASSIGN);
        ASSIGN_OP_MAP.put(LexSymbol.SUB_ASSIGN, SUB_ASSIGN);
        ASSIGN_OP_MAP.put(LexSymbol.MUL_ASSIGN, MUL_ASSIGN);
        ASSIGN_OP_MAP.put(LexSymbol.QUO_ASSIGN, QUO_ASSIGN);
        ASSIGN_OP_MAP.put(LexSymbol.REM_ASSIGN, REM_ASSIGN);
        ASSIGN_OP_MAP.put(LexSymbol.AND_ASSIGN, AND_ASSIGN);
        ASSIGN_OP_MAP.put(LexSymbol.OR_ASSIGN, OR_ASSIGN);
        ASSIGN_OP_MAP.put(LexSymbol.XOR_ASSIGN, XOR_ASSIGN);
        ASSIGN_OP_MAP.put(LexSymbol.SHL_ASSIGN, SHL_ASSIGN);
        ASSIGN_OP_MAP.put(LexSymbol.SHR_ASSIGN, SHR_ASSIGN);
    }

    public static final Map<LexSymbol, BinaryOp> REL_OP_MAP = new HashMap<>();

    static {
        REL_OP_MAP.put(LexSymbol.EQ, EQ);
        REL_OP_MAP.put(LexSymbol.NE, NE);
        REL_OP_MAP.put(LexSymbol.LT, LT);
        REL_OP_MAP.put(LexSymbol.LE, LE);
        REL_OP_MAP.put(LexSymbol.GT, GT);
        REL_OP_MAP.put(LexSymbol.GE, GE);
    }

    public static final Map<LexSymbol, BinaryOp> LOGIC_OP_MAP = new HashMap<>();

    static {
        LOGIC_OP_MAP.put(LexSymbol.LAND, LAND);
        LOGIC_OP_MAP.put(LexSymbol.LOR, LOR);
    }

    public static final Map<LexSymbol, BinaryOp> OP_MAP = new HashMap<>();

    static {
        OP_MAP.putAll(ARITH_OP_MAP);
        OP_MAP.putAll(ASSIGN_OP_MAP);
        OP_MAP.putAll(REL_OP_MAP);
        OP_MAP.putAll(LOGIC_OP_MAP);
    }
}
