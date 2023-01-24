package model.statement;

import exceptions.MyException;
import model.state.ProgramState;
import model.type.IType;
import model.state.MyIDictionary;
import model.state.MyIStack;

public class Sleep implements IStatement{
    private final int value;

    public Sleep(int value) {
        this.value = value;
    }

    @Override
    public ProgramState execute(ProgramState state) throws MyException {
        if (value != 0) {
            MyIStack<IStatement> exeStack = state.getExeStack();
            exeStack.push(new Sleep(value - 1));
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
        return new Sleep(value);
    }

    @Override
    public String toString() {
        return String.format("sleep(%s)", value);
    }
}
