package com.ibmexico.controllers;

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
import com.ibmexico.entities.CatalogoActivoEntity;
import com.ibmexico.libraries.DataTable;
import com.ibmexico.libraries.Templates;
import com.ibmexico.libraries.notifications.ApplicationException;
import com.ibmexico.libraries.notifications.EnumException;
import com.ibmexico.libraries.notifications.EnumMessage;
import com.ibmexico.services.CatalogoActivoService;

@Controller
@RequestMapping("controlPanel/catalogoActivo")
public class CatalogoActivoController {
	
	@Autowired
	@Qualifier("catalogoActivoService")
	private CatalogoActivoService catActivoService;
	
	@Autowired
	@Qualifier("modelAndViewComponent")
	private ModelAndViewComponent modelAndViewComponent;
	
	@GetMapping({"","/"})
	public ModelAndView index() {
		ModelAndView modelAndView=modelAndViewComponent.createModelAndViewControlPanel(Templates.CATALOGO_ACTIVO);
		return modelAndView;
	}
	
	@GetMapping("/create")
	public ModelAndView create() {
		ModelAndView objModelAndView = modelAndViewComponent.createModelAndViewControlPanel(Templates.CATALOGO_CREATE);
		return objModelAndView;
	}
	
	
	
	@RequestMapping(value = "", method = RequestMethod.POST)
	public RedirectView store( @RequestParam(value="txtNombre") String txtNombre,
								@RequestParam(value="txtDescripcion", required=false) String txtDescripcion,
								RedirectAttributes objRedirectAttributes) {
					
		RedirectView objRedirectView = null;
		CatalogoActivoEntity objCatActivo=new CatalogoActivoEntity();
		
		try {
			objCatActivo.setNombre(txtNombre);
			objCatActivo.setDescripcion(txtDescripcion);
					
									
			catActivoService.create(objCatActivo);
			objRedirectView = new RedirectView("/WebSar/controlPanel/catalogoActivo");
			modelAndViewComponent.addResult(objRedirectAttributes, EnumMessage.ACTIVOS_CREATE_001);
			
		} catch(ApplicationException exception) {
			objRedirectView = new RedirectView("/WebSar/controlPanel/catalogoActivo/create");
			modelAndViewComponent.addResult(objRedirectAttributes, exception);
		}
		return objRedirectView;
	}
	
	
	@RequestMapping(value="storeCatalogoAjax", method=RequestMethod.POST)
	public @ResponseBody String storeCatalogoAjax(@RequestParam(value="txtNombre", required=true) String txtNombre,
												  @RequestParam(value="txtDescripcion", required=false) String txtDescripcion) {
		Boolean respuesta = false;
		String titulo = "Oops!";
		String mensaje = "Ocurrió un error al crear los Activos en el sistema.";
		
		CatalogoActivoEntity objCatActivo=new CatalogoActivoEntity();
		try {
			objCatActivo.setNombre(txtNombre);
			objCatActivo.setDescripcion(txtDescripcion);
			catActivoService.create(objCatActivo);		
			
			respuesta = true;
			titulo = "Excelente!";
			mensaje = "Nuevo activo exitosamente creado.";
			
		} catch(ApplicationException exception) {
			throw new ApplicationException(EnumException.ACTIVO_CREATE_001);
		}
		
		JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
		jsonReturn	.add("respuesta", respuesta)
					.add("titulo", titulo)
					.add("mensaje", mensaje);

		return jsonReturn.build().toString();
	}
		
	@RequestMapping(value = "/table", method = RequestMethod.POST)	
	public @ResponseBody String table(	@RequestParam("offset") int offset,
										@RequestParam("limit") int limit,
										@RequestParam("_csrf") String _csrf,
										@RequestParam(value="search", required=false, defaultValue="") String search,
										@RequestParam(value="txtBootstrapTableDesde", required=false) String txtBootstrapTableDesde,
										@RequestParam(value="txtBootstrapTableHasta", required=false) String txtBootstrapTableHasta) {
		
		DataTable<CatalogoActivoEntity> dtCatalogo = catActivoService.dataTable(search, offset, limit, txtBootstrapTableDesde, txtBootstrapTableHasta);
		
		JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
		jsonReturn.add("total", dtCatalogo.getTotal());
		JsonArrayBuilder jsonRows = Json.createArrayBuilder();
		
		dtCatalogo.getRows().forEach((itemCatActivo)-> {

			jsonRows.add(Json.createObjectBuilder()
				.add("idCatalogoActivo", itemCatActivo.getIdCatalogoActivo())
				.add("nombre", itemCatActivo.getNombre())
				.add("descripcion", itemCatActivo.getDescripcion())
				.add("fecharegistro", itemCatActivo.getCreacionFechaNatural()));
		});
		jsonReturn.add("rows", jsonRows);	

		return jsonReturn.build().toString();
	}
	
	// @RequestMapping("{id}/delete")
	// public RedirectView  delete(@PathVariable(name="id")int id) {

	// 	catActivoService.delete(id);
	// 	RedirectView objRedirectView = new RedirectView("/WebSar/controlPanel/activos");
	// 	return objRedirectView;
		
	// }

	@RequestMapping(value = {"{idCatalogo}/delete","{idCatalogo}/delete/"} ,method = RequestMethod.POST)
	public @ResponseBody String deleteAjax(@PathVariable(name="idCatalogo")int idCatalogo){

		Boolean respuesta = false;
		String titulo = "Oops!";
		String mensaje = "Ocurrió un error al intentar eliminar un Activo.";

		CatalogoActivoEntity objCatalogo= catActivoService.findByIdCatalogoActivo(idCatalogo);
		try {
			if(objCatalogo!=null){
				objCatalogo.setEliminado(true);
				catActivoService.update(objCatalogo);

				 respuesta = true;
				 titulo = "Eliminado!";
				 mensaje = "El catalogo ha sido eliminado exitosamente.";
			}else{
				throw new ApplicationException(EnumException.ACTIVO_DELETE_001);
			}
		} catch (ApplicationException e) {
			throw new ApplicationException(EnumException.ACTIVO_DELETE_001);
		}


		JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
		jsonReturn	.add("respuesta", respuesta)
					.add("titulo", titulo)
					.add("mensaje", mensaje);

										
		return jsonReturn.build().toString();
	}
}
