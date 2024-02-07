package io.github.riicarus.front.semantic;

import io.github.riicarus.common.ast.CodeFile;
import io.github.riicarus.front.semantic.types.Scope;

/**
 * Semantic analyzer
 *
 * @author Riicarus
 * @create 2024-1-30 13:46
 * @since 1.0.0
 */
public class Checker {

    /**
     * Root AST node
     */
    private CodeFile codeFile;
    /**
     * Root scope
     */
    private Scope rootScope;
    private Scope curScope;
    private boolean debug;

    private int loopCnt;

    /**
     * @param codeFile CodeFile, Root AST node
     * @param debug    boolean, is analyzer in debug mode
     */
    public void init(CodeFile codeFile, boolean debug) {
        this.codeFile = codeFile;
        this.rootScope = codeFile.getScope();
        this.curScope = this.rootScope;
        this.debug = debug;
        this.loopCnt = 0;
    }

    public void enter(String name) {
        curScope = curScope.enter(name);
    }

    public void exit() {
        curScope = curScope.exit();
    }

    /* **************************************************************
     * Getters and Setters
     *************************************************************** */

    public CodeFile getCodeFile() {
        return codeFile;
    }

    public void setCodeFile(CodeFile codeFile) {
        this.codeFile = codeFile;
    }

    public Scope getRootScope() {
        return rootScope;
    }

    public void setRootScope(Scope rootScope) {
        this.rootScope = rootScope;
    }

    public Scope getCurScope() {
        return curScope;
    }

    public void setCurScope(Scope curScope) {
        this.curScope = curScope;
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public int getLoopCnt() {
        return loopCnt;
    }

    public void setLoopCnt(int loopCnt) {
        this.loopCnt = loopCnt;
    }
}
