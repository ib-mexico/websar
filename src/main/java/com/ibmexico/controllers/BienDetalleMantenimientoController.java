package com.ibmexico.controllers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import com.ibmexico.components.ModelAndViewComponent;
import com.ibmexico.configurations.GeneralConfiguration;
import com.ibmexico.entities.BienDetalleMantenimientoEntity;
import com.ibmexico.libraries.DataTable;
import com.ibmexico.libraries.Templates;
import com.ibmexico.libraries.notifications.ApplicationException;
import com.ibmexico.libraries.notifications.EnumException;
import com.ibmexico.services.ActivoEstatusService;
import com.ibmexico.services.ActivoServicioProveedorMantService;
import com.ibmexico.services.ActivoServicioProveedorService;
import com.ibmexico.services.ActivoServicioService;
import com.ibmexico.services.BienActivoFicheroService;
import com.ibmexico.services.BienActivoService;
import com.ibmexico.services.BienDetalleMantenimientoService;
import com.ibmexico.services.CatalogoActivoService;
import com.ibmexico.services.SessionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("controlPanel/DetalleMant")
public class BienDetalleMantenimientoController{

    @Autowired
    @Qualifier("sessionService")
    private SessionService sesionService;

    @Autowired
    @Qualifier("bienActivoService")
    private BienActivoService bienActivoService;

    @Autowired
    @Qualifier("catalogoActivoService")
    private CatalogoActivoService catActivoService;

    @Autowired
    @Qualifier("bienActivoFicheroService")
    private BienActivoFicheroService ficheroActivoService;

    @Autowired
    @Qualifier("bienDetalleMantService")
    private BienDetalleMantenimientoService bienDetMantService;

    @Autowired
    @Qualifier("activo_servicio")
    private ActivoServicioService activoServicioService;
    
    @Autowired
    @Qualifier("modelAndViewComponent")
    private ModelAndViewComponent modelAndViewComponent;

    //Para servicio proveedor
    @Autowired
    @Qualifier("activo_servicio_proveedor")
    private ActivoServicioProveedorService servicioPservice;

    //Para sevicio_proveedor_manto
    @Autowired
    @Qualifier("activo_servicio_proveedor_mant_service")
    private ActivoServicioProveedorMantService servProMantoservice;

    @Autowired
    @Qualifier("activo_estatus_service")
    private ActivoEstatusService activoestatus;

    @RequestMapping({ "", "/" })
    public ModelAndView index() {
        ModelAndView modelAndView = modelAndViewComponent.createModelAndViewControlPanel(Templates.DETALLE_INDEX);
        return modelAndView;
    }

    //Obtener los datos necesarios para el select
    @RequestMapping(value="get-recurso-data-form", method = RequestMethod.GET)
    public @ResponseBody String getDataRecursoForm(){
        Boolean respuesta=false;
        JsonObject jsonCatalogoActivo=null;
        JsonObject jsonRecursoActivo=null;
        JsonObject jsonRecursoFichero=null;
        try {
            jsonCatalogoActivo= catActivoService.jsonCatalogoActivo();
            jsonRecursoActivo = bienActivoService.jsonRecursoActivo();
            jsonRecursoFichero = ficheroActivoService.jsonRecursoFichero();
            respuesta=true;
        } catch (ApplicationException exception) { 
        }
        JsonObjectBuilder jsonReturn=Json.createObjectBuilder();
        jsonReturn.add("respuesta", respuesta)
                    .add("jsonCatalogoActivo", jsonCatalogoActivo)
                    .add("jsonRecursoActivo", jsonRecursoActivo)
                    .add("jsonRecursoFichero", jsonRecursoFichero);
                    return jsonReturn.build().toString();
    }

    //esta obtiene el fichero necesario
    @RequestMapping(value="get-fichero/{idActivoFijo}", method = RequestMethod.GET)
    public @ResponseBody String getFicheroIdActivoFijo(@PathVariable("idActivoFijo") int idActivoFijo){
        Boolean respuesta=false;
        JsonObject jsonFichero=null;
        try {
            jsonFichero=ficheroActivoService.JsonRecursoFichaIdActivo(idActivoFijo);
            respuesta=true;
        } catch (ApplicationException exception) {
        }
        JsonObjectBuilder jsonReturn= Json.createObjectBuilder();
        jsonReturn.add("respuesta",respuesta).
        add("jsonFichero", jsonFichero);
        return jsonReturn.build().toString();
    }

    //Consultar proveedor por Servicio
    @RequestMapping(value="get-proveedor/{idTipoActivo}/{idServicio}", method = RequestMethod.GET)
    public @ResponseBody String getProveedorServicio(@PathVariable("idTipoActivo")int idTipoActivo,
                                                    @PathVariable("idServicio") int idServicio){
        JsonObject jsonServicioProveedor=null;
        try {        
            jsonServicioProveedor=servicioPservice.jsonlstServicioProveedor(idTipoActivo, idServicio);
        } catch (ApplicationException exception) {
           
        }
        JsonObjectBuilder jsonReturn= Json.createObjectBuilder();
        jsonReturn.add("jsonServicioProveedor", jsonServicioProveedor);
        return jsonReturn.build().toString();
    }

   //esta obtiene el servicio necesario
   @RequestMapping(value="get-servicio/{idTipoActivo}", method = RequestMethod.GET)
   public @ResponseBody String getServicioIdTipo(@PathVariable("idTipoActivo") int idTipoActivo){
       Boolean respuesta=false;
       JsonObject jsonServicioTipoActivo=null;
       try {
           jsonServicioTipoActivo=activoServicioService.jsonServicioTipoActivo(idTipoActivo);
           respuesta=true;
       } catch (ApplicationException exception) {
       }
       JsonObjectBuilder jsonReturn= Json.createObjectBuilder();
       jsonReturn.add("respuesta",respuesta).
       add("jsonServicio", jsonServicioTipoActivo);
       return jsonReturn.build().toString();
   }
   /**Obtener todos los datos relacionados para la edicion de manto. para ejecutar dicho methodo update. */
   @RequestMapping(value="get-servicio-proveedor/{idBienDetalleManto}", method = RequestMethod.GET)
   public @ResponseBody String getServicioProveedorManto(@PathVariable("idBienDetalleManto")int idBienDetalleManto){
       Boolean respuesta=false;
       JsonObject jsonBienDetalleManto=null;
       JsonObject jsonServicioProveedorManto=null;
       JsonObject jsonActivoCatalogo = null;

       BienDetalleMantenimientoEntity objdetallemanto=bienDetMantService.findByIdBienDetalleMantenimiento(idBienDetalleManto);
       try {
           jsonBienDetalleManto=bienDetMantService.jsonBienDetalleMantId(idBienDetalleManto);
           jsonServicioProveedorManto=servProMantoservice.jsonActivoServicioProveedorMant(idBienDetalleManto);
           jsonActivoCatalogo = bienActivoService.jsonRecursoActivoIdCatalogo(objdetallemanto.getBienActivo().
           getIdActivo().getIdCatalogoActivo());
           respuesta=true;
       } catch (Exception e) {
           throw e;
       }
       JsonObjectBuilder jsonReturn=Json.createObjectBuilder();
       jsonReturn.add("respuesta", respuesta).
       add("jsonBienDetalleManto", jsonBienDetalleManto).
       add("jsonServicioProveedorManto", jsonServicioProveedorManto)
       .add("jsonActivoCatalogo", jsonActivoCatalogo)
       ;
       return jsonReturn.build().toString();
   }

    // Datatable
    @RequestMapping(value = "/table", method = RequestMethod.POST)
    private @ResponseBody String table( @RequestParam("offset") int offset,
        @RequestParam("limit")int limit,
        @RequestParam("_csrf") String _csrf,
        @RequestParam(value = "search", required = false, defaultValue = "") String search,
        @RequestParam(value = "txtBootstrapTableDesde", required = false) String txtBootstrapTableDesde,
        @RequestParam(value="txtBootstrapTableHasta", required = false) String txtBootstrapTableHasta){
            
            DataTable<BienDetalleMantenimientoEntity> dtDetalleMant=bienDetMantService.dataTable(search, offset, limit, txtBootstrapTableDesde, txtBootstrapTableHasta);

            JsonObjectBuilder jsonReturn=Json.createObjectBuilder();
            jsonReturn.add("total", dtDetalleMant.getTotal());
            JsonArrayBuilder jsRows=Json.createArrayBuilder();
            BigDecimal d = BigDecimal.ZERO ;
            dtDetalleMant.getRows().forEach((itemDetalle)->{
                jsRows.add(Json.createObjectBuilder()
                .add("idDetalleMant", itemDetalle.getIdDetalleMantenimiento())
                .add("creacion_fecha", itemDetalle.getCreacionFechaNatural())
                .add("diagnostico", itemDetalle.getObservaciones())
                .add("fecha_mantenimiento", itemDetalle.getFechaMantenimientoProgramadaFechaNatural())
                .add("estatus_recordatorio", itemDetalle.isEstatus_recordatorio())
                .add("bienActivo", itemDetalle.getBienActivo().getNumeroEconomico())
                .add("gasto_aproximado", itemDetalle.getGastoAproximado()!=null ? itemDetalle.getGastoAproximado() : d)
                .add("finalizado",itemDetalle.isFinalizado())
                );
            });
            jsonReturn.add("rows", jsRows);
            return jsonReturn.build().toString();
    }


    @RequestMapping(value="storeAjaxServicioProveeedor", method=RequestMethod.POST)
    public @ResponseBody String storeCotizacionProveedor(
        @RequestParam(value="cmbRecurso")Integer cmbRecurso,
        @RequestParam(value="txtObservaciones", required = false) String txtObservaciones,
        @RequestParam(value="txtFechaProgramada",required = false)String txtFechaProgramada,
        @RequestParam(value="txtTotalgasto", required = true) BigDecimal txtTotalgasto,
        @RequestParam(value="txtIdServicioProveedor", required = false) int[] txtIdServicioProveedor,
        @RequestParam(value = "txtPrecio",required = false) BigDecimal[] txtPrecio,
        @RequestParam(value="txtObserProv",required = false) String[] txtObserProv,
        @RequestParam(value = "ficheroCotizacion",required = false) MultipartFile [] ficheroCotizacion,
        @RequestParam(value="imgDetalleActivo") String imgDetalleActivo    
        ) {
            System.err.println(imgDetalleActivo +"img detalle activo");
            System.err.println(txtTotalgasto +"totalgasto");
            Boolean respuesta = false;
            String titulo = "Oops!";
            String mensaje = "Ocurrió un error al intentar mandar a mantenimiento el Activo.";
            BienDetalleMantenimientoEntity objDetalleMant= new BienDetalleMantenimientoEntity();            
            try {
                // for (int i = 0; i < txtIdServicioProveedor.length; i++) {
                //     System.err.println(txtIdServicioProveedor[i]+" -"+ txtPrecio[i] +" - "+txtObserProv[i] + " fichero ->" + ficheroCotizacion[i] );
                // }
                objDetalleMant.setObservaciones(txtObservaciones);
                if (txtTotalgasto!=null) {
                    objDetalleMant.setGastoAproximado(txtTotalgasto);                    
                }
                objDetalleMant.setBienActivo(bienActivoService.findByIdRecursoActive(cmbRecurso));
                objDetalleMant.setFechaMantenimientoProgramada(LocalDate.parse(txtFechaProgramada, GeneralConfiguration.getInstance().getDateFormatterNatural()));
                objDetalleMant.setActivoEstatus(activoestatus.findByIdActivoEstatus(1));
                objDetalleMant.setFinalizado(false);
                objDetalleMant.setEstatus_recordatorio(false);
                bienDetMantService.create(objDetalleMant, txtObserProv, txtPrecio, txtIdServicioProveedor, ficheroCotizacion, imgDetalleActivo);
                respuesta = true;
                titulo = "Excelente!";
                mensaje = "Nuevo activo exitosamente creado.";
            } catch (Exception e) {
                throw new ApplicationException(EnumException.ACTIVO_CREATE_001);
            }

            JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
            jsonReturn	.add("respuesta", respuesta)
                        .add("titulo", titulo)
                        .add("mensaje", mensaje);
            return jsonReturn.build().toString();
    }

    /**Update data manto */
    @RequestMapping(value={"{idDetalleManto}/update","{idDetalleManto}/update/"}, method = RequestMethod.POST)
    public @ResponseBody String UpdateDataManto(
        @RequestParam(value="cmbRecurso",required = false)Integer cmbRecurso,
        @RequestParam(value="txtObservaciones", required = false) String txtObservaciones,
        @RequestParam(value="txtFechaProgramada",required = false)String txtFechaProgramada,
        @RequestParam(value="txtTotalgasto", required = false) BigDecimal txtTotalgasto,
        @RequestParam(value="txtIdServicioProveedor", required = false) int[] txtIdServicioProveedor,
        @RequestParam(value = "txtPrecio",required = false) BigDecimal[] txtPrecio,
        @RequestParam(value="txtObserProv",required = false) String[] txtObserProv,
        @RequestParam(value = "ficheroCotizacion",required = false) MultipartFile [] ficheroCotizacion,
        @RequestParam(value="imgDetalleActivo", required = false) String imgDetalleActivo,
        @PathVariable(name="idDetalleManto")int idDetalleManto
    ){
        BienDetalleMantenimientoEntity objDetalleManto=bienDetMantService.findByIdBienDetalleMantenimiento(idDetalleManto);
        Boolean respuesta = false;
        String titulo = "Oops!";
        String mensaje = "Ocurrió un error en la edición.";
        System.err.println(txtObservaciones);
        System.err.println(txtFechaProgramada);
        System.err.println(cmbRecurso);
        System.err.println(objDetalleManto);
        // for (int i = 0; i < txtIdServicioProveedor.length; i++) {
        //     System.err.println("entrando al ciclo de for");
        //     System.err.println(txtIdServicioProveedor[i]+" -"+ txtPrecio[i] +" - "+txtObserProv[i] + " fichero ->" + ficheroCotizacion[i] );
        // }
        System.err.println("saliendo al ciclo for");
        try {
            if (objDetalleManto!=null) {
                objDetalleManto.setObservaciones(txtObservaciones);
                objDetalleManto.setBienActivo(bienActivoService.findByIdRecursoActive(cmbRecurso));
                objDetalleManto.setFechaMantenimientoProgramada(LocalDate.parse(txtFechaProgramada, GeneralConfiguration.getInstance().getDateFormatterNatural()));
                if(txtTotalgasto!=null){
                    objDetalleManto.setGastoAproximado(txtTotalgasto);
                }

                // int idmanto=objDetalleManto.getIdDetalleMantenimiento();
                bienDetMantService.create(objDetalleManto, txtObserProv, txtPrecio, txtIdServicioProveedor, ficheroCotizacion, imgDetalleActivo);
                respuesta = true;
                titulo = "Genial!";
                mensaje = "Edición exitosa.";
            }
 
        } catch (Exception e) {
            throw new ApplicationException(EnumException.ACTIVO_CREATE_001);
        }
        JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
		jsonReturn	.add("respuesta", respuesta)
					.add("titulo", titulo)
					.add("mensaje", mensaje);							
		return jsonReturn.build().toString();
    }
}