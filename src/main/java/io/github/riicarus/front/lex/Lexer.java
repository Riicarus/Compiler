package io.github.riicarus.front.lex;

import io.github.riicarus.common.Position;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.joining;

/**
 * Lexical analyzer
 *
 * @author Riicarus
 * @create 2024-1-12 14:48
 * @since 1.0.0
 */
public class Lexer {

    private static final char EOF = (char) -1;

    private boolean debug;  // is in debug mod

    private String filename;    // source file name, there could be more attributes like: directory
    private char[] src; // src file buffer

    private char ch;    // current char
    private int offset; // current char offset(offset of ch)
    private int lineOffset; // current line offset(row)
    private int inlineOffset;   // current column offset of line(col)

    /**
     * init(String, boolean) initializes Lexer's init attributes, preparing for lex scan in the next file.
     *
     * @param path  String, src code file path of next scan.
     * @param debug boolean, is next scan in debug mod.
     */
    public void init(String path, boolean debug) {
        this.debug = debug;
        int idx = path.lastIndexOf(File.separator);
        this.filename = path.substring(idx + 1);

        try {
            StringBuilder sb = new StringBuilder();
            Files.readAllLines(Path.of(path)).forEach(l -> sb.append(l).append("\n"));
            src = sb.toString().toCharArray();
        } catch (IOException e) {
            throw new IllegalStateException("Lexer: Can not read file from: " + path);
        }

        ch = EOF;
        offset = -1;
        lineOffset = 1;
        inlineOffset = 0;
    }

    /**
     * scan() completely scans the given src file in previous init(String, boolean) call,
     * returns scanned token list.
     *
     * @return List&lt;Token&gt;, scanned token list
     */
    public List<Token> scan() {
        List<Token> tokenList = new ArrayList<>();
        List<Token> illegalTokenList = new ArrayList<>();
        Token token;
        while ((token = next()).getSymbol() != LexSymbol.EOF) {
            if (token.getSymbol() == LexSymbol.ILLEGAL) illegalTokenList.add(token);
            tokenList.add(token);
        }

        if (debug) {
            System.out.print("Lexer tokens:\r\n\t");
            System.out.println(tokenList.stream().map(Token::toString).collect(joining("\r\n\t")));
        }

        if (!illegalTokenList.isEmpty()) {
            throw new IllegalStateException("Lexer scanned illegal symbol(s):\r\n\t"
                    + illegalTokenList.stream().map(Token::toString).collect(joining("\r\n\t")));
        }

        return tokenList;
    }

    /**
     * next() scans the given src file in previous int(String, boolean) call, but only scans <b>one</b> token and returns it.
     *
     * @return Token, next scanned token.
     */
    public Token next() {
        nextSkipWhiteSpace();

        Position position = new Position(filename, offset, lineOffset, inlineOffset);

        // keywords / identifier
        if ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z') || ch == '_') {
            String word = scanWord();
            Token token;
            final LexSymbol symbol = LexSymbol.lookUpReserved(word);
            if (symbol == null) token = new Token(LexSymbol.IDENT, word, position);
            else token = new Token(symbol, position);

            return token;
        }

        // values_lit
        // int / float
        if ((ch >= '0' && ch <= '9')) {
            boolean isFloat = false;
            String number = scanNumber();
            if (ch == '.') {
                number += ch;
                isFloat = true;
            }
            number += scanNumber();
            return new Token(isFloat ? LexSymbol.FLOAT_LIT : LexSymbol.INT_LIT, number, position);
        }
        // string
        if (ch == '"') {
            int off = 1;
            String lit;
            while (offset + off < src.length && src[offset + off] != '"') {
                // the string can only be declared in one line
                if (src[offset + off] == '\n') {
                    lit = String.valueOf(src, offset + 1, off - 1);
                    // do not eat the '\n'
                    offset += off - 1;
                    return new IllegalToken(LexSymbol.ILLEGAL, ch + lit, position, "Illegal line end in string literal.");
                }

                off++;
            }
            lit = String.valueOf(src, offset + 1, off - 1);

            // update offset to offset of matched right '"'
            offset += off;

            // if no right '"' to match, make it a wrong token.
            if (offset >= src.length) {
                return new IllegalToken(LexSymbol.ILLEGAL, "\"" + lit, position, "Illegal line end in string literal.");
            }

            return new Token(LexSymbol.STRING_LIT, lit, position);
        }
        // char
        if (ch == '\'') {
            nextChar();
            if (ch == '\'')
                return new IllegalToken(LexSymbol.ILLEGAL, "''", position, "Empty character literal");

            String lit = String.valueOf(ch);
            nextChar();
            if (ch != '\'') {
                contract();
                return new IllegalToken(LexSymbol.ILLEGAL, "'" + ch, position, "Unclosed character literal");
            }
            return new Token(LexSymbol.CHAR_LIT, lit, position);
        }

        // symbols
        // non-prefix char symbols
        if (ch == '(') return new Token(LexSymbol.LPAREN, position);
        if (ch == ')') return new Token(LexSymbol.RPAREN, position);
        if (ch == '[') return new Token(LexSymbol.LBRACK, position);
        if (ch == ']') return new Token(LexSymbol.RBRACK, position);
        if (ch == '{') return new Token(LexSymbol.LBRACE, position);
        if (ch == '}') return new Token(LexSymbol.RBRACE, position);
        if (ch == ',') return new Token(LexSymbol.COMMA, position);
        if (ch == '.') return new Token(LexSymbol.PERIOD, position);
        if (ch == ';') return new Token(LexSymbol.SEMICOLON, position);
        if (ch == ':') return new Token(LexSymbol.COLON, position);
        if (ch == EOF) return new Token(LexSymbol.EOF, position);
        if (ch == '\n') {
            newLine();
            return next();
        }

        // prefixed char symbols
        char pch = ch;
        nextChar();

        // newline: \r\n
        if (pch == '\r') {
            if (ch == '\n') {
                newLine();
                return next();
            }
            return new Token(LexSymbol.ILLEGAL, String.valueOf(pch) + ch, position);
        }
        // LT, LE, SHL, SHL_ASSIGN: <, <=, <<, <<=
        if (pch == '<') {
            if (ch == '=') return new Token(LexSymbol.LE, position);
            if (ch == '<') {
                nextChar();
                if (ch == '=') return new Token(LexSymbol.SHL_ASSIGN, position);

                contract();
                return new Token(LexSymbol.SHL, position);
            }

            contract();
            return new Token(LexSymbol.LT, position);
        }
        // GT, GE, SHR, SHR_ASSIGN: >, >=, >>, >>=
        if (pch == '>') {
            if (ch == '=') return new Token(LexSymbol.GE, position);
            if (ch == '>') {
                nextChar();
                if (ch == '=') return new Token(LexSymbol.SHR_ASSIGN, position);

                contract();
                return new Token(LexSymbol.SHR, position);
            }

            contract();
            return new Token(LexSymbol.GT, position);
        }
        // ASSIGN, EQ: =, ==
        if (pch == '=') {
            if (ch == '=') return new Token(LexSymbol.EQ, position);

            contract();
            return new Token(LexSymbol.ASSIGN, position);
        }
        // NOT, NE: !, !=
        if (pch == '!') {
            if (ch == '=') return new Token(LexSymbol.NE, position);

            contract();
            return new Token(LexSymbol.NOT, position);
        }
        // ADD, INC, ADD_ASSIGN: +, ++, +=
        if (pch == '+') {
            if (ch == '+') return new Token(LexSymbol.INC, position);
            if (ch == '=') return new Token(LexSymbol.ADD_ASSIGN, position);

            contract();
            return new Token(LexSymbol.ADD, position);
        }
        // SUB, DEC, SUB_ASSIGN, RARROW: -, --, -=, ->
        if (pch == '-') {
            if (ch == '-') return new Token(LexSymbol.DEC, position);
            if (ch == '=') return new Token(LexSymbol.SUB_ASSIGN, position);
            if (ch == '>') return new Token(LexSymbol.RARROW, position);

            contract();
            return new Token(LexSymbol.SUB, position);
        }
        // MUL, MUL_ASSIGN: *, *=
        if (pch == '*') {
            if (ch == '=') return new Token(LexSymbol.MUL_ASSIGN, position);

            contract();
            return new Token(LexSymbol.MUL, position);
        }
        // QUO, QUO_ASSIGN, COMMENT: /, /=, //
        if (pch == '/') {
            if (ch == '=') return new Token(LexSymbol.QUO_ASSIGN, position);
            if (ch == '/') {
                int off = 1;
                while (src[offset + off] != '\n') {
                    off++;
                }
                String comment = String.valueOf(src, offset + 1, off - 1);
                // do not eat '\n'
                offset += off - 1;

                return new Token(LexSymbol.COMMENT, comment, position);
            }

            contract();
            return new Token(LexSymbol.QUO, position);
        }
        // REM, REM_ASSIGN: %, %=
        if (pch == '%') {
            if (ch == '=') return new Token(LexSymbol.REM_ASSIGN, position);

            contract();
            return new Token(LexSymbol.REM, position);
        }
        // AND, AND_ASSIGN, LAND: &, &=, &&
        if (pch == '&') {
            if (ch == '=') return new Token(LexSymbol.AND_ASSIGN, position);
            if (ch == '&') return new Token(LexSymbol.LAND, position);

            contract();
            return new Token(LexSymbol.AND, position);
        }
        // OR, OR_ASSIGN, LOR: |, |=, ||
        if (pch == '|') {
            if (ch == '=') return new Token(LexSymbol.OR_ASSIGN, position);
            if (ch == '|') return new Token(LexSymbol.LOR, position);

            contract();
            return new Token(LexSymbol.OR, position);
        }
        // XOR, XOR_ASSIGN: ^, ^=
        if (pch == '^') {
            if (ch == '=') return new Token(LexSymbol.XOR_ASSIGN, position);

            contract();
            return new Token(LexSymbol.XOR, position);
        }

        return new Token(LexSymbol.ILLEGAL, String.valueOf(ch), position);
    }

    private void nextChar() {
        if (offset < src.length - 1) {
            ch = src[++offset];
            inlineOffset++;
        } else {
            offset = src.length - 1;
            ch = EOF;
        }
    }

    private void contract() {
        if (offset > 0) {
            ch = src[--offset];
            inlineOffset--;
        }
    }

    private void newLine() {
        lineOffset++;
        inlineOffset = 0;
    }

    private void nextSkipWhiteSpace() {
        do {
            nextChar();
        } while (ch == ' ');
    }

    private String scanWord() {
        StringBuilder sb = new StringBuilder();
        while ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z') || (ch >= '0' && ch <= '9') || ch == '_') {
            sb.append(ch);
            nextChar();
        }

        return sb.toString();
    }

    private String scanNumber() {
        StringBuilder sb = new StringBuilder();
        while ((ch >= '0' && ch <= '9')) {
            sb.append(ch);
            nextChar();
        }

        return sb.toString();
    }
}
