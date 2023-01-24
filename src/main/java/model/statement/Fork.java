package model.statement;

import exceptions.MyException;
import model.state.ProgramState;
import model.type.IType;
import model.state.MyDictionary;
import model.state.MyIDictionary;
import model.state.MyIStack;
import model.state.MyStack;
import model.value.IValue;

import java.util.Map;

public class Fork implements IStatement{
    private final IStatement statement;

    public Fork(IStatement statement) {
        this.statement = statement;
    }
    @Override
    public ProgramState execute(ProgramState state) throws MyException {
        MyIStack<IStatement> newStack = new MyStack<>();
        newStack.push(statement);
        MyIStack<MyIDictionary<String, IValue>> newSymTable = state.getSymTable().clone();
        return new ProgramState(newStack, newSymTable, state.getOut(), state.getFileTable(), state.getHeap(), state.getProcTable());
    }

    @Override
    public MyIDictionary<String, IType> typeCheck(MyIDictionary<String, IType> typeEnv) throws MyException {
        statement.typeCheck(typeEnv.deepCopy());
        return typeEnv;
    }

    @Override
    public IStatement deepCopy() {
        return new Fork(statement.deepCopy());
    }

    @Override
    public String toString() {
        return String.format("Fork(%s)", statement.toString());
    }
}
