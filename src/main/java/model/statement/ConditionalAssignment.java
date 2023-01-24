package model.statement;

import exceptions.MyException;
import model.expression.IExpression;
import model.expression.VariableExpression;
import model.state.ProgramState;
import model.state.MyIDictionary;
import model.state.MyIStack;
import model.type.BooleanType;
import model.type.IType;

public class ConditionalAssignment implements IStatement{
    private final String variable;
    private final IExpression expression1;
    private final IExpression expression2;
    private final IExpression expression3;

    public ConditionalAssignment(String variable, IExpression expression1, IExpression expression2, IExpression expression3) {
        this.variable = variable;
        this.expression1 = expression1;
        this.expression2 = expression2;
        this.expression3 = expression3;
    }
    @Override
    public ProgramState execute(ProgramState state) throws MyException {
        MyIStack<IStatement> exeStack = state.getExeStack();
        IStatement converted = new If(expression1, new Assignment(variable, expression2), new Assignment(variable, expression3));
        exeStack.push(converted);
        state.setExeStack(exeStack);
        return null;
    }

    @Override
    public MyIDictionary<String, IType> typeCheck(MyIDictionary<String, IType> typeEnv) throws MyException {
        IType variableType = new VariableExpression(variable).typeCheck(typeEnv);
        IType type1 = expression1.typeCheck(typeEnv);
        IType type2 = expression2.typeCheck(typeEnv);
        IType type3 = expression3.typeCheck(typeEnv);
        if (type1.equals(new BooleanType()) && type2.equals(variableType) && type3.equals(variableType))
            return typeEnv;
        else
            throw new MyException("The conditional assignment is invalid!");
    }

    @Override
    public IStatement deepCopy() {
        return new ConditionalAssignment(variable, expression1.deepCopy(), expression2.deepCopy(), expression3.deepCopy());
    }

    @Override
    public String toString() {
        return String.format("%s=(%s)? %s: %s", variable, expression1, expression2, expression3);
    }
}
