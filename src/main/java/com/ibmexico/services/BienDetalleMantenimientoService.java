package com.ibmexico.services;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import com.ibmexico.entities.BienDetalleMantenimientoEntity;
import com.ibmexico.entities.UsuarioEntity;
import com.ibmexico.libraries.DataTable;
import com.ibmexico.libraries.notifications.ApplicationException;
import com.ibmexico.libraries.notifications.EnumException;
import com.ibmexico.repositories.IBienDetalleMantenimientoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service("bienDetalleMantService")
public class BienDetalleMantenimientoService{

    @Autowired
    @Qualifier("bienDetalleMantenimientoRepository")
    private IBienDetalleMantenimientoRepository bienMantRep;

    @Autowired
    @Qualifier("sessionService")
    private SessionService sesionService;

    @Autowired
    @Qualifier("activo_servicio_proveedor_mant_service")
    private ActivoServicioProveedorMantService activoServProvMantService;
    
    //Encontrar un elemento detalle mantenimiento,en consideracion con el param ID .
    public BienDetalleMantenimientoEntity findByIdBienDetalleMantenimiento(int idDetalleMantenimiento){
        return bienMantRep.findByIdDetalleMantenimiento(idDetalleMantenimiento);
    }

    //Registro de un Activo a Mantenimiento, con los sig, parametros, para crear es necesario, registrar los servicios que requieren dicho mant.
    // servicios, que integran de varios proveedores.
    synchronized public void create(BienDetalleMantenimientoEntity objDetalle, String [] txtObservaciones, BigDecimal [] precioServProv,
        int [] idActivoServicioProveedor, MultipartFile [] cotizacion, String imgDetalleActivo) throws IOException {
        if(objDetalle!=null){
            UsuarioEntity objUser=sesionService.getCurrentUser();
            LocalDateTime ldtNow = LocalDateTime.now();
            objDetalle.setCreacionUsuario(objUser);
            objDetalle.setModificacionUsuario(objUser);
            objDetalle.setCreacionFecha(ldtNow);
            objDetalle.setModificacionFecha(ldtNow);

            if (imgDetalleActivo!=null && !imgDetalleActivo.equals("") && imgDetalleActivo.length()>0) {
                String urlDetalleActivo= UUID.randomUUID().toString()+".png";
                objDetalle.setUrlDetalleMantenimiento(urlDetalleActivo);
                addFileDetalleActivo(objDetalle, imgDetalleActivo);         
            }else{
                bienMantRep.save(objDetalle);
            }
            //Si este no esta vacio, se invoca el  sig. method, donde registra los multiples proveedores disponibles para el Mantenimiento.
            if (idActivoServicioProveedor!=null) {
                activoServProvMantService.addServicioProveedor(objDetalle, txtObservaciones, precioServProv, idActivoServicioProveedor, cotizacion);
            }
        }else{
            throw new ApplicationException(EnumException.ACTIVO_CREATE_001);
        }
    }
   
    
    
    /*Metodo de crear cuando existan servicios, pero se desea agregar un nuevo servicio */
    synchronized public void createEdit(BienDetalleMantenimientoEntity objDetalle, String [] txtObservaciones, BigDecimal [] precioServProv,
        int [] idActivoServicioProveedor, MultipartFile [] cotizacion, String imgDetalleActivo,int lengthManto, int lengthNuevo) throws IOException {

        if(objDetalle!=null){
            UsuarioEntity objUser=sesionService.getCurrentUser();
            LocalDateTime ldtNow = LocalDateTime.now();
            objDetalle.setCreacionUsuario(objUser);
            objDetalle.setModificacionUsuario(objUser);
            objDetalle.setCreacionFecha(ldtNow);
            objDetalle.setModificacionFecha(ldtNow);

            if (imgDetalleActivo!=null && !imgDetalleActivo.equals("") && imgDetalleActivo.length()>0) {
                String urlDetalleActivo= UUID.randomUUID().toString()+".png";
                objDetalle.setUrlDetalleMantenimiento(urlDetalleActivo);
                addFileDetalleActivo(objDetalle, imgDetalleActivo);         
            }else{
                bienMantRep.save(objDetalle);
            }
            //Si este no esta vacio, se invoca el  sig. method, donde registra los multiples proveedores disponibles para el Mantenimiento.
            if (idActivoServicioProveedor!=null && lengthManto!=0 && lengthNuevo!=0) {
                activoServProvMantService.addServicioProveedorEdit(objDetalle, txtObservaciones, precioServProv, idActivoServicioProveedor, cotizacion, lengthManto, lengthNuevo);
            }
        }else{
            throw new ApplicationException(EnumException.ACTIVO_CREATE_001);
        }
    }


        
    /*Metodo de crear cuando existan servicios, pero se desea agregar un nuevo servicio */
    synchronized public void createUpdate(BienDetalleMantenimientoEntity objDetalle, String [] txtObservaciones, BigDecimal [] precioServProv,
        int [] idActivoServicioProveedor, MultipartFile [] cotizacion, String imgDetalleActivo,int [] ServicioProveedorManto) throws IOException {
        if(objDetalle!=null){
            UsuarioEntity objUser=sesionService.getCurrentUser();
            LocalDateTime ldtNow = LocalDateTime.now();
            objDetalle.setCreacionUsuario(objUser);
            objDetalle.setModificacionUsuario(objUser);
            objDetalle.setCreacionFecha(ldtNow);
            objDetalle.setModificacionFecha(ldtNow);

            if (imgDetalleActivo!=null && !imgDetalleActivo.equals("") && imgDetalleActivo.length()>0) {
                String urlDetalleActivo= UUID.randomUUID().toString()+".png";
                objDetalle.setUrlDetalleMantenimiento(urlDetalleActivo);
                addFileDetalleActivo(objDetalle, imgDetalleActivo);         
            }else{
                bienMantRep.save(objDetalle);
            }
            //Si este no esta vacio, se invoca el  sig. method, donde registra los multiples proveedores disponibles para el Mantenimiento.
            if (idActivoServicioProveedor!=null && ServicioProveedorManto!=null) {
                activoServProvMantService.addServicioProveedorUpdate(objDetalle, txtObservaciones, precioServProv, idActivoServicioProveedor, cotizacion,ServicioProveedorManto);
            }
        }else{
            throw new ApplicationException(EnumException.ACTIVO_CREATE_001);
        }
    }



    //Method for save file png, indicador detalles del activo en mantenimiento
    @Transactional
    public void addFileDetalleActivo(BienDetalleMantenimientoEntity objDetalle, String imgDetalleActivo) throws IOException{
        URL urlPath=this.getClass().getResource("/");
        if (objDetalle!=null) {
            try {
                byte[] byteFichero=Base64.getDecoder().decode(imgDetalleActivo);
                BufferedImage img= ImageIO.read(new ByteArrayInputStream(byteFichero));
                File imgFile=new File(urlPath.getPath()+"static/ficheros/detalleMantenimiento/"+objDetalle.getUrlDetalleMantenimiento());
                ImageIO.write(img,"png",imgFile);
                bienMantRep.save(objDetalle);
            } catch (Exception excepcion) {
                throw new ApplicationException(EnumException.ENTREGAS_FICHEROS_ADD_FILE_002);
            }
        }
    }

    public void  update(BienDetalleMantenimientoEntity bienDetalleManto){
        if (bienDetalleManto!=null) {
            LocalDateTime ldtnow=LocalDateTime.now();
            UsuarioEntity objUsuarioCreacion=sesionService.getCurrentUser();
            bienDetalleManto.setModificacionFecha(ldtnow);
            bienDetalleManto.setModificacionUsuario(objUsuarioCreacion);
            bienMantRep.save(bienDetalleManto);
        }else{
            throw new ApplicationException(EnumException.ACTIVIDADES_CREATE_001);
        }
    }

    //Tabla detalle de los activos en mantenimiento.
    public DataTable<BienDetalleMantenimientoEntity> dataTable(String search, int offset, int limit, String txtBootstrapTableDesde, String txtBootstrapTableHasta){
        List<BienDetalleMantenimientoEntity> lstDetalleMantenEntity=null;
        LocalDate fechaInicio=null;
        LocalDate fechaFin=null;
        long totalDetalle=100;
        if(search!=null){
            if(!txtBootstrapTableDesde.equals("") && !txtBootstrapTableHasta.equals("")){
                String arrFechaInicio[]= txtBootstrapTableDesde.split("/");
                int yearInicio=Integer.parseInt(arrFechaInicio[2]);
                int monthInicio=Integer.parseInt(arrFechaInicio[1]);
                int dayInicio=Integer.parseInt(arrFechaInicio[0]);

                String arrFechaFin[]=txtBootstrapTableHasta.split("/");
                int yearFin=Integer.parseInt(arrFechaFin[2]);
                int monthFin= Integer.parseInt(arrFechaFin[1]);
                int dayFin= Integer.parseInt(arrFechaFin[0]);

                fechaInicio=LocalDate.of(yearInicio, monthInicio, dayInicio);
                fechaFin=LocalDate.of(yearFin, monthFin, dayFin);

                lstDetalleMantenEntity=bienMantRep.findForDataTable(search, fechaInicio, fechaFin, DataTable.getPageRequest(offset, limit));
                totalDetalle=bienMantRep.countForDataTable(search, fechaInicio, fechaFin);
            }
            else{
                lstDetalleMantenEntity=bienMantRep.findForDataTable(search, DataTable.getPageRequest(offset, limit));
                totalDetalle=bienMantRep.countForDataTable(search);
            }
        }else{
            lstDetalleMantenEntity=bienMantRep.findForDataTable(DataTable.getPageRequest(offset, limit));
            totalDetalle=bienMantRep.countForDataTable();
        }
        
        DataTable<BienDetalleMantenimientoEntity> returnDataTable=new DataTable<BienDetalleMantenimientoEntity>(lstDetalleMantenEntity, totalDetalle);
        return returnDataTable;
    }


    /**los metodos de tipo json */
    public JsonObject jsonBienDetalleMantId(int idDetalleMantenimiento){
        JsonObjectBuilder jsonReturn =Json.createObjectBuilder();
        JsonArrayBuilder jsonRows=Json.createArrayBuilder();
        BienDetalleMantenimientoEntity lstBienDetalleMant=bienMantRep.findByIdDetalleMantenimiento(idDetalleMantenimiento);
        BigDecimal gastonull= BigDecimal.ZERO;
        jsonRows.add(Json.createObjectBuilder()
            .add("id_detalle_manto", lstBienDetalleMant.getIdDetalleMantenimiento())
            .add("fecha_manto_programada", lstBienDetalleMant.getFechaMantenimientoProgramadaFechaNatural())
            .add("finalizado", lstBienDetalleMant.isFinalizado())
            .add("observaciones", lstBienDetalleMant.getObservaciones())
            .add("id_bien_activo", lstBienDetalleMant.getBienActivo().getIdRecursoActivo())
            .add("gasto_aproximado", lstBienDetalleMant.getGastoAproximado()!=null ? lstBienDetalleMant.getGastoAproximado() : gastonull)
            .add("url_detalle_manto", lstBienDetalleMant.getUrlDetalleMantenimiento()!=null ? lstBienDetalleMant.getUrlDetalleMantenimiento() : "")
            .add("id_tipo_activo", lstBienDetalleMant.getBienActivo().getIdActivo().getIdCatalogoActivo())
            .add("clave_unica", lstBienDetalleMant.getBienActivo().getNumeroEconomico())
            .add("estatusManto", lstBienDetalleMant.getActivoEstatus().getIdActivoEstatus())
        );
        jsonReturn.add("bienDetalleManto", jsonRows);
        return  jsonReturn.build();
    }

}