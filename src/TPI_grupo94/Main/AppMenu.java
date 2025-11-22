package TPI_grupo94.Main;

import java.util.Scanner;

import TPI_grupo94.Dao.LibroDAO;
import TPI_grupo94.Dao.FichaBibliograficaDAO;
import TPI_grupo94.Service.LibroService;
import TPI_grupo94.Service.FichaBibliograficaService;

public class AppMenu {

    private final Scanner scanner;
    private final MenuHandler menuHandler;
    private boolean running;

    public AppMenu() {
        this.scanner = new Scanner(System.in);

        // Crear servicios y DAOs
        LibroService libroService = createLibroService();
        FichaBibliograficaService fichaService = createFichaService();

        // Pasar services + scanner al menú
        this.menuHandler = new MenuHandler(scanner, libroService, fichaService);

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
            } catch (Exception e) {
                System.err.println("Error inesperado: " + e.getMessage());
            }
        }
        scanner.close();
        System.out.println("¡Hasta luego!");
    }

    private void processOption(int opcion) {
        switch (opcion) {
            case 1:
                menuHandler.crearLibro();
                break;
            case 2:
                menuHandler.listarLibros();
                break;
            case 3:
                menuHandler.actualizarLibro();
                break;
            case 4:
                menuHandler.eliminarLibro();
                break;
            case 5:
                menuHandler.crearFicha();
                break;
            case 6:
                menuHandler.listarFichas();
                break;
            case 7:
                menuHandler.actualizarFichaPorId();
                break;
            case 8:
                menuHandler.eliminarFichaPorId();
                break;
            case 9:
                menuHandler.actualizarFichaPorLibro();
                break;
            case 10:
                menuHandler.eliminarFichaPorLibro();
                break;
            case 0:
                System.out.println("Saliendo...");
                running = false;
                break;
            default:
                System.out.println("Opción inválida. Intente nuevamente.");
                break;
        }
    }

    private LibroService createLibroService() {
        // Crear DAOs
        FichaBibliograficaDAO fichaDAO = new FichaBibliograficaDAO();
        LibroDAO libroDAO = new LibroDAO(fichaDAO);

        // Crear Service principal
        return new LibroService(libroDAO);
    }

    private FichaBibliograficaService createFichaService() {
        // Crear DAO de ficha
        FichaBibliograficaDAO fichaDAO = new FichaBibliograficaDAO();
        
        // Crear Service de ficha
        return new FichaBibliograficaService(fichaDAO);
    }
}
