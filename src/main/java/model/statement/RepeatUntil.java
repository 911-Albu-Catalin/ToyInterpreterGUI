package model.statement;

import exceptions.MyException;
import model.expression.IExpression;
import model.expression.NotExpression;
import model.state.ProgramState;
import model.type.BooleanType;
import model.type.IType;
import model.state.MyIDictionary;
import model.state.MyIStack;

public class RepeatUntil implements IStatement{
    private final IStatement statement;
    private final IExpression expression;

    public RepeatUntil(IStatement statement, IExpression expression) {
        this.statement = statement;
        this.expression = expression;
    }
    @Override
    public ProgramState execute(ProgramState state) throws MyException {
        MyIStack<IStatement> exeStack = state.getExeStack();
        IStatement converted = new Compound(statement, new While(new NotExpression(expression), statement));
        exeStack.push(converted);
        state.setExeStack(exeStack);
        return null;
    }

    @Override
    public MyIDictionary<String, IType> typeCheck(MyIDictionary<String, IType> typeEnv) throws MyException {
        IType type = expression.typeCheck(typeEnv);
        if (type.equals(new BooleanType())) {
            statement.typeCheck(typeEnv.deepCopy());
            return typeEnv;
        } else {
            throw new MyException("Expression in the repeat statement must be of Bool type!");
        }
    }

    @Override
    public IStatement deepCopy() {
        return new RepeatUntil(statement.deepCopy(), expression.deepCopy());
    }

    @Override
    public String toString() {
        return String.format("repeat(%s) until (%s)", statement, expression);
    }
}
