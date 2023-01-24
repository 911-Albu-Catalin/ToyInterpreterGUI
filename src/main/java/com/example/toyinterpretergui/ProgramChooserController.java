package com.example.toyinterpretergui;

import controller.Controller;
import exceptions.MyException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.expression.*;
import model.state.*;
import model.statement.*;
import model.type.BooleanType;
import model.type.IntegerType;
import model.type.RefType;
import model.type.StringType;
import model.value.BooleanValue;
import model.value.IntegerValue;
import model.value.StringValue;
import repository.IRepository;
import repository.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProgramChooserController {
    private ProgramExecutorController programExecutorController;

    public void setProgramExecutorController(ProgramExecutorController programExecutorController) {
        this.programExecutorController = programExecutorController;
    }
    @FXML
    private ListView<IStatement> programsListView;

    @FXML
    private Button displayButton;

    @FXML
    public void initialize() {
        programsListView.setItems(getAllStatements());
        programsListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

    @FXML
    private void displayProgram(ActionEvent actionEvent) {
        IStatement selectedStatement = programsListView.getSelectionModel().getSelectedItem();
        if (selectedStatement == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error encountered!");
            alert.setContentText("No statement selected!");
            alert.showAndWait();
        } else {
            int id = programsListView.getSelectionModel().getSelectedIndex();
            try {
                selectedStatement.typeCheck(new MyDictionary<>());
                ProgramState programState = new ProgramState(new MyStack<>(), new MyDictionary<>(), new MyList<>(), new MyDictionary<>(), new MyHeap(), new MyLatchTable(), selectedStatement);
                IRepository repository = new Repository(programState, "log" + (id + 1) + ".txt");
                Controller controller = new Controller(repository);
                programExecutorController.setController(controller);
            } catch (MyException | IOException exception) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error encountered!");
                alert.setContentText(exception.getMessage());
                alert.showAndWait();
            }
        }
    }

    @FXML
    private ObservableList<IStatement> getAllStatements() {
        List<IStatement> allStatements = new ArrayList<>();

        IStatement ex1 = new Compound(new VariableDeclaration("v", new IntegerType()),
                new Compound(new Assignment("v", new ValueExpression(new IntegerValue(2))),
                        new Print(new VariableExpression("v"))));
        allStatements.add(ex1);

        IStatement ex2 = new Compound(new VariableDeclaration("a",new IntegerType()),
                new Compound(new VariableDeclaration("b",new IntegerType()),
                        new Compound(new Assignment("a", new ArithmeticExpression('+',new ValueExpression(new IntegerValue(2)),new
                                ArithmeticExpression('*',new ValueExpression(new IntegerValue(3)), new ValueExpression(new IntegerValue(5))))),
                                new Compound(new Assignment("b",new ArithmeticExpression('+',new VariableExpression("a"), new ValueExpression(new
                                        IntegerValue(1)))), new Print(new VariableExpression("b"))))));
        allStatements.add(ex2);

        IStatement ex3 = new Compound(new VariableDeclaration("a", new BooleanType()),
                new Compound(new VariableDeclaration("v", new IntegerType()),
                        new Compound(new Assignment("a", new ValueExpression(new BooleanValue(true))),
                                new Compound(new If(new VariableExpression("a"),
                                        new Assignment("v", new ValueExpression(new IntegerValue(2))),
                                        new Assignment("v", new ValueExpression(new IntegerValue(3)))),
                                        new Print(new VariableExpression("v"))))));
        allStatements.add(ex3);

        IStatement ex4 = new Compound(new VariableDeclaration("varf", new StringType()),
                new Compound(new Assignment("varf", new ValueExpression(new StringValue("test.in"))),
                        new Compound(new OpenFile(new VariableExpression("varf")),
                                new Compound(new VariableDeclaration("varc", new IntegerType()),
                                        new Compound(new ReadFile(new VariableExpression("varf"), "varc"),
                                                new Compound(new Print(new VariableExpression("varc")),
                                                        new Compound(new ReadFile(new VariableExpression("varf"), "varc"),
                                                                new Compound(new Print(new VariableExpression("varc")),
                                                                        new CloseFile(new VariableExpression("varf"))))))))));
        allStatements.add(ex4);

        IStatement ex5 = new Compound(new VariableDeclaration("a", new IntegerType()),
                new Compound(new VariableDeclaration("b", new IntegerType()),
                        new Compound(new Assignment("a", new ValueExpression(new IntegerValue(5))),
                                new Compound(new Assignment("b", new ValueExpression(new IntegerValue(7))),
                                        new If(new RelationalExpression(">", new VariableExpression("a"),
                                                new VariableExpression("b")),new Print(new VariableExpression("a")),
                                                new Print(new VariableExpression("b")))))));
        allStatements.add(ex5);

        IStatement ex6 = new Compound(new VariableDeclaration("v", new IntegerType()),
                new Compound(new Assignment("v", new ValueExpression(new IntegerValue(4))),
                        new Compound(new While(new RelationalExpression(">", new VariableExpression("v"), new ValueExpression(new IntegerValue(0))),
                                new Compound(new Print(new VariableExpression("v")), new Assignment("v",new ArithmeticExpression('-', new VariableExpression("v"), new ValueExpression(new IntegerValue(1)))))),
                                new Print(new VariableExpression("v")))));
        allStatements.add(ex6);

        IStatement ex7 = new Compound(new VariableDeclaration("v", new RefType(new IntegerType())),
                new Compound(new HeapAllocation("v", new ValueExpression(new IntegerValue(20))),
                        new Compound(new VariableDeclaration("a", new RefType(new RefType(new IntegerType()))),
                                new Compound(new HeapAllocation("a", new VariableExpression("v")),
                                        new Compound(new Print(new VariableExpression("v")), new Print(new VariableExpression("a")))))));
        allStatements.add(ex7);

        IStatement ex8 = new Compound(new VariableDeclaration("v", new RefType(new IntegerType())),
                new Compound(new HeapAllocation("v", new ValueExpression(new IntegerValue(20))),
                        new Compound(new VariableDeclaration("a", new RefType(new RefType(new IntegerType()))),
                                new Compound(new HeapAllocation("a", new VariableExpression("v")),
                                        new Compound(new Print(new HeapReading(new VariableExpression("v"))),
                                                new Print(new ArithmeticExpression('+',new HeapReading(new HeapReading(new VariableExpression("a"))), new ValueExpression(new IntegerValue(5)))))))));
        allStatements.add(ex8);

        IStatement ex9 = new Compound(new VariableDeclaration("v", new RefType(new IntegerType())),
                new Compound(new HeapAllocation("v", new ValueExpression(new IntegerValue(20))),
                        new Compound( new Print(new HeapReading(new VariableExpression("v"))),
                                new Compound(new HeapWriting("v", new ValueExpression(new IntegerValue(30))),
                                        new Print(new ArithmeticExpression('+', new HeapReading(new VariableExpression("v")), new ValueExpression(new IntegerValue(5))))))));
        allStatements.add(ex9);

        IStatement ex10 = new Compound(new VariableDeclaration("v", new RefType(new IntegerType())),
                new Compound(new HeapAllocation("v", new ValueExpression(new IntegerValue(20))),
                        new Compound(new VariableDeclaration("a", new RefType(new RefType(new IntegerType()))),
                                new Compound(new HeapAllocation("a", new VariableExpression("v")),
                                        new Compound(new HeapAllocation("v", new ValueExpression(new IntegerValue(30))),
                                                new Print(new HeapReading(new HeapReading(new VariableExpression("a")))))))));
        allStatements.add(ex10);

        IStatement ex11 = new Compound(new VariableDeclaration("v", new IntegerType()),
                new Compound(new VariableDeclaration("a", new RefType(new IntegerType())),
                        new Compound(new Assignment("v", new ValueExpression(new IntegerValue(10))),
                                new Compound(new HeapAllocation("a", new ValueExpression(new IntegerValue(22))),
                                        new Compound(new Fork(new Compound(new HeapWriting("a", new ValueExpression(new IntegerValue(30))),
                                                new Compound(new Assignment("v", new ValueExpression(new IntegerValue(32))),
                                                        new Compound(new Print(new VariableExpression("v")), new Print(new HeapReading(new VariableExpression("a"))))))),
                                                new Compound(new Print(new VariableExpression("v")), new Print(new HeapReading(new VariableExpression("a")))))))));
        allStatements.add(ex11);
//        //FOR STATEMENT
//        IStatement ex12 = new Compound(new VariableDeclaration("v", new IntegerType()),
//                new Compound(new Assignment("v", new ValueExpression(new IntegerValue(20))),
//                        new Compound(new ForStatement("v", new ValueExpression(new IntegerValue(0)),
//                                new ValueExpression(new IntegerValue(3)),
//                                new ArithmeticExpression('+', new VariableExpression("v"), new ValueExpression(new IntegerValue(1))),
//                                new Fork(new Compound(new Print(new VariableExpression("v")),
//                                        new Assignment("v", new ArithmeticExpression('+', new VariableExpression("v"), new ValueExpression(new IntegerValue(1))))))),
//                                new Print(new ArithmeticExpression('*', new VariableExpression("v"), new ValueExpression(new IntegerValue(10)))))));
//        allStatements.add(ex12);
//
//        //SLEEP STATEMENT
//        IStatement ex13 = new Compound(new VariableDeclaration("v", new IntegerType()),
//                new Compound(new Assignment("v", new ValueExpression(new IntegerValue(0))),
//                        new Compound(new While(new RelationalExpression("<", new VariableExpression("v"), new ValueExpression(new IntegerValue(3))),
//                                new Compound(new Fork(new Compound(new Print(new VariableExpression("v")),
//                                        new Assignment("v", new ArithmeticExpression('+', new VariableExpression("v"), new ValueExpression(new IntegerValue(1)))))),
//                                        new Assignment("v", new ArithmeticExpression('+', new VariableExpression("v"), new ValueExpression(new IntegerValue(1)))))),
//                                new Compound(new Sleep(5), new Print(new ArithmeticExpression('*', new VariableExpression("v"), new ValueExpression(new IntegerValue(10))))))));
//        allStatements.add(ex13);
//
//        //WAIT STATEMENT
//        IStatement ex14 = new Compound(new VariableDeclaration("v", new IntegerType()),
//                new Compound(new Assignment("v", new ValueExpression(new IntegerValue(20))),
//                        new Compound(new Wait(10),
//                                new Print(new ArithmeticExpression('*', new VariableExpression("v"), new ValueExpression(new IntegerValue(10)))))));
//        allStatements.add(ex14);
//
//        //SWITCH STATEMENT
//        IStatement ex15 = new Compound(new VariableDeclaration("a", new IntegerType()),
//                new Compound(new VariableDeclaration("b", new IntegerType()),
//                        new Compound(new VariableDeclaration("c", new IntegerType()),
//                                new Compound(new Assignment("a", new ValueExpression(new IntegerValue(1))),
//                                        new Compound(new Assignment("b", new ValueExpression(new IntegerValue(2))),
//                                                new Compound(new Assignment("c", new ValueExpression(new IntegerValue(5))),
//                                                        new Compound(new Switch(
//                                                                new ArithmeticExpression('*', new VariableExpression("a"), new ValueExpression(new IntegerValue(10))),
//                                                                new ArithmeticExpression('*', new VariableExpression("b"), new VariableExpression("c")),
//                                                                new Compound(new Print(new VariableExpression("a")), new Print(new VariableExpression("b"))),
//                                                                new ValueExpression(new IntegerValue(10)),
//                                                                new Compound(new Print(new ValueExpression(new IntegerValue(100))), new Print(new ValueExpression(new IntegerValue(200)))),
//                                                                new Print(new ValueExpression(new IntegerValue(300)))
//                                                        ), new Print(new ValueExpression(new IntegerValue(300))))))))));
//        allStatements.add(ex15);
//
//        //REPEAT UNTIL STATEMENT
//        IStatement ex16 = new Compound(new VariableDeclaration("v", new IntegerType()),
//                new Compound(new Assignment("v", new ValueExpression(new IntegerValue(0))),
//                        new Compound(new RepeatUntil(
//                                new Compound(new Fork(new Compound(new Print(new VariableExpression("v")),
//                                        new Assignment("v", new ArithmeticExpression('-', new VariableExpression("v"), new ValueExpression(new IntegerValue(1)))))),
//                                        new Assignment("v", new ArithmeticExpression('+', new VariableExpression("v"), new ValueExpression(new IntegerValue(1))))),
//                                new RelationalExpression("==", new VariableExpression("v"), new ValueExpression(new IntegerValue(3)))
//                        ),
//                                new Compound(new VariableDeclaration("x", new IntegerType()),
//                                        new Compound(new VariableDeclaration("y", new IntegerType()),
//                                                new Compound(new VariableDeclaration("z", new IntegerType()),
//                                                        new Compound(new VariableDeclaration("w", new IntegerType()),
//                                                                new Compound(new Assignment("x", new ValueExpression(new IntegerValue(1))),
//                                                                        new Compound(new Assignment("y", new ValueExpression(new IntegerValue(2))),
//                                                                                new Compound(new Assignment("z", new ValueExpression(new IntegerValue(3))),
//                                                                                        new Compound(new Assignment("w", new ValueExpression(new IntegerValue(4))),
//                                                                                                new Print(new ArithmeticExpression('*', new VariableExpression("v"), new ValueExpression(new IntegerValue(10)))))))))))))));
//        allStatements.add(ex16);
//
//        //CONDITIONAL ASSIGNMENT
//        IStatement ex17 = new Compound(new VariableDeclaration("b", new BooleanType()),
//                new Compound(new VariableDeclaration("c", new IntegerType()),
//                        new Compound(new Assignment("b", new ValueExpression(new BooleanValue(true))),
//                                new Compound(new ConditionalAssignment("c",
//                                        new VariableExpression("b"),
//                                        new ValueExpression(new IntegerValue(100)),
//                                        new ValueExpression(new IntegerValue(200))),
//                                        new Compound(new Print(new VariableExpression("c")),
//                                                new Compound(new ConditionalAssignment("c",
//                                                        new ValueExpression(new BooleanValue(false)),
//                                                        new ValueExpression(new IntegerValue(100)),
//                                                        new ValueExpression(new IntegerValue(200))),
//                                                        new Print(new VariableExpression("c"))))))));
//        allStatements.add(ex17);
//
//        //MUL EXPRESSION
//        IStatement ex18 = new Compound(new VariableDeclaration("v1", new IntegerType()),
//                new Compound(new VariableDeclaration("v2", new IntegerType()),
//                        new Compound(new Assignment("v1", new ValueExpression(new IntegerValue(2))),
//                                new Compound(new Assignment("v2", new ValueExpression(new IntegerValue(3))),
//                                        new If(new RelationalExpression("!=", new VariableExpression("v1"), new ValueExpression(new IntegerValue(0))),
//                                                new Print(new MULExpression(new VariableExpression("v1"), new VariableExpression("v2"))),
//                                                new Print(new VariableExpression("v1")))))));
//        allStatements.add(ex18);
//
//        //DO WHILE STATEMENT
//        IStatement ex19 = new Compound(new VariableDeclaration("v", new IntegerType()),
//                new Compound(new Assignment("v", new ValueExpression(new IntegerValue(4))),
//                        new Compound(new DoWhileStatement(new RelationalExpression(">", new VariableExpression("v"), new ValueExpression(new IntegerValue(0))),
//                                new Compound(new Print(new VariableExpression("v")), new Assignment("v",new ArithmeticExpression('-', new VariableExpression("v"), new ValueExpression(new IntegerValue(1)))))),
//                                new Print(new VariableExpression("v")))));
//        allStatements.add(ex19);

        IStatement ex12 = new Compound(
                new VariableDeclaration("v1", new RefType(new IntegerType())),
                new Compound(
                        new VariableDeclaration("v2", new RefType(new IntegerType())),
                        new Compound(
                                new VariableDeclaration("v3", new RefType(new IntegerType())),
                                new Compound(
                                        new VariableDeclaration("cnt", new IntegerType()),
                                        new Compound(
                                                new HeapAllocation("v1", new ValueExpression(new IntegerValue(2))),
                                                new Compound(
                                                        new HeapAllocation("v2", new ValueExpression(new IntegerValue(3))),
                                                        new Compound(
                                                                new HeapAllocation("v3", new ValueExpression(new IntegerValue(4))),
                                                                new Compound(
                                                                        new NewLatch("cnt", new HeapReading(new VariableExpression("v2"))),
                                                                        new Compound(
                                                                                new Fork(
                                                                                        new Compound(
                                                                                                new HeapWriting("v1", new ArithmeticExpression('*', new HeapReading(new VariableExpression("v1")), new ValueExpression(new IntegerValue(10)))),
                                                                                                new Compound(
                                                                                                        new Print(new HeapReading(new VariableExpression("v1"))),
                                                                                                        new Compound(
                                                                                                                new CountDown("cnt"),
                                                                                                                new Fork(
                                                                                                                        new Compound(
                                                                                                                                new HeapWriting("v2", new ArithmeticExpression('*', new HeapReading(new VariableExpression("v2")), new ValueExpression(new IntegerValue(10)))),
                                                                                                                                new Compound(
                                                                                                                                        new Print(new HeapReading(new VariableExpression("v2"))),
                                                                                                                                        new Compound(
                                                                                                                                                new CountDown("cnt"),
                                                                                                                                                new Fork(
                                                                                                                                                        new Compound(
                                                                                                                                                                new HeapWriting("v3", new ArithmeticExpression('*', new HeapReading(new VariableExpression("v3")), new ValueExpression(new IntegerValue(10)))),
                                                                                                                                                                new Compound(
                                                                                                                                                                        new Print(new HeapReading(new VariableExpression("v3"))),
                                                                                                                                                                        new CountDown("cnt")
                                                                                                                                                                )
                                                                                                                                                        )
                                                                                                                                                )
                                                                                                                                        )
                                                                                                                                )
                                                                                                                        )
                                                                                                                )
                                                                                                        )
                                                                                                )
                                                                                        )
                                                                                ),
                                                                                new Compound(
                                                                                        new Await("cnt"),
                                                                                        new Compound(
                                                                                                new Print(new ValueExpression(new IntegerValue(100))),
                                                                                                new Compound(
                                                                                                        new CountDown("cnt"),
                                                                                                        new Print(new ValueExpression(new IntegerValue(100)))
                                                                                                )
                                                                                        )
                                                                                )
                                                                        )
                                                                )
                                                        )
                                                )
                                        )
                                )
                        )
                )
        );
        allStatements.add(ex12);
        return FXCollections.observableArrayList(allStatements);

    }
}
