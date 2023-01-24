package model.statement;

import exceptions.MyException;
import model.expression.IExpression;
import model.state.ProgramState;
import model.type.IType;
import model.state.MyIDictionary;
import model.state.MyIList;
import model.value.IValue;

public class Print implements IStatement {
    IExpression expression;

    public Print(IExpression expression) {
        this.expression = expression;
    }

    @Override
    public ProgramState execute(ProgramState state) throws MyException {
        MyIList<IValue> out = state.getOut();
        out.add(expression.eval(state.getSymTable().peek(), state.getHeap()));
        state.setOut(out);
        return null;
    }

    @Override
    public MyIDictionary<String, IType> typeCheck(MyIDictionary<String, IType> typeEnv) throws MyException {
        expression.typeCheck(typeEnv);
        return typeEnv;
    }

    @Override
    public IStatement deepCopy() {
        return new Print(expression.deepCopy());
    }

    @Override
    public String toString() {
        return String.format("Print(%s)", expression.toString());
    }
}
