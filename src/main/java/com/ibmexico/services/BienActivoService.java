package com.ibmexico.services;

import com.ibmexico.entities.BienActivoEntity;
import com.ibmexico.entities.BienActivoFicheroEntity;
import com.ibmexico.entities.UsuarioEntity;
import com.ibmexico.libraries.DataTable;
import com.ibmexico.libraries.notifications.ApplicationException;
import com.ibmexico.libraries.notifications.EnumException;
import com.ibmexico.repositories.IBienActivoRepository;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service("bienActivoService")
public class BienActivoService {
    
    @Autowired
    @Qualifier("bienActivoRepository")
    private IBienActivoRepository Activorep;

    @Autowired
	@Qualifier("sessionService")
    private SessionService sessionService;

    @Autowired
    @Qualifier("bienActivoFicheroService")
    private BienActivoFicheroService bienActivoFicheroService;


    public List<BienActivoEntity> listActivo(){
        return Activorep.findAll();
    }

    public void create(BienActivoEntity bienActivo ,MultipartFile[] arrFiles)throws IOException{
        if(bienActivo!=null){
            try {
                LocalDateTime ldtNow= LocalDateTime.now();
                UsuarioEntity objUsuarioCreacion = sessionService.getCurrentUser();
                bienActivo.setCreacionFecha(ldtNow);
                bienActivo.setModificacionFecha(ldtNow);
                bienActivo.setCreacionUsuario(objUsuarioCreacion);
                bienActivo.setModificacionUsuario(objUsuarioCreacion);
                bienActivo.setEstatus(true);
                Activorep.save(bienActivo);
                if(arrFiles!=null){
                    addFicheros(bienActivo, arrFiles);
                }
            } catch(ApplicationException | IOException exception) {
                System.out.println(exception.getMessage());
                throw new ApplicationException(EnumException.ACTIVO_FICHEROS_ADD_FILE_001);
			}	

        }else{
            throw new ApplicationException(EnumException.ACTIVO_CREATE_001);
        }
    }

    @Transactional
    public void addFicheros(BienActivoEntity objBienActivo, MultipartFile[] arrFiles)throws IOException{
        if (objBienActivo!=null) {
            if (arrFiles!=null) {
                for (MultipartFile objFile : arrFiles) {
                    if (objFile!=null) {
                        try {
                            if(!objFile.getOriginalFilename().trim().equals("")){

                                String ficheroNombre = UUID.randomUUID().toString();
                                String[] arrNombreFichero = objFile.getOriginalFilename().split("\\.");
                                
                                if(arrNombreFichero.length>=2){
                                    ficheroNombre=ficheroNombre+"."+arrNombreFichero[arrNombreFichero.length-1];
                                }
                                BienActivoFicheroEntity objActivoFichero=new BienActivoFicheroEntity();
                                objActivoFichero.setUrl(ficheroNombre);
                                objActivoFichero.setBienActivo(objBienActivo);
                                bienActivoFicheroService.create(objActivoFichero, objFile);
                            }

                        } catch (ApplicationException exception) {
                            throw new ApplicationException(EnumException.ACTIVO_FICHEROS_ADD_FILE_001);
                        }
                    } else {
                        throw new ApplicationException(EnumException.ACTIVO_FICHEROS_ADD_FILE_001);
                    }
                }
            } else {
                throw new ApplicationException(EnumException.ACTIVO_FICHEROS_ADD_FILE_001);
            }
        } else {
            throw new ApplicationException(EnumException.ACTIVO_FICHEROS_ADD_FILE_001);
        }
    }

    
    public void update(BienActivoEntity bienActivo){
        if(bienActivo!=null){
            LocalDateTime ldtNow=LocalDateTime.now();
            UsuarioEntity objUsuarioCreacion=sessionService.getCurrentUser();
            bienActivo.setModificacionFecha(ldtNow);
            bienActivo.setModificacionUsuario(objUsuarioCreacion);
            // bienActivo.setEstatus(false);
            Activorep.save(bienActivo);
        }else{
            throw new ApplicationException(EnumException.ACTIVO_CREATE_001);
        }
    }

    public DataTable<BienActivoEntity> dataTable(String search, int offset, int limit, String txtBootstrapTableDesde, String txtBootstrapTableHasta) {
		List<BienActivoEntity> lstBienActivoEntity = null;
		long totalActivo = 100;
		if(search!=null){
            lstBienActivoEntity =Activorep.findForDataTable(search, DataTable.getPageRequest(offset, limit));
            totalActivo=Activorep.countForDataTable(search);

        }else{
		lstBienActivoEntity = Activorep.findForDataTable(DataTable.getPageRequest(offset, limit));
        totalActivo = Activorep.countForDataTable();
    }
		DataTable<BienActivoEntity> returnDataTable = new DataTable<BienActivoEntity>(lstBienActivoEntity, totalActivo);
		return returnDataTable;
    }

	public BienActivoEntity findByIdActivoMobiliario(int idActivoMobiliario) {
		return Activorep.findByIdActivoMobiliario(idActivoMobiliario);
    }
    
    // la funcion de arriba, ya no se usa porque se maneja con json los datos y 
    //esta sirve para obtener la data de lo que se va a editar mediante el ID.
    public JsonObject jsonFindByIdMobiliario(int id) {
		
		JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
		JsonArrayBuilder jsonRows = Json.createArrayBuilder();
        BienActivoEntity lstActivo = Activorep.findByIdActivoMobiliario(id);
		
		jsonRows.add(Json.createObjectBuilder()
            .add("marca",lstActivo.getMarca())
            .add("nombre", lstActivo.getNombre())
            .add("color", lstActivo.getColor())
            .add("serie", lstActivo.getSerie())
            .add("modelo", lstActivo.getModelo())
            .add("placa", lstActivo.getPlaca())
            .add("observaciones", lstActivo.getObservaciones())
            .add("id_empresa", lstActivo.getEmpresa().getIdEmpresa())
            .add("id_activo", lstActivo.getIdActivo().getIdCatalogoActivo())
            .add("id_activo_mobiliario", lstActivo.getIdActivoMobiliario())
            .add("id_departamento", lstActivo.getIdDepartamento().getIdDepartamento())
            .add("id_usuario", lstActivo.getUsuario().getIdUsuario())
			);
		jsonReturn.add("Activos", jsonRows);
		
		return jsonReturn.build();
    }

    //Obtener la lista de todos los recursos activos
    public JsonObject jsonRecursoActivo(){
        JsonObjectBuilder jsonReturn=Json.createObjectBuilder();
        JsonArrayBuilder jsonRows=Json.createArrayBuilder();
        List<BienActivoEntity> lstRecursoActivo=Activorep.lstCatalogoActivo();
        lstRecursoActivo.forEach((item)->{
            jsonRows.add(Json.createObjectBuilder()
            .add("id_activo_mobiliario", item.getIdActivoMobiliario())
            .add("descripcion_completa", item.getNombre()+'-'+item.getMarca()+'-'+item.getModelo())
            
            );
        });
        jsonReturn.add("rows", jsonRows);
        return jsonReturn.build();
    }

    //obtener la lista de activos por el id del catalogo
    public JsonObject jsonRecursoActivoIdCatalogo(int idCatalogoActivo){
        JsonObjectBuilder jsonReturn=Json.createObjectBuilder();
        JsonArrayBuilder jsonRows=Json.createArrayBuilder();
        List<BienActivoEntity> lstRecursoIdCatalogo=Activorep.findByIdCatalogoActivo(idCatalogoActivo);
        lstRecursoIdCatalogo.forEach((item)->{
            jsonRows.add(Json.createObjectBuilder()
            .add("id_activo_mobiliario", item.getIdActivoMobiliario())
            .add("descripcion_completa", item.getNombre()+'-'+item.getMarca()+'-'+item.getModelo())
            );
        });
        jsonReturn.add("rows", jsonRows);
        return jsonReturn.build();
    }
    


}