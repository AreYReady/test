package trade.xkj.com.trade.utils;

import java.util.LinkedList;

/**
 * Created by huangsc on 2017-01-23.
 * TODO:具有lifo的特性，后进先出
 */

public class Stack<T> {
    private LinkedList<T> storage = new LinkedList<T>();

    public void push(T v){
        storage.addFirst(v);
    }
    public T peek(){
        return storage.getFirst();
    }
    public T pop(){
        return storage.removeFirst();
    }
    public boolean empty(){
        return storage.isEmpty();
    }
    public String toString(){
        return storage.toString();
    }
}
