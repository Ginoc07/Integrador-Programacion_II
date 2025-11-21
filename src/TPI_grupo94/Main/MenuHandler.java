package TPI_grupo94.Main;

import java.util.Scanner;

import tpi_grupo94.Models.Libro;
import tpi_grupo94.Models.FichaBibliografica;
import TPI_grupo94.Service.LibroServiceImpl;

import java.util.List;

public class MenuHandler {

    private final Scanner scanner;
    private final LibroServiceImpl libroService;

    public MenuHandler(Scanner scanner, LibroServiceImpl libroService) {
        this.scanner = scanner;
        this.libroService = libroService;
    }

    // ============================================================
    // 1. CREAR LIBRO
    // ============================================================
    public void crearLibro() {
        try {
            Libro libro = new Libro();
            libroService.insertar(libro);
            System.out.println("Libro creado con ID: " + libro.getId());

            System.out.print("¿Agregar ficha bibliográfica? (s/n): ");
            if (scanner.nextLine().trim().equalsIgnoreCase("s")) {
                FichaBibliografica ficha = crearFichaBibliografica(libro.getId());
                libroService.insertarFicha(ficha);
                System.out.println("Ficha creada.");
            }

        } catch (Exception e) {
            System.err.println("Error al crear libro: " + e.getMessage());
        }
    }

    // ============================================================
    // 2. LISTAR LIBROS
    // ============================================================
    public void listarLibros() {
        try {
            List<Libro> libros = libroService.getAll();

            if (libros.isEmpty()) {
                System.out.println("No hay libros cargados.");
                return;
            }

            for (Libro l : libros) {
                System.out.println("Libro ID: " + l.getId());
            }

        } catch (Exception e) {
            System.err.println("Error al listar libros: " + e.getMessage());
        }
    }

    // ============================================================
    // 3. ACTUALIZAR LIBRO
    // ============================================================
    public void actualizarLibro() {
        System.out.println("El libro no tiene campos editables. Use opciones de ficha.");
    }

    // ============================================================
    // 4. ELIMINAR LIBRO
    // ============================================================
    public void eliminarLibro() {
        try {
            System.out.print("ID del libro: ");
            int id = Integer.parseInt(scanner.nextLine());

            libroService.eliminar(id);
            libroService.eliminarFichaPorLibro(id);

            System.out.println("Libro y ficha eliminados.");

        } catch (Exception e) {
            System.err.println("Error al eliminar libro: " + e.getMessage());
        }
    }

    // ============================================================
    // 5. CREAR FICHA
    // ============================================================
    public void crearFicha() {
        try {
            System.out.print("ID del libro: ");
            int idLibro = Integer.parseInt(scanner.nextLine());

            FichaBibliografica ficha = crearFichaBibliografica(idLibro);
            libroService.insertarFicha(ficha);

            System.out.println("Ficha creada.");

        } catch (Exception e) {
            System.err.println("Error al crear ficha: " + e.getMessage());
        }
    }

    // ============================================================
    // 6. LISTAR FICHAS
    // ============================================================
    public void listarFichas() {
        try {
            List<FichaBibliografica> fichas = libroService.getAllFichas();

            if (fichas.isEmpty()) {
                System.out.println("No hay fichas cargadas.");
                return;
            }

            for (FichaBibliografica f : fichas) {
                System.out.println(f);
            }

        } catch (Exception e) {
            System.err.println("Error al listar fichas: " + e.getMessage());
        }
    }

    // ============================================================
    // 7. ACTUALIZAR FICHA POR ID
    // ============================================================
    public void actualizarFichaPorId() {
        try {
            System.out.print("ID de la ficha: ");
            int idFicha = Integer.parseInt(scanner.nextLine());

            FichaBibliografica ficha = libroService.getFichaById(idFicha);

            if (ficha == null) {
                System.out.println("Ficha no encontrada.");
                return;
            }

            actualizarFichaInterna(ficha);
            libroService.actualizarFicha(ficha);

            System.out.println("Ficha actualizada.");

        } catch (Exception e) {
            System.err.println("Error al actualizar ficha: " + e.getMessage());
        }
    }

    // ============================================================
    // 8. ELIMINAR FICHA POR ID
    // ============================================================
    public void eliminarFichaPorId() {
        try {
            System.out.print("ID de la ficha: ");
            int idFicha = Integer.parseInt(scanner.nextLine());

            libroService.eliminarFicha(idFicha);

            System.out.println("Ficha eliminada.");

        } catch (Exception e) {
            System.err.println("Error al eliminar ficha: " + e.getMessage());
        }
    }

    // ============================================================
    // 9. ACTUALIZAR FICHA POR ID DE LIBRO
    // ============================================================
    public void actualizarFichaPorLibro() {
        try {
            System.out.print("ID del libro: ");
            int idLibro = Integer.parseInt(scanner.nextLine());

            FichaBibliografica ficha = libroService.getFichaByLibroId(idLibro);

            if (ficha == null) {
                System.out.println("Ese libro no tiene ficha.");
                return;
            }

            actualizarFichaInterna(ficha);
            libroService.actualizarFicha(ficha);

        } catch (Exception e) {
            System.err.println("Error al actualizar ficha: " + e.getMessage());
        }
    }

    // ============================================================
    // 10. ELIMINAR FICHA POR ID DE LIBRO
    // ============================================================
    public void eliminarFichaPorLibro() {
        try {
            System.out.print("ID del libro: ");
            int idLibro = Integer.parseInt(scanner.nextLine());

            libroService.eliminarFichaPorLibro(idLibro);

            System.out.println("Ficha eliminada.");

        } catch (Exception e) {
            System.err.println("Error al eliminar ficha: " + e.getMessage());
        }
    }

    // ============================================================
    // MÉTODOS AUXILIARES
    // ============================================================
    private FichaBibliografica crearFichaBibliografica(int idLibro) {

        System.out.print("Título: ");
        String titulo = scanner.nextLine().trim();

        System.out.print("Autor: ");
        String autor = scanner.nextLine().trim();

        System.out.print("Año: ");
        short anio = Short.parseShort(scanner.nextLine().trim());

        System.out.print("Editorial: ");
        String editorial = scanner.nextLine().trim();

        return new FichaBibliografica(0, idLibro, titulo, autor, anio, editorial, false);
    }

    private void actualizarFichaInterna(FichaBibliografica ficha) {

        System.out.print("Nuevo título (" + ficha.getTitulo() + "): ");
        String titulo = scanner.nextLine().trim();
        if (!titulo.isEmpty()) ficha.setTitulo(titulo);

        System.out.print("Nuevo autor (" + ficha.getAutor() + "): ");
        String autor = scanner.nextLine().trim();
        if (!autor.isEmpty()) ficha.setAutor(autor);

        System.out.print("Nuevo año (" + ficha.getAnio() + "): ");
        String anioStr = scanner.nextLine().trim();
        if (!anioStr.isEmpty()) ficha.setAnio(Short.parseShort(anioStr));

        System.out.print("Nueva editorial (" + ficha.getEditorial() + "): ");
        String editorial = scanner.nextLine().trim();
        if (!editorial.isEmpty()) ficha.setEditorial(editorial);
    }
}
