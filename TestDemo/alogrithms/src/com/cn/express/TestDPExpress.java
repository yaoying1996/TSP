package com.cn.express;

import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TestDPExpress {

    public static void main(String[] args){
        //读取所有的点
        List<Point> points=readTxt("G:\\ideaspace\\TSP\\TestDemo\\alogrithms\\points.txt");
        List<Point> points2=readTxt("G:\\ideaspace\\TSP\\TestDemo\\alogrithms\\points.txt");
        //所有点的距离矩阵，用来分区
        DistanceMatrix d=new DistanceMatrix(points);
        double[][] dMatrix=d.dMatrix();
        Region r=new Region(points,dMatrix);
        List<List<Point>> region=r.partition();

        int num=1;
        double distance=0;
        double time=0;
        DecimalFormat df=new DecimalFormat("#.##");

        while(true){
            System.out.println("++++++++++经过第"+num+"次分区后++++++++++");
            for(Point point:points){
                System.out.print(point.getAddress()+"\t"+point.getWeight()+"\t"+point.getStock());
                System.out.println();
            }

            System.out.println("+++++++++++++++每次的遍历路径++++++++++++++");
            for(List<Point> list :region){
                List<Point> list_bk=new ArrayList<>();
                if(list==null||list.size()==0)System.out.println("----------------------------------");
                int origin=0;
                for(Point p :list){
                    int status=p.getStatus();
                    for(Point p_bk : points2){
                        if(p_bk.getAddress().equals(p.getAddress())){
                            if(status==1||status==3) origin+=p_bk.getStock();
                            p_bk.setStatus(status);
                            list_bk.add(p_bk);
                        }//if
                    }//for
                }//for
                System.out.println();

                //对每条路径求出最优走法  动态规划
                System.out.println("快递员需要携带的快递初始重量为"+origin+"kg");
                Point start=new Point(0,0,"快递起始地址",0,origin );
                if(list_bk.size()==0) continue;
                list_bk.add(0,start);
                //求每条路径的距离矩阵
                DistanceMatrix dlist=new DistanceMatrix(list_bk);
                // TSPDP tsp=new TSPDP(dlist.dMatrix(),list);
                double dis = new TSPDP(dlist.dMatrix(),list_bk).DP();
                double t=(dis/20)/1000+2.0/3.0;
                time+=Double.parseDouble(df.format(t));
                distance+=dis;
                System.out.println("最小路径为:"+Double.parseDouble(df.format(dis))+"m");
                System.out.println("在区域送货需要的时间:"+Double.parseDouble(df.format(t))+"h");

            }

            //每一次分区完之后point2和point分区前的值相同
            for(int i=0;i<points.size();i++){
                points2.get(i).setStock(points.get(i).getStock());
                points2.get(i).setWeight(points.get(i).getWeight());
            }

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

        }//while


       /* //对每条路径求出最优走法  动态规划
        Point start=new Point(0,0,"快递起始地址",0,25 );
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
            System.out.println("最小路径为:"+dis+"m");
            System.out.println("在区域送货需要的时间:"+Double.parseDouble(df.format(t))+"h");
        }*/

        System.out.println();
        System.out.println("所有路径的最小值总和为："+Double.parseDouble(df.format(distance))+"m");
        System.out.println("前提：每个送货点停留的时间为15min，速度20km/h，则需要的总时间为："+Double.parseDouble(df.format(time))+"h");
        System.out.println("前提：每个快递员每天的工作时间为6h，则需要"+((int)time/6+1)+"个快递员");
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