package com.ibmexico.controllers;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import com.ibmexico.components.ModelAndViewComponent;
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
        JsonObject jsonEmpresas=null;
        JsonObject jsonDepartamento=null;
        JsonObject jsonUsuarios=null;
        try {
            jsonCatalogoActivo = catActiveService.jsonCatalogoActivo();
            jsonEmpresas=empresaService.jsonEmpresas();
            jsonDepartamento= departamentoService.jsonDepartamento();
            jsonUsuarios= usuarioService.jsonUsuariosActivos();
            respuesta = true;
        } catch (ApplicationException exception) {

        }
        JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
        jsonReturn.add("respuesta", respuesta).add("jsonCatalogoActivo", jsonCatalogoActivo)
        .add("jsonEmpresas", jsonEmpresas).add("jsonDepartamento", jsonDepartamento)
        .add("jsonUsuarios", jsonUsuarios);

        return jsonReturn.build().toString();
    }
    //OBTENER ACTIVOS DEL CATALOGO MEDIANTE AXIOS 
    @RequestMapping(value="get-activos/{idCatalogo}", method = RequestMethod.GET)
    public @ResponseBody String getActivoCatalogo(@PathVariable("idCatalogo") int idCatalogo){

        Boolean respuesta=false;
        JsonObject jsonActivoCatalogo=null;
        try {
            jsonActivoCatalogo=bienActivoService.jsonRecursoActivoIdCatalogo(idCatalogo);
            respuesta =true;
        } catch (ApplicationException exception) {
            
        }
        JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
        jsonReturn.add("respuesta", respuesta).add("jsonActivoCatalogo", jsonActivoCatalogo);
        return jsonReturn.build().toString();
    }

    @RequestMapping(value = {"{idActivo}/edit","{idActivo}/edit/"} , method = RequestMethod.GET)
    public @ResponseBody String getDataEdit(@PathVariable(name ="idActivo")int idActivo){
        // List<CatalogoActivoEntity> lstCatalogo= catActiveService.listCatalogo();
        // List<EmpresaEntity> lstEmpresas = empresaService.listEmpresas();
        Boolean respuesta=false;

        JsonObject jsonActivo=null;
        try {
            jsonActivo=bienActivoService.jsonFindByIdMobiliario(idActivo);
            respuesta=true;
        } catch (Exception e) {
          
        }
        JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
        jsonReturn.add("respuesta", respuesta).add("dataActivo", jsonActivo);

        return jsonReturn.build().toString();
    }

    // Store data from ajax
    @RequestMapping(value = "storeActivoAjax", method = RequestMethod.POST)
    public @ResponseBody String storeActivoAjax(@RequestParam(value = "txtNombre", required = true) String txtNombre,
            @RequestParam(value = "txtMarca", required = false) String txtMarca,
            @RequestParam(value = "txtModelo", required = false, defaultValue="") String txtModelo,
            @RequestParam(value = "txtColor", required = false) String txtColor,
            @RequestParam(value= "txtSerie", required=false) String txtSerie,
            @RequestParam(value="txtPlaca", required = false, defaultValue="") String txtPlaca,
            @RequestParam(value="txtObservaciones", required = false) String txtObservaciones,
            @RequestParam(value = "cmbCatalogo") Integer cmbCatalogo,
            @RequestParam(value="cmbEmpresa" )Integer cmbEmpresa,
            @RequestParam(value = "cmbDepartamento" ,required=false) Integer cmbDepartamento,
            @RequestParam(value="cmbUsuario",required=false) Integer cmbUsuario,
            @RequestParam("fichero") MultipartFile[] fichero) {

		Boolean respuesta = false;
		String titulo = "Oops!";
		String mensaje = "Ocurrió un error al crear los Activos en el sistema.";

		BienActivoEntity objActivo=new BienActivoEntity();
		try {
            objActivo.setNombre(txtNombre);
            objActivo.setMarca(txtMarca);
            objActivo.setColor(txtColor);
            objActivo.setSerie(txtSerie);

            if(!txtModelo.equals("") ){
                objActivo.setModelo(txtModelo);
            }
            if(!txtPlaca.equals("")){
                objActivo.setPlaca(txtPlaca);    
            }
            
            objActivo.setObservaciones(txtObservaciones);
            objActivo.setEmpresa(empresaService.findByIdEmpresa(cmbEmpresa));
            objActivo.setIdActivo(catActiveService.findByIdCatalogoActivo(cmbCatalogo));

            if(cmbDepartamento !=null){
                objActivo.setIdDepartamento(departamentoService.findByIdDepartamento(cmbDepartamento));
            }
            if(cmbUsuario !=null){
                objActivo.setUsuario(usuarioService.findByIdUsuarioNoEliminado(cmbUsuario));
            }
            bienActivoService.create(objActivo, fichero);

            // bienActivoService.create(objActivo);
			respuesta = true;
			titulo = "Excelente!";
			mensaje = "Nuevo activo exitosamente creado.";
			
		} catch(Exception e) {
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
                .add("id_activo_mobiliario", itemCatActivo.getIdActivoMobiliario())
                .add("nombre", itemCatActivo.getNombre())
                .add("marca", itemCatActivo.getMarca())
                .add("modelo",itemCatActivo.getModelo() !=null ?  itemCatActivo.getModelo() : "No definido" )
                .add("color", itemCatActivo.getColor())
                .add("placa", itemCatActivo.getPlaca()  != null ?  itemCatActivo.getPlaca() :  "No tiene" )
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

		BienActivoEntity objActivo= bienActivoService.findByIdActivoMobiliario(idActivo);
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
    
    @RequestMapping(value = {"{idActivo}/update","{idActivo}/update/"}, method = RequestMethod.POST)
    public @ResponseBody String updateDataActivo(   @RequestParam(value = "txtNombre", required=false) String txtNombre,
                                                    @RequestParam(value = "txtMarca", required=false) String txtMarca,
                                                    @RequestParam(value = "txtModelo", required=false) String txtModelo,
                                                    @RequestParam(value = "txtColor", required = false) String txtColor,
                                                    @RequestParam(value= "txtSerie", required=false) String txtSerie,
                                                    @RequestParam(value="txtObservaciones", required = false) String txtObservaciones,
                                                    @RequestParam(value = "cmbCatalogo",required = false) int cmbCatalogo,
                                                    @RequestParam(value="cmbEmpresa", required = false)int cmbEmpresa,
                                                    @PathVariable(name="idActivo")int idActivo                                                  
        ){
            Boolean respuesta = false;
            String titulo = "Oops!";
            String mensaje = "Ocurrió un error al intentar editar un Activo.";
            System.out.println(txtNombre+txtMarca+txtModelo);
            BienActivoEntity objActivo= bienActivoService.findByIdActivoMobiliario(idActivo);

            try {
                if(objActivo!=null){
                    objActivo.setNombre(txtNombre); 

                    objActivo.setMarca(txtMarca);
                    objActivo.setModelo(txtModelo);
                    objActivo.setColor(txtColor);
                    objActivo.setSerie(txtSerie);
                    objActivo.setObservaciones(txtObservaciones);
                    if(cmbEmpresa>0){
                         objActivo.setEmpresa(empresaService.findByIdEmpresa(cmbEmpresa));
                    }else{
                        objActivo.setEmpresa(null);
                    }
                    if(cmbCatalogo>0){
                       objActivo.setIdActivo(catActiveService.findByIdCatalogoActivo(cmbCatalogo));  
                    }else{
                        objActivo.setIdActivo(null);
                    }
                   
                   
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