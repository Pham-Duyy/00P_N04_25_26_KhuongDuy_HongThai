package com.example.servingwebcontent;

public class threadexample extends Thread {
    public static int count = 0;

    public static void main(String[] args) throws InterruptedException {
        ThreadExample threadExp = new ThreadExample();
        threadExp.start();
        threadExp.join();  // đợi thread chạy xong

        System.out.println("Count: " + count);
    }

    @Override
    public void run() {
        count++;
    }
}
