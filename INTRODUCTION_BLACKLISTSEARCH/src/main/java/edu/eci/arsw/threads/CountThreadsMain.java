/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.threads;
import edu.eci.arsw.threads.*;
/**
 *
 * @author hcadavid
 */
public class CountThreadsMain {
    
    public static void main(String a[]){
        CountThread g = new CountThread(1,50);
        CountThread h = new CountThread(99,199);
        CountThread j = new CountThread(500,600);
        g.start();
        h.start();
        j.start();
    }
    
}
