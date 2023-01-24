package model.value;

import model.type.BooleanType;
import model.type.IType;

public class BooleanValue implements IValue {
    private final boolean value;

    public BooleanValue(boolean value) {
        this.value = value;
    }

    @Override
    public IType getType() {
        return new BooleanType();
    }

    @Override
    public boolean equals(Object anotherValue) {
        if (!(anotherValue instanceof BooleanValue))
            return false;
        BooleanValue castObj = (BooleanValue) anotherValue;
        return value == castObj.value;
    }

    @Override
    public IValue deepCopy() {
        return new BooleanValue(value);
    }

    public boolean getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return this.value ? "true" : "false";
    }
}
