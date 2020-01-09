package com.ibmexico.controllers;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import com.ibmexico.components.ModelAndViewComponent;
import com.ibmexico.entities.ActivoServicioProveedorEntity;
import com.ibmexico.entities.ProveedorEntity;
import com.ibmexico.libraries.DataTable;
import com.ibmexico.libraries.Templates;
import com.ibmexico.libraries.notifications.ApplicationException;
import com.ibmexico.services.ActivoServicioProveedorService;
import com.ibmexico.services.ActivoServicioService;
import com.ibmexico.services.ProveedorService;

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

@Controller
@RequestMapping("controlPanel/proveedores/")
public class ProveedoresServiciosController{

    @Autowired
	@Qualifier("modelAndViewComponent")
    private ModelAndViewComponent modelAndViewComponent;

    @Autowired
	@Qualifier("proveedorService")
	private ProveedorService proveedorService;

	@Autowired
	@Qualifier("activo_servicio_proveedor")
	private ActivoServicioProveedorService servicioProveedorService;

	@Autowired
	@Qualifier("activo_servicio")
	private ActivoServicioService servicioService;
	
    
    @GetMapping({"{paramIdProveedor}/servicios", "{paramIdProveedor}/servicios/"})
	public ModelAndView index(@PathVariable int paramIdProveedor) {
        ProveedorEntity objProveedor = proveedorService.findByIdProveedor(paramIdProveedor);
		ModelAndView objModelAndView = modelAndViewComponent.createModelAndViewControlPanel(Templates.CONTROL_PANEL_PROVEEDORES_SERVICIO_INDEX);
		objModelAndView.addObject("objProveedor", objProveedor);
		return objModelAndView;
	}

	   /**Obtener en JSON, los datos iniciales, */
	   @RequestMapping(value={"getServicios/{idProveedor}","getServicios/{idProveedor}/"})
	   public @ResponseBody String getDataCatalogoActivo(@PathVariable(value = "idProveedor") int idProveedor){
		   Boolean respuesta = false;
		   JsonObject jsonServicios = null;
		   JsonObject jsonProveedorServicio = null;
		   try {
			   jsonServicios = servicioService.jsonServicios();
			   jsonProveedorServicio = servicioProveedorService.jsonServicioProveedor(idProveedor);
			   respuesta = true;
		   } catch (ApplicationException exception) {
			   respuesta = false;
		   }
		   JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
		   jsonReturn.add("respuesta", respuesta).add("jsonServicios", jsonServicios)
		   			.add("jsonProveedorServico", jsonProveedorServicio);
		   return jsonReturn.build().toString();
	   }

	@RequestMapping(value = "{paramIdProveedor}/servicios/table", method = RequestMethod.POST)	
	public @ResponseBody String tableServicio(	@PathVariable("paramIdProveedor") int idProveedor,
										@RequestParam("offset") int offset,
										@RequestParam("limit") int limit,
										@RequestParam("_csrf") String _csrf,
										@RequestParam(value="search", required=false, defaultValue="") String search,
										@RequestParam(value="txtTableDesde", required=false) String txtTableDesde,
										@RequestParam(value="txtTableHasta", required=false) String txtTableHasta) {
		
		DataTable<ActivoServicioProveedorEntity> dtServicioProveedor = servicioProveedorService.dataTable(idProveedor, search, offset, limit, txtTableDesde, txtTableHasta);
		
		JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
		jsonReturn.add("total", dtServicioProveedor.getTotal());
		JsonArrayBuilder jsonRows = Json.createArrayBuilder();
		
		dtServicioProveedor.getRows().forEach((itemServProveedor)-> {
			jsonRows.add(Json.createObjectBuilder()
					
				.add("idServicioProveedor", itemServProveedor.getIdServicioProveedor())
				.add("idProveedor", itemServProveedor.getActivoProveedor().getIdProveedor())
				.add("proveedor", itemServProveedor.getActivoProveedor().getProveedor())
				.add("razon_social", itemServProveedor.getActivoProveedor().getRazonSocial())
				.add("proveedor_rfc", itemServProveedor.getActivoProveedor().getRfc())
				.add("idServicio", itemServProveedor.getActivoServicio().getIdServicioActivo())
				.add("servicio", itemServProveedor.getActivoServicio().getDescripcion())
				.add("tipoServicio", itemServProveedor.getActivoServicio().getTipoActivo().getNombre())
				.add("precioEstimado", itemServProveedor.getActivoServicio().getPrecioEstimadoNatural())
				//considero que falta un campo, donde se define si un servicio esta elimnado
			);
		});
		
		jsonReturn.add("rows", jsonRows);

		return jsonReturn.build().toString();
	}

	@RequestMapping(value={"storeServiciosProveedor/{paramIdProveedor}","storeServiciosProveedor/{paramIdProveedor}/"}, method =  RequestMethod.POST)
	public @ResponseBody String storeServicios(
		@PathVariable("paramIdProveedor") int idProveedor,
		@RequestParam("nuevo") int idServicios[]
	){
		Boolean respuesta=false;
        String titulo="Oops!";
        String mensaje="Ocurrio un error al intentar guardar los Servicios";
		try {
			for (int i : idServicios) {
				ActivoServicioProveedorEntity objServicio = new ActivoServicioProveedorEntity();
				objServicio.setActivoProveedor(proveedorService.findByIdProveedor(idProveedor));
				objServicio.setActivoServicio(servicioService.findByIdServicio(i));
				servicioProveedorService.createServicio(objServicio);
			}

			respuesta=true;
			titulo="Guardado!";
			mensaje="Los servicios se han relacionado exitosamente con el proveedor";
		}
		catch (Exception err) {
			respuesta = false;
		}
		JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
		jsonReturn	.add("respuesta", respuesta)
					.add("titulo", titulo)
					.add("mensaje", mensaje);							
		return jsonReturn.build().toString();	
	}
}