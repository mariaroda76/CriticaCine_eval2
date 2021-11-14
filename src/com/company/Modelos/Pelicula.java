package com.company.Modelos;

import com.company.Rubro;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

public class Pelicula implements Serializable {

    private int idPelicula;
    private String nombre;
    private int anyo;
    private Rubro rubro;
    private int duracion;
    private String descripcion;
    private int valoracionMedia;
   // private int idDirector;


    public Pelicula(int idPelicula, String nombre, int anyo, Rubro rubro, int duracion, String descripcion, int valoracionMedia) {
        this.idPelicula = idPelicula;
        this.nombre = nombre;
        this.anyo = anyo;
        this.rubro = rubro;
        this.duracion = duracion;
        this.descripcion = descripcion;
        this.valoracionMedia = valoracionMedia;
    }

    public Pelicula() {
    }

    public Pelicula(int idPelicula, int anyo, int duracion,  int valoracionMedia) {
        this.idPelicula = idPelicula;
        this.anyo = anyo;
        this.duracion = duracion;
        this.valoracionMedia = valoracionMedia;
    }

    public Pelicula(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public Pelicula(Rubro rubro) {

    }


    public int getIdPelicula() {
        return idPelicula;
    }

    public void setIdPelicula(int idPelicula) {
        this.idPelicula = idPelicula;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getAnyo() {
        return anyo;
    }

    public void setAnyo(int anyo) {
        this.anyo = anyo;
    }

    public Rubro getRubro() {
        return rubro;
    }

    public void setRubro(Rubro rubro) {
        this.rubro = rubro;
    }

    public int getDuracion() {
        return duracion;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getValoracionMedia() {
        return valoracionMedia;
    }

    public void setValoracionMedia(int valoracionMedia) {
        this.valoracionMedia = valoracionMedia;
    }

    public void imprimir()
    {
        System.out.println( "ID: "+ idPelicula +". "+ nombre + " Año de estreno: " +  anyo +
                "\tRubro: " + rubro.name() + "Duracion: " + duracion +
                "\t Descripcion: "+ splitStringBySize(descripcion,50));

    }


    public void imprimirMedias (){

        System.out.println("ID: "+ idPelicula +". "+ nombre + " Valoracion Media: " +  valoracionMedia);


    }


    private static Collection<String> splitStringBySize(String str, int size) {
        ArrayList<String> split = new ArrayList<>();
        for (int i = 0; i <= str.length() / size; i++) {
            split.add(str.substring(i * size, Math.min((i + 1) * size, str.length())));
        }
        return split;
    }

}
