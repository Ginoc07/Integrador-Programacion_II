package tpi_grupo94.Models;

import java.util.Objects;

/**
 * Entidad que representa la ficha bibliográfica de un libro.
 * Hereda de Base para obtener id y eliminado.
 *
 * Relación con Libro:
 * - Una FichaBibliografica pertenece a un único libro (FK Id_libro).
 *
 * Tabla BD: FichaBibliografica
 * Campos:
 * - id: INT PRIMARY KEY (heredado)
 * - titulo: VARCHAR(200)
 * - autor: VARCHAR(200)
 * - anio: SMALLINT NOT NULL CHECK (anio BETWEEN 1450 AND 2025)
 * - editorial: VARCHAR(200)
 * - id_libro: INT NOT NULL UNIQUE (FK a Libro)
 * - eliminado: BOOLEAN DEFAULT FALSE (heredado)
 */
public class FichaBibliografica extends Base {

    private Libro libro;
    private String titulo;
    private String autor;
    private short anio;
    private String editorial;

    /** Constructor por defecto */
    public FichaBibliografica() {
        super();
    }

    /** Constructor completo para reconstruir desde BD */
    public FichaBibliografica(int id, String titulo, String autor, short anio, String editorial) {
        super(id, false);
        this.titulo = titulo;
        this.autor = autor;
        this.anio = anio;
        this.editorial = editorial;
    }

    public Libro getLibro() {
        return libro;
    }

    public void setLibro(Libro libro) {
        this.libro = libro;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public short getAnio() {
        return anio;
    }

    public void setAnio(short anio) {
        this.anio = anio;
    }

    public String getEditorial() {
        return editorial;
    }

    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }

    @Override
    public String toString() {
        return "FichaBibliografica{" +
                "id=" + getId() +
                ", titulo='" + titulo + '\'' +
                ", autor='" + autor + '\'' +
                ", anio=" + anio +
                ", editorial='" + editorial + '\'' +
                ", libroId=" + (libro != null ? libro.getId() : "null") +
                ", eliminado=" + isEliminado() +
                '}';
    }

    /** Igualdad basada en ID, lo cual es correcto porque Id_libro es único */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FichaBibliografica)) return false;
        FichaBibliografica that = (FichaBibliografica) o;
        return getId() == that.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}