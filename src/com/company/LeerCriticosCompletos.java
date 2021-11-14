package com.company;

import com.company.Modelos.Critico;


import java.io.EOFException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;


public class LeerCriticosCompletos {

    public static void main(String[] args) throws Exception {

        System.out.printf("%-5s%-45s%-15s%-10s\n","Id","Nombre", "Q valoraciones","Media de Valoraciones");

        ObjectInputStream criticosIS = new ObjectInputStream(new FileInputStream("CriticosConValoraciones.dat"));
        //saco a consola contenido de mi dat

        Critico micriticoTemp= (Critico) criticosIS.readObject();

        while (micriticoTemp != null) {

            System.out.printf("%-5s%-45s%-15s%-10s\n",micriticoTemp.getIdCritico(),micriticoTemp.getNombre(), micriticoTemp.getCantidadValoraciones(),micriticoTemp.getMediaValoraciones());

            try {
                micriticoTemp = (Critico) criticosIS.readObject();
            } catch (EOFException e) {
                micriticoTemp = null;
            } catch (ClassNotFoundException | IOException e) {
            }
        }

    }

}
