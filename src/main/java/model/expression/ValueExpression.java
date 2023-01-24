package model.expression;
import exceptions.InvalidOperandTypeException;
import exceptions.MyException;
import model.type.IType;
import model.state.MyIDictionary;
import model.state.MyIHeap;
import model.value.IValue;

public class ValueExpression implements IExpression {
    IValue value;

    public ValueExpression(IValue value) {
        this.value = value;
    }

    @Override
    public IType typeCheck(MyIDictionary<String, IType> typeEnv) throws InvalidOperandTypeException {
        return value.getType();
    }

    @Override
    public IValue eval(MyIDictionary<String, IValue> symbolTable, MyIHeap heap) throws MyException {
        return this.value;
    }

    @Override
    public IExpression deepCopy() {
        return new ValueExpression(value);
    }

    @Override
    public String toString() {
        return this.value.toString();
    }
}
