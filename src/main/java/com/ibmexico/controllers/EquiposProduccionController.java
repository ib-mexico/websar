package com.ibmexico.controllers;

import java.time.LocalDate;
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
import com.ibmexico.components.PdfComponent;
import com.ibmexico.configurations.GeneralConfiguration;
import com.ibmexico.entities.ClienteEntity;
import com.ibmexico.entities.EquipoMarcaEntity;
import com.ibmexico.entities.EquipoProduccionEntity;
import com.ibmexico.libraries.DataTable;
import com.ibmexico.libraries.Templates;
import com.ibmexico.services.ClienteService;
import com.ibmexico.services.EquipoMarcaService;
import com.ibmexico.services.EquipoProduccionEstatusService;
import com.ibmexico.services.EquipoProduccionService;
import com.ibmexico.services.SessionService;
import com.ibmexico.services.UsuarioService;

@Controller
@RequestMapping("controlPanel/equipos-produccion")
public class EquiposProduccionController {

	@Autowired
	@Qualifier("modelAndViewComponent")
	private ModelAndViewComponent modelAndViewComponent;
	
	@Autowired
	@Qualifier("pdfComponent")
	private PdfComponent pdfComponent;
	
	@Autowired
	@Qualifier("equipoProduccionService")
	private EquipoProduccionService equipoProduccionService;
	
	@Autowired
	@Qualifier("equipoProduccionEstatusService")
	private EquipoProduccionEstatusService equipoProduccionEstatusService;
	
	@Autowired
	@Qualifier("equipoMarcaService")
	private EquipoMarcaService equipoMarcaService;
	
	@Autowired
	@Qualifier("usuarioService")
	private UsuarioService usuarioService;
	
	@Autowired
	@Qualifier("clienteService")
	private ClienteService clienteService;
	
	@Autowired
	@Qualifier("sessionService")
	private SessionService sessionService;
	
	@GetMapping({"", "/"})
	public ModelAndView index() {		
		
		ModelAndView objModelAndView = modelAndViewComponent.createModelAndViewControlPanel(Templates.CONTROL_PANEL_EQUIPOS_PRODUCCION_INDEX);
		objModelAndView.addObject("rolNuevoEquipo", sessionService.hasRol("EQUIPOS_PRODUCCION_CREATE"));
		objModelAndView.addObject("rolEditarEquipo", sessionService.hasRol("EQUIPOS_PRODUCCION_EDIT"));
		
		return objModelAndView;
	}
	
	@RequestMapping(value = "/table", method = RequestMethod.POST)	
	public @ResponseBody String table(	@RequestParam("offset") int offset,
										@RequestParam("limit") int limit,
										@RequestParam("_csrf") String _csrf,
										@RequestParam(value="search", required=false, defaultValue="") String search,
										@RequestParam(value="txtBootstrapTableDesde", required=false) String txtBootstrapTableDesde,
										@RequestParam(value="txtBootstrapTableHasta", required=false) String txtBootstrapTableHasta) {
		
		DataTable<EquipoProduccionEntity> dtEquipos = equipoProduccionService.dataTable(search, offset, limit, txtBootstrapTableDesde, txtBootstrapTableHasta);
		
		JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
		jsonReturn.add("total", dtEquipos.getTotal());
		JsonArrayBuilder jsonRows = Json.createArrayBuilder();
		
		dtEquipos.getRows().forEach((itemEquipo)-> {

			jsonRows.add(Json.createObjectBuilder()
				.add("idEquipo", itemEquipo.getIdEquipoProduccion())
				.add("marca", itemEquipo.getEquipoMarca().getMarca())
				.add("modelo", itemEquipo.getModelo())
				.add("numero_serie", itemEquipo.getNumeroSerie())
				.add("cliente", itemEquipo.getCliente().getCliente())
				.add("idEstatus", itemEquipo.getEquipoEstatus().getIdEquipoProduccionEstatus())
				.add("estatus", itemEquipo.getEquipoEstatus().getEquipoProduccionEstatus())
				.add("fecha_renovacion", itemEquipo.getRenovacionFechaNatural())
				
				.add("creacionFecha", itemEquipo.getCreacionFechaNatural())
				.add("creacionUsuario", itemEquipo.getCreacionUsuario().getAlias())
			);
		});

		jsonReturn.add("rows", jsonRows);

		return jsonReturn.build().toString();
	}	
	
	@GetMapping({"/create", "/create/"})
	public ModelAndView create() {		
		
		List<ClienteEntity> lstClientes = clienteService.listClientesActivos();
		List<EquipoMarcaEntity> lstEquiposMarcas = equipoMarcaService.listMarcasActivas();
		
		ModelAndView objModelAndView = modelAndViewComponent.createModelAndViewControlPanel(Templates.CONTROL_PANEL_EQUIPOS_PRODUCCION_CREATE);
		objModelAndView.addObject("lstEquiposMarcas", lstEquiposMarcas);
		objModelAndView.addObject("lstClientes", lstClientes);
		
		return objModelAndView;
	}
	
	@RequestMapping(value = "", method = RequestMethod.POST)
	public RedirectView store(@RequestParam(value="cmbCliente") Integer cmbCliente,
								@RequestParam(value="txtFechaRenovacion", required=false, defaultValue="") String txtFechaRenovacion,
								@RequestParam(value="cmbEquipoMarca") Integer cmbEquipoMarca,
								@RequestParam(value="txtModelo") String txtModelo,							
								@RequestParam(value="txtNumeroSerie") String txtNumeroSerie,
								@RequestParam(value="modalidadUso") String modalidadUso,
								@RequestParam(value="txtObservaciones") String txtObservaciones,
								RedirectAttributes objRedirectAttributes) {
		
		RedirectView objRedirectView = null;
		EquipoProduccionEntity objEquipo = new EquipoProduccionEntity();
		
		try {
			
			objEquipo.setCliente(clienteService.findByIdCliente(cmbCliente));
			objEquipo.SetRenovacionFecha(LocalDate.parse(txtFechaRenovacion, GeneralConfiguration.getInstance().getDateFormatterNatural()));
			objEquipo.setNumeroSerie(txtNumeroSerie);
			objEquipo.setModelo(txtModelo);
			objEquipo.setEquipoMarca(equipoMarcaService.findByIdEquipoMarca(cmbEquipoMarca));
			objEquipo.setObservaciones(txtObservaciones);
			
			if(modalidadUso.equals("venta")) {
				objEquipo.setBoolVenta(true);
			} else {
				objEquipo.setBoolRenta(true);
			}
			
			objEquipo.setEquipoEstatus(equipoProduccionEstatusService.findByIdEquipoProduccionEstatus(1));
			
			equipoProduccionService.create(objEquipo);
			objRedirectView = new RedirectView("/WebSar/controlPanel/equipos-produccion");
			modelAndViewComponent.addResult(objRedirectAttributes, EnumMessage.EQUIPOS_PRODUCCION_CREATE_001);
			
		} catch(ApplicationException exception) {
			objRedirectView = new RedirectView("/WebSar/controlPanel/equipos-produccion/create");
			modelAndViewComponent.addResult(objRedirectAttributes, exception);
		}
		
		
		return objRedirectView;
	}
	
	@GetMapping({"{paramIdEquipo}/edit", "{paramIdEquipo}/edit/"})
	public ModelAndView edit(@PathVariable(value="paramIdEquipo") Integer paramIdEquipo) {		
		
		EquipoProduccionEntity objEquipo = equipoProduccionService.findByIdEquipoProduccion(paramIdEquipo);
		
		List<ClienteEntity> lstClientes = clienteService.listClientesActivos();
		List<EquipoMarcaEntity> lstEquiposMarcas = equipoMarcaService.listMarcasActivas();
		
		ModelAndView objModelAndView = modelAndViewComponent.createModelAndViewControlPanel(Templates.CONTROL_PANEL_EQUIPOS_PRODUCCION_EDIT);
		objModelAndView.addObject("objEquipo", objEquipo);
		objModelAndView.addObject("lstClientes", lstClientes);
		objModelAndView.addObject("lstEquiposMarcas", lstEquiposMarcas);
		
		return objModelAndView;
	}
	
	@RequestMapping(value = {"{paramIdEquipo}", "{paramIdEquipo}/"}, method = RequestMethod.PUT)
	public RedirectView store( @PathVariable(value="paramIdEquipo") Integer paramIdEquipo,
								@RequestParam(value="cmbCliente") Integer cmbCliente,
								@RequestParam(value="txtFechaRenovacion", required=false, defaultValue="") String txtFechaRenovacion,
								@RequestParam(value="cmbEquipoMarca") Integer cmbEquipoMarca,
								@RequestParam(value="txtModelo") String txtModelo,							
								@RequestParam(value="txtNumeroSerie") String txtNumeroSerie,
								@RequestParam(value="modalidadUso") String modalidadUso,
								@RequestParam(value="txtObservaciones") String txtObservaciones,
								RedirectAttributes objRedirectAttributes) {
		
		RedirectView objRedirectView = null;
		EquipoProduccionEntity objEquipo = equipoProduccionService.findByIdEquipoProduccion(paramIdEquipo);
		
		try {
			if(objEquipo != null) {
				
				objEquipo.setCliente(clienteService.findByIdCliente(cmbCliente));
				
				//VALIDAMOS QUE LA FECHA INGRESADA SEA POSTERIOR QUE LA FECHA REGISTRADA PARA ACTUALIZAR EL ESTATUS DEL EQUIPO
				if(LocalDate.parse(txtFechaRenovacion, GeneralConfiguration.getInstance().getDateFormatterNatural()).isAfter(objEquipo.getRenovacionFecha())) {					
					objEquipo.SetRenovacionFecha(LocalDate.parse(txtFechaRenovacion, GeneralConfiguration.getInstance().getDateFormatterNatural()));
					objEquipo.setEquipoEstatus(equipoProduccionEstatusService.findByIdEquipoProduccionEstatus(1));
				}
				
				objEquipo.setNumeroSerie(txtNumeroSerie);
				objEquipo.setModelo(txtModelo);
				objEquipo.setEquipoMarca(equipoMarcaService.findByIdEquipoMarca(cmbEquipoMarca));
				objEquipo.setObservaciones(txtObservaciones);
				
				if(modalidadUso.equals("venta")) {
					objEquipo.setBoolVenta(true);
					objEquipo.setBoolRenta(false);
				} else {
					objEquipo.setBoolRenta(true);
					objEquipo.setBoolVenta(false);
				}
				
				equipoProduccionService.update(objEquipo);
				objRedirectView = new RedirectView("/WebSar/controlPanel/equipos-produccion");
				modelAndViewComponent.addResult(objRedirectAttributes, EnumMessage.EQUIPOS_PRODUCCION_EDIT_001);
			} else {
				throw new ApplicationException(EnumException.EQUIPOS_PRODUCCION_UPDATE_001);
			}						
		} catch(ApplicationException exception) {
			objRedirectView = new RedirectView("/WebSar/controlPanel/equipos-produccion/" + paramIdEquipo + "/edit");
			modelAndViewComponent.addResult(objRedirectAttributes, exception);
		}
		
		
		return objRedirectView;
	}
	
}
