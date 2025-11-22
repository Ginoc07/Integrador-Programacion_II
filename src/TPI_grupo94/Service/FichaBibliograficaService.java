package TPI_grupo94.Service;

import TPI_grupo94.Dao.FichaBibliograficaDAO;
import TPI_grupo94.Models.FichaBibliografica;
import TPI_grupo94.Config.DatabaseConnection;

import java.sql.Connection;
import java.util.List;

public class FichaBibliograficaService implements GenericService<FichaBibliografica> {

    private final FichaBibliograficaDAO fichaDAO;

    // Corrección: nombre del constructor igual al de la clase
    public FichaBibliograficaService(FichaBibliograficaDAO fichaDAO) {
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
            if (conn != null) {
                try {
                    conn.rollback(); // revertir cambios si hay error
                } catch (Exception rollbackEx) {
                    // Log del error de rollback
                }
            }
            throw new Exception("Error al insertar ficha bibliográfica: " + e.getMessage(), e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (Exception closeEx) {
                    // Log del error de cierre
                }
            }
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
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (Exception rollbackEx) {
                    // Log del error de rollback
                }
            }
            throw new Exception("Error al actualizar ficha bibliográfica: " + e.getMessage(), e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (Exception closeEx) {
                    // Log del error de cierre
                }
            }
        }
    }

    @Override
    public void eliminar(Long id) throws Exception {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            fichaDAO.eliminar(id, conn);

            conn.commit();
        } catch (Exception e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (Exception rollbackEx) {
                    // Log del error de rollback
                }
            }
            throw new Exception("Error al eliminar ficha bibliográfica: " + e.getMessage(), e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (Exception closeEx) {
                    // Log del error de cierre
                }
            }
        }
    }

    @Override
    public FichaBibliografica getById(Long id) throws Exception {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            return fichaDAO.leer(id, conn);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (Exception e) {
                    // Log del error de cierre
                }
            }
        }
    }

    @Override
    public List<FichaBibliografica> getAll() throws Exception {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            return fichaDAO.leerTodos(conn);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (Exception e) {
                    // Log del error de cierre
                }
            }
        }
    }
}