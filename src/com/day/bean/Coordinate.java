package com.day.bean;

/**
 * @Author: day
 * @Date: Created in 2020/12/1 下午7:23
 * @Description: 作为坐标类，用来记录一个位置的横纵坐标，默认为-1
 * @Version: 1.0
 */
public class Coordinate {

    private int abscissa;
    private int ordinate;

    public Coordinate() {
        this.abscissa = -1;
        this.ordinate = -1;
    }

    public int getAbscissa() {
        return abscissa;
    }

    public void setAbscissa(int abscissa) {
        this.abscissa = abscissa;
    }

    public int getOrdinate() {
        return ordinate;
    }

    public void setOrdinate(int ordinate) {
        this.ordinate = ordinate;
    }
}
