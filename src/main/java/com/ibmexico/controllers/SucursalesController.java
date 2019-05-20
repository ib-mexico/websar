package com.ibmexico.controllers;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ibmexico.components.ModelAndViewComponent;
import com.ibmexico.entities.SucursalEntity;
import com.ibmexico.libraries.DataTable;
import com.ibmexico.libraries.Templates;
import com.ibmexico.services.SucursalService;

@Controller
@RequestMapping("controlPanel/sucursales")
public class SucursalesController {

	@Autowired
	@Qualifier("modelAndViewComponent")
	private ModelAndViewComponent modelAndViewComponent;
	
	@Autowired
	@Qualifier("sucursalService")
	private SucursalService sucursalService;
	
	@GetMapping({"", "/"})
	public ModelAndView index() {
		ModelAndView objModelAndView = modelAndViewComponent.createModelAndViewControlPanel(Templates.CONTROL_PANEL_SUCURSALES_INDEX);
		return objModelAndView;
	}
	
	@RequestMapping(value = "/table", method = RequestMethod.POST)	
	public @ResponseBody String table(	@RequestParam("offset") int offset,
										@RequestParam("limit") int limit,
										@RequestParam("_csrf") String _csrf,
										@RequestParam(value="search", required=false, defaultValue="") String search,
										@RequestParam(value="txtBootstrapTableDesde", required=false) String txtBootstrapTableDesde,
										@RequestParam(value="txtBootstrapTableHasta", required=false) String txtBootstrapTableHasta) {
		
		DataTable<SucursalEntity> dtSucursales = sucursalService.dataTable(search, offset, limit, txtBootstrapTableDesde, txtBootstrapTableHasta);
		
		JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
		jsonReturn.add("total", dtSucursales.getTotal());
		JsonArrayBuilder jsonRows = Json.createArrayBuilder();
		
		dtSucursales.getRows().forEach((itemSucursal)-> {

			jsonRows.add(Json.createObjectBuilder()
				.add("idSucursal", itemSucursal.getIdSucursal())
				
				.add("sucursal", itemSucursal.getSucursal())
				.add("domicilio", itemSucursal.getDomicilio())
				.add("colonia", itemSucursal.getColonia())
				.add("municipio", itemSucursal.getMunicipio())
				.add("ciudad", itemSucursal.getCiudad())
				.add("estado", itemSucursal.getEstado())				
				.add("codigoPostal", itemSucursal.getCodigoPostal())
				.add("telefono", itemSucursal.getTelefono())
				
				.add("eliminado", itemSucursal.isEliminado())
			);
		});
		jsonReturn.add("rows", jsonRows);

		return jsonReturn.build().toString();
	}
}
