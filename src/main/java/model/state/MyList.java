package model.state;

import exceptions.MyException;

import java.util.ArrayList;
import java.util.List;

public class MyList<T> implements MyIList<T>{
    List<T> list;

    public MyList() {
        this.list = new ArrayList<>();
    }

    @Override
    public void add(T elem) {
        synchronized (this) {
            this.list.add(elem);
        }
    }

    @Override
    public T pop() throws MyException {
        synchronized (this) {
            if (list.isEmpty())
                throw new MyException("The list is empty!");
            return this.list.remove(0);
        }
    }

    @Override
    public boolean isEmpty() {
        synchronized (this) {
            return this.list.isEmpty();
        }
    }

    @Override
    public List<T> getList() {
        synchronized (this) {
            return list;
        }
    }

    @Override
    public String toString() {
        return this.list.toString();
    }
}
