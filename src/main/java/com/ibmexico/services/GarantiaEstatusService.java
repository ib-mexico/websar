package com.ibmexico.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.ibmexico.entities.GarantiaEstatusEntity;
import com.ibmexico.repositories.IGarantiaEstatusRepository;

@Service("garantiaEstatusService")
public class GarantiaEstatusService {
	
	@Autowired
	@Qualifier("garantiaEstatusRepository")
	private IGarantiaEstatusRepository garantiaEstatusRepository;
	
	public List<GarantiaEstatusEntity> listAll() {
		return garantiaEstatusRepository.findAll();
	}
	
	public GarantiaEstatusEntity findByIdGarantiaEstatus(int idGarantiaEstatus) {
		return garantiaEstatusRepository.findByIdGarantiaEstatus(idGarantiaEstatus);
	}

}
