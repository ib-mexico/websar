package com.ibmexico.controllers;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
import com.ibmexico.entities.ActividadEntity;
import com.ibmexico.entities.ActividadTipoEntity;
import com.ibmexico.entities.ClienteContactoEntity;
import com.ibmexico.entities.ClienteEntity;
import com.ibmexico.entities.ClienteGiroEntity;
import com.ibmexico.entities.EmpresaEntity;
import com.ibmexico.entities.OportunidadNegocioEntity;
import com.ibmexico.entities.OportunidadNegocioEstatusEntity;
import com.ibmexico.entities.OportunidadNegocioFicheroEntity;
import com.ibmexico.entities.SucursalEntity;
import com.ibmexico.entities.UsuarioEntity;
import com.ibmexico.libraries.Templates;
import com.ibmexico.services.ActividadService;
import com.ibmexico.services.ActividadTipoService;
import com.ibmexico.services.ClienteContactoService;
import com.ibmexico.services.ClienteGiroService;
import com.ibmexico.services.ClienteService;
import com.ibmexico.services.EmpresaService;
import com.ibmexico.services.OportunidadNegocioEstatusService;
import com.ibmexico.services.OportunidadNegocioFicheroService;
import com.ibmexico.services.OportunidadNegocioService;
import com.ibmexico.services.SessionService;
import com.ibmexico.services.SucursalService;
import com.ibmexico.services.UsuarioService;
import com.lowagie.text.DocumentException;
import com.pusher.rest.Pusher;

@Controller
@RequestMapping("controlPanel/oportunidadesNegocios")
public class OportunidadesNegociosController {

	@Autowired
	@Qualifier("modelAndViewComponent")
	private ModelAndViewComponent modelAndViewComponent;
	
	@Autowired
	@Qualifier("pdfComponent")
	private PdfComponent pdfComponent;
	
	@Autowired
	@Qualifier("oportunidadNegocioService")
	private OportunidadNegocioService oportunidadNegocioService;
	
	@Autowired
	@Qualifier("oportunidadNegocioFicheroService")
	private OportunidadNegocioFicheroService oportunidadNegocioFicheroService;
	
	@Autowired
	@Qualifier("oportunidadNegocioEstatusService")
	private OportunidadNegocioEstatusService oportunidadNegocioEstatusService;
	
	@Autowired
	@Qualifier("usuarioService")
	private UsuarioService usuarioService;
	
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
	@Qualifier("sucursalService")
	private SucursalService sucursalService;
	
	@Autowired
	@Qualifier("actividadTipoService")
	private ActividadTipoService actividadTipoService;
	
	@Autowired
	@Qualifier("actividadService")
	private ActividadService actividadService;
	
	@Autowired
	@Qualifier("empresaService")
	private EmpresaService empresaService;
	
	@Autowired
	@Qualifier("sessionService")
	private SessionService sessionService;
	
	@GetMapping({"", "/"})
	public ModelAndView index() {
		
		List<OportunidadNegocioEntity> lstAbiertos = oportunidadNegocioService.listOportunidadesNegociosEmpresa(1,1);
		List<OportunidadNegocioEntity> lstProgreso = oportunidadNegocioService.listOportunidadesNegociosEmpresa(2,1);
		List<OportunidadNegocioEntity> lstRentas = oportunidadNegocioService.listOportunidadesNegociosEmpresa(3,1);
		List<OportunidadNegocioEntity> lstCerrado = oportunidadNegocioService.listOportunidadesNegociosEmpresa(4,1);
		List<OportunidadNegocioEntity> lstPerdidos = oportunidadNegocioService.listOportunidadesNegociosEmpresa(5,1);
		
		List<OportunidadNegocioEntity> lstAbiertosS3s = oportunidadNegocioService.listOportunidadesNegociosEmpresa(1,2);
		List<OportunidadNegocioEntity> lstProgresoS3s = oportunidadNegocioService.listOportunidadesNegociosEmpresa(2,2);
		List<OportunidadNegocioEntity> lstRentasS3s = oportunidadNegocioService.listOportunidadesNegociosEmpresa(3,2);
		List<OportunidadNegocioEntity> lstCerradoS3s = oportunidadNegocioService.listOportunidadesNegociosEmpresa(4,2);
		List<OportunidadNegocioEntity> lstPerdidosS3s = oportunidadNegocioService.listOportunidadesNegociosEmpresa(5,2);
		
		List<OportunidadNegocioEntity> lstAbiertosR2a = oportunidadNegocioService.listOportunidadesNegociosEmpresa(1,3);
		List<OportunidadNegocioEntity> lstProgresoR2a = oportunidadNegocioService.listOportunidadesNegociosEmpresa(2,3);
		List<OportunidadNegocioEntity> lstRentasR2a = oportunidadNegocioService.listOportunidadesNegociosEmpresa(3,3);
		List<OportunidadNegocioEntity> lstCerradoR2a = oportunidadNegocioService.listOportunidadesNegociosEmpresa(4,3);
		List<OportunidadNegocioEntity> lstPerdidosR2a = oportunidadNegocioService.listOportunidadesNegociosEmpresa(5,3);
		
		String totalAbiertos = oportunidadNegocioService.totalIngresoEstimadoEmpresa(1,1);
		String totalProgreso = oportunidadNegocioService.totalIngresoEstimadoEmpresa(2,1);
		String totalRentas = oportunidadNegocioService.totalIngresoEstimadoEmpresa(3,1);
		String totalCerrado = oportunidadNegocioService.totalIngresoEstimadoEmpresa(4,1);
		String totalPerdidos = oportunidadNegocioService.totalIngresoEstimadoEmpresa(5,1);
		
		String totalAbiertosS3s = oportunidadNegocioService.totalIngresoEstimadoEmpresa(1,2);
		String totalProgresoS3s = oportunidadNegocioService.totalIngresoEstimadoEmpresa(2,2);
		String totalRentasS3s = oportunidadNegocioService.totalIngresoEstimadoEmpresa(3,2);
		String totalCerradoS3s = oportunidadNegocioService.totalIngresoEstimadoEmpresa(4,2);
		String totalPerdidosS3s = oportunidadNegocioService.totalIngresoEstimadoEmpresa(5,2);
		
		String totalAbiertosR2a = oportunidadNegocioService.totalIngresoEstimadoEmpresa(1,3);
		String totalProgresoR2a = oportunidadNegocioService.totalIngresoEstimadoEmpresa(2,3);
		String totalRentasR2a = oportunidadNegocioService.totalIngresoEstimadoEmpresa(3,3);
		String totalCerradoR2a = oportunidadNegocioService.totalIngresoEstimadoEmpresa(4,3);
		String totalPerdidosR2a = oportunidadNegocioService.totalIngresoEstimadoEmpresa(5,3);
		
		ModelAndView objModelAndView = modelAndViewComponent.createModelAndViewControlPanel(Templates.CONTROL_PANEL_OPORTUNIDADES_INDEX);
		objModelAndView.addObject("totalAbiertos", totalAbiertos);
		objModelAndView.addObject("totalProgreso", totalProgreso);
		objModelAndView.addObject("totalRentas", totalRentas);
		objModelAndView.addObject("totalCerrado", totalCerrado);
		objModelAndView.addObject("totalPerdidos", totalPerdidos);		
		objModelAndView.addObject("lstAbiertos", lstAbiertos);
		objModelAndView.addObject("lstProgreso", lstProgreso);
		objModelAndView.addObject("lstRentas", lstRentas);
		objModelAndView.addObject("lstCerrado", lstCerrado);
		objModelAndView.addObject("lstPerdidos", lstPerdidos);
		
		objModelAndView.addObject("totalAbiertosS3s", totalAbiertosS3s);
		objModelAndView.addObject("totalProgresoS3s", totalProgresoS3s);
		objModelAndView.addObject("totalRentasS3s", totalRentasS3s);
		objModelAndView.addObject("totalCerradoS3s", totalCerradoS3s);
		objModelAndView.addObject("totalPerdidosS3s", totalPerdidosS3s);		
		objModelAndView.addObject("lstAbiertosS3s", lstAbiertosS3s);
		objModelAndView.addObject("lstProgresoS3s", lstProgresoS3s);
		objModelAndView.addObject("lstRentasS3s", lstRentasS3s);
		objModelAndView.addObject("lstCerradoS3s", lstCerradoS3s);
		objModelAndView.addObject("lstPerdidosS3s", lstPerdidosS3s);
		
		objModelAndView.addObject("totalAbiertosR2a", totalAbiertosR2a);
		objModelAndView.addObject("totalProgresoR2a", totalProgresoR2a);
		objModelAndView.addObject("totalRentasR2a", totalRentasR2a);
		objModelAndView.addObject("totalCerradoR2a", totalCerradoR2a);
		objModelAndView.addObject("totalPerdidosR2a", totalPerdidosR2a);		
		objModelAndView.addObject("lstAbiertosR2a", lstAbiertosR2a);
		objModelAndView.addObject("lstProgresoR2a", lstProgresoR2a);
		objModelAndView.addObject("lstRentasR2a", lstRentasR2a);
		objModelAndView.addObject("lstCerradoR2a", lstCerradoR2a);
		objModelAndView.addObject("lstPerdidosR2a", lstPerdidosR2a);
		
		return objModelAndView;
	}
	
	@GetMapping("/create")
	public ModelAndView create() {
		
		List<UsuarioEntity> lstUsuarios = usuarioService.listUsuarios();
		List<ClienteEntity> lstClientes = clienteService.listClientesActivos();
		List<SucursalEntity> lstSucursales = sucursalService.listSucursales();
		List<ClienteGiroEntity> lstClientesGiros = clienteGiroService.listClientesGiros();
		List<EmpresaEntity> lstEmpresas = empresaService.listEmpresas();
		
		ModelAndView objModelAndView = modelAndViewComponent.createModelAndViewControlPanel(Templates.CONTROL_PANEL_OPORTUNIDADES_CREATE);
		objModelAndView.addObject("lstUsuarios", lstUsuarios);
		objModelAndView.addObject("lstClientes", lstClientes);
		objModelAndView.addObject("lstClientesGiros", lstClientesGiros);
		objModelAndView.addObject("lstSucursales", lstSucursales);
		objModelAndView.addObject("lstEmpresas", lstEmpresas);
		
		return objModelAndView;
	}
	
	@RequestMapping(value = "", method = RequestMethod.POST)
	public RedirectView store(@RequestParam(value="txtOportunidad") String txtOportunidad,
								@RequestParam(value="cmbEmpresa") Integer cmbEmpresa,
								@RequestParam(value="txtIngresoEstimado") String txtIngresoEstimado,
								@RequestParam(value="txtProbabilidad", required=false, defaultValue="") String txtProbabilidad,
								@RequestParam(value="cmbCliente") int cmbCliente,
								@RequestParam(value="txtCierreFecha", required=false, defaultValue="") String txtCierreFecha,
								@RequestParam(value="cmbClienteContacto") int cmbClienteContacto,
								@RequestParam(value="cmbVendedor") int cmbVendedor,
								@RequestParam(value="txtPrioridad", required=false, defaultValue="0") int txtPrioridad,
								@RequestParam(value="txtNotasInternas", required=false, defaultValue="") String txtNotasInternas,
								RedirectAttributes objRedirectAttributes) {
		
		RedirectView objRedirectView = null;
		OportunidadNegocioEntity objOportunidad = new OportunidadNegocioEntity();
		
		try {
			
			objOportunidad.setOportunidad(txtOportunidad);
			objOportunidad.setEmpresa(empresaService.findByIdEmpresa(cmbEmpresa));
			objOportunidad.setIngresoEstimado(new BigDecimal(txtIngresoEstimado));
			objOportunidad.setProbabilidadPorcentaje(new BigDecimal(txtProbabilidad));
			objOportunidad.setCliente(clienteService.findByIdCliente(cmbCliente));
			objOportunidad.setClienteContacto(clienteContactoService.findByIdClienteContacto(cmbClienteContacto));
			objOportunidad.setUsuarioVendedor(usuarioService.findByIdUsuario(cmbVendedor));
			
			if(!txtCierreFecha.equals("")) {
				objOportunidad.setCierreFecha(LocalDate.parse(txtCierreFecha, GeneralConfiguration.getInstance().getDateFormatterNatural()));
			}
			
			objOportunidad.setPrioridad(txtPrioridad);
			objOportunidad.setNotasInternas(txtNotasInternas);
			objOportunidad.setOportunidadNegocioEstatus(oportunidadNegocioEstatusService.findByIdOportunidadNegocioEstatus(1));
			
			oportunidadNegocioService.create(objOportunidad);
			objRedirectView = new RedirectView("/WebSar/controlPanel/oportunidadesNegocios");
			modelAndViewComponent.addResult(objRedirectAttributes, EnumMessage.OPORTUNIDADES_CREATE_001);
			
		} catch(ApplicationException exception) {
			objRedirectView = new RedirectView("/WebSar/controlPanel/oportunidadesNegocios/create");
			modelAndViewComponent.addResult(objRedirectAttributes, exception);
		}
		
		
		return objRedirectView;
	}

	@GetMapping({"{paramIdOportunidad}/edit", "{paramIdOportunidad}/edit/"})
	public ModelAndView edit(@PathVariable("paramIdOportunidad") int paramIdOportunidad) {
		
		OportunidadNegocioEntity objOportunidad = oportunidadNegocioService.findByIdOportunidadNegocio(paramIdOportunidad);
		
		List<OportunidadNegocioEstatusEntity> lstOportunidadesEstatus = oportunidadNegocioEstatusService.listOportunidadesEstatus();
		List<UsuarioEntity> lstUsuarios = usuarioService.listUsuarios();
		List<ClienteEntity> lstClientes = clienteService.listClientesActivos();
		List<ClienteContactoEntity> lstContactos = clienteContactoService.listClienteContactosActivos(objOportunidad.getCliente().getIdCliente());
		List<SucursalEntity> lstSucursales = sucursalService.listSucursales();
		List<ClienteGiroEntity> lstClientesGiros = clienteGiroService.listClientesGiros();
		List<EmpresaEntity> lstEmpresas = empresaService.listEmpresas();
		
		ModelAndView objModelAndView = modelAndViewComponent.createModelAndViewControlPanel(Templates.CONTROL_PANEL_OPORTUNIDADES_EDIT);
		objModelAndView.addObject("objOportunidad", objOportunidad);
		objModelAndView.addObject("lstOportunidadesEstatus", lstOportunidadesEstatus);
		objModelAndView.addObject("lstUsuarios", lstUsuarios);
		objModelAndView.addObject("lstClientes", lstClientes);
		objModelAndView.addObject("lstContactos", lstContactos);
		objModelAndView.addObject("lstClientesGiros", lstClientesGiros);
		objModelAndView.addObject("lstSucursales", lstSucursales);
		objModelAndView.addObject("lstEmpresas", lstEmpresas);
		
		return objModelAndView;
	}
	
	@RequestMapping(value = {"{paramIdOportunidad}", "{paramIdOportunidad}/"}, method = RequestMethod.PUT)
	public RedirectView store(	@PathVariable("paramIdOportunidad") int paramIdOportunidad,
								@RequestParam(value="cmbEmpresa") Integer cmbEmpresa,
								@RequestParam(value="txtOportunidad") String txtOportunidad,
								@RequestParam(value="cmbEstatus") int cmbEstatus,
								@RequestParam(value="txtIngresoEstimado") String txtIngresoEstimado,
								@RequestParam(value="txtProbabilidad", required=false, defaultValue="") String txtProbabilidad,
								@RequestParam(value="cmbCliente") int cmbCliente,
								@RequestParam(value="txtCierreFecha", required=false, defaultValue="") String txtCierreFecha,
								@RequestParam(value="cmbClienteContacto") int cmbClienteContacto,
								@RequestParam(value="cmbVendedor") int cmbVendedor,
								@RequestParam(value="txtPrioridad", required=false, defaultValue="0") int txtPrioridad,
								@RequestParam(value="txtNotasInternas", required=false, defaultValue="") String txtNotasInternas,
								RedirectAttributes objRedirectAttributes) {
		
		RedirectView objRedirectView = null;
		OportunidadNegocioEntity objOportunidad = oportunidadNegocioService.findByIdOportunidadNegocio(paramIdOportunidad);
		
		try {
			
			objOportunidad.setOportunidad(txtOportunidad);
			objOportunidad.setEmpresa(empresaService.findByIdEmpresa(cmbEmpresa));
			objOportunidad.setIngresoEstimado(new BigDecimal(txtIngresoEstimado));
			objOportunidad.setProbabilidadPorcentaje(new BigDecimal(txtProbabilidad));
			objOportunidad.setCliente(clienteService.findByIdCliente(cmbCliente));
			objOportunidad.setClienteContacto(clienteContactoService.findByIdClienteContacto(cmbClienteContacto));
			objOportunidad.setUsuarioVendedor(usuarioService.findByIdUsuario(cmbVendedor));
			
			if(!txtCierreFecha.equals("")) {
				objOportunidad.setCierreFecha(LocalDate.parse(txtCierreFecha, GeneralConfiguration.getInstance().getDateFormatterNatural()));
			}
			
			objOportunidad.setPrioridad(txtPrioridad);
			objOportunidad.setNotasInternas(txtNotasInternas);
			objOportunidad.setOportunidadNegocioEstatus(oportunidadNegocioEstatusService.findByIdOportunidadNegocioEstatus(cmbEstatus));
			
			oportunidadNegocioService.update(objOportunidad);
			objRedirectView = new RedirectView("/WebSar/controlPanel/oportunidadesNegocios");
			modelAndViewComponent.addResult(objRedirectAttributes, EnumMessage.OPORTUNIDADES_UPDATE_001);
			
		} catch(ApplicationException exception) {
			objRedirectView = new RedirectView("/WebSar/controlPanel/oportunidadesNegocios/" + paramIdOportunidad + "/edit");
			modelAndViewComponent.addResult(objRedirectAttributes, exception);
		}
		
		
		return objRedirectView;
	}
	
	@GetMapping({"{paramIdOportunidad}/upload-file", "{paramIdOportunidad}/upload-file/"})
	public ModelAndView upload(@PathVariable("paramIdOportunidad") int paramIdOportunidad) {
		
		OportunidadNegocioEntity objOportunidad = oportunidadNegocioService.findByIdOportunidadNegocio(paramIdOportunidad);	
		
		ModelAndView objModelAndView = modelAndViewComponent.createModelAndViewControlPanel(Templates.CONTROL_PANEL_OPORTUNIDADES_UPLOAD);
		objModelAndView.addObject("objOportunidad", objOportunidad);
		
		return objModelAndView;
	}
	
	@RequestMapping(value = "{paramIdOportunidad}/upload-file", method = RequestMethod.POST)
	public RedirectView storeFile(@PathVariable("paramIdOportunidad")  int paramIdOportunidad,
									@RequestParam("txtTitulo") String txtTitulo,
									@RequestParam("txtDescripcion") String txtDescripcion,
									@RequestParam(value="fichero", required=false) MultipartFile fichero,
									
									RedirectAttributes objRedirectAttributes) {
		
		RedirectView objRedirectView = null;
		OportunidadNegocioFicheroEntity objOportunidadFichero = new OportunidadNegocioFicheroEntity();
		OportunidadNegocioEntity objOportunidad = oportunidadNegocioService.findByIdOportunidadNegocio(paramIdOportunidad);
		
		try {
			objOportunidadFichero.setTitulo(txtTitulo);
			objOportunidadFichero.setDescripcion(txtDescripcion);
			objOportunidadFichero.setOportunidadNegocio(objOportunidad);
			
			oportunidadNegocioFicheroService.addFile(objOportunidadFichero, fichero);
			objRedirectView = new RedirectView("/WebSar/controlPanel/oportunidadesNegocios");
			modelAndViewComponent.addResult(objRedirectAttributes, EnumMessage.OPORTUNIDADES_FICHEROS_CREATE_001);
			
		} catch(ApplicationException exception) {
			objRedirectView = new RedirectView("/WebSar/controlPanel/oportunidadesNegocios/" + paramIdOportunidad + "/upload-file");
			modelAndViewComponent.addResult(objRedirectAttributes, exception);
		}
		
		
		
		return objRedirectView;
	}
	
	@RequestMapping(value = {"{paramIdOportunidad}/delete", "{paramIdOportunidad}/delete/"}, method = RequestMethod.POST)
	public @ResponseBody String delete( @PathVariable("paramIdOportunidad") int paramIdOportunidad,
										@RequestParam("idOportunidad") int idOportunidad) {
		
		OportunidadNegocioEntity objOportunidad = oportunidadNegocioService.findByIdOportunidadNegocio(idOportunidad);	
		Boolean respuesta = false;
		String titulo = "";
		String mensaje = "";
				
		try {
			if(objOportunidad != null) {
				
				objOportunidad.setEliminado(true);
				oportunidadNegocioService.update(objOportunidad);
				
				respuesta = true;
				titulo = "Eliminado!";
				mensaje = "La partida ha sido eliminada exitosamente.";
			}
			else {
				throw new ApplicationException(EnumException.OPORTUNIDADES_DELETE_001);
			}
			
		} catch(ApplicationException exception) {
			throw new ApplicationException(EnumException.OPORTUNIDADES_DELETE_001);
		}
		
		JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
		jsonReturn	.add("respuesta", respuesta)
					.add("titulo", titulo)
					.add("mensaje", mensaje);

										
		return jsonReturn.build().toString();
	}
	
	//ACTIVIDADES DE LA OPORTUNIDAD
	@GetMapping({"{paramIdOportunidad}/create-actividad", "{paramIdOportunidad}/create-actividad/"})
	public ModelAndView createActividad(@PathVariable("paramIdOportunidad") int paramIdOportunidad) {
		
		OportunidadNegocioEntity objOportunidad = oportunidadNegocioService.findByIdOportunidadNegocio(paramIdOportunidad);
		List<ActividadTipoEntity> lstActividadesTipos = actividadTipoService.listActividadesTipos();
		List<UsuarioEntity> lstUsuarios = usuarioService.listUsuarios();
		
		ModelAndView objModelAndView = modelAndViewComponent.createModelAndViewControlPanel(Templates.CONTROL_PANEL_OPORTUNIDADES_ACTIVIDADES_CREATE);
		objModelAndView.addObject("objOportunidad", objOportunidad);
		objModelAndView.addObject("lstActividadesTipos", lstActividadesTipos);
		objModelAndView.addObject("lstUsuarios", lstUsuarios);
		
		return objModelAndView;
	}
	
	@RequestMapping(value = {"{paramIdOportunidad}/actividad", "{paramIdOportunidad}/actividad/"}, method = RequestMethod.POST)
	public RedirectView store(@PathVariable("paramIdOportunidad") int paramIdOportunidad,
								@RequestParam(value="cmbActividad") int cmbActividad,
								@RequestParam(value="txtVencimientoFecha", required=false, defaultValue="") String txtVencimientoFecha,
								@RequestParam(value="txtResumen") String txtResumen,
								@RequestParam(value="cmbUsuario") int cmbUsuario,
								@RequestParam(value="txtNotas") String txtNotas,
								@RequestParam(value="txtColor", required=false, defaultValue="#F44336") String txtColor,
								RedirectAttributes objRedirectAttributes) {
		
		RedirectView objRedirectView = null;
		OportunidadNegocioEntity objOportunidad = oportunidadNegocioService.findByIdOportunidadNegocio(paramIdOportunidad);
		
		try {
			
			if(objOportunidad != null) {
				
				ActividadEntity objActividad = new ActividadEntity();
				
				objActividad.setActividadTipo(actividadTipoService.findByIdActividadTipo(cmbActividad));
				objActividad.setResumen(txtResumen);
				objActividad.setUsuario(usuarioService.findByIdUsuario(cmbUsuario));
				objActividad.setOportunidad(objOportunidad);
				objActividad.setNotas(txtNotas);
				objActividad.setColor(txtColor);
				
				if(!txtVencimientoFecha.equals("")) {
					objActividad.setVencimientoFecha(LocalDate.parse(txtVencimientoFecha, GeneralConfiguration.getInstance().getDateFormatterNatural()));
				}
				
				actividadService.create(objActividad);
				
				Pusher pusher = new Pusher("575478", "7b4b9197d41e13beb30d", "8c116fb03f6b79d32085");
				pusher.setCluster("us2");
				
				JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
				jsonReturn	.add("id_usuario", objActividad.getUsuario().getIdUsuario())
							.add("message", "Te asignaron una actividad en la OPN: " + objActividad.getOportunidad().getOportunidad());						
				
				pusher.trigger("notifications", "new-notification", jsonReturn.build());
				
				objRedirectView = new RedirectView("/WebSar/controlPanel/oportunidadesNegocios");
				modelAndViewComponent.addResult(objRedirectAttributes, EnumMessage.ACTIVIDADES_CREATE_001);
				
			} else {
				throw new ApplicationException(EnumException.ACTIVIDADES_CREATE_001);
			}
		}catch(ApplicationException exception) {
			objRedirectView = new RedirectView("/WebSar/controlPanel/oportunidadesNegocios");
			modelAndViewComponent.addResult(objRedirectAttributes, exception);
		}
		
		return objRedirectView;
	}
	
	@GetMapping({"{paramIdActividad}/edit-actividad", "{paramIdActividad}/edit-actividad/"})
	public ModelAndView editActividad(@PathVariable("paramIdActividad") int paramIdActividad) {
		
		ActividadEntity objActividad = actividadService.findByIdActividad(paramIdActividad);
		List<ActividadTipoEntity> lstActividadesTipos = actividadTipoService.listActividadesTipos();
		List<UsuarioEntity> lstUsuarios = usuarioService.listUsuarios();
		
		ModelAndView objModelAndView = modelAndViewComponent.createModelAndViewControlPanel(Templates.CONTROL_PANEL_OPORTUNIDADES_ACTIVIDADES_EDIT);
		objModelAndView.addObject("objActividad", objActividad);
		objModelAndView.addObject("lstActividadesTipos", lstActividadesTipos);
		objModelAndView.addObject("lstUsuarios", lstUsuarios);
		
		return objModelAndView;
	}		
	
	@RequestMapping(value = {"{paramIdActividad}/actividad", "{paramIdActividad}/actividad/"}, method = RequestMethod.PUT)
	public RedirectView store(@PathVariable("paramIdActividad") int paramIdActividad,
								@RequestParam(value="cmbActividad") int cmbActividad,
								@RequestParam(value="txtVencimientoFecha", required=false, defaultValue="") String txtVencimientoFecha,
								@RequestParam(value="txtResumen") String txtResumen,
								@RequestParam(value="cmbUsuario") int cmbUsuario,
								@RequestParam(value="txtNotas") String txtNotas,
								@RequestParam(value="txtColor", required=false, defaultValue="#F44336") String txtColor,
								@RequestParam(value="chkFinalizado", required=false, defaultValue="false") String chkFinalizado,
								RedirectAttributes objRedirectAttributes) {
		
		RedirectView objRedirectView = null;
		ActividadEntity objActividad = actividadService.findByIdActividad(paramIdActividad);
		
		try {
			
			if(objActividad != null) {				
				
				objActividad.setActividadTipo(actividadTipoService.findByIdActividadTipo(cmbActividad));
				objActividad.setResumen(txtResumen);
				objActividad.setUsuario(usuarioService.findByIdUsuario(cmbUsuario));
				objActividad.setNotas(txtNotas);
				objActividad.setColor(txtColor);
				
				if(!txtVencimientoFecha.equals("")) {
					objActividad.setVencimientoFecha(LocalDate.parse(txtVencimientoFecha, GeneralConfiguration.getInstance().getDateFormatterNatural()));
				}
				
				if(chkFinalizado.equals("true")) {
					objActividad.setFinalizado(true);
				}
				
				actividadService.update(objActividad);
				objRedirectView = new RedirectView("/WebSar/controlPanel/oportunidadesNegocios");
				modelAndViewComponent.addResult(objRedirectAttributes, EnumMessage.ACTIVIDADES_UPDATE_001);
				
			} else {
				throw new ApplicationException(EnumException.ACTIVIDADES_UPDATE_001);
			}
		}catch(ApplicationException exception) {
			objRedirectView = new RedirectView("/WebSar/controlPanel/oportunidadesNegocios");
			modelAndViewComponent.addResult(objRedirectAttributes, exception);
		}
		
		return objRedirectView;
	}
	
	//REPORTES DE LA OPORTUNIDAD
	@RequestMapping(value = {"reporte-opn-pdf/{paramIdOportunidad}", "reporte-opn-pdf/{paramIdOportunidad}/"}, method = RequestMethod.GET, produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<InputStreamResource> previewPdfReporteOPN(@PathVariable(value="paramIdOportunidad", required=true) int paramIdOportunidad) throws IOException, DocumentException {
		
		OportunidadNegocioEntity objOportunidad = oportunidadNegocioService.findByIdOportunidadNegocio(paramIdOportunidad);		
		
		LocalDateTime ldtNow = LocalDateTime.now();
		Templates objTemplates = new Templates();
		
		String path_file = ldtNow.getYear() + "_" + ldtNow.getMonthValue() + "_" + ldtNow.getDayOfMonth() + "_REPORTE_OPORTUNIDAD_NEGOCIO.pdf";
				
		Context objContext = new Context();
		objContext.setVariable("_TEMPLATE_", Templates.PDF_REPORTE_OPORTUNIDAD_NEGOCIO);
		objContext.setVariable("title", "Reporte de Oportunidad de Negocio");
		
		objContext.setVariable("objOportunidad", objOportunidad);			
		
		return pdfComponent.generate(path_file, objTemplates.FOUNDATION_PDF, objContext);	
	}
	
	
	//CALENDARIO
	@GetMapping({"calendario", "calendario/"})
	public ModelAndView verCalendario() {
		
		List<ActividadEntity> lstActividades = actividadService.listActividades();
		
		JsonArrayBuilder jsonActividades = Json.createArrayBuilder();
		for(ActividadEntity itemActividad : lstActividades) {
			
			jsonActividades.add(Json.createObjectBuilder()
				.add("id", itemActividad.getIdActividad())
				.add("title", itemActividad.getResumen())
				.add("color", itemActividad.getColor())
				
				.add("usuario", itemActividad.getUsuario().getAliasCorreo())
				.add("tipoActividad", itemActividad.getActividadTipo().getActividadTipo())
				.add("creacionFecha", itemActividad.getCreacionFecha().toString())
				.add("vencimientoFechaNatural", itemActividad.getVencimientoFechaNatural())
				.add("vencimientoFecha", itemActividad.getVencimientoFecha().toString())
				
				.add("start", LocalDateTime.of(itemActividad.getVencimientoFecha(), LocalTime.of(0,0)).toString() + ":00" )
				.add("end", LocalDateTime.of(itemActividad.getVencimientoFecha(), LocalTime.of(23,59)).toString() + ":59" )
			);
		}
		
		
		
		ModelAndView objModelAndView = modelAndViewComponent.createModelAndViewControlPanel(Templates.CONTROL_PANEL_OPORTUNIDADES_CALENDAR);
		objModelAndView.addObject("lstActividades", lstActividades);
		objModelAndView.addObject("jsonActividades", jsonActividades.build().toString());
		
		return objModelAndView;
	}	
	
	
	
	
}
