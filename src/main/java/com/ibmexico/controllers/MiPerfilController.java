package com.ibmexico.controllers;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.ibmexico.libraries.notifications.ApplicationException;
import com.ibmexico.libraries.notifications.EnumException;
import com.ibmexico.libraries.notifications.EnumMessage;
import com.ibmexico.components.ModelAndViewComponent;
import com.ibmexico.entities.UsuarioEntity;
import com.ibmexico.libraries.Templates;
import com.ibmexico.services.RolesService;
import com.ibmexico.services.SessionService;
import com.ibmexico.services.UsuarioService;

@Controller
@RequestMapping("controlPanel/miPerfil")
public class MiPerfilController {

	@Autowired
	@Qualifier("modelAndViewComponent")
	private ModelAndViewComponent modelAndViewComponent;
	
	@Autowired
	@Qualifier("usuarioService")
	private UsuarioService usuarioService;
	
	@Autowired
	@Qualifier("rolesService")
	private RolesService rolesService;
	
	@Autowired
	@Qualifier("sessionService")
	private SessionService sessionService;
	
	@GetMapping({"", "/"})
	public ModelAndView index() {
		
		UsuarioEntity objUsuario = sessionService.getCurrentUser();
		
		ModelAndView objModelAndView = modelAndViewComponent.createModelAndViewControlPanel(Templates.CONTROL_PANEL_MI_PERFIL_INDEX);
		objModelAndView.addObject("objUsuario", objUsuario);
		return objModelAndView;
	}
	
	@RequestMapping(value = {"", "/"}, method = RequestMethod.PUT)
	public RedirectView store(	
								@RequestParam(value="txtNombre") String txtNombre,								
								@RequestParam(value="txtApellidoPaterno") String txtApellidoPaterno,
								@RequestParam(value="txtApellidoMaterno") String txtApellidoMaterno,
								@RequestParam(value="txtDireccion", required=false) String txtDireccion,
								@RequestParam(value="txtTelefono", required=false) String txtTelefono,
								@RequestParam(value="txtCelular", required=false) String txtCelular,
								@RequestParam(value="fichero", required=false) MultipartFile fichero,
								@RequestParam(value="txtPassword", required=false, defaultValue="empty") String txtPassword,
								
								RedirectAttributes objRedirectAttributes) throws IOException {
					
		RedirectView objRedirectView = null;
		UsuarioEntity objUsuario = usuarioService.findByIdUsuario(sessionService.getCurrentUser().getIdUsuario());
		
		try {
				
			if(objUsuario != null) {
				
				objUsuario.setNombre(txtNombre);
				objUsuario.setApellidoPaterno(txtApellidoPaterno);
				objUsuario.setApellidoMaterno(txtApellidoMaterno);
				objUsuario.setDireccion(txtDireccion);
				objUsuario.setTelefono(txtTelefono);
				objUsuario.setCelular(txtCelular);
				
				if(!txtPassword.equals("empty")) {				
					objUsuario.setPassword(txtPassword);
				}
				
				if(fichero != null) {
					usuarioService.update(objUsuario, fichero);
				} else {
					usuarioService.update(objUsuario);
				}
								
				objRedirectView = new RedirectView("/WebSar/controlPanel/miPerfil");
				modelAndViewComponent.addResult(objRedirectAttributes, EnumMessage.USUARIOS_UPDATE_001);
				
			} else {
				throw new ApplicationException(EnumException.USUARIOS_UPDATE_001);
			}
			
		} catch(ApplicationException exception) {
			objRedirectView = new RedirectView("/WebSar/controlPanel/miPerfil");
			modelAndViewComponent.addResult(objRedirectAttributes, exception);
		}
		
		return objRedirectView;
	}
	
}
