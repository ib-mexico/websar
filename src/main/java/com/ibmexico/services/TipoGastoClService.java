package com.ibmexico.services;

import java.util.List;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import com.ibmexico.entities.TipoGastoClEntity;
import com.ibmexico.repositories.ITipoGastoClRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
@Service("principal_clasificacion_gasto")
public class TipoGastoClService{

    @Autowired
    @Qualifier("principal_clasificacion_gasto_rep")
    private ITipoGastoClRepository tipoGastoClRep;

    public List<TipoGastoClEntity> lstTipoGasto(){
        return tipoGastoClRep.findActivos();
    }

    public TipoGastoClEntity findById(int id){
        return tipoGastoClRep.findByIdTipoGastoCl(id);
    }


    public JsonObject jsonClasificacionGasto(){
        JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
        JsonArrayBuilder jsonRows = Json.createArrayBuilder();
        List<TipoGastoClEntity> lstTipoGastoCl= tipoGastoClRep.findActivos();
        lstTipoGastoCl.forEach((item)->{
            jsonRows.add(Json.createObjectBuilder()
            .add("idClasificacionGasto", item.getIdTipoGastoCl())
            .add("descripcion",item.getDescripcion())
            );
        });
        jsonReturn.add("rows", jsonRows);
        return jsonReturn.build();       
    }
}