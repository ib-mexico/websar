package com.ibmexico.components;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ibmexico.libraries.Templates;
import com.ibmexico.libraries.notifications.ApplicationException;
import com.ibmexico.libraries.notifications.EnumException;
import com.ibmexico.libraries.notifications.EnumMessage;
import com.ibmexico.services.RolesService;
import com.ibmexico.services.SessionService;



@Component("modelAndViewComponent")
public class ModelAndViewComponent {

	@Autowired
	@Qualifier("rolesService")
	private RolesService rolesService;
	
	@Autowired
	@Qualifier("sessionService")
	private SessionService sessionService;

	public ModelAndView createModelAndViewControlPanel(String templateName) {

		Templates objTemplates = new Templates();
		ModelAndView objModelAndViewControlPanel = new ModelAndView(objTemplates.FOUNDATION_CONTROL_PANEL);
		objModelAndViewControlPanel.addObject("_TEMPLATE_", templateName);
		
		//PATH INICIAL
		objModelAndViewControlPanel.addObject("_PATH_", "/WebSar/");
		
		//DATOS DE USUARIO
		objModelAndViewControlPanel.addObject("_USER_", sessionService.getCurrentUser());

		//cambiar nombre de atributo
		objModelAndViewControlPanel.addObject("_ROLES_MENU_", sessionService.getCategoriesRolesMenu());

		objModelAndViewControlPanel.addObject("_VERSION_", "1.5.8");
		
		Map<String, Boolean> mapRoles = sessionService.getStringFullRoles();
		objModelAndViewControlPanel.addObject("_ROLES_", mapRoles);
		
		
		return objModelAndViewControlPanel;
	}

	public ModelAndView createModelAndView(String templateName) {
		Templates objTemplates = new Templates();
	
		ModelAndView objModelAndViewRoot = new ModelAndView(objTemplates.FOUNDATION_ROOT);
		objModelAndViewRoot.addObject("_TEMPLATE_", templateName);
		return objModelAndViewRoot;
	}
	
	public void addResult(RedirectAttributes objRedirectAttributes, ApplicationException exception, Map<String, Object> mapAtrributes) {
		
		mapAtrributes.forEach((key, value)-> {
			objRedirectAttributes.addFlashAttribute(key, value);
		});
		
		addResult(objRedirectAttributes, exception);
	}
	
	public void addResult(RedirectAttributes objRedirectAttributes, ApplicationException exception) {
		objRedirectAttributes.addFlashAttribute("action", false);
		objRedirectAttributes.addFlashAttribute("title", exception.getTitle());
		objRedirectAttributes.addFlashAttribute("code", exception.getCode());
		objRedirectAttributes.addFlashAttribute("simpleMessage", exception.getSimpleMessage());
		objRedirectAttributes.addFlashAttribute("message", exception.getMessage());
	}
	
	
	
	public void addResult(RedirectAttributes objRedirectAttributes, EnumException enumException, Map<String, Object> mapAtrributes) {
		mapAtrributes.forEach((key, value)-> {
			objRedirectAttributes.addFlashAttribute(key, value);
		});
		
		addResult(objRedirectAttributes, enumException);
	}
	
	public void addResult(RedirectAttributes objRedirectAttributes, EnumException enumException) {
		objRedirectAttributes.addFlashAttribute("action", false);
		objRedirectAttributes.addFlashAttribute("title", enumException.getTitle());
		objRedirectAttributes.addFlashAttribute("code", enumException.getCode());
		objRedirectAttributes.addFlashAttribute("message", enumException.getMessage());
		objRedirectAttributes.addFlashAttribute("simpleMessage", enumException.getSimpleMessage());
	}
	
	public void addResult(RedirectAttributes objRedirectAttributes, EnumMessage objMessage) {
		objRedirectAttributes.addFlashAttribute("action", true);
		objRedirectAttributes.addFlashAttribute("title", objMessage.getTitle());
		objRedirectAttributes.addFlashAttribute("code", objMessage.getCode());
		objRedirectAttributes.addFlashAttribute("message", objMessage.getMessage());
		objRedirectAttributes.addFlashAttribute("simpleMessage", objMessage.getSimpleMessage());
	}
}
