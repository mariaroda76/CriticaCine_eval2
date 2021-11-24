package com.company;

import com.company.Modelos.Valoracion;
import com.company.Modelos.Critico;
import com.company.Modelos.Pelicula;
import com.company.Utils.CriticoConverter;
import com.company.Utils.PelisConverter;
import com.company.Utils.ValoracionConverter;
import com.thoughtworks.xstream.XStream;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.*;
import org.xmldb.api.base.Collection;
import org.xmldb.api.modules.XPathQueryService;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Scanner;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class Main {

    //fichoros dat iniciales
    File f = new File ("dats_iniciales\\Peliculas.dat");
    File cr = new File ("dats_iniciales\\Criticos.dat");
    File val = new File ("dats_iniciales\\Valoraciones.dat");


    //conexion
    static String driver = "org.exist.xmldb.DatabaseImpl"; //Driver para eXist
    static String URI = "xmldb:exist://localhost:8085/exist/xmlrpc/db/Colecciones"; //URI colección
    static String usu = "admin"; //Usuario
    static String usuPwd = "admin"; //Clave
    static Collection col = null;


    public static void main(String[] args) throws Exception {

        //CrearFichCriticas.main(null);

        Scanner input = new Scanner (System.in);
        boolean mainLoop = true;

        String choice;
        while (true) {
            System.out.println ("\nEjercicio BDDNoSQL Menu\n");
            System.out.print ("1.) Regenerar ficheros XML para BDD: \n" +
                    "\t\tPeliculas.xml, Criticos.xml y Valoraciones.xml\n" +
                    "\t\tFALTA IMPLEMENTAR LA SUBIDA A LA COLECCION!!!!!!!!!!!!!!!!!!!!!\n" +
                    "\t\tEsta accion dejará la BDD en su punto inicial (sin valoraciones ni items nuevos)\n");
            System.out.print ("2.) Insertar Pelicula\n");
            System.out.print ("3.) Modificar Pelicula\n");
            System.out.print ("4.) Eliminar Pelicula\n");
            System.out.print ("5.) Listar Peliculas\n");
            System.out.print ("6.) Valorar pelicula\n");
            System.out.print ("7.) --\n");
            System.out.print ("8.) crear pelicula xml\n");
            System.out.print ("0.) Exit\n");
            System.out.print ("\nSelecciona una opcion valida: ");

            choice = input.nextLine ();

            switch (choice) {

                case "1":
                    CrearFichPelicula.main (null);
                    CrearFichCriticos.main (null);
                    CrearFichValoraciones.main (null);

                    crearColeccionPeliculasXML ();
                    crearColeccionCriticosXML ();
                    crearColeccionValoracionXML ();
                    break;
                case "2":
                    Pelicula peli = new Pelicula ();
                    peli = ingresarDatosPeli ();
                    insertarPelicula (peli);
                    break;
                case "3":
                    // listarPeliculas();
                    String idP;
                    System.out.println ("\nSelecciona una Pelicula por su id\n");

                    //controlar que le id es correcto
                    idP = input.next ();
                    //modificarGeneral(idP,"Peliculas","Pelicula","idPelicula","anyo", (Integer) 1952);
                    //modificar nombre de la pelicula
                    modificarGeneral (idP, "Peliculas", "Pelicula", "@idPelicula", "nombre", "nuevonombre");


                    break;
                case "4":
                    // listarPeliculas();
                    String idP2;
                    System.out.println ("\nSelecciona una Pelicula por su id\n");

                    //controlar que le id es correcto
                    idP2 = input.next ();
                    borrarrGeneral (idP2, "Peliculas", "Pelicula", "@idPelicula");


                    break;
                case "5":
                    listarPeliculas ();

                    break;
                case "6":

                    break;
                case "7":


                    break;

                case "8":

                    Pelicula pelicula2 = new Pelicula ();
                    pelicula2.setIdPelicula (450);
                    pelicula2.setRubro (Rubro.Comedia);
                    pelicula2.setNombre ("Test");
                    pelicula2.setAnyo (1942);
                    pelicula2.setDuracion (20);
                    pelicula2.setDescripcion ("bosefhwpe");
                    pelicula2.setValoracionMedia (0);

                    crearPeliculaTempXML (pelicula2);

                    break;
                case "0":
                    System.out.println ("Salir del programa...");
                    System.exit (0);
                    break;
                default:
                    System.out.println ("Opcion no valida! Selecciona otra.");
                    break;
            }


        }


    }

    private static Map<Integer, List<Integer>> diccionarioValoracionesPorPelicula() throws IOException, ClassNotFoundException {
        //Abrir flujo de entrada de datos para leer criticas .dat
        FileInputStream fiCriticas = new FileInputStream ("Criticas.dat");
        ObjectInputStream oiCriticas = new ObjectInputStream (fiCriticas);

        Map<Integer, List<Integer>> mapaValoraciones = new HashMap<> ();

        //meto las criticas en diccionario con key id de peli
        Valoracion valoracion = (Valoracion) oiCriticas.readObject ();
        Integer myPeliKey;
        while (valoracion != null) {
            myPeliKey = valoracion.getIdPelicula ();
            if (!mapaValoraciones.containsKey (myPeliKey)) {
                mapaValoraciones.put (myPeliKey, new ArrayList<> ());
            }
            mapaValoraciones.get (myPeliKey).add (valoracion.getValoracion ());

            try {
                valoracion = (Valoracion) oiCriticas.readObject ();
            } catch (EOFException e) {
                valoracion = null;
            } catch (ClassNotFoundException e) {
                e.printStackTrace ();
            }

        }
        oiCriticas.close ();
        return mapaValoraciones;

    }

    private static Map<Integer, List<Integer>> diccionarioValoracionesPorCritico() throws IOException, ClassNotFoundException {
        //Abrir flujo de entrada de datos para leer criticas .dat
        FileInputStream fiCriticas = new FileInputStream ("Criticas.dat");
        ObjectInputStream oiCriticas = new ObjectInputStream (fiCriticas);

        Map<Integer, List<Integer>> mapaValoraciones = new HashMap<> ();

        //meto las criticas en diccionario con key id de peli
        Valoracion valoracion = (Valoracion) oiCriticas.readObject ();
        Integer myCriticoKey;
        while (valoracion != null) {
            myCriticoKey = valoracion.getIdCritico ();
            if (!mapaValoraciones.containsKey (myCriticoKey)) {
                mapaValoraciones.put (myCriticoKey, new ArrayList<> ());
            }
            mapaValoraciones.get (myCriticoKey).add (valoracion.getValoracion ());

            try {
                valoracion = (Valoracion) oiCriticas.readObject ();
            } catch (EOFException e) {
                valoracion = null;
            } catch (ClassNotFoundException e) {
                e.printStackTrace ();
            }

        }
        oiCriticas.close ();
        return mapaValoraciones;

    }

    private static void generarpeliculaValorada(Map mapaValoraciones) throws IOException, ClassNotFoundException {

        //Abrir flujo de entrada de datos para leer peliculas sin valorar .dat
        FileInputStream fiPeliculas = new FileInputStream ("Peliculas.dat");
        ObjectInputStream oiPeliculas = new ObjectInputStream (fiPeliculas);


        //Abrir flujo de salida de datos para Escribir peliculavalorada .dat
        FileOutputStream foPeliculaValorada = new FileOutputStream ("PeliculasValoradas.dat");
        ObjectOutputStream ooPeliculaValorada = new ObjectOutputStream (foPeliculaValorada);


        //recorro el fichero peliculas no valoradas, y consulto al map si tiene una entrada para esa peli
        Pelicula pelicula = (Pelicula) oiPeliculas.readObject ();

        while (pelicula != null) {

            int idPeli = pelicula.getIdPelicula ();
            int valoracionMedia = 0;

            //si la peli tiene critica...
            if (mapaValoraciones.containsKey (idPeli)) {
                List<Integer> listaValoracionesPeliId = (List<Integer>) mapaValoraciones.get (idPeli);
                double mediaExacta = calcularMedia (listaValoracionesPeliId);
                valoracionMedia = (int) Math.round (mediaExacta);
            }

            //si no hay critica la media será cero
            pelicula.setValoracionMedia (valoracionMedia);

            ooPeliculaValorada.writeObject (pelicula);
            try {
                pelicula = (Pelicula) oiPeliculas.readObject ();
            } catch (EOFException e) {
                pelicula = null;
            } catch (ClassNotFoundException e) {
                e.printStackTrace ();
            }
        }
        //escribir fichero
        //Escribimos "null" al final del fichero
        ooPeliculaValorada.writeObject (null);

        //Cerramos todos
        ooPeliculaValorada.close ();
        foPeliculaValorada.close ();
        oiPeliculas.close ();

    }

    private static void generarvaloracionesPorCritico(Map mapaValoraciones) throws IOException, ClassNotFoundException {

        //Abrir flujo de entrada de datos para leer criticos .dat
        FileInputStream fiCriticOs = new FileInputStream ("Criticos.dat");
        ObjectInputStream oiCriticOs = new ObjectInputStream (fiCriticOs);


        //Abrir flujo de salida de datos para Escribir critico con datos de valoracion .dat
        FileOutputStream foCriticoConValoracion = new FileOutputStream ("CriticosConValoraciones.dat");
        ObjectOutputStream ooCriticoConValoracion = new ObjectOutputStream (foCriticoConValoracion);


        //recorro el fichero criticos, y consulto al map si tiene una entrada para esa critico

        Critico critico = (Critico) oiCriticOs.readObject ();

        while (critico != null) {

            int idCritico = critico.getIdCritico ();
            int valoracionMedia = 0;
            int cantidadCriticas = 0;

            //si el critico ha hecho una critica...
            if (mapaValoraciones.containsKey (idCritico)) {
                List<Integer> listaValoracionesCriticoId = (List<Integer>) mapaValoraciones.get (idCritico);
                double mediaExacta = calcularMedia (listaValoracionesCriticoId);
                cantidadCriticas = listaValoracionesCriticoId.size ();
                valoracionMedia = (int) Math.round (mediaExacta);
            }

            //si el critico no ha realizado aun una critica, la media será cero y la cantidad de valoraciones tambien
            critico.setMediaValoraciones (valoracionMedia);
            critico.setCantidadValoraciones (cantidadCriticas);


            ooCriticoConValoracion.writeObject (critico);
            try {
                critico = (Critico) oiCriticOs.readObject ();
            } catch (EOFException e) {
                critico = null;
            } catch (ClassNotFoundException e) {
                e.printStackTrace ();
            }
        }


    }

    private static void pasarPelisValoradasAXML() throws IOException, ClassNotFoundException {

        //Abrir flujo de entrada de datos para leer peliculasValoradas .dat
        File ficheroPelis = new File ("PeliculasValoradas.dat");
        FileInputStream fiPelis = new FileInputStream (ficheroPelis);
        ObjectInputStream oiPelis = new ObjectInputStream (fiPelis);


        System.out.println ("Comienza el proceso de creación del fichero a XML ...");

        //Creamos una  Lista de peliculas con los objetos del dat

        ListaPeliculas listapelisValiradas = new ListaPeliculas ();
        Pelicula pelicula = (Pelicula) oiPelis.readObject ();

        while (pelicula != null) {
            listapelisValiradas.add (pelicula);
            try {
                pelicula = (Pelicula) oiPelis.readObject ();
            } catch (EOFException e) {
                pelicula = null;
            } catch (ClassNotFoundException e) {
                e.printStackTrace ();
            }

        }

        oiPelis.close (); //cerrar stream de entrada

        try {
            XStream xstream = new XStream ();

            //cambiar de nombre a las etiquetas XML

            xstream.registerConverter (new PelisConverter ());
            xstream.alias ("ListadePeliculas", ListaPeliculas.class);
            xstream.alias ("Pelicula", Pelicula.class);


            //quitar etiqueta lista (atributo de la clase ListaEmpleado)
            xstream.addImplicitCollection (ListaPeliculas.class, "lista");

            //Insrtar los objetos en el XML
            xstream.toXML (listapelisValiradas, new FileOutputStream ("Colecciones\\Peliculas.xml"));
            System.out.println ("Creado fichero XML....");

        } catch (Exception e) {
            e.printStackTrace ();
        }
    }

    private static void pasarCriticosCompletosAXML() throws IOException, ClassNotFoundException {

        //Abrir flujo de entrada de datos para leer los criticos completos .dat
        File ficheroCriticos = new File ("CriticosConValoraciones.dat");
        FileInputStream fiCriticos = new FileInputStream (ficheroCriticos);
        ObjectInputStream oiCriticos = new ObjectInputStream (fiCriticos);

        System.out.println ("Comienza el proceso de creación del fichero a XML ...");

        //Creamos una  Lista de peliculas con los objetos del dat

        ListaCriticos listaCriticosCompletos = new ListaCriticos ();
        Critico critico = (Critico) oiCriticos.readObject ();

        while (critico != null) {
            listaCriticosCompletos.add (critico);
            try {
                critico = (Critico) oiCriticos.readObject ();
            } catch (EOFException e) {
                critico = null;
            } catch (ClassNotFoundException e) {
                e.printStackTrace ();
            }

        }

        oiCriticos.close (); //cerrar stream de entrada

        try {
            XStream xstream = new XStream ();

            xstream.registerConverter (new CriticoConverter ());
            xstream.alias ("ListadeCriticos", ListaCriticos.class);
            xstream.alias ("Critico", Critico.class);

            xstream.addImplicitCollection (ListaCriticos.class, "lista");

            //Insrtar los objetos en el XML

            xstream.toXML (listaCriticosCompletos, new FileOutputStream ("Colecciones\\Criticos.xml"));
            System.out.println ("Creado fichero XML....");

        } catch (Exception e) {
            e.printStackTrace ();
        }
    }

    private static double calcularMedia(List<Integer> marks) {
        Integer sum = 0;
        if (!marks.isEmpty ()) {
            for (Integer mark : marks) {
                sum += mark;
            }
            return sum.doubleValue () / marks.size ();
        }
        return sum;
    }


    //xml metodos
    private static void crearColeccionPeliculasXML() throws IOException, ClassNotFoundException {

        //Abrir flujo de entrada de datos para leer peliculasValoradas .dat
        File ficheroPelis = new File ("dats_iniciales\\Peliculas.dat");
        FileInputStream fiPelis = new FileInputStream (ficheroPelis);
        ObjectInputStream oiPelis = new ObjectInputStream (fiPelis);


        System.out.println ("Comienza el proceso de creación del fichero a XML ...");

        //Creamos una  Lista de peliculas con los objetos del dat

        ListaPeliculas listaPelis = new ListaPeliculas ();
        Pelicula pelicula = (Pelicula) oiPelis.readObject ();

        while (pelicula != null) {
            listaPelis.add (pelicula);
            try {
                pelicula = (Pelicula) oiPelis.readObject ();
            } catch (EOFException e) {
                pelicula = null;
            } catch (ClassNotFoundException e) {
                e.printStackTrace ();
            }

        }

        oiPelis.close (); //cerrar stream de entrada

        try {
            XStream xstream = new XStream ();

            //cambiar de nombre a las etiquetas XML

            xstream.registerConverter (new PelisConverter ());
            xstream.alias ("Peliculas", ListaPeliculas.class);
            xstream.alias ("Pelicula", Pelicula.class);


            //quitar etiqueta lista (atributo de la clase ListaEmpleado)
            xstream.addImplicitCollection (ListaPeliculas.class, "lista");

            //Insrtar los objetos en el XML
            xstream.toXML (listaPelis, new FileOutputStream ("Colecciones\\Peliculas.xml"));
            System.out.println ("Creado fichero XML....");

        } catch (Exception e) {
            e.printStackTrace ();
        }
    }

    private static void crearColeccionCriticosXML() throws IOException, ClassNotFoundException {

        //Abrir flujo de entrada de datos para leer los criticos completos .dat
        File ficheroCriticos = new File ("dats_iniciales\\Criticos.dat");
        FileInputStream fiCriticos = new FileInputStream (ficheroCriticos);
        ObjectInputStream oiCriticos = new ObjectInputStream (fiCriticos);

        System.out.println ("Comienza el proceso de creación del fichero a XML ...");

        //Creamos una  Lista de criticos con los objetos del dat

        ListaCriticos listaCriticosCompletos = new ListaCriticos ();
        Critico critico = (Critico) oiCriticos.readObject ();

        while (critico != null) {
            listaCriticosCompletos.add (critico);
            try {
                critico = (Critico) oiCriticos.readObject ();
            } catch (EOFException e) {
                critico = null;
            } catch (ClassNotFoundException e) {
                e.printStackTrace ();
            }

        }

        oiCriticos.close (); //cerrar stream de entrada

        try {
            XStream xstream = new XStream ();

            xstream.registerConverter (new CriticoConverter ());
            xstream.alias ("Criticos", ListaCriticos.class);
            xstream.alias ("Critico", Critico.class);

            xstream.addImplicitCollection (ListaCriticos.class, "lista");

            //Insrtar los objetos en el XML

            xstream.toXML (listaCriticosCompletos, new FileOutputStream ("Colecciones\\Criticos.xml"));
            System.out.println ("Creado fichero XML....");

        } catch (Exception e) {
            e.printStackTrace ();
        }
    }

    private static void crearColeccionValoracionXML() throws IOException, ClassNotFoundException {

        //Abrir flujo de entrada de datos para leer los criticos completos .dat
        File ficheroValoraciones = new File ("dats_iniciales\\Valoraciones.dat");
        FileInputStream fiValoraciones = new FileInputStream (ficheroValoraciones);
        ObjectInputStream oiValoraciones = new ObjectInputStream (fiValoraciones);

        System.out.println ("Comienza el proceso de creación del fichero a XML ...");

        //Creamos una  Lista de valoraciones con los objetos del dat

        ListaValoraciones listaValoraciones = new ListaValoraciones ();
        Valoracion valoracion = (Valoracion) oiValoraciones.readObject ();

        while (valoracion != null) {
            listaValoraciones.add (valoracion);
            try {
                valoracion = (Valoracion) oiValoraciones.readObject ();
            } catch (EOFException e) {
                valoracion = null;
            } catch (ClassNotFoundException e) {
                e.printStackTrace ();
            }

        }

        oiValoraciones.close (); //cerrar stream de entrada

        try {
            XStream xstream = new XStream ();

            xstream.registerConverter (new ValoracionConverter ());
            xstream.alias ("Valoraciones", ListaValoraciones.class);
            xstream.alias ("valoracion", Valoracion.class);

            xstream.addImplicitCollection (ListaValoraciones.class, "lista");

            //Insrtar los objetos en el XML

            xstream.toXML (listaValoraciones, new FileOutputStream ("Colecciones\\Valoraciones.xml"));
            System.out.println ("Creado fichero XML....");

        } catch (Exception e) {
            e.printStackTrace ();
        }

    }

    private static void crearPeliculaTempXML(Pelicula pelicula) throws IOException, ClassNotFoundException {

        ListaPeliculas listaPelis = new ListaPeliculas ();
        listaPelis.add (pelicula);

        try {
            XStream xstream = new XStream ();

            //cambiar de nombre a las etiquetas XML

            xstream.registerConverter (new PelisConverter ());
            xstream.alias ("Peliculas", ListaPeliculas.class);
            xstream.alias ("Pelicula", Pelicula.class);

            xstream.addImplicitCollection (ListaPeliculas.class, "lista");


            //Insrtar los objetos en el XML
            xstream.toXML (listaPelis, new FileOutputStream ("temp\\Temp.xml"));
            System.out.println ("Creado fichero XML....");

        } catch (Exception e) {
            e.printStackTrace ();
        }
    }


    //exist metodos
    public static Collection conectar() {

        try {
            Class cl = Class.forName (driver); //Cargar del driver
            Database database = (Database) cl.getDeclaredConstructor ().newInstance (); //Instancia de la BD
            DatabaseManager.registerDatabase (database); //Registro del driver
            col = (Collection) DatabaseManager.getCollection (URI, usu, usuPwd);
            return col;
        } catch (XMLDBException e) {
            System.out.println ("Error al inicializar la BD eXist.");
            //e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println ("Error en el driver.");
            //e.printStackTrace();
        } catch (InstantiationException e) {
            System.out.println ("Error al instanciar la BD.");
            //e.printStackTrace();
        } catch (IllegalAccessException e) {
            System.out.println ("Error al instanciar la BD.");
            //e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace ();
        } catch (NoSuchMethodException e) {
            e.printStackTrace ();
        }
        return null;
    }

    private static void insertarPelicula(Pelicula pelicula) throws IOException, ClassNotFoundException {

        crearPeliculaTempXML (pelicula);

        col = conectar ();

        if (col != null) {
            try {
                XPathQueryService servicio = (XPathQueryService) col.getService ("XPathQueryService", "1.0");
                String query = "for $pel in doc('file:///C:/Users/Fran/Desktop/maria/curso3/AD/PROYECTO_JOKIN_EVAL2/CriticaCine/CriticaCine_eval2/temp/Temp.xml') /Peliculas/Pelicula return update insert $pel into /Peliculas";
                ResourceSet result = servicio.query (query);
                col.close (); //borramos
                System.out.println ("Pelicula Insertada.");

            } catch (Exception e) {
                System.out.println ("Error al insertar Pelicula.");
                e.printStackTrace ();

            }
        } else {
            System.out.println ("Error en la conexión. Comprueba datos.");

        }
    }

    private static void modificarGeneral(String id, String tabla, String dato, String idStr, String datoACambiar, Object nuevodato) {

        if (comprobarGeneral (id, tabla, dato, idStr)) {

            if (conectar () != null) {
                try {
                    System.out.printf ("Actualizo " + dato + ": %s\n", id);
                    XPathQueryService servicio = (XPathQueryService) col.getService ("XPathQueryService", "1.0");
                    //Consulta para modificar/actualizar un valor --> update value
                    String consulta1 = "update value /" + tabla + "/" + dato + "[" + idStr + "= '" + id + "']/" + datoACambiar + " with data('" + nuevodato + "') ";
                    ResourceSet result = servicio.query (
                            consulta1);

                    col.close ();
                    System.out.println (dato + " actualizado.");
                } catch (Exception e) {
                    System.out.println ("Error al actualizar.");
                    e.printStackTrace ();
                }
            } else {
                System.out.println ("Error en la conexión. Comprueba datos.");
            }
        } else {
            System.out.println ("El " + dato + " NO EXISTE.");
        }
    }

    private static void borrarrGeneral(String id, String tabla, String dato, String idStr) {

        if (comprobarGeneral (id, tabla, dato, idStr)) {
            if (conectar () != null) {
                try {
                    System.out.printf ("Borro el " + dato + " : %s\n", id);
                    XPathQueryService servicio = (XPathQueryService) col.getService ("XPathQueryService", "1.0");
                    //Consulta para borrar  --> update delete
                    String consulta2 = "update delete /" + tabla + "/" + dato + "[" + idStr + "= '" + id + "']";
                    ResourceSet result = servicio.query (
                            consulta2);
                    col.close ();
                    System.out.println (dato + " borrado.");
                } catch (Exception e) {
                    System.out.println ("Error al borrar.");
                    e.printStackTrace ();
                }
            } else {
                System.out.println ("Error en la conexión. Comprueba datos.");
            }
        } else {
            System.out.println ("El" + dato + "NO EXISTE.");
        }

    }


    private static boolean comprobarGeneral(String id, String tabla, String dato, String idStr) {
        //Devuelve true si el lo que sea existe
        if (conectar () != null) {
            try {
                XPathQueryService servicio = (XPathQueryService) col.getService ("XPathQueryService", "1.0");
                //Consulta para consultar la información de un departamento
                String consulta = "/" + tabla + "/" + dato + "[" + idStr + "='" + id + "']";
                ResourceSet result = servicio.query (consulta);
                ResourceIterator i;
                i = result.getIterator ();
                col.close ();
                if (!i.hasMoreResources ()) {
                    return false;
                } else {
                    return true;
                }
            } catch (Exception e) {
                System.out.println ("Error al consultar.");
                // e.printStackTrace();
            }
        } else {
            System.out.println ("Error en la conexión. Comprueba datos.");
        }

        return false;

    }

    private static void listarPeliculas() {
        if (conectar () != null) {
            try {
                XPathQueryService servicio;
                servicio = (XPathQueryService) col.getService ("XPathQueryService", "1.0");
                //Preparamos la consulta
                ResourceSet result = servicio.query ("for $peli in /Peliculas/Pelicula return $peli");
                // recorrer los datos del recurso.
                ResourceIterator i;
                i = result.getIterator ();
                if (!i.hasMoreResources ()) {
                    System.out.println (" LA CONSULTA NO DEVUELVE NADA O ESTÁ MAL ESCRITA");
                }
                while (i.hasMoreResources ()) {
                    Resource r = i.nextResource ();
                    System.out.println ("--------------------------------------------");
                    System.out.println ((String) r.getContent ());
                }
                col.close ();
            } catch (XMLDBException e) {
                System.out.println (" ERROR AL CONSULTAR DOCUMENTO.");
                e.printStackTrace ();
            }
        } else {
            System.out.println ("Error en la conexión. Comprueba datos.");
        }

    }

    private static boolean existeCodigo(String id) {
        boolean existe = false;
        if (conectar () != null) {
            try {
                XPathQueryService servicio;
                servicio = (XPathQueryService) col.getService ("XPathQueryService", "1.0");
                //Preparamos la consulta
                ResourceSet result = servicio.query ("for $peli in /Peliculas/Pelicula where $peli/@idPelicula= " + id + " return <a> {$peli/@idPelicula} </a>");
                // recorrer los datos del recurso.
                ResourceIterator i;
                i = result.getIterator ();
                if (!i.hasMoreResources ()) {
                    existe = false;
                } else {
                    existe = true;
                }
                col.close ();
            } catch (XMLDBException e) {
                System.out.println (" ERROR AL CONSULTAR DOCUMENTO.");
                e.printStackTrace ();
                existe = false;
            }
        } else {
            System.out.println ("Error en la conexión. Comprueba datos.");

        }
        return existe;

    }


    public static boolean isInteger(String s) {
        try {
            Integer.parseInt (s);
        } catch (NumberFormatException e) {
            return false;
        } catch (NullPointerException e) {
            return false;
        }
        // only got here if we didn't return false
        return true;
    }

    public static boolean isInteger2(String s, int radix) {
        Scanner sc = new Scanner (s.trim ());
        if (!sc.hasNextInt (radix)) return false;
        // we know it starts with a valid int, now make sure
        // there's nothing left!
        sc.nextInt (radix);
        return !sc.hasNext ();
    }

    public static Pelicula ingresarDatosPeli() {
        boolean fallido = true;
        Pelicula nuevaPeli = new Pelicula ();
        Scanner input = new Scanner (System.in);

        //ingreso de ID
        while (fallido) {
            System.out.println ("Ingresa Id válido para la nueva pelicula:");

            Scanner scan = new Scanner (System.in);
            //int id = scan.nextInt ();
            if (scan.hasNextInt ()) {
                int x = scan.nextInt ();
                if (!existeCodigo (String.valueOf (x))) {
                    nuevaPeli.setIdPelicula (x);
                    fallido = false;
                } else {
                    System.out.println ("YA EXISTE UN PELICULA CON EL CODIGO INSERTADO");
                }

            } else {
                System.out.println ("DATO INTRODUCIDO NO VALIDO");
                fallido = true;

            }

        }
        fallido = true;

        //ingreso de rubro
        while (fallido) {
            System.out.println ("Ingresa rubropara la nueva pelicula:");
            String genero;

            System.out.println ("\nRubros:");
            System.out.print ("\t\t1.) Horror\n");
            System.out.print ("\t\t2.) Biografia\n");
            System.out.print ("\t\t3.) Sci_Fi\n");
            System.out.print ("\t\t4.) Accion\n");
            System.out.print ("\t\t5.) Aventura\n");
            System.out.print ("\t\t6.) Comedia\n");
            System.out.print ("\t\t7.) Drama\n");
            System.out.print ("\t\t8.) Romance\n");
            System.out.print ("\t\t9.) Documental\n");
            System.out.print ("\nSelecciona una opcion valida: ");

            genero = input.nextLine ();

            switch (genero) {
                case "1":
                    nuevaPeli.setRubro (Rubro.Horror);
                    fallido = false;
                    break;
                case "2":
                    nuevaPeli.setRubro (Rubro.Biografia);
                    fallido = false;
                    break;
                case "3":
                    nuevaPeli.setRubro (Rubro.Sci_Fi);
                    fallido = false;
                    break;
                case "4":
                    nuevaPeli.setRubro (Rubro.Accion);
                    fallido = false;
                    break;
                case "5":
                    nuevaPeli.setRubro (Rubro.Aventura);
                    fallido = false;
                    break;
                case "6":
                    nuevaPeli.setRubro (Rubro.Comedia);
                    fallido = false;
                    break;
                case "7":
                    nuevaPeli.setRubro (Rubro.Drama);
                    fallido = false;
                    break;
                case "8":
                    nuevaPeli.setRubro (Rubro.Romance);
                    fallido = false;
                    break;
                case "9":
                    nuevaPeli.setRubro (Rubro.Documental);
                    fallido = false;
                    break;
                default:
                    System.out.println ("Opcion no valida...");
            }
        }
        fallido = true;

        //ingreso de nombre
        while (fallido) {
            System.out.println ("Ingresa nombre para la nueva pelicula:");
            String nombre = input.nextLine ();
            if (!nombre.equals ("")) {
                nuevaPeli.setNombre (nombre);
                fallido = false;
            } else {
                System.out.println ("EL NOMBRE NO PUEDE ESTAR VACIO");
            }
        }
        fallido = true;

        //ingreso de año
        while (fallido) {
            System.out.println ("Ingresa año de estreno nueva pelicula:");

            Scanner scan = new Scanner (System.in);
            if (scan.hasNextInt ()) {
                int x = scan.nextInt ();
                if (x >= 1800 && x <= 2021) {
                    nuevaPeli.setAnyo (x);
                    fallido = false;
                } else {
                    System.out.println ("AÑOS VALIDOS DE 1800 A 2021");
                }
            } else {
                System.out.println ("DATO INTRODUCIDO NO VALIDO");
                fallido = true;
            }

        }
        fallido = true;

        //ingreso de duracion
        while (fallido) {
            System.out.println ("Ingresa duracion en minutos de la nueva pelicula:");

            Scanner scan = new Scanner (System.in);
            if (scan.hasNextInt ()) {
                int x = scan.nextInt ();
                if (x >= 1 && x <= 360) {
                    nuevaPeli.setDuracion (x);
                    fallido = false;
                } else {
                    System.out.println ("MINUTOS VALIDOS ENTRE 1 Y 360");
                }
            } else {
                System.out.println ("DATO INTRODUCIDO NO VALIDO");
                fallido = true;
            }

        }
        fallido = true;

        //ingreso de descripcion
        while (fallido) {
            System.out.println ("Ingresa descripcion para la nueva pelicula:");
            String descripcion = input.nextLine ();
            if (!descripcion.equals ("")) {
                nuevaPeli.setDescripcion (descripcion);
                fallido = false;
            } else {
                System.out.println ("LA DESCRIPCION NO PUEDE ESTAR VACIA");
            }
        }

        nuevaPeli.setValoracionMedia (0);

        return nuevaPeli;
    }


//basuras


    private static void insertarPeliculaKK(Pelicula pelicula) {

        String nuevaPeli = "<Pelicula idPelicula = " + "\"" + pelicula.getIdPelicula () + "\"" +
                " rubro = " + "\"" + pelicula.getRubro () + "\"" + ">" + pelicula.getNombre () + " <anyo> " + pelicula.getAnyo () + " </anyo > " +
                "<duracion > " + pelicula.getDuracion () + "</duracion > " +
                "<descripcion > " + pelicula.getDescripcion () + "</descripcion >" +
                "<valoracionMedia > " + pelicula.getValoracionMedia () + "</valoracionMedia > </Pelicula >";


        String nuevaPeli2 = "<Pelicula idPelicula=\"500\" rubro=\"Sci_Fi\">Alien:El octavo pasajero<anyo>1979</anyo><duracion>117</duracion><descripcion>De regreso a la Tierra, la nave de carga Nostromo interrumpe su viaje y despierta a sus siete tripulantes. El ordenador central, MADRE, ha detectado la misteriosa transmision de una forma de vida desconocida, procedente de un planeta cercano aparentemente deshabitado.</descripcion><valoracionMedia>0</valoracionMedia></Pelicula>";


        col = conectar ();

        if (col != null) {
            try {
                XPathQueryService servicio = (XPathQueryService) col.getService ("XPathQueryService", "1.0");
                System.out.printf ("Inserto: %s \n", nuevaPeli);
                //Consulta para insertar --> update insert ... into
                ResourceSet result = servicio.query ("update insert " + nuevaPeli + " into /Peliculas");
                col.close (); //borramos
                System.out.println ("Pelicula Insertada.");
            } catch (Exception e) {
                System.out.println ("Error al insertar Pelicula.");
                e.printStackTrace ();
            }
        } else {
            System.out.println ("Error en la conexión. Comprueba datos.");
        }
    }

    private static void crearXMLTemp() throws XMLDBException {

        File archivo = new File ("temp\\Temp.xml");
        if (!archivo.canRead ())
            System.out.println ("ERROR AL LEER EL FICHERO");
        else {
            Resource nuevoRecurso = col.createResource (archivo.getName (),
                    "XMLResource");
            nuevoRecurso.setContent (archivo); //Asignamos el archivo
            col.storeResource (nuevoRecurso); //Lo almacenamos en la colección
        }
    }

    private static void borrarXMLTemp() {
        try {
            Resource recursoBorrar = col.getResource ("temp\\Temp.xml");
            col.removeResource (recursoBorrar);
        } catch (NullPointerException | XMLDBException e) {
            System.out.println ("El recurso no se puede borrar porque no se encuentra.");
        }
    }

    public static String validaciones(Pelicula pelicula, int tipoAccion) {

        HashMap<String, String> errores = new HashMap<> ();

        if (tipoAccion == 0) { //si es insert=0 NO DEBE EXISTIR CODIGO
            if (existeCodigo (String.valueOf (pelicula.getIdPelicula ()))) {
                errores.put ("CODIGO", "YA EXISTE UN PELICULA CON EL CODIGO INSERTADO");
            }
        } else if (tipoAccion == 1) { //si es modificar= 1 NO DEBE EXISTIR EL CODIGO
            if (!existeCodigo (String.valueOf (pelicula.getIdPelicula ()))) {
                errores.put ("CODIGO", "EL CODIGO DE PELICULA NO EXISTE");
            }
        } else if (tipoAccion == 2) {//si es eliminar= 2 DEBE EXISTIR CODIGO
            if (!existeCodigo (String.valueOf (pelicula.getIdPelicula ()))) {
                errores.put ("CODIGO", "EL CODIGO DE PELICULA NO EXISTE");
            }
        }


        if (tipoAccion != 2) { // si no es eliminar necesito todos los datos en el form

            //controlar que sea un int cuando lo teclean, si no no puedo armar el objeto

            if ((String.valueOf (pelicula.getIdPelicula ())).equals ("")) {
                errores.put ("CODIGO", "EL CODIGO NO PUEDE ESTAR VACIO");
            }


            if (pelicula.getNombre ().equals ("")) {
                errores.put ("NOMBRE", "EL NOMBRE NO PUEDE ESTAR VACIO");
            }

            //controlar que sea un rubro cuando lo teclean, si no no puedo armar el objeto

            //controlar que sea un int cuando lo teclean, si no no puedo armar el objeto
            if ((String.valueOf (pelicula.getAnyo ())).equals ("")) {
                errores.put ("AÑO", "EL AÑO NO PUEDE ESTAR VACIO");
            }


            //controlar que sea un int cuando lo teclean, si no no puedo armar el objeto
            if ((String.valueOf (pelicula.getDuracion ())).equals ("")) {
                errores.put ("DURACION", "LA DURACION NO PUEDE ESTAR VACIA");
            }


            if (pelicula.getDescripcion ().equals ("")) {
                errores.put ("DESCRIPCION", "LA DESCRIPCION NO PUEDE QUEDAR VACIA");
            }


        }


        //Utilizamos esta variable para guardar el mensaje de error.
        StringBuilder texto = new StringBuilder ();

        if (errores.size () > 0) {
            for (Map.Entry<String, String> entry : errores.entrySet ()) {
                String k = entry.getKey ();
                String v = entry.getValue ();
                texto.append (v + "\n");
            }
            return texto.toString ();
        } else {
            return null;
        }

    }


}







