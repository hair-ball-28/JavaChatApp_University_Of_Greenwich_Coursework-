public class Singleton {
    private static Singleton instance;

    private Singleton() {
        System.out.println("Singleton logger created.");
    }

    public static synchronized Singleton getInstance() {
        if (instance == null) {
            instance = new Singleton();
        }
        return instance;
    }

    public void log(String message) {
        System.out.println("[LOG] " + message);
    }
}

