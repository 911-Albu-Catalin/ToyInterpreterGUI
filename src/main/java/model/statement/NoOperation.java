package model.statement;

import exceptions.MyException;
import model.state.ProgramState;
import model.type.IType;
import model.state.MyIDictionary;

public class NoOperation implements IStatement {
    @Override
    public ProgramState execute(ProgramState state) throws MyException {
        return null;
    }

    @Override
    public MyIDictionary<String, IType> typeCheck(MyIDictionary<String, IType> typeEnv) throws MyException {
        return typeEnv;
    }

    @Override
    public IStatement deepCopy() {
        return new NoOperation();
    }

    @Override
    public String toString() {
        return "NOP";
    }
}
