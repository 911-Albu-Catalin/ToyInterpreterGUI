package model.statement;

import exceptions.MyException;
import model.state.MyIStack;
import model.state.ProgramState;
import model.type.IType;
import model.state.MyIDictionary;
import model.value.IValue;

public class VariableDeclaration implements IStatement {
    String name;
    IType type;

    public VariableDeclaration(String name, IType type) {
        this.name = name;
        this.type = type;
    }

    @Override
    public ProgramState execute(ProgramState state) throws MyException {
        MyIStack<MyIDictionary<String, IValue>> symTable = state.getSymTable();
        MyIDictionary<String, IValue> currentSymbolTable = symTable.peek();
        if (currentSymbolTable.isDefined(name)) {
            throw new MyException("Variable " + name + " already exists in the symTable.");
        }
        currentSymbolTable.put(name, type.defaultValue());
        state.setSymTable(symTable);
        return null;
    }

    @Override
    public MyIDictionary<String, IType> typeCheck(MyIDictionary<String, IType> typeEnv) throws MyException {
        typeEnv.put(name, type);
        return typeEnv;
    }

    @Override
    public IStatement deepCopy() {
        return new VariableDeclaration(name, type);
    }

    @Override
    public String toString() {
        return String.format("%s %s", type.toString(), name);
    }
}

