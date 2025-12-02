package App;

import java.awt.Desktop;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Main {
	LocalDate fechaActual = LocalDate.now();

	public void obtenerNoticias(String simbolo) {
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

	public void guardarWatchlist(String ticker) {
		String token = "d4lg8khr01qr851o6g90d4lg8khr01qr851o6g9g";
		HttpClient client = HttpClient.newHttpClient();
		ObjectMapper om = new ObjectMapper();
		String urlQuoteWatchlist = "https://finnhub.io/api/v1/quote?symbol=" + ticker + "&token=" + token;
		HttpRequest requestQuoteWatchlist = HttpRequest.newBuilder().uri(URI.create(urlQuoteWatchlist)).build();
		try {
			HttpResponse<String> responseQuoteWatchlist = client.send(requestQuoteWatchlist, BodyHandlers.ofString());
			Quote quote = om.readValue(responseQuoteWatchlist.body(), Quote.class);
			double precioActualWatchlist = quote.getCurrentPrice();
			String precioDosDecimales = String.format("%.2f", precioActualWatchlist);
			String formato = ticker + " | " + precioDosDecimales + "$ | " + fechaActual;
			try (FileWriter fw = new FileWriter("miWatchlist.txt", true);
					BufferedWriter bw = new BufferedWriter(fw);
					PrintWriter printwriter = new PrintWriter(bw)) {
				printwriter.println(formato);
				System.out
						.println("Se ha añadido la acción " + ticker + " a la watchlist con el precio y fecha actual");

			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void buscarEmpresaDeseada(String nombreEmpresa, String tickerEmpresa) {
		String token = "d4lg8khr01qr851o6g90d4lg8khr01qr851o6g9g";
		String encoded = URLEncoder.encode(nombreEmpresa, StandardCharsets.UTF_8);

		HttpClient client = HttpClient.newHttpClient();
		ObjectMapper om = new ObjectMapper();

		try {
			String urlSymbol = "https://finnhub.io/api/v1/search?q=" + encoded + "&exchange=US&token=" + token;
			HttpRequest requestSymbol = HttpRequest.newBuilder().uri(URI.create(urlSymbol)).build();
			HttpResponse<String> responseSearch = client.send(requestSymbol, BodyHandlers.ofString());

			System.out.println(responseSearch.statusCode() + ":\n" + responseSearch.body());

			Root_SymbolLookup r = om.readValue(responseSearch.body(), Root_SymbolLookup.class);

			boolean existeTicker = false;

			for (Symbol s : r.getResult()) {
				if (s.getSymbol().equalsIgnoreCase(tickerEmpresa)) {
					existeTicker = true;
					// Meter break para que no consuma recursos??
					break;
				}
			}

			if (!existeTicker) {
				System.out.println("El ticker introducido no coincide con ninguno de los resultados.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void mostrarSymbolLookup(String nombreEmpresa) {
		String token = "d4lg8khr01qr851o6g90d4lg8khr01qr851o6g9g";
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
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();

		}
	}

	public void buscarFillingWeb(String ticker) {
		HttpClient client = HttpClient.newHttpClient();
		ObjectMapper om = new ObjectMapper();

		String token = "d4lg8khr01qr851o6g90d4lg8khr01qr851o6g9g";

		String urlSECFillings = "https://finnhub.io/api/v1/stock/filings?symbol=" + ticker + "&token=" + token;
		HttpRequest requestSECFilling = HttpRequest.newBuilder().uri(URI.create(urlSECFillings)).build();
		try {
			HttpResponse<String> responseSECFilling = client.send(requestSECFilling, BodyHandlers.ofString());

			List<SECFillings> fillings = om.readValue(responseSECFilling.body(),
					om.getTypeFactory().constructCollectionType(List.class, SECFillings.class));
			Scanner sc = new Scanner(System.in);
			System.out.print("Introduce el Access Number del filling que quieres ver en la web:");
			String AccessNumber = sc.nextLine();
			String URLCik = null;
			for (SECFillings f : fillings) {
				if (f.getAccessNumber().equals(AccessNumber)) {
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
	}

	public void mostrarSECFilling(String ticker) {
		HttpClient client = HttpClient.newHttpClient();
		ObjectMapper om = new ObjectMapper();

		String token = "d4lg8khr01qr851o6g90d4lg8khr01qr851o6g9g";

		String urlSECFillings = "https://finnhub.io/api/v1/stock/filings?symbol=" + ticker + "&token=" + token;
		HttpRequest requestSECFilling = HttpRequest.newBuilder().uri(URI.create(urlSECFillings)).build();

		try {
			HttpResponse<String> responseSECFilling = client.send(requestSECFilling, BodyHandlers.ofString());

			System.out.println("SEC Fillings para " + ticker + ":");

			List<SECFillings> fillings = om.readValue(responseSECFilling.body(),
					om.getTypeFactory().constructCollectionType(List.class, SECFillings.class));

			int maxFillings = Math.min(4, fillings.size());
			for (int i = 0; i < maxFillings; i++) {
				System.out.println("\n-------------------------------------");
				System.out.println("    Access Number: " + fillings.get(i).getAccessNumber());
				System.out.println("    Cik: " + fillings.get(i).getCik());
				System.out.println("    Tipo: " + fillings.get(i).getForm());
				System.out.println("    Fecha inscripción: " + fillings.get(i).getFilledDate());
				System.out.println("    Fecha aceptado: " + fillings.get(i).getAcceptedDate());
				System.out.println("    Filing URL: " + fillings.get(i).getFilingUrl());
				System.out.println("-------------------------------------\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		Main main = new Main();
		Scanner sc = new Scanner(System.in);
		main.mostrarInfoBase("QQQ");
		main.mostrarInfoBase("SPY");
		System.out.print("Introduce el nombre de la empresa que quieres buscar: ");
		String nombreEmpresa = sc.nextLine(); // Creamos la variable token que nos permitirá usarlo en todas nuestras
												// llamadas a las APIs
		final String token = "d4lg8khr01qr851o6g90d4lg8khr01qr851o6g9g";
		// Necesitamos evitar los espacios y codificarlos de forma que la petición de la
		// API lo reconozca

		String encoded = URLEncoder.encode(nombreEmpresa, StandardCharsets.UTF_8);

		HttpClient client = HttpClient.newHttpClient();
		ObjectMapper om = new ObjectMapper();
		main.mostrarSymbolLookup(nombreEmpresa);

		System.out.print("Una vez vistos los resultados, introduce el Ticker de la empresa deseada: ");
		String ticker = sc.nextLine();
		main.buscarEmpresaDeseada(nombreEmpresa, ticker);

		System.out.println("Gracias, has seleccionado el ticker " + ticker + "\n");

		System.out.println("Te gustaría añadir el ticker a tu Watchlist?");
		System.out.println("1. Sí");
		System.out.println("2. No");
		System.out.print("Respuesta:");
		int respuestaWatchlist = sc.nextInt();
		sc.nextLine();

		if (respuestaWatchlist == 1) {
			main.guardarWatchlist(ticker);
		}

		System.out.println("¿Te gustaría conocer los últimos SEC fillings de la empresa?");
		System.out.println("1. Sí");
		System.out.println("2. No");
		System.out.print("Respuesta:");

		int respuestaSEC = -1;
		boolean entradaValida = false;

		do {
			if (sc.hasNextInt()) {
				respuestaSEC = sc.nextInt();
				sc.nextLine();
				if (respuestaSEC == 1 || respuestaSEC == 2) {
					entradaValida = true;

				} else {
					System.out.println("Por favor, introduzca únicamente 1 o 2.");
				}
			} else {
				System.out.println("Por favor, introduzca un número (1 o 2).");
				sc.nextLine();
			}
		} while (!entradaValida);


		if (respuestaSEC == 1) {
			main.mostrarSECFilling(ticker);
			main.buscarFillingWeb(ticker);
		} else if (respuestaSEC == 2) {
			System.out.println("Perfecto, no mostraremos los últimos SEC Fillings");
		} else {
			System.out.println("Por favor, introduzca 1 o 2");
		}

		int respuestaNoticias = -1;
		entradaValida = false;
		do {
			if (sc.hasNextInt()) {
				respuestaNoticias = sc.nextInt();
				sc.nextLine();
				if (respuestaNoticias == 1 || respuestaNoticias == 2) {
					entradaValida = true;

				} else {
					System.out.println("Por favor, introduzca únicamente 1 o 2.");
				}
			} else {
				System.out.println("Por favor, introduzca un número (1 o 2).");
				sc.nextLine();
			}
		} while (!entradaValida);
		System.out.println("¿Quieres obtener las ultimas noticias sobre " + nombreEmpresa + "?");
		System.out.println("1. Si / 2. No ");
		if (respuestaNoticias == 1) {
			System.out.println("\nEstas son las últimas noticias sobre " + nombreEmpresa + "\n");
			main.obtenerNoticias(ticker);
		} else if (respuestaNoticias == 2) {
			System.out.println("Vale, no mostraremos más noticias.");
		} else {
			System.out.println("Número introducido no válido.");
		}

	}

}