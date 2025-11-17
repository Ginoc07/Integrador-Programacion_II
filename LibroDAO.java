package Fichaylibro;

import config.DatabaseConnection;

import models.Libro;

import java.sql.*;

import java.util.ArrayList;

import java.util.List;

import models.FichaBibliografica;

public class LibroDAO implements GenericDAO<Libro> {

    //Query para insertar un libro en la db
    // El id es AUTO_INCREMENT y se obtiene con RETURN_GENERATED_KEYS.
    private static final String INSERT_SQL = "INSERT INTO libros (id_ficha, titulo, autor, anio_publicacion, genero) VALUES (?, ?,?,?,?)";

    //Query para actualizar un libro en la db
    //NO actualiza el flag eliminado (solo se modifica en soft delete).
    private static final String UPDATE_SQL = "UPDATE libros SET id_ficha = ?, titulo = ?, autor = ?,  anio_publicacion = ?, genero = ? WHERE id = ?";

    // Soft delete > Marca eliminado=TRUE sin borrar físicamente la fila.
    //Preserva integridad referencial y datos históricos.
    private static final String DELETE_SQL = "UPDATE libros SET eliminado = TRUE WHERE id = ?";

    //Obtener libro por id
    //Muestra el isbn que obtiene de la tabla FichaBibliografica
    private static final String SELECT_LIBRO_BY_ID_SQL = "SELECT l.*, f.isbn "
            + "FROM libros l "
            + "LEFT JOIN fichabibliografica f ON l.fichabibliografica_id = f.id "
            + "WHERE l.id = ? AND l.eliminado = FALSE";

    //Seleccionar todos los libros activos 
    private static final String SELECT_ALL_BOOKS_SQL
            = "SELECT l.*, f.isbn "
            + "FROM libros l "
            + "LEFT JOIN fichabibliografica f ON l.fichabibliografica_id = f.id "
            + "WHERE l.eliminado = FALSE";

// Buscar libros por autor de manera flexible con like
    private static final String SEARCH_BOOKS_BY_AUTHOR_SQL
            = "SELECT l.id, l.titulo, l.autor, l.anio_publicacion, l.fichabibliografica_id, f.isbn "
            + "FROM libros l "
            + "LEFT JOIN fichabibliografica f ON l.fichabibliografica_id = f.id "
            + "WHERE l.eliminado = FALSE AND l.autor LIKE ?";

//Es importante crearlo para futuras operaciones aunque no lo usemos en este caso
    private final FichaBibliograficaDAO fichaDAO;

    public LibroDAO(FichaBibliograficaDAO fichaDAO) {
        if (fichaDAO == null) {
            throw new IllegalArgumentException("FichaBibliograficaDAO no puede ser null");
        }
        this.fichaDAO = fichaDAO;
    }



public void crear(Libro libro, Connection conn) throws Exception {
    try (PreparedStatement stmt = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
        setLibroParameters(stmt, libro);
        stmt.executeUpdate();
        setGeneratedId(stmt, libro);
    }
}

//Actualizar libro
    
    public void actualizar(Libro libro, Connection conn) throws Exception {
    try (PreparedStatement stmt = conn.prepareStatement(UPDATE_SQL)) {
        stmt.setLong(1, libro.getFichaBibliografica().getId());
        stmt.setString(2, libro.getTitulo());
        stmt.setString(3, libro.getAutor());
        stmt.setInt(4, libro.getAnioPublicacion());
        stmt.setString(5, libro.getGenero());
        stmt.setLong(6, libro.getId());
        
        int rowsAffected = stmt.executeUpdate();
        if (rowsAffected == 0) {
            throw new SQLException("No se pudo actualizar el libro con ID: " + libro.getId());
        }
    }
}

//Soft delete de libro 

public void eliminar(Long id, Connection conn) throws Exception {
    try (PreparedStatement stmt = conn.prepareStatement(DELETE_SQL)) {
        stmt.setLong(1, id);
        int rowsAffected = stmt.executeUpdate();
        if (rowsAffected == 0) {
            throw new SQLException("No se encontró libro con ID: " + id);
        }
    }
}

/// Mostrar libro por ID 
  

public Libro leer(Long id, Connection conn) throws Exception {
    try (PreparedStatement stmt = conn.prepareStatement(SELECT_LIBRO_BY_ID_SQL)) {
        stmt.setLong(1, id);

        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return mapResultSetToLibro(rs);
            }
        }
    } catch (SQLException e) {
        throw new Exception("Error al obtener libro por ID: " + e.getMessage(), e);
    }
    return null;
}

// Mostrar todos los libros activos (transaccional)

public List<Libro> leerTodos(Connection conn) throws Exception {
    List<Libro> libros = new ArrayList<>();

    try (Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(SELECT_ALL_BOOKS_SQL)) {

        while (rs.next()) {
            libros.add(mapResultSetToLibro(rs));
        }
    } catch (SQLException e) {
        throw new Exception("Error al obtener todos los libros: " + e.getMessage(), e);
    }

    return libros;
}

    
    public List<Libro> buscarPorAutor(String filtro) throws SQLException {
        if (filtro == null || filtro.trim().isEmpty()) {
            throw new IllegalArgumentException("El filtro de búsqueda no puede estar vacío");
        }

        List<Libro> libros = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(SEARCH_BOOKS_BY_AUTHOR_SQL)) {

            // Construye el patrón LIKE: %filtro%
            String searchPattern = "%" + filtro + "%";
            stmt.setString(1, searchPattern);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    libros.add(mapResultSetToLibro(rs));
                }
            }
        }

        return libros;
    }

    //parametros de libros
    
private void setLibroParameters(PreparedStatement stmt, Libro libro) throws SQLException {
    stmt.setString(1, libro.getTitulo());
    stmt.setString(2, libro.getAutor());
    stmt.setInt(3, libro.getAnioPublicacion());
    stmt.setString(4, libro.getGenero());
    setFichaBibliograficaId(stmt, 5, libro); 
}

//Obtiene el id autogerado por la bd luego del insert
    private void setGeneratedId(PreparedStatement stmt, Libro libro) throws SQLException {
        try (ResultSet rs = stmt.getGeneratedKeys()) {
            if (rs.next()) {
                libro.setId(rs.getLong(1));
            } else {
                throw new SQLException("No se pudo obtener el ID generado para el libro.");
            }
        }
    }

    //Setear la FK de ficha para manejo de null
private void setFichaBibliograficaId(PreparedStatement stmt, int parameterIndex, Libro libro) throws SQLException {
    if (libro.getFichaBibliografica() != null && libro.getFichaBibliografica().getId() != null) {
        stmt.setLong(parameterIndex, libro.getFichaBibliografica().getId());
    } else {
        stmt.setNull(parameterIndex, Types.INTEGER);
    }
}

    
    private Libro mapResultSetToLibro(ResultSet rs) throws SQLException {
        Libro libro = new Libro();
        libro.setId(rs.getLong("id"));
        libro.setTitulo(rs.getString("titulo"));
        libro.setAutor(rs.getString("autor"));
        libro.setAnioPublicacion(rs.getInt("anio_publicacion"));
        libro.setGenero(rs.getString("genero"));

        // Manejo de LEFT JOIN
        long fichaId = rs.getLong("fichabibliografica_id");
        if (fichaId > 0 && !rs.wasNull()) {
            FichaBibliografica ficha = new FichaBibliografica();
            ficha.setId(fichaId);
            ficha.setIsbn(rs.getString("isbn"));
            libro.setFichaBibliografica(ficha);
        }

        return libro;
    }

}

