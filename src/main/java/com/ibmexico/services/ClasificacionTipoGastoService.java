package com.ibmexico.services;

import java.util.List;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;


import com.ibmexico.entities.ClasificacionTipoGastoEntity;
import com.ibmexico.repositories.IClasificacionTipoGastoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("clasificacion_tipogastoService")
public class ClasificacionTipoGastoService{

    @Autowired
    @Qualifier("clasificacion_tipogasto_repository")
    private IClasificacionTipoGastoRepository tipoGastoRepository;

    public ClasificacionTipoGastoEntity findByIdClasificacion(int idClasificacion){
        return tipoGastoRepository.findByIdClasificacion(idClasificacion);
    }


    public JsonObject jsonTipoGastoActivo(int idTipoGasto){
        JsonObjectBuilder jsonReturn= Json.createObjectBuilder();
        JsonArrayBuilder jsonRows=Json.createArrayBuilder();
        List<ClasificacionTipoGastoEntity> lstTipoGasto= tipoGastoRepository.findClasificacionTipoGastoActivo(idTipoGasto);

        lstTipoGasto.forEach((item)->{
            jsonRows.add(Json.createObjectBuilder()
            .add("id_tipogasto", item.getIdClasificacion())
            .add("nombre", item.getNombre())
            );
        });
        jsonReturn.add("rows", jsonRows);
        return jsonReturn.build();
    }
}