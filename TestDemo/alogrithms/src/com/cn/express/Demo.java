package com.cn.express;


import java.util.*;

public class Demo {
    public static void main(String args[]) {
     /*   Map<Integer ,Double> map=new TreeMap<>();
        map.put(1,3.4);
        map.put(2,1.1);
        map.put(3,5.7);
        //这里将map.entrySet()转换成list
        List<Map.Entry<Integer ,Double> > list = new ArrayList<Map.Entry<Integer ,Double> >(map.entrySet());
        //然后通过比较器来实现排序
        Collections.sort(list,new Comparator<Map.Entry<Integer ,Double> >() {
            //升序排序
            public int compare(Map.Entry<Integer ,Double>  o1,
                               Map.Entry<Integer ,Double>  o2) {
                return o1.getValue().compareTo(o2.getValue());
            }

        });

        for(Map.Entry<Integer ,Double>  mapping:list){
            System.out.println(mapping.getKey()+":"+mapping.getValue());
        }*/
        //value();
        double a=1.00;
        if(a>0)System.out.println(1);
        else if(a>0)System.out.println(1);

    }
   /*public static void value() {
        // 默认情况，TreeMap按key升序排序
        Map<Integer ,Double> map = new TreeMap<Integer ,Double>();
        Map<Integer ,Double> sort = new HashMap<Integer ,Double>();
        map.put(1, 5.0);
        map.put(2, 3.0);
        map.put(4, 20.0);
        map.put(5, 80.0);
        map.put(6, 1.0);
        map.put(7, 10.0);
        map.put(8, 12.0);
        // 升序比较器
        Comparator<Map.Entry<Integer, Double>> valueComparat = new Comparator<Map.Entry<Integer ,Double>>() {
            @Override
            public int compare(Map.Entry<Integer ,Double> o1,Map.Entry<Integer ,Double> o2) {
                if(o1.getValue()>o2.getValue()) return -1;
                if(o1.getValue()<o2.getValue()) return 1;
                return 0;
            }
        };
        // map转换成list进行排序
        List<Map.Entry<Integer, Double>> list = new ArrayList<Map.Entry<Integer, Double>>(map.entrySet());
        // 排序
        Collections.sort(list,valueComparat);
        // 默认情况下，TreeMap对key进行升序排序
        System.out.println("------------map按照value升序排序--------------------");

       for (Map.Entry<Integer, Double> entry : list) {
           System.out.println(entry.getKey() + ":" + entry.getValue());
           sort.put(entry.getKey(),entry.getValue());
       }

       for (Map.Entry<Integer, Double> entry : sort.entrySet()) {
           System.out.println(entry.getKey() + ":" + entry.getValue());

       }
    }*/

}





