package com.ibmexico.services;

import java.util.List;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.ibmexico.entities.DepartamentoEntity;
import com.ibmexico.repositories.IDepartamentoRepository;

@Service("departamentoService")
public class DepartamentoService {

	@Autowired
	@Qualifier("departamentoRepository")
	private IDepartamentoRepository departamentoRepository;
	
	@Autowired
	@Qualifier("sessionService")
	private SessionService sessionService;
	
	public DepartamentoEntity findByIdDepartamento(int idDepartamento) {
		return departamentoRepository.findByIdDepartamento(idDepartamento);
	}
	
	public List<DepartamentoEntity> listDepartamentos() {
		return departamentoRepository.findAll();
	}

	//crear un listado en format json
	public JsonObject jsonDepartamento(){
		JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
		JsonArrayBuilder jsonRows = Json.createArrayBuilder();

		List<DepartamentoEntity> lstDepartamento =  departamentoRepository.findAll();

		lstDepartamento.forEach((item)-> {
			jsonRows.add(Json.createObjectBuilder()
				.add("id_departamento", item.getIdDepartamento())
				.add("nombre_departamento", item.getDepartamento())
			);
		});

		jsonReturn.add("rows", jsonRows);
		
		return jsonReturn.build();
	}
}
