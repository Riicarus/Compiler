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
    private String name;
    private boolean isFunc;
    private boolean collecting = true;

    public Element lookup(String name) {
        return elements.get(name);
    }

    public Element lookupAll(String name) {
        Element x = elements.get(name);
        if (x == null && parent != null) x = parent.lookupAll(name);
        return x;
    }

    public void addEle(String name, Element e) {
        elements.put(name, e);
    }

    public Scope enter(String name) {
        if (collecting) {
            Scope child = new Scope();
            child.setName(name + "#" + children.size());
            children.add(child);
            child.setParent(this);
            return child;
        }

        for (Scope scope : children) {
            if (scope.name.equals(name)) return scope;
        }

        return this;
    }

    public Scope exit() {
        if (isCollecting()) collecting = false;
        return getParent();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Scope scope)) return false;

        if (getParent() != null ? !getParent().equals(scope.getParent()) : scope.getParent() != null) return false;
        return getName() != null ? getName().equals(scope.getName()) : scope.getName() == null;
    }

    @Override
    public int hashCode() {
        int result = getParent() != null ? getParent().hashCode() : 0;
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return String.format("%-50s  %-20s  %-20s  (%s)", getFullName(), start, end, isFunc ? "Func" : "Normal");
    }

    /* ***************************************************************
     * Getters and Setters
     **************************************************************** */
    public Scope getParent() {
        return parent == null ? this : parent;
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

    public String getFullName() {
        return (parent == null ? "" : parent.getFullName() + ".") + name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isFunc() {
        return isFunc;
    }

    public void setFunc(boolean func) {
        isFunc = func;
    }

    public boolean isCollecting() {
        return collecting;
    }

    public void setCollecting(boolean collecting) {
        this.collecting = collecting;
    }
}
