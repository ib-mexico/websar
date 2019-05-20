package com.ibmexico.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.ibmexico.libraries.notifications.ApplicationException;
import com.ibmexico.libraries.notifications.EnumException;
import com.ibmexico.entities.OrdenServicioEntity;
import com.ibmexico.entities.OrdenServicioPartidaEntity;
import com.ibmexico.entities.UsuarioEntity;
import com.ibmexico.repositories.IOrdenServicioPartidaRepository;

@Service("ordenServicioPartidaService")
public class OrdenServicioPartidaService {

	@Autowired
	@Qualifier("ordenServicioPartidaRepository")
	private IOrdenServicioPartidaRepository ordenServicioPartidaRepository;
	
	@Autowired
	@Qualifier("sessionService")
	private SessionService sessionService;
	
	public List<OrdenServicioPartidaEntity> listOrdenServicioPartidas(int idOrdenServicio) {
		return ordenServicioPartidaRepository.findOrdenServicioPartidas(idOrdenServicio);
	}
	
	@Transactional
	public BigDecimal calcularSubotales(OrdenServicioEntity objOrdenServicio) {
		return ordenServicioPartidaRepository.sumOrdenServicioPartidas(objOrdenServicio.getIdOrdenServicio());
	}
	
	@Transactional
	public void create(OrdenServicioPartidaEntity objPartida) {
		
		if(objPartida != null) {
			LocalDateTime ldtNow = LocalDateTime.now();
			UsuarioEntity objUsuarioCreacion = sessionService.getCurrentUser();
			objPartida.setCreacionFecha(ldtNow);
			objPartida.setCreacionUsuario(objUsuarioCreacion);
			objPartida.setModificacionFecha(ldtNow);
			objPartida.setModificacionUsuario(objUsuarioCreacion);
			ordenServicioPartidaRepository.save(objPartida);
		}
		else {
			throw new ApplicationException(EnumException.COTIZACIONES_ORDENES_SERVICIOS_CREATE_003);
		}
	}
	
	@Transactional
	public void deleteAll(OrdenServicioEntity objOrdenServicio) {
		ordenServicioPartidaRepository.removeOrdenServicioPartidas(objOrdenServicio.getIdOrdenServicio());
	}
}
