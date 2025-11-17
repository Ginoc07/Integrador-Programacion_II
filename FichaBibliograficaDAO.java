package Fichaylibro;

import Clases.FichaBibliografica;
import FichaBibliografica;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FichaBibliograficaDAO implements GenericDAO<FichaBibliografica> {

    private static final String INSERT_SQL =
        "INSERT INTO fichas_bibliograficas (editorial, ISBN, idioma, num_paginas, sinopsis) VALUES (?, ?, ?, ?, ?)";

    private static final String UPDATE_SQL =
        "UPDATE fichas_bibliograficas SET editorial = ?, ISBN = ?, idioma = ?, num_paginas = ?, sinopsis = ? WHERE id_ficha = ?";

    private static final String DELETE_SQL =
        "UPDATE fichas_bibliograficas SET eliminado = 1 WHERE id_ficha = ?";

    private static final String SELECT_BY_ID_SQL =
        "SELECT * FROM fichas_bibliograficas WHERE id_ficha = ? AND eliminado = 0";

    private static final String SELECT_ALL_SQL =
        "SELECT * FROM fichas_bibliograficas WHERE eliminado = 0";

    @Override
    public void crear(FichaBibliografica ficha, Connection conn) throws Exception {
        try (PreparedStatement stmt = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            setFichaParameters(stmt, ficha);
            stmt.executeUpdate();
            setGeneratedId(stmt, ficha);
        }
    }

    @Override
    public void actualizar(FichaBibliografica ficha, Connection conn) throws Exception {
        try (PreparedStatement stmt = conn.prepareStatement(UPDATE_SQL)) {
            stmt.setString(1, ficha.getEditorial());
            stmt.setString(2, ficha.getIsbn());
            stmt.setString(3, ficha.getIdioma());
            stmt.setInt(4, ficha.getNroPaginas());
            stmt.setString(5, ficha.getSinopsis());
            stmt.setLong(6, ficha.getId());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("No se pudo actualizar la ficha bibliográfica con ID: " + ficha.getId());
            }
        }
    }

    @Override
    public void eliminar(Long id, Connection conn) throws Exception {
        try (PreparedStatement stmt = conn.prepareStatement(DELETE_SQL)) {
            stmt.setLong(1, id);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("No se encontró ficha bibliográfica con ID: " + id);
            }
        }
    }

    @Override
    public FichaBibliografica leer(Long id, Connection conn) throws Exception {
        try (PreparedStatement stmt = conn.prepareStatement(SELECT_BY_ID_SQL)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToFicha(rs);
                }
            }
        }
        return null;
    }

    @Override
    public List<FichaBibliografica> leerTodos(Connection conn) throws Exception {
        List<FichaBibliografica> fichas = new ArrayList<>();
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_ALL_SQL)) {

            while (rs.next()) {
                fichas.add(mapResultSetToFicha(rs));
            }
        }
        return fichas;
    }

    private void setFichaParameters(PreparedStatement stmt, FichaBibliografica ficha) throws SQLException {
        stmt.setString(1, ficha.getEditorial());
        stmt.setString(2, ficha.getIsbn());
        stmt.setString(3, ficha.getIdioma());
        stmt.setInt(4, ficha.getNroPaginas());
        stmt.setString(5, ficha.getSinopsis());
    }

    private void setGeneratedId(PreparedStatement stmt, FichaBibliografica ficha) throws SQLException {
        try (ResultSet rs = stmt.getGeneratedKeys()) {
            if (rs.next()) {
                ficha.setId(rs.getLong(1));
            } else {
                throw new SQLException("No se pudo obtener el ID generado para la ficha bibliográfica.");
            }
        }
    }

    private FichaBibliografica mapResultSetToFicha(ResultSet rs) throws SQLException {
        FichaBibliografica ficha = new FichaBibliografica();
        ficha.setId(rs.getLong("id_ficha"));
        ficha.setEditorial(rs.getString("editorial"));
        ficha.setIsbn(rs.getString("ISBN"));
        ficha.setIdioma(rs.getString("idioma"));
        ficha.setNroPaginas(rs.getInt("num_paginas"));
        ficha.setSinopsis(rs.getString("sinopsis"));
        return ficha;
    }
}


