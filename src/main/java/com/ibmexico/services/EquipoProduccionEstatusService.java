package com.ibmexico.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.ibmexico.entities.EquipoProduccionEstatusEntity;
import com.ibmexico.repositories.IEquipoProduccionEstatusRepository;

@Service("equipoProduccionEstatusService")
public class EquipoProduccionEstatusService {
	
	@Autowired
	@Qualifier("equipoProduccionEstatusRepository")
	private IEquipoProduccionEstatusRepository equipoProduccionEstatusRepository;
	
	public List<EquipoProduccionEstatusEntity> listAll() {
		return equipoProduccionEstatusRepository.findAll();
	}
	
	public List<EquipoProduccionEstatusEntity> listEstatusActivos() {
		return equipoProduccionEstatusRepository.listEstatusActivos();
	}
	
	public EquipoProduccionEstatusEntity findByIdEquipoProduccionEstatus(int idEquipoProduccionEstatus) {
		return equipoProduccionEstatusRepository.findByIdEquipoProduccionEstatus(idEquipoProduccionEstatus);
	}

}
