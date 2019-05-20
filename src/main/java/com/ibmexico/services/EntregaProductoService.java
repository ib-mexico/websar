package com.ibmexico.services;

import java.time.LocalDateTime;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.ibmexico.libraries.notifications.ApplicationException;
import com.ibmexico.libraries.notifications.EnumException;
import com.ibmexico.entities.EntregaEntity;
import com.ibmexico.entities.EntregaProductoEntity;
import com.ibmexico.entities.UsuarioEntity;
import com.ibmexico.repositories.IEntregaProductoRepository;

@Service("entregaProductoService")
public class EntregaProductoService {

	@Autowired
	@Qualifier("entregaProductoRepository")
	private IEntregaProductoRepository entregaProductoRepository;
	
	@Autowired
	@Qualifier("sessionService")
	private SessionService sessionService;
	
	public List<EntregaProductoEntity> listEntregaProductos(int idEntrega) {
		return entregaProductoRepository.findEntregasProductos(idEntrega);
	}
	
	@Transactional
	public void create(EntregaProductoEntity objProducto) {
		
		if(objProducto != null) {
			LocalDateTime ldtNow = LocalDateTime.now();
			UsuarioEntity objUsuarioCreacion = sessionService.getCurrentUser();
			objProducto.setCreacionFecha(ldtNow);
			objProducto.setCreacionUsuario(objUsuarioCreacion);
			objProducto.setModificacionFecha(ldtNow);
			objProducto.setModificacionUsuario(objUsuarioCreacion);
			entregaProductoRepository.save(objProducto);
		}
		else {
			throw new ApplicationException(EnumException.ENTREGAS_PRODUCTOS_CREATE_001);
		}
	}
	
	@Transactional
	public void deleteAll(EntregaEntity objEntrega) {
		entregaProductoRepository.removeEntregaProductos(objEntrega.getIdEntrega());
	}
}
