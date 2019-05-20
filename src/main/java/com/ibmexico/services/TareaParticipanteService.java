package com.ibmexico.services;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.ibmexico.libraries.notifications.ApplicationException;
import com.ibmexico.libraries.notifications.EnumException;
import com.ibmexico.entities.TareaParticipanteEntity;
import com.ibmexico.repositories.ITareaParticipanteRepository;

@Service("tareaParticipanteService")
public class TareaParticipanteService {

	@Autowired
	@Qualifier("tareaParticipanteRepository")
	private ITareaParticipanteRepository tareaParticipanteRepository;
	
	@Autowired
	@Qualifier("sessionService")
	private SessionService sessionService;
	
	public TareaParticipanteEntity findByIdTareaParticipante(int idTareaParticipante) {
		return tareaParticipanteRepository.findByIdTareaParticipante(idTareaParticipante);
	}
	
	public List<TareaParticipanteEntity> findByTarea_IdTarea(int idTarea) {
		return tareaParticipanteRepository.findByTarea_IdTarea(idTarea);
	}
	
	public List<TareaParticipanteEntity> listTareasParticipantes() {
		
		List<TareaParticipanteEntity> lstTareasParticipantes = null;
		
		if(sessionService.hasRol("TAREAS_ADMINISTRADOR")) {
			lstTareasParticipantes = tareaParticipanteRepository.listTareaParticipantes();
		}
		else {
			lstTareasParticipantes = tareaParticipanteRepository.listTareaParticipantes(sessionService.getCurrentUser().getIdUsuario());
		}
		
		return lstTareasParticipantes;
	}
	
	@Transactional
	public void removeParticipantes(int idTarea) {
		tareaParticipanteRepository.removeTareaParticipantes(idTarea);
	}
	
	@Transactional
	public void create(TareaParticipanteEntity objTareaParticipante) {
		
		if(objTareaParticipante != null) {
			tareaParticipanteRepository.save(objTareaParticipante);
		}
		else {
			throw new ApplicationException(EnumException.TAREAS_PARTICIPANTES_CREATE_001);
		}
	}
	
	public void update(TareaParticipanteEntity objTareaParticipante) {
		
		if(objTareaParticipante != null) {
			tareaParticipanteRepository.save(objTareaParticipante);
		}
		else {
			throw new ApplicationException(EnumException.TAREAS_PARTICIPANTES_UPDATE_001);
		}
	}
}
