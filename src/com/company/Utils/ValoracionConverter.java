package com.company.Utils;

import com.company.Modelos.Valoracion;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class ValoracionConverter implements Converter {


    public void marshal(Object value, HierarchicalStreamWriter writer,
                        MarshallingContext context) {
        Valoracion valoracion = (Valoracion) value;

        writer.startNode("idPelicula");
        writer.setValue(String.valueOf(valoracion.getIdPelicula()));
        writer.endNode();

        writer.startNode("idCritico");
        writer.setValue(String.valueOf(valoracion.getIdCritico()));
        writer.endNode();

        writer.startNode("valoracion");
        writer.setValue(String.valueOf(valoracion.getValoracion()));
        writer.endNode();



    }

    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        Valoracion valoracion = new Valoracion();

        reader.moveDown();
        valoracion.setIdPelicula(Integer.parseInt(reader.getValue()));
        reader.moveUp();

        reader.moveDown();
        valoracion.setIdCritico(Integer.parseInt(reader.getValue()));
        reader.moveUp();

        reader.moveDown();
        valoracion.setValoracion(Integer.parseInt(reader.getValue()));
        reader.moveUp();


        return valoracion;
    }

    @Override
    public boolean canConvert(Class aClass) {
        return false;
    }
}
