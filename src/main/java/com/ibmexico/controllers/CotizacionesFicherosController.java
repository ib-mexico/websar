package com.ibmexico.controllers;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import org.thymeleaf.context.Context;

import com.ibmexico.libraries.notifications.ApplicationException;
import com.ibmexico.libraries.notifications.EnumException;
import com.ibmexico.libraries.notifications.EnumMessage;
import com.ibmexico.components.ModelAndViewComponent;
import com.ibmexico.components.PdfComponent;
import com.ibmexico.configurations.GeneralConfiguration;
import com.ibmexico.entities.CotizacionEntity;
import com.ibmexico.entities.CotizacionFicheroEntity;
import com.ibmexico.entities.CotizacionTipoFicheroEntity;
import com.ibmexico.libraries.Templates;
import com.lowagie.text.DocumentException;
import com.ibmexico.services.CotizacionFicheroService;
import com.ibmexico.services.CotizacionService;
import com.ibmexico.services.CotizacionTipoFicheroService;
import com.ibmexico.services.SessionService;

@Controller
@RequestMapping("controlPanel/cotizaciones")
public class CotizacionesFicherosController {

	@Autowired
	@Qualifier("modelAndViewComponent")
	private ModelAndViewComponent modelAndViewComponent;
	
	@Autowired
	@Qualifier("pdfComponent")
	private PdfComponent pdfComponent;
	
	@Autowired
	@Qualifier("cotizacionService")
	private CotizacionService cotizacionService;
	
	@Autowired
	@Qualifier("cotizacionFicheroService")
	private CotizacionFicheroService cotizacionFicheroService;
	
	@Autowired
	@Qualifier("cotizacionTipoFicheroService")
	private CotizacionTipoFicheroService cotizacionTipoFicheroService;
	
	@Autowired
	@Qualifier("sessionService")
	private SessionService sessionService;
	
	@GetMapping({"{paramIdCotizacion}/ficheros", "{paramIdCotizacion}/ficheros/"})
	public ModelAndView index(@PathVariable("paramIdCotizacion") int paramIdCotizacion) {
		
		CotizacionEntity objCotizacion = cotizacionService.findByIdCotizacion(paramIdCotizacion);
		List<CotizacionFicheroEntity> lstFicheros = cotizacionFicheroService.listCotizacionFicheros(paramIdCotizacion);
		List<CotizacionTipoFicheroEntity> lstTiposFicheros = cotizacionTipoFicheroService.listAll();
		
		//TOTAL GASTOS INDIRECTOS
		BigDecimal importeGastos = cotizacionFicheroService.totalImporteDocumentoTipo(paramIdCotizacion, 3);
		
		ModelAndView objModelAndView = modelAndViewComponent.createModelAndViewControlPanel(Templates.CONTROL_PANEL_COTIZACIONES_FICHEROS_INDEX);
		objModelAndView.addObject("objCotizacion", objCotizacion);
		objModelAndView.addObject("lstFicheros", lstFicheros);
		objModelAndView.addObject("lstTiposFicheros", lstTiposFicheros);
		objModelAndView.addObject("rolExpedienteGerente", sessionService.hasRol("COTIZACIONES_EXPEDIENTES_GERENTE"));
		objModelAndView.addObject("rolExpedienteAdministrador", sessionService.hasRol("COTIZACIONES_EXPEDIENTES_ADMINISTRADOR"));
		objModelAndView.addObject("totalGastos", GeneralConfiguration.getInstance().getNumberFormat().format(importeGastos));
		
		return objModelAndView;
	}
	
	@RequestMapping(value = {"{paramIdCotizacion}/ficheros", "{paramIdCotizacion}/ficheros/"}, method = RequestMethod.POST)
	public @ResponseBody String store(	@PathVariable("paramIdCotizacion") int paramIdCotizacion,
								@RequestParam(value="cmbTipoFichero") int cmbTipoFichero,
								@RequestParam(value="txtFolio", required=false, defaultValue="") String txtFolio,
								@RequestParam(value="txtFolioOrdenCompra", required=false, defaultValue="") String txtFolioOrdenCompra,
								@RequestParam(value="txtImporte", required=false, defaultValue="") String txtImporte,
								@RequestParam(value="txtProveedor", required=false, defaultValue="") String txtProveedor,
								@RequestParam(value="txtBanco", required=false, defaultValue="") String txtBanco,
								@RequestParam(value="txtFechaVencimiento", required=false, defaultValue="") String txtFechaVencimiento,
								@RequestParam(value="txtDescripcion") String txtDescripcion,
								@RequestParam(value="fichero", required=false) MultipartFile fichero,
								
								RedirectAttributes objRedirectAttributes) {
		
		CotizacionFicheroEntity objFichero = new CotizacionFicheroEntity();
		
		Boolean respuesta = false;
		String titulo = "";
		String mensaje = "";
		
		try {
			
			objFichero.setCotizacion(cotizacionService.findByIdCotizacion(paramIdCotizacion));
			objFichero.setCotizacionTipoFichero(cotizacionTipoFicheroService.findByIdCotizacionTipoFichero(cmbTipoFichero));
			objFichero.setFolio(txtFolio);
			objFichero.setFolioOrdenCompra(txtFolioOrdenCompra);
			objFichero.setProveedor(txtProveedor);
			objFichero.setBanco(txtBanco);
			
			if(!txtImporte.equals("")) {
				objFichero.setImporte(new BigDecimal(txtImporte));
			}
			
			if(!txtFechaVencimiento.equals("")) {
				objFichero.setVencimientoFecha(LocalDate.parse(txtFechaVencimiento, GeneralConfiguration.getInstance().getDateFormatterNatural()));
			}
			
			objFichero.setObservaciones(txtDescripcion);
			cotizacionFicheroService.addFile(objFichero, fichero);
			
			if(cmbTipoFichero == 3 || cmbTipoFichero == 4) {					
				if(objFichero.getCotizacion().getCotizacionEstatus().getIdCotizacionEstatus() >= 3 &&  objFichero.getCotizacion().getCotizacionEstatus().getIdCotizacionEstatus() != 5) {	
					cotizacionService.recalcularCotizacion(objFichero.getCotizacion());
				}				
			}
			
			respuesta = true;
			titulo = "Cargado!";
			mensaje = "El expediente de la cotizacion se cargo exitosamente.";
			
		} catch(ApplicationException exception) {
			respuesta = false;
			titulo = "Error!";
			mensaje = "Ocurrió un error al cargar el expediente.";
		}
		
		JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
		jsonReturn	.add("respuesta", respuesta)
					.add("titulo", titulo)
					.add("mensaje", mensaje);
		
		return jsonReturn.build().toString();
	}
	
	@GetMapping({"{paramIdCotizacion}/ficheros/{paramIdFichero}/edit", "{paramIdCotizacion}/ficheros/{paramIdFichero}/edit/"})
	public ModelAndView edit(@PathVariable("paramIdCotizacion") int paramIdCotizacion,
							 @PathVariable("paramIdFichero") int paramIdFichero) {
		
		CotizacionFicheroEntity objFichero = cotizacionFicheroService.findIdCotizacionFichero(paramIdFichero);
		List<CotizacionTipoFicheroEntity> lstTiposFicheros = cotizacionTipoFicheroService.listAll();
		
		ModelAndView objModelAndView = modelAndViewComponent.createModelAndViewControlPanel(Templates.CONTROL_PANEL_COTIZACIONES_FICHEROS_EDIT);
		objModelAndView.addObject("objFichero", objFichero);
		objModelAndView.addObject("lstTiposFicheros", lstTiposFicheros);
		
		return objModelAndView;
	}
	
	@RequestMapping(value = {"{paramIdCotizacion}/ficheros/{paramIdCotizacionFichero}", "{paramIdCotizacion}/ficheros/{paramIdCotizacionFichero}/"}, method = RequestMethod.POST)
	public RedirectView store( @PathVariable("paramIdCotizacion") int paramIdCotizacion,
								@PathVariable("paramIdCotizacionFichero") int paramIdCotizacionFichero,
								@RequestParam(value="cmbTipoFichero") int cmbTipoFichero,
								@RequestParam(value="txtFolio", required=false, defaultValue="") String txtFolio,
								@RequestParam(value="txtFolioOrdenCompra", required=false, defaultValue="") String txtFolioOrdenCompra,
								@RequestParam(value="txtImporte", required=false, defaultValue="") String txtImporte,
								@RequestParam(value="txtProveedor", required=false, defaultValue="") String txtProveedor,
								@RequestParam(value="txtBanco", required=false, defaultValue="") String txtBanco,
								@RequestParam(value="txtFechaVencimiento", required=false, defaultValue="") String txtFechaVencimiento,
								@RequestParam(value="txtDescripcion") String txtDescripcion,
								@RequestParam(value="fichero", required=false) MultipartFile fichero,
								
								RedirectAttributes objRedirectAttributes) {
		
		RedirectView objRedirectView = null;
		CotizacionFicheroEntity objFichero = cotizacionFicheroService.findIdCotizacionFichero(paramIdCotizacionFichero);		
		
		try {
			
			if(objFichero != null) {
				
				objFichero.setCotizacion(cotizacionService.findByIdCotizacion(paramIdCotizacion));
				objFichero.setCotizacionTipoFichero(cotizacionTipoFicheroService.findByIdCotizacionTipoFichero(cmbTipoFichero));
				objFichero.setFolio(txtFolio);
				objFichero.setFolioOrdenCompra(txtFolioOrdenCompra);
				objFichero.setProveedor(txtProveedor);
				objFichero.setBanco(txtBanco);
				
				if(!txtImporte.equals("")) {
					objFichero.setImporte(new BigDecimal(txtImporte));
				}
				
				if(!txtFechaVencimiento.equals("")) {
					objFichero.setVencimientoFecha(LocalDate.parse(txtFechaVencimiento, GeneralConfiguration.getInstance().getDateFormatterNatural()));
				}
				
				objFichero.setObservaciones(txtDescripcion);
				
				cotizacionFicheroService.updateFile(objFichero, fichero);
				
				if(cmbTipoFichero == 3 || cmbTipoFichero == 4) {
					if(objFichero.getCotizacion().getCotizacionEstatus().getIdCotizacionEstatus() >= 3 &&  objFichero.getCotizacion().getCotizacionEstatus().getIdCotizacionEstatus() != 5) {	
						cotizacionService.recalcularCotizacion(objFichero.getCotizacion());
					}
				}
				
				objRedirectView = new RedirectView("/WebSar/controlPanel/cotizaciones/" + paramIdCotizacion + "/ficheros");
				modelAndViewComponent.addResult(objRedirectAttributes, EnumMessage.COTIZACIONES_FICHEROS_UPDATE_001);
				
			}
			else {
				throw new ApplicationException(EnumException.COTIZACIONES_FICHEROS_UPDATE_001);
			}
		} catch(ApplicationException exception) {
			objRedirectView = new RedirectView("/WebSar/controlPanel/cotizaciones/" + paramIdCotizacion + "/ficheros/" + paramIdCotizacionFichero);
			modelAndViewComponent.addResult(objRedirectAttributes, exception);
		}
		
		return objRedirectView;
	}
	
	@RequestMapping(value = {"{paramIdCotizacion}/get-ficheros", "{paramIdCotizacion}/get-ficheros/"}, method = RequestMethod.GET)
	public @ResponseBody String getFicheros(  @PathVariable("paramIdCotizacion") int paramIdCotizacion,
												@RequestParam("idCotizacion") int idCotizacion) {
		
		List<CotizacionFicheroEntity> lstFicheros = cotizacionFicheroService.listCotizacionFicheros(idCotizacion);
		JsonArrayBuilder jsonFicheros = Json.createArrayBuilder();
		
		lstFicheros.forEach((itemFichero)-> {
			
			jsonFicheros.add(Json.createObjectBuilder()
					.add("idCotizacionFichero", itemFichero.getIdCotizacionFichero())
					.add("descripcion", itemFichero.getObservaciones())
					.add("url", itemFichero.getUrl())
					.add("ficheroTipo", itemFichero.getFicheroTipo())
			);
		});
					
		return jsonFicheros.build().toString();
	}
	
	@RequestMapping(value = {"{paramIdCotizacion}/reporte-utilidad", "{paramIdCotizacion}/reporte-utilidad/"}, method = RequestMethod.GET, produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<InputStreamResource> previewReporteUtilidad(@PathVariable("paramIdCotizacion") int paramIdCotizacion) throws IOException, DocumentException {
		
		BigDecimal totalUtilidad = new BigDecimal(0);
		
		CotizacionEntity objCotizacion = cotizacionService.findByIdCotizacion(paramIdCotizacion);
		BigDecimal subtotalCotizacion = objCotizacion.getSubtotal();		
		
		List<CotizacionFicheroEntity> lstCompras = cotizacionFicheroService.listDocumentosTipo(paramIdCotizacion, 4);
		List<CotizacionFicheroEntity> lstGastos = cotizacionFicheroService.listDocumentosTipo(paramIdCotizacion, 3);
		
		BigDecimal importeCompras = cotizacionFicheroService.totalImporteDocumentoTipo(paramIdCotizacion, 4);
		BigDecimal importeGastos = cotizacionFicheroService.totalImporteDocumentoTipo(paramIdCotizacion, 3);
		
		totalUtilidad = subtotalCotizacion.subtract(importeCompras).subtract(importeGastos);
		
		
		
		LocalDateTime ldtNow = LocalDateTime.now();
		Templates objTemplates = new Templates();
		
		String path_file = ldtNow.getYear() + "_" + ldtNow.getMonthValue() + "_" + ldtNow.getDayOfMonth() + "_REPORTE_UTILIDAD.pdf";
				
		Context objContext = new Context();
		objContext.setVariable("_TEMPLATE_", Templates.PDF_REPORTE_UTILIDAD);
		objContext.setVariable("title", "Reporte de Utilidad");
		objContext.setVariable("objCotizacion", objCotizacion);
		objContext.setVariable("lstCompras", lstCompras);
		objContext.setVariable("lstGastos", lstGastos);
		
		objContext.setVariable("totalCompras", GeneralConfiguration.getInstance().getNumberFormat().format(importeCompras));
		objContext.setVariable("totalGastos", GeneralConfiguration.getInstance().getNumberFormat().format(importeGastos));
		objContext.setVariable("subtotalCotizacion", GeneralConfiguration.getInstance().getNumberFormat().format(subtotalCotizacion));
		objContext.setVariable("totalUtilidad", GeneralConfiguration.getInstance().getNumberFormat().format(totalUtilidad));
		
		
		
		
		return pdfComponent.generate(path_file, objTemplates.FOUNDATION_PDF, objContext);		
	}
	
	@RequestMapping(value = {"{paramIdCotizacion}/reporte-facturas", "{paramIdCotizacion}/reporte-facturas/"}, method = RequestMethod.GET, produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<InputStreamResource> previewPdf(@PathVariable("paramIdCotizacion") int paramIdCotizacion) throws IOException, DocumentException {
		
		CotizacionEntity objCotizacion = cotizacionService.findByIdCotizacion(paramIdCotizacion);
		List<CotizacionFicheroEntity> lstDocumentos = cotizacionFicheroService.listDocumentosFacturas(paramIdCotizacion);
		String totalImporte = cotizacionFicheroService.totalImporteFacturas(paramIdCotizacion);
		
		LocalDateTime ldtNow = LocalDateTime.now();
		Templates objTemplates = new Templates();
		
		String path_file = ldtNow.getYear() + "_" + ldtNow.getMonthValue() + "_" + ldtNow.getDayOfMonth() + "_REPORTE_FACTURAS.pdf";
				
		Context objContext = new Context();
		objContext.setVariable("_TEMPLATE_", Templates.PDF_REPORTE_FACTURA);
		objContext.setVariable("title", "Reporte de Facturas");
		objContext.setVariable("objCotizacion", objCotizacion);
		objContext.setVariable("lstDocumentos", lstDocumentos);
		objContext.setVariable("totalImporte", totalImporte);
		
		return pdfComponent.generate(path_file, objTemplates.FOUNDATION_PDF, objContext);		
	}
	
	@RequestMapping(value = {"{paramIdCotizacion}/recalcular", "{paramIdCotizacion}/recalcular/"}, method = RequestMethod.GET)
	public @ResponseBody String recalculateCotizacion(	@PathVariable("paramIdCotizacion") int paramIdCotizacion,								
														RedirectAttributes objRedirectAttributes) {
		
		CotizacionEntity objCotizacion = cotizacionService.findByIdCotizacion(paramIdCotizacion);
				
		Boolean respuesta = false;
		String titulo = "";
		String mensaje = "";
		
		if(objCotizacion != null) {
			if(objCotizacion.getCotizacionEstatus().getIdCotizacionEstatus() >= 3 &&  objCotizacion.getCotizacionEstatus().getIdCotizacionEstatus() != 5) {				
				try {
					cotizacionService.recalcularCotizacion(objCotizacion);
																				
					respuesta = true;
					titulo = "Completado!";
					mensaje = "Cuotas y comisiones de la cotización, recalculadas exitosamente.";
					
				} catch(ApplicationException exception) {
					titulo = "Error!";
					mensaje = "Ocurrió un error al cargar el fichero.";
				}
			} else {
				titulo = "Error!";
				mensaje = "Para realizar el recalculo es necesario que la cotización se encuentre facturada o pagada.";
			}
		} else {
			titulo = "Error!";
			mensaje = "La cotización a recalcular no se encuentra registrada en el sistema";
		}
		
		
		JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
		jsonReturn	.add("respuesta", respuesta)
					.add("titulo", titulo)
					.add("mensaje", mensaje);
		
		return jsonReturn.build().toString();
	}

	@RequestMapping(value = {"fichero-eliminar", "fichero-eliminar/"}, method = RequestMethod.POST)
	public @ResponseBody String delete(@RequestParam(value="idCotizacion") int idCotizacion,
										@RequestParam(value="idCotizacionFichero") int idCotizacionFichero,
										
										RedirectAttributes objRedirectAttributes) {
		
		Boolean respuesta = false;
		String titulo = "";
		String mensaje = "";
		
		CotizacionFicheroEntity objFichero = cotizacionFicheroService.findIdCotizacionFichero(idCotizacionFichero);
		
		if(objFichero != null) {
			try {
				cotizacionFicheroService.delete(objFichero);
				
				respuesta = true;
				titulo = "Eliminado!";
				mensaje = "El expediente ha sido eliminado exitosamente.";
				
			} catch(ApplicationException exception) {
				respuesta = false;
				titulo = "Error!";
				mensaje = exception.toString();
			}
		} else {
			respuesta = false;
			titulo = "Error!";
			mensaje = "El expediente que deseas eliminar no existe.";
		}
		
		JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
		jsonReturn	.add("respuesta", respuesta)
					.add("titulo", titulo)
					.add("mensaje", mensaje);
		
		return jsonReturn.build().toString();
	}
}
