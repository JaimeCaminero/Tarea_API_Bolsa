package App;

import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		Finnhub main = new Finnhub();
		Scanner sc = new Scanner(System.in);
		main.mostrarInfoBase("QQQ");
		main.mostrarInfoBase("SPY");
		System.out.print("Introduce el nombre de la empresa que quieres buscar: ");
		String nombreEmpresa = sc.nextLine(); // Creamos la variable token que nos permitirá usarlo en todas nuestras
												// llamadas a las APIs
		final String token = "d4lg8khr01qr851o6g90d4lg8khr01qr851o6g9g";
		// Necesitamos evitar los espacios y codificarlos de forma que la petición de la
		// API lo reconozca
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
				System.out.println("¿Te gustaría conocer los últimos SEC fillings de la empresa?");
				System.out.println("1. Sí");
				System.out.println("2. No");
				System.out.print("Respuesta:");
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
		System.out.println("¿Quieres obtener las ultimas noticias sobre " + nombreEmpresa + "?");
		System.out.println("1. Si / 2. No ");
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
				System.out.println("¿Quieres obtener las ultimas noticias sobre " + nombreEmpresa + "?");
				System.out.println("1. Si / 2. No ");
				sc.nextLine();
			}
		} while (!entradaValida);

		if (respuestaNoticias == 1) {
			System.out.println("\nEstas son las últimas noticias sobre " + nombreEmpresa + "\n");
			main.obtenerNoticias(ticker);
		} else {
			System.out.println("Vale, no mostraremos más noticias.");
		}

	}

}