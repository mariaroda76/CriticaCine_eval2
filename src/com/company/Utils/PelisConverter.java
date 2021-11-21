package com.company.Utils;

import com.company.Modelos.Pelicula;
import com.company.Rubro;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class PelisConverter implements Converter {


        public void marshal(Object value, HierarchicalStreamWriter writer,
                            MarshallingContext context) {
            Pelicula pelicula = (Pelicula) value;


            writer.addAttribute("idPelicula", String.valueOf( pelicula.getIdPelicula()));
            writer.addAttribute("rubro", pelicula.getRubro().toString());
            writer.setValue(pelicula.getNombre());

            writer.startNode("anyo");
            writer.setValue(String.valueOf(pelicula.getAnyo()));
            writer.endNode();

            writer.startNode("duracion");
            writer.setValue(String.valueOf(pelicula.getDuracion()));
            writer.endNode();

            writer.startNode("descripcion");
            writer.setValue(pelicula.getDescripcion());
            writer.endNode();

            writer.startNode("valoracionMedia");
            writer.setValue(String.valueOf(pelicula.getValoracionMedia()));
            writer.endNode();


        }

        public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
            Pelicula pelicula = new Pelicula();
            pelicula.setNombre(reader.getValue());
            pelicula.setIdPelicula(Integer.parseInt(reader.getAttribute("idPelicula")));
            pelicula.setRubro(Rubro.valueOf(reader.getAttribute("rubro")));

            reader.moveDown();
            pelicula.setAnyo(Integer.parseInt(reader.getValue()));
            reader.moveUp();

            reader.moveDown();
            pelicula.setDuracion(Integer.parseInt(reader.getValue()));
            reader.moveUp();

            reader.moveDown();
            pelicula.setDescripcion(reader.getValue());
            reader.moveUp();

            reader.moveDown();
            pelicula.setValoracionMedia(Integer.parseInt(reader.getValue()));
            reader.moveUp();

            return pelicula;
        }

        public boolean canConvert(Class clazz) {
            return clazz.equals(Pelicula.class);
        }

    }
    
    
    
    
    
    
    
