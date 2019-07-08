package com.ibmexico.controllers;

import java.time.LocalDate;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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

import com.ibmexico.components.ModelAndViewComponent;
import com.ibmexico.configurations.GeneralConfiguration;
import com.ibmexico.entities.NoticiaEntity;
import com.ibmexico.libraries.DataTable;
import com.ibmexico.libraries.Templates;
import com.ibmexico.libraries.notifications.ApplicationException;
import com.ibmexico.libraries.notifications.EnumMessage;
import com.ibmexico.services.NoticiaService;
import com.ibmexico.services.SessionService;

@Controller
@RequestMapping("controlPanel/noticias")
public class NoticiasController {

	@Autowired
	@Qualifier("modelAndViewComponent")
	private ModelAndViewComponent modelAndViewComponent;
	
	@Autowired
	@Qualifier("noticiaService")
	private NoticiaService noticiaService;
	
	@Autowired
	@Qualifier("sessionService")
	private SessionService sessionService;
	
	@GetMapping({"", "/"})
	public ModelAndView index() {
		ModelAndView objModelAndView = modelAndViewComponent.createModelAndViewControlPanel(Templates.CONTROL_PANEL_NOTICIAS_INDEX);
		objModelAndView.addObject("rolNuevaNoticia", sessionService.hasRol("NOTICIAS_CREATE"));
		return objModelAndView;
	}
	
	@RequestMapping(value = "/table", method = RequestMethod.POST)	
	public @ResponseBody String table(	@RequestParam("offset") int offset,
										@RequestParam("limit") int limit,
										@RequestParam("_csrf") String _csrf,
										@RequestParam(value="search", required=false, defaultValue="") String search,
										@RequestParam(value="txtBootstrapTableDesde", required=false) String txtBootstrapTableDesde,
										@RequestParam(value="txtBootstrapTableHasta", required=false) String txtBootstrapTableHasta) {
		
		DataTable<NoticiaEntity> dtNoticias = noticiaService.dataTable(search, offset, limit, txtBootstrapTableDesde, txtBootstrapTableHasta);
		
		JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
		jsonReturn.add("total", dtNoticias.getTotal());
		JsonArrayBuilder jsonRows = Json.createArrayBuilder();
		
		dtNoticias.getRows().forEach((itemNoticia)-> {

			jsonRows.add(Json.createObjectBuilder()
				.add("idNoticia", itemNoticia.getIdNoticia())
				
				.add("titulo", itemNoticia.getTitulo())
				.add("descripcion", itemNoticia.getDescripcion())
				.add("vencimientoFecha", itemNoticia.getVencimientoFechaNatural())
				
				.add("creacionUsuario", itemNoticia.getCreacionUsuario().getAlias())			
				.add("importante", itemNoticia.isImportante())				
				.add("eliminado", itemNoticia.isEliminado())
			);
		});
		jsonReturn.add("rows", jsonRows);

		return jsonReturn.build().toString();
	}
	
	@GetMapping("/create")
	public ModelAndView create() {

		ModelAndView objModelAndView = modelAndViewComponent.createModelAndViewControlPanel(Templates.CONTROL_PANEL_NOTICIAS_CREATE);		
		return objModelAndView;
	}
	
	@RequestMapping(value = "", method = RequestMethod.POST)
	public RedirectView store( @RequestParam(value="txtTitulo") String txtTitulo,
								@RequestParam(value="txtDescripcion") String txtDescripcion,
								@RequestParam(value="txtVencimientoFecha", required=false, defaultValue="") String txtVencimientoFecha,
								@RequestParam(value="txtMensaje") String txtMensaje,
								
								RedirectAttributes objRedirectAttributes) {
					
		RedirectView objRedirectView = null;
		NoticiaEntity objNoticia = new NoticiaEntity();
		
		try {
			
			objNoticia.setTitulo(txtTitulo);
			objNoticia.setDescripcion(txtDescripcion);
			objNoticia.setMensaje(txtMensaje);
			
			if(!txtVencimientoFecha.equals("")) {
				objNoticia.setVencimientoFecha(LocalDate.parse(txtVencimientoFecha, GeneralConfiguration.getInstance().getDateFormatterNatural()));
			}
									
			noticiaService.create(objNoticia);
			objRedirectView = new RedirectView("/WebSar/controlPanel/noticias");
			modelAndViewComponent.addResult(objRedirectAttributes, EnumMessage.NOTICIAS_CREATE_001);
			
		} catch(ApplicationException exception) {
			objRedirectView = new RedirectView("/WebSar/controlPanel/noticias/create");
			modelAndViewComponent.addResult(objRedirectAttributes, exception);
		}
		
		return objRedirectView;
	}
	
	@GetMapping("/{paramIdNoticia}/show")
	public ModelAndView show(@PathVariable("paramIdNoticia") int paramIdNoticia) {
		
		NoticiaEntity objNoticia = noticiaService.findByIdNoticia(paramIdNoticia);

		ModelAndView objModelAndView = modelAndViewComponent.createModelAndViewControlPanel(Templates.CONTROL_PANEL_NOTICIAS_SHOW);
		objModelAndView.addObject("objNoticia", objNoticia);
		
		return objModelAndView;
	}
}
