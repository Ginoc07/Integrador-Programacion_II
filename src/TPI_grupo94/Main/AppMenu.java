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
        LibroServiceImpl libroService = createLibroService();
        this.menuHandler = new MenuHandler(scanner, libroService);
        this.running = true;
    }

    public static void main(String[] args) {
        AppMenu app = new AppMenu();
        app.run();
    }

    public void run() {
        while (running) {
            try {
                MenuDisplay.mostrarMenuPrincipal();
                int opcion = Integer.parseInt(scanner.nextLine());
                processOption(opcion);
            } catch (NumberFormatException e) {
                System.out.println("Entrada invalida. Por favor, ingrese un numero.");
            }
        }
        scanner.close();
    }

    private void processOption(int opcion) {
        switch (opcion) {
            case 1 -> menuHandler.crearLibro();
            case 2 -> menuHandler.listarLibros();
            case 3 -> menuHandler.actualizarLibros();
            case 4 -> menuHandler.eliminarLibro();
            case 5 -> menuHandler.crearFichaBibliografica();
            case 6 -> menuHandler.listarFichasBibliograficas();
            case 7 -> menuHandler.actualizarFichaBibliograficaPorId();
            case 8 -> menuHandler.eliminarFichaBibliograficaPorId();
            case 9 -> menuHandler.actualizarFichaBibliograficaPorIdDeLibro();
            case 10 -> menuHandler.eliminarFichaBibliograficaPorIdDeLibro();
            case 0 -> {
                System.out.println("Saliendo...");
                running = false;
            }
            default -> System.out.println("Opcion no valida.");
        }
    }

    private LibroServiceImpl createLibroService() {
        FichaBibliograficaDAO fichaBibliograficaDAO = new FichaBibliograficaDAO();
        LibroDAO libroDAO = new LibroDAO(FichaBibliograficaDAO);
        FichaBibliograficaServiceImpl FichaBibliograficaService = new FichaBibliograficaServiceImpl(FichaBibliograficaDAO);
        return new LibroServiceImpl(libroDAO, FichaBibliograficaService);
    }
}