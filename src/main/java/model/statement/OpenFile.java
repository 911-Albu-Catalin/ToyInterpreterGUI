package model.statement;

import exceptions.MyException;
import exceptions.TypeCheckException;
import model.expression.IExpression;
import model.state.ProgramState;
import model.type.StringType;
import model.type.IType;
import model.state.MyIDictionary;
import model.value.StringValue;
import model.value.IValue;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class OpenFile implements IStatement{
    private final IExpression expression;

    public OpenFile(IExpression expression) {
        this.expression = expression;
    }

    @Override
    public ProgramState execute(ProgramState state) throws MyException {
        IValue value = expression.eval(state.getSymTable(), state.getHeap());
        if (value.getType().equals(new StringType())) {
            StringValue fileName = (StringValue) value;
            MyIDictionary<String, BufferedReader> fileTable = state.getFileTable();
            if (!fileTable.isDefined(fileName.getValue())) {
                BufferedReader br;
                try {
                    br = new BufferedReader(new FileReader(fileName.getValue()));
                } catch (FileNotFoundException e) {
                    throw new MyException(String.format("%s could not be opened", fileName.getValue()));
                }
                fileTable.put(fileName.getValue(), br);
                state.setFileTable(fileTable);
            } else {
                throw new MyException(String.format("%s is already opened", fileName.getValue()));
            }
        } else {
            throw new MyException(String.format("%s does not evaluate to StringType", expression));
        }
        return null;
    }

    @Override
    public MyIDictionary<String, IType> typeCheck(MyIDictionary<String, IType> typeEnv) throws TypeCheckException, MyException {
        if (expression.typeCheck(typeEnv).equals(new StringType()))
            return typeEnv;
        else
            throw new TypeCheckException("OpenFile requires a string.");
    }

    @Override
    public IStatement deepCopy() {
        return new OpenFile(expression.deepCopy());
    }

    @Override
    public String toString() {
        return String.format("OpenFile(%s)", expression.toString());
    }
}
