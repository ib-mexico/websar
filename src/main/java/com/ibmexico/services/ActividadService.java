package com.ibmexico.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.ibmexico.libraries.notifications.ApplicationException;
import com.ibmexico.libraries.notifications.EnumException;
import com.ibmexico.entities.ActividadEntity;
import com.ibmexico.entities.UsuarioEntity;
import com.ibmexico.repositories.IActividadRepository;

@Service("actividadService")
public class ActividadService {

	@Autowired
	@Qualifier("actividadRepository")
	private IActividadRepository actividadRepository;
	
	@Autowired
	@Qualifier("sessionService")
	private SessionService sessionService;
	
	public ActividadEntity findByIdActividad(int idActividad) {
		return actividadRepository.findByIdActividad(idActividad);
	}
	
	public List<ActividadEntity> listActividadesNoFinalizadas(int idUsuario) {
		return actividadRepository.listActividadesNoFinalizadas(idUsuario);
	}
	
	public List<ActividadEntity> listActividades() {
		
		List<ActividadEntity> lstActividades = null;
		
		if(sessionService.hasRol("OPORTUNIDADES_ADMINISTRADOR")) {
			lstActividades = actividadRepository.listActividades();
		}
		else {
			lstActividades = actividadRepository.listActividades(sessionService.getCurrentUser().getIdUsuario());
		}
		
		return lstActividades;
	}
	
	public void create(ActividadEntity objActividad) {
		
		if(objActividad != null) {
			LocalDateTime ldtNow = LocalDateTime.now();
			UsuarioEntity objUsuarioCreacion = sessionService.getCurrentUser();
			objActividad.setCreacionFecha(ldtNow);
			objActividad.setCreacionUsuario(objUsuarioCreacion);
			objActividad.setModificacionFecha(ldtNow);
			objActividad.setModificacionUsuario(objUsuarioCreacion);
			actividadRepository.save(objActividad);
		}
		else {
			throw new ApplicationException(EnumException.ACTIVIDADES_CREATE_001);
		}
	}
	
	public void update(ActividadEntity objActividad) {
		
		if(objActividad != null) {
			LocalDateTime ldtNow = LocalDateTime.now();
			UsuarioEntity objUsuarioModificacion = sessionService.getCurrentUser();
			objActividad.setModificacionFecha(ldtNow);
			objActividad.setModificacionUsuario(objUsuarioModificacion);
			actividadRepository.save(objActividad);
		}
		else {
			throw new ApplicationException(EnumException.ACTIVIDADES_UPDATE_001);
		}
	}
}
