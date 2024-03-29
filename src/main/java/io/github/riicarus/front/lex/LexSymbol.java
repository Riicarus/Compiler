package io.github.riicarus.front.lex;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Lexical symbols, including reserved words, op/assistant symbols, identifiers, numbers, .etc.
 * <p>
 * LexSymbol will be formatted as: &lt;code, name&gt;.
 * <p>
 * All illegal lex symbol will be represented as: {@code ILLEGAL}
 *
 * @author Riicarus
 * @create 2024-1-12 12:50
 * @since 1.0.0
 */
public enum LexSymbol {

    // reserved words
    // type
    INT("int"),
    FLOAT("float"),
    BOOL("bool"),
    CHAR("char"),
    STRING("string"),
    FUNC("func"),
    VOID("void"),
    TYPE("type"),
    STRUCT("struct"),
    // new
    NEW("new"),
    SIZE_OF("sizeof"),
    // value
    TRUE("true"),
    FALSE("false"),
    NULL("null"),
    // control
    FOR("for"),
    WHILE("while"),
    IF("if"),
    ELSE("else"),
    ELSE_IF("elseif"),
    SWITCH("switch"),
    CASE("case"),
    DEFAULT("default"),
    CONTINUE("continue"),
    BREAK("break"),
    RETURN("return"),
    // scope
    PACKAGE("package"),
    IMPORT("import"),
    CONST("const"),

    // literal
    IDENT("identifier"),
    INT_LIT("int_lit"),
    FLOAT_LIT("float_lit"),
    CHAR_LIT("char_lit"),
    STRING_LIT("string_lit"),

    // op/assistant symbols
    // rel
    EQ("=="),
    NE("!="),
    LT("<"),
    LE("<="),
    GT(">"),
    GE(">="),
    // arith
    ADD("+"),
    SUB("-"),
    MUL("*"),
    QUO("/"),
    REM("%"),
    AND("&"),
    OR("|"),
    XOR("^"),
    NOT("~"),
    SHL("<<"),
    SHR(">>"),
    // assign
    ASSIGN("="),
    ADD_ASSIGN("+="),
    SUB_ASSIGN("-="),
    MUL_ASSIGN("*="),
    QUO_ASSIGN("/="),
    REM_ASSIGN("%="),
    AND_ASSIGN("&="),
    OR_ASSIGN("|="),
    XOR_ASSIGN("^="),
    SHL_ASSIGN("<<="),
    SHR_ASSIGN(">>="),
    INC("++"),
    DEC("--"),
    // logic,
    LAND("&&"),
    LOR("||"),
    LNOT("!"),
    // assistant
    LPAREN("("),
    RPAREN(")"),
    LBRACK("["),
    RBRACK("]"),
    LBRACE("{"),
    RBRACE("}"),
    RARROW("->"),
    COMMA(","),
    PERIOD("."),
    SEMICOLON(";"),
    COLON(":"),
    QUES("?"),
    COMMENT("//"),

    // others
    EOF("EOF"),
    ILLEGAL("ILLEGAL");

    private final String name;

    LexSymbol(String name) {
        this.name = name;
    }

    private static final Map<String, LexSymbol> reservedWordMap = new HashMap<>();

    static {
        reservedWordMap.put(INT.name, INT);
        reservedWordMap.put(FLOAT.name, FLOAT);
        reservedWordMap.put(BOOL.name, BOOL);
        reservedWordMap.put(CHAR.name, CHAR);
        reservedWordMap.put(STRING.name, STRING);
        reservedWordMap.put(FUNC.name, FUNC);
        reservedWordMap.put(VOID.name, VOID);
        reservedWordMap.put(TYPE.name, TYPE);
        reservedWordMap.put(STRUCT.name, STRUCT);
        reservedWordMap.put(NEW.name, NEW);
        reservedWordMap.put(SIZE_OF.name, SIZE_OF);
        reservedWordMap.put(TRUE.name, TRUE);
        reservedWordMap.put(FALSE.name, FALSE);
        reservedWordMap.put(NULL.name, NULL);
        reservedWordMap.put(FOR.name, FOR);
        reservedWordMap.put(WHILE.name, WHILE);
        reservedWordMap.put(IF.name, IF);
        reservedWordMap.put(ELSE.name, ELSE);
        reservedWordMap.put(ELSE_IF.name, ELSE_IF);
        reservedWordMap.put(SWITCH.name, SWITCH);
        reservedWordMap.put(CASE.name, CASE);
        reservedWordMap.put(DEFAULT.name, DEFAULT);
        reservedWordMap.put(CONTINUE.name, CONTINUE);
        reservedWordMap.put(BREAK.name, BREAK);
        reservedWordMap.put(RETURN.name, RETURN);
        reservedWordMap.put(PACKAGE.name, PACKAGE);
        reservedWordMap.put(IMPORT.name, IMPORT);
        reservedWordMap.put(CONST.name, CONST);
    }

    public static LexSymbol lookUpReserved(String lexeme) {
        return reservedWordMap.get(lexeme);
    }

    public static final Set<LexSymbol> LIT_SET = Set.of(
            INT_LIT,
            FLOAT_LIT,
            CHAR_LIT,
            STRING_LIT,
            TRUE,
            FALSE,
            NULL
    );

    public static final Set<LexSymbol> TYPE_SET = Set.of(
            INT,
            FLOAT,
            BOOL,
            CHAR,
            STRING,
            FUNC,
            VOID
    );

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
