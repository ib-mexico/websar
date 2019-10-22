package com.ibmexico.services;

import java.util.List;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import com.ibmexico.entities.ActivoEstatusEntity;
import com.ibmexico.repositories.IActivoEstatusRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("activo_estatus_service")
public class ActivoEstatusService{

    @Autowired
    @Qualifier("activo_estatus_repository")
	private IActivoEstatusRepository activoEstatus;
	
    public ActivoEstatusEntity findByIdActivoEstatus(int idActivoEstatus){
        return activoEstatus.findByIdActivoEstatus(idActivoEstatus);
	}
	
	public JsonObject jsonEstatus() {
		JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
		JsonArrayBuilder jsonRows = Json.createArrayBuilder();
		List<ActivoEstatusEntity> lstEstatus = activoEstatus.lstEstatus();
		lstEstatus.forEach((item)-> {
            jsonRows.add(Json.createObjectBuilder()
                .add("id_activo_estatus", item.getIdActivoEstatus())
                .add("activo_estatus", item.getActivoEstatus())
			);
		});
		jsonReturn.add("estatus", jsonRows);
		return jsonReturn.build();
	}
}