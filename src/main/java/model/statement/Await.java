package model.statement;

import exceptions.MyException;
import javafx.util.Pair;
import model.state.ProgramState;
import model.type.IntegerType;
import model.type.IType;
import model.state.MyIBarrierTable;
import model.state.MyIDictionary;
import model.state.MyIHeap;
import model.value.IntegerValue;
import model.value.IValue;

import java.util.ArrayList;
import java.util.List;
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
        MyIBarrierTable barrierTable = state.getBarrierTable();
        if (symTable.isDefined(var)) {
            IntegerValue f = (IntegerValue) symTable.lookUp(var);
            int foundIndex = f.getValue();
            if (barrierTable.containsKey(foundIndex)) {
                Pair<Integer, List<Integer>> foundBarrier = barrierTable.get(foundIndex);
                int NL = foundBarrier.getValue().size();
                int N1 = foundBarrier.getKey();
                ArrayList<Integer> list = (ArrayList<Integer>) foundBarrier.getValue();
                if (N1 > NL) {
                    if (list.contains(state.getId()))
                        state.getExeStack().push(this);
                    else {
                        list.add(state.getId());
                        barrierTable.update(foundIndex, new Pair<>(N1, list));
                        state.setBarrierTable(barrierTable);
                    }
                }
            } else {
                throw new MyException("Index not in Barrier Table!");
            }
        } else {
            throw new MyException("Var is not defined!");
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
        return new Await(var);
    }

    @Override
    public String toString() {
        return String.format("await(%s)", var);
    }
}
