package com.ibmexico.components;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.ibmexico.configurations.GeneralConfiguration;
import com.ibmexico.entities.BienDetalleMantenimientoEntity;
import com.ibmexico.entities.CotizacionEntity;
import com.ibmexico.entities.EmpresaEntity;
import com.ibmexico.entities.EquipoProduccionEntity;
import com.ibmexico.entities.OportunidadNegocioEntity;
import com.ibmexico.entities.UsuarioEntity;
import com.ibmexico.libraries.Templates;
import com.ibmexico.services.BienDetalleMantenimientoService;
import com.ibmexico.services.CotizacionService;
import com.ibmexico.services.EmpresaService;
import com.ibmexico.services.EquipoProduccionService;
import com.ibmexico.services.OportunidadNegocioService;
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
	@Qualifier("oportunidadNegocioService")
	private OportunidadNegocioService oportunidadNegocioService;
	
	@Autowired
	@Qualifier("equipoProduccionService")
	private EquipoProduccionService equipoProduccionService;
	
	@Autowired
	@Qualifier("empresaService")
	private EmpresaService empresaService;
	
	@Autowired
	@Qualifier("usuarioService")
	private UsuarioService usuarioService;

	/**Mantenimientos ibmexico */
	@Autowired
	@Qualifier("bienDetalleMantService")
	private BienDetalleMantenimientoService mantenimientoService;
	
	@Autowired
	@Qualifier("sessionService")
	private SessionService sessionService;
	
	@Scheduled(cron = "0 0 8 ? * 3")
	public void cronJobNotificadorCotizacionesPorCobrar() {
		LocalDate ldNow = LocalDate.now();
		
		List<EmpresaEntity> lstEmpresas = empresaService.listEmpresas();
		
		for(EmpresaEntity itemEmpresa : lstEmpresas) {
			
			List<CotizacionEntity> lstCotizaciones = cotizacionService.lstCotizacionesNoCobradas(itemEmpresa);
			//List<UsuarioEntity> lstUsuarios = usuarioService.listUsuariosActivos();
			List<Map<String , String>> mapCotizacionesFiltradas  = new ArrayList<Map<String,String>>();
			Integer iterator = 0;
			BigDecimal totalCotizaciones = new BigDecimal(0);
						
			for(CotizacionEntity itemCotizacion : lstCotizaciones) {				
				Map<String,String> myMap1 = new HashMap<String, String>();
				if(itemCotizacion.getAprobacionFecha() != null) {
					
					int diff = (int) ChronoUnit.DAYS.between(itemCotizacion.getAprobacionFecha(), ldNow);
					
					if(diff > Integer.parseInt(itemCotizacion.getDiasCredito().trim())) {
						myMap1.put("folio", itemCotizacion.getFolio());
						myMap1.put("fecha_aprobacion", itemCotizacion.getAprobacionFechaNatural());
						myMap1.put("dias_credito", itemCotizacion.getDiasCredito());
						myMap1.put("fecha_factura", itemCotizacion.getFacturacionFechaNatural());
						myMap1.put("factura", itemCotizacion.getFacturaNumero());
						myMap1.put("cliente", itemCotizacion.getCliente().getCliente());
						myMap1.put("total", itemCotizacion.getTotalNatural());
						
						mapCotizacionesFiltradas.add(iterator, myMap1);
						
						iterator++; 
						totalCotizaciones = totalCotizaciones.add(itemCotizacion.getTotal());
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
				mapVariables.put("empresa", itemEmpresa.getEmpresa());
				mapVariables.put("idEmpresa", itemEmpresa.getIdEmpresa());
				mapVariables.put("total_cotizaciones_natural", GeneralConfiguration.getInstance().getNumberFormat().format(totalCotizaciones));
				
				cronJobCobranzaEnviarMails(mapVariables);
			}
		}		
	}
	
	private void cronJobCobranzaEnviarMails(Map<String, Object> mapVariables) {
		try {
			mapVariables.put("titulo", "Cotizaciones pendientes por cobrar");
			mapVariables.put("alias", "Cobranza");
			for (Map.Entry<String, Object> entry : mapVariables.entrySet()) {
				String k = entry.getKey();
				Object v= entry.getValue();
				/**Para futuras empresas añadir las siguientes condicionales y cuenta de correo*/
				if(k.equals("idEmpresa")){
					switch (v.toString()) {
						case "1":
						case "2":
						case "4":
						mailerComponent.send("cobranza@ib-mexico.com", "Reporte: Facturas pendientes de pago.", Templates.EMAIL_COTIZACIONES_POR_COBRAR, mapVariables);
							break;
						case "3":
						mailerComponent.send("cobranza@r2a.com.mx", "Reporte: Facturas pendientes de pago.", Templates.EMAIL_COTIZACIONES_POR_COBRAR, mapVariables);
							break;
						default:
							break;
					}
				}
			}
			// mailerComponent.send("cobranza@ib-mexico.com", "Reporte: Facturas pendientes de pago.", Templates.EMAIL_COTIZACIONES_POR_COBRAR, mapVariables);
		} catch(Exception exception) { }
	}
	
	@Scheduled(cron = "0 0 8 ? * *")
	public void cronJobNotificadorOportunidadesRenovacion() {
		
		List<UsuarioEntity> lstUsuarios = usuarioService.listUsuariosActivos();
		LocalDate ldNow = LocalDate.now().plusDays(30);
		
		for(UsuarioEntity itemUsuario : lstUsuarios) {
			List<OportunidadNegocioEntity> lstOportunidades = oportunidadNegocioService.listOportunidadesNegociosRenovaciones(itemUsuario, ldNow);
			List<Map<String , String>> mapOportunidades  = new ArrayList<Map<String,String>>();
			Integer iterator = 0;
			
			if(!lstOportunidades.isEmpty()) {				
				for(OportunidadNegocioEntity itemOportunidad : lstOportunidades) {
					
					Map<String,String> myMap1 = new HashMap<String, String>();
					myMap1.put("id_oportunidad",  Integer.toString(itemOportunidad.getIdOportunidadNegocio()));
					myMap1.put("oportunidad", itemOportunidad.getOportunidad());
					myMap1.put("cliente", itemOportunidad.getCliente().getCliente());
					myMap1.put("moneda", itemOportunidad.getMoneda().getMonedaCodigo());
					
					if(itemOportunidad.getMoneda().getIdMoneda() != 1) {
						myMap1.put("ingreso", itemOportunidad.getValorMonedaExtranjeraNatural());
					} else {
						myMap1.put("ingreso", itemOportunidad.getIngresoEstimadoNatural());
					}
					
					mapOportunidades.add(iterator, myMap1);
					
					iterator++;
				}
				
				Map<String, Object> mapVariables = new HashMap<String, Object>();
				mapVariables.put("lstOportunidades", mapOportunidades);
				
				cronJobOportunidadesRenovarEnviarMails(itemUsuario, mapVariables);
				
			}
		}
		
	}
	
	private void cronJobOportunidadesRenovarEnviarMails(UsuarioEntity objUsuario, Map<String, Object> mapVariables) {
		try {
			mapVariables.put("titulo", "Oportunidades de negocio que debes renovar");
			mapVariables.put("alias", objUsuario.getAlias());
			
			mailerComponent.send(objUsuario.getCorreo(), "Hay oportunidades pendientes de renovación", Templates.EMAIL_OPORTUNIDADES_NEGOCIOS_RENOVACION, mapVariables);
			
			//COPIA DE CORREO PARA ERICK
			if(objUsuario.getIdUsuario() != 2) {
				mailerComponent.send("erick.montoya@ib-mexico.com", "Hay oportunidades pendientes de renovación - COPIA", Templates.EMAIL_OPORTUNIDADES_NEGOCIOS_RENOVACION, mapVariables);
			}
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
	
	@Scheduled(cron = "0 0 7 ? * *")
	public void cronJobNotificadorVencimientoEquipoProduccion() {
		
		LocalDate ldNow = LocalDate.now().plusDays(30);
		System.out.println(ldNow.toString());
		List<EquipoProduccionEntity> lstEquipos = equipoProduccionService.listEquiposProduccionVencimiento(ldNow);
		
		List<Map<String , String>> mapEquipos  = new ArrayList<Map<String,String>>();
		Integer iterator = 0;
		
		if(!lstEquipos.isEmpty()) {			
			for(EquipoProduccionEntity itemEquipo : lstEquipos) {
				
				Map<String,String> myMap1 = new HashMap<String, String>();
				myMap1.put("id_equipo",  Integer.toString(itemEquipo.getIdEquipoProduccion()));
				myMap1.put("cliente", itemEquipo.getCliente().getCliente());
				myMap1.put("modelo", itemEquipo.getModelo());
				myMap1.put("numero_serie", itemEquipo.getNumeroSerie());
				myMap1.put("fecha_renovacion", itemEquipo.getRenovacionFechaNatural());
				
				mapEquipos.add(iterator, myMap1);				
				iterator++;
			}
			
			Map<String, Object> mapVariables = new HashMap<String, Object>();
			mapVariables.put("lstEquipos", mapEquipos);
			
			cronJobNotificadorVencimientoEquipoProduccionEnviarMails(mapVariables);
		}
	}
	
	private void cronJobNotificadorVencimientoEquipoProduccionEnviarMails(Map<String, Object> mapVariables) {
		try {
			mapVariables.put("titulo", "Equipos en Producción proximos a vencer");
			mapVariables.put("alias", "Soporte");
			
			mailerComponent.send("jorge.cortes@ib-mexico.com", "Hay equipos en producción próximos a renovación.", Templates.EMAIL_EQUIPOS_PRODUCCION_RENOVACION, mapVariables);
			
		} catch(Exception exception) { }
	}

	@Scheduled(cron = "0 0 9 ? * *")
	public void cronJobNotificadorMantenimientoVencido(){
		LocalDate ldNow= LocalDate.now();
		List<BienDetalleMantenimientoEntity> lstMantenimientoVencido=mantenimientoService.listMantenimientoVencido();
		List<Map<String, String>> mymapManto=new ArrayList<Map<String, String>>();
		Integer iterator=0;
		

		for (BienDetalleMantenimientoEntity itemMantenimiento : lstMantenimientoVencido) {
			Map<String, String> mymap1=new HashMap<String, String>();
			if(itemMantenimiento.getFechaMantenimientoProgramada()!=null){
				int diff=(int) ChronoUnit.DAYS.between(ldNow,itemMantenimiento.getFechaMantenimientoProgramada());
				System.err.println(diff);
				if (diff<0) {
					System.err.println("hola if");
					// mymap1.put("numeroEconomico", itemMantenimiento.getBienActivo().getNumeroEconomico());
					mymap1.put("comentario_validador", itemMantenimiento.getComentario());
					mymap1.put("fechaProgramada", itemMantenimiento.getFechaMantenimientoProgramadaFechaNatural());
					mymap1.put("gastoAproximado", GeneralConfiguration.getInstance().getNumberFormat().format(itemMantenimiento.getGastoAproximado()));
					mymap1.put("observaciones", itemMantenimiento.getObservaciones());
					// mymap1.put("Estatus", itemMantenimiento.getActivoEstatus().getNombreEstatus());
					// mymap1.put("TipoActivo", itemMantenimiento.getBienActivo().getIdActivo().getNombre());
					System.err.println("que onda");
					mymapManto.add(iterator, mymap1);
					System.err.println("que onda2");
					iterator++;
				}
			}
		}
		for (Map<String,String> map : mymapManto) {
			System.err.println(map);
		}
		if(!mymapManto.isEmpty()){
			Map<String, Object> mapVariables=new HashMap<String, Object>();
			mapVariables.put("lstMantenimiento", mymapManto);
			cronJobMantenimientoVencidoEnviarMails(mapVariables);
		}
	}

	private void cronJobMantenimientoVencidoEnviarMails(Map<String, Object> mapVariables) {
		try {
			mapVariables.put("titulo", "Mantenimiento vencidos no finalizados");
			mapVariables.put("alias", "Logistica y Mantenimiento");
			
			mailerComponent.send("neyser36@gmail.com", "Hay Mantenimientos en espera.", Templates.EMAIL_MANTENIMIENTO_VENCIDO, mapVariables);
			
		} catch(Exception exception) { }
	}
}
