package prog2int.Main;

public class MenuDisplay {
    /**
     * Muestra el menú principal con todas las opciones CRUD.
     * No lee entradas del usuario.
     * Nota: Los números de opción corresponden al switch en AppMenu.processOption().
     */
    public static void mostrarMenuPrincipal() {
        System.out.println("\n========= MENU =========");
        System.out.println("1. Crear libro");
        System.out.println("2. Listar libros");
        System.out.println("3. Actualizar libros");
        System.out.println("4. Eliminar libros");
        System.out.println("5. Crear Ficha Bibliografica");
        System.out.println("6. Listar Fichas Bibliograficas");
        System.out.println("7. Actualizar Ficha Bibliografica por ID");
        System.out.println("8. Eliminar Ficha Bibliografica por ID");
        System.out.println("9. Actualizar Ficha Bibliografica por ID de libro");
        System.out.println("10. Eliminar Ficha Bibliografica por ID de libro");
        System.out.println("0. Salir");
        System.out.print("Ingrese una opcion: ");
    }
}