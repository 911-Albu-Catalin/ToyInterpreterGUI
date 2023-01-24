package model.value;

import model.type.IntegerType;
import model.type.IType;

public class IntegerValue implements IValue {
    private final int value;

    public IntegerValue(int value) {
        this.value = value;
    }

    @Override
    public IType getType() {
        return new IntegerType();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof IntegerValue))
            return false;
        IntegerValue castObj = (IntegerValue) obj;
        return value == castObj.value;
    }

    @Override
    public IValue deepCopy() {
        return new IntegerValue(value);
    }

    public int getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return String.format("%d", this.value);
    }
}
