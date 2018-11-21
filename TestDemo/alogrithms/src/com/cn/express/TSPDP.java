package com.cn.express;

import java.util.ArrayList;
//import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TSPDP {
    private double[][] graph;
    private List<Point> points;
    HashMap<Integer, ArrayList<Integer>> idtoset = new HashMap<Integer, ArrayList<Integer>>();
    //get subset by id
    HashMap< ArrayList<Integer>, Integer> settoid = new HashMap< ArrayList<Integer>, Integer>();
    //get id by subset

    private List<Integer> select=new ArrayList<>();//已经选择过的点，不能再选择

    public TSPDP(double[][] graph,List<Point> points){
        this.graph = graph;
        this.points=points;
    }
    /**
     * Solve Traveling Salesperson Probl  em by Dynamic Programming
     * @return the min length
     * */
    public double DP(){
        long starttime=System.currentTimeMillis();

        double sum=points.get(0).getStock();
        double origin=points.get(0).getStock();

        int n = graph.length;
        int[] vertex = new int[n-1];
        int vertexid = 1;
        for(int i = 0; i < vertex.length; i++)  {
            vertex[i] = vertexid;
            vertexid++;
        }
        getsubsets(vertex);
        //System.out.println("settoid="+settoid.size());
        double[][] D = new double[n][settoid.size()];//To record the distance
        int[][] P = new int[n][settoid.size()];//To track the path

        for(int i = 1; i < n; i++){
            D[i][0] = this.graph[i][0];
        }
        for(int k = 1; k <= n - 2; k++){
            for(int id = 0; id < idtoset.size(); id++){
                ArrayList<Integer> subset = idtoset.get(id);
                if(subset.size() != k)
                    continue;
                for(int i = 1; i < n; i++){
                    if(subset.contains(i))
                        continue;

                    double min = 10000;
                    double value = 0;
                    int status=-1; //0:可卸货可装货 1:可装货不可卸货 2:可卸货不能装货 3:不能卸货也不能装货

                    for(int j : subset){
                        ArrayList<Integer> Aminusj = remove(subset, j);
                        int idj = settoid.get(Aminusj);
                        try{
                            value = this.graph[i][j] + D[j][idj];

                           // System.out.println("graph["+i+"]["+j+"]+" + "D["+j+"]["+idj+"]="+value);
                        }catch(Exception e){
                            System.out.print("Error!___");
                            System.out.println("i: " + String.valueOf(i) + " j: "+ String.valueOf(j));
                            int size = this.graph[i].length;
                            System.out.println(" graph.length: "+ String.valueOf(size));
                            System.out.println(" D.length: "+ String.valueOf(D.length));
                        }
                        if(value < min && value != 0){
                            origin-=points.get(j).getStock();
                            if(origin<0)
                                sum=sum+points.get(j).getWeight();
                            else  sum=sum+points.get(j).getWeight()-points.get(j).getStock();
                            if(sum<=25&&origin>=0){//可卸货可装货
                                min = value;
                                P[i][id] = j;
                                status=0;
                            }else
                                out1:   if(sum<=25&&origin<0){//可装货不可卸货
                                            origin+=points.get(j).getStock();
                                            if(points.get(j).getWeight()==0) break out1;//当该点的装货货重量为0时，该点不需要装货
                                            min = value;
                                            P[i][id] = j;
                                            status=1;
                            }else
                                out : if(sum>25&&origin>=0){//可卸货不能装货
                                    sum=sum-points.get(j).getWeight();//不能装货能卸货
                                    if(points.get(j).getStock()==0) break out;//当该点的卸货重量为0时，该点不需要卸货
                                    min = value;
                                    P[i][id] = j;
                                    status=2;
                                }else if(sum>25&&origin<0){//不能卸货也不能装货
                                    sum-=points.get(j).getWeight();
                                    origin+=points.get(j).getStock();
                                    status=3;
                                }

                        }
                    }
                    if(min < 9999){
                        D[i][id] = min;
                        int j= P[i][id];
                        if (status==0){
                            points.get(j).setStock(0);
                            points.get(j).setWeight(0);
                        }else if (status==1){
                            points.get(j).setWeight(0);
                        }else if(status==2){
                            points.get(j).setStock(0);
                        }
                    }

                    //System.out.println("D["+i+"]["+id+"]="+min);
                }
            }
        }

        ArrayList<Integer> Vminusv0 = new ArrayList<Integer>();
        for(int i = 0; i < vertex.length; i++){
            Vminusv0.add(vertex[i]);
        }

        int vminusv0id = settoid.get(Vminusv0);
        double min = Integer.MAX_VALUE;
        for(int j : Vminusv0){
            ArrayList<Integer> Vminusv0vj = remove(Vminusv0,j);
            int idj = settoid.get(Vminusv0vj);

            double value = (this.graph[0][j]!=0 && D[j][idj]!=0) ? this.graph[0][j] + D[j][idj]:0;


            if(value < min && value != 0){
                min = value;
                P[0][vminusv0id] = j;
            }
        }
        if(min < 99999);
        D[0][vminusv0id] = min;
        generateOpttour(P, Vminusv0);
        long endtime=System.currentTimeMillis();
        System.out.println("每块区域最优路径计算耗时:"+(endtime-starttime)+"ms");

        return D[0][vminusv0id];
    }

    private void printDP(int[][] D){
        for(int i = 0; i < D.length; i++){
            for(int j = 0; j < D[i].length; j++){
                System.out.print(D[i][j]);
                System.out.print(" ");
            }
            System.out.println();
        }
    }
    /**
     * Generate optimal tour by P, and print it
     * @param P
     * @param V containing all vertexes   except V0
     * */
    public void generateOpttour(int[][] P, ArrayList<Integer> V){
        //String path = "1->";
        String path=points.get(0).getAddress()+" -> ";
        ArrayList<Integer> Set = V;
        int start = 0;
        while(!Set.isEmpty()){

            int id = settoid.get(Set);
           // String vertex = String.valueOf(P[start][id]+1);
            String vertex = points.get(P[start][id]).getAddress();
            path += vertex + " -> ";
            Set = remove(Set, P[start][id]);
            start = P[start][id];

        }
        //path += "1"
        path+=points.get(0).getAddress();
        System.out.println("遍历地区顺序为:"+path);
    }

    /**
     * Get all subsets of a input set. And number subsets
     * All results will be recorded in member variables
     * @param set input set
     * */
    private void getsubsets(int[] set){
        idtoset.clear();
        settoid.clear();
        int max = 1 << set.length; //how many sub sets
        int id = 0;
        for(int i=0; i<max; i++){
            int index = 0;
            int k = i;
            ArrayList<Integer> s = new ArrayList<Integer>();
            while(k > 0){
                if((k&1) > 0){
                    s.add(set[index]);
                }
                k>>=1;
                index++;
            }
            idtoset.put(id, s);
            settoid.put(s, id);
            id++;

        }
    }
    /**
     * Remove an input value in a list
     * @param src source list
     * @param n the value to be removed
     * @return list after removing n
     * */
    private ArrayList<Integer> remove(ArrayList<Integer> src, int n){
        ArrayList<Integer> dest = new ArrayList<Integer>();
        int j = 0;
        for(int i = 0; i < src.size(); i++){
            int vertex = src.get(i);
            if(vertex == n)
                continue;
            dest.add(vertex);
        }
        return dest;
    }


    /*public static void main(String[] args){

        TSPDP test = new TSPDP(new double[][]{
                {0,16,16,7,13,6},
                {16,0,9,5,19,7},
                {16,9,0, 7,9,6},
                {7,5, 7,0,7,7},
                {13,19,9,7,0,13},
                {6,7,6,7,13,0}});
        double dis = test.DP();
        System.out.println("最小路径为:"+dis);

    }*/
}
