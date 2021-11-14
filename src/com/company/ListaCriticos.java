package com.company;

import com.company.Modelos.Critico;


import java.util.ArrayList;
import java.util.List;


public class ListaCriticos
{

    //para xml...
    private List<Critico> lista = new ArrayList<Critico>();

    public ListaCriticos(){
    }

    public void add(Critico c) {
        lista.add(c);
    }

    public List<Critico> getListaCriticos() {
        return lista;
    }
}
