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
public class Datos implements Serializable{
    public long tam;
    public String nombre;
    public Datos(String nombre, long tam){
	this.tam = tam;
	this.nombre = nombre;
    }
}