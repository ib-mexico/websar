package com.ibmexico.services;

import java.time.LocalDateTime;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import com.ibmexico.entities.OpnNegocioColaboradorEntity;
import com.ibmexico.entities.UsuarioEntity;
import com.ibmexico.libraries.notifications.ApplicationException;
import com.ibmexico.libraries.notifications.EnumException;
import com.ibmexico.repositories.IOpnNegocioColaboradorRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("opnNegocioColaboradorService")
public class OpnNegocioColaboradorService {

    @Autowired
    @Qualifier("opnNegocioColaboradorRepository")
    private IOpnNegocioColaboradorRepository opnNegocioColaboradorRepository;

    @Autowired
	@Qualifier("sessionService")
    private SessionService sessionService;
    
    public OpnNegocioColaboradorEntity findByIdOpnNegocio(int idOportunidad){
        return opnNegocioColaboradorRepository.findByOpnNegocio_IdOportunidadNegocio(idOportunidad);
    }

    public void create(OpnNegocioColaboradorEntity objNegocioColaborador){
        if(objNegocioColaborador != null){
            LocalDateTime ldtNow = LocalDateTime.now();
            UsuarioEntity objUsuarioCreacion = sessionService.getCurrentUser();
            objNegocioColaborador.setCreacionFecha(ldtNow);
            objNegocioColaborador.setModificacionFecha(ldtNow);
            objNegocioColaborador.setCreacionUsuario(objUsuarioCreacion);
            objNegocioColaborador.setModificacionUsuario(objUsuarioCreacion);
            opnNegocioColaboradorRepository.save(objNegocioColaborador);
        }
    }

    public void update(OpnNegocioColaboradorEntity objNegocioColaborador) {
		
		if(objNegocioColaborador != null) {
			LocalDateTime ldtNow = LocalDateTime.now();
			UsuarioEntity objUsuarioCreacion = sessionService.getCurrentUser();
			objNegocioColaborador.setModificacionFecha(ldtNow);
			objNegocioColaborador.setModificacionUsuario(objUsuarioCreacion);
			opnNegocioColaboradorRepository.save(objNegocioColaborador);
		}
		else {
			throw new ApplicationException(EnumException.OPORTUNIDADES_UPDATE_001);
		}
    }
    
    public JsonObject jsonColaboradores(int idOPN){

        JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
        JsonArrayBuilder jsonRows = Json.createArrayBuilder();
        List<OpnNegocioColaboradorEntity> lstColaborador = opnNegocioColaboradorRepository.lstColaboradorOPN(idOPN);
        
        lstColaborador.forEach((itemColaborador)-> {
			jsonRows.add(Json.createObjectBuilder()
				.add("idOpnColaborador", itemColaborador.getIdOportunidadNegocioColaborador())
				.add("idOpnNegocio", itemColaborador.getOpnNegocio().getIdOportunidadNegocio())
                .add("id_usuario", itemColaborador.getUsuarioColaborador().getIdUsuario())
                .add("nombre_completo", itemColaborador.getUsuarioColaborador().getNombreCompleto())
                .add("empresa", itemColaborador.getUsuarioColaborador().getEmpresa().getEmpresa())
                .add("claveEmpresa", itemColaborador.getUsuarioColaborador().getEmpresa().getClave())
			);
		});
        
        jsonReturn.add("jsonColaboradores", jsonRows);
        return jsonReturn.build();
    }

    @Transactional
    public void deleteColaborador(OpnNegocioColaboradorEntity objColaborador){
        try {
            if (objColaborador != null) {
                opnNegocioColaboradorRepository.delete(objColaborador);
            }
        }
        catch (Exception err) {
            System.out.println(err.getMessage());
        }
    }

}