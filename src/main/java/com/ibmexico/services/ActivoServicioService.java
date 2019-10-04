package com.ibmexico.services;

import java.util.List;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import com.ibmexico.entities.ActivoServicioEntity;
import com.ibmexico.repositories.IActivoServicioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("activo_servicio")
public class ActivoServicioService{

     @Autowired
     @Qualifier("activo_servicio_repository")
     private IActivoServicioRepository activoServiciorep;

    public ActivoServicioEntity findByIdActivoServicio(int idActivoServicio){
        return activoServiciorep.findByIdActivoServicio(idActivoServicio);
    }

    public List<ActivoServicioEntity> findByIdTipoActivo(int idTipoActivo){
        return activoServiciorep.findByIdTipoActivo(idTipoActivo);
    }

    public JsonObject jsonServicioTipoActivo(int idTipoActivo){

        JsonObjectBuilder jsonReturn= Json.createObjectBuilder();
        JsonArrayBuilder jsonRows = Json.createArrayBuilder();
        List<ActivoServicioEntity> lstServicioTipo=activoServiciorep.findByIdTipoActivo(idTipoActivo);
        lstServicioTipo.forEach((item)->{
            jsonRows.add(Json.createObjectBuilder()
            .add("id_servicio",item.getIdActivoServicio())
            .add("descripcion", item.getDescripcion())
            .add("precio_estimado", item.getPrecioEstimado()));
        });
        jsonReturn.add("rows",jsonRows);
        return jsonReturn.build();
    }



}