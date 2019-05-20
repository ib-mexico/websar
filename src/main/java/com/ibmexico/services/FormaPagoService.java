package com.ibmexico.services;

import java.util.List;

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
	
	public FormaPagoEntity find(int idFormaPago) {
		return formaPagoRepository.findByIdFormaPago(idFormaPago);
	}
}
