package com.company;

import com.company.Modelos.Valoracion;
import com.company.Modelos.Critico;
import com.company.Modelos.Pelicula;
import com.company.Utils.CriticoConverter;
import com.company.Utils.PelisConverter;
import com.company.Utils.ValoracionConverter;
import com.thoughtworks.xstream.XStream;
import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.*;
import org.xmldb.api.base.Collection;
import org.xmldb.api.modules.XPathQueryService;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Scanner;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class Main {

    //fichoros dat iniciales
    File f = new File("dats_iniciales\\Peliculas.dat");
    File cr = new File("dats_iniciales\\Criticos.dat");
    File val = new File("dats_iniciales\\Valoraciones.dat");


    //conexion
    static String driver = "org.exist.xmldb.DatabaseImpl"; //Driver para eXist
    static String URI = "xmldb:exist://localhost:8085/exist/xmlrpc/db/Colecciones"; //URI colección
    static String usu = "admin"; //Usuario
    static String usuPwd = "admin"; //Clave
    static Collection col = null;


    public static void main(String[] args) throws Exception {

        //CrearFichCriticas.main(null);

        Scanner input = new Scanner(System.in);
        boolean mainLoop = true;

        int choice;
        while (true) {
            System.out.println("\nEjercicio BDDNoSQL Menu\n");
            System.out.print("1.) Regenerar ficheros XML para BDD: \n" +
                    "\t\tPeliculas.xml, Criticos.xml y Valoraciones.xml\n" +
                    "\t\tEsta accion dejará la BDD en su punto inicial (sin valoraciones ni items nuevos)\n");
            System.out.print("2.) Insertar Pelicula\n");
            System.out.print("3.) Modificar Pelicula\n");
            System.out.print("4.) Eliminar Pelicula\n");
            System.out.print("5.) Listar Peliculas\n");
            System.out.print("6.) Valorar pelicula\n");
            System.out.print("7.) --\n");
            System.out.print("8.) --\n");
            System.out.print("0.) Exit\n");
            System.out.print("\nSelecciona una opcion valida: ");

            choice = input.nextInt();

            switch (choice) {

                case 1:
                    CrearFichPelicula.main(null);
                    CrearFichCriticos.main(null);
                    CrearFichValoraciones.main(null);

                    crearColeccionPeliculasXML();
                    crearColeccionCriticosXML();
                    crearColeccionValoracionXML();
                    break;
                case 2:
                    Pelicula pelicula = new Pelicula();
                    pelicula.setIdPelicula(450);
                    pelicula.setNombre("Test");
                    pelicula.setDuracion(20);

                    insertarPelicula(pelicula);
                    break;
                case 3:
                    listarPeliculas();
                    break;
                case 4:

                    break;
                case 5:


                    break;
                case 6:

                    break;
                case 7:

                    break;

                case 8:

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
        Valoracion valoracion = (Valoracion) oiCriticas.readObject();
        Integer myPeliKey;
        while (valoracion != null) {
            myPeliKey = valoracion.getIdPelicula();
            if (!mapaValoraciones.containsKey(myPeliKey)) {
                mapaValoraciones.put(myPeliKey, new ArrayList<>());
            }
            mapaValoraciones.get(myPeliKey).add(valoracion.getValoracion());

            try {
                valoracion = (Valoracion) oiCriticas.readObject();
            } catch (EOFException e) {
                valoracion = null;
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
        Valoracion valoracion = (Valoracion) oiCriticas.readObject();
        Integer myCriticoKey;
        while (valoracion != null) {
            myCriticoKey = valoracion.getIdCritico();
            if (!mapaValoraciones.containsKey(myCriticoKey)) {
                mapaValoraciones.put(myCriticoKey, new ArrayList<>());
            }
            mapaValoraciones.get(myCriticoKey).add(valoracion.getValoracion());

            try {
                valoracion = (Valoracion) oiCriticas.readObject();
            } catch (EOFException e) {
                valoracion = null;
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
            int cantidadCriticas = 0;

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

            xstream.registerConverter(new PelisConverter());
            xstream.alias("ListadePeliculas", ListaPeliculas.class);
            xstream.alias("Pelicula", Pelicula.class);


            //quitar etiqueta lista (atributo de la clase ListaEmpleado)
            xstream.addImplicitCollection(ListaPeliculas.class, "lista");

            //Insrtar los objetos en el XML
            xstream.toXML(listapelisValiradas, new FileOutputStream("Colecciones\\Peliculas.xml"));
            System.out.println("Creado fichero XML....");

        } catch (Exception e) {
            e.printStackTrace();
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

            xstream.registerConverter(new CriticoConverter());
            xstream.alias("ListadeCriticos", ListaCriticos.class);
            xstream.alias("Critico", Critico.class);

            xstream.addImplicitCollection(ListaCriticos.class, "lista");

            //Insrtar los objetos en el XML

            xstream.toXML(listaCriticosCompletos, new FileOutputStream("Colecciones\\Criticos.xml"));
            System.out.println("Creado fichero XML....");

        } catch (Exception e) {
            e.printStackTrace();
        }
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


    private static void crearColeccionPeliculasXML() throws IOException, ClassNotFoundException {

        //Abrir flujo de entrada de datos para leer peliculasValoradas .dat
        File ficheroPelis = new File("dats_iniciales\\Peliculas.dat");
        FileInputStream fiPelis = new FileInputStream(ficheroPelis);
        ObjectInputStream oiPelis = new ObjectInputStream(fiPelis);


        System.out.println("Comienza el proceso de creación del fichero a XML ...");

        //Creamos una  Lista de peliculas con los objetos del dat

        ListaPeliculas listaPelis = new ListaPeliculas();
        Pelicula pelicula = (Pelicula) oiPelis.readObject();

        while (pelicula != null) {
            listaPelis.add(pelicula);
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

            xstream.registerConverter(new PelisConverter());
            xstream.alias("Peliculas", ListaPeliculas.class);
            xstream.alias("Pelicula", Pelicula.class);


            //quitar etiqueta lista (atributo de la clase ListaEmpleado)
            xstream.addImplicitCollection(ListaPeliculas.class, "lista");

            //Insrtar los objetos en el XML
            xstream.toXML(listaPelis, new FileOutputStream("Colecciones\\Peliculas.xml"));
            System.out.println("Creado fichero XML....");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void crearColeccionCriticosXML() throws IOException, ClassNotFoundException {

        //Abrir flujo de entrada de datos para leer los criticos completos .dat
        File ficheroCriticos = new File("dats_iniciales\\Criticos.dat");
        FileInputStream fiCriticos = new FileInputStream(ficheroCriticos);
        ObjectInputStream oiCriticos = new ObjectInputStream(fiCriticos);

        System.out.println("Comienza el proceso de creación del fichero a XML ...");

        //Creamos una  Lista de criticos con los objetos del dat

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

            xstream.registerConverter(new CriticoConverter());
            xstream.alias("Criticos", ListaCriticos.class);
            xstream.alias("Critico", Critico.class);

            xstream.addImplicitCollection(ListaCriticos.class, "lista");

            //Insrtar los objetos en el XML

            xstream.toXML(listaCriticosCompletos, new FileOutputStream("Colecciones\\Criticos.xml"));
            System.out.println("Creado fichero XML....");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void crearColeccionValoracionXML() throws IOException, ClassNotFoundException {

        //Abrir flujo de entrada de datos para leer los criticos completos .dat
        File ficheroValoraciones = new File("dats_iniciales\\Valoraciones.dat");
        FileInputStream fiValoraciones = new FileInputStream(ficheroValoraciones);
        ObjectInputStream oiValoraciones = new ObjectInputStream(fiValoraciones);

        System.out.println("Comienza el proceso de creación del fichero a XML ...");

        //Creamos una  Lista de valoraciones con los objetos del dat

        ListaValoraciones listaValoraciones = new ListaValoraciones();
        Valoracion valoracion = (Valoracion) oiValoraciones.readObject();

        while (valoracion != null) {
            listaValoraciones.add(valoracion);
            try {
                valoracion = (Valoracion) oiValoraciones.readObject();
            } catch (EOFException e) {
                valoracion = null;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        }

        oiValoraciones.close(); //cerrar stream de entrada

        try {
            XStream xstream = new XStream();

            xstream.registerConverter(new ValoracionConverter());
            xstream.alias("Valoraciones", ListaValoraciones.class);
            xstream.alias("valoracion", Valoracion.class);

            xstream.addImplicitCollection(ListaValoraciones.class, "lista");

            //Insrtar los objetos en el XML

            xstream.toXML(listaValoraciones, new FileOutputStream("Colecciones\\Valoraciones.xml"));
            System.out.println("Creado fichero XML....");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static Collection conectar() {

        try {
            Class cl = Class.forName(driver); //Cargar del driver
            Database database = (Database) cl.getDeclaredConstructor().newInstance(); //Instancia de la BD
            DatabaseManager.registerDatabase(database); //Registro del driver
            col = (Collection) DatabaseManager.getCollection(URI, usu, usuPwd);
            return col;
        } catch (XMLDBException e) {
            System.out.println("Error al inicializar la BD eXist.");
            //e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("Error en el driver.");
            //e.printStackTrace();
        } catch (InstantiationException e) {
            System.out.println("Error al instanciar la BD.");
            //e.printStackTrace();
        } catch (IllegalAccessException e) {
            System.out.println("Error al instanciar la BD.");
            //e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void insertarPelicula(Pelicula pelicula) {

        String nuevaPeli = "<Pelicula idPelicula = " + "\"" + pelicula.getIdPelicula() +"\"" +
                " rubro = " + "\"" + pelicula.getRubro() + "\"" + ">"+pelicula.getNombre()+" <anyo> " + pelicula.getAnyo() + " </anyo > " +
                "<duracion > " + pelicula.getDuracion() + "</duracion > " +
                "<descripcion > " + pelicula.getDescripcion() + "</descripcion >" +
                "<valoracionMedia > " + pelicula.getValoracionMedia() + "</valoracionMedia > </Pelicula >";


        String nuevaPeli2 ="<Pelicula idPelicula=\"500\" rubro=\"Sci_Fi\">Alien:El octavo pasajero<anyo>1979</anyo><duracion>117</duracion><descripcion>De regreso a la Tierra, la nave de carga Nostromo interrumpe su viaje y despierta a sus siete tripulantes. El ordenador central, MADRE, ha detectado la misteriosa transmision de una forma de vida desconocida, procedente de un planeta cercano aparentemente deshabitado.</descripcion><valoracionMedia>0</valoracionMedia></Pelicula>";



        col = conectar();

        if (col != null) {
            try {
                XPathQueryService servicio = (XPathQueryService) col.getService("XPathQueryService", "1.0");
                System.out.printf("Inserto: %s \n", nuevaPeli);
                //Consulta para insertar --> update insert ... into
                ResourceSet result = servicio.query("update insert " + nuevaPeli + " into /Peliculas");
                col.close(); //borramos
                System.out.println("Pelicula Insertada.");
            } catch (Exception e) {
                System.out.println("Error al insertar Pelicula.");
                e.printStackTrace();
            }
        } else {
            System.out.println("Error en la conexión. Comprueba datos.");
        }
    }

    private static void listarPeliculas() {
        if (conectar() != null) {
            try {
                XPathQueryService servicio;
                servicio = (XPathQueryService) col.getService("XPathQueryService", "1.0");
                //Preparamos la consulta
                ResourceSet result = servicio.query("for $peli in /Peliculas/Pelicula return $peli");
                // recorrer los datos del recurso.
                ResourceIterator i;
                i = result.getIterator();
                if (!i.hasMoreResources()) {
                    System.out.println(" LA CONSULTA NO DEVUELVE NADA O ESTÁ MAL ESCRITA");
                }
                while (i.hasMoreResources()) {
                    Resource r = i.nextResource();
                    System.out.println("--------------------------------------------");
                    System.out.println((String) r.getContent());
                }
                col.close();
            } catch (XMLDBException e) {
                System.out.println(" ERROR AL CONSULTAR DOCUMENTO.");
                e.printStackTrace();
            }
        } else {
            System.out.println("Error en la conexión. Comprueba datos.");
        }

    }

}







