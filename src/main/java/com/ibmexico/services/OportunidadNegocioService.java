package com.ibmexico.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.ibmexico.libraries.notifications.ApplicationException;
import com.ibmexico.libraries.notifications.EnumException;
import com.ibmexico.configurations.GeneralConfiguration;
import com.ibmexico.entities.OportunidadNegocioEntity;
import com.ibmexico.entities.UsuarioEntity;
import com.ibmexico.repositories.IOportunidadNegocioRepository;

@Service("oportunidadNegocioService")
public class OportunidadNegocioService {
	
	@Autowired
	@Qualifier("oportunidadNegocioRepository")
	private IOportunidadNegocioRepository oportunidadNegocioRepository;

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
}
