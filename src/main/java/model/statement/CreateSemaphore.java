package model.statement;

import exceptions.MyException;
import javafx.util.Pair;
import model.expression.IExpression;
import model.state.ProgramState;
import model.type.IntegerType;
import model.type.IType;
import model.state.MyIDictionary;
import model.state.MyIHeap;
import model.state.MyISemaphoreTable;
import model.value.IntegerValue;
import model.value.IValue;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CreateSemaphore implements IStatement{
    private final String var;
    private final IExpression expression;
    private static final Lock lock = new ReentrantLock();

    public CreateSemaphore(String var, IExpression expression) {
        this.var = var;
        this.expression = expression;
    }
    @Override
    public ProgramState execute(ProgramState state) throws MyException {
        lock.lock();
        MyIDictionary<String, IValue> symTable = state.getSymTable();
        MyIHeap heap = state.getHeap();
        MyISemaphoreTable semaphoreTable = state.getSemaphoreTable();
        IntegerValue nr = (IntegerValue) (expression.eval(symTable, heap));
        int number = nr.getValue();
        int freeAddress = semaphoreTable.getFreeAddress();
        semaphoreTable.put(freeAddress, new Pair<>(number, new ArrayList<>()));
        if (symTable.isDefined(var) && symTable.lookUp(var).getType().equals(new IntegerType()))
            symTable.update(var, new IntegerValue(freeAddress));
        else
            throw new MyException(String.format("Error for variable %s: not defined/does not have int type!", var));
        lock.unlock();
        return null;
    }

    @Override
    public MyIDictionary<String, IType> typeCheck(MyIDictionary<String, IType> typeEnv) throws MyException {
        if (typeEnv.lookUp(var).equals(new IntegerType())) {
            if (expression.typeCheck(typeEnv).equals(new IntegerType()))
                return typeEnv;
            else
                throw new MyException("Expression is not of int type!");
        } else {
            throw new MyException(String.format("%s is not of type int!", var));
        }
    }

    @Override
    public IStatement deepCopy() {
        return new CreateSemaphore(var, expression.deepCopy());
    }

    @Override
    public String toString() {
        return String.format("createSemaphore(%s, %s)", var, expression);
    }
}
