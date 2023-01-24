package model.expression;

import exceptions.MyException;
import model.type.IType;
import model.state.MyIDictionary;
import model.state.MyIHeap;
import model.value.IValue;

public class VariableExpression implements IExpression {
    String key;

    public VariableExpression(String key) {
        this.key = key;
    }

    @Override
    public IType typeCheck(MyIDictionary<String, IType> typeEnv) throws MyException {
        try {
            return typeEnv.lookUp(key);
        } catch (MyException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public IValue eval(MyIDictionary<String, IValue> symbolTable, MyIHeap heap) {
        try {
            return symbolTable.lookUp(key);
        } catch (MyException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public IExpression deepCopy() {
        return new VariableExpression(key);
    }

    @Override
    public String toString() {
        return key;
    }
}
