package App;

import java.util.Scanner;

public class Main {
	// Creamos un método menú que consistirá en un bucle con switch
	public void Menu() {
		Scanner sc = new Scanner(System.in);
		Finnhub miFinn = new Finnhub();
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
			// da error en el parseo, por eso lo tratamos como String
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
						// Si el número está fuera de rango lo indicamos
						System.out.print("Introduce un número entre el 1 y 5: ");
					}
					// Si al parsear es una letra o caracter especial lo indicaremos
				} catch (NumberFormatException e) {
					System.out.print("Debes introducir un número válido: ");
				}
			}
			// Creamos el switch
			switch (respuesta) {
			// Este caso pide el nombre o ticker de la empresa para poder mostrar datos
			// principales
			case 1:
				System.out.print("\nIntroduce el nombre o Ticker de la empresa que quieres buscar: ");
				String nombreEmpresa = sc.nextLine();
				// Si se hace enter indicaremos y entraremos en bucle hasta que se corrija
				while (nombreEmpresa.trim().isEmpty()) {
					System.out.print("No puedes dejarlo vacío. Introduce un nombre válido: ");
					nombreEmpresa = sc.nextLine();
				}
				// Llamamos al método de la clase Finnhub con el parámetro de entrada
				miFinn.mostrarSymbolLookup(nombreEmpresa);
				break;
			// Caso para ver noticias de un ticker
			case 2:
				System.out.print("Introduce el Ticker de la empresa de la que quieres ver noticias: ");
				String tickerEmpresa = sc.nextLine();
				while (tickerEmpresa.trim().isEmpty()) {
					System.out.print("No puedes dejarlo vacío. Introduce un ticker válido: ");
					tickerEmpresa = sc.nextLine();
				}
				miFinn.obtenerNoticias(tickerEmpresa);
				break;
			// Caso para ver SEC Fillings de una empresa
			case 3:
				System.out.print("Introduce el Ticker de la empresa de la que quieres ver los SEC Fillings: ");
				String tickerFillings = sc.nextLine();
				while (tickerFillings.trim().isEmpty()) {
					System.out.print("No puedes dejarlo vacío. Introduce un ticker válido: ");
					tickerFillings = sc.nextLine();
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
				// Para guardar un ticker llamaremos al método correspondiente
				System.out.print("Introduce el ticker de la empresa que quieres guardar:");
				String tickerFichero = sc.nextLine();
				while (tickerFichero.trim().isEmpty()) {
					System.out.print("No puedes dejarlo vacío. Introduce un ticker válido: ");
					tickerFichero = sc.nextLine();
				}
				miFinn.guardarWatchlist(tickerFichero);
				break;
			// Método para cerra el programa
			case 5:
				continuar = false;
				System.out.println("Gracias por usar nuestro programa. Saliendo....");
				break;

			default:
				System.out.println("\nError. Introduce uno de los números indicados\n");
				break;
			}
		}
		sc.close();
	}

	public static void main(String[] args) {
		// Cramos objeto finnhub para llamar a los métodos
		Finnhub metodos = new Finnhub();
		Main miMain = new Main();
		Scanner sc = new Scanner(System.in);
		try {
			metodos.mostrarInfoBase("QQQ");
			metodos.mostrarInfoBase("SPY");
			miMain.Menu();
		} catch (ErrorConnectException e) {
			System.out.println("Ha ocurrido un error de conexión: " + e.getMessage());
		} catch (Exception e) {
			System.out.println("Ha ocurrido un error inesperado: " + e.getMessage());
		}
	}
}