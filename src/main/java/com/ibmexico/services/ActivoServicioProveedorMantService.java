
package com.ibmexico.services;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.UUID;

import javax.transaction.Transactional;

import com.ibmexico.entities.ActivoServicioProveedorMantEntity;
import com.ibmexico.entities.BienDetalleMantenimientoEntity;
import com.ibmexico.entities.UsuarioEntity;
import com.ibmexico.libraries.notifications.ApplicationException;
import com.ibmexico.libraries.notifications.EnumException;
import com.ibmexico.repositories.IActivoServicioProveedorMantRepository;
import com.ibmexico.repositories.IActivoServicioProveedorRepository;
import com.ibmexico.repositories.IBienDetalleMantenimientoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service("activo_servicio_proveedor_mant_service")
public class ActivoServicioProveedorMantService{

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

    //Agregar una arreglo de servicios-proveedores
    @Transactional
    public void addServicioProveedor(BienDetalleMantenimientoEntity objDetalle, String [] arrObservaciones, BigDecimal [] arrprecioServProv,
    int [] arridActivoServicioProveedor, MultipartFile [] arrcotizacion){
        if (objDetalle!=null) {
            if (arridActivoServicioProveedor!=null &&  arrprecioServProv!=null && arrObservaciones!=null && arrcotizacion!=null) {
                System.err.println(arrcotizacion.length);
                System.err.println(arrObservaciones);
                System.err.println(arrcotizacion);
                for (int i = 0; i < arridActivoServicioProveedor.length; i++) {
                    ActivoServicioProveedorMantEntity objActivoSerProvMant=new ActivoServicioProveedorMantEntity();
                    LocalDateTime ldtnow=LocalDateTime.now();
                    UsuarioEntity objUsuario= sesionService.getCurrentUser();
                    objActivoSerProvMant.setCreacionFecha(ldtnow);
                    objActivoSerProvMant.setModificacionFecha(ldtnow);
                    objActivoSerProvMant.setCreacionUsuario(objUsuario);
                    objActivoSerProvMant.setModificacionUsuario(objUsuario);
                    objActivoSerProvMant.setBienDetalleMant(bienDetalleMant.findByIdDetalleMantenimiento(objDetalle.getIdDetalleMantenimiento()));
                    objActivoSerProvMant.setActivoServicioProveedor(activoServProv.findByIdServicioProveedor(arridActivoServicioProveedor[i]));
                    objActivoSerProvMant.setPrecioServicioProveedor(arrprecioServProv[i]);
                    objActivoSerProvMant.setObservaciones(arrObservaciones[i]);
                    addCotizacion(objActivoSerProvMant, arrcotizacion[i]);
                    // serviProveeMant.save(objActivoSerProvMant);
                }
            }
        }
    }


    @Transactional
    public void createFicheroCotizacion(ActivoServicioProveedorMantEntity objServProvMant, MultipartFile objFile)throws IOException{
        URL urlPath=this.getClass().getResource("/");

        if (objServProvMant!=null) {
            if (objServProvMant.getUrlCotizacion() !="") {
                byte [] bytesFichero= objFile.getBytes();
                try (BufferedOutputStream buffStream=new BufferedOutputStream(new FileOutputStream(new 
                    File(urlPath.getPath()+"static/ficheros/ProveedorCotizacion/"+objServProvMant.getUrlCotizacion())))) {
                    buffStream.write(bytesFichero);
                    // LocalDateTime ldtnow=LocalDateTime.now();
                    // objServProvMant.setModificacionUsuario(objUsurioCreacion);
                    serviProveeMant.save(objServProvMant);
                } catch (Exception e) {
                    throw new ApplicationException(EnumException.ACTIVO_FICHEROS_ADD_FILE_003);
                }
            }else {
                throw new ApplicationException(EnumException.ACTIVO_FICHEROS_ADD_FILE_004);
            }
        }else{
            throw new ApplicationException(EnumException.ACTIVO_CREATE_001);
        }
    }

    @Transactional
    public void addCotizacion(ActivoServicioProveedorMantEntity objServProvMant, MultipartFile file){
        if (objServProvMant!=null) {
            if(file!=null && !file.isEmpty()){
                try {
                    if (!file.getOriginalFilename().trim().equals("")) {
                        String ficheroNombre= UUID.randomUUID().toString();
                        String [] arrNombreFichero= file.getOriginalFilename().split("\\.");
                        if (arrNombreFichero.length>=2) {
                            ficheroNombre=ficheroNombre+"."+arrNombreFichero[arrNombreFichero.length-1];
                        }
                        System.err.println("file no esta vacio");
                        objServProvMant.setUrlCotizacion(ficheroNombre);
                        createFicheroCotizacion(objServProvMant, file);
                    }
                } catch (Exception e) {
                    throw new ApplicationException(EnumException.ACTIVO_FICHEROS_ADD_FILE_003);
                }
            }else{
                LocalDateTime ldtnow=LocalDateTime.now();
                UsuarioEntity objUsurioCreacion=sesionService.getCurrentUser();
                objServProvMant.setCreacionFecha(ldtnow);
                objServProvMant.setModificacionFecha(ldtnow);
                objServProvMant.setCreacionUsuario(objUsurioCreacion);
                objServProvMant.setModificacionUsuario(objUsurioCreacion);
                serviProveeMant.save(objServProvMant);
            }           
        }else{
            throw new ApplicationException(EnumException.ACTIVO_FICHEROS_ADD_FILE_001);
        }
    }
}