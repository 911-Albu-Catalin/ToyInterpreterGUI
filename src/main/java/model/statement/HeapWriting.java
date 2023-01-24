package model.statement;

import exceptions.InvalidOperandTypeException;
import exceptions.MyException;
import exceptions.TypeCheckException;
import model.expression.IExpression;
import model.state.ProgramState;
import model.type.RefType;
import model.type.IType;
import model.state.MyIDictionary;
import model.state.MyIHeap;
import model.value.RefValue;
import model.value.IValue;

public class HeapWriting implements IStatement{
    private final String varName;
    private final IExpression expression;

    public HeapWriting(String varName, IExpression expression) {
        this.varName = varName;
        this.expression = expression;
    }

    @Override
    public ProgramState execute(ProgramState state) throws MyException {
        MyIDictionary<String, IValue> symTable = state.getSymTable().peek();
        MyIHeap heap = state.getHeap();
        if (symTable.isDefined(varName)) {
            IValue value = symTable.lookUp(varName);
            if (value.getType() instanceof RefType) {
                RefValue refValue = (RefValue) value;
                if (heap.containsKey(refValue.getAddress())) {
                    IValue evaluated = expression.eval(symTable, heap);
                    if (evaluated.getType().equals(refValue.getLocationType())) {
                        heap.update(refValue.getAddress(), evaluated);
                        state.setHeap(heap);
                    } else
                        throw new MyException(String.format("%s not of %s", evaluated, refValue.getLocationType()));
                } else
                    throw new MyException(String.format("The RefValue %s is not in the heap", value));
            } else
                throw new MyException(String.format("%s not of RefType", value));
        } else
            throw new MyException(String.format("%s not present in the symTable", varName));
        return null;
    }

    @Override
    public MyIDictionary<String, IType> typeCheck(MyIDictionary<String, IType> typeEnv) throws MyException {
        if (typeEnv.lookUp(varName).equals(new RefType(expression.typeCheck(typeEnv))))
            return typeEnv;
        else
            throw new TypeCheckException("HeapWriting: they must be the same type.");
    }

    @Override
    public IStatement deepCopy() {
        return new HeapWriting(varName, expression.deepCopy());
    }

    @Override
    public String toString() {
        return String.format("HeapWriting(%s, %s)", varName, expression);
    }
}
