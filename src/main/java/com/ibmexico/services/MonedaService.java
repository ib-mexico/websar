package com.ibmexico.services;

import java.util.List;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.ibmexico.entities.MonedaEntity;
import com.ibmexico.repositories.IMonedaRepository;

@Service("monedaService")
public class MonedaService {

	@Autowired
	@Qualifier("monedaRepository")
	IMonedaRepository monedaRepository;
	
	public List<MonedaEntity> listMonedas() {
		return monedaRepository.findAll();
	}

	public JsonObject jsonMonedas() {
		JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
		JsonArrayBuilder jsonRows = Json.createArrayBuilder();

		List<MonedaEntity> lstMonedas = monedaRepository.findAll();

		lstMonedas.forEach((item)-> {
			jsonRows.add(Json.createObjectBuilder()
				.add("id_moneda", item.getIdMoneda())
				.add("moneda", item.getMoneda())
			);
		});

		jsonReturn.add("rows", jsonRows);
		
		return jsonReturn.build();
	}
	
	public MonedaEntity find(int idMoneda) {
		return monedaRepository.findByIdMoneda(idMoneda);
	}
}
