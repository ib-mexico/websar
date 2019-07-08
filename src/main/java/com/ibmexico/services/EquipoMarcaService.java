package com.ibmexico.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.ibmexico.entities.EquipoMarcaEntity;
import com.ibmexico.repositories.IEquipoMarcaRepository;

@Service("equipoMarcaService")
public class EquipoMarcaService {
	
	@Autowired
	@Qualifier("equipoMarcaRepository")
	private IEquipoMarcaRepository equipoMarcaRepository;
	
	public List<EquipoMarcaEntity> listAll() {
		return equipoMarcaRepository.findAll();
	}
	
	public List<EquipoMarcaEntity> listMarcasActivas() {
		return equipoMarcaRepository.listMarcasActivas();
	}
	
	public EquipoMarcaEntity findByIdEquipoMarca(int idEquipoMarca) {
		return equipoMarcaRepository.findByIdEquipoMarca(idEquipoMarca);
	}

}
