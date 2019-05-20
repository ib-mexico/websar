package com.ibmexico.components;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.ibmexico.services.ConfiguracionService;
import com.lowagie.text.DocumentException;

@Component("pdfComponent")
public class PdfComponent {	
	
	@Autowired
	@Qualifier("configuracionService")
	ConfiguracionService configuracionService;
	
	@Autowired
    private SpringTemplateEngine templateEngine;
	
	public static final String PATH_STATIC = "target/classes/static/";
	
	public void save(String directoryPath, String filePath, String template, Context objContext) throws IOException, DocumentException {

		String processedHtml = templateEngine.process(template, objContext);
		
		OutputStream outputStream = new FileOutputStream(directoryPath + filePath);
		ITextRenderer renderer = new ITextRenderer();
		renderer.setDocumentFromString(processedHtml);
		renderer.layout();
		renderer.createPDF(outputStream);
		outputStream.close();
		
	}
	
	public ResponseEntity<InputStreamResource> generate(String filePath, String template, Context objContext) throws IOException, DocumentException {

		String processedHtml = templateEngine.process(template, objContext);
		
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		ITextRenderer renderer = new ITextRenderer();
		//SharedContext sharedContext = renderer.getSharedContext();
		//sharedContext.setReplacedElementFactory(new B64ImgReplacedElementFactory());
		renderer.setDocumentFromString(processedHtml);
		renderer.layout();
		renderer.createPDF(outputStream);
		outputStream.flush();
		outputStream.close();
		
		ByteArrayInputStream bis = new ByteArrayInputStream(outputStream.toByteArray());
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "inline; filename="+filePath);
		
		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(new InputStreamResource(bis));
	}

}
