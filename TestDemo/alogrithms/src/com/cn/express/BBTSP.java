package com.cn.express;

import java.text.DecimalFormat;
import java.util.*;

class Node implements Comparable{
    int[] visp;//标记哪些点走了
    int st;//起点
    int ed;//终点
    int k;//走过的点数
    double sumv;//经过路径的距离
    double lb;//目标函数的值
    double sum;//经过每个点之后快递车的重量
    Map<Integer,Integer> map_edge=new HashMap<>();//记录已经加入的边

    @Override
    public int compareTo(Object o){
        Node node=(Node) o;
        if(node.lb<this.lb)
            return 1;
        else if(node.lb>this.lb)
            return -1;
        return 0;
    }
}
public class BBTSP {
    private double[][] mp;
    int n;
    double low;//路径和最小值
    double sum_origin;//每次开始的时候的总重量
    double sum_up=25.0;//总量上界
    private List<Point> points;
    private PriorityQueue<Node> q=new PriorityQueue<>();
    private PriorityQueue<Node> q_last=new PriorityQueue<>();//记录每条路径的最后一个节点，以及对应的路径值

    public BBTSP(double[][] mp,List<Point> points){
        this.mp=mp;
        this.points=points;
        n=points.size();
        sum_origin=points.get(0).getStock();
       // System.out.println("sum_origin="+sum_origin);
    }

    void get_low()
    {
        low=0;
        for(int i=0; i<n; i++)
        {
        /*通过排序求两个最小值*/
            //double min1=Double.MAX_VALUE,min2=Double.MAX_VALUE;
            double[] tmpA=new double[n];
            for(int j=0; j<n; j++)
            {
                //if(i==j) continue;
                tmpA[j]=mp[i][j];
            }
           Arrays.sort(tmpA);//对临时的数组进行排序
            low+=tmpA[1]+tmpA[2];

        }
        low=low/2;
    }

    public double get_lb(Node p){
        double ret=p.sumv*2;//路径上的点的距离
        double min1=Double.MAX_VALUE,min2=Double.MAX_VALUE;//起点和终点连出来的边
       /* System.out.println("边：");
        for(Map.Entry<Integer,Integer> entry: p.map_edge.entrySet())
            System.out.println("start="+entry.getKey()+",end="+entry.getValue());*/
        Map<Integer,Integer> map=p.map_edge;

        for(int i=0; i<n; i++) {
           // System.out.println("++++++++++++i="+i+"+++++++++++++++++++");
            boolean flag1 = false;//该点为出点
            boolean flag2 = false;//该点为入点

            int end = -1;
            int start = -1;

            if (map.containsKey(i)) {
                flag1 = true;
                end = map.get(i);
            }
            if (map.containsValue(i)) {
                flag2 = true;
                for(Map.Entry<Integer,Integer> entry:map.entrySet())
                    if(entry.getValue()==i) start=entry.getKey();
            }
            if (flag1 && flag2) continue;

            List<Double> array=new ArrayList<>();
            if (!flag1&&flag2) {//该点只有入点，没有出点
                for (int j = 0; j < n; j++) {
                    if (i == j || j == start) continue;
                    array.add(mp[i][j]);
                    //System.out.println("flag1       map["+i+"]"+"["+j+"]="+mp[i][j]);
                }
                Collections.sort(array);

                ret += array.get(0);
               // System.out.println("array.get(0)="+array.get(0)+",ret="+ret);
            }

            if (!flag2&&flag1) {
                array=new ArrayList<>();
                for (int j = 0; j < n; j++) {
                    if (i == j || j == end) continue;
                    array.add(mp[j][i]);
                    //System.out.println("flag2       map["+j+"]"+"["+i+"]="+mp[j][i]);
                }
                Collections.sort(array);
                ret += array.get(0);
                //System.out.println("array.get(0)="+array.get(0)+",ret="+ret);
            }

            if(!flag1&&!flag2){
                array=new ArrayList<>();
                for (int j = 0; j < n; j++) {
                    if (i == j) continue;
                    array.add(mp[i][j]);
                }
                Collections.sort(array);
                ret += array.get(0)+array.get(1);
               // System.out.println("array.get(0)="+array.get(0)+"array.get(1)="+array.get(1)+",ret="+ret);
            }


        }
        //System.out.println("2.ret="+ret);
        return ret/2;
    }

    public Node solve(){
        get_low();
        //System.out.println("low="+low);
        /*设置初始点,默认从1开始 */
        Node star=new Node();
        star.st=0;
        star.ed=0;
        star.k=1;
        star.visp=new int[n];
        for(int i=0; i<n; i++) star.visp[i]=0;
        star.visp[0]=1;
        star.sumv=0;
        star.lb=low;
        star.sum=sum_origin;
       // System.out.println("n="+n);
        /*ret为问题的解*/
        double ret=Double.MAX_VALUE;

        q.add(star);
        while(!q.isEmpty())
        {
            Node tmp=q.peek();
            //System.out.println("tmp.sum="+tmp.sum);
            if(!q_last.isEmpty()){
                Node last=q_last.peek();
                if(last.lb<=tmp.lb) return last;
            }
            //System.out.println("当前的点"+points.get(tmp.ed));
          /*  System.out.println("输出队列里面的数据");
            Iterator<Node> it=q.iterator();
            while (it.hasNext()){
                Node no=it.next();
                int num=no.ed;
               System.out.println(points.get(num).getAddress()+"; node.st="+no.st+";node.ed="+no.ed+";node.sumv="+no.sumv);
            }*/
            //System.out.println("--------------------------------------------------------");
          //  System.out.println("tmp.st="+tmp.st+";tmp.ed="+tmp.ed+";tmp.sumv="+tmp.sumv);
            Map<Integer,Integer> tmp_map=tmp.map_edge;
            q.poll();
            if(tmp.k==n-1)
            {
               //System.out.println("最后一个点");
            /*找最后一个没有走的点*/
                int p=0;
                for(int i=0; i<n; i++)
                {
                    if(tmp.visp[i]==0)
                    {
                        p=i;
                        break;
                    }
                }
               double sum_last=tmp.sum;
                int status=points.get(p).getStatus();
                if(status==1){
                    sum_last+=points.get(p).getWeight()-points.get(p).getStock();
                    //System.out.println("status=1 ;sum="+sum_last);

                }else if(status==2){
                    sum_last+=points.get(p).getWeight();
                    //System.out.println("status=2 ;sum="+sum_last);

                }else if(status==3){
                    sum_last-=points.get(p).getStock();
                    //System.out.println("status=3 ;sum="+sum_last);
                }


                Node next=new Node();
                next.visp=new int[n];
                next.st=tmp.ed;
                next.ed=p;
                double ans=tmp.sumv+mp[p][0]+mp[tmp.ed][p];//最终的最短路径
                next.sumv=ans;
                next.k=tmp.k+1;
                next.map_edge.putAll(tmp.map_edge);
                next.map_edge.put(next.st,next.ed);
                next.map_edge.put(next.ed,0);
                next.lb=ans;
                next.sum=sum_last;

                //System.out.println("next.i="+p+";next.lib="+next.lb+";next.st="+next.st+";next.ed="+next.ed+";next.sum="+next.sum);
                Node judge = q.peek();
                /*如果当前的路径和比所有的目标函数值都小则跳出*/
                if(ans <= judge.lb||judge==null)
                {
                    //ret=Math.min(ans,ret);
                   // ret_map.put(ans,next);
                    return next;
                   // break;
                }
               /*否则继续求其他可能的路径和，并更新上界*/
                else
                {
                    //up = Math.min(up,ans);
                    q_last.add(next);
                   // ret=Math.min(ret,ans);
                    continue;
                }
            }
        /*当前点可以向下扩展的点入优先级队列*/

            for(int i=0; i<n; i++)
            {
                if(tmp.visp[i]==0)
                {
                    Point p=points.get(i);
                    //System.out.println("------------------"+p.getAddress()+"---------------------");
                    double sum_p=tmp.sum;
                    //System.out.println("sum_p="+sum_p);
                    int status=p.getStatus();
                    if(status==1){
                        sum_p+=p.getWeight()-p.getStock();
                      // System.out.println("status=1 ;sum="+sum_p);

                    }else if(status==2){
                        sum_p+=p.getWeight();
                        //System.out.println("status=2; sum="+sum_p);

                    }else if(status==3){
                        sum_p-=p.getStock();
                        //System.out.println("status=3 sum="+sum_p);
                    }else continue;

                    if (sum_p>25) continue;
                    Node next=new Node();
                    next.visp=new int[n];
                    next.st=tmp.ed;
                    next.sum=sum_p;
                   /*更新路径和*/
                    //System.out.println("tmp.sumv="+tmp.sumv);
                    next.sumv=tmp.sumv+mp[tmp.ed][i];

                /*更新最后一个点*/
                    next.ed=i;

                /*更新顶点数*/
                    next.k=tmp.k+1;

                /*更新经过的顶点*/
                    for(int j=0; j<n; j++) next.visp[j]=tmp.visp[j];
                    next.visp[i]=1;

                /*求目标函数*/
                    Map<Integer,Integer> next_map=new HashMap<>();
                    next_map.putAll(tmp_map);
                    next_map.put(next.st,next.ed);
                    next.map_edge=next_map;

                    next.lb=get_lb(next);
                    //System.out.println("sumv="+next.sumv);
                   // System.out.println("next.i="+i+";next.lib="+next.lb+";next.st="+next.st+";next.ed="+next.ed+";next.sum="+next.sum);

                    q.add(next);
                }
            }
        }
       // return ret;
        return null;
    }

    /*public static void main(String[] args){
        double[][] d={
                {0,3,1,5,8},
                {3,0,6,7,9},
                {1,6,0,4,2},
                {5,7,4,0,3},
                {8,9,2,3,0}
        };
        BBTSP b=new BBTSP(d);
        Node node=b.solve();
        System.out.println();
        System.out.println("+++++++++++++++++++++++++输出结果：++++++++++++++++++++++++");
        System.out.println("最后遍历的点的信息：");
        System.out.println("node.lib="+node.lb+";node.st="+node.st+";node.ed="+node.ed+";node.sumv="+node.sumv);
        System.out.println("最短路径为："+node.lb);
        System.out.println("构成的边为：");
        for(Map.Entry<Integer,Integer> entry: node.map_edge.entrySet()){
            System.out.println(entry.getKey()+"  ->  "+entry.getValue());
        }
      *//*  Node n1=new Node();
        n1.lb=9;
        Node n2=new Node();
        n2.lb=19;
        Node n3=new Node();
        n3.lb=11;
        Node n4=new Node();
        n4.lb=5;
        Node n5=new Node();
        n5.lb=2;
         PriorityQueue<Node> q1=new PriorityQueue<>();
        q1.add(n1);
        q1.add(n2);
        q1.add(n3);
        q1.add(n4);
        q1.add(n5);
        while(!q1.isEmpty()){
            Node nn=q1.poll();
            System.out.println(nn.lb);
        }*//*

    }*/

    /*public  static void main(String[] args){
        Point p1=new Point(0,0,"快递起始地址",0,23);p1.setStatus(0);
        Point p2=new Point(284,-606,"华工13区",3,4);p2.setStatus(1);
        Point p3=new Point(200,-1100,"华工15区",3,4);p3.setStatus(1);
        Point p4=new Point(1200,-730,"华工17区",7,3);p4.setStatus(3);
        Point p5=new Point(810,-190,"华工12区",5,12);p5.setStatus(1);
        Point p6=new Point(2000,-600,"华工18区",5,3);p6.setStatus(2);
        Point p7=new Point(2100,-500,"华工19区",3,8);p7.setStatus(2);
        Point p8=new Point(2800,0,"华工21区",4,4);p8.setStatus(2);
        List<Point> points=new ArrayList<>();
        points.add(p1);
        points.add(p2);
        points.add(p3);
        points.add(p4);
        points.add(p5);
        points.add(p6);
        points.add(p7);
        points.add(p8);

        DistanceMatrix d=new DistanceMatrix(points);
        double[][] dMatrix=d.dMatrix();
        BBTSP b=new BBTSP(dMatrix,points);
        Node node=b.solve();
        if(node==null) System.out.println("不存在这样的路径");
        DecimalFormat df=new DecimalFormat("#.##");
        System.out.println("最小路径为:"+Double.parseDouble(df.format(node.lb))+"m");
        System.out.println("构成的边为：");
        StringBuilder sb=new StringBuilder(points.get(0).getAddress());
        for(int i=0,j=0;i<points.size()&&j<points.size();j++){
            int value=node.map_edge.get(i);
            sb.append(" -> "+points.get(value).getAddress());
            i=value;
        }
        System.out.println(sb.toString());
    }*/
}
