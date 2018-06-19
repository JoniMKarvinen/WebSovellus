package tikape.runko.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {

    private String databaseAddress;

       public Database(String databaseAddress) throws ClassNotFoundException {
        this.databaseAddress = databaseAddress;
    }

    public Connection getConnection() throws Exception{
        String dbUrl = System.getenv("JDBC_DATABASE_URL");
        if (dbUrl != null && dbUrl.length() > 0) {
            return DriverManager.getConnection(dbUrl);
        }

        return DriverManager.getConnection(databaseAddress);
    }

    public void init() {
        List<String> lauseet = postgreLauseet();

        try (Connection conn = getConnection()) {
            Statement st = conn.createStatement();

            for (String lause : lauseet) {
                System.out.println("Running command >> " + lause);
                st.executeUpdate(lause);
            }

        } catch (Throwable t) {
            System.out.println("Error >> " + t.getMessage());
        }
    }

    private List<String> postgreLauseet() {
        ArrayList<String> lista = new ArrayList<>();
        lista.add("CREATE TABLE Smoothie (id SERIAL PRIMARY KEY, nimi VARCHAR(40));");
        lista.add("CREATE TABLE RaakaAine (id SERIAL PRIMARY KEY, nimi VARCHAR(40));");
        lista.add("CREATE TABLE SmoothieRaakaAine (smoothie_id integer, raakaaine_id integer, jarjestys integer, maara varchar(40), ohje VARCHAR(100));");
        return lista;
    }
}
