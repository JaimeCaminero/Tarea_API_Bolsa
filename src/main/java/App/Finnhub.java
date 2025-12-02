package App;

import java.awt.Desktop;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Finnhub {
	private final String inicioComunApi;
	private final String token;
	private final HttpClient client = HttpClient.newHttpClient();
	private final ObjectMapper om = new ObjectMapper();
	private LocalDate fechaActual = LocalDate.now();

	public Finnhub() {
		this.token = "&token=d0hf44pr01qv1u373vhgd0hf44pr01qv1u373vi0";
		this.inicioComunApi = "https://finnhub.io/api/v1";
	}

	public void obtenerNoticias(String ticker) {
		String encoded = URLEncoder.encode(ticker, StandardCharsets.UTF_8);
		LocalDate semanaPasada = fechaActual.minusDays(7);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String parteVariable = "/company-news?symbol=" + encoded + "&from=" + semanaPasada.format(formatter) + "&to="
				+ fechaActual.format(formatter);
		String httpTotal = inicioComunApi + parteVariable + token;
		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(httpTotal)).build();
		try {
			HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
			CompanyNews empresaNoticias[] = om.readValue(response.body(), CompanyNews[].class);

			if (empresaNoticias.length == 0) {
				System.out.println("La empresa no existe o no se han encontrado noticias para esa empresa. \n");
			} else {
				for (int i = 0; i < 3; i++) {
					System.out.println("Noticia " + (i + 1));
					System.out.println("-------------------");
					System.out.println(empresaNoticias[i].getHeadline());
					System.out.println(empresaNoticias[i].getSummary());
					System.out.println("Fuente: " + empresaNoticias[i].getSource());
					System.out.println(" ");
				}
			}
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}

	}

	public void mostrarInfoBase(String simbolo) {
		String parteVariable = "/quote?symbol=" + simbolo;
		String httpTotal = inicioComunApi + parteVariable + token;
		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(httpTotal)).build();
		try {
			HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
			Quote quote = om.readValue(response.body(), Quote.class);
			if (simbolo.equals("QQQ")) {
				System.out
						.println("El Nasdaq 100 (" + simbolo + ") ha abierto en " + quote.getOpenPriceOfTheDay() + "$");
			} else {
				System.out.println("El S&P 500 (" + simbolo + ") ha abierto en " + quote.getOpenPriceOfTheDay() + "$");
			}
			System.out.println("Actualmente tiene un precio de " + quote.getCurrentPrice() + "$");
			System.out.println("Tiene un cambio porcentual de: " + quote.getPercentChange() + "% \n");

		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}

	}

	public void guardarWatchlist(String ticker) {
		String encoded = URLEncoder.encode(ticker, StandardCharsets.UTF_8);
		String urlQuoteWatchlist = inicioComunApi + "/quote?symbol=" + encoded + token;
		HttpRequest requestQuoteWatchlist = HttpRequest.newBuilder().uri(URI.create(urlQuoteWatchlist)).build();
		try {
			HttpResponse<String> responseQuoteWatchlist = client.send(requestQuoteWatchlist, BodyHandlers.ofString());
			Quote quote = om.readValue(responseQuoteWatchlist.body(), Quote.class);
			double precioActualWatchlist = quote.getCurrentPrice();
			String precioDosDecimales = String.format("%.2f", precioActualWatchlist);
			String formato = encoded + " | " + precioDosDecimales + "$ | " + fechaActual;
			try (FileWriter fw = new FileWriter("miWatchlist.txt", true);
					BufferedWriter bw = new BufferedWriter(fw);
					PrintWriter printwriter = new PrintWriter(bw)) {
				printwriter.println(formato);
				System.out
						.println("Se ha añadido la acción " + encoded + " a la watchlist con el precio y fecha actual");

			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void buscarEmpresaDeseada(String nombreEmpresa, String tickerEmpresa) {
		String encoded = URLEncoder.encode(nombreEmpresa, StandardCharsets.UTF_8);
		try {
			String urlSymbol = inicioComunApi + "/search?q=" + encoded + "&exchange=US" + token;
			HttpRequest requestSymbol = HttpRequest.newBuilder().uri(URI.create(urlSymbol)).build();
			HttpResponse<String> responseSearch = client.send(requestSymbol, BodyHandlers.ofString());

			// System.out.println(responseSearch.statusCode() + ":\n" +
			// responseSearch.body());

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
				System.out.println("El ticker introducido no coincide con ninguno de los resultados.\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public boolean mostrarSymbolLookup(String nombreEmpresa) {
		String encoded = URLEncoder.encode(nombreEmpresa, StandardCharsets.UTF_8);
		boolean existe = false;
		try {
			String urlSymbol = inicioComunApi + "/search?q=" + encoded + "&exchange=US" + token;
			HttpRequest requestSymbol = HttpRequest.newBuilder().uri(URI.create(urlSymbol)).build();
			HttpResponse<String> responseSearch = client.send(requestSymbol, BodyHandlers.ofString());

			// System.out.println(responseSearch.statusCode() + ":\n" +
			// responseSearch.body());

			Root_SymbolLookup r = om.readValue(responseSearch.body(), Root_SymbolLookup.class);

			System.out.println("Resultados obtenidos: " + r.getCount());

			if (r.getCount() == 0) {
				System.out.println("Por favor, revisa que has introducido el nombre correctamente \n");
			} else {
				for (Symbol s : r.getResult()) {
					System.out.println("    Nombre: " + s.getDescription());
					System.out.println("    Ticker: " + s.getSymbol());
					System.out.println("    Tipo de acción: " + s.getType());

					String urlQuote = inicioComunApi + "/quote?symbol=" + s.getSymbol() + token;
					HttpRequest requestQuote = HttpRequest.newBuilder().uri(URI.create(urlQuote)).build();

					HttpResponse<String> responseQuote = client.send(requestQuote,
							HttpResponse.BodyHandlers.ofString());
					Quote quote = om.readValue(responseQuote.body(), Quote.class);

					System.out.println("    Precio actual: " + quote.getCurrentPrice());
					System.out.println("    % Cambio: " + quote.getPercentChange() + "\n");
					existe = true;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();

		}
		return existe;
	}

	public void buscarFillingWeb(String ticker) {
		String encoded = URLEncoder.encode(ticker, StandardCharsets.UTF_8);
		String urlSECFillings = inicioComunApi + "/stock/filings?symbol=" + encoded + token;
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

	public boolean mostrarSECFilling(String ticker) {
		String encoded = URLEncoder.encode(ticker, StandardCharsets.UTF_8);
		String urlSECFillings = inicioComunApi + "/stock/filings?symbol=" + encoded + token;
		HttpRequest requestSECFilling = HttpRequest.newBuilder().uri(URI.create(urlSECFillings)).build();
		boolean existe = false;
		try {
			HttpResponse<String> responseSECFilling = client.send(requestSECFilling, BodyHandlers.ofString());

			System.out.println("SEC Fillings para " + encoded + ":");

			List<SECFillings> fillings = om.readValue(responseSECFilling.body(),
					om.getTypeFactory().constructCollectionType(List.class, SECFillings.class));
			if (fillings.size() == 0) {
				System.out.println("La empresa no existe o no se han encontrado SEC Fillings para esa empresa. \n");
			} else {
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
					existe = true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return existe;
	}
}