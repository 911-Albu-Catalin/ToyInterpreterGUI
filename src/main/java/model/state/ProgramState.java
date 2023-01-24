package model.state;

import exceptions.MyException;
import model.statement.IStatement;
import model.value.IValue;

import java.io.BufferedReader;
import java.util.List;

public class ProgramState {
    private MyIStack<IStatement> exeStack;
    private MyIDictionary<String, IValue> symTable;
    private MyIList<IValue> out;
    private MyIDictionary<String, BufferedReader> fileTable;
    private MyIHeap heap;

    private MyILockTable lockTable;
    private IStatement originalProgram;
    private int id;
    private static int lastId = 0;

    public ProgramState(MyIStack<IStatement> stack, MyIDictionary<String, IValue> symTable, MyIList<IValue> out, MyIDictionary<String, BufferedReader> fileTable, MyIHeap heap,  MyILockTable lockTable, IStatement program) {
        this.exeStack = stack;
        this.symTable = symTable;
        this.out = out;
        this.fileTable = fileTable;
        this.heap = heap;
        this.originalProgram = program.deepCopy();
        this.exeStack.push(this.originalProgram);
        this.id = setId();
        this.lockTable = lockTable;

    }

    public ProgramState(MyIStack<IStatement> stack, MyIDictionary<String, IValue> symTable, MyIList<IValue> out, MyIDictionary<String, BufferedReader> fileTable, MyIHeap heap,  MyILockTable lockTable) {
        this.exeStack = stack;
        this.symTable = symTable;
        this.out = out;
        this.fileTable = fileTable;
        this.heap = heap;
        this.id = setId();
        this.lockTable = lockTable;

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

    public void setSymTable(MyIDictionary<String, IValue> newSymTable) {
        this.symTable = newSymTable;
    }

    public void setHeap(MyIHeap newHeap) {
        this.heap = newHeap;
    }

    public void setFileTable(MyIDictionary<String, BufferedReader> newFileTable) {
        this.fileTable = newFileTable;
    }

    public void setLockTable(MyILockTable newLockTable) {
        this.lockTable = newLockTable;
    }

    public int getId() {
        return this.id;
    }


    public MyIDictionary<String, IValue> getSymTable() {
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

    public MyILockTable getLockTable() {
        return lockTable;
    }

    public boolean isNotCompleted() {
        return exeStack.isEmpty();
    }

    public ProgramState oneStep() throws MyException {
        if (exeStack.isEmpty())
            throw new MyException("Program state is empty!");
        IStatement currentStatement = exeStack.pop();
        return currentStatement.execute(this);
    }

    public String exeStackToString() {
        StringBuilder exeStackStringBuilder = new StringBuilder();
        List<IStatement> stack = exeStack.getReversed();
        for (IStatement statement: stack) {
            exeStackStringBuilder.append(statement.toString()).append("\n");
        }
        return exeStackStringBuilder.toString();
    }

    public String symTableToString() throws MyException {
        StringBuilder symTableStringBuilder = new StringBuilder();
        for (String key: symTable.keySet()) {
            symTableStringBuilder.append(String.format("%s -> %s\n", key, symTable.lookUp(key).toString()));
        }
        return symTableStringBuilder.toString();
    }

    public String outToString() {
        StringBuilder outStringBuilder = new StringBuilder();
        for (IValue elem: out.getList()) {
            outStringBuilder.append(String.format("%s\n", elem.toString()));
        }
        return outStringBuilder.toString();
    }

    public String heapToString() throws MyException {
        StringBuilder heapStringBuilder = new StringBuilder();
        for (Object key: heap.keySet()) {
            heapStringBuilder.append(String.format("%d -> %s\n", key, heap.get((Integer) key)));
        }
        return heapStringBuilder.toString();
    }

    public String fileTableToString() {
        StringBuilder fileTableStringBuilder = new StringBuilder();
        for (String key: fileTable.keySet()) {
            fileTableStringBuilder.append(String.format("%s\n", key));
        }
        return fileTableStringBuilder.toString();
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
        returnStr += "Lock Table:\n";
        returnStr += lockTable.toString();
        returnStr += "\n";
        return returnStr;
    }
}
