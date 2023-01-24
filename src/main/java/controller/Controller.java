package controller;

import exceptions.ControllerException;
import exceptions.MyException;
import model.state.ProgramState;
import model.value.RefValue;
import model.value.IValue;
import repository.IRepository;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Controller {
    IRepository repository;
    boolean displayFlag = false;
    ExecutorService executorService;

    public void setDisplayFlag(boolean value) {
        this.displayFlag = value;
    }

    public Controller(IRepository repository) {
        this.repository = repository;
    }


    public void oneStep() throws ControllerException, InterruptedException {
        executorService = Executors.newFixedThreadPool(2);
        List<ProgramState> programStates = removeCompletedPrg(repository.getProgramList());
        oneStepForAllPrograms(programStates);
        conservativeGarbageCollector(programStates);
        executorService.shutdownNow();
    }

    public void allStep() throws ControllerException, InterruptedException {
        executorService = Executors.newFixedThreadPool(2);
        List<ProgramState> programStates = removeCompletedPrg(repository.getProgramList());
        while (programStates.size() > 0) {
            oneStepForAllPrograms(programStates);
            conservativeGarbageCollector(programStates);
        }
        executorService.shutdownNow();
    }

    public void oneStepForAllPrograms(List<ProgramState> programStates) throws ControllerException, InterruptedException {
        programStates.forEach(programState -> {
            try {
                repository.logPrgState(programState);
                display(programState);
            } catch (IOException | ControllerException e) {
                System.out.println("\u001B[31m" + e.getMessage() + "\u001B[0m");
            } catch (MyException e) {
                throw new ControllerException(e.getMessage());
            }
        });
        List<Callable<ProgramState>> callList = programStates.stream()
                .map((ProgramState p) -> (Callable<ProgramState>) (p::oneStep))
                .collect(Collectors.toList());

        List<ProgramState> newProgramList;
        newProgramList = executorService.invokeAll(callList).stream()
                .map(future -> {
                    try {
                        return future.get();
                    } catch (ExecutionException | InterruptedException e) {
                        return null;
                    }
                }).filter(Objects::nonNull)
                .filter(f -> f != null )
                .collect(Collectors.toList());

        programStates.addAll(new ArrayList<>(newProgramList));

        programStates.forEach(programState -> {
            try {
                repository.logPrgState(programState);
            } catch (IOException | ControllerException | MyException e) {
                throw new ControllerException(e.getMessage());
            }
        });
        repository.setProgramStates(programStates);
    }

    private void display(ProgramState programState) {
        if (displayFlag) {
            System.out.println(programState.toString());
        }
    }

    public List<Integer> getAddrFromSymTable(Collection<IValue> symTableValues) {
        return symTableValues.stream()
                .filter(v -> v instanceof RefValue)
                .map(v -> {RefValue v1 = (RefValue) v; return v1.getAddress();})
                .collect(Collectors.toList());
    }

    public List<Integer> getAddrFromHeap(Collection<IValue> heapValues) {
        return heapValues.stream()
                .filter(v -> v instanceof RefValue)
                .map(v -> {RefValue v1 = (RefValue) v; return v1.getAddress();})
                .collect(Collectors.toList());
    }

    public Map<Integer, IValue> safeGarbageCollector(List<Integer> symTableAddresses, List<Integer> heapAddr, Map<Integer, IValue> heap) {
        return heap.entrySet().stream()
                .filter(e -> ( symTableAddresses.contains(e.getKey()) || heapAddr.contains(e.getKey())))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }


    public void conservativeGarbageCollector(List<ProgramState> programStates) {
        List<Integer> symTableAddr = Objects.requireNonNull(programStates.stream()
                        .map(p -> getAddrFromSymTable(p.getTopSymTable().values()))
                        .map(Collection::stream)
                        .reduce(Stream::concat).orElse(null))
                .collect(Collectors.toList());
        programStates.forEach(p -> p.getHeap().setContent((HashMap<Integer, IValue>) safeGarbageCollector(symTableAddr, getAddrFromHeap(p.getHeap().getContent().values()), p.getHeap().getContent())));
    }


    public List<ProgramState> removeCompletedPrg(List<ProgramState> inPrgList) {
        return inPrgList.stream().filter(p -> !p.isNotCompleted()).collect(Collectors.toList());
    }

    public void setProgramStates(List<ProgramState> programStates) {
        this.repository.setProgramStates(programStates);
    }
    public List<ProgramState> getProgramStates() {
        return this.repository.getProgramList();
    }
}
