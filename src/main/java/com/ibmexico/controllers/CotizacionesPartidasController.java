package com.ibmexico.controllers;

import java.math.BigDecimal;
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
import com.ibmexico.entities.CotizacionEntity;
import com.ibmexico.libraries.Templates;
import com.ibmexico.services.CotizacionPartidaService;
import com.ibmexico.services.CotizacionService;
import com.ibmexico.services.CotizacionTotalesService;
import com.ibmexico.services.SessionService;
import com.ibmexico.entities.CotizacionPartidaEntity;
import com.ibmexico.libraries.DataTable;

@Controller
@RequestMapping("controlPanel/cotizaciones/")
public class CotizacionesPartidasController {

	@Autowired
	@Qualifier("modelAndViewComponent")
	private ModelAndViewComponent modelAndViewComponent;
	
	@Autowired
	@Qualifier("cotizacionService")
	private CotizacionService cotizacionService;
	
	@Autowired
	@Qualifier("cotizacionPartidaService")
	private CotizacionPartidaService cotizacionPartidaService;
	
	@Autowired
	@Qualifier("cotizacionTotalesService")
	private CotizacionTotalesService cotizacionTotalesService;
	
	@Autowired
	@Qualifier("sessionService")
	private SessionService sessionService;
	
	@GetMapping({"{paramIdCotizacion}/partidas", "{paramIdCotizacion}/partidas/"})
	public ModelAndView index(@PathVariable int paramIdCotizacion) {
		
		CotizacionEntity objCotizacion = cotizacionService.findByIdCotizacion(paramIdCotizacion);
		List<CotizacionPartidaEntity> lstPartidas = cotizacionPartidaService.listCotizacionesPartidas(paramIdCotizacion);
		
		ModelAndView objModelAndView = modelAndViewComponent.createModelAndViewControlPanel(Templates.CONTROL_PANEL_COTIZACIONES_PARTIDAS_INDEX);
		objModelAndView.addObject("objCotizacion", objCotizacion);
		objModelAndView.addObject("lstPartidas", lstPartidas);
		
		return objModelAndView;
	}
	
	@RequestMapping(value = "{paramIdCotizacion}/partidas/table", method = RequestMethod.POST)	
	public @ResponseBody String table(	@PathVariable("paramIdCotizacion") int paramIdCotizacion,
										@RequestParam("hddIdCotizacion") int idCotizacion,
										@RequestParam("offset") int offset,
										@RequestParam("limit") int limit,
										@RequestParam("_csrf") String _csrf,
										@RequestParam(value="search", required=false, defaultValue="") String search,
										@RequestParam(value="txtBootstrapTableDesde", required=false) String txtBootstrapTableDesde,
										@RequestParam(value="txtBootstrapTableHasta", required=false) String txtBootstrapTableHasta) {
		
		DataTable<CotizacionPartidaEntity> dtCotizacionPartida = cotizacionPartidaService.dataTable(paramIdCotizacion, search, offset, limit, txtBootstrapTableDesde, txtBootstrapTableHasta);
		
		JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
		jsonReturn.add("total", dtCotizacionPartida.getTotal());
		JsonArrayBuilder jsonRows = Json.createArrayBuilder();
		
		dtCotizacionPartida.getRows().forEach((itemPartida)-> {
			jsonRows.add(Json.createObjectBuilder()
					
				.add("idCotizacionPartida", itemPartida.getIdCotizacionPartida())
				.add("numeroParte", itemPartida.getNumeroParte())
				.add("descripcion", itemPartida.getDescripcion())
				.add("cantidad", itemPartida.getCantidad())
				.add("precioUnitarioListaNatural", itemPartida.getPrecioUnitarioListaNatural())
				.add("descuentoPorcentajeNatural", itemPartida.getDescuentoPorcentajeNatural())
				.add("totalNatural", itemPartida.getTotalNatural())
				
				.add("eliminado", itemPartida.isEliminado())
			);
		});
		
		jsonReturn.add("rows", jsonRows);

		return jsonReturn.build().toString();
	}
	
	@GetMapping("{paramIdCotizacion}/partidas/create")
	public ModelAndView create(@PathVariable int paramIdCotizacion) {
		CotizacionEntity objCotizacion = cotizacionService.findByIdCotizacion(paramIdCotizacion);
		
		ModelAndView objModelAndView = modelAndViewComponent.createModelAndViewControlPanel(Templates.CONTROL_PANEL_COTIZACIONES_PARTIDAS_CREATE);
		objModelAndView.addObject("objCotizacion", objCotizacion);
		
		return objModelAndView;
	}
	
	/*@RequestMapping(value = "{paramIdCotizacion}/partidas", method = RequestMethod.POST)
	public RedirectView store(	@RequestParam(value="hddIdCotizacion") int hddIdCotizacion,
								@RequestParam(value="txtNumeroParte") String txtNumeroParte,
								@RequestParam(value="txtTiempoEntrega") int txtTiempoEntrega,
								@RequestParam(value="txtDescripcion") String txtDescripcion,
								@RequestParam(value="txtCantidad") int txtCantidad,
								@RequestParam(value="txtPrecioUnitarioLista") String txtPrecioUnitarioLista,
								@RequestParam(value="txtDescuento") String txtDescuento,
								
								RedirectAttributes objRedirectAttributes) {

		RedirectView objRedirectView = null;
		CotizacionPartidaEntity objCotizacionPartida = new CotizacionPartidaEntity();
		
		try {
			objCotizacionPartida.setCotizacion(cotizacionService.findByIdCotizacion(hddIdCotizacion));
			objCotizacionPartida.setNumeroParte(txtNumeroParte);;
			objCotizacionPartida.setEntregaDiasHabiles(txtTiempoEntrega);
			objCotizacionPartida.setDescripcion(txtDescripcion.trim());
			objCotizacionPartida.setCantidad(txtCantidad);
			objCotizacionPartida.setPrecioUnitarioLista(new BigDecimal(txtPrecioUnitarioLista));
			objCotizacionPartida.setDescuentoPorcentaje(new BigDecimal(txtDescuento));
			objCotizacionPartida.setUsuario(sessionService.getCurrentUser());
			objCotizacionPartida.setOrdenIndex(0);
			
			cotizacionPartidaService.create(objCotizacionPartida);

			objRedirectView = new RedirectView("/WebSar/controlPanel/cotizaciones/" + hddIdCotizacion + "/partidas");
			modelAndViewComponent.addResult(objRedirectAttributes, EnumMessage.COTIZACIONES_PARTIDAS_CREATE_001);
			
		} catch(ApplicationException exception) {
			objRedirectView = new RedirectView("/WebSar/controlPanel/cotizaciones/" + hddIdCotizacion + "/partidas/create");
			modelAndViewComponent.addResult(objRedirectAttributes, exception);
		} catch(NumberFormatException exceptionNumber) {
			objRedirectView = new RedirectView("/WebSar/controlPanel/cotizaciones/" + hddIdCotizacion + "/partidas/create");
			modelAndViewComponent.addResult(objRedirectAttributes, EnumException.GENERAL_PARSE);
		}
			
		
		return objRedirectView;
	}*/

	@RequestMapping(value = "{paramIdCotizacion}/partidas", method = RequestMethod.POST)
	public @ResponseBody String store( @RequestParam(value="hddIdCotizacion") int hddIdCotizacion,
										@RequestParam(value="txtNumeroParte") String txtNumeroParte,
										@RequestParam(value="txtTiempoEntrega") int txtTiempoEntrega,
										@RequestParam(value="txtDescripcion") String txtDescripcion,
										@RequestParam(value="txtCantidad") int txtCantidad,
										@RequestParam(value="txtPrecioUnitarioLista") String txtPrecioUnitarioLista,
										@RequestParam(value="txtDescuento") String txtDescuento) {
		
		CotizacionPartidaEntity objCotizacionPartida = new CotizacionPartidaEntity();
		Boolean respuesta = false;
		String titulo = "Oops!";
		String mensaje = "Ocurrió un error al crear la partida en la cotización.";
		
		
		try {
			objCotizacionPartida.setCotizacion(cotizacionService.findByIdCotizacion(hddIdCotizacion));
			objCotizacionPartida.setNumeroParte(txtNumeroParte);;
			objCotizacionPartida.setEntregaDiasHabiles(txtTiempoEntrega);
			objCotizacionPartida.setDescripcion(txtDescripcion.trim());
			objCotizacionPartida.setCantidad(txtCantidad);
			objCotizacionPartida.setPrecioUnitarioLista(new BigDecimal(txtPrecioUnitarioLista));
			objCotizacionPartida.setDescuentoPorcentaje(new BigDecimal(txtDescuento));
			objCotizacionPartida.setUsuario(sessionService.getCurrentUser());
			objCotizacionPartida.setOrdenIndex(0);
			
			cotizacionPartidaService.create(objCotizacionPartida);

			respuesta = true;
			titulo = "Excelente!";
			mensaje = "La partida de la cotización se creó exitosamente.";
			
		} catch(ApplicationException exception) {
			throw new ApplicationException(EnumException.COTIZACIONES_PARTIDAS_DELETE_001);
		}
		
		JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
		jsonReturn	.add("respuesta", respuesta)
					.add("titulo", titulo)
					.add("mensaje", mensaje);

										
		return jsonReturn.build().toString();
	}
	
	@GetMapping({"{paramIdCotizacion}/partidas/{paramIdCotizacionPartida}/edit", "{paramIdCotizacion}/partidas/{paramIdCotizacionPartida}/edit/"})
	public ModelAndView edit(@PathVariable("paramIdCotizacion") int paramIdCotizacion,
							 @PathVariable("paramIdCotizacionPartida") int paramIdCotizacionPartida) {
		
		CotizacionEntity objCotizacion = cotizacionService.findByIdCotizacion(paramIdCotizacion);
		CotizacionPartidaEntity objCotizacionPartida = cotizacionPartidaService.findByIdCotizacionPartida(paramIdCotizacionPartida);
				
		ModelAndView objModelAndView = modelAndViewComponent.createModelAndViewControlPanel(Templates.CONTROL_PANEL_COTIZACIONES_PARTIDAS_EDIT);
		objModelAndView.addObject("objCotizacion", objCotizacion);
		objModelAndView.addObject("objCotizacionPartida", objCotizacionPartida);
		
		return objModelAndView;
	}
	
	@RequestMapping(value = {"{paramIdCotizacion}/partidas/{paramIdCotizacionPartida}", "{paramIdCotizacion}/partidas/{paramIdCotizacionPartida}/"}, method = RequestMethod.PUT)
	public RedirectView store( @PathVariable("paramIdCotizacion") int paramIdCotizacion,
								@PathVariable("paramIdCotizacionPartida") int paramIdCotizacionPartida,
								@RequestParam(value="hddIdCotizacion") int hddIdCotizacion,
								@RequestParam(value="txtNumeroParte") String txtNumeroParte,
								@RequestParam(value="txtTiempoEntrega") int txtTiempoEntrega,
								@RequestParam(value="txtDescripcion") String txtDescripcion,
								@RequestParam(value="txtCantidad") int txtCantidad,
								@RequestParam(value="txtPrecioUnitarioLista") String txtPrecioUnitarioLista,
								@RequestParam(value="txtDescuento") String txtDescuento,
								
								RedirectAttributes objRedirectAttributes) {
		RedirectView objRedirectView = null;
		CotizacionEntity objCotizacion = cotizacionService.findByIdCotizacion(paramIdCotizacion);
		CotizacionPartidaEntity objCotizacionPartida = cotizacionPartidaService.findByIdCotizacionPartida(paramIdCotizacionPartida);		
		
		try {
			
			if(objCotizacion != null && objCotizacionPartida != null) {
				
				objCotizacionPartida.setCotizacion(objCotizacion);
				objCotizacionPartida.setNumeroParte(txtNumeroParte);;
				objCotizacionPartida.setEntregaDiasHabiles(txtTiempoEntrega);
				objCotizacionPartida.setDescripcion(txtDescripcion.trim());
				objCotizacionPartida.setCantidad(txtCantidad);
				objCotizacionPartida.setPrecioUnitarioLista(new BigDecimal(txtPrecioUnitarioLista));
				objCotizacionPartida.setDescuentoPorcentaje(new BigDecimal(txtDescuento));
				
				cotizacionPartidaService.update(objCotizacionPartida);
				objRedirectView = new RedirectView("/WebSar/controlPanel/cotizaciones/" + paramIdCotizacion + "/partidas");
				modelAndViewComponent.addResult(objRedirectAttributes, EnumMessage.COTIZACIONES_PARTIDAS_UPDATE_001);
			}
			else {
				throw new ApplicationException(EnumException.COTIZACIONES_PARTIDAS_UPDATE_001);
			}
			
		} catch(ApplicationException exception) {
			objRedirectView = new RedirectView("/WebSar/controlPanel/cotizaciones/" + paramIdCotizacion + "/partidas");
			modelAndViewComponent.addResult(objRedirectAttributes, exception);
		}
		
		return objRedirectView;
	}

	@RequestMapping(value = {"{paramIdCotizacion}/partidas/delete", "{paramIdCotizacion}/partidas/delete/"}, method = RequestMethod.POST)
	public @ResponseBody String delete( @PathVariable("paramIdCotizacion") int paramIdCotizacion,
										@RequestParam("idCotizacion") int idCotizacion,
										@RequestParam("idCotizacionPartida") int idCotizacionPartida) {
		

		CotizacionPartidaEntity objPartida = cotizacionPartidaService.findByIdCotizacionPartida(idCotizacionPartida);
		CotizacionEntity objCotizacion = cotizacionService.findByIdCotizacion(paramIdCotizacion);
		Boolean respuesta = false;
		String titulo = "";
		String mensaje = "";
		String subtotal = "";
		
		
		try {
			if(objPartida != null) {			
				cotizacionPartidaService.delete(objPartida);
				cotizacionTotalesService.calcularTotales(objCotizacion);
				respuesta = true;
				subtotal = objCotizacion.getSubtotalNatural();
				titulo = "Eliminado!";
				mensaje = "La partida ha sido eliminada exitosamente.";
			}
			else {
				throw new ApplicationException(EnumException.COTIZACIONES_PARTIDAS_DELETE_001);
			}
			
		} catch(ApplicationException exception) {
			throw new ApplicationException(EnumException.COTIZACIONES_PARTIDAS_DELETE_001);
		}
		
		JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
		jsonReturn	.add("respuesta", respuesta)
					.add("subtotal", subtotal)
					.add("titulo", titulo)
					.add("mensaje", mensaje);

										
		return jsonReturn.build().toString();
	}
	
	@RequestMapping(value = {"{paramIdCotizacion}/partidas/clone", "{paramIdCotizacion}/partidas/clone/"}, method = RequestMethod.POST)
	public @ResponseBody String clone( @PathVariable("paramIdCotizacion") int paramIdCotizacion,
										@RequestParam("idCotizacion") int idCotizacion,
										@RequestParam("idCotizacionPartida") int idCotizacionPartida) {
		

		CotizacionPartidaEntity objPartida = cotizacionPartidaService.findByIdCotizacionPartida(idCotizacionPartida);
		CotizacionPartidaEntity objCotizacionPartida = new CotizacionPartidaEntity();
		Boolean respuesta = false;
		String titulo = "";
		String mensaje = "";
		
		
		try {
			if(objPartida != null) {			
				
				objCotizacionPartida.setCotizacion(objPartida.getCotizacion());
				objCotizacionPartida.setOrdenIndex(objPartida.getOrdenIndex());
				objCotizacionPartida.setNumeroParte(objPartida.getNumeroParte());
				objCotizacionPartida.setEntregaDiasHabiles(objPartida.getEntregaDiasHabiles());
				objCotizacionPartida.setDescripcion(objPartida.getDescripcion());
				objCotizacionPartida.setCantidad(objPartida.getCantidad());
				objCotizacionPartida.setPrecioUnitarioLista(objPartida.getPrecioUnitarioLista());
				objCotizacionPartida.setDescuentoPorcentaje(objPartida.getDescuentoPorcentaje());
				objCotizacionPartida.setUsuario(objPartida.getUsuario());
				cotizacionPartidaService.create(objCotizacionPartida);
				
				respuesta = true;
				titulo = "Clonado!";
				mensaje = "La partida ha sido clonada exitosamente.";
			}
			else {
				throw new ApplicationException(EnumException.COTIZACIONES_PARTIDAS_CLONE_001);
			}
			
		} catch(ApplicationException exception) {
			throw new ApplicationException(EnumException.COTIZACIONES_PARTIDAS_CLONE_001);
		}
		
		JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
		jsonReturn	.add("respuesta", respuesta)
					.add("titulo", titulo)
					.add("mensaje", mensaje);

										
		return jsonReturn.build().toString();
	}
	
	@RequestMapping(value = {"{paramIdCotizacion}/partida/modificar-inline", "{paramIdCotizacion}/partida/modificar-inline/"}, method = RequestMethod.POST)
	public @ResponseBody String update( @PathVariable("paramIdCotizacion") int paramIdCotizacion,
										@RequestParam("valor") String valor,
										@RequestParam("campo") String campo,
										@RequestParam("idCotizacionPartida") int idCotizacionPartida) {
		

		CotizacionPartidaEntity objPartida = cotizacionPartidaService.findByIdCotizacionPartida(idCotizacionPartida);
		CotizacionEntity objCotizacion = cotizacionService.findByIdCotizacion(paramIdCotizacion);
		Boolean respuesta = false;
		Boolean flagCalculate = false;
		String titulo = "";
		String mensaje = "";
		String subtotal = "";
		String totalPartida = "";
				
		try {
			if(objPartida != null) {			
				switch (campo) {
					case "descripcion":
						objPartida.setDescripcion(valor);
					break;

					case "cantidad":
						objPartida.setCantidad(new Integer(valor));
						flagCalculate = true;
					break;
						
					case "precioUnitario":
						objPartida.setPrecioUnitarioLista(new BigDecimal(valor));
						flagCalculate = true;
					break;
						
					case "descuentoPorcentaje":
						objPartida.setDescuentoPorcentaje(new BigDecimal(valor));
						flagCalculate = true;
					break;
	
					default:
					break;
				}
								
				cotizacionPartidaService.update(objPartida);
				
				if(flagCalculate) {
					cotizacionTotalesService.calcularTotales(objCotizacion);
				}
				
				respuesta = true;
				totalPartida = objPartida.getTotalNatural();
				subtotal = objCotizacion.getSubtotalNatural();
				titulo = "Modificado!";
				mensaje = "El registro se modificó exitosamente.";
			}
			else {
				throw new ApplicationException(EnumException.COTIZACIONES_PARTIDAS_UPDATE_001);
			}
			
		} catch(ApplicationException exception) {
			throw new ApplicationException(EnumException.COTIZACIONES_PARTIDAS_UPDATE_002);
		}
		
		JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
		jsonReturn	.add("respuesta", respuesta)
					.add("subtotal", subtotal)
					.add("totalPartida", totalPartida)
					.add("titulo", titulo)
					.add("mensaje", mensaje);

										
		return jsonReturn.build().toString();
	}
	
	//OBTENER PARTIDAS DE LA COTIZACION MEDIANTE AJAX
	@RequestMapping(value = {"{paramIdCotizacion}/get-partidas", "{paramIdCotizacion}/get-partidas/"}, method = RequestMethod.GET)
	public @ResponseBody String getPartidas(  @PathVariable("paramIdCotizacion") int paramIdCotizacion,
												@RequestParam("idCotizacion") int idCotizacion) {
		
		List<CotizacionPartidaEntity> lstPartidas = cotizacionPartidaService.listCotizacionesPartidas(idCotizacion);
		JsonArrayBuilder jsonPartidas = Json.createArrayBuilder();
		
		lstPartidas.forEach((itemPartida)-> {
			
			jsonPartidas.add(Json.createObjectBuilder()
					.add("idCotizacionPartida", itemPartida.getIdCotizacionPartida())
					.add("ordenIndex", itemPartida.getOrdenIndex())
					.add("cantidad", itemPartida.getCantidad())
					.add("numeroParte", itemPartida.getNumeroParte())
					.add("descripcion", itemPartida.getDescripcion())
			);
		});
					
		return jsonPartidas.build().toString();
	}
	
	@RequestMapping(value = {"{paramIdCotizacion}/sort-partidas", "{paramIdCotizacion}/sort-partidas/"}, method = RequestMethod.POST)
	public @ResponseBody String sort( @PathVariable("paramIdCotizacion") int paramIdCotizacion,
										@RequestParam("ordenPartidas") String ordenPartidas) {
				
		String[] arrStrIdPartidas = ordenPartidas.split(",");	
		
		CotizacionEntity objCotizacion = cotizacionService.findByIdCotizacion(paramIdCotizacion);		
		Boolean respuesta = false;
		String titulo = "";
		String mensaje = "";
		
		
		try {
			if(objCotizacion != null) {	
				
				for(Integer i = 0; i < arrStrIdPartidas.length; i++) {
					Integer idPartida = Integer.parseInt(arrStrIdPartidas[i]);
					
					if(idPartida != null) {
						
						CotizacionPartidaEntity objPartida = cotizacionPartidaService.findByIdCotizacionPartida(idPartida);
						
						objPartida.setOrdenIndex(i);
						cotizacionPartidaService.update(objPartida);
					}
				}
				
				respuesta = true;
				titulo = "Exelente!";
				mensaje = "Las partidas han sido ordenadas satisfactoriamente.";
			}
			else {
				throw new ApplicationException(EnumException.COTIZACIONES_PARTIDAS_SORT_002);
			}
			
		} catch(ApplicationException exception) {
			throw new ApplicationException(EnumException.COTIZACIONES_PARTIDAS_SORT_001);
		}
		
		JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
		jsonReturn	.add("respuesta", respuesta)
					.add("titulo", titulo)
					.add("mensaje", mensaje);

										
		return jsonReturn.build().toString();
	}
	
}
