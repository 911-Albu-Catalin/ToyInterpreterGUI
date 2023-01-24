package model.expression;


import exceptions.InvalidOperandTypeException;
import exceptions.MyException;
import model.type.BooleanType;
import model.type.IType;
import model.state.MyIDictionary;
import model.state.MyIHeap;
import model.value.BooleanValue;
import model.value.IValue;

import java.util.Objects;

public class LogicExpression implements IExpression {
    IExpression expression1;
    IExpression expression2;
    String operation;

    public LogicExpression(String operation, IExpression expression1, IExpression expression2) {
        this.expression1 = expression1;
        this.expression2 = expression2;
        this.operation = operation;
    }

    @Override
    public IValue eval(MyIDictionary<String, IValue> symbolTable, MyIHeap heap) throws MyException {
        IValue value1, value2;
        value1 = this.expression1.eval(symbolTable, heap);
        if (value1.getType().equals(new BooleanType())) {
            value2 = this.expression2.eval(symbolTable, heap);
            if (value2.getType().equals(new BooleanType())) {
                BooleanValue bool1 = (BooleanValue) value1;
                BooleanValue bool2 = (BooleanValue) value2;
                boolean b1, b2;
                b1 = bool1.getValue();
                b2 = bool2.getValue();
                if (Objects.equals(this.operation, "and")) {
                    return new BooleanValue(b1 && b2);
                } else if (Objects.equals(this.operation, "or")) {
                    return new BooleanValue(b1 || b2);
                }
            } else {
                throw new MyException("Second operand is not a boolean.");
            }
        } else {
            throw new MyException("First operand is not a boolean.");
        }
        return null;
    }

    @Override
    public IExpression deepCopy() {
        return new LogicExpression(operation, expression1.deepCopy(), expression2.deepCopy());
    }

    @Override
    public String toString() {
        return expression1.toString() + " " + operation + " " + expression2.toString();
    }

    @Override
    public IType typeCheck(MyIDictionary<String, IType> typeEnv) throws InvalidOperandTypeException, MyException {
        IType type1, type2;
        type1 = expression1.typeCheck(typeEnv);
        type2 = expression2.typeCheck(typeEnv);
        if (type1.equals(new BooleanType())) {
            if (type2.equals(new BooleanType())) {
                return new BooleanType();
            } else
                throw new InvalidOperandTypeException("Second operand is not a boolean.");
        } else
            throw new InvalidOperandTypeException("First operand is not a boolean.");

    }
}
