package com.ibmexico.services;

import com.ibmexico.entities.BienActivoEntity;
import com.ibmexico.entities.UsuarioEntity;
import com.ibmexico.libraries.DataTable;
import com.ibmexico.libraries.notifications.ApplicationException;
import com.ibmexico.libraries.notifications.EnumException;
import com.ibmexico.repositories.IBienActivoRepository;

import java.time.LocalDateTime;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("bienActivoService")
public class BienActivoService {
    
    @Autowired
    @Qualifier("bienActivoRepository")
    private IBienActivoRepository Activorep;

    @Autowired
	@Qualifier("sessionService")
	private SessionService sessionService;

    public List<BienActivoEntity> listActivo(){
        return Activorep.findAll();
    }

    public void create(BienActivoEntity bienActivo){
        if(bienActivo!=null){
            LocalDateTime ldtNow= LocalDateTime.now();
            UsuarioEntity objUsuarioCreacion = sessionService.getCurrentUser();
            bienActivo.setCreacionFecha(ldtNow);
            bienActivo.setModificacionFecha(ldtNow);
            bienActivo.setCreacionUsuario(objUsuarioCreacion);
            bienActivo.setModificacionUsuario(objUsuarioCreacion);
            bienActivo.setEstatus(true);
            Activorep.save(bienActivo);
        }else{
            throw new ApplicationException(EnumException.ACTIVO_CREATE_001);
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
		
		lstBienActivoEntity = Activorep.findForDataTable(DataTable.getPageRequest(offset, limit));
		totalActivo = Activorep.countForDataTable();

		DataTable<BienActivoEntity> returnDataTable = new DataTable<BienActivoEntity>(lstBienActivoEntity, totalActivo);
		return returnDataTable;
    }

	public BienActivoEntity findByIdActivoMobiliario(int idActivoMobiliario) {
		return Activorep.findByIdActivoMobiliario(idActivoMobiliario);
    }
    

    public JsonObject jsonFindByIdMobiliario(int id) {
		
		JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
		JsonArrayBuilder jsonRows = Json.createArrayBuilder();
        
        BienActivoEntity lstActivo = Activorep.findByIdActivoMobiliario(id);
		
		
			jsonRows.add(Json.createObjectBuilder()
                .add("marca",lstActivo.getMarca())
                .add("nombre", lstActivo.getNombre())
                .add("modelo", lstActivo.getModelo())
                .add("color", lstActivo.getColor())
                .add("serie", lstActivo.getSerie())
                .add("observaciones", lstActivo.getObservaciones())
                .add("id_empresa", lstActivo.getEmpresa().getIdEmpresa())
                .add("id_activo", lstActivo.getIdActivo().getIdCatalogoActivo())
                .add("id_activo_mobiliario", lstActivo.getIdActivoMobiliario())
			);
	
		
		jsonReturn.add("Activos", jsonRows);
		
		return jsonReturn.build();
	}

}