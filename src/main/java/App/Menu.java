package App;

import java.util.Scanner;

public class Menu {
	private Scanner sc = new Scanner(System.in);
	private Finnhub finnhub = new Finnhub("d0hf44pr01qv1u373vhgd0hf44pr01qv1u373vi0");

	public void comienzo() {
		int opcion = 1;
		System.out.println("Bienvenido al programa de Bolsa");

		while (opcion != 0) {
			System.out.println("1. Buscar un valor");
			System.out.println("2. salir");
			System.out.print("Introduce una opción:");
			opcion = sc.nextInt();
			sc.nextLine();

			switch (opcion) {
			case 1:
				System.out.print("Introduce el nombre de la empresa que quieres buscar: ");
				String empresa = sc.nextLine();
				finnhub.searchCompany(empresa);
				break;

			default:
				System.out.println("Opción no válida");
				break;
			}

		}
	}
}
