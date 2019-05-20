package com.ibmexico.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.ibmexico.entities.CotizacionTipoFicheroEntity;
import com.ibmexico.repositories.ICotizacionTipoFicheroRepository;

@Service("cotizacionTipoFicheroService")
public class CotizacionTipoFicheroService {

	@Autowired
	@Qualifier("cotizacionTipoFicheroRepository")
	private ICotizacionTipoFicheroRepository cotizacionTipoFicheroRepository;
	
	public List<CotizacionTipoFicheroEntity> listAll() {
		return cotizacionTipoFicheroRepository.findAll();
	}
	
	public CotizacionTipoFicheroEntity findByIdCotizacionTipoFichero(int idCotizacionTipoFichero) {
		return cotizacionTipoFicheroRepository.findByIdCotizacionTipoFichero(idCotizacionTipoFichero);
	}
}
