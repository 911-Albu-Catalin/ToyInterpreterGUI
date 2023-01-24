package model.statement;

import exceptions.MyException;
import model.expression.IExpression;
import model.state.ProgramState;
import model.type.IntegerType;
import model.type.IType;
import model.state.MyIDictionary;
import model.state.MyIHeap;
import model.state.MyIToySemaphoreTable;
import model.state.Tuple;
import model.value.IntegerValue;
import model.value.IValue;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class NewSemaphore implements IStatement{
    private final String var;
    private final IExpression expression1;
    private final IExpression expression2;
    private static final Lock lock = new ReentrantLock();

    public NewSemaphore(String var, IExpression expression1, IExpression expression2) {
        this.var = var;
        this.expression1 = expression1;
        this.expression2 = expression2;
    }

    @Override
    public ProgramState execute(ProgramState state) throws MyException {
        lock.lock();
        MyIDictionary<String, IValue> symTable = state.getSymTable();
        MyIHeap heap = state.getHeap();
        MyIToySemaphoreTable semaphoreTable = state.getToySemaphoreTable();
        IntegerValue nr1 = (IntegerValue) (expression1.eval(symTable, heap));
        IntegerValue nr2 = (IntegerValue) (expression2.eval(symTable, heap));
        int number1 = nr1.getValue();
        int number2 = nr2.getValue();
        int freeAddress = semaphoreTable.getFreeAddress();
        semaphoreTable.put(freeAddress, new Tuple<>(number1, new ArrayList<>(), number2));
        if (symTable.isDefined(var) && symTable.lookUp(var).getType().equals(new IntegerType()))
            symTable.update(var, new IntegerValue(freeAddress));
        else
            throw new MyException(String.format("%s in not defined in the symbol table!", var));
        lock.unlock();
        return null;
    }

    @Override
    public MyIDictionary<String, IType> typeCheck(MyIDictionary<String, IType> typeEnv) throws MyException {
        return typeEnv;
    }

    @Override
    public IStatement deepCopy() {
        return new NewSemaphore(var, expression1.deepCopy(), expression2.deepCopy());
    }

    @Override
    public String toString() {
        return String.format("newSemaphore(%s, %s, %s)", var, expression1, expression2);
    }
}
