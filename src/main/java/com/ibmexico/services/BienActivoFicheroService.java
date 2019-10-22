package com.ibmexico.services;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import com.ibmexico.entities.BienActivoFicheroEntity;
import com.ibmexico.libraries.notifications.ApplicationException;
import com.ibmexico.libraries.notifications.EnumException;
import com.ibmexico.repositories.IBienActivoFicheroRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service("bienActivoFicheroService")
public class BienActivoFicheroService{

    @Autowired
    @Qualifier("sessionService")
    private SessionService sessionService;

    @Autowired
    @Qualifier("bienActivoFicheroRepository")
    private IBienActivoFicheroRepository bienActFicheRepo;

    public List<BienActivoFicheroEntity> listaActivoFichero(int idBienActivo){
        return bienActFicheRepo.findByBienActivoIdRecursoActivo(idBienActivo);  
    }


    public void create(BienActivoFicheroEntity objActivoFichero, MultipartFile objFile)throws IOException{
        URL urlPath=this.getClass().getResource("/");
        
        if(objActivoFichero!=null){
            if(objActivoFichero.getUrl()!=""){
                byte[] byteFichero=objFile.getBytes();
                try(BufferedOutputStream buffStream = new BufferedOutputStream(new FileOutputStream(new File(urlPath.getPath() + "static/ficheros/activoFijos/"+ objActivoFichero.getUrl())))){
                    buffStream.write(byteFichero);
                    LocalDateTime ldtfecha=LocalDateTime.now();
                    objActivoFichero.setCreacionUsuario(sessionService.getCurrentUser());
                    objActivoFichero.setCreacionFecha(ldtfecha);
                    bienActFicheRepo.save(objActivoFichero);
                } catch (Exception  exception) {
                    throw new ApplicationException(EnumException.ACTIVO_FICHEROS_ADD_FILE_003);
                }
            }else{
                throw new ApplicationException(EnumException.ACTIVO_FICHEROS_ADD_FILE_004);
            }
        }else{
            throw new ApplicationException(EnumException.ACTIVO_FICHEROS_ADD_FILE_001);
        }
    }

    public JsonObject jsonRecursoFichero(){
        JsonObjectBuilder jsonReturn=Json.createObjectBuilder();
    
        JsonArrayBuilder jsonRows=Json.createArrayBuilder();
        List<BienActivoFicheroEntity> lstFichero= bienActFicheRepo.lstBienActivoFichero();
        lstFichero.forEach((item)->{
            jsonRows.add(Json.createObjectBuilder()
            .add("id_fichero", item.getIdBienActivoFichero())
            .add("id_bien_activo_fijo", item.getBienActivo().getIdRecursoActivo())
            .add("url_img_activo", item.getUrl())
            );
        });
        jsonReturn.add("rows", jsonRows);
        return jsonReturn.build();
    }

    public JsonObject JsonRecursoFichaIdActivo(int idBienActivo){
        JsonObjectBuilder jsonReturn=Json.createObjectBuilder();
        JsonArrayBuilder jsonRows=Json.createArrayBuilder();
        List<BienActivoFicheroEntity> lstficheroidAct=bienActFicheRepo.findByBienActivoIdRecursoActivo(idBienActivo);
        lstficheroidAct.forEach((item)->{
            jsonRows.add(Json.createObjectBuilder().
            add("id_fichero", item.getIdBienActivoFichero())
            .add("id_bien_activo_fijo", item.getBienActivo().getIdRecursoActivo())
            .add("url_img_activo", item.getUrl())
            );
        });
        jsonReturn.add("rows", jsonRows);
        return jsonReturn.build();
    }
}