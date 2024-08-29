/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arst.concprg.prodcons;

import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hcadavid
 */
public class Consumer extends Thread{
    
    private Queue<Integer> queue;
    
    
    public Consumer(Queue<Integer> queue){
        this.queue=queue;        
    }
    
   @Override
    public void run() {
    while (true) {
        synchronized (queue) {
            while (queue.isEmpty()) {
                try {
                    queue.wait(); // Espera hasta que un productor notifique
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // Vuelve a poner la bandera de interrupción si es interrumpido
                    Logger.getLogger(Consumer.class.getName()).log(Level.SEVERE, null, e);
                }
            }
           
        }
         int elem = queue.poll();
         System.out.println("Consumer consumes " + elem);
    }
}
}
