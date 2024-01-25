package io.github.riicarus.common.ast.expr.op;

import io.github.riicarus.front.lex.LexSymbol;

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
