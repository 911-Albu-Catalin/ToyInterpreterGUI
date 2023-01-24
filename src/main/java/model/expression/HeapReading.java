package model.expression;

import exceptions.InvalidOperandTypeException;
import exceptions.MyException;
import model.type.RefType;
import model.type.IType;
import model.state.MyIDictionary;
import model.state.MyIHeap;
import model.value.RefValue;
import model.value.IValue;

public class HeapReading implements IExpression{
    private final IExpression expression;

    public HeapReading(IExpression expression) {
        this.expression = expression;
    }

    @Override
    public IValue eval(MyIDictionary<String, IValue> symbolTable, MyIHeap heap) throws MyException {
        IValue result = expression.eval(symbolTable, heap);
            if (result instanceof RefValue) {
                Integer address = ((RefValue) result).getAddress();
                if (heap.containsKey(address)) {
                    return heap.get(address);
                }
                else
                    throw new MyException("The key does not exist in the heap!\n");
            } else throw new MyException(String.format("%s not of RefType", result));
    }

    @Override
    public IExpression deepCopy() {
        return new HeapReading(expression.deepCopy());
    }

    @Override
    public String toString() {
        return String.format("ReadHeap(%s)", expression);
    }

    @Override
    public IType typeCheck(MyIDictionary<String, IType> typeEnv) throws InvalidOperandTypeException, MyException {
        IType type = expression.typeCheck(typeEnv);
        if (type instanceof RefType) {
            RefType refType = (RefType) type;
            return refType.getInner();
        } else
            throw new InvalidOperandTypeException("The argument is not a RefType.");
    }
}
