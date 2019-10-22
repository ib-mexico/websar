package com.ibmexico.controllers;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import com.ibmexico.components.ModelAndViewComponent;
import com.ibmexico.configurations.GeneralConfiguration;
import com.ibmexico.entities.ActivoEstatusEntity;
import com.ibmexico.entities.BienActivoEntity;
import com.ibmexico.entities.BienDetalleMantenimientoEntity;
import com.ibmexico.libraries.DataTable;
import com.ibmexico.libraries.Templates;
import com.ibmexico.libraries.notifications.ApplicationException;
import com.ibmexico.libraries.notifications.EnumException;
import com.ibmexico.repositories.IBienActivoRepository;
import com.ibmexico.repositories.IBienDetalleMantenimientoRepository;
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
public class BienDetalleMantenimientoController {

    @Autowired
    @Qualifier("sessionService")
    private SessionService sesionService;

    @Autowired
    @Qualifier("bienActivoService")
    private BienActivoService bienActivoService;

    @Autowired
    @Qualifier("bienActivoRepository")
    private IBienActivoRepository bienActivoRepo;

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
    @Qualifier("bienDetalleMantenimientoRepository")
    private IBienDetalleMantenimientoRepository bienDetMantRepository;

    @Autowired
    @Qualifier("activo_servicio")
    private ActivoServicioService activoServicioService;

    @Autowired
    @Qualifier("modelAndViewComponent")
    private ModelAndViewComponent modelAndViewComponent;

    // Para servicio proveedor
    @Autowired
    @Qualifier("activo_servicio_proveedor")
    private ActivoServicioProveedorService servicioPservice;

    // Para sevicio_proveedor_manto
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

    @RequestMapping({ "/validacion" })
    public ModelAndView validacion() {
        ModelAndView modelAndView = modelAndViewComponent.createModelAndViewControlPanel(Templates.VALIDATION_MANTO);
        return modelAndView;
    }
    // Obtener los datos necesarios para el select
    @RequestMapping(value = "get-recurso-data-form", method = RequestMethod.GET)
    public @ResponseBody String getDataRecursoForm() {
        Boolean respuesta = false;
        JsonObject jsonCatalogoActivo = null;
        JsonObject jsonRecursoActivo = null;
        JsonObject jsonRecursoFichero = null;
        try {
            jsonCatalogoActivo = catActivoService.jsonCatalogoActivo();
            jsonRecursoActivo = bienActivoService.jsonRecursoActivo();
            jsonRecursoFichero = ficheroActivoService.jsonRecursoFichero();
            respuesta = true;
        } catch (ApplicationException exception) {
        }
        JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
        jsonReturn.add("respuesta", respuesta).add("jsonCatalogoActivo", jsonCatalogoActivo)
                .add("jsonRecursoActivo", jsonRecursoActivo).add("jsonRecursoFichero", jsonRecursoFichero);
        return jsonReturn.build().toString();
    }

    // esta obtiene el fichero necesario
    @RequestMapping(value = "get-fichero/{idActivoFijo}", method = RequestMethod.GET)
    public @ResponseBody String getFicheroIdActivoFijo(@PathVariable("idActivoFijo") int idActivoFijo) {
        Boolean respuesta = false;
        JsonObject jsonFichero = null;
        try {
            jsonFichero = ficheroActivoService.JsonRecursoFichaIdActivo(idActivoFijo);
            respuesta = true;
        } catch (ApplicationException exception) {
        }
        JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
        jsonReturn.add("respuesta", respuesta).add("jsonFichero", jsonFichero);
        return jsonReturn.build().toString();
    }

    // Consultar proveedor por Servicio
    @RequestMapping(value = "get-proveedor/{idTipoActivo}/{idServicio}", method = RequestMethod.GET)
    public @ResponseBody String getProveedorServicio(@PathVariable("idTipoActivo") int idTipoActivo,
            @PathVariable("idServicio") int idServicio) {
        JsonObject jsonServicioProveedor = null;
        try {
            jsonServicioProveedor = servicioPservice.jsonlstServicioProveedor(idTipoActivo, idServicio);
        } catch (ApplicationException exception) {

        }
        JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
        jsonReturn.add("jsonServicioProveedor", jsonServicioProveedor);
        return jsonReturn.build().toString();
    }

    // esta obtiene el servicio necesario
    @RequestMapping(value = "get-servicio/{idTipoActivo}", method = RequestMethod.GET)
    public @ResponseBody String getServicioIdTipo(@PathVariable("idTipoActivo") int idTipoActivo) {
        Boolean respuesta = false;
        JsonObject jsonServicioTipoActivo = null;
        try {
            jsonServicioTipoActivo = activoServicioService.jsonServicioTipoActivo(idTipoActivo);
            respuesta = true;
        } catch (ApplicationException exception) {
        }
        JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
        jsonReturn.add("respuesta", respuesta).add("jsonServicio", jsonServicioTipoActivo);
        return jsonReturn.build().toString();
    }

    /**
     * Obtener todos los datos relacionados para la edicion de manto. para ejecutar
     * dicho methodo update.
     */
    @RequestMapping(value = "get-servicio-proveedor/{idBienDetalleManto}", method = RequestMethod.GET)
    public @ResponseBody String getServicioProveedorManto(@PathVariable("idBienDetalleManto") int idBienDetalleManto) {
        Boolean respuesta = false;
        JsonObject jsonBienDetalleManto = null;
        JsonObject jsonServicioProveedorManto = null;
        JsonObject jsonActivoCatalogo = null;
        JsonObject jsonEstatus=null;

        BienDetalleMantenimientoEntity objdetallemanto = bienDetMantService
                .findByIdBienDetalleMantenimiento(idBienDetalleManto);
        try {
            jsonBienDetalleManto = bienDetMantService.jsonBienDetalleMantId(idBienDetalleManto);
            jsonServicioProveedorManto = servProMantoservice.jsonActivoServicioProveedorMant(idBienDetalleManto);
            jsonActivoCatalogo = bienActivoService.jsonRecursoActivoAll(objdetallemanto.getBienActivo().getIdActivo().getIdCatalogoActivo());
            jsonEstatus=activoestatus.jsonEstatus();
            respuesta = true;
        } catch (Exception e) {
            throw e;
        }
        JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
        jsonReturn.add("respuesta", respuesta).add("jsonBienDetalleManto", jsonBienDetalleManto)
                .add("jsonServicioProveedorManto", jsonServicioProveedorManto)
                .add("jsonActivoCatalogo", jsonActivoCatalogo)
                .add("jsonEstatus", jsonEstatus);
        return jsonReturn.build().toString();
    }

    /**
     * Obtener todos los servicios aceptados y posible proceso a pago de dicho
     * servicio
     */
    @RequestMapping(value = "get-servicioAceptado/{idBienDetalleManto}", method = RequestMethod.GET)
    public @ResponseBody String getServicioAceptado(@PathVariable("idBienDetalleManto") int idBienDetalleManto) {
        Boolean respuesta = false;
        JsonObject jsonBienDetalleManto = null;
        JsonObject jsonServicioAceptado = null;
        JsonObject jsonActivoCatalogo = null;

        BienDetalleMantenimientoEntity objdetallemanto = bienDetMantService
                .findByIdBienDetalleMantenimiento(idBienDetalleManto);
        try {
            jsonBienDetalleManto = bienDetMantService.jsonBienDetalleMantId(idBienDetalleManto);
            jsonServicioAceptado = servProMantoservice.jsonServicioProvAceptado(idBienDetalleManto);
            jsonActivoCatalogo = bienActivoService.jsonRecursoActivoIdCatalogo(objdetallemanto.getBienActivo().getIdActivo().getIdCatalogoActivo());
            respuesta = true;
        } catch (Exception e) {
            throw e;
        }
        JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
        jsonReturn.add("respuesta", respuesta).add("jsonBienDetalleManto", jsonBienDetalleManto)
                .add("jsonServicioAceptado", jsonServicioAceptado).add("jsonActivoCatalogo", jsonActivoCatalogo);
        return jsonReturn.build().toString();
    }

    // Datatable
    @RequestMapping(value = "/table", method = RequestMethod.POST)
    private @ResponseBody String table(@RequestParam("offset") int offset, @RequestParam("limit") int limit,
            @RequestParam("_csrf") String _csrf,
            @RequestParam(value = "search", required = false, defaultValue = "") String search,
            @RequestParam(value = "txtBootstrapTableDesde", required = false) String txtBootstrapTableDesde,
            @RequestParam(value = "txtBootstrapTableHasta", required = false) String txtBootstrapTableHasta) {

        DataTable<BienDetalleMantenimientoEntity> dtDetalleMant = bienDetMantService.dataTable(search, offset, limit,
                txtBootstrapTableDesde, txtBootstrapTableHasta);

        JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
        jsonReturn.add("total", dtDetalleMant.getTotal());
        JsonArrayBuilder jsRows = Json.createArrayBuilder();
        BigDecimal d = BigDecimal.ZERO;
        dtDetalleMant.getRows().forEach((itemDetalle) -> {
            jsRows.add(Json.createObjectBuilder().add("idDetalleMant", itemDetalle.getIdDetalleMantenimiento())
                    .add("creacion_fecha", itemDetalle.getCreacionFechaNatural())
                    .add("diagnostico", itemDetalle.getObservaciones())
                    .add("fecha_mantenimiento", itemDetalle.getFechaMantenimientoProgramadaFechaNatural())
                    .add("estatus_recordatorio", itemDetalle.isEstatus_recordatorio())
                    .add("bienActivo", itemDetalle.getBienActivo().getNumeroEconomico())
                    .add("gasto_aproximado",
                            itemDetalle.getGastoAproximado() != null ? itemDetalle.getGastoAproximado() : d)
                    .add("finalizado", itemDetalle.isFinalizado())
                    .add("estatusmanto", itemDetalle.getActivoEstatus().getIdActivoEstatus()));
        });
        jsonReturn.add("rows", jsRows);
        return jsonReturn.build().toString();
    }


    /**Datatable for validation */
   // Datatable
    @RequestMapping(value = "/tablevalidation", method = RequestMethod.POST)
    private @ResponseBody String tablevalidation(@RequestParam("offset") int offset, @RequestParam("limit") int limit,
            @RequestParam("_csrf") String _csrf,
            @RequestParam(value = "search", required = false, defaultValue = "") String search,
            @RequestParam(value = "txtBootstrapTableDesde", required = false) String txtBootstrapTableDesde,
            @RequestParam(value = "txtBootstrapTableHasta", required = false) String txtBootstrapTableHasta) {

        DataTable<BienDetalleMantenimientoEntity> dtDetalleMant = bienDetMantService.dataTableValidation(search, offset, limit,
                txtBootstrapTableDesde, txtBootstrapTableHasta);

        JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
        jsonReturn.add("total", dtDetalleMant.getTotal());
        JsonArrayBuilder jsRows = Json.createArrayBuilder();
        BigDecimal d = BigDecimal.ZERO;
        dtDetalleMant.getRows().forEach((itemDetalle) -> {
            jsRows.add(Json.createObjectBuilder().add("idDetalleMant", itemDetalle.getIdDetalleMantenimiento())
                    .add("creacion_fecha", itemDetalle.getCreacionFechaNatural())
                    .add("diagnostico", itemDetalle.getObservaciones())
                    .add("fecha_mantenimiento", itemDetalle.getFechaMantenimientoProgramadaFechaNatural())
                    .add("estatus_recordatorio", itemDetalle.isEstatus_recordatorio())
                    .add("bienActivo", itemDetalle.getBienActivo().getNumeroEconomico())
                    .add("gasto_aproximado",
                            itemDetalle.getGastoAproximado() != null ? itemDetalle.getGastoAproximado() : d)
                    .add("finalizado", itemDetalle.isFinalizado())
                    .add("estatusmanto", itemDetalle.getActivoEstatus().getIdActivoEstatus()));
        });
        jsonReturn.add("rows", jsRows);
        return jsonReturn.build().toString();
    }



    @RequestMapping(value = "storePagos", method = RequestMethod.POST)
    public @ResponseBody String storePagos(
            @RequestParam(value = "txtFechaPago", required = false) String[] txtFechaPago,
            @RequestParam(value = "idProvManto", required = false) int[] idProvManto,
            @RequestParam(value = "idBienDetalleManto", required = false) int idBienDetalleManto,
            @RequestParam(value = "ComprobantePago", required = false) MultipartFile[] comprobantePago,
            @RequestParam(value = "cmbRecurso") Integer cmbRecurso) {
        Boolean respuesta = false;
        String titulo = "Oops!";
        String mensaje = "Ocurrió un error al intentar guardar los pagos";
        // for (int i = 0; i < txtFechaPago.length; i++) {
        //     System.err.println(txtFechaPago[i] + "---" + idProvManto[i] + "--");
        // }
        BienActivoEntity objActivo=bienActivoService.findByIdRecursoActive(cmbRecurso);
        try {
            for (int i = 0; i < txtFechaPago.length;i++) {
                servProMantoservice.PagoServicioProveedor(txtFechaPago[i], idProvManto[i], comprobantePago[i], idBienDetalleManto);
            }
            objActivo.setEnMantenimiento(false);
            bienActivoRepo.save(objActivo);
            respuesta = true;
            titulo = "Excelente!";
            mensaje = "Pagos guardados exitosamente.";
        } catch (Exception e) {
            throw new ApplicationException(EnumException.ACTIVO_CREATE_001);
        }
        JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
        jsonReturn.add("respuesta", respuesta).add("titulo", titulo).add("mensaje", mensaje);
        return jsonReturn.build().toString();
    }

    @RequestMapping(value = "storeAjaxServicioProveeedor", method = RequestMethod.POST)
    public @ResponseBody String storeCotizacionProveedor(@RequestParam(value = "cmbRecurso") Integer cmbRecurso,
            @RequestParam(value = "txtObservaciones", required = false) String txtObservaciones,
            @RequestParam(value = "txtFechaProgramada", required = false) String txtFechaProgramada,
            @RequestParam(value = "txtTotalgasto", required = true) BigDecimal txtTotalgasto,
            @RequestParam(value = "txtIdServicioProveedor", required = false) int[] txtIdServicioProveedor,
            @RequestParam(value = "txtPrecio", required = false) BigDecimal[] txtPrecio,
            @RequestParam(value = "txtObserProv", required = false) String[] txtObserProv,
            @RequestParam(value = "ficheroCotizacion", required = false) MultipartFile[] ficheroCotizacion,
            @RequestParam(value = "imgnuevoDetalleActivo") String imgDetalleActivo) {
        Boolean respuesta = false;
        String titulo = "Oops!";
        String mensaje = "Ocurrió un error al intentar mandar a mantenimiento el Activo.";
        BienDetalleMantenimientoEntity objDetalleMant = new BienDetalleMantenimientoEntity();
        BienActivoEntity objBienActivo=bienActivoService.findByIdRecursoActive(cmbRecurso);
        try {
            objDetalleMant.setObservaciones(txtObservaciones);
            if (txtTotalgasto != null) {
                objDetalleMant.setGastoAproximado(txtTotalgasto);
            }
            /*Cambiar a verdadero para definir que esta en proceso de mantenimiento*/ 
            objBienActivo.setEnMantenimiento(true);
            

            objDetalleMant.setBienActivo(bienActivoService.findByIdRecursoActive(cmbRecurso));
            objDetalleMant.setFechaMantenimientoProgramada(
                    LocalDate.parse(txtFechaProgramada, GeneralConfiguration.getInstance().getDateFormatterNatural()));
            objDetalleMant.setActivoEstatus(activoestatus.findByIdActivoEstatus(1));
            objDetalleMant.setFinalizado(false);
            objDetalleMant.setEstatus_recordatorio(false);
            bienDetMantService.create(objDetalleMant, txtObserProv, txtPrecio, txtIdServicioProveedor,
                    ficheroCotizacion, imgDetalleActivo);
            bienActivoRepo.save(objBienActivo);
            respuesta = true;
            titulo = "Excelente!";
            mensaje = "Nuevo activo exitosamente creado.";
        } catch (Exception e) {
            throw new ApplicationException(EnumException.ACTIVO_CREATE_001);
        }

        JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
        jsonReturn.add("respuesta", respuesta).add("titulo", titulo).add("mensaje", mensaje);
        return jsonReturn.build().toString();
    }

    /** Update data manto */
    @RequestMapping(value = { "{idDetalleManto}/update", "{idDetalleManto}/update/" }, method = RequestMethod.POST)
    public @ResponseBody String UpdateDataManto(
            @RequestParam(value = "cmbRecurso", required = false) Integer cmbRecurso,
            @RequestParam(value = "txtObservaciones", required = false) String txtObservaciones,
            @RequestParam(value = "txtFechaProgramada", required = false) String txtFechaProgramada,
            @RequestParam(value = "txtTotalgasto", required = false) BigDecimal txtTotalgasto,
            @RequestParam(value = "txtIdServicioProveedor", required = false) int[] txtIdServicioProveedor,
            @RequestParam(value = "txtIdServicioProveedorManto", required = false) int[] txtIdServicioProveedorManto,
            @RequestParam(value = "txtServicioProveedorNuevo", required = false) String[] txtServicioProveedorNuevo,
            @RequestParam(value = "txtPrecio", required = false) BigDecimal[] txtPrecio,
            @RequestParam(value = "txtObserProv", required = false) String[] txtObserProv,
            @RequestParam(value = "ficheroCotizacion", required = false) MultipartFile[] ficheroCotizacion,
            @RequestParam(value = "imgDetalleActivo", required = false) String imgDetalleActivo,
            @PathVariable(name = "idDetalleManto") int idDetalleManto) {
        BienDetalleMantenimientoEntity objDetalleManto = bienDetMantService
                .findByIdBienDetalleMantenimiento(idDetalleManto);
        Boolean respuesta = false;
        String titulo = "Oops!";
        String mensaje = "Ocurrió un error en la edición.";
        try {
            if (objDetalleManto != null) {
                objDetalleManto.setObservaciones(txtObservaciones);
                objDetalleManto.setBienActivo(bienActivoService.findByIdRecursoActive(cmbRecurso));
                objDetalleManto.setFechaMantenimientoProgramada(LocalDate.parse(txtFechaProgramada,
                        GeneralConfiguration.getInstance().getDateFormatterNatural()));
                if (txtTotalgasto != null) {
                    objDetalleManto.setGastoAproximado(txtTotalgasto);
                }
                if (txtIdServicioProveedorManto != null) {
                    // method for update
                    bienDetMantService.createUpdate(objDetalleManto, txtObserProv, txtPrecio, txtIdServicioProveedor,
                            ficheroCotizacion, imgDetalleActivo, txtIdServicioProveedorManto);
                    if (txtServicioProveedorNuevo != null) {
                        int j = txtIdServicioProveedorManto.length;
                        int cantidad = txtServicioProveedorNuevo.length;
                        bienDetMantService.createEdit(objDetalleManto, txtObserProv, txtPrecio, txtIdServicioProveedor,
                                ficheroCotizacion, imgDetalleActivo, j, cantidad);
                        // method add new proveedor and pass parameter length array manto
                    }
                } else {
                    bienDetMantService.create(objDetalleManto, txtObserProv, txtPrecio, txtIdServicioProveedor,
                            ficheroCotizacion, imgDetalleActivo);
                }
                respuesta = true;
                titulo = "Genial!";
                mensaje = "Edición exitosa.";
            }
        } catch (Exception e) {
            throw new ApplicationException(EnumException.ACTIVO_CREATE_001);
        }
        JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
        jsonReturn.add("respuesta", respuesta).add("titulo", titulo).add("mensaje", mensaje);
        return jsonReturn.build().toString();
    }

    @RequestMapping(value={"{idDetalleManto}/validate","{idDetalleManto}/validate/"}, method = RequestMethod.POST)
    public @ResponseBody String MantoProveedorValidate(
        @RequestParam(value="txtIdServicioProveedorManto", required = false) int[] txtIdServicioProveedorManto,
        @RequestParam(value="txtAceptado", required=false) Boolean txtAceptado[],
        @RequestParam(value="txtNombre_proveedor", required = false) String[] txtNombre_proveedor,
        @RequestParam(value="idEstatus", required = false) int idEstatus,
        @RequestParam(value="txtComentarios", required=false) String txtComentarios,
        @PathVariable(name = "idDetalleManto") int idDetalleManto)
    {
        Boolean respuesta = false;
        String titulo = "Oops!";
        String mensaje = "Ocurrió un error en las validaciones.";

        BienDetalleMantenimientoEntity objDetalleMant=bienDetMantService.findByIdBienDetalleMantenimiento(idDetalleManto);
        ActivoEstatusEntity objEstatus=activoestatus.findByIdActivoEstatus(idEstatus);
        // for (int i = 0; i < txtIdServicioProveedorManto.length; i++) {
        //     if(txtAceptado[i]==null && txtAceptado[i].equals(null)){
        //         txtAceptado[i]=false;
        //     }
        //     System.err.println(txtIdServicioProveedorManto[i]+ "----"+txtAceptado[i]+"----"+txtNombre_proveedor[i]);
        // }
        try {
            if(objDetalleMant!=null){
                objDetalleMant.setComentario(txtComentarios);
                objDetalleMant.setActivoEstatus(objEstatus);
                if(txtIdServicioProveedorManto!=null){
                    for (int i = 0; i < txtIdServicioProveedorManto.length; i++) {
                        servProMantoservice.ValidarProveedor(txtIdServicioProveedorManto[i], txtAceptado[i]);
                    }
                }
            }
            respuesta = true;
            titulo = "Genial!";
            mensaje = "Validación exitosa.";
        } catch (Exception e) {
            throw new ApplicationException(EnumException.ACTIVO_CREATE_001);
        }

        JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
        jsonReturn.add("respuesta", respuesta).add("titulo", titulo).add("mensaje", mensaje);
        return jsonReturn.build().toString();
    }



    @RequestMapping(value = {"{idBienDetalleManto}/sendvalidate","{idBienDetalleManto}/sendvalidate/"} ,method = RequestMethod.POST)
	public @ResponseBody String updateStatus(@PathVariable(name="idBienDetalleManto")int idBienDetalleManto){
		Boolean respuesta = false;
		String titulo = "Oops!";
        String mensaje = "Ocurrió un error al enviar a validación.";
        BienDetalleMantenimientoEntity objDetalle= bienDetMantService.findByIdBienDetalleMantenimiento(idBienDetalleManto);
        ActivoEstatusEntity objEstatus=activoestatus.findByIdActivoEstatus(7);
		try {
			if(objDetalle!=null){
                objDetalle.setActivoEstatus(objEstatus);
                bienDetMantRepository.save(objDetalle);
				 respuesta = true;
				 titulo = "Enviado!";
				 mensaje = "El mantenimiento se ha enviado exitosamente.";
			}else{
				throw new ApplicationException(EnumException.ACTIVO_DELETE_001);
			}
		} catch (ApplicationException e) {
			throw new ApplicationException(EnumException.ACTIVO_DELETE_001);
		}

		JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
		jsonReturn	.add("respuesta", respuesta)
					.add("titulo", titulo)
					.add("mensaje", mensaje);		
		return jsonReturn.build().toString();
	}



}