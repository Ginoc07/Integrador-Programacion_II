package tpi_grupo94.Models;

import java.util.Objects;

/**
 * Entidad que representa un libro en el sistema.
 * Hereda de Base para obtener id y eliminado.
 *
 * Relación con FichaBibliografica:
 * - Un Libro puede tener 0 o 1 FichaBibliografica.
 * - Relación 1:1 mediante FK en la tabla FichaBibliografica (campo Id_libro).
 *
 * Tabla BD: Libro
 * Campos:
 * - id: INT PRIMARY KEY (heredado de Base)
 * - eliminado: BOOLEAN DEFAULT FALSE (heredado de Base)
 */
public class Libro extends Base {

    /** Ficha bibliográfica asociada al libro. Puede ser null. */
    private FichaBibliografica fichaBibliografica;

    /** Constructor por defecto */
    public Libro() {
        super();
    }

    /** Constructor para reconstruir desde la BD */
    public Libro(int id) {
        super(id, false);
    }

    public FichaBibliografica getFichaBibliografica() {
        return fichaBibliografica;
    }

    public void setFichaBibliografica(FichaBibliografica fichaBibliografica) {
        this.fichaBibliografica = fichaBibliografica;
    }

    @Override
    public String toString() {
        return "Libro{" +
                "id=" + getId() +
                ", eliminado=" + isEliminado() +
                ", fichaBibliografica=" + (fichaBibliografica != null ? fichaBibliografica.getId() : "null") +
                '}';
    }

    /** Igualdad basada en el ID del libro */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Libro)) return false;
        Libro libro = (Libro) o;
        return getId() == libro.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}