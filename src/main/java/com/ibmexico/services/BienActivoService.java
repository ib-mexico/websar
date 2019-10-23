package com.ibmexico.services;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.transaction.Transactional;

import com.ibmexico.entities.BienActivoEntity;
import com.ibmexico.entities.BienActivoFicheroEntity;
import com.ibmexico.entities.UsuarioEntity;
import com.ibmexico.libraries.DataTable;
import com.ibmexico.libraries.notifications.ApplicationException;
import com.ibmexico.libraries.notifications.EnumException;
import com.ibmexico.repositories.IBienActivoRepository;

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
    public int contForCatalogo(int idactivo){
        return Activorep.countForCatalogo(idactivo);
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
                bienActivo.setFechaUltimoMantenimiento(null);
                bienActivo.setEnMantenimiento(false);
                //
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
            bienActivo.setEnMantenimiento(false);
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

	public BienActivoEntity findByIdRecursoActive(int idRecursoActivo) {
		return Activorep.findByIdRecursoActivo(idRecursoActivo);
    }
    
    // la funcion de arriba, ya no se usa porque se maneja con json los datos y 
    //esta sirve para obtener la data de lo que se va a editar mediante el ID.
    public JsonObject jsonFindByIdRecursoActive(int id) {
		JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
		JsonArrayBuilder jsonRows = Json.createArrayBuilder();
        BienActivoEntity lstActivo = Activorep.findByIdRecursoActivo(id);
        BigDecimal d = BigDecimal.ZERO ;

        jsonRows.add(Json.createObjectBuilder()
            .add("id_activo", lstActivo.getIdRecursoActivo())
            .add("id_tipo_activo", lstActivo.getIdActivo().getIdCatalogoActivo())
            .add("descripcion", lstActivo.getDescripcion()!=null ? lstActivo.getDescripcion() :"")
            .add("marca", lstActivo.getMarca()!=null ? lstActivo.getMarca() : "")
            .add("modelo", lstActivo.getModelo()!=null ? lstActivo.getModelo() : "")
            .add("serie",lstActivo.getSerie()!=null ? lstActivo.getSerie() : "")
            .add("color", lstActivo.getColor()!= null ? lstActivo.getColor() : "")
            .add("fechaCompra", lstActivo.getFechaCompraNatural()!=null ? lstActivo.getFechaCompraNatural() : "")
            .add("costoActivo", lstActivo.getCosto()!=null ?  lstActivo.getCosto() : d)
            .add("garantiames", lstActivo.getGarantiaMes()!=null ? lstActivo.getGarantiaMes() : 0)
            .add("fecha_obsolescencia", lstActivo.getObsolecensiaFechaNatural()!=null ? lstActivo.getObsolecensiaFechaNatural() : "")

            .add("placa", lstActivo.getPlaca()!=null ? lstActivo.getPlaca() : "")
            .add("tipovehiculo", lstActivo.getTipoVehiculo()!=null ? lstActivo.getTipoVehiculo() :"")

            .add("serieEvaporadora", lstActivo.getSerieEvaporadora()!=null ? lstActivo.getSerieEvaporadora() : "")
            .add("serieCondensadora", lstActivo.getSerieCondensadora()!=null ? lstActivo.getSerieCondensadora() : "" )
            .add("chkControlRemoto", lstActivo.isBoolControlRemoto())

            .add("fechaEntregaMovil", lstActivo.getFechaEntregaMovilNatural()!=null ? lstActivo.getFechaEntregaMovilNatural() : "")
            .add("imei", lstActivo.getImei()!=null ? lstActivo.getImei() : 0)
            .add("almacenamientoExterno", lstActivo.getAlmacenamientoExterna()>=1 ? lstActivo.getAlmacenamientoExterna() : 0)

            .add("tipoequipo", lstActivo.getTipoEquipo()!=null ? lstActivo.getTipoEquipo() : "")
            .add("tipoMemoriaRam",lstActivo.getTipoMemoriaRam()!=null ? lstActivo.getTipoMemoriaRam() : "")
            .add("capacidadMemoriaRam", lstActivo.getCapacidadMemoriaRam()!=null ? lstActivo.getCapacidadMemoriaRam() : 0)
            .add("tipoProcesador", lstActivo.getTipoProcesador()!=null ? lstActivo.getTipoProcesador() : "")
            .add("marcaProcesador",lstActivo.getMarcaProcesador()!=null ? lstActivo.getMarcaProcesador() : "")
            .add("capProcesador", lstActivo.getCapacidadProcesador()!=null ? lstActivo.getCapacidadProcesador() : 0)
            .add("tipoHDD", lstActivo.getTipoHdd()!=null ? lstActivo.getTipoHdd() : "")
            .add("capIntHDD", lstActivo.getCapacidadInternoHdd()!=null ? lstActivo.getCapacidadInternoHdd() : 0)
            
            .add("chkMonitor", lstActivo.isCuentaMonitor())
                .add("tamanioMonitor", lstActivo.getTamanio()!=null ? lstActivo.getTamanio() : 0)
                .add("colorMonitor", lstActivo.getColorMonitor()!=null ? lstActivo.getColorMonitor() : "")
                .add("modeloMonitor", lstActivo.getModeloMonitor()!=null ? lstActivo.getModeloMonitor() : "")
                .add("numParte", lstActivo.getNumeroParte()!=null ? lstActivo.getNumeroParte() : "")
            
            .add("periodomantenimiento", lstActivo.getPeriodoMantEstimado()!=null ? lstActivo.getPeriodoMantEstimado() : 0)
            .add("id_empresa", lstActivo.getEmpresa().getIdEmpresa())
            .add("id_tipo_activo", lstActivo.getIdActivo().getIdCatalogoActivo())
            .add("id_departamento", lstActivo.getIdDepartamento().getIdDepartamento())
            .add("id_usuario", lstActivo.getUsuario()!=null ? lstActivo.getUsuario().getIdUsuario() : 0 )

            .add("observaciones", lstActivo.getObservaciones()!=null ? lstActivo.getObservaciones() : "")      
            );     
		jsonReturn.add("Activos", jsonRows);
		return jsonReturn.build();
    }

    /*LISTA DE TODOS LOS RECURSOS ACTIVOS */
    public JsonObject jsonRecursoActivo(){
        JsonObjectBuilder jsonReturn=Json.createObjectBuilder();
        JsonArrayBuilder jsonRows=Json.createArrayBuilder();
        List<BienActivoEntity> lstRecursoActivo=Activorep.lstCatalogoActivo();
        lstRecursoActivo.forEach((item)->{
            jsonRows.add(Json.createObjectBuilder()
            .add("id_recurso_activo", item.getIdRecursoActivo())
            .add("descripcion_completa", item.getNumeroEconomico()+'-'+item.getMarca())
            );
        });
        jsonReturn.add("rows", jsonRows);
        return jsonReturn.build();
    }
    /*OBTENER LA LISTA DE ACTIVOS MEDIANTE EL ID DEL TIPO DE ACTIVO Y QUE NO ESTEN EN MANTO */
    public JsonObject jsonRecursoActivoIdCatalogo(int idCatalogoActivo){
        JsonObjectBuilder jsonReturn=Json.createObjectBuilder();
        JsonArrayBuilder jsonRows=Json.createArrayBuilder();
        List<BienActivoEntity> lstRecursoIdCatalogo=Activorep.findByIdCatalogoActivo(idCatalogoActivo);
        lstRecursoIdCatalogo.forEach((item)->{
            jsonRows.add(Json.createObjectBuilder()
            .add("id_recurso_activo", item.getIdRecursoActivo())
            .add("descripcion_completa", item.getNumeroEconomico()+'-'+item.getMarca())
            );
        });
        jsonReturn.add("rows", jsonRows);
        return jsonReturn.build();
    }
    
    /*Todos los recursos sin condicionarla */
    public JsonObject jsonRecursoActivoAll(int idCatalogoActivo){
        JsonObjectBuilder jsonReturn=Json.createObjectBuilder();
        JsonArrayBuilder jsonRows=Json.createArrayBuilder();
        List<BienActivoEntity> lstRecursoIdCatalogo=Activorep.findByIdCatalogoActivoAll(idCatalogoActivo);
        lstRecursoIdCatalogo.forEach((item)->{
            jsonRows.add(Json.createObjectBuilder()
            .add("id_recurso_activo", item.getIdRecursoActivo())
            .add("descripcion_completa", item.getNumeroEconomico()+'-'+item.getMarca())
            );
        });
        jsonReturn.add("rows", jsonRows);
        return jsonReturn.build();
    }

}