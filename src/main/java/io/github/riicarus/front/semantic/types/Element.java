package io.github.riicarus.front.semantic.types;

import io.github.riicarus.common.Position;
import io.github.riicarus.common.ast.Decl;
import io.github.riicarus.front.semantic.types.type.Signature;

/**
 * Element in scope <br/>
 * Equals Rule: <br/>
 * If elements' type is signature, their name and param type must be the same; <br/>
 * Otherwise, only need their name to be the same.
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Element element)) return false;

        if (getType() instanceof Signature) {
            return name.equals(element.getName()) && getType().equals(element.getType());
        }

        return name.equals(element.getName());
    }

    @Override
    public int hashCode() {
        int result = getName() != null ? getName().hashCode() : 0;
        result = 31 * result + (getType() != null && getType() instanceof Signature ? getType().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return String.format("%-15s  %-5s  %-3d  %s", name, isConst ? "const" : "", order, type);
    }

    /* **************************************************************
     * Getters and Setters
     *************************************************************** */

    public Scope getScope() {
        return scope;
    }

    public Element setScope(Scope scope) {
        this.scope = scope;
        return this;
    }

    public Scope getRootScope() {
        return rootScope;
    }

    public Element setRootScope(Scope rootScope) {
        this.rootScope = rootScope;
        return this;
    }

    public Position getPos() {
        return pos;
    }

    public Element setPos(Position pos) {
        this.pos = pos;
        return this;
    }

    public String getName() {
        return name;
    }

    public Element setName(String name) {
        this.name = name;
        return this;
    }

    public Type getType() {
        return type;
    }

    public Element setType(Type type) {
        this.type = type;
        return this;
    }

    public Decl getTypeDecl() {
        return typeDecl;
    }

    public Element setTypeDecl(Decl typeDecl) {
        this.typeDecl = typeDecl;
        return this;
    }

    public boolean isConst() {
        return isConst;
    }

    public Element setConst(boolean isConst) {
        this.isConst = isConst;
        return this;
    }

    public int getOrder() {
        return order;
    }

    public Element setOrder(int order) {
        this.order = order;
        return this;
    }
}
