package com.ibmexico.services;

import java.util.List;

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
}
