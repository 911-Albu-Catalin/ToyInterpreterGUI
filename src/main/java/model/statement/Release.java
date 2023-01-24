package model.statement;

import exceptions.MyException;
import javafx.util.Pair;
import model.state.ProgramState;
import model.type.IntegerType;
import model.type.IType;
import model.state.MyIDictionary;
import model.state.MyISemaphoreTable;
import model.value.IntegerValue;
import model.value.IValue;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Release implements IStatement{
    private final String var;
    private static final Lock lock = new ReentrantLock();

    public Release(String var) {
        this.var = var;
    }

    @Override
    public ProgramState execute(ProgramState state) throws MyException {
        lock.lock();
        MyIDictionary<String, IValue> symTable = state.getSymTable();
        MyISemaphoreTable semaphoreTable = state.getSemaphoreTable();
        if (symTable.isDefined(var)) {
            if (symTable.lookUp(var).getType().equals(new IntegerType())) {
                IntegerValue fi = (IntegerValue) symTable.lookUp(var);
                int foundIndex = fi.getValue();
                if (semaphoreTable.getSemaphoreTable().containsKey(foundIndex)) {
                    Pair<Integer, List<Integer>> foundSemaphore = semaphoreTable.get(foundIndex);
                    if (foundSemaphore.getValue().contains(state.getId()))
                        foundSemaphore.getValue().remove((Integer) state.getId());
                    semaphoreTable.update(foundIndex, new Pair<>(foundSemaphore.getKey(), foundSemaphore.getValue()));
                } else {
                    throw new MyException("Index not in the semaphore table!");
                }
            } else {
                throw new MyException("Index must be of int type!");
            }
        } else {
            throw new MyException("Index not in symbol table!");
        }
        lock.unlock();
        return null;
    }

    @Override
    public MyIDictionary<String, IType> typeCheck(MyIDictionary<String, IType> typeEnv) throws MyException {
        if (typeEnv.lookUp(var).equals(new IntegerType())) {
            return typeEnv;
        } else {
            throw new MyException(String.format("%s is not int!", var));
        }
    }

    @Override
    public IStatement deepCopy() {
        return new Release(var);
    }

    @Override
    public String toString() {
        return String.format("release(%s)", var);
    }
}
