package com.ibmexico.services;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.ibmexico.configurations.GeneralConfiguration;
import com.ibmexico.entities.ConfiguracionEntity;
import com.ibmexico.repositories.IConfiguracionRepository;

@Service("configuracionService")
public class ConfiguracionService {

	@Autowired
	@Qualifier("configuracionRepository")
	private IConfiguracionRepository configuracionRepository;
	
	//private BigDecimal iva;
	
	public ConfiguracionEntity getConfiguracion(String variable) {
		ConfiguracionEntity objReturn = null;
		
		if(variable != null) {
			objReturn = configuracionRepository.findByVariable(variable);
		}
		
		return objReturn;
	}
	
	public void update(ConfiguracionEntity objConfiguracion) {
		configuracionRepository.save(objConfiguracion);
	}
	
	public Object getValue(String variable) throws NumberFormatException {
		Object objReturn = null;
		
		ConfiguracionEntity objConfiguracionEntity = getConfiguracion(variable);
		
		if(objConfiguracionEntity != null) {
			switch(objConfiguracionEntity.getTipo()) {
				case "String":
					objReturn = objConfiguracionEntity.getValor();
				break;
				
				case "Boolean":
					objReturn = Boolean.parseBoolean(objConfiguracionEntity.getValor());
				break;
				
				case "BigDecimal":
					objReturn = new BigDecimal(objConfiguracionEntity.getValor());
				break;
				case "LocalDate" :
					objReturn = new  String(objConfiguracionEntity.getValor());
				break;
			}
		}
		
		return objReturn;
	}
	
	public List<ConfiguracionEntity> listConfiguraciones() {
		return configuracionRepository.findAll();
	}
	
	public BigDecimal getSmartPorcentajeIva() {
		
		BigDecimal returnPorcentajeIva = new BigDecimal(0);
		
		if( (boolean)getValue("IVA_ACTIVO") ) {
			
			returnPorcentajeIva = (BigDecimal)getValue("IVA_PORCENTAJE");
		}
			
		return returnPorcentajeIva;
	}
	
	public String getSmartPorcentajeIvaNatural() {
		return GeneralConfiguration.getInstance().getNumberFormat().format(getSmartPorcentajeIva());
	}
	
	public BigDecimal getSmartIva(BigDecimal importe) {
		
		BigDecimal returnIva = null;
		
		if(importe != null) {
			returnIva = importe.divide(new BigDecimal(100)).multiply(getSmartPorcentajeIva());
		}
			
		return returnIva;
	}
	
	public String getSmartIvaNatural(BigDecimal importe) {
		return GeneralConfiguration.getInstance().getCurrencyNumberFormat().format(getSmartIva(importe));
	}
	
	public BigDecimal getSmartTotal(BigDecimal importe) {
		
		BigDecimal returnImporteNeto = null;
		
		if(importe != null) {
			returnImporteNeto = importe.add(getSmartIva(importe));
		}
		
		return returnImporteNeto;
	}
	
	public String getSmartTotalNatural(BigDecimal importe) {
		return GeneralConfiguration.getInstance().getCurrencyNumberFormat().format(getSmartTotal(importe));
	}
	
}