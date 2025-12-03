package App;

import java.util.Scanner;

public class Main {

	public void Menu() {
		Scanner sc = new Scanner(System.in);
		Finnhub miFinn = new Finnhub();
		miFinn.mostrarInfoBase("QQQ");
		miFinn.mostrarInfoBase("SPY");
		boolean continuar = true;
		System.out.println("Bienvenido a nuestro programa de Bolsa!");
		while (continuar) {
			System.out.println("Escoja una de las siguientes opciones:");
			System.out.println("1. Buscar empresa por nombre");
			System.out.println("2. Buscar noticias empresa por Ticker");
			System.out.println("3. Ver SEC Fillings de una empresa");
			System.out.println("4. Guardar empresa en Watchlist");
			System.out.println("5. Salir");
			System.out.print("Opción escogida: ");

			// Necesitaremos validar si el usuario pulsa enter o si mete una letra que luego
			// da error en el parseo
			int respuesta = -1;
			boolean respuestaValida = false;
			while (!respuestaValida) {
				String entrada = sc.nextLine();
				while (entrada.trim().isEmpty()) {
					System.out.print("No puedes pulsar ENTER. Introduce una opción: ");
					entrada = sc.nextLine();
				}

				try {
					respuesta = Integer.parseInt(entrada);

					if (respuesta >= 1 && respuesta <= 5) {
						respuestaValida = true;
					} else {
						System.out.print("Introduce un número entre el 1 y 5: ");
					}

				} catch (NumberFormatException e) {
					System.out.print("Debes introducir un número válido: ");
				}
			}

			switch (respuesta) {
			case 1:
				System.out.print("\nIntroduce el nombre de la empresa que quieres buscar: ");
				String nombreEmpresa = sc.nextLine();
				while (nombreEmpresa.trim().isEmpty()) {
					System.out.print("No puedes dejarlo vacío. Introduce un nombre válido: ");
					nombreEmpresa = sc.nextLine();
				}
				if (miFinn.mostrarSymbolLookup(nombreEmpresa)) {
					System.out.print("\nUna vez vistos los resultados, introduce el Ticker de la empresa deseada: ");
					String ticker = sc.nextLine();
					while (ticker.trim().isEmpty()) {
						System.out.print("No puedes dejarlo vacío. Introduce un ticker válido: ");
						ticker = sc.nextLine();
					}
					miFinn.buscarEmpresaDeseada(nombreEmpresa, ticker);

				}
				break;
			case 2:
				System.out.print("Introduce el Ticker de la empresa de la que quieres ver noticias: ");
				String tickerEmpresa = sc.nextLine();
				while (tickerEmpresa.trim().isEmpty()) {
					System.out.print("No puedes dejarlo vacío. Introduce un ticker válido: ");
					tickerEmpresa = sc.nextLine();
				}
				miFinn.obtenerNoticias(tickerEmpresa);
				break;
			case 3:
				System.out.print("Introduce el Ticker de la empresa de la que quieres ver los SEC Fillings: ");
				String tickerFillings = sc.nextLine();
				while (tickerFillings.trim().isEmpty()) {
					System.out.print("No puedes dejarlo vacío. Introduce un ticker válido: ");
					tickerEmpresa = sc.nextLine();
				}
				tickerFillings.trim();
				if (miFinn.mostrarSECFilling(tickerFillings)) {
					System.out.println("Quieres abrir uno en web?");
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
							System.out.println("Quieres abrir uno en web?");
							System.out.println("1. Sí");
							System.out.println("2. No");
							System.out.print("Respuesta:");
							sc.nextLine();
						}
					} while (!entradaValida);
					if (respuestaSEC == 1)
						miFinn.buscarFillingWeb(tickerFillings);
					else {
						System.out.println("No abriremos ningún SEC Fillings en web");
					}
				}
				break;

			case 4:
				System.out.println("¿De que empresa quieres guardar datos?");
				String tickerFichero = sc.nextLine();
				miFinn.guardarWatchlist(tickerFichero);
				break;
			case 5:
				continuar = false;
				System.out.println("Saliendo....");
				break;

			default:
				System.out.println("\nError. Introduce uno de los números indicados\n");
				break;
			}

		}
		sc.close();
	}

	public static void main(String[] args) {
		Main miMain = new Main();
		miMain.Menu();
	}

}