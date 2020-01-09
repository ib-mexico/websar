package com.ibmexico.controllers;

import java.math.BigDecimal;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import com.ibmexico.components.ModelAndViewComponent;
import com.ibmexico.entities.ActivoServicioEntity;
import com.ibmexico.entities.CatalogoActivoEntity;
import com.ibmexico.libraries.DataTable;
import com.ibmexico.libraries.Templates;
import com.ibmexico.libraries.notifications.ApplicationException;
import com.ibmexico.libraries.notifications.EnumException;
import com.ibmexico.repositories.ICatalogoActivoRepository;
import com.ibmexico.services.ActivoServicioService;
import com.ibmexico.services.CatalogoActivoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("controlPanel/activoServicio")
public class ActivoServicioController{

    @Autowired
    @Qualifier("modelAndViewComponent")
    private ModelAndViewComponent modelAndViewComponent;

    @Autowired
    @Qualifier("activo_servicio")
    private ActivoServicioService servicioService;

    @Autowired
    @Qualifier("catalogoActivoRepository")
    private ICatalogoActivoRepository catActivoRep;

    @Autowired
    @Qualifier("catalogoActivoService")
    private CatalogoActivoService catActivoService;

    @RequestMapping(value={"","/"},method = RequestMethod.GET)
    public ModelAndView index() {
        ModelAndView modelAndView = modelAndViewComponent.createModelAndViewControlPanel(Templates.CAT_ACTIVO_SERVICIO);
        return modelAndView;
    }

    /**Obtener en JSON, los datos iniciales, principalmente para los selects */
    @RequestMapping(value={"getCatalogoActivo","getCatalogoActivo/"})
    public @ResponseBody String getDataCatalogoActivo(){
        Boolean respuesta = false;
        JsonObject jsonCatalogoActivo = null;
        try {
            jsonCatalogoActivo = catActivoService.jsonCatalogoActivo();
            respuesta = true;
        } catch (ApplicationException exception) {
            respuesta = false;
        }
        JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
        jsonReturn.add("respuesta", respuesta).add("jsonCatalogoActivo", jsonCatalogoActivo);
        return jsonReturn.build().toString();
    }

    //Obtener un Servicio para editar   
    @RequestMapping(value = { "{idServicio}/editar", "{idActivo}/editar/" }, method = RequestMethod.GET)
    public @ResponseBody String getDataServicio(@PathVariable(name = "idServicio") int idServicio) {
        Boolean respuesta = false;
        JsonObject jsonServicio = null;
        try {
            jsonServicio = servicioService.jsonIdServicio(idServicio);
            respuesta = true;
        } catch (Exception e) {
            throw new ApplicationException(EnumException.ACTIVO_SERVICIO_CREATE_001);
        }
        JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
        jsonReturn.add("respuesta", respuesta).add("jsonServicio", jsonServicio);
        return jsonReturn.build().toString();
    }


    @RequestMapping(value={"store/servicio","store/servicio/"},method = RequestMethod.POST)
    public @ResponseBody String storeServicio(
        @RequestParam(value = "cmbTipoActivo", required = true) Integer cmbCatalogo,
        @RequestParam(value = "txtDescripcion", required = true) String txtDescripcion,
        @RequestParam(value = "txtPrecioEstimado", required = false) BigDecimal txtPrecioEstimado
    ){
        Boolean respuesta = false;
		String titulo = "Oops!";
        String mensaje = "Ocurrió un error al crear un Servicio en el sistema.";
        
        try {
            ActivoServicioEntity objServicio = new ActivoServicioEntity();
            CatalogoActivoEntity  objCatActivo = catActivoRep.findByIdCatalogoActivo(cmbCatalogo);
            objServicio.setDescripcion(txtDescripcion);
            objServicio.setPrecioEstimado(txtPrecioEstimado);
            objServicio.setTipoActivo(objCatActivo);
            servicioService.create(objServicio);

            respuesta = true;
			titulo = "Excelente!";
            mensaje = "Nuevo servicio exitosamente creado.";
        } catch (Exception e) {
            throw new ApplicationException(EnumException.ACTIVO_SERVICIO_CREATE_001);
        }

        JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
		jsonReturn	.add("respuesta", respuesta)
					.add("titulo", titulo)
					.add("mensaje", mensaje);
		return jsonReturn.build().toString();
    }

    // Para la  tabla de Activos presentes.
	@RequestMapping(value = "/tableService", method = RequestMethod.POST)	
	public @ResponseBody String table(	@RequestParam("offset") int offset,
		@RequestParam("limit") int limit,
		@RequestParam("_csrf") String _csrf,
		@RequestParam(value="search", required=false, defaultValue="") String search,
		@RequestParam(value="txtTableDesde", required=false) String txtTableDesde,
		@RequestParam(value="txtTableHasta", required=false) String txtTableHasta) {
           
            DataTable<ActivoServicioEntity> dtServicio = servicioService.dataTable(search, offset, limit, txtTableDesde, txtTableHasta);

            JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
		    jsonReturn.add("total", dtServicio.getTotal());
            JsonArrayBuilder jsonRows = Json.createArrayBuilder();
            
            dtServicio.getRows().forEach((itemServicio)->{
                jsonRows.add(Json.createObjectBuilder()
                .add("idServicio", itemServicio.getIdServicioActivo())
                .add("creacionFecha", itemServicio.getCreacionFechaNatural())
                .add("descripcion", itemServicio.getDescripcion())
                .add("precioEstimado", itemServicio.getPrecioEstimadoNatural())
                .add("idCatalogo", itemServicio.getTipoActivo().getIdCatalogoActivo())
                .add("catalogoNombre", itemServicio.getTipoActivo().getNombre())
                .add("catalogoClave", itemServicio.getTipoActivo().getClave())
                );
            });

    	jsonReturn.add("rows", jsonRows);	
		return jsonReturn.build().toString();
    }


    //Eliminar servicio, cambiando el estatus de eliminado a verdadero en BD
    @RequestMapping(value = {"{idServicio}/delete","{idServicio}/delete/"} ,method = RequestMethod.POST)
    public @ResponseBody String delete(@PathVariable(name = "idServicio")int idServicio){
        Boolean respuesta = false;
        String titulo = "Oops!";
        String mensaje = "Ocurrió un error al intentar eliminar un Servicio.";
        ActivoServicioEntity objServicio =  servicioService.findByIdServicio(idServicio);
        try {
            if(objServicio != null){
                objServicio.setEliminado(true);
                servicioService.update(objServicio);
                respuesta = true;
                titulo = "Eliminado!";
                mensaje = "El Servicio ha sido eliminado exitosamente.";
            }else{
                throw new ApplicationException(EnumException.ACTIVO_SERVICIO_DELETE_001);
            }
        } catch (ApplicationException e) {
            throw new ApplicationException(EnumException.ACTIVO_SERVICIO_DELETE_001);
        }
        JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
        jsonReturn	.add("respuesta", respuesta)
                    .add("titulo", titulo)
                    .add("mensaje", mensaje);							
        return jsonReturn.build().toString();
       }

    @RequestMapping(value={"update/servicio/{idServicio}","update/servicio/{idServicio}/"},method = RequestMethod.POST)
    public @ResponseBody String updateServicio(
        @PathVariable(name="idServicio", required = true) int idServicio,
        @RequestParam(value = "cmbTipoActivo", required = true) Integer cmbCatalogo,
        @RequestParam(value = "txtDescripcion", required = true) String txtDescripcion,
        @RequestParam(value = "txtPrecioEstimado", required = false) BigDecimal txtPrecioEstimado
    ){
        Boolean respuesta = false;
		String titulo = "Oops!";
        String mensaje = "Ocurrió un error al modificar un Servicio en el sistema.";
        
        try {
            ActivoServicioEntity objServicio = servicioService.findByIdServicio(idServicio);
            CatalogoActivoEntity  objCatActivo = catActivoRep.findByIdCatalogoActivo(cmbCatalogo);
            objServicio.setDescripcion(txtDescripcion);
            objServicio.setPrecioEstimado(txtPrecioEstimado);
            objServicio.setTipoActivo(objCatActivo);
            servicioService.create(objServicio);

            respuesta = true;
			titulo = "Excelente!";
            mensaje = "Nuevo servicio exitosamente creado.";
        } catch (Exception e) {
            throw new ApplicationException(EnumException.ACTIVO_SERVICIO_CREATE_001);
        }

        JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
		jsonReturn	.add("respuesta", respuesta)
					.add("titulo", titulo)
					.add("mensaje", mensaje);
		return jsonReturn.build().toString();
    }

}