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
import com.ibmexico.entities.CotizacionPartidaEntity;
import com.ibmexico.entities.UsuarioEntity;
import com.ibmexico.libraries.DataTable;
import com.ibmexico.repositories.ICotizacionPartidaRepository;

@Service("cotizacionPartidaService")
public class CotizacionPartidaService {

	@Autowired
	@Qualifier("cotizacionPartidaRepository")
	private ICotizacionPartidaRepository cotizacionPartidaRepository;
	
	@Autowired
	@Qualifier("cotizacionTotalesService")
	private CotizacionTotalesService cotizacionTotalesService;
	
	@Autowired
	@Qualifier("sessionService")
	private SessionService sessionService;
	
	public DataTable<CotizacionPartidaEntity> dataTable(int idCotizacion, String search, int offset, int limit, String txtBootstrapTableDesde, String txtBootstrapTableHasta) {
		List<CotizacionPartidaEntity> lstCotizacionPartidaEntity = null;
		long totalPartidas = 100;
		
		if(search != null) {			
			lstCotizacionPartidaEntity = cotizacionPartidaRepository.findForDataTable(idCotizacion, search, DataTable.getPageRequest(offset, limit));
			totalPartidas = cotizacionPartidaRepository.countForDataTable(idCotizacion, search);
		} else {
			lstCotizacionPartidaEntity = cotizacionPartidaRepository.findForDataTable(idCotizacion, DataTable.getPageRequest(offset, limit));
			totalPartidas = cotizacionPartidaRepository.countForDataTable(idCotizacion);
		}

		DataTable<CotizacionPartidaEntity> returnDataTable = new DataTable<CotizacionPartidaEntity>(lstCotizacionPartidaEntity, totalPartidas);
		return returnDataTable;
	}
	
	public CotizacionPartidaEntity findByIdCotizacionPartida(int idCotizacionPartida) {
		return cotizacionPartidaRepository.findByIdCotizacionPartida(idCotizacionPartida);
	}
	
	public List<CotizacionPartidaEntity> listCotizacionesPartidas(int idCotizacion) {
		return cotizacionPartidaRepository.findForDataTable(idCotizacion, null);
	}
	
	@Transactional
	public void create(CotizacionPartidaEntity objPartida) {
		
		if(objPartida != null) {
			LocalDateTime ldtNow = LocalDateTime.now();
			UsuarioEntity objUsuarioCreacion = sessionService.getCurrentUser();
			BigDecimal descuentoPorcentaje = objPartida.getDescuentoPorcentaje().divide(new BigDecimal(100));
			BigDecimal descuentoValor = objPartida.getPrecioUnitarioLista().multiply(descuentoPorcentaje);
			
			BigDecimal subtotal = objPartida.getPrecioUnitarioLista().subtract(descuentoValor);
			
			BigDecimal cantidad = new BigDecimal(objPartida.getCantidad());
			BigDecimal total = subtotal.multiply(cantidad);
			
			objPartida.setTotal(total);
			objPartida.setCreacionFecha(ldtNow);
			objPartida.setCreacionUsuario(objUsuarioCreacion);
			objPartida.setModificacionFecha(ldtNow);
			objPartida.setModificacionUsuario(objUsuarioCreacion);
			cotizacionPartidaRepository.save(objPartida);
			
			cotizacionTotalesService.calcularTotales(objPartida.getCotizacion());
		}
		else {
			throw new ApplicationException(EnumException.COTIZACIONES_PARTIDAS_CREATE_001);
		}
	}
	
	@Transactional
	public void update(CotizacionPartidaEntity objPartida) {
		
		if(objPartida != null) {
			LocalDateTime ldtNow = LocalDateTime.now();
			UsuarioEntity objUsuarioModificacion = sessionService.getCurrentUser();
			/*BigDecimal divisor = new BigDecimal(100);
			BigDecimal cantidad = new BigDecimal(objPartida.getCantidad());
			BigDecimal total = (objPartida.getPrecioUnitarioLista().subtract(((objPartida.getDescuentoPorcentaje().divide(divisor))).subtract(objPartida.getPrecioUnitarioLista())).multiply(cantidad));
			*/
			
			BigDecimal descuentoPorcentaje = objPartida.getDescuentoPorcentaje().divide(new BigDecimal(100));
			BigDecimal descuentoValor = objPartida.getPrecioUnitarioLista().multiply(descuentoPorcentaje);
			
			BigDecimal subtotal = objPartida.getPrecioUnitarioLista().subtract(descuentoValor);
			
			BigDecimal cantidad = new BigDecimal(objPartida.getCantidad());
			BigDecimal total = subtotal.multiply(cantidad);
			
			objPartida.setTotal(total);
						
			objPartida.setModificacionFecha(ldtNow);
			objPartida.setModificacionUsuario(objUsuarioModificacion);
			cotizacionPartidaRepository.save(objPartida);
			
			cotizacionTotalesService.calcularTotales(objPartida.getCotizacion());
		}
		else {
			throw new ApplicationException(EnumException.COTIZACIONES_PARTIDAS_UPDATE_002);
		}
	}
	
	public void delete(CotizacionPartidaEntity objPartida) {
		if(objPartida != null) {
			cotizacionPartidaRepository.delete(objPartida);
		}
		else {
			throw new ApplicationException(EnumException.COTIZACIONES_PARTIDAS_DELETE_001);
		}
	}
}
