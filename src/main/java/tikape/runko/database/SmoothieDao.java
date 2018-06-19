package tikape.runko.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import tikape.runko.domain.Smoothie;

public class SmoothieDao implements Dao<Smoothie, Integer> {

    private Database database;

    public SmoothieDao(Database database) {
        this.database = database;
    }

    @Override
    public Smoothie findOne(Integer key) throws SQLException {
        
        try{
            Connection connection = database.getConnection();
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Smoothie WHERE id = ?");
            stmt.setObject(1, key);
            ResultSet rs = stmt.executeQuery();
            boolean hasOne = rs.next();
            if (!hasOne) {
                return null;
            }

            Integer id = rs.getInt("id");
            String nimi = rs.getString("nimi");

            Smoothie S = new Smoothie(id, nimi);

            rs.close();
            stmt.close();
            connection.close();

            return S;
            
        } catch (Exception ex) {
            System.out.println("Error >> " + ex);
        }
        return null;
    }

    @Override
    public List<Smoothie> findAll() throws SQLException {
        try {
            Connection connection = database.getConnection();
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Smoothie");

            ResultSet rs = stmt.executeQuery();
            List<Smoothie> Smoothiet = new ArrayList<>();
            while (rs.next()) {
                String nimi = rs.getString("nimi");
                int id = rs.getInt("id");
                Smoothiet.add(new Smoothie(id, nimi));
            }

            rs.close();
            stmt.close();
            connection.close();

            return Smoothiet;
        }catch (Exception ex){
            System.out.println("Error >> " + ex);
        }
        return null;
    }

    
        public Smoothie saveOrUpdate(Smoothie smoothie) throws SQLException {
        if (smoothie.getNimi().length() == 0){
            return null;
        }
        try {
            Connection conn = database.getConnection();
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO Smoothie"
                    + " (nimi)"
                    + " VALUES (?)");
            stmt.setString(1, smoothie.getNimi());


            stmt.executeUpdate();
            stmt.close();
            conn.close();
            return null;
        }catch (Exception ex){
            System.out.println("Error >> " + ex);
        }
        return null;
    }
    
    @Override
    public void delete(Integer key) throws SQLException {
        try {
            Connection connection = database.getConnection();
            PreparedStatement stmt = connection.prepareStatement("DELETE FROM Smoothie WHERE id = ?");
            stmt.setInt(1, key);
            stmt.executeUpdate();
            stmt.close();
            connection.close();
        }catch (Exception ex){
            System.out.println("Error >> " + ex);
        }
        
    }
    
        public List<Smoothie> SmoothietAkkosjärjestyksessä() throws SQLException {
        try {
            Connection connection = database.getConnection();
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Smoothie " + "ORDER BY nimi");
            ResultSet rs = stmt.executeQuery();
            List<Smoothie> smoothiet = new ArrayList<>();
            if(rs.next()) {
                Smoothie s = new Smoothie(rs.getInt("id"), rs.getString("nimi"));
                smoothiet.add(s);
            }
            rs.close();
            stmt.close();
            connection.close();
            return smoothiet;
            
        } catch (Exception ex) {
            System.out.println("Error >> " + ex);
        }        
        return new ArrayList<>();
    }
}