package com.ibmexico.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.ibmexico.repositories.ISucursalRepository;
import com.ibmexico.services.SessionService;
import com.ibmexico.entities.SucursalEntity;
import com.ibmexico.libraries.DataTable;

@Service("sucursalService")
public class SucursalService {

	@Autowired
	@Qualifier("sucursalRepository")
	private ISucursalRepository sucursalRepository;
	
	@Autowired
	@Qualifier("sessionService")
	private SessionService sessionService;
	
	public List<SucursalEntity> listSucursales() {
		return sucursalRepository.findAll();
	}
	
	public DataTable<SucursalEntity> dataTable(String search, int offset, int limit, String txtBootstrapTableDesde, String txtBootstrapTableHasta) {
		List<SucursalEntity> lstSucursalEntity = null;
		long totalSucursales = 100;
		
		lstSucursalEntity = sucursalRepository.findForDataTable(DataTable.getPageRequest(offset, limit));
		totalSucursales = sucursalRepository.countForDataTable();

		DataTable<SucursalEntity> returnDataTable = new DataTable<SucursalEntity>(lstSucursalEntity, totalSucursales);
		return returnDataTable;
	}
	
	public SucursalEntity findByIdSucursal(int idSucursal) {
		return sucursalRepository.findByIdSucursal(idSucursal);
	}
}
