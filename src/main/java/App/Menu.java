package App;

import java.util.Scanner;

public class Menu {
	private Main main;
	private Scanner sc = new Scanner(System.in);

	public void comienzo() {

		System.out.println("Bienvenido al programa de Bolsa");
		int opcion = 0;

		while (opcion != 4) {
			System.out.println("\n===== MENÚ PRINCIPAL =====");
			System.out.println("1. Mostrar info básica del mercado");
			System.out.println("2. Buscar empresa");
			System.out.println("3. Obtener noticias");
			System.out.println("4. Salir");
			System.out.print("Opción: ");

			opcion = sc.nextInt();
			sc.nextLine();

			switch (opcion) {

			case 1:
				// Mostrar datos mercado
				break;

			case 2:
				// Buscar Valor
				break;

			case 3:
				System.out.print("Introduce ticker para noticias: ");
				String ticker = sc.nextLine();
				// Método para noticias
				break;

			case 0:
				System.out.println("Saliendo del programa...");
				break;

			default:
				System.out.println("Opción inválida.");
			}
		}
	}

}
