package model.statement;

import exceptions.MyException;
import exceptions.TypeCheckException;
import model.expression.IExpression;
import model.state.ProgramState;
import model.type.BooleanType;
import model.type.IType;
import model.state.MyIDictionary;
import model.state.MyIStack;
import model.value.BooleanValue;
import model.value.IValue;

public class If implements IStatement {
    IExpression expression;
    IStatement ifBranch;
    IStatement elseBranch;

    public If(IExpression expression, IStatement thenStatement, IStatement elseStatement) {
        this.expression = expression;
        this.ifBranch = thenStatement;
        this.elseBranch = elseStatement;
    }

    @Override
    public ProgramState execute(ProgramState state) throws MyException {
        IValue result = this.expression.eval(state.getSymTable(), state.getHeap());
        if (result.getType().equals(new BooleanType())) {
            BooleanValue castResult = (BooleanValue) result;
            IStatement statement;
            if (castResult.getValue())
                statement = ifBranch;
            else
                statement = elseBranch;

            MyIStack<IStatement> stack = state.getExeStack();
            stack.push(statement);
            state.setExeStack(stack);
            return null;
        } else {
            throw new MyException("The statement should be boolean.");
        }
    }

    @Override
    public MyIDictionary<String, IType> typeCheck(MyIDictionary<String, IType> typeEnv) throws MyException {
        IType typeExpr = expression.typeCheck(typeEnv);
        if (typeExpr.equals(new BooleanType())) {
            ifBranch.typeCheck(typeEnv.deepCopy());
            elseBranch.typeCheck(typeEnv.deepCopy());
            return typeEnv;
        } else
            throw new TypeCheckException("The statement should be boolean.");
    }

    @Override
    public IStatement deepCopy() {
        return new If(expression.deepCopy(), ifBranch.deepCopy(), elseBranch.deepCopy());
    }

    @Override
    public String toString() {
        return String.format("if(%s){%s}else{%s}", expression.toString(), ifBranch.toString(), elseBranch.toString());
    }
}
