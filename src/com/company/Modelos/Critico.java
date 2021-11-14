package com.company.Modelos;

import java.io.Serializable;

public class Critico implements Serializable {

    private int idCritico;
    private String nombre;

    private int cantidadValoraciones;
    private int mediaValoraciones;

    public Critico(int idCritico, String nombre, int cantidadValoraciones, int mediaValoraciones) {
        this.idCritico = idCritico;
        this.nombre = nombre;
        this.cantidadValoraciones = cantidadValoraciones;
        this.mediaValoraciones = mediaValoraciones;

    }

    public Critico() {
    }

    public int getIdCritico() {
        return idCritico;
    }

    public void setIdCritico(int idCritico) {
        this.idCritico = idCritico;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void imprimir()
    {
        System.out.println( idCritico +"  "+ nombre);

    }

    public int getCantidadValoraciones() {
        return cantidadValoraciones;
    }

    public void setCantidadValoraciones(int cantidadValoraciones) {
        this.cantidadValoraciones = cantidadValoraciones;
    }

    public int getMediaValoraciones() {
        return mediaValoraciones;
    }

    public void setMediaValoraciones(int mediaValoraciones) {
        this.mediaValoraciones = mediaValoraciones;
    }
}
