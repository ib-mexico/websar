package com.ibmexico.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.ibmexico.libraries.notifications.ApplicationException;
import com.ibmexico.libraries.notifications.EnumException;
import com.ibmexico.entities.CotizacionComisionEntity;
import com.ibmexico.entities.CotizacionEntity;
import com.ibmexico.entities.UsuarioEntity;
import com.ibmexico.repositories.ICotizacionComisionRepository;

@Service("cotizacionComisionService")
public class CotizacionComisionService {
	
	@Autowired
	@Qualifier("cotizacionComisionRepository")
	private ICotizacionComisionRepository cotizacionComisionRepository;
	
	@Autowired
	@Qualifier("cotizacionUsuarioQuotaService")
	private CotizacionUsuarioQuotaService cotizacionUsuarioQuotaService;
	
	@Autowired
	@Qualifier("cotizacionFicheroService")
	private CotizacionFicheroService cotizacionFicheroService;
	
	@Autowired
	@Qualifier("configuracionService")
	private ConfiguracionService configuracionService;
	
	@Autowired
	@Qualifier("sessionService")
	private SessionService sessionService;
	
	public CotizacionComisionEntity findByIdCotizacionComision(int idCotizacionComision) {
		return cotizacionComisionRepository.findByIdCotizacionComision(idCotizacionComision);
	}
	
	public CotizacionComisionEntity findByCotizacion(CotizacionEntity objCotizacion) {
		return cotizacionComisionRepository.findByCotizacion_IdCotizacion(objCotizacion.getIdCotizacion());
	}
	
	public List<CotizacionComisionEntity> listComisionesUsuarioPeriodo(LocalDate ldFechaInicio, LocalDate ldFechaFin) {
		return cotizacionComisionRepository.listComisionesUsuarioPeriodo(sessionService.getCurrentUser().getIdUsuario(), ldFechaInicio, ldFechaFin);
	}
	
	public CotizacionComisionEntity comisionUsuarioCotizacionPeriodo(UsuarioEntity objUsuario, CotizacionEntity objCotizacion, LocalDate ldFechaInicio, LocalDate ldFechaFin) {
		return cotizacionComisionRepository.comisionUsuarioCotizacionPeriodo(objUsuario.getIdUsuario(), objCotizacion.getIdCotizacion(), ldFechaInicio, ldFechaFin);
	}
	
	public CotizacionComisionEntity comisionUsuarioCotizacionPagadaPeriodo(UsuarioEntity objUsuario, CotizacionEntity objCotizacion, LocalDate ldFechaInicio, LocalDate ldFechaFin) {
		return cotizacionComisionRepository.comisionUsuarioCotizacionPagadaPeriodo(objUsuario.getIdUsuario(), objCotizacion.getIdCotizacion(), ldFechaInicio, ldFechaFin);
	}
	
	public CotizacionComisionEntity comisionUsuarioCotizacionCobradaPeriodo(UsuarioEntity objUsuario, CotizacionEntity objCotizacion, LocalDate ldFechaInicio, LocalDate ldFechaFin) {
		return cotizacionComisionRepository.comisionUsuarioCotizacionCobradaPeriodo(objUsuario.getIdUsuario(), objCotizacion.getIdCotizacion(), ldFechaInicio, ldFechaFin);
	}
	
	@Transactional
	public void eliminarComision(CotizacionEntity objCotizacion) {
		cotizacionComisionRepository.removeCotizacionComision(objCotizacion.getIdCotizacion());
	}
	
	@Transactional
	public void create(CotizacionComisionEntity objComision, CotizacionEntity objCotizacion) {
		
		if(objComision != null) {
			
			/* CALCULO DE LA UTILIDAD BRUTA */
			BigDecimal gastos = cotizacionFicheroService.totalImporteDocumentoTipo(objCotizacion.getIdCotizacion(), 3);
			BigDecimal compras = cotizacionFicheroService.totalImporteDocumentoTipo(objCotizacion.getIdCotizacion(), 4);

			BigDecimal utilidadBruta = objCotizacion.getSubtotal().subtract(gastos).subtract(compras);
			objComision.setUtilidadBruta(utilidadBruta);
			
			/* CALCULO DEL 10% DE COMISION DE LA UTILIDAD BRUTA */
			BigDecimal comisionTotal = utilidadBruta.divide(new BigDecimal(100)).multiply(new BigDecimal(10));
			
			if( objCotizacion.getInicioCobranzaFecha() == null) {				
				/*CALCULO DE COMISION DEL EJECUTIVO O IMPLEMENTADOR */
				if(objCotizacion.isImplementacion()) {
					objComision.setUsuarioImplementador(objCotizacion.getUsuarioImplementador());
					objComision.setPorcentajeImplementador((BigDecimal) configuracionService.getValue("COMISION_PORCENTAJE_IMPLEMENTADOR"));
					objComision.setComisionImplementador(comisionTotal.divide(new BigDecimal(100)).multiply((BigDecimal) configuracionService.getValue("COMISION_PORCENTAJE_IMPLEMENTADOR")));
					
					objComision.setPorcentajeEjecutivo(new BigDecimal(0));
					objComision.setComisionEjecutivo(new BigDecimal(0));								
					
				} else {
					//SELECCIÓN DE EJECUTIVO POR EMPRESA
					switch (objCotizacion.getEmpresa().getIdEmpresa()) {
					case 1:
						objComision.setUsuarioEjecutivo(objCotizacion.getCliente().getUsuarioEjecutivo());
						break;
						
					case 2:
						objComision.setUsuarioEjecutivo(objCotizacion.getCliente().getUsuarioEjecutivoS3s());
						break;
						
					case 3:
						objComision.setUsuarioEjecutivo(objCotizacion.getCliente().getUsuarioEjecutivoR2a());
						break;
						
					default:
						break;
					}
					
					objComision.setPorcentajeEjecutivo((BigDecimal) configuracionService.getValue("COMISION_PORCENTAJE_EJECUTIVO"));
					objComision.setComisionEjecutivo(comisionTotal.divide(new BigDecimal(100)).multiply((BigDecimal) configuracionService.getValue("COMISION_PORCENTAJE_EJECUTIVO")));
					
					objComision.setPorcentajeImplementador(new BigDecimal(0));
					objComision.setComisionImplementador(new BigDecimal(0));								
				}
				
				
				/*CALCULO DE COMISION DEL COTIZANTE */
				objComision.setUsuarioCotizante(objCotizacion.getUsuario());
				objComision.setPorcentajeCotizante((BigDecimal) configuracionService.getValue("COMISION_PORCENTAJE_COTIZADOR"));
				objComision.setComisionCotizante(comisionTotal.divide(new BigDecimal(100)).multiply((BigDecimal) configuracionService.getValue("COMISION_PORCENTAJE_COTIZADOR")));									
				
				
				
				/*CALCULO DE COMISION DEL VENDEDOR */
				objComision.setUsuarioVendedor(objCotizacion.getUsuarioVendedor());
				objComision.setPorcentajeVendedor((BigDecimal) configuracionService.getValue("COMISION_PORCENTAJE_VENDEDOR"));
				objComision.setComisionVendedor(comisionTotal.divide(new BigDecimal(100)).multiply((BigDecimal) configuracionService.getValue("COMISION_PORCENTAJE_VENDEDOR")));
				
				
				/* CALCULO DE COMISION DE COBRANZA */
				objComision.setPorcentajeCobranza(new BigDecimal(0));
				objComision.setComisionCobranza(new BigDecimal(0));		
				
				
				/* TOTAL DE COMISIONES */
				objComision.setTotalComisiones(objComision.getComisionEjecutivo().add(objComision.getComisionCotizante()).add(objComision.getComisionVendedor()).add(objComision.getComisionImplementador()));
			
			} else {
				
				//COMISION EJECUTIVO
				objComision.setPorcentajeEjecutivo(new BigDecimal(0));
				objComision.setComisionEjecutivo(new BigDecimal(0));
				
				//COMISION COTIZANTE
				objComision.setUsuarioCotizante(objCotizacion.getUsuario());
				objComision.setPorcentajeCotizante(new BigDecimal(0));
				objComision.setComisionCotizante(new BigDecimal(0));
				
				//COMISION VENDEDOR
				objComision.setUsuarioVendedor(objCotizacion.getUsuarioVendedor());
				objComision.setPorcentajeVendedor(new BigDecimal(0));
				objComision.setComisionVendedor(new BigDecimal(0));
				
				//COMISION IMPLEMENTADOR
				objComision.setPorcentajeImplementador(new BigDecimal(0));
				objComision.setComisionImplementador(new BigDecimal(0));
				
				//COMISION COBRANZA
				objComision.setUsuarioCobranza(sessionService.getCurrentUser());
				
				if(objCotizacion.getFormaPago().getFormaPago().equals("Credito")) {					
					int diff = (int) ChronoUnit.DAYS.between(objCotizacion.getInicioCobranzaFecha(), objCotizacion.getPagoFecha());
					if(diff <= 30) {
						objComision.setPorcentajeCobranza((BigDecimal) configuracionService.getValue("COMISION_PROCENTAJE_COBRANZA_30_DIAS"));
						objComision.setComisionCobranza(comisionTotal.divide(new BigDecimal(100)).multiply((BigDecimal) configuracionService.getValue("COMISION_PROCENTAJE_COBRANZA_30_DIAS")));
					} else if(diff <= 60) {
						objComision.setPorcentajeCobranza((BigDecimal) configuracionService.getValue("COMISION_PROCENTAJE_COBRANZA_60_DIAS"));
						objComision.setComisionCobranza(comisionTotal.divide(new BigDecimal(100)).multiply((BigDecimal) configuracionService.getValue("COMISION_PROCENTAJE_COBRANZA_60_DIAS")));
					} else if(diff <= 90) {
						objComision.setPorcentajeCobranza((BigDecimal) configuracionService.getValue("COMISION_PROCENTAJE_COBRANZA_90_DIAS"));
						objComision.setComisionCobranza(comisionTotal.divide(new BigDecimal(100)).multiply((BigDecimal) configuracionService.getValue("COMISION_PROCENTAJE_COBRANZA_90_DIAS")));
					} else {
						objComision.setPorcentajeCobranza(new BigDecimal(0));
						objComision.setComisionCobranza(new BigDecimal(0));
					}
				} else {
					objComision.setPorcentajeCobranza(new BigDecimal(0));
					objComision.setComisionCobranza(new BigDecimal(0));
				}
				
				/* TOTAL DE COMISIONES */
				objComision.setTotalComisiones(objComision.getComisionCobranza());
				
			}
			
			LocalDateTime ldtNow = LocalDateTime.now();
			UsuarioEntity objUsuarioCreacion = sessionService.getCurrentUser();
			objComision.setCreacionFecha(ldtNow);
			objComision.setCreacionUsuario(objUsuarioCreacion);
			objComision.setModificacionFecha(ldtNow);
			objComision.setModificacionUsuario(objUsuarioCreacion);
			cotizacionComisionRepository.save(objComision);
		}
		else {
			throw new ApplicationException(EnumException.COTIZACION_COMISION_CREATE_001);
		}
	}
	
	/*public void update(ClienteContactoEntity objContacto) {
		
		if(objContacto != null) {
			LocalDateTime ldtNow = LocalDateTime.now();
			UsuarioEntity objUsuarioModificacion = sessionService.getCurrentUser();
			objContacto.setModificacionFecha(ldtNow);
			objContacto.setModificacionUsuario(objUsuarioModificacion);
			clienteContactoRepository.save(objContacto);						
		}
		else {
			throw new ApplicationException(EnumException.CLIENTES_CONTACTOS_UPDATE_001);
		}
	}*/

}
