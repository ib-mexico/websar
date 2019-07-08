package com.ibmexico.libraries;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Divisas {
	
	final String dominio = "https://kio.banamex.com/framework/query" ;

	public String obtenerDivisas() throws MalformedURLException {
		
		String retorno = "";
		
		final String POST_PARAMS = "{\n" + "\"site\": \"5\",\r\n" +
		        "    \"entity\": \"divisas_usd\",\r\n" +
		        "    \"operation\": \"getAll\",\r\n" +
		        "    \"orderBy\": \"id,DESC\",\r\n" +
		        "    \"page_size\": \"1\"" + "\n}";
		
		try {
			URL url = new URL(dominio);
			HttpURLConnection postConnection = (HttpURLConnection) url.openConnection();
			postConnection.setRequestMethod("POST");
			postConnection.setRequestProperty("Referer", "https://www.banamex.com/assets/js/w-finanzas.js");
			postConnection.setRequestProperty("Content-Type", "text/plain");
			
			postConnection.setDoOutput(true);
		    OutputStream os = postConnection.getOutputStream();
		    os.write(POST_PARAMS.getBytes());
		    os.flush();
		    os.close();
		    
		    int responseCode = postConnection.getResponseCode();
		    System.out.println("POST Response Code :  " + responseCode);
		    System.out.println("POST Response Message : " + postConnection.getResponseMessage());
		    
		    if (responseCode == HttpURLConnection.HTTP_CREATED) { //success
		        BufferedReader in = new BufferedReader(new InputStreamReader(
		            postConnection.getInputStream()));
		        String inputLine;
		        StringBuffer response = new StringBuffer();
		        while ((inputLine = in .readLine()) != null) {
		            response.append(inputLine);
		        } in .close();
		        // print result
		        System.out.println(response.toString());
		    } else {
		        System.out.println("POST NOT WORKED");
		    }
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return retorno;
	}
}
