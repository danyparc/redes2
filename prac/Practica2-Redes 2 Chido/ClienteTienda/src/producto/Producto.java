/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package producto;

import java.io.Serializable;

/**
 *
 * @author jose
 */
public class Producto implements Serializable {
    private String nombre;
    private int id;
    private int existencia;
    private String descripcion;
    private String[] imagenes;
    private long[] sizes;
    private float precio;

    public Producto(String nombre, int id, int existencia, float precio, String descripcion, int noImages){
        this.nombre = nombre;
        this.id = id;
        this.existencia = existencia;
        this.descripcion = descripcion;
        this.imagenes = new String[noImages];
        this.sizes = new long[noImages];
        this.precio = precio;
    }
    
    
    public float getPrecio() {
        return precio;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }
    
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getExistencia() {
        return existencia;
    }

    public void setExistencia(int existencia) {
        this.existencia = existencia;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setImagen(int index, String imagen, long size){
        imagenes[index] = imagen;
        sizes[index] = size;
    }
    public String getImagen(int index){
        return imagenes[index];
    }
    public long getSize(int index){
        return sizes[index];
    }
    public String[] getImagenes(){
        return imagenes;
    }
}


