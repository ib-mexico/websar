package com.ibmexico.services;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.ibmexico.libraries.notifications.ApplicationException;
import com.ibmexico.libraries.notifications.EnumException;
import com.ibmexico.configurations.GeneralConfiguration;
import com.ibmexico.entities.CotizacionComisionEntity;
import com.ibmexico.entities.CotizacionEntity;
import com.ibmexico.entities.CotizacionPartidaEntity;
import com.ibmexico.entities.CotizacionUsuarioQuotaEntity;
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
	@Qualifier("cotizacionUsuarioQuotaService")
	private CotizacionUsuarioQuotaService cotizacionUsuarioQuotaService;
	
	@Autowired
	@Qualifier("cotizacionComisionService")
	private CotizacionComisionService cotizacionComisionService;
	
	@Autowired
	@Qualifier("sessionService")
	private SessionService sessionService;
	private static DecimalFormat df = new DecimalFormat("0.00");
	
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

	public List<CotizacionEntity> findByCotizacionIdOpn(int idCotizacion){
		return cotizacionRepository.findCotizacionIdOpn(idCotizacion);
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
		BigDecimal porcentajeEnCobranza = null;
		
		if(sessionService.hasRol("COTIZACIONES_ADMINISTRADOR")) {
			
			long totalCotizaciones = cotizacionRepository.countCotizacionesPeriodo(ldFechaInicio, ldFechaFin);
			
			if(totalCotizaciones > 0) {
				
				porcentajeActivos =  new BigDecimal(cotizacionRepository.countCotizacionesEstatusPeriodo(1, ldFechaInicio, ldFechaFin)).divide(new BigDecimal(totalCotizaciones), 2, RoundingMode.HALF_UP);
				porcentajeAprobados =  new BigDecimal(cotizacionRepository.countCotizacionesEstatusPeriodo(2, ldFechaInicio, ldFechaFin)).divide(new BigDecimal(totalCotizaciones), 2, RoundingMode.HALF_UP);
				porcentajeFacturados =  new BigDecimal(cotizacionRepository.countCotizacionesEstatusPeriodo(3, ldFechaInicio, ldFechaFin)).divide(new BigDecimal(totalCotizaciones), 2, RoundingMode.HALF_UP);
				porcentajePagados =  new BigDecimal(cotizacionRepository.countCotizacionesEstatusPeriodo(4, ldFechaInicio, ldFechaFin)).divide(new BigDecimal(totalCotizaciones), 2, RoundingMode.HALF_UP);
				porcentajeCancelados =  new BigDecimal(cotizacionRepository.countCotizacionesEstatusPeriodo(5, ldFechaInicio, ldFechaFin)).divide(new BigDecimal(totalCotizaciones), 2, RoundingMode.HALF_UP);
				porcentajeEnCobranza =  new BigDecimal(cotizacionRepository.countCotizacionesEstatusPeriodo(6, ldFechaInicio, ldFechaFin)).divide(new BigDecimal(totalCotizaciones), 2, RoundingMode.HALF_UP);
			} else {
				porcentajeActivos = new BigDecimal(0);
				porcentajeAprobados = new BigDecimal(0);
				porcentajeFacturados = new BigDecimal(0);
				porcentajePagados = new BigDecimal(0);
				porcentajeCancelados = new BigDecimal(0);
				porcentajeEnCobranza = new BigDecimal(0);
			}
			
		} else {
			
			long totalCotizaciones = cotizacionRepository.countCotizacionesPeriodo(sessionService.getCurrentUser().getIdUsuario(), ldFechaInicio, ldFechaFin);
			
			if(totalCotizaciones > 0 ) {
				
				porcentajeActivos =  new BigDecimal(cotizacionRepository.countCotizacionesEstatusPeriodo(1, ldFechaInicio, ldFechaFin, sessionService.getCurrentUser().getIdUsuario())).divide(new BigDecimal(totalCotizaciones), 2, RoundingMode.HALF_UP);
				porcentajeAprobados =  new BigDecimal(cotizacionRepository.countCotizacionesEstatusPeriodo(2, ldFechaInicio, ldFechaFin, sessionService.getCurrentUser().getIdUsuario())).divide(new BigDecimal(totalCotizaciones), 2, RoundingMode.HALF_UP);
				porcentajeFacturados =  new BigDecimal(cotizacionRepository.countCotizacionesEstatusPeriodo(3, ldFechaInicio, ldFechaFin, sessionService.getCurrentUser().getIdUsuario())).divide(new BigDecimal(totalCotizaciones), 2, RoundingMode.HALF_UP);
				porcentajePagados =  new BigDecimal(cotizacionRepository.countCotizacionesEstatusPeriodo(4, ldFechaInicio, ldFechaFin, sessionService.getCurrentUser().getIdUsuario())).divide(new BigDecimal(totalCotizaciones), 2, RoundingMode.HALF_UP);
				porcentajeCancelados =  new BigDecimal(cotizacionRepository.countCotizacionesEstatusPeriodo(5, ldFechaInicio, ldFechaFin, sessionService.getCurrentUser().getIdUsuario())).divide(new BigDecimal(totalCotizaciones), 2, RoundingMode.HALF_UP);
				porcentajeEnCobranza =  new BigDecimal(cotizacionRepository.countCotizacionesEstatusPeriodo(6, ldFechaInicio, ldFechaFin, sessionService.getCurrentUser().getIdUsuario())).divide(new BigDecimal(totalCotizaciones), 2, RoundingMode.HALF_UP);
			} else {
				porcentajeActivos = new BigDecimal(0);
				porcentajeAprobados = new BigDecimal(0);
				porcentajeFacturados = new BigDecimal(0);
				porcentajePagados = new BigDecimal(0);
				porcentajeCancelados = new BigDecimal(0);
				porcentajeEnCobranza = new BigDecimal(0);
			}
			
		}		
				
		JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
		jsonReturn	.add("porcentajeActivos", porcentajeActivos.multiply(new BigDecimal(100)).toString())
					.add("porcentajeAprobados", porcentajeAprobados.multiply(new BigDecimal(100)).toString())
					.add("porcentajeFacturados", porcentajeFacturados.multiply(new BigDecimal(100)).toString())
					.add("porcentajePagados", porcentajePagados.multiply(new BigDecimal(100)).toString())
					.add("porcentajeCancelados", porcentajeCancelados.multiply(new BigDecimal(100)).toString())
					.add("porcentajeEnCobranza", porcentajeEnCobranza.multiply(new BigDecimal(100)).toString());
		
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
	
	public List<CotizacionEntity> lstCotizacionesNoCobradas() {		
		return cotizacionRepository.lstCotizacionesNoCobradas();
	}
	
	public List<CotizacionEntity> lstCotizacionesNoCobradas(EmpresaEntity objEmpresa) {		
		return cotizacionRepository.lstCotizacionesNoCobradas(objEmpresa.getIdEmpresa());
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
	
	/* REPORTES DE COMISIONES DE COBRANZA */
	public List<CotizacionEntity> listCotizacionesCobradasPeriodo(LocalDate ldFechaInicio, LocalDate ldFechaFin, UsuarioEntity objUsuario, EmpresaEntity objEmpresa) {
		return cotizacionRepository.findCotizacionesCobradasPorMes(ldFechaInicio, ldFechaFin, objUsuario.getIdUsuario(), objEmpresa.getIdEmpresa());
	}
	//**************************
	
	public BigDecimal sumCotizacionesSubtotalVentasPeriodo(LocalDate ldFechaInicio, LocalDate ldFechaFin) {		
		return cotizacionRepository.sumTotalCotizacionPorMes(ldFechaInicio, ldFechaFin, sessionService.getCurrentUser().getIdUsuario());		
	}
	
	public List<CotizacionEntity> listCotizacionesActivas() {
		return cotizacionRepository.findCotizacionesActivas();
	}
	
	/* CRUD DE COTIZACIONES */
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
	//**************************
	
	/* RECALCULO DE LA COTIZACION */
	public void recalcularCotizacion(CotizacionEntity objCotizacion) {
		if( objCotizacion != null) {
			//VALIDAMOS QUE LA COTIZACION NO SE UNA RENTA
			if(objCotizacion.isMaestra() || objCotizacion.isNormal()) {
				
				List<CotizacionUsuarioQuotaEntity> lstCuotas = cotizacionUsuarioQuotaService.listCotizacionQuotas(objCotizacion);
				
				if(!lstCuotas.isEmpty()) {						
					cotizacionUsuarioQuotaService.eliminarQuota(objCotizacion);													
				}
				
				if(objCotizacion.isMaestra()) {					
					CotizacionComisionEntity objComisionCotizacion = cotizacionComisionService.findByCotizacion(objCotizacion);
					
					if(objComisionCotizacion != null) {							
						cotizacionComisionService.eliminarComision(objCotizacion);														
					}													
				}
				
				CotizacionUsuarioQuotaEntity objcuota = new CotizacionUsuarioQuotaEntity();
				objcuota.setCotizacion(objCotizacion);
				
				cotizacionUsuarioQuotaService.cargarQuota(objCotizacion);						
			}
			
			//VALIDAMOS QUE LA COTIZACION NO SE MAESTRA
			if(objCotizacion.getCotizacionEstatus().getIdCotizacionEstatus() == 4) {				
				if(objCotizacion.isRenta() || objCotizacion.isNormal()) {
					
					CotizacionComisionEntity objComisionCotizacion = cotizacionComisionService.findByCotizacion(objCotizacion);
					
					if(objComisionCotizacion != null) {							
						cotizacionComisionService.eliminarComision(objCotizacion);														
					}
					
					if(objCotizacion.isRenta()) {					
						List<CotizacionUsuarioQuotaEntity> lstCuotas = cotizacionUsuarioQuotaService.listCotizacionQuotas(objCotizacion);
						
						if(!lstCuotas.isEmpty()) {						
							cotizacionUsuarioQuotaService.eliminarQuota(objCotizacion);													
						}													
					}
					
					CotizacionComisionEntity objComision = new CotizacionComisionEntity();
					objComision.setCotizacion(objCotizacion);
					
					cotizacionComisionService.create(objComision, objCotizacion);						
				}
			}
		}
	}
	//**************************
	
	//Listado de cotizaciones activas 
	public JsonObject jsonCotizacionesActivas(){
		JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
		JsonArrayBuilder jsonRows = Json.createArrayBuilder();
	
		List<CotizacionEntity> lstCotizaciones =  cotizacionRepository.findCotizacionesActivas();
	
		lstCotizaciones.forEach((item)-> {
			jsonRows.add(Json.createObjectBuilder()
				.add("id_cotizacion", item.getIdCotizacion())
				.add("factura_numero", item.getFolio())
			);
		});
	
		jsonReturn.add("rows", jsonRows);	
		return jsonReturn.build();
	}


	//Listado de cotizaciones pasaron de aceptadas y no canceladas 
	public JsonObject jsonCotizacionesActivos(){
		JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
		JsonArrayBuilder jsonRows = Json.createArrayBuilder();
		
		List<CotizacionEntity> lstCotizaciones =  cotizacionRepository.findCotizacionesActivos();
		
		lstCotizaciones.forEach((item)-> {
			jsonRows.add(Json.createObjectBuilder()
				.add("id_cotizacion", item.getIdCotizacion())
				.add("factura_numero", item.getFolio())
			);
		});
		
		jsonReturn.add("rows", jsonRows);	
		return jsonReturn.build();
	}
	
	/*------------------Tablero Indicadores---------------*/
	public JsonObject jsonCotizacionesAceptadas(LocalDate ldFechaInicio, LocalDate ldFechaFin, int idEjecutivo){
		JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
		JsonArrayBuilder jsonRows = Json.createArrayBuilder();
		List<CotizacionEntity> lstCotizacionesAceptadas=cotizacionRepository.cotizacionesAprobada(ldFechaInicio, ldFechaFin, idEjecutivo);
		int totalCotizacionesAceptadas=lstCotizacionesAceptadas.size();
		double montoCotizacion=0;
		df.setRoundingMode(RoundingMode.DOWN);
		for (CotizacionEntity cotAceptadas : lstCotizacionesAceptadas) {
			montoCotizacion=montoCotizacion + cotAceptadas.getSubtotal().doubleValue();
		}
		int meta=50;
		double porcentaje=(totalCotizacionesAceptadas*100)/meta;
		if (porcentaje>100) {
			porcentaje=100;
		}
		jsonRows.add(Json.createObjectBuilder()
			.add("titulo", "Total de Cotizaciones Aceptadas")
			.add("total", totalCotizacionesAceptadas)
			.add("montoCotizacion", df.format(montoCotizacion))
			.add("meta", meta)
			.add("porcentaje", df.format(porcentaje))
			);
		jsonReturn.add("jsonCotizacionesAceptadas", jsonRows);	
		return jsonReturn.build();
	}
	
	public JsonObject jsonCotizacionesFacturadas(LocalDate ldFechaInicio, LocalDate ldFechaFin, int idEjecutivo){
		JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
		JsonArrayBuilder jsonRows = Json.createArrayBuilder();
		List<CotizacionEntity> lstCotizacionesFacturadas = cotizacionRepository.cotizacionesFacturada(idEjecutivo, ldFechaInicio, ldFechaFin);
		int totalCotizacionesFacturadas=lstCotizacionesFacturadas.size();
		float montoCotizacion = 0;
		for (CotizacionEntity cotAceptadas : lstCotizacionesFacturadas) {
			montoCotizacion=montoCotizacion + cotAceptadas.getSubtotal().floatValue();
		}
		df.setRoundingMode(RoundingMode.DOWN);
		int meta=50;
		double porcentaje=(totalCotizacionesFacturadas*100)/meta;
		if (porcentaje>100) {
			porcentaje=100;
		}
		jsonRows.add(Json.createObjectBuilder()
			.add("titulo", "Total de Cotizaciones Facturadas")
			.add("total", totalCotizacionesFacturadas)
			.add("montoCotizacion", df.format(montoCotizacion))
			.add("meta", meta)
			.add("porcentaje", porcentaje)
			);
		jsonReturn.add("jsonCotizacionesFacturadas", jsonRows);	
		return jsonReturn.build();
	}
	
	public JsonObject jsonCotizacionesNuevas(LocalDate ldFechaInicio, LocalDate ldFechaFin, int idEjecutivo){
		JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
		JsonArrayBuilder jsonRows = Json.createArrayBuilder();
		List<CotizacionEntity> lstCotizacionesNuevas = cotizacionRepository.cotizacionesPeriodoEjecutivo(ldFechaInicio, ldFechaFin, idEjecutivo);
		int totalCotizacionesNuevas=lstCotizacionesNuevas.size();
		float montoCotizacion=0;
		for (CotizacionEntity cotAceptadas : lstCotizacionesNuevas) {
			montoCotizacion =montoCotizacion+ cotAceptadas.getSubtotal().floatValue();
		}
		df.setRoundingMode(RoundingMode.DOWN);
		int meta=20;
		double porcentaje=(totalCotizacionesNuevas*100)/meta;
		if (porcentaje>100) {
			porcentaje=100;
		}
		jsonRows.add(Json.createObjectBuilder()
			.add("titulo", "Total de Cotizaciones Nuevas")
			.add("total", totalCotizacionesNuevas)
			.add("montoCotizacion", df.format(montoCotizacion))
			.add("meta", meta)
			.add("porcentaje", porcentaje)
			);
		jsonReturn.add("jsonCotizacionesNuevas", jsonRows);	
		return jsonReturn.build();
	}

	public JsonObject jsonCotizacionesCobradasMas90(LocalDate ldFechaInicio, LocalDate ldFechaFin, int idEjecutivo){
		JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
		JsonArrayBuilder jsonRows = Json.createArrayBuilder();
		List<CotizacionEntity> lstCotizacionesPagadas=cotizacionRepository.totalCotizacionesPagadas(idEjecutivo,ldFechaInicio, ldFechaFin);
		int valorMaximo = 90; double montoCotizacion = 0; int numeroCotCobradasMayor90Dias = 0;
		df.setRoundingMode(RoundingMode.DOWN);
		
		for (int i = 0; i < lstCotizacionesPagadas.size(); i++) {
			int diff = (int) ChronoUnit.DAYS.between(lstCotizacionesPagadas.get(i).getAprobacionFecha(), lstCotizacionesPagadas.get(i).getPagoFecha());
			if(diff > valorMaximo){
				montoCotizacion = montoCotizacion+lstCotizacionesPagadas.get(i).getSubtotal().doubleValue();
				numeroCotCobradasMayor90Dias++;
			}
		}
		int meta=100;
		double porcentaje=(numeroCotCobradasMayor90Dias*100)/meta;
		if (porcentaje>100) {
			porcentaje=100;
		}
		jsonRows.add(Json.createObjectBuilder()
			.add("titulo", "Total de Cotizaciones Cobradas Mas de 90 Dias")
			.add("total", numeroCotCobradasMayor90Dias)
			.add("montoCotizacion", df.format(montoCotizacion))
			.add("meta", 100)
			.add("porcentaje", porcentaje)
			);
		jsonReturn.add("jsonCotCobradasMas90Dias", jsonRows);	
		return jsonReturn.build();
	}

	public JsonObject jsonCotizacionesCobradasMenos90(LocalDate ldFechaInicio, LocalDate ldFechaFin, int idEjecutivo){
		JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
		JsonArrayBuilder jsonRows = Json.createArrayBuilder();
		List<CotizacionEntity> lstCotizacionesPagadas = cotizacionRepository.totalCotizacionesPagadas(idEjecutivo,ldFechaInicio, ldFechaFin);
		int valorMaximo = 90; double montoCotizacion = 0;
		int numCotCobradasMenor90Dias=0;
		for (int i = 0; i < lstCotizacionesPagadas.size(); i++) {
			int diff = (int) ChronoUnit.DAYS.between(lstCotizacionesPagadas.get(i).getAprobacionFecha(), lstCotizacionesPagadas.get(i).getPagoFecha());
			if(diff < valorMaximo){
				montoCotizacion = montoCotizacion+lstCotizacionesPagadas.get(i).getSubtotal().doubleValue();
				numCotCobradasMenor90Dias++;
			}
		}
		df.setRoundingMode(RoundingMode.DOWN);
		int meta=100;
		double porcentaje=(numCotCobradasMenor90Dias*100)/100;
		if (porcentaje>100) {
			porcentaje=100;
		}
		jsonRows.add(Json.createObjectBuilder()
			.add("titulo", "Total de Cotizaciones Cobradas menos de 90 Dias")
			.add("total", numCotCobradasMenor90Dias)
			.add("montoCotizacion", df.format(montoCotizacion))
			.add("meta", meta)
			.add("porcentaje", porcentaje)
			);
		jsonReturn.add("jsonCotCobradasMenos90Dias", jsonRows);	
		return jsonReturn.build();
	}


	public JsonObject totalCotizadosPeriodoProduccion(LocalDate ldFechaInicio, LocalDate ldFechaFin, int idEjecutivo){

		JsonObject jsonCotizacionesNuevas=null;
		JsonObject jsonCotizacionesAprobadas=null;
		JsonObject jsonCotizacionesFacturadas=null;
		JsonObject jsonCotizacionesCobradasMas90=null;
		JsonObject jsonCotizacionesCobradasMenos90=null;
		Boolean respuesta=false;
		try {
			jsonCotizacionesNuevas=jsonCotizacionesNuevas(ldFechaInicio, ldFechaFin, idEjecutivo);
			jsonCotizacionesAprobadas=jsonCotizacionesAceptadas(ldFechaInicio, ldFechaFin, idEjecutivo);
			jsonCotizacionesFacturadas=jsonCotizacionesFacturadas(ldFechaInicio, ldFechaFin, idEjecutivo);
			jsonCotizacionesCobradasMas90=jsonCotizacionesCobradasMas90(ldFechaInicio, ldFechaFin, idEjecutivo);
			jsonCotizacionesCobradasMenos90=jsonCotizacionesCobradasMenos90(ldFechaInicio, ldFechaFin, idEjecutivo);
			respuesta=true;
		} catch (Exception e) {
			respuesta=false;
		}
		JsonObjectBuilder jsonReturn=Json.createObjectBuilder();
		jsonReturn.add("jsonCotNuevas", jsonCotizacionesNuevas)
					.add("jsonCotAprobadas", jsonCotizacionesAprobadas)
					.add("jsonCotFacturadas", jsonCotizacionesFacturadas)
					.add("jsonCotCobradasMas90", jsonCotizacionesCobradasMas90)
					.add("jsonCotCobradasMenos90", jsonCotizacionesCobradasMenos90)
					.add("respuesta", respuesta);
		return jsonReturn.build();

	}

	public JsonObject jsonCotizacionSeleccionado(int idCotizacion){
		JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
		JsonArrayBuilder jsonRows=Json.createArrayBuilder();

		List<CotizacionEntity> lstCotizacionUnique=cotizacionRepository.findCotizacionId(idCotizacion);

		jsonRows.add(Json.createObjectBuilder().
		add("idCotizacion", lstCotizacionUnique.get(0).getIdCotizacion()).
		add("empresa", lstCotizacionUnique.get(0).getEmpresa().getEmpresa()).
		add("usuarioVendedor", lstCotizacionUnique.get(0).getUsuarioVendedor().getNombreCompleto()).
		add("folio", lstCotizacionUnique.get(0).getFolio()));

		jsonReturn.add("jsonCotizacionSeleccionado", jsonRows);
		return jsonReturn.build();
	}
}
