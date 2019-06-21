package com.ibmexico.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.ibmexico.components.ModelAndViewComponent;
import com.ibmexico.libraries.Templates;
import com.ibmexico.services.SessionService;

@Controller
public class LoginController {

	// public static final Log LOG = LogFactory.getLog(LoginController.class);

	@Autowired
	@Qualifier("modelAndViewComponent")
	private ModelAndViewComponent modelAndViewComponent;
	
	@Autowired
	@Qualifier("sessionService")
	private SessionService sessionService;

	@GetMapping({ "/", ""})
	public ModelAndView index(	@RequestParam(name="error", required=false) String error,
								@RequestParam(name="logout", required=false) String logout) {
		
		sessionService.clearSession();
		ModelAndView objModelAndView = modelAndViewComponent.createModelAndView(Templates.LOGIN);
		objModelAndView.addObject("error", error);
		objModelAndView.addObject("logout", logout);
		return objModelAndView;
	}

	@GetMapping("/session")
	public RedirectView session() {
		return new RedirectView("controlPanel");
	}

	/*
	@GetMapping("/sessionDestroy")
	public RedirectView sessionDestroy() {
		System.out.println("***************** ENTRO SESSION DESTROY **********************");
		sessionService.clearSession();
		return new RedirectView("/Suburbios/?logout");
	}
	
	
	@RequestMapping(value="/logout1", method = RequestMethod.POST)
	public RedirectView logoutPage (HttpServletRequest request, HttpServletResponse response) {
		
		System.out.println("***************** ENTRO logoutPage **********************");
	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    if (auth != null){    
	        new SecurityContextLogoutHandler().logout(request, response, auth);
	    }
	    return new RedirectView("/logout");
	}
	*/
}
