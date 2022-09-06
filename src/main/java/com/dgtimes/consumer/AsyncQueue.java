package com.dgtimes.consumer;

import java.util.*;

class AsyncQueue<E> {

    Queue<E> elementQueue = new LinkedList<>();

    public synchronized void add(E e) {
        elementQueue.add(e);
    }

    public synchronized List<E> pollList() {
        List<E> list = new ArrayList<>();
        for(int i = 0; i < 100; i++){
            try{
                list.add(elementQueue.remove());
            }catch (NoSuchElementException e){
                break;
            }
        }
        return list;
    }

    public int size() {
        return elementQueue.size();
    }
}
