package model.statement;

import exceptions.MyException;
import model.state.ProgramState;
import model.type.IntegerType;
import model.type.IType;
import model.state.MyIDictionary;
import model.state.MyILockTable;
import model.value.IntegerValue;
import model.value.IValue;

import java.util.concurrent.locks.ReentrantLock;

public class Lock implements IStatement{
    private final String var;
    private static final java.util.concurrent.locks.Lock lock = new ReentrantLock();

    public Lock(String var) {
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
                    if (lockTable.get(foundIndex) == -1) {
                        lockTable.update(foundIndex, state.getId());
                        state.setLockTable(lockTable);
                    } else {
                        state.getExeStack().push(this);
                    }
                } else {
                    throw new MyException("Index is not in the lock table!");
                }
            } else {
                throw new MyException("Var is not of type int!");
            }
        } else {
            throw new MyException("Variable not defined!");
        }
        lock.unlock();
        return null;
    }

    @Override
    public MyIDictionary<String, IType> typeCheck(MyIDictionary<String, IType> typeEnv) throws MyException {
        if (typeEnv.lookUp(var).equals(new IntegerType())) {
            return typeEnv;
        } else {
            throw new MyException("Var is not of int type!");
        }
    }

    @Override
    public IStatement deepCopy() {
        return new Lock(var);
    }

    @Override
    public String toString() {
        return String.format("lock(%s)", var);
    }
}
