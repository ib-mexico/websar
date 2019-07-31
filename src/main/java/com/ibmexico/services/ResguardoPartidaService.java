package com.ibmexico.services;

import java.time.LocalDateTime;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.ibmexico.libraries.notifications.ApplicationException;
import com.ibmexico.libraries.notifications.EnumException;
import com.ibmexico.entities.ResguardoEntity;
import com.ibmexico.entities.ResguardoPartidaEntity;
import com.ibmexico.entities.UsuarioEntity;
import com.ibmexico.repositories.IResguardoPartidaRepository;

@Service("resguardoPartidaService")
public class ResguardoPartidaService {

	@Autowired
	@Qualifier("resguardoPartidaRepository")
	private IResguardoPartidaRepository resguardoPartidaRepository;
	
	@Autowired
	@Qualifier("sessionService")
	private SessionService sessionService;
	
	public List<ResguardoPartidaEntity> listEntregaProductos(int idEntrega) {
		return resguardoPartidaRepository.findResguardoPartidas(idEntrega);
	}
	
	@Transactional
	public void create(ResguardoPartidaEntity objPartida) {
		
		if(objPartida != null) {
			LocalDateTime ldtNow = LocalDateTime.now();
			UsuarioEntity objUsuarioCreacion = sessionService.getCurrentUser();
			objPartida.setCreacionFecha(ldtNow);
			objPartida.setCreacionUsuario(objUsuarioCreacion);
			objPartida.setModificacionFecha(ldtNow);
			objPartida.setModificacionUsuario(objUsuarioCreacion);
			resguardoPartidaRepository.save(objPartida);
		}
		else {
			throw new ApplicationException(EnumException.RESGUARDOS_PARTIDAS_CREATE_001);
		}
	}
	
	@Transactional
	public void deleteAll(ResguardoEntity objResguardo) {
		resguardoPartidaRepository.removeResguardoPartidas(objResguardo.getIdResguardo());
	}
}
