package com.cn.express;

import java.text.DecimalFormat;
import java.util.List;

public class DistanceMatrix {

    private List<Point> points;


    public DistanceMatrix(List<Point> points){
        this.points=points;
    }

    public  double[][] dMatrix(){
        long strat=System.currentTimeMillis();
        int lenght=points.size();
        //距离矩阵
        double[][] dmatrix=new double[lenght][lenght];
        //计算所有点之间的距离
        for(int i=0;i<lenght;i++)
            for(int j=0;j<lenght;j++){
                if(i==j){
                    dmatrix[i][j]=0;
                    continue;
                }
                dmatrix[i][j]=distance(points.get(i),points.get(j));
            }
        long end=System.currentTimeMillis();
       // System.out.println("计算距离矩阵的时间："+(end-strat)+"ms");
       // print(dmatrix);
        return dmatrix;
    }

    private  double  distance(Point point1,Point point2){
        DecimalFormat df=new DecimalFormat("#.##");
        double distance=(point1.getX()-point2.getX())*(point1.getX()-point2.getX())+(point1.getY()-point2.getY())*(point1.getY()-point2.getY());
        return Double.parseDouble(df.format(Math.sqrt(distance)));
    }

     private void print(double[][] dmatrix){
         //输出距离矩阵
         System.out.print("\t\t\t ");
         for (int i=0;i<dmatrix.length;i++)
             System.out.format("%-22s",points.get(i).getAddress());
         System.out.println();
         for (int i=0;i<dmatrix.length;i++){
             System.out.print(points.get(i).getAddress()+"\t\t");
             for (int j=0;j<dmatrix[i].length;j++){
                 System.out.format("%-25s",dmatrix[i][j]);
             }
             System.out.println();
         }
    }
}

