package TPI_grupo94.Service;

import TPI_grupo94.Dao.FichaBibliograficaDAO;
import TPI_grupo94.Models.FichaBibliografica;
import TPI_grupo94.Config.DatabaseConnection;

import java.sql.Connection;
import java.util.List;

public class FichaBibliograficaServiceImpl implements GenericService<FichaBibliografica> {

    private final FichaBibliograficaDAO fichaDAO;

    public FichaBibliograficaServiceImpl(FichaBibliograficaDAO fichaDAO) {
        this.fichaDAO = fichaDAO;
    }

    @Override
    public void insertar(FichaBibliografica ficha) throws Exception {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); // iniciar transacción

            fichaDAO.crear(ficha, conn);

            conn.commit(); // confirmar cambios
        } catch (Exception e) {
            if (conn != null) conn.rollback(); // revertir cambios si hay error
            throw new Exception("Error al insertar ficha bibliográfica: " + e.getMessage(), e);
        } finally {
            if (conn != null) conn.close();
        }
    }

    @Override
    public void actualizar(FichaBibliografica ficha) throws Exception {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            fichaDAO.actualizar(ficha, conn);

            conn.commit();
        } catch (Exception e) {
            if (conn != null) conn.rollback();
            throw new Exception("Error al actualizar ficha bibliográfica: " + e.getMessage(), e);
        } finally {
            if (conn != null) conn.close();
        }
    }

    @Override
    public void eliminar(long id) throws Exception {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            fichaDAO.eliminar(id, conn);

            conn.commit();
        } catch (Exception e) {
            if (conn != null) conn.rollback();
            throw new Exception("Error al eliminar ficha bibliográfica: " + e.getMessage(), e);
        } finally {
            if (conn != null) conn.close();
        }
    }

    @Override
    public FichaBibliografica getById(long id) throws Exception {
        try (Connection conn = DatabaseConnection.getConnection()) {
            return fichaDAO.leer(id, conn);
        }
    }

    @Override
    public List<FichaBibliografica> getAll() throws Exception {
        try (Connection conn = DatabaseConnection.getConnection()) {
            return fichaDAO.leerTodos(conn);
        }
    }
}