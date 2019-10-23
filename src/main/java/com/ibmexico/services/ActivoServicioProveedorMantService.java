
package com.ibmexico.services;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.transaction.Transactional;

import com.ibmexico.configurations.GeneralConfiguration;
import com.ibmexico.entities.ActivoServicioProveedorMantEntity;
import com.ibmexico.entities.BienDetalleMantenimientoEntity;
import com.ibmexico.entities.UsuarioEntity;
import com.ibmexico.libraries.notifications.ApplicationException;
import com.ibmexico.libraries.notifications.EnumException;
import com.ibmexico.repositories.IActivoEstatusRepository;
import com.ibmexico.repositories.IActivoServicioProveedorMantRepository;
import com.ibmexico.repositories.IActivoServicioProveedorRepository;
import com.ibmexico.repositories.IBienDetalleMantenimientoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service("activo_servicio_proveedor_mant_service")
public class ActivoServicioProveedorMantService {

    @Autowired
    @Qualifier("sessionService")
    private SessionService sesionService;

    @Autowired
    @Qualifier("activo_servicio_proveedor_mant_repo")
    private IActivoServicioProveedorMantRepository serviProveeMant;

    @Autowired
    @Qualifier("activoServicioProveedorRepository")
    private IActivoServicioProveedorRepository activoServProv;

    @Autowired
    @Qualifier("bienDetalleMantenimientoRepository")
    private IBienDetalleMantenimientoRepository bienDetalleMant;

    @Autowired
    @Qualifier("activo_estatus_repository")
    private IActivoEstatusRepository activoEstatusRepo;

    /*return un registro de servicio-proveedor*/
    public ActivoServicioProveedorMantEntity findByIdServicioProveedorMant(int idServicioProvMant){
        return serviProveeMant.findByIdServicioProveedorMant(idServicioProvMant);
    }

    /*Registrar un conjunto de servicio_proveedor_mant seleccionado*/
    @Transactional
    public void addServicioProveedor(BienDetalleMantenimientoEntity objDetalle, String[] arrObservaciones,
            BigDecimal[] arrprecioServProv, int[] arridActivoServicioProveedor, MultipartFile[] arrcotizacion) {
        if (objDetalle != null) {
            if (arridActivoServicioProveedor != null && arrprecioServProv != null && arrObservaciones != null
                    && arrcotizacion != null) {
                for (int i = 0; i < arridActivoServicioProveedor.length; i++) {
                    ActivoServicioProveedorMantEntity objActivoSerProvMant = new ActivoServicioProveedorMantEntity();
                    LocalDateTime ldtnow = LocalDateTime.now();
                    UsuarioEntity objUsuario = sesionService.getCurrentUser();
                    objActivoSerProvMant.setCreacionFecha(ldtnow);
                    objActivoSerProvMant.setModificacionFecha(ldtnow);
                    objActivoSerProvMant.setCreacionUsuario(objUsuario);
                    objActivoSerProvMant.setModificacionUsuario(objUsuario);
                    objActivoSerProvMant.isAceptado();
                    objActivoSerProvMant.isPagado();
                    objActivoSerProvMant.setBienDetalleMant(
                            bienDetalleMant.findByIdDetalleMantenimiento(objDetalle.getIdDetalleMantenimiento()));
                    objActivoSerProvMant.setActivoServicioProveedor(
                            activoServProv.findByIdServicioProveedor(arridActivoServicioProveedor[i]));
                    objActivoSerProvMant.setPrecioServicioProveedor(arrprecioServProv[i]);
                    objActivoSerProvMant.setObservaciones(arrObservaciones[i]);
                    /* POR AGREGAR FICHERO COTIZACION DE POSIBLES PROVEEDORES */
                    addCotizacion(objActivoSerProvMant, arrcotizacion[i]);
                    // serviProveeMant.save(objActivoSerProvMant);
                }
            }
        }
    }

    /*REGISTRAR UN CONJUNTO DE SERVICIO_PROVEEDORES_MANT CUANDO EXISTA SERVICIOS REGISTRAD*/
    @Transactional
    public void addServicioProveedorEdit(BienDetalleMantenimientoEntity objDetalle, String[] arrObservaciones,
            BigDecimal[] arrprecioServProv, int[] arridActivoServicioProveedor, MultipartFile[] arrcotizacion, int lengthManto, int lengthNuevo) {
        if (objDetalle != null) {
            if (arridActivoServicioProveedor != null && arrObservaciones != null) {
                /**recorrer el arreglo desde la posicion length manto hasta la cantidad del nuevoservicio agregado */
                for (int i =lengthManto; i < lengthManto+lengthNuevo; i++) {
                    System.err.println(lengthManto +" ->>>> tamnio manto >>>>>>>" +lengthNuevo);
                    ActivoServicioProveedorMantEntity objActivoSerProvMant = new ActivoServicioProveedorMantEntity();
                    LocalDateTime ldtnow = LocalDateTime.now();
                    UsuarioEntity objUsuario = sesionService.getCurrentUser();
                    objActivoSerProvMant.setCreacionFecha(ldtnow);
                    objActivoSerProvMant.setModificacionFecha(ldtnow);
                    objActivoSerProvMant.setCreacionUsuario(objUsuario);
                    objActivoSerProvMant.setModificacionUsuario(objUsuario);
                    objActivoSerProvMant.isAceptado();
                    objActivoSerProvMant.isPagado();
                    objActivoSerProvMant.setBienDetalleMant(
                            bienDetalleMant.findByIdDetalleMantenimiento(objDetalle.getIdDetalleMantenimiento()));
                    objActivoSerProvMant.setActivoServicioProveedor(
                            activoServProv.findByIdServicioProveedor(arridActivoServicioProveedor[i]));
                    objActivoSerProvMant.setPrecioServicioProveedor(arrprecioServProv[i]);
                    objActivoSerProvMant.setObservaciones(arrObservaciones[i]);
                    /* POR AGREGAR FICHERO COTIZACION DE POSIBLES PROVEEDORES */
                    addCotizacion(objActivoSerProvMant, arrcotizacion[i]);
                    // serviProveeMant.save(objActivoSerProvMant);
                }
            }
        }
    }

    /*UPDATE SERVICIO_PROVEEDORES_MANT */
    @Transactional
    public void addServicioProveedorUpdate(BienDetalleMantenimientoEntity objDetalle, String[] arrObservaciones,
            BigDecimal[] arrprecioServProv, int[] arridActivoServicioProveedor, MultipartFile[] arrcotizacion, int [] ServicioProveedorManto) {
        if (objDetalle != null) {
            if (arridActivoServicioProveedor != null && arrprecioServProv != null && arrObservaciones != null
                  && ServicioProveedorManto!=null){
                /**recorrer el arreglo desde la posicion length manto hasta la cantidad del nuevoservicio agregado */
                for (int i =0; i < ServicioProveedorManto.length; i++) {
                    ActivoServicioProveedorMantEntity objActivoSerProvMant=this.findByIdServicioProveedorMant(ServicioProveedorManto[i]);

                    System.err.println(objActivoSerProvMant.getObservaciones());
                    // ActivoServicioProveedorMantEntity objActivoSerProvMant = new ActivoServicioProveedorMantEntity();
                    LocalDateTime ldtnow = LocalDateTime.now();
                    UsuarioEntity objUsuario = sesionService.getCurrentUser();
                    objActivoSerProvMant.setCreacionFecha(ldtnow);
                    objActivoSerProvMant.setModificacionFecha(ldtnow);
                    objActivoSerProvMant.setCreacionUsuario(objUsuario);
                    objActivoSerProvMant.setModificacionUsuario(objUsuario);
                    objActivoSerProvMant.isAceptado();
                    objActivoSerProvMant.isPagado();
                    objActivoSerProvMant.setBienDetalleMant(
                            bienDetalleMant.findByIdDetalleMantenimiento(objDetalle.getIdDetalleMantenimiento()));
                    objActivoSerProvMant.setActivoServicioProveedor(
                            activoServProv.findByIdServicioProveedor(arridActivoServicioProveedor[i]));
                    objActivoSerProvMant.setPrecioServicioProveedor(arrprecioServProv[i]);
                    objActivoSerProvMant.setObservaciones(arrObservaciones[i]);
                    /* POR AGREGAR FICHERO COTIZACION DE POSIBLES PROVEEDORES */
                    if (arrcotizacion[i]!=null) {
                        addCotizacion(objActivoSerProvMant, arrcotizacion[i]);
                    }
                    // serviProveeMant.save(objActivoSerProvMant);
                }
            }
        }
    }


    @Transactional
    public void addCotizacion(ActivoServicioProveedorMantEntity objServProvMant, MultipartFile file) {
        if (objServProvMant != null) {
            if (file != null && !file.isEmpty()) {
                try {
                    if (!file.getOriginalFilename().trim().equals("")) {
                        String ficheroNombre = UUID.randomUUID().toString();
                        String[] arrNombreFichero = file.getOriginalFilename().split("\\.");
                        if (arrNombreFichero.length >= 2) {
                            ficheroNombre = ficheroNombre + "." + arrNombreFichero[arrNombreFichero.length - 1];
                        }
                        System.err.println("file no esta vacio");
                        objServProvMant.setUrlCotizacion(ficheroNombre);
                        createFicheroCotizacion(objServProvMant, file);
                    }
                } catch (Exception e) {
                    throw new ApplicationException(EnumException.ACTIVO_FICHEROS_ADD_FILE_003);
                }
            } else {
                LocalDateTime ldtnow = LocalDateTime.now();
                UsuarioEntity objUsurioCreacion = sesionService.getCurrentUser();
                objServProvMant.setCreacionFecha(ldtnow);
                objServProvMant.setModificacionFecha(ldtnow);
                objServProvMant.setCreacionUsuario(objUsurioCreacion);
                objServProvMant.setModificacionUsuario(objUsurioCreacion);
                serviProveeMant.save(objServProvMant);
            }
        } else {
            throw new ApplicationException(EnumException.ACTIVO_FICHEROS_ADD_FILE_001);
        }
    }

    @Transactional
    public void createFicheroCotizacion(ActivoServicioProveedorMantEntity objServProvMant, MultipartFile objFile)
            throws IOException {
        URL urlPath = this.getClass().getResource("/");
        if (objServProvMant != null) {
            if (objServProvMant.getUrlCotizacion() != "") {
                byte[] bytesFichero = objFile.getBytes();
                // Crear carpeta si no existe
                File fileruta = new File(urlPath.getPath() + "static/ficheros/ProveedorCotizacion");
                if (!fileruta.exists()) {
                    fileruta.mkdirs();
                }
                try (BufferedOutputStream buffStream = new BufferedOutputStream(
                        new FileOutputStream(new File(urlPath.getPath() + "static/ficheros/ProveedorCotizacion/"
                                + objServProvMant.getUrlCotizacion())))) {
                    buffStream.write(bytesFichero);
                    // LocalDateTime ldtnow=LocalDateTime.now();
                    // objServProvMant.setModificacionUsuario(objUsurioCreacion);
                    serviProveeMant.save(objServProvMant);
                } catch (Exception e) {
                    throw new ApplicationException(EnumException.ACTIVO_FICHEROS_ADD_FILE_003);
                }
            } else {
                throw new ApplicationException(EnumException.ACTIVO_FICHEROS_ADD_FILE_004);
            }
        } else {
            throw new ApplicationException(EnumException.ACTIVO_CREATE_001);
        }
    }

    @Transactional
    public void PagoServicioProveedor(String txtFechaPago, int idProveedorServicio, MultipartFile file, int DetalleManto){
        if (txtFechaPago!=null) {
            // ActivoVoucherEntity objVoucher=new ActivoVoucherEntity();
                ActivoServicioProveedorMantEntity objProveedorServicio=serviProveeMant.findByIdServicioProveedorMant(idProveedorServicio);
                BienDetalleMantenimientoEntity objDetalleManto=bienDetalleMant.findByIdDetalleMantenimiento(DetalleManto);
                objProveedorServicio.setFechaPago(LocalDate.parse(txtFechaPago, GeneralConfiguration.getInstance().getDateFormatterNatural()));
                // objVoucher.setActivoServicioProveedor(objProveedorServicio);
                LocalDateTime ldtnow =LocalDateTime.now();
                objProveedorServicio.setCreacionFechaPago(ldtnow);
                objProveedorServicio.setModificacionFechaPago(ldtnow);
                if(file!=null){
                    this.addComprobante(file,objProveedorServicio);
                    System.err.println(objProveedorServicio);
                }
                objProveedorServicio.setPagado(true);
                serviProveeMant.save(objProveedorServicio);
                /*Pasar el estatus a pagado*/
                // serviProveeMant.save(objProveedorServicio);
                objDetalleManto.setActivoEstatus(activoEstatusRepo.findByIdActivoEstatus(3));
                bienDetalleMant.save(objDetalleManto);
            }
        }
    

    @Transactional
    public void addComprobante(MultipartFile voucher, ActivoServicioProveedorMantEntity objVoucher){
        System.err.println("method aaddComprobante");
        if(voucher!=null && !voucher.isEmpty()){
            System.err.println(voucher);
            System.err.println(objVoucher.getFechaPago());
            System.err.println("addCOmprobante if");
            try {
                System.err.println("methos try addcomprobante");
                if(!voucher.getOriginalFilename().trim().equals("")){
                        String ficheroNombre = UUID.randomUUID().toString();
                        String[] arrNombreFichero = voucher.getOriginalFilename().split("\\.");
                        if (arrNombreFichero.length >= 2) {
                            ficheroNombre = ficheroNombre + "." + arrNombreFichero[arrNombreFichero.length - 1];
                        }
                        objVoucher.setUrl_comprobante(ficheroNombre);
                        // voucheRepo.save(objVoucher);
                        this.createComprobante(objVoucher,voucher);
                }
            } catch (Exception e) {
                throw new ApplicationException(EnumException.ACTIVO_FICHEROS_ADD_FILE_003);
            }
        }
    }
    @Transactional
    public void createComprobante(ActivoServicioProveedorMantEntity objVoucher, MultipartFile objfile) throws IOException{
        URL urlPath= this.getClass().getResource("/");
        if (objVoucher!=null) {
            if (objVoucher.getUrl_comprobante()!="") {
                byte[] bytesFichero=objfile.getBytes();
                File fileruta=new File(urlPath.getPath()+"static/ficheros/Comprobantes/");
                if(!fileruta.exists()){
                    fileruta.mkdirs();
                }
                try (BufferedOutputStream buffStream=new BufferedOutputStream(new FileOutputStream(new File(urlPath.getPath()+"static/ficheros/Comprobantes/"+objVoucher.getUrl_comprobante())))) {
                    buffStream.write(bytesFichero);
                } catch (Exception e) {
                    throw new ApplicationException(EnumException.ACTIVO_FICHEROS_ADD_FILE_003);
                }   
            } else {
                throw new ApplicationException(EnumException.ACTIVO_FICHEROS_ADD_FILE_004);
            }
        } else {
            throw new ApplicationException(EnumException.ACTIVO_CREATE_001);
        }
    }



    @Transactional
    public void ValidarProveedor(int idServicioProveedorManto, Boolean aceptado){
        if (idServicioProveedorManto>0) {
            ActivoServicioProveedorMantEntity objProveedorServicio=serviProveeMant.findByIdServicioProveedorMant(idServicioProveedorManto);
            LocalDateTime ldtnow =LocalDateTime.now();
            objProveedorServicio.setCreacionFecha(ldtnow);
            objProveedorServicio.setModificacionFecha(ldtnow);
            objProveedorServicio.setAceptado(aceptado);
            serviProveeMant.save(objProveedorServicio);
        }
    }


    /*CREAR UN JSON DE LOS SERVICIOS PROVEEDORES MANTO REGISTRADOS. */
    public JsonObject jsonActivoServicioProveedorMant(int idDetalleMantenimiento) {
        JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
        JsonArrayBuilder jsonRows = Json.createArrayBuilder();
        List<ActivoServicioProveedorMantEntity> lstActServicioProveedorMant = serviProveeMant.lstActivoServProveedorMant(idDetalleMantenimiento);
        BigDecimal pricenull= BigDecimal.ZERO;
        lstActServicioProveedorMant.forEach((item) -> {
            jsonRows.add(Json.createObjectBuilder()
            .add("id_servicio_proveedor_manto", item.getIdServicioProveedorMant())
            .add("observaciones", item.getObservaciones()!=null ? item.getObservaciones() : "")
            .add("precio_servicio_proveedor", item.getPrecioServicioProveedor()!=null ? item.getPrecioServicioProveedor() : pricenull)
            .add("url_cotizacion", item.getUrlCotizacion()!=null ? item.getUrlCotizacion() : "")

            .add("id_servicio_proveedor", item.getActivoServicioProveedor().getIdServicioProveedor())
            .add("id_proveedor",item.getActivoServicioProveedor().getActivoProveedor().getIdProveedor())
            .add("nombre_proveedor",item.getActivoServicioProveedor().getActivoProveedor().getProveedor())

            .add("id_bien_detalle_manto", item.getBienDetalleMant().getIdDetalleMantenimiento())
            .add("id_servicio",item.getActivoServicioProveedor().getActivoServicio().getIdServicioActivo())
            .add("nombre_servicio",item.getActivoServicioProveedor().getActivoServicio().getDescripcion())
            .add("tipo_activo", item.getBienDetalleMant().getBienActivo().getIdActivo().getIdCatalogoActivo())
            .add("nombre_tipo_activo", item.getBienDetalleMant().getBienActivo().getIdActivo().getNombre())
            .add("aceptado",item.isAceptado())
            .add("pagado", item.isPagado())

            );
        });
        jsonReturn.add("rows", jsonRows);
        return jsonReturn.build();
    }


    
    /*UN OBJETO DE LOS PROVEEDORES ACEPTADOS. */
    public JsonObject jsonServicioProvAceptado(int idDetalleMantenimiento) {
        JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
        JsonArrayBuilder jsonRows = Json.createArrayBuilder();
        List<ActivoServicioProveedorMantEntity> lstActServicioProveedorMant = serviProveeMant.lstServProvAceptado(idDetalleMantenimiento);
        BigDecimal pricenull= BigDecimal.ZERO;
        lstActServicioProveedorMant.forEach((item) -> {
            jsonRows.add(Json.createObjectBuilder()
            .add("id_servicio_proveedor_manto", item.getIdServicioProveedorMant())
            .add("observaciones", item.getObservaciones()!=null ? item.getObservaciones() : "")
            .add("precio_servicio_proveedor", item.getPrecioServicioProveedor()!=null ? item.getPrecioServicioProveedor() : pricenull)
            .add("url_cotizacion", item.getUrlCotizacion()!=null ? item.getUrlCotizacion() : "")

            .add("id_servicio_proveedor", item.getActivoServicioProveedor().getIdServicioProveedor())
            .add("id_proveedor",item.getActivoServicioProveedor().getActivoProveedor().getIdProveedor())
            .add("nombre_proveedor",item.getActivoServicioProveedor().getActivoProveedor().getProveedor())

            .add("id_bien_detalle_manto", item.getBienDetalleMant().getIdDetalleMantenimiento())
            .add("id_servicio",item.getActivoServicioProveedor().getActivoServicio().getIdServicioActivo())
            .add("nombre_servicio",item.getActivoServicioProveedor().getActivoServicio().getDescripcion())
            .add("tipo_activo", item.getBienDetalleMant().getBienActivo().getIdActivo().getIdCatalogoActivo())
            .add("nombre_tipo_activo", item.getBienDetalleMant().getBienActivo().getIdActivo().getNombre())
            .add("aceptado",item.isAceptado())
            .add("pagado", item.isPagado())
            .add("url_comprobante", item.getUrl_comprobante()!=null ? item.getUrl_comprobante() : "")
            .add("fechapago", item.getFechaPagoNatural().equals(null) ? "" : item.getFechaPagoNatural())
            );
        });
        jsonReturn.add("aceptado", jsonRows);
        return jsonReturn.build();
    }

}