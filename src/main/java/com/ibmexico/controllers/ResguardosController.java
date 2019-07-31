package com.ibmexico.controllers;

import java.io.IOException;
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
import com.ibmexico.entities.EmpresaEntity;
import com.ibmexico.entities.ResguardoEntity;
import com.ibmexico.entities.ResguardoPartidaEntity;
import com.ibmexico.entities.ResguardoTipoEntity;
import com.ibmexico.entities.UsuarioEntity;
import com.ibmexico.libraries.DataTable;
import com.ibmexico.libraries.Templates;
import com.ibmexico.services.EmpresaService;
import com.ibmexico.services.ResguardoPartidaService;
import com.ibmexico.services.ResguardoService;
import com.ibmexico.services.ResguardoTipoService;
import com.ibmexico.services.SessionService;
import com.ibmexico.services.UsuarioService;
import com.lowagie.text.DocumentException;

@Controller
@RequestMapping("controlPanel/resguardos")
public class ResguardosController {

	@Autowired
	@Qualifier("modelAndViewComponent")
	private ModelAndViewComponent modelAndViewComponent;
	
	@Autowired
	@Qualifier("pdfComponent")
	private PdfComponent pdfComponent;
	
	@Autowired
	@Qualifier("resguardoService")
	private ResguardoService resguardoService;
	
	@Autowired
	@Qualifier("resguardoPartidaService")
	private ResguardoPartidaService resguardoPartidaService;
	
	@Autowired
	@Qualifier("resguardoTipoService")
	private ResguardoTipoService resguardoTipoService;
	
	@Autowired
	@Qualifier("empresaService")
	private EmpresaService empresaService;
	
	@Autowired
	@Qualifier("usuarioService")
	private UsuarioService usuarioService;
	
	@Autowired
	@Qualifier("sessionService")
	private SessionService sessionService;
	
	@GetMapping({"", "/"})
	public ModelAndView index() {		
		ModelAndView objModelAndView = modelAndViewComponent.createModelAndViewControlPanel(Templates.CONTROL_PANEL_RESGUARDOS_INDEX);
		objModelAndView.addObject("rolNuevoResguardo", sessionService.hasRol("RESGUARDOS_CREATE"));
		
		return objModelAndView;
	}
	
	@RequestMapping(value = "/table", method = RequestMethod.POST)	
	public @ResponseBody String table(	@RequestParam("offset") int offset,
										@RequestParam("limit") int limit,
										@RequestParam("_csrf") String _csrf,
										@RequestParam(value="search", required=false, defaultValue="") String search,
										@RequestParam(value="txtBootstrapTableDesde", required=false) String txtBootstrapTableDesde,
										@RequestParam(value="txtBootstrapTableHasta", required=false) String txtBootstrapTableHasta) {
		
		DataTable<ResguardoEntity> dtResguardos = resguardoService.dataTable(search, offset, limit, txtBootstrapTableDesde, txtBootstrapTableHasta);
		
		JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
		jsonReturn.add("total", dtResguardos.getTotal());
		JsonArrayBuilder jsonRows = Json.createArrayBuilder();
		
		dtResguardos.getRows().forEach((itemResguardo)-> {

			jsonRows.add(Json.createObjectBuilder()
				.add("idResguardo", itemResguardo.getIdResguardo())
				.add("folio", itemResguardo.getFolio())
				.add("resguardoTipo", itemResguardo.getResguardoTipo().getResguardoTipo())
				
				.add("nombreEntrega", itemResguardo.getUsuarioEntrega().getNombreCompleto())
				.add("nombreRecibe", itemResguardo.getUsuarioRecibe().getNombreCompleto())
				
				.add("creacionFecha", itemResguardo.getCreacionFechaNatural())

				.add("eliminado", itemResguardo.isEliminado())
				.add("url_firma_recibe", itemResguardo.getUrlFirmaRecibe())
			);
		});

		jsonReturn.add("rows", jsonRows);

		return jsonReturn.build().toString();
	}	
	
	@GetMapping({"/create", "/create/"})
	public ModelAndView create() {		
		
		List<EmpresaEntity> lstEmpresas = empresaService.listEmpresas();
		List<UsuarioEntity> lstUsuarios = usuarioService.listUsuariosActivos();
		List<ResguardoTipoEntity> lstResguardosTipos = resguardoTipoService.listAll();
		
		ModelAndView objModelAndView = modelAndViewComponent.createModelAndViewControlPanel(Templates.CONTROL_PANEL_RESGUARDOS_CREATE);
		objModelAndView.addObject("lstUsuarios", lstUsuarios);
		objModelAndView.addObject("lstEmpresas", lstEmpresas);
		objModelAndView.addObject("lstResguardosTipos", lstResguardosTipos);
		
		return objModelAndView;
	}
	
	@RequestMapping(value = "", method = RequestMethod.POST)
	public RedirectView store(@RequestParam(value="cmbEmpresa") Integer cmbEmpresa,
								@RequestParam(value="cmbResguardoTipo") Integer cmbResguardoTipo,
								@RequestParam(value="cmbUsuarioEntrega") int cmbUsuarioEntrega,
								@RequestParam(value="cmbUsuarioRecibe") int cmbUsuarioRecibe,
								@RequestParam(value="txtObservaciones", required=false, defaultValue="") String txtObservaciones,
								@RequestParam(value="txtCantidad[]") String[] txtCantidad,
								@RequestParam(value="txtMarca[]") String[] txtMarca,
								@RequestParam(value="txtDescripcion[]") String[] txtDescripcion,
								@RequestParam(value="txtNumeroSerie[]") String[] txtNumeroSerie,
								@RequestParam(value="txtModelo[]") String[] txtModelo,
								
								RedirectAttributes objRedirectAttributes) {
		
		RedirectView objRedirectView = null;
		ResguardoEntity objResguardo = new ResguardoEntity();
		
		try {
			
			objResguardo.setEmpresa(empresaService.findByIdEmpresa(cmbEmpresa));
			objResguardo.setResguardoTipo(resguardoTipoService.findByIdResguardoTipo(cmbResguardoTipo));
			objResguardo.setUsuarioEntrega(usuarioService.findByIdUsuario(cmbUsuarioEntrega));
			objResguardo.setUsuarioRecibe(usuarioService.findByIdUsuario(cmbUsuarioRecibe));
			objResguardo.setObservaciones(txtObservaciones);
			
			
			resguardoService.create(objResguardo, txtCantidad, txtMarca, txtDescripcion, txtNumeroSerie, txtModelo);
			objRedirectView = new RedirectView("/WebSar/controlPanel/resguardos");
			modelAndViewComponent.addResult(objRedirectAttributes, EnumMessage.RESGUARDOS_CREATE_001);
			
		} catch(ApplicationException exception) {
			objRedirectView = new RedirectView("/WebSar/controlPanel/resguardos/create");
			modelAndViewComponent.addResult(objRedirectAttributes, exception);
		}
		
		
		return objRedirectView;
	}
	
	@GetMapping({"{paramIdResguardo}/edit", "{paramIdResguardo}/edit/"})
	public ModelAndView edit(@PathVariable(value="paramIdResguardo") Integer paramIdResguardo) {		
		
		ResguardoEntity objResguardo = resguardoService.findByIdResguardo(paramIdResguardo);
		List<ResguardoPartidaEntity> lstPartidas = resguardoPartidaService.listEntregaProductos(paramIdResguardo);
		
		List<EmpresaEntity> lstEmpresas = empresaService.listEmpresas();
		List<UsuarioEntity> lstUsuarios = usuarioService.listUsuariosActivos();
		List<ResguardoTipoEntity> lstResguardosTipos = resguardoTipoService.listAll();
		
		ModelAndView objModelAndView = modelAndViewComponent.createModelAndViewControlPanel(Templates.CONTROL_PANEL_RESGUARDOS_EDIT);
		objModelAndView.addObject("objResguardo", objResguardo);
		objModelAndView.addObject("lstPartidas", lstPartidas);
		objModelAndView.addObject("lstEmpresas", lstEmpresas);
		objModelAndView.addObject("lstUsuarios", lstUsuarios);
		objModelAndView.addObject("lstResguardosTipos", lstResguardosTipos);
		
		return objModelAndView;
	}
	
	@RequestMapping(value = {"{paramIdResguardo}/edit", "{paramIdResguardo}/edit/"}, method = RequestMethod.PUT)
	public RedirectView store( @PathVariable(value="paramIdResguardo") Integer paramIdResguardo,
								@RequestParam(value="cmbEmpresa") Integer cmbEmpresa,
								@RequestParam(value="cmbResguardoTipo") Integer cmbResguardoTipo,
								@RequestParam(value="cmbUsuarioEntrega") int cmbUsuarioEntrega,
								@RequestParam(value="cmbUsuarioRecibe") int cmbUsuarioRecibe,
								@RequestParam(value="txtObservaciones", required=false, defaultValue="") String txtObservaciones,
								@RequestParam(value="txtCantidad[]") String[] txtCantidad,
								@RequestParam(value="txtMarca[]") String[] txtMarca,
								@RequestParam(value="txtDescripcion[]") String[] txtDescripcion,
								@RequestParam(value="txtNumeroSerie[]") String[] txtNumeroSerie,
								@RequestParam(value="txtModelo[]") String[] txtModelo,
								RedirectAttributes objRedirectAttributes) {
		
		RedirectView objRedirectView = null;
		ResguardoEntity objResguardo = resguardoService.findByIdResguardo(paramIdResguardo);
		
		try {
			if(objResguardo != null) {
				objResguardo.setEmpresa(empresaService.findByIdEmpresa(cmbEmpresa));
				objResguardo.setResguardoTipo(resguardoTipoService.findByIdResguardoTipo(cmbResguardoTipo));
				objResguardo.setUsuarioEntrega(usuarioService.findByIdUsuario(cmbUsuarioEntrega));
				objResguardo.setUsuarioRecibe(usuarioService.findByIdUsuario(cmbUsuarioRecibe));
				objResguardo.setObservaciones(txtObservaciones);
				
				resguardoService.update(objResguardo, txtCantidad, txtMarca, txtDescripcion, txtNumeroSerie, txtModelo);
				objRedirectView = new RedirectView("/WebSar/controlPanel/resguardos");
				modelAndViewComponent.addResult(objRedirectAttributes, EnumMessage.RESGUARDOS_EDIT_001);
			}
			else {
				throw new ApplicationException(EnumException.RESGUARDOS_UPDATE_001);
			}
			
			
			
		} catch(ApplicationException exception) {
			objRedirectView = new RedirectView("/WebSar/controlPanel/resguardos/" + paramIdResguardo + "/edit");
			modelAndViewComponent.addResult(objRedirectAttributes, exception);
		}
		
		
		return objRedirectView;
	}
	
	@GetMapping({"{paramIdResguardo}/firma", "{paramIdResguardo}/firma/"})
	public ModelAndView firma(@PathVariable(value="paramIdResguardo") Integer paramIdResguardo) {		
		
		ResguardoEntity objResguardo = resguardoService.findByIdResguardo(paramIdResguardo);
		List<ResguardoPartidaEntity> lstPartidas = resguardoPartidaService.listEntregaProductos(paramIdResguardo);
		
		ModelAndView objModelAndView = modelAndViewComponent.createModelAndViewControlPanel(Templates.CONTROL_PANEL_RESGUARDOS_FIRMA);
		objModelAndView.addObject("objResguardo", objResguardo);
		objModelAndView.addObject("lstPartidas", lstPartidas);
		
		return objModelAndView;
	}
	
	@RequestMapping(value = "/firma", method = RequestMethod.POST)
	public RedirectView store(@RequestParam(value="hddIdResguardo") Integer hddIdResguardo,
								@RequestParam(value="imgFirmaEntrega") String imgFirmaEntrega,
								@RequestParam(value="imgFirmaRecepcion") String imgFirmaRecepcion,
								RedirectAttributes objRedirectAttributes) {
		
		RedirectView objRedirectView = null;
		ResguardoEntity objResguardo = resguardoService.findByIdResguardo(hddIdResguardo);
		
		try {			
			if(objResguardo != null) {				
				resguardoService.update(objResguardo, imgFirmaEntrega, imgFirmaRecepcion);
				objRedirectView = new RedirectView("/WebSar/controlPanel/resguardos");
				modelAndViewComponent.addResult(objRedirectAttributes, EnumMessage.RESGUARDOS_FIRMA_001);
				
			} else {
				throw new ApplicationException(EnumException.RESGUARDOS_UPDATE_001);
			}									
			
		} catch(ApplicationException exception) {
			objRedirectView = new RedirectView("/WebSar/controlPanel/resguardos/" + hddIdResguardo + "/firma");
			modelAndViewComponent.addResult(objRedirectAttributes, exception);
		}
		
		
		return objRedirectView;
	}
	
	@RequestMapping(value = {"{paramIdResguardo}/pdf", "{paramIdResguardo}/pdf/"}, method = RequestMethod.GET, produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<InputStreamResource> previewPdf(@PathVariable("paramIdResguardo") int paramIdResguardo) throws IOException, DocumentException {
		
		ResguardoEntity objResguardo = resguardoService.findByIdResguardo(paramIdResguardo);
		List<ResguardoPartidaEntity> lstPartidas = resguardoPartidaService.listEntregaProductos(paramIdResguardo);
		
		LocalDateTime ldtNow = LocalDateTime.now();
		Templates objTemplates = new Templates();
		
		String path_file = ldtNow.getYear() + "_" + ldtNow.getMonthValue() + "_" + ldtNow.getDayOfMonth() + "_" + objResguardo.getFolio() + ".pdf";
				
		Context objContext = new Context();
		
		if(objResguardo.getResguardoTipo().getIdResguardoTipo() == 1) {
			objContext.setVariable("_TEMPLATE_", Templates.PDF_RESGUARDO_EQUIPO);
		} else {			
			objContext.setVariable("_TEMPLATE_", Templates.PDF_RESGUARDO_UNIFORME_CREDENCIAL);
		}
		
		objContext.setVariable("title", "Resguardo: " + objResguardo.getFolio());
		objContext.setVariable("objResguardo", objResguardo);
		objContext.setVariable("lstPartidas", lstPartidas);
		
		return pdfComponent.generate(path_file, objTemplates.FOUNDATION_PDF, objContext);		
	}
}
