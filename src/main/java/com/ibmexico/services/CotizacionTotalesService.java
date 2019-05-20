package com.ibmexico.services;

import java.math.BigDecimal;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.ibmexico.libraries.notifications.ApplicationException;
import com.ibmexico.libraries.notifications.EnumException;
import com.ibmexico.entities.CotizacionEntity;
import com.ibmexico.repositories.ICotizacionPartidaRepository;
import com.ibmexico.repositories.ICotizacionRepository;

@Service("cotizacionTotalesService")
public class CotizacionTotalesService {
	
	@Autowired
	@Qualifier("cotizacionService")
	private CotizacionService cotizacionService;
	
	@Autowired
	@Qualifier("configuracionService")
	private ConfiguracionService configuracionService;
	
	@Autowired
	@Qualifier("cotizacionRepository")
	private ICotizacionRepository cotizacionRepository;
	
	@Autowired
	@Qualifier("cotizacionPartidaRepository")
	private ICotizacionPartidaRepository cotizacionPartidaRepository;
	
	@Transactional
	public void calcularPartidasTotales(CotizacionEntity objCotizacion) {
		
		try {
			if(objCotizacion != null) {
				
				BigDecimal subtotal = cotizacionPartidaRepository.sumTotalCotizacionPartidas(objCotizacion.getIdCotizacion());
				BigDecimal iva = configuracionService.getSmartIva(subtotal);
				BigDecimal ivaPorcentaje = configuracionService.getSmartPorcentajeIva();
				BigDecimal total = configuracionService.getSmartTotal(subtotal);
				
				objCotizacion.setSubtotal(subtotal);
				objCotizacion.setIva(iva);
				objCotizacion.setIvaPorcentaje(ivaPorcentaje);
				objCotizacion.setTotal(total);
				cotizacionRepository.save(objCotizacion);	
				
			} else {
				throw new ApplicationException(EnumException.COTIZACIONES_CALCULAR_002);
			}			
		} catch(Exception exception) {
			throw new ApplicationException(EnumException.COTIZACIONES_CALCULAR_001);
		}
	}
	
	@Transactional
	public void calcularTotales(CotizacionEntity objCotizacion) {
		calcularPartidasTotales(objCotizacion);
	}
}
