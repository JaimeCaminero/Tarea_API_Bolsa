package App;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Main {
	// HABLAR de LocalDate
	public void obtenerNoticias(String simbolo) {
		LocalDate fechaActual = LocalDate.now();
		LocalDate semanaPasada = fechaActual.minusDays(7);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String inicioComunApi = "https://finnhub.io/api/v1";
		String token = "&token=d0hf44pr01qv1u373vhgd0hf44pr01qv1u373vi0";
		String parteVariable = "/company-news?symbol=" + simbolo + "&from=" + semanaPasada.format(formatter) + "&to="
				+ fechaActual.format(formatter);
		String httpTotal = inicioComunApi + parteVariable + token;
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(httpTotal)).build();
		ObjectMapper om = new ObjectMapper();
		try {
			HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
			CompanyNews apple[] = om.readValue(response.body(), CompanyNews[].class);
			for (int i = 0; i < 3; i++) {
				System.out.println(apple[i].getHeadline());
				System.out.println(apple[i].getSummary());
				System.out.println("Fuente: " + apple[i].getSource());
				System.out.println(" ");
			}

		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		Main main =  new Main();
		main.obtenerNoticias("AAPL");
	}

}
