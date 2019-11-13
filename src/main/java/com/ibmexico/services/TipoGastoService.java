package com.ibmexico.services;

import java.util.List;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import com.ibmexico.entities.TipoGastoEntity;
import com.ibmexico.repositories.ITipoGastoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
@Service("tipo_gasto_service")
public class TipoGastoService{

    @Autowired
    @Qualifier("tipo_gasto_rep")
    private ITipoGastoRepository tipoGastoRep;

    public List<TipoGastoEntity> lstTipoGasto(){
        return tipoGastoRep.findActivos();
    }
    public TipoGastoEntity findByIdTipoGasto(int idTipo){
        return tipoGastoRep.findByIdTipoGasto(idTipo);
    }

    public JsonObject jsonTipoGasto(){
        JsonObjectBuilder jsonReturn=Json.createObjectBuilder();
        JsonArrayBuilder jsonRows=Json.createArrayBuilder();
        List<TipoGastoEntity> lstTipoGasto= tipoGastoRep.findActivos();
        lstTipoGasto.forEach((item)->{
            jsonRows.add(Json.createObjectBuilder()
            .add("id_tipo_gasto", item.getIdTipoGasto())
            .add("nombre_gasto",item.getNombre())
            );
        });
        jsonReturn.add("tipo_gasto", jsonRows);
        return jsonReturn.build();       
    }
}