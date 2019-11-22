package com.ibmexico.services;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.ibmexico.entities.OrdenServicioEntity;
import com.ibmexico.entities.OrdenServicioPartidaEntity;
import com.ibmexico.entities.UsuarioEntity;
import com.ibmexico.libraries.DataTable;
import com.ibmexico.libraries.notifications.ApplicationException;
import com.ibmexico.libraries.notifications.EnumException;
import com.ibmexico.repositories.IOrdenServicioRepository;

@Service("ordenServicioService")
public class OrdenServicioService {

	@Autowired
	@Qualifier("ordenServicioRepository")
	private IOrdenServicioRepository ordenServicioRepository;
	
	@Autowired
	@Qualifier("ordenServicioPartidaService")
	private OrdenServicioPartidaService ordenServicioPartidaService;
	
	@Autowired
	@Qualifier("sessionService")
	private SessionService sessionService;
	
	public OrdenServicioEntity findByIdOrdenServicio(int idOrdenServicio) {
		return ordenServicioRepository.findByIdOrdenServicio(idOrdenServicio);
	}
	
	public List<OrdenServicioEntity> listOrdenesServicios() {
		
		List<OrdenServicioEntity> lstOrdenesServicios = null;
		
		if(sessionService.hasRol("ORDENES_SERVICIOS_ADMINISTRADOR")) {
			lstOrdenesServicios = ordenServicioRepository.listOrdenesServicios();
		}
		else {
			lstOrdenesServicios = ordenServicioRepository.listOrdenesServicios(sessionService.getCurrentUser().getIdUsuario());
		}
		
		return lstOrdenesServicios;
	}
	
	public DataTable<OrdenServicioEntity> dataTable(int idCotizacion, String search, int offset, int limit, String txtBootstrapTableDesde, String txtBootstrapTableHasta) {
		List<OrdenServicioEntity> lstCotizacionOrdenServicio = null;
		long totalServicios = 100;
		
		if(search != null) {			
			lstCotizacionOrdenServicio = ordenServicioRepository.findForDataTable(idCotizacion, search, DataTable.getPageRequest(offset, limit));
			totalServicios = ordenServicioRepository.countForDataTable(idCotizacion, search);
		} else {
			lstCotizacionOrdenServicio = ordenServicioRepository.findForDataTable(idCotizacion, DataTable.getPageRequest(offset, limit));
			totalServicios = ordenServicioRepository.countForDataTable(idCotizacion);
		}

		DataTable<OrdenServicioEntity> returnDataTable = new DataTable<OrdenServicioEntity>(lstCotizacionOrdenServicio, totalServicios);
		return returnDataTable;
	}
	
	@Transactional
	public void create(OrdenServicioEntity objOrdenServicio, String[] arrCantidad, String[] arrDescripcion, String[] arrNumeroParte, String[] arrPrecioUnitario, String[] arrImporte, String imgElabora, String imgRecibe) {
		
		if(objOrdenServicio != null) {
			
			try {
				
				LocalDateTime ldtNow = LocalDateTime.now();
				UsuarioEntity objUsuarioCreacion = sessionService.getCurrentUser();
				objOrdenServicio.setCreacionFecha(ldtNow);
				objOrdenServicio.setCreacionUsuario(objUsuarioCreacion);
				objOrdenServicio.setModificacionFecha(ldtNow);
				objOrdenServicio.setModificacionUsuario(objUsuarioCreacion);
				
				String ficheroElaboraNombre = UUID.randomUUID().toString() + ".png";
				String ficheroRecibeNombre = UUID.randomUUID().toString() + ".png";
				
				objOrdenServicio.setUrlFirmaElabora(ficheroElaboraNombre);
				objOrdenServicio.setUrlFirmaRecibe(ficheroRecibeNombre);
				
				ordenServicioRepository.save(objOrdenServicio);
				
				if(arrCantidad != null) {
					addPartidas(objOrdenServicio, arrCantidad, arrDescripcion, arrNumeroParte, arrPrecioUnitario, arrImporte);
				}
				
				addFileEntrega(objOrdenServicio, imgElabora, imgRecibe);
				
			} catch (ApplicationException | IOException exception) {
				System.out.println(exception.getMessage());
				throw new ApplicationException(EnumException.COTIZACIONES_ORDENES_SERVICIOS_FIRMA_ADD_FILE_003);
			}
			
		}
		else {
			throw new ApplicationException(EnumException.COTIZACIONES_ORDENES_SERVICIOS_CREATE_001);
		}
	}
	
	@Transactional
	public void addPartidas(OrdenServicioEntity objOrdenServicio, String[] arrCantidad, String[] arrDescripcion, String[] arrNumeroParte, String[] arrPrecioUnitario, String[] arrImporte) {
		
		if(objOrdenServicio != null) {
			if(arrCantidad != null && arrNumeroParte != null && arrDescripcion != null && arrPrecioUnitario != null) {
				
				for(int i = 0; i < arrCantidad.length; i++) {
					
					OrdenServicioPartidaEntity objPartida = new OrdenServicioPartidaEntity();
					
					objPartida.setOrdenServicio(objOrdenServicio);
					objPartida.setCantidad(new Integer(arrCantidad[i]));
					objPartida.setDescripcion(arrDescripcion[i]);
					objPartida.setNumeroParte(arrNumeroParte[i]);
					objPartida.setPrecioUnitario(new BigDecimal(arrPrecioUnitario[i]));
					objPartida.setImporte(new BigDecimal(arrImporte[i]));
					
					ordenServicioPartidaService.create(objPartida);
				}
				
				BigDecimal subtotal = ordenServicioPartidaService.calcularSubotales(objOrdenServicio);
				objOrdenServicio.setSubtotal(subtotal);
				ordenServicioRepository.save(objOrdenServicio);
				
			} else {
				throw new ApplicationException(EnumException.COTIZACIONES_ORDENES_SERVICIOS_PARTIDAS_CREATE_002);
			}	
		} else {
			throw new ApplicationException(EnumException.COTIZACIONES_ORDENES_SERVICIOS_PARTIDAS_CREATE_001);
		}
	}
	
	@Transactional
	public void addFileEntrega(OrdenServicioEntity objOrdenServicio, String ficheroElabora, String ficheroRecibe) throws IOException {
		
		URL urlPath = this.getClass().getResource("/");
		byte[] bytesFichero = Base64.getDecoder().decode(ficheroElabora);
		 
		if(objOrdenServicio != null) {
				File urlFile=new File(urlPath.getPath()+"static/ficheros/ordenesServicios");			
            	if(!urlFile.exists()){
					urlFile.mkdir();
				}
			try{
				BufferedImage img = ImageIO.read(new ByteArrayInputStream(bytesFichero));
				File imgFile = new File(urlPath.getPath() +"static/ficheros/ordenesServicios/" + objOrdenServicio.getUrlFirmaElabora());
				ImageIO.write(img, "png", imgFile);
				
				addFileRecibe(objOrdenServicio, ficheroRecibe);
			
            } catch(Exception excepcion) {
            	throw new ApplicationException(EnumException.COTIZACIONES_ORDENES_SERVICIOS_FIRMA_ADD_FILE_001);
            }        
		} else {
			throw new ApplicationException(EnumException.COTIZACIONES_ORDENES_SERVICIOS_FIRMA_CREATE_001);
		}
		
	}
	
	@Transactional
	public void addFileRecibe(OrdenServicioEntity objOrdenServicio, String fichero) throws IOException {
		
		URL urlPath = this.getClass().getResource("/");
		 
		if(objOrdenServicio != null) {
			byte[] bytesFichero = Base64.getDecoder().decode(fichero);
			
			try {
				
				BufferedImage img = ImageIO.read(new ByteArrayInputStream(bytesFichero));
				File imgFile = new File(urlPath.getPath() +"static/ficheros/ordenesServicios/" + objOrdenServicio.getUrlFirmaRecibe());
				ImageIO.write(img, "png", imgFile);            				
            	
            } catch(Exception excepcion) {
            	throw new ApplicationException(EnumException.COTIZACIONES_ORDENES_SERVICIOS_FIRMA_ADD_FILE_002);
            }          
		} else {
			throw new ApplicationException(EnumException.COTIZACIONES_ORDENES_SERVICIOS_FIRMA_CREATE_001);
		}
		
	}

	@Transactional
	public void update(OrdenServicioEntity objOrdenServicio, String imgRevisa) {
		
		if(objOrdenServicio != null) {			
			try {
				
				LocalDateTime ldtNow = LocalDateTime.now();
				UsuarioEntity objUsuarioCreacion = sessionService.getCurrentUser();
				objOrdenServicio.setModificacionFecha(ldtNow);
				objOrdenServicio.setModificacionUsuario(objUsuarioCreacion);
				
				String ficheroRevisaNombre = UUID.randomUUID().toString() + ".png";
				
				objOrdenServicio.setUrlFirmaRevisa(ficheroRevisaNombre);
				
				ordenServicioRepository.save(objOrdenServicio);				
				
				addFileRevisa(objOrdenServicio, imgRevisa);
				
			} catch (ApplicationException | IOException exception) {
				System.out.println(exception.getMessage());
				throw new ApplicationException(EnumException.COTIZACIONES_ORDENES_SERVICIOS_FIRMA_ADD_FILE_003);
			}			
		}
		else {
			throw new ApplicationException(EnumException.COTIZACIONES_ORDENES_SERVICIOS_UPDATE_001);
		}
	}
	
	@Transactional
	public void addFileRevisa(OrdenServicioEntity objOrdenServicio, String fichero) throws IOException {
		
		URL urlPath = this.getClass().getResource("/");
		 
		if(objOrdenServicio != null) {
			byte[] bytesFichero = Base64.getDecoder().decode(fichero);
			
			try {
				
				BufferedImage img = ImageIO.read(new ByteArrayInputStream(bytesFichero));
				File imgFile = new File(urlPath.getPath() +"static/ficheros/ordenesServicios/" + objOrdenServicio.getUrlFirmaRevisa());
				ImageIO.write(img, "png", imgFile);            				
            	
            } catch(Exception excepcion) {
            	throw new ApplicationException(EnumException.COTIZACIONES_ORDENES_SERVICIOS_FIRMA_ADD_FILE_004);
            }          
		} else {
			throw new ApplicationException(EnumException.COTIZACIONES_ORDENES_SERVICIOS_UPDATE_001);
		}
		
	}

	/**Metodos para el modulo de ordenes servicio sin asociacion de una cotizacion */
	public DataTable<OrdenServicioEntity> dataTableOrdenes(String search, int offset, int limit, String txtBootstrapTableDesde, String txtBootstrapTableHasta){
		List<OrdenServicioEntity> lstOrdenServicio=null;
		long totalServicios=100;
		if (search!=null) {
			lstOrdenServicio=ordenServicioRepository.findForDataTableOrdenes(search, DataTable.getPageRequest(offset, limit));
			totalServicios=ordenServicioRepository.countForDataTableOrdenes(search);
		}else{
			lstOrdenServicio=ordenServicioRepository.findForDataTableOrdenes(DataTable.getPageRequest(offset, limit));
			totalServicios=ordenServicioRepository.countForDataTableOrdenes();
		}
		DataTable<OrdenServicioEntity> returnDataTable=new DataTable<OrdenServicioEntity>(lstOrdenServicio, totalServicios);
		return returnDataTable;	
	}
	/**Fin de los metodos de ordenServicios without cotizacion */
}
