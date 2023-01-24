package view;

import controller.Controller;
import exceptions.MyException;
import model.expression.*;
import model.state.ProgramState;
import model.statement.*;
import model.type.BooleanType;
import model.type.IntegerType;
import model.type.RefType;
import model.type.StringType;
import model.state.MyDictionary;
import model.state.MyHeap;
import model.state.MyList;
import model.state.MyStack;
import model.value.BooleanValue;
import model.value.IValue;
import model.value.IntegerValue;
import model.value.StringValue;
import repository.IRepository;
import repository.Repository;

import java.io.IOException;

public class Interpreter {
    public static void main(String[] args){
        TextMenu menu = new TextMenu();
        menu.addCommand(new ExitCommand("0", "exit"));

//        IStatement ex1 = new Compound(new VariableDeclaration("v", new IntegerType()),
//                new Compound(new Assignment("v", new ValueExpression(new IntegerValue(2))),
//                        new Print(new VariableExpression("v"))));
//        try {
//            ex1.typeCheck(new MyDictionary<>());
//            ProgramState prg1 = new ProgramState(new MyStack<>(), new MyStack<>(), new MyList<>(), new MyDictionary<>(), new MyHeap(), ex1);
//            IRepository repo1;
//            repo1 = new Repository(prg1, "log1.txt");
//            Controller controller1 = new Controller(repo1);
//            menu.addCommand(new RunExampleCommand("1", ex1.toString(), controller1));
//        } catch (IOException | MyException e) {
//            System.out.println("\u001B[31m" + e.getMessage() + "\u001B[0m");
//        }
//
//        IStatement ex2 = new Compound(new VariableDeclaration("a",new IntegerType()),
//                new Compound(new VariableDeclaration("b",new IntegerType()),
//                        new Compound(new Assignment("a", new ArithmeticExpression('+',new ValueExpression(new IntegerValue(2)),new
//                                ArithmeticExpression('/',new ValueExpression(new IntegerValue(3)), new ValueExpression(new IntegerValue(0))))),
//                                new Compound(new Assignment("b",new ArithmeticExpression('+',new VariableExpression("a"), new ValueExpression(new
//                                        IntegerValue(1)))), new Print(new VariableExpression("b"))))));
//        try {
//            ex2.typeCheck(new MyDictionary<>());
//            ProgramState prg2 = new ProgramState(new MyStack<>(), new MyStack<>(), new MyList<>(), new MyDictionary<>(), new MyHeap(), ex2);
//            IRepository repo2;
//            repo2 = new Repository(prg2, "log2.txt");
//            Controller controller2 = new Controller(repo2);
//            menu.addCommand(new RunExampleCommand("2", ex2.toString(), controller2));
//        } catch (IOException | MyException e) {
//            System.out.println("\u001B[31m" + e.getMessage() + "\u001B[0m");
//        }
//
//        IStatement ex3 = new Compound(new VariableDeclaration("a", new BooleanType()),
//                new Compound(new VariableDeclaration("v", new IntegerType()),
//                        new Compound(new Assignment("a", new ValueExpression(new BooleanValue(true))),
//                                new Compound(new If(new VariableExpression("a"),
//                                        new Assignment("v", new ValueExpression(new IntegerValue(2))),
//                                        new Assignment("v", new ValueExpression(new IntegerValue(3)))),
//                                        new Print(new VariableExpression("v"))))));
//        try {
//            ex3.typeCheck(new MyDictionary<>());
//            ProgramState prg3 = new ProgramState(new MyStack<>(), new MyStack<>(), new MyList<>(), new MyDictionary<>(), new MyHeap(), ex3);
//            IRepository repo3;
//            repo3 = new Repository(prg3, "log3.txt");
//            Controller controller3 = new Controller(repo3);
//            menu.addCommand(new RunExampleCommand("3", ex3.toString(), controller3));
//        } catch (IOException | MyException e) {
//            System.out.println("\u001B[31m" + e.getMessage() + "\u001B[0m");
//        }
//
//        IStatement ex4 = new Compound(new VariableDeclaration("varf", new StringType()),
//                new Compound(new Assignment("varf", new ValueExpression(new StringValue("test.in"))),
//                        new Compound(new OpenFile(new VariableExpression("varf")),
//                                new Compound(new VariableDeclaration("varc", new IntegerType()),
//                                        new Compound(new ReadFile(new VariableExpression("varf"), "varc"),
//                                                new Compound(new Print(new VariableExpression("varc")),
//                                                        new Compound(new ReadFile(new VariableExpression("varf"), "varc"),
//                                                                new Compound(new Print(new VariableExpression("varc")),
//                                                                        new CloseFile(new VariableExpression("varf"))))))))));
//
//        try {
//            ex4.typeCheck(new MyDictionary<>());
//            ProgramState prg4 = new ProgramState(new MyStack<>(), new MyStack<>(), new MyList<>(), new MyDictionary<>(), new MyHeap(), ex4);
//            IRepository repo4;
//            repo4 = new Repository(prg4, "log4.txt");
//            Controller controller4 = new Controller(repo4);
//            menu.addCommand(new RunExampleCommand("4", ex4.toString(), controller4));
//        } catch (IOException | MyException e) {
//            System.out.println("\u001B[31m" + e.getMessage() + "\u001B[0m");
//        }
//
//        IStatement ex5 = new Compound(new VariableDeclaration("a", new IntegerType()),
//                new Compound(new VariableDeclaration("b", new IntegerType()),
//                        new Compound(new Assignment("a", new ValueExpression(new IntegerValue(5))),
//                                new Compound(new Assignment("b", new ValueExpression(new IntegerValue(7))),
//                                        new If(new RelationalExpression(">", new VariableExpression("a"),
//                                                new VariableExpression("b")),new Print(new VariableExpression("a")),
//                                                new Print(new VariableExpression("b")))))));
//        try {
//            ex5.typeCheck(new MyDictionary<>());
//            ProgramState prg5 = new ProgramState(new MyStack<>(), new MyStack<>(), new MyList<>(), new MyDictionary<>(), new MyHeap(), ex5);
//            IRepository repo5;
//            repo5 = new Repository(prg5, "log5.txt");
//            Controller controller5 = new Controller(repo5);
//            menu.addCommand(new RunExampleCommand("5", ex5.toString(), controller5));
//        } catch (IOException | MyException e) {
//            System.out.println("\u001B[31m" + e.getMessage() + "\u001B[0m");
//        }
//
//        IStatement ex6 = new Compound(new VariableDeclaration("varf", new StringType()),
//                new Compound(new Assignment("varf", new ValueExpression(new StringValue("test.in"))),
//                        new Compound(new OpenFile(new VariableExpression("varf")),
//                                new Compound(new VariableDeclaration("varc", new IntegerType()),
//                                        new Compound(new ReadFile(new VariableExpression("varf"), "varc"),
//                                                new Compound(new Print(new VariableExpression("varc")),
//                                                        new Compound(new ReadFile(new VariableExpression("varf"), "varc"),
//                                                                new Compound(new Print(new VariableExpression("varc")),
//                                                                        new Compound(new CloseFile(new VariableExpression("varf")), new CloseFile(new VariableExpression("varf")))))))))));
//        try {
//            ex6.typeCheck(new MyDictionary<>());
//            ProgramState prg6 = new ProgramState(new MyStack<>(), new MyStack<>(), new MyList<>(), new MyDictionary<>(), new MyHeap(), ex6);
//            IRepository repo6;
//            repo6 = new Repository(prg6, "log6.txt");
//            Controller controller6 = new Controller(repo6);
//            menu.addCommand(new RunExampleCommand("6", ex6.toString(), controller6));
//        } catch (IOException | MyException e) {
//            System.out.println("\u001B[31m" + e.getMessage() + "\u001B[0m");
//        }
//
//        IStatement ex7 = new Compound(new VariableDeclaration("v", new IntegerType()),
//                new Compound(new Assignment("v", new ValueExpression(new IntegerValue(4))),
//                        new Compound(new While(new RelationalExpression(">", new VariableExpression("v"), new ValueExpression(new IntegerValue(0))),
//                                new Compound(new Print(new VariableExpression("v")), new Assignment("v",new ArithmeticExpression('-', new VariableExpression("v"), new ValueExpression(new IntegerValue(1)))))),
//                                new Print(new VariableExpression("v")))));
//        try {
//            ex7.typeCheck(new MyDictionary<>());
//            ProgramState prg7 = new ProgramState(new MyStack<>(), new MyStack<>(), new MyList<>(), new MyDictionary<>(), new MyHeap(), ex7);
//            IRepository repo7;
//            repo7 = new Repository(prg7, "log7.txt");
//            Controller controller7 = new Controller(repo7);
//            menu.addCommand(new RunExampleCommand("7", ex7.toString(), controller7));
//        } catch (IOException | MyException e) {
//            System.out.println("\u001B[31m" + e.getMessage() + "\u001B[0m");
//        }
//
//        IStatement ex8 = new Compound(new VariableDeclaration("v", new RefType(new IntegerType())),
//                new Compound(new HeapAllocation("v", new ValueExpression(new IntegerValue(20))),
//                        new Compound(new VariableDeclaration("a", new RefType(new RefType(new IntegerType()))),
//                                new Compound(new HeapAllocation("a", new VariableExpression("v")),
//                                        new Compound(new Print(new VariableExpression("v")), new Print(new VariableExpression("a")))))));
//        try {
//            ex8.typeCheck(new MyDictionary<>());
//            ProgramState prg8 = new ProgramState(new MyStack<>(), new MyStack<>(), new MyList<>(), new MyDictionary<>(), new MyHeap(), ex8);
//            IRepository repo8;
//            repo8 = new Repository(prg8, "log8.txt");
//            Controller controller8 = new Controller(repo8);
//            menu.addCommand(new RunExampleCommand("8", ex8.toString(), controller8));
//        } catch (IOException | MyException e) {
//            System.out.println("\u001B[31m" + e.getMessage() + "\u001B[0m");
//        }
//
//        IStatement ex9 = new Compound(new VariableDeclaration("v", new RefType(new IntegerType())),
//                new Compound(new HeapAllocation("v", new ValueExpression(new IntegerValue(20))),
//                        new Compound(new VariableDeclaration("a", new RefType(new RefType(new IntegerType()))),
//                                new Compound(new HeapAllocation("a", new VariableExpression("v")),
//                                        new Compound(new Print(new HeapReading(new VariableExpression("v"))),
//                                                new Print(new ArithmeticExpression('+',new HeapReading(new HeapReading(new VariableExpression("a"))), new ValueExpression(new IntegerValue(5)))))))));
//        try {
//            ex9.typeCheck(new MyDictionary<>());
//            ProgramState prg9 = new ProgramState(new MyStack<>(), new MyStack<>(), new MyList<>(), new MyDictionary<>(), new MyHeap(), ex9);
//            IRepository repo9;
//            repo9 = new Repository(prg9, "log9.txt");
//            Controller controller9 = new Controller(repo9);
//            menu.addCommand(new RunExampleCommand("9", ex9.toString(), controller9));
//        } catch (IOException | MyException e) {
//            System.out.println("\u001B[31m" + e.getMessage() + "\u001B[0m");
//        }
//
//        IStatement ex10 = new Compound(new VariableDeclaration("v", new RefType(new IntegerType())),
//                new Compound(new HeapAllocation("v", new ValueExpression(new IntegerValue(20))),
//                        new Compound( new Print(new HeapReading(new VariableExpression("v"))),
//                                new Compound(new HeapWriting("v", new ValueExpression(new IntegerValue(30))),
//                                        new Print(new ArithmeticExpression('+', new HeapReading(new VariableExpression("v")), new ValueExpression(new IntegerValue(5))))))));
//        try {
//            ex10.typeCheck(new MyDictionary<>());
//            ProgramState prg10 = new ProgramState(new MyStack<>(), new MyStack<>(), new MyList<>(), new MyDictionary<>(), new MyHeap(), ex10);
//            IRepository repo10;
//            repo10 = new Repository(prg10, "log10.txt");
//            Controller controller10 = new Controller(repo10);
//            menu.addCommand(new RunExampleCommand("10", ex10.toString(), controller10));
//        } catch (IOException | MyException e) {
//            System.out.println("\u001B[31m" + e.getMessage() + "\u001B[0m");
//        }
//
//        IStatement ex11 = new Compound(new VariableDeclaration("v", new RefType(new IntegerType())),
//                new Compound(new HeapAllocation("v", new ValueExpression(new IntegerValue(20))),
//                        new Compound(new VariableDeclaration("a", new RefType(new RefType(new IntegerType()))),
//                                new Compound(new HeapAllocation("a", new VariableExpression("v")),
//                                        new Compound(new HeapAllocation("v", new ValueExpression(new IntegerValue(30))),
//                                                new Print(new HeapReading(new HeapReading(new VariableExpression("a")))))))));
//        try {
//            ex11.typeCheck(new MyDictionary<>());
//            ProgramState prg11 = new ProgramState(new MyStack<>(), new MyStack<>(), new MyList<>(), new MyDictionary<>(), new MyHeap(), ex11);
//            IRepository repo11;
//            repo11 = new Repository(prg11, "log11.txt");
//            Controller controller11 = new Controller(repo11);
//            menu.addCommand(new RunExampleCommand("11", ex11.toString(), controller11));
//        } catch (IOException | MyException e) {
//            System.out.println("\u001B[31m" + e.getMessage() + "\u001B[0m");
//        }
//
//        IStatement ex12 = new Compound(new VariableDeclaration("v", new IntegerType()),
//                new Compound(new VariableDeclaration("a", new RefType(new IntegerType())),
//                        new Compound(new Assignment("v", new ValueExpression(new IntegerValue(10))),
//                                new Compound(new HeapAllocation("a", new ValueExpression(new IntegerValue(22))),
//                                        new Compound(new Fork(new Compound(new HeapWriting("a", new ValueExpression(new IntegerValue(30))),
//                                                new Compound(new Assignment("v", new ValueExpression(new IntegerValue(32))),
//                                                        new Compound(new Print(new VariableExpression("v")), new Print(new HeapReading(new VariableExpression("a"))))))),
//                                                new Compound(new Print(new VariableExpression("v")), new Print(new HeapReading(new VariableExpression("a")))))))));
//        try {
//            ex12.typeCheck(new MyDictionary<>());
//            ProgramState prg12 = new ProgramState(new MyStack<>(), new MyStack<>(), new MyList<>(), new MyDictionary<>(), new MyHeap(), ex12);
//            IRepository repo12;
//            repo12 = new Repository(prg12, "log12.txt");
//            Controller controller12 = new Controller(repo12);
//            menu.addCommand(new RunExampleCommand("12", ex12.toString(), controller12));
//        } catch (IOException | MyException e) {
//            System.out.println("\u001B[31m" + e.getMessage() + "\u001B[0m");
//        }

        menu.show();
    }
}
