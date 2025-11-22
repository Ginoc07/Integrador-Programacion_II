package TPI_grupo94.Dao;

import TPI_grupo94.Config.DatabaseConnection;
import TPI_grupo94.Models.Libro;
import TPI_grupo94.Models.FichaBibliografica;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LibroDAO {

    private static final String INSERT_SQL = "INSERT INTO libros (titulo, autor, anio_publicacion, genero) VALUES (?, ?, ?, ?)";

    private static final String UPDATE_SQL = "UPDATE libros SET titulo = ?, autor = ?, anio_publicacion = ?, genero = ? WHERE id = ?";

    private static final String DELETE_SQL = "UPDATE libros SET eliminado = TRUE WHERE id = ?";

    private static final String SELECT_BY_ID_SQL = "SELECT * FROM libros WHERE id = ? AND eliminado = FALSE";

    private static final String SELECT_ALL_SQL = "SELECT * FROM libros WHERE eliminado = FALSE";

    private static final String SELECT_BY_AUTOR_SQL = "SELECT * FROM libros WHERE eliminado = FALSE AND autor LIKE ?";

    private static final String SELECT_BY_TITULO_AUTOR_SQL = "SELECT * FROM libros WHERE eliminado = FALSE AND (titulo LIKE ? OR autor LIKE ?)";

    private static final String SELECT_BY_ISBN_SQL = "SELECT l.* FROM libros l INNER JOIN fichas_bibliograficas f ON l.id = f.id_libro WHERE f.isbn = ? AND l.eliminado = FALSE AND f.eliminado = FALSE";

    private final FichaBibliograficaDAO fichaDAO;

    public LibroDAO(FichaBibliograficaDAO fichaDAO) {
        if (fichaDAO == null) {
            throw new IllegalArgumentException("FichaBibliograficaDAO no puede ser null");
        }
        this.fichaDAO = fichaDAO;
    }

    public void insertar(Libro libro) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS);
            setLibroParameters(stmt, libro);
            stmt.executeUpdate();
            
            rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                libro.setId(rs.getLong(1));
            } else {
                throw new SQLException("No se pudo obtener el ID generado para el libro.");
            }
        } finally {
            closeResources(rs, stmt, conn);
        }
    }

    public void actualizar(Libro libro) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(UPDATE_SQL);
            setLibroParameters(stmt, libro);
            stmt.setLong(5, libro.getId());
            
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("No se pudo actualizar el libro con ID: " + libro.getId());
            }
        } finally {
            closeResources(null, stmt, conn);
        }
    }

    public void eliminar(Long id) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(DELETE_SQL);
            stmt.setLong(1, id);
            
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("No se encontró libro con ID: " + id);
            }
        } finally {
            closeResources(null, stmt, conn);
        }
    }

    public Libro getById(Long id) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(SELECT_BY_ID_SQL);
            stmt.setLong(1, id);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToLibro(rs);
            }
            return null;
        } finally {
            closeResources(rs, stmt, conn);
        }
    }

    public List<Libro> getAll() throws Exception {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(SELECT_ALL_SQL);
            
            List<Libro> libros = new ArrayList<Libro>();
            while (rs.next()) {
                libros.add(mapResultSetToLibro(rs));
            }
            return libros;
        } finally {
            closeResources(rs, stmt, conn);
        }
    }

    public List<Libro> buscarPorTituloAutor(String filtro) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(SELECT_BY_TITULO_AUTOR_SQL);
            String searchPattern = "%" + filtro + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);
            rs = stmt.executeQuery();
            
            List<Libro> libros = new ArrayList<Libro>();
            while (rs.next()) {
                libros.add(mapResultSetToLibro(rs));
            }
            return libros;
        } finally {
            closeResources(rs, stmt, conn);
        }
    }

    public List<Libro> buscarPorAutor(String autor) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(SELECT_BY_AUTOR_SQL);
            stmt.setString(1, "%" + autor + "%");
            rs = stmt.executeQuery();
            
            List<Libro> libros = new ArrayList<Libro>();
            while (rs.next()) {
                libros.add(mapResultSetToLibro(rs));
            }
            return libros;
        } finally {
            closeResources(rs, stmt, conn);
        }
    }

    public Libro buscarPorIsbn(String isbn) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(SELECT_BY_ISBN_SQL);
            stmt.setString(1, isbn);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToLibro(rs);
            }
            return null;
        } finally {
            closeResources(rs, stmt, conn);
        }
    }

    private void setLibroParameters(PreparedStatement stmt, Libro libro) throws SQLException {
        stmt.setString(1, libro.getTitulo());
        stmt.setString(2, libro.getAutor());
        
        if (libro.getAnioPublicacion() != null) {
            stmt.setInt(3, libro.getAnioPublicacion());
        } else {
            stmt.setNull(3, Types.INTEGER);
        }
        
        stmt.setString(4, libro.getGenero());
    }

    private Libro mapResultSetToLibro(ResultSet rs) throws SQLException {
        Libro libro = new Libro();
        libro.setId(rs.getLong("id"));
        libro.setTitulo(rs.getString("titulo"));
        libro.setAutor(rs.getString("autor"));
        libro.setAnioPublicacion(rs.getInt("anio_publicacion"));
        if (rs.wasNull()) {
            libro.setAnioPublicacion(null);
        }
        libro.setGenero(rs.getString("genero"));
        libro.setEliminado(rs.getBoolean("eliminado"));
        return libro;
    }

    private void closeResources(ResultSet rs, Statement stmt, Connection conn) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                // Log error
            }
        }
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                // Log error
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                // Log error
            }
        }
    }
}


