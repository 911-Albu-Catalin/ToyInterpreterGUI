package model.state;

import exceptions.MyException;

import java.util.HashMap;

public interface MyILatchTable {
    void put(int key, int value) throws MyException;
    int get(int key) throws MyException;
    boolean containsKey(int key);
    int getFreeAddress();
    void update(int key, int value) throws MyException;
    void setFreeAddress(int freeAddress);
    HashMap<Integer, Integer> getLatchTable();
    void setLatchTable(HashMap<Integer, Integer> newLatchTable);
}
