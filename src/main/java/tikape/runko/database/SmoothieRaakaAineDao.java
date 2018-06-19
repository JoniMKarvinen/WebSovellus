package tikape.runko.database;

import java.sql.SQLException;
import tikape.runko.domain.Smoothie;
import java.sql.*;
import java.util.*;
import tikape.runko.domain.RaakaAine;
import tikape.runko.domain.SmoothieRaakaAine;

/**
 *
 * @author User
 */
public class SmoothieRaakaAineDao {
    private Database database;
    private String nimi;

    public SmoothieRaakaAineDao(Database database) {
        this.database = database;
    }
    
    public SmoothieRaakaAine findOne(Integer sId, Integer raId) throws SQLException {
        try {
            Connection connection = database.getConnection();
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM SmoothieRaakaAine WHERE Smoothie_id = ? AND RaakaAine_id = ?");
            stmt.setObject(1, raId);
            stmt.setObject(2, sId);
            ResultSet rs = stmt.executeQuery();

            boolean hasOne = rs.next();
            if (!hasOne) {
                return null;
            }

            SmoothieRaakaAine SRA = new SmoothieRaakaAine(rs.getInt("smoothie_id"), rs.getInt("RaakaAine_id"), rs.getString("ohje"), rs.getString("maara"), rs.getInt("jarjestys"));

            rs.close();
            stmt.close();
            connection.close();

            return SRA;
        }catch (Exception ex){
            System.out.println("Error >> " + ex);
        }
        return null;
    }
    
    
    public List<SmoothieRaakaAine> findAll() throws SQLException {
        try {
            Connection connection = database.getConnection();
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM SmoothieRaakaAine");

            ResultSet rs = stmt.executeQuery();
            List<SmoothieRaakaAine> smoothieRaakaAineet = new ArrayList<>();
            while (rs.next()) {
                smoothieRaakaAineet.add(new SmoothieRaakaAine(rs.getInt("smoothie_id"), rs.getInt("RaakaAine_id"), rs.getString("ohje"), rs.getString("maara"), rs.getInt("jarjestys")));
            }

            rs.close();
            stmt.close();
            connection.close();

            return smoothieRaakaAineet;
        }catch (Exception ex){
            System.out.println("Error >> " + ex);
        }
        return null;
    }
    
    public void delete (Integer sId, Integer raId) throws SQLException {
        try{
            Connection connection = database.getConnection();
            PreparedStatement stmt = connection.prepareStatement("DELETE FROM SmoothieRaakaAine WHERE smoothie_id = ? AND raakaaine_id = ?");
            stmt.setObject(1, raId);
            stmt.setObject(2, sId);
            stmt.executeUpdate();
            stmt.close();
            connection.close();
        }catch (Exception ex){
            System.out.println("Error >> " + ex);
        }
    }
    
    public SmoothieRaakaAine saveOrUpdate(SmoothieRaakaAine SRA) throws SQLException {
        try{
            Connection connection = database.getConnection();
            PreparedStatement stmt = connection.prepareStatement("INSERT INTO SmoothieRaakaAine (smoothie_id, raakaaine_id, ohje, maara, jarjestys) VALUES(?, ?, ?, ?, ?)");
            stmt.setInt(1, SRA.getSmoothieId());
            stmt.setInt(2, SRA.getRaakaAineId());
            stmt.setString(3, SRA.getOhje());
            stmt.setString(4, SRA.getMaara());
            stmt.setInt(5, SRA.getJarjestys());

            stmt.executeUpdate();

            stmt.close();
            connection.close();
            return null;
        }catch (Exception ex){
            System.out.println("Error >> " + ex);
        }
        return null;
    }
    
    public int smoothietJoissaRaakaAine(Integer raId) throws SQLException {
         
        try {
            Connection connection = database.getConnection();
            PreparedStatement stmt = connection.prepareStatement("SELECT COUNT(DISTINCT smoothie_id) " + "AS lukumaara FROM SmoothieRaakaAine WHERE raakaaine_id = ?");
            stmt.setInt(1, raId);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            int määrä = rs.getInt("lukumaara");
            rs.close();
            stmt.close();
            connection.close();
            return määrä;
            
        } catch (Exception ex) {
            System.out.println("Error >> " + ex);
        }
        
        return 0;
    }
        
    public List<SmoothieRaakaAine> smoothienRaakaAineetJärjestyksessä(Integer smoothieId) throws SQLException {
        try {
            Connection connection = database.getConnection();
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM  RaakaAine, SmoothieRaakaAine WHERE id = raakaaine_id AND smoothie_id = ? ORDER BY jarjestys");
            stmt.setInt(1, smoothieId);
            ResultSet rs = stmt.executeQuery();
            List<SmoothieRaakaAine> sra_lista = new ArrayList<>();
            while(rs.next()) {
                SmoothieRaakaAine sra = new SmoothieRaakaAine(rs.getInt("smoothie_id"), rs.getInt("raakaaine_id"), rs.getString("ohje"), rs.getString("maara"), rs.getInt("jarjestys"));
                sra.setNimi(rs.getString("nimi"));
                sra_lista.add(sra);
            }
            rs.close();
            stmt.close();
            connection.close();
            return sra_lista;
            
        } catch (Exception ex) {
            System.out.println("Error >> " + ex);
        }        
        return new ArrayList<>();
    }
    
    
    public void smoothieIdlläPoistaminen(Integer sId) throws SQLException {
        
        try {
            Connection connection = database.getConnection();
            PreparedStatement stmt = connection.prepareStatement("DELETE FROM SmoothieRaakaAine WHERE smoothie_id = ?");
            stmt.setInt(1, sId);
            stmt.executeUpdate();
            stmt.close();
            connection.close();
            
        } catch (Exception ex) {
            System.out.println("Error >> " + ex);
        }
        
    }
    
    public void raakaAineIdlläpoistaminen(Integer raId) throws SQLException {
        
        try {
            Connection connection = database.getConnection();
            PreparedStatement stmt = connection.prepareStatement("DELETE FROM SmoothieRaakaAine WHERE raakaaine_id = ?");
            stmt.setInt(1, raId);
            stmt.executeUpdate();
            stmt.close();
            connection.close();
            
        } catch (Exception ex) {
            System.out.println("Error >> " + ex);
        }
        
    }
        
        
    
}
