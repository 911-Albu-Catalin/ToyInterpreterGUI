package model.statement;

import exceptions.MyException;
import model.expression.IExpression;
import model.state.ProgramState;
import model.type.BooleanType;
import model.type.IType;
import model.state.MyIDictionary;

public class DoWhileStatement implements IStatement{
    private final IStatement statement;
    private final IExpression expression;

    public DoWhileStatement(IExpression expression, IStatement statement) {
        this.statement = statement;
        this.expression = expression;
    }
    @Override
    public ProgramState execute(ProgramState state) throws MyException {
        IStatement converted = new Compound(statement, new While(expression, statement));
        state.getExeStack().push(converted);
        return null;
    }

    @Override
    public MyIDictionary<String, IType> typeCheck(MyIDictionary<String, IType> typeEnv) throws MyException {
        IType typeExpression = expression.typeCheck(typeEnv);
        if (typeExpression.equals(new BooleanType())) {
            statement.typeCheck(typeEnv.deepCopy());
            return typeEnv;
        } else
            throw new MyException("Condition in the do while statement must be bool!");
    }

    @Override
    public IStatement deepCopy() {
        return new DoWhileStatement(expression.deepCopy(), statement.deepCopy());
    }

    @Override
    public String toString() {
        return String.format("do {%s} while (%s)", statement, expression);
    }
}
