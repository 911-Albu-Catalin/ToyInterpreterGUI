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

public class Unlock implements IStatement{
    private final String var;
    private static final Lock lock = new ReentrantLock();

    public Unlock(String var) {
        this.var = var;
    }

    @Override
    public ProgramState execute(ProgramState state) throws MyException {
        lock.lock();
        MyIDictionary<String, IValue> symTable = state.getSymTable();
        MyILockTable lockTable = state.getLockTable();
        if (symTable.isDefined(var)) {
            if (symTable.lookUp(var).getType().equals(new IntegerType())) {
                IntegerValue fi = (IntegerValue) symTable.lookUp(var);
                int foundIndex = fi.getValue();
                if (lockTable.containsKey(foundIndex)) {
                    if (lockTable.get(foundIndex) == state.getId())
                        lockTable.update(foundIndex, -1);
                } else {
                    throw new MyException("Index not in the lock table!");
                }
            } else {
                throw new MyException("Var is not of int type!");
            }
        } else {
            throw new MyException("Variable is not defined!");
        }
        lock.unlock();
        return null;
    }

    @Override
    public MyIDictionary<String, IType> typeCheck(MyIDictionary<String, IType> typeEnv) throws MyException {
        if (typeEnv.lookUp(var).equals(new IntegerType()))
            return typeEnv;
        else
            throw new MyException("Var is not of type int!");
    }

    @Override
    public IStatement deepCopy() {
        return new Unlock(var);
    }

    @Override
    public String toString() {
        return String.format("unlock(%s)", var);
    }
}
