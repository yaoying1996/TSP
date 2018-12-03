package com.cn.express;

public class Point {

    private String address;//地址
    private double x;//x坐标
    private double y;//y坐标
    private double weight;//每个地方待寄的快递（给快递员）
    private double stock;//每个地方待取的快递(快递员送过去)
    private int status;//0:所有操作都不可以 1：可卸货可装货 2：可装货不可卸货 3：可卸货不能装货

    public Point(double x,double y,String address,double weight,double stock){
        this.x=x;
        this.y=y;
        this.address=address;
        this.weight=weight;
        this.stock=stock;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public double getStock() {
        return stock;
    }

    public void setStock(double stock) {
        this.stock = stock;
    }
    @Override
    public  String toString(){
        return "address:"+address+",("+x+","+y+")"+",weight="+weight+",stock="+stock;
    }
}