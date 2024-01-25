package io.github.riicarus.front.syntax;

import io.github.riicarus.common.Position;
import io.github.riicarus.common.ast.*;
import io.github.riicarus.common.ast.expr.*;
import io.github.riicarus.common.ast.expr.lit.BasicLit;
import io.github.riicarus.common.ast.expr.lit.CompositeLit;
import io.github.riicarus.common.ast.expr.lit.FuncLit;
import io.github.riicarus.common.ast.expr.op.BinaryOp;
import io.github.riicarus.common.ast.expr.op.Operator;
import io.github.riicarus.common.ast.expr.op.UnaryOp;
import io.github.riicarus.common.ast.stmt.CodeBlock;
import io.github.riicarus.common.ast.stmt.ctrl.*;
import io.github.riicarus.common.ast.stmt.decl.FieldDecl;
import io.github.riicarus.common.ast.stmt.decl.FuncDecl;
import io.github.riicarus.common.ast.stmt.decl.type.ArrayType;
import io.github.riicarus.common.ast.stmt.decl.type.BasicType;
import io.github.riicarus.common.ast.stmt.decl.type.FuncType;
import io.github.riicarus.common.ast.stmt.decl.type.TypeDecl;
import io.github.riicarus.front.lex.LexSymbol;
import io.github.riicarus.front.lex.Lexer;
import io.github.riicarus.front.lex.LitKind;
import io.github.riicarus.front.lex.Token;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Syntax analyzer.
 *
 * @author Riicarus
 * @create 2024-1-15 12:21
 * @since 1.0.0
 */
public class Syntaxer {

    private interface ListUpdater {

        boolean apply(Token token);
    }

    private final Lexer lexer = new Lexer();    // lexical analyzer

    private boolean isDebug;
    private Token token;    // current token
    private Token nt;   // next preview token

    public void init(String path, boolean isDebug) {
        this.isDebug = isDebug;
        nt = null;
        token = null;
        lexer.init(path, this.isDebug);
        next();
    }

    /**
     * Parse a list of elements(tokens), which is separated by sep and closed by close, each will be applied to updater function.
     * <p>
     * This progress will stop when updater function returns true or meet close or EOF.
     *
     * @param context String, name the list method is doing
     * @param sep     String, list element separator
     * @param close   String, list closed by close
     * @param updater ListUpdater, for each element, updater will be called and return if list method should stop
     * @return Position, position close token
     */
    @SuppressWarnings("SameParameterValue")    // avoid argument warning: actual argument is always ...
    private Position list(String context, LexSymbol sep, LexSymbol close, ListUpdater updater) {
        if (!LexSymbol.COMMA.equals(sep) && !LexSymbol.SEMICOLON.equals(sep))
            throw new IllegalArgumentException("Illegal sep argument for list method");
        if (!LexSymbol.RPAREN.equals(close) && !LexSymbol.RBRACK.equals(close) && !LexSymbol.RBRACE.equals(close) && !LexSymbol.SEMICOLON.equals(close))
            throw new IllegalArgumentException("Illegal close argument for list method");

        boolean done = false;
        while (!close.equals(token.getSymbol()) && !LexSymbol.EOF.equals(token.getSymbol()) && !done) {
            done = updater.apply(token);

            // should meet sep or close, sep is optional before close
            if (!got(sep) && !close.equals(token.getSymbol()))
                throw new IllegalStateException(String.format("in %s, expect %s or %s, but get %s", context, sep, close, token));
        }

        Position position = token.getPosition();
        // we haven't consume close in while loop
        want(close);
        return position;
    }

    private void want(LexSymbol symbol) {
        if (!got(symbol)) {
            throw new IllegalStateException("expect " + symbol + ", but get " + token.getSymbol());
            // advance(); consume some token ahead and continue parse progress
        }
    }

    private boolean got(LexSymbol symbol) {
        if (token.getSymbol().equals(symbol)) {
            next();
            return true;
        }
        return false;
    }

    private boolean is(LexSymbol symbol) {
        return token != null && token.getSymbol().equals(symbol);
    }

    private boolean is(Collection<LexSymbol> symbols) {
        return token != null && symbols.contains(token.getSymbol());
    }

    private void next() {
        if (token == null || !LexSymbol.EOF.equals(token.getSymbol())) {
            if (nt != null) {
                token = nt;
                nt = null;
            } else {
                token = lexer.next();
            }

            while (is(LexSymbol.COMMENT)) token = lexer.next();
        }
    }

    private Token preview() {
        if (nt != null) return nt;

        nt = lexer.next();
        return nt;
    }

    private void error(String s) throws IllegalStateException {
        throw new IllegalStateException("Syntax error: at " + token.getPosition() + ": " + s);
    }

    private void debug(String s) {
        if (isDebug) System.out.printf("%-20s%s%n", s, token);
    }

    // Stmt

    // Expr
    private Expr expr() {
        debug("Expr");

        return binaryExpr(null, -1);
    }

    /**
     * BinaryExpr:  UnaryExpr | BinaryExpr BinaryOp BinaryExpr
     *
     * @param x Expr, left hand side of binary operation
     * @param p int, priority of operator
     * @return Expr
     */
    @SuppressWarnings("SameParameterValue")
    private Expr binaryExpr(Expr x, int p) {
        debug("BinaryExpr");

        if (x == null) x = unaryExpr();

        Operator op;
        while ((is(UnaryOp.OP_MAP.keySet()) || is(BinaryOp.OP_MAP.keySet()))
                && (op = is(UnaryOp.OP_MAP.keySet()) ?
                UnaryOp.OP_MAP.get(token.getSymbol()) : BinaryOp.OP_MAP.get(token.getSymbol())).getPriority() > p) {
            final Operation t = new Operation();
            t.setPosition(token.getPosition());
            t.setOp(op);
            int tp = op.getPriority();
            next();
            t.setX(x);
            t.setY(binaryExpr(null, tp));
            x = t;
        }

        return x;
    }

    /**
     * UnaryExpr:   PrimaryExpr | UnaryOp UnaryExpr
     *
     * @return Expr
     */
    private Expr unaryExpr() {
        debug("UnaryExpr");

        if (is(UnaryOp.OP_MAP.keySet())) {  // is unary operation
            final Operation x = new Operation();
            x.setPosition(token.getPosition());
            x.setOp(UnaryOp.OP_MAP.get(token.getSymbol()));
            next();
            x.setX(unaryExpr());
            return x;
        }

        // is PrimaryExpr
        return primaryExpr(null);
    }

    /**
     * Operand: Literal | Id | "(" Expr ")" | NewArr <br/>
     * Literal: BasicLit | CompositeLit | FuncLit <br/>
     * NewArr:  "new" Type "[" Expr "]" [ CompositeList ]
     *
     * @return Expr
     */
    private Expr operand() {
        debug("Operand");

        // Id
        if (is(LexSymbol.IDENT)) return nameExpr();
        // BasicLit
        if (is(LexSymbol.LIT_SET)) return basicLit();
        // "(" Expr ")"
        if (got(LexSymbol.LPAREN)) {
            final Expr x = expr();
            want(LexSymbol.RPAREN);
            return x;
        }
        // NewArr
        if (is(LexSymbol.NEW)) return newArrExpr();

        // FuncLit
        if (is(LexSymbol.TYPE_SET)) return funcLit();

        throw new IllegalStateException("Illegal expression");
    }

    /**
     * PrimaryExpr: Operand <br/>
     * |    { "++" | "--" } PrimaryExpr <br/>
     * |    PrimaryExpr Index <br/>
     * |    PrimaryExpr Slice <br/>
     * |    PrimaryExpr TypeAssert <br/>
     * |    PrimaryExpr Arguments <br/>
     * |    PrimaryExpr { "++" | "--" } <br/>
     *
     * @param x Expr, operand
     * @return Expr
     */
    @SuppressWarnings("SameParameterValue")
    private Expr primaryExpr(Expr x) {
        debug("PrimaryExpr");

        if (x == null) {
            switch (token.getSymbol()) {
                case INC -> {
                    final IncExpr t = new IncExpr();
                    t.setPreOrPost(true);
                    t.setPosition(token.getPosition());
                    next();
                    t.setX(primaryExpr(null));
                    x = t;
                }
                case DEC -> {
                    final DecExpr t = new DecExpr();
                    t.setPreOrPost(true);
                    t.setPosition(token.getPosition());
                    next();
                    t.setX(primaryExpr(null));
                    x = t;
                }
                default -> x = operand();
            }
        }

        LOOP:
        for (; ; ) {
            final Position pos = token.getPosition();

            switch (token.getSymbol()) {
                case LBRACK -> {
                    next();

                    if (got(LexSymbol.RBRACK))  // "[" "]"
                        throw new IllegalStateException("Expect operand");

                    if (is(LexSymbol.COLON)) {  // "[" ":" [ Expr ] "]"
                        final SliceExpr t = new SliceExpr();
                        t.setX(x);
                        t.setPosition(pos);
                        if (!got(LexSymbol.RBRACK)) {
                            t.setIndex2(expr());
                            want(LexSymbol.RBRACK);
                        }
                        x = t;
                        break;
                    }

                    // "[" Expr ":" [ Expr ] "]"
                    final Expr idx1 = expr();
                    switch (token.getSymbol()) {
                        case COLON -> {
                            next();
                            final SliceExpr t = new SliceExpr();
                            t.setX(x);
                            t.setIndex1(idx1);
                            t.setPosition(pos);
                            if (!got(LexSymbol.RBRACK)) {
                                t.setIndex2(expr());
                                want(LexSymbol.RBRACK);
                            }
                            x = t;
                        }
                        case RBRACK -> {
                            next();
                            final IndexExpr t = new IndexExpr();
                            t.setX(x);
                            t.setIndex(idx1);
                            t.setPosition(pos);
                            x = t;
                        }
                        default -> throw new IllegalStateException("Expect : or [");
                    }
                }
                case PERIOD -> {
                    next();
                    if (got(LexSymbol.LPAREN)) {
                        final CastExpr t = new CastExpr();
                        t.setPosition(pos);
                        t.setX(x);
                        t.setType(typeDecl());
                        want(LexSymbol.RPAREN);
                        x = t;
                    } else throw new IllegalStateException("Expect (");
                }
                case LPAREN -> {
                    next();
                    final CallExpr t = new CallExpr();
                    t.setPosition(pos);
                    t.setFunc(x);
                    final List<Expr> params = new ArrayList<>();
                    t.setParams(params);
                    list("func call params", LexSymbol.COMMA, LexSymbol.RPAREN, token -> {
                        params.add(expr());
                        return false;
                    });
                    x = t;
                }
                case INC -> {
                    next();
                    final IncExpr t = new IncExpr();
                    t.setPosition(pos);
                    t.setPreOrPost(false);
                    t.setX(x);
                    x = t;
                }
                case DEC -> {
                    next();
                    final DecExpr t = new DecExpr();
                    t.setPosition(pos);
                    t.setPreOrPost(false);
                    t.setX(x);
                    x = t;
                }
                default -> {
                    break LOOP;
                }
            }
        }

        return x;
    }

    /**
     * NewArrExpr:  "new" Type "[" Expr "]" [ "{" Elements "}" ] <br/>
     * Elements:    Expr Elements' | e <br/>
     * Elements':   ", " Expr Elements' | e<br/>
     * <br/>
     * If there's no element or the composite element is null, all elements are initialized with default value.
     *
     * @return NewArrExpr
     */
    private ArrExpr newArrExpr() {
        debug("ArrExpr");

        want(LexSymbol.NEW);

        final ArrExpr x = new ArrExpr();
        x.setPosition(token.getPosition());

        TypeDecl baseType = typeDecl();
        x.setBaseType(baseType);

        want(LexSymbol.LBRACK);

        Expr size = expr();
        x.setSize(size);

        want(LexSymbol.RBRACK);

        if (is(LexSymbol.LBRACE)) {
            final CompositeLit elements = compositeLit();
            x.setLit(elements);
            want(LexSymbol.RBRACE);
        }

        return x;
    }

    /**
     * identifier
     *
     * @return NameExpr
     */
    private NameExpr nameExpr() {
        debug("NameExpr");

        if (!is(LexSymbol.IDENT))
            throw new IllegalStateException("Expect identifier");

        final NameExpr x = new NameExpr();
        x.setPosition(token.getPosition());
        x.setValue(token.getLexeme());
        next();

        return x;
    }

    private BasicLit basicLit() {
        debug("BasicLit");

        final BasicLit x = new BasicLit();
        try {
            x.setKind(LitKind.LIT_KIND_MAP.get(token.getSymbol()));
        } catch (ClassCastException e) {
            throw new IllegalStateException("Expect literal");
        }
        x.setValue(token.getLexeme());
        next();
        return x;
    }

    /**
     * CompositeLit:    "{" Elements "}" <br/>
     * Elements:    Element [ "," Elements ]
     *
     * @return CompositeLit
     */
    private CompositeLit compositeLit() {
        debug("CompositeLit");

        want(LexSymbol.LBRACE);
        final CompositeLit x = new CompositeLit();
        x.setPosition(token.getPosition());

        final List<Expr> elements = new ArrayList<>();
        x.setElements(elements);
        list("composite lit elements", LexSymbol.COMMA, LexSymbol.RBRACE, token -> {
            elements.add(bareCompositeLit());
            return false;
        });

        return x;
    }

    /**
     * Element: Expr | CompositeLit
     *
     * @return Expr
     */
    private Expr bareCompositeLit() {
        debug("Bare CompositeLit");

        if (is(LexSymbol.LBRACE)) return compositeLit();

        return expr();
    }

    /**
     * FuncLit: Type "(" FieldDecls ")" "->" Stmt
     *
     * @return FuncLit
     */
    private FuncLit funcLit() {
        debug("FuncLit");

        final FuncLit x = new FuncLit();
        x.setRetType(typeDecl());
        want(LexSymbol.LPAREN);
        final List<FieldDecl> params = new ArrayList<>();
        x.setParamDecls(params);
        list("func param decl", LexSymbol.COMMA, LexSymbol.RPAREN, token -> {
            if (got(LexSymbol.CONST))
                throw new IllegalStateException("Illegal statement");
            params.add(fieldDecl());
            return false;
        });
        want(LexSymbol.RARROW);
        x.setBody(stmt());

        return x;
    }

    /**
     * Program: Stmts
     *
     * @return Program
     */
    public Program program() {
        debug("Program");

        final Program x = new Program();
        x.setPosition(token.getPosition());
        x.setStmts(stmts(true));

        return x;
    }

    /**
     * CodeBlock:   "{" Stmts "}"
     *
     * @return CodeBlock
     */
    private CodeBlock codeBlock() {
        debug("CodeBlock");

        want(LexSymbol.LBRACE);
        final CodeBlock x = new CodeBlock();
        x.setPosition(token.getPosition());
        x.setStmts(stmts(true));
        want(LexSymbol.RBRACE);

        return x;
    }

    /**
     * Stmts:   [ Stmt Stmts ]
     *
     * @return List&lt;Stmt&gt;
     */
    @SuppressWarnings("SameParameterValue")
    private List<Stmt> stmts(boolean report) {
        debug("Stmts");

        final List<Stmt> stmts = new ArrayList<>();
        Stmt stmt;
        try {
            while ((stmt = stmt()) != null) stmts.add(stmt);
        } catch (IllegalStateException e) {
            if (report) throw e;
        }

        return stmts;
    }

    /**
     * Stmt:    Decl
     * |    Expr ";"
     * |    Control
     * |    CodeBlock
     * |    ";"
     *
     * @return Stmt
     */
    private Stmt stmt() {
        debug("Stmt");

        return switch (token.getSymbol()) {
            case CONST, INT, FLOAT, BOOL, CHAR, STRING, VOID -> decl();
            case IDENT, LPAREN, INC, DEC, NOT, LNOT, NEW -> {
                final Expr expr = expr();
                want(LexSymbol.SEMICOLON);
                yield expr;
            }
            case BREAK, CONTINUE, RETURN, IF, SWITCH, FOR, WHILE -> control();
            case LBRACE -> codeBlock();
            case SEMICOLON -> new EmptyStmt();
            case EOF, RBRACE -> null;
            default -> throw new IllegalStateException("Illegal statement");
        };
    }

    // control

    /**
     * Control: Break
     * |   Continue
     * |   Return
     * |   If
     * |   Switch
     * |   For
     * |   While
     *
     * @return Control
     */
    private Ctrl control() {
        debug("Control");

        return switch (token.getSymbol()) {
            case BREAK -> breakStmt();
            case CONTINUE -> continueStmt();
            case RETURN -> retStmt();
            case IF -> ifStmt();
            case SWITCH -> switchStmt();
            case FOR -> forStmt();
            case WHILE -> whileStmt();
            default -> throw new IllegalStateException("Illegal statement");
        };
    }

    /**
     * Break:   "break" ";"
     *
     * @return BreakStmt
     */
    private BreakStmt breakStmt() {
        debug("BreakStmt");

        final BreakStmt x = new BreakStmt();
        x.setPosition(token.getPosition());
        want(LexSymbol.BREAK);
        want(LexSymbol.SEMICOLON);
        return x;
    }

    /**
     * Continue:    "Continue" ";"
     *
     * @return ContinueStmt
     */
    private ContinueStmt continueStmt() {
        debug("ContinueStmt");

        final ContinueStmt x = new ContinueStmt();
        x.setPosition(token.getPosition());
        want(LexSymbol.CONTINUE);
        want(LexSymbol.SEMICOLON);
        return x;
    }

    /**
     * Return:  "return" [ Expr ] ";"
     *
     * @return RetStmt
     */
    private RetStmt retStmt() {
        debug("RetStmt");

        final RetStmt x = new RetStmt();
        x.setPosition(token.getPosition());
        want(LexSymbol.RETURN);

        if (got(LexSymbol.SEMICOLON)) return x;

        x.setRetVal(expr());
        want(LexSymbol.SEMICOLON);
        return x;
    }

    /**
     * If:  "if" "(" Expr ")" { Stmt | ";" }
     * [ { "elseif" "(" Expr ")" { Stmt | ";" } }... ]
     * [ "else" { Stmt | ";" } ]
     *
     * @return IfStmt
     */
    private IfStmt ifStmt() {
        debug("IfStmt");

        final IfStmt x = new IfStmt();
        x.setPosition(token.getPosition());
        want(LexSymbol.IF);
        want(LexSymbol.LPAREN);
        x.setCond(expr());
        want(LexSymbol.RPAREN);

        if (!got(LexSymbol.SEMICOLON)) x.setThen(stmt());

        final List<ElseifStmt> elseifStmts = new ArrayList<>();
        x.setElseIfs(elseifStmts);
        while (got(LexSymbol.ELSE_IF)) {
            final ElseifStmt elseifStmt = new ElseifStmt();
            elseifStmt.setPosition(token.getPosition());
            want(LexSymbol.LPAREN);
            elseifStmt.setCond(expr());
            want(LexSymbol.RPAREN);
            if (!got(LexSymbol.SEMICOLON)) elseifStmt.setThen(stmt());
            elseifStmts.add(elseifStmt);
        }

        if (got(LexSymbol.ELSE)) {
            if (!got(LexSymbol.SEMICOLON)) x.setElse(stmt());
        }

        return x;
    }

    /**
     * Switch:  "switch" "(" Expr ")" "{" [ { "case" Expr ":" Stmt }... ] [ "default" ":" Stmt ] "}"
     *
     * @return SwitchStmt
     */
    private SwitchStmt switchStmt() {
        final SwitchStmt x = new SwitchStmt();
        x.setPosition(token.getPosition());
        want(LexSymbol.SWITCH);
        want(LexSymbol.LPAREN);
        x.setX(expr());
        want(LexSymbol.RPAREN);
        want(LexSymbol.LBRACE);

        final List<CaseStmt> caseStmts = new ArrayList<>();
        x.setCases(caseStmts);
        while (got(LexSymbol.CASE)) {
            final CaseStmt caseStmt = new CaseStmt();
            caseStmt.setPosition(token.getPosition());
            caseStmt.setCond(expr());
            want(LexSymbol.COLON);
            caseStmt.setBody(stmt());
            caseStmts.add(caseStmt);
        }

        if (got(LexSymbol.DEFAULT)) {
            want(LexSymbol.COLON);
            x.setDefault(stmt());
        }

        want(LexSymbol.RBRACE);

        return x;
    }

    /**
     * For: "for" "(" [ { [ Type ] Id "=" Expr "," }... ] ";" [ Expr ] ";" [ { [ Type ] Id "=" Expr "," }... ] ")" { Stmt | ";" }
     *
     * @return ForStmt
     */
    private ForStmt forStmt() {
        debug("ForStmt");

        final ForStmt x = new ForStmt();
        x.setPosition(token.getPosition());
        want(LexSymbol.FOR);
        want(LexSymbol.LPAREN);
        if (!got(LexSymbol.SEMICOLON)) {
            final List<SimpleStmt> inits = new ArrayList<>();
            x.setInits(inits);
            list("for inits", LexSymbol.COMMA, LexSymbol.SEMICOLON, token -> {
                inits.add(simpleStmt());
                return false;
            });
        }

        if (!got(LexSymbol.SEMICOLON)) {
            x.setCond(expr());
            want(LexSymbol.SEMICOLON);
        }

        if (!got(LexSymbol.RPAREN)) {
            final List<SimpleStmt> updates = new ArrayList<>();
            x.setUpdates(updates);
            list("for updates", LexSymbol.COMMA, LexSymbol.RPAREN, token -> {
                updates.add(simpleStmt());
                return false;
            });
        }

        if (!got(LexSymbol.SEMICOLON)) x.setBody(stmt());

        return x;
    }

    /**
     * While:   "while" "(" Expr ")" { Stmt | ";" }
     *
     * @return WhileStmt
     */
    private WhileStmt whileStmt() {
        debug("WhileStmt");

        final WhileStmt x = new WhileStmt();
        x.setPosition(token.getPosition());
        want(LexSymbol.WHILE);
        want(LexSymbol.LPAREN);
        x.setCond(expr());
        want(LexSymbol.RPAREN);
        if (!got(LexSymbol.SEMICOLON)) x.setBody(stmt());

        return x;
    }

    /**
     * VarAssign:   FieldDecl | Expr
     *
     * @return SimpleStmt
     */
    private SimpleStmt simpleStmt() {
        debug("SimpleStmt");

        return switch (token.getSymbol()) {
            case INT, FLOAT, BOOL, CHAR, STRING, VOID -> fieldDecl();
            case IDENT, LPAREN, INC, DEC, NOT, LNOT, NEW -> expr();
            default -> throw new IllegalStateException("Illegal statement");
        };
    }

    // Decl

    /**
     * Decl:    [ "const" ] Type Id [ "=" Expr ] ";" <br/>
     * |   FuncDecl
     *
     * @return Decl
     */
    private Decl decl() {
        debug("Decl");

        boolean isConst = got(LexSymbol.CONST);

        final TypeDecl type = typeDecl();

        if (got(LexSymbol.FUNC) && is(LexSymbol.IDENT)) return funcDecl(type);

        final FieldDecl x = new FieldDecl();
        x.setType(type);
        x.setConst(isConst);
        x.setPosition(token.getPosition());
        x.setName(nameExpr());

        if (got(LexSymbol.ASSIGN)) {
            final AssignExpr assign = new AssignExpr();
            assign.setPosition(token.getPosition());
            assign.setX(x.getName());
            assign.setOp(BinaryOp.ASSIGN);
            assign.setY(expr());
            x.setAssign(assign);
        }

        want(LexSymbol.SEMICOLON);

        return x;
    }

    /**
     * FuncDecl:    Type "func" Id "(" [ { Type Id [ "=" Expr ] }... ] ")" CodeBlock
     *
     * @return FuncDecl
     */
    private FuncDecl funcDecl(TypeDecl retType) {
        debug("FuncDecl");

        final FuncDecl x = new FuncDecl();
        x.setPosition(token.getPosition());
        x.setFuncName(nameExpr());
        want(LexSymbol.LPAREN);
        final List<FieldDecl> params = new ArrayList<>();
        list("func param decl", LexSymbol.COMMA, LexSymbol.RPAREN, token -> {
            if (got(LexSymbol.CONST))
                throw new IllegalStateException("Illegal statement");
            params.add(fieldDecl());
            return false;
        });
        final FuncLit lit = new FuncLit();
        x.setFuncLit(lit);
        lit.setRetType(retType);
        lit.setParamDecls(params);
        lit.setPosition(retType.getPosition());
        lit.setBody(codeBlock());

        return x;
    }

    /**
     * FieldDecl:   [ "const" ] Type Id [ "=" Expr ]
     *
     * @return FieldDecl
     */
    private FieldDecl fieldDecl() {
        debug("FieldDecl");

        final FieldDecl x = new FieldDecl();
        x.setConst(got(LexSymbol.CONST));
        x.setType(typeDecl());
        x.setPosition(token.getPosition());
        x.setName(nameExpr());

        if (got(LexSymbol.ASSIGN)) {
            final AssignExpr assign = new AssignExpr();
            assign.setPosition(token.getPosition());
            assign.setX(x.getName());
            assign.setOp(BinaryOp.ASSIGN);
            assign.setY(expr());
            x.setAssign(assign);
        }

        return x;
    }

    /**
     * TypeDecl:    BasicType ExtType <br/>
     * ExtType:     [ { "func" "(" ParamTypeDecls ")" ExtType } | { "[" "]" } ]
     *
     * @return FuncDecl
     */
    private TypeDecl typeDecl() {
        debug("TypeDecl");

        BasicType basicType = basicType();

        TypeDecl x;
        if (is(LexSymbol.FUNC) && LexSymbol.LPAREN.equals(preview().getSymbol())) x = funcType(basicType);
        else if (got(LexSymbol.LBRACK)) x = arrayType(basicType);
        else x = basicType;

        return x;
    }

    private BasicType basicType() {
        debug("BasicType");

        BasicType x = switch (token.getSymbol()) {
            case INT -> BasicType.INT;
            case FLOAT -> BasicType.FLOAT;
            case BOOL -> BasicType.BOOL;
            case CHAR -> BasicType.CHAR;
            case STRING -> BasicType.STRING;
            case VOID -> BasicType.VOID;
            default -> throw new IllegalStateException("Illegal basic type");
        };

        x.setPosition(token.getPosition());

        next();
        return x;
    }

    private ArrayType arrayType(TypeDecl baseType) {
        debug("ArrayType");

        want(LexSymbol.RBRACK);
        final ArrayType x = new ArrayType();
        x.setPosition(baseType.getPosition());
        x.setEleType(baseType);
        return x;
    }

    /**
     * Type "func" "(" {Type "," Type "," ...} ")"
     *
     * @param retType TypeDecl, function's return type
     * @return FuncType, function type
     */
    private FuncType funcType(TypeDecl retType) {
        debug("FuncType");

        want(LexSymbol.FUNC);
        final FuncType x = new FuncType();
        x.setRetType(retType);

        want(LexSymbol.LPAREN);
        List<TypeDecl> paramTypeDecls = new ArrayList<>();
        Position position = list("func param type decl", LexSymbol.COMMA, LexSymbol.RPAREN, token -> {
            paramTypeDecls.add(typeDecl());
            return false;
        });

        x.setPosition(position);
        x.setParamTypeDecls(paramTypeDecls);

        return x;
    }
}