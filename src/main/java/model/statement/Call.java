package model.statement;

import exceptions.MyException;
import model.expression.IExpression;
import model.state.*;
import model.type.IType;
import model.value.IValue;

import java.util.List;

public class Call implements IStatement{
    private final String procedureName;
    private final List<IExpression> expressions;

    public Call(String procedureName, List<IExpression> expressions) {
        this.procedureName = procedureName;
        this.expressions = expressions;
    }
    @Override
    public ProgramState execute(ProgramState state) throws  MyException {
        MyIDictionary<String, IValue> symTable = state.getTopSymTable();
        MyIHeap heap = state.getHeap();
        MyIProcedureTable procTable = state.getProcTable();
        if (procTable.isDefined(procedureName)) {
            List<String> variables = procTable.lookUp(procedureName).getKey();
            IStatement statement = procTable.lookUp(procedureName).getValue();
            MyIDictionary<String, IValue> newSymTable = new MyDictionary<>();
            for(String var: variables) {
                int ind = variables.indexOf(var);
                newSymTable.put(var, expressions.get(ind).eval(symTable, heap));
            }
            state.getSymTable().push(newSymTable);
            state.getExeStack().push(new Return());
            state.getExeStack().push(statement);
        } else {
            throw new MyException("Procedure not defined!");
        }
        return null;
    }

    @Override
    public MyIDictionary<String, IType> typeCheck(MyIDictionary<String, IType> typeEnv) throws MyException {
        return typeEnv;
    }

    @Override
    public IStatement deepCopy() {
        return new Call(procedureName, expressions);
    }

    @Override
    public String toString() {
        return String.format("call %s%s", procedureName, expressions.toString());
    }
}
