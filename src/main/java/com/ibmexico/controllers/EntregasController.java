package com.ibmexico.controllers;

import java.io.IOException;
import java.time.LocalDate;
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

import com.ibmexico.libraries.notifications.ApplicationException;
import com.ibmexico.libraries.notifications.EnumException;
import com.ibmexico.libraries.notifications.EnumMessage;
import com.ibmexico.components.ModelAndViewComponent;
import com.ibmexico.components.PdfComponent;
import com.ibmexico.configurations.GeneralConfiguration;
import com.ibmexico.entities.ClienteContactoEntity;
import com.ibmexico.entities.ClienteEntity;
import com.ibmexico.entities.ClienteGiroEntity;
import com.ibmexico.entities.CotizacionEntity;
import com.ibmexico.entities.EmpresaEntity;
import com.ibmexico.entities.EntregaEntity;
import com.ibmexico.entities.EntregaProductoEntity;
import com.ibmexico.entities.SucursalEntity;
import com.ibmexico.entities.UsuarioEntity;
import com.ibmexico.libraries.DataTable;
import com.ibmexico.libraries.Templates;
import com.ibmexico.services.ClienteContactoService;
import com.ibmexico.services.ClienteGiroService;
import com.ibmexico.services.ClienteService;
import com.ibmexico.services.CotizacionService;
import com.ibmexico.services.EmpresaService;
import com.ibmexico.services.EntregaProductoService;
import com.ibmexico.services.EntregaService;
import com.ibmexico.services.SessionService;
import com.ibmexico.services.SucursalService;
import com.ibmexico.services.UsuarioService;
import com.lowagie.text.DocumentException;

@Controller
@RequestMapping("controlPanel/entregas")
public class EntregasController {

	@Autowired
	@Qualifier("modelAndViewComponent")
	private ModelAndViewComponent modelAndViewComponent;
	
	@Autowired
	@Qualifier("pdfComponent")
	private PdfComponent pdfComponent;
	
	@Autowired
	@Qualifier("entregaService")
	private EntregaService entregaService;
	
	@Autowired
	@Qualifier("entregaProductoService")
	private EntregaProductoService entregaProductoService;
	
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
	@Qualifier("cotizacionService")
	private CotizacionService cotizacionService;
	
	@Autowired
	@Qualifier("sessionService")
	private SessionService sessionService;
	
	@GetMapping({"", "/"})
	public ModelAndView index() {		
		ModelAndView objModelAndView = modelAndViewComponent.createModelAndViewControlPanel(Templates.CONTROL_PANEL_ENTREGAS_INDEX);
		objModelAndView.addObject("rolNuevaEntrega", sessionService.hasRol("ENTREGAS_CREATE"));
		
		return objModelAndView;
	}
	
	@RequestMapping(value = "/table", method = RequestMethod.POST)	
	public @ResponseBody String table(	@RequestParam("offset") int offset,
										@RequestParam("limit") int limit,
										@RequestParam("_csrf") String _csrf,
										@RequestParam(value="search", required=false, defaultValue="") String search,
										@RequestParam(value="txtBootstrapTableDesde", required=false) String txtBootstrapTableDesde,
										@RequestParam(value="txtBootstrapTableHasta", required=false) String txtBootstrapTableHasta) {
		
		DataTable<EntregaEntity> dtEntregas = entregaService.dataTable(search, offset, limit, txtBootstrapTableDesde, txtBootstrapTableHasta);
		
		JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
		jsonReturn.add("total", dtEntregas.getTotal());
		JsonArrayBuilder jsonRows = Json.createArrayBuilder();
		
		dtEntregas.getRows().forEach((itemEntrega)-> {

			jsonRows.add(Json.createObjectBuilder()
				.add("idEntrega", itemEntrega.getIdEntrega())
				.add("folio", itemEntrega.getFolio())
				.add("cliente", itemEntrega.getCliente().getCliente())
				.add("contacto", itemEntrega.getClienteContacto().getContacto())
				
				.add("cotizacion", (itemEntrega.getCotizacion() != null)?itemEntrega.getCotizacion().getFolio():"")
				.add("ordenCompra", itemEntrega.getOrdenCompra())
				
				.add("nombreEntrega", itemEntrega.getUsuarioEntrega().getNombreCompleto())
				.add("nombreRecibe", itemEntrega.getPersonaRecibe())
				
				.add("creacionFecha", itemEntrega.getCreacionFechaNatural())
				.add("horaEntrega", itemEntrega.getHoraEntrada())

				.add("eliminado", itemEntrega.isEliminado())
			);
		});

		jsonReturn.add("rows", jsonRows);

		return jsonReturn.build().toString();
	}	
	
	@GetMapping({"/create", "/create/"})
	public ModelAndView create() {		
		
		List<EmpresaEntity> lstEmpresas = empresaService.listEmpresas();
		List<UsuarioEntity> lstUsuarios = usuarioService.listUsuarios();
		List<ClienteEntity> lstClientes = clienteService.listClientesActivos();
		List<SucursalEntity> lstSucursales = sucursalService.listSucursales();
		List<ClienteGiroEntity> lstClientesGiros = clienteGiroService.listClientesGiros();
		List<CotizacionEntity> lstCotizaciones = cotizacionService.listCotizacionesActivas();		
		
		ModelAndView objModelAndView = modelAndViewComponent.createModelAndViewControlPanel(Templates.CONTROL_PANEL_ENTREGAS_CREATE);
		objModelAndView.addObject("lstUsuarios", lstUsuarios);
		objModelAndView.addObject("lstEmpresas", lstEmpresas);
		objModelAndView.addObject("lstClientes", lstClientes);
		objModelAndView.addObject("lstClientesGiros", lstClientesGiros);
		objModelAndView.addObject("lstSucursales", lstSucursales);
		objModelAndView.addObject("lstCotizaciones", lstCotizaciones);		
		
		return objModelAndView;
	}
	
	@RequestMapping(value = "", method = RequestMethod.POST)
	public RedirectView store(@RequestParam(value="cmbCliente") Integer cmbCliente,
								@RequestParam(value="cmbClienteContacto") Integer cmbClienteContacto,
								@RequestParam(value="cmbEmpresa") Integer cmbEmpresa,
								@RequestParam(value="cmbCotizacion", required=false, defaultValue="0") Integer cmbCotizacion,
								@RequestParam(value="txtOrdenCompra", required=false, defaultValue="") String txtOrdenCompra,
								@RequestParam(value="cmbUsuarioEntrega") int cmbUsuarioEntrega,
								@RequestParam(value="chkPrestamo", required=false, defaultValue="false") String chkPrestamo,
								@RequestParam(value="txtEntregaFecha", required=false, defaultValue="") String txtEntregaFecha,
								@RequestParam(value="txtObservaciones", required=false, defaultValue="") String txtObservaciones,
								@RequestParam(value="txtCantidad[]") String[] txtCantidad,
								@RequestParam(value="txtNumeroParte[]") String[] txtNumeroParte,
								@RequestParam(value="txtDescripcion[]") String[] txtDescripcion,
								@RequestParam(value="txtNumeroSerie[]") String[] txtNumeroSerie,
								
								RedirectAttributes objRedirectAttributes) {
		
		RedirectView objRedirectView = null;
		EntregaEntity objEntrega = new EntregaEntity();
		
		try {
			
			objEntrega.setEmpresa(empresaService.findByIdEmpresa(cmbEmpresa));
			objEntrega.setCliente(clienteService.findByIdCliente(cmbCliente));
			objEntrega.setClienteContacto(clienteContactoService.findByIdClienteContacto(cmbClienteContacto));
			objEntrega.setOrdenCompra(txtOrdenCompra);
			objEntrega.setUsuarioEntrega(usuarioService.findByIdUsuario(cmbUsuarioEntrega));
			objEntrega.setObservaciones(txtObservaciones);
			
			if(chkPrestamo.equals("true")) {
				objEntrega.setPrestamo(true);
				
				if(!txtEntregaFecha.equals("")) {
					objEntrega.setRegresoFecha(LocalDate.parse(txtEntregaFecha, GeneralConfiguration.getInstance().getDateFormatterNatural()));
				}				
			}
			
			if(!cmbCotizacion.equals(0) ) {
				objEntrega.setCotizacion(cotizacionService.findByIdCotizacion(cmbCotizacion));
			}
			
			entregaService.create(objEntrega, txtCantidad, txtNumeroParte, txtDescripcion, txtNumeroSerie);
			objRedirectView = new RedirectView("/WebSar/controlPanel/entregas");
			modelAndViewComponent.addResult(objRedirectAttributes, EnumMessage.ENTREGAS_CREATE_001);
			
		} catch(ApplicationException exception) {
			objRedirectView = new RedirectView("/WebSar/controlPanel/entregas/create");
			modelAndViewComponent.addResult(objRedirectAttributes, exception);
		}
		
		
		return objRedirectView;
	}
	
	@GetMapping({"{paramIdEntrega}/edit", "{paramIdEntrega}/edit/"})
	public ModelAndView edit(@PathVariable(value="paramIdEntrega") Integer paramIdEntrega) {		
		
		EntregaEntity objEntrega = entregaService.findByIdEntrega(paramIdEntrega);
		List<EntregaProductoEntity> lstProductos = entregaProductoService.listEntregaProductos(paramIdEntrega);
		List<EmpresaEntity> lstEmpresas = empresaService.listEmpresas();
		List<UsuarioEntity> lstUsuarios = usuarioService.listUsuarios();
		List<ClienteEntity> lstClientes = clienteService.listClientesActivos();
		List<ClienteContactoEntity> lstContactos = clienteContactoService.listClienteContactosActivos(objEntrega.getCliente().getIdCliente());
		List<SucursalEntity> lstSucursales = sucursalService.listSucursales();
		List<ClienteGiroEntity> lstClientesGiros = clienteGiroService.listClientesGiros();
		List<CotizacionEntity> lstCotizaciones = cotizacionService.listCotizacionesActivas();	
		
		ModelAndView objModelAndView = modelAndViewComponent.createModelAndViewControlPanel(Templates.CONTROL_PANEL_ENTREGAS_EDIT);
		objModelAndView.addObject("objEntrega", objEntrega);
		objModelAndView.addObject("lstProductos", lstProductos);
		objModelAndView.addObject("lstEmpresas", lstEmpresas);
		objModelAndView.addObject("lstUsuarios", lstUsuarios);
		objModelAndView.addObject("lstClientes", lstClientes);
		objModelAndView.addObject("lstContactos", lstContactos);
		objModelAndView.addObject("lstSucursales", lstSucursales);
		objModelAndView.addObject("lstClientesGiros", lstClientesGiros);
		objModelAndView.addObject("lstCotizaciones", lstCotizaciones);
		
		return objModelAndView;
	}
	
	@RequestMapping(value = {"{paramIdEntrega}/edit", "{paramIdEntrega}/edit/"}, method = RequestMethod.PUT)
	public RedirectView store( @PathVariable(value="paramIdEntrega") Integer paramIdEntrega,
								@RequestParam(value="cmbCliente") Integer cmbCliente,
								@RequestParam(value="cmbClienteContacto") Integer cmbClienteContacto,
								@RequestParam(value="cmbEmpresa") Integer cmbEmpresa,
								@RequestParam(value="cmbCotizacion", required=false, defaultValue="0") Integer cmbCotizacion,
								@RequestParam(value="txtOrdenCompra", required=false, defaultValue="") String txtOrdenCompra,
								@RequestParam(value="cmbUsuarioEntrega") int cmbUsuarioEntrega,
								@RequestParam(value="chkPrestamo", required=false, defaultValue="false") String chkPrestamo,
								@RequestParam(value="txtEntregaFecha", required=false, defaultValue="") String txtEntregaFecha,
								@RequestParam(value="txtObservaciones", required=false, defaultValue="") String txtObservaciones,
								@RequestParam(value="txtCantidad[]") String[] txtCantidad,
								@RequestParam(value="txtNumeroParte[]") String[] txtNumeroParte,
								@RequestParam(value="txtDescripcion[]") String[] txtDescripcion,
								@RequestParam(value="txtNumeroSerie[]") String[] txtNumeroSerie,
								RedirectAttributes objRedirectAttributes) {
		
		RedirectView objRedirectView = null;
		EntregaEntity objEntrega = entregaService.findByIdEntrega(paramIdEntrega);
		
		try {
			objEntrega.setEmpresa(empresaService.findByIdEmpresa(cmbEmpresa));
			objEntrega.setCliente(clienteService.findByIdCliente(cmbCliente));
			objEntrega.setClienteContacto(clienteContactoService.findByIdClienteContacto(cmbClienteContacto));
			objEntrega.setOrdenCompra(txtOrdenCompra);
			objEntrega.setUsuarioEntrega(usuarioService.findByIdUsuario(cmbUsuarioEntrega));
			objEntrega.setObservaciones(txtObservaciones);
			
			if(chkPrestamo.equals("true")) {
				objEntrega.setPrestamo(true);
				
				if(!txtEntregaFecha.equals("")) {
					objEntrega.setRegresoFecha(LocalDate.parse(txtEntregaFecha, GeneralConfiguration.getInstance().getDateFormatterNatural()));
				}				
			}
			
			if(!cmbCotizacion.equals(0) ) {
				objEntrega.setCotizacion(cotizacionService.findByIdCotizacion(cmbCotizacion));
			}
			
			entregaService.update(objEntrega, txtCantidad, txtNumeroParte, txtDescripcion, txtNumeroSerie);
			objRedirectView = new RedirectView("/WebSar/controlPanel/entregas");
			modelAndViewComponent.addResult(objRedirectAttributes, EnumMessage.ENTREGAS_EDIT_001);
			
		} catch(ApplicationException exception) {
			objRedirectView = new RedirectView("/WebSar/controlPanel/entregas/" + paramIdEntrega + "/edit");
			modelAndViewComponent.addResult(objRedirectAttributes, exception);
		}
		
		
		return objRedirectView;
	}
	
	@GetMapping({"{paramIdEntrega}/firma", "{paramIdEntrega}/firma/"})
	public ModelAndView firma(@PathVariable(value="paramIdEntrega") Integer paramIdEntrega) {		
		
		EntregaEntity objEntrega = entregaService.findByIdEntrega(paramIdEntrega);
		List<EntregaProductoEntity> lstProductos = entregaProductoService.listEntregaProductos(paramIdEntrega);
		
		ModelAndView objModelAndView = modelAndViewComponent.createModelAndViewControlPanel(Templates.CONTROL_PANEL_ENTREGAS_FIRMA);
		objModelAndView.addObject("objEntrega", objEntrega);
		objModelAndView.addObject("lstProductos", lstProductos);
		
		return objModelAndView;
	}
	
	@RequestMapping(value = "/firma", method = RequestMethod.POST)
	public RedirectView store(@RequestParam(value="hddIdEntrega") Integer hddIdEntrega,
								@RequestParam(value="imgFirmaEntrega") String imgFirmaEntrega,
								@RequestParam(value="txtNombreRecepcion") String txtNombreRecepcion,
								@RequestParam(value="imgFirmaRecepcion") String imgFirmaRecepcion,
								RedirectAttributes objRedirectAttributes) {
		
		RedirectView objRedirectView = null;
		EntregaEntity objEntrega = entregaService.findByIdEntrega(hddIdEntrega);
		
		try {
			
			if(objEntrega != null) {
				
				objEntrega.setPersonaRecibe(txtNombreRecepcion);
				
				LocalDateTime ldtNow = LocalDateTime.now();
				objEntrega.setHoraEntrada(ldtNow.getHour() + ":" + ldtNow.getMinute());
				
				entregaService.update(objEntrega, imgFirmaEntrega, imgFirmaRecepcion);
				objRedirectView = new RedirectView("/WebSar/controlPanel/entregas");
				modelAndViewComponent.addResult(objRedirectAttributes, EnumMessage.ENTREGAS_FIRMA_001);
				
			} else {
				throw new ApplicationException(EnumException.ENTREGAS_UPDATE_001);
			}									
			
		} catch(ApplicationException exception) {
			objRedirectView = new RedirectView("/WebSar/controlPanel/entregas/" + hddIdEntrega + "/firma");
			modelAndViewComponent.addResult(objRedirectAttributes, exception);
		}
		
		
		return objRedirectView;
	}
	
	@RequestMapping(value = {"{paramIdEntrega}/pdf", "{paramIdEntrega}/pdf/"}, method = RequestMethod.GET, produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<InputStreamResource> previewPdf(@PathVariable("paramIdEntrega") int paramIdEntrega) throws IOException, DocumentException {
		
		EntregaEntity objEntrega = entregaService.findByIdEntrega(paramIdEntrega);
		List<EntregaProductoEntity> lstProductos = entregaProductoService.listEntregaProductos(paramIdEntrega);
		
		LocalDateTime ldtNow = LocalDateTime.now();
		Templates objTemplates = new Templates();
		
		String path_file = ldtNow.getYear() + "_" + ldtNow.getMonthValue() + "_" + ldtNow.getDayOfMonth() + "_" + objEntrega.getFolio() + ".pdf";
				
		Context objContext = new Context();
		objContext.setVariable("_TEMPLATE_", Templates.PDF_ENTREGA_PRODUCTO);
		objContext.setVariable("title", "EntregaProducto: " + objEntrega.getFolio());
		objContext.setVariable("objEntrega", objEntrega);
		objContext.setVariable("lstProductos", lstProductos);
		
		return pdfComponent.generate(path_file, objTemplates.FOUNDATION_PDF, objContext);		
	}
}
