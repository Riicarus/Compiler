package io.github.riicarus.front.semantic.types.type;

import io.github.riicarus.front.semantic.types.Type;

import java.util.List;
import java.util.Objects;

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
    private List<Type> paramType;
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

        if (getParamType() != null ? !getParamType().equals(signature.getParamType()) : signature.getParamType() != null)
            return false;
        return Objects.equals(name, signature.name);
    }

    @Override
    public int hashCode() {
        int result = getParamType() != null ? getParamType().hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return String.format("%s (%s) -> %s", name, paramType.stream().map(Type::toString).collect(joining(", ")), retType);
    }

    public Type getRetType() {
        return retType;
    }

    public void setRetType(Type retType) {
        this.retType = retType;
    }

    public List<Type> getParamType() {
        return paramType;
    }

    public void setParamType(List<Type> paramType) {
        this.paramType = paramType;
    }

    public void setName(String name) {
        this.name = name;
    }
}
