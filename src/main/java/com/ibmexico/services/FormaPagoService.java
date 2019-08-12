package com.ibmexico.services;

import java.util.List;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.ibmexico.entities.FormaPagoEntity;
import com.ibmexico.repositories.IFormaPagoRepository;

@Service("formaPagoService")
public class FormaPagoService {

	@Autowired
	@Qualifier("formaPagoRepository")
	IFormaPagoRepository formaPagoRepository;
	
	public List<FormaPagoEntity> listFormasPagos() {
		return formaPagoRepository.findAll();
	}

	public JsonObject jsonFormasPagos() {
		JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
		JsonArrayBuilder jsonRows = Json.createArrayBuilder();

		List<FormaPagoEntity> lstFormasPagos = formaPagoRepository.findAll();

		lstFormasPagos.forEach((item)-> {
			jsonRows.add(Json.createObjectBuilder()
				.add("id_forma_pago", item.getIdFormaPago())
				.add("forma_pago", item.getFormaPago())
			);
		});

		jsonReturn.add("rows", jsonRows);
		
		return jsonReturn.build();
	}
	
	public FormaPagoEntity find(int idFormaPago) {
		return formaPagoRepository.findByIdFormaPago(idFormaPago);
	}
}
