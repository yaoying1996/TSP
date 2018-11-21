package com.cn.express;


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class TxTsp {

    private double[][] distance;
    private List<Point> points;
    private int cityNum;
    private int[] colable;//代表列，也表示是否走过，走过置0
    private int[] row;//代表行，选过置0

    public TxTsp(double[][] distance,List<Point> points){
        this.distance= distance;
        this.points=points;
        cityNum=points.size();
    }
    public double TX(){

        return 1.0;
    }




    public static void main(String[] args) throws IOException {
      /*  System.out.println("Start....");
        TxTsp ts = new TxTsp(48);
        ts.init("c://data.txt");
        //ts.printinit();
        ts.solve();*/
    }
}
