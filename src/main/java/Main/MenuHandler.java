package Main;

import Models.CodigoBarras;
import Models.Producto;
import Service.ProductoServiceImpl;

import java.util.List;
import java.util.Scanner;

/**
 * Lógica de interacción por consola para Producto y CodigoBarras.
 * Validación de negocio queda en la capa Service.
 */
public class MenuHandler {
    private final Scanner scanner;
    private final ProductoServiceImpl productoService;

    public MenuHandler(Scanner scanner, ProductoServiceImpl productoService) {
        if (scanner == null) throw new IllegalArgumentException("Scanner no puede ser null");
        if (productoService == null) throw new IllegalArgumentException("ProductoService no puede ser null");
        this.scanner = scanner;
        this.productoService = productoService;
    }

    // ===== Productos =====

    public void crearProducto() {
        try {
            System.out.print("Nombre: ");
            String nombre = scanner.nextLine().trim();

            System.out.print("Precio (usar punto decimal): ");
            String precioStr = scanner.nextLine().trim();

            Double precio = precioStr.isEmpty() ? null : Double.parseDouble(precioStr);

            CodigoBarras codigo = null;
            System.out.print("¿Desea agregar un codigo de barras? (s/n): ");
            if (scanner.nextLine().equalsIgnoreCase("s")) {
                codigo = crearCodigo();
            }

            Producto p = new Producto(0, nombre, precio);
            p.setCodigoBarras(codigo);
            productoService.insertar(p);
            System.out.println("Producto creado. ID: " + p.getId());
        } catch (Exception e) {
            System.err.println("Error al crear producto: " + e.getMessage());
        }
    }

    public void listarProductos() {
        try {
            System.out.print("¿Desea (1) listar todos o (2) buscar por nombre? ");
            int sub = Integer.parseInt(scanner.nextLine());

            List<Producto> productos;
            if (sub == 1) {
                productos = productoService.getAll();
            } else if (sub == 2) {
                System.out.print("Texto a buscar: ");
                String filtro = scanner.nextLine().trim();
                productos = productoService.buscarPorNombre(filtro);
            } else {
                System.out.println("Opcion invalida.");
                return;
            }

            if (productos.isEmpty()) {
                System.out.println("No se encontraron productos.");
                return;
            }

            for (Producto p : productos) {
                System.out.println("ID: " + p.getId() + ", Nombre: " + p.getNombre() +
                        ", Precio: " + (p.getPrecio() == null ? "-" : p.getPrecio()));
                if (p.getCodigoBarras() != null) {
                    System.out.println("   Codigo: " + p.getCodigoBarras().getValor());
                }
            }
        } catch (Exception e) {
            System.err.println("Error al listar productos: " + e.getMessage());
        }
    }

    public void actualizarProducto() {
        try {
            System.out.print("ID del producto a actualizar: ");
            int id = Integer.parseInt(scanner.nextLine());
            Producto p = productoService.getById(id);
            if (p == null) {
                System.out.println("Producto no encontrado.");
                return;
            }

            System.out.print("Nuevo nombre (actual: " + p.getNombre() + ", Enter para mantener): ");
            String nombre = scanner.nextLine().trim();
            if (!nombre.isEmpty()) p.setNombre(nombre);

            System.out.print("Nuevo precio (actual: " + (p.getPrecio() == null ? "-" : p.getPrecio()) + ", Enter para mantener): ");
            String precioStr = scanner.nextLine().trim();
            if (!precioStr.isEmpty()) p.setPrecio(Double.parseDouble(precioStr));

            actualizarCodigoDe(p);
            productoService.actualizar(p);
            System.out.println("Producto actualizado.");
        } catch (Exception e) {
            System.err.println("Error al actualizar producto: " + e.getMessage());
        }
    }

    public void eliminarProducto() {
        try {
            System.out.print("ID del producto a eliminar: ");
            int id = Integer.parseInt(scanner.nextLine());
            productoService.eliminar(id);
            System.out.println("Producto eliminado.");
        } catch (Exception e) {
            System.err.println("Error al eliminar producto: " + e.getMessage());
        }
    }

    // ===== Codigos de barras (independientes) =====

    public void crearCodigoIndependiente() {
        try {
            CodigoBarras c = crearCodigo();
            productoService.getCodigoBarrasService().insertar(c);
            System.out.println("Codigo creado. ID: " + c.getId());
        } catch (Exception e) {
            System.err.println("Error al crear codigo: " + e.getMessage());
        }
    }

    public void listarCodigos() {
        try {
            List<CodigoBarras> codigos = productoService.getCodigoBarrasService().getAll();
            if (codigos.isEmpty()) {
                System.out.println("No se encontraron codigos.");
                return;
            }
            for (CodigoBarras c : codigos) {
                System.out.println("ID: " + c.getId() + ", Valor: " + c.getValor());
            }
        } catch (Exception e) {
            System.err.println("Error al listar codigos: " + e.getMessage());
        }
    }

    public void actualizarCodigoPorId() {
        try {
            System.out.print("ID del codigo a actualizar: ");
            int id = Integer.parseInt(scanner.nextLine());
            CodigoBarras c = productoService.getCodigoBarrasService().getById(id);
            if (c == null) {
                System.out.println("Codigo no encontrado.");
                return;
            }
            System.out.print("Nuevo valor (" + c.getValor() + "): ");
            String valor = scanner.nextLine().trim();
            if (!valor.isEmpty()) c.setValor(valor);
            productoService.getCodigoBarrasService().actualizar(c);
            System.out.println("Codigo actualizado.");
        } catch (Exception e) {
            System.err.println("Error al actualizar codigo: " + e.getMessage());
        }
    }

    public void eliminarCodigoPorId() {
        try {
            System.out.print("ID del codigo a eliminar: ");
            int id = Integer.parseInt(scanner.nextLine());
            productoService.getCodigoBarrasService().eliminar(id);
            System.out.println("Codigo eliminado.");
        } catch (Exception e) {
            System.err.println("Error al eliminar codigo: " + e.getMessage());
        }
    }

    // ===== Asociación Producto ↔ CodigoBarras =====

    public void actualizarCodigoDeProducto() {
        try {
            System.out.print("ID del producto: ");
            int productoId = Integer.parseInt(scanner.nextLine());
            Producto p = productoService.getById(productoId);
            if (p == null) {
                System.out.println("Producto no encontrado.");
                return;
            }
            if (p.getCodigoBarras() == null) {
                System.out.println("El producto no tiene codigo asociado.");
                System.out.print("¿Desea agregar uno? (s/n): ");
                if (scanner.nextLine().equalsIgnoreCase("s")) {
                    CodigoBarras nuevo = crearCodigo();
                    productoService.getCodigoBarrasService().insertar(nuevo);
                    p.setCodigoBarras(nuevo);
                    productoService.actualizar(p);
                    System.out.println("Codigo agregado al producto.");
                }
                return;
            }

            CodigoBarras c = p.getCodigoBarras();
            System.out.print("Nuevo valor (" + c.getValor() + "): ");
            String valor = scanner.nextLine().trim();
            if (!valor.isEmpty()) c.setValor(valor);
            productoService.getCodigoBarrasService().actualizar(c);
            System.out.println("Codigo del producto actualizado.");
        } catch (Exception e) {
            System.err.println("Error al actualizar codigo del producto: " + e.getMessage());
        }
    }

    public void eliminarCodigoDeProducto() {
        try {
            System.out.print("ID del producto: ");
            int productoId = Integer.parseInt(scanner.nextLine());
            Producto p = productoService.getById(productoId);
            if (p == null) {
                System.out.println("Producto no encontrado.");
                return;
            }
            if (p.getCodigoBarras() == null) {
                System.out.println("El producto no tiene codigo asociado.");
                return;
            }
            int codigoId = p.getCodigoBarras().getId();
            productoService.eliminarCodigoDeProducto(productoId, codigoId);
            System.out.println("Codigo eliminado y referencia actualizada.");
        } catch (Exception e) {
            System.err.println("Error al eliminar codigo del producto: " + e.getMessage());
        }
    }

    // ===== Helpers =====

    private CodigoBarras crearCodigo() {
        System.out.print("Valor del codigo de barras: ");
        String valor = scanner.nextLine().trim();
        return new CodigoBarras(0, valor);
    }

    private void actualizarCodigoDe(Producto p) throws Exception {
        if (p.getCodigoBarras() != null) {
            System.out.print("¿Desea actualizar el codigo de barras? (s/n): ");
            if (scanner.nextLine().equalsIgnoreCase("s")) {
                System.out.print("Nuevo valor (" + p.getCodigoBarras().getValor() + "): ");
                String valor = scanner.nextLine().trim();
                if (!valor.isEmpty()) p.getCodigoBarras().setValor(valor);
                productoService.getCodigoBarrasService().actualizar(p.getCodigoBarras());
            }
        } else {
            System.out.print("El producto no tiene codigo. ¿Desea agregar uno? (s/n): ");
            if (scanner.nextLine().equalsIgnoreCase("s")) {
                CodigoBarras nuevo = crearCodigo();
                productoService.getCodigoBarrasService().insertar(nuevo);
                p.setCodigoBarras(nuevo);
            }
        }
    }
}
