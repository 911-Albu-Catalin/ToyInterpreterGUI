package model.state;

import exceptions.MyException;
import model.value.IValue;

import java.util.HashMap;
import java.util.Set;

public interface MyIHeap{
    int getFreeValue();
    HashMap<Integer, IValue> getContent();
    void setContent(HashMap<Integer, IValue> newMap);
    int add(IValue value);
    void update(Integer position, IValue value) throws MyException;
    IValue get(Integer position) throws MyException;
    boolean containsKey(Integer position);
    void remove(Integer key) throws MyException;
    Set<Integer> keySet();

}
