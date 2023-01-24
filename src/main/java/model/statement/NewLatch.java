package model.statement;

import exceptions.MyException;
import model.expression.IExpression;
import model.state.ProgramState;
import model.type.IntegerType;
import model.type.IType;
import model.state.MyIDictionary;
import model.state.MyIHeap;
import model.state.MyILatchTable;
import model.value.IntegerValue;
import model.value.IValue;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class NewLatch implements IStatement{
    private final String var;
    private final IExpression expression;
    private static final Lock lock = new ReentrantLock();

    public NewLatch(String var, IExpression expression) {
        this.var = var;
        this.expression = expression;
    }

    @Override
    public ProgramState execute(ProgramState state) throws MyException {
        lock.lock();
        MyIDictionary<String, IValue> symTable = state.getSymTable();
        MyIHeap heap = state.getHeap();
        MyILatchTable latchTable = state.getLatchTable();
        IntegerValue nr = (IntegerValue) (expression.eval(symTable, heap));
        int number = nr.getValue();
        int freeAddress = latchTable.getFreeAddress();
        latchTable.put(freeAddress, number);
        if (symTable.isDefined(var)) {
            symTable.update(var, new IntegerValue(freeAddress));
        } else {
            throw new MyException(String.format("%s is not defined in the symbol table!", var));
        }
        lock.unlock();
        return null;
    }

    @Override
    public MyIDictionary<String, IType> typeCheck(MyIDictionary<String, IType> typeEnv) throws MyException {
        if (typeEnv.lookUp(var).equals(new IntegerType())) {
            if (expression.typeCheck(typeEnv).equals(new IntegerType())) {
                return typeEnv;
            } else {
                throw new MyException("Expression doesn't have the int type!");
            }
        } else {
            throw new MyException(String.format("%s is not of int type!", var));
        }
    }

    @Override
    public IStatement deepCopy() {
        return new NewLatch(var, expression.deepCopy());
    }

    @Override
    public String toString() {
        return String.format("newLatch(%s, %s)", var, expression);
    }
}
