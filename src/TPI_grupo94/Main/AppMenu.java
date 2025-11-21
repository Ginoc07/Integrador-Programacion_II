package TPI_grupo94.Main;

import java.util.Scanner;

import TPI_grupo94.Dao.LibroDAO;
import TPI_grupo94.Dao.FichaBibliograficaDAO;
import TPI_grupo94.Service.LibroServiceImpl;
import TPI_grupo94.Service.FichaBibliograficaServiceImpl;

public class AppMenu {

    private final Scanner scanner;
    private final MenuHandler menuHandler;
    private boolean running;

    public AppMenu() {
        this.scanner = new Scanner(System.in);

        // Crear servicios y DAOs
        LibroServiceImpl libroService = createLibroService();

        // Pasar service + scanner al menú
        this.menuHandler = new MenuHandler(scanner, libroService);

        this.running = true;
    }

    public static void main(String[] args) {
        AppMenu app = new AppMenu();
        app.run();
    }

    public void run() {
        while (running) {
            MenuDisplay.mostrarMenuPrincipal();
            try {
                int opcion = Integer.parseInt(scanner.nextLine());
                processOption(opcion);
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Ingrese un número.");
            }
        }
        scanner.close();
    }

    private void processOption(int opcion) {
        switch (opcion) {

            case 1 -> menuHandler.crearLibro();
            case 2 -> menuHandler.listarLibros();
            case 3 -> menuHandler.actualizarLibro();
            case 4 -> menuHandler.eliminarLibro();

            case 5 -> menuHandler.crearFicha();
            case 6 -> menuHandler.listarFichas();
            case 7 -> menuHandler.actualizarFichaPorId();
            case 8 -> menuHandler.eliminarFichaPorId();

            case 9 -> menuHandler.actualizarFichaPorLibro();
            case 10 -> menuHandler.eliminarFichaPorLibro();

            case 0 -> {
                System.out.println("Saliendo...");
                running = false;
            }

            default -> System.out.println("Opción inválida.");
        }
    }

    private LibroServiceImpl createLibroService() {

        // Crear DAOs
        FichaBibliograficaDAO fichaDAO = new FichaBibliograficaDAO();
        LibroDAO libroDAO = new LibroDAO(fichaDAO);

        // Crear Service de ficha
        FichaBibliograficaServiceImpl fichaService = new FichaBibliograficaServiceImpl(fichaDAO);

        // Service principal
        return new LibroServiceImpl(libroDAO, fichaService);
    }
}
