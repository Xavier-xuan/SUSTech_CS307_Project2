package SUSTech_CS307_Project2;

import javax.xml.crypto.Data;
import SUSTech_CS307_Project2.DatabaseManipulation;

public class TEST {
    public static void main(String[] args) {
        DatabaseManipulation dm = new DatabaseManipulation("127.0.0.1:5432/postgres", "postgres", "postgres");
    }
}
