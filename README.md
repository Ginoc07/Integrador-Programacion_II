# Integrador-Programacion_II



Programacion II Trabajo Final Integrador (TFI) - Grupo 94

Fecha de entrega: 17/11/2025

Link del video: -------

Integrantes: 

Gino Canevaro – Comisión 6 
/
Daiana Ghisio – Comisión 7
/
Nicolás Otaño – Comisión 14
/
Araceli Cabañas – Comisión 5 

- Descripción del dominio elegido. El proyecto modela un sistema de gestión para una Librería, basándose en los siguientes modelos:

El foco principal: Relación 1 -> 1 unidireccional entre Libro y FichaBibliografica. Cada libro conoce a una ficha única que detalla su información técnica.

## Requisitos

- Java JDK 17 o superior
- Apache NetBeans (opcional, pero recomendado)
- MySQL Server o cualquier gestor compatible con el archivo `.sql` provisto

## Creación de la base de datos

1. Abrí tu gestor de base de datos (por ejemplo, MySQL Workbench).
2. Ejecutá el script `inventario.sql` que se encuentra en la carpeta `/bd`.
3. Verificá que se hayan creado las tablas correctamente.
4. Configurá el archivo de conexión (por ejemplo, `Conexion.java`) con los datos de tu servidor:
   ```java
   String url = "jdbc:mysql://localhost:3306/inventario";
   String user = "root";
   String password = "tu_clave";


## Compilación y ejecución

1. Abrí el proyecto en NetBeans o tu IDE favorito.
2. Asegurate de tener configurado el JDK y el conector JDBC para MySQL.
3. Compilá el proyecto (`Build Project`).
4. Ejecutá la clase `MainStock.java`.

### Credenciales de prueba

- Usuario: `root`
- Contraseña: `tu_clave`
- Base de datos: `inventario`

### Flujo de uso

- Se cargan productos de ejemplo.
- Se listan todos los productos.
- Se busca un producto por ID.
- Se filtra por categoría.
- Se actualiza el stock de un producto.
- Se muestra el total de unidades en stock.
- Se identifica el producto con mayor stock.
- Se filtran productos disponibles.
- Se listan las categorías disponibles.

------------------------------------------------------------------------------------------------------
