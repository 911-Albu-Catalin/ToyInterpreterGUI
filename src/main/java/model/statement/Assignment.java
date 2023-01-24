package model.statement;

import exceptions.AssignmentException;
import exceptions.InvalidOperandTypeException;
import exceptions.MyException;
import exceptions.TypeCheckException;
import model.expression.IExpression;
import model.state.ProgramState;
import model.type.IType;
import model.state.MyIDictionary;
import model.value.IValue;

public class Assignment implements IStatement {
    private final String key;
    private final IExpression expression;

    public Assignment(String key, IExpression expression) {
        this.key = key;
        this.expression = expression;
    }

    @Override
    public ProgramState execute(ProgramState state) throws AssignmentException, MyException {
        MyIDictionary<String, IValue> symbolTable = state.getSymTable();

        if (symbolTable.isDefined(key)) {
            IValue value = expression.eval(symbolTable, state.getHeap());
            IType typeId = (symbolTable.lookUp(key)).getType();
            if (value.getType().equals(typeId)) {
                symbolTable.update(key, value);
            } else {
                throw new AssignmentException("The type of the variable and the value do not match.");
            }
        } else {
            throw new AssignmentException( key + " was not declared before.");
        }

        state.setSymTable(symbolTable);
        return null;
    }

    @Override
    public MyIDictionary<String, IType> typeCheck(MyIDictionary<String, IType> typeEnv) throws TypeCheckException, MyException {
        IType typeVar =  typeEnv.lookUp(key);
        IType typeExpr = expression.typeCheck(typeEnv);
        if (typeVar.equals(typeExpr))
            return typeEnv;
        else
            throw new TypeCheckException("Right side and left side have different types.");
    }

    @Override
    public IStatement deepCopy() {
        return new Assignment(key, expression.deepCopy());
    }

    @Override
    public String toString() {
        return String.format("%s=%s", key, expression.toString());
    }
}
