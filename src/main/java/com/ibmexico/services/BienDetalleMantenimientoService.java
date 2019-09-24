package com.ibmexico.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.ibmexico.entities.BienDetalleMantenimientoEntity;
import com.ibmexico.entities.UsuarioEntity;
import com.ibmexico.libraries.DataTable;
import com.ibmexico.libraries.notifications.ApplicationException;
import com.ibmexico.libraries.notifications.EnumException;
import com.ibmexico.repositories.IBienDetalleMantenimientoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("bienDetalleMantService")
public class BienDetalleMantenimientoService{

    @Autowired
    @Qualifier("bienDetalleMantenimientoRepository")
    private IBienDetalleMantenimientoRepository bienMantRep;

    @Autowired
    @Qualifier("sessionService")
    private SessionService sesionService;

    public void create(BienDetalleMantenimientoEntity objDetalle){
        if(objDetalle!=null){
            UsuarioEntity objUser=sesionService.getCurrentUser();
            LocalDateTime ldtNow = LocalDateTime.now();
            objDetalle.setCreacionUsuario(objUser);
            objDetalle.setModificacionUsuario(objUser);
            objDetalle.setCreacionFecha(ldtNow);
            objDetalle.setModificacionFecha(ldtNow);
            bienMantRep.save(objDetalle);     
        }else{
            throw new ApplicationException(EnumException.ACTIVO_CREATE_001);
        }
    }
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
}