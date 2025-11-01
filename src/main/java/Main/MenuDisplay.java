package Main;

/**
 * Muestra el menú principal para Producto y CodigoBarras.
 */
public class MenuDisplay {
    public static void mostrarMenuPrincipal() {
        System.out.println("\n========= MENU =========");
        System.out.println("1. Crear producto");
        System.out.println("2. Listar productos");
        System.out.println("3. Actualizar producto");
        System.out.println("4. Eliminar producto");

        System.out.println("5. Crear codigo de barras");
        System.out.println("6. Listar codigos de barras");
        System.out.println("7. Actualizar codigo por ID");
        System.out.println("8. Eliminar codigo por ID");

        System.out.println("9. Actualizar codigo de un producto");
        System.out.println("10. Eliminar codigo de un producto");

        System.out.println("0. Salir");
        System.out.print("Ingrese una opcion: ");
    }
}
