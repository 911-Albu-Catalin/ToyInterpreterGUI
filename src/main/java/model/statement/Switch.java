package model.statement;

import exceptions.MyException;
import model.expression.IExpression;
import model.expression.RelationalExpression;
import model.state.ProgramState;
import model.type.IType;
import model.state.MyIDictionary;
import model.state.MyIStack;

public class Switch implements IStatement{
    private final IExpression mainExpression;
    private final IExpression expression1;
    private final IStatement statement1;
    private final IExpression expression2;
    private final IStatement statement2;
    private final IStatement defaultStatement;

    public Switch(IExpression mainExpression, IExpression expression1, IStatement statement1, IExpression expression2, IStatement statement2, IStatement defaultStatement) {
        this.mainExpression = mainExpression;
        this.expression1 = expression1;
        this.statement1 = statement1;
        this.expression2 = expression2;
        this.statement2 = statement2;
        this.defaultStatement = defaultStatement;
    }
    @Override
    public ProgramState execute(ProgramState state) throws MyException {
        MyIStack<IStatement> exeStack = state.getExeStack();
        IStatement converted = new If(new RelationalExpression("==", mainExpression, expression1),
                statement1, new If(new RelationalExpression("==", mainExpression, expression2), statement2, defaultStatement));
        exeStack.push(converted);
        state.setExeStack(exeStack);
        return null;
    }

    @Override
    public MyIDictionary<String, IType> typeCheck(MyIDictionary<String, IType> typeEnv) throws MyException {
        IType mainType = mainExpression.typeCheck(typeEnv);
        IType type1 = expression1.typeCheck(typeEnv);
        IType type2 = expression2.typeCheck(typeEnv);
        if (mainType.equals(type1) && mainType.equals(type2)) {
            statement1.typeCheck(typeEnv.deepCopy());
            statement2.typeCheck(typeEnv.deepCopy());
            defaultStatement.typeCheck(typeEnv.deepCopy());
            return typeEnv;
        } else {
            throw new MyException("The expression types don't match in the switch statement!");
        }
    }

    @Override
    public IStatement deepCopy() {
        return new Switch(mainExpression.deepCopy(), expression1.deepCopy(), statement1.deepCopy(), expression2.deepCopy(), statement2.deepCopy(), defaultStatement.deepCopy());
    }

    @Override
    public String toString() {
        return String.format("switch(%s){(case(%s): %s)(case(%s): %s)(default: %s)}", mainExpression, expression1, statement1, expression2, statement2, defaultStatement);
    }
}
