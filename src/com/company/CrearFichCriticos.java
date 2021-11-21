package com.company;

import com.company.Modelos.Critico;
import com.company.Modelos.Pelicula;

import java.io.*;

public class CrearFichCriticos {

    public static void main(String[] args) throws IOException, ClassNotFoundException {

        //fichero dat
        ObjectOutputStream fileCriticosOS = new ObjectOutputStream(new FileOutputStream("dats_iniciales\\Criticos.dat"));

        int idCriticos[] = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15,16,17,18,19,20};
        String nombresCriticos[] = {"Antonio José Navarro", "Marcial Cantero", "María Luz Morales", "Juan Piqueras Martínez",
                "Óscar Esquivias", "Ángel Fernández-Santos", "Emilio García Riera", "Florentino Hernández Girbal",
                "César Santos Fontenla", "Daniel V. Villamediana", "Toni Junyent", "José S. Isbert", "Elena Duque", "Miguel Juan Payán", "Fernando Rodríguez Lafuente", "Juan Perez", "Pedro Dominguez", "Micaela Paz","Juan Rivas","Roberto Cazas" };

        //escribo en el fichero dat
        for (int i = 0; i < idCriticos.length; i++) {

            Critico critico = new Critico();
            critico.setIdCritico(idCriticos[i]);
            critico.setNombre(nombresCriticos[i]);
            critico.setCantidadValoraciones(0);
            critico.setMediaValoraciones(0);

            fileCriticosOS.writeObject(critico);
        }
        //escribo por ultimo un null, asi al hacer los while al final encuentra un null... igualmente hago un try al leerlos por si me olvido
        fileCriticosOS.writeObject(null);

        //cerrar ObjectStream
        fileCriticosOS.close();




    }

    public static void leerFicheroCriticos() throws IOException, ClassNotFoundException {
        ObjectInputStream fileCriticosIP = new ObjectInputStream(new FileInputStream("Criticos.dat"));
        Critico critico = (Critico) fileCriticosIP.readObject();
        try {
            while (critico != null) {
                System.out.println("---------------");
                critico.imprimir();
                critico = (Critico) fileCriticosIP.readObject();

            }
        } catch (EOFException e) {
        }

        fileCriticosIP.close();
    }

}
