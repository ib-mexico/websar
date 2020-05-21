package com.ibmexico.services;

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
import com.ibmexico.entities.ClienteContactoEntity;
import com.ibmexico.entities.UsuarioEntity;
import com.ibmexico.libraries.DataTable;
import com.ibmexico.repositories.IClienteContactoRepository;

@Service("clienteContactoService")
public class ClienteContactoService {
	
	@Autowired
	@Qualifier("clienteContactoRepository")
	private IClienteContactoRepository clienteContactoRepository;
	
	@Autowired
	@Qualifier("sessionService")
	private SessionService sessionService;
	
	public List<ClienteContactoEntity> listClienteContactos() {
		return clienteContactoRepository.findAll();
	}
	
	public List<ClienteContactoEntity> listClienteContactosActivos(int idCliente) {
		return clienteContactoRepository.findContactosActivos(idCliente);
	}

	public JsonObject jsonClienteContactosActivos(int idCliente) {
		JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
		JsonArrayBuilder jsonRows = Json.createArrayBuilder();

		List<ClienteContactoEntity> lstClientes = clienteContactoRepository.findContactosActivos(idCliente);

		lstClientes.forEach((item)-> {
			jsonRows.add(Json.createObjectBuilder()
				.add("id_contacto", item.getIdClienteContacto())
				.add("contacto", item.getContacto())
				.add("correo", item.getCorreo())
				.add("puesto", item.getPuesto())
			);
		});

		jsonReturn.add("rows", jsonRows);
		
		return jsonReturn.build();
	}
	
	public DataTable<ClienteContactoEntity> dataTable(int idCliente, String search, int offset, int limit, String txtBootstrapTableDesde, String txtBootstrapTableHasta) {
		List<ClienteContactoEntity> lstClienteContactoEntity = null;
		long totalContactos = 100;
		
		if(search != null) {			
			lstClienteContactoEntity = clienteContactoRepository.findForDataTable(idCliente, search, DataTable.getPageRequest(offset, limit));
			totalContactos = clienteContactoRepository.countForDataTable(idCliente, search);
		} else {
			lstClienteContactoEntity = clienteContactoRepository.findForDataTable(idCliente, DataTable.getPageRequest(offset, limit));
			totalContactos = clienteContactoRepository.countForDataTable(idCliente);
		}

		DataTable<ClienteContactoEntity> returnDataTable = new DataTable<ClienteContactoEntity>(lstClienteContactoEntity, totalContactos);
		return returnDataTable;
	}
	
	public ClienteContactoEntity findByIdClienteContacto(int idClienteContacto) {
		return clienteContactoRepository.findByIdClienteContacto(idClienteContacto);
	}
	
	public void create(ClienteContactoEntity objContacto) {
		
		if(objContacto != null) {
			LocalDateTime ldtNow = LocalDateTime.now();
			UsuarioEntity objUsuarioCreacion = sessionService.getCurrentUser();
			objContacto.setCreacionFecha(ldtNow);
			objContacto.setCreacionUsuario(objUsuarioCreacion);
			objContacto.setModificacionFecha(ldtNow);
			objContacto.setModificacionUsuario(objUsuarioCreacion);
			clienteContactoRepository.save(objContacto);
		}
		else {
			throw new ApplicationException(EnumException.CLIENTES_CONTACTOS_CREATE_001);
		}
	}
	
public void update(ClienteContactoEntity objContacto) {
		
		if(objContacto != null) {
			LocalDateTime ldtNow = LocalDateTime.now();
			UsuarioEntity objUsuarioModificacion = sessionService.getCurrentUser();
			objContacto.setModificacionFecha(ldtNow);
			objContacto.setModificacionUsuario(objUsuarioModificacion);
			clienteContactoRepository.save(objContacto);						
		}
		else {
			throw new ApplicationException(EnumException.CLIENTES_CONTACTOS_UPDATE_001);
		}
	}

}
