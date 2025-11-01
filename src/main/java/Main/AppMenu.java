package Main;

import Dao.ProductoDAO;
import Dao.CodigoBarrasDAO;
import Service.ProductoServiceImpl;
import Service.CodigoBarrasServiceImpl;

import java.util.Scanner;

/**
 * Orquestador del menú para Producto y CodigoBarras.
 * Ensambla dependencias y ejecuta el loop principal.
 */
public class AppMenu {
    private final Scanner scanner;
    private final MenuHandler menuHandler;
    private boolean running;

    public AppMenu() {
        this.scanner = new Scanner(System.in);
        ProductoServiceImpl productoService = createProductoService();
        this.menuHandler = new MenuHandler(scanner, productoService);
        this.running = true;
    }

    public static void main(String[] args) {
        AppMenu app = new AppMenu();
        app.run();
    }

    public void run() {
        while (running) {
            try {
                MenuDisplay.mostrarMenuPrincipal();
                int opcion = Integer.parseInt(scanner.nextLine());
                processOption(opcion);
            } catch (NumberFormatException e) {
                System.out.println("Entrada invalida. Ingrese un numero.");
            }
        }
        scanner.close();
    }

    private void processOption(int opcion) {
        switch (opcion) {
            case 1 -> menuHandler.crearProducto();
            case 2 -> menuHandler.listarProductos();
            case 3 -> menuHandler.actualizarProducto();
            case 4 -> menuHandler.eliminarProducto();

            case 5 -> menuHandler.crearCodigoIndependiente();
            case 6 -> menuHandler.listarCodigos();
            case 7 -> menuHandler.actualizarCodigoPorId();
            case 8 -> menuHandler.eliminarCodigoPorId();

            case 9 -> menuHandler.actualizarCodigoDeProducto();
            case 10 -> menuHandler.eliminarCodigoDeProducto();

            case 0 -> {
                System.out.println("Saliendo...");
                running = false;
            }
            default -> System.out.println("Opcion no valida.");
        }
    }

    /**
     * Construye la cadena DAO → Service para Producto/CodigoBarras.
     */
    private ProductoServiceImpl createProductoService() {
        CodigoBarrasDAO codigoDAO = new CodigoBarrasDAO();
        ProductoDAO productoDAO = new ProductoDAO(codigoDAO);
        CodigoBarrasServiceImpl codigoService = new CodigoBarrasServiceImpl(codigoDAO);
        return new ProductoServiceImpl(productoDAO, codigoService);
    }
}
