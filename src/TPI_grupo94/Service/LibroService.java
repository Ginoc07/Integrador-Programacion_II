package TPI_grupo94.Service;

import prog2int.Models.Libro;
import java.util.List;
import prog2int.Dao.LibroDAO;

/**
 * Implementación del servicio de negocio para la entidad Libro.
 * Capa intermedia entre la UI y el DAO que aplica validaciones de negocio.
 *
 * Responsabilidades:
 * - Validar datos de libro antes de persistir (RN-101: título, autor e ISBN obligatorios)
 * - Garantizar unicidad del ISBN en el sistema (RN-102)
 * - Proporcionar métodos de búsqueda especializados (por ISBN, título o autor)
 *
 * Patrón: Service Layer con inyección de dependencias
 */
public class LibroServiceImpl implements GenericService<Libro> {

    private final LibroDAO libroDAO;

    /**
     * Constructor con inyección de dependencias.
     * Valida que la dependencia no sea null (fail-fast).
     *
     * @param libroDAO DAO de libros
     * @throws IllegalArgumentException si la dependencia es null
     */
    public LibroServiceImpl(LibroDAO libroDAO) {
        if (libroDAO == null) {
            throw new IllegalArgumentException("LibroDAO no puede ser null");
        }
        this.libroDAO = libroDAO;
    }

    /**
     * Inserta un nuevo libro en la base de datos.
     *
     * Validaciones:
     * - Título, autor e ISBN obligatorios
     * - ISBN único
     *
     * @param libro Libro a insertar
     * @throws Exception Si la validación falla o hay error de BD
     */
    @Override
    public void insertar(Libro libro) throws Exception {
        validateLibro(libro);
        validateIsbnUnique(libro.getIsbn(), null);
        libroDAO.insertar(libro);
    }

    /**
     * Actualiza un libro existente en la base de datos.
     *
     * Validaciones:
     * - Libro debe tener ID > 0
     * - ISBN único (excepto para el mismo libro)
     *
     * @param libro Libro a actualizar
     * @throws Exception Si la validación falla o el libro no existe
     */
    @Override
    public void actualizar(Libro libro) throws Exception {
        validateLibro(libro);
        if (libro.getId() <= 0) {
            throw new IllegalArgumentException("El ID del libro debe ser mayor a 0 para actualizar");
        }
        validateIsbnUnique(libro.getIsbn(), libro.getId());
        libroDAO.actualizar(libro);
    }

    /**
     * Elimina un libro por ID.
     *
     * @param id ID del libro a eliminar
     * @throws Exception Si el ID es inválido o no existe el libro
     */
    @Override
    public void eliminar(int id) throws Exception {
        if (id <= 0) {
            throw new IllegalArgumentException("El ID debe ser mayor a 0");
        }
        libroDAO.eliminar(id);
    }

    /**
     * Obtiene un libro por ID.
     *
     * @param id ID del libro
     * @return Libro encontrado o null si no existe
     * @throws Exception Si el ID es inválido o hay error de BD
     */
    @Override
    public Libro getById(int id) throws Exception {
        if (id <= 0) {
            throw new IllegalArgumentException("El ID debe ser mayor a 0");
        }
        return libroDAO.getById(id);
    }

    /**
     * Obtiene todos los libros disponibles.
     *
     * @return Lista de libros (puede estar vacía)
     * @throws Exception Si hay error de BD
     */
    @Override
    public List<Libro> getAll() throws Exception {
        return libroDAO.getAll();
    }

    /**
     * Busca libros por título o autor (búsqueda flexible con LIKE).
     *
     * @param filtro Texto a buscar
     * @return Lista de libros que coinciden con el filtro
     * @throws IllegalArgumentException Si el filtro está vacío
     * @throws Exception Si hay error de BD
     */
    public List<Libro> buscarPorTituloAutor(String filtro) throws Exception {
        if (filtro == null || filtro.trim().isEmpty()) {
            throw new IllegalArgumentException("El filtro de búsqueda no puede estar vacío");
        }
        return libroDAO.buscarPorTituloAutor(filtro);
    }

    /**
     * Busca un libro por ISBN exacto.
     *
     * @param isbn ISBN del libro
     * @return Libro encontrado o null si no existe
     * @throws IllegalArgumentException Si el ISBN está vacío
     * @throws Exception Si hay error de BD
     */
    public Libro buscarPorIsbn(String isbn) throws Exception {
        if (isbn == null || isbn.trim().isEmpty()) {
            throw new IllegalArgumentException("El ISBN no puede estar vacío");
        }
        return libroDAO.buscarPorIsbn(isbn);
    }

    /**
     * Valida que un libro tenga datos correctos.
     *
     * Reglas de negocio:
     * - Título, autor e ISBN obligatorios
     *
     * @param libro Libro a validar
     * @throws IllegalArgumentException Si alguna validación falla
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
        if (libro.getIsbn() == null || libro.getIsbn().trim().isEmpty()) {
            throw new IllegalArgumentException("El ISBN del libro no puede estar vacío");
        }
    }

    /**
     * Valida que un ISBN sea único en el sistema.
     *
     * @param isbn ISBN a validar
     * @param libroId ID del libro (null para INSERT, != null para UPDATE)
     * @throws IllegalArgumentException Si el ISBN ya existe y pertenece a otro libro
     * @throws Exception Si hay error de BD
     */
    private void validateIsbnUnique(String isbn, Integer libroId) throws Exception {
        Libro existente = libroDAO.buscarPorIsbn(isbn);
        if (existente != null) {
            if (libroId == null || existente.getId() != libroId) {
                throw new IllegalArgumentException("Ya existe un libro con el ISBN: " + isbn);
            }
        }
    }
}