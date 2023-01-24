package model.type;

import model.value.IntegerValue;
import model.value.IValue;

public class IntegerType implements IType {
    @Override
    public boolean equals(IType anotherType) {
        return anotherType instanceof IntegerType;
    }

    @Override
    public IValue defaultValue() {
        return new IntegerValue(0);
    }

    @Override
    public IType deepCopy() {
        return new IntegerType();
    }

    @Override
    public String toString() {
        return "int";
    }
}
