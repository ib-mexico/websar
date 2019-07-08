package com.ibmexico.controllers;

import java.util.List;

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
import com.ibmexico.entities.ProveedorEntity;
import com.ibmexico.libraries.DataTable;
import com.ibmexico.libraries.Templates;
import com.ibmexico.services.ProveedorService;
import com.ibmexico.services.SessionService;

@Controller
@RequestMapping("controlPanel/proveedores")
public class ProveedoresController {

	@Autowired
	@Qualifier("modelAndViewComponent")
	private ModelAndViewComponent modelAndViewComponent;
	
	@Autowired
	@Qualifier("proveedorService")
	private ProveedorService proveedorService;	
	
	@Autowired
	@Qualifier("sessionService")
	private SessionService sessionService;
	
	@GetMapping({"", "/"})
	public ModelAndView index() {
		ModelAndView objModelAndView = modelAndViewComponent.createModelAndViewControlPanel(Templates.CONTROL_PANEL_PROVEEDORES_INDEX);
		objModelAndView.addObject("rolNuevoProveedor", sessionService.hasRol("PROVEEDORES_CREATE"));
		return objModelAndView;
	}
	
	@RequestMapping(value = "/table", method = RequestMethod.POST)	
	public @ResponseBody String table(	@RequestParam("offset") int offset,
										@RequestParam("limit") int limit,
										@RequestParam("_csrf") String _csrf,
										@RequestParam(value="search", required=false, defaultValue="") String search,
										@RequestParam(value="txtBootstrapTableDesde", required=false) String txtBootstrapTableDesde,
										@RequestParam(value="txtBootstrapTableHasta", required=false) String txtBootstrapTableHasta) {
		
		DataTable<ProveedorEntity> dtProveedores = proveedorService.dataTable(search, offset, limit, txtBootstrapTableDesde, txtBootstrapTableHasta);
		
		JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
		jsonReturn.add("total", dtProveedores.getTotal());
		JsonArrayBuilder jsonRows = Json.createArrayBuilder();
		
		dtProveedores.getRows().forEach((itemProveedor)-> {

			jsonRows.add(Json.createObjectBuilder()
				.add("idProveedor", itemProveedor.getIdProveedor())				
				.add("proveedor", itemProveedor.getProveedor())
				
				.add("razonSocial", itemProveedor.getRazonSocial())
				.add("rfc", itemProveedor.getRfc())
				.add("direccion", itemProveedor.getDireccion())
				.add("ciudad", itemProveedor.getCiudad())
				.add("codigoPostal", itemProveedor.getCodigoPostal())
				
				.add("creacionUsuario", itemProveedor.getCreacionUsuario().getCorreo())
				.add("creacionFecha", itemProveedor.getCreacionFechaNatural())
				.add("modificacionUsuario", itemProveedor.getModificacionUsuario().getCorreo())
				.add("modificacionFecha", itemProveedor.getModificacionFechaNatural())
				
				.add("eliminado", itemProveedor.isEliminado())
			);
		});
		jsonReturn.add("rows", jsonRows);

		return jsonReturn.build().toString();
	}
	
	@GetMapping("/create")
	public ModelAndView create() {

		ModelAndView objModelAndView = modelAndViewComponent.createModelAndViewControlPanel(Templates.CONTROL_PANEL_PROVEEDORES_CREATE);
		
		return objModelAndView;
	}
	
	@RequestMapping(value = "", method = RequestMethod.POST)
	public RedirectView store( @RequestParam(value="txtProveedor") String txtProveedor,
								@RequestParam(value="txtRazonSocial") String txtRazonSocial,
								@RequestParam(value="txtRfc") String txtRfc,
								@RequestParam(value="txtCiudad") String txtCiudad,
								@RequestParam(value="txtDireccion") String txtDireccion,
								@RequestParam(value="txtCodigoPostal") String txtCodigoPostal,
								@RequestParam(value="txtObservaciones") String txtObservaciones,
								
								RedirectAttributes objRedirectAttributes) {
					
		RedirectView objRedirectView = null;
		ProveedorEntity objProveedor = new ProveedorEntity();
		
		try {
							
			objProveedor.setProveedor(txtProveedor);
			objProveedor.setRazonSocial(txtRazonSocial);
			objProveedor.setRfc(txtRfc);
			objProveedor.setCiudad(txtCiudad);
			objProveedor.setDireccion(txtDireccion);
			objProveedor.setCodigoPostal(txtCodigoPostal);
			objProveedor.setObservaciones(txtObservaciones);						
			
			proveedorService.create(objProveedor);
			objRedirectView = new RedirectView("/WebSar/controlPanel/proveedores");
			modelAndViewComponent.addResult(objRedirectAttributes, EnumMessage.PROVEEDORES_CREATE_001);
			
		} catch(ApplicationException exception) {
			objRedirectView = new RedirectView("/WebSar/controlPanel/proveedores/create");
			modelAndViewComponent.addResult(objRedirectAttributes, exception);
		}
		
		return objRedirectView;
	}
	
	@GetMapping({"{paramIdProveedor}/edit", "{paramIdProveedor}/edit/"})
	public ModelAndView edit(@PathVariable("paramIdProveedor") int paramIdProveedor) {
		
		ProveedorEntity objProveedor = proveedorService.findByIdProveedor(paramIdProveedor);

		ModelAndView objModelAndView = modelAndViewComponent.createModelAndViewControlPanel(Templates.CONTROL_PANEL_PROVEEDORES_EDIT);
		objModelAndView.addObject("objProveedor", objProveedor);
		
		return objModelAndView;
	}
	
	@RequestMapping(value = {"{paramIdProveedor}", "{paramIdProveedor}/"}, method = RequestMethod.PUT)
	public RedirectView store( 	@PathVariable("paramIdProveedor") int paramIdProveedor,
								@RequestParam(value="hddIdProveedor") int hddIdProveedor,
								@RequestParam(value="txtProveedor") String txtProveedor,
								@RequestParam(value="txtRazonSocial") String txtRazonSocial,
								@RequestParam(value="txtRfc") String txtRfc,
								@RequestParam(value="txtCiudad") String txtCiudad,
								@RequestParam(value="txtDireccion") String txtDireccion,
								@RequestParam(value="txtCodigoPostal") String txtCodigoPostal,
								@RequestParam(value="txtObservaciones") String txtObservaciones,
								
								RedirectAttributes objRedirectAttributes) {
					
		RedirectView objRedirectView = null;
		ProveedorEntity objProveedor = proveedorService.findByIdProveedor(hddIdProveedor);
		
		try {
			
			if(objProveedor != null) {
				
				objProveedor.setProveedor(txtProveedor);
				objProveedor.setRazonSocial(txtRazonSocial);
				objProveedor.setRfc(txtRfc);
				objProveedor.setCiudad(txtCiudad);
				objProveedor.setDireccion(txtDireccion);
				objProveedor.setCodigoPostal(txtCodigoPostal);
				objProveedor.setObservaciones(txtObservaciones);						
				
				proveedorService.update(objProveedor);
				objRedirectView = new RedirectView("/WebSar/controlPanel/proveedores");
				modelAndViewComponent.addResult(objRedirectAttributes, EnumMessage.PROVEEDORES_UPDATE_001);
			}
			else {
				throw new ApplicationException(EnumException.PROVEEDORES_UPDATE_001);
			}
			
		} catch(ApplicationException exception) {
			objRedirectView = new RedirectView("/WebSar/controlPanel/proveedores/" + hddIdProveedor + "/edit");
			modelAndViewComponent.addResult(objRedirectAttributes, exception);
		}
		
		return objRedirectView;
	}
	
	@RequestMapping(value = {"lock", "lock/"}, method = RequestMethod.POST)
	public @ResponseBody String lock(@RequestParam("idProveedor") int idProveedor) {
		
		ProveedorEntity objProveedor = proveedorService.findByIdProveedor(idProveedor);
		Boolean respuesta = false;
		String titulo = "";
		String mensaje = "";
				
		try {
			if(objProveedor != null) {			
				objProveedor.setEliminado(true);
				proveedorService.update(objProveedor);
				
				respuesta = true;
				titulo = "Bloqueado!";
				mensaje = "El proveedor ha sido bloqueado exitosamente.";
			}
			else {
				throw new ApplicationException(EnumException.PROVEEDORES_LOCK_001);
			}
			
		} catch(ApplicationException exception) {
			throw new ApplicationException(EnumException.PROVEEDORES_LOCK_001);
		}
		
		JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
		jsonReturn	.add("respuesta", respuesta)
					.add("titulo", titulo)
					.add("mensaje", mensaje);

										
		return jsonReturn.build().toString();
	}
	
	@RequestMapping(value = {"unlock", "unlock/"}, method = RequestMethod.POST)
	public @ResponseBody String unlock( @RequestParam("idProveedor") int idProveedor) {
		
		ProveedorEntity objProveedor = proveedorService.findByIdProveedor(idProveedor);
		Boolean respuesta = false;
		String titulo = "";
		String mensaje = "";
				
		try {
			if(objProveedor != null) {			
				objProveedor.setEliminado(false);
				proveedorService.update(objProveedor);
				
				respuesta = true;
				titulo = "Desbloqueado!";
				mensaje = "El proveedor ha sido desbloqueado exitosamente.";
			}
			else {
				throw new ApplicationException(EnumException.PROVEEDORES_UNLOCK_001);
			}
			
		} catch(ApplicationException exception) {
			throw new ApplicationException(EnumException.PROVEEDORES_UNLOCK_001);
		}
		
		JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
		jsonReturn	.add("respuesta", respuesta)
					.add("titulo", titulo)
					.add("mensaje", mensaje);

										
		return jsonReturn.build().toString();
	}
	
	//OBTENER PROVEEDORES MEDIANTE AJAX
	@RequestMapping(value = "/get-proveedores", method = RequestMethod.GET)
	public @ResponseBody String getClientes() {
		
		List<ProveedorEntity> lstProveedores = proveedorService.listProveedoreesActivos();
		JsonArrayBuilder jsonProveedores = Json.createArrayBuilder();
		
		lstProveedores.forEach((itemProveedor)-> {
			
			jsonProveedores.add(Json.createObjectBuilder()
					.add("idProveedor", itemProveedor.getIdProveedor())
					.add("proveedor", itemProveedor.getProveedor())
					.add("rfc", itemProveedor.getRfc())
			);
		});
					
		return jsonProveedores.build().toString();
	}
	
	//AGREGAR PROVEEDOR MEDIANTE AJAX
	@RequestMapping(value = "/add-proveedor", method = RequestMethod.POST)
	public @ResponseBody String storeProveedor( @RequestParam("txtProveedor") String txtProveedor,
												@RequestParam("txtRazonSocial") String txtRazonSocial,
												@RequestParam("txtDireccion") String txtDireccion,
												@RequestParam("txtRFC") String txtRFC,
												@RequestParam("txtCiudad") String txtCiudad,
												@RequestParam("txtCodigoPostal") String txtCodigoPostal) {
		
		ProveedorEntity objProveedor = new ProveedorEntity();
		Boolean respuesta = false;
		String titulo = "";
		String mensaje = "";
				
		try {			
			objProveedor.setProveedor(txtProveedor);
			objProveedor.setRazonSocial(txtRazonSocial);
			objProveedor.setDireccion(txtDireccion);
			objProveedor.setRfc(txtRFC);
			
			objProveedor.setCiudad(txtCiudad);
			objProveedor.setCodigoPostal(txtCodigoPostal);
			objProveedor.setObservaciones("");
			
			proveedorService.create(objProveedor);
				
				respuesta = true;
				titulo = "Proveedor Creado!";
				mensaje = "El proveedor ha sido creado exitosamente.";
			
		} catch(ApplicationException exception) {
			throw new ApplicationException(EnumException.PROVEEDORES_CREATE_001);
		}
		
		JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
		jsonReturn	.add("respuesta", respuesta)
					.add("titulo", titulo)
					.add("mensaje", mensaje);

										
		return jsonReturn.build().toString();
	}
	
	
}
