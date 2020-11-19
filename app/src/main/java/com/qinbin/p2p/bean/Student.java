package com.qinbin.p2p.bean;

/**
 * Created by teacher on 2016/4/23.
 */
public class Student {
    public   String name;
    public  float score;

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", score=" + score +
                '}';
    }
}
