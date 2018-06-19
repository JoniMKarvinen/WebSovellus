package tikape.runko.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import tikape.runko.domain.RaakaAine;

public class RaakaAineDao implements Dao<RaakaAine, Integer> {

    private Database database;

    public RaakaAineDao(Database database) {
        this.database = database;
    }

    @Override
    public RaakaAine findOne(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM RaakaAine WHERE id = ?");
        stmt.setObject(1, key);

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

        Integer id = rs.getInt("id");
        String nimi = rs.getString("nimi");

        RaakaAine RA = new RaakaAine(nimi);

        rs.close();
        stmt.close();
        connection.close();

        return RA;
    }

    @Override
    public List<RaakaAine> findAll() throws SQLException {

        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM RaakaAine");

        ResultSet rs = stmt.executeQuery();
        List<RaakaAine> raakaAineet = new ArrayList<>();
        while (rs.next()) {
            String nimi = rs.getString("nimi");
            int id = rs.getInt("id");
            raakaAineet.add(new RaakaAine(id, nimi));
        }

        rs.close();
        stmt.close();
        connection.close();

        return raakaAineet;
    }
    
    public boolean onkoJoOlemassa (RaakaAine ra) throws SQLException{
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM RaakaAine WHERE nimi = ?");
        stmt.setString(1, ra.getNimi());
        ResultSet rs = stmt.executeQuery();
        if (rs.next()){
            stmt.close();
            connection.close();
            return true;
        }
        stmt.close();
        connection.close();
        return false;
    }

    
        public RaakaAine saveOrUpdate(RaakaAine raakaAine) throws SQLException {
        if (raakaAine.getNimi().length() == 0 || onkoJoOlemassa(raakaAine)){
            return null;
        }
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("INSERT INTO RaakaAine"
                + " (nimi)"
                + " VALUES (?)");
        stmt.setString(1, raakaAine.getNimi());
        stmt.executeUpdate();
        stmt.close();
        connection.close();
        return null;
    }
    
    @Override
    public void delete(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("DELETE FROM RaakaAine WHERE id = ?");
        stmt.setInt(1, key);
        stmt.executeUpdate();
        stmt.close();
        connection.close();
    }
    
    public List<RaakaAine> raakaAineetAakkosjärjestyksessä() throws SQLException {
        try {
            Connection connection = database.getConnection();
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM RaakaAine ORDER BY nimi");
            ResultSet rs = stmt.executeQuery();
            List<RaakaAine> raakaAineet = new ArrayList<>();
            while(rs.next()) {
                RaakaAine ra = new RaakaAine(rs.getInt("id"), rs.getString("nimi"));
                raakaAineet.add(ra);
            }
            rs.close();
            stmt.close();
            connection.close();
            return raakaAineet;
        } catch (Exception ex) {
            System.out.println("Error >> " + ex);
        }        
        return new ArrayList<>();
    }

}