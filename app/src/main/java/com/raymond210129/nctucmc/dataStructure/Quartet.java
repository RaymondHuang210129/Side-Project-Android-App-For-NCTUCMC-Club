package com.raymond210129.nctucmc.dataStructure;

public class Quartet<T, U, V, W> {

    private T first;
    private U second;
    private V third;
    private W forth;

    public Quartet(T first, U second, V third, W forth) {
        this.first = first;
        this.second = second;
        this.third = third;
        this.forth = forth;
    }
    public void put(T first, U second, V third, W forth) {
        this.first = first;
        this.second = second;
        this.third = third;
        this.forth = forth;
    }

    public void setFirst(T first)
    {
        this.first = first;
    }

    public void setSecond(U second)
    {
        this.second = second;
    }

    public void setThird(V third)
    {
        this.third = third;
    }

    public  void setForth(W forth)
    {
        this.forth = forth;
    }

    public T getFirst() { return first; }
    public U getSecond() { return second; }
    public V getThird() { return third; }
    public W getForth() {return forth; }
}
