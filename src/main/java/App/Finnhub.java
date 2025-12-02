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
	/*
	 * public void obtenerNoticias(String simbolo) {
		LocalDate semanaPasada = fechaActual.minusDays(7);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String inicioComunApi = "https://finnhub.io/api/v1";
		String token = "&token=d0hf44pr01qv1u373vhgd0hf44pr01qv1u373vi0";
		String parteVariable = "/company-news?symbol=" + simbolo + "&from=" + semanaPasada.format(formatter) + "&to="
				+ fechaActual.format(formatter);
		String httpTotal = inicioComunApi + parteVariable + token;
		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(httpTotal)).build();
		HttpClient client = HttpClient.newHttpClient();
		ObjectMapper om = new ObjectMapper();
		try {
			HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
			CompanyNews apple[] = om.readValue(response.body(), CompanyNews[].class);
			for (int i = 0; i < 3; i++) {
				System.out.println("Noticia " + (i + 1));
				System.out.println("-------------------");
				System.out.println(apple[i].getHeadline());
				System.out.println(apple[i].getSummary());
				System.out.println("Fuente: " + apple[i].getSource());
				System.out.println(" ");
			}

		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}

	}

	public void mostrarInfoBase(String simbolo) {
		String inicioComunApi = "https://finnhub.io/api/v1";
		String token = "&token=d0hf44pr01qv1u373vhgd0hf44pr01qv1u373vi0";
		String parteVariable = "/quote?symbol=" + simbolo;
		String httpTotal = inicioComunApi + parteVariable + token;
		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(httpTotal)).build();
		HttpClient client = HttpClient.newHttpClient();
		ObjectMapper om = new ObjectMapper();
		try {
			HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
			Quote quote = om.readValue(response.body(), Quote.class);
			System.out.println("El Nasdaq 100 (" + simbolo + ") ha abierto en " + quote.getOpenPriceOfTheDay() + "$");
			System.out.println("Actualmente tiene un precio de " + quote.getCurrentPrice() + "$");
			System.out.println("Tiene un cambio porcentual de: " + quote.getPercentChange() + "%");

		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}

	}

	public void guardarWatchlist(String ticker, double precioActual) {
		 String precioDosDecimales = String.format("%.2f", precioActual);
		String formato = ticker + " | " + precioDosDecimales + "$ | " + fechaActual;
		try (FileWriter fw = new FileWriter("miWatchlist.txt", true);
				BufferedWriter bw = new BufferedWriter(fw);
				PrintWriter printwriter = new PrintWriter(bw)) {
			printwriter.println(formato);
			System.out.println("Se aha añadido la acción " + ticker + " a la watchlist con el precio y fecha actual");

		} catch (IOException e) {
			e.printStackTrace();
		}

	}*/
}
