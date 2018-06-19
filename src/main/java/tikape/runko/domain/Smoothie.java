package tikape.runko.domain;

public class Smoothie {

    private String nimi;
    private int id;

    public Smoothie(String nimi) {
        this.nimi = nimi;
    }
    
    public Smoothie(int id, String nimi) {
        this.nimi = nimi;
        this.id = id;
    }

    public String getNimi() {
        return nimi;
    }

    public void setNimi(String nimi) {
        this.nimi = nimi;
    }

    public int getId() {
        return id;
    }
    
    
}