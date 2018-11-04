package com.cn.frist;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.io.*;
import java.util.Arrays;

public class BinarySearch {
    public static int rank(int key,int[] a)
    {
        int lo=0;
        int high=a.length-1;
       while(lo<=high){
           int mid=lo+(high-lo)/2;
           if(a[mid]>key) high=mid-1;
           else if (a[mid]<key) lo=mid+1;
           else return mid;
       }
        return -1;
    }

    public static void main(String args[]) throws Exception {
        int[] whitelist= In.readInts(args[0]);
        /*File filename=new File(args[0]);
        InputStreamReader reader=new InputStreamReader(new FileInputStream(filename));
        BufferedReader br=new BufferedReader(reader);
        String line="";
        line=br.readLine();*/
        Arrays.sort(whitelist);
        while(!StdIn.isEmpty()){
            int key=StdIn.readInt();
            if(rank(key,whitelist)<0)
                StdOut.println(key);
        }
    }

}