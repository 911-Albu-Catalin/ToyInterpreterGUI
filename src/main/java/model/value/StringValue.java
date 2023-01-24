package model.value;

import model.type.StringType;
import model.type.IType;

public class StringValue implements IValue {
    private final String value;

    public StringValue(String value) {
        this.value = value;
    }
    @Override
    public IType getType() {
        return new StringType();
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof StringValue))
            return false;
        StringValue castObj = (StringValue)obj;
        return value.equals(castObj.value);
    }

    @Override
    public IValue deepCopy() {
        return new StringValue(value);
    }

    public String getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return "\"" + this.value + "\"";
    }
}
