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
import com.ibmexico.entities.ClienteContactoEntity;
import com.ibmexico.entities.ClienteEntity;
import com.ibmexico.entities.PuestoEntity;
import com.ibmexico.libraries.DataTable;
import com.ibmexico.libraries.Templates;
import com.ibmexico.services.ClienteContactoService;
import com.ibmexico.services.ClienteService;
import com.ibmexico.services.PuestoService;
import com.ibmexico.services.SessionService;

@Controller
@RequestMapping("controlPanel/clientes/")
public class ClientesContactosController {

	@Autowired
	@Qualifier("modelAndViewComponent")
	private ModelAndViewComponent modelAndViewComponent;
	
	@Autowired
	@Qualifier("clienteService")
	private ClienteService clienteService;	
	
	@Autowired
	@Qualifier("clienteContactoService")
	private ClienteContactoService clienteContactoService;	

	@Autowired
	@Qualifier("puestoService")
	private PuestoService puestoService;
	
	@Autowired
	@Qualifier("sessionService")
	private SessionService sessionService;
	
	@GetMapping({"{paramIdCliente}/contactos", "{paramIdCliente}/contactos/"})
	public ModelAndView index(@PathVariable int paramIdCliente) {
		
		ClienteEntity objCliente = clienteService.findByIdCliente(paramIdCliente);
		
		ModelAndView objModelAndView = modelAndViewComponent.createModelAndViewControlPanel(Templates.CONTROL_PANEL_CLIENTES_CONTACTOS_INDEX);
		objModelAndView.addObject("objCliente", objCliente);
		return objModelAndView;
	}
	
	@RequestMapping(value = "{paramIdCliente}/contactos/table", method = RequestMethod.POST)	
	public @ResponseBody String table(	@PathVariable("paramIdCliente") int paramIdCliente,
										@RequestParam("offset") int offset,
										@RequestParam("limit") int limit,
										@RequestParam("_csrf") String _csrf,
										@RequestParam(value="search", required=false, defaultValue="") String search,
										@RequestParam(value="txtBootstrapTableDesde", required=false) String txtBootstrapTableDesde,
										@RequestParam(value="txtBootstrapTableHasta", required=false) String txtBootstrapTableHasta) {
		
		DataTable<ClienteContactoEntity> dtClienteContacto = clienteContactoService.dataTable(paramIdCliente, search, offset, limit, txtBootstrapTableDesde, txtBootstrapTableHasta);
		
		JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
		jsonReturn.add("total", dtClienteContacto.getTotal());
		JsonArrayBuilder jsonRows = Json.createArrayBuilder();
		
		dtClienteContacto.getRows().forEach((itemContacto)-> {
			jsonRows.add(Json.createObjectBuilder()
					
				.add("idClienteContacto", itemContacto.getIdClienteContacto())
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

	@GetMapping("{paramIdCliente}/contactos/create")
	public ModelAndView create(@PathVariable int paramIdCliente) {
		ClienteEntity objCliente = clienteService.findByIdCliente(paramIdCliente);
		List<PuestoEntity> lstPuesto = puestoService.listPuesto();

		ModelAndView objModelAndView = modelAndViewComponent.createModelAndViewControlPanel(Templates.CONTROL_PANEL_CLIENTES_CONTACTOS_CREATE);
		objModelAndView.addObject("objCliente", objCliente);
		objModelAndView.addObject("lstPuesto", lstPuesto);
		
		return objModelAndView;
	}
	
	@RequestMapping(value = "{paramIdCliente}/contactos", method = RequestMethod.POST)
	public RedirectView store(	@RequestParam(value="hddIdCliente") int hddIdCliente,
								@RequestParam(value="txtContacto") String txtContacto,
								@RequestParam(value="chkAdministrador", required=false, defaultValue="false") String chkAdministrador,

								@RequestParam(value="cmbPuesto", required = false) int cmbPuesto,
								@RequestParam(value="txtPuesto", required=false) String txtPuesto,
								@RequestParam(value="txtCorreo") String txtCorreo,
								@RequestParam(value="txtTelefono", required=false) String txtTelefono,
								@RequestParam(value="txtCelular") String txtCelular,
								
								RedirectAttributes objRedirectAttributes) {

		RedirectView objRedirectView = null;
		ClienteContactoEntity objClienteContacto = new ClienteContactoEntity();
		PuestoEntity objPuesto = puestoService.findIdPuesto(cmbPuesto);
		
		try {
			objClienteContacto.setCliente(clienteService.findByIdCliente(hddIdCliente));
			objClienteContacto.setContacto(txtContacto);
			objClienteContacto.setPuesto(objPuesto.getCargo());
			objClienteContacto.setCorreo(txtCorreo);
			objClienteContacto.setTelefono(txtTelefono);
			objClienteContacto.setCelular(txtCelular);
			objClienteContacto.setPuestoContacto(objPuesto);
			
			if(chkAdministrador.equals("true")) {
				objClienteContacto.setAdministrador(true);
			}
			
			clienteContactoService.create(objClienteContacto);

			objRedirectView = new RedirectView("/WebSar/controlPanel/clientes/" + hddIdCliente + "/contactos");
			modelAndViewComponent.addResult(objRedirectAttributes, EnumMessage.CLIENTES_CONTACTOS_CREATE_001);
			
		} catch(ApplicationException exception) {
			objRedirectView = new RedirectView("/WebSar/controlPanel/clientes/" + hddIdCliente + "/contactos/create");
			modelAndViewComponent.addResult(objRedirectAttributes, exception);
		} catch(NumberFormatException exceptionNumber) {
			objRedirectView = new RedirectView("/WebSar/controlPanel/clientes/" + hddIdCliente + "/contactos/create");
			modelAndViewComponent.addResult(objRedirectAttributes, EnumException.GENERAL_PARSE);
		}
			
		
		return objRedirectView;
	}
	
	@GetMapping({"{paramIdCliente}/contactos/{paramIdClienteContacto}/edit", "{paramIdCliente}/contactos/{paramIdClienteContacto}/edit/"})
	public ModelAndView edit(@PathVariable("paramIdCliente") int paramIdCliente,
							 @PathVariable("paramIdClienteContacto") int paramIdClienteContacto) {
		
		ClienteEntity objCliente = clienteService.findByIdCliente(paramIdCliente);
		ClienteContactoEntity objClienteContacto = clienteContactoService.findByIdClienteContacto(paramIdClienteContacto);

		List<PuestoEntity> lstPuesto = puestoService.listPuesto();
				
		ModelAndView objModelAndView = modelAndViewComponent.createModelAndViewControlPanel(Templates.CONTROL_PANEL_CLIENTES_CONTACTOS_EDIT);
		objModelAndView.addObject("objCliente", objCliente);
		objModelAndView.addObject("objClienteContacto", objClienteContacto);
		objModelAndView.addObject("lstPuesto", lstPuesto);
		objModelAndView.addObject("objPuesto", objClienteContacto.getPuestoContacto() != null ? objClienteContacto.getPuestoContacto().getIdPuesto() : 0);
		
		return objModelAndView;
	}
	
	@RequestMapping(value = {"{paramIdCliente}/contactos/{paramIdClienteContacto}", "{paramIdCliente}/contactos/{paramIdClienteContacto}/"}, method = RequestMethod.PUT)
	public RedirectView store(	@PathVariable("paramIdCliente") int paramIdCliente,
								@PathVariable("paramIdClienteContacto") int paramIdClienteContacto,
								@RequestParam(value="hddIdCliente") int hddIdCliente,
								@RequestParam(value="txtContacto") String txtContacto,
								@RequestParam(value="chkAdministrador", required=false, defaultValue="false") String chkAdministrador,
								
								@RequestParam(value = "cmbPuesto",required = false) int cmbPuesto,
								@RequestParam(value="txtPuesto", required=false) String txtPuesto,
								@RequestParam(value="txtCorreo") String txtCorreo,
								@RequestParam(value="txtTelefono", required=false) String txtTelefono,
								@RequestParam(value="txtCelular") String txtCelular,
								
								RedirectAttributes objRedirectAttributes) {

		RedirectView objRedirectView = null;
		ClienteContactoEntity objClienteContacto = clienteContactoService.findByIdClienteContacto(paramIdClienteContacto);
		
		try {
			
			if(objClienteContacto != null) {
				
				objClienteContacto.setContacto(txtContacto);
				objClienteContacto.setPuesto(txtPuesto);
				objClienteContacto.setCorreo(txtCorreo);
				objClienteContacto.setTelefono(txtTelefono);
				objClienteContacto.setCelular(txtCelular);
				if(cmbPuesto > 0 ){
					objClienteContacto.setPuestoContacto(puestoService.findIdPuesto(cmbPuesto));
					if(objClienteContacto.getPuesto() == null){
						objClienteContacto.setPuesto(puestoService.findIdPuesto(cmbPuesto).getCargo());
					}
				}

				if(chkAdministrador.equals("true")) {
					objClienteContacto.setAdministrador(true);
				}
				
				clienteContactoService.update(objClienteContacto);
				
				objRedirectView = new RedirectView("/WebSar/controlPanel/clientes/" + hddIdCliente + "/contactos");
				modelAndViewComponent.addResult(objRedirectAttributes, EnumMessage.CLIENTES_CONTACTOS_UPDATE_001);
				
			} else {
				throw new ApplicationException(EnumException.CLIENTES_CONTACTOS_UPDATE_001);
			}
			
		} catch(ApplicationException exception) {
			objRedirectView = new RedirectView("/WebSar/controlPanel/clientes/" + hddIdCliente + "/contactos/" + paramIdClienteContacto + "/edit");
			modelAndViewComponent.addResult(objRedirectAttributes, exception);
		} catch(NumberFormatException exceptionNumber) {
			objRedirectView = new RedirectView("/WebSar/controlPanel/clientes/" + hddIdCliente + "/contactos/" + paramIdClienteContacto + "/edit");
			modelAndViewComponent.addResult(objRedirectAttributes, EnumException.GENERAL_PARSE);
		}
					
		return objRedirectView;
	}

	@RequestMapping(value = {"{paramIdCliente}/contactos/lock", "{paramIdCliente}/contactos/lock/"}, method = RequestMethod.POST)
	public @ResponseBody String lock(	@PathVariable("paramIdCliente") int paramIdCliente,
										@RequestParam("idClienteContacto") int idClienteContacto) {
		
		ClienteContactoEntity objContacto = clienteContactoService.findByIdClienteContacto(idClienteContacto);
		Boolean respuesta = false;
		String titulo = "";
		String mensaje = "";
				
		try {
			if(objContacto != null) {			
				objContacto.setEliminado(true);
				clienteContactoService.update(objContacto);
				
				respuesta = true;
				titulo = "Bloqueado!";
				mensaje = "El contacto ha sido bloqueado exitosamente.";
			}
			else {
				throw new ApplicationException(EnumException.CLIENTES_CONTACTOS_LOCK_001);
			}
			
		} catch(ApplicationException exception) {
			throw new ApplicationException(EnumException.CLIENTES_CONTACTOS_LOCK_001);
		}
		
		JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
		jsonReturn	.add("respuesta", respuesta)
					.add("titulo", titulo)
					.add("mensaje", mensaje);

										
		return jsonReturn.build().toString();
	}
	
	@RequestMapping(value = {"{paramIdCliente}/contactos/unlock", "{paramIdCliente}/contactos/unlock/"}, method = RequestMethod.POST)
	public @ResponseBody String unlock( @PathVariable("paramIdCliente") int paramIdCliente,
										@RequestParam("idClienteContacto") int idClienteContacto) {
		
		ClienteContactoEntity objContacto = clienteContactoService.findByIdClienteContacto(idClienteContacto);
		Boolean respuesta = false;
		String titulo = "";
		String mensaje = "";
				
		try {
			if(objContacto != null) {			
				objContacto.setEliminado(false);
				clienteContactoService.update(objContacto);
				
				respuesta = true;
				titulo = "Desbloqueado!";
				mensaje = "El contacto ha sido desbloqueado exitosamente.";
			}
			else {
				throw new ApplicationException(EnumException.CLIENTES_CONTACTOS_UNLOCK_001);
			}
			
		} catch(ApplicationException exception) {
			throw new ApplicationException(EnumException.CLIENTES_CONTACTOS_UNLOCK_001);
		}
		
		JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
		jsonReturn	.add("respuesta", respuesta)
					.add("titulo", titulo)
					.add("mensaje", mensaje);

										
		return jsonReturn.build().toString();
	}

}
