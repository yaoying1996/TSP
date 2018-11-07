package com.cn.express;

import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TestSeparateExpress {

    public static void main(String[] args){
        //读取所有的点
        List<Point> points=readTxt("G:\\ideaspace\\TSP\\TestDemo\\alogrithms\\test.txt",0);
        //快递总重量
       /* double weight=0;
        for(Point po:points) weight+=po.getWeight();
        System.out.println("+++++++++++++++++至少分成"+((int)weight/25+1)+"个区域+++++++++++++++++");*/
        //所有点的距离矩阵，用来分区
        DistanceMatrix d=new DistanceMatrix(points);
        double[][] dMatrix=d.dMatrix();

        //快递员取快递
        Region1 r=new Region1(points,dMatrix);
        List<List<Point>> region=r.partition();
        System.out.println("取货路线：");
        for(List<Point> list :region){
            for(Point point: list){
                System.out.print(point.getAddress()+"\t");
            }
            System.out.println();
        }
        //对每条路径求出最优走法
        Point start=new Point(0,0,"快递起始地址",0,0 );
        double distance=0;
        double time=0;
        DecimalFormat df=new DecimalFormat("#.##");
        for(List<Point> list :region){
            System.out.println();
            if(list.size()==0) continue;
            list.add(0,start);
            //求每条路径的距离矩阵
            DistanceMatrix dlist=new DistanceMatrix(list);
            // TSPDP tsp=new TSPDP(dlist.dMatrix(),list);
            double dis = new TSPDP(dlist.dMatrix(),list).DP();
            double t=(dis/20)/1000+2.0/3.0;
            time+=Double.parseDouble(df.format(t));
            distance+=dis;
            System.out.println("取货最小路径为:"+dis+"m");
            System.out.println("在区域取货需要的时间:"+Double.parseDouble(df.format(t))+"h");
        }
        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        //快递员送快递
        points=readTxt("G:\\ideaspace\\TSP\\TestDemo\\alogrithms\\test.txt",25);
        Region2 r2=new Region2(points,dMatrix);
        List<List<Point>> region2=r2.partition();
        System.out.println("送货货路线：");
        for(List<Point> list :region2){
            for(Point point: list){
                System.out.print(point.getAddress()+"\t");
            }
            System.out.println();
        }
        start.setStock(25);
        for(List<Point> list :region2){
            System.out.println();
            if(list.size()==0) continue;
            list.add(0,start);
            //求每条路径的距离矩阵
            DistanceMatrix dlist=new DistanceMatrix(list);
            // TSPDP tsp=new TSPDP(dlist.dMatrix(),list);
            double dis = new TSPDP(dlist.dMatrix(),list).DP();
            double t=(dis/20)/1000+2.0/3.0;
            time+=Double.parseDouble(df.format(t));
            distance+=dis;
            System.out.println("送货最小路径为:"+dis+"m");
            System.out.println("在区域送货需要的时间:"+Double.parseDouble(df.format(t))+"h");
        }
        System.out.println();
        System.out.println("所有送、取货路径的最小值总和为："+Double.parseDouble(df.format(distance))+"m");
        System.out.println("前提：每个送货点停留的时间为15min，速度20km/h，则需要的总时间为："+Double.parseDouble(df.format(time))+"h");
        System.out.println("前提：每个快递员每天的工作时间为6h，则需要"+((int)time/6+1)+"个快递员");
    }

    /**
     * 读取每个地址的位置，待取快递重量，以及待寄快递的重量
     * @param name
     * @return
     */
    private static List<Point> readTxt(String name,double stock1) {
        List<String> arrayList = new ArrayList<>();
        try {
            File file = new File(name);
            InputStreamReader inputReader = new InputStreamReader(new FileInputStream(file), "gbk");
            BufferedReader bf = new BufferedReader(inputReader);
            String str;
            while ((str = bf.readLine()) != null) {
                arrayList.add(str);
            }
            bf.close();
            inputReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<Point> points = new ArrayList<>();
        //加入初始快递起点(0,0)快递起点站
        Point start=new Point(0,0,"快递起始位置",0,stock1);
        points.add(start);
        for (String s : arrayList) {
            String[] object = s.split("\t");
            String address = object[0];
            double x = Double.parseDouble(object[1]);
            double y = Double.parseDouble(object[2]);
            double weight = Double.parseDouble(object[3]);
            double stock = Double.parseDouble(object[4]);
            Point point = new Point(x, y, address, weight,stock);
            points.add(point);
        }
        return points;
    }

    /**
     * 检测距离矩阵是否合法
     * @param dArray
     * @return
     */
    private static  boolean check(double[][] dArray) {
      /*  if (dArray.length < 3) {
            System.out.println("错误信息：距离矩阵长度过小");
            return false;
        }*/
        for (int i = 0; i < dArray.length; i++) {  // 每个double[]的长度都进行判断
            if (dArray.length != dArray[i].length) {
                System.out.println("错误信息：距离数组长度不合法");
                return false;
            }
            if(dArray[i][i]!=0){
                System.out.println("错误信息：距离数组对角线不为0");
                return false;
            }

        }
        return true;
    }
}