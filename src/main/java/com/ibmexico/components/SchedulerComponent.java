package com.ibmexico.components;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.ibmexico.configurations.GeneralConfiguration;
import com.ibmexico.entities.CotizacionEntity;
import com.ibmexico.entities.UsuarioEntity;
import com.ibmexico.libraries.Templates;
import com.ibmexico.services.CotizacionService;
import com.ibmexico.services.SessionService;
import com.ibmexico.services.UsuarioService;

@Component
public class SchedulerComponent {

	@Autowired
	private MailerComponent mailerComponent;
	
	@Autowired
	@Qualifier("cotizacionService")
	private CotizacionService cotizacionService;
	
	@Autowired
	@Qualifier("usuarioService")
	private UsuarioService usuarioService;
	
	@Autowired
	@Qualifier("sessionService")
	private SessionService sessionService;
	
	//@Scheduled(cron = "10 * * * * *")
	@Scheduled(cron = "0 0 8 ? * 3")
	public void cronJobNotificadorCotizacionesPorCobrar() {
		
		List<CotizacionEntity> lstCotizaciones = cotizacionService.lstCotizacionesNoCobradas();
		//List<UsuarioEntity> lstUsuarios = usuarioService.listUsuariosActivos();
		List<Map<String , String>> mapCotizacionesFiltradas  = new ArrayList<Map<String,String>>();
		Integer iterator = 0;
		
		LocalDate ldNow = LocalDate.now();
		
		System.out.println("Cotizaciones: " + lstCotizaciones.size());
		
		for(CotizacionEntity itemCotizacion : lstCotizaciones) {
			
			Map<String,String> myMap1 = new HashMap<String, String>();
			if(itemCotizacion.getAprobacionFecha() != null) {
				
				int diff = (int) ChronoUnit.DAYS.between(itemCotizacion.getAprobacionFecha(), ldNow);
				
				if(diff > Integer.parseInt(itemCotizacion.getDiasCredito().trim())) {
					myMap1.put("folio", itemCotizacion.getFolio());
					myMap1.put("fecha_factura", itemCotizacion.getFacturacionFechaNatural());
					myMap1.put("estatus", itemCotizacion.getCotizacionEstatus().getCotizacionEstatus());
					myMap1.put("cliente", itemCotizacion.getCliente().getCliente());
					myMap1.put("total", itemCotizacion.getTotalNatural());
					
					mapCotizacionesFiltradas.add(iterator, myMap1);
					
					iterator++;
				}
			}
		}
		
		if(!mapCotizacionesFiltradas.isEmpty() ) {
			
			/*for(UsuarioEntity itemUsuario : lstUsuarios) {
				if(sessionService.hasRol("COTIZACIONES_COBRANZA", itemUsuario)) {
					Map<String, Object> mapVariables = new HashMap<String, Object>();
					mapVariables.put("objUsuario", itemUsuario);
					mapVariables.put("lstCotizaciones", mapCotizacionesFiltradas);
					
					System.out.println("***** ENVIO DE CORREO *****");
					cronJobCobranzaEnviarMails(itemUsuario, mapVariables);
				}
			}*/
			Map<String, Object> mapVariables = new HashMap<String, Object>();
			mapVariables.put("lstCotizaciones", mapCotizacionesFiltradas);
			
			cronJobCobranzaEnviarMails(mapVariables);
		}
		
	}
	
	private void cronJobCobranzaEnviarMails(Map<String, Object> mapVariables) {
		try {
			mapVariables.put("titulo", "Cotizaciones pendientes por cobrar");
			mapVariables.put("alias", "Cobranza");
			mailerComponent.send("cobranza@ib-mexico.com", "Hay cotizaciones con falta de pago", Templates.EMAIL_COTIZACIONES_POR_COBRAR, mapVariables);
		} catch(Exception exception) { }
	}
	
	/*private void cronJobCobranzaEnviarMails(UsuarioEntity objUsuario, Map<String, Object> mapVariables) {
		try {
			mapVariables.put("titulo", "Cotizaciones pendientes por cobrar");
			
			if(EmailValidator.getInstance().isValid(objUsuario.getCorreo())) {
				mailerComponent.send(objUsuario.getCorreo(), "Hay cotizaciones con falta de pago", Templates.EMAIL_COTIZACIONES_POR_COBRAR, mapVariables);
			}
		} catch(Exception exception) { }
	}*/
}
