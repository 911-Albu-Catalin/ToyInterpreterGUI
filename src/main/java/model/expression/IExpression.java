package model.expression;

import exceptions.AssignmentException;
import exceptions.InvalidOperandTypeException;
import exceptions.MyException;
import model.type.IType;
import model.state.MyIDictionary;
import model.state.MyIHeap;
import model.value.IValue;

public interface IExpression {
    IType typeCheck(MyIDictionary<String, IType> typeEnv) throws InvalidOperandTypeException, MyException;
    IValue eval(MyIDictionary<String, IValue> symbolTable, MyIHeap heap) throws AssignmentException, MyException;
    IExpression deepCopy();
}
