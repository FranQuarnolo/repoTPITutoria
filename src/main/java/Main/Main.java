package Main;

/**
 * Punto de entrada alternativo. Delegación a AppMenu.
 */
public class Main {
    public static void main(String[] args) {
        AppMenu app = new AppMenu();
        app.run();
    }
}
