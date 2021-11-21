package com.company;

import com.company.Modelos.Valoracion;

import java.util.ArrayList;
import java.util.List;


public class ListaValoraciones
{

    //para xml...
    private List<Valoracion> lista = new ArrayList<Valoracion>();

    public ListaValoraciones(){
    }

    public void add(Valoracion v) {
        lista.add(v);
    }

    public List<Valoracion> getListaPeliculas() {
        return lista;
    }
}
