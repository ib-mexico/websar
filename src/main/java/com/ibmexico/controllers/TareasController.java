package com.ibmexico.controllers;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;

import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.ibmexico.libraries.notifications.ApplicationException;
import com.ibmexico.libraries.notifications.EnumException;
import com.ibmexico.libraries.notifications.EnumMessage;
import com.ibmexico.components.ModelAndViewComponent;
import com.ibmexico.configurations.GeneralConfiguration;
import com.ibmexico.entities.TareaEntity;
import com.ibmexico.entities.TareaParticipanteEntity;
import com.ibmexico.entities.UsuarioEntity;
import com.ibmexico.libraries.Templates;
import com.ibmexico.services.SessionService;
import com.ibmexico.services.TareaParticipanteService;
import com.ibmexico.services.TareaService;
import com.ibmexico.services.UsuarioService;
import com.pusher.rest.Pusher;
import com.ibmexico.components.MailerComponent;
import com.ibmexico.libraries.Formats;

@Controller
@RequestMapping("controlPanel/tareas")
public class TareasController {

	@Autowired
	@Qualifier("modelAndViewComponent")
	private ModelAndViewComponent modelAndViewComponent;
	
	@Autowired
	private MailerComponent mailerComponent;
	
	@Autowired
	@Qualifier("tareaService")
	private TareaService tareaService;
	
	@Autowired
	@Qualifier("tareaParticipanteService")
	private TareaParticipanteService tareaParticipanteService;
	
	@Autowired
	@Qualifier("usuarioService")
	private UsuarioService usuarioService;
	
	@Autowired
	@Qualifier("sessionService")
	private SessionService sessionService;
	
	@GetMapping({"", "/"})
	public ModelAndView index() {
		
		List<TareaEntity> lstTareas = tareaService.listTareas();
		
		JsonArrayBuilder jsonTareas = Json.createArrayBuilder();
		for(TareaEntity itemTarea : lstTareas) {
			
			String participantes = "";
			
			List<TareaParticipanteEntity> lstParticipantes = tareaParticipanteService.findByTarea_IdTarea(itemTarea.getIdTarea());
			for(TareaParticipanteEntity itemParticipante : lstParticipantes) {
				if(participantes != "") {
					participantes += ", ";
				}
				participantes += itemParticipante.getUsuario().getAliasCorreo();
			}
			
			jsonTareas.add(Json.createObjectBuilder()
				.add("id", itemTarea.getIdTarea())
				.add("title", itemTarea.getTarea())
				.add("color", itemTarea.getColor())
				
				.add("usuario", itemTarea.getUsuario().getAliasCorreo())
				.add("descripcion", itemTarea.getDescripcion())
				.add("lugar", itemTarea.getLugar())
				.add("inicioFecha", itemTarea.getInicioFechaNatural() + " - " + itemTarea.getInicioHoraNatural() + " Hrs.")
				.add("finFecha", itemTarea.getFinFechaNatural() + " - " + itemTarea.getFinHoraNatural() + " Hrs.")
				.add("participantes", participantes)
				
				.add("start", itemTarea.getInicioFecha().toString() )
				.add("end", itemTarea.getFinFecha().toString() )
			);
		}
		
		
		
		ModelAndView objModelAndView = modelAndViewComponent.createModelAndViewControlPanel(Templates.CONTROL_PANEL_TAREAS_INDEX);
		objModelAndView.addObject("jsonTareas", jsonTareas.build().toString());
		
		return objModelAndView;
	}
	
	
	@GetMapping(value={"create", "create/{paramFecha}"})
	public ModelAndView create(@PathVariable(value="paramFecha", required=false) String paramFecha) {
		
		List<UsuarioEntity> lstUsuarios = usuarioService.listUsuariosActivos(sessionService.getCurrentUser().getIdUsuario());
				
		ModelAndView objModelAndView = modelAndViewComponent.createModelAndViewControlPanel(Templates.CONTROL_PANEL_TAREAS_CREATE);
		objModelAndView.addObject("lstUsuarios", lstUsuarios);
		
		if(Formats.getInstance().isLocalDate(paramFecha)) {
			objModelAndView.addObject("fecha", Formats.getInstance().toLocalDate(paramFecha).format(GeneralConfiguration.getInstance().getDateFormatterNatural()) + " " + LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")));
		}
		
		return objModelAndView;
	}
	
	@RequestMapping(value = "", method = RequestMethod.POST)
	public RedirectView store(@RequestParam(value="txtTarea") String txtTarea,
								@RequestParam(value="txtFechaInicio") String txtFechaInicio,
								@RequestParam(value="txtFechaFin") String txtFechaFin,
								@RequestParam(value="txtDescripcion", required=false, defaultValue="") String txtDescripcion,
								@RequestParam(value="txtLugar") String txtLugar,
								@RequestParam(value="txtColor", required=false, defaultValue="#00AABB") String txtColor,
								@RequestParam(value="chkParticipante[]", required=false, defaultValue="") Integer[] participantes,
								RedirectAttributes objRedirectAttributes) {
		
		RedirectView objRedirectView = null;
		TareaEntity objTarea = new TareaEntity();
		
		try {
			
			objTarea.setTarea(txtTarea);
			objTarea.setColor(txtColor);
			objTarea.setLugar(txtLugar);
			objTarea.setUsuario(sessionService.getCurrentUser());
			objTarea.setInicioFecha(Formats.getInstance().toLocalDateTime(txtFechaInicio));
			objTarea.setFinFecha(Formats.getInstance().toLocalDateTime(txtFechaFin));
			objTarea.setDescripcion(txtDescripcion);
			
			tareaService.create(objTarea, participantes);
			
			//CORREO DE CREADOR DE TAREA
			Map<String, Object> mapVariables = new HashMap<String, Object>();
			mapVariables.put("objTarea", objTarea);
			try {
				if(EmailValidator.getInstance().isValid(objTarea.getUsuario().getCorreo())) {
					mailerComponent.send(objTarea.getUsuario().getCorreo(), "Tarea creada", Templates.EMAIL_TAREAS_CREATE, mapVariables);
					System.out.println("correo: " + objTarea.getUsuario().getCorreo());
				} else {
					System.out.println("Ocurrió un error y no se pudo enviar el correo: correo no validado");
				}
			} catch(Exception exception) {
				System.out.println("Ocurrió un error y no se pudo enviar el correo: " + exception.getMessage());
			}
			
			//CORREO PARA LOS PARTICIPANTES
			for(TareaParticipanteEntity objParticipante : tareaParticipanteService.findByTarea_IdTarea(objTarea.getIdTarea())) {
				
				Map<String, Object> mapVariables2 = new HashMap<String, Object>();
				mapVariables2.put("objTarea", objTarea);
				mapVariables2.put("participante", objParticipante.getUsuario());
				try {
					if(EmailValidator.getInstance().isValid(objParticipante.getUsuario().getCorreo())) {
						mailerComponent.send(objParticipante.getUsuario().getCorreo(), "Te agregaron a una tarea", Templates.EMAIL_TAREAS_PARTICIPANTES_CREATE, mapVariables2);
						System.out.println("correo: " + objParticipante.getUsuario().getCorreo());
					} else {
						System.out.println("Ocurrió un error y no se pudo enviar el correo: correo no validado");
					}
				} catch(Exception exception) {
					System.out.println("Ocurrió un error y no se pudo enviar el correo: " + exception.getMessage());
				}
				
				Pusher pusher = new Pusher("575478", "7b4b9197d41e13beb30d", "8c116fb03f6b79d32085");
				pusher.setCluster("us2");
				
				JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
				jsonReturn	.add("id_usuario", objParticipante.getUsuario().getIdUsuario())
							.add("message", "Te asignaron en una tarea de " + objTarea.getUsuario().getAlias());						

				pusher.trigger("notifications", "new-notification", jsonReturn.build());
			}

			objRedirectView = new RedirectView("/WebSar/controlPanel/tareas");
			modelAndViewComponent.addResult(objRedirectAttributes, EnumMessage.TAREAS_CREATE_001);
			
		} catch(ApplicationException exception) {
			objRedirectView = new RedirectView("/WebSar/controlPanel/tareas/create");
			modelAndViewComponent.addResult(objRedirectAttributes, exception);
		}
		
		
		return objRedirectView;
	}

	
	@GetMapping({"{paramIdTarea}/edit", "{paramIdTarea}/edit/"})
	public ModelAndView edit(@PathVariable("paramIdTarea") int paramIdTarea) {
		
		boolean boolParticipantes = false;
		
		TareaEntity objTarea = tareaService.findByIdTarea(paramIdTarea);		
		List<TareaParticipanteEntity> lstParticipantes = tareaParticipanteService.findByTarea_IdTarea(objTarea.getIdTarea());	
		List<UsuarioEntity> lstUsuarios = usuarioService.listUsuariosActivos(objTarea.getUsuario().getIdUsuario());
		ArrayList<Integer> arrParticipantes = new ArrayList<Integer>();
		
		for (TareaParticipanteEntity participante : lstParticipantes) {
			arrParticipantes.add(participante.getUsuario().getIdUsuario());
		}
		
		if(lstParticipantes.size() > 0) {
			boolParticipantes = true;
		}
		
		
		ModelAndView objModelAndView = modelAndViewComponent.createModelAndViewControlPanel(Templates.CONTROL_PANEL_TAREAS_EDIT);
		objModelAndView.addObject("objTarea", objTarea);
		objModelAndView.addObject("lstUsuarios", lstUsuarios);
		objModelAndView.addObject("lstParticipantes", lstParticipantes);
		objModelAndView.addObject("arrParticipantes", arrParticipantes);
		objModelAndView.addObject("boolParticipantes", boolParticipantes);
		
		return objModelAndView;
	}
	
	@RequestMapping(value = {"{paramIdTarea}", "{paramIdTarea}/"}, method = RequestMethod.PUT)
	public RedirectView store( @PathVariable("paramIdTarea") int paramIdTarea,
								@RequestParam(value="txtTarea") String txtTarea,
								@RequestParam(value="txtFechaInicio") String txtFechaInicio,
								@RequestParam(value="txtFechaFin") String txtFechaFin,
								@RequestParam(value="txtDescripcion", required=false, defaultValue="") String txtDescripcion,
								@RequestParam(value="txtLugar") String txtLugar,
								@RequestParam(value="txtColor", required=false, defaultValue="#00AABB") String txtColor,
								@RequestParam(value="chkParticipante[]", required=false, defaultValue="") Integer[] participantes,
								RedirectAttributes objRedirectAttributes) {
		
		RedirectView objRedirectView = null;
		TareaEntity objTarea = tareaService.findByIdTarea(paramIdTarea);
		
		try {
			
			if(objTarea != null) {
				
				objTarea.setTarea(txtTarea);
				objTarea.setColor(txtColor);
				objTarea.setLugar(txtLugar);
				objTarea.setUsuario(sessionService.getCurrentUser());
				objTarea.setInicioFecha(Formats.getInstance().toLocalDateTime(txtFechaInicio));
				objTarea.setFinFecha(Formats.getInstance().toLocalDateTime(txtFechaFin));
				objTarea.setDescripcion(txtDescripcion);
				
				tareaService.update(objTarea, participantes);
				
				//CORREO DE CREADOR DE TAREA
				Map<String, Object> mapVariables = new HashMap<String, Object>();
				mapVariables.put("objTarea", objTarea);
				try {
					if(EmailValidator.getInstance().isValid(objTarea.getUsuario().getCorreo())) {
						mailerComponent.send(objTarea.getUsuario().getCorreo(), "Actualización de Tarea", Templates.EMAIL_TAREAS_UPDATE, mapVariables);
						System.out.println("correo: " + objTarea.getUsuario().getCorreo());
					} else {
						System.out.println("Ocurrió un error y no se pudo enviar el correo: correo no validado");
					}
				} catch(Exception exception) {
					System.out.println("Ocurrió un error y no se pudo enviar el correo: " + exception.getMessage());
				}
				
				//CORREO PARA LOS PARTICIPANTES
				for(TareaParticipanteEntity objParticipante : tareaParticipanteService.findByTarea_IdTarea(objTarea.getIdTarea())) {
					
					Map<String, Object> mapVariables2 = new HashMap<String, Object>();
					mapVariables2.put("objTarea", objTarea);
					mapVariables2.put("participante", objParticipante.getUsuario());
					try {
						if(EmailValidator.getInstance().isValid(objParticipante.getUsuario().getCorreo())) {
							mailerComponent.send(objParticipante.getUsuario().getCorreo(), "Actualización de Tarea", Templates.EMAIL_TAREAS_PARTICIPANTES_UPDATE, mapVariables2);
							System.out.println("correo: " + objParticipante.getUsuario().getCorreo());
						} else {
							System.out.println("Ocurrió un error y no se pudo enviar el correo: correo no validado");
						}
					} catch(Exception exception) {
						System.out.println("Ocurrió un error y no se pudo enviar el correo: " + exception.getMessage());
					}
				}
				
				
				objRedirectView = new RedirectView("/WebSar/controlPanel/tareas");
				modelAndViewComponent.addResult(objRedirectAttributes, EnumMessage.TAREAS_UPDATE_001);
			}
			else {
				throw new ApplicationException(EnumException.TAREAS_UPDATE_001);
			}
			
		} catch(ApplicationException exception) {
			objRedirectView = new RedirectView("/WebSar/controlPanel/tareas/" + paramIdTarea + "/edit");
			modelAndViewComponent.addResult(objRedirectAttributes, exception);
		}
		
		
		return objRedirectView;
	}	
	
}
