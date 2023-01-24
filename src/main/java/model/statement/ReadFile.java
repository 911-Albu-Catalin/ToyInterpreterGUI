package model.statement;

import exceptions.MyException;
import exceptions.TypeCheckException;
import model.expression.IExpression;
import model.state.ProgramState;
import model.type.IntegerType;
import model.type.StringType;
import model.type.IType;
import model.state.MyIDictionary;
import model.value.IntegerValue;
import model.value.StringValue;
import model.value.IValue;

import java.io.BufferedReader;
import java.io.IOException;

public class ReadFile implements IStatement{
    private final IExpression expression;
    private final String fileName;

    public ReadFile(IExpression expression, String varName) {
        this.expression = expression;
        this.fileName = varName;
    }
    @Override
    public ProgramState execute(ProgramState state) throws MyException {
        MyIDictionary<String, IValue> symTable = state.getSymTable();
        MyIDictionary<String, BufferedReader> fileTable = state.getFileTable();

        if (symTable.isDefined(fileName)) {
            IValue value = symTable.lookUp(fileName);
            if (value.getType().equals(new IntegerType())) {
                IValue fileNameValue = expression.eval(symTable, state.getHeap());
                if (fileNameValue.getType().equals(new StringType())) {
                    StringValue castValue = (StringValue)fileNameValue;
                    if (fileTable.isDefined(castValue.getValue())) {
                        BufferedReader br = fileTable.lookUp(castValue.getValue());
                        try {
                            String line = br.readLine();
                            if (line == null)
                                line = "0";
                            symTable.put(fileName, new IntegerValue(Integer.parseInt(line)));
                        } catch (IOException e) {
                            throw new MyException(String.format("Could not read from file %s", castValue));
                        }
                    } else {
                        throw new MyException(String.format("The file table does not contain %s", castValue));
                    }
                } else {
                    throw new MyException(String.format("%s does not evaluate to StringType", value));
                }
            } else {
                throw new MyException(String.format("%s is not of type IntType", value));
            }
        } else {
            throw new MyException(String.format("%s is not present in the symTable.", fileName));
        }
        return null;
    }

    @Override
    public MyIDictionary<String, IType> typeCheck(MyIDictionary<String, IType> typeEnv) throws MyException {
        if (expression.typeCheck(typeEnv).equals(new StringType()))
            if (typeEnv.lookUp(fileName).equals(new IntegerType()))
                return typeEnv;
            else
                throw new TypeCheckException("ReadFile requires an int as its variable parameter.");
        else
            throw new TypeCheckException("ReadFile requires a string as es expression parameter.");
    }

    @Override
    public IStatement deepCopy() {
        return new ReadFile(expression.deepCopy(), fileName);
    }

    @Override
    public String toString() {
        return String.format("ReadFile(%s, %s)", expression.toString(), fileName);
    }
}
