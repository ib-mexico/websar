package com.ibmexico.services;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;

import com.ibmexico.entities.CotizacionEntity;
import com.ibmexico.entities.OportunidadNegocioEntity;
import com.ibmexico.repositories.ICotizacionRepository;
import com.ibmexico.repositories.IOportunidadNegocioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;


@Service("indicadoresService")
public class IndicadoresService{

    @Autowired
	@Qualifier("cotizacionRepository")
	private ICotizacionRepository cotizacionRepository;

	@Autowired
	@Qualifier("oportunidadNegocioRepository")
	private IOportunidadNegocioRepository opnRepository;
    
    @Autowired
	@Qualifier("sessionService")
    private SessionService sessionService;
    
	private static DecimalFormat df = new DecimalFormat("0.00");
    
    public JsonObject jsonCotAreasFacturadas(LocalDate ldFechaInicio, LocalDate ldFechaFin,
			int idGrupo) {
		JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
		JsonArrayBuilder jsonRows = Json.createArrayBuilder();

		List<CotizacionEntity> lstAreaCotFacturada = cotizacionRepository.cotAreaFacturada(idGrupo, ldFechaInicio, ldFechaFin);
		df.setRoundingMode(RoundingMode.DOWN);
        lstAreaCotFacturada.forEach( (item) -> {
			jsonRows.add(Json.createObjectBuilder().
			add("nombreCompleto", item.getUsuario().getNombreCompleto()).
			add("idUsuario", item.getUsuario().getIdUsuario())
			.add("subtotal", df.format(item.getSubtotal()))
			.add("total", df.format(item.getTotal())));
		});
		jsonReturn.add("jsonCotArea", jsonRows);
		return jsonReturn.build();
	}

	public JsonObject jsonCotAreasPagadaMenos90(int idArea,LocalDate ldFechaInicio, LocalDate ldFechaFin){
		JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
		JsonArrayBuilder jsonRows = Json.createArrayBuilder();
		List<CotizacionEntity> lstCotAreaPagadas = cotizacionRepository.cotAreaPagada(idArea,ldFechaInicio, ldFechaFin);
		int valorMaximo = 90;
		List<CotizacionEntity> cotPagadasMenor90 = new ArrayList<>();
		for (int i = 0; i < lstCotAreaPagadas.size(); i++) {
			int diff = (int) ChronoUnit.DAYS.between(lstCotAreaPagadas.get(i).getAprobacionFecha(), lstCotAreaPagadas.get(i).getPagoFecha());
			if(diff < valorMaximo){
				cotPagadasMenor90.add(lstCotAreaPagadas.get(i));
			}
		}
		df.setRoundingMode(RoundingMode.DOWN);
		cotPagadasMenor90.forEach((item)->{
			jsonRows.add(Json.createObjectBuilder().
			add("nombreCompleto", item.getUsuario().getNombreCompleto())
			.add("idUsuario", item.getUsuario().getIdUsuario())
			.add("subtotal", df.format(item.getSubtotal()))
			.add("total", df.format(item.getTotal())));
		});
		jsonReturn.add("jsonCotMenos90Dias", jsonRows);	
		return jsonReturn.build();
	}

	public JsonObject jsonOpnGeneradasArea(int idArea, LocalDate ldFechaInicio,LocalDate ldFechaFin){
		JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
		JsonArrayBuilder jsonRows = Json.createArrayBuilder();

		List<OportunidadNegocioEntity> lstOpnArea=opnRepository.opnNuevasArea(idArea, ldFechaInicio, ldFechaFin);

		lstOpnArea.forEach((item)->{
			jsonRows.add(Json.createObjectBuilder()
			.add("nombreCompleto", item.getUsuarioVendedor().getNombreCompleto()).
			add("idUsuario", item.getUsuarioVendedor().getIdUsuario())
			.add("subtotal", df.format(item.getIngresoEstimado()))
			.add("idestatus", item.getOportunidadNegocioEstatus().getIdOportunidadNegocioEstatus())
			);
			
		});

		jsonReturn.add("jsonOpns", jsonRows);	
		return jsonReturn.build();

	}


	public JsonObject jsonOpnCerradaArea(int idArea, LocalDate ldFechaInicio,LocalDate ldFechaFin){
		JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
		JsonArrayBuilder jsonRows = Json.createArrayBuilder();

		List<OportunidadNegocioEntity> lstOpnClose=opnRepository.opnNuevasGanadas(idArea, ldFechaInicio, ldFechaFin);

		lstOpnClose.forEach((item)->{
			jsonRows.add(Json.createObjectBuilder()
			.add("nombreCompleto", item.getUsuarioVendedor().getNombreCompleto()).
			add("idUsuario", item.getUsuarioVendedor().getIdUsuario())
			.add("subtotal", df.format(item.getIngresoEstimado()))
			.add("idestatus", item.getOportunidadNegocioEstatus().getIdOportunidadNegocioEstatus())
			);
			
		});

		jsonReturn.add("jsonOpnsClose", jsonRows);	
		return jsonReturn.build();

	}





	@PersistenceContext
	private EntityManager em;

	public List<Object[]> getCotizacion() {
		final StoredProcedureQuery query = em.createNamedStoredProcedureQuery("subtotalCotizacion");
		query.registerStoredProcedureParameter(1, Number.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(2, Number.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(3, Date.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(4, Number.class, ParameterMode.IN);
		query.setParameter(1, 0000002);
		query.setParameter(2, 4);
		query.setParameter(3, 2019 - 11 - 01);
		query.setParameter(4, 2019 - 11 - 29);

		final List<Object[]> subtotalCotizacion = query.getResultList();
		return subtotalCotizacion;
	}

}