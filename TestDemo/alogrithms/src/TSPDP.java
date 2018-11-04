
import java.util.ArrayList;
//import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class TSPDP {
    private int[][] graph;
    HashMap<Integer, ArrayList<Integer>> idtoset = new HashMap<Integer, ArrayList<Integer>>();
    //get subset by id
    HashMap< ArrayList<Integer>, Integer> settoid = new HashMap< ArrayList<Integer>, Integer>();
    //get id by subset
    public TSPDP(int[][] graph){
        this.graph = graph;
    }
    /**
     * Solve Traveling Salesperson Probl  em by Dynamic Programming
     * @return the min length
     * */
    public int DP(){
        long starttime=System.currentTimeMillis();
        int n = graph.length;
        int[] vertex = new int[n-1];
        int vertexid = 1;
        for(int i = 0; i < vertex.length; i++)  {
            vertex[i] = vertexid;
            vertexid++;
        }
        getsubsets(vertex);
        int[][] D = new int[n][settoid.size()];//To record the distance
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

                    int min = 10000;
                    int value = 0;
                    /*System.out.println("++++++++++++++k="+k+"+++++++++++++++++++");
                    System.out.println("++++++++++++++id="+id+"+++++++++++++++++++");
                    System.out.println("++++++++++++++i="+i+"+++++++++++++++++++");*/
                    //System.out.println("subset[]="+subset.toString());
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
                            min = value;
                            //System.out.println("P["+i+"]["+id+"]="+j);
                            P[i][id] = j;
                        }
                    }
                    if(min < 9999);
                    D[i][id] = min;
                    //System.out.println("D["+i+"]["+id+"]="+min);
                }
            }
        }
       /* System.out.println("输出P为：");
        for(int i=0;i<P.length;i++){
            for(int j=0;j<P[i].length;j++)
                System.out.print(P[i][j]+"\t");
            System.out.println();
        }*/
        ArrayList<Integer> Vminusv0 = new ArrayList<Integer>();
        for(int i = 0; i < vertex.length; i++){
            Vminusv0.add(vertex[i]);
        }
        //System.out.println("+++++++++++++++++++打印Vminusv0++++++++++++++");
        //System.out.println(Vminusv0.toString());
        int vminusv0id = settoid.get(Vminusv0);
        int min = Integer.MAX_VALUE;
        for(int j : Vminusv0){
            ArrayList<Integer> Vminusv0vj = remove(Vminusv0,j);
            int idj = settoid.get(Vminusv0vj);

            int value = (this.graph[0][j]!=0 && D[j][idj]!=0) ? this.graph[0][j] + D[j][idj]:0;


            if(value < min && value != 0){
                min = value;
                P[0][vminusv0id] = j;
            }
        }
        if(min < 99999);
        D[0][vminusv0id] = min;
       /* System.out.println("+++++++++++++++最后++++++++++++");
        System.out.println("min="+min+",中间点="+P[0][vminusv0id]);*/
        generateOpttour(P, Vminusv0);
        long endtime=System.currentTimeMillis();
        System.out.println("计算耗时:"+(endtime-starttime)+"ms");
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
        String path = "1->";
        ArrayList<Integer> Set = V;
        int start = 0;
        while(!Set.isEmpty()){
           // System.out.println("set="+Set.toString());
            int id = settoid.get(Set);
            //System.out.println("id="+id);
            String vertex = String.valueOf(P[start][id]+1);
            //System.out.println("vertex="+vertex);
            path += vertex + "->";
            Set = remove(Set, P[start][id]);
            start = P[start][id];
           // System.out.println("start="+start);
        }
        path += "1";
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
          /*  System.out.println("当i的值="+i+",idtoset的值为");
            for(Map.Entry<Integer, ArrayList<Integer>> entry:idtoset.entrySet())
                System.out.print("("+entry.getKey()+","+entry.getValue().toString()+")");
            System.out.println();*/
           /* System.out.println("当i的值="+i+",settoid的值为");
            for(Map.Entry<ArrayList<Integer>, Integer> entry:settoid.entrySet())
                System.out.print("("+entry.getKey()+","+entry.getValue().toString()+")");
            System.out.println();*/
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

    public static void main(String[] args){

        TSPDP test = new TSPDP(new int[][]{
                {0,16,16,7,13,6},
                {16,0,9,5,19,7},
                {16,9,0, 7,9,6},
                {7,5, 7,0,7,7},
                {13,19,9,7,0,13},
                {6,7,6,7,13,0}});
        int dis = test.DP();
        System.out.println("最小路径为:"+dis);

    }
}
