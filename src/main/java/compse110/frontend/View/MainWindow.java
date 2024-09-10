package compse110.frontend.View;

public class MainWindow {

    /**
     * Global singleton object
     */
    private static MainWindow mainWindow;

    private MainWindow() {
        //
    }

    /**
     * Singleton Pattern
     * @return get singleton pattern
     */
    public static MainWindow getInstance() {
        if (mainWindow == null) {
            mainWindow = new MainWindow();
        }
        return mainWindow;
    }

}
