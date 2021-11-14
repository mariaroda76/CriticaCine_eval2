package com.company;

import com.company.Modelos.Pelicula;
import java.io.*;


public class LeerPeliculasValoradas {

    public static void main(String[] args) throws Exception {

        System.out.printf("%-5s%-45s%-15s%-10s%-15s%-10s%-100s\n","Id","Nombre", "Valoracion","Año", "Género", "Duración", "Descripcion");

        ObjectInputStream pelisIS = new ObjectInputStream(new FileInputStream("PeliculasValoradas.dat"));
        //saco a consola contenido de mi dat

        Pelicula mipeliTemp= (Pelicula) pelisIS.readObject();

        while (mipeliTemp != null) {

            System.out.printf("%-5s%-45s%-15s%-10s%-15s%-10s%-100s\n",mipeliTemp.getIdPelicula(),mipeliTemp.getNombre(), mipeliTemp.getValoracionMedia(),mipeliTemp.getAnyo(),mipeliTemp.getRubro(), mipeliTemp.getDuracion(),mipeliTemp.getDescripcion());

            try {
                mipeliTemp = (Pelicula) pelisIS.readObject();
            } catch (EOFException e) {
                mipeliTemp = null;
            } catch (ClassNotFoundException | IOException e) {
            }
        }

    }

}
