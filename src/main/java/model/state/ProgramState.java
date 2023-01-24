package model.state;

import exceptions.MyException;
import model.statement.IStatement;
import model.value.IValue;

import java.io.BufferedReader;
import java.util.List;

public class ProgramState {
    private MyIStack<IStatement> exeStack;
    private MyIStack<MyIDictionary<String, IValue>> symTable;
    private MyIList<IValue> out;
    private MyIDictionary<String, BufferedReader> fileTable;
    private MyIProcedureTable procTable;
    private MyIHeap heap;
    private IStatement originalProgram;
    private int id;
    private static int lastId = 0;

    public ProgramState(MyIStack<IStatement> stack, MyIStack<MyIDictionary<String, IValue>> symTable, MyIList<IValue> out, MyIDictionary<String, BufferedReader> fileTable, MyIHeap heap, MyIProcedureTable pTable, IStatement program) {
        this.exeStack = stack;
        this.symTable = symTable;
        this.out = out;
        this.fileTable = fileTable;
        this.heap = heap;
        this.originalProgram = program.deepCopy();
        this.exeStack.push(this.originalProgram);
        this.procTable = pTable;
        this.id = setId();
    }

    public ProgramState(MyIStack<IStatement> stack, MyIStack<MyIDictionary<String, IValue>> symTable, MyIList<IValue> out, MyIDictionary<String, BufferedReader> fileTable, MyIHeap heap, MyIProcedureTable pTable) {
        this.exeStack = stack;
        this.symTable = symTable;
        this.out = out;
        this.fileTable = fileTable;
        this.heap = heap;
        this.id = setId();
        this.procTable = pTable;
    }

    public synchronized int setId() {
        lastId++;
        return lastId;
    }


    public void setExeStack(MyIStack<IStatement> newStack) {
        this.exeStack = newStack;
    }

    public void setOut(MyIList<IValue> newOut) {
        this.out = newOut;
    }

    public void setSymTable(MyIStack<MyIDictionary<String, IValue>> newSymTable) {
        this.symTable = newSymTable;
    }

    public void setHeap(MyIHeap newHeap) {
        this.heap = newHeap;
    }

    public void setFileTable(MyIDictionary<String, BufferedReader> newFileTable) {
        this.fileTable = newFileTable;
    }

    public void setProcedureTable(MyIProcedureTable newProcTable) {
        this.procTable = newProcTable;
    }

    public int getId() {
        return this.id;
    }


    public MyIStack<MyIDictionary<String, IValue>> getSymTable() {
        return symTable;
    }

    public MyIHeap getHeap() {
        return heap;
    }

    public MyIStack<IStatement> getExeStack() {
        return exeStack;
    }

    public MyIDictionary<String, BufferedReader> getFileTable() {
        return fileTable;
    }

    public MyIList<IValue> getOut() {
        return out;
    }

    public MyIProcedureTable getProcTable() {
        return procTable;
    }

    public boolean isNotCompleted() {
        return exeStack.isEmpty();
    }

    public MyIDictionary<String, IValue> getTopSymTable() {
        try {
            return symTable.peek();
        } catch (Exception e) {
            return null;
        }
    }

    public ProgramState oneStep() throws MyException {
        if (exeStack.isEmpty())
            throw new MyException("Program state is empty!");
        IStatement currentStatement = exeStack.pop();
        return currentStatement.execute(this);
    }

    @Override
    public String toString() {
        String returnStr = "";
        returnStr += "Id: " + id + "\n";
        returnStr += "Execution Stack:\n";
        returnStr += exeStack.toString() + "\n";
        returnStr += "Symbol table:\n";
        returnStr += symTable.toString() + "\n";
        returnStr += "Output:\n";
        returnStr += out.toString() + "\n";
        returnStr += "File Table:\n";
        returnStr += fileTable.toString() + "\n";
        returnStr += "Heap Table:\n";
        returnStr += heap.toString();
        returnStr += "\n";
        returnStr += "Proc table:\n";
        returnStr += procTable.toString() + "\n";
        return returnStr;
    }
}
