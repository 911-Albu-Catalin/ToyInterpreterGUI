package repository;

import exceptions.MyException;
import model.state.ProgramState;

import java.io.IOException;
import java.util.List;

public interface IRepository {
    List<ProgramState> getProgramList();
    void setProgramStates(List<ProgramState> programStates);
    void addProgram(ProgramState program);
    void logPrgState(ProgramState programState) throws IOException, MyException;
    void emptyLogFile() throws IOException;
}