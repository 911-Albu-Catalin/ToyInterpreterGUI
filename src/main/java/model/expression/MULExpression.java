package model.expression;

import exceptions.MyException;
import model.type.IntegerType;
import model.type.IType;
import model.state.MyIDictionary;
import model.state.MyIHeap;
import model.value.IValue;

public class MULExpression implements IExpression{
    private final IExpression expression1;
    private final IExpression expression2;

    public MULExpression(IExpression expression1, IExpression expression2) {
        this.expression1 = expression1;
        this.expression2 = expression2;
    }

    @Override
    public IType typeCheck(MyIDictionary<String, IType> typeEnv) throws MyException {
        IType type1 = expression1.typeCheck(typeEnv);
        IType type2 = expression2.typeCheck(typeEnv);
        if (type1.equals(new IntegerType()) && type2.equals(new IntegerType()))
            return new IntegerType();
        else
            throw new MyException("Expressions in the MUL should be int!");
    }

    @Override
    public IValue eval(MyIDictionary<String, IValue> table, MyIHeap heap) throws MyException {
        IExpression converted = new ArithmeticExpression('-',
                new ArithmeticExpression('*', expression1, expression2),
                new ArithmeticExpression('+', expression1, expression2));
        return converted.eval(table, heap);
    }

    @Override
    public IExpression deepCopy() {
        return new MULExpression(expression1.deepCopy(), expression2.deepCopy());
    }

    @Override
    public String toString() {
        return String.format("MUL(%s, %s)", expression1, expression2);
    }
}
