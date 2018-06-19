package tikape.runko.domain;
import tikape.runko.domain.RaakaAine;
import tikape.runko.domain.Smoothie;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

public class SmoothieRaakaAine {

    private int smoothieId;
    private int raakaAineId;
    private String ohje;
    private String maara;
    private int jarjestys;
    private String nimi;

    public SmoothieRaakaAine(int smoothieId, int raakaAineId, String ohje, String maara, int jarjestys) {
        this.smoothieId = smoothieId;
        this.raakaAineId = raakaAineId;
        this.ohje = ohje;
        this.maara = maara;
        this.jarjestys = jarjestys;
    }

    public int getRaakaAineId() {
        return raakaAineId;
    }

    public int getSmoothieId() {
        return smoothieId;
    }

    public String getOhje() {
        return ohje;
    }

    public String getMaara() {
        return maara;
    }

    public int getJarjestys() {
        return jarjestys;
    }

    public void setNimi(String nimi) {
        this.nimi = nimi;
    }

    public String getNimi() {
        return nimi;
    }
}