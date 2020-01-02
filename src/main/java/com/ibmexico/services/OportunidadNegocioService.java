package com.ibmexico.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.ibmexico.libraries.notifications.ApplicationException;
import com.ibmexico.libraries.notifications.EnumException;
import com.ibmexico.configurations.GeneralConfiguration;
import com.ibmexico.entities.ActividadEntity;
import com.ibmexico.entities.CotizacionEntity;
import com.ibmexico.entities.OportunidadNegocioEntity;
import com.ibmexico.entities.OportunidadNegocioFicheroEntity;
import com.ibmexico.entities.UsuarioEntity;
import com.ibmexico.repositories.IOportunidadNegocioRepository;

@Service("oportunidadNegocioService")
public class OportunidadNegocioService {
	
	@Autowired
	@Qualifier("oportunidadNegocioRepository")
	private IOportunidadNegocioRepository oportunidadNegocioRepository;

	@Autowired
	@Qualifier("oportunidadNegocioFicheroService")
	private OportunidadNegocioFicheroService opnNegocioFicheroService;

	@Autowired
	@Qualifier("sessionService")
	private SessionService sessionService;
	
	public OportunidadNegocioEntity findByIdOportunidadNegocio(int idOportunidad) {
		return oportunidadNegocioRepository.findByIdOportunidadNegocio(idOportunidad);
	}
	
	public List<OportunidadNegocioEntity> listOportunidadesNegocios() {
		
		List<OportunidadNegocioEntity> lstOportunidades = null;
		
		if(sessionService.hasRol("OPORTUNIDADES_ADMINISTRADOR")) {
			lstOportunidades = oportunidadNegocioRepository.listOportunidadesNegocios();
		}
		else {			
			lstOportunidades = oportunidadNegocioRepository.listOportunidadesNegocios(sessionService.getCurrentUser().getIdUsuario());
		}
		
		return lstOportunidades;
	}
	
	public List<OportunidadNegocioEntity> listOportunidadesNegocios(int idOportunidadEstatus) {
		
		List<OportunidadNegocioEntity> lstOportunidades = null;
		
		if(sessionService.hasRol("OPORTUNIDADES_ADMINISTRADOR")) {
			lstOportunidades = oportunidadNegocioRepository.findAll(idOportunidadEstatus);
		}
		else {			
			lstOportunidades = oportunidadNegocioRepository.findAll(idOportunidadEstatus, sessionService.getCurrentUser().getIdUsuario());
		}
		
		return lstOportunidades;
	}
	
	public List<OportunidadNegocioEntity> listOportunidadesNegociosEmpresa(int idOportunidadEstatus, int idEmpresa) {
		
		List<OportunidadNegocioEntity> lstOportunidades = null;
		
		if(sessionService.hasRol("OPORTUNIDADES_ADMINISTRADOR")) {
			lstOportunidades = oportunidadNegocioRepository.findAllEmpresa(idOportunidadEstatus, idEmpresa);
		}
		else {			
			lstOportunidades = oportunidadNegocioRepository.findAllEmpresa(idOportunidadEstatus, sessionService.getCurrentUser().getIdUsuario(), idEmpresa);
		}
		
		return lstOportunidades;
	}
	
	public JsonObject jsonOportunidadesNegociosEmpresa(int idOportunidadEstatus, int idEmpresa) {
		
		JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
		JsonArrayBuilder jsonRows = Json.createArrayBuilder();
		String estatus_key;
		
		List<OportunidadNegocioEntity> lstOportunidades = listOportunidadesNegociosEmpresa(idOportunidadEstatus, idEmpresa);
		
		lstOportunidades.forEach((itemOportunidad)-> {
			jsonRows.add(Json.createObjectBuilder()
				.add("id_oportunidad", itemOportunidad.getIdOportunidadNegocio())
				.add("oportunidad", itemOportunidad.getOportunidad())
				.add("vendedor", itemOportunidad.getUsuarioVendedor().getNombreCompleto())
				.add("ultima_modificacion", itemOportunidad.getModificacionFechaNatural())
				.add("cliente", itemOportunidad.getCliente().getCliente())
				.add("ingreso_estimado", itemOportunidad.getIngresoEstimadoNatural())
				.add("prioridad", itemOportunidad.getPrioridad())
				.add("oportunidad", itemOportunidad.getOportunidad())
				.add("ficheroCalidad", opnNegocioFicheroService.countOpnFicheroCalidad(itemOportunidad.getIdOportunidadNegocio())>0 ? true : false )
			);
		});
		
		switch (idOportunidadEstatus) {
			case 1:
				estatus_key = "abiertos";
				break;
				
			case 2:
				estatus_key = "en_curso";
				break;
				
			case 3:
				estatus_key = "rentas";
				break;
		
			case 4:
				estatus_key = "cerrados";
				break;
		
			case 5:
				estatus_key = "perdidos";
				break;
	
			default:
				estatus_key = "abiertos";
				break;
		}
		
		jsonReturn.add(estatus_key, jsonRows);
		jsonReturn.add("total_" + estatus_key, totalIngresoEstimadoEmpresa(idOportunidadEstatus, idEmpresa));
		
		return jsonReturn.build();
	}
	
	public JsonObject jsonOportunidadesNegociosCotizaciones(OportunidadNegocioEntity objOportunidad) {
		
		JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
		JsonArrayBuilder jsonRows = Json.createArrayBuilder();
		
		List<CotizacionEntity> lstCotizaciones = objOportunidad.getCotizaciones();
		
		lstCotizaciones.forEach((itemCotizacion)-> {
			jsonRows.add(Json.createObjectBuilder()
				.add("id_cotizacion", itemCotizacion.getIdCotizacion())
				.add("folio", itemCotizacion.getFolio())
				.add("maestra", itemCotizacion.isMaestra())
				.add("renta", itemCotizacion.isRenta())
				.add("normal", itemCotizacion.isNormal())
				.add("usuario", itemCotizacion.getUsuario().getAliasCorreo())
				.add("total", itemCotizacion.getTotalNatural())
				.add("cliente", itemCotizacion.getCliente().getCliente())
				.add("contacto", itemCotizacion.getClienteContacto().getContacto())
			);
		});
		
		jsonReturn.add("cotizaciones", jsonRows);
		
		return jsonReturn.build();
	}
	
	public JsonObject jsonOportunidadesNegociosFicheros(OportunidadNegocioEntity objOportunidad) {
		
		JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
		JsonArrayBuilder jsonRows = Json.createArrayBuilder();
		
		List<OportunidadNegocioFicheroEntity> lstFicheros = objOportunidad.getFicheros();
		
		lstFicheros.forEach((itemFichero)-> {
			jsonRows.add(Json.createObjectBuilder()
				.add("id_oportunidad_fichero", itemFichero.getIdOportunidadNegocioFichero())
				.add("titulo", itemFichero.getTitulo())
				.add("descripcion", itemFichero.getDescripcion())
				.add("url", itemFichero.getUrl())
				.add("fechallamada", itemFichero.getInicioLlamada()!=null ? itemFichero.getInicioLlamadaFullNatural() : "N/A")
				.add("tipoFichero", itemFichero.getCotizacionTipoFichero()!=null ? true : false)
			);
		});
		
		jsonReturn.add("ficheros", jsonRows);
		
		return jsonReturn.build();
	}
	
	public JsonObject jsonOportunidadesNegociosActividades(OportunidadNegocioEntity objOportunidad) {
		
		JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
		JsonArrayBuilder jsonRows = Json.createArrayBuilder();
		
		List<ActividadEntity> lstActividades = objOportunidad.getOportunidadesActividades();
		
		lstActividades.forEach((itemActividad)-> {
			jsonRows.add(Json.createObjectBuilder()
				.add("id_actividad", itemActividad.getIdActividad())
				.add("actividad_tipo", itemActividad.getActividadTipo().getActividadTipo())
				.add("finalizado", itemActividad.isFinalizado())
				.add("usuario", itemActividad.getUsuario().getAliasCorreo())
				.add("fecha_vencimiento", itemActividad.getVencimientoFecha().toString())
			);
		});
		
		jsonReturn.add("actividades", jsonRows);
		
		return jsonReturn.build();
	}
	
	public List<OportunidadNegocioEntity> listOportunidadesNegociosRenovaciones(UsuarioEntity objUsuario, LocalDate ldFechaRenovacion) {
		return oportunidadNegocioRepository.listOportunidadesNegociosRenovaciones(objUsuario.getIdUsuario(), ldFechaRenovacion);
	}
	
	public String totalIngresoEstimado(int idOportunidadEstatus) {
		
		BigDecimal total = null;
		
		if(sessionService.hasRol("OPORTUNIDADES_ADMINISTRADOR")) {
			total = oportunidadNegocioRepository.sumIngresosEstimados(idOportunidadEstatus);
		}
		else {			
			total = oportunidadNegocioRepository.sumIngresosEstimados(idOportunidadEstatus, sessionService.getCurrentUser().getIdUsuario());
		}
				
		return GeneralConfiguration.getInstance().getNumberFormat().format(total);
	}
	
	public String totalIngresoEstimadoEmpresa(int idOportunidadEstatus, int idEmpresa) {
		
		BigDecimal total = null;
		
		if(sessionService.hasRol("OPORTUNIDADES_ADMINISTRADOR")) {
			total = oportunidadNegocioRepository.sumIngresosEstimadosEmpresa(idOportunidadEstatus, idEmpresa);
		}
		else {			
			total = oportunidadNegocioRepository.sumIngresosEstimadosEmpresa(idOportunidadEstatus, sessionService.getCurrentUser().getIdUsuario(), idEmpresa);
		}
				
		return GeneralConfiguration.getInstance().getNumberFormat().format(total);
	}
	
	public String sumOportunidadesTotalesPorFecha(LocalDate ldFechaInicial, LocalDate ldFechaFinal) {
		
		String total = "";
		
		if(sessionService.hasRol("OPORTUNIDADES_ADMINISTRADOR")) {
			total =  GeneralConfiguration.getInstance().getNumberFormat().format(oportunidadNegocioRepository.sumTotalOportunidadPorMes(ldFechaInicial, ldFechaFinal));
		} else {
			total =  GeneralConfiguration.getInstance().getNumberFormat().format(oportunidadNegocioRepository.sumTotalOportunidadPorMes(ldFechaInicial, ldFechaFinal, sessionService.getCurrentUser().getIdUsuario()));
		}
		
		return total;
	}
	
	public void create(OportunidadNegocioEntity objOportunidad) {
		
		if(objOportunidad != null) {
			LocalDateTime ldtNow = LocalDateTime.now();
			UsuarioEntity objUsuarioCreacion = sessionService.getCurrentUser();
			objOportunidad.setCreacionFecha(ldtNow);
			objOportunidad.setCreacionUsuario(objUsuarioCreacion);
			objOportunidad.setModificacionFecha(ldtNow);
			objOportunidad.setModificacionUsuario(objUsuarioCreacion);
			oportunidadNegocioRepository.save(objOportunidad);
		}
		else {
			throw new ApplicationException(EnumException.OPORTUNIDADES_CREATE_001);
		}
	}
	
	public void update(OportunidadNegocioEntity objOportunidad) {
		
		if(objOportunidad != null) {
			LocalDateTime ldtNow = LocalDateTime.now();
			UsuarioEntity objUsuarioCreacion = sessionService.getCurrentUser();
			objOportunidad.setModificacionFecha(ldtNow);
			objOportunidad.setModificacionUsuario(objUsuarioCreacion);
			oportunidadNegocioRepository.save(objOportunidad);
		}
		else {
			throw new ApplicationException(EnumException.OPORTUNIDADES_UPDATE_001);
		}
	}

	public JsonObject jsonOportunidades(int idOportunidadNegocio){
		JsonObjectBuilder jsonReturn= Json.createObjectBuilder();
		JsonArrayBuilder jsonRows=Json.createArrayBuilder();

		OportunidadNegocioEntity objOportunidad=oportunidadNegocioRepository.findByIdOportunidadNegocio(idOportunidadNegocio);
		
		jsonRows.add(Json.createObjectBuilder()
			.add("idOportunidad", objOportunidad.getIdOportunidadNegocio())
			.add("nombre", objOportunidad.getOportunidad())
			.add("usuarioEncargado", objOportunidad.getUsuarioEncargado() != null ? objOportunidad.getUsuarioEncargado().getNombreCompleto() :"N/A" )
			.add("empresa", objOportunidad.getEmpresa() != null ? objOportunidad.getEmpresa().getEmpresa() : "N/A")
		);
		jsonReturn.add("oportunidad", jsonRows);
		return jsonReturn.build();
	}
}
