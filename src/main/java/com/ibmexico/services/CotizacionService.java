package com.ibmexico.services;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.ibmexico.libraries.notifications.ApplicationException;
import com.ibmexico.libraries.notifications.EnumException;
import com.ibmexico.configurations.GeneralConfiguration;
import com.ibmexico.entities.CotizacionEntity;
import com.ibmexico.entities.CotizacionPartidaEntity;
import com.ibmexico.entities.EmpresaEntity;
import com.ibmexico.entities.UsuarioEntity;
import com.ibmexico.libraries.DataTable;
import com.ibmexico.repositories.ICotizacionRepository;

@Service("cotizacionService")
public class CotizacionService {

	@Autowired
	@Qualifier("cotizacionRepository")
	private ICotizacionRepository cotizacionRepository;
	
	@Autowired
	@Qualifier("cotizacionPartidaService")
	private CotizacionPartidaService cotizacionPartidaService;
	
	@Autowired
	@Qualifier("cotizacionFicheroService")
	private CotizacionFicheroService cotizacionFicheroService;
	
	@Autowired
	@Qualifier("sessionService")
	private SessionService sessionService;
	
	public DataTable<CotizacionEntity> dataTable(String search, int offset, int limit, String txtBootstrapTableDesde, String txtBootstrapTableHasta) {
		List<CotizacionEntity> lstCotizacionEntity = null;
		LocalDate fechaInicio = null;
		LocalDate fechaFin = null;
		long totalCotizaciones = 100;
		
		if(search != null) {	
			
			if(sessionService.hasRol("COTIZACIONES_ADMINISTRADOR")) {
				if(!txtBootstrapTableDesde.equals("") && !txtBootstrapTableHasta.equals("")) {
										
					String[] arrFechaInicio = txtBootstrapTableDesde.split("/");
					int yearInicio = Integer.parseInt(arrFechaInicio[2]);
					int monthInicio = Integer.parseInt(arrFechaInicio[1]);
					int dayInicio = Integer.parseInt(arrFechaInicio[0]);
					
					String[] arrFechaFin = txtBootstrapTableHasta.split("/");
					int yearFin = Integer.parseInt(arrFechaFin[2]);
					int monthFin = Integer.parseInt(arrFechaFin[1]);
					int dayFin = Integer.parseInt(arrFechaFin[0]);
					
					fechaInicio = LocalDate.of(yearInicio, monthInicio, dayInicio);
					fechaFin = LocalDate.of(yearFin, monthFin, dayFin);
					
					lstCotizacionEntity = cotizacionRepository.findForDataTable(search, fechaInicio, fechaFin, DataTable.getPageRequest(offset, limit));
					totalCotizaciones = cotizacionRepository.countForDataTable(search, fechaInicio, fechaFin);
				} else {					
					lstCotizacionEntity = cotizacionRepository.findForDataTable(search, DataTable.getPageRequest(offset, limit));
					totalCotizaciones = cotizacionRepository.countForDataTable(search);
				}
			} else {
				if(!txtBootstrapTableDesde.equals("") && !txtBootstrapTableHasta.equals("")) {
					
					String[] arrFechaInicio = txtBootstrapTableDesde.split("/");
					int yearInicio = Integer.parseInt(arrFechaInicio[2]);
					int monthInicio = Integer.parseInt(arrFechaInicio[1]);
					int dayInicio = Integer.parseInt(arrFechaInicio[0]);
					
					String[] arrFechaFin = txtBootstrapTableHasta.split("/");
					int yearFin = Integer.parseInt(arrFechaFin[2]);
					int monthFin = Integer.parseInt(arrFechaFin[1]);
					int dayFin = Integer.parseInt(arrFechaFin[0]);
					
					fechaInicio = LocalDate.of(yearInicio, monthInicio, dayInicio);
					fechaFin = LocalDate.of(yearFin, monthFin, dayFin);
					
					lstCotizacionEntity = cotizacionRepository.findForDataTable(sessionService.getCurrentUser().getIdUsuario() ,search, fechaInicio, fechaFin, DataTable.getPageRequest(offset, limit));
					totalCotizaciones = cotizacionRepository.countForDataTable(sessionService.getCurrentUser().getIdUsuario(), search, fechaInicio, fechaFin);
				} else {					
					lstCotizacionEntity = cotizacionRepository.findForDataTable(sessionService.getCurrentUser().getIdUsuario() ,search, DataTable.getPageRequest(offset, limit));
					totalCotizaciones = cotizacionRepository.countForDataTable(sessionService.getCurrentUser().getIdUsuario(), search);
				}
			}			
		} else {
			lstCotizacionEntity = cotizacionRepository.findForDataTable(DataTable.getPageRequest(offset, limit));
			totalCotizaciones = cotizacionRepository.countForDataTable();
		}

		DataTable<CotizacionEntity> returnDataTable = new DataTable<CotizacionEntity>(lstCotizacionEntity, totalCotizaciones);
		return returnDataTable;
	}
	
	public CotizacionEntity findByIdCotizacion(int idCotizacion) {
		return cotizacionRepository.findByIdCotizacion(idCotizacion);
	}
	
	public String sumCotizacionesTotalesPorFecha(LocalDate ldFechaInicial, LocalDate ldFechaFinal) {
		
		String total = "";
		
		if(sessionService.hasRol("COTIZACIONES_ADMINISTRADOR")) {
			total =  GeneralConfiguration.getInstance().getNumberFormat().format(cotizacionRepository.sumTotalCotizacionPorMes(ldFechaInicial, ldFechaFinal));
		} else {
			total =  GeneralConfiguration.getInstance().getNumberFormat().format(cotizacionRepository.sumTotalCotizacionPorMes(ldFechaInicial, ldFechaFinal, sessionService.getCurrentUser().getIdUsuario()));
		}
		
		return total;
	}
	
	public String countCotizacionesEstatusPeriodo(LocalDate ldFechaInicio, LocalDate ldFechaFin) {
		
		BigDecimal porcentajeActivos = null;
		BigDecimal porcentajeAprobados = null;
		BigDecimal porcentajeFacturados = null;
		BigDecimal porcentajePagados = null;
		BigDecimal porcentajeCancelados = null;
		
		if(sessionService.hasRol("COTIZACIONES_ADMINISTRADOR")) {
			
			long totalCotizaciones = cotizacionRepository.countCotizacionesPeriodo(ldFechaInicio, ldFechaFin);
			
			if(totalCotizaciones > 0) {
				
				porcentajeActivos =  new BigDecimal(cotizacionRepository.countCotizacionesEstatusPeriodo(1, ldFechaInicio, ldFechaFin)).divide(new BigDecimal(totalCotizaciones), 2, RoundingMode.HALF_UP);
				porcentajeAprobados =  new BigDecimal(cotizacionRepository.countCotizacionesEstatusPeriodo(2, ldFechaInicio, ldFechaFin)).divide(new BigDecimal(totalCotizaciones), 2, RoundingMode.HALF_UP);
				porcentajeFacturados =  new BigDecimal(cotizacionRepository.countCotizacionesEstatusPeriodo(3, ldFechaInicio, ldFechaFin)).divide(new BigDecimal(totalCotizaciones), 2, RoundingMode.HALF_UP);
				porcentajePagados =  new BigDecimal(cotizacionRepository.countCotizacionesEstatusPeriodo(4, ldFechaInicio, ldFechaFin)).divide(new BigDecimal(totalCotizaciones), 2, RoundingMode.HALF_UP);
				porcentajeCancelados =  new BigDecimal(cotizacionRepository.countCotizacionesEstatusPeriodo(5, ldFechaInicio, ldFechaFin)).divide(new BigDecimal(totalCotizaciones), 2, RoundingMode.HALF_UP);			
			} else {
				porcentajeActivos = new BigDecimal(0);
				porcentajeAprobados = new BigDecimal(0);
				porcentajeFacturados = new BigDecimal(0);
				porcentajePagados = new BigDecimal(0);
				porcentajeCancelados = new BigDecimal(0);
			}
			
		} else {
			
			long totalCotizaciones = cotizacionRepository.countCotizacionesPeriodo(sessionService.getCurrentUser().getIdUsuario(), ldFechaInicio, ldFechaFin);
			
			if(totalCotizaciones > 0 ) {
				
				porcentajeActivos =  new BigDecimal(cotizacionRepository.countCotizacionesEstatusPeriodo(1, ldFechaInicio, ldFechaFin, sessionService.getCurrentUser().getIdUsuario())).divide(new BigDecimal(totalCotizaciones), 2, RoundingMode.HALF_UP);
				porcentajeAprobados =  new BigDecimal(cotizacionRepository.countCotizacionesEstatusPeriodo(2, ldFechaInicio, ldFechaFin, sessionService.getCurrentUser().getIdUsuario())).divide(new BigDecimal(totalCotizaciones), 2, RoundingMode.HALF_UP);
				porcentajeFacturados =  new BigDecimal(cotizacionRepository.countCotizacionesEstatusPeriodo(3, ldFechaInicio, ldFechaFin, sessionService.getCurrentUser().getIdUsuario())).divide(new BigDecimal(totalCotizaciones), 2, RoundingMode.HALF_UP);
				porcentajePagados =  new BigDecimal(cotizacionRepository.countCotizacionesEstatusPeriodo(4, ldFechaInicio, ldFechaFin, sessionService.getCurrentUser().getIdUsuario())).divide(new BigDecimal(totalCotizaciones), 2, RoundingMode.HALF_UP);
				porcentajeCancelados =  new BigDecimal(cotizacionRepository.countCotizacionesEstatusPeriodo(5, ldFechaInicio, ldFechaFin, sessionService.getCurrentUser().getIdUsuario())).divide(new BigDecimal(totalCotizaciones), 2, RoundingMode.HALF_UP);			
			} else {
				porcentajeActivos = new BigDecimal(0);
				porcentajeAprobados = new BigDecimal(0);
				porcentajeFacturados = new BigDecimal(0);
				porcentajePagados = new BigDecimal(0);
				porcentajeCancelados = new BigDecimal(0);
			}
			
		}		
				
		JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
		jsonReturn	.add("porcentajeActivos", porcentajeActivos.multiply(new BigDecimal(100)).toString())
					.add("porcentajeAprobados", porcentajeAprobados.multiply(new BigDecimal(100)).toString())
					.add("porcentajeFacturados", porcentajeFacturados.multiply(new BigDecimal(100)).toString())
					.add("porcentajePagados", porcentajePagados.multiply(new BigDecimal(100)).toString())
					.add("porcentajeCancelados", porcentajeCancelados.multiply(new BigDecimal(100)).toString());
		
		return jsonReturn.build().toString();
	}
	
	public String sumCotizacionesVentasPeriodo(LocalDate ldFechaInicio, LocalDate ldFechaFin) {
		
		BigDecimal valorCotizaciones = null;
		BigDecimal valorCompras = null;
		BigDecimal valorGastos = null;
		
		List<CotizacionEntity> lstCotizaciones = cotizacionRepository.findCotizacionesPorMes(ldFechaInicio, ldFechaFin, sessionService.getCurrentUser().getIdUsuario());
		
		if(lstCotizaciones.size() > 0 ) {
			
			valorCotizaciones =  cotizacionRepository.sumTotalCotizacionPorMes(ldFechaInicio, ldFechaFin, sessionService.getCurrentUser().getIdUsuario());
			valorCompras = new BigDecimal(0);
			valorGastos = new BigDecimal(0);
			
			for(CotizacionEntity itemCotizacion : lstCotizaciones) {
				
				valorCompras = valorCompras.add(cotizacionFicheroService.totalImporteDocumentoTipo(itemCotizacion.getIdCotizacion(), 4));
				valorGastos = valorGastos.add(cotizacionFicheroService.totalImporteDocumentoTipo(itemCotizacion.getIdCotizacion(), 3));
			}
			
		} else {
			valorCotizaciones = new BigDecimal(0);
			valorCompras = new BigDecimal(0);
			valorGastos = new BigDecimal(0);
		}
				
				
		JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
		jsonReturn	.add("totalCotizacionesNatural", GeneralConfiguration.getInstance().getNumberFormat().format(valorCotizaciones))
					.add("totalComprasNatural", GeneralConfiguration.getInstance().getNumberFormat().format(valorCompras))
					.add("totalGastosNatural", GeneralConfiguration.getInstance().getNumberFormat().format(valorGastos))
					.add("totalCotizaciones", valorCotizaciones.toString())
					.add("totalCompras", valorCompras.toString())
					.add("totalGastos", valorGastos.toString());
		
		return jsonReturn.build().toString();
	}
	
	
	/* REPORTE DE APROBADOS */
	public List<CotizacionEntity> listCotizacionesAprobados(LocalDate ldFechaActual, int condicional) {
		
		List<CotizacionEntity> lstCotizaciones = null;
		
		switch(condicional) {
			case 1:
				lstCotizaciones = cotizacionRepository.lstCotizacionesAprobadasIgualA(ldFechaActual);
			break;
			
			case 2:
				lstCotizaciones = cotizacionRepository.lstCotizacionesAprobadasMenorIgualA(ldFechaActual);
			break;
			
			case 3:
				lstCotizaciones = cotizacionRepository.lstCotizacionesAprobadasMayorIgualA(ldFechaActual);
			break;
		}
		
		return lstCotizaciones;
	}
	//**************************
	
	/* REPORTE DE FACTURADOS */
	public List<CotizacionEntity> listCotizacionesFacturados(LocalDate ldFechaActual, int condicional) {
		
		List<CotizacionEntity> lstCotizaciones = null;
		
		switch(condicional) {
			case 1:
				lstCotizaciones = cotizacionRepository.lstCotizacionesFacturadasIgualA(ldFechaActual);
			break;
			
			case 2:
				lstCotizaciones = cotizacionRepository.lstCotizacionesFacturadasIgualA(ldFechaActual);
			break;
			
			case 3:
				lstCotizaciones = cotizacionRepository.lstCotizacionesFacturadasMayorIgualA(ldFechaActual);
			break;
		}
		
		return lstCotizaciones;
	}
	//**************************
	
	/* REPORTES DE VENTAS */
	public List<CotizacionEntity> listCotizacionesPeriodo(LocalDate ldFechaInicio, LocalDate ldFechaFin, UsuarioEntity objUsuario, EmpresaEntity objEmpresa) {
		return cotizacionRepository.findCotizacionesPorMes(ldFechaInicio, ldFechaFin, objUsuario.getIdUsuario(), objEmpresa.getIdEmpresa());
	}
	//**************************
	
	
	
	/* REPORTES DE COMISIONES */
	public List<CotizacionEntity> listCotizacionesPagadasPeriodo(LocalDate ldFechaInicio, LocalDate ldFechaFin, UsuarioEntity objUsuario, EmpresaEntity objEmpresa) {
		return cotizacionRepository.findCotizacionesPagadasPorMes(ldFechaInicio, ldFechaFin, objUsuario.getIdUsuario(), objEmpresa.getIdEmpresa());
	}
	//**************************
	
	public BigDecimal sumCotizacionesSubtotalVentasPeriodo(LocalDate ldFechaInicio, LocalDate ldFechaFin) {		
		return cotizacionRepository.sumTotalCotizacionPorMes(ldFechaInicio, ldFechaFin, sessionService.getCurrentUser().getIdUsuario());		
	}
	
	public List<CotizacionEntity> listCotizacionesActivas() {
		return cotizacionRepository.findCotizacionesActivas();
	}
	
	public void create(CotizacionEntity objCotizacion) {
		
		if(objCotizacion != null) {
			LocalDateTime ldtNow = LocalDateTime.now();
			UsuarioEntity objUsuarioCreacion = sessionService.getCurrentUser();
			objCotizacion.setCreacionFecha(ldtNow);
			objCotizacion.setCreacionUsuario(objUsuarioCreacion);
			objCotizacion.setModificacionFecha(ldtNow);
			objCotizacion.setModificacionUsuario(objUsuarioCreacion);
			cotizacionRepository.save(objCotizacion);
		}
		else {
			throw new ApplicationException(EnumException.COTIZACIONES_CREATE_001);
		}
	}
	
	public void update(CotizacionEntity objCotizacion) {
		
		if(objCotizacion != null) {
			LocalDateTime ldtNow = LocalDateTime.now();
			UsuarioEntity objUsuarioModificacion = sessionService.getCurrentUser();
			objCotizacion.setModificacionFecha(ldtNow);
			objCotizacion.setModificacionUsuario(objUsuarioModificacion);
			cotizacionRepository.save(objCotizacion);
		}
		else {
			throw new ApplicationException(EnumException.COTIZACIONES_UPDATE_001);
		}
	}

	@Transactional
	public void clone(CotizacionEntity objCotizacion, CotizacionEntity objCotizacionNueva) {
		
		if(objCotizacion != null && objCotizacionNueva != null) {
			
			LocalDateTime ldtNow = LocalDateTime.now();
			UsuarioEntity objUsuarioCreacion = sessionService.getCurrentUser();
			objCotizacionNueva.setCreacionFecha(ldtNow);
			objCotizacionNueva.setCreacionUsuario(objUsuarioCreacion);
			objCotizacionNueva.setModificacionFecha(ldtNow);
			objCotizacionNueva.setModificacionUsuario(objUsuarioCreacion);
			cotizacionRepository.save(objCotizacionNueva);
			
			List<CotizacionPartidaEntity> lstPartidas = cotizacionPartidaService.listCotizacionesPartidas(objCotizacion.getIdCotizacion());
			
			for(CotizacionPartidaEntity itemPartida : lstPartidas) {
				
				try {
					if(itemPartida != null) {
						
						CotizacionPartidaEntity objPartida = new CotizacionPartidaEntity();
						
						objPartida.setCotizacion(objCotizacionNueva);
						objPartida.setOrdenIndex(itemPartida.getOrdenIndex());
						objPartida.setNumeroParte(itemPartida.getNumeroParte());
						objPartida.setEntregaDiasHabiles(itemPartida.getEntregaDiasHabiles());
						objPartida.setDescripcion(itemPartida.getDescripcion());
						objPartida.setCantidad(itemPartida.getCantidad());
						objPartida.setPrecioUnitarioLista(itemPartida.getPrecioUnitarioLista());
						objPartida.setDescuentoPorcentaje(itemPartida.getDescuentoPorcentaje());
						objPartida.setUsuario(itemPartida.getUsuario());
						
						cotizacionPartidaService.create(objPartida);
					
					} else {
						throw new ApplicationException(EnumException.COTIZACIONES_CLONE_004);	
					}
				} catch(ApplicationException exception) {
					throw new ApplicationException(EnumException.COTIZACIONES_CLONE_003);
				}			
			}			
		} else {
			throw new ApplicationException(EnumException.COTIZACIONES_CLONE_002);
		}
	}
	
	
	
}
