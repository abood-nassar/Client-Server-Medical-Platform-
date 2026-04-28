public interface User {
    String name = null;
    String username = null;
    String password = null;

    void login();
    boolean isloggedIn();
    boolean isloggedIn(String username, String password);
}

