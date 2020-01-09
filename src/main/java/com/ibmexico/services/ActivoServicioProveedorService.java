package com.ibmexico.services;

import java.time.LocalDateTime;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import com.ibmexico.entities.ActivoServicioProveedorEntity;
import com.ibmexico.entities.UsuarioEntity;
import com.ibmexico.libraries.DataTable;
import com.ibmexico.repositories.IActivoServicioProveedorRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("activo_servicio_proveedor")
public class ActivoServicioProveedorService {

    @Autowired
    @Qualifier("activoServicioProveedorRepository")
    private IActivoServicioProveedorRepository aSProveedorRep;

    @Autowired
	@Qualifier("sessionService")
    private SessionService sessionService;
    
    public ActivoServicioProveedorEntity findByIdServicioProveedor(int idServicioProveedor){
        return  aSProveedorRep.findByIdServicioProveedor(idServicioProveedor);
    }

    public void createServicio(ActivoServicioProveedorEntity objServicioProveedor){
        if (objServicioProveedor != null) {
            LocalDateTime ldtnow= LocalDateTime.now();
            UsuarioEntity objUsuario= sessionService.getCurrentUser();
            objServicioProveedor.setCreacionFecha(ldtnow);
            objServicioProveedor.setModificacionFecha(ldtnow);
            objServicioProveedor.setCreacionUsuario(objUsuario);
            objServicioProveedor.setModificacionUsuario(objUsuario);
            objServicioProveedor.setEliminado(false);
            aSProveedorRep.save(objServicioProveedor);
        }
    }

    public DataTable<ActivoServicioProveedorEntity> dataTable(int idProveedor, String search, int offset, int limit, String txtBootstrapTableDesde, String txtBootstrapTableHasta) {
		List<ActivoServicioProveedorEntity> lstServicioProveedorEntity = null;
		long totalServicios = 100;
		
		if(search != null) {			
			lstServicioProveedorEntity = aSProveedorRep.findForDataTable(idProveedor, search, DataTable.getPageRequest(offset, limit));
			totalServicios = aSProveedorRep.countForDataTable(idProveedor, search);
		} else {
			lstServicioProveedorEntity = aSProveedorRep.findForDataTable(idProveedor, DataTable.getPageRequest(offset, limit));
			totalServicios = aSProveedorRep.countForDataTable(idProveedor);
		}

		DataTable<ActivoServicioProveedorEntity> returnDataTable = new DataTable<ActivoServicioProveedorEntity>(lstServicioProveedorEntity, totalServicios);
		return returnDataTable;
    }

    //Listar Proveedores dependiendo del id TipoActivo y del id Servicio Seleccionado en ese momento.
    public JsonObject jsonlstServicioProveedor(int idTipoActivo, int idServicioActivo){
        JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
        JsonArrayBuilder jsonRows = Json.createArrayBuilder();
        List<ActivoServicioProveedorEntity> lstServicioProv = aSProveedorRep.findByActivoServicio(idTipoActivo, idServicioActivo);
        String url_cotizacion_vacio = "";
        lstServicioProv.forEach((item)->{ 
            jsonRows.add(Json.createObjectBuilder()
            .add("id_servicio_proveedor", item.getIdServicioProveedor())
            .add("id_servicio", item.getActivoServicio()!=null ? item.getActivoServicio().getIdServicioActivo() : 0)
            .add("id_proveedor", item.getActivoProveedor()!=null ? item.getActivoProveedor().getIdProveedor() : 0)
            .add("nombre_proveedor",item.getActivoProveedor().getProveedor()!=null ? item.getActivoProveedor().getProveedor() :"")
            .add("nombre_servicio", item.getActivoServicio()!=null ? item.getActivoServicio().getDescripcion() : "")
            .add("url_cotizacion",url_cotizacion_vacio)
            );
         });
        jsonReturn.add("rows", jsonRows);
        return  jsonReturn.build();
    }

    //Lista de servicios-proveedores por el ID PROVEEDOR
    public JsonObject jsonServicioProveedor (int idProveedor){
        JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
        JsonArrayBuilder jsonRows = Json.createArrayBuilder();
        List<ActivoServicioProveedorEntity> lstServicioProveedor = aSProveedorRep.lstServicioIdProveedor(idProveedor);
        
        lstServicioProveedor.forEach((servicio)->{
            jsonRows.add(Json.createObjectBuilder()
            .add("idServicioProveedor", servicio.getIdServicioProveedor())
            .add("idServicio", servicio.getActivoServicio().getIdServicioActivo())
            .add("idProveedor", servicio.getActivoProveedor().getIdProveedor())
            .add("descripcion",servicio.getActivoServicio().getDescripcion())
            .add("precio_estimado", servicio.getActivoServicio().getPrecioEstimadoNatural())
            );
        });

        jsonReturn.add("rows", jsonRows);
        return  jsonReturn.build();
    }
    @Transactional
    public void delete(ActivoServicioProveedorEntity objServicioProveedor){
        System.err.println(objServicioProveedor.getIdServicioProveedor());
        if(objServicioProveedor != null){
            aSProveedorRep.deleteServicio(objServicioProveedor.getIdServicioProveedor());
        }
    }
}