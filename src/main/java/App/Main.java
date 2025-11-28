package App;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.StandardCharsets;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Main {

	public static void main(String[] args) {
		String nombreEmpresa = "Cipher Mining INC";
		//Creamos la variable token que nos permitirá usarlo en todas nuestras llamadas a las APIs
		final String token = "d0hf44pr01qv1u373vhgd0hf44pr01qv1u373vi0";
		//Necesitamos evitar los espacios y codificarlos de forma que la petición de la API lo reconozca
		//HABLAR DE LA CLASE URLENCONDER
		String encoded = URLEncoder.encode(nombreEmpresa, StandardCharsets.UTF_8);


		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create("https://finnhub.io/api/v1/search?q=" + encoded + "&exchange=US&token=" + token))
				.build();

		try {
			HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
			System.out.println(response.statusCode() + ":\n" + response.body());
			ObjectMapper om = new ObjectMapper();
			try {
				Root_SymbolLookup r = om.readValue(response.body(), Root_SymbolLookup.class);
				System.out.println("Resultados obtenidos: " + r.getCount());
				for (Symbol s : r.getResult()) {
					System.out.println("    Nombre: " + s.getDescription());
					System.out.println("    Ticker: " + s.getSymbol());
					System.out.println("    Tipo de acción: " + s.getType() + "\n");
					
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
