package com.ibmexico.components;

import javax.mail.internet.MimeMessage;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;

import com.ibmexico.libraries.Templates;

@Component
public class MailerComponent {

	private final String strFrom = "no-reply@ib-mexico.com";

	@Autowired
	private JavaMailSender objJavaMailSender;

	@Autowired
	private SpringTemplateEngine templateEngine;

	public void send(String para, String asunto, String template) throws MessagingException {
		send(para, asunto, template, null, null);
	}

	public void send(String para, String asunto, String template, Map<String, Object> mapVariable)
			throws MessagingException {
		send(para, asunto, template, mapVariable, null);
	}

	public void send(String para, String asunto, String template, Map<String, Object> mapVariable,
			List<ClassPathResource> lstFiles) throws MessagingException {

		MimeMessage objMimeMessager = objJavaMailSender.createMimeMessage();
		MimeMessageHelper objMimeMessageHelper = new MimeMessageHelper(objMimeMessager,
				MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());

		ClassPathResource objPath = new ClassPathResource("static/assets/images/logotipo_250.png");
		objMimeMessageHelper.addAttachment("logotipo_250.png", objPath);

		Context objContext = new Context();
		Templates objTemplates = new Templates();

		objContext.setVariable("_TEMPLATE_", template);

		if (mapVariable != null) {
			mapVariable.forEach((k, v) -> {
				objContext.setVariable(k, v);
			});
		}

		objMimeMessageHelper.setTo(para);
		objMimeMessageHelper.setSubject(asunto);
		objMimeMessageHelper.setFrom(strFrom);

		if (lstFiles != null) {
			for (ClassPathResource itemFile : lstFiles) {
				objMimeMessageHelper.addAttachment(itemFile.getFilename(), itemFile);
			}
		}

		objMimeMessageHelper.setText(templateEngine.process(objTemplates.FOUNDATION_MAIL, objContext), true);
		objJavaMailSender.send(objMimeMessager);
	}

	public void sendPoliza(String para, String asunto, ByteArrayResource bytePDF, String folio) throws MessagingException, IOException {

		final InputStreamSource attachment = bytePDF;
		MimeMessage objMimeMessager = objJavaMailSender.createMimeMessage();
		MimeMessageHelper objMimeMessageHelper = new MimeMessageHelper(objMimeMessager, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,  StandardCharsets.UTF_8.name());
	
		ClassPathResource objPath = new ClassPathResource("static/assets/images/logotipo_250.png");
		objMimeMessageHelper.addAttachment("logotipo_250.png", objPath);
		//InputStream stream = new ByteArrayInputStream(objPDF);
		// ByteArrayDataSource attachment=new ByteArrayDataSource(objPDF.getInputStream(), "application/pdf");
		//stream.close();
		objMimeMessageHelper.addAttachment("Poliza_"+folio+".pdf", attachment);
        objMimeMessageHelper.setTo(para);
        objMimeMessageHelper.setSubject(asunto);
		objMimeMessageHelper.setFrom(strFrom);
		objMimeMessageHelper.setText("Se le adjunta la póliza de servicío con Folio : "+ folio);

        objJavaMailSender.send(objMimeMessager);
	}

	public void sendNotificacionCotizacion(String para, String asunto, ByteArrayResource bytePDF, 
	String folio, String template, Map<String, Object> mapVariable) throws MessagingException, IOException {

		final InputStreamSource attachment = bytePDF;
		MimeMessage objMimeMessager = objJavaMailSender.createMimeMessage();

		MimeMessageHelper objMimeMessageHelper = new MimeMessageHelper(objMimeMessager,
		MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());

		ClassPathResource objPath = new ClassPathResource("static/assets/images/logotipo_250.png");
		objMimeMessageHelper.addAttachment("logotipo_250.png", objPath);

		Context objContext = new Context();
		Templates objTemplates = new Templates();

		objContext.setVariable("_TEMPLATE_", template);

		if (mapVariable != null) {
			mapVariable.forEach((k, v) -> {
				objContext.setVariable(k, v);
			});
		}

		if (bytePDF.exists() && bytePDF != null) {
			objMimeMessageHelper.addAttachment("Cotización "+folio+".pdf", attachment);
		}
		objMimeMessageHelper.setTo(para);
		objMimeMessageHelper.setSubject(asunto);
		objMimeMessageHelper.setFrom(strFrom);

		objMimeMessageHelper.setText(templateEngine.process(objTemplates.FOUNDATION_MAIL, objContext), true);
		objJavaMailSender.send(objMimeMessager);
	}

	public void sendNotificacionOportunidadNegocio(String para, String asunto, ByteArrayResource bytePDFOportunidad, String oportunidad,
			String templateNotificacionOportunidad, Map<String, Object> mapVariableOportunidad) throws MessagingException , IOException {

				final InputStreamSource attachment = bytePDFOportunidad;
				MimeMessage objMimeMessager = objJavaMailSender.createMimeMessage();
		
				MimeMessageHelper objMimeMessageHelper = new MimeMessageHelper(objMimeMessager,
				MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
		
				ClassPathResource objPath = new ClassPathResource("static/assets/images/logotipo_250.png");
				objMimeMessageHelper.addAttachment("logotipo_250.png", objPath);
		
				Context objContext = new Context();
				Templates objTemplates = new Templates();
		
				objContext.setVariable("_TEMPLATE_", templateNotificacionOportunidad);
		
				if (mapVariableOportunidad != null) {
					mapVariableOportunidad.forEach((k, v) -> {
						objContext.setVariable(k, v);
					});
				}
		
				if (bytePDFOportunidad.exists()) {
					objMimeMessageHelper.addAttachment(" Oportunidad  "+oportunidad+".pdf", attachment);
				}
				objMimeMessageHelper.setTo(para);
				objMimeMessageHelper.setSubject(asunto);
				objMimeMessageHelper.setFrom(strFrom);
		
				objMimeMessageHelper.setText(templateEngine.process(objTemplates.FOUNDATION_MAIL, objContext), true);
				objJavaMailSender.send(objMimeMessager);
	}
}
