package com.ibmexico.controllers;


import java.io.IOException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
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
import com.ibmexico.entities.ClienteContactoEntity;
import com.ibmexico.entities.ClienteEntity;
import com.ibmexico.entities.ClienteGiroEntity;
import com.ibmexico.entities.CotizacionComisionEntity;
import com.ibmexico.entities.CotizacionEntity;
import com.ibmexico.entities.CotizacionEstatusEntity;
import com.ibmexico.entities.CotizacionPartidaEntity;
import com.ibmexico.entities.CotizacionUsuarioQuotaEntity;
import com.ibmexico.entities.EmpresaEntity;
import com.ibmexico.entities.FormaPagoEntity;
import com.ibmexico.entities.MonedaEntity;
import com.ibmexico.entities.OportunidadNegocioEntity;
import com.ibmexico.entities.SucursalEntity;
import com.ibmexico.entities.UsuarioEntity;
import com.ibmexico.libraries.DataTable;
import com.ibmexico.libraries.Templates;
import com.ibmexico.services.ClienteContactoService;
import com.ibmexico.services.ClienteGiroService;
import com.ibmexico.services.ClienteService;
import com.ibmexico.services.CotizacionComisionService;
import com.ibmexico.services.CotizacionEstatusService;
import com.ibmexico.services.CotizacionPartidaService;
import com.ibmexico.services.CotizacionService;
import com.ibmexico.services.CotizacionUsuarioQuotaService;
import com.ibmexico.services.EmpresaService;
import com.ibmexico.services.FormaPagoService;
import com.ibmexico.services.MonedaService;
import com.ibmexico.services.OportunidadNegocioService;
import com.ibmexico.services.RolesService;
import com.ibmexico.services.SessionService;
import com.ibmexico.services.SucursalService;
import com.ibmexico.services.UsuarioRolService;
import com.ibmexico.services.UsuarioService;
import com.lowagie.text.DocumentException;
import com.pusher.rest.Pusher;
//import com.twilio.Twilio;
//import com.twilio.rest.api.v2010.account.Message;
//import com.twilio.type.PhoneNumber;

@Controller
@RequestMapping("controlPanel/cotizaciones")
public class CotizacionesController {

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
	@Qualifier("cotizacionComisionService")
	private CotizacionComisionService cotizacionComisionService;
	
	@Autowired
	@Qualifier("cotizacionUsuarioQuotaService")
	private CotizacionUsuarioQuotaService cotizacionUsuarioQuotaService;
	
	@Autowired
	@Qualifier("cotizacionPartidaService")
	private CotizacionPartidaService cotizacionPartidaService;
	
	@Autowired
	@Qualifier("cotizacionEstatusService")
	private CotizacionEstatusService cotizacionEstatusService;
	
	@Autowired
	@Qualifier("empresaService")
	private EmpresaService empresaService;
	
	@Autowired
	@Qualifier("usuarioService")
	private UsuarioService usuarioService;
	
	@Autowired
	@Qualifier("sucursalService")
	private SucursalService sucursalService;
	
	@Autowired
	@Qualifier("clienteService")
	private ClienteService clienteService;
	
	@Autowired
	@Qualifier("clienteGiroService")
	private ClienteGiroService clienteGiroService;
	
	@Autowired
	@Qualifier("clienteContactoService")
	private ClienteContactoService clienteContactoService;
	
	@Autowired
	@Qualifier("oportunidadNegocioService")
	private OportunidadNegocioService oportunidadNegocioService;
	
	@Autowired
	@Qualifier("monedaService")
	private MonedaService monedaService;
	
	@Autowired
	@Qualifier("formaPagoService")
	private FormaPagoService formaPagoService;
	
	@Autowired
	@Qualifier("rolesService")
	private RolesService rolesService;
	
	@Autowired
	@Qualifier("usuarioRolService")
	private UsuarioRolService usuarioRolService;
	
	@Autowired
	@Qualifier("sessionService")
	private SessionService sessionService;
	
	//COTIZACION	
	@GetMapping({"", "/"})
	public ModelAndView index() {
		
		List<CotizacionEstatusEntity> lstCotizacionEstatus = cotizacionEstatusService.listAll();
		
		ModelAndView objModelAndView = modelAndViewComponent.createModelAndViewControlPanel(Templates.CONTROL_PANEL_COTIZACIONES_INDEX);
		objModelAndView.addObject("lstCotizacionEstatus", lstCotizacionEstatus);
		objModelAndView.addObject("rolCotizacionExpediente", sessionService.hasRol("COTIZACIONES_EXPEDIENTES"));
		objModelAndView.addObject("rolCotizacionCobranza", sessionService.hasRol("COTIZACIONES_COBRANZA"));
		objModelAndView.addObject("rolNuevaCotizacion", sessionService.hasRol("COTIZACIONES_CREATE"));
		objModelAndView.addObject("rolCotizacionAdmin", sessionService.hasRol("COTIZACIONES_ADMINISTRADOR"));
		
		return objModelAndView;
	}
	
	@GetMapping({"/create/{paramIdOportunidad}", "/create"})
	public ModelAndView create(	@PathVariable(value="paramIdOportunidad", required=false) Integer paramIdOportunidad ) {
		
		OportunidadNegocioEntity objOportunidad = null;
		
		if(paramIdOportunidad != null) {
			objOportunidad = oportunidadNegocioService.findByIdOportunidadNegocio(paramIdOportunidad);
		}
		
		List<EmpresaEntity> lstEmpresas 			= empresaService.listEmpresas();
		List<MonedaEntity> lstMonedas 				= monedaService.listMonedas();
		List<FormaPagoEntity> lstFormasPagos 		= formaPagoService.listFormasPagos();
		List<UsuarioEntity> lstUsuarios 			= usuarioService.listUsuariosActivos();
		List<UsuarioEntity> lstUsuariosGrupos 		= usuarioService.listUsuariosGruposActivos();
		List<ClienteEntity> lstClientes 			= clienteService.listClientesActivos();
		List<SucursalEntity> lstSucursales 			= sucursalService.listSucursales();
		List<ClienteGiroEntity> lstClientesGiros 	= clienteGiroService.listClientesGiros();
		
		
		ModelAndView objModelAndView = modelAndViewComponent.createModelAndViewControlPanel(Templates.CONTROL_PANEL_COTIZACIONES_CREATE);
		objModelAndView.addObject("lstEmpresas", lstEmpresas);
		objModelAndView.addObject("lstMonedas", lstMonedas);
		objModelAndView.addObject("lstFormasPagos", lstFormasPagos);
		objModelAndView.addObject("lstUsuarios", lstUsuarios);
		objModelAndView.addObject("lstUsuariosGrupos", lstUsuariosGrupos);
		objModelAndView.addObject("lstClientes", lstClientes);
		objModelAndView.addObject("lstClientesGiros", lstClientesGiros);
		objModelAndView.addObject("lstSucursales", lstSucursales);
		objModelAndView.addObject("objOportunidad", objOportunidad);
		
		
		return objModelAndView;
	}
	
	@RequestMapping(value = "", method = RequestMethod.POST)
	public RedirectView store( @RequestParam(value="cmbUsuario", required=false) Integer cmbUsuario,								
			 					@RequestParam(value="cmbEmpresa") Integer cmbEmpresa,
								@RequestParam(value="txtConcepto") String txtConcepto,
								@RequestParam(value="txtSolicitudFecha", required=false) String txtSolicitudFecha,
								@RequestParam(value="cmbCliente") int cmbCliente,
								@RequestParam(value="cmbClienteContacto") int cmbClienteContacto,
								@RequestParam(value="txtUbicacion") String txtUbicacion,
								@RequestParam(value="txtLugarEntrega") String txtLugarEntrega,
								@RequestParam(value="txtTiempoEntrega") String txtTiempoEntrega,
								@RequestParam(value="txtVigenciaPrecios") String txtVigenciaPrecios,
								@RequestParam(value="cmbMoneda") int cmbMoneda,
								@RequestParam(value="cmbFormaPago") int cmbFormaPago,
								@RequestParam(value="txtDiasCredito") String txtDiasCredito,
								@RequestParam(value="chkVentaCompartida", required=false, defaultValue="false") String chkVentaCompartida,
								@RequestParam(value="cmbVendedor", required=false) Integer cmbVendedor,
								@RequestParam(value="chkImplementacion", required=false, defaultValue="false") String chkImplementacion,
								@RequestParam(value="rdTipoCotizacion", required=false, defaultValue="false") String rdTipoCotizacion,
								@RequestParam(value="cmbImplementador", required=false) Integer cmbImplementador,
								@RequestParam(value="txtCondicionesPago", required=false) String txtCondicionesPago,
								@RequestParam(value="txtObservaciones", required=false) String txtObservaciones,
								@RequestParam(value="hddIdOportunidad", required=false, defaultValue="0") int hddIdOportunidad,
								RedirectAttributes objRedirectAttributes) {
					
		RedirectView objRedirectView = null;
		CotizacionEntity objCotizacion = new CotizacionEntity();
		
		try {
					
			if(sessionService.hasRol("COTIZACIONES_ADMINISTRADOR")) {
				UsuarioEntity objUsuario = usuarioService.findByIdUsuario(cmbUsuario);
				objCotizacion.setUsuario(objUsuario);
				objCotizacion.setSucursal(sucursalService.findByIdSucursal(objUsuario.getSucursal().getIdSucursal()));
			} else {
				objCotizacion.setUsuario(sessionService.getCurrentUser());
				objCotizacion.setSucursal(sucursalService.findByIdSucursal(sessionService.getCurrentUser().getSucursal().getIdSucursal()));
			}
			
			if(hddIdOportunidad > 0) {
				objCotizacion.setOportunidadNegocio(oportunidadNegocioService.findByIdOportunidadNegocio(hddIdOportunidad));
			}
					
			objCotizacion.setEmpresa(empresaService.findByIdEmpresa(cmbEmpresa));
			objCotizacion.setConcepto(txtConcepto);
			objCotizacion.setSolicitudFecha(LocalDate.parse(txtSolicitudFecha, GeneralConfiguration.getInstance().getDateFormatterNatural()));
			objCotizacion.setCliente(clienteService.findByIdCliente(cmbCliente));
			objCotizacion.setClienteContacto(clienteContactoService.findByIdClienteContacto(cmbClienteContacto));
			objCotizacion.setUbicacion(txtUbicacion);
			objCotizacion.setEntregaLugar(txtLugarEntrega);
			objCotizacion.setEntregaDiasHabiles(txtTiempoEntrega);
			objCotizacion.setVigenciaPrecioDiasHabiles(txtVigenciaPrecios);
			objCotizacion.setMoneda(monedaService.find(cmbMoneda));
			objCotizacion.setFormaPago(formaPagoService.find(cmbFormaPago));
			objCotizacion.setDiasCredito(txtDiasCredito);
			objCotizacion.setCondicionesPago(txtCondicionesPago.trim());
			objCotizacion.setObservaciones(txtObservaciones.trim());
			objCotizacion.setCotizacionEstatus(cotizacionEstatusService.findByIdCotizacionEstatus(1));
			
			//VENTA COMPARTIDA
			if(chkVentaCompartida.equals("true")) {
				objCotizacion.setVentaCompartida(true);
				objCotizacion.setUsuarioVendedor(usuarioService.findByIdUsuario(cmbVendedor));
			} else {
				objCotizacion.setVentaCompartida(false);
				objCotizacion.setUsuarioVendedor(sessionService.getCurrentUser());
			}
			
			
			//IMPLEMENTACION
			if(chkImplementacion.equals("true")) {
				objCotizacion.setImplementacion(true);
				objCotizacion.setUsuarioImplementador(usuarioService.findByIdUsuario(cmbImplementador));
			} else {
				objCotizacion.setImplementacion(false);
			}
			
			
			
			//TIPO DE COTIZACION
			if(rdTipoCotizacion.equals("master")) {
				objCotizacion.setMaestra(true);
			} else if(rdTipoCotizacion.equals("renta")) {
				objCotizacion.setRenta(true);
			} else {
				objCotizacion.setNormal(true);
			}
			
			objCotizacion.setSubtotal(new BigDecimal(0));
			objCotizacion.setIvaPorcentaje(new BigDecimal(0));
			objCotizacion.setIva(new BigDecimal(0));
			objCotizacion.setTotal(new BigDecimal(0));
			
			cotizacionService.create(objCotizacion);						
			
			/**
			 * NOTIFICACIONES DE PUSHER
			 */						
			Pusher pusher = new Pusher("575478", "7b4b9197d41e13beb30d", "8c116fb03f6b79d32085");
			pusher.setCluster("us2");
			
			//NOTIFICACION USUARIO VENDEDOR
			JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
			jsonReturn	.add("id_usuario", objCotizacion.getUsuarioVendedor().getIdUsuario())
						.add("message", "Te agregaron como vendedor(a) en la cotización: " + objCotizacion.getIdCotizacion());						

			pusher.trigger("notifications", "new-notification", jsonReturn.build());
			

			if(chkImplementacion.equals("true")) {
				
				//NOTIFICACION USUARIO IMPLEMENTADOR
				JsonObjectBuilder jsonReturn2 = Json.createObjectBuilder();
				jsonReturn2	.add("id_usuario", objCotizacion.getUsuarioImplementador().getIdUsuario())
				.add("message", "Te agregaron como implementador(a) en la cotización: " + objCotizacion.getIdCotizacion());						
			
				pusher.trigger("notifications", "new-notification", jsonReturn2.build());
			}
			/**
			 * 
			 */
			
			objRedirectView = new RedirectView("/WebSar/controlPanel/cotizaciones");
			modelAndViewComponent.addResult(objRedirectAttributes, EnumMessage.COTIZACIONES_CREATE_001);
			
		} catch(ApplicationException exception) {
			objRedirectView = new RedirectView("/WebSar/controlPanel/cotizaciones/create");
			modelAndViewComponent.addResult(objRedirectAttributes, exception);
		}
		
		return objRedirectView;
	}

	@RequestMapping(value = "storeAJAX", method = RequestMethod.POST)
	public @ResponseBody String store( @RequestParam(value="cmbUsuario", required=false) Integer cmbUsuario,								
										@RequestParam(value="cmbEmpresa") Integer cmbEmpresa,
										@RequestParam(value="txtConcepto") String txtConcepto,
										@RequestParam(value="txtSolicitudFecha", required=false) String txtSolicitudFecha,
										@RequestParam(value="cmbCliente") int cmbCliente,
										@RequestParam(value="cmbClienteContacto") int cmbClienteContacto,
										@RequestParam(value="txtUbicacion") String txtUbicacion,
										@RequestParam(value="txtLugarEntrega") String txtLugarEntrega,
										@RequestParam(value="txtTiempoEntrega") String txtTiempoEntrega,
										@RequestParam(value="txtVigenciaPrecios") String txtVigenciaPrecios,
										@RequestParam(value="cmbMoneda") int cmbMoneda,
										@RequestParam(value="cmbFormaPago") int cmbFormaPago,
										@RequestParam(value="txtDiasCredito") String txtDiasCredito,
										@RequestParam(value="chkVentaCompartida", required=false, defaultValue="false") String chkVentaCompartida,
										@RequestParam(value="cmbVendedor", required=false) Integer cmbVendedor,
										@RequestParam(value="chkImplementacion", required=false, defaultValue="false") String chkImplementacion,
										@RequestParam(value="rdTipoCotizacion", required=false, defaultValue="false") String rdTipoCotizacion,
										@RequestParam(value="cmbImplementador", required=false) Integer cmbImplementador,
										@RequestParam(value="txtCondicionesPago", required=false) String txtCondicionesPago,
										@RequestParam(value="txtObservaciones", required=false) String txtObservaciones) {
		
		Boolean respuesta = false;
		String titulo = "Oops!";
		String mensaje = "Ocurrió un error al crear la cotización en el sistema.";

		CotizacionEntity objCotizacion = new CotizacionEntity();
		
		try {
					
			if(sessionService.hasRol("COTIZACIONES_ADMINISTRADOR")) {
				UsuarioEntity objUsuario = usuarioService.findByIdUsuario(cmbUsuario);
				objCotizacion.setUsuario(objUsuario);
				objCotizacion.setSucursal(sucursalService.findByIdSucursal(objUsuario.getSucursal().getIdSucursal()));
			} else {
				objCotizacion.setUsuario(sessionService.getCurrentUser());
				objCotizacion.setSucursal(sucursalService.findByIdSucursal(sessionService.getCurrentUser().getSucursal().getIdSucursal()));
			}
					
			objCotizacion.setEmpresa(empresaService.findByIdEmpresa(cmbEmpresa));
			objCotizacion.setConcepto(txtConcepto);
			objCotizacion.setSolicitudFecha(LocalDate.parse(txtSolicitudFecha, GeneralConfiguration.getInstance().getDateFormatterNatural()));
			objCotizacion.setCliente(clienteService.findByIdCliente(cmbCliente));
			objCotizacion.setClienteContacto(clienteContactoService.findByIdClienteContacto(cmbClienteContacto));
			objCotizacion.setUbicacion(txtUbicacion);
			objCotizacion.setEntregaLugar(txtLugarEntrega);
			objCotizacion.setEntregaDiasHabiles(txtTiempoEntrega);
			objCotizacion.setVigenciaPrecioDiasHabiles(txtVigenciaPrecios);
			objCotizacion.setMoneda(monedaService.find(cmbMoneda));
			objCotizacion.setFormaPago(formaPagoService.find(cmbFormaPago));
			objCotizacion.setDiasCredito(txtDiasCredito);
			objCotizacion.setCondicionesPago(txtCondicionesPago.trim());
			objCotizacion.setObservaciones(txtObservaciones.trim());
			objCotizacion.setCotizacionEstatus(cotizacionEstatusService.findByIdCotizacionEstatus(1));
			
			//VENTA COMPARTIDA
			if(chkVentaCompartida.equals("true")) {
				objCotizacion.setVentaCompartida(true);
				objCotizacion.setUsuarioVendedor(usuarioService.findByIdUsuario(cmbVendedor));
			} else {
				objCotizacion.setVentaCompartida(false);
				objCotizacion.setUsuarioVendedor(sessionService.getCurrentUser());
			}
			
			
			//IMPLEMENTACION
			if(chkImplementacion.equals("true")) {
				objCotizacion.setImplementacion(true);
				objCotizacion.setUsuarioImplementador(usuarioService.findByIdUsuario(cmbImplementador));
			} else {
				objCotizacion.setImplementacion(false);
			}
			
			
			
			//TIPO DE COTIZACION
			if(rdTipoCotizacion.equals("master")) {
				objCotizacion.setMaestra(true);
			} else if(rdTipoCotizacion.equals("renta")) {
				objCotizacion.setRenta(true);
			} else {
				objCotizacion.setNormal(true);
			}
			
			objCotizacion.setSubtotal(new BigDecimal(0));
			objCotizacion.setIvaPorcentaje(new BigDecimal(0));
			objCotizacion.setIva(new BigDecimal(0));
			objCotizacion.setTotal(new BigDecimal(0));
			
			cotizacionService.create(objCotizacion);						
			
			/**
			 * NOTIFICACIONES DE PUSHER
			 */						
			Pusher pusher = new Pusher("575478", "7b4b9197d41e13beb30d", "8c116fb03f6b79d32085");
			pusher.setCluster("us2");
			
			//NOTIFICACION USUARIO VENDEDOR
			JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
			jsonReturn	.add("id_usuario", objCotizacion.getUsuarioVendedor().getIdUsuario())
						.add("message", "Te agregaron como vendedor(a) en la cotización: " + objCotizacion.getIdCotizacion());						

			pusher.trigger("notifications", "new-notification", jsonReturn.build());
			

			if(chkImplementacion.equals("true")) {
				
				//NOTIFICACION USUARIO IMPLEMENTADOR
				JsonObjectBuilder jsonReturn2 = Json.createObjectBuilder();
				jsonReturn2	.add("id_usuario", objCotizacion.getUsuarioImplementador().getIdUsuario())
				.add("message", "Te agregaron como implementador(a) en la cotización: " + objCotizacion.getIdCotizacion());						
			
				pusher.trigger("notifications", "new-notification", jsonReturn2.build());
			}
			/**
			 * 
			 */

			respuesta = true;
			titulo = "Excelente!";
			mensaje = "La cotización se creó exitosamente.";
			
		} catch(ApplicationException exception) {
			throw new ApplicationException(EnumException.COTIZACIONES_CREATE_001);
		}
		
		JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
		jsonReturn	.add("respuesta", respuesta)
					.add("titulo", titulo)
					.add("mensaje", mensaje);

										
		return jsonReturn.build().toString();
	}
	
	@GetMapping({"{paramIdCotizacion}/edit", "{paramIdCotizacion}/edit/"})
	public ModelAndView edit(@PathVariable("paramIdCotizacion") int paramIdCotizacion) {
		
		CotizacionEntity objCotizacion = cotizacionService.findByIdCotizacion(paramIdCotizacion);
		
		List<EmpresaEntity> lstEmpresas = empresaService.listEmpresas();
		List<MonedaEntity> lstMonedas = monedaService.listMonedas();
		List<FormaPagoEntity> lstFormasPagos = formaPagoService.listFormasPagos();
		List<UsuarioEntity> lstUsuarios = usuarioService.listUsuarios();
		List<UsuarioEntity> lstUsuariosGrupos = usuarioService.listUsuariosGruposActivos();
		List<ClienteEntity> lstClientes = clienteService.listClientesActivos();
		List<ClienteContactoEntity> lstClientesContactos = clienteContactoService.listClienteContactosActivos(objCotizacion.getCliente().getIdCliente());
		List<OportunidadNegocioEntity> lstOportunidades = oportunidadNegocioService.listOportunidadesNegocios();
		
		ModelAndView objModelAndView = modelAndViewComponent.createModelAndViewControlPanel(Templates.CONTROL_PANEL_COTIZACIONES_EDIT);
		objModelAndView.addObject("objCotizacion", objCotizacion);
		objModelAndView.addObject("lstEmpresas", lstEmpresas);
		objModelAndView.addObject("lstMonedas", lstMonedas);
		objModelAndView.addObject("lstFormasPagos", lstFormasPagos);
		objModelAndView.addObject("lstUsuarios", lstUsuarios);
		objModelAndView.addObject("lstUsuariosGrupos", lstUsuariosGrupos);
		objModelAndView.addObject("lstClientes", lstClientes);
		objModelAndView.addObject("lstClientesContactos", lstClientesContactos);
		objModelAndView.addObject("lstOportunidades", lstOportunidades);
		
		return objModelAndView;
	}
	
	@RequestMapping(value = {"{paramIdCotizacion}", "{paramIdCotizacion}/"}, method = RequestMethod.PUT)
	public RedirectView store( @PathVariable("paramIdCotizacion") int paramIdCotizacion,
								@RequestParam(value="cmbEmpresa") int cmbEmpresa,
								@RequestParam(value="txtConcepto") String txtConcepto,
								@RequestParam(value="cmbCliente") int cmbCliente,
								@RequestParam(value="cmbClienteContacto") int cmbClienteContacto,
								@RequestParam(value="txtSolicitudFecha", required=false) String txtSolicitudFecha,
								@RequestParam(value="txtUbicacion") String txtUbicacion,
								@RequestParam(value="txtLugarEntrega") String txtLugarEntrega,
								@RequestParam(value="txtTiempoEntrega") String txtTiempoEntrega,
								@RequestParam(value="txtVigenciaPrecios") String txtVigenciaPrecios,
								@RequestParam(value="cmbMoneda") int cmbMoneda,
								@RequestParam(value="cmbFormaPago") int cmbFormaPago,
								@RequestParam(value="txtDiasCredito") String txtDiasCredito,
								@RequestParam(value="chkVentaCompartida", required=false, defaultValue="false") String chkVentaCompartida,
								@RequestParam(value="cmbVendedor", required=false) Integer cmbVendedor,
								@RequestParam(value="chkImplementacion", required=false, defaultValue="false") String chkImplementacion,
								@RequestParam(value="rdTipoCotizacion", required=false, defaultValue="false") String rdTipoCotizacion,
								@RequestParam(value="cmbImplementador", required=false) Integer cmbImplementador,
								@RequestParam(value="cmbOportunidadNegocio", required=false, defaultValue="0") Integer cmbOportunidadNegocio,
								@RequestParam(value="txtCondicionesPago", required=false) String txtCondicionesPago,
								@RequestParam(value="txtObservaciones", required=false) String txtObservaciones,
								RedirectAttributes objRedirectAttributes) {
		
		RedirectView objRedirectView = null;
		CotizacionEntity objCotizacion = cotizacionService.findByIdCotizacion(paramIdCotizacion);
		
		try {
			
			if(objCotizacion != null) {
				
				objCotizacion.setEmpresa(empresaService.findByIdEmpresa(cmbEmpresa));
				objCotizacion.setConcepto(txtConcepto);
				objCotizacion.setCliente(clienteService.findByIdCliente(cmbCliente));
				objCotizacion.setClienteContacto(clienteContactoService.findByIdClienteContacto(cmbClienteContacto));
				objCotizacion.setSolicitudFecha(LocalDate.parse(txtSolicitudFecha, GeneralConfiguration.getInstance().getDateFormatterNatural()));
				objCotizacion.setUbicacion(txtUbicacion);
				objCotizacion.setEntregaLugar(txtLugarEntrega);
				objCotizacion.setEntregaDiasHabiles(txtTiempoEntrega);
				objCotizacion.setVigenciaPrecioDiasHabiles(txtVigenciaPrecios);
				objCotizacion.setMoneda(monedaService.find(cmbMoneda));
				objCotizacion.setFormaPago(formaPagoService.find(cmbFormaPago));
				objCotizacion.setDiasCredito(txtDiasCredito);			
				objCotizacion.setCondicionesPago(txtCondicionesPago.trim());
				objCotizacion.setObservaciones(txtObservaciones.trim());
				
				if(cmbOportunidadNegocio > 0 && cmbOportunidadNegocio != null) {
					objCotizacion.setOportunidadNegocio(oportunidadNegocioService.findByIdOportunidadNegocio(cmbOportunidadNegocio));
				} else {
					objCotizacion.setOportunidadNegocio(null);
				}
				
				//VENTA COMPARTIDA
				if(chkVentaCompartida.equals("true")) {
					objCotizacion.setVentaCompartida(true);
					objCotizacion.setUsuarioVendedor(usuarioService.findByIdUsuario(cmbVendedor));
				} else {
					objCotizacion.setVentaCompartida(false);
					objCotizacion.setUsuarioVendedor(objCotizacion.getUsuario());
				}
				
				
				//IMPLEMENTACION
				if(chkImplementacion.equals("true")) {
					objCotizacion.setImplementacion(true);
					objCotizacion.setUsuarioImplementador(usuarioService.findByIdUsuario(cmbImplementador));
				} else {
					objCotizacion.setImplementacion(false);
				}
				
				
				
				//TIPO DE COTIZACION
				if(rdTipoCotizacion.equals("master")) {
					objCotizacion.setMaestra(true);
					objCotizacion.setRenta(false);
					objCotizacion.setNormal(false);
				} else if(rdTipoCotizacion.equals("renta")) {
					objCotizacion.setRenta(true);
					objCotizacion.setMaestra(false);
					objCotizacion.setNormal(false);
				} else {
					objCotizacion.setNormal(true);
					objCotizacion.setMaestra(false);
					objCotizacion.setRenta(false);
				}
				
				cotizacionService.update(objCotizacion);
				objRedirectView = new RedirectView("/WebSar/controlPanel/cotizaciones");
				modelAndViewComponent.addResult(objRedirectAttributes, EnumMessage.COTIZACIONES_UPDATE_001);
			}
			else {
				throw new ApplicationException(EnumException.COTIZACIONES_UPDATE_001);
			}
			
		} catch(ApplicationException exception) {
			objRedirectView = new RedirectView("/WebSar/controlPanel/cotizaciones/" + paramIdCotizacion + "/edit");
			modelAndViewComponent.addResult(objRedirectAttributes, exception);
		}
		
		return objRedirectView;
	}
	
	@RequestMapping(value = "/table", method = RequestMethod.POST)	
	public @ResponseBody String table(	@RequestParam("offset") int offset,
										@RequestParam("limit") int limit,
										@RequestParam("_csrf") String _csrf,
										@RequestParam(value="search", required=false, defaultValue="") String search,
										@RequestParam(value="txtBootstrapTableDesde", required=false) String txtBootstrapTableDesde,
										@RequestParam(value="txtBootstrapTableHasta", required=false) String txtBootstrapTableHasta) {
		
		DataTable<CotizacionEntity> dtCotizaciones = cotizacionService.dataTable(search, offset, limit, txtBootstrapTableDesde, txtBootstrapTableHasta);
		
		JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
		jsonReturn.add("total", dtCotizaciones.getTotal());
		JsonArrayBuilder jsonRows = Json.createArrayBuilder();
		
		dtCotizaciones.getRows().forEach((itemCotizacion)-> {
			
			String ejecutivo = "";
			switch (itemCotizacion.getEmpresa().getIdEmpresa()) {
				case 1:
					if(itemCotizacion.getCliente().getUsuarioEjecutivo() != null) {
						ejecutivo = itemCotizacion.getCliente().getUsuarioEjecutivo().getNombreCompleto();
					}
					break;
				
				case 2:
					if(itemCotizacion.getCliente().getUsuarioEjecutivoS3s() != null) {
						ejecutivo = itemCotizacion.getCliente().getUsuarioEjecutivoS3s().getNombreCompleto();
					}
					break;
				
				case 3:
					if(itemCotizacion.getCliente().getUsuarioEjecutivoR2a() != null) {
						ejecutivo = itemCotizacion.getCliente().getUsuarioEjecutivoR2a().getNombreCompleto();
					}
					break;

			default:
				break;
			}
			

			jsonRows.add(Json.createObjectBuilder()
				.add("idCotizacion", itemCotizacion.getIdCotizacion())
				.add("idEstatus", itemCotizacion.getCotizacionEstatus().getIdCotizacionEstatus())
				.add("estatus", itemCotizacion.getCotizacionEstatus().getCotizacionEstatus())
				.add("sucursal", itemCotizacion.getSucursal().getSucursal())
				
				.add("folio", itemCotizacion.getFolio())
				.add("concepto", itemCotizacion.getConcepto())
				
				.add("subtotal", itemCotizacion.getSubtotalNatural())
				.add("iva", itemCotizacion.getIvaNatural())
				.add("total", itemCotizacion.getTotalNatural())
				
				.add("requisicion_folio", itemCotizacion.getRequisicionMaterialFolio())
				.add("usuario", itemCotizacion.getUsuario().getCorreo())
				.add("cliente", itemCotizacion.getCliente().getCliente())
				.add("ejecutivo", ejecutivo)
				
				.add("creacionFecha", itemCotizacion.getCreacionFechaNatural())

				.add("eliminado", itemCotizacion.isEliminado())
			);
		});

		jsonReturn.add("rows", jsonRows);

		return jsonReturn.build().toString();
	}		

	@RequestMapping(value = {"{paramIdCotizacion}/pdf", "{paramIdCotizacion}/pdf/"}, method = RequestMethod.GET, produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<InputStreamResource> previewPdf(@PathVariable("paramIdCotizacion") int paramIdCotizacion) throws IOException, DocumentException {
		
		CotizacionEntity objCotizacion = cotizacionService.findByIdCotizacion(paramIdCotizacion);
		List<CotizacionPartidaEntity> lstPartida = cotizacionPartidaService.listCotizacionesPartidas(paramIdCotizacion);
		
		LocalDateTime ldtNow = LocalDateTime.now();
		Templates objTemplates = new Templates();
		
		String path_file = ldtNow.getYear() + "_" + ldtNow.getMonthValue() + "_" + ldtNow.getDayOfMonth() + "_" + objCotizacion.getFolio() + ".pdf";
				
		Context objContext = new Context();
		objContext.setVariable("_TEMPLATE_", Templates.PDF_COTIZACION);
		objContext.setVariable("title", "Cotización: " + objCotizacion.getFolio());
		objContext.setVariable("objCotizacion", objCotizacion);
		objContext.setVariable("lstPartida", lstPartida);
		objContext.setVariable("fechaActual", ldtNow.format(GeneralConfiguration.getInstance().getDateFormatterNatural()));
		
		return pdfComponent.generate(path_file, objTemplates.FOUNDATION_PDF, objContext);		
	}
	
	@RequestMapping(value = {"{paramIdCotizacion}/get-cotizacion", "{paramIdCotizacion}/get-cotizacion/"}, method = RequestMethod.GET)
	public @ResponseBody String getCotizacion( @PathVariable("paramIdCotizacion") int paramIdCotizacion) {
						
		CotizacionEntity objCotizacion = cotizacionService.findByIdCotizacion(paramIdCotizacion);		
		Boolean respuesta = false;
		int idEstatus = 0;
		String folio = "";
		String facturacionFecha = "";
		String facturaNumero = "";
		String pagoFecha = "";
		String pagoReferencia = "";
				
		if(objCotizacion != null) {							
			respuesta = true;
			idEstatus = objCotizacion.getCotizacionEstatus().getIdCotizacionEstatus();
			folio = objCotizacion.getFolio();
			facturacionFecha = objCotizacion.getFacturacionFechaNatural();
			facturaNumero = objCotizacion.getFacturaNumero();
			pagoFecha = objCotizacion.getPagoFechaNatural();
			pagoReferencia = objCotizacion.getPagoReferencia();
		}
					
		JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
		jsonReturn	.add("respuesta", respuesta)
					.add("idEstatus", idEstatus)
					.add("folio", folio)
					.add("facturacionFecha", facturacionFecha)
					.add("facturaNumero", facturaNumero)
					.add("pagoFecha", pagoFecha)
					.add("pagoReferencia", pagoReferencia);

										
		return jsonReturn.build().toString();
	}
	
	@RequestMapping(value = "modificar-estatus", method = RequestMethod.POST)
	public @ResponseBody String store( @RequestParam(value="hddIdCotizacion") int hddIdCotizacion,
								@RequestParam(value="cmbEstatus") Integer cmbEstatus,
								@RequestParam(value="txtFacturaFecha", required=false, defaultValue="") String txtFacturaFecha,
								@RequestParam(value="txtFacturaNumero", required=false, defaultValue="") String txtFacturaNumero,
								@RequestParam(value="txtPagoFecha", required=false, defaultValue="") String txtPagoFecha,
								@RequestParam(value="txtPagoReferencia", required=false, defaultValue="") String txtPagoReferencia) {
		
		CotizacionEntity objCotizacion = cotizacionService.findByIdCotizacion(hddIdCotizacion);
		Boolean respuesta = false;
		String titulo = "Cotización Modificada!";
		String mensaje = "El estatus de la cotizacion ha sido modificada exitosamente.";
		
		try {
			
			if(objCotizacion != null) {												
				
				if(cmbEstatus.equals(2) && objCotizacion.getAprobacionFecha() == null) {
					LocalDate ldNow = LocalDate.now();
					objCotizacion.setAprobacionFecha(ldNow);
					
					//ESTA NOTIFICACION IRIA EN LA PARTE DE APROBACION AL MOMENTO QUE SE APRUEBE UNA COTIZACION MAYOR A 10,000
					/*Twilio.init(GeneralConfiguration.getInstance().getTwilioAccountSID(), GeneralConfiguration.getInstance().getTwilioAuthToken());
					
					Message message = Message.creator(new PhoneNumber("whatsapp:+5219931695789"), 
							new PhoneNumber("whatsapp:+14155238886"),
							"Your appointment is coming up on July 21 at 3PM").create();
					
					System.out.println(message.getSid());*/
					objCotizacion.setCotizacionEstatus(cotizacionEstatusService.findByIdCotizacionEstatus(cmbEstatus));
					cotizacionService.update(objCotizacion);
					respuesta = true;
				}

				if(objCotizacion.getAprobacionFecha() != null && cmbEstatus > 2 ){
					if(cmbEstatus.equals(3) && objCotizacion.getCotizacionEstatus().getIdCotizacionEstatus() != 3) {
						objCotizacion.setFacturacionFecha(LocalDate.parse(txtFacturaFecha, GeneralConfiguration.getInstance().getDateFormatterNatural()));
						objCotizacion.setFacturaNumero(txtFacturaNumero);			
						
						//VALIDAMOS QUE LA COTIZACION NO SEA UNA RENTA
						if(objCotizacion.isMaestra() || objCotizacion.isNormal()) {						
							CotizacionUsuarioQuotaEntity objQuota = new CotizacionUsuarioQuotaEntity();
							objQuota.setCotizacion(objCotizacion);
							
							cotizacionUsuarioQuotaService.cargarQuota(objCotizacion);
						}
					}
					
					if(cmbEstatus.equals(4)) {
						objCotizacion.setPagoFecha(LocalDate.parse(txtPagoFecha, GeneralConfiguration.getInstance().getDateFormatterNatural()));
						objCotizacion.setPagoReferencia(txtPagoReferencia);					
						
						//VALIDAMOS QUE LA COTIZACION NO SE MAESTRA
						if(objCotizacion.isRenta() || objCotizacion.isNormal()) {	
							
							//VALIDAMOS QUE NO EXISTA UN REGISTRO DE COMISION
							CotizacionComisionEntity objComisionExistente = cotizacionComisionService.findByCotizacion(objCotizacion);
							if(objComisionExistente == null) {							
								CotizacionComisionEntity objComision = new CotizacionComisionEntity();
								objComision.setCotizacion(objCotizacion);
								
								cotizacionComisionService.create(objComision, objCotizacion);
							}
						}
					}
					
					if(cmbEstatus.equals(6) && objCotizacion.getInicioCobranzaFecha() == null) {
						LocalDate ldNow = LocalDate.now();
						objCotizacion.setInicioCobranzaFecha(ldNow);
						objCotizacion.setUsuarioCobranza(sessionService.getCurrentUser());
					}
					
					objCotizacion.setCotizacionEstatus(cotizacionEstatusService.findByIdCotizacionEstatus(cmbEstatus));
					cotizacionService.update(objCotizacion);
					respuesta = true;
				}else{
					if(cmbEstatus.equals(5)) {
						objCotizacion.setCotizacionEstatus(cotizacionEstatusService.findByIdCotizacionEstatus(cmbEstatus));
						cotizacionService.update(objCotizacion);
						respuesta = true;
					}
				}
				
				if(!respuesta) {
					titulo = "Cotización Modificada!";
					mensaje = "Es necesario tener la cotización aprobada para proceder con los demás estatus.";
				}
				
			}
			else {
				throw new ApplicationException(EnumException.COTIZACIONES_UPDATE_001);
			}
			
		} catch(ApplicationException exception) {
			throw new ApplicationException(EnumException.COTIZACIONES_UPDATE_001);
		}
		
		JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
		jsonReturn	.add("respuesta", respuesta)
					.add("titulo", titulo)
					.add("mensaje", mensaje);
		
		return jsonReturn.build().toString();
	}
	
	@RequestMapping(value = "{paramIdCotizacion}/clone", method = RequestMethod.POST)
	public @ResponseBody String clone(@PathVariable("paramIdCotizacion") int paramIdCotizacion,
										@RequestParam(value="idCotizacion") int idCotizacion) {
		
		CotizacionEntity objCotizacion = cotizacionService.findByIdCotizacion(idCotizacion);
		Boolean respuesta = false;
		String titulo = "";
		String mensaje = "";
		
		try {
			
			if(objCotizacion != null) {
				
				CotizacionEntity objCotizacionNueva = new CotizacionEntity();
				
				objCotizacionNueva.setSucursal(objCotizacion.getSucursal());
				objCotizacionNueva.setUsuario(sessionService.getCurrentUser());
				objCotizacionNueva.setEmpresa(objCotizacion.getEmpresa());
				objCotizacionNueva.setConcepto(objCotizacion.getConcepto());
				objCotizacionNueva.setSolicitudFecha(objCotizacion.getSolicitudFecha());
				objCotizacionNueva.setCliente(objCotizacion.getCliente());
				objCotizacionNueva.setClienteContacto(objCotizacion.getClienteContacto());
				objCotizacionNueva.setUbicacion(objCotizacion.getUbicacion());
				objCotizacionNueva.setEntregaLugar(objCotizacion.getEntregaLugar());
				objCotizacionNueva.setEntregaDiasHabiles(objCotizacion.getEntregaDiasHabiles());
				objCotizacionNueva.setVigenciaPrecioDiasHabiles(objCotizacion.getVigenciaPrecioDiasHabiles());
				objCotizacionNueva.setMoneda(objCotizacion.getMoneda());
				objCotizacionNueva.setFormaPago(objCotizacion.getFormaPago());
				objCotizacionNueva.setDiasCredito(objCotizacion.getDiasCredito());
				objCotizacionNueva.setCondicionesPago(objCotizacion.getCondicionesPago());
				objCotizacionNueva.setObservaciones(objCotizacion.getObservaciones());
				objCotizacionNueva.setCotizacionEstatus(cotizacionEstatusService.findByIdCotizacionEstatus(1));
				objCotizacionNueva.setUsuarioVendedor(objCotizacion.getUsuarioVendedor());
				objCotizacionNueva.setMaestra(objCotizacion.isMaestra());
				objCotizacionNueva.setRenta(objCotizacion.isRenta());
				objCotizacionNueva.setNormal(objCotizacion.isNormal());
				
				if(objCotizacion.getOportunidadNegocio() != null) {
					objCotizacionNueva.setOportunidadNegocio(objCotizacion.getOportunidadNegocio());
				}
				
				objCotizacionNueva.setSubtotal(objCotizacion.getSubtotal());
				objCotizacionNueva.setIvaPorcentaje(objCotizacion.getIvaPorcentaje());
				objCotizacionNueva.setIva(objCotizacion.getIva());
				objCotizacionNueva.setTotal(objCotizacion.getTotal());
				
				cotizacionService.clone(objCotizacion, objCotizacionNueva);
				respuesta = true;
				titulo = "Cotización Clonada!";
				mensaje = "La cotizacion ha sido clonada exitosamente.";				
				
			} else {
				throw new ApplicationException(EnumException.COTIZACIONES_CLONE_002);
			}
			
		} catch(ApplicationException exception) {
			throw new ApplicationException(EnumException.COTIZACIONES_CLONE_001);
		}
								
		JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
		jsonReturn	.add("respuesta", respuesta)
					.add("titulo", titulo)
					.add("mensaje", mensaje);
		
		return jsonReturn.build().toString();
	}

	@RequestMapping(value = "get-cotizacion-data-form", method = RequestMethod.GET)
	public @ResponseBody String getDataForm() {
								
		Boolean respuesta = false;
		JsonObject jsonEmpresas = null;
		JsonObject jsonMonedas = null;
		JsonObject jsonFormasPagos = null;
		JsonObject jsonUsuarios = null;
		JsonObject jsonUsuariosGrupos = null;
		JsonObject jsonClientes = null;
				
		try {
			jsonEmpresas = empresaService.jsonEmpresas();
			jsonMonedas = monedaService.jsonMonedas();
			jsonFormasPagos = formaPagoService.jsonFormasPagos();
			jsonUsuarios = usuarioService.jsonUsuariosActivos();
			jsonUsuariosGrupos = usuarioService.jsonUsuariosGruposActivos();
			jsonClientes = clienteService.jsonClientesActivos();

			respuesta = true;
		} catch(ApplicationException exception) {

		}
					
		JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
		jsonReturn	.add("respuesta", respuesta)
					.add("jsonEmpresas", jsonEmpresas)
					.add("jsonMonedas", jsonMonedas)
					.add("jsonFormasPagos", jsonFormasPagos)
					.add("jsonUsuarios", jsonUsuarios)
					.add("jsonUsuariosGrupos", jsonUsuariosGrupos)
					.add("jsonClientes", jsonClientes);

		
		return jsonReturn.build().toString();
	}

}
