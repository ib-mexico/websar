package com.ibmexico.services;

import java.time.LocalDateTime;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.ibmexico.libraries.notifications.ApplicationException;
import com.ibmexico.libraries.notifications.EnumException;
import com.ibmexico.entities.TareaEntity;
import com.ibmexico.entities.TareaParticipanteEntity;
import com.ibmexico.entities.UsuarioEntity;
import com.ibmexico.repositories.ITareaRepository;

@Service("tareaService")
public class TareaService {

	@Autowired
	@Qualifier("tareaRepository")
	private ITareaRepository tareaRepository;
	
	@Autowired
	@Qualifier("tareaParticipanteService")
	private TareaParticipanteService tareaParticipanteService;
	
	@Autowired
	@Qualifier("usuarioService")
	private UsuarioService usuarioService;
	
	@Autowired
	@Qualifier("sessionService")
	private SessionService sessionService;
	
	public TareaEntity findByIdTarea(int idTarea) {
		return tareaRepository.findByIdTarea(idTarea);
	}
	
	public List<TareaEntity> listTareas() {
		
		List<TareaEntity> lstTareas = null;
		
		if(sessionService.hasRol("TAREAS_ADMINISTRADOR")) {
			lstTareas = tareaRepository.listTareas();
		}
		else {
			lstTareas = tareaRepository.listTareas(sessionService.getCurrentUser().getIdUsuario());
		}
		
		return lstTareas;
	}
	
	@Transactional
	public void create(TareaEntity objTarea, Integer[] arrParticipantes) {
		
		if(objTarea != null) {
			LocalDateTime ldtNow = LocalDateTime.now();
			UsuarioEntity objUsuarioCreacion = sessionService.getCurrentUser();
			objTarea.setCreacionFecha(ldtNow);
			objTarea.setCreacionUsuario(objUsuarioCreacion);
			objTarea.setModificacionFecha(ldtNow);
			objTarea.setModificacionUsuario(objUsuarioCreacion);
			tareaRepository.save(objTarea);
			
			if(arrParticipantes != null) {
				addParticipantes(objTarea, arrParticipantes);
			}
		}
		else {
			throw new ApplicationException(EnumException.TAREAS_CREATE_001);
		}
	}
	
	@Transactional
	public void addParticipantes(TareaEntity objTarea, Integer[] arrParticipantes) {
		
		if(objTarea != null) {
			if(arrParticipantes != null) {
				
				for(int i = 0; i < arrParticipantes.length; i++) {
					
					TareaParticipanteEntity objParticipante = new TareaParticipanteEntity();
					UsuarioEntity objUsuario = usuarioService.findByIdUsuario(arrParticipantes[i]);
					
					objParticipante.setUsuario(objUsuario);
					objParticipante.setTarea(objTarea);
					tareaParticipanteService.create(objParticipante);
				}
				
			} else {
				throw new ApplicationException(EnumException.TAREAS_CREATE_002);
			}
		} else {
			throw new ApplicationException(EnumException.TAREAS_CREATE_001);
		}
	}
	
	@Transactional
	public void update(TareaEntity objTarea, Integer[] arrParticipantes) {
		
		if(objTarea != null) {
			LocalDateTime ldtNow = LocalDateTime.now();
			UsuarioEntity objUsuarioModificacion = sessionService.getCurrentUser();
			objTarea.setModificacionFecha(ldtNow);
			objTarea.setModificacionUsuario(objUsuarioModificacion);
			tareaRepository.save(objTarea);
			
			if(arrParticipantes != null) {
				updateParticipantes(objTarea, arrParticipantes);
			} else {
				updateParticipantes(objTarea);
			}
		}
		else {
			throw new ApplicationException(EnumException.TAREAS_UPDATE_001);
		}
	}
	
	@Transactional
	public void updateParticipantes(TareaEntity objTarea) {
		
		if(objTarea != null) {			
			tareaParticipanteService.removeParticipantes(objTarea.getIdTarea());
		} else {
			throw new ApplicationException(EnumException.TAREAS_UPDATE_001);
		}
	}
	
	@Transactional
	public void updateParticipantes(TareaEntity objTarea, Integer[] arrParticipantes) {
		
		if(objTarea != null) {
			if(arrParticipantes != null) {
				
				tareaParticipanteService.removeParticipantes(objTarea.getIdTarea());
				
				for(int i = 0; i < arrParticipantes.length; i++) {
					
					TareaParticipanteEntity objParticipante = new TareaParticipanteEntity();
					UsuarioEntity objUsuario = usuarioService.findByIdUsuario(arrParticipantes[i]);
					
					objParticipante.setUsuario(objUsuario);
					objParticipante.setTarea(objTarea);
					tareaParticipanteService.create(objParticipante);
				}
				
			} else {
				throw new ApplicationException(EnumException.TAREAS_PARTICIPANTES_UPDATE_002);
			}
		} else {
			throw new ApplicationException(EnumException.TAREAS_UPDATE_001);
		}
	}
}
