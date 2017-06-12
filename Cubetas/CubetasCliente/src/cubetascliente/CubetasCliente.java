/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cubetascliente;

import java.io.BufferedReader;
import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author Usuario
 */
public class CubetasCliente {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        int n=0, interval, nOfNumbers=3000, i, j, k;
        int[] numbrs= new int[nOfNumbers];
        Random generator= new Random();
        Cubeta[] cubetas;
        
        for (i = 0; i < nOfNumbers; i++) {
            numbrs[i]= generator.nextInt(1000);
        }
        System.out.print("Cuantas cubetas? ");
        Scanner scn= new Scanner(System.in);
        n=scn.nextInt();
        System.out.println("Lista desordenada:");
        for(i=0; i<nOfNumbers; i++)
            System.out.printf("%d ", numbrs[i]);
        int res=1000%n;
        interval=1000/n;
        cubetas=new Cubeta[n];
        int pto = 8888;
        for (i = 0; i < n-1; i++) {
            cubetas[i]=new Cubeta(interval*i,interval, pto);
            pto++;
        }
        cubetas[n-1]=new Cubeta(interval*(n-1),interval+res, pto);
        for(i=0; i<nOfNumbers ; i++){
            for(j=0; j<n; j++){
                if(cubetas[j].getMin()<=numbrs[i] && cubetas[j].getMax()>=numbrs[i]){
                    cubetas[j].addItem(numbrs[i]);
                    break;
                }
            }
        }
        for(i=0; i<n; i++){
            cubetas[i].start();
        }
        for(i=0; i<n; i++){
            try{
                cubetas[i].join();
            }
            catch(Exception e){
                e.printStackTrace();
                continue;
            }
        }
        k=0;
        for(i=0; i<n; i++)
            for(j=0; j<cubetas[i].getContenido().size(); j++){
                numbrs[k] = cubetas[i].getContenido().get(j);
                k++;
            }
        System.out.println("Lista ordenada:");
        for(i=0; i<nOfNumbers; i++)
            System.out.printf("%d ", numbrs[i]);
        System.out.println("");
    }
    
}
