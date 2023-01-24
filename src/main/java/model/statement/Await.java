package model.statement;

import exceptions.MyException;
import model.state.ProgramState;
import model.type.IntegerType;
import model.type.IType;
import model.state.MyIDictionary;
import model.state.MyILatchTable;
import model.value.IntegerValue;
import model.value.IValue;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Await implements IStatement{
    private final String var;
    private static final Lock lock = new ReentrantLock();

    public Await(String var) {
        this.var = var;
    }
    @Override
    public ProgramState execute(ProgramState state) throws MyException {
        lock.lock();
        MyIDictionary<String, IValue> symTable = state.getSymTable();
        MyILatchTable latchTable = state.getLatchTable();
        if (symTable.isDefined(var)) {
            IntegerValue fi = (IntegerValue) symTable.lookUp(var);
            int foundIndex = fi.getValue();
            if (latchTable.containsKey(foundIndex)) {
                if (latchTable.get(foundIndex) != 0) {
                    state.getExeStack().push(this);
                }
            } else {
                throw new MyException("Index not found in the latch table!");
            }
        } else {
            throw new MyException("Variable not defined!");
        }
        lock.unlock();
        return null;
    }

    @Override
    public MyIDictionary<String, IType> typeCheck(MyIDictionary<String, IType> typeEnv) throws MyException {
        if (typeEnv.lookUp(var).equals(new IntegerType()))
            return typeEnv;
        else
            throw new MyException(String.format("%s is not of int type!", var));
    }

    @Override
    public IStatement deepCopy() {
        return new Await(var);
    }

    @Override
    public String toString() {
        return String.format("await(%s)", var);
    }
}
