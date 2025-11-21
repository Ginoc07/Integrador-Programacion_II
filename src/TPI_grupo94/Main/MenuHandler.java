package TPI_grupo94.Main;

import java.util.Scanner;
import TPI_grupo94.Models.Libro;
import TPI_grupo94.Models.FichaBibliografica;
import TPI_grupo94.Service.LibroService;
import TPI_grupo94.Service.FichaBibliograficaService;

import java.util.List;

public class MenuHandler {

    private final Scanner scanner;
    private final LibroService libroService;
    private final FichaBibliograficaService fichaService;

    public MenuHandler(Scanner scanner, LibroService libroService, FichaBibliograficaService fichaService) {
        this.scanner = scanner;
        this.libroService = libroService;
        this.fichaService = fichaService;
    }

    // ============================================================
    // 1. CREAR LIBRO
    // ============================================================
    public void crearLibro() {
        try {
            // Crear y configurar el libro
            Libro libro = new Libro();
            
            System.out.print("Título del libro: ");
            libro.setTitulo(scanner.nextLine().trim());
            
            System.out.print("Autor del libro: ");
            libro.setAutor(scanner.nextLine().trim());
            
            System.out.print("Año de publicación: ");
            libro.setAnioPublicacion(Integer.parseInt(scanner.nextLine().trim()));
            
            System.out.print("Género: ");
            libro.setGenero(scanner.nextLine().trim());
            
            // Insertar libro
            libroService.insertar(libro);
            System.out.println("Libro creado con ID: " + libro.getId());

            System.out.print("¿Agregar ficha bibliográfica? (s/n): ");
            if (scanner.nextLine().trim().equalsIgnoreCase("s")) {
                FichaBibliografica ficha = crearFichaBibliografica(libro.getId());
                fichaService.insertar(ficha);
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

            System.out.println("\n=== LISTA DE LIBROS ===");
            for (Libro libro : libros) {
                System.out.println("ID: " + libro.getId() + 
                                 " | Título: " + libro.getTitulo() +
                                 " | Autor: " + libro.getAutor() +
                                 " | Año: " + libro.getAnioPublicacion() +
                                 " | Género: " + libro.getGenero());
            }

        } catch (Exception e) {
            System.err.println("Error al listar libros: " + e.getMessage());
        }
    }

    // ============================================================
    // 3. ACTUALIZAR LIBRO
    // ============================================================
    public void actualizarLibro() {
        try {
            System.out.print("ID del libro a actualizar: ");
            Long id = Long.parseLong(scanner.nextLine());
            
            Libro libro = libroService.getById(id);
            if (libro == null) {
                System.out.println("No se encontró libro con ID: " + id);
                return;
            }
            
            System.out.print("Nuevo título (" + libro.getTitulo() + "): ");
            String titulo = scanner.nextLine().trim();
            if (!titulo.isEmpty()) libro.setTitulo(titulo);
            
            System.out.print("Nuevo autor (" + libro.getAutor() + "): ");
            String autor = scanner.nextLine().trim();
            if (!autor.isEmpty()) libro.setAutor(autor);
            
            System.out.print("Nuevo año (" + libro.getAnioPublicacion() + "): ");
            String anioStr = scanner.nextLine().trim();
            if (!anioStr.isEmpty()) libro.setAnioPublicacion(Integer.parseInt(anioStr));
            
            System.out.print("Nuevo género (" + libro.getGenero() + "): ");
            String genero = scanner.nextLine().trim();
            if (!genero.isEmpty()) libro.setGenero(genero);
            
            libroService.actualizar(libro);
            System.out.println("Libro actualizado correctamente.");

        } catch (Exception e) {
            System.err.println("Error al actualizar libro: " + e.getMessage());
        }
    }

    // ============================================================
    // 4. ELIMINAR LIBRO
    // ============================================================
    public void eliminarLibro() {
        try {
            System.out.print("ID del libro a eliminar: ");
            Long id = Long.parseLong(scanner.nextLine());

            // Primero buscar si tiene ficha para eliminarla
            List<FichaBibliografica> fichas = fichaService.getAll();
            for (FichaBibliografica ficha : fichas) {
                if (ficha.getIdLibro() != null && ficha.getIdLibro().equals(id)) {
                    fichaService.eliminar(ficha.getId());
                    break;
                }
            }

            libroService.eliminar(id);
            System.out.println("Libro eliminado.");

        } catch (Exception e) {
            System.err.println("Error al eliminar libro: " + e.getMessage());
        }
    }

    // ============================================================
    // 5. CREAR FICHA
    // ============================================================
    public void crearFicha() {
        try {
            System.out.print("ID del libro para la ficha: ");
            Long idLibro = Long.parseLong(scanner.nextLine());

            // Verificar que el libro existe
            Libro libro = libroService.getById(idLibro);
            if (libro == null) {
                System.out.println("No existe libro con ID: " + idLibro);
                return;
            }

            FichaBibliografica ficha = crearFichaBibliografica(idLibro);
            fichaService.insertar(ficha);
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
            List<FichaBibliografica> fichas = fichaService.getAll();

            if (fichas.isEmpty()) {
                System.out.println("No hay fichas cargadas.");
                return;
            }

            System.out.println("\n=== LISTA DE FICHAS BIBLIOGRÁFICAS ===");
            for (FichaBibliografica ficha : fichas) {
                System.out.println("ID: " + ficha.getId() + 
                                 " | Libro ID: " + ficha.getIdLibro() +
                                 " | Título: " + ficha.getTitulo() +
                                 " | Autor: " + ficha.getAutor() +
                                 " | Año: " + ficha.getAnio() +
                                 " | Editorial: " + ficha.getEditorial());
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
            System.out.print("ID de la ficha a actualizar: ");
            Long idFicha = Long.parseLong(scanner.nextLine());

            FichaBibliografica ficha = fichaService.getById(idFicha);

            if (ficha == null) {
                System.out.println("Ficha no encontrada.");
                return;
            }

            actualizarFichaInterna(ficha);
            fichaService.actualizar(ficha);
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
            System.out.print("ID de la ficha a eliminar: ");
            Long idFicha = Long.parseLong(scanner.nextLine());

            fichaService.eliminar(idFicha);
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
            Long idLibro = Long.parseLong(scanner.nextLine());

            // Buscar ficha por ID de libro
            FichaBibliografica fichaEncontrada = null;
            List<FichaBibliografica> fichas = fichaService.getAll();
            for (FichaBibliografica ficha : fichas) {
                if (ficha.getIdLibro() != null && ficha.getIdLibro().equals(idLibro)) {
                    fichaEncontrada = ficha;
                    break;
                }
            }

            if (fichaEncontrada == null) {
                System.out.println("Ese libro no tiene ficha.");
                return;
            }

            actualizarFichaInterna(fichaEncontrada);
            fichaService.actualizar(fichaEncontrada);
            System.out.println("Ficha actualizada.");

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
            Long idLibro = Long.parseLong(scanner.nextLine());

            // Buscar y eliminar ficha por ID de libro
            List<FichaBibliografica> fichas = fichaService.getAll();
            for (FichaBibliografica ficha : fichas) {
                if (ficha.getIdLibro() != null && ficha.getIdLibro().equals(idLibro)) {
                    fichaService.eliminar(ficha.getId());
                    System.out.println("Ficha eliminada.");
                    return;
                }
            }
            
            System.out.println("No se encontró ficha para el libro con ID: " + idLibro);

        } catch (Exception e) {
            System.err.println("Error al eliminar ficha: " + e.getMessage());
        }
    }

    // ============================================================
    // MÉTODOS AUXILIARES
    // ============================================================
    private FichaBibliografica crearFichaBibliografica(Long idLibro) {
        FichaBibliografica ficha = new FichaBibliografica();
        ficha.setIdLibro(idLibro);

        System.out.print("Título: ");
        ficha.setTitulo(scanner.nextLine().trim());

        System.out.print("Autor: ");
        ficha.setAutor(scanner.nextLine().trim());

        System.out.print("Año: ");
        ficha.setAnio(Short.parseShort(scanner.nextLine().trim()));

        System.out.print("Editorial: ");
        ficha.setEditorial(scanner.nextLine().trim());

        System.out.print("ISBN: ");
        ficha.setIsbn(scanner.nextLine().trim());

        return ficha;
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

        System.out.print("Nuevo ISBN (" + ficha.getIsbn() + "): ");
        String isbn = scanner.nextLine().trim();
        if (!isbn.isEmpty()) ficha.setIsbn(isbn);
    }
}
