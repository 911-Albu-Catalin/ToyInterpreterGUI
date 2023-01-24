package model.statement;

import exceptions.MyException;
import model.state.ProgramState;
import model.type.IntegerType;
import model.type.IType;
import model.state.MyIDictionary;
import model.state.MyILockTable;
import model.value.IntegerValue;
import model.value.IValue;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class NewLock implements IStatement{
    private final String var;
    private static final Lock lock = new ReentrantLock();

    public NewLock(String var) {
        this.var = var;
    }

    @Override
    public ProgramState execute(ProgramState state) throws MyException {
        lock.lock();
        MyILockTable lockTable = state.getLockTable();
        MyIDictionary<String, IValue> symTable = state.getSymTable();
        int freeAddress = lockTable.getFreeValue();
        lockTable.put(freeAddress, -1);
        if (symTable.isDefined(var) && symTable.lookUp(var).getType().equals(new IntegerType()))
            symTable.update(var, new IntegerValue(freeAddress));
        else
            throw new MyException("Variable not declared!");
        lock.unlock();
        return null;
    }

    @Override
    public MyIDictionary<String, IType> typeCheck(MyIDictionary<String, IType> typeEnv) throws MyException {
        if (typeEnv.lookUp(var).equals(new IntegerType()))
            return typeEnv;
        else
            throw new MyException("Var is not of int type!");
    }

    @Override
    public IStatement deepCopy() {
        return new NewLock(var);
    }

    @Override
    public String toString() {
        return String.format("newLock(%s)", var);
    }
}
