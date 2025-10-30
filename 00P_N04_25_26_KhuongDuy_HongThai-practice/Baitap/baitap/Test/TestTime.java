package Test;

import Code.Time;

public class TestTime{
    public static void main ( String [] args){
        Time t1 = new Time();
        Time t2 = new Time (20, 3, 45);

        t1.setHour(7).setMinute(32).setSecond(23);

        System.out.println("t1 (12h) : " + t1);
        System.out.println("t1 (24h): " + t1.to24HourString());

        System.out.println("t2 (12h): " + t2);
        System.out.println("t2 (24h): " + t2.to24HourString());
    } 
 }