package com.ibmexico.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.ibmexico.libraries.notifications.ApplicationException;
import com.ibmexico.libraries.notifications.EnumException;
import com.ibmexico.entities.CotizacionEntity;
import com.ibmexico.entities.CotizacionUsuarioQuotaEntity;
import com.ibmexico.entities.EmpresaEntity;
import com.ibmexico.entities.UsuarioEntity;
import com.ibmexico.repositories.ICotizacionUsuarioQuotaRepository;

@Service("cotizacionUsuarioQuotaService")
public class CotizacionUsuarioQuotaService {
	
	@Autowired
	@Qualifier("cotizacionUsuarioQuotaRepository")
	private ICotizacionUsuarioQuotaRepository cotizacionUsuarioQuotaRepository;
	
	@Autowired
	@Qualifier("cotizacionFicheroService")
	private CotizacionFicheroService cotizacionFicheroService;
	
	@Autowired
	@Qualifier("configuracionService")
	private ConfiguracionService configuracionService;
	
	@Autowired
	@Qualifier("sessionService")
	private SessionService sessionService;
	
	public CotizacionUsuarioQuotaEntity findByIdCotizacionUsuarioQuota(int idCotizacionUsuarioQuota) {
		return cotizacionUsuarioQuotaRepository.findByIdCotizacionUsuarioQuota(idCotizacionUsuarioQuota);
	}
	
	public CotizacionUsuarioQuotaEntity findByCotizacion(CotizacionEntity objCotizacion) {
		return cotizacionUsuarioQuotaRepository.findByCotizacion_IdCotizacion(objCotizacion.getIdCotizacion());
	}
	
	public List<CotizacionUsuarioQuotaEntity> listCotizacionQuotas(CotizacionEntity objCotizacion) {
		return cotizacionUsuarioQuotaRepository.listCotizacionQuotas(objCotizacion.getIdCotizacion());
	}
	
	public List<CotizacionUsuarioQuotaEntity> listUsuarioQuotas(int idUsuario) {
		return cotizacionUsuarioQuotaRepository.listUsuarioQuotas(idUsuario);
	}
	
	public List<CotizacionUsuarioQuotaEntity> listUsuarioQuotasPeriodo(int idUsuario, LocalDate ldFechaInicio, LocalDate ldFechaFin) {
		return cotizacionUsuarioQuotaRepository.listUsuarioQuotasPeriodo(idUsuario, ldFechaInicio, ldFechaFin);
	}
	
	public List<CotizacionUsuarioQuotaEntity> listUsuarioQuotasPeriodo(LocalDate ldFechaInicio, LocalDate ldFechaFin) {
		return cotizacionUsuarioQuotaRepository.listUsuarioQuotasPeriodo(sessionService.getCurrentUser().getIdUsuario(), ldFechaInicio, ldFechaFin);
	}
	
	/*
	 * CUOTA DEL USUARIO OBTENIDA EN UN PERIODO
	 */
	public BigDecimal totalUsuarioQuotaPeriodo(LocalDate ldFechaInicio, LocalDate ldFechaFin) {				
		return cotizacionUsuarioQuotaRepository.sumUsuarioQuotaPeriodo(sessionService.getCurrentUser().getIdUsuario(), ldFechaInicio, ldFechaFin);				
	}
	
	public BigDecimal totalUsuarioQuotaPeriodo(UsuarioEntity objUsuario, LocalDate ldFechaInicio, LocalDate ldFechaFin) {				
		return cotizacionUsuarioQuotaRepository.sumUsuarioQuotaPeriodo(objUsuario.getIdUsuario(), ldFechaInicio, ldFechaFin);				
	}
	
	public BigDecimal totalUsuarioQuotaPeriodo(UsuarioEntity objUsuario, LocalDate ldFechaInicio, LocalDate ldFechaFin, EmpresaEntity objEmpresa) {				
		return cotizacionUsuarioQuotaRepository.sumUsuarioQuotaPeriodo(objUsuario.getIdUsuario(), ldFechaInicio, ldFechaFin, objEmpresa.getIdEmpresa());				
	}
	
	/*
	 * TOTAL DE CUOTA DE LA COTIZACION DEL USUARIO EN UN PERIODO
	 */
	public BigDecimal totalUsuarioQuotaCotizacionPeriodo(CotizacionEntity objCotizacion, LocalDate ldFechaInicio, LocalDate ldFechaFin) {				
		return cotizacionUsuarioQuotaRepository.sumUsuarioQuotaCotizacionPeriodo(sessionService.getCurrentUser().getIdUsuario(), objCotizacion.getIdCotizacion(), ldFechaInicio, ldFechaFin);				
	}
	
	public BigDecimal totalUsuarioQuotaCotizacionPeriodo(UsuarioEntity objUsuario, CotizacionEntity objCotizacion, LocalDate ldFechaInicio, LocalDate ldFechaFin) {				
		return cotizacionUsuarioQuotaRepository.sumUsuarioQuotaCotizacionPeriodo(objUsuario.getIdUsuario(), objCotizacion.getIdCotizacion(), ldFechaInicio, ldFechaFin);				
	}
	
	public BigDecimal totalUsuarioQuotaPeriodo(int idUsuario, LocalDate ldFechaInicio, LocalDate ldFechaFin) {				
		return cotizacionUsuarioQuotaRepository.sumUsuarioQuotaPeriodo(idUsuario, ldFechaInicio, ldFechaFin);				
	}
		
	@Transactional
	public void cargarQuota(CotizacionEntity objCotizacion) {
		
		if(objCotizacion != null) {
			List<CotizacionUsuarioQuotaEntity> lstCuotas = listCotizacionQuotas(objCotizacion);
				
			if(!lstCuotas.isEmpty()) {						
				eliminarQuota(objCotizacion);													
			}
			
			/* CALCULO DE LA UTILIDAD BRUTA */
			BigDecimal gastos = cotizacionFicheroService.totalImporteDocumentoTipo(objCotizacion.getIdCotizacion(), 3);
			BigDecimal compras = cotizacionFicheroService.totalImporteDocumentoTipo(objCotizacion.getIdCotizacion(), 4);

			BigDecimal utilidadBruta = objCotizacion.getSubtotal().subtract(gastos).subtract(compras);
			
			if(objCotizacion.isImplementacion()) {
				
				//QUOTA DE IMPLEMENTADOR
				CotizacionUsuarioQuotaEntity objQuotaImplementador = new CotizacionUsuarioQuotaEntity();
				objQuotaImplementador.setCotizacion(objCotizacion);
				objQuotaImplementador.setUsuario(objCotizacion.getUsuarioImplementador());
				objQuotaImplementador.setValorQuota(utilidadBruta.divide(new BigDecimal(100)).multiply((BigDecimal) configuracionService.getValue("QUOTA_IMPLEMENTADOR")));
				create(objQuotaImplementador);
			} else {
				
				//QUOTA DE EJECUTIVO
				CotizacionUsuarioQuotaEntity objQuotaEjecutivo = new CotizacionUsuarioQuotaEntity();
				objQuotaEjecutivo.setCotizacion(objCotizacion);
				
				
				//SELECCIÓN DE EJECUTIVO POR EMPRESA
				switch (objCotizacion.getEmpresa().getIdEmpresa()) {
				case 1:
					objQuotaEjecutivo.setUsuario(objCotizacion.getCliente().getUsuarioEjecutivo());
					break;
					
				case 2:
					objQuotaEjecutivo.setUsuario(objCotizacion.getCliente().getUsuarioEjecutivoS3s());
					break;
					
				case 3:
					objQuotaEjecutivo.setUsuario(objCotizacion.getCliente().getUsuarioEjecutivoR2a());
					break;

				default:
					break;
				}
											
				objQuotaEjecutivo.setValorQuota(utilidadBruta.divide(new BigDecimal(100)).multiply((BigDecimal) configuracionService.getValue("QUOTA_EJECUTIVO")));
				create(objQuotaEjecutivo);
			}
			
			if(objCotizacion.isImplementacion()){
				//QUOTA DE COTIZANTE
				CotizacionUsuarioQuotaEntity objQuotaCotizador = new CotizacionUsuarioQuotaEntity();
				objQuotaCotizador.setCotizacion(objCotizacion);
				objQuotaCotizador.setUsuario(objCotizacion.getUsuario());
				objQuotaCotizador.setValorQuota(utilidadBruta.divide(new BigDecimal(100)).multiply((BigDecimal) configuracionService.getValue("QUOTA_COTIZADOR_IMPL")));
				create(objQuotaCotizador);
			}else{
				//QUOTA DE COTIZANTE
				CotizacionUsuarioQuotaEntity objQuotaCotizador = new CotizacionUsuarioQuotaEntity();
				objQuotaCotizador.setCotizacion(objCotizacion);
				objQuotaCotizador.setUsuario(objCotizacion.getUsuario());
				objQuotaCotizador.setValorQuota(utilidadBruta.divide(new BigDecimal(100)).multiply((BigDecimal) configuracionService.getValue("QUOTA_COTIZADOR")));
				create(objQuotaCotizador);
			}
			
			
			//QUOTA DE VENDEDOR
			CotizacionUsuarioQuotaEntity objQuotaVendedor = new CotizacionUsuarioQuotaEntity();
			objQuotaVendedor.setCotizacion(objCotizacion);
			objQuotaVendedor.setUsuario(objCotizacion.getUsuarioVendedor());
			objQuotaVendedor.setValorQuota(utilidadBruta.divide(new BigDecimal(100)).multiply((BigDecimal) configuracionService.getValue("QUOTA_VENDEDOR")));
			create(objQuotaVendedor);
		}
		else {
			throw new ApplicationException(EnumException.COTIZACION_QUOTA_CREATE_002);
		}
	}
	
	@Transactional
	public void eliminarQuota(CotizacionEntity objCotizacion) {
		
		if(objCotizacion != null) {
			cotizacionUsuarioQuotaRepository.removeCotizacionCuota(objCotizacion.getIdCotizacion());		
		}
		else {
			throw new ApplicationException(EnumException.COTIZACION_QUOTA_DELETE_001);
		}
	}
	
	@Transactional
	public void create(CotizacionUsuarioQuotaEntity objQuota) {
		
		if(objQuota != null) {
									
			LocalDateTime ldtNow = LocalDateTime.now();
			UsuarioEntity objUsuarioCreacion = sessionService.getCurrentUser();
			objQuota.setCreacionFecha(ldtNow);
			objQuota.setCreacionUsuario(objUsuarioCreacion);
			cotizacionUsuarioQuotaRepository.save(objQuota);
		}
		else {
			throw new ApplicationException(EnumException.COTIZACION_QUOTA_CREATE_001);
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
