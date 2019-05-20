package com.ibmexico.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.ibmexico.entities.CotizacionEstatusEntity;
import com.ibmexico.repositories.ICotizacionEstatusRepository;

@Service("cotizacionEstatusService")
public class CotizacionEstatusService {
	
	@Autowired
	@Qualifier("cotizacionEstatusRepository")
	private ICotizacionEstatusRepository cotizacionEstatusRepository;
	
	public List<CotizacionEstatusEntity> listAll() {
		return cotizacionEstatusRepository.findAll();
	}
	
	public CotizacionEstatusEntity findByIdCotizacionEstatus(int idCotizacionEstatus) {
		return cotizacionEstatusRepository.findByIdCotizacionEstatus(idCotizacionEstatus);
	}

}
