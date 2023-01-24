package model.state;

import exceptions.MyException;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public interface MyIDictionary<T1,T2> {
    void put(T1 key, T2 value);
    void remove(T1 key) throws MyException;
    void update(T1 key, T2 value) throws MyException;
    Collection<T2> values();
    Map<T1, T2> getContent();
    Set<T1> keySet();
    boolean isDefined(T1 key);
    T2 lookUp(T1 key) throws MyException;
    MyIDictionary<T1, T2> deepCopy() throws MyException;
}
