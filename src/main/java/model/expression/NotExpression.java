package model.expression;

import exceptions.MyException;
import model.type.IType;
import model.state.MyIDictionary;
import model.state.MyIHeap;
import model.value.BooleanValue;
import model.value.IValue;

public class NotExpression implements IExpression{
    private final IExpression expression;

    public NotExpression(IExpression expression) {
        this.expression = expression;
    }

    @Override
    public IType typeCheck(MyIDictionary<String, IType> typeEnv) throws MyException {
        return expression.typeCheck(typeEnv);
    }

    @Override
    public IValue eval(MyIDictionary<String, IValue> table, MyIHeap heap) throws MyException {
        BooleanValue value = (BooleanValue) expression.eval(table, heap);
        if (!value.getValue())
            return new BooleanValue(true);
        else
            return new BooleanValue(false);
    }

    @Override
    public IExpression deepCopy() {
        return new NotExpression(expression.deepCopy());
    }

    @Override
    public String toString() {
        return String.format("!(%s)", expression);
    }
}
