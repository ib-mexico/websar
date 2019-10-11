package com.ibmexico.controllers;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import com.ibmexico.components.ModelAndViewComponent;
import com.ibmexico.configurations.GeneralConfiguration;
import com.ibmexico.libraries.DataTable;
import com.ibmexico.libraries.Templates;
import com.ibmexico.libraries.notifications.ApplicationException;
import com.ibmexico.libraries.notifications.EnumException;
import com.ibmexico.services.BienActivoService;
import com.ibmexico.services.CatalogoActivoService;
import com.ibmexico.services.DepartamentoService;
import com.ibmexico.services.EmpresaService;
import com.ibmexico.services.UsuarioService;
import com.ibmexico.entities.BienActivoEntity;
import com.ibmexico.entities.CatalogoActivoEntity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("controlPanel/BienActivo")
public class BienActivoController {

    @Autowired
    @Qualifier("modelAndViewComponent")
    private ModelAndViewComponent modelAndViewComponent;

    @Autowired
    @Qualifier("bienActivoService")
    private BienActivoService bienActivoService;

    @Autowired
    @Qualifier("catalogoActivoService")
    private CatalogoActivoService catActiveService;

    @Autowired
    @Qualifier("empresaService")
    private EmpresaService empresaService;

    @Autowired
    @Qualifier("departamentoService")
    private DepartamentoService departamentoService;

    @Autowired
    @Qualifier("usuarioService")
    private UsuarioService usuarioService;

    @RequestMapping({ "", "/" })
    public ModelAndView index() {
        ModelAndView modelAndView = modelAndViewComponent.createModelAndViewControlPanel(Templates.ACTIVO_INDEX);
        return modelAndView;
    }

    @RequestMapping(value = "get-catalogo-data-form", method = RequestMethod.GET)
    public @ResponseBody String getDataForm() {
        Boolean respuesta = false;
        JsonObject jsonCatalogoActivo = null;
        JsonObject jsonEmpresas = null;
        JsonObject jsonDepartamento = null;
        JsonObject jsonUsuarios = null;
        try {
            jsonCatalogoActivo = catActiveService.jsonCatalogoActivo();
            jsonEmpresas = empresaService.jsonEmpresas();
            jsonDepartamento = departamentoService.jsonDepartamento();
            jsonUsuarios = usuarioService.jsonUsuariosActivos();
            respuesta = true;
        } catch (ApplicationException exception) {

        }
        JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
        jsonReturn.add("respuesta", respuesta).add("jsonCatalogoActivo", jsonCatalogoActivo)
                .add("jsonEmpresas", jsonEmpresas).add("jsonDepartamento", jsonDepartamento)
                .add("jsonUsuarios", jsonUsuarios);

        return jsonReturn.build().toString();
    }

    // OBTENER ACTIVOS DEL CATALOGO MEDIANTE AXIOS
    @RequestMapping(value = "get-activos/{idCatalogo}", method = RequestMethod.GET)
    public @ResponseBody String getActivoCatalogo(@PathVariable("idCatalogo") int idCatalogo) {
        Boolean respuesta = false;
        JsonObject jsonActivoCatalogo = null;
        try {
            jsonActivoCatalogo = bienActivoService.jsonRecursoActivoIdCatalogo(idCatalogo);
            respuesta = true;
        } catch (ApplicationException exception) {
        }
        JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
        jsonReturn.add("respuesta", respuesta).add("jsonActivoCatalogo", jsonActivoCatalogo);
        return jsonReturn.build().toString();
    }

    @RequestMapping(value = { "{idActivo}/edit", "{idActivo}/edit/" }, method = RequestMethod.GET)
    public @ResponseBody String getDataEdit(@PathVariable(name = "idActivo") int idActivo) {
        // List<CatalogoActivoEntity> lstCatalogo= catActiveService.listCatalogo();
        // List<EmpresaEntity> lstEmpresas = empresaService.listEmpresas();
        Boolean respuesta = false;
        JsonObject jsonActivo = null;
        try {
            jsonActivo = bienActivoService.jsonFindByIdRecursoActive(idActivo);
            respuesta = true;
        } catch (Exception e) {
        }
        System.out.println("salir");
        JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
        jsonReturn.add("respuesta", respuesta).add("dataActivo", jsonActivo);
        return jsonReturn.build().toString();
    }

    // Store data from ajax
    @RequestMapping(value = "storeActivoAjax", method = RequestMethod.POST)
    public @ResponseBody String storeActivoAjax(
            @RequestParam(value = "cmbCatalogo", required = true) Integer cmbCatalogo,
            @RequestParam(value = "txtDescripcion", required = true) String txtDescripcion,
            @RequestParam(value = "txtMarca", required = true) String txtMarca,
            @RequestParam(value = "txtModelo", required = true) String txtModelo,
            @RequestParam(value = "txtSerie", required = true) String txtSerie,
            @RequestParam(value = "txtColor", required = true) String txtColor,
            @RequestParam(value = "txtFechaCompra", required = false) String txtFechaCompra,

            @RequestParam(value = "txtCosto", required = false) BigDecimal txtCosto,
            @RequestParam(value = "txtGarantia", required = false) Integer txtGarantia,
            @RequestParam(value = "txtObsolecencia", required = false) String txtObsolecencia,

            @RequestParam(value = "txtPlaca", required = false) String txtPlaca,
            @RequestParam(value = "txtTipoVehiculo", required = false) String txtTipoVehiculo,

            @RequestParam(value = "txtSerieEvaporadora", required = false) String txtSerieEvaporadora,
            @RequestParam(value = "txtSerieCondensadora", required = false) String txtSerieCondensadora,
            @RequestParam(value = "chkControlRemoto", required = false, defaultValue = "false") String chkControlRemoto,

            @RequestParam(value = "txtFechaEntrega", required = false) String txtFechaEntrega,
            @RequestParam(value = "txtImei", required = false) Integer txtImei,
            @RequestParam(value = "txtAlmacenamientoExterna", required = false) Integer txtAlmacenamientoExterna,

            @RequestParam(value = "txtTipoEquipo", required = false) String txtTipoEquipo,
            @RequestParam(value = "txtTipoRam", required = false) String txtTipoRam,
            @RequestParam(value = "txtCapMemRam", required = false) Integer txtCapMemRam,
            @RequestParam(value = "txtTipoProcesador", required = false) String txtTipoProcesador,
            @RequestParam(value = "txtMarcaProcesador", required = false) String txtMarcaProcesador,
            @RequestParam(value = "txtCapProcesador", required = false) Integer txtCapProcesador,
            @RequestParam(value = "txtTipoHDD", required = false) String txtTipoHDD,
            @RequestParam(value = "txtCapIntHDD", required = false) Integer txtCapIntHDD,
            @RequestParam(value = "chkMonitor", required = false) String chkMonitor,

            @RequestParam(value = "txtTamanioMonitor", required = false) Integer txtTamanioMonitor,
            @RequestParam(value = "txtColorMonitor", required = false) String txtColorMonitor,
            @RequestParam(value = "txtModeloMonitor", required = false) String txtModeloMonitor,
            @RequestParam(value = "txtNumParte", required = false) String txtNumParte,

            @RequestParam(value = "txtPeridoMantenimiento", required = false) Integer txtPeridoMantenimiento,
            // @RequestParam(value = "txtCostoPromedioMant", required = false) BigDecimal txtCostoPromedioMant,
            // @RequestParam(value = "txtFechaUltimoMant", required = false) String txtFechaUltimoMant,

            @RequestParam(value = "cmbEmpresa") Integer cmbEmpresa,
            @RequestParam(value = "cmbDepartamento", required = false) Integer cmbDepartamento,
            @RequestParam(value = "cmbUsuario", required = false) Integer cmbUsuario,
            @RequestParam("fichero") MultipartFile[] fichero,
            // @RequestParam(value = "chkRequireMant", required = false) String chkRequireMant,
            // @RequestParam(value="txtFechaMant", required = false) String txtFechaMant,
            @RequestParam(value = "txtObservaciones", required = false) String txtObservaciones) throws IOException {

		Boolean respuesta = false;
		String titulo = "Oops!";
		String mensaje = "Ocurrió un error al crear los Activos en el sistema.";

        BienActivoEntity objActivo=new BienActivoEntity();
        CatalogoActivoEntity objCatalogo= catActiveService.findByIdCatalogoActivo(cmbCatalogo);
        
        int tamanioActivoPorCatalogo = bienActivoService.contForCatalogo(objCatalogo.getIdCatalogoActivo());

        try{
            objActivo.setIdActivo(catActiveService.findByIdCatalogoActivo(cmbCatalogo));
            objActivo.setDescripcion(txtDescripcion);
            objActivo.setMarca(txtMarca);
            objActivo.setModelo(txtModelo);
            objActivo.setSerie(txtSerie);
            objActivo.setColor(txtColor);

            System.out.println(txtFechaCompra);
            
            objActivo.setFechaCompra(LocalDate.parse(txtFechaCompra, GeneralConfiguration.getInstance().getDateFormatterNatural()));
            objActivo.setCosto(txtCosto);
            objActivo.setGarantiaMes(txtGarantia);
            if(!txtObsolecencia.equals("")){
                objActivo.setObsolecensia(LocalDate.parse(txtObsolecencia, GeneralConfiguration.getInstance().getDateFormatterNatural()));
            }

            if(cmbCatalogo==1){
                objActivo.setPlaca(txtPlaca);
                objActivo.setTipoVehiculo(txtTipoVehiculo);
            }
            if(cmbCatalogo==2){
                objActivo.setSerieEvaporadora(txtSerieEvaporadora);
                objActivo.setSerieCondensadora(txtSerieCondensadora);
                if(chkControlRemoto.equals("true")){
                    objActivo.setBoolControlRemoto(true);
                }else{
                    objActivo.setBoolControlRemoto(false);
                }
            }
            if(cmbCatalogo==3){
                objActivo.setFechaEntregaMovil(LocalDate.parse(txtFechaEntrega, GeneralConfiguration.getInstance().getDateFormatterNatural()));
                objActivo.setImei(txtImei);
                objActivo.setAlmacenamientoExterna(txtAlmacenamientoExterna);
            }
            if(cmbCatalogo==4){
                objActivo.setTipoEquipo(txtTipoEquipo);
                objActivo.setTipoMemoriaRam(txtTipoRam);
                objActivo.setCapacidadMemoriaRam(txtCapMemRam);
                objActivo.setTipoProcesador(txtTipoProcesador);
                objActivo.setMarcaProcesador(txtMarcaProcesador);
                objActivo.setCapacidadProcesador(txtCapProcesador);
                objActivo.setTipoHdd(txtTipoHDD);
                objActivo.setCapacidadInternoHdd(txtCapIntHDD);
                if(chkMonitor.equals("true")){
                    objActivo.setCuentaMonitor(true);
                    objActivo.setTamanio(txtTamanioMonitor);
                    objActivo.setColorMonitor(txtColorMonitor);
                    objActivo.setModeloMonitor(txtModeloMonitor);
                    objActivo.setNumeroParte(txtNumParte);
                }else{
                    objActivo.setCuentaMonitor(false);
                }
            }
            objActivo.setPeriodoMantEstimado(txtPeridoMantenimiento);
            // if(!txtModelo.equals("") ){
            //     objActivo.setModelo(txtModelo);
            // }
            objActivo.setEmpresa(empresaService.findByIdEmpresa(cmbEmpresa));
            objActivo.setIdActivo(catActiveService.findByIdCatalogoActivo(cmbCatalogo));

            if(cmbDepartamento !=null){
                objActivo.setIdDepartamento(departamentoService.findByIdDepartamento(cmbDepartamento));
            }
            if(cmbUsuario !=null){
                objActivo.setUsuario(usuarioService.findByIdUsuarioNoEliminado(cmbUsuario));
            }
            // if(chkRequireMant.equals("true")){
            //     objActivo.setRequiereMantenimiento(true);
            //     objActivo.setFechaDeMantenimiento(LocalDate.parse(txtFechaMant, GeneralConfiguration.getInstance().getDateFormatterNatural()));
            // }else{
            //     objActivo.setRequiereMantenimiento(false);
            //     objActivo.setFechaDeMantenimiento(null);
            // }
            objActivo.setObservaciones(txtObservaciones);

            objActivo.setNumeroEconomico(objCatalogo.getClave()+'-'+objActivo.getEmpresa().getClave()+"-00"+ ++tamanioActivoPorCatalogo);
            
            bienActivoService.create(objActivo, fichero);

            // bienActivoService.create(objActivo);
			respuesta = true;
			titulo = "Excelente!";
            mensaje = "Nuevo activo exitosamente creado.";
        }catch(Exception e){
            throw new ApplicationException(EnumException.ACTIVO_CREATE_001);
        }
		

		
		JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
		jsonReturn	.add("respuesta", respuesta)
					.add("titulo", titulo)
					.add("mensaje", mensaje);
		return jsonReturn.build().toString();
	}

    // for table
	@RequestMapping(value = "/table", method = RequestMethod.POST)	
	public @ResponseBody String table(	@RequestParam("offset") int offset,
										@RequestParam("limit") int limit,
										@RequestParam("_csrf") String _csrf,
										@RequestParam(value="search", required=false, defaultValue="") String search,
										@RequestParam(value="txtBootstrapTableDesde", required=false) String txtBootstrapTableDesde,
										@RequestParam(value="txtBootstrapTableHasta", required=false) String txtBootstrapTableHasta) {
		
		DataTable<BienActivoEntity> dtActivo = bienActivoService.dataTable(search, offset, limit, txtBootstrapTableDesde, txtBootstrapTableHasta);
		
		JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
		jsonReturn.add("total", dtActivo.getTotal());
		JsonArrayBuilder jsonRows = Json.createArrayBuilder();
		
		dtActivo.getRows().forEach((itemCatActivo)-> {

            jsonRows.add(Json.createObjectBuilder()
                .add("id_recurso_activo", itemCatActivo.getIdRecursoActivo())
                .add("numero_unico",itemCatActivo.getNumeroEconomico())
                .add("descripcion", itemCatActivo.getDescripcion())
                .add("marca", itemCatActivo.getMarca())
                .add("modelo",itemCatActivo.getModelo() !=null ?  itemCatActivo.getModelo() : "N/A" )
                .add("estatus", itemCatActivo.isEstatus())
                .add("catalogo", itemCatActivo.getIdActivo().getNombre())
                .add("departamento", itemCatActivo.getIdDepartamento() != null ? itemCatActivo.getIdDepartamento().getDepartamento() : "No definido" )
                .add("usuario_asignado", itemCatActivo.getUsuario() != null ? itemCatActivo.getUsuario().getNombreCompleto() : "No definido")
                .add("creacion_fecha", itemCatActivo.getCreacionFechaNatural())
                .add("creacion_id_usuario", itemCatActivo.getCreacionUsuario().getNombre()));
		});
		jsonReturn.add("rows", jsonRows);	

		return jsonReturn.build().toString();
    }
    
    //delete active change your status=false

    @RequestMapping(value = {"{idActivo}/delete","{idActivo}/delete/"} ,method = RequestMethod.POST)
	public @ResponseBody String deleteAjax(@PathVariable(name="idActivo")int idActivo){

		Boolean respuesta = false;
		String titulo = "Oops!";
		String mensaje = "Ocurrió un error al intentar eliminar un Activo.";

		BienActivoEntity objActivo= bienActivoService.findByIdRecursoActive(idActivo);
		try {
			if(objActivo!=null){
                objActivo.setEstatus(false);
				bienActivoService.update(objActivo);
                respuesta = true;
                titulo = "Eliminado!";
                mensaje = "El catalogo ha sido eliminado exitosamente.";
			}else{
				throw new ApplicationException(EnumException.ACTIVO_DELETE_001);
			}
		} catch (ApplicationException e) {
			throw new ApplicationException(EnumException.ACTIVO_DELETE_001);
		}
		JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
		jsonReturn	.add("respuesta", respuesta)
					.add("titulo", titulo)
					.add("mensaje", mensaje);							
		return jsonReturn.build().toString();
    }
    
    // update active
    @RequestMapping(value = {"{idActivo}/update","{idActivo}/update/"}, method = RequestMethod.POST)
    public @ResponseBody String updateDataActivo(
        @RequestParam(value = "cmbCatalogo", required = false) Integer cmbCatalogo,
        @RequestParam(value = "txtDescripcion", required = false) String txtDescripcion,
        @RequestParam(value = "txtMarca", required = false) String txtMarca,
        @RequestParam(value = "txtModelo", required = false) String txtModelo,
        @RequestParam(value = "txtSerie", required = false) String txtSerie,
        @RequestParam(value = "txtColor", required = false) String txtColor,
        @RequestParam(value = "txtFechaCompra", required = false) String txtFechaCompra,

        @RequestParam(value = "txtCosto", required = false) BigDecimal txtCosto,
        @RequestParam(value = "txtGarantia", required = false) Integer txtGarantia,
        @RequestParam(value = "txtObsolecencia", required = false) String txtObsolecencia,

        @RequestParam(value = "txtPlaca", required = false) String txtPlaca,
        @RequestParam(value = "txtTipoVehiculo", required = false) String txtTipoVehiculo,

        @RequestParam(value = "txtSerieEvaporadora", required = false) String txtSerieEvaporadora,
        @RequestParam(value = "txtSerieCondensadora", required = false) String txtSerieCondensadora,
        @RequestParam(value = "chkControlRemoto", required = false, defaultValue = "false") String chkControlRemoto,

        @RequestParam(value = "txtFechaEntrega", required = false) String txtFechaEntrega,
        @RequestParam(value = "txtImei", required = false) Integer txtImei,
        @RequestParam(value = "txtAlmacenamientoExterna", required = false) Integer txtAlmacenamientoExterna,

        @RequestParam(value = "txtTipoEquipo", required = false) String txtTipoEquipo,
        @RequestParam(value = "txtTipoRam", required = false) String txtTipoRam,
        @RequestParam(value = "txtCapMemRam", required = false) Integer txtCapMemRam,
        @RequestParam(value = "txtTipoProcesador", required = false) String txtTipoProcesador,
        @RequestParam(value = "txtMarcaProcesador", required = false) String txtMarcaProcesador,
        @RequestParam(value = "txtCapProcesador", required = false) Integer txtCapProcesador,
        @RequestParam(value = "txtTipoHDD", required = false) String txtTipoHDD,
        @RequestParam(value = "txtCapIntHDD", required = false) Integer txtCapIntHDD,
        @RequestParam(value = "chkMonitor", required = false) String chkMonitor,

        @RequestParam(value = "txtTamanioMonitor", required = false) Integer txtTamanioMonitor,
        @RequestParam(value = "txtColorMonitor", required = false) String txtColorMonitor,
        @RequestParam(value = "txtModeloMonitor", required = false) String txtModeloMonitor,
        @RequestParam(value = "txtNumParte", required = false) String txtNumParte,

        @RequestParam(value = "txtPeridoMantenimiento", required = false) Integer txtPeridoMantenimiento,

        @RequestParam(value = "cmbEmpresa") Integer cmbEmpresa,
        @RequestParam(value = "cmbDepartamento", required = false) Integer cmbDepartamento,
        @RequestParam(value = "cmbUsuario", required = false) Integer cmbUsuario,
        // @RequestParam("fichero") MultipartFile[] fichero,
        @RequestParam(value = "txtObservaciones", required = false) String txtObservaciones,
        @PathVariable(name="idActivo")int idActivo ){

            Boolean respuesta = false;
            String titulo = "Oops!";
            String mensaje = "Ocurrió un error al intentar editar un Activo.";
            BienActivoEntity objActivo= bienActivoService.findByIdRecursoActive(idActivo);

            try {
                if(objActivo!=null){
                    objActivo.setIdActivo(catActiveService.findByIdCatalogoActivo(cmbCatalogo));
                    objActivo.setDescripcion(txtDescripcion);
                    objActivo.setMarca(txtMarca);
                    objActivo.setModelo(txtModelo);
                    objActivo.setSerie(txtSerie);
                    objActivo.setColor(txtColor);
                    System.out.println(txtFechaCompra);
                    objActivo.setFechaCompra(LocalDate.parse(txtFechaCompra, GeneralConfiguration.getInstance().getDateFormatterNatural()));
                    objActivo.setCosto(txtCosto);
                    objActivo.setGarantiaMes(txtGarantia);
                    if(!txtObsolecencia.equals("")){
                        objActivo.setObsolecensia(LocalDate.parse(txtObsolecencia, GeneralConfiguration.getInstance().getDateFormatterNatural()));
                    }
                    if(cmbCatalogo==1){
                        objActivo.setPlaca(txtPlaca);
                        objActivo.setTipoVehiculo(txtTipoVehiculo);
                    }
                    if(cmbCatalogo==2){
                        objActivo.setSerieEvaporadora(txtSerieEvaporadora);
                        objActivo.setSerieCondensadora(txtSerieCondensadora);
                        if(chkControlRemoto.equals("on")){
                            objActivo.setBoolControlRemoto(true);
                        }else{
                            objActivo.setBoolControlRemoto(false);
                        }
                    }
                    if(cmbCatalogo==3){
                        objActivo.setFechaEntregaMovil(LocalDate.parse(txtFechaEntrega, GeneralConfiguration.getInstance().getDateFormatterNatural()));
                        objActivo.setImei(txtImei);
                        objActivo.setAlmacenamientoExterna(txtAlmacenamientoExterna);
                    }
                    if(cmbCatalogo==4){
                        objActivo.setTipoEquipo(txtTipoEquipo);
                        objActivo.setTipoMemoriaRam(txtTipoRam);
                        objActivo.setCapacidadMemoriaRam(txtCapMemRam);
                        objActivo.setTipoProcesador(txtTipoProcesador);
                        objActivo.setMarcaProcesador(txtMarcaProcesador);
                        objActivo.setCapacidadProcesador(txtCapProcesador);
                        objActivo.setTipoHdd(txtTipoHDD);
                        objActivo.setCapacidadInternoHdd(txtCapIntHDD);
                        if(chkMonitor.equals("on")){
                            objActivo.setCuentaMonitor(true);
                            objActivo.setTamanio(txtTamanioMonitor);
                            objActivo.setColorMonitor(txtColorMonitor);
                            objActivo.setModeloMonitor(txtModeloMonitor);
                            objActivo.setNumeroParte(txtNumParte);
                        }else{
                            objActivo.setCuentaMonitor(false);
                        }
                    }
                    objActivo.setPeriodoMantEstimado(txtPeridoMantenimiento);

                    objActivo.setEmpresa(empresaService.findByIdEmpresa(cmbEmpresa));
        
                    if(cmbDepartamento !=null){
                        objActivo.setIdDepartamento(departamentoService.findByIdDepartamento(cmbDepartamento));
                    }
                    if( cmbUsuario>1 && cmbUsuario !=null ){
                        objActivo.setUsuario(usuarioService.findByIdUsuarioNoEliminado(cmbUsuario));
                    }
                    objActivo.setObservaciones(txtObservaciones);
                    bienActivoService.update(objActivo);
                    respuesta = true;
				 titulo = "Modificado!";
				 mensaje = "El catalogo ha sido modificado exitosamente.";
                }
                
            } catch (ApplicationException exception) {
                throw new ApplicationException(EnumException.ACTIVO_CREATE_001);
            }
        JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
		jsonReturn	.add("respuesta", respuesta)
					.add("titulo", titulo)
					.add("mensaje", mensaje);							
		return jsonReturn.build().toString();
    }
    
}