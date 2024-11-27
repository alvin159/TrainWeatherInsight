package compse110.Utils;

// ref: https://developer.android.com/reference/android/util/Log
public class Log {

    private final static boolean isDebug = false;

    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_BLACK = "\u001B[30m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_PURPLE = "\u001B[35m";
    private static final String ANSI_CYAN = "\u001B[36m";
    private static final String ANSI_WHITE = "\u001B[37m";

    public static void i(String TAG, String message) {
        if (isDebug) {
            System.out.println(ANSI_BLUE + TAG + " -> Info: " + message + ANSI_RESET);
        }
    }

    public static void i(String message) {
        if (isDebug) {
            System.out.println(ANSI_BLUE + "Info: " + message + ANSI_RESET);
        }
    }

    public static void d(String TAG, String message) {
        if (isDebug) {
            System.out.println(ANSI_CYAN + TAG + " -> Debug: " + message + ANSI_RESET);
        }
    }

    public static void d(String message) {
        if (isDebug) {
            System.out.println(ANSI_CYAN + "Debug: " + message + ANSI_RESET);
        }
    }

    public static void e(String TAG, String message) {
        if (isDebug) {
            System.out.println(ANSI_RED + TAG + " -> Error: " + message + ANSI_RESET);
        }
    }

    public static void e(String message) {
        if (isDebug) {
            System.out.println(ANSI_RED + "Error: " + message + ANSI_RESET);
        }
    }

    public static void w(String TAG, String message) {
        if (isDebug) {
            System.out.println(ANSI_YELLOW + TAG + " -> WARN: " + message + ANSI_RESET);
        }
    }

    public static void w(String message) {
        if (isDebug) {
            System.out.println(ANSI_YELLOW + "WARN: " + message + ANSI_RESET);
        }
    }

    public static void v(String TAG, String message) {
        if (isDebug) {
            System.out.println(ANSI_GREEN + TAG + " -> VERBOSE: " + message + ANSI_RESET);
        }
    }

    public static void v(String message) {
        if (isDebug) {
            System.out.println(ANSI_GREEN + "VERBOSE: " + message + ANSI_RESET);
        }
    }

    public static boolean getIsDebug() {
        return isDebug;
    }

}
