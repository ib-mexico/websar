package com.ibmexico.services;

import java.util.List;

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
	
	public MonedaEntity find(int idMoneda) {
		return monedaRepository.findByIdMoneda(idMoneda);
	}
}
