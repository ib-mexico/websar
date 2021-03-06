package com.ibmexico.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Collections;
import java.util.Comparator;

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
import com.ibmexico.entities.OpnNegocioColaboradorEntity;
import com.ibmexico.entities.OportunidadNegocioEntity;
import com.ibmexico.entities.OportunidadNegocioFicheroEntity;
import com.ibmexico.entities.UsuarioEntity;
import com.ibmexico.repositories.IOpnNegocioColaboradorRepository;
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
	@Qualifier("opnNegocioColaboradorRepository")
	private IOpnNegocioColaboradorRepository opnNegocioColaboradorRepository;

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
		List<OpnNegocioColaboradorEntity> lstIdOpn = null;
		List<OportunidadNegocioEntity> lstOportunidadesColaborador = null;
		
		if(sessionService.hasRol("OPORTUNIDADES_ADMINISTRADOR")) {
			lstOportunidades = oportunidadNegocioRepository.findAllEmpresa(idOportunidadEstatus, idEmpresa);
		}
		else {			
			lstOportunidades = oportunidadNegocioRepository.findAllEmpresa(idOportunidadEstatus, sessionService.getCurrentUser().getIdUsuario(), idEmpresa);
			
			lstIdOpn = opnNegocioColaboradorRepository.lstOpnUsuario(sessionService.getCurrentUser().getIdUsuario(), idOportunidadEstatus, idEmpresa);
		
			ArrayList<Integer> arrIdOpn = new ArrayList<>();
			if (lstIdOpn.size() > 0) {
				for (OpnNegocioColaboradorEntity opnc : lstIdOpn) {
					arrIdOpn.add(opnc.getOpnNegocio().getIdOportunidadNegocio());
				}				
				lstOportunidadesColaborador =  oportunidadNegocioRepository.lstOpnColaboradorEmpresa(arrIdOpn);
				lstOportunidades.addAll(lstOportunidadesColaborador);
			}
			Set<OportunidadNegocioEntity> set = new HashSet<>(lstOportunidades);
			lstOportunidades.clear();
			lstOportunidades.addAll(set);
			Collections.sort(lstOportunidades, new Comparator<OportunidadNegocioEntity>() {
				public int compare(OportunidadNegocioEntity opn1, OportunidadNegocioEntity opn2) {
					return opn2.getIdOportunidadNegocio() - opn1.getIdOportunidadNegocio();
				}
			});
		}
		
		return lstOportunidades;
	}

	/**Consulta para las OPN Cerradas y perdidas de este anio */
	public List<OportunidadNegocioEntity> lstOpnNegocioEmpresa(int idOportunidadEstatus, int idEmpresa, int anio){
		List<OportunidadNegocioEntity> lstOportunidades = null;
		List<OpnNegocioColaboradorEntity> lstIdOpn = null;
		List<OportunidadNegocioEntity> lstOportunidadesColaborador = null;
		if(sessionService.hasRol("OPORTUNIDADES_ADMINISTRADOR")){
			lstOportunidades = oportunidadNegocioRepository.findAllEmpresaAnio(idOportunidadEstatus, idEmpresa, anio);
		}else{
			lstOportunidades = oportunidadNegocioRepository.findAllEmpresaAnio(idOportunidadEstatus, sessionService.getCurrentUser().getIdUsuario(), idEmpresa,anio);
			lstIdOpn = opnNegocioColaboradorRepository.lstOpnUsuarioAnio(sessionService.getCurrentUser().getIdUsuario(), idOportunidadEstatus, idEmpresa, anio);
			ArrayList<Integer> arrIdOpn = new ArrayList<>();
			if (lstIdOpn.size() > 0) {
				for (OpnNegocioColaboradorEntity opnc : lstIdOpn) {
					arrIdOpn.add(opnc.getOpnNegocio().getIdOportunidadNegocio());
				}				
				lstOportunidadesColaborador =  oportunidadNegocioRepository.lstOpnColaboradorEmpresa(arrIdOpn);
				lstOportunidades.addAll(lstOportunidadesColaborador);
			}
			Set<OportunidadNegocioEntity> set = new HashSet<>(lstOportunidades);
			lstOportunidades.clear();
			lstOportunidades.addAll(set);
			Collections.sort(lstOportunidades, new Comparator<OportunidadNegocioEntity>() {
				public int compare(OportunidadNegocioEntity opn1, OportunidadNegocioEntity opn2) {
					return opn2.getIdOportunidadNegocio() - opn1.getIdOportunidadNegocio();
				}
			});
		}
		return lstOportunidades;
	}

	/**Consulta de OPN cerradas y perdidas en Historico */
	public List<OportunidadNegocioEntity> lstOpnNegocioEmpresaHistorico(int idOportunidadEstatus, int idEmpresa, int anio){
		List<OportunidadNegocioEntity> lstOportunidades = null;
		/**lista de id de las OPN con colaboradores */
		List<OpnNegocioColaboradorEntity> lstIdOpn = null;
		List<OportunidadNegocioEntity> lstOportunidadesColaborador = null;

		if(sessionService.hasRol("OPORTUNIDADES_ADMINISTRADOR")){
			lstOportunidades = oportunidadNegocioRepository.findAllEmpresaHistorico(idOportunidadEstatus, idEmpresa, anio);
		}else{
			lstOportunidades = oportunidadNegocioRepository.findAllEmpresaHistorico(idOportunidadEstatus, sessionService.getCurrentUser().getIdUsuario(), idEmpresa, anio);
			lstIdOpn = opnNegocioColaboradorRepository.lstOpnUsuarioHistorico(sessionService.getCurrentUser().getIdUsuario(), idOportunidadEstatus, idEmpresa, anio);

			ArrayList<Integer> arrIdOpn = new ArrayList<>();
			if (lstIdOpn.size() > 0) {
				for (OpnNegocioColaboradorEntity opnc : lstIdOpn) {
					arrIdOpn.add(opnc.getOpnNegocio().getIdOportunidadNegocio());
				}				
				lstOportunidadesColaborador =  oportunidadNegocioRepository.lstOpnColaboradorEmpresa(arrIdOpn);
				lstOportunidades.addAll(lstOportunidadesColaborador);
			}
			Set<OportunidadNegocioEntity> set = new HashSet<>(lstOportunidades);
			lstOportunidades.clear();
			lstOportunidades.addAll(set);
			Collections.sort(lstOportunidades, new Comparator<OportunidadNegocioEntity>() {
				public int compare(OportunidadNegocioEntity opn1, OportunidadNegocioEntity opn2) {
					return opn2.getIdOportunidadNegocio() - opn1.getIdOportunidadNegocio();
				}
			});

		}
		return lstOportunidades;
	}

	/**Lista de las OPN Cerradas y perdidas de los años anteriores */
	public JsonObject jsonOportunidadesNegociosEmpresaHistorico(int idOportunidadEstatus, int idEmpresa, int anio) {
		
		JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
		JsonArrayBuilder jsonRows = Json.createArrayBuilder();
		String estatus_key;

		List<OportunidadNegocioEntity> lstOportunidades = lstOpnNegocioEmpresaHistorico(idOportunidadEstatus, idEmpresa, anio);
		
		lstOportunidades.forEach((itemOportunidad)-> {
			jsonRows.add(Json.createObjectBuilder()
				.add("id_oportunidad", itemOportunidad.getIdOportunidadNegocio())
				.add("oportunidad", itemOportunidad.getOportunidad())
				.add("vendedor", itemOportunidad.getUsuarioVendedor().getNombreCompleto())
				.add("color", !itemOportunidad.getUsuarioVendedor().getColor().isEmpty() ? itemOportunidad.getUsuarioVendedor().getColor() : "" )
				.add("ultima_modificacion", itemOportunidad.getModificacionFechaNatural())
				.add("cliente", itemOportunidad.getCliente().getCliente())
				.add("ingreso_estimado", itemOportunidad.getIngresoEstimadoNatural())
				.add("ingreso", itemOportunidad.getIngresoEstimado())
				.add("prioridad", itemOportunidad.getPrioridad())
				.add("oportunidad", itemOportunidad.getOportunidad())
				// .add("ficheroCalidad", opnNegocioFicheroService.countOpnFicheroCalidad(itemOportunidad.getIdOportunidadNegocio())>0 ? true : false )
				.add("ficheroCalidad", itemOportunidad.isBoolCalidad())

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
		// jsonReturn.add("total_" + estatus_key, totalIngresoEstimadoEmpresaHistorico(idOportunidadEstatus, idEmpresa, anio));
		
		return jsonReturn.build();
	}
	
	
	//Lista de las OPN cerradas y perdidas de este anio
	public JsonObject jsonOportunidadesNegociosEmpresaAnio(int idOportunidadEstatus, int idEmpresa, int anio) {
		
		JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
		JsonArrayBuilder jsonRows = Json.createArrayBuilder();
		String estatus_key;
		
		List<OportunidadNegocioEntity> lstOportunidades = lstOpnNegocioEmpresa(idOportunidadEstatus, idEmpresa, anio);
		
		lstOportunidades.forEach((itemOportunidad)-> {
			jsonRows.add(Json.createObjectBuilder()
				.add("id_oportunidad", itemOportunidad.getIdOportunidadNegocio())
				.add("oportunidad", itemOportunidad.getOportunidad())
				.add("vendedor", itemOportunidad.getUsuarioVendedor().getNombreCompleto())
				.add("color", !itemOportunidad.getUsuarioVendedor().getColor().isEmpty() ? itemOportunidad.getUsuarioVendedor().getColor() : "" )
				.add("ultima_modificacion", itemOportunidad.getModificacionFechaNatural())
				.add("cliente", itemOportunidad.getCliente().getCliente())
				.add("ingreso_estimado", itemOportunidad.getIngresoEstimadoNatural())
				.add("ingreso", itemOportunidad.getIngresoEstimado())
				.add("prioridad", itemOportunidad.getPrioridad())
				.add("oportunidad", itemOportunidad.getOportunidad())
				// .add("ficheroCalidad", opnNegocioFicheroService.countOpnFicheroCalidad(itemOportunidad.getIdOportunidadNegocio())>0 ? true : false )
				.add("ficheroCalidad", itemOportunidad.isBoolCalidad())
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
		// jsonReturn.add("total_" + estatus_key, totalIngresoEstimadoEmpresaAnual(idOportunidadEstatus, idEmpresa, anio));
		
		return jsonReturn.build();
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
				.add("color", !itemOportunidad.getUsuarioVendedor().getColor().isEmpty() ? itemOportunidad.getUsuarioVendedor().getColor() : "" )
				.add("ultima_modificacion", itemOportunidad.getModificacionFechaNatural())
				.add("cliente", itemOportunidad.getCliente().getCliente())
				.add("ingreso_estimado", itemOportunidad.getIngresoEstimadoNatural())
				.add("ingreso",itemOportunidad.getIngresoEstimado())
				.add("prioridad", itemOportunidad.getPrioridad())
				.add("oportunidad", itemOportunidad.getOportunidad())
				// .add("ficheroCalidad", opnNegocioFicheroService.countOpnFicheroCalidad(itemOportunidad.getIdOportunidadNegocio()) >0 ? true : false )
				.add("ficheroCalidad", itemOportunidad.isBoolCalidad())
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
			case 6:
				estatus_key = "financiamiento";
				break;
	
			default:
				estatus_key = "abiertos";
				break;
		}
		
		jsonReturn.add(estatus_key, jsonRows);
		// jsonReturn.add("total_" + estatus_key, totalIngresoEstimadoEmpresa(idOportunidadEstatus, idEmpresa));
		
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
				.add("boom", itemCotizacion.isBoom())
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

			// lstIdOpn = opnNegocioColaboradorRepository.lstOpnUsuario(sessionService.getCurrentUser().getIdUsuario(), idOportunidadEstatus, idEmpresa);
		
			// ArrayList<Integer> arrIdOpn = new ArrayList<>();
			// if (lstIdOpn.size() > 0) {
			// 	for (OpnNegocioColaboradorEntity opnc : lstIdOpn) {
			// 		arrIdOpn.add(opnc.getOpnNegocio().getIdOportunidadNegocio());
			// 	}				
			// 	BigDecimal suma =  oportunidadNegocioRepository.sumOpnColaboradorEmpresa(arrIdOpn);
			// 	total= total.add(suma);
			// }
		}
		return GeneralConfiguration.getInstance().getNumberFormat().format(total);
	}
	
	/**Calcular el total de las OPN perdidas o cerradas de este año*/
	public String totalIngresoEstimadoEmpresaAnual(int idOportunidadEstatus, int idEmpresa, int anio) {	
		BigDecimal total = null;
		if(sessionService.hasRol("OPORTUNIDADES_ADMINISTRADOR")) {
			total = oportunidadNegocioRepository.sumIngresosEstimadosEmpresaAnio(idOportunidadEstatus, idEmpresa, anio);
		}
		else {			
			total = oportunidadNegocioRepository.sumIngresosEstimadosEmpresaAnio(idOportunidadEstatus, sessionService.getCurrentUser().getIdUsuario(), idEmpresa, anio);
		}
		return GeneralConfiguration.getInstance().getNumberFormat().format(total);
	}
	

	/**Calculo de el total de las OPN perdidas o cerradas de los años anteriores */
	public String totalIngresoEstimadoEmpresaHistorico(int idOportunidadEstatus, int idEmpresa, int anio) {	
		BigDecimal total = null;
		if(sessionService.hasRol("OPORTUNIDADES_ADMINISTRADOR")) {
			total = oportunidadNegocioRepository.sumIngresosEstimadosEmpresaHistorico(idOportunidadEstatus, idEmpresa, anio);
		}
		else {			
			total = oportunidadNegocioRepository.sumIngresosEstimadosEmpresaHistorico(idOportunidadEstatus, sessionService.getCurrentUser().getIdUsuario(), idEmpresa, anio);
		}
		return GeneralConfiguration.getInstance().getNumberFormat().format(total);
	}
	
	/**Suma total de las OPN por mes HOME */
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
