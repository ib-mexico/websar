package com.ibmexico.services;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ibmexico.entities.EmpresaEntity;
import com.ibmexico.entities.UsuarioEntity;
import com.ibmexico.libraries.DataTable;
import com.ibmexico.libraries.notifications.ApplicationException;
import com.ibmexico.libraries.notifications.EnumException;
import com.ibmexico.repositories.IEmpresaRepository;

@Service("empresaService")
public class EmpresaService {

	@Autowired
	@Qualifier("empresaRepository")
	private IEmpresaRepository empresaRepository;
	
	@Autowired
	@Qualifier("sessionService")
	private SessionService sessionService;
	
	public EmpresaEntity findByIdEmpresa(int idEmpresa) {
		return empresaRepository.findByIdEmpresa(idEmpresa);
	}
	
	public List<EmpresaEntity> listEmpresas() {
		return empresaRepository.listEmpresas();
	}

	public JsonObject jsonEmpresas() {
		JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
		JsonArrayBuilder jsonRows = Json.createArrayBuilder();

		List<EmpresaEntity> lstEmpresas = empresaRepository.listEmpresas();

		lstEmpresas.forEach((item)-> {
			jsonRows.add(Json.createObjectBuilder()
				.add("id_empresa", item.getIdEmpresa())
				.add("razon_social", item.getRazonSocial())
			);
		});

		jsonReturn.add("rows", jsonRows);
		
		return jsonReturn.build();
	}

	public JsonObject jsonEmpresasById(int idEmpresa) {
		JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
		JsonArrayBuilder jsonRows = Json.createArrayBuilder();
		EmpresaEntity obJEmpresa = empresaRepository.findByIdEmpresa(idEmpresa);
		
		jsonRows.add(Json.createObjectBuilder()
					.add("idEmpresa", obJEmpresa.getIdEmpresa())
					.add("nombreEmpresa", obJEmpresa.getEmpresa())
					.add("claveEmpresa", obJEmpresa.getClave())
					.add("direccionEmpresa", obJEmpresa.getDireccion())
					.add("dominioEmpresa", obJEmpresa.getDominio())
					.add("razonSocial", obJEmpresa.getRazonSocial())
					.add("logo", obJEmpresa.getLogotipo())
					);

		jsonReturn.add("rows", jsonRows);
		return jsonReturn.build();
	}


	    //Tabla detalle de los gastos
		public DataTable<EmpresaEntity> dataTable(String search, int offset, int limit, String txtBootstrapTableDesde, String txtBootstrapTableHasta){
			List<EmpresaEntity> lstEmpresa = null;
			LocalDate fechaInicio=null;
			LocalDate fechaFin=null;
			long total = 100;
			
			if(search != null){
				if(!txtBootstrapTableDesde.equals("") && !txtBootstrapTableHasta.equals("")){
					String arrFechaInicio[]= txtBootstrapTableDesde.split("/");
					int yearInicio=Integer.parseInt(arrFechaInicio[2]);
					int monthInicio=Integer.parseInt(arrFechaInicio[1]);
					int dayInicio=Integer.parseInt(arrFechaInicio[0]);
		
					String arrFechaFin[]=txtBootstrapTableHasta.split("/");
					int yearFin=Integer.parseInt(arrFechaFin[2]);
					int monthFin= Integer.parseInt(arrFechaFin[1]);
					int dayFin= Integer.parseInt(arrFechaFin[0]);
		
					fechaInicio=LocalDate.of(yearInicio, monthInicio, dayInicio);
					fechaFin=LocalDate.of(yearFin, monthFin, dayFin);
		
					lstEmpresa = empresaRepository.findForDataTable(search, fechaInicio	, fechaFin, DataTable.getPageRequest(offset, limit));
						total= empresaRepository.countForDataTable(search, fechaInicio, fechaFin);
				}else{
					lstEmpresa = empresaRepository.findForDataTable(search, DataTable.getPageRequest(offset, limit));
					total = empresaRepository.countForDataTable(search);	
				}
			}
			else{
				lstEmpresa = empresaRepository.findForDataTable(DataTable.getPageRequest(offset, limit));
				total = empresaRepository.countForDataTable();
			}
			
			DataTable<EmpresaEntity> returnDataTable=new DataTable<EmpresaEntity>(lstEmpresa , total);
			return returnDataTable;
		}
	


	@Transactional
	public void create(EmpresaEntity objEmpresa, MultipartFile file){
		if (objEmpresa!=null) {
			if (file!=null && !file.isEmpty() ) {
				try {
					if(!file.getOriginalFilename().trim().equals("")){
						String ficheroNombre = UUID.randomUUID().toString();
						String[] arrNombreFichero = file.getOriginalFilename().split("\\.");
						if(arrNombreFichero.length>=2){
							ficheroNombre =ficheroNombre +"."+arrNombreFichero[arrNombreFichero.length-1];
						}
						
						objEmpresa.setLogotipo(ficheroNombre);
						addLogo(objEmpresa, file);
					}
				} catch (Exception e) {
					throw new ApplicationException(EnumException.EMPRESA_FICHERO_ADD_FILE_003);
				}
			} else {
				LocalDateTime ldtFecha=LocalDateTime.now();
				UsuarioEntity objUsuario = sessionService.getCurrentUser();
				objEmpresa.setCreacionFecha(ldtFecha);
				objEmpresa.setModificacionFecha(ldtFecha);
				objEmpresa.setCreacionUsuario(objUsuario);
				objEmpresa.setModificacionUsuario(objUsuario);
				objEmpresa.setEliminado(false);
				empresaRepository.save(objEmpresa);
			}
		} else {
			throw new ApplicationException(EnumException.EMPRESA_FICHERO_ADD_FILE_001);
		}

	}

	//metodo para guardar archivo / recurso logo
	@Transactional
	public void addLogo(EmpresaEntity objEmpresa, MultipartFile objFile)throws IOException {
		URL urlPath = this.getClass().getResource("/");

		if (objEmpresa!=null) {
			if(objEmpresa.getLogotipo() !=""){
				byte[] bytesFichero=objFile.getBytes();
				 // Crear carpeta si no existe
				 File fileruta = new File(urlPath.getPath() + "static/ficheros/LogoEmpresa");
				 if (!fileruta.exists()) {
					 fileruta.mkdirs();
				 }
				try (BufferedOutputStream buffStream=new BufferedOutputStream(new FileOutputStream(new File(urlPath.getPath()+"static/ficheros/LogoEmpresa/"+objEmpresa.getLogotipo())))) {
					buffStream.write(bytesFichero);
					LocalDateTime ldtFecha=LocalDateTime.now();
					UsuarioEntity objUsuario = sessionService.getCurrentUser();
					objEmpresa.setCreacionFecha(ldtFecha);
					objEmpresa.setModificacionFecha(ldtFecha);
					objEmpresa.setCreacionUsuario(objUsuario);
					objEmpresa.setModificacionUsuario(objUsuario);
					objEmpresa.setEliminado(false);
					empresaRepository.save(objEmpresa);
				} catch (Exception e) {
					throw new ApplicationException(EnumException.EMPRESA_FICHERO_ADD_FILE_003);
				}
			}else{
				throw new ApplicationException(EnumException.EMPRESA_FICHERO_ADD_FILE_004);
			}
		}
		else {
			throw new ApplicationException(EnumException.EMPRESA_FICHERO_ADD_FILE_001);
		}	
	}
	
	public void delete(int id) {
		try {
			empresaRepository.delete(id);
		} catch (Exception e) {
			throw new ApplicationException(EnumException.EMPRESA_DELETE_001);
		}
	}

	public void update(EmpresaEntity objEmpresa){
		if (objEmpresa!=null) {
			LocalDateTime ldtNow=LocalDateTime.now();
			objEmpresa.setModificacionFecha(ldtNow);
			empresaRepository.save(objEmpresa);
		} else {
			throw new ApplicationException(EnumException.EMPRESA_DELETE_001);
		}

	}
}
