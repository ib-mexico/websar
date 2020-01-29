package com.ibmexico.controllers;

import java.time.LocalDate;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import com.ibmexico.components.ModelAndViewComponent;
import com.ibmexico.libraries.Templates;
import com.ibmexico.libraries.notifications.ApplicationException;
import com.ibmexico.services.CotizacionService;
import com.ibmexico.services.IndicadoresService;
import com.ibmexico.services.RolesService;
import com.ibmexico.services.SessionService;
import com.ibmexico.services.UsuarioGrupoService;
import com.ibmexico.services.UsuarioRolService;
import com.ibmexico.services.UsuarioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("controlPanel/Indicadores")
public class IndicadoresController {
	@Autowired
	@Qualifier("modelAndViewComponent")
	private ModelAndViewComponent modelAndViewComponent;

	@Autowired
	@Qualifier("usuarioService")
	private UsuarioService usuarioService;

	@Autowired
	@Qualifier("usuarioGrupoService")
	private UsuarioGrupoService userGrupoService;

	@Autowired
	@Qualifier("rolesService")
	private RolesService rolesService;

	@Autowired
	@Qualifier("cotizacionService")
	private CotizacionService cotizacionService;

	@Autowired
	@Qualifier("usuarioRolService")
	private UsuarioRolService usuarioRolService;

	@Autowired
	@Qualifier("indicadoresService")
	private IndicadoresService indicadoresService;

	@Autowired
	@Qualifier("sessionService")
	private SessionService sessionService;

	// Indicadores de producción
	@GetMapping({ "", "/" })
	public ModelAndView index() {
		ModelAndView objModelAndView = modelAndViewComponent
				.createModelAndViewControlPanel(Templates.INDICADORES_PRODUCCION);
		return objModelAndView;
	}

	/** Consulta Indicadores de produccion de un ejecutivo en un rango de fecha */
	@RequestMapping(value = "get-indicadores-produccion/{paramIdEjecutivo}/{paramFechaInicio}/{paramFechaFin}", method = RequestMethod.GET)
	public @ResponseBody String getIndicadoresProduccion(@PathVariable("paramIdEjecutivo") int paramIdEjecutivo,
			@PathVariable("paramFechaInicio") String paramFechaInicio,
			@PathVariable("paramFechaFin") String paramFechaFin) {
		Boolean respuesta = false;
		JsonObject jsonIndicadoresProduccion = null;
		LocalDate fechaInicio;
		LocalDate fechaFin;

		if (!paramFechaFin.equals("") && !paramFechaInicio.equals("")) {
			String arrFechaInicio[] = paramFechaInicio.split("-");
			int yearInicio = Integer.parseInt(arrFechaInicio[2]);
			int monthInicio = Integer.parseInt(arrFechaInicio[1]);
			int dayInicio = Integer.parseInt(arrFechaInicio[0]);

			String arrFechaFin[] = paramFechaFin.split("-");
			int yearFin = Integer.parseInt(arrFechaFin[2]);
			int monthFin = Integer.parseInt(arrFechaFin[1]);
			int dayFin = Integer.parseInt(arrFechaFin[0]);

			fechaInicio = LocalDate.of(yearInicio, monthInicio, dayInicio);
			fechaFin = LocalDate.of(yearFin, monthFin, dayFin);
			try {

				jsonIndicadoresProduccion = cotizacionService.totalCotizadosPeriodoProduccion(fechaInicio, fechaFin,
						paramIdEjecutivo);
				respuesta = true;
			} catch (Exception e) {

			}
		}
		JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
		jsonReturn.add("jsonIndicadores", jsonIndicadoresProduccion).add("respuesta", respuesta);
		return jsonReturn.build().toString();
	}

	/** Consultar todos los usuarios activos */
	@RequestMapping(value = "get-ejecutivos", method = RequestMethod.GET)
	public @ResponseBody String getEjecutivos() {
		Boolean respuesta = false;
		JsonObject jsonEjecutivos = null;
		try {
			jsonEjecutivos = usuarioService.jsonUsuariosGruposActivos();
			respuesta = true;
		} catch (ApplicationException e) {

		}
		JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
		jsonReturn.add("jsonEjecutivos", jsonEjecutivos).add("respuesta", respuesta);
		return jsonReturn.build().toString();
	}

	/** Consulta Indicadores de produccion de un ejecutivo en un rango de fecha */
	@RequestMapping(value = "get-indicadoresArea-produccion/{paramIdArea}/{paramFechaInicio}/{paramFechaFin}", method = RequestMethod.GET)
	public @ResponseBody String getIndicadoresAreaProduccion(@PathVariable("paramIdArea") int paramIdArea,
			@PathVariable("paramFechaInicio") String paramFechaInicio, @PathVariable("paramFechaFin") String paramFechaFin) {
		Boolean respuesta = false;
		JsonObject jsonIndicadorFacturada = null;
		JsonObject jsonIndicadorPagada=null;
		JsonObject jsonOpnArea=null;
		JsonObject jsonOpnCerradas=null;
		LocalDate fechaInicio;
		LocalDate fechaFin;

		if (!paramFechaFin.equals("") && !paramFechaInicio.equals("")) {
			String arrFechaInicio[] = paramFechaInicio.split("-");
			int yearInicio = Integer.parseInt(arrFechaInicio[2]);
			int monthInicio = Integer.parseInt(arrFechaInicio[1]);
			int dayInicio = Integer.parseInt(arrFechaInicio[0]);

			String arrFechaFin[] = paramFechaFin.split("-");
			int yearFin = Integer.parseInt(arrFechaFin[2]);
			int monthFin = Integer.parseInt(arrFechaFin[1]);
			int dayFin = Integer.parseInt(arrFechaFin[0]);

			fechaInicio = LocalDate.of(yearInicio, monthInicio, dayInicio);
			fechaFin = LocalDate.of(yearFin, monthFin, dayFin);
			try {

				jsonIndicadorFacturada = indicadoresService.jsonCotAreasFacturadas(fechaInicio, fechaFin, paramIdArea);
				jsonIndicadorPagada = indicadoresService.jsonCotAreasPagadaMenos90(paramIdArea,fechaInicio, fechaFin);
				jsonOpnArea=indicadoresService.jsonOpnGeneradasArea(paramIdArea, fechaInicio, fechaFin);
				jsonOpnCerradas=indicadoresService.jsonOpnCerradaArea(paramIdArea, fechaInicio, fechaFin);


				// milista = indicadoresService.getCotizacion();
				respuesta = true;

			} catch (Exception e) {

			}
		}
		JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
		jsonReturn.add("jsonIndicadoresArea", jsonIndicadorFacturada)
					.add("jsonIndicadorPagada", jsonIndicadorPagada)
					.add("jsonOpnArea",jsonOpnArea)
					.add("jsonOpnCerrada",jsonOpnCerradas)
					.add("respuesta", respuesta);
		return jsonReturn.build().toString();			
	}
	
	@RequestMapping(value = "get-usuarioGrupo",method = RequestMethod.GET)
	public @ResponseBody String getUsuarioGrupo(){
		Boolean respuesta = false;
		JsonObject jsonUsuarioGrupo = null;
		try {
			jsonUsuarioGrupo = userGrupoService.jsonUsuarioGrupoActivos();
			respuesta = true;
		} catch (ApplicationException e) {
			
		}
		JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
		jsonReturn.add("jsonUsuarioGrupo", jsonUsuarioGrupo).add("respuesta", respuesta);
		return jsonReturn.build().toString();
	}
}