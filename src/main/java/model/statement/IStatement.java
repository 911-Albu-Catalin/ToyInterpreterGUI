package model.statement;

import exceptions.MyException;
import exceptions.TypeCheckException;
import model.state.ProgramState;
import model.type.IType;
import model.state.MyIDictionary;

public interface IStatement {
    ProgramState execute(ProgramState state) throws MyException;
    MyIDictionary<String, IType> typeCheck(MyIDictionary<String, IType> typeEnv) throws TypeCheckException, MyException;
    IStatement deepCopy();
}
