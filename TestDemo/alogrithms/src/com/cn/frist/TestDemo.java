package com.cn.frist;

public class TestDemo {
    public static void main(String args[]){
        int a=1;

        int[] b={1,2,3};
        change(a,b);
        System.out.println(a);
        System.out.println(b[2]);
    }
    public static void change(int a,int[] b){
        a=2;
        b[2]=100;
    }
}