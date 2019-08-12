package com.ibmexico.services;

import java.util.List;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.ibmexico.entities.EmpresaEntity;
import com.ibmexico.repositories.IEmpresaRepository;

@Service("empresaService")
public class EmpresaService {

	@Autowired
	@Qualifier("empresaRepository")
	private IEmpresaRepository empresaRepository;
	
	@Autowired
	@Qualifier("sessionService")
	private SessionService sessionService;
	
	public EmpresaEntity findByIdEmpresa(int idEmpresa) {
		return empresaRepository.findByIdEmpresa(idEmpresa);
	}
	
	public List<EmpresaEntity> listEmpresas() {
		return empresaRepository.listEmpresas();
	}

	public JsonObject jsonEmpresas() {
		JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
		JsonArrayBuilder jsonRows = Json.createArrayBuilder();

		List<EmpresaEntity> lstEmpresas = empresaRepository.listEmpresas();

		lstEmpresas.forEach((item)-> {
			jsonRows.add(Json.createObjectBuilder()
				.add("id_empresa", item.getIdEmpresa())
				.add("razon_social", item.getRazonSocial())
			);
		});

		jsonReturn.add("rows", jsonRows);
		
		return jsonReturn.build();
	}
}
