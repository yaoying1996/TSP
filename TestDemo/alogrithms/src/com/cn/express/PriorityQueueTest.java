package com.cn.express;

import java.util.PriorityQueue;

public class PriorityQueueTest {
    public static void main(String[] args) {

        final PriorityQueue<Student> queue=new PriorityQueue<>();

        Student p1=new Student(95,"张三");
        Student p2=new Student(89,"李四");
        Student p3=new Student(89,"李四");
        Student p4=new Student(67,"王五");
        Student p5=new Student(92,"赵六");
        queue.add(p1);
        queue.add(p2);
        queue.add(p3);//add 和offer效果一样。
        queue.offer(p4);//add 方法实现，其实就是调用了offer
        queue.offer(p5);

        for (Student Student : queue) {
            System.out.println(Student.toString());
        }

        System.out.println("---------------------");
        while(!queue.isEmpty()){
            System.out.println(queue.poll());

        }
    }

}
class Student implements Comparable{
    private int score;
    private String name;

    public Student(int age,String name){
        this.score=age;
        this.name=name;
    }
    public int getScore() {
        return score;
    }
    public void setScore(int score) {
        this.score = score;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String toString(){
        return "姓名："+name+"-"+score+"分";
    }

    @Override
    public int compareTo(Object o) {
        Student current=(Student)o;
        if(current.getScore()<this.score){
            return 1;
        }else if(current.getScore()==this.score){
            return 0;
        }
        return -1;
    }
}