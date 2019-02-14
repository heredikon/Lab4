/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.threads;

/**
 *
 * @author hcadavid
 */
public class CountThread extends Thread {

    int a, b;

    CountThread(int a, int b) {
        this.a=a;
        this.b=b;
        System.out.println("new thread created");
    }

    @Override
    public void run() {
        count(a, b);
    }

    public void count(int a, int b) {
        for (int i = a; i < b; i++) {
            System.out.println(i);
        }
    }
}
