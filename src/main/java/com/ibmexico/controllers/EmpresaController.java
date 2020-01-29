package com.ibmexico.controllers;

import java.io.IOException;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import com.ibmexico.components.ModelAndViewComponent;
import com.ibmexico.entities.EmpresaEntity;
import com.ibmexico.libraries.DataTable;
import com.ibmexico.libraries.Templates;
import com.ibmexico.libraries.notifications.ApplicationException;
import com.ibmexico.libraries.notifications.EnumException;
import com.ibmexico.services.EmpresaService;
import com.ibmexico.services.SessionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("controlPanel/empresa")
public class EmpresaController{

    @Autowired
    @Qualifier("empresaService")
    private EmpresaService empresaService;

    @Autowired
    @Qualifier("modelAndViewComponent")
    private ModelAndViewComponent modelAndViewComponent;

    @Autowired
	@Qualifier("sessionService")
	private SessionService sessionService;

    @GetMapping({"","/"})
    public ModelAndView index(){
        ModelAndView modelAndView = modelAndViewComponent.createModelAndViewControlPanel(Templates.CATALOGO_EMPRESA);
        return modelAndView;
    }
    
    @RequestMapping(value = "get-empresa", method = RequestMethod.GET)
    public @ResponseBody String getDataRecursoForm() {
        Boolean respuesta = false;
        JsonObject jsonEmpresa = null;
        Boolean OpnUca= false;
        try {
            OpnUca = sessionService.hasRol("OPORTUNIDADES_EMPRESA_UCA");
            jsonEmpresa = empresaService.jsonEmpresas();
            respuesta = true;
        } catch (Exception exception) {
            exception.getMessage();
        }
        JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
        jsonReturn.add("respuesta", respuesta).add("opnUca", OpnUca).add("jsonEmpresa", jsonEmpresa);
        return jsonReturn.build().toString();
    }
    

      //Datatable
      @RequestMapping(value = "/table", method = RequestMethod.POST)
      private @ResponseBody String table(
              @RequestParam("offset") int offset, 
              @RequestParam("limit") int limit,
              @RequestParam("_csrf") String _csrf,
              @RequestParam(value = "search", required = false, defaultValue = "") String search,
              @RequestParam(value = "txtBootstrapTableDesde", required = false, defaultValue = "") String txtBootstrapTableDesde,
              @RequestParam(value = "txtBootstrapTableHasta", required = false, defaultValue = "") String txtBootstrapTableHasta
            ) {
          
          DataTable<EmpresaEntity> dtEmpresa = empresaService.dataTable(search, offset, limit,
                  txtBootstrapTableDesde, txtBootstrapTableHasta);
  
          JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
          jsonReturn.add("total", dtEmpresa.getTotal());
          JsonArrayBuilder jsRows = Json.createArrayBuilder();
          dtEmpresa.getRows().forEach((itemDetalle) -> {
              jsRows.add(Json.createObjectBuilder()
                  .add("idEmpresa", itemDetalle.getIdEmpresa())
                  .add("creacion_fecha", itemDetalle.getCreacionFechaNatural())
                  .add("clave", itemDetalle.getClave())
                  .add("direccion", itemDetalle.getDireccion())
                  .add("dominio", itemDetalle.getDominio())
                  .add("empresa", itemDetalle.getEmpresa())
                  .add("logotipo", itemDetalle.getLogotipo())
                  .add("razonSocial", itemDetalle.getRazonSocial())
                  );
          });
          jsonReturn.add("rows", jsRows);
          return jsonReturn.build().toString();
      }
  


    @RequestMapping(value = "storeEmpresa", method = RequestMethod.POST)
    public @ResponseBody String storeEmpresa(
            @RequestParam(value = "txtNombreEmpresa", required = true) String txtNombreEmpresa,
            @RequestParam(value = "txtClaveEmpresa", required = false) String txtClaveEmpresa,
            @RequestParam(value = "txtDireccionEmpresa") String txtDireccionEmpresa,
            @RequestParam(value = "txtDominioEmpresa") String txtDominioEmpresa,
            @RequestParam(value = "fileLogotipo", required = false) MultipartFile fileLogotipo) throws IOException {
        Boolean respuesta = false;
        String titulo = "Oops!";
        String mensaje = "Ocurrió un error al registrar la nueva empresa.";
        
        EmpresaEntity objEmpresa=new EmpresaEntity();
        try {
            objEmpresa.setEmpresa(txtNombreEmpresa);
            objEmpresa.setClave(txtClaveEmpresa);
            objEmpresa.setDireccion(txtDireccionEmpresa);
            objEmpresa.setDominio(txtDominioEmpresa);
            empresaService.create(objEmpresa, fileLogotipo);
            
            respuesta = true;
            titulo = "Excelente!";
            mensaje = "Nuevo empresa registrada exitosamente.";
            
        } catch(ApplicationException exception) {
            throw new ApplicationException(EnumException.EMPRESA_CREATE_001);
        }
        
        JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
        jsonReturn	.add("respuesta", respuesta)
                    .add("titulo", titulo)
                    .add("mensaje", mensaje);

        return jsonReturn.build().toString();
    }
    
    @RequestMapping(value={"{idEmpresa}/eliminar","{idEmpresa}/eliminar/"}, method = RequestMethod.POST)
    public @ResponseBody String deleteEmpresa(@PathVariable(name = "idEmpresa")int idEmpresa){
        Boolean respuesta=false;
        String titulo = "Oops!";
        String mensaje = "Ocurrio un error al intentar eliminar la empresa";
        EmpresaEntity objEmpresa= empresaService.findByIdEmpresa(idEmpresa);
        try {
            if(objEmpresa != null){
                objEmpresa.setEliminado(true);
                empresaService.update(objEmpresa);
                respuesta=true;
                titulo="Eliminado!";
                mensaje="La empresa ha sido eliminado exitosamente.";
            }else{
                throw new ApplicationException(EnumException.EMPRESA_DELETE_001);
            }
        } catch (Exception e) {
            throw new ApplicationException(EnumException.EMPRESA_DELETE_001);
        }
        JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
		jsonReturn	.add("respuesta", respuesta)
					.add("titulo", titulo)
					.add("mensaje", mensaje);							
		return jsonReturn.build().toString();
    }   

    /** Obtener la data de la empresa para editar */
    @RequestMapping(value = "get-empresa/{idEmpresa}", method = RequestMethod.GET)
    public @ResponseBody String editEmpresa(@PathVariable("idEmpresa") int idEmpresa) {
        Boolean respuesta = false;
        JsonObject jsonEmpresa = null;
        try {
            jsonEmpresa =  empresaService.jsonEmpresasById(idEmpresa);
            respuesta = true;
        } catch (Exception e) {
            throw e;
        }
        JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
        jsonReturn.add("respuesta", respuesta).add("jsonEmpresa", jsonEmpresa);
        return jsonReturn.build().toString();
    }
    

    @RequestMapping(value ={"{idEmpresa}/updateEmpresa","{idEmpresa}/updateEmpresa/"},  method = RequestMethod.POST)
    public @ResponseBody String updateEmpresa(
            @PathVariable(value="idEmpresa") int idEmpresa,
            @RequestParam(value = "txtNombreEmpresa", required = true) String txtNombreEmpresa,
            @RequestParam(value = "txtClaveEmpresa", required = false) String txtClaveEmpresa,
            @RequestParam(value = "txtDireccionEmpresa") String txtDireccionEmpresa,
            @RequestParam(value = "txtDominioEmpresa") String txtDominioEmpresa,
            @RequestParam(value = "fileLogotipo", required = false) MultipartFile fileLogotipo) throws IOException {
        Boolean respuesta = false;
        String titulo = "Oops!";
        String mensaje = "Ocurrió un error al registrar la nueva empresa.";
        
        EmpresaEntity objEmpresa = empresaService.findByIdEmpresa(idEmpresa);
        try {
            objEmpresa.setEmpresa(txtNombreEmpresa);
            objEmpresa.setClave(txtClaveEmpresa);
            objEmpresa.setDireccion(txtDireccionEmpresa);
            objEmpresa.setDominio(txtDominioEmpresa);
            empresaService.create(objEmpresa, fileLogotipo);
            
            respuesta = true;
            titulo = "Excelente!";
            mensaje = "Nuevo empresa registrada exitosamente.";
            
        } catch(ApplicationException exception) {
            throw new ApplicationException(EnumException.EMPRESA_CREATE_001);
        }
        
        JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
        jsonReturn	.add("respuesta", respuesta)
                    .add("titulo", titulo)
                    .add("mensaje", mensaje);

        return jsonReturn.build().toString();
    }

}