package model.type;

import model.value.StringValue;
import model.value.IValue;

public class StringType implements IType {
    @Override
    public boolean equals(IType anotherType) {
        return anotherType instanceof StringType;
    }

    @Override
    public IValue defaultValue() {
        return new StringValue("");
    }

    @Override
    public IType deepCopy() {
        return new StringType();
    }

    @Override
    public String toString() {
        return "string";
    }
}
