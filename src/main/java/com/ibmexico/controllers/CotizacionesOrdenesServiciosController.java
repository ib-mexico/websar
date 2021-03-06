package com.ibmexico.controllers;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import org.thymeleaf.context.Context;

import com.ibmexico.components.ModelAndViewComponent;
import com.ibmexico.components.PdfComponent;
import com.ibmexico.entities.ClienteEntity;
import com.ibmexico.entities.CotizacionEntity;
import com.ibmexico.entities.OrdenServicioEntity;
import com.ibmexico.entities.OrdenServicioPartidaEntity;
import com.ibmexico.libraries.DataTable;
import com.ibmexico.libraries.Templates;
import com.ibmexico.libraries.notifications.ApplicationException;
import com.ibmexico.libraries.notifications.EnumException;
import com.ibmexico.libraries.notifications.EnumMessage;
import com.ibmexico.services.ClienteContactoService;
import com.ibmexico.services.ClienteService;
import com.ibmexico.services.CotizacionService;
import com.ibmexico.services.OrdenServicioPartidaService;
import com.ibmexico.services.OrdenServicioService;
import com.ibmexico.services.SessionService;
import com.lowagie.text.DocumentException;
import com.ibmexico.libraries.Formats;

@Controller
@RequestMapping("controlPanel/cotizaciones")
public class CotizacionesOrdenesServiciosController {

	@Autowired
	@Qualifier("modelAndViewComponent")
	private ModelAndViewComponent modelAndViewComponent;
	
	@Autowired
	@Qualifier("pdfComponent")
	private PdfComponent pdfComponent;
	
	@Autowired
	@Qualifier("ordenServicioService")
	private OrdenServicioService ordenServicioService;
	
	@Autowired
	@Qualifier("ordenServicioPartidaService")
	private OrdenServicioPartidaService ordenServicioPartidaService;
	
	@Autowired
	@Qualifier("cotizacionService")
	private CotizacionService cotizacionService;
	
	@Autowired
	@Qualifier("clienteService")
	private ClienteService clienteService;
	
	@Autowired
	@Qualifier("clienteContactoService")
	private ClienteContactoService clienteContactoService;
	
	@Autowired
	@Qualifier("sessionService")
	private SessionService sessionService;
	
	@GetMapping({"{paramIdCotizacion}/ordenesServicios", "{paramIdCotizacion}/ordenesServicios/"})
	public ModelAndView index(@PathVariable("paramIdCotizacion") int paramIdCotizacion) {
		
		CotizacionEntity objCotizacion = cotizacionService.findByIdCotizacion(paramIdCotizacion);
		
		ModelAndView objModelAndView = modelAndViewComponent.createModelAndViewControlPanel(Templates.CONTROL_PANEL_COTIZACIONES_ORDENES_SERVICIOS_INDEX);
		objModelAndView.addObject("objCotizacion", objCotizacion);
		
		return objModelAndView;
	}
	
	@RequestMapping(value = "{paramIdCotizacion}/ordenesServicios/table", method = RequestMethod.POST)	
	public @ResponseBody String table(	@PathVariable("paramIdCotizacion") int paramIdCotizacion,
										@RequestParam("offset") int offset,
										@RequestParam("limit") int limit,
										@RequestParam("_csrf") String _csrf,
										@RequestParam(value="search", required=false, defaultValue="") String search,
										@RequestParam(value="txtBootstrapTableDesde", required=false) String txtBootstrapTableDesde,
										@RequestParam(value="txtBootstrapTableHasta", required=false) String txtBootstrapTableHasta) {
		
		DataTable<OrdenServicioEntity> dtOrdenes = ordenServicioService.dataTable(paramIdCotizacion, search, offset, limit, txtBootstrapTableDesde, txtBootstrapTableHasta);
		
		JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
		jsonReturn.add("total", dtOrdenes.getTotal());
		JsonArrayBuilder jsonRows = Json.createArrayBuilder();
		
		dtOrdenes.getRows().forEach((itemOrden)-> {

			jsonRows.add(Json.createObjectBuilder()
				.add("idOrdenServicio", itemOrden.getIdOrdenServicio())
				.add("folio", itemOrden.getFolio())
				.add("cliente", itemOrden.getCliente().getCliente())
				.add("contacto", itemOrden.getClienteContacto().getContacto())
				
				.add("subtotal", itemOrden.getSubtotalNatural())
				.add("nombreElabora", itemOrden.getUsuarioElabora().getNombreCompleto())
				.add("nombreRecibe", itemOrden.getNombreRecibe())
				
				.add("firmaRevisa", itemOrden.getUrlFirmaRevisa())
				
				.add("fechaInicio", itemOrden.getInicioFechaFullNatural())
				.add("fechaEntrega", itemOrden.getEntregaFechaFullNatural())
			);
		});

		jsonReturn.add("rows", jsonRows);

		return jsonReturn.build().toString();
	}
	
	
	@GetMapping({"{paramIdCotizacion}/ordenesServicios/create", "{paramIdCotizacion}/ordenesServicios/create/"})
	public ModelAndView create(@PathVariable("paramIdCotizacion") int paramIdCotizacion) {
		
		CotizacionEntity objCotizacion = cotizacionService.findByIdCotizacion(paramIdCotizacion);
		List<ClienteEntity> lstClientes = clienteService.listClientesActivos();
		
		ModelAndView objModelAndView = modelAndViewComponent.createModelAndViewControlPanel(Templates.CONTROL_PANEL_COTIZACIONES_ORDENES_SERVICIOS_CREATE);
		objModelAndView.addObject("objCotizacion", objCotizacion);
		objModelAndView.addObject("lstClientes", lstClientes);
		
		return objModelAndView;
	}
	
	@RequestMapping(value = "{paramIdCotizacion}/ordenesServicios", method = RequestMethod.POST)
	public RedirectView store(@PathVariable("paramIdCotizacion") int paramIdCotizacion,
								@RequestParam("hddIdCotizacion") int hddIdCotizacion,
								@RequestParam("cmbCliente") int cmbCliente,
								@RequestParam("cmbClienteContacto") int cmbClienteContacto,
								@RequestParam("txtFechaInicio") String txtFechaInicio,
								@RequestParam("txtFechaEntrega") String txtFechaEntrega,
								@RequestParam(value="txtTiempoEspera", required=false, defaultValue="") String txtTiempoEspera,
								@RequestParam(value="txtOSTicket", required=false, defaultValue="") String txtOSTicket,
								@RequestParam(value="txtAlcanceServicios") String txtAlcanceServicios,
								@RequestParam(value="txtBitacoraActividades") String txtBitacoraActividades,
								@RequestParam(value="txtCantidad[]") String[] txtCantidad,
								@RequestParam(value="txtDescripcion[]") String[] txtDescripcion,
								@RequestParam(value="txtNumeroParte[]") String[] txtNumeroParte,								
								@RequestParam(value="txtPrecioUnitario[]") String[] txtPrecioUnitario,
								@RequestParam(value="txtImporte[]") String[] txtImporte,
								@RequestParam(value="imgFirmaRecepcion") String imgFirmaRecepcion,
								@RequestParam(value="imgFirmaEntrega") String imgFirmaEntrega,
								@RequestParam(value="txtNombreRecibe") String txtNombreRecibe,
								@RequestParam(value="txtPuestoRecibe") String txtPuestoRecibe,
								@RequestParam(value="txtTelefonoRecibe") String txtTelefonoRecibe,
								@RequestParam(value="txtCorreoRecibe") String txtCorreoRecibe,
								RedirectAttributes objRedirectAttributes) {
		
		RedirectView objRedirectView = null;
		CotizacionEntity objCotizacion = cotizacionService.findByIdCotizacion(hddIdCotizacion);
		
		try {
			
			if(objCotizacion != null) {
				
				OrdenServicioEntity objOrdenServicio = new OrdenServicioEntity();
				
				objOrdenServicio.setCotizacion(objCotizacion);
				objOrdenServicio.setCliente(clienteService.findByIdCliente(cmbCliente));
				objOrdenServicio.setClienteContacto(clienteContactoService.findByIdClienteContacto(cmbClienteContacto));
				objOrdenServicio.setInicioFecha(Formats.getInstance().toLocalDateTime(txtFechaInicio));
				objOrdenServicio.setEntregaFecha(Formats.getInstance().toLocalDateTime(txtFechaEntrega));
				objOrdenServicio.setTiempoEspera(txtTiempoEspera);
				objOrdenServicio.setOsTicket(txtOSTicket);
				objOrdenServicio.setAlcanceServicios(txtAlcanceServicios);
				objOrdenServicio.setBitacoraActividades(txtBitacoraActividades);
				objOrdenServicio.setUsuarioElabora(sessionService.getCurrentUser());
				objOrdenServicio.setNombreRecibe(txtNombreRecibe);
				objOrdenServicio.setPuestoRecibe(txtPuestoRecibe);
				objOrdenServicio.setCorreoRecibe(txtCorreoRecibe);
				objOrdenServicio.setTelefonoRecibe(txtTelefonoRecibe);
				objOrdenServicio.setSubtotal(new BigDecimal(0));
				objOrdenServicio.setUrlFirmaRevisa(" ");
				
				ordenServicioService.create(objOrdenServicio, txtCantidad, txtDescripcion, txtNumeroParte, txtPrecioUnitario, txtImporte, imgFirmaEntrega, imgFirmaRecepcion);
				objRedirectView = new RedirectView("/WebSar/controlPanel/cotizaciones/" + hddIdCotizacion + "/ordenesServicios");
				modelAndViewComponent.addResult(objRedirectAttributes, EnumMessage.COTIZACIONES_ORDENES_SERVICIOS_CREATE_001);
				
			} else {
				throw new ApplicationException(EnumException.COTIZACIONES_ORDENES_SERVICIOS_CREATE_001);
			}
			
		} catch(ApplicationException exception) {
			objRedirectView = new RedirectView("/WebSar/controlPanel/cotizaciones/" + hddIdCotizacion + "/ordenesServicios/create");
			modelAndViewComponent.addResult(objRedirectAttributes, exception);
		}
		
		return objRedirectView;
	}
	
	@GetMapping({"{paramIdCotizacion}/ordenesServicios/{paramIdOrdenServicio}/revision", "{paramIdCotizacion}/ordenesServicios/{paramIdOrdenServicio}/revision/"})
	public ModelAndView revision(@PathVariable("paramIdCotizacion") int paramIdCotizacion,
									@PathVariable("paramIdOrdenServicio") int paramIdOrdenServicio) {
		
		OrdenServicioEntity objOrdenServicio = ordenServicioService.findByIdOrdenServicio(paramIdOrdenServicio);
		List<OrdenServicioPartidaEntity> lstPartidas = ordenServicioPartidaService.listOrdenServicioPartidas(paramIdOrdenServicio);
		CotizacionEntity objCotizacion = cotizacionService.findByIdCotizacion(paramIdCotizacion);
		
		ModelAndView objModelAndView = modelAndViewComponent.createModelAndViewControlPanel(Templates.CONTROL_PANEL_COTIZACIONES_ORDENES_SERVICIOS_REVIEW);
		objModelAndView.addObject("objCotizacion", objCotizacion);
		objModelAndView.addObject("objOrdenServicio", objOrdenServicio);
		objModelAndView.addObject("lstPartidas", lstPartidas);
		
		return objModelAndView;
	}
	
	@RequestMapping(value = "{paramIdCotizacion}/ordenesServicios/{paramIdOrdenServicio}/revision", method = RequestMethod.PUT)
	public RedirectView store(@PathVariable("paramIdCotizacion") int paramIdCotizacion,
								@PathVariable("paramIdOrdenServicio") int paramIdOrdenServicio,
								@RequestParam("hddIdCotizacion") int hddIdCotizacion,
								@RequestParam("hddIdOrdenServicio") int hddIdOrdenServicio,
								@RequestParam(value="imgFirmaRevision") String imgFirmaRevision,
								RedirectAttributes objRedirectAttributes) {
		
		RedirectView objRedirectView = null;
		OrdenServicioEntity objOrdenServicio = ordenServicioService.findByIdOrdenServicio(hddIdOrdenServicio);
		
		try {
			
			if(objOrdenServicio != null) {				
				
				objOrdenServicio.setUsuarioRevisa(sessionService.getCurrentUser());
				
				ordenServicioService.update(objOrdenServicio, imgFirmaRevision);
				objRedirectView = new RedirectView("/WebSar/controlPanel/cotizaciones/" + hddIdCotizacion + "/ordenesServicios");
				modelAndViewComponent.addResult(objRedirectAttributes, EnumMessage.COTIZACIONES_ORDENES_SERVICIOS_REVISION_UPDATE_001);
				
			} else {
				throw new ApplicationException(EnumException.COTIZACIONES_ORDENES_SERVICIOS_UPDATE_001);
			}
			
		} catch(ApplicationException exception) {
			objRedirectView = new RedirectView("/WebSar/controlPanel/cotizaciones/" + hddIdCotizacion + "/ordenesServicios/create");
			modelAndViewComponent.addResult(objRedirectAttributes, exception);
		}
		
		return objRedirectView;
	}
	
	@RequestMapping(value = {"{paramIdCotizacion}/ordenesServicios/{paramIdOrdenServicio}/pdf", "{paramIdCotizacion}/ordenesServicios/{paramIdOrdenServicio}/pdf/"}, method = RequestMethod.GET, produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<InputStreamResource> previewPdf(@PathVariable("paramIdCotizacion") int paramIdCotizacion,
															@PathVariable("paramIdOrdenServicio") int paramIdOrdenServicio) throws IOException, DocumentException {
		
		CotizacionEntity objCotizacion = cotizacionService.findByIdCotizacion(paramIdCotizacion);
		OrdenServicioEntity objOrdenServicio = ordenServicioService.findByIdOrdenServicio(paramIdOrdenServicio);
		List<OrdenServicioPartidaEntity> lstPartidas = ordenServicioPartidaService.listOrdenServicioPartidas(paramIdOrdenServicio);
		
		LocalDateTime ldtNow = LocalDateTime.now();
		Templates objTemplates = new Templates();
		
		String path_file = ldtNow.getYear() + "_" + ldtNow.getMonthValue() + "_" + ldtNow.getDayOfMonth() + "_" + objOrdenServicio.getFolio() + ".pdf";
				
		Context objContext = new Context();
		objContext.setVariable("_TEMPLATE_", Templates.PDF_ORDEN_SERVICIO);
		objContext.setVariable("title", "Orden de Servicio: " + objOrdenServicio.getFolio());
		objContext.setVariable("objCotizacion", objCotizacion);
		objContext.setVariable("objOrdenServicio", objOrdenServicio);
		objContext.setVariable("lstPartidas", lstPartidas);
		
		return pdfComponent.generate(path_file, objTemplates.FOUNDATION_PDF, objContext);		
	}
}
