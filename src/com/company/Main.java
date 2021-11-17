package com.company;

import com.company.Modelos.Critica;
import com.company.Modelos.Critico;
import com.company.Modelos.Pelicula;
import com.thoughtworks.xstream.XStream;

import java.io.*;
import java.util.*;

public class Main {

    public static void main(String[] args) throws Exception {

        File f = new File("PeliculasValoradas.dat");
        File cr = new File("CriticosConValoraciones.dat");

        //me aseguro que la informacion dat este generada
        CrearFichPelicula.main(null);
        CrearFichCriticos.main(null);
        CrearFichCriticas.main(null);

        Scanner input = new Scanner(System.in);
        boolean mainLoop = true;

        int choice;
        while (true) {
            System.out.println("\nEjercicio Ficheros Menu\n");
            System.out.print("1.) Generar Valoracion a peliculas (dat) \n");
            System.out.print("2.) Leer peliculas Valoradas\n");
            System.out.print("3.) Pasar peliculas Valoradas a XML\n");
            System.out.print("4.) Leer fichero de Criticas completas\n");
            System.out.print("5.) Preguntar la valoracion de una pelicula concreta\n");
            System.out.print("6.) Generar medias y cantidades de valoracion a Criticos (dat) \n");
            System.out.print("7.) Leer Criticos Completos\n");
            System.out.print("8.) Pasar Criticos completos a XML\n");
            System.out.print("0.) Exit\n");
            System.out.print("\nSelecciona una opcion valida: ");

            choice = input.nextInt();

            switch (choice) {

                case 1:
                    Map<Integer, List<Integer>> mapaValoraciones = diccionarioValoracionesPorPelicula();
                    generarpeliculaValorada(mapaValoraciones);
                    break;
                case 2:
                    if (f.exists() && !f.isDirectory()) {
                        LeerPeliculasValoradas.main(null);
                    }
                    System.out.println("No has creado aun el fichero de peliculas Valoradas (dat)");
                    break;
                case 3:
                    if (f.exists() && !f.isDirectory()) {
                        pasarPelisValoradasAXML();
                    }
                    System.out.println("No has creado aun el fichero de peliculas Valoradas (dat)");
                    break;
                case 4:
                    CrearFichCriticas.leerCriticasTodas(new File("Criticas.dat"));
                    break;
                case 5:
                    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                    System.out.println("Ingresa Id de Pelicula: ");
                    String s = br.readLine();

                    try {
                        int id = Integer.parseInt(s);

                        if (diccionarioValoracionesPorPelicula().containsKey(id)) {
                            double mediaExacta = calcularMedia(diccionarioValoracionesPorPelicula().get(id));
                            int valoracionMedia = (int) Math.round(mediaExacta);
                            System.out.println("Valoracion media es de: " + valoracionMedia);
                        }else {
                            System.out.println("El Id proporcionado no existe");
                        }

                    } catch (NumberFormatException e) {
                        System.out.println("Debes seleccionar un Id Valido");;
                    }
                    break;
                case 6:
                    Map<Integer, List<Integer>> mapaCriticosVal = diccionarioValoracionesPorCritico();
                    generarvaloracionesPorCritico(mapaCriticosVal);
                    break;
                case 7:
                    if (cr.exists() && !cr.isDirectory()) {
                        LeerCriticosCompletos.main(null);
                    } System.out.println("No has creado aun el fichero de Criticos completos(dat)");
                    break;

                case 8:
                    if (cr.exists() && !cr.isDirectory()) {
                        pasarCriticosCompletosAXML();
                    } System.out.println("No has creado aun el fichero de Criticos completos(dat)");
                    break;
                case 0:
                    System.out.println("Salir del programa...");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Opcion no valida! Selecciona otra.");
                    break;
            }


        }


    }

    private static Map<Integer, List<Integer>> diccionarioValoracionesPorPelicula() throws IOException, ClassNotFoundException {
        //Abrir flujo de entrada de datos para leer criticas .dat
        FileInputStream fiCriticas = new FileInputStream("Criticas.dat");
        ObjectInputStream oiCriticas = new ObjectInputStream(fiCriticas);

        Map<Integer, List<Integer>> mapaValoraciones = new HashMap<>();

        //meto las criticas en diccionario con key id de peli
        Critica critica = (Critica) oiCriticas.readObject();
        Integer myPeliKey;
        while (critica != null) {
            myPeliKey = critica.getIdPelicula();
            if (!mapaValoraciones.containsKey(myPeliKey)) {
                mapaValoraciones.put(myPeliKey, new ArrayList<>());
            }
            mapaValoraciones.get(myPeliKey).add(critica.getValoracion());

            try {
                critica = (Critica) oiCriticas.readObject();
            } catch (EOFException e) {
                critica = null;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        }
        oiCriticas.close();
        return mapaValoraciones;

    }

    private static Map<Integer, List<Integer>> diccionarioValoracionesPorCritico() throws IOException, ClassNotFoundException {
        //Abrir flujo de entrada de datos para leer criticas .dat
        FileInputStream fiCriticas = new FileInputStream("Criticas.dat");
        ObjectInputStream oiCriticas = new ObjectInputStream(fiCriticas);

        Map<Integer, List<Integer>> mapaValoraciones = new HashMap<>();

        //meto las criticas en diccionario con key id de peli
        Critica critica = (Critica) oiCriticas.readObject();
        Integer myCriticoKey;
        while (critica != null) {
            myCriticoKey= critica.getIdCritico();
            if (!mapaValoraciones.containsKey(myCriticoKey)) {
                mapaValoraciones.put(myCriticoKey, new ArrayList<>());
            }
            mapaValoraciones.get(myCriticoKey).add(critica.getValoracion());

            try {
                critica = (Critica) oiCriticas.readObject();
            } catch (EOFException e) {
                critica = null;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        }
        oiCriticas.close();
        return mapaValoraciones;

    }

    private static void generarpeliculaValorada(Map mapaValoraciones) throws IOException, ClassNotFoundException {

        //Abrir flujo de entrada de datos para leer peliculas sin valorar .dat
        FileInputStream fiPeliculas = new FileInputStream("Peliculas.dat");
        ObjectInputStream oiPeliculas = new ObjectInputStream(fiPeliculas);


        //Abrir flujo de salida de datos para Escribir peliculavalorada .dat
        FileOutputStream foPeliculaValorada = new FileOutputStream("PeliculasValoradas.dat");
        ObjectOutputStream ooPeliculaValorada = new ObjectOutputStream(foPeliculaValorada);


        //recorro el fichero peliculas no valoradas, y consulto al map si tiene una entrada para esa peli
        Pelicula pelicula = (Pelicula) oiPeliculas.readObject();

        while (pelicula != null) {

            int idPeli = pelicula.getIdPelicula();
            int valoracionMedia = 0;

            //si la peli tiene critica...
            if (mapaValoraciones.containsKey(idPeli)) {
                List<Integer> listaValoracionesPeliId = (List<Integer>) mapaValoraciones.get(idPeli);
                double mediaExacta = calcularMedia(listaValoracionesPeliId);
                valoracionMedia = (int) Math.round(mediaExacta);
            }

            //si no hay critica la media será cero
            pelicula.setValoracionMedia(valoracionMedia);

            ooPeliculaValorada.writeObject(pelicula);
            try {
                pelicula = (Pelicula) oiPeliculas.readObject();
            } catch (EOFException e) {
                pelicula = null;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        //escribir fichero
        //Escribimos "null" al final del fichero
        ooPeliculaValorada.writeObject(null);

        //Cerramos todos
        ooPeliculaValorada.close();
        foPeliculaValorada.close();
        oiPeliculas.close();

    }

    private static double calcularMedia(List<Integer> marks) {
        Integer sum = 0;
        if (!marks.isEmpty()) {
            for (Integer mark : marks) {
                sum += mark;
            }
            return sum.doubleValue() / marks.size();
        }
        return sum;
    }

    private static void pasarPelisValoradasAXML() throws IOException, ClassNotFoundException {

        //Abrir flujo de entrada de datos para leer peliculasValoradas .dat
        File ficheroPelis = new File("PeliculasValoradas.dat");
        FileInputStream fiPelis = new FileInputStream(ficheroPelis);
        ObjectInputStream oiPelis = new ObjectInputStream(fiPelis);


        System.out.println("Comienza el proceso de creación del fichero a XML ...");

        //Creamos una  Lista de peliculas con los objetos del dat

        ListaPeliculas listapelisValiradas = new ListaPeliculas();
        Pelicula pelicula = (Pelicula) oiPelis.readObject();

        while (pelicula != null) {
            listapelisValiradas.add(pelicula);
            try {
                pelicula = (Pelicula) oiPelis.readObject();
            } catch (EOFException e) {
                pelicula = null;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        }

        oiPelis.close(); //cerrar stream de entrada

        try {
            XStream xstream = new XStream();

            //cambiar de nombre a las etiquetas XML
            xstream.alias("ListaPeliculas", ListaPeliculas.class);
            xstream.alias("DatosPeliculas", Pelicula.class);

            //quitar etiqueta lista (atributo de la clase ListaEmpleado)
            xstream.addImplicitCollection(ListaPeliculas.class, "lista");

            //Insrtar los objetos en el XML
            xstream.toXML(listapelisValiradas, new FileOutputStream("PeliculasValoradas.xml"));
            System.out.println("Creado fichero XML....");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void generarvaloracionesPorCritico(Map mapaValoraciones) throws IOException, ClassNotFoundException {

        //Abrir flujo de entrada de datos para leer criticos .dat
        FileInputStream fiCriticOs = new FileInputStream("Criticos.dat");
        ObjectInputStream oiCriticOs = new ObjectInputStream(fiCriticOs);


        //Abrir flujo de salida de datos para Escribir critico con datos de valoracion .dat
        FileOutputStream foCriticoConValoracion = new FileOutputStream("CriticosConValoraciones.dat");
        ObjectOutputStream ooCriticoConValoracion = new ObjectOutputStream(foCriticoConValoracion);


        //recorro el fichero criticos, y consulto al map si tiene una entrada para esa critico

        Critico critico = (Critico) oiCriticOs.readObject();

        while (critico != null) {

            int idCritico = critico.getIdCritico();
            int valoracionMedia = 0;
            int cantidadCriticas =0;

            //si el critico ha hecho una critica...
            if (mapaValoraciones.containsKey(idCritico)) {
                List<Integer> listaValoracionesCriticoId = (List<Integer>) mapaValoraciones.get(idCritico);
                double mediaExacta = calcularMedia(listaValoracionesCriticoId);
                cantidadCriticas = listaValoracionesCriticoId.size();
                valoracionMedia = (int) Math.round(mediaExacta);
            }

            //si el critico no ha realizado aun una critica, la media será cero y la cantidad de valoraciones tambien
            critico.setMediaValoraciones(valoracionMedia);
            critico.setCantidadValoraciones(cantidadCriticas);


            ooCriticoConValoracion.writeObject(critico);
            try {
                critico = (Critico) oiCriticOs.readObject();
            } catch (EOFException e) {
                critico = null;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }


    }

    private static void pasarCriticosCompletosAXML() throws IOException, ClassNotFoundException {

        //Abrir flujo de entrada de datos para leer los criticos completos .dat
        File ficheroCriticos = new File("CriticosConValoraciones.dat");
        FileInputStream fiCriticos = new FileInputStream(ficheroCriticos);
        ObjectInputStream oiCriticos = new ObjectInputStream(fiCriticos);


        System.out.println("Comienza el proceso de creación del fichero a XML ...");

        //Creamos una  Lista de peliculas con los objetos del dat

        ListaCriticos listaCriticosCompletos = new ListaCriticos();
        Critico critico = (Critico) oiCriticos.readObject();

        while (critico != null) {
            listaCriticosCompletos.add(critico);
            try {
                critico = (Critico) oiCriticos.readObject();
            } catch (EOFException e) {
                critico = null;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        }

        oiCriticos.close(); //cerrar stream de entrada

        try {
            XStream xstream = new XStream();

            //cambiar de nombre a las etiquetas XML
            xstream.alias("ListaCriticos", ListaCriticos.class);
            xstream.alias("DatosCritico", Critico.class);


            xstream.addImplicitCollection(ListaCriticos.class, "lista");

            //Insrtar los objetos en el XML

            xstream.toXML( listaCriticosCompletos , new FileOutputStream("CriticosCompletos.xml"));
            System.out.println("Creado fichero XML....");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}







