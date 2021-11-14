package com.company;

import com.company.Modelos.Pelicula;
import java.util.ArrayList;
import java.util.List;


public class ListaPeliculas
{

    //para xml...
    private List<Pelicula> lista = new ArrayList<Pelicula>();

    public ListaPeliculas(){
    }

    public void add(Pelicula p) {
        lista.add(p);
    }

    public List<Pelicula> getListaPeliculas() {
        return lista;
    }
}
