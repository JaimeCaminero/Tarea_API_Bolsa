package App;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Finnhub {
	private final String token;
	private final HttpClient cliente = HttpClient.newHttpClient();
	private final ObjectMapper om = new ObjectMapper();

	public Finnhub(String token) {
		this.token = token;
	}



	public Root_SymbolLookup searchCompany(String name) {
		try {
			String encoded = URLEncoder.encode(name, StandardCharsets.UTF_8);
			String url = "https://finnhub.io/api/v1/search?q=" + encoded + "&exchange=US&token=" + token;

			HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();

			HttpResponse<String> response = cliente.send(request, HttpResponse.BodyHandlers.ofString());

			return om.readValue(response.body(), Root_SymbolLookup.class);

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
