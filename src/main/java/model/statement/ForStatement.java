package model.statement;

import exceptions.MyException;
import model.expression.IExpression;
import model.expression.RelationalExpression;
import model.expression.VariableExpression;
import model.state.ProgramState;
import model.type.IntegerType;
import model.type.IType;
import model.state.MyIDictionary;
import model.state.MyIStack;

public class ForStatement implements IStatement{
    private final String variable;
    private final IExpression expression1;
    private final IExpression expression2;
    private final IExpression expression3;
    private final IStatement statement;

    public ForStatement(String variable, IExpression expression1, IExpression expression2, IExpression expression3, IStatement statement) {
        this.variable = variable;
        this.expression1 = expression1;
        this.expression2 = expression2;
        this.expression3 = expression3;
        this.statement = statement;
    }

    @Override
    public ProgramState execute(ProgramState state) throws MyException {
        MyIStack<IStatement> exeStack = state.getExeStack();
        IStatement converted = new Compound(new Assignment("v", expression1),
                new While(new RelationalExpression("<", new VariableExpression("v"), expression2),
                        new Compound(statement, new Assignment("v", expression3))));
        exeStack.push(converted);
        state.setExeStack(exeStack);
        return null;
    }

    @Override
    public MyIDictionary<String, IType> typeCheck(MyIDictionary<String, IType> typeEnv) throws MyException {
        IType type1 = expression1.typeCheck(typeEnv);
        IType type2 = expression2.typeCheck(typeEnv);
        IType type3 = expression3.typeCheck(typeEnv);

        if (type1.equals(new IntegerType()) && type2.equals(new IntegerType()) && type3.equals(new IntegerType()))
            return typeEnv;
        else
            throw new MyException("The for statement is invalid!");
    }

    @Override
    public IStatement deepCopy() {
        return new ForStatement(variable, expression1.deepCopy(), expression2.deepCopy(), expression3.deepCopy(), statement.deepCopy());
    }

    @Override
    public String toString() {
        return String.format("for(%s=%s; %s<%s; %s=%s) {%s}", variable, expression1, variable, expression2, variable, expression3, statement);
    }
}
