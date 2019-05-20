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

import com.ibmexico.libraries.notifications.ApplicationException;
import com.ibmexico.libraries.notifications.EnumException;
import com.ibmexico.libraries.notifications.EnumMessage;
import com.ibmexico.components.ModelAndViewComponent;
import com.ibmexico.entities.ProveedorContactoEntity;
import com.ibmexico.entities.ProveedorEntity;
import com.ibmexico.libraries.DataTable;
import com.ibmexico.libraries.Templates;
import com.ibmexico.services.ProveedorContactoService;
import com.ibmexico.services.ProveedorService;
import com.ibmexico.services.SessionService;

@Controller
@RequestMapping("controlPanel/proveedores/")
public class ProveedoresContactosController {

	@Autowired
	@Qualifier("modelAndViewComponent")
	private ModelAndViewComponent modelAndViewComponent;
	
	@Autowired
	@Qualifier("proveedorService")
	private ProveedorService proveedorService;	
	
	@Autowired
	@Qualifier("proveedorContactoService")
	private ProveedorContactoService proveedorContactoService;	
	
	@Autowired
	@Qualifier("sessionService")
	private SessionService sessionService;
	
	@GetMapping({"{paramIdProveedor}/contactos", "{paramIdProveedor}/contactos/"})
	public ModelAndView index(@PathVariable int paramIdProveedor) {
		
		ProveedorEntity objProveedor = proveedorService.findByIdProveedor(paramIdProveedor);
		
		ModelAndView objModelAndView = modelAndViewComponent.createModelAndViewControlPanel(Templates.CONTROL_PANEL_PROVEEDORES_CONTACTOS_INDEX);
		objModelAndView.addObject("objProveedor", objProveedor);
		return objModelAndView;
	}
	
	@RequestMapping(value = "{paramIdProveedor}/contactos/table", method = RequestMethod.POST)	
	public @ResponseBody String table(	@PathVariable("paramIdProveedor") int paramIdProveedor,
										@RequestParam("offset") int offset,
										@RequestParam("limit") int limit,
										@RequestParam("_csrf") String _csrf,
										@RequestParam(value="search", required=false, defaultValue="") String search,
										@RequestParam(value="txtBootstrapTableDesde", required=false) String txtBootstrapTableDesde,
										@RequestParam(value="txtBootstrapTableHasta", required=false) String txtBootstrapTableHasta) {
		
		DataTable<ProveedorContactoEntity> dtProveedorContacto = proveedorContactoService.dataTable(paramIdProveedor, search, offset, limit, txtBootstrapTableDesde, txtBootstrapTableHasta);
		
		JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
		jsonReturn.add("total", dtProveedorContacto.getTotal());
		JsonArrayBuilder jsonRows = Json.createArrayBuilder();
		
		dtProveedorContacto.getRows().forEach((itemContacto)-> {
			jsonRows.add(Json.createObjectBuilder()
					
				.add("idProveedorContacto", itemContacto.getIdProveedorContacto())
				.add("contacto", itemContacto.getContacto())
				.add("puesto", itemContacto.getPuesto())
				.add("correo", itemContacto.getCorreo())
				.add("telefono", itemContacto.getTelefono())
				.add("celular", itemContacto.getCelular())
				
				.add("eliminado", itemContacto.isEliminado())
			);
		});
		
		jsonReturn.add("rows", jsonRows);

		return jsonReturn.build().toString();
	}

	@GetMapping("{paramIdProveedor}/contactos/create")
	public ModelAndView create(@PathVariable int paramIdProveedor) {
		ProveedorEntity objProveedor = proveedorService.findByIdProveedor(paramIdProveedor);
		
		ModelAndView objModelAndView = modelAndViewComponent.createModelAndViewControlPanel(Templates.CONTROL_PANEL_PROVEEDORES_CONTACTOS_CREATE);
		objModelAndView.addObject("objProveedor", objProveedor);
		
		return objModelAndView;
	}
	
	@RequestMapping(value = "{paramIdProveedor}/contactos", method = RequestMethod.POST)
	public RedirectView store(	@RequestParam(value="hddIdProveedor") int hddIdProveedor,
								@RequestParam(value="txtContacto") String txtContacto,
								@RequestParam(value="chkAdministrador", required=false, defaultValue="false") String chkAdministrador,
								@RequestParam(value="txtPuesto", required=false) String txtPuesto,
								@RequestParam(value="txtCorreo") String txtCorreo,
								@RequestParam(value="txtTelefono", required=false) String txtTelefono,
								@RequestParam(value="txtCelular") String txtCelular,
								
								RedirectAttributes objRedirectAttributes) {

		RedirectView objRedirectView = null;
		ProveedorContactoEntity objProveedorContacto = new ProveedorContactoEntity();
		
		try {
			objProveedorContacto.setProveedor(proveedorService.findByIdProveedor(hddIdProveedor));
			objProveedorContacto.setContacto(txtContacto);
			objProveedorContacto.setPuesto(txtPuesto);
			objProveedorContacto.setCorreo(txtCorreo);
			objProveedorContacto.setTelefono(txtTelefono);
			objProveedorContacto.setCelular(txtCelular);
			
			if(chkAdministrador.equals("true")) {
				objProveedorContacto.setAdministrador(true);
			}
			
			proveedorContactoService.create(objProveedorContacto);

			objRedirectView = new RedirectView("/WebSar/controlPanel/proveedores/" + hddIdProveedor + "/contactos");
			modelAndViewComponent.addResult(objRedirectAttributes, EnumMessage.PROVEEDORES_CONTACTOS_CREATE_001);
			
		} catch(ApplicationException exception) {
			objRedirectView = new RedirectView("/WebSar/controlPanel/proveedores/" + hddIdProveedor + "/contactos/create");
			modelAndViewComponent.addResult(objRedirectAttributes, exception);
		} catch(NumberFormatException exceptionNumber) {
			objRedirectView = new RedirectView("/WebSar/controlPanel/proveedores/" + hddIdProveedor + "/contactos/create");
			modelAndViewComponent.addResult(objRedirectAttributes, EnumException.GENERAL_PARSE);
		}
			
		
		return objRedirectView;
	}
	
	@GetMapping({"{paramIdProveedor}/contactos/{paramIdProveedorContacto}/edit", "{paramIdProveedor}/contactos/{paramIdProveedorContacto}/edit/"})
	public ModelAndView edit(@PathVariable("paramIdProveedor") int paramIdProveedor,
							 @PathVariable("paramIdProveedorContacto") int paramIdProveedorContacto) {
		
		ProveedorEntity objProveedor = proveedorService.findByIdProveedor(paramIdProveedor);
		ProveedorContactoEntity objProveedorContacto = proveedorContactoService.findByIdProveedorContacto(paramIdProveedorContacto);
				
		ModelAndView objModelAndView = modelAndViewComponent.createModelAndViewControlPanel(Templates.CONTROL_PANEL_PROVEEDORES_CONTACTOS_EDIT);
		objModelAndView.addObject("objProveedor", objProveedor);
		objModelAndView.addObject("objProveedorContacto", objProveedorContacto);
		
		return objModelAndView;
	}
	
	@RequestMapping(value = {"{paramIdProveedor}/contactos/{paramIdProveedorContacto}", "{paramIdProveedor}/contactos/{paramIdProveedorContacto}/"}, method = RequestMethod.PUT)
	public RedirectView store(	@PathVariable("paramIdProveedor") int paramIdProveedor,
								@PathVariable("paramIdProveedorContacto") int paramIdProveedorContacto,
								@RequestParam(value="hddIdProveedor") int hddIdProveedor,
								@RequestParam(value="txtContacto") String txtContacto,
								@RequestParam(value="chkAdministrador", required=false, defaultValue="false") String chkAdministrador,
								@RequestParam(value="txtPuesto", required=false) String txtPuesto,
								@RequestParam(value="txtCorreo") String txtCorreo,
								@RequestParam(value="txtTelefono", required=false) String txtTelefono,
								@RequestParam(value="txtCelular") String txtCelular,
								
								RedirectAttributes objRedirectAttributes) {

		RedirectView objRedirectView = null;
		ProveedorContactoEntity objProveedorContacto = proveedorContactoService.findByIdProveedorContacto(paramIdProveedorContacto);
		
		try {
			
			if(objProveedorContacto != null) {
				
				objProveedorContacto.setContacto(txtContacto);
				objProveedorContacto.setPuesto(txtPuesto);
				objProveedorContacto.setCorreo(txtCorreo);
				objProveedorContacto.setTelefono(txtTelefono);
				objProveedorContacto.setCelular(txtCelular);
				
				if(chkAdministrador.equals("true")) {
					objProveedorContacto.setAdministrador(true);
				}
				
				proveedorContactoService.update(objProveedorContacto);
				
				objRedirectView = new RedirectView("/WebSar/controlPanel/proveedores/" + hddIdProveedor + "/contactos");
				modelAndViewComponent.addResult(objRedirectAttributes, EnumMessage.PROVEEDORES_CONTACTOS_UPDATE_001);
				
			} else {
				throw new ApplicationException(EnumException.CLIENTES_CONTACTOS_UPDATE_001);
			}
			
		} catch(ApplicationException exception) {
			objRedirectView = new RedirectView("/WebSar/controlPanel/proveedores/" + hddIdProveedor + "/contactos/" + paramIdProveedorContacto + "/edit");
			modelAndViewComponent.addResult(objRedirectAttributes, exception);
		} catch(NumberFormatException exceptionNumber) {
			objRedirectView = new RedirectView("/WebSar/controlPanel/proveedores/" + hddIdProveedor + "/contactos/" + paramIdProveedorContacto + "/edit");
			modelAndViewComponent.addResult(objRedirectAttributes, EnumException.GENERAL_PARSE);
		}
					
		return objRedirectView;
	}

	@RequestMapping(value = {"{paramIdProveedor}/contactos/lock", "{paramIdProveedor}/contactos/lock/"}, method = RequestMethod.POST)
	public @ResponseBody String lock(	@PathVariable("paramIdProveedor") int paramIdProveedor,
										@RequestParam("idProveedorContacto") int idProveedorContacto) {
		
		ProveedorContactoEntity objContacto = proveedorContactoService.findByIdProveedorContacto(idProveedorContacto);
		Boolean respuesta = false;
		String titulo = "";
		String mensaje = "";
				
		try {
			if(objContacto != null) {			
				objContacto.setEliminado(true);
				proveedorContactoService.update(objContacto);
				
				respuesta = true;
				titulo = "Bloqueado!";
				mensaje = "El contacto ha sido bloqueado exitosamente.";
			}
			else {
				throw new ApplicationException(EnumException.PROVEEDORES_CONTACTOS_UNLOCK_001);
			}
			
		} catch(ApplicationException exception) {
			throw new ApplicationException(EnumException.PROVEEDORES_CONTACTOS_LOCK_001);
		}
		
		JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
		jsonReturn	.add("respuesta", respuesta)
					.add("titulo", titulo)
					.add("mensaje", mensaje);

										
		return jsonReturn.build().toString();
	}
	
	@RequestMapping(value = {"{paramIdProveedor}/contactos/unlock", "{paramIdProveedor}/contactos/unlock/"}, method = RequestMethod.POST)
	public @ResponseBody String unlock( @PathVariable("paramIdProveedor") int paramIdProveedor,
										@RequestParam("idProveedorContacto") int idProveedorContacto) {
		
		ProveedorContactoEntity objContacto = proveedorContactoService.findByIdProveedorContacto(idProveedorContacto);
		Boolean respuesta = false;
		String titulo = "";
		String mensaje = "";
				
		try {
			if(objContacto != null) {			
				objContacto.setEliminado(false);
				proveedorContactoService.update(objContacto);
				
				respuesta = true;
				titulo = "Desbloqueado!";
				mensaje = "El contacto ha sido desbloqueado exitosamente.";
			}
			else {
				throw new ApplicationException(EnumException.PROVEEDORES_CONTACTOS_UNLOCK_001);
			}
			
		} catch(ApplicationException exception) {
			throw new ApplicationException(EnumException.PROVEEDORES_CONTACTOS_UNLOCK_001);
		}
		
		JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
		jsonReturn	.add("respuesta", respuesta)
					.add("titulo", titulo)
					.add("mensaje", mensaje);

										
		return jsonReturn.build().toString();
	}

}
