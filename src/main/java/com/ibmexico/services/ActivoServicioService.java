package com.ibmexico.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import com.ibmexico.entities.ActivoServicioEntity;
import com.ibmexico.entities.UsuarioEntity;
import com.ibmexico.libraries.DataTable;
import com.ibmexico.libraries.notifications.ApplicationException;
import com.ibmexico.libraries.notifications.EnumException;
import com.ibmexico.repositories.IActivoServicioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("activo_servicio")
public class ActivoServicioService{

    @Autowired
    @Qualifier("activo_servicio_repository")
    private IActivoServicioRepository activoServiciorep;
    
    @Autowired
    @Qualifier("sessionService")
    private SessionService sessionService;

    public List<ActivoServicioEntity> findByIdTipoActivo(int idTipoActivo){
        return activoServiciorep.findByTipoActivo(idTipoActivo);
    }
    public ActivoServicioEntity findByIdServicio(int idServicio){
        return activoServiciorep.findByIdServicioActivo(idServicio);
    }

    public void create(ActivoServicioEntity objServicio){
        if(objServicio != null){
            try {
                LocalDateTime ldtnow= LocalDateTime.now();
                UsuarioEntity objUsuario= sessionService.getCurrentUser();
                objServicio.setCreacionFecha(ldtnow);
                objServicio.setModificacionFecha(ldtnow);
                objServicio.setCreacionUsuario(objUsuario);
                objServicio.setModificacionUsuario(objUsuario);
                objServicio.setEliminado(false);
                activoServiciorep.save(objServicio);
            } catch (Exception e) {
                throw new ApplicationException(EnumException.ACTIVO_SERVICIO_CREATE_001);
            }
        }
    }

    //Lista de Servicios por el Tipo de Activo
    public JsonObject jsonServicioTipoActivo(int idTipoActivo){
        JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
        JsonArrayBuilder jsonRows = Json.createArrayBuilder();
        List<ActivoServicioEntity> lstServicioTipo=activoServiciorep.findByTipoActivo(idTipoActivo);
        lstServicioTipo.forEach((item)->{
            jsonRows.add(Json.createObjectBuilder()
            .add("id_servicio",item.getIdServicioActivo())
            .add("descripcion", item.getDescripcion())
            .add("precio_estimado", item.getPrecioEstimado()));
        });
        jsonReturn.add("rows",jsonRows);
        return jsonReturn.build();
    }

    //Listar todos los servicios vigentes en JSON
    public JsonObject jsonServicios(){
        JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
        JsonArrayBuilder jsonRows = Json.createArrayBuilder();
        List<ActivoServicioEntity> lstServicioAll = activoServiciorep.lstServicios();
        lstServicioAll.forEach((item)->{
            jsonRows.add(Json.createObjectBuilder()
            .add("idServicio",item.getIdServicioActivo())
            .add("descripcion", item.getDescripcion())
            .add("precio_estimado", item.getPrecioEstimadoNatural()));
        });
        jsonReturn.add("rows",jsonRows);
        return jsonReturn.build();
    }


    //Obtener un servicio en JSON
    public JsonObject jsonIdServicio(int idServicio){
        JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
        JsonArrayBuilder jsonRows = Json.createArrayBuilder();
        ActivoServicioEntity objServicio= activoServiciorep.findByIdServicioActivo(idServicio);
        jsonRows.add(Json.createObjectBuilder()
            .add("id_servicio", objServicio.getIdServicioActivo())
            .add("descripcion", objServicio.getDescripcion())
            .add("precioEstimado", objServicio.getPrecioEstimado())
            .add("idActivo", objServicio.getTipoActivo().getIdCatalogoActivo())
        );
        jsonReturn.add("servicio", jsonRows);
        return jsonReturn.build();
    }

    //Recurso de la tabla de Servicios vigentes.
    public DataTable<ActivoServicioEntity> dataTable(String search, int offset, int limit, String txtTableDesde, String txtTableHasta) {
        List<ActivoServicioEntity> lstActivoServicio = null;
        long totalServicio = 100;
        LocalDate fechaInicio = null;
		LocalDate fechaFin = null;
        if (search != null) {
            if(!txtTableDesde.equals("") && !txtTableHasta.equals("")){
                String[] arrFechaInicio = txtTableDesde.split("/");
                int yearInicio = Integer.parseInt(arrFechaInicio[2]);
                int monthInicio = Integer.parseInt(arrFechaInicio[1]);
                int dayInicio = Integer.parseInt(arrFechaInicio[0]);
                
                String[] arrFechaFin = txtTableHasta.split("/");
                int yearFin = Integer.parseInt(arrFechaFin[2]);
                int monthFin = Integer.parseInt(arrFechaFin[1]);
                int dayFin = Integer.parseInt(arrFechaFin[0]);
                
                fechaInicio = LocalDate.of(yearInicio, monthInicio, dayInicio);
                fechaFin = LocalDate.of(yearFin, monthFin, dayFin);
                lstActivoServicio = activoServiciorep.findForDataTable(search, fechaInicio, fechaFin, DataTable.getPageRequest(offset, limit));
                totalServicio = activoServiciorep.countForDataTable(search, fechaInicio, fechaFin);
            }else{
                lstActivoServicio = activoServiciorep.findForDataTable(search, DataTable.getPageRequest(offset, limit));
                totalServicio = activoServiciorep.countForDataTable(search);
            }
        } else {
            lstActivoServicio = activoServiciorep.findForDataTable(DataTable.getPageRequest(offset, limit));
            totalServicio = activoServiciorep.countForDataTable();
        }
        DataTable<ActivoServicioEntity> returnDataTable = new DataTable<ActivoServicioEntity>(lstActivoServicio, totalServicio);
        return  returnDataTable;
    }

    public void update(ActivoServicioEntity objServicio){
        if (objServicio != null) {
            LocalDateTime ldtNow=LocalDateTime.now();
            UsuarioEntity objUsuario=sessionService.getCurrentUser();
            objServicio.setModificacionFecha(ldtNow);
            objServicio.setModificacionUsuario(objUsuario);
            activoServiciorep.save(objServicio);
        }else{
            throw new ApplicationException(EnumException.ACTIVO_SERVICIO_DELETE_001);
        }
    }
}