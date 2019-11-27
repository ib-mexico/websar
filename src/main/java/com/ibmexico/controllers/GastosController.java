package com.ibmexico.controllers;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import com.ibmexico.components.ModelAndViewComponent;
import com.ibmexico.components.PdfComponent;
import com.ibmexico.configurations.GeneralConfiguration;

import com.ibmexico.entities.ActivoServicioProveedorMant2Entity;
import com.ibmexico.entities.CotizacionEntity;
import com.ibmexico.entities.CotizacionFicheroEntity;
import com.ibmexico.entities.EmpresaEntity;
import com.ibmexico.entities.FacturaEntity;
import com.ibmexico.entities.UsuarioEntity;
import com.ibmexico.libraries.DataTable;
import com.ibmexico.libraries.Templates;
import com.ibmexico.libraries.notifications.ApplicationException;
import com.ibmexico.libraries.notifications.EnumException;
import com.ibmexico.services.ActivoServicioProveedorMant2Service;
import com.ibmexico.services.ClasificacionTipoGastoService;
import com.ibmexico.services.CotizacionFicheroService;
import com.ibmexico.services.CotizacionService;
import com.ibmexico.services.CotizacionTipoFicheroService;
import com.ibmexico.services.DepartamentoService;
import com.ibmexico.services.EmpresaService;
import com.ibmexico.services.FacturaService;
import com.ibmexico.services.ProveedorService;
import com.ibmexico.services.SessionService;
import com.ibmexico.services.SucursalService;
import com.ibmexico.services.TipoGastoService;
import com.ibmexico.services.UsuarioService;
import com.lowagie.text.DocumentException;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.context.Context;

@Controller
@RequestMapping("controlPanel/Gastos")
public class GastosController {

    @Autowired
    @Qualifier("sessionService")
    private SessionService sesionService;

    @Autowired
    @Qualifier("modelAndViewComponent")
    private ModelAndViewComponent modelAndViewComponent;

    @Autowired
    @Qualifier("empresaService")
    private EmpresaService empresaService;

    @Autowired
    @Qualifier("proveedorService")
    private ProveedorService proveedorService;

    @Autowired
    @Qualifier("tipo_gasto_service")
    private TipoGastoService gastoService;

    @Autowired
    @Qualifier("usuarioService")
    private UsuarioService usuarioService;

    @Autowired
    @Qualifier("departamentoService")
    private DepartamentoService departamentoService;

    @Autowired
    @Qualifier("sucursalService")
    private SucursalService sucursalService;

    @Autowired
    @Qualifier("cotizacionService")
    private CotizacionService cotizacionService;

    @Autowired
    @Qualifier("clasificacion_tipogastoService")
    private ClasificacionTipoGastoService tipoGastoService;

    @Autowired
    @Qualifier("activo_servicio_proveedor_mant2_service")
    private ActivoServicioProveedorMant2Service gastosService;

    @Autowired
    @Qualifier("factura_gasto_service")
    private FacturaService facturaService;

    @Autowired
    @Qualifier("cotizacionFicheroService")
    private CotizacionFicheroService cotizacionFicheroService;

    @Autowired
    @Qualifier("cotizacionTipoFicheroService")
    private CotizacionTipoFicheroService cotizacionTipoFicheroService;

    @Autowired
    @Qualifier("pdfComponent")
    private PdfComponent pdfComponent;

    @RequestMapping(value = { "", "/" }, method = RequestMethod.GET)
    public ModelAndView index() {
        ModelAndView modelAndView = modelAndViewComponent.createModelAndViewControlPanel(Templates.DETALLE_GASTO);
        modelAndView.addObject("rolNuevoGasto", sesionService.hasRol("DETALLE_GASTO"));
        return modelAndView;
    }

    // Obtener los datos necesarios para el select
    @RequestMapping(value = "get-recurso-data-form", method = RequestMethod.GET)
    public @ResponseBody String getDataRecursoForm() {
        Boolean respuesta = false;
        JsonObject jsonEmpresa = null;
        JsonObject jsonProveedor = null;
        JsonObject jsonTipoGasto = null;
        JsonObject jsonCotizacion = null;
        JsonObject jsonUsuario = null;
        JsonObject jsonTipoFichero = null;
        try {
            jsonEmpresa = empresaService.jsonEmpresas();
            jsonProveedor = proveedorService.jsonProveedores();
            jsonTipoGasto = gastoService.jsonTipoGasto();
            jsonUsuario = usuarioService.jsonUsuariosActivos();
            jsonCotizacion = cotizacionService.jsonCotizacionesActivos();
            jsonTipoFichero = cotizacionTipoFicheroService.jsonTipoFichero();

            respuesta = true;
        } catch (Exception exception) {
            exception.getMessage();
        }
        JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
        jsonReturn.add("respuesta", respuesta).add("jsonEmpresa", jsonEmpresa).add("jsonProveedor", jsonProveedor)
                .add("jsonTipoGasto", jsonTipoGasto).add("jsonUsuario", jsonUsuario)
                .add("jsonCotizacion", jsonCotizacion).add("jsonTipoFichero", jsonTipoFichero);
        return jsonReturn.build().toString();
    }

    // Obtener la clasificacion de tipo gasto.
    @RequestMapping(value = "get-clasificacion/{idTipoGasto}", method = RequestMethod.GET)
    public @ResponseBody String getClasificacionTipoGasto(@PathVariable("idTipoGasto") int idTipoGasto) {
        Boolean respuesta = false;
        JsonObject jsonClasificacionGasto = null;
        try {
            jsonClasificacionGasto = tipoGastoService.jsonTipoGastoActivo(idTipoGasto);
            respuesta = true;
        } catch (ApplicationException exception) {
        }
        JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
        jsonReturn.add("respuesta", respuesta).add("jsonClasificacionGasto", jsonClasificacionGasto);
        return jsonReturn.build().toString();
    }

    /** Obtener la data del gasto para editar */
    @RequestMapping(value = "get-gasto/{idGasto}", method = RequestMethod.GET)
    public @ResponseBody String getServicioProveedorManto(@PathVariable("idGasto") int idGasto) {
        Boolean respuesta = false;

        JsonObject jsonCotizacionFichero = null;
        JsonObject jsonGasto = null;
        // ActivoServicioProveedorMant2Entity
        // objGasto=gastosService.findByIdServicioProveedorMant(idGasto);
        try {
            jsonCotizacionFichero = cotizacionFicheroService.GetFicheroEdit(idGasto);
            jsonGasto = gastosService.jsonGastoById(idGasto);
            respuesta = true;
        } catch (Exception e) {
            throw e;
        }
        JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
        jsonReturn.add("respuesta", respuesta).add("jsonGasto", jsonGasto).add("jsonCotizacionFichero",
                jsonCotizacionFichero);
        return jsonReturn.build().toString();
    }

    @RequestMapping(value = "storeGastos", method = RequestMethod.POST)
    public @ResponseBody String storeGastos(@RequestParam(value = "cmbEmpresa", required = false) Integer cmbEmpresa,
            @RequestParam(value = "cmbProveedor", required = false) Integer cmbProveedor,
            @RequestParam(value = "cmbTipoFichero", required = false) Integer cmbTipoFichero,
            @RequestParam(value = "txtFacturaNota", required = false) String txtFacturaNota,
            @RequestParam(value = "ficheroFactura", required = false) MultipartFile fileFactura,
            @RequestParam(value = "txtEstado") String txtEstado, @RequestParam(value = "txtCiudad") String txtCiudad,
            @RequestParam(value = "txtPais") String txtPais,
            @RequestParam(value = "formatted_address") String formatted_address,
            @RequestParam(value = "txtFechagasto") String txtFechagasto,
            @RequestParam(value = "txtSubtotal") BigDecimal txtSubtotal,
            @RequestParam(value = "cmbTipogasto") Integer cmbTipogasto,
            @RequestParam(value = "cmbClasificacion") Integer cmbClasificacion,

            @RequestParam(value = "txtCotizacion", required = false) String txtCotizacion,
            @RequestParam(value = "chkCotizacion", required = false, defaultValue = "false") String chkCotizacion,
            // @RequestParam(value = "cmbCotizacion", required = false, defaultValue="0")
            // Integer cmbCotizacion,
            @RequestParam(value = "OpcionGasto", required = false) Integer OpcionGasto,
            @RequestParam(value = "folioCotizacion", required = false) String[] folioCotizacion,
            @RequestParam(value = "subTotal", required = false) BigDecimal[] subTotal,
            @RequestParam(value = "idCotizacion", required = false) int[] idCotizacion,
            @RequestParam(value = "ficheroCotizacion", required = false) MultipartFile ficheroCotizacion,

            @RequestParam(value = "cmbUsuario") Integer cmbUsuario,
            @RequestParam(value = "txtObservaciones", required = false) String txtObservaciones) {
        Boolean respuesta = false;
        String titulo = "Oops!";
        String mensaje = "Ocurrió un error al intentar mandar a mantenimiento el Activo.";

        ActivoServicioProveedorMant2Entity objServMant2 = new ActivoServicioProveedorMant2Entity();
        try {
            objServMant2.setEmpresa(empresaService.findByIdEmpresa(cmbEmpresa));
            objServMant2.setProveedor(proveedorService.findByIdProveedor(cmbProveedor));
            objServMant2.setEstado(txtEstado);
            objServMant2.setCiudad(txtCiudad);
            objServMant2.setPais(txtPais);
            objServMant2.setFormatted_address(formatted_address);
            objServMant2.setFechaPago(
                    LocalDate.parse(txtFechagasto, GeneralConfiguration.getInstance().getDateFormatterNatural()));
            objServMant2.setPrecioServicioProveedor(txtSubtotal);
            objServMant2.setTipoGasto(gastoService.findByIdTipoGasto(cmbTipogasto));
            objServMant2.setClasificacionTipoGasto(tipoGastoService.findByIdClasificacion(cmbClasificacion));
            /** Subiendo factura */
            if (!txtFacturaNota.isEmpty()) {
                FacturaEntity objfactura = new FacturaEntity();
                objfactura.setNumeroFactura(txtFacturaNota);
                if (!fileFactura.isEmpty() && fileFactura != null) {
                    facturaService.addFactura(objfactura, fileFactura);
                } else {
                    facturaService.createFactura(objfactura);
                }

                objServMant2.setFacturaGasto(facturaService.findByIdFactura(objfactura.getIdFactura()));
                objServMant2.setCotizacionFichero(cotizacionFicheroService.findIdCotizacionFichero(cmbTipoFichero));
            }
            objServMant2.setUsuario(usuarioService.findByIdUsuario(cmbUsuario));
            objServMant2.setObservaciones(txtObservaciones);
            objServMant2.setAceptado(true);

            /** Subiendo cotizacion */
            if (chkCotizacion.equals("true")) {
                if (OpcionGasto.intValue() == 1) {
                    objServMant2.setIsGastoParcial(false);
                } else {
                    objServMant2.setIsGastoParcial(true);
                }
                objServMant2.setPerteneceCotizacion(true);
                objServMant2.setFolioCotizacion(String.join("  / ", folioCotizacion));
                // objServMant2.setCotizacion(cotizacionService.findByIdCotizacion(cmbCotizacion));
                gastosService.addGastoGeneral(objServMant2);
                for (int i = 0; i < idCotizacion.length; i++) {
                    CotizacionFicheroEntity objFicheros = new CotizacionFicheroEntity();

                    objFicheros.setCotizacion(cotizacionService.findByIdCotizacion(idCotizacion[i]));
                    objFicheros.setCotizacionTipoFichero(cotizacionTipoFicheroService.findByIdCotizacionTipoFichero(cmbTipoFichero));
                    objFicheros.setFolio(txtFacturaNota);
                    objFicheros.setProveedor(proveedorService.findByIdProveedor(cmbProveedor).getProveedor());
                    if (!subTotal[i].equals("")) {
                        objFicheros.setImporte(subTotal[i]);
                    }
                    objFicheros.setObservaciones(txtObservaciones);
                    objFicheros.setGasto(gastosService.findByIdServicioProveedorMant(objServMant2.getIdServicioProveedorMant()));
                    // if(i>0){
                    //     CotizacionFicheroEntity objUrlFactura = cotizacionFicheroService.findIdCotizacionFichero(0);
                    //     String urlFacturaUnica=objUrlFactura.getUrl();
                    //     objFicheros.setUrl(urlFacturaUnica);
                    //     cotizacionFicheroService.addFile(objFicheros, null);
                    // }else{
                        cotizacionFicheroService.addFile(objFicheros, fileFactura);
                    // }
                    if (cmbTipoFichero == 3 || cmbTipoFichero == 4) {
                        if (objFicheros.getCotizacion().getCotizacionEstatus().getIdCotizacionEstatus() >= 3
                                && objFicheros.getCotizacion().getCotizacionEstatus().getIdCotizacionEstatus() != 5) {
                            cotizacionService.recalcularCotizacion(objFicheros.getCotizacion());
                        }
                    }

                } /** End for */
            } else if (!txtCotizacion.equals("")) {
                objServMant2.setFolioCotizacion(txtCotizacion);
                if (!ficheroCotizacion.isEmpty() && ficheroCotizacion != null) {
                    gastosService.addCotizacion(objServMant2, ficheroCotizacion);
                }
                gastosService.addGastoGeneral(objServMant2);
            }
            respuesta = true;
            titulo = "Excelente!";
            mensaje = "Nuevo activo exitosamente creado.";
        } catch (ApplicationException e) {
            throw new ApplicationException(EnumException.GASTO_CREATE_001);
        }
        JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
        jsonReturn.add("respuesta", respuesta).add("titulo", titulo).add("mensaje", mensaje);
        return jsonReturn.build().toString();
    }

    @RequestMapping(value = { "{idGasto}/actualizar", "{idGasto}/actualizar/" }, method = RequestMethod.POST)
    public @ResponseBody String updateGasto(@RequestParam(value = "cmbEmpresa", required = false) Integer cmbEmpresa,
            @RequestParam(value = "cmbProveedor", required = false) Integer cmbProveedor,
            @RequestParam(value = "cmbTipoFichero", required = false) Integer cmbTipoFichero,
            @RequestParam(value = "txtFacturaNota", required = false) String txtFacturaNota,
            @RequestParam(value = "ficheroFactura", required = false) MultipartFile fileFactura,
            @RequestParam(value = "idFactura", required = false) int idFactura,
            @RequestParam(value = "txtEstado", required = false) String txtEstado,
            @RequestParam(value = "txtCiudad", required = false) String txtCiudad,
            @RequestParam(value = "txtPais", required = false) String txtPais,
            @RequestParam(value = "formatted_address", required = false) String formatted_address,

            @RequestParam(value = "txtFechagasto") String txtFechagasto,
            @RequestParam(value = "txtSubtotal") BigDecimal txtSubtotal,

            @RequestParam(value = "cmbTipogasto") Integer cmbTipogasto,
            @RequestParam(value = "cmbClasificacion") Integer cmbClasificacion,

            @RequestParam(value = "txtCotizacion", required = false) String txtCotizacion,
            @RequestParam(value = "chkCotizacion", required = false, defaultValue = "false") String chkCotizacion,
            // @RequestParam(value = "cmbCotizacion", required = false, defaultValue="0")
            // Integer cmbCotizacion,
            @RequestParam(value = "ficheroCotizacion", required = false) MultipartFile ficheroCotizacion,

            @RequestParam(value = "folioCotizacion", required = false) String[] folioCotizacion,
            @RequestParam(value = "subTotal", required = false) BigDecimal[] subTotal,
            @RequestParam(value = "idCotizacion", required = false) int[] idCotizacion,
            @RequestParam(value = "idCotizacionFichero", required = false) int[] idCotizacionFichero,
            @RequestParam(value = "NuevoFicheroGasto", required = false) String[] NuevoFicheroGasto,

            @RequestParam(value = "cmbUsuario") Integer cmbUsuario,
            @RequestParam(value = "txtObservaciones", required = false) String txtObservaciones,
            @PathVariable(name = "idGasto") int idGasto) {

        ActivoServicioProveedorMant2Entity objGasto = gastosService.findByIdServicioProveedorMant(idGasto);
        Boolean respuesta = false;
        String titulo = "Oops!";
        String mensaje = "Ocurrio un error en la edicion.";
        try {
            if (objGasto != null) {
                objGasto.setEmpresa(empresaService.findByIdEmpresa(cmbEmpresa));
                objGasto.setProveedor(proveedorService.findByIdProveedor(cmbProveedor));
                if (txtEstado != null && txtCiudad != null && txtPais != null) {
                    objGasto.setEstado(txtEstado);
                    objGasto.setCiudad(txtCiudad);
                    objGasto.setPais(txtPais);
                    objGasto.setFormatted_address(formatted_address);
                }
                objGasto.setFechaPago(LocalDate.parse(txtFechagasto, GeneralConfiguration.getInstance().getDateFormatterNatural()));
                objGasto.setPrecioServicioProveedor(txtSubtotal);
                objGasto.setTipoGasto(gastoService.findByIdTipoGasto(cmbTipogasto));
                objGasto.setClasificacionTipoGasto(tipoGastoService.findByIdClasificacion(cmbClasificacion));
                if (idFactura > 0) {
                    if (!txtFacturaNota.isEmpty()) {
                        FacturaEntity objFactura = facturaService.findByIdFactura(idFactura);
                        objFactura.setNumeroFactura(txtFacturaNota);
                        if (!fileFactura.isEmpty()) {
                            facturaService.addFactura(objFactura, fileFactura);
                        } else {
                            facturaService.createFactura(objFactura);
                        }
                        objGasto.setFacturaGasto(facturaService.findByIdFactura(objFactura.getIdFactura()));
                    }
                }

                objGasto.setUsuario(usuarioService.findByIdUsuario(cmbUsuario));
                objGasto.setObservaciones(txtObservaciones);
                objGasto.setAceptado(true);
                if (chkCotizacion.equals("true")) {
                    objGasto.setPerteneceCotizacion(true);
                    // objGasto.setFolioCotizacion(cotizacionService.findByIdCotizacion(cmbCotizacion).getFolio());
                    objGasto.setFolioCotizacion(String.join("/", folioCotizacion));
                    // objGasto.setCotizacion(cotizacionService.findByIdCotizacion(cmbCotizacion));
                    gastosService.addGastoGeneral(objGasto);

                    if (idCotizacion != null) {
                        if (idCotizacionFichero != null) {
                            for (int i = 0; i < idCotizacionFichero.length; i++) {
                                CotizacionFicheroEntity objFicheros = cotizacionFicheroService.findIdCotizacionFichero(idCotizacionFichero[i]);
                                if (idCotizacion[i] == objFicheros.getCotizacion().getIdCotizacion()) {
                                    try {
                                        objFicheros.setCotizacion(cotizacionService.findByIdCotizacion(idCotizacion[i]));
                                        objFicheros.setCotizacionTipoFichero(cotizacionTipoFicheroService.findByIdCotizacionTipoFichero(cmbTipoFichero));
                                        objFicheros.setFolio(txtFacturaNota);
                                        objFicheros.setProveedor(proveedorService.findByIdProveedor(cmbProveedor).getProveedor());
                                        if (!subTotal[i].equals("")) {
                                            objFicheros.setImporte(subTotal[i]);
                                        }
                                        objFicheros.setObservaciones(txtObservaciones);
                                        objFicheros.setGasto(gastosService.findByIdServicioProveedorMant(objGasto.getIdServicioProveedorMant()));
                                        cotizacionFicheroService.addFile(objFicheros, fileFactura);
                                        if (cmbTipoFichero == 3 || cmbTipoFichero == 4) {
                                            if (objFicheros.getCotizacion().getCotizacionEstatus().getIdCotizacionEstatus() >= 3
                                                    && objFicheros.getCotizacion().getCotizacionEstatus().getIdCotizacionEstatus() != 5) {
                                                cotizacionService.recalcularCotizacion(objFicheros.getCotizacion());
                                            }
                                        }
                                    } catch (Exception e) {
                                        throw new ApplicationException(EnumException.GASTO_CREATE_001);
                                    }
                                }
                            } /* End for */
                        }

                        /** Si desea agregar una nueva Cotizacion en aquello Gasto */
                        if (NuevoFicheroGasto != null) {
                            int j = 0;
                            /**Obtener el tamanio del arreglo CotizacionFichero que existe */
                            if (idCotizacionFichero != null) {
                                j = idCotizacionFichero.length;
                            }
                            /**Obtener el tamanio del arreglo NuevoCotizacioFichero*/
                            int cantidad = NuevoFicheroGasto.length;
                            System.err.println(cantidad + "   valor de la cantidad");
                            if (cantidad == 0) {
                                cantidad = cantidad + 1;
                            }
                            for (int n = j; n < j + cantidad; n++) {
                                CotizacionFicheroEntity nuevoFichero = new CotizacionFicheroEntity();
                                try {
                                    nuevoFichero.setCotizacion(cotizacionService.findByIdCotizacion(idCotizacion[n]));
                                    nuevoFichero.setCotizacionTipoFichero(cotizacionTipoFicheroService.findByIdCotizacionTipoFichero(cmbTipoFichero));
                                    nuevoFichero.setFolio(txtFacturaNota);
                                    nuevoFichero.setProveedor(proveedorService.findByIdProveedor(cmbProveedor).getProveedor());
                                    if (!subTotal[n].equals("")) {
                                        nuevoFichero.setImporte(subTotal[n]);
                                    }
                                    nuevoFichero.setObservaciones(txtObservaciones);
                                    nuevoFichero.setGasto(gastosService.findByIdServicioProveedorMant(objGasto.getIdServicioProveedorMant()));

                                    // if (fileFactura.isEmpty()) {
                                    //     ClassPathResource resource = new ClassPathResource(
                                    //         "static/ficheros/FacturasGastos/" + objGasto.getFacturaGasto().getUrl());
                                        
                                    //     File file=resource.getFile();
                                        
                                    //     if (objGasto.getFacturaGasto()!=null && !objGasto.getFacturaGasto().getUrl().equals("") && objGasto.getFacturaGasto().getUrl()!=null) {
                                    //         DiskFileItem fileItem=new DiskFileItem("factura", null, false, file.getName(), (int) file.length(), file.getParentFile());                                            
                                    //         fileItem.getOutputStream();
                                    //         fileFactura=new CommonsMultipartFile(fileItem);
                                    //     }
                                    // }
                                    if (fileFactura.isEmpty()) {
                                        if(idCotizacionFichero!=null){
                                        CotizacionFicheroEntity objFichero = cotizacionFicheroService.findIdCotizacionFichero(idCotizacionFichero[1]);
                                        String urlFactura=objFichero.getUrl();
                                        nuevoFichero.setUrl(urlFactura);                   
                                        }
                                        cotizacionFicheroService.addFile(nuevoFichero, null);
                                    }else{
                                        cotizacionFicheroService.addFile(nuevoFichero, fileFactura);
                                    }
                                        if(cmbTipoFichero == 3 || cmbTipoFichero == 4) {					
                                            if(nuevoFichero.getCotizacion().getCotizacionEstatus().getIdCotizacionEstatus() >= 3 &&  nuevoFichero.getCotizacion().getCotizacionEstatus().getIdCotizacionEstatus() != 5) {	
                                                cotizacionService.recalcularCotizacion(nuevoFichero.getCotizacion());
                                            }				
                                        }
                                    } catch (Exception e) {
                                        throw new ApplicationException(EnumException.GASTO_CREATE_001);
                                    }
                                    
                                }
                            }
                        }
                    }
                    else{
                        objGasto.setFolioCotizacion(txtCotizacion);
                        if(!ficheroCotizacion.isEmpty()){
                            gastosService.addCotizacion(objGasto, ficheroCotizacion);
                        }else{
                            gastosService.addGastoGeneral(objGasto);
                        }
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

 
    //Datatable
    @RequestMapping(value = "/table", method = RequestMethod.POST)
    private @ResponseBody String table(
            @RequestParam("offset") int offset, 
            @RequestParam("limit") int limit,
            @RequestParam("_csrf") String _csrf,
            @RequestParam(value = "search", required = false, defaultValue = "") String search,
            @RequestParam(value = "txtBootstrapTableDesde", required = false, defaultValue = "") String txtBootstrapTableDesde,
            @RequestParam(value = "txtBootstrapTableHasta", required = false, defaultValue = "") String txtBootstrapTableHasta,
            @RequestParam(value="filtro", required = false)Integer filtro
          ) {
        
        int filtrado=0;
        if(filtro!=null && filtro>0){
            filtrado=filtro;
        }
        System.err.println(filtrado);

        DataTable<ActivoServicioProveedorMant2Entity> dtDetalleMant = gastosService.dataTable(search, offset, limit,
                txtBootstrapTableDesde, txtBootstrapTableHasta, filtrado);

        JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
        jsonReturn.add("total", dtDetalleMant.getTotal());
        JsonArrayBuilder jsRows = Json.createArrayBuilder();
        BigDecimal d = BigDecimal.ZERO;
        dtDetalleMant.getRows().forEach((itemDetalle) -> {
            
            jsRows.add(Json.createObjectBuilder()
                .add("idgasto", itemDetalle.getIdServicioProveedorMant())
                .add("creacion_fecha", itemDetalle.getCreacionFechaNatural())
                .add("diagnostico", itemDetalle.getObservaciones())
                .add("fechaPago", itemDetalle.getFechaPagoNatural())
                .add("numeroFactura", itemDetalle.getFacturaGasto()!=null ? itemDetalle.getFacturaGasto().getNumeroFactura() : "N/A")
                .add("numeroCotizacion", itemDetalle.getFolioCotizacion()!=null ? itemDetalle.getFolioCotizacion() :"N/A")
                .add("tipoGasto", itemDetalle.getTipoGasto()!=null ? itemDetalle.getTipoGasto().getNombre() : "N/A")
                .add("Clasificacion", itemDetalle.getClasificacionTipoGasto()!=null ? itemDetalle.getClasificacionTipoGasto().getNombre() : "N/A")
                .add("usuario", itemDetalle.getUsuario()!=null ? itemDetalle.getUsuario().getNombreCompleto() : "N/A")
                .add("subtotal",itemDetalle.getSubtotalNatural()!=null ? itemDetalle.getSubtotalNatural() : "N/A")
                .add("fileFactura", itemDetalle.getFacturaGasto().getUrl()!=null? itemDetalle.getFacturaGasto().getUrl() : "")
                );
        });
        String FechaInicio=txtBootstrapTableDesde;
        String FechaFin=txtBootstrapTableHasta;
        jsonReturn.add("rows", jsRows).add("fechaInicio", FechaInicio).add("fechaFin", FechaFin);
        return jsonReturn.build().toString();
    }

    @RequestMapping(value={"{idGasto}/eliminar","{idGasto}/eliminar/"}, method=RequestMethod.POST)
    public @ResponseBody String deleteGasto(@PathVariable(name="idGasto")int idGasto){
        Boolean respuesta=false;
        String titulo="Oops!";
        String mensaje="Ocurrio un error al intentar eliminar el Gasto";
        ActivoServicioProveedorMant2Entity objGasto=gastosService.findByIdServicioProveedorMant(idGasto);
        List<CotizacionFicheroEntity> objFichero=cotizacionFicheroService.CotizacionFIcheroByGastoID(idGasto);

        try {
            if(objGasto!=null){
                objGasto.setIsEliminado(true);
                System.err.println("antes del foreach");
                for (CotizacionFicheroEntity cotFichero : objFichero) {
                    System.err.println("iniciando foreach");
                    if(cotFichero.getCotizacion().getCotizacionEstatus().getCotizacionEstatus().equals("Facturada") ||
                        cotFichero.getCotizacion().getCotizacionEstatus().getCotizacionEstatus().equals("Pagada") 
                    ){
                         System.err.println("entro");
                         cotizacionFicheroService.delete(cotFichero);
                         System.err.println("se elimino actualizando");
                        CotizacionEntity objCotizacion=cotizacionService.findByIdCotizacion(cotFichero.getCotizacion().getIdCotizacion());
                        cotizacionService.recalcularCotizacion(objCotizacion);
                    }
                    
                }
                gastosService.update(objGasto);
                respuesta=true;
                titulo="Eliminado!";
                mensaje="El gasto ha sido eliminado exitosamente.";
            }else{
                throw new ApplicationException(EnumException.GASTO_CREATE_001);
            }
        } catch (Exception e) {
            throw new ApplicationException(EnumException.GASTO_DELETE_001);
        }
        JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
		jsonReturn	.add("respuesta", respuesta)
					.add("titulo", titulo)
					.add("mensaje", mensaje);							
		return jsonReturn.build().toString();
    }


    @RequestMapping(value={"{idCotizacion}/{idCotizacionFichero}/eliminarFichero","{idCotizacion}/{idCotizacionFichero}/eliminarFichero/"},method = RequestMethod.POST)
    public @ResponseBody String eliminarFichero(@PathVariable(name = "idCotizacion")int idCotizacion,
        @PathVariable(name="idCotizacionFichero")int idCotizacionFichero){
            Boolean respuesta=false;
            String titulo="Oops!";
            String mensaje="Ocurrio un error al intentar eliminar el Gasto";
            CotizacionFicheroEntity objFichero=cotizacionFicheroService.findIdCotizacionFichero(idCotizacionFichero);
            CotizacionEntity objCotizacion=cotizacionService.findByIdCotizacion(idCotizacion);
            if(objFichero!=null){
                try {
                    cotizacionFicheroService.delete(objFichero);
                    if(objCotizacion.getCotizacionEstatus().getCotizacionEstatus().equals("Facturada") ||
                     objCotizacion.getCotizacionEstatus().getCotizacionEstatus().equals("Pagada")){
                         cotizacionService.recalcularCotizacion(objCotizacion);
                     }
                    respuesta = true;
				    titulo = "Eliminado!";
				    mensaje = "El expediente ha sido eliminado exitosamente.";
                } catch (ApplicationException e) {
                    respuesta = false;
				    titulo = "Error!";
				    mensaje = e.toString();
                }
            }
            else{
                respuesta = false;
                titulo = "Error!";
                mensaje = "El expediente que deseas eliminar no existe.";
            } 
            JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
            jsonReturn	.add("respuesta", respuesta)
                        .add("titulo", titulo)
                        .add("mensaje", mensaje);
            
            return jsonReturn.build().toString();

    }

    
    @RequestMapping(value={"reporte-gasto-pdf",
                            "reporte-gasto-pdf/{fechaInicio}/{fechaFin}/{idEmpresa}/{idtipoGasto}/tipoGasto",
                            "reporte-gasto-pdf/{fechaInicio}/{fechaFin}/{idEmpresa}/{idUsuario}",
                            "reporte-gasto-pdf/{fechaInicio}/{fechaFin}/{idEmpresa}/{idtipoGasto}/{idUsuario}",
                            "reporte-gasto-pdf/{fechaInicio}/{fechaFin}/{idEmpresa}"}, 
                            method=RequestMethod.GET,produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> previewPdfGasto(@PathVariable(value="fechaInicio",required=false)String fecha,
                                                                @PathVariable(value="fechaFin", required = false) String fechaFinal,
                                                                @PathVariable(value ="idtipoGasto",required = false)Integer idTipoGasto,
                                                                @PathVariable(value="idEmpresa", required=false) Integer idEmpresa,
                                                                @PathVariable(value = "idUsuario",required = false)Integer idUsuario) throws IOException,  DocumentException {
        LocalDate fechaMesInicio=null;
        LocalDate fechaMesFin= null;
        UsuarioEntity objUsuario=null;

        System.err.println(idUsuario +"values");
        System.err.println(idTipoGasto +"values");
        System.err.println(fecha +"values");
        System.err.println(fechaFinal +"values");

        if(idUsuario!=null && idUsuario>=1){
            objUsuario=usuarioService.findByIdUsuario(idUsuario);
        }

        if(!fecha.equals("") && fecha.contains("-")){
            String[] arrFecha=fecha.split("-");
            int year= Integer.parseInt(arrFecha[2]);
            int month= Integer.parseInt(arrFecha[1]);
            int day=Integer.parseInt(arrFecha[0]);
            fechaMesInicio=LocalDate.of(year, month, day);
            if (!fechaFinal.equals("") && fechaFinal.contains("-")) {
                String [] arrFechaFin=fechaFinal.split("-");
                int anio= Integer.parseInt(arrFechaFin[2]);
                int mes= Integer.parseInt(arrFechaFin[1]);
                int dia=Integer.parseInt(arrFechaFin[0]);
                fechaMesFin=LocalDate.of(anio, mes, dia);
            }else{
                fechaMesFin=LocalDate.now();
            }
        }else{
            fechaMesInicio=LocalDate.now().withDayOfMonth(1);
            fechaMesFin=LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());
        }

        /**Totales del reporte para mostrar */
        BigDecimal totalGastos=new BigDecimal(0);

        final LocalDate fechaInicio=fechaMesInicio;
        final LocalDate fechaFin=fechaMesFin;
        List<ActivoServicioProveedorMant2Entity> lstGastos=null;
     
        String user="";
        if(idUsuario!=null && idUsuario>=1){
            if(idTipoGasto!=null && idTipoGasto>=1){
                lstGastos=gastosService.findByGastoUsuarioFechaTipo(fechaInicio, fechaFin, idUsuario, idTipoGasto, idEmpresa);
            }else{
                lstGastos=gastosService.findByGastoUsarioFecha(fechaInicio, fechaFin, idUsuario, idEmpresa);
            }
            user="USUARIO";
        }
        if(idTipoGasto!=null && idTipoGasto>=1 && idUsuario==null){
            lstGastos=gastosService.findByGastoTipoFecha(fechaInicio, fechaFin, idTipoGasto, idEmpresa);
        }else if(idTipoGasto==null && idUsuario==null){
            lstGastos=gastosService.findByGastoFecha(fechaInicio, fechaFin, idEmpresa);
        }
        // List<ActivoServicioProveedorMant2Entity> lstGastos= gastosService.findByGastoTipoFecha(fechaInicio, fechaFin, idUsuario);
        List<Map<String, String>> myMap=new ArrayList<Map<String, String>>();
        Integer iterator=0;
        
        for (ActivoServicioProveedorMant2Entity itemGasto : lstGastos) {
            Map<String,String> myMap1 = new HashMap<String, String>();
            /*Calculo de totales */
            totalGastos= totalGastos.add(itemGasto.getPrecioServicioProveedor());
            myMap1.put("folioCotizacion", itemGasto.getFolioCotizacion());
            myMap1.put("folioFactura", itemGasto.getFacturaGasto().getNumeroFactura());
            myMap1.put("ciudad", itemGasto.getCiudad());
            myMap1.put("estado", itemGasto.getEstado());
            myMap1.put("lugar_formatter", itemGasto.getFormatted_address());
            myMap1.put("observaciones", itemGasto.getObservaciones());
            myMap1.put("subTotal", GeneralConfiguration.getInstance().getNumberFormat().format(itemGasto.getPrecioServicioProveedor()));
            // myMap1.put("perteneceCotizacion", GeneralConfiguration.getInstance().getNumberFormat().format(itemGasto.getPerteneceCotizacion()).equals("true") ? "SI" :"NO");
            myMap1.put("clasificacionGasto", itemGasto.getClasificacionTipoGasto().getNombre());
            myMap1.put("tipoGasto", itemGasto.getTipoGasto().getNombre());
            myMap1.put("proveedor", itemGasto.getProveedor().getProveedor());
            myMap1.put("usuario",itemGasto.getUsuario().getNombreCompleto());
            myMap1.put("fechaRegistro", itemGasto.getCreacionFechaNatural());
            myMap1.put("fechaGasto", itemGasto.getFechaPagoNatural());
            myMap.add(iterator, myMap1);
            iterator++;
        }
        
        LocalDateTime ldtNow = LocalDateTime.now();
        Templates objTemplates=new Templates();
        String path_file=ldtNow.getYear() +"_"+ldtNow.getMonthValue() +"_"+ldtNow.getDayOfMonth() +"_REPORTE_GASTO.pdf";
        EmpresaEntity objEmpresa=empresaService.findByIdEmpresa(idEmpresa);
        Context objContext = new Context();
        objContext.setVariable("_TEMPLATE_", Templates.PDF_GASTO);
        objContext.setVariable("title", "Reporte de Gasto");
        objContext.setVariable("objUsuario", objUsuario);
        objContext.setVariable("objEmpresa",objEmpresa);
        objContext.setVariable("GastosMap", myMap);
        objContext.setVariable("tieneUsuario",user);

        objContext.setVariable("totalSubtotal", GeneralConfiguration.getInstance().getNumberFormat().format(totalGastos));
        objContext.setVariable("fechaInicio", fechaInicio.format(GeneralConfiguration.getInstance().getDateFormatterNatural()));
        objContext.setVariable("fechaFin", fechaFin.format(GeneralConfiguration.getInstance().getDateFormatterNatural()));
        objContext.setVariable("mesActual", GeneralConfiguration.getInstance().getCurrentMonthNatural(fechaInicio.getMonthValue()));
        System.err.println(user +"valor de user");
        return pdfComponent.generate(path_file, objTemplates.FOUNDATION_PDF, objContext);
    }
    

}