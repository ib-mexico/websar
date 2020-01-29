package com.ibmexico.controllers;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.mail.MessagingException;

import com.ibmexico.components.MailerComponent;
import com.ibmexico.components.ModelAndViewComponent;
import com.ibmexico.components.PdfComponent;
import com.ibmexico.entities.ClienteEntity;
import com.ibmexico.entities.OrdenServicioEntity;
import com.ibmexico.entities.OrdenServicioPartidaEntity;
import com.ibmexico.entities.UsuarioEntity;
import com.ibmexico.libraries.DataTable;
import com.ibmexico.libraries.Formats;
import com.ibmexico.libraries.Templates;
import com.ibmexico.libraries.notifications.ApplicationException;
import com.ibmexico.libraries.notifications.EnumException;
import com.ibmexico.libraries.notifications.EnumMessage;
import com.ibmexico.services.ClienteContactoService;
import com.ibmexico.services.ClienteService;
import com.ibmexico.services.OrdenServicioPartidaService;
import com.ibmexico.services.OrdenServicioService;
import com.ibmexico.services.SessionService;
import com.ibmexico.services.UsuarioService;
import com.lowagie.text.DocumentException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import org.thymeleaf.context.Context;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequestMapping("controlPanel/ordenesServicios")
public class OrdenesServiciosController {

        @Autowired
        @Qualifier("modelAndViewComponent")
        private ModelAndViewComponent modelAndViewComponent;

        @Autowired
        @Qualifier("pdfComponent")
        private PdfComponent pdfComponent;

        @Autowired
        @Qualifier("ordenServicioService")
        private OrdenServicioService ordenServicioService;

        @Autowired
        @Qualifier("ordenServicioPartidaService")
        private OrdenServicioPartidaService ordenServicioPartidaService;

        @Autowired
        @Qualifier("clienteService")
        private ClienteService clienteService;

        @Autowired
        @Qualifier("clienteContactoService")
        private ClienteContactoService clienteContactoService;

        @Autowired
        @Qualifier("usuarioService")
        private UsuarioService usuarioService;        

        @Autowired
        @Qualifier("sessionService")
        private SessionService sessionService;

        @Autowired
        private MailerComponent mailerComponent;

        @RequestMapping(value = { "", "/" }, method = RequestMethod.GET)
        public ModelAndView index() {
                ModelAndView objModelAndView = modelAndViewComponent.createModelAndViewControlPanel(Templates.CONTROL_PANEL_ORDENES_SERVICIOS_INDEX);
                objModelAndView.addObject("rolNuevaOrdenServicio", sessionService.hasRol("NUEVA_ORDEN_SERVICIO"));
                return objModelAndView;
        }

        @RequestMapping(value = "/table", method = RequestMethod.POST)
        public @ResponseBody String table(@RequestParam("offset") int offset, @RequestParam("limit") int limit,
                        @RequestParam("_csrf") String _csrf,
                        @RequestParam(value = "search", required = false, defaultValue = "") String search,
                        @RequestParam(value = "txtBootstrapTableDesde", required = false) String txtBootstrapTableDesde,
                        @RequestParam(value = "txtBootstrapTableHasta", required = false) String txtBootstrapTableHasta) {
                DataTable<OrdenServicioEntity> dtOrdenes = ordenServicioService.dataTableOrdenes(search, offset, limit,
                                txtBootstrapTableDesde, txtBootstrapTableHasta);
                JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
                jsonReturn.add("total", dtOrdenes.getTotal());
                JsonArrayBuilder jsonRows = Json.createArrayBuilder();
                dtOrdenes.getRows().forEach((itemOrden) -> {
                        jsonRows.add(Json.createObjectBuilder().add("idOrdenServicio", itemOrden.getIdOrdenServicio())
                        .add("folio", itemOrden.getFolio())
                        .add("cliente", itemOrden.getCliente().getCliente())
                        .add("contacto", itemOrden.getClienteContacto().getContacto())

                        .add("subtotal", itemOrden.getSubtotalNatural())
                        .add("nombreElabora", itemOrden.getUsuarioElabora().getNombreCompleto())
                        .add("nombreRecibe", itemOrden.getNombreRecibe())

                        .add("firmaRevisa", itemOrden.getUrlFirmaRevisa())
                        .add("fechaInicio", itemOrden.getInicioFechaFullNatural())
                        .add("fechaEntrega", itemOrden.getEntregaFechaFullNatural()));
                });
                jsonReturn.add("rows", jsonRows);
                return jsonReturn.build().toString();
        }

        @GetMapping({ "/create", "/create/" })
        public ModelAndView create() {
                List<ClienteEntity> lstClientes = clienteService.listClientesActivos();
                List<UsuarioEntity> lstUsuarios = usuarioService.listUsuariosActivos();
                ModelAndView objModelAndView = modelAndViewComponent
                                .createModelAndViewControlPanel(Templates.CONTROL_PANEL_ORDENES_SERVICIOS_CREATE);
                objModelAndView.addObject("lstClientes", lstClientes);
                objModelAndView.addObject("lstUsuarios", lstUsuarios);
                return objModelAndView;
        }

        @RequestMapping(value = "storeOrdenServicio", method = RequestMethod.POST)
        public RedirectView store(
                        @RequestParam("cmbCliente") int cmbCliente,
                        @RequestParam("cmbClienteContacto") int cmbClienteContacto,
                        @RequestParam(value="cmbUsuarioSupervisa", required =false) int cmbUsuarioSupervisa,
                        @RequestParam("txtFechaInicio") String txtFechaInicio,
                        @RequestParam("txtFechaEntrega") String txtFechaEntrega,
                        @RequestParam(value = "txtTiempoEspera", required = false, defaultValue = "") String txtTiempoEspera,
                        @RequestParam(value = "txtOSTicket", required = false, defaultValue = "") String txtOSTicket,
                        @RequestParam(value = "txtAlcanceServicios") String txtAlcanceServicios,
                        @RequestParam(value = "txtBitacoraActividades") String txtBitacoraActividades,
                        @RequestParam(value = "txtCantidad[]") String[] txtCantidad,
                        @RequestParam(value = "txtDescripcion[]") String[] txtDescripcion,
                        @RequestParam(value = "txtNumeroParte[]") String[] txtNumeroParte,
                        @RequestParam(value = "txtPrecioUnitario[]") String[] txtPrecioUnitario,
                        @RequestParam(value = "txtImporte[]") String[] txtImporte,
                        @RequestParam(value = "imgFirmaRecepcion") String imgFirmaRecepcion,
                        @RequestParam(value = "imgFirmaEntrega") String imgFirmaEntrega,
                        @RequestParam(value = "txtNombreRecibe") String txtNombreRecibe,
                        @RequestParam(value = "txtPuestoRecibe") String txtPuestoRecibe,
                        @RequestParam(value = "txtTelefonoRecibe") String txtTelefonoRecibe,
                        @RequestParam(value = "txtCorreoRecibe") String txtCorreoRecibe,
                        RedirectAttributes objRedirectAttributes) {
                RedirectView objRedirectView = null;
                try {
                        OrdenServicioEntity objOrdenServicio = new OrdenServicioEntity();

                        objOrdenServicio.setCliente(clienteService.findByIdCliente(cmbCliente));
                        if(cmbUsuarioSupervisa > 0){
                            objOrdenServicio.setUsuarioRevisa(usuarioService.findByIdUsuario(cmbUsuarioSupervisa));
                        }
                        objOrdenServicio.setClienteContacto(
                                        clienteContactoService.findByIdClienteContacto(cmbClienteContacto));
                        objOrdenServicio.setInicioFecha(Formats.getInstance().toLocalDateTime(txtFechaInicio));
                        objOrdenServicio.setEntregaFecha(Formats.getInstance().toLocalDateTime(txtFechaEntrega));
                        objOrdenServicio.setTiempoEspera(txtTiempoEspera);
                        objOrdenServicio.setOsTicket(txtOSTicket);
                        objOrdenServicio.setAlcanceServicios(txtAlcanceServicios);
                        objOrdenServicio.setBitacoraActividades(txtBitacoraActividades);
                        objOrdenServicio.setUsuarioElabora(sessionService.getCurrentUser());
                        objOrdenServicio.setNombreRecibe(txtNombreRecibe);
                        objOrdenServicio.setPuestoRecibe(txtPuestoRecibe);
                        objOrdenServicio.setCorreoRecibe(txtCorreoRecibe);
                        objOrdenServicio.setTelefonoRecibe(txtTelefonoRecibe);
                        objOrdenServicio.setSubtotal(new BigDecimal(0));
                        objOrdenServicio.setUrlFirmaRevisa(" ");
                        ordenServicioService.create(objOrdenServicio, txtCantidad, txtDescripcion, txtNumeroParte,
                                        txtPrecioUnitario, txtImporte, imgFirmaEntrega, imgFirmaRecepcion);
                        objRedirectView = new RedirectView("/WebSar/controlPanel/ordenesServicios");
                        modelAndViewComponent.addResult(objRedirectAttributes,
                                        EnumMessage.COTIZACIONES_ORDENES_SERVICIOS_CREATE_001);
                } catch (ApplicationException exception) {
                        objRedirectView = new RedirectView("/WebSar/controlPanel/ordenesServicios/create");
                        modelAndViewComponent.addResult(objRedirectAttributes, exception);
                }
                return objRedirectView;
        }

        @GetMapping({ "{paramIdOrdenServicio}/revision", "{paramIdOrdenServicio}/revision/" })
        public ModelAndView revision(@PathVariable("paramIdOrdenServicio") int paramIdOrdenServicio) {

                OrdenServicioEntity objOrdenServicio = ordenServicioService.findByIdOrdenServicio(paramIdOrdenServicio);
                List<OrdenServicioPartidaEntity> lstPartidas = ordenServicioPartidaService
                                .listOrdenServicioPartidas(paramIdOrdenServicio);

                ModelAndView objModelAndView = modelAndViewComponent
                                .createModelAndViewControlPanel(Templates.CONTROL_PANEL_ORDENES_SERVICIOS_REVIEW);
                objModelAndView.addObject("objOrdenServicio", objOrdenServicio);
                objModelAndView.addObject("lstPartidas", lstPartidas);

                return objModelAndView;
        }

        @RequestMapping(value = "{paramIdOrdenServicio}/revision", method = RequestMethod.PUT)
        public RedirectView store(@PathVariable("paramIdOrdenServicio") int paramIdOrdenServicio,
                        @RequestParam("hddIdOrdenServicio") int hddIdOrdenServicio,
                        @RequestParam(value = "imgFirmaRevision") String imgFirmaRevision,
                        RedirectAttributes objRedirectAttributes) {
                RedirectView objRedirectView = null;
                OrdenServicioEntity objOrdenServicio = ordenServicioService.findByIdOrdenServicio(hddIdOrdenServicio);
                try {

                        if (objOrdenServicio != null) {
                                if (objOrdenServicio.getUsuarioRevisa() != null) {
                                        objOrdenServicio.setUsuarioRevisa(objOrdenServicio.getUsuarioRevisa());
                                }else{
                                        objOrdenServicio.setUsuarioRevisa(sessionService.getCurrentUser());
                                }
                                ordenServicioService.update(objOrdenServicio, imgFirmaRevision);
                                objRedirectView = new RedirectView("/WebSar/controlPanel/ordenesServicios");
                                modelAndViewComponent.addResult(objRedirectAttributes,
                                                EnumMessage.COTIZACIONES_ORDENES_SERVICIOS_REVISION_UPDATE_001);

                        } else {
                                throw new ApplicationException(EnumException.COTIZACIONES_ORDENES_SERVICIOS_UPDATE_001);
                        }

                } catch (ApplicationException exception) {
                        objRedirectView = new RedirectView("/WebSar/controlPanel/ordenesServicios/create");
                        modelAndViewComponent.addResult(objRedirectAttributes, exception);
                }

                return objRedirectView;
        }

        @RequestMapping(value = { "{paramIdOrdenServicio}/pdf",
                        "/{paramIdOrdenServicio}/pdf/" }, method = RequestMethod.GET, produces = MediaType.APPLICATION_PDF_VALUE)
        public ResponseEntity<InputStreamResource> previewPdf(
                        @PathVariable("paramIdOrdenServicio") int paramIdOrdenServicio)
                        throws IOException, DocumentException, MessagingException {

                OrdenServicioEntity objOrdenServicio = ordenServicioService.findByIdOrdenServicio(paramIdOrdenServicio);
                List<OrdenServicioPartidaEntity> lstPartidas = ordenServicioPartidaService
                                .listOrdenServicioPartidas(paramIdOrdenServicio);
                UsuarioEntity objUser = sessionService.getCurrentUser();

                LocalDateTime ldtNow = LocalDateTime.now();
                Templates objTemplates = new Templates();

                String path_file = ldtNow.getYear() + "_" + ldtNow.getMonthValue() + "_" + ldtNow.getDayOfMonth() + "_"
                                + objOrdenServicio.getFolio() + ".pdf";

                Context objContext = new Context();
                objContext.setVariable("_TEMPLATE_", Templates.PDF_ORDEN_SERVICIO);
                objContext.setVariable("title", "Orden de Servicio: " + objOrdenServicio.getFolio());
                /**
                 * El objCotizacion se reutilizo para evitar modificar el reporte de
                 * ordenesServicio en vez de eso se hace uso del objUser para acceder a las
                 * propiedades de la empresa.
                 */
                objContext.setVariable("objCotizacion", objUser);
                objContext.setVariable("objOrdenServicio", objOrdenServicio);
                objContext.setVariable("lstPartidas", lstPartidas);
                // ByteArrayResource objPDF = pdfComponent.generateFile(path_file, objTemplates.FOUNDATION_PDF, objContext);
                // mailerComponent.sendPoliza("neyser36@gmail.com", "Poliza",objPDF);
                return pdfComponent.generate(path_file, objTemplates.FOUNDATION_PDF, objContext);
        }
        
        @RequestMapping(value={"{paramIdOrdenServicio}/envioPoliza","{paramIdOrdenServicio}/envioPoliza/"}, method=RequestMethod.POST)
        public @ResponseBody String envioPoliza(@PathVariable(name="paramIdOrdenServicio")int paramIdOrdenServicio)throws DocumentException, IOException, MessagingException {
                Boolean respuesta = false;
		String titulo = "Oops!";
                String mensaje = "Ocurrió un error al enviar a validación.";
                
                OrdenServicioEntity objOrdenServicio = ordenServicioService.findByIdOrdenServicio(paramIdOrdenServicio);
                
                List<OrdenServicioPartidaEntity> lstPartidas = ordenServicioPartidaService
                                .listOrdenServicioPartidas(paramIdOrdenServicio);
                UsuarioEntity objUser = sessionService.getCurrentUser();

                LocalDateTime ldtNow = LocalDateTime.now();
                Templates objTemplates = new Templates();

                String path_file = ldtNow.getYear() + "_" + ldtNow.getMonthValue() + "_" + ldtNow.getDayOfMonth() + "_"
                                + objOrdenServicio.getFolio() + ".pdf";

                Context objContext = new Context();
                objContext.setVariable("_TEMPLATE_", Templates.PDF_ORDEN_SERVICIO);
                objContext.setVariable("title", "Orden de Servicio: " + objOrdenServicio.getFolio());
                /**El objCotizacion se reutilizo para evitar modificar el reporte de ordenesServicio en vez de eso se 
                 * hace uso del objUser para acceder a las propiedades de la empresa.
                 */
                objContext.setVariable("objCotizacion", objUser);
                objContext.setVariable("objOrdenServicio", objOrdenServicio);
                objContext.setVariable("lstPartidas", lstPartidas);
                ByteArrayResource objPDF = pdfComponent.generateFile(path_file, objTemplates.FOUNDATION_PDF, objContext);
                try {
                        String folio=objOrdenServicio.getFolio();
                        mailerComponent.sendPoliza(objOrdenServicio.getCorreoRecibe(), "Poliza de Servicio con Folio : "+objOrdenServicio.getFolio(),objPDF,folio );
                        respuesta = true;
			titulo = "Enviado!";
			mensaje = "El Correo se ha enviado exitosamente.";
                } catch (Exception e) {
                        throw new ApplicationException(EnumException.CORREO_ENVIO_001);
                }
		JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
		jsonReturn.add("respuesta", respuesta)
			.add("titulo", titulo)
			.add("mensaje", mensaje);		
		return jsonReturn.build().toString();
                
        }

}