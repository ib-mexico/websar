package com.ibmexico.controllers;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.context.Context;

import com.ibmexico.components.ModelAndViewComponent;
import com.ibmexico.components.PdfComponent;
import com.ibmexico.entities.CotizacionEntity;
import com.ibmexico.libraries.Templates;
import com.ibmexico.services.CotizacionService;
import com.lowagie.text.DocumentException;

@Controller
@RequestMapping("controlPanel/cobranza")
public class CobranzaController {
	
	@Autowired
	@Qualifier("modelAndViewComponent")
	private ModelAndViewComponent modelAndViewComponent;
	
	@Autowired
	@Qualifier("pdfComponent")
	private PdfComponent pdfComponent;
	
	@Autowired
	@Qualifier("cotizacionService")
	private CotizacionService cotizacionService;
	
	@GetMapping({"reportes", "reportes/"})
	public ModelAndView index() {
		ModelAndView objModelAndView = modelAndViewComponent.createModelAndViewControlPanel(Templates.CONTROL_PANEL_COBRANZA_REPORTES);
		return objModelAndView;
	}
	
	@RequestMapping(value = "reporteAprobadosFacturados", method = RequestMethod.POST, produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<InputStreamResource> showReport( @RequestParam(value="cmbCondicional1") int cmbCondicional1,
															@RequestParam(value="txtDias1") int txtDias1) throws IOException, DocumentException {
		
		LocalDate ldtFechaCondicional = LocalDate.now().minusDays(txtDias1);
		
		//LISTA DE APROBADOS
		List<CotizacionEntity> lstAprobados = cotizacionService.listCotizacionesAprobados(ldtFechaCondicional, cmbCondicional1);
		
		//LISTA DE FACTURADOS
		List<CotizacionEntity> lstFacturados = cotizacionService.listCotizacionesFacturados(ldtFechaCondicional, cmbCondicional1);
		
		System.out.println("--------------- Fecha ------------------");
		System.out.println("Fecha Condicional: " + ldtFechaCondicional.toString());
		
		
		Templates objTemplates = new Templates();
		
		String path_file = "REPORTE_APROBADAS_FACTURADAS.pdf";
		
		Context objContext = new Context();
		objContext.setVariable("_TEMPLATE_", Templates.PDF_REPORTE_APROBADAS_FACTURADAS);
		objContext.setVariable("title", "REPORTE DE COTIZACIONES APROBADAS Y FACTURADAS");
		objContext.setVariable("dias", txtDias1);
		objContext.setVariable("lstAprobados", lstAprobados);
		objContext.setVariable("lstFacturados", lstFacturados);
		
		return pdfComponent.generate(path_file, objTemplates.FOUNDATION_PDF, objContext);	
		
	}

}
