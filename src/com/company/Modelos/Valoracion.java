package com.company.Modelos;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.io.Serializable;

@XStreamAlias("Valoracion")
public class Valoracion implements Serializable {

    private int idPelicula;    // código pelicula
    private int idCritico;    // código critico
    private int valoracion;    // valoracion del critico a la pelicula

    public Valoracion(int idPelicula, int idCritico, int valoracion) {
        this.idPelicula = idPelicula;
        this.idCritico = idCritico;
        this.valoracion = valoracion;
    }

    public Valoracion() {
    }

    public int getIdPelicula() {
        return idPelicula;
    }

    public void setIdPelicula(int idPelicula) {
        this.idPelicula = idPelicula;
    }

    public int getIdCritico() {
        return idCritico;
    }

    public void setIdCritico(int idCritico) {
        this.idCritico = idCritico;
    }

    public int getValoracion() {
        return valoracion;
    }

    public void setValoracion(int valoracion) {
        this.valoracion = valoracion;
    }



    public void imrpimir() {
        System.out.println("Pelicula : " + this.idPelicula);
        System.out.println("Critico: " + this.idCritico);
        System.out.println("Valoración : " + this.valoracion);

    }


}
