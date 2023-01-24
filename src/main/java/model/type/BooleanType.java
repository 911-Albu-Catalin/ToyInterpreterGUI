package model.type;

import model.value.BooleanValue;
import model.value.IValue;

public class BooleanType implements IType {
    @Override
    public boolean equals(IType anotherType) {
        return anotherType instanceof BooleanType;
    }

    @Override
    public IValue defaultValue() {
        return new BooleanValue(false);
    }

    @Override
    public IType deepCopy() {
        return new BooleanType();
    }

    @Override
    public String toString() {
        return "bool";
    }
}
