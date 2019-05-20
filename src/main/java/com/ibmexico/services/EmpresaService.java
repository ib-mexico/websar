package com.ibmexico.services;

import java.util.List;

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
}
