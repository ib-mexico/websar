package com.ibmexico.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.ibmexico.libraries.notifications.ApplicationException;
import com.ibmexico.libraries.notifications.EnumException;
import com.ibmexico.entities.ClienteEntity;
import com.ibmexico.entities.UsuarioEntity;
import com.ibmexico.libraries.DataTable;
import com.ibmexico.repositories.IClienteRepository;

@Service("clienteService")
public class ClienteService {

	@Autowired
	@Qualifier("clienteRepository")
	private IClienteRepository clienteRepository;
	
	@Autowired
	@Qualifier("sessionService")
	private SessionService sessionService;
	
	public List<ClienteEntity> listClientes() {
		return clienteRepository.findAll();
	}
	
	public List<ClienteEntity> listClientesActivos() {
		return clienteRepository.findActivos();
	}
	
	public DataTable<ClienteEntity> dataTable(String search, int offset, int limit, String txtBootstrapTableDesde, String txtBootstrapTableHasta) {
		List<ClienteEntity> lstClienteEntity = null;
		LocalDate fechaInicio = null;
		LocalDate fechaFin = null;
		long totalClientes = 100;
		
		if(search != null) {
			if(!txtBootstrapTableDesde.equals("") && !txtBootstrapTableHasta.equals("")) {
				
				String[] arrFechaInicio = txtBootstrapTableDesde.split("/");
				int yearInicio = Integer.parseInt(arrFechaInicio[2]);
				int monthInicio = Integer.parseInt(arrFechaInicio[1]);
				int dayInicio = Integer.parseInt(arrFechaInicio[0]);
				
				String[] arrFechaFin = txtBootstrapTableHasta.split("/");
				int yearFin = Integer.parseInt(arrFechaFin[2]);
				int monthFin = Integer.parseInt(arrFechaFin[1]);
				int dayFin = Integer.parseInt(arrFechaFin[0]);
				
				fechaInicio = LocalDate.of(yearInicio, monthInicio, dayInicio);
				fechaFin = LocalDate.of(yearFin, monthFin, dayFin);
				
				lstClienteEntity = clienteRepository.findForDataTable(search, fechaInicio, fechaFin, DataTable.getPageRequest(offset, limit));
				totalClientes = clienteRepository.countForDataTable(search, fechaInicio, fechaFin);
			} else {				
				lstClienteEntity = clienteRepository.findForDataTable(search, DataTable.getPageRequest(offset, limit));
				totalClientes = clienteRepository.countForDataTable(search);
			}
		} else {
			lstClienteEntity = clienteRepository.findForDataTable(DataTable.getPageRequest(offset, limit));
			totalClientes = clienteRepository.countForDataTable();
		}				

		DataTable<ClienteEntity> returnDataTable = new DataTable<ClienteEntity>(lstClienteEntity, totalClientes);
		return returnDataTable;
	}
	
	public ClienteEntity findByIdCliente(int idCliente) {
		return clienteRepository.findByIdCliente(idCliente);
	}
	
	public void create(ClienteEntity objCliente) {
		
		if(objCliente != null) {
			LocalDateTime ldtNow = LocalDateTime.now();
			UsuarioEntity objUsuarioCreacion = sessionService.getCurrentUser();
			objCliente.setCreacionFecha(ldtNow);
			objCliente.setCreacionUsuario(objUsuarioCreacion);
			objCliente.setModificacionFecha(ldtNow);
			objCliente.setModificacionUsuario(objUsuarioCreacion);
			clienteRepository.save(objCliente);
		}
		else {
			throw new ApplicationException(EnumException.CLIENTES_CREATE_001);
		}
	}
	
	public void update(ClienteEntity objCliente) {
		
		if(objCliente != null) {
			LocalDateTime ldtNow = LocalDateTime.now();
			UsuarioEntity objUsuarioModificacion = sessionService.getCurrentUser();
			objCliente.setModificacionFecha(ldtNow);
			objCliente.setModificacionUsuario(objUsuarioModificacion);
			clienteRepository.save(objCliente);
		}
		else {
			throw new ApplicationException(EnumException.CLIENTES_UPDATE_001);
		}
	}
}
