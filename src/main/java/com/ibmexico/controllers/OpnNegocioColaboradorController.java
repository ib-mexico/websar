package com.ibmexico.controllers;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import com.ibmexico.entities.OpnNegocioColaboradorEntity;
import com.ibmexico.libraries.notifications.ApplicationException;
import com.ibmexico.libraries.notifications.EnumException;
import com.ibmexico.services.OpnNegocioColaboradorService;
import com.ibmexico.services.UsuarioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("controlPanel/opnNegocioColaborador")
public class OpnNegocioColaboradorController{
    
    @Autowired
    @Qualifier("opnNegocioColaboradorService")
    private OpnNegocioColaboradorService opnNegocioColaboradorService;

    @Autowired
    @Qualifier("usuarioService")
    private UsuarioService usuarioService;
    
    // Obtener los datos necesarios para el select
    @RequestMapping(value = "get-users", method = RequestMethod.GET)
    public @ResponseBody String getUser() {
        Boolean respuesta = false;
        JsonObject jsonUsuario = null;
       try {
           jsonUsuario = usuarioService.jsonUsuariosActivos();
   
        } catch (Exception exception) {
               exception.getMessage();
           }
           JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
           jsonReturn.add("respuesta", respuesta).add("jsonUsuario", jsonUsuario);
           return jsonReturn.build().toString();
    }

    @RequestMapping(value = {"get-Colaboradores/{idOPN}","get-Colaboradores/{idOPN}/"}, method = RequestMethod.GET)
    public @ResponseBody String getColaboradores(
        @PathVariable("idOPN") int idOPN){
        Boolean respuesta = false;
        JsonObject jsonColaboradores = null;

        try {
            jsonColaboradores = opnNegocioColaboradorService.jsonColaboradores(idOPN);
            respuesta = true;
        } catch (Exception e) {
            e.getMessage();
        }
        JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
        jsonReturn.add("respuesta", respuesta).add("jsonColaboradores", jsonColaboradores);
        return jsonReturn.build().toString();
    }

    @RequestMapping(value={"{idOpnColaborador}/eliminar","{idOpnColaborador}/eliminar/"}, method=RequestMethod.POST)
    public @ResponseBody String deleteColaborador(@PathVariable(name="idOpnColaborador")int idOpnColaborador){
        Boolean respuesta=false;
        String titulo="Oops!";
        String mensaje="Ocurrio un error al intentar eliminar el colaborador";

        OpnNegocioColaboradorEntity objOpnColaborador = opnNegocioColaboradorService.findByIdOpnNegocio(idOpnColaborador);
        try {
            if(objOpnColaborador!=null){
                opnNegocioColaboradorService.deleteColaborador(objOpnColaborador);
                respuesta=true;
                titulo="Eliminado!";
                mensaje="El colaborador ha sido eliminado exitosamente.";
            }else{
                throw new ApplicationException(EnumException.OPN_COLABORADOR_DELETE_001);
            }
        } catch (Exception e) {
            throw new ApplicationException(EnumException.OPN_COLABORADOR_DELETE_001);
        }
        JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
		jsonReturn	.add("respuesta", respuesta)
					.add("titulo", titulo)
					.add("mensaje", mensaje);							
		return jsonReturn.build().toString();
    }

}