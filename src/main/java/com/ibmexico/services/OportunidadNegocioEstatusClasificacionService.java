package com.ibmexico.services;

import java.util.List;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import com.ibmexico.entities.OportunidadNegocioEstatusClasificacionEntity;
import com.ibmexico.repositories.IOportunidadNegocioEstatusClasificacionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("oportunidadNegocioEstatusClasificacionService")
public class OportunidadNegocioEstatusClasificacionService {

    @Autowired
	@Qualifier("oportunidadNegocioEstatusClasificacionRepository")
	private IOportunidadNegocioEstatusClasificacionRepository opnNegocioEstatusClasificacionRepository;

	@Autowired
	@Qualifier("sessionService")
	private SessionService sessionService;
	
	public OportunidadNegocioEstatusClasificacionEntity findByIdOportunidadNegocioEstatus(int idOpnNegocioEstatusClasificacion) {
		return opnNegocioEstatusClasificacionRepository.findByIdOpnNegocioEstatusClasificacion(idOpnNegocioEstatusClasificacion);
	}
	
	public List<OportunidadNegocioEstatusClasificacionEntity> listOpnEstatusClasificacion() {
		return opnNegocioEstatusClasificacionRepository.findAll();
    }

    public JsonObject jsonEstatusClasificacion(int idEstatus) {
		JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
		JsonArrayBuilder jsonRows = Json.createArrayBuilder();

		List<OportunidadNegocioEstatusClasificacionEntity> lstOpnEstatusClasificacion = opnNegocioEstatusClasificacionRepository.findByIdOpnEstatus(idEstatus);

		lstOpnEstatusClasificacion.forEach((item)-> {
			jsonRows.add(Json.createObjectBuilder()
				.add("id_Opn_Estatus_clasificacion", item.getIdOpnNegocioEstatusClasificacion())
				.add("nombre", item.getNombre())
			);
		});
		jsonReturn.add("rows", jsonRows);
		
		return jsonReturn.build();
	}

    public List<OportunidadNegocioEstatusClasificacionEntity> lstOpnEstatusClasificacion(int idEstatus) {
		return opnNegocioEstatusClasificacionRepository.findByIdOpnEstatus(idEstatus);
    }
}