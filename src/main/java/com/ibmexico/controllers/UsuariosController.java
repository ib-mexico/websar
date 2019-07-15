package com.ibmexico.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
import com.ibmexico.entities.SucursalEntity;
import com.ibmexico.entities.UsuarioEntity;
import com.ibmexico.entities.UsuarioRolEntity;
import com.ibmexico.libraries.DataTable;
import com.ibmexico.libraries.Templates;
import com.ibmexico.services.EmpresaService;
import com.ibmexico.services.RolesService;
import com.ibmexico.services.SessionService;
import com.ibmexico.services.SucursalService;
import com.ibmexico.services.UsuarioRolService;
import com.ibmexico.services.UsuarioService;
import com.pusher.rest.Pusher;
import com.ibmexico.entities.EmpresaEntity;
import com.ibmexico.entities.RolCategoriaEntity;
import com.ibmexico.entities.RolEntity;

@Controller
@RequestMapping("controlPanel/usuarios")
public class UsuariosController {

	@Autowired
	@Qualifier("modelAndViewComponent")
	private ModelAndViewComponent modelAndViewComponent;
	
	@Autowired
	@Qualifier("usuarioService")
	private UsuarioService usuarioService;	
	
	@Autowired
	@Qualifier("sucursalService")
	private SucursalService sucursalService;
	
	@Autowired
	@Qualifier("rolesService")
	private RolesService rolesService;
	
	@Autowired
	@Qualifier("usuarioRolService")
	private UsuarioRolService usuarioRolService;
	
	@Autowired
	@Qualifier("empresaService")
	private EmpresaService empresaService;
	
	@Autowired
	@Qualifier("sessionService")
	private SessionService sessionService;
	
	@GetMapping({"", "/"})
	public ModelAndView index() {
		ModelAndView objModelAndView = modelAndViewComponent.createModelAndViewControlPanel(Templates.CONTROL_PANEL_USUARIOS_INDEX);
		objModelAndView.addObject("rolNuevoUsuario", sessionService.hasRol("USUARIOS_CREATE"));
		return objModelAndView;
	}
	
	@RequestMapping(value = "/table", method = RequestMethod.POST)	
	public @ResponseBody String table(	@RequestParam("offset") int offset,
										@RequestParam("limit") int limit,
										@RequestParam("_csrf") String _csrf,
										@RequestParam(value="search", required=false, defaultValue="") String search,
										@RequestParam(value="txtBootstrapTableDesde", required=false) String txtBootstrapTableDesde,
										@RequestParam(value="txtBootstrapTableHasta", required=false) String txtBootstrapTableHasta) {
		
		DataTable<UsuarioEntity> dtUsuarios = usuarioService.dataTable(search, offset, limit, txtBootstrapTableDesde, txtBootstrapTableHasta);
		
		JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
		jsonReturn.add("total", dtUsuarios.getTotal());
		JsonArrayBuilder jsonRows = Json.createArrayBuilder();
		
		dtUsuarios.getRows().forEach((itemUsuario)-> {

			jsonRows.add(Json.createObjectBuilder()
				.add("idUsuario", itemUsuario.getIdUsuario())				
				.add("clave", itemUsuario.getClave())
				.add("usuario", itemUsuario.getCorreo())
				.add("sucursal", itemUsuario.getSucursal().getSucursal())
				.add("nombreCompleto", itemUsuario.getNombreCompleto())
				
				.add("direccion", itemUsuario.getDireccion())
				.add("telefono", itemUsuario.getTelefono())
				.add("celular", itemUsuario.getCelular())
				
				.add("creacionUsuario", itemUsuario.getCreacionUsuario().getCorreo())
				.add("creacionFecha", itemUsuario.getCreacionFechaNatural())
				.add("modificacionUsuario", itemUsuario.getModificacionUsuario().getCorreo())
				.add("modificacionFecha", itemUsuario.getModificacionFechaNatural())
				
				.add("eliminado", itemUsuario.isEliminado())
			);
		});
		jsonReturn.add("rows", jsonRows);

		return jsonReturn.build().toString();
	}
	
	@GetMapping("/create")
	public ModelAndView create() {
		
		List<SucursalEntity> lstSucursales = sucursalService.listSucursales();
		List<EmpresaEntity> lstEmpresas = empresaService.listEmpresas();
		
		ModelAndView objModelAndView = modelAndViewComponent.createModelAndViewControlPanel(Templates.CONTROL_PANEL_USUARIOS_CREATE);
		objModelAndView.addObject("lstSucursales", lstSucursales);
		objModelAndView.addObject("lstEmpresas", lstEmpresas);
		
		return objModelAndView;
	}
	
	@RequestMapping(value = "", method = RequestMethod.POST)
	public RedirectView store( @RequestParam(value="txtNombre") String txtNombre,								
								@RequestParam(value="txtApellidoPaterno") String txtApellidoPaterno,
								@RequestParam(value="txtApellidoMaterno") String txtApellidoMaterno,
								@RequestParam(value="txtCorreo") String txtCorreo,
								@RequestParam(value="txtPuesto") String txtPuesto,
								@RequestParam(value="txtClave") String txtClave,
								@RequestParam(value="txtDireccion", required=false) String txtDireccion,
								@RequestParam(value="cmbEmpresa") int cmbEmpresa,
								@RequestParam(value="txtTelefono", required=false) String txtTelefono,
								@RequestParam(value="txtCelular", required=false) String txtCelular,
								@RequestParam(value="cmbSucursal") int cmbSucursal,
								@RequestParam(value="txtUsername") String txtUsername,
								@RequestParam(value="txtPassword") String txtPassword,
								
								RedirectAttributes objRedirectAttributes) {
					
		RedirectView objRedirectView = null;
		UsuarioEntity objUsuario = new UsuarioEntity();
		
		try {
							
			objUsuario.setNombre(txtNombre);
			objUsuario.setApellidoPaterno(txtApellidoPaterno);
			objUsuario.setApellidoMaterno(txtApellidoMaterno);
			objUsuario.setUsername(txtUsername);
			objUsuario.setCorreo(txtCorreo);
			objUsuario.setPuesto(txtPuesto);
			objUsuario.setClave(txtClave);
			objUsuario.setDireccion(txtDireccion);
			objUsuario.setEmpresa(empresaService.findByIdEmpresa(cmbEmpresa));
			objUsuario.setTelefono(txtTelefono);
			objUsuario.setCelular(txtCelular);
			objUsuario.setSucursal(sucursalService.findByIdSucursal(cmbSucursal));
			objUsuario.setPassword(txtPassword);
									
			usuarioService.create(objUsuario);
			objRedirectView = new RedirectView("/WebSar/controlPanel/usuarios");
			modelAndViewComponent.addResult(objRedirectAttributes, EnumMessage.USUARIOS_CREATE_001);
			
		} catch(ApplicationException exception) {
			objRedirectView = new RedirectView("/WebSar/controlPanel/usuarios/create");
			modelAndViewComponent.addResult(objRedirectAttributes, exception);
		}
		
		return objRedirectView;
	}
	
	@GetMapping({"{paramIdUsuario}/edit", "{paramIdUsuario}/edit/"})
	public ModelAndView edit(@PathVariable("paramIdUsuario") int paramIdUsuario) {
		
		UsuarioEntity objUsuario = usuarioService.findByIdUsuario(paramIdUsuario);		
		List<SucursalEntity> lstSucursales = sucursalService.listSucursales();
		List<EmpresaEntity> lstEmpresas = empresaService.listEmpresas();
		
		ModelAndView objModelAndView = modelAndViewComponent.createModelAndViewControlPanel(Templates.CONTROL_PANEL_USUARIOS_EDIT);
		objModelAndView.addObject("objUsuario", objUsuario);
		objModelAndView.addObject("lstSucursales", lstSucursales);
		objModelAndView.addObject("lstEmpresas", lstEmpresas);
		
		return objModelAndView;
	}
	
	@RequestMapping(value = {"{paramIdUsuario}", "{paramIdUsuario}/"}, method = RequestMethod.PUT)
	public RedirectView store(	@PathVariable("paramIdUsuario") int paramIdUsuario,
								@RequestParam(value="hddIdUsuario") int hddIdUsuario,	
								@RequestParam(value="txtNombre") String txtNombre,								
								@RequestParam(value="txtApellidoPaterno") String txtApellidoPaterno,
								@RequestParam(value="txtApellidoMaterno") String txtApellidoMaterno,
								@RequestParam(value="txtPuesto") String txtPuesto,
								@RequestParam(value="txtDireccion", required=false) String txtDireccion,
								@RequestParam(value="cmbEmpresa") int cmbEmpresa,
								@RequestParam(value="txtTelefono", required=false) String txtTelefono,
								@RequestParam(value="txtCelular", required=false) String txtCelular,
								@RequestParam(value="cmbSucursal") int cmbSucursal,
								@RequestParam(value="txtPassword", required=false, defaultValue="empty") String txtPassword,
								
								RedirectAttributes objRedirectAttributes) {
					
		RedirectView objRedirectView = null;
		UsuarioEntity objUsuario = usuarioService.findByIdUsuario(hddIdUsuario);
		
		try {
				
			if(objUsuario != null) {
				
				objUsuario.setNombre(txtNombre);
				objUsuario.setApellidoPaterno(txtApellidoPaterno);
				objUsuario.setApellidoMaterno(txtApellidoMaterno);
				objUsuario.setPuesto(txtPuesto);
				objUsuario.setDireccion(txtDireccion);
				objUsuario.setEmpresa(empresaService.findByIdEmpresa(cmbEmpresa));
				objUsuario.setTelefono(txtTelefono);
				objUsuario.setCelular(txtCelular);
				objUsuario.setSucursal(sucursalService.findByIdSucursal(cmbSucursal));
				
				if(!txtPassword.equals("empty")) {		
					objUsuario.setPassword(txtPassword);
				}								
				
				usuarioService.update(objUsuario);
				
				if(sessionService.getCurrentUser() != objUsuario) {
					
					Pusher pusher = new Pusher("575478", "7b4b9197d41e13beb30d", "8c116fb03f6b79d32085");
					pusher.setCluster("us2");
					
					JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
					jsonReturn	.add("id_usuario", objUsuario.getIdUsuario())
					.add("message", "Los datos de tu cuenta fueron actualizados.");						
					
					pusher.trigger("notifications", "new-notification", jsonReturn.build());
				}
				
				objRedirectView = new RedirectView("/WebSar/controlPanel/usuarios");
				modelAndViewComponent.addResult(objRedirectAttributes, EnumMessage.USUARIOS_UPDATE_001);
				
			} else {
				throw new ApplicationException(EnumException.USUARIOS_UPDATE_001);
			}
			
		} catch(ApplicationException exception) {
			objRedirectView = new RedirectView("/WebSar/controlPanel/usuarios/" + hddIdUsuario + "/edit");
			modelAndViewComponent.addResult(objRedirectAttributes, exception);
		}
		
		return objRedirectView;
	}
	
	@GetMapping({"{paramIdUsuario}/privileges", "{paramIdUsuario}/privileges/"})
	public ModelAndView privilegios(@PathVariable("paramIdUsuario") int paramIdUsuario) {
		
		UsuarioEntity objUsuario = usuarioService.findByIdUsuario(paramIdUsuario);
		List<UsuarioRolEntity> lstUsuarioRoles = usuarioRolService.findByUsuario_IdUsuario(paramIdUsuario);
		ArrayList<Integer> arrUsuarioRoles = new ArrayList<Integer>();
		
		for (UsuarioRolEntity usuarioRol : lstUsuarioRoles) {
			arrUsuarioRoles.add(usuarioRol.getRol().getIdRol());
		}
		
		Map<RolCategoriaEntity, List<RolEntity>> mapRoles = rolesService.getHierarchyFullRoles();
		
		System.out.println(mapRoles.toString());
		
		ModelAndView objModelAndView = modelAndViewComponent.createModelAndViewControlPanel(Templates.CONTROL_PANEL_USUARIOS_PRIVILEGES);
		objModelAndView.addObject("objUsuario", objUsuario);
		objModelAndView.addObject("lstUsuarioRoles", lstUsuarioRoles);
		objModelAndView.addObject("arrUsuarioRoles", arrUsuarioRoles);
		objModelAndView.addObject("mapRoles", mapRoles);
		
		return objModelAndView;
	}
	
	@RequestMapping(value = {"{paramIdUsuario}/privileges", "{paramIdUsuario}/privileges/"}, method = RequestMethod.PUT)
	public RedirectView store(	@PathVariable("paramIdUsuario") int paramIdUsuario,
								@RequestParam(value="chkRol", required=false) Integer[] chkRol,
								RedirectAttributes objRedirectAttributes) {
		
		RedirectView objRedirectView = null;
		UsuarioEntity objUsuario = usuarioService.findByIdUsuario(paramIdUsuario);
		
		try {
				
			if(objUsuario != null) {
				
				usuarioService.createPrivileges(objUsuario, chkRol);
				
				if(sessionService.getCurrentUser() != objUsuario) {
					
					Pusher pusher = new Pusher("575478", "7b4b9197d41e13beb30d", "8c116fb03f6b79d32085");
					pusher.setCluster("us2");
					
					JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
					jsonReturn	.add("id_usuario", objUsuario.getIdUsuario())
					.add("message", "Tus privilegios fueron actualizados, inicia sesión nuevamente para ver los cambios.");						
					
					pusher.trigger("notifications", "new-notification", jsonReturn.build());
				}
				
				objRedirectView = new RedirectView("/WebSar/controlPanel/usuarios");
				modelAndViewComponent.addResult(objRedirectAttributes, EnumMessage.USUARIOS_UPDATE_001);
				
			} else {
				throw new ApplicationException(EnumException.USUARIOS_UPDATE_001);
			}
			
		} catch(ApplicationException exception) {
			objRedirectView = new RedirectView("/WebSar/controlPanel/usuarios/" + paramIdUsuario + "/edit");
			modelAndViewComponent.addResult(objRedirectAttributes, exception);
		}
		
		return objRedirectView;
	}
	
	@RequestMapping(value = {"lock", "lock/"}, method = RequestMethod.POST)
	public @ResponseBody String lock(@RequestParam("idUsuario") int idUsuario) {
		
		UsuarioEntity objUsuario = usuarioService.findByIdUsuario(idUsuario);
		Boolean respuesta = false;
		String titulo = "";
		String mensaje = "";
				
		try {
			if(objUsuario != null) {			
				usuarioService.delete(objUsuario);
				
				respuesta = true;
				titulo = "Bloqueado!";
				mensaje = "El usuario ha sido bloqueado exitosamente.";
			}
			else {
				throw new ApplicationException(EnumException.USUARIOS_LOCK_001);
			}
			
		} catch(ApplicationException exception) {
			throw new ApplicationException(EnumException.USUARIOS_LOCK_001);
		}
		
		JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
		jsonReturn	.add("respuesta", respuesta)
					.add("titulo", titulo)
					.add("mensaje", mensaje);

										
		return jsonReturn.build().toString();
	}
	
	@RequestMapping(value = {"unlock", "unlock/"}, method = RequestMethod.POST)
	public @ResponseBody String unlock( @RequestParam("idUsuario") int idUsuario) {
		
		UsuarioEntity objUsuario = usuarioService.findByIdUsuario(idUsuario);
		Boolean respuesta = false;
		String titulo = "";
		String mensaje = "";
				
		try {
			if(objUsuario != null) {			
				objUsuario.setEliminado(false);
				usuarioService.update(objUsuario);
				
				respuesta = true;
				titulo = "Desbloqueado!";
				mensaje = "El usuario ha sido desbloqueado exitosamente.";
			}
			else {
				throw new ApplicationException(EnumException.USUARIOS_UNLOCK_001);
			}
			
		} catch(ApplicationException exception) {
			throw new ApplicationException(EnumException.USUARIOS_UNLOCK_001);
		}
		
		JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
		jsonReturn	.add("respuesta", respuesta)
					.add("titulo", titulo)
					.add("mensaje", mensaje);

										
		return jsonReturn.build().toString();
	}
}
