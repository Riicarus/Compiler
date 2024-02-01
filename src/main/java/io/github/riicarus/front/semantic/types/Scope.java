package io.github.riicarus.front.semantic.types;

import io.github.riicarus.common.Position;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Scope of elements
 *
 * @author Riicarus
 * @create 2024-1-26 17:05
 * @since 1.0.0
 */
public class Scope {

    private Scope parent;
    private final List<Scope> children = new LinkedList<>();
    private final Map<String, Element> elements = new HashMap<>();
    private Position start;
    private Position end;
    private String comment;
    private boolean isFunc;

    public Element lookup(String name) {
        return elements.get(name);
    }

    public Element lookupAll(String name) {
        Element x = elements.get(name);
        if (x == null && parent != null) x = parent.lookupAll(name);
        return x;
    }

    public void add(String name, Element e) {
        elements.put(name, e);
    }

    public void addChild(Scope child) {
        children.add(child);
    }

    /* ***************************************************************
    * Getters and Setters
    **************************************************************** */
    public Scope getParent() {
        return parent;
    }

    public void setParent(Scope parent) {
        this.parent = parent;
    }

    public List<Scope> getChildren() {
        return children;
    }

    public Map<String, Element> getElements() {
        return elements;
    }

    public Position getStart() {
        return start;
    }

    public void setStart(Position start) {
        this.start = start;
    }

    public Position getEnd() {
        return end;
    }

    public void setEnd(Position end) {
        this.end = end;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public boolean isFunc() {
        return isFunc;
    }

    public void setFunc(boolean func) {
        isFunc = func;
    }
}
