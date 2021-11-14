package com.company;

import com.company.Modelos.Critica;
import com.company.Modelos.Pelicula;

import java.io.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class CrearFichCriticas {

    public static void main(String[] args) throws Exception {
        File fileCriticas = new File("Criticas.dat");
        FileOutputStream criticasOS = new FileOutputStream(fileCriticas);
        ObjectOutputStream criticasOP = new ObjectOutputStream(criticasOS);

        FileInputStream fiPeliculas = new FileInputStream("Peliculas.dat");
        ObjectInputStream oiPeliculas = new ObjectInputStream(fiPeliculas);

        //para asignar valoracion aleatoria
        Random rv = new Random();
        int valMin = 1;
        int valMax = 10;
        int valoracion;

        int cantidadValoraciones = 3; // como máximo seria numero de criticos que tengo!! >> 20

        Pelicula pelicula = (Pelicula) oiPeliculas.readObject();

        while (pelicula != null) {
            Integer[] criticosId;

            for (int j = 0; j < cantidadValoraciones; j++) {

                criticosId= getCriticosId();
                valoracion = rv.nextInt(valMax - valMin) + valMin;

                Critica critica = new Critica(pelicula.getIdPelicula(), criticosId[j], valoracion);
                criticasOP.writeObject(critica);
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
        criticasOP.writeObject(null);

        //cerrar filestream y ObjectStream (solo con el object bastaria pero mejor)
        criticasOP.close();
        criticasOS.close();

    }

    public static void leerCriticasTodas(File fileCriticas) throws IOException, ClassNotFoundException {
        ObjectInputStream criticasIP = new ObjectInputStream(new FileInputStream(fileCriticas));
        Critica critica = (Critica) criticasIP.readObject();
        try {
            while (critica != null) {
                System.out.println("---------------");
                critica.imrpimir();
                critica = (Critica) criticasIP.readObject();

            }
        } catch (EOFException e) {
        }
        criticasIP.close();
    }

    //genera un array de 5 criticos de los 15 que hay para que valoren la pelicula
    private static Integer[] getCriticosId() {
        Integer[] arr = new Integer[20];
        for (int i = 0; i < 20; i++) {
            arr[i] = i + 1;
        }
        Collections.shuffle(Arrays.asList(arr));
        return arr;
    }

}

