package io.github.riicarus.front.semantic.types;

import io.github.riicarus.common.Position;
import io.github.riicarus.common.ast.Decl;

/**
 * Element in scope
 *
 * @author Riicarus
 * @create 2024-1-26 17:08
 * @since 1.0.0
 */
public class Element {

    /**
     * element's declaration scope
     */
    private Scope scope;
    /**
     * element's root scope
     */
    private Scope rootScope;
    /**
     * element's declaration position
     */
    private Position pos;
    /**
     * element's name
     */
    private String name;
    /**
     * element's type
     */
    private Type type;
    /**
     * element's type decl node
     */
    private Decl typeDecl;
    /**
     * Is element declared as a const value
     */
    private boolean isConst;
    /**
     * Returns the declaration order of element. <br/>
     * If a is declared before b, a.order() < b.order() <br/>
     * Order in the same pkg is always great than 0. <br/>
     */
    private int order;

    /* **************************************************************
     * Getters and Setters
     *************************************************************** */

    public Scope getScope() {
        return scope;
    }

    public void setScope(Scope scope) {
        this.scope = scope;
    }

    public Scope getRootScope() {
        return rootScope;
    }

    public void setRootScope(Scope rootScope) {
        this.rootScope = rootScope;
    }

    public Position getPos() {
        return pos;
    }

    public void setPos(Position pos) {
        this.pos = pos;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Decl getTypeDecl() {
        return typeDecl;
    }

    public void setTypeDecl(Decl typeDecl) {
        this.typeDecl = typeDecl;
    }

    public boolean isConst() {
        return isConst;
    }

    public void setConst(boolean aConst) {
        isConst = aConst;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
