package com.ibmexico.controllers;

import java.util.List;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
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
import com.ibmexico.entities.ClienteGiroEntity;
import com.ibmexico.entities.ClienteGrupoEmpresarialEntity;
import com.ibmexico.entities.SucursalEntity;
import com.ibmexico.entities.UsuarioEntity;
import com.ibmexico.libraries.DataTable;
import com.ibmexico.libraries.Templates;
import com.ibmexico.services.ClienteContactoService;
import com.ibmexico.services.ClienteGiroService;
import com.ibmexico.services.ClienteGrupoEmpresarialService;
import com.ibmexico.services.ClienteService;
import com.ibmexico.services.SessionService;
import com.ibmexico.services.SucursalService;
import com.ibmexico.services.UsuarioService;

@Controller
@RequestMapping("controlPanel/clientes")
public class ClientesController {

	@Autowired
	@Qualifier("modelAndViewComponent")
	private ModelAndViewComponent modelAndViewComponent;
	
	@Autowired
	@Qualifier("clienteService")
	private ClienteService clienteService;
	
	@Autowired
	@Qualifier("clienteGiroService")
	private ClienteGiroService clienteGiroService;
	
	@Autowired
	@Qualifier("clienteGrupoEmpresarialService")
	private ClienteGrupoEmpresarialService clienteGrupoEmpresarialService;
	
	@Autowired
	@Qualifier("clienteContactoService")
	private ClienteContactoService clienteContactoService;
	
	@Autowired
	@Qualifier("sucursalService")
	private SucursalService sucursalService;
	
	@Autowired
	@Qualifier("usuarioService")
	private UsuarioService usuarioService;
	
	@Autowired
	@Qualifier("sessionService")
	private SessionService sessionService;
	
	@GetMapping({"", "/"})
	public ModelAndView index() {
		ModelAndView objModelAndView = modelAndViewComponent.createModelAndViewControlPanel(Templates.CONTROL_PANEL_CLIENTES_INDEX);
		objModelAndView.addObject("rolNuevoCliente", sessionService.hasRol("CLIENTES_CREATE"));
		return objModelAndView;
	}
	
	@GetMapping("/create")
	public ModelAndView create() {
		List<ClienteGiroEntity> lstClientesGiros = clienteGiroService.listClientesGiros();
		List<SucursalEntity> lstSucursales = sucursalService.listSucursales();
		List<UsuarioEntity> lstUsuarios = usuarioService.listUsuariosActivos();
		List<ClienteGrupoEmpresarialEntity> lstGruposEmpresariales = clienteGrupoEmpresarialService.listGruposEmpresariales();

		
		ModelAndView objModelAndView = modelAndViewComponent.createModelAndViewControlPanel(Templates.CONTROL_PANEL_CLIENTES_CREATE);
		objModelAndView.addObject("lstClientesGiros", lstClientesGiros);
		objModelAndView.addObject("lstSucursales", lstSucursales);
		objModelAndView.addObject("lstUsuarios", lstUsuarios);
		objModelAndView.addObject("lstGruposEmpresariales", lstGruposEmpresariales);
		
		return objModelAndView;
	}
	
	@RequestMapping(value = "", method = RequestMethod.POST)
	public RedirectView store( @RequestParam(value="txtNombreComercial") String txtNombreComercial,
								@RequestParam(value="cmbSucursal") int cmbSucursal,
								@RequestParam(value="txtTelefono", required=false) String txtTelefono,
								@RequestParam(value="cmbGiro", required=false) int cmbGiro,
								@RequestParam(value="cmbEjecutivo") int cmbEjecutivo,
								@RequestParam(value="cmbEjecutivoS3s") int cmbEjecutivoS3s,
								@RequestParam(value="cmbEjecutivoR2a") int cmbEjecutivoR2a,
								@RequestParam(value="cmbGrupoEmpresarial", required=false, defaultValue="0") int cmbGrupoEmpresarial,
								@RequestParam(value="txtClienteSae", required=false) String txtClienteSae,
								@RequestParam(value="txtClienteSaeIbMexico", required=false) String txtClienteSaeIbMexico,
								@RequestParam(value="txtClienteSaeS3s", required=false) String txtClienteSaeS3s,
								@RequestParam(value="txtClienteSaeR2a", required=false) String txtClienteSaeR2a,
								@RequestParam(value="txtRazonSocial") String txtRazonSocial,
								@RequestParam(value="txtDireccion", required=false) String txtDireccion,
								@RequestParam(value="txtRFC") String txtRFC,
								@RequestParam(value="txtCiudad", required=false) String txtCiudad,
								@RequestParam(value="txtEstado", required=false) String txtEstado,
								@RequestParam(value="txtCodigoPostal", required=false) String txtCodigoPostal,
								
								RedirectAttributes objRedirectAttributes) {
					
		RedirectView objRedirectView = null;
		ClienteEntity objCliente = new ClienteEntity();
		
		try {
							
			objCliente.setCliente(txtNombreComercial.trim());
			objCliente.setSucursal(sucursalService.findByIdSucursal(cmbSucursal));
			objCliente.setTelefono(txtTelefono);
			objCliente.setClienteGiro(clienteGiroService.findByIdClienteGiro(cmbGiro));
			objCliente.setRazonSocial(txtRazonSocial.trim());
			objCliente.setDireccion(txtDireccion.trim());
			objCliente.setRfc(txtRFC.trim());
			objCliente.setCiudad(txtCiudad.trim());
			objCliente.setEstado(txtEstado.trim());
			objCliente.setCodigoPostal(txtCodigoPostal);
			objCliente.setUsuarioEjecutivo(usuarioService.findByIdUsuarioNoEliminado(cmbEjecutivo));
			objCliente.setUsuarioEjecutivoS3s(usuarioService.findByIdUsuarioNoEliminado(cmbEjecutivoS3s));
			objCliente.setUsuarioEjecutivoR2a(usuarioService.findByIdUsuarioNoEliminado(cmbEjecutivoR2a));
			objCliente.setClienteSae(txtClienteSae);
			/**Campos adicionales para el manejo de num de cliente de las empresas*/
			objCliente.setClienteSaeIbMexico(txtClienteSaeIbMexico);
			objCliente.setClienteSaeR2a(txtClienteSaeR2a);
			objCliente.setClienteSaeS3s(txtClienteSaeS3s);
			
			if(cmbGrupoEmpresarial > 0) {
				objCliente.setClienteGrupoEmpresarial(clienteGrupoEmpresarialService.findByIdGrupoEmpresarial(cmbGrupoEmpresarial));
			}
									
			clienteService.create(objCliente);
			objRedirectView = new RedirectView("/WebSar/controlPanel/clientes");
			modelAndViewComponent.addResult(objRedirectAttributes, EnumMessage.CLIENTES_CREATE_001);
			
		} catch(ApplicationException exception) {
			objRedirectView = new RedirectView("/WebSar/controlPanel/clientes/create");
			modelAndViewComponent.addResult(objRedirectAttributes, exception);
		}
		
		return objRedirectView;
	}
	
	@GetMapping({"{paramIdCliente}/edit", "{paramIdCliente}/edit/"})
	public ModelAndView edit(@PathVariable("paramIdCliente") int paramIdCliente) {
		List<ClienteGiroEntity> lstClientesGiros = clienteGiroService.listClientesGiros();
		List<SucursalEntity> lstSucursales = sucursalService.listSucursales();
		List<UsuarioEntity> lstUsuarios = usuarioService.listUsuariosActivos();
		List<ClienteGrupoEmpresarialEntity> lstGruposEmpresariales = clienteGrupoEmpresarialService.listGruposEmpresariales();
		
		ClienteEntity objCliente = clienteService.findByIdCliente(paramIdCliente);
		
		ModelAndView objModelAndView = modelAndViewComponent.createModelAndViewControlPanel(Templates.CONTROL_PANEL_CLIENTES_EDIT);
		objModelAndView.addObject("objCliente", objCliente);
		objModelAndView.addObject("lstClientesGiros", lstClientesGiros);
		objModelAndView.addObject("lstSucursales", lstSucursales);
		objModelAndView.addObject("lstUsuarios", lstUsuarios);
		objModelAndView.addObject("lstGruposEmpresariales", lstGruposEmpresariales);
		
		return objModelAndView;
	}
	
	@RequestMapping(value = {"{paramIdCliente}", "{paramIdCliente}/"}, method = RequestMethod.PUT)
	public RedirectView store( 	@PathVariable("paramIdCliente") int paramIdCliente,
								@RequestParam(value="txtNombreComercial") String txtNombreComercial,
								@RequestParam(value="cmbSucursal") int cmbSucursal,
								@RequestParam(value="txtTelefono", required=false) String txtTelefono,
								@RequestParam(value="cmbGiro", required=false) int cmbGiro,
								@RequestParam(value="cmbEjecutivo") int cmbEjecutivo,
								@RequestParam(value="cmbEjecutivoS3s") int cmbEjecutivoS3s,
								@RequestParam(value="cmbEjecutivoR2a") int cmbEjecutivoR2a,
								@RequestParam(value="cmbGrupoEmpresarial", required=false, defaultValue="0") int cmbGrupoEmpresarial,
								@RequestParam(value="txtClienteSae", required=false) String txtClienteSae,

								@RequestParam(value="txtClienteSaeIbMexico", required=false) String txtClienteSaeIbMexico,
								@RequestParam(value="txtClienteSaeS3s", required=false) String txtClienteSaeS3s,
								@RequestParam(value="txtClienteSaeR2a", required=false) String txtClienteSaeR2a,
								
								@RequestParam(value="txtRazonSocial") String txtRazonSocial,
								@RequestParam(value="txtDireccion", required=false) String txtDireccion,
								@RequestParam(value="txtRFC") String txtRFC,
								@RequestParam(value="txtCiudad", required=false) String txtCiudad,
								@RequestParam(value="txtEstado", required=false) String txtEstado,
								@RequestParam(value="txtCodigoPostal", required=false) String txtCodigoPostal,
								
								RedirectAttributes objRedirectAttributes) {
					
		RedirectView objRedirectView = null;
		ClienteEntity objCliente = clienteService.findByIdCliente(paramIdCliente);
		
		try {
			
			if(objCliente != null) {
				
				objCliente.setCliente(txtNombreComercial.trim());
				objCliente.setSucursal(sucursalService.findByIdSucursal(cmbSucursal));
				objCliente.setTelefono(txtTelefono);
				objCliente.setClienteGiro(clienteGiroService.findByIdClienteGiro(cmbGiro));
				objCliente.setRazonSocial(txtRazonSocial.trim());
				objCliente.setDireccion(txtDireccion.trim());
				objCliente.setRfc(txtRFC.trim());
				objCliente.setCiudad(txtCiudad.trim());
				objCliente.setEstado(txtEstado.trim());
				objCliente.setCodigoPostal(txtCodigoPostal);
				objCliente.setUsuarioEjecutivo(usuarioService.findByIdUsuarioNoEliminado(cmbEjecutivo));
				objCliente.setUsuarioEjecutivoS3s(usuarioService.findByIdUsuarioNoEliminado(cmbEjecutivoS3s));
				objCliente.setUsuarioEjecutivoR2a(usuarioService.findByIdUsuarioNoEliminado(cmbEjecutivoR2a));
				objCliente.setClienteSae(txtClienteSae);
				/**Datos adicionales para el manejo de cliente SAE por empresa */
				objCliente.setClienteSaeIbMexico(txtClienteSaeIbMexico);
				objCliente.setClienteSaeR2a(txtClienteSaeR2a);
				objCliente.setClienteSaeS3s(txtClienteSaeS3s);
				
				if(cmbGrupoEmpresarial > 0) {
					objCliente.setClienteGrupoEmpresarial(clienteGrupoEmpresarialService.findByIdGrupoEmpresarial(cmbGrupoEmpresarial));
				}
				
				clienteService.update(objCliente);
				objRedirectView = new RedirectView("/WebSar/controlPanel/clientes");
				modelAndViewComponent.addResult(objRedirectAttributes, EnumMessage.CLIENTES_UPDATE_001);
				
			} else {
				throw new ApplicationException(EnumException.CLIENTES_UPDATE_001);
			}
										
		} catch(ApplicationException exception) {
			objRedirectView = new RedirectView("/WebSar/controlPanel/clientes/" + paramIdCliente + "/edit");
			modelAndViewComponent.addResult(objRedirectAttributes, exception);
		}
		
		return objRedirectView;
	}
	
	@RequestMapping(value = {"lock", "lock/"}, method = RequestMethod.POST)
	public @ResponseBody String lock(@RequestParam("idCliente") int idCliente) {
		
		ClienteEntity objCliente = clienteService.findByIdCliente(idCliente);
		Boolean respuesta = false;
		String titulo = "";
		String mensaje = "";
				
		try {
			if(objCliente != null) {			
				objCliente.setEliminado(true);
				clienteService.update(objCliente);
				
				respuesta = true;
				titulo = "Bloqueado!";
				mensaje = "El cliente ha sido bloqueado exitosamente.";
			}
			else {
				throw new ApplicationException(EnumException.CLIENTES_LOCK_001);
			}
			
		} catch(ApplicationException exception) {
			throw new ApplicationException(EnumException.CLIENTES_LOCK_001);
		}
		
		JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
		jsonReturn	.add("respuesta", respuesta)
					.add("titulo", titulo)
					.add("mensaje", mensaje);

										
		return jsonReturn.build().toString();
	}
	
	@RequestMapping(value = {"unlock", "unlock/"}, method = RequestMethod.POST)
	public @ResponseBody String unlock( @RequestParam("idCliente") int idCliente) {
		
		ClienteEntity objCliente = clienteService.findByIdCliente(idCliente);
		Boolean respuesta = false;
		String titulo = "";
		String mensaje = "";
				
		try {
			if(objCliente != null) {			
				objCliente.setEliminado(false);
				clienteService.update(objCliente);
				
				respuesta = true;
				titulo = "Desbloqueado!";
				mensaje = "El cliente ha sido desbloqueado exitosamente.";
			}
			else {
				throw new ApplicationException(EnumException.CLIENTES_UNLOCK_001);
			}
			
		} catch(ApplicationException exception) {
			throw new ApplicationException(EnumException.CLIENTES_UNLOCK_001);
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
		
		DataTable<ClienteEntity> dtClientes = clienteService.dataTable(search, offset, limit, txtBootstrapTableDesde, txtBootstrapTableHasta);
		
		JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
		jsonReturn.add("total", dtClientes.getTotal());
		JsonArrayBuilder jsonRows = Json.createArrayBuilder();
		
		dtClientes.getRows().forEach((itemCliente)-> {

			jsonRows.add(Json.createObjectBuilder()
				.add("idCliente", itemCliente.getIdCliente())
				
				.add("sucursal", itemCliente.getSucursal()!= null ? itemCliente.getSucursal().getSucursal() :"")
				.add("cliente", itemCliente.getCliente())
				.add("ejecutivo", (itemCliente.getUsuarioEjecutivo() != null)?itemCliente.getUsuarioEjecutivo().getNombreCompleto():"")
				.add("ejecutivoS3s", (itemCliente.getUsuarioEjecutivoS3s() != null)?itemCliente.getUsuarioEjecutivoS3s().getNombreCompleto():"")
				.add("ejecutivoR2a", (itemCliente.getUsuarioEjecutivoR2a() != null)?itemCliente.getUsuarioEjecutivoR2a().getNombreCompleto():"")
				.add("clienteGiro", itemCliente.getClienteGiro().getClienteGiro())
				.add("grupoEmpresarial", (itemCliente.getClienteGrupoEmpresarial() != null)?itemCliente.getClienteGrupoEmpresarial().getGrupoEmpresarial(): "")
				.add("telefono", itemCliente.getTelefono()!=null ? itemCliente.getTelefono() :"")
				
				.add("razonSocial", itemCliente.getRazonSocial() != null ? itemCliente.getRazonSocial() :"")
				.add("rfc", itemCliente.getRfc() != null ? itemCliente.getRfc() : "")
				.add("direccion", itemCliente.getDireccion() != null ? itemCliente.getDireccion() :"" )
				.add("ciudad", itemCliente.getCiudad() != null ? itemCliente.getCiudad() : "")
				.add("estado", itemCliente.getEstado()!=null ? itemCliente.getEstado() : "" )				
				.add("codigoPostal", itemCliente.getCodigoPostal()!= null ? itemCliente.getCodigoPostal() :"")
				
				.add("creacion_usuario", itemCliente.getCreacionUsuario().getNombreCompleto())
				.add("eliminado", itemCliente.isEliminado())
			);
		});
		jsonReturn.add("rows", jsonRows);

		return jsonReturn.build().toString();
	}
	
	
	//AGREGAR CLIENTE MEDIANTE AJAX
	@RequestMapping(value = "/add-cliente", method = RequestMethod.POST)
	public @ResponseBody String storeCliente( @RequestParam("txtNombreComercial") String txtNombreComercial,
												@RequestParam("cmbSucursal") int cmbSucursal,
												@RequestParam("cmbGiro") int cmbGiro,
												@RequestParam("txtRazonSocial") String txtRazonSocial,
												@RequestParam("txtRFC") String txtRFC) {
		
		ClienteEntity objCliente = new ClienteEntity();
		Boolean respuesta = false;
		String titulo = "";
		String mensaje = "";
				
		try {			
			objCliente.setCliente(txtNombreComercial.trim());
			objCliente.setSucursal(sucursalService.findByIdSucursal(cmbSucursal));
			objCliente.setClienteGiro(clienteGiroService.findByIdClienteGiro(cmbGiro));
			objCliente.setUsuarioEjecutivo(sessionService.getCurrentUser());
			objCliente.setUsuarioEjecutivoS3s(sessionService.getCurrentUser());
			objCliente.setUsuarioEjecutivoR2a(sessionService.getCurrentUser());
			objCliente.setRazonSocial(txtRazonSocial.trim());
			objCliente.setRfc(txtRFC.trim());
			
			objCliente.setCiudad(" ");
			objCliente.setCodigoPostal(" ");
			objCliente.setDireccion(" ");
			objCliente.setEstado(" ");
			objCliente.setTelefono(" ");
			
			clienteService.create(objCliente);
				
				respuesta = true;
				titulo = "Cliente Creado!";
				mensaje = "El cliente ha sido creado exitosamente.";
			
		} catch(ApplicationException exception) {
			throw new ApplicationException(EnumException.CLIENTES_CREATE_001);
		}
		
		JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
		jsonReturn	.add("respuesta", respuesta)
					.add("titulo", titulo)
					.add("mensaje", mensaje);

										
		return jsonReturn.build().toString();
	}
	
	//OBTENER CLIENTES MEDIANTE AJAX
	@RequestMapping(value = "/get-clientes", method = RequestMethod.GET)
	public @ResponseBody String getClientes() {
		
		List<ClienteEntity> lstClientes = clienteService.listClientesActivos();
		JsonArrayBuilder jsonClientes = Json.createArrayBuilder();
		
		lstClientes.forEach((itemCliente)-> {
			
			jsonClientes.add(Json.createObjectBuilder()
					.add("idCliente", itemCliente.getIdCliente())
					.add("cliente", itemCliente.getCliente())
					.add("rfc", itemCliente.getRfc() != null ? itemCliente.getRfc() : "N/A" )					
			);
		});
					
		return jsonClientes.build().toString();
	}
	
	//OBTENER CONTACTOS DEL CLIENTE MEDIANTE AXIOS
	@RequestMapping(value = "get-contactos/{paramIdCliente}", method = RequestMethod.GET)
	public @ResponseBody String getContactos(@PathVariable("paramIdCliente") int paramIdCliente) {
								
		Boolean respuesta = false;
		JsonObject jsonContactos = null;
				
		try {
			jsonContactos = clienteContactoService.jsonClienteContactosActivos(paramIdCliente);

			respuesta = true;
		} catch(ApplicationException exception) {

		}
					
		JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
		jsonReturn	.add("respuesta", respuesta)
					.add("jsonContactos", jsonContactos);

		
		return jsonReturn.build().toString();
	}

	@RequestMapping(value = "/get-clientes-contactos", method = RequestMethod.GET)
	public @ResponseBody String getClientesContactos( @RequestParam("idCliente") int idCliente) {
		
		List<ClienteContactoEntity> lstClientesContactos = clienteContactoService.listClienteContactosActivos(idCliente);
		JsonArrayBuilder jsonContactos = Json.createArrayBuilder();
		
		lstClientesContactos.forEach((itemContacto)-> {
			
			jsonContactos.add(Json.createObjectBuilder()
					.add("idClienteContacto", itemContacto.getIdClienteContacto())
					.add("contacto", itemContacto.getContacto())
					.add("correo", itemContacto.getCorreo())					
			);
		});
					
		return jsonContactos.build().toString();
	}
	
	@RequestMapping(value = "/add-contacto", method = RequestMethod.POST)
	public @ResponseBody String storeContacto( @RequestParam(value="hddIdCliente") int hddIdCliente,
												@RequestParam(value="txtContacto") String txtContacto,
												@RequestParam(value="chkAdministrador", required=false, defaultValue="false") String chkAdministrador,
												@RequestParam(value="txtPuesto", required=false) String txtPuesto,
												@RequestParam(value="txtCorreo") String txtCorreo,
												@RequestParam(value="txtTelefono", required=false) String txtTelefono,
												@RequestParam(value="txtCelular") String txtCelular) {
		
		ClienteContactoEntity objContacto = new ClienteContactoEntity();
		Boolean respuesta = false;
		String titulo = "";
		String mensaje = "";
				
		try {			
			objContacto.setCliente(clienteService.findByIdCliente(hddIdCliente));
			objContacto.setContacto(txtContacto);
			objContacto.setTelefono(txtTelefono);
			objContacto.setPuesto(txtPuesto);
			objContacto.setCorreo(txtCorreo);
			objContacto.setCelular(txtCelular);
			
			if(chkAdministrador.equals("true")) {
				objContacto.setAdministrador(true);
			}
			
			clienteContactoService.create(objContacto);
				
			respuesta = true;
			titulo = "Contacto Creado!";
			mensaje = "El contacto ha sido creado exitosamente.";
			
		} catch(ApplicationException exception) {
			throw new ApplicationException(EnumException.CLIENTES_CONTACTOS_CREATE_001);
		}
		
		JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
		jsonReturn	.add("respuesta", respuesta)
					.add("titulo", titulo)
					.add("mensaje", mensaje);

										
		return jsonReturn.build().toString();
	}
	
	
}
