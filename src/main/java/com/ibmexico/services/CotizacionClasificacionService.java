package com.ibmexico.services;

import java.util.List;

import com.ibmexico.entities.CotizacionClasificacionEntity;
import com.ibmexico.repositories.ICotizacionClasificacionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("cotizacionClasificacionService")
public class CotizacionClasificacionService {
	
	@Autowired
	@Qualifier("cotizacionClasificacionRepository")
	private ICotizacionClasificacionRepository cotizacionClasificacionRepository;
	
	public List<CotizacionClasificacionEntity> getlistAll() {
		return cotizacionClasificacionRepository.findAllByOrderByOrdenAsc();
	}
	
	public CotizacionClasificacionEntity findByIdCotizacionClasificacion(int idCotizacionClasificacion) {
		return cotizacionClasificacionRepository.findByIdCotizacionClasificacion(idCotizacionClasificacion);
	}

}