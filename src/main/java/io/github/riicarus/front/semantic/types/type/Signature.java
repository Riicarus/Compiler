package io.github.riicarus.front.semantic.types.type;

import io.github.riicarus.front.semantic.types.Type;

import java.util.List;

import static java.util.stream.Collectors.joining;

/**
 * Func signature
 *
 * @author Riicarus
 * @create 2024-1-26 17:51
 * @since 1.0.0
 */
public class Signature implements Type {

    private Type retType;
    private List<Type> paramTypes;
    private String name;

    @Override
    public Type underlying() {
        return retType;
    }

    @Override
    public String naming() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Signature signature)) return false;

        if (getRetType() != null ? !getRetType().equals(signature.getRetType()) : signature.getRetType() != null)
            return false;
        return getParamTypes() != null ? getParamTypes().equals(signature.getParamTypes()) : signature.getParamTypes() == null;
    }

    @Override
    public int hashCode() {
        int result = getRetType() != null ? getRetType().hashCode() : 0;
        result = 31 * result + (getParamTypes() != null ? getParamTypes().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return String.format("%s (%s) -> %s", name == null ? "_" : name, paramTypes.stream().map(Type::toString).collect(joining(", ")), retType);
    }

    public Type getRetType() {
        return retType;
    }

    public void setRetType(Type retType) {
        this.retType = retType;
    }

    public List<Type> getParamTypes() {
        return paramTypes;
    }

    public void setParamTypes(List<Type> paramTypes) {
        this.paramTypes = paramTypes;
    }

    public void setName(String name) {
        this.name = name;
    }
}
