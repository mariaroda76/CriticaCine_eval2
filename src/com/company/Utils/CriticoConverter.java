package com.company.Utils;

import com.company.Modelos.Critico;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class CriticoConverter implements Converter {

    public void marshal(Object value, HierarchicalStreamWriter writer,
                        MarshallingContext context) {
        Critico critico = (Critico) value;


        writer.addAttribute("idCritico", String.valueOf( critico.getIdCritico()));
        writer.setValue(critico.getNombre());

        writer.startNode("cantidadValoraciones");
        writer.setValue(String.valueOf(critico.getCantidadValoraciones()));
        writer.endNode();

        writer.startNode("mediaValoraciones");
        writer.setValue(String.valueOf(critico.getMediaValoraciones()));
        writer.endNode();




    }

    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        Critico critico = new Critico();
        critico.setNombre(reader.getValue());
        critico.setIdCritico(Integer.parseInt(reader.getAttribute("idCritico")));


        reader.moveDown();
        critico.setCantidadValoraciones(Integer.parseInt(reader.getValue()));
        reader.moveUp();

        reader.moveDown();
        critico.setMediaValoraciones(Integer.parseInt(reader.getValue()));
        reader.moveUp();

        return critico;
    }


    @Override
    public boolean canConvert(Class aClass) {
        return false;
    }
}
