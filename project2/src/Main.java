import main.DatabaseManipulation;

public class Main {
    public static void main(String[] args) {
        DatabaseManipulation dm = new DatabaseManipulation("127.0.0.1:5432/postgres", "postgres", "postgres");
    }
}