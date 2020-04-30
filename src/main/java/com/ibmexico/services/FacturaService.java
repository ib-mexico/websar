package com.ibmexico.services;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.UUID;

import javax.transaction.Transactional;

import com.ibmexico.entities.FacturaEntity;
import com.ibmexico.entities.UsuarioEntity;
import com.ibmexico.libraries.notifications.ApplicationException;
import com.ibmexico.libraries.notifications.EnumException;
import com.ibmexico.repositories.IFacturaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service("factura_gasto_service")
public class FacturaService{

    @Autowired
    @Qualifier("factura_gastos")
    public IFacturaRepository facturaRepo;

    @Autowired
    @Qualifier("sessionService")
    private SessionService sesionService;

    public FacturaEntity findByIdFactura(int idFactura){
        return facturaRepo.findByIdFactura(idFactura);
    }

    public void createFactura(FacturaEntity objFactura){
        if(objFactura != null){
            LocalDateTime ldtnow = LocalDateTime.now();
            objFactura.setCreacionFecha(ldtnow);
            objFactura.setModificacionFecha(ldtnow);
            objFactura.setIsEliminado(false);
            UsuarioEntity objUsuario = sesionService.getCurrentUser();
            objFactura.setCreacionUsuario(objUsuario);
            objFactura.setModificacionUsuario(objUsuario);
            facturaRepo.save(objFactura);
        }
    }

@Transactional
public void addFactura(FacturaEntity objFactura, MultipartFile file)  throws IOException {
    if (objFactura != null) {
        if (file != null && !file.isEmpty()) {
            try {
                if (!file.getOriginalFilename().trim().equals("")) {
                    String ficheroNombre = UUID.randomUUID().toString();
                    String[] arrNombreFichero = file.getOriginalFilename().split("\\.");
                    if (arrNombreFichero.length >= 2) {
                        ficheroNombre = ficheroNombre + "." + arrNombreFichero[arrNombreFichero.length - 1];
                    }
                    // System.err.println("file no esta vacio factura");
                    objFactura.setUrl(ficheroNombre);
                    createFicheroFactura(objFactura, file);
                }
            } catch (Exception e) {
                throw new ApplicationException(EnumException.ACTIVO_FICHEROS_ADD_FILE_003);
            }
        }
    } else {
        throw new ApplicationException(EnumException.ACTIVO_FICHEROS_ADD_FILE_001);
    }
}

@Transactional
public void createFicheroFactura(FacturaEntity objFactura, MultipartFile objFile)
        throws IOException {
    URL urlPath = this.getClass().getResource("/");
    if (objFactura != null) {
        if (objFactura.getUrl() != "") {
            // Crear carpeta si no existe
            File fileruta = new File(urlPath.getPath() + "static/ficheros/FacturasGastos");
            if (!fileruta.exists()) {
                fileruta.mkdirs();
            }
            try (BufferedOutputStream buffStream = new BufferedOutputStream( new FileOutputStream(new File(urlPath.getPath() + "static/ficheros/FacturasGastos/"+objFactura.getUrl())))) {
                byte[] bytesFichero = objFile.getBytes();
                buffStream.write(bytesFichero);
                createFactura(objFactura);
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


}