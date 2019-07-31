package com.ibmexico.controllers;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.json.Json;
import javax.json.JsonObjectBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.context.Context;

import com.ibmexico.components.MailerComponent;
import com.ibmexico.components.ModelAndViewComponent;
import com.ibmexico.components.PdfComponent;
import com.ibmexico.configurations.GeneralConfiguration;
import com.ibmexico.entities.CotizacionComisionEntity;
import com.ibmexico.entities.CotizacionEntity;
import com.ibmexico.entities.EmpresaEntity;
import com.ibmexico.entities.NoticiaEntity;
import com.ibmexico.entities.UsuarioEntity;
import com.ibmexico.libraries.Templates;
import com.ibmexico.services.CotizacionComisionService;
import com.ibmexico.services.CotizacionFicheroService;
import com.ibmexico.services.CotizacionService;
import com.ibmexico.services.CotizacionUsuarioQuotaService;
import com.ibmexico.services.EmpresaService;
import com.ibmexico.services.NoticiaService;
import com.ibmexico.services.OportunidadNegocioService;
import com.ibmexico.services.SessionService;
import com.ibmexico.services.UsuarioService;
import com.lowagie.text.DocumentException;

@Controller
@RequestMapping("controlPanel")
public class HomeController {
	
	//private static final Log LOG = LogFactory.getLog(Home.class);
	
	@Autowired
	@Qualifier("modelAndViewComponent")
	private ModelAndViewComponent modelAndViewComponent;
	
	@Autowired
	private MailerComponent mailerComponent;
	
	@Autowired
	@Qualifier("pdfComponent")
	private PdfComponent pdfComponent;
	
	@Autowired
	@Qualifier("usuarioService")
	private UsuarioService usuarioService;
	
	@Autowired
	@Qualifier("noticiaService")
	private NoticiaService noticiaService;
	
	@Autowired
	@Qualifier("cotizacionService")
	private CotizacionService cotizacionService;
	
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
	@Qualifier("oportunidadNegocioService")
	private OportunidadNegocioService oportunidadNegocioService;
	
	@Autowired
	@Qualifier("empresaService")
	private EmpresaService empresaService;
	
	@Autowired
	@Qualifier("sessionService")
	private SessionService sessionService;
	
	
	
	@GetMapping({"/Home", "/", ""})
	public ModelAndView index() {
		
		//PERIODO
		LocalDate fechaMesInicio = LocalDate.now().withDayOfMonth(1);
		LocalDate fechaMesFin = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());	
		
		//USUARIOS
		List<UsuarioEntity> lstUsuarios = usuarioService.listUsuariosActivos();
		
		//EMPRESAS
		List<EmpresaEntity> lstEmpresas = empresaService.listEmpresas();
		
		//NOTICIAS
		List<NoticiaEntity> lstNoticias = noticiaService.listNoticias();
		List<NoticiaEntity> lstNoticiasImportantes = noticiaService.listNoticiasImportantes();
		
		ModelAndView objModelAndView = modelAndViewComponent.createModelAndViewControlPanel(Templates.CONTROL_PANEL_HOME_INDEX);
		objModelAndView.addObject("lstNoticias", lstNoticias);
		objModelAndView.addObject("lstNoticiasImportantes", lstNoticiasImportantes);
		objModelAndView.addObject("lstUsuarios", lstUsuarios);
		objModelAndView.addObject("lstEmpresas", lstEmpresas);
		
		objModelAndView.addObject("rolCotizacionCobranza", sessionService.hasRol("COTIZACIONES_COBRANZA"));
		objModelAndView.addObject("rolReportesGerencia", sessionService.hasRol("REPORTES_GERENCIA"));
		
		objModelAndView.addObject("fechaMesInicio", fechaMesInicio.format(GeneralConfiguration.getInstance().getDateFormatterNatural()));
		objModelAndView.addObject("fechaMesFin", fechaMesFin.format(GeneralConfiguration.getInstance().getDateFormatterNatural()));
		
		objModelAndView.addObject("totalCotizaciones", cotizacionService.sumCotizacionesTotalesPorFecha(fechaMesInicio, fechaMesFin));
		objModelAndView.addObject("totalOportunidades", oportunidadNegocioService.sumOportunidadesTotalesPorFecha(fechaMesInicio, fechaMesFin));
		
		objModelAndView.addObject("totalPorcentajes", cotizacionService.countCotizacionesEstatusPeriodo(fechaMesInicio, fechaMesFin));
		
		objModelAndView.addObject("totalQuota", cotizacionUsuarioQuotaService.totalUsuarioQuotaPeriodo(fechaMesInicio, fechaMesFin));
		
		objModelAndView.addObject("totalVentas", cotizacionService.sumCotizacionesVentasPeriodo(fechaMesInicio, fechaMesFin));
		objModelAndView.addObject("monthActual", GeneralConfiguration.getInstance().getCurrentMonthNatural(fechaMesInicio.getMonthValue()));
		objModelAndView.addObject("yearActual", fechaMesInicio.getYear());
		
		
		
		
		
		
				
		
		
		/*objModelAndView.addObject("totalIngresosMesActual", GeneralConfiguration.getInstance().getCurrencyNumberFormat().format(cajaMovimientoService.totalIngresos(fechaMesInicio, fechaMesFin)));
		objModelAndView.addObject("totalEgresosMesActual", GeneralConfiguration.getInstance().getCurrencyNumberFormat().format(cajaMovimientoService.totalEgresos(fechaMesInicio, fechaMesFin)));
		objModelAndView.addObject("totalCargosPendientes", GeneralConfiguration.getInstance().getCurrencyNumberFormat().format(movimientoService.sumMovimientosCargosPendientes()));
		objModelAndView.addObject("totalCargosPagados", GeneralConfiguration.getInstance().getCurrencyNumberFormat().format(movimientoService.sumMovimientosCargosPagados()));

		
		objModelAndView.addObject("totalIngresos", GeneralConfiguration.getInstance().getCurrencyNumberFormat().format(cajaMovimientoService.totalIngresos()));
		objModelAndView.addObject("totalEgresos", GeneralConfiguration.getInstance().getCurrencyNumberFormat().format(cajaMovimientoService.totalEgresos()));
		objModelAndView.addObject("totalBalance", GeneralConfiguration.getInstance().getCurrencyNumberFormat().format(cajaMovimientoService.totalBalance()));
		objModelAndView.addObject("totalUsuariosActivos", GeneralConfiguration.getInstance().getNumberFormat().format(usuarioService.countUsuariosNoEliminados()));
		objModelAndView.addObject("totalInmueblesActivos", GeneralConfiguration.getInstance().getNumberFormat().format(inmuebleService.countInmueblesNoEliminado()));
		
		
		List<AreaComunReservacionEntity> lstAreasComunesReservaciones = areaComunReservacionService.listTakeNext(5);
		objModelAndView.addObject("lstAreasComunesReservaciones", lstAreasComunesReservaciones);
		
		List<ReunionEntity> lstReuniones = reunionService.listTakeNext(5);

		double[][] arrAbonosMensuales = movimientoService.estadisticasArrayAbonosTake(6);
		
		
		List<LocalDate> lstMeses = cajaMovimientoService.listMesesTake(6);
		String[] arrMeses = new String[lstMeses.size()];
		
		for(int i=0; i< lstMeses.size(); i++) {
			arrMeses[i] = GeneralConfiguration.getInstance().getMonthFormatterNatural().format(lstMeses.get(i));
			arrMeses[i] = arrMeses[i].substring(0, 1).toUpperCase() +  arrMeses[i].substring(1) + " " + lstMeses.get(i).getYear();
		}
		
		double[] arrIngresos = cajaMovimientoService.arrEstadisticasIngresosTake(lstMeses.size());
		double[] arrEgresos = cajaMovimientoService.arrEstadisticasEgresosTake(lstMeses.size());
		double[] arrCargos = movimientoService.arrEstadisticasCargosTake(lstMeses.size());
		double[] arrAbonos = movimientoService.arrEstadisticasAbonosTake(lstMeses.size());
		
		objModelAndView.addObject("lstReuniones", lstReuniones);
		objModelAndView.addObject("arrMeses", arrMeses);
		objModelAndView.addObject("arrIngresos", arrIngresos);
		objModelAndView.addObject("arrEgresos", arrEgresos);
		objModelAndView.addObject("arrCargos", arrCargos);
		objModelAndView.addObject("arrAbonos", arrAbonos);
		objModelAndView.addObject("lstUsuariosDeudores", usuarioService.listUsuariosDeudores());*/
		
		return objModelAndView;
	}
		
	@RequestMapping(value = {"reporte-facturacion-pdf", "reporte-facturacion-pdf/{fecha}/{empresa}/{usuario}"}, method = RequestMethod.GET, produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<InputStreamResource> previewPdfFacturacion(@PathVariable(value="fecha", required=false) String fecha,
																	 @PathVariable(value="empresa", required=false) Integer idEmpresa,
																	 @PathVariable(value="usuario", required=false) Integer idUsuario) throws IOException, DocumentException {
		
		LocalDate fechaMesInicio = null;
		LocalDate fechaMesFin = null;
		
		UsuarioEntity objUsuario = null;
		EmpresaEntity objEmpresa = empresaService.findByIdEmpresa(idEmpresa);
		
		//USUARIO
		if((idUsuario != null && idUsuario >= 1) && sessionService.hasRol("COTIZACIONES_COBRANZA")) {
			objUsuario = usuarioService.findByIdUsuario(idUsuario);
		} else {
			objUsuario = sessionService.getCurrentUser();
		}
		
		
		//PERIODO
		if(!fecha.equals("") && fecha.contains("-")) {
			
			String[] arrFecha = fecha.split("-");
			int year = Integer.parseInt(arrFecha[2]);
			int month = Integer.parseInt(arrFecha[1]);
			int day = Integer.parseInt(arrFecha[0]);
			
			fechaMesInicio = LocalDate.of(year, month, day).withDayOfMonth(1);
			fechaMesFin = LocalDate.of(year, month, day).withDayOfMonth(LocalDate.of(year, month, day).lengthOfMonth());
			
		} else {			
			fechaMesInicio = LocalDate.now().withDayOfMonth(1);
			fechaMesFin = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());
		}
		
		//TOTALES DEL REPORTE DE UTILIDAD
		BigDecimal totalSubtotal = new BigDecimal(0);
		BigDecimal totalGastos = new BigDecimal(0);
		BigDecimal totalCompras = new BigDecimal(0);
		BigDecimal totalUtilidad = new BigDecimal(0);
		BigDecimal totalAcumuladoCuota = new BigDecimal(0);
		BigDecimal totalAcumuladoComision = new BigDecimal(0);
		
		final LocalDate fechaInicio = fechaMesInicio;
		final LocalDate fechaFin = fechaMesFin;
		
		List<CotizacionEntity> lstCotizaciones = cotizacionService.listCotizacionesPeriodo(fechaInicio, fechaFin, objUsuario, objEmpresa);
		List<Map<String , String>> myMap  = new ArrayList<Map<String,String>>();
		Integer iterator = 0;
		
		
		for(CotizacionEntity itemCotizacion: lstCotizaciones) {
			
			Map<String,String> myMap1 = new HashMap<String, String>();
			
			/* GASTOS Y COMPRAS */
			BigDecimal importeCompra = cotizacionFicheroService.totalImporteDocumentoTipoCotizacionPeriodo(4, itemCotizacion, fechaInicio, fechaFin, objUsuario);
			String importeCompraNatural = GeneralConfiguration.getInstance().getNumberFormat().format(importeCompra);
			
			BigDecimal importeGasto = cotizacionFicheroService.totalImporteDocumentoTipoCotizacionPeriodo(3, itemCotizacion, fechaInicio, fechaFin, objUsuario);
			String importeGastoNatural = GeneralConfiguration.getInstance().getNumberFormat().format(importeGasto);
			
			/* UTILIDAD BRUTA */
			BigDecimal utilidadBruta = itemCotizacion.getSubtotal().subtract(importeCompra).subtract(importeGasto);
			String utilidadBrutaNatural = GeneralConfiguration.getInstance().getNumberFormat().format(utilidadBruta);
			
			/* QUOTA DEL USUARIO */
			BigDecimal totalCuota = cotizacionUsuarioQuotaService.totalUsuarioQuotaCotizacionPeriodo(objUsuario, itemCotizacion, fechaInicio, fechaFin);
			String totalCuotaNatural = GeneralConfiguration.getInstance().getNumberFormat().format(totalCuota);
			
			
			BigDecimal totalPorcentaje = new BigDecimal(0);;
			BigDecimal acumuladoPorcentaje = totalPorcentaje.multiply(new BigDecimal(0.1));
			String acumuladoPorcentajeNatural = GeneralConfiguration.getInstance().getNumberFormat().format(acumuladoPorcentaje);
			
			//******************************************************************
			//CALCULO DE TOTALES
			totalSubtotal 	= totalSubtotal.add(itemCotizacion.getSubtotal());
			totalGastos		= totalGastos.add(importeGasto);
			totalCompras	= totalCompras.add(importeCompra);
			totalUtilidad	= totalUtilidad.add(utilidadBruta);
			totalAcumuladoCuota = totalAcumuladoCuota.add(totalCuota);
			//*******************************************************************
			
			myMap1.put("folio", itemCotizacion.getFolio());
			myMap1.put("concepto", itemCotizacion.getConcepto());
			myMap1.put("fecha_facturacion", itemCotizacion.getFacturacionFechaNatural());
			myMap1.put("subtotal", itemCotizacion.getSubtotalNatural());
			myMap1.put("gastos_indirectos", importeGastoNatural);
			myMap1.put("compras", importeCompraNatural);
			myMap1.put("utilidad_bruta", utilidadBrutaNatural);
			myMap1.put("acumulado_cuota", totalCuotaNatural);
			myMap1.put("acumulado_porcentaje", acumuladoPorcentajeNatural);
			
			myMap.add(iterator,myMap1);
			
			iterator++;						
		};
		
		/*TOTAL DE QUOTA DEL USUARIO */
		BigDecimal totalQuota = cotizacionUsuarioQuotaService.totalUsuarioQuotaPeriodo(objUsuario, fechaInicio, fechaFin);
		
		
		LocalDateTime ldtNow = LocalDateTime.now();
		Templates objTemplates = new Templates();
		
		String path_file = ldtNow.getYear() + "_" + ldtNow.getMonthValue() + "_" + ldtNow.getDayOfMonth() + "_REPORTE_UTILIDAD.pdf";
				
		Context objContext = new Context();
		objContext.setVariable("_TEMPLATE_", Templates.PDF_REPORTE_UTILIDAD_GENERAL);
		objContext.setVariable("title", "Reporte de Utilidad");
		
		objContext.setVariable("objUsuario", objUsuario);
		objContext.setVariable("objEmpresa", objEmpresa);
		
		objContext.setVariable("cotizacionesMap", myMap);
		
		objContext.setVariable("totalQuota", GeneralConfiguration.getInstance().getNumberFormat().format(totalQuota));
		objContext.setVariable("totalSubtotal", GeneralConfiguration.getInstance().getNumberFormat().format(totalSubtotal));
		objContext.setVariable("totalGastos", GeneralConfiguration.getInstance().getNumberFormat().format(totalGastos));
		objContext.setVariable("totalCompras", GeneralConfiguration.getInstance().getNumberFormat().format(totalCompras));
		objContext.setVariable("totalUtilidad", GeneralConfiguration.getInstance().getNumberFormat().format(totalUtilidad));
		objContext.setVariable("totalAcumuladoCuota", GeneralConfiguration.getInstance().getNumberFormat().format(totalAcumuladoCuota));
		objContext.setVariable("totalAcumuladoComision", GeneralConfiguration.getInstance().getNumberFormat().format(totalAcumuladoComision));
		
		objContext.setVariable("fechaInicio", fechaInicio.format(GeneralConfiguration.getInstance().getDateFormatterNatural()));
		objContext.setVariable("fechaFin", fechaFin.format(GeneralConfiguration.getInstance().getDateFormatterNatural()));
		objContext.setVariable("mesActual", GeneralConfiguration.getInstance().getCurrentMonthNatural(fechaInicio.getMonthValue()));

		
		return pdfComponent.generate(path_file, objTemplates.FOUNDATION_PDF, objContext);		
	}
	
	@RequestMapping(value = {"reporte-facturacion-area-pdf/{fecha}/{empresa}/{usuario}"}, method = RequestMethod.GET, produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<InputStreamResource> previewPdfFacturacionArea(@PathVariable(value="fecha", required=false) String fecha,
																	 	 @PathVariable(value="empresa", required=false) Integer idEmpresa,
																	 	 @PathVariable(value="usuario", required=false) Integer idUsuario) throws IOException, DocumentException {
		
		LocalDate fechaMesInicio = null;
		LocalDate fechaMesFin = null;
		
		UsuarioEntity objUsuario = sessionService.getCurrentUser();
		
		//USUARIO
		if((idUsuario != null && idUsuario >= 1) && sessionService.hasRol("REPORTES_ADMINISTRADOR")) {
			objUsuario = usuarioService.findByIdUsuario(idUsuario);
		} else {
			objUsuario = sessionService.getCurrentUser();
		}
		
		EmpresaEntity objEmpresa = empresaService.findByIdEmpresa(idEmpresa);
		
		
		//PERIODO
		if(!fecha.equals("") && fecha.contains("-")) {
			
			String[] arrFecha = fecha.split("-");
			int year = Integer.parseInt(arrFecha[2]);
			int month = Integer.parseInt(arrFecha[1]);
			int day = Integer.parseInt(arrFecha[0]);
			
			fechaMesInicio = LocalDate.of(year, month, day).withDayOfMonth(1);
			fechaMesFin = LocalDate.of(year, month, day).withDayOfMonth(LocalDate.of(year, month, day).lengthOfMonth());
			
		} else {			
			fechaMesInicio = LocalDate.now().withDayOfMonth(1);
			fechaMesFin = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());
		}
				
		
		final LocalDate fechaInicio = fechaMesInicio;
		final LocalDate fechaFin = fechaMesFin;
		
		//USUARIOS DEL AREA
		List<UsuarioEntity> lstUsuarios = usuarioService.listUsuariosGruposActivos(objUsuario.getUsuarioGrupo().getIdUsuarioGrupo());
		
		List<Map<String , String>> myMap  = new ArrayList<Map<String,String>>();
		Integer iterator = 0;
		
		//TOTALES DEL REPORTE DE UTILIDAD
		BigDecimal totalFacturacion = new BigDecimal(0);
		BigDecimal totalUtilidad = new BigDecimal(0);
		BigDecimal totalAcumuladoCuota = new BigDecimal(0);
		BigDecimal totalPorcentajeLog = new BigDecimal(0);
		
		for(UsuarioEntity itemUsuario: lstUsuarios) {
			List<CotizacionEntity> lstCotizaciones = cotizacionService.listCotizacionesPeriodo(fechaInicio, fechaFin, itemUsuario, objEmpresa);
			
			//TOTALES DE USUARIO
			BigDecimal subtotal = new BigDecimal(0);
			BigDecimal utilidad = new BigDecimal(0);
			BigDecimal acumuladoCuota = new BigDecimal(0);
			BigDecimal porcentajeLog = new BigDecimal(0);
			
			Map<String,String> myMap1 = new HashMap<String, String>();
			for(CotizacionEntity itemCotizacion: lstCotizaciones) {
								
				/* GASTOS Y COMPRAS */
				BigDecimal importeCompra = cotizacionFicheroService.totalImporteDocumentoTipoCotizacionPeriodo(4, itemCotizacion, fechaInicio, fechaFin, itemUsuario);			
				BigDecimal importeGasto = cotizacionFicheroService.totalImporteDocumentoTipoCotizacionPeriodo(3, itemCotizacion, fechaInicio, fechaFin, itemUsuario);
				
				/* UTILIDAD BRUTA */
				BigDecimal utilidadBruta = itemCotizacion.getSubtotal().subtract(importeCompra).subtract(importeGasto);
				
				/* QUOTA DEL USUARIO */
				BigDecimal cuota = cotizacionUsuarioQuotaService.totalUsuarioQuotaCotizacionPeriodo(itemUsuario, itemCotizacion, fechaInicio, fechaFin);
				
				
				//******************************************************************
				//CALCULO DE TOTALES
				subtotal 	= subtotal.add(itemCotizacion.getSubtotal());				
				utilidad	= utilidad.add(utilidadBruta);
				acumuladoCuota = acumuladoCuota.add(cuota);
				//*******************************************************************
				
			}						
			
			/* PORCENTAJE DE CUOTA LOGRADA */
			if(acumuladoCuota.compareTo(new BigDecimal(0)) == 1 && itemUsuario.getQuota().compareTo(new BigDecimal(0)) == 1) {
				porcentajeLog = acumuladoCuota.divide(itemUsuario.getQuota(), 2, RoundingMode.HALF_UP).multiply(new BigDecimal(100));
			}
			
			myMap1.put("usuario", itemUsuario.getNombreCompleto());
			myMap1.put("facturacion", GeneralConfiguration.getInstance().getNumberFormat().format(subtotal));
			myMap1.put("utilidad", GeneralConfiguration.getInstance().getNumberFormat().format(utilidad));
			myMap1.put("cuota_log", GeneralConfiguration.getInstance().getNumberFormat().format(acumuladoCuota));
			myMap1.put("cuota", ((fechaMesInicio.isAfter(itemUsuario.getCreacionFecha().toLocalDate())) ? itemUsuario.getQuotaNatural() : "$0.00"));
			myMap1.put("porcentaje_log", GeneralConfiguration.getInstance().getNumberFormat().format(porcentajeLog));
			
			myMap.add(iterator,myMap1);
			
			iterator++;
			
			
			/* TOTALES DEL AREA */
			totalFacturacion = totalFacturacion.add(subtotal);
			totalUtilidad = totalUtilidad.add(utilidad);
			totalAcumuladoCuota = totalAcumuladoCuota.add(acumuladoCuota);
		}
		
		/* PORCENTAJE LOGRADO DEL AREA */
		if(totalUtilidad.compareTo(new BigDecimal(0)) == 1 && objUsuario.getUsuarioGrupo().getQuotaGrupo().compareTo(new BigDecimal(0)) == 1) {
			totalPorcentajeLog = totalUtilidad.divide(objUsuario.getUsuarioGrupo().getQuotaGrupo(), 2, RoundingMode.HALF_UP).multiply(new BigDecimal(100));
		}						
		
		
		LocalDateTime ldtNow = LocalDateTime.now();
		Templates objTemplates = new Templates();
		
		String path_file = ldtNow.getYear() + "_" + ldtNow.getMonthValue() + "_" + ldtNow.getDayOfMonth() + "_REPORTE_UTILIDAD_AREA.pdf";
				
		Context objContext = new Context();
		objContext.setVariable("_TEMPLATE_", Templates.PDF_REPORTE_UTILIDAD_AREA);
		objContext.setVariable("title", "Reporte de Utilidad de Area");
		
		objContext.setVariable("objEmpresa", objEmpresa);
		
		objContext.setVariable("usuariosMap", myMap);
		
		objContext.setVariable("totalFacturacion", GeneralConfiguration.getInstance().getNumberFormat().format(totalFacturacion));
		objContext.setVariable("totalUtilidad", GeneralConfiguration.getInstance().getNumberFormat().format(totalUtilidad));
		objContext.setVariable("totalAcumuladoCuota", GeneralConfiguration.getInstance().getNumberFormat().format(totalAcumuladoCuota));
		objContext.setVariable("totalCuota", objUsuario.getUsuarioGrupo().getQuotaGrupoNatural());
		objContext.setVariable("totalPorcentajeLog", GeneralConfiguration.getInstance().getNumberFormat().format(totalPorcentajeLog));
		
		objContext.setVariable("fechaInicio", fechaInicio.format(GeneralConfiguration.getInstance().getDateFormatterNatural()));
		objContext.setVariable("fechaFin", fechaFin.format(GeneralConfiguration.getInstance().getDateFormatterNatural()));
		objContext.setVariable("mesActual", GeneralConfiguration.getInstance().getCurrentMonthNatural(fechaInicio.getMonthValue()));

		
		return pdfComponent.generate(path_file, objTemplates.FOUNDATION_PDF, objContext);		
	}
	
	@RequestMapping(value = {"reporte-comision-pdf", "reporte-comision-pdf/{fecha}/{empresa}/{usuario}"}, method = RequestMethod.GET, produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<InputStreamResource> previewPdfComision(@PathVariable(value="fecha", required=false) String fecha,
																  @PathVariable(value="empresa", required=false) Integer idEmpresa,
																  @PathVariable(value="usuario", required=false) Integer idUsuario) throws IOException, DocumentException {
		
		LocalDate fechaMesInicio = null;
		LocalDate fechaMesFin = null;
		int dayFlag = LocalDate.now().getDayOfMonth();
		
		UsuarioEntity objUsuario = null;
		EmpresaEntity objEmpresa = empresaService.findByIdEmpresa(idEmpresa);
		
		//USUARIO CON PRIVILEGIO DE COBRANZA (OBTIENE REPORTE POR 15 DIAS Y POR MES COMPLETO)
		if((idUsuario != null && idUsuario >= 1) && sessionService.hasRol("COTIZACIONES_COBRANZA")) {
			objUsuario = usuarioService.findByIdUsuario(idUsuario);
			
			if(!fecha.equals("") && fecha.contains("-")) {
				
				String[] arrFecha = fecha.split("-");
				int year = Integer.parseInt(arrFecha[2]);
				int month = Integer.parseInt(arrFecha[1]);
				int day = Integer.parseInt(arrFecha[0]);
				
				fechaMesInicio = LocalDate.of(year, month, day).withDayOfMonth(1);
				fechaMesFin = LocalDate.of(year, month, day).withDayOfMonth(LocalDate.of(year, month, day).lengthOfMonth());
				
			} else {			
				if(dayFlag >= 1 && dayFlag <= 15) {
					fechaMesInicio = LocalDate.now().minusMonths(1).withDayOfMonth(16);
					fechaMesFin = LocalDate.now().minusMonths(1).withDayOfMonth(LocalDate.now().minusMonths(1).lengthOfMonth());
				} else {
					fechaMesInicio = LocalDate.now().withDayOfMonth(1);
					fechaMesFin = LocalDate.now().withDayOfMonth(15);
				}
			}
			
		} else {
			objUsuario = sessionService.getCurrentUser();			
			
			if(dayFlag >= 1 && dayFlag <= 15) {
				fechaMesInicio = LocalDate.now().minusMonths(1).withDayOfMonth(16);
				fechaMesFin = LocalDate.now().minusMonths(1).withDayOfMonth(LocalDate.now().minusMonths(1).lengthOfMonth());
			} else {
				fechaMesInicio = LocalDate.now().withDayOfMonth(1);
				fechaMesFin = LocalDate.now().withDayOfMonth(15);
			}
		}
		
		//TOTALES DEL REPORTE DE UTILIDAD
		BigDecimal totalEjecutivo = new BigDecimal(0);
		BigDecimal totalCotizador = new BigDecimal(0);
		BigDecimal totalVendedor = new BigDecimal(0);
		BigDecimal totalImplementador = new BigDecimal(0);
		BigDecimal totalAcumuladoComision = new BigDecimal(0);
		
		final LocalDate fechaInicio = fechaMesInicio;
		final LocalDate fechaFin = fechaMesFin;
		
		List<CotizacionEntity> lstCotizaciones = cotizacionService.listCotizacionesPagadasPeriodo(fechaInicio, fechaFin, objUsuario, objEmpresa);
		List<Map<String , String>> myMap  = new ArrayList<Map<String,String>>();
		Integer iterator = 0;
		
		
		for(CotizacionEntity itemCotizacion: lstCotizaciones) {
			
			Map<String,String> myMap1 = new HashMap<String, String>();
			
			/*COMISION DEL USUARIO */
			CotizacionComisionEntity objComision = cotizacionComisionService.comisionUsuarioCotizacionPagadaPeriodo(objUsuario, itemCotizacion, fechaInicio, fechaFin);					
			
			BigDecimal comisionEjecutivo = new BigDecimal(0);
			BigDecimal comisionCotizante = new BigDecimal(0);
			BigDecimal comisionVendedor = new BigDecimal(0);
			BigDecimal comisionImplementador = new BigDecimal(0);
			
			String comisionEjecutivoNatural = "-";
			String comisionCotizanteNatural = "-";
			String comisionVendedorNatural = "-";
			String comisionImplementadorNatural = "-";
			
			
			if(objComision != null) {
				
				if(objComision.getUsuarioCotizante().getIdUsuario() == objUsuario.getIdUsuario()){
					comisionCotizante = objComision.getComisionCotizante();
					comisionCotizanteNatural = GeneralConfiguration.getInstance().getNumberFormat().format(objComision.getComisionCotizante());
				}
				
				if(objComision.getUsuarioVendedor().getIdUsuario() == objUsuario.getIdUsuario()){
					comisionVendedor = objComision.getComisionVendedor();
					comisionVendedorNatural = GeneralConfiguration.getInstance().getNumberFormat().format(objComision.getComisionVendedor());
				}
				
				if(objComision.getUsuarioEjecutivo() != null) {
					if(objComision.getUsuarioEjecutivo().getIdUsuario() == objUsuario.getIdUsuario()){
						comisionEjecutivo = objComision.getComisionEjecutivo();
						comisionEjecutivoNatural = GeneralConfiguration.getInstance().getNumberFormat().format(objComision.getComisionEjecutivo());
					}
				}
				
				if(objComision.getUsuarioImplementador() != null) {
					if(objComision.getUsuarioImplementador().getIdUsuario() == objUsuario.getIdUsuario()){
						comisionImplementador = objComision.getComisionImplementador();
						comisionImplementadorNatural = GeneralConfiguration.getInstance().getNumberFormat().format(objComision.getComisionImplementador());
					}
				}				
			}
			
			//TOTAL DE COMISIÓN GENERADA POR LA COTIZACIÓN
			BigDecimal totalComision = comisionEjecutivo.add(comisionCotizante).add(comisionVendedor).add(comisionImplementador);
			String totalComisionNatural = GeneralConfiguration.getInstance().getNumberFormat().format(totalComision);
			
			//***********************************************************************************************************
			// VALIDACIÓN DE COMISIONES Y FORMAS DE PAGO DE COTIZACIÓN
			String estatusPago = "PROCEDE";
			
			if(itemCotizacion.getFormaPago().getFormaPago().equals("Credito")) {
				if(itemCotizacion.getAprobacionFecha() != null) {					
					int diff = (int) ChronoUnit.DAYS.between(itemCotizacion.getAprobacionFecha(), itemCotizacion.getPagoFecha());
					
					if(!itemCotizacion.getDiasCredito().trim().equals("")) {					
						if(diff > Integer.parseInt(itemCotizacion.getDiasCredito())) {
							estatusPago = "NO PROCEDE";						
						} else {
							//******************************************************************
							//CALCULO DE TOTALES
							totalEjecutivo 			= totalEjecutivo.add(comisionEjecutivo);
							totalCotizador			= totalCotizador.add(comisionCotizante);
							totalVendedor			= totalVendedor.add(comisionVendedor);
							totalImplementador		= totalImplementador.add(comisionImplementador);
							totalAcumuladoComision 	= totalAcumuladoComision.add(totalComision);
							//*******************************************************************
						}
					} else {
						estatusPago = "NO PROCEDE";
					}
				} else {
					estatusPago = "NO PROCEDE";
				}
			} else {
				//******************************************************************
				//CALCULO DE TOTALES
				totalEjecutivo 			= totalEjecutivo.add(comisionEjecutivo);
				totalCotizador			= totalCotizador.add(comisionCotizante);
				totalVendedor			= totalVendedor.add(comisionVendedor);
				totalImplementador		= totalImplementador.add(comisionImplementador);
				totalAcumuladoComision 	= totalAcumuladoComision.add(totalComision);
				//*******************************************************************
			}			
			
			myMap1.put("folio", itemCotizacion.getFolio());	
			myMap1.put("fecha_aprobacion", itemCotizacion.getAprobacionFechaNatural());
			myMap1.put("dias_credito", itemCotizacion.getDiasCredito());
			myMap1.put("fecha_pago", itemCotizacion.getPagoFechaNatural());
			myMap1.put("comision_ejecutivo", comisionEjecutivoNatural);
			myMap1.put("comision_cotizador", comisionCotizanteNatural);
			myMap1.put("comision_vendedor", comisionVendedorNatural);
			myMap1.put("comision_implementador", comisionImplementadorNatural);
			myMap1.put("comision_acumulado", totalComisionNatural);
			myMap1.put("estatus", estatusPago);
			
			myMap.add(iterator,myMap1);
			
			iterator++;						
		};
		
		/*TOTAL DE QUOTA DEL USUARIO */
		BigDecimal totalQuota = cotizacionUsuarioQuotaService.totalUsuarioQuotaPeriodo(objUsuario, fechaInicio, fechaFin, objEmpresa);
		BigDecimal cuotaMinima = new BigDecimal(0);
		
		Boolean boolCuota = false;
		
		if(objUsuario.getQuota() != null) {			
			cuotaMinima = objUsuario.getQuota().multiply(new BigDecimal(0.80));
			
			if(totalQuota.compareTo(cuotaMinima) >= 0) {
				boolCuota = true;
			}
		}
		
		
		LocalDateTime ldtNow = LocalDateTime.now();
		Templates objTemplates = new Templates();
		
		String path_file = ldtNow.getYear() + "_" + ldtNow.getMonthValue() + "_" + ldtNow.getDayOfMonth() + "_REPORTE_UTILIDAD.pdf";
				
		Context objContext = new Context();
		objContext.setVariable("_TEMPLATE_", Templates.PDF_REPORTE_COMISION_GENERAL);
		objContext.setVariable("title", "Reporte de Comisión");
		
		objContext.setVariable("objUsuario", objUsuario);
		objContext.setVariable("objEmpresa", objEmpresa);
		objContext.setVariable("boolCuota", boolCuota);
		objContext.setVariable("rolCotizacionCobranza", sessionService.hasRol("COTIZACIONES_COBRANZA"));
		
		objContext.setVariable("cotizacionesMap", myMap);
		
		objContext.setVariable("totalEjecutivo", GeneralConfiguration.getInstance().getNumberFormat().format(totalEjecutivo));
		objContext.setVariable("totalCotizador", GeneralConfiguration.getInstance().getNumberFormat().format(totalCotizador));
		objContext.setVariable("totalVendedor", GeneralConfiguration.getInstance().getNumberFormat().format(totalVendedor));
		objContext.setVariable("totalImplementador", GeneralConfiguration.getInstance().getNumberFormat().format(totalImplementador));
		objContext.setVariable("totalAcumuladoComision", GeneralConfiguration.getInstance().getNumberFormat().format(totalAcumuladoComision));
		objContext.setVariable("totalCuota", GeneralConfiguration.getInstance().getNumberFormat().format(totalQuota));
		
		objContext.setVariable("fechaInicio", fechaInicio.format(GeneralConfiguration.getInstance().getDateFormatterNatural()));
		objContext.setVariable("fechaFin", fechaFin.format(GeneralConfiguration.getInstance().getDateFormatterNatural()));
		objContext.setVariable("mesActual", GeneralConfiguration.getInstance().getCurrentMonthNatural(fechaInicio.getMonthValue()));

		
		return pdfComponent.generate(path_file, objTemplates.FOUNDATION_PDF, objContext);		
	}

	@RequestMapping(value = {"reporte-cobranza-pdf", "reporte-cobranza-pdf/{fecha}/{empresa}/{usuario}"}, method = RequestMethod.GET, produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<InputStreamResource> previewPdfCobranza(@PathVariable(value="fecha", required=false) String fecha,
																	 @PathVariable(value="empresa", required=false) Integer idEmpresa,
																	 @PathVariable(value="usuario", required=false) Integer idUsuario) throws IOException, DocumentException {
		
		LocalDate fechaMesInicio = null;
		LocalDate fechaMesFin = null;
		
		UsuarioEntity objUsuario = null;
		EmpresaEntity objEmpresa = empresaService.findByIdEmpresa(idEmpresa);
		
		//USUARIO
		if((idUsuario != null && idUsuario >= 1) && sessionService.hasRol("COTIZACIONES_COBRANZA")) {
			objUsuario = usuarioService.findByIdUsuario(idUsuario);
		} else {
			objUsuario = sessionService.getCurrentUser();
		}
		
		 
		//PERIODO
		if(!fecha.equals("") && fecha.contains("-")) {
			
			String[] arrFecha = fecha.split("-");
			int year = Integer.parseInt(arrFecha[2]);
			int month = Integer.parseInt(arrFecha[1]);
			int day = Integer.parseInt(arrFecha[0]);
			
			fechaMesInicio = LocalDate.of(year, month, day).withDayOfMonth(1);
			fechaMesFin = LocalDate.of(year, month, day).withDayOfMonth(LocalDate.of(year, month, day).lengthOfMonth());
			
		} else {			
			fechaMesInicio = LocalDate.now().withDayOfMonth(1);
			fechaMesFin = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());
		}
		
		//TOTALES DEL REPORTE DE COMISION
		BigDecimal totalAcumuladoComision = new BigDecimal(0);
		
		final LocalDate fechaInicio = fechaMesInicio;
		final LocalDate fechaFin = fechaMesFin;
		
		List<CotizacionEntity> lstCotizaciones = cotizacionService.listCotizacionesCobradasPeriodo(fechaInicio, fechaFin, objUsuario, objEmpresa);
		List<Map<String , String>> myMap  = new ArrayList<Map<String,String>>();
		Integer iterator = 0;
		
		
		for(CotizacionEntity itemCotizacion: lstCotizaciones) {
			
			Map<String,String> myMap1 = new HashMap<String, String>();
			
			/*COMISION DEL USUARIO */
			CotizacionComisionEntity objComision = cotizacionComisionService.comisionUsuarioCotizacionCobradaPeriodo(objUsuario, itemCotizacion, fechaInicio, fechaFin);					
			
			BigDecimal comisionCobranza = new BigDecimal(0);			
			String comisionCobranzaNatural = "-";
			String porcentajeCobranzaNatural = "-";
			
			
			if(objComision != null) {
				
				if(objComision.getUsuarioCobranza() != null) {
					if(objComision.getUsuarioCobranza().getIdUsuario() == objUsuario.getIdUsuario()){
						comisionCobranza = objComision.getComisionCobranza();
						comisionCobranzaNatural = GeneralConfiguration.getInstance().getNumberFormat().format(objComision.getComisionCobranza());
						porcentajeCobranzaNatural = objComision.getPorcentajeCobranzaNatural();
					}
				}	
			}
			
			//TOTAL DE COMISIÓN GENERADA POR LA COTIZACIÓN
			totalAcumuladoComision 	= totalAcumuladoComision.add(comisionCobranza);		
			
			myMap1.put("folio", itemCotizacion.getFolio());	
			myMap1.put("fecha_aprobacion", itemCotizacion.getAprobacionFechaNatural());
			myMap1.put("dias_credito", itemCotizacion.getDiasCredito());
			myMap1.put("fecha_cobranza", itemCotizacion.getInicioCobranzaFechaNatural());
			myMap1.put("fecha_pago", itemCotizacion.getPagoFechaNatural());
			myMap1.put("porcentaje_cobranza", porcentajeCobranzaNatural);
			myMap1.put("comision_cobranza", comisionCobranzaNatural);
			
			myMap.add(iterator,myMap1);
			
			iterator++;						
		};
		
		
		LocalDateTime ldtNow = LocalDateTime.now();
		Templates objTemplates = new Templates();
		
		String path_file = ldtNow.getYear() + "_" + ldtNow.getMonthValue() + "_" + ldtNow.getDayOfMonth() + "_REPORTE_COBRANZA_2DO_NIVEL.pdf";
				
		Context objContext = new Context();
		objContext.setVariable("_TEMPLATE_", Templates.PDF_REPORTE_COMISION_COBRANZA);
		objContext.setVariable("title", "Reporte de Comisión de Cobranza");
		
		objContext.setVariable("objUsuario", objUsuario);
		objContext.setVariable("objEmpresa", objEmpresa);
		objContext.setVariable("rolCotizacionCobranza", sessionService.hasRol("COTIZACIONES_COBRANZA"));
		
		objContext.setVariable("cotizacionesMap", myMap);		
		objContext.setVariable("totalAcumuladoComision", GeneralConfiguration.getInstance().getNumberFormat().format(totalAcumuladoComision));
		
		objContext.setVariable("fechaInicio", fechaInicio.format(GeneralConfiguration.getInstance().getDateFormatterNatural()));
		objContext.setVariable("fechaFin", fechaFin.format(GeneralConfiguration.getInstance().getDateFormatterNatural()));
		objContext.setVariable("mesActual", GeneralConfiguration.getInstance().getCurrentMonthNatural(fechaInicio.getMonthValue()));

		
		return pdfComponent.generate(path_file, objTemplates.FOUNDATION_PDF, objContext);		
	}

	@RequestMapping(value = {"reporte-cotizaciones-correo/{tipoReporte}", "/reporte-cotizaciones-correo/{tipoReporte}"}, method = RequestMethod.GET)
	public @ResponseBody String recalculateCotizacion(	@PathVariable("tipoReporte") int paramTipoReporte,								
														RedirectAttributes objRedirectAttributes) {
		
		Boolean respuesta = false;
		String titulo = "";
		String mensaje = "";
		
		if(paramTipoReporte < 1) {
			
			List<CotizacionEntity> lstCotizaciones = cotizacionService.lstCotizacionesNoCobradas();
			List<Map<String , String>> mapCotizacionesFiltradas  = new ArrayList<Map<String,String>>();
			Integer iterator = 0;
			BigDecimal totalCotizaciones = new BigDecimal(0);
			
			LocalDate ldNow = LocalDate.now();		
			
			for(CotizacionEntity itemCotizacion : lstCotizaciones) {
				
				Map<String,String> myMap1 = new HashMap<String, String>();
				if(itemCotizacion.getAprobacionFecha() != null) {
					
					int diff = (int) ChronoUnit.DAYS.between(itemCotizacion.getAprobacionFecha(), ldNow);
					
					if(diff > Integer.parseInt(itemCotizacion.getDiasCredito().trim())) {
						myMap1.put("folio", itemCotizacion.getFolio());
						myMap1.put("fecha_aprobacion", itemCotizacion.getAprobacionFechaNatural());
						myMap1.put("dias_credito", itemCotizacion.getDiasCredito());
						myMap1.put("fecha_factura", itemCotizacion.getFacturacionFechaNatural());
						myMap1.put("factura", itemCotizacion.getFacturaNumero());
						myMap1.put("cliente", itemCotizacion.getCliente().getCliente());
						myMap1.put("total", itemCotizacion.getTotalNatural());
						
						mapCotizacionesFiltradas.add(iterator, myMap1);
						
						iterator++;
						totalCotizaciones = totalCotizaciones.add(itemCotizacion.getTotal());
					}
				}
			}
			
			if(!mapCotizacionesFiltradas.isEmpty() ) {				
				try {
					Map<String, Object> mapVariables = new HashMap<String, Object>();
					mapVariables.put("lstCotizaciones", mapCotizacionesFiltradas);
					mapVariables.put("total_cotizaciones_natural", GeneralConfiguration.getInstance().getNumberFormat().format(totalCotizaciones));
					mapVariables.put("empresa", "");
					mapVariables.put("titulo", "Cotizaciones pendientes por cobrar");
					mapVariables.put("alias", sessionService.getCurrentUser().getAlias());
					mailerComponent.send(sessionService.getCurrentUser().getCorreo(), "Hay cotizaciones con falta de pago", Templates.EMAIL_COTIZACIONES_POR_COBRAR, mapVariables);
					
					respuesta = true;
					titulo = "Enviado!";
					mensaje = "Revisa tu bandeja de entrada, el reporte ya debe de estar por llegar.";
				} catch(Exception exception) {
					respuesta = false;
					titulo = "Error!";
					mensaje = "Ocurrió un error al generar el correo.";
				}
			} else {
				respuesta = false;
				titulo = "Aviso";
				mensaje = "No existen cotizaciones vencidas.";
			}						
		} else if(paramTipoReporte < 2) {
			try {				
				LocalDate ldtFechaCondicional = LocalDate.now().minusDays(15);
				
				//LISTA DE APROBADOS
				List<CotizacionEntity> lstAprobados = cotizacionService.listCotizacionesAprobados(ldtFechaCondicional, 3);
				
				Map<String, Object> mapVariables = new HashMap<String, Object>();
				mapVariables.put("lstCotizaciones", lstAprobados);
				mapVariables.put("objUsuario", sessionService.getCurrentUser());
				mapVariables.put("descripcion", "Las siguientes cotizaciones se encuentran en estatus de aprobadas, están a la espera de ser facturadas");
				mapVariables.put("tipoFecha", "Aprobación");
				
				mailerComponent.send(sessionService.getCurrentUser().getCorreo(), "Hay cotizaciones pendientes de facturar", Templates.EMAIL_HOME_COTIZACIONES, mapVariables);
				
				respuesta = true;
				titulo = "Enviado!";
				mensaje = "Revisa tu bandeja de entrada, el reporte ya debe de estar por llegar.";
			} catch(Exception exception) {
				respuesta = false;
				titulo = "Error!";
				mensaje = "Ocurrió un error al generar el correo.";
			}
		} else {
			try {				
				LocalDate ldtFechaCondicional = LocalDate.now().minusDays(15);
				
				//LISTA DE FACTURADOS
				List<CotizacionEntity> lstFacturados = cotizacionService.listCotizacionesFacturados(ldtFechaCondicional, 3);
				
				Map<String, Object> mapVariables = new HashMap<String, Object>();
				mapVariables.put("lstCotizaciones", lstFacturados);
				mapVariables.put("objUsuario", sessionService.getCurrentUser());
				mapVariables.put("descripcion", "Las siguientes cotizaciones se encuentran en estatus de facturadas, están a la espera de ser pagadas");
				mapVariables.put("tipoFecha", "Facturación");
				
				mailerComponent.send(sessionService.getCurrentUser().getCorreo(), "Hay cotizaciones pendientes de pagar", Templates.EMAIL_HOME_COTIZACIONES, mapVariables);
				
				respuesta = true;
				titulo = "Enviado!";
				mensaje = "Revisa tu bandeja de entrada, el reporte ya debe de estar por llegar.";
			} catch(Exception exception) { 
				respuesta = false;
				titulo = "Error!";
				mensaje = "Ocurrió un error al generar el correo.";
			}
		}
		
		JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
		jsonReturn	.add("respuesta", respuesta)
					.add("titulo", titulo)
					.add("mensaje", mensaje);
		
		return jsonReturn.build().toString();		
	}

}