package model.statement;

import exceptions.MyException;
import model.expression.IExpression;
import model.state.ProgramState;
import model.type.BooleanType;
import model.type.IType;
import model.state.MyIDictionary;
import model.state.MyIStack;
import model.value.BooleanValue;
import model.value.IValue;

public class While implements IStatement{
    private final IExpression expression;
    private final IStatement statement;

    public While(IExpression expression, IStatement statement) {
        this.expression = expression;
        this.statement = statement;
    }

    @Override
    public ProgramState execute(ProgramState state) throws MyException {
        IValue value = expression.eval(state.getSymTable().peek(), state.getHeap());
        MyIStack<IStatement> stack = state.getExeStack();
        if (!value.getType().equals(new BooleanType()))
            throw new MyException(String.format("%s is not of BooleanType", value));
        if (!(value instanceof BooleanValue))
            throw new MyException(String.format("%s is not a BooleanValue", value));
        if (value.getType().equals(new BooleanType())) {
            BooleanValue castResult = (BooleanValue) value;
            if(castResult.getValue()) {
                stack.push(this.deepCopy());
                stack.push(statement);
            }
        }
        return null;
    }

    @Override
    public MyIDictionary<String, IType> typeCheck(MyIDictionary<String, IType> typeEnv) throws MyException {
        IType typeExpr = expression.typeCheck(typeEnv);
        if (typeExpr.equals(new BooleanType())) {
            statement.typeCheck(typeEnv.deepCopy());
            return typeEnv;
        } else
            throw new MyException("The condition must be boolean.");
    }

    @Override
    public IStatement deepCopy() {
        return new While(expression.deepCopy(), statement.deepCopy());
    }

    @Override
    public String toString() {
        return String.format("while(%s){%s}", expression, statement);
    }
}
