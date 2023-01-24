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

public class HeapAllocation implements IStatement{
    private final String varName;
    private final IExpression expression;

    public HeapAllocation(String varName, IExpression expression) {
        this.varName = varName;
        this.expression = expression;
    }

    @Override
    public ProgramState execute(ProgramState state) throws MyException {
        MyIDictionary<String, IValue> symTable = state.getSymTable();
        MyIHeap heap = state.getHeap();
        if (symTable.isDefined(varName)) {
            IValue varValue = symTable.lookUp(varName);
            if ((varValue.getType() instanceof RefType)) {
                IValue evaluated = expression.eval(symTable, heap);
                IType locationType = ((RefValue) varValue).getLocationType();
                if (locationType.equals(evaluated.getType())) {
                    int newPosition = heap.add(evaluated);
                    symTable.put(varName, new RefValue(newPosition, locationType));
                    state.setSymTable(symTable);
                    state.setHeap(heap);
                } else
                    throw new MyException(String.format("%s not of %s", varName, evaluated.getType()));
            } else {
                throw new MyException(String.format("%s in not of RefType", varName));
            }
        } else {
            throw new MyException(String.format("%s not in symTable", varName));
        }
        return null;
    }

    @Override
    public MyIDictionary<String, IType> typeCheck(MyIDictionary<String, IType> typeEnv) throws MyException {
        IType typeVar = typeEnv.lookUp(varName);
        IType typeExpr = expression.typeCheck(typeEnv);
        if (typeVar.equals(new RefType(typeExpr)))
            return typeEnv;
        else
            throw new TypeCheckException("they must be of the same type");
    }

    @Override
    public IStatement deepCopy() {
        return new HeapAllocation(varName, expression.deepCopy());
    }

    @Override
    public String toString() {
        return String.format("HeapAllocation(%s, %s)", varName, expression);
    }
}
