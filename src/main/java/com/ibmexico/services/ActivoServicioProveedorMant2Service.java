
package com.ibmexico.services;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.UUID;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.transaction.Transactional;

import com.ibmexico.entities.ActivoServicioProveedorMant2Entity;
import com.ibmexico.entities.UsuarioEntity;
import com.ibmexico.libraries.DataTable;
import com.ibmexico.libraries.notifications.ApplicationException;
import com.ibmexico.libraries.notifications.EnumException;
import com.ibmexico.repositories.IActivoEstatusRepository;
import com.ibmexico.repositories.IActivoServicioProveedorMant2Repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Service("activo_servicio_proveedor_mant2_service")
public class ActivoServicioProveedorMant2Service {

    @Autowired
    @Qualifier("sessionService")
    private SessionService sesionService;

    @Autowired
    @Qualifier("activo_servicio_proveedor_mant2_repo")
    private IActivoServicioProveedorMant2Repository serviProveeMant;

    @Autowired
    @Qualifier("activo_estatus_repository")
    private IActivoEstatusRepository activoEstatusRepo;

    /*return un registro de servicio-proveedor*/
    public ActivoServicioProveedorMant2Entity findByIdServicioProveedorMant(int idServicioProvMant){
        return serviProveeMant.findByIdServicioProveedorMant(idServicioProvMant);
    }
    /* Regresar una lista de gastos para el reporte */
    public List<ActivoServicioProveedorMant2Entity> findByGastoUsuarioFechaTipo(LocalDate ldFechaInicio, LocalDate ldFechaFin, int idUsuario, int idTipoGasto, int idEmpresa){
        return serviProveeMant.findByGastoUsuarioFechaTipo(ldFechaInicio, ldFechaFin, idUsuario,idTipoGasto, idEmpresa);
    }
    public List<ActivoServicioProveedorMant2Entity> findByGastoUsarioFecha(LocalDate ldFechaInicio, LocalDate ldFechaFin, int idUsuario, int idEmpresa){
        return serviProveeMant.findByGastoUsarioFecha(ldFechaInicio, ldFechaFin, idUsuario, idEmpresa);
    }

    public List<ActivoServicioProveedorMant2Entity> findByGastoTipoFecha(LocalDate ldFechaInicio, LocalDate ldFechaFin, int idTipoGasto, int idEmpresa){
        return serviProveeMant.findByGastoTipoFecha(ldFechaInicio, ldFechaFin, idTipoGasto, idEmpresa);
    }

    public List<ActivoServicioProveedorMant2Entity> findByGastoFecha(LocalDate ldFechaInicio, LocalDate ldFechaFin, int idEmpresa){
        return serviProveeMant.findByGastoFecha(ldFechaInicio, ldFechaFin, idEmpresa);
    }



    /**Registro de gastos generales */
    public void addGastoGeneral(ActivoServicioProveedorMant2Entity objGasto){
        if(objGasto!=null){
            LocalDateTime ldtnow=LocalDateTime.now();
            objGasto.setCreacionFecha(ldtnow);
            objGasto.setModificacionFecha(ldtnow);
            objGasto.setIsGasto(true);
            objGasto.setIsEliminado(false);

            UsuarioEntity objUsuario=sesionService.getCurrentUser();
            objGasto.setCreacionUsuario(objUsuario);
            objGasto.setModificacionUsuario(objUsuario);
            serviProveeMant.save(objGasto);
        }
    }
    
    @Transactional
    public void addCotizacion(ActivoServicioProveedorMant2Entity objServProvMant, MultipartFile file) {
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
                        addGastoGeneral(objServProvMant);
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
    public void createFicheroCotizacion(ActivoServicioProveedorMant2Entity objServProvMant, MultipartFile objFile)
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

     //Tabla detalle de los gastos
     public DataTable<ActivoServicioProveedorMant2Entity> dataTable(String search, int offset, int limit, String txtBootstrapTableDesde, String txtBootstrapTableHasta, int filtro){
        List<ActivoServicioProveedorMant2Entity> lstGastos=null;
        LocalDate fechaInicio=null;
        LocalDate fechaFin=null;
        long totalDetalle=100;
        
        LocalDate ldtnow=LocalDate.now();
        /*System.err.println(ldtnow.with(TemporalAdjusters.lastDayOfMonth()));*/
        if(filtro>0){
            if (filtro==1) {
                String partesDesde []=ldtnow.withDayOfMonth(1).toString().split("-");
                String partesHasta[]=ldtnow.toString().split("-");
                txtBootstrapTableDesde=partesDesde[2]+"/"+partesDesde[1]+"/"+partesDesde[0];
                txtBootstrapTableHasta=partesHasta[2]+"/"+partesHasta[1]+"/"+partesHasta[0];
            }else if (filtro==2) {
                /* System.err.println("mes actual - 3 meses");*/
                LocalDate mes3 = ldtnow.plusMonths(-3).with(TemporalAdjusters.firstDayOfMonth());
                String partesDesde []=mes3.toString().split("-");
                String partesHasta[]=ldtnow.toString().split("-");
                txtBootstrapTableDesde=partesDesde[2]+"/"+partesDesde[1]+"/"+partesDesde[0];
                txtBootstrapTableHasta=partesHasta[2]+"/"+partesHasta[1]+"/"+partesHasta[0];
            }
            else if(filtro==3){
                /*Busqueda por fecha un rango de 6 meses */
                if(ldtnow.getMonthValue()>6){
                    LocalDate mes6=ldtnow.plusMonths(-6).with(TemporalAdjusters.firstDayOfMonth());
                    String partesDesde []=mes6.toString().split("-");
                    String partesHasta[]=ldtnow.toString().split("-");
                    txtBootstrapTableDesde=partesDesde[2]+"/"+partesDesde[1]+"/"+partesDesde[0];
                    txtBootstrapTableHasta=partesHasta[2]+"/"+partesHasta[1]+"/"+partesHasta[0];
                }else{
                    String partesDesde []=ldtnow.with(TemporalAdjusters.firstDayOfYear()).toString().split("-");                
                    String partesHasta[]=ldtnow.minusMonths(6).with(TemporalAdjusters.lastDayOfMonth()).toString().split("-");
                    txtBootstrapTableDesde=partesDesde[2]+"/"+partesDesde[1]+"/"+partesDesde[0];
                    txtBootstrapTableHasta=partesHasta[2]+"/"+partesHasta[1]+"/"+partesHasta[0];
                }
            }
            else if(filtro==4){
                // String partesDesde []=ldtnow.with(TemporalAdjusters.firstDayOfYear()).toString().split("-");
                String partesDesde []=ldtnow.plusDays(-365).toString().split("-");
                String partesHasta[]=ldtnow.toString().split("-");
                txtBootstrapTableDesde=partesDesde[2]+"/"+partesDesde[1]+"/"+partesDesde[0];
                txtBootstrapTableHasta=partesHasta[2]+"/"+partesHasta[1]+"/"+partesHasta[0];
            }
        }
        
        if(search!=null){
            if(sesionService.hasRol("DETALLE_GASTO")){
                System.err.println("entro con permiso detalle_gasto");
                if(!txtBootstrapTableDesde.equals("") && !txtBootstrapTableHasta.equals("")){
                    System.err.println("en if buscando");
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
    
                    lstGastos=serviProveeMant.findForDataTable(search, fechaInicio, fechaFin, DataTable.getPageRequest(offset, limit));
                    totalDetalle=serviProveeMant.countForDataTable(search, fechaInicio, fechaFin);
                }
                else{
                    System.err.println("buscador vacio");
                    lstGastos=serviProveeMant.findForDataTableGasto(search, DataTable.getPageRequest(offset, limit));
                    totalDetalle=serviProveeMant.countForDataTableGasto(search);
                }
            }
            /**Tabla de consulta para los registros de mantenimiento */ 
            else if(sesionService.hasRol("DETALLE_INDEX")){
                if(!txtBootstrapTableDesde.equals("") && !txtBootstrapTableHasta.equals("")){
                    System.err.println("en if buscando");
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
    
                    lstGastos=serviProveeMant.findForDataTableManto(search, fechaInicio, fechaFin, DataTable.getPageRequest(offset, limit));
                    totalDetalle=serviProveeMant.countForDataTableManto(search, fechaInicio, fechaFin);
                }
                else{
                    System.err.println("buscador vacio");
                    lstGastos=serviProveeMant.findForDataTableManto(search, DataTable.getPageRequest(offset, limit));
                    totalDetalle=serviProveeMant.countForDataTableManto(search);
                }
            } 
            /**End session user mantenimiento */
        }else{
            System.err.println("search vacio");
            lstGastos=serviProveeMant.findForDataTable(DataTable.getPageRequest(offset, limit));
            totalDetalle=serviProveeMant.countForDataTable();
        }
        
        DataTable<ActivoServicioProveedorMant2Entity> returnDataTable=new DataTable<ActivoServicioProveedorMant2Entity>(lstGastos, totalDetalle);
        return returnDataTable;
    }


    public JsonObject jsonGastoById(int idGasto){
        JsonObjectBuilder jsonReturn=Json.createObjectBuilder();
        JsonArrayBuilder jrows=Json.createArrayBuilder();
        List<ActivoServicioProveedorMant2Entity> lstGasto=serviProveeMant.findByGasto(idGasto);
        lstGasto.forEach((item)->{
            jrows.add(Json.createObjectBuilder().
            add("id_gasto",item.getIdServicioProveedorMant())
            .add("id_empresa", item.getEmpresa().getIdEmpresa())
            .add("id_tipoFichero", item.getCotizacionFichero()!=null ? item.getCotizacionFichero().getIdCotizacionFichero() :0)
            .add("id_factura", item.getFacturaGasto().getIdFactura())
            .add("factura_nombre", item.getFacturaGasto().getNumeroFactura())
            .add("format_address",item.getFormatted_address()!=null ? item.getFormatted_address() : "N/A")
            .add("ciudad", item.getCiudad()!=null ? item.getCiudad() :"N/A")
            .add("estado", item.getEstado()!=null ? item.getEstado() : "N/A")
            .add("pais", item.getPais()!=null ?  item.getPais(): "N/A")
            .add("fecha_gasto", item.getFechaPagoNatural())
            .add("subtotal", item.getPrecioServicioProveedor())
            .add("id_tipoGasto", item.getTipoGasto()!=null ? item.getTipoGasto().getIdTipoGasto() :0)
            .add("id_clasificacion", item.getClasificacionTipoGasto().getIdClasificacion())
            .add("id_proveedor", item.getProveedor().getIdProveedor())
            .add("id_cotizacion", item.getCotizacion()!=null ? item.getCotizacion().getIdCotizacion(): 0)
            .add("folio_cotizacion", item.getFolioCotizacion()!=null ? item.getFolioCotizacion() : "N/A")
            .add("checkCotizacion", item.getPerteneceCotizacion())
            .add("checkGastoParcial", item.getIsGastoParcial()!=null ? item.getIsGastoParcial() : false)
            .add("usuario", item.getUsuario().getIdUsuario())
            .add("usuario_departamento", item.getUsuario().getDepartamento()!=null ? item.getUsuario().getDepartamento().getDepartamento() : "N/A")
            .add("usuario_sucursal", item.getUsuario().getSucursal().getSucursal())
            .add("observaciones", item.getObservaciones()!=null ? item.getObservaciones() : "N/A")
            );
        });
        jsonReturn.add("gastoSelect", jrows);
        return jsonReturn.build();
    }

    public void update(ActivoServicioProveedorMant2Entity objGasto){
        if(objGasto!=null){
            LocalDateTime ldtNow=LocalDateTime.now();
            UsuarioEntity objUsuarioCreacion=sesionService.getCurrentUser();
            objGasto.setModificacionFecha(ldtNow);
            objGasto.setModificacionUsuario(objUsuarioCreacion);
            objGasto.setIsEliminado(true);
            serviProveeMant.save(objGasto);
        }else{
            throw new ApplicationException(EnumException.ACTIVO_CREATE_001);
        }
    }

}