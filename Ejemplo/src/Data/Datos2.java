/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Data;

import java.io.Serializable;

/**
 *
 * @author Callmetorre
 */
public class Datos2 implements Serializable{
	public int sec;
	public byte[] b;
	public int n;
	public Datos2(int sec, byte[] b, int n){
		this.sec = sec;
		this.b = b;
		this.n = n;
	}
}