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
import com.ibmexico.services.EmpresaService;
import com.ibmexico.entities.BienActivoEntity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
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
        try {
            jsonCatalogoActivo = catActiveService.jsonCatalogoActivo();
            jsonEmpresas=empresaService.jsonEmpresas();
            respuesta = true;
        } catch (ApplicationException exception) {

        }
        JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
        jsonReturn.add("respuesta", respuesta).add("jsonCatalogoActivo", jsonCatalogoActivo)
        .add("jsonEmpresas", jsonEmpresas);

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
            @RequestParam(value = "txtModelo", required = false) String txtModelo,
            @RequestParam(value = "txtColor", required = false) String txtColor,
            @RequestParam(value= "txtSerie", required=false) String txtSerie,
            @RequestParam(value="txtObservaciones", required = false) String txtObservaciones,
            @RequestParam(value = "cmbCatalogo") Integer cmbCatalogo,
            @RequestParam(value="cmbEmpresa" )Integer cmbEmpresa) {

		Boolean respuesta = false;
		String titulo = "Oops!";
		String mensaje = "Ocurrió un error al crear los Activos en el sistema.";
		
		BienActivoEntity objActivo=new BienActivoEntity();
		try {
            objActivo.setNombre(txtNombre);
            objActivo.setMarca(txtMarca);
            objActivo.setModelo(txtModelo);
            objActivo.setColor(txtColor);
            objActivo.setSerie(txtSerie);
            objActivo.setObservaciones(txtObservaciones);
            objActivo.setEmpresa(empresaService.findByIdEmpresa(cmbEmpresa));
            objActivo.setIdActivo(catActiveService.findByIdCatalogoActivo(cmbCatalogo));
    
            bienActivoService.create(objActivo);
			respuesta = true;
			titulo = "Excelente!";
			mensaje = "Nuevo activo exitosamente creado.";
			
		} catch(ApplicationException exception) {
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
                .add("modelo",itemCatActivo.getModelo())
                .add("color", itemCatActivo.getColor())
                .add("estatus", itemCatActivo.isEstatus())
                .add("Catalogo", itemCatActivo.getIdActivo().getNombre())
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