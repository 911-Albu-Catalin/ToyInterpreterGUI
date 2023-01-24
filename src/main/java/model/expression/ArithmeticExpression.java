package model.expression;

import exceptions.DivisionByZeroException;
import exceptions.MyException;
import exceptions.InvalidOperandTypeException;
import model.type.IntegerType;
import model.type.IType;
import model.state.MyIDictionary;
import model.state.MyIHeap;
import model.value.IntegerValue;
import model.value.IValue;

public class ArithmeticExpression implements IExpression {
    IExpression expression1;
    IExpression expression2;
    char operation;

    public ArithmeticExpression(char operation, IExpression expression1, IExpression expression2) {
        this.expression1 = expression1;
        this.expression2 = expression2;
        this.operation = operation;
    }

    @Override
    public IValue eval(MyIDictionary<String, IValue> symbolTable, MyIHeap heap) throws MyException {
        IValue value1, value2;
        value1 = this.expression1.eval(symbolTable, heap);
        if (value1.getType().equals(new IntegerType())) {
            value2 = this.expression2.eval(symbolTable, heap);
            if (value2.getType().equals(new IntegerType())) {
                IntegerValue int1 = (IntegerValue) value1;
                IntegerValue int2 = (IntegerValue) value2;

                int result1 = int1.getValue();
                int result2 = int2.getValue();
                if (this.operation == '+')
                    return new IntegerValue(result1 + result2);
                else if (this.operation == '-')
                    return new IntegerValue(result1 - result2);
                else if (this.operation == '*')
                    return new IntegerValue(result1 * result2);
                else if (this.operation == '/')
                    if (result2 == 0)
                        throw new DivisionByZeroException("Division by zero.");
                    else
                        return new IntegerValue(result1 / result2);
            } else
                throw new InvalidOperandTypeException("Second operand is not an integer.");
        } else
            throw new InvalidOperandTypeException("First operand is not an integer.");
        return null;
    }

    @Override
    public IExpression deepCopy() {
        return new ArithmeticExpression(operation, expression1.deepCopy(), expression2.deepCopy());
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
        if (type1.equals(new IntegerType())) {
            if (type2.equals(new IntegerType())) {
                return new IntegerType();
            } else
                throw new InvalidOperandTypeException("Second operand is not an integer.");
        } else
            throw new InvalidOperandTypeException("First operand is not an integer.");
    }

}
