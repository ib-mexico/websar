package com.ibmexico.services;

import java.util.List;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.ibmexico.entities.CotizacionTipoFicheroEntity;
import com.ibmexico.repositories.ICotizacionTipoFicheroRepository;

@Service("cotizacionTipoFicheroService")
public class CotizacionTipoFicheroService {

	@Autowired
	@Qualifier("cotizacionTipoFicheroRepository")
	private ICotizacionTipoFicheroRepository cotizacionTipoFicheroRepository;
	
	public List<CotizacionTipoFicheroEntity> listAll() {
		return cotizacionTipoFicheroRepository.findAll();
	}
	
	public CotizacionTipoFicheroEntity findByIdCotizacionTipoFichero(int idCotizacionTipoFichero) {
		return cotizacionTipoFicheroRepository.findByIdCotizacionTipoFichero(idCotizacionTipoFichero);
	}

	public JsonObject jsonTipoFichero(){
		JsonObjectBuilder jsonReturn= Json.createObjectBuilder();
		JsonArrayBuilder jsonRows=Json.createArrayBuilder();
		List<CotizacionTipoFicheroEntity> lstTipoFichero=cotizacionTipoFicheroRepository.findAll();
		lstTipoFichero.forEach((item)->{
			jsonRows.add(Json.createObjectBuilder()
			.add("id_tipo_fichero", item.getIdCotizacionTipoFichero())
			.add("cotizacion", item.getCotizacionTipoFichero())
			);
		});
		jsonReturn.add("rows", jsonRows);
		return jsonReturn.build();
	}
}
