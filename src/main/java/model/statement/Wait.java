package model.statement;

import exceptions.MyException;
import model.expression.ValueExpression;
import model.state.ProgramState;
import model.type.IType;
import model.state.MyIDictionary;
import model.state.MyIStack;
import model.value.IntegerValue;

public class Wait implements IStatement{
    private final int value;

    public Wait(int value) {
        this.value = value;
    }
    @Override
    public ProgramState execute(ProgramState state) throws MyException {
        if (value != 0) {
            MyIStack<IStatement> exeStack = state.getExeStack();
            exeStack.push(new Compound(new Print(new ValueExpression(new IntegerValue(value))),
                    new Wait(value - 1)));
            state.setExeStack(exeStack);
        }
        return null;
    }

    @Override
    public MyIDictionary<String, IType> typeCheck(MyIDictionary<String, IType> typeEnv) throws MyException {
        return typeEnv;
    }

    @Override
    public IStatement deepCopy() {
        return new Wait(value);
    }

    @Override
    public String toString() {
        return String.format("wait(%s)", value);
    }
}
