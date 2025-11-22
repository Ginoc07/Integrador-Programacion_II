package TPI_grupo94.Service;

import TPI_grupo94.Models.Libro;
import java.util.List;
import TPI_grupo94.Dao.LibroDAO;

/**
 * Implementación del servicio de negocio para la entidad Libro.
 * Capa intermedia entre la UI y el DAO que aplica validaciones de negocio.
 */
public class LibroService implements GenericService<Libro> {

    private final LibroDAO libroDAO;

    /**
     * Constructor con inyección de dependencias.
     */
    public LibroService(LibroDAO libroDAO) {
        if (libroDAO == null) {
            throw new IllegalArgumentException("LibroDAO no puede ser null");
        }
        this.libroDAO = libroDAO;
    }

    /**
     * Inserta un nuevo libro en la base de datos.
     */
    @Override
    public void insertar(Libro libro) throws Exception {
        validateLibro(libro);
        libroDAO.insertar(libro);
    }

    /**
     * Actualiza un libro existente en la base de datos.
     */
    @Override
    public void actualizar(Libro libro) throws Exception {
        validateLibro(libro);
        if (libro.getId() == null || libro.getId() <= 0) {
            throw new IllegalArgumentException("El ID del libro debe ser mayor a 0 para actualizar");
        }
        libroDAO.actualizar(libro);
    }

    /**
     * Elimina un libro por ID.
     */
    @Override
    public void eliminar(Long id) throws Exception {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID debe ser mayor a 0");
        }
        libroDAO.eliminar(id);
    }

    /**
     * Obtiene un libro por ID.
     */
    @Override
    public Libro getById(Long id) throws Exception {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID debe ser mayor a 0");
        }
        return libroDAO.getById(id);
    }

    /**
     * Obtiene todos los libros disponibles.
     */
    @Override
    public List<Libro> getAll() throws Exception {
        return libroDAO.getAll();
    }

    /**
     * Busca libros por título o autor (búsqueda flexible con LIKE).
     */
    public List<Libro> buscarPorTituloAutor(String filtro) throws Exception {
        if (filtro == null || filtro.trim().isEmpty()) {
            throw new IllegalArgumentException("El filtro de búsqueda no puede estar vacío");
        }
        return libroDAO.buscarPorTituloAutor(filtro);
    }

    /**
     * Busca un libro por ISBN exacto.
     */
    public Libro buscarPorIsbn(String isbn) throws Exception {
        if (isbn == null || isbn.trim().isEmpty()) {
            throw new IllegalArgumentException("El ISBN no puede estar vacío");
        }
        return libroDAO.buscarPorIsbn(isbn);
    }

    /**
     * Valida que un libro tenga datos correctos.
     */
    private void validateLibro(Libro libro) {
        if (libro == null) {
            throw new IllegalArgumentException("El libro no puede ser null");
        }
        if (libro.getTitulo() == null || libro.getTitulo().trim().isEmpty()) {
            throw new IllegalArgumentException("El título del libro no puede estar vacío");
        }
        if (libro.getAutor() == null || libro.getAutor().trim().isEmpty()) {
            throw new IllegalArgumentException("El autor del libro no puede estar vacío");
        }
    }
}