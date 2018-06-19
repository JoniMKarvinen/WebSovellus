package tikape.runko;

import java.util.HashMap;
import spark.ModelAndView;
import spark.Spark;
import static spark.Spark.*;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import tikape.runko.database.Database;
import tikape.runko.database.RaakaAineDao;
import tikape.runko.domain.RaakaAine;
import tikape.runko.database.SmoothieDao;
import tikape.runko.domain.Smoothie;
import tikape.runko.database.SmoothieRaakaAineDao;
import tikape.runko.domain.SmoothieRaakaAine;

public class Main {
    
    public static void main(String[] args) throws Exception {
        if (System.getenv("PORT") != null) {
            Spark.port(Integer.valueOf(System.getenv("PORT")));
        }
        Database database = new Database("jdbc:sqlite:smoothie.db");
        database.init();

        RaakaAineDao raDao = new RaakaAineDao(database);
        SmoothieDao sDao = new SmoothieDao(database);
        SmoothieRaakaAineDao sraDao = new SmoothieRaakaAineDao(database);
        
        Spark.staticFileLocation("/public");
        get("/", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("viesti", "tervehdys");

            return new ModelAndView(map, "index");
        }, new ThymeleafTemplateEngine());
 
        post("/raaka-aineet", (req, res) -> {
            String nimi = req.queryParams("nimi");
            raDao.saveOrUpdate(new RaakaAine(nimi));
            res.redirect("/raaka-aineet");
            return "";
        });

        get("/raaka-aineet", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("raakaaineet", raDao.findAll());
            return new ModelAndView(map, "raaka-aineet");
        }, new ThymeleafTemplateEngine());
        
        post("/raaka-aineet/delete", (req, res) -> {
            int id = Integer.parseInt(req.queryParams("id"));
            sraDao.raakaAineIdlläpoistaminen(id);
            raDao.delete(id);
            res.redirect("/raaka-aineet");
            return "";
        });
        
        post("/smoothiet", (req, res) -> {
            String nimi = req.queryParams("nimi");
            sDao.saveOrUpdate(new Smoothie(nimi));
            res.redirect("/smoothiet");
            return "";
        });

        get("/smoothiet", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("smoothie", sDao.findAll());
            return new ModelAndView(map, "Smoothiet");
        }, new ThymeleafTemplateEngine());
  
        post("/smoothiet/delete", (req, res) -> {
            int id = Integer.parseInt(req.queryParams("id"));
            sraDao.smoothieIdlläPoistaminen(id);
            sDao.delete(id);
            res.redirect("/smoothiet");
            return "";
        });
        
        Spark.get("/raakaaine/:id", (req, res) -> {
            int id = Integer.parseInt(req.params("id"));
            HashMap map = new HashMap<>();
            map.put("raakaaine", raDao.findOne(id));
            map.put("maara", sraDao.smoothietJoissaRaakaAine(id));
            return new ModelAndView(map, "RaakaAineInfo");
        }, new ThymeleafTemplateEngine());        
        
        Spark.post("/smoothie/smoothieraakaaine/delete", (req, res) -> {
            int raId = Integer.parseInt(req.queryParams("raakaAineId"));
            int sId = Integer.parseInt(req.queryParams("smoothieId"));
            sraDao.delete(raId, sId);
            res.redirect("/smoothie/" + sId);
            return "";
        });
        
        get("/smoothie/:id", (req, res) -> {
            int id = Integer.parseInt(req.params("id"));
            HashMap map = new HashMap<>();
            map.put("smoothie", sDao.findOne(id));
            map.put("smoothiet", sDao.findAll());
            map.put("raakaaineet", raDao.raakaAineetAakkosjärjestyksessä());
            map.put("RAjarjestys", sraDao.smoothienRaakaAineetJärjestyksessä(id));
            return new ModelAndView(map, "SmoothieInfo");
        }, new ThymeleafTemplateEngine()); 
        
        Spark.post("/smoothie/luo/smoothieraakaaine", (req, res) -> {
            int sId = Integer.parseInt(req.queryParams("smoothieId"));
            int raId = Integer.parseInt(req.queryParams("raakaAineId"));
            String ohje = req.queryParams("ohje").trim();
            String maara = req.queryParams("maara").trim();
            int jarjestys = Integer.parseInt(req.queryParams("jarjestys"));    
            SmoothieRaakaAine sra = new SmoothieRaakaAine(sId, raId, ohje, maara, jarjestys);
            sraDao.saveOrUpdate(sra);
            
            res.redirect("/smoothie/" + sId);
            return "";
        });
    }
}
