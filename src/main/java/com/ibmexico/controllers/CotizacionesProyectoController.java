package com.ibmexico.controllers;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;

import com.ibmexico.components.MailerComponent;
import com.ibmexico.components.ModelAndViewComponent;
import com.ibmexico.components.PdfComponent;
import com.ibmexico.configurations.GeneralConfiguration;
import com.ibmexico.entities.CotizacionEntity;
import com.ibmexico.entities.CotizacionEstatusEntity;
import com.ibmexico.entities.CotizacionPartidaEntity;
import com.ibmexico.libraries.DataTable;
import com.ibmexico.libraries.Templates;
import com.ibmexico.libraries.notifications.ApplicationException;
import com.ibmexico.libraries.notifications.EnumException;
import com.ibmexico.services.ClienteContactoService;
import com.ibmexico.services.ClienteGiroService;
import com.ibmexico.services.ClienteService;
import com.ibmexico.services.ConfiguracionService;
import com.ibmexico.services.CotizacionComisionService;
import com.ibmexico.services.CotizacionEstatusService;
import com.ibmexico.services.CotizacionFicheroService;
import com.ibmexico.services.CotizacionPartidaService;
import com.ibmexico.services.CotizacionService;
import com.ibmexico.services.CotizacionTipoFicheroService;
import com.ibmexico.services.CotizacionUsuarioQuotaService;
import com.ibmexico.services.EmpresaService;
import com.ibmexico.services.FormaPagoService;
import com.ibmexico.services.MonedaService;
import com.ibmexico.services.OportunidadNegocioService;
import com.ibmexico.services.RolesService;
import com.ibmexico.services.SessionService;
import com.ibmexico.services.SucursalService;
import com.ibmexico.services.UsuarioRolService;
import com.ibmexico.services.UsuarioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequestMapping("controlPanel/cotizacionesProyecto")
public class CotizacionesProyectoController{
    
    @Autowired
    @Qualifier("modelAndViewComponent")
    private ModelAndViewComponent modelAndViewComponent;

    @Autowired
    @Qualifier("pdfComponent")
    private PdfComponent pdfComponent;

	@Autowired
	@Qualifier("cotizacionService")
	private CotizacionService cotizacionService;
	
	@Autowired
	@Qualifier("cotizacionComisionService")
	private CotizacionComisionService cotizacionComisionService;
	
	@Autowired
	@Qualifier("cotizacionUsuarioQuotaService")
	private CotizacionUsuarioQuotaService cotizacionUsuarioQuotaService;
	
	@Autowired
	@Qualifier("cotizacionPartidaService")
	private CotizacionPartidaService cotizacionPartidaService;
	
	@Autowired
	@Qualifier("cotizacionEstatusService")
	private CotizacionEstatusService cotizacionEstatusService;
	
	@Autowired
	@Qualifier("empresaService")
	private EmpresaService empresaService;
	
	@Autowired
	@Qualifier("usuarioService")
	private UsuarioService usuarioService;
	
	@Autowired
	@Qualifier("sucursalService")
	private SucursalService sucursalService;
	
	@Autowired
	@Qualifier("clienteService")
	private ClienteService clienteService;
	
	@Autowired
	@Qualifier("clienteGiroService")
	private ClienteGiroService clienteGiroService;
	
	@Autowired
	@Qualifier("clienteContactoService")
	private ClienteContactoService clienteContactoService;
	
	@Autowired
	@Qualifier("oportunidadNegocioService")
	private OportunidadNegocioService oportunidadNegocioService;
	
	@Autowired
	@Qualifier("monedaService")
	private MonedaService monedaService;
	
	@Autowired
	@Qualifier("formaPagoService")
	private FormaPagoService formaPagoService;
	
	@Autowired
	@Qualifier("rolesService")
	private RolesService rolesService;

	@Autowired
	@Qualifier("cotizacionFicheroService")
	private CotizacionFicheroService cotizaFicheroService;

	@Autowired
	@Qualifier("cotizacionTipoFicheroService")
	private CotizacionTipoFicheroService cotizacionTipoFicheroService;

	@Autowired
	@Qualifier("usuarioRolService")
	private UsuarioRolService usuarioRolService;
	
	@Autowired
	@Qualifier("sessionService")
	private SessionService sessionService;

	@Autowired
	@Qualifier("configuracionService")
	private ConfiguracionService configuracionService;
    
    //COTIZACIONES SERVICIOS ADMINISTRADOS 
    @GetMapping(value={"","/"})
    public ModelAndView index() {
        List<CotizacionEstatusEntity> lstCotizacionEstatus = cotizacionEstatusService.listAll();
		
		ModelAndView objModelAndView = modelAndViewComponent.createModelAndViewControlPanel(Templates.CONTROL_PANEL_COTIZACIONES_INDEX_PROYECTO);
		objModelAndView.addObject("lstCotizacionEstatus", lstCotizacionEstatus);
		objModelAndView.addObject("rolCotizacionExpediente", sessionService.hasRol("COTIZACIONES_EXPEDIENTES"));
		objModelAndView.addObject("rolCotizacionCobranza", sessionService.hasRol("COTIZACIONES_COBRANZA"));
		objModelAndView.addObject("rolNuevaCotizacion", sessionService.hasRol("COTIZACIONES_CREATE"));
		objModelAndView.addObject("rolCotizacionAdmin", sessionService.hasRol("COTIZACIONES_ADMINISTRADOR"));
		objModelAndView.addObject("rolEntrega", sessionService.hasRol("ENTREGAS_CREATE"));
		objModelAndView.addObject("rolEntregaAdmin", sessionService.hasRol("ENTREGAS_ADMINISTRADOR"));
		objModelAndView.addObject("rolCalidad", sessionService.hasRol("LLAMADA_CALIDAD"));
		
		return objModelAndView;
    }
	
	@RequestMapping(value = "/tableProyecto", method = RequestMethod.POST)	
	public @ResponseBody String tableProyecto(	@RequestParam("offset") int offset,
										@RequestParam("limit") int limit,
										@RequestParam("_csrf") String _csrf,
										@RequestParam(value="search", required=false, defaultValue="") String search,
										@RequestParam(value="txtBootstrapTableDesde", required=false) String txtBootstrapTableDesde,
										@RequestParam(value="txtBootstrapTableHasta", required=false) String txtBootstrapTableHasta) {
		
		DataTable<CotizacionEntity> dtCotizaciones = cotizacionService.dataTableProyecto(search, offset, limit, txtBootstrapTableDesde, txtBootstrapTableHasta);
		
		JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
		jsonReturn.add("total", dtCotizaciones.getTotal());
		JsonArrayBuilder jsonRows = Json.createArrayBuilder();
		
		dtCotizaciones.getRows().forEach((itemCotizacion)-> {
			
			String ejecutivo = "";
			switch (itemCotizacion.getEmpresa().getIdEmpresa()) {
				case 1:
					if(itemCotizacion.getCliente().getUsuarioEjecutivo() != null) {
						ejecutivo = itemCotizacion.getCliente().getUsuarioEjecutivo().getNombreCompleto();
					}
					break;
				
				case 2:
					if(itemCotizacion.getCliente().getUsuarioEjecutivoS3s() != null) {
						ejecutivo = itemCotizacion.getCliente().getUsuarioEjecutivoS3s().getNombreCompleto();
					}
					break;
				
				case 3:
					if(itemCotizacion.getCliente().getUsuarioEjecutivoR2a() != null) {
						ejecutivo = itemCotizacion.getCliente().getUsuarioEjecutivoR2a().getNombreCompleto();
					}
					break;

			default:
				break;
			}
			LocalDate ldtInicioCalidad =  LocalDate.parse(configuracionService.getValue("INICIO_CALIDAD").toString(), GeneralConfiguration.getInstance().getDateFormatter());

			String arrFechaInicio[] = itemCotizacion.getCreacionFechaNatural().split("/");
			int yearInicio = Integer.parseInt(arrFechaInicio[2]);
			int monthInicio = Integer.parseInt(arrFechaInicio[1]);
			int dayInicio = Integer.parseInt(arrFechaInicio[0]);
			LocalDate fechaInicio = LocalDate.of(yearInicio, monthInicio, dayInicio);

			jsonRows.add(Json.createObjectBuilder()
				.add("idCotizacion", itemCotizacion.getIdCotizacion())
				.add("idEstatus", itemCotizacion.getCotizacionEstatus().getIdCotizacionEstatus())
				.add("estatus", itemCotizacion.getCotizacionEstatus().getCotizacionEstatus())
				.add("sucursal", itemCotizacion.getSucursal().getSucursal())

				// .add("calidad",cotizaFicheroService.countCotizacionFicheroCalidad(itemCotizacion.getIdCotizacion())>0 ? 1: 0 )
				.add("boolCalidad", itemCotizacion.isCalidad())
				.add("paseCalidad", fechaInicio.isAfter(ldtInicioCalidad) ? true : false)
				
				.add("folio", itemCotizacion.getFolio())
				.add("concepto", itemCotizacion.getConcepto())
				
				.add("subtotal", itemCotizacion.getSubtotalNatural())
				.add("iva", itemCotizacion.getIvaNatural())
				.add("total", itemCotizacion.getTotalNatural())
				
				.add("requisicion_folio", itemCotizacion.getRequisicionMaterialFolio())
				.add("usuario", itemCotizacion.getUsuario().getCorreo())
				.add("cliente", itemCotizacion.getCliente().getCliente())
				.add("ejecutivo", ejecutivo)
				
				.add("creacionFecha", itemCotizacion.getCreacionFechaNatural())

				.add("eliminado", itemCotizacion.isEliminado())

				/**Estatus de las cotizaciones*/
				.add("boolMaestra", itemCotizacion.isMaestra())
				.add("boolBoom", itemCotizacion.isBoom())
				.add("boolNormal", itemCotizacion.isNormal())
				.add("boolRenta", itemCotizacion.isRenta())
				.add("idProyecto", itemCotizacion.getIdProyecto() != null ? itemCotizacion.getIdProyecto().getIdCotizacion() : 0 )
				.add("boolActivoRenta", itemCotizacion.isActivoRenta())
				.add("numMesRenta", itemCotizacion.getNumeroMesRenta())
				.add("acumuladoRenta", itemCotizacion.getAcumuladoMes())

			);
		});

		jsonReturn.add("rows", jsonRows);

		return jsonReturn.build().toString();
	}		

	
	@RequestMapping(value = "{paramIdCotizacion}/generateRenta", method = RequestMethod.POST)
	public @ResponseBody String clone(@PathVariable("paramIdCotizacion") int paramIdCotizacion,
										@RequestParam(value="idCotizacion") int idCotizacion) {
		
		CotizacionEntity objCotizacion = cotizacionService.findByIdCotizacion(idCotizacion);
		Boolean respuesta = false;
		String titulo = "";
		String mensaje = "";
		
		try {
			
			if(objCotizacion != null) {
				objCotizacion.isActivoRenta();
				cotizacionService.update(objCotizacion);
				int acumulado = objCotizacion.getAcumuladoMes()+1;
				if (acumulado > objCotizacion.getNumeroMesRenta()) {
					respuesta = true;
					titulo = "Rentas no permitidas!";
					mensaje = "Se termino el limite de rentas asignada en el proyecto";	
				} else{
					objCotizacion.setAcumuladoMes(acumulado);
					cotizacionService.update(objCotizacion);
				// for (int i = 0; i < objCotizacion.getNumeroMesRenta(); i++) {
					CotizacionEntity objCotizacionNueva = new CotizacionEntity();
					objCotizacionNueva.setIdProyecto(objCotizacion);
					objCotizacionNueva.setSucursal(objCotizacion.getSucursal());
					objCotizacionNueva.setUsuario(sessionService.getCurrentUser());
					objCotizacionNueva.setEmpresa(objCotizacion.getEmpresa());
					objCotizacionNueva.setConcepto(objCotizacion.getConcepto());
					objCotizacionNueva.setSolicitudFecha(objCotizacion.getSolicitudFecha());
					objCotizacionNueva.setCliente(objCotizacion.getCliente());
					objCotizacionNueva.setClienteContacto(objCotizacion.getClienteContacto());
					objCotizacionNueva.setUbicacion(objCotizacion.getUbicacion());
					objCotizacionNueva.setEntregaLugar(objCotizacion.getEntregaLugar());
					objCotizacionNueva.setEntregaDiasHabiles(objCotizacion.getEntregaDiasHabiles());
					objCotizacionNueva.setVigenciaPrecioDiasHabiles(objCotizacion.getVigenciaPrecioDiasHabiles());
					objCotizacionNueva.setMoneda(objCotizacion.getMoneda());
					objCotizacionNueva.setFormaPago(objCotizacion.getFormaPago());
					objCotizacionNueva.setDiasCredito(objCotizacion.getDiasCredito());
					objCotizacionNueva.setCondicionesPago(objCotizacion.getCondicionesPago());
					objCotizacionNueva.setObservaciones(objCotizacion.getObservaciones());
					objCotizacionNueva.setCotizacionEstatus(cotizacionEstatusService.findByIdCotizacionEstatus(2));

					objCotizacionNueva.setUsuarioVendedor(objCotizacion.getUsuarioVendedor());
					objCotizacionNueva.setMaestra(false);
					objCotizacionNueva.setRenta(true);
					objCotizacionNueva.setNormal(false);
					objCotizacionNueva.setBoom(false);
					
					/* Orden de las cotizaciones */
					objCotizacionNueva.setOrdenMes(acumulado);
					
					if(objCotizacion.getOportunidadNegocio() != null) {
						objCotizacionNueva.setOportunidadNegocio(objCotizacion.getOportunidadNegocio());
					}
					
					objCotizacionNueva.setSubtotal(objCotizacion.getSubtotal());
					objCotizacionNueva.setIvaPorcentaje(objCotizacion.getIvaPorcentaje());
					objCotizacionNueva.setIva(objCotizacion.getIva());
					objCotizacionNueva.setTotal(objCotizacion.getTotal());
					
					cotizacionService.cloneRenta(objCotizacion, objCotizacionNueva, acumulado);
				// }
				respuesta = true;
				titulo = "Renta generada!";
				mensaje = "La rentaa has sido generada exitosamente.";	
			}
			} else {
				throw new ApplicationException(EnumException.COTIZACIONES_CLONE_002);
			}
			
		} catch(ApplicationException exception) {
			throw new ApplicationException(EnumException.COTIZACIONES_CLONE_001);
		}
								
		JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
		jsonReturn	.add("respuesta", respuesta)
					.add("titulo", titulo)
					.add("mensaje", mensaje);
		
		return jsonReturn.build().toString();
	}


	//OBTENER CLIENTES MEDIANTE AJAX
	@RequestMapping(value = "{paramIdCotizacion}/getProyecto", method = RequestMethod.GET)
	public @ResponseBody String getProyecto(@PathVariable("paramIdCotizacion") int paramIdCotizacion) {
		
		JsonArrayBuilder jsonRows = Json.createArrayBuilder();

		List<CotizacionPartidaEntity> lstPartidas = cotizacionPartidaService.listCotizacionesPartidas(paramIdCotizacion);
		CotizacionEntity objCotizacion = cotizacionService.findByIdCotizacion(paramIdCotizacion);
		BigDecimal totalAcumulado = BigDecimal.ZERO;
		if (lstPartidas != null) {
			for (int i = 0; i < lstPartidas.size(); i++) {
				totalAcumulado = totalAcumulado.add(lstPartidas.get(i).getPrecioUnitarioLista()
					.subtract(lstPartidas.get(i).getPrecioUnitarioLista().divide(new BigDecimal(100))
						.multiply(lstPartidas.get(i).getDescuentoPorcentaje()))
					.multiply(BigDecimal.valueOf(1)));
			}
		}

		jsonRows.add(Json.createObjectBuilder().
			add("idCotizacion", lstPartidas.get(0).getCotizacion().getIdCotizacion()).
			add("totalAcumulado", totalAcumulado != null ? totalAcumulado : BigDecimal.ZERO).
			add("mesRenta", lstPartidas.get(0).getCantidad()).
			add("subtotalProyecto", objCotizacion.getSubtotal())
		);
		return jsonRows.build().toString();
	}

}