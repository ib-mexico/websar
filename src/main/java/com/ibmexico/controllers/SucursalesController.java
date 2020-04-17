package com.ibmexico.controllers;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
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

import com.ibmexico.components.ModelAndViewComponent;
import com.ibmexico.entities.SucursalEntity;
import com.ibmexico.libraries.DataTable;
import com.ibmexico.libraries.Templates;
import com.ibmexico.libraries.notifications.ApplicationException;
import com.ibmexico.libraries.notifications.EnumException;
import com.ibmexico.services.EmpresaService;
import com.ibmexico.services.SessionService;
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

	@Autowired
	@Qualifier("sessionService")
	private SessionService sessionService;

	@Autowired
	@Qualifier("empresaService")
	private EmpresaService empresaService;

	@GetMapping({ "", "/" })
	public ModelAndView index() {
		ModelAndView objModelAndView = modelAndViewComponent
				.createModelAndViewControlPanel(Templates.CONTROL_PANEL_SUCURSALES_INDEX);
		objModelAndView.addObject("rolNuevaSucursal", sessionService.hasRol("SUCURSALES_CREATE"));
		return objModelAndView;
	}

	@RequestMapping(value = "/table", method = RequestMethod.POST)
	public @ResponseBody String table(@RequestParam("offset") int offset, @RequestParam("limit") int limit,
			@RequestParam("_csrf") String _csrf,
			@RequestParam(value = "search", required = false, defaultValue = "") String search,
			@RequestParam(value = "txtBootstrapTableDesde", required = false) String txtBootstrapTableDesde,
			@RequestParam(value = "txtBootstrapTableHasta", required = false) String txtBootstrapTableHasta) {

		DataTable<SucursalEntity> dtSucursales = sucursalService.dataTable(search, offset, limit,
				txtBootstrapTableDesde, txtBootstrapTableHasta);

		JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
		jsonReturn.add("total", dtSucursales.getTotal());
		JsonArrayBuilder jsonRows = Json.createArrayBuilder();

		dtSucursales.getRows().forEach((itemSucursal) -> {

			jsonRows.add(Json.createObjectBuilder().add("idSucursal", itemSucursal.getIdSucursal())
					.add("empresa", itemSucursal.getEmpresa() == null ? "" : itemSucursal.getEmpresa().getEmpresa())
					.add("sucursal", itemSucursal.getSucursal()).add("domicilio", itemSucursal.getDomicilio())
					.add("colonia", itemSucursal.getColonia()).add("municipio", itemSucursal.getMunicipio())
					.add("ciudad", itemSucursal.getCiudad()).add("estado", itemSucursal.getEstado())
					.add("codigoPostal", itemSucursal.getCodigoPostal()).add("telefono", itemSucursal.getTelefono())
					.add("eliminado", itemSucursal.isEliminado()));
		});
		jsonReturn.add("rows", jsonRows);

		return jsonReturn.build().toString();
	}

	/* Recursos para registro de sucursales Obtener los datos necesarios para el select*/
	@RequestMapping(value = "getEmpresa", method = RequestMethod.GET)
	public @ResponseBody String getEmpresa() {
		Boolean respuesta = false;
		JsonObject jsonEmpresa = null;
		try {
			jsonEmpresa = empresaService.jsonEmpresas();

			respuesta = true;
		} catch (Exception exception) {
			exception.getMessage();
		}
		JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
		jsonReturn.add("respuesta", respuesta).add("jsonEmpresa", jsonEmpresa);
		return jsonReturn.build().toString();
	}

	@RequestMapping(value = "storeSucursal", method = RequestMethod.POST)
	public @ResponseBody String storeSucursal(@RequestParam(value = "cmbEmpresa", required = false) Integer cmbEmpresa,
			@RequestParam(value = "txtNombreSucursal", required = true) String txtNombreSucursal,
			@RequestParam(value = "txtCodigoPostal", required = false) String txtCodigoPostal,
			@RequestParam(value = "txtTelefono") String txtTelefono,
			@RequestParam(value = "txtDomicilioSucursal") String txtDomicilioSucursal,
			@RequestParam(value = "txtColoniaSucursal") String txtColoniaSucursal,
			@RequestParam(value = "txtMunicipio") String txtMunicipio,
			@RequestParam(value = "txtCiudad") String txtCiudad, @RequestParam(value = "txtEstado") String txtEstado) {
		Boolean respuesta = false;
		String titulo = "Oops!";
		String mensaje = "Ocurrió un error al registrar la nueva Sucursal.";

		SucursalEntity objSucursal = new SucursalEntity();
		try {
			objSucursal.setEmpresa(empresaService.findByIdEmpresa(cmbEmpresa));
			objSucursal.setSucursal(txtNombreSucursal);
			objSucursal.setCodigoPostal(txtCodigoPostal);
			objSucursal.setTelefono(txtTelefono);
			objSucursal.setDomicilio(txtDomicilioSucursal);
			objSucursal.setColonia(txtColoniaSucursal);
			objSucursal.setMunicipio(txtMunicipio);
			objSucursal.setCiudad(txtCiudad);
			objSucursal.setEstado(txtEstado);
			sucursalService.save(objSucursal);

			respuesta = true;
			titulo = "Excelente!";
			mensaje = "Nuevo sucursal registrada exitosamente.";

		} catch (ApplicationException exception) {
			throw new ApplicationException(EnumException.SUCURSAL_CREATE_001);
		}

		JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
		jsonReturn.add("respuesta", respuesta).add("titulo", titulo).add("mensaje", mensaje);
		return jsonReturn.build().toString();
	}

	/** Obtener la data de la empresa para editar */
	@RequestMapping(value = "getSucursal/{idSucursal}", method = RequestMethod.GET)
	public @ResponseBody String editSucursal(@PathVariable("idSucursal") int idSucursal) {
		Boolean respuesta = false;
		JsonObject jsonSucursal = null;
		try {
			jsonSucursal = sucursalService.jsonSucursalById(idSucursal);
			respuesta = true;
		} catch (Exception e) {
			throw e;
		}
		JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
		jsonReturn.add("respuesta", respuesta).add("jsonSucursal", jsonSucursal);
		return jsonReturn.build().toString();
	}

	/* Actualizar datos de una sucursal */
	@RequestMapping(value = {"{idSucursal}/updateSucursal","{idSucursal}/updateSucursal/"}, method = RequestMethod.POST)
	public @ResponseBody String updateSucursal(
			@PathVariable(value="idSucursal") int idSucursal,
			@RequestParam(value = "cmbEmpresa", required = false) Integer cmbEmpresa,
			@RequestParam(value = "txtNombreSucursal", required = true) String txtNombreSucursal,
			@RequestParam(value = "txtCodigoPostal", required = false) String txtCodigoPostal,
			@RequestParam(value = "txtTelefono") String txtTelefono,
			@RequestParam(value = "txtDomicilioSucursal") String txtDomicilioSucursal,
			@RequestParam(value = "txtColoniaSucursal") String txtColoniaSucursal,
			@RequestParam(value = "txtMunicipio") String txtMunicipio,
			@RequestParam(value = "txtCiudad") String txtCiudad, @RequestParam(value = "txtEstado") String txtEstado) {
		Boolean respuesta = false;
		String titulo = "Oops!";
		String mensaje = "Ocurrió un error al modificar la Sucursal.";

		SucursalEntity objSucursal = sucursalService.findByIdSucursal(idSucursal);
		try {
			objSucursal.setEmpresa(empresaService.findByIdEmpresa(cmbEmpresa));
			objSucursal.setSucursal(txtNombreSucursal);
			objSucursal.setCodigoPostal(txtCodigoPostal);
			objSucursal.setTelefono(txtTelefono);
			objSucursal.setDomicilio(txtDomicilioSucursal);
			objSucursal.setColonia(txtColoniaSucursal);
			objSucursal.setMunicipio(txtMunicipio);
			objSucursal.setCiudad(txtCiudad);
			objSucursal.setEstado(txtEstado);
			sucursalService.save(objSucursal);

			respuesta = true;
			titulo = "Excelente!";
			mensaje = "Sucursal actualizada exitosamente.";

		} catch (ApplicationException exception) {
			throw new ApplicationException(EnumException.SUCURSAL_CREATE_001);
		}

		JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
		jsonReturn.add("respuesta", respuesta).add("titulo", titulo).add("mensaje", mensaje);
		return jsonReturn.build().toString();
	}

	/* Eliminar sucursal */
	@RequestMapping(value={"{idSucursal}/eliminar","{idSucursal}/eliminar/"}, method = RequestMethod.POST)
    public @ResponseBody String deleteEmpresa(@PathVariable(name = "idSucursal")int idSucursal){
        Boolean respuesta=false;
        String titulo = "Oops!";
        String mensaje = "Ocurrio un error al intentar eliminar la sucursal";
        SucursalEntity objSucursal= sucursalService.findByIdSucursal(idSucursal);
        try {
            if(objSucursal != null){
                objSucursal.setEliminado(true);
                sucursalService.update(objSucursal);
                respuesta=true;
                titulo = "Eliminado!";
                mensaje = "El sucursal "+objSucursal.getSucursal() +" ha sido eliminado exitosamente.";
            }else{
                throw new ApplicationException(EnumException.SUCURSAL_DELETE_001);
            }
        } catch (Exception e) {
            throw new ApplicationException(EnumException.SUCURSAL_DELETE_001);
        }
        JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
		jsonReturn	.add("respuesta", respuesta)
					.add("titulo", titulo)
					.add("mensaje", mensaje);							
		return jsonReturn.build().toString();
    } 
}
