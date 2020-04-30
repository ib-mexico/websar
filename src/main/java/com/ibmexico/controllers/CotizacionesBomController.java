package com.ibmexico.controllers;

import java.time.LocalDate;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;

import com.ibmexico.components.MailerComponent;
import com.ibmexico.components.ModelAndViewComponent;
import com.ibmexico.components.PdfComponent;
import com.ibmexico.entities.CotizacionEntity;
import com.ibmexico.entities.CotizacionEstatusEntity;
import com.ibmexico.libraries.DataTable;
import com.ibmexico.libraries.Templates;
import com.ibmexico.services.ClienteContactoService;
import com.ibmexico.services.ClienteGiroService;
import com.ibmexico.services.ClienteService;
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
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequestMapping("controlPanel/cotizacionesBom")
public class CotizacionesBomController{
    
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
    private MailerComponent mailerComponent;
    
    //COTIZACIONES SERVICIOS ADMINISTRADOS 
    @GetMapping(value={"","/"})
    public ModelAndView index() {
        List<CotizacionEstatusEntity> lstCotizacionEstatus = cotizacionEstatusService.listAll();
		
		ModelAndView objModelAndView = modelAndViewComponent.createModelAndViewControlPanel(Templates.CONTROL_PANEL_COTIZACIONES_INDEX_BOM);
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
	
	@RequestMapping(value = "/tableBom", method = RequestMethod.POST)	
	public @ResponseBody String tableBom(	@RequestParam("offset") int offset,
										@RequestParam("limit") int limit,
										@RequestParam("_csrf") String _csrf,
										@RequestParam(value="search", required=false, defaultValue="") String search,
										@RequestParam(value="txtBootstrapTableDesde", required=false) String txtBootstrapTableDesde,
										@RequestParam(value="txtBootstrapTableHasta", required=false) String txtBootstrapTableHasta) {
		
		DataTable<CotizacionEntity> dtCotizaciones = cotizacionService.dataTableBom(search, offset, limit, txtBootstrapTableDesde, txtBootstrapTableHasta);
		
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
			LocalDate ldtInicioCalidad =  LocalDate.of(2020, 03, 31);
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

				.add("calidad",cotizaFicheroService.countCotizacionFicheroCalidad(itemCotizacion.getIdCotizacion())>0 ? 1: 0 )
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
			);
		});

		jsonReturn.add("rows", jsonRows);

		return jsonReturn.build().toString();
	}		
	
}