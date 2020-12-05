package com.day.bean;

/**
 * @Author: day
 * @Date: Created in 2020/12/2 下午6:09
 * @Description: 用来记录玩家的用时
 * @Version: 1.0
 */
public class ScoreRecord implements Comparable<ScoreRecord>{

    private String name;
    private int timeRecord;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTimeRecord() {
        return timeRecord;
    }

    public void setTimeRecord(int timeRecord) {
        this.timeRecord = timeRecord;
    }

    @Override
    public int compareTo(ScoreRecord scoreRecord) {
        return this.timeRecord - scoreRecord.timeRecord;
    }
}
