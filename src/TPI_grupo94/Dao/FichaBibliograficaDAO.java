package TPI_grupo94.Dao;

import TPI_grupo94.Models.FichaBibliografica;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FichaBibliograficaDAO {

    private static final String INSERT_SQL =
        "INSERT INTO fichas_bibliograficas (id_libro, titulo, autor, anio, editorial, isbn) VALUES (?, ?, ?, ?, ?, ?)";

    private static final String UPDATE_SQL =
        "UPDATE fichas_bibliograficas SET titulo = ?, autor = ?, anio = ?, editorial = ?, isbn = ? WHERE id = ?";

    private static final String DELETE_SQL =
        "UPDATE fichas_bibliograficas SET eliminado = TRUE WHERE id = ?";

    private static final String SELECT_BY_ID_SQL =
        "SELECT * FROM fichas_bibliograficas WHERE id = ? AND eliminado = FALSE";

    private static final String SELECT_ALL_SQL =
        "SELECT * FROM fichas_bibliograficas WHERE eliminado = FALSE";

    private static final String SELECT_BY_LIBRO_SQL =
        "SELECT * FROM fichas_bibliograficas WHERE id_libro = ? AND eliminado = FALSE";

    public void crear(FichaBibliografica ficha, Connection conn) throws Exception {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS);
            setFichaParameters(stmt, ficha);
            stmt.executeUpdate();
            
            rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                ficha.setId(rs.getLong(1));
            } else {
                throw new SQLException("No se pudo obtener el ID generado para la ficha bibliográfica.");
            }
        } finally {
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
        }
    }

    public void actualizar(FichaBibliografica ficha, Connection conn) throws Exception {
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(UPDATE_SQL);
            stmt.setString(1, ficha.getTitulo());
            stmt.setString(2, ficha.getAutor());
            stmt.setShort(3, ficha.getAnio());
            stmt.setString(4, ficha.getEditorial());
            stmt.setString(5, ficha.getIsbn());
            stmt.setLong(6, ficha.getId());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("No se pudo actualizar la ficha bibliográfica con ID: " + ficha.getId());
            }
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    // Log error
                }
            }
        }
    }

    public void eliminar(Long id, Connection conn) throws Exception {
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(DELETE_SQL);
            stmt.setLong(1, id);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("No se encontró ficha bibliográfica con ID: " + id);
            }
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    // Log error
                }
            }
        }
    }

    public FichaBibliografica leer(Long id, Connection conn) throws Exception {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement(SELECT_BY_ID_SQL);
            stmt.setLong(1, id);
            rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToFicha(rs);
            }
            return null;
        } finally {
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
        }
    }

    public List<FichaBibliografica> leerTodos(Connection conn) throws Exception {
        List<FichaBibliografica> fichas = new ArrayList<FichaBibliografica>();
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(SELECT_ALL_SQL);

            while (rs.next()) {
                fichas.add(mapResultSetToFicha(rs));
            }
            return fichas;
        } finally {
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
        }
    }

    public FichaBibliografica leerPorLibro(Long idLibro, Connection conn) throws Exception {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement(SELECT_BY_LIBRO_SQL);
            stmt.setLong(1, idLibro);
            rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToFicha(rs);
            }
            return null;
        } finally {
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
        }
    }

    private void setFichaParameters(PreparedStatement stmt, FichaBibliografica ficha) throws SQLException {
        if (ficha.getIdLibro() != null) {
            stmt.setLong(1, ficha.getIdLibro());
        } else {
            stmt.setNull(1, Types.BIGINT);
        }
        stmt.setString(2, ficha.getTitulo());
        stmt.setString(3, ficha.getAutor());
        stmt.setShort(4, ficha.getAnio());
        stmt.setString(5, ficha.getEditorial());
        stmt.setString(6, ficha.getIsbn());
    }

    private FichaBibliografica mapResultSetToFicha(ResultSet rs) throws SQLException {
        FichaBibliografica ficha = new FichaBibliografica();
        ficha.setId(rs.getLong("id"));
        ficha.setIdLibro(rs.getLong("id_libro"));
        if (rs.wasNull()) {
            ficha.setIdLibro(null);
        }
        ficha.setTitulo(rs.getString("titulo"));
        ficha.setAutor(rs.getString("autor"));
        ficha.setAnio(rs.getShort("anio"));
        ficha.setEditorial(rs.getString("editorial"));
        ficha.setIsbn(rs.getString("isbn"));
        ficha.setEliminado(rs.getBoolean("eliminado"));
        return ficha;
    }
}


