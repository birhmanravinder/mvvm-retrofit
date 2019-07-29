package com.isolsgroup.rxmvvmdemo.model;

public class FinalCounter {
    private int val;

    public FinalCounter(int intialVal) {
        val=intialVal;
    }
    public void increment(){
        val++;
    }
    public void decrement(){
        val--;
    }

    public int getVal(){return val;}
    public void assign(int value){ val=value; }
}
