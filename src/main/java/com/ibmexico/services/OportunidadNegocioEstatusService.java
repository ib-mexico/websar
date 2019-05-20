package com.ibmexico.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.ibmexico.entities.OportunidadNegocioEstatusEntity;
import com.ibmexico.repositories.IOportunidadNegocioEstatusRepository;

@Service("oportunidadNegocioEstatusService")
public class OportunidadNegocioEstatusService {
	
	@Autowired
	@Qualifier("oportunidadNegocioEstatusRepository")
	private IOportunidadNegocioEstatusRepository oportunidadNegocioEstatusRepository;

	@Autowired
	@Qualifier("sessionService")
	private SessionService sessionService;
	
	public OportunidadNegocioEstatusEntity findByIdOportunidadNegocioEstatus(int idOportunidadEstatus) {
		return oportunidadNegocioEstatusRepository.findByIdOportunidadNegocioEstatus(idOportunidadEstatus);
	}
	
	public List<OportunidadNegocioEstatusEntity> listOportunidadesEstatus() {
		return oportunidadNegocioEstatusRepository.findAll();
	}
}
