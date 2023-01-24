package model.expression;

import exceptions.InvalidOperandTypeException;
import exceptions.MyException;
import model.type.BooleanType;
import model.type.IntegerType;
import model.type.IType;
import model.state.MyIDictionary;
import model.state.MyIHeap;
import model.value.BooleanValue;
import model.value.IntegerValue;
import model.value.IValue;

import java.util.Objects;

public class RelationalExpression implements IExpression{
    IExpression expr1;
    IExpression expr2;
    String operator;

    public RelationalExpression(String o, IExpression e1, IExpression e2) {
        this.expr1 = e1;
        this.expr2 = e2;
        this.operator = o;
    }

    @Override
    public IValue eval(MyIDictionary<String, IValue> symbolTable, MyIHeap heap) throws MyException {
        IValue value1, value2;
        value1 = this.expr1.eval(symbolTable, heap);
        if (value1.getType().equals(new IntegerType())) {
            value2 = this.expr2.eval(symbolTable, heap);
            if (value2.getType().equals(new IntegerType())) {
                IntegerValue val1 = (IntegerValue) value1;
                IntegerValue val2 = (IntegerValue) value2;
                int v1, v2;
                v1 = val1.getValue();
                v2 = val2.getValue();
                if (Objects.equals(this.operator, "<"))
                    return new BooleanValue(v1 < v2);
                else if (Objects.equals(this.operator, "<="))
                    return new BooleanValue(v1 <= v2);
                else if (Objects.equals(this.operator, "=="))
                    return new BooleanValue(v1 == v2);
                else if (Objects.equals(this.operator, "!="))
                    return new BooleanValue(v1 != v2);
                else if (Objects.equals(this.operator, ">"))
                    return new BooleanValue(v1 > v2);
                else if (Objects.equals(this.operator, ">="))
                    return new BooleanValue(v1 >= v2);
            } else
                throw new InvalidOperandTypeException("Second operand is not an integer.");
        } else
            throw new InvalidOperandTypeException("First operand in not an integer.");
        return null;
    }

    @Override
    public IExpression deepCopy() {
        return new RelationalExpression(operator, expr1.deepCopy(), expr2.deepCopy());
    }

    @Override
    public String toString() {
        return this.expr1.toString() + " " + this.operator + " " + this.expr2.toString();
    }

    @Override
    public IType typeCheck(MyIDictionary<String, IType> typeEnv) throws InvalidOperandTypeException, MyException {
        IType type1, type2;
        type1 = expr1.typeCheck(typeEnv);
        type2 = expr2.typeCheck(typeEnv);
        if (type1.equals(new IntegerType())) {
            if (type2.equals(new IntegerType())) {
                return new BooleanType();
            } else
                throw new InvalidOperandTypeException("Second operand is not an integer.");
        } else
            throw new InvalidOperandTypeException("First operand is not an integer.");

    }
}
