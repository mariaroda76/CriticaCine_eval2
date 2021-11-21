package com.company;

import com.company.Modelos.Valoracion;
import com.company.Modelos.Pelicula;

import java.io.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class CrearFichValoraciones {

    public static void main(String[] args) throws Exception {

        valoracionesRandom();

    }

    public static void leerValoracionTodas(File fileValoracion) throws IOException, ClassNotFoundException {
        ObjectInputStream valoracionIP = new ObjectInputStream(new FileInputStream(fileValoracion));
        Valoracion valoracion = (Valoracion) valoracionIP.readObject();
        try {
            while (valoracion != null) {
                System.out.println("---------------");
                valoracion.imrpimir();
                valoracion = (Valoracion) valoracionIP.readObject();

            }
        } catch (EOFException e) {
        }
        valoracionIP.close();
    }

   //shuffle con Ids de criticos
    private static Integer[] getCriticosId() {
        //introducir cuantos criticos hay en BDD y pedirles el ID?
        Integer[] arr = new Integer[20];
        for (int i = 0; i < 20; i++) {
            arr[i] = i + 1;
        }
        Collections.shuffle(Arrays.asList(arr));
        return arr;
    }
    
    public static void valoracionesRandom() throws IOException, ClassNotFoundException {

        File fileValoracion = new File("dats_iniciales\\Valoraciones.dat");
        FileOutputStream valoracionOS = new FileOutputStream(fileValoracion);
        ObjectOutputStream valoracionOP = new ObjectOutputStream(valoracionOS);

        FileInputStream fiPeliculas = new FileInputStream("dats_iniciales\\Peliculas.dat");
        ObjectInputStream oiPeliculas = new ObjectInputStream(fiPeliculas);

        //para asignar valoracion aleatoria
        Random rv = new Random();
        int valMin = 1;
        int valMax = 10;
        int valoracion;

        int cantidadValoraciones = 2; // como máximo seria numero de criticos que tengo!! >> 20

        Pelicula pelicula = (Pelicula) oiPeliculas.readObject();

        while (pelicula != null) {
            Integer[] criticosId;

            for (int j = 0; j < cantidadValoraciones; j++) {

                criticosId= getCriticosId();
                valoracion = rv.nextInt(valMax - valMin) + valMin;

                Valoracion valoracion1 = new Valoracion(pelicula.getIdPelicula(), criticosId[j], valoracion);
                valoracionOP.writeObject(valoracion1);
            }

            try {
                pelicula = (Pelicula) oiPeliculas.readObject();
            } catch (EOFException e) {
                pelicula=null;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        }

        //escribo por ultimo un null, asi al hacer los while al final encuentra un null... igualmente hago un try al leerlos por si me olvido
        valoracionOP.writeObject(null);

        //cerrar filestream y ObjectStream (solo con el object bastaria pero mejor)
        valoracionOP.close();
        valoracionOS.close();
        
    }

}

