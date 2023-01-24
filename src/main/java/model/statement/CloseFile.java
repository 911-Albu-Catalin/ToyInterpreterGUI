package model.statement;

import exceptions.FileException;
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
import java.io.IOException;

public class CloseFile implements IStatement{
    private final IExpression expression;

    public CloseFile(IExpression expression) {
        this.expression = expression;
    }

    @Override
    public ProgramState execute(ProgramState state) throws MyException {
        IValue value = expression.eval(state.getSymTable(), state.getHeap());
        if (value.getType().equals(new StringType())) {
            StringValue fileName = (StringValue) value;
            MyIDictionary<String, BufferedReader> fileTable = state.getFileTable();
            if (fileTable.isDefined(fileName.getValue())) {
                BufferedReader br = fileTable.lookUp(fileName.getValue());
                try {
                    br.close();
                } catch (IOException e) {
                    throw new FileException(String.format("Error when closing %s", value));
                }
                fileTable.remove(fileName.getValue());
                state.setFileTable(fileTable);
            } else
                throw new FileException(String.format("%s is not in the FileTable", value));
        } else
            throw new FileException(String.format("%s not StringValue", expression));
        return null;
    }

    @Override
    public MyIDictionary<String, IType> typeCheck(MyIDictionary<String, IType> typeEnv) throws MyException {
        if (expression.typeCheck(typeEnv).equals(new StringType()))
            return typeEnv;
        else
            throw new TypeCheckException("CloseFile requires a string expression.");

    }

    @Override
    public IStatement deepCopy() {
        return new CloseFile(expression.deepCopy());
    }

    @Override
    public String toString() {
        return String.format("CloseFile(%s)", expression.toString());
    }
}
