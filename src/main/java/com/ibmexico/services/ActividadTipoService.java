package com.ibmexico.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.ibmexico.entities.ActividadTipoEntity;
import com.ibmexico.repositories.IActividadTipoRepository;

@Service("actividadTipoService")
public class ActividadTipoService {

	@Autowired
	@Qualifier("actividadTipoRepository")
	private IActividadTipoRepository actividadTipoRepository;
	
	@Autowired
	@Qualifier("sessionService")
	private SessionService sessionService;
	
	public ActividadTipoEntity findByIdActividadTipo(int idActividadTipo) {
		return actividadTipoRepository.findByIdActividadTipo(idActividadTipo);
	}
	
	public List<ActividadTipoEntity> listActividadesTipos() {
		return actividadTipoRepository.findAll();
	}
}
