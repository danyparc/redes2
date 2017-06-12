
import java.io.Serializable;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author jose
 */ 
public class Fragmento implements Serializable{
    public int secuencia;
    public byte[] datos;
    public int tam;
    public Fragmento(int secuencia, byte[] datos, int tam){
        this.secuencia = secuencia;
        this.datos = datos;
        this.tam = tam;
    }
}
