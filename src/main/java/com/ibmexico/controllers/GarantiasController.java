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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import org.thymeleaf.context.Context;

import com.ibmexico.libraries.notifications.ApplicationException;
import com.ibmexico.libraries.notifications.EnumMessage;
import com.ibmexico.components.ModelAndViewComponent;
import com.ibmexico.components.PdfComponent;
import com.ibmexico.configurations.GeneralConfiguration;
import com.ibmexico.entities.ClienteEntity;
import com.ibmexico.entities.ClienteGiroEntity;
import com.ibmexico.entities.GarantiaEntity;
import com.ibmexico.entities.GarantiaEstatusEntity;
import com.ibmexico.entities.GarantiaFicheroEntity;
import com.ibmexico.entities.ProveedorEntity;
import com.ibmexico.entities.SucursalEntity;
import com.ibmexico.entities.UsuarioEntity;
import com.ibmexico.libraries.DataTable;
import com.ibmexico.libraries.Templates;
import com.ibmexico.services.ClienteContactoService;
import com.ibmexico.services.ClienteGiroService;
import com.ibmexico.services.ClienteService;
import com.ibmexico.services.GarantiaEstatusService;
import com.ibmexico.services.GarantiaFicheroService;
import com.ibmexico.services.GarantiaService;
import com.ibmexico.services.ProveedorService;
import com.ibmexico.services.SessionService;
import com.ibmexico.services.SucursalService;
import com.ibmexico.services.UsuarioService;
import com.lowagie.text.DocumentException;

@Controller
@RequestMapping("controlPanel/garantias")
public class GarantiasController {

	@Autowired
	@Qualifier("modelAndViewComponent")
	private ModelAndViewComponent modelAndViewComponent;
	
	@Autowired
	@Qualifier("pdfComponent")
	private PdfComponent pdfComponent;
	
	@Autowired
	@Qualifier("garantiaService")
	private GarantiaService garantiaService;
	
	@Autowired
	@Qualifier("garantiaFicheroService")
	private GarantiaFicheroService garantiaFicheroService;
	
	@Autowired
	@Qualifier("garantiaEstatusService")
	private GarantiaEstatusService garantiaEstatusService;
	
	@Autowired
	@Qualifier("usuarioService")
	private UsuarioService usuarioService;
	
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
	@Qualifier("proveedorService")
	private ProveedorService proveedorService;
	
	@Autowired
	@Qualifier("sucursalService")
	private SucursalService sucursalService;
	
	@Autowired
	@Qualifier("sessionService")
	private SessionService sessionService;
	
	@GetMapping({"", "/"})
	public ModelAndView index() {		
		
		ModelAndView objModelAndView = modelAndViewComponent.createModelAndViewControlPanel(Templates.CONTROL_PANEL_GARANTIAS_INDEX);
		
		return objModelAndView;
	}
	
	@RequestMapping(value = "/table", method = RequestMethod.POST)	
	public @ResponseBody String table(	@RequestParam("offset") int offset,
										@RequestParam("limit") int limit,
										@RequestParam("_csrf") String _csrf,
										@RequestParam(value="search", required=false, defaultValue="") String search,
										@RequestParam(value="txtBootstrapTableDesde", required=false) String txtBootstrapTableDesde,
										@RequestParam(value="txtBootstrapTableHasta", required=false) String txtBootstrapTableHasta) {
		
		DataTable<GarantiaEntity> dtGarantias = garantiaService.dataTable(search, offset, limit, txtBootstrapTableDesde, txtBootstrapTableHasta);
		
		JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
		jsonReturn.add("total", dtGarantias.getTotal());
		JsonArrayBuilder jsonRows = Json.createArrayBuilder();
		
		dtGarantias.getRows().forEach((itemGarantia)-> {

			jsonRows.add(Json.createObjectBuilder()
				.add("idGarantia", itemGarantia.getIdGarantia())
				.add("folio", itemGarantia.getFolio())
				.add("cliente", itemGarantia.getCliente().getCliente())
				.add("contacto", itemGarantia.getClienteContacto().getContacto())
				.add("idEstatus", itemGarantia.getGarantiaEstatus().getIdGarantiaEstatus())
				.add("estatus", itemGarantia.getGarantiaEstatus().getGarantiaEstatus())
				
				.add("producto", itemGarantia.getProducto())
				.add("creacionFecha", itemGarantia.getCreacionFechaNatural())
				.add("creacionUsuario", itemGarantia.getCreacionUsuario().getAlias())
			);
		});

		jsonReturn.add("rows", jsonRows);

		return jsonReturn.build().toString();
	}	
	
	@GetMapping({"/create", "/create/"})
	public ModelAndView create() {		
		
		List<UsuarioEntity> lstUsuarios = usuarioService.listUsuarios();
		List<ClienteEntity> lstClientes = clienteService.listClientesActivos();
		List<SucursalEntity> lstSucursales = sucursalService.listSucursales();
		List<ClienteGiroEntity> lstClientesGiros = clienteGiroService.listClientesGiros();	
		List<ProveedorEntity> lstProveedores = proveedorService.listProveedoreesActivos();
		
		ModelAndView objModelAndView = modelAndViewComponent.createModelAndViewControlPanel(Templates.CONTROL_PANEL_GARANTIAS_CREATE);
		objModelAndView.addObject("lstUsuarios", lstUsuarios);
		objModelAndView.addObject("lstClientes", lstClientes);
		objModelAndView.addObject("lstClientesGiros", lstClientesGiros);
		objModelAndView.addObject("lstSucursales", lstSucursales);
		objModelAndView.addObject("lstProveedores", lstProveedores);		
		
		return objModelAndView;
	}
	
	@RequestMapping(value = "", method = RequestMethod.POST)
	public RedirectView store(@RequestParam(value="cmbCliente") Integer cmbCliente,
								@RequestParam(value="cmbClienteContacto") Integer cmbClienteContacto,
								@RequestParam(value="txtFechaEntrega", required=false, defaultValue="") String txtFechaEntrega,
								@RequestParam(value="cmbProveedor") Integer cmbProveedor,
								@RequestParam(value="txtFactura", required=false, defaultValue="") String txtFactura,
								@RequestParam(value="txtFalla", required=false, defaultValue="") String txtFalla,
								@RequestParam(value="txtCantidad") Integer txtCantidad,
								@RequestParam(value="txtProducto") String txtProducto,							
								@RequestParam(value="txtNumeroSerie") String txtNumeroSerie,
								@RequestParam(value="txtNombreRecepcion") String txtNombreRecepcion,
								@RequestParam(value="imgFirmaEntrega") String imgFirmaEntrega,
								@RequestParam("fichero") MultipartFile[] fichero,
								RedirectAttributes objRedirectAttributes) {
		
		RedirectView objRedirectView = null;
		GarantiaEntity objGarantia = new GarantiaEntity();
		
		try {
			
			objGarantia.setCliente(clienteService.findByIdCliente(cmbCliente));
			objGarantia.setClienteContacto(clienteContactoService.findByIdClienteContacto(cmbClienteContacto));
			
			if(!txtFechaEntrega.equals("")) {
				objGarantia.setEntregaFecha(LocalDate.parse(txtFechaEntrega, GeneralConfiguration.getInstance().getDateFormatterNatural()));
			}
			
			objGarantia.setProveedor(proveedorService.findByIdProveedor(cmbProveedor));
			objGarantia.setFalla(txtFalla.trim());
			objGarantia.setCantidad(txtCantidad);
			objGarantia.setProducto(txtProducto);
			objGarantia.setNumeroSerie(txtNumeroSerie);
			objGarantia.setGarantiaEstatus(garantiaEstatusService.findByIdGarantiaEstatus(1));
			objGarantia.setNombreRecibe(txtNombreRecepcion);
			
			garantiaService.create(objGarantia, imgFirmaEntrega, fichero);
			objRedirectView = new RedirectView("/WebSar/controlPanel/garantias");
			modelAndViewComponent.addResult(objRedirectAttributes, EnumMessage.GARANTIAS_CREATE_001);
			
		} catch(ApplicationException exception) {
			objRedirectView = new RedirectView("/WebSar/controlPanel/garantias/create");
			modelAndViewComponent.addResult(objRedirectAttributes, exception);
		}
		
		
		return objRedirectView;
	}
	
	@GetMapping({"{paramIdGarantia}/edit", "{paramIdGarantia}/edit/"})
	public ModelAndView edit(@PathVariable(value="paramIdGarantia") Integer paramIdGarantia) {		
		
		GarantiaEntity objGarantia = garantiaService.findByIdGarantia(paramIdGarantia);
		
		List<GarantiaEstatusEntity> lstGarantiaEstatus = garantiaEstatusService.listAll();	
		
		ModelAndView objModelAndView = modelAndViewComponent.createModelAndViewControlPanel(Templates.CONTROL_PANEL_GARANTIAS_EDIT);
		objModelAndView.addObject("objGarantia", objGarantia);
		objModelAndView.addObject("lstGarantiaEstatus", lstGarantiaEstatus);
		
		return objModelAndView;
	}
	
	@RequestMapping(value = {"{paramIdGarantia}", "{paramIdGarantia}/"}, method = RequestMethod.PUT)
	public RedirectView store( @PathVariable(value="paramIdGarantia") Integer paramIdGarantia,
								@RequestParam(value="txtFalla") String txtFalla,
								@RequestParam(value="txtDiagnostico", required=false, defaultValue="") String txtDiagnostico,
								@RequestParam(value="txtFechaEntrega", required=false, defaultValue="") String txtFechaEntrega,
								@RequestParam(value="cmbEstatus") Integer cmbEstatus,
								@RequestParam(value="tipoDiagnostico") String tipoDiagnostico,
								RedirectAttributes objRedirectAttributes) {
		
		RedirectView objRedirectView = null;
		GarantiaEntity objGarantia = garantiaService.findByIdGarantia(paramIdGarantia);
		
		try {
			
			objGarantia.setFalla(txtFalla.trim());
			objGarantia.setDiagnostico(txtDiagnostico.trim());
			objGarantia.setGarantiaEstatus(garantiaEstatusService.findByIdGarantiaEstatus(cmbEstatus));
			
			if(!txtFechaEntrega.equals("") ) {
				objGarantia.setEntregaFecha(LocalDate.parse(txtFechaEntrega, GeneralConfiguration.getInstance().getDateFormatterNatural()));
			}
			
			if(tipoDiagnostico.equals("doa")) {
				objGarantia.setDoa(true);
			} else {
				objGarantia.setRma(true);
			}
			
			garantiaService.update(objGarantia);
			objRedirectView = new RedirectView("/WebSar/controlPanel/garantias");
			modelAndViewComponent.addResult(objRedirectAttributes, EnumMessage.ENTREGAS_EDIT_001);
			
		} catch(ApplicationException exception) {
			objRedirectView = new RedirectView("/WebSar/controlPanel/garantias/" + paramIdGarantia + "/edit");
			modelAndViewComponent.addResult(objRedirectAttributes, exception);
		}
		
		
		return objRedirectView;
	}
	
	
	@RequestMapping(value = {"{paramIdGarantia}/pdf", "{paramIdGarantia}/pdf/"}, method = RequestMethod.GET, produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<InputStreamResource> previewPdf(@PathVariable("paramIdGarantia") int paramIdGarantia) throws IOException, DocumentException {
		
		GarantiaEntity objGarantia = garantiaService.findByIdGarantia(paramIdGarantia);
		List<GarantiaFicheroEntity> lstFicheros = garantiaFicheroService.listGarantiaFicheros(paramIdGarantia);
		
		LocalDateTime ldtNow = LocalDateTime.now();
		Templates objTemplates = new Templates();
		
		String path_file = ldtNow.getYear() + "_" + ldtNow.getMonthValue() + "_" + ldtNow.getDayOfMonth() + "_" + objGarantia.getFolio() + ".pdf";
				
		Context objContext = new Context();
		objContext.setVariable("_TEMPLATE_", Templates.PDF_GARANTIA);
		objContext.setVariable("title", "Garantia: " + objGarantia.getFolio());
		objContext.setVariable("objGarantia", objGarantia);
		objContext.setVariable("lstFicheros", lstFicheros);
		
		return pdfComponent.generate(path_file, objTemplates.FOUNDATION_PDF, objContext);		
	}
}
