package App;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Main {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		System.out.print("Introduce el nombre de la empresa que quieres buscar: ");
		String nombreEmpresa = sc.nextLine(); // Creamos la variable token que nos permitirá usarlo en todas nuestras
												// llamadas a las APIs
		final String token = "d4lg8khr01qr851o6g90d4lg8khr01qr851o6g9g";
		// Necesitamos evitar los espacios y codificarlos de forma que la petición de la
		// API lo reconozca

		String encoded = URLEncoder.encode(nombreEmpresa, StandardCharsets.UTF_8);

		HttpClient client = HttpClient.newHttpClient();
		ObjectMapper om = new ObjectMapper();

		try {
			String urlSymbol = "https://finnhub.io/api/v1/search?q=" + encoded + "&exchange=US&token=" + token;
			HttpRequest requestSymbol = HttpRequest.newBuilder().uri(URI.create(urlSymbol)).build();
			HttpResponse<String> responseSearch = client.send(requestSymbol, BodyHandlers.ofString());

			System.out.println(responseSearch.statusCode() + ":\n" + responseSearch.body());

			Root_SymbolLookup r = om.readValue(responseSearch.body(), Root_SymbolLookup.class);

			System.out.println("Resultados obtenidos: " + r.getCount());

			if (r.getCount() == 0) {
				System.out.println("Por favor, revisa que has introducido el nombre correctamente");
			} else {

				for (Symbol s : r.getResult()) {
					System.out.println("    Nombre: " + s.getDescription());
					System.out.println("    Ticker: " + s.getSymbol());
					System.out.println("    Tipo de acción: " + s.getType());

					String urlQuote = "https://finnhub.io/api/v1/quote?symbol=" + s.getSymbol() + "&token=" + token;
					HttpRequest requestQuote = HttpRequest.newBuilder().uri(URI.create(urlQuote)).build();

					HttpResponse<String> responseQuote = client.send(requestQuote,
							HttpResponse.BodyHandlers.ofString());
					Quote quote = om.readValue(responseQuote.body(), Quote.class);

					System.out.println("    Precio actual: " + quote.getCurrentPrice());
					System.out.println("    % Cambio: " + quote.getPercentChange() + "\n");
				}

				System.out.println("Una vez vistos los resultados, introduce el Ticker de la empresa deseada: ");
				String ticker = sc.nextLine();
				System.out.println("Gracias, has seleccionado el ticker " + ticker + "\n");
				System.out.println("¿Te gustaría conocer los últimos SEC fillings de la empresa?");
				System.out.println("1. Sí");
				System.out.println("2. No");

				int respuestaSEC = sc.nextInt();
				sc.nextLine();

				// Meter try catch con InputMismatch Exception
				if (respuestaSEC == 1) {
					String urlSECFillings = "https://finnhub.io/api/v1/stock/filings?symbol=" + ticker + "&token="
							+ token;
					HttpRequest requestSECFilling = HttpRequest.newBuilder().uri(URI.create(urlSECFillings)).build();

					try {
						HttpResponse<String> responseSECFilling = client.send(requestSECFilling,
								BodyHandlers.ofString());

						System.out.println("SEC Fillings para " + ticker + ":");

						List<SECFillings> fillings = om.readValue(responseSECFilling.body(),
								om.getTypeFactory().constructCollectionType(List.class, SECFillings.class));

						for (SECFillings f : fillings) {
							System.out.println("\n=====================================");
							System.out.println("    Cik: " + f.getCik());
							System.out.println("    Tipo: " + f.getForm());
							System.out.println("    Fecha inscripción: " + f.getFilledDate());
							System.out.println("    Fecha aceptado: " + f.getAcceptedDate());
							System.out.println("    Filing URL: " + f.getFilingUrl());
							System.out.println("=====================================\n");
						}

						System.out.print("Introduce el Cik del filling que quieres ver en la web:");
						String Cik = sc.nextLine();
						String URLCik = null;
						for (SECFillings f : fillings) {
							if (f.getCik().equals(Cik)) {
								URLCik = f.getFilingUrl();
							}
						}

						// Paquete Desktop que permite tomar un string (url) y abrirlo en el navegador
						// mediante desktop.browse
						try {
							Desktop desktop = Desktop.getDesktop();
							desktop.browse(new URI(URLCik));
						} catch (Exception e) {
							e.printStackTrace();
						}

					} catch (Exception e) {
						e.printStackTrace();
					}

				} else {
					System.out.println("Perfecto, no mostraremos los últimos SEC Fillings");

				}

			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}