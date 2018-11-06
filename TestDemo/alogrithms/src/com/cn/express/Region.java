package com.cn.express;

import java.util.*;

public class Region {

    private List<Point> points;
    private double[][] dMatrix;
    private List<List<Point>> region=new ArrayList<>();//记录整个区域
    private List<Point> re=new ArrayList<>();//记录每块区域的点
    private List<Integer> select=new ArrayList<>();//已经选择过的点，不能再选择

    public Region(List<Point> points,double[][] dMatrix){
        this.points=points;
        this.dMatrix=dMatrix;
    }

    public List<List<Point>> partition(){

        double sum=points.get(0).getStock();
        double origin=points.get(0).getStock();
        int count=0;
        for(int i=0;i<points.size()&&select.size()<(points.size()-1)&&count<points.size();count++){

            //记录第i个点到哪个点最近且sum不超过25kg
            Map<Integer,Double> map=new TreeMap<>();

            //排序会打乱点的坐标，用map的点和距离
            for(int j=1;j<points.size();j++){
                if(i!=j) map.put(j,dMatrix[i][j]);
            }

            //排序
            List<Map.Entry<Integer, Double>> list=valueUpSort(map);
           /* System.out.println("------------i="+i+"--------------------");
            for (Map.Entry<Integer, Double> entry : list) {
                System.out.println(entry.getKey() + ":" + entry.getValue());
            }*/
            //记录从i到j是否又满足新增的j点 sum值不超过25kg false表示没有点可以选择了 true表示j为下个点
            boolean flag=false;

            //循环每一行 选择距离最小且满足 快递重量总和不超过25kg
            for(Map.Entry<Integer, Double> entry:list){
                int j=entry.getKey();
                if(select.contains(j)) continue; //地区已经遍历过
                origin-=points.get(j).getStock();
                if(origin<0)
                    sum=sum+points.get(j).getWeight();
                else  sum=sum+points.get(j).getWeight()-points.get(j).getStock();
                //总量总和小于25kg
                if(sum<=25||origin>=0){
                    boolean b=false;
                    if(sum>25){
                        sum=sum-points.get(j).getWeight();//不能装货能卸货
                        points.get(j).setStock(0);
                        b=true;
                    }
                    if(origin<0){//不能卸货能装货
                        origin+=points.get(j).getStock();
                        points.get(j).setWeight(0);
                        b=true;
                    }
                    if(!b){
                        points.get(j).setStock(0);
                        points.get(j).setWeight(0);
                    }
                    flag=true;//有路可走
                    re.add(points.get(j));//每块区域加上j点
                    select.add(j);
                    i=j;//i->J j为下次遍历的起点
                    if(select.size()==points.size()-1) region.add(re);//如果j为最后一个未被选中的点，分区操作结束
                    break;
                }else if(origin<0){//不能卸货也不能装货
                    sum-=points.get(j).getWeight();
                    origin+=points.get(j).getStock();
                }
                //System.out.println("++++++++++++++sum="+sum+"+++++++++++++++++");
            }
            //遍历所有的点，已经没有可以走的点，则重新开始新的路径
            if(!flag){
                sum=points.get(0).getStock();
                origin=points.get(0).getStock();
                //加上已经遍历好的上个区域 ，开始下个区域的选择
                region.add(re);
                re= new  ArrayList<>();
                i=0;
            }

        }
        return region;
    }

    /**
     * dMatrix[i]行排序
     * @param map
     * @return
     */
    private  List<Map.Entry<Integer, Double>> valueUpSort(Map<Integer,Double> map) {
        Map<Integer,Double> sort=new TreeMap<>();
        // 降序序比较器
        Comparator<Map.Entry<Integer, Double>> valueComparator = new Comparator<Map.Entry<Integer ,Double>>() {
            @Override
            public int compare(Map.Entry<Integer ,Double> o1,Map.Entry<Integer ,Double> o2) {
                if(o1.getValue()>o2.getValue()) return 1;
                if(o1.getValue()<o2.getValue()) return -1;
                return 0;
            }
        };
        // map转换成list进行排序
        List<Map.Entry<Integer, Double>> list = new ArrayList<Map.Entry<Integer, Double>>(map.entrySet());
        // 排序
        Collections.sort(list,valueComparator);
        return list;

    }

}

