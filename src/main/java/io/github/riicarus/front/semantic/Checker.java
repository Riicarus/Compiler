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
    private boolean debug;

    /**
     * @param codeFile CodeFile, Root AST node
     * @param debug    boolean, is analyzer in debug mode
     */
    public void init(CodeFile codeFile, boolean debug) {
        this.codeFile = codeFile;
        this.rootScope = codeFile.getScope();
        this.debug = debug;
    }

}
