package com.arp.practicaxml12.pojo;

import java.io.Serializable;
import java.util.ArrayList;


/**
 * Created by Alex on 07/10/2015.
 */
public class Contacto implements Serializable,Comparable<Contacto>{

    private long id;
    private String nombre;
    private ArrayList<String> tlfs;
    private int edicion;

    public Contacto() {
    }

    public Contacto(long id, String nombre, ArrayList<String> tlfs) {
        this.id = id;
        this.nombre = nombre;
        this.tlfs = tlfs;
    }

    public int getEdicion() {
        return edicion;
    }

    public void setEdicion(int edicion) {
        this.edicion = edicion;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public ArrayList<String> getTlfs() {
        return tlfs;
    }

    public void setTlfs(ArrayList<String> tlfs) {
        this.tlfs = tlfs;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Contacto contacto = (Contacto) o;

        return id == contacto.id;

    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Override
    public int compareTo(Contacto another) {
        int r=nombre.compareTo(another.nombre);
        if(r==0){
            r=(int)(id-another.id);
        }
        return r;
    }

    @Override
    public String toString() {
        return "Contacto{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", tlfs=" + tlfs +
                '}';
    }

}
