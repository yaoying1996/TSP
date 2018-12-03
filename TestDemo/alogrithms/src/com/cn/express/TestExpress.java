package com.cn.express;

import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TestExpress {
    private static  List<Point> points_bk=readTxt("G:\\ideaspace\\TSP\\TestDemo\\alogrithms\\points.txt");
    private  static  List<List<Point>> region_all=new ArrayList<>();
    public static void main(String[] args){
        DecimalFormat df=new DecimalFormat("#.##");
        //读取所有的点
        List<Point> points=readTxt("G:\\ideaspace\\TSP\\TestDemo\\alogrithms\\points.txt");

        //所有点的距离矩阵，用来分区
        DistanceMatrix d=new DistanceMatrix(points);
        double[][] dMatrix=d.dMatrix();
        Region r=new Region(points,dMatrix);
        List<List<Point>> region=r.partition();

        int num=1;
        double sum=0;
        int count=0;
        while(true){
            /*System.out.println("++++++++++经过第"+num+"次分区后++++++++++");
            for(Point point:points){
                System.out.print(point.getAddress()+"\t"+point.getWeight()+"\t"+point.getStock());
                System.out.println();
            }*/

           // System.out.println("+++++++++++++++每次的遍历路径++++++++++++++");
            region_all.addAll(region);
            for(List<Point> list :region){
                if(list.size()==0) continue;
                Point front=new Point(0,0,"快递起始位置",0,25);
                StringBuilder sb=new StringBuilder(front.getAddress()+"->");
                int origin1=0;
                double sum_list=0;
                for(Point p : list){
                    double fp=distance(front,p);
                    int status=p.getStatus();
                    sum_list+=fp;
                    front=p;
                    sb.append(p.getAddress()+"->");
                    for(Point bk:points_bk) {
                        if (bk.getAddress().equals(p.getAddress())) {
                            if(status==1||status==3) origin1 += bk.getStock();
                            //System.out.println(bk.getAddress()+"的重量为"+bk.getStock());
                            break;
                        }
                    }
                }
                sum_list+=distance(front,new Point(0,0,"快递起始位置",0,25));
                sum+=sum_list;
                sb.append("快递起始位置");
                System.out.println(sb.toString());
                if(origin1>25) origin1=25;
                System.out.println("快递员需要携带的快递初始重量为"+origin1+"kg");
                System.out.println("最短路径的距离为"+Double.parseDouble(df.format(sum_list))+"m");
                System.out.println();
                count++;

            }//for

            boolean flag=false; //判断是否还有送的货，即所有的stock为0
            double sumStock=0;
            for(Iterator<Point> it=points.iterator();it.hasNext();){
                Point dp=it.next();
                if(dp.getX()==0&&dp.getY()==0) continue;
                if(dp.getWeight()==0&&dp.getStock()==0)
                    it.remove();
                if(dp.getStock()!=0){
                    flag=true;
                    sumStock+=dp.getStock();
                }

            }

            if(points.size()==1) break;
            else{
                if(!flag) points.get(0).setStock(0);//快递员只收货
                if(0<sumStock&&sumStock<25) points.get(0).setStock(sumStock);//剩下地区的货物不足25kg
                d=new DistanceMatrix(points);
                dMatrix=d.dMatrix();
                r=new Region(points,dMatrix);
                List<List<Point>> listadd=r.partition();
                if(listadd!=null) {
                    //region.addAll(listadd);
                    region=listadd;
                }
                num++;
            }

        }


        /*System.out.println();
        System.out.println("------------------贪心算法的遍历顺序-------------------");
        double sum=0;
        int count=0;
        for(List<Point>  list :region){
            if(list.size()==0) continue;
            Point front=new Point(0,0,"快递起始位置",0,25);
            StringBuilder sb=new StringBuilder(front.getAddress()+"->");
            int origin1=0;
            double sum_list=0;
            for(Point p : list){

                double fp=distance(front,p);
                int status=p.getStatus();
                sum_list+=fp;
                front=p;
                sb.append(p.getAddress()+"->");
                for(Point bk:points_bk) {
                    if (bk.getAddress().equals(p.getAddress())) {
                        if(status==1||status==3) origin1 += bk.getStock();
                        //System.out.println(bk.getAddress()+"的重量为"+bk.getStock());
                        break;
                    }
                }
            }
            sum_list+=distance(front,new Point(0,0,"快递起始位置",0,25));
            sum+=sum_list;
            sb.append("快递起始位置");
            System.out.println(sb.toString());
            if(origin1>25) origin1=25;
            System.out.println("快递员需要携带的快递初始重量为"+origin1+"kg");
            System.out.println("最短路径的距离为"+Double.parseDouble(df.format(sum_list))+"m");
            System.out.println();
            count++;
        }*/

        double t=(sum/20)/1000+(2.0/3.0)*count;

        System.out.println();
        System.out.println("所有路径的最小值总和为："+Double.parseDouble(df.format(sum))+"m");
        System.out.println("前提：每个送货点停留的时间为15min，速度20km/h，则需要的总时间为："+Double.parseDouble(df.format(t))+"h");
        System.out.println("前提：每个快递员每天的工作时间为6h，则需要"+((int)t/6+1)+"个快递员");


        System.out.println();
        System.out.println("贪婪算法分区：");
        StringBuilder sb=new StringBuilder("快递起始位置->");
        for(List<Point> list :region_all){
            for(Point p : list){
                sb.append(p.getAddress()+"->");
            }
            sb.append("快递起始位置");
            System.out.println(sb.toString());
            sb=new StringBuilder("快递起始位置->");

        }

    }


    /**
     *求两点之间的距离
    */
    private  static double  distance(Point point1,Point point2){
        DecimalFormat df=new DecimalFormat("#.##");
        double distance=(point1.getX()-point2.getX())*(point1.getX()-point2.getX())+(point1.getY()-point2.getY())*(point1.getY()-point2.getY());
        return Double.parseDouble(df.format(Math.sqrt(distance)));
    }
    /**
     * 读取每个地址的位置，待取快递重量，以及待寄快递的重量
     * @param name
     * @return
     */
    private static List<Point> readTxt(String name) {
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
        Point start=new Point(0,0,"快递起始位置",0,25);
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