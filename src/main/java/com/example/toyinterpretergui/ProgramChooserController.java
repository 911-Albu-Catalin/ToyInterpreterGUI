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
import model.type.IntegerType;
import model.value.IValue;
import model.value.IntegerValue;
import repository.IRepository;
import repository.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import javafx.util.Pair;
import java.util.List;

public class ProgramChooserController {
    private ProgramExecutorController programExecutorController;
    private MyIProcedureTable procTable;

    public void setProgramExecutorController(ProgramExecutorController programExecutorController) {
        this.programExecutorController = programExecutorController;
    }
    @FXML
    private ListView<IStatement> programsListView;

    @FXML
    private Button displayButton;



    @FXML
    public void initialize() {
        this.procTable = new MyProcedureTable();
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
                MyIStack<MyIDictionary<String, IValue>> symTableStack = new MyStack<>();
                symTableStack.push(new MyDictionary<>());
                ProgramState programState = new ProgramState(new MyStack<>(), symTableStack, new MyList<>(), new MyDictionary<>(), new MyHeap(), procTable, selectedStatement);
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

        IStatement sumProc = new Compound(
                new VariableDeclaration("v", new IntegerType()),
                new Compound(
                        new Assignment("v", new ArithmeticExpression('+', new VariableExpression("a"), new VariableExpression("b"))),
                        new Print(new VariableExpression("v"))
                )
        );

        List<String> varSum = new ArrayList<>();
        varSum.add("a");
        varSum.add("b");
        procTable.put("sum", new Pair<>(varSum, sumProc));

        IStatement prodProc = new Compound(
                new VariableDeclaration("v" , new IntegerType()),
                new Compound(
                        new Assignment("v", new ArithmeticExpression('*', new VariableExpression("a"), new VariableExpression("b"))),
                        new Print(new VariableExpression("v"))
                )
        );

        List<String> varProd = new ArrayList<>();
        varProd.add("a");
        varProd.add("b");
        procTable.put("product", new Pair<>(varProd, prodProc));

        IStatement ex2 = new Compound(
                new VariableDeclaration("v", new IntegerType()),
                new Compound(
                        new VariableDeclaration("w", new IntegerType()),
                        new Compound(
                                new Assignment("v", new ValueExpression(new IntegerValue(2))),
                                new Compound(
                                        new Assignment("w", new ValueExpression(new IntegerValue(5))),
                                        new Compound(
                                                new Call("sum", new ArrayList<>(Arrays.asList(new ArithmeticExpression('*', new VariableExpression("v"), new ValueExpression(new IntegerValue(10))), new VariableExpression("w")))),
                                                new Compound(
                                                        new Print(new VariableExpression("v")),
                                                        new Fork(
                                                                new Compound(
                                                                        new Call("product", new ArrayList<>(Arrays.asList(new VariableExpression("v"), new VariableExpression("w")))),
                                                                        new Fork(
                                                                                new Call("sum", new ArrayList<>(Arrays.asList(new VariableExpression("v"), new VariableExpression("w"))))
                                                                        )
                                                                )
                                                        )
                                                )
                                        )
                                )
                        )
                )
        );
        allStatements.add(ex2);
        return FXCollections.observableArrayList(allStatements);
    }
}
