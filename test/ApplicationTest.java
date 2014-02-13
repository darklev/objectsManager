import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.avaje.ebean.Ebean;
import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.EbeanServerFactory;
import com.avaje.ebean.config.ServerConfig;
import com.fasterxml.jackson.databind.JsonNode;

import models.*;

import org.junit.*;

import play.mvc.*;
import play.test.*;
import play.data.DynamicForm;
import play.data.validation.ValidationError;
import play.data.validation.Constraints.RequiredValidator;
import play.i18n.Lang;
import play.libs.F;
import play.libs.F.*;

import static play.test.Helpers.*;
import static org.fest.assertions.Assertions.*;

import models.*;

/**
*
* Simple (JUnit) tests that can call all parts of a play app.
* If you are interested in mocking a whole application, see the wiki for more details.
*
*/
public class ApplicationTest {

    @Test
    public void simpleCheck() {
        int a = 1 + 1;
        assertThat(a).isEqualTo(2);
    }

    @Test
    public void renderTemplate() {
        Content html = views.html.index.render("Your new application is ready.");
        assertThat(contentType(html)).isEqualTo("text/html");
        assertThat(contentAsString(html)).contains("Your new application is ready.");
    }

    @Test
    public void nomObjet(){
		Objet o = new Objet("Arme",15);
		assertThat(o.getNom()).isEqualTo("Arme");
    }
    
    @Test
    public void prixObjet(){
		Objet o = new Objet("Ventilateur",12);
		assertThat(o.getPrix()).isEqualTo(12);
	}
	
	@Test
	public void equipableObjet()
	{
		Objet o = new Objet("Gâteau", 5, false);
		assertThat(o.estEquipable()).isEqualTo(false);
		o = new Objet("Epée", 10, true);
		assertThat(o.estEquipable()).isEqualTo(true);
	}
	
	@Test
	public void testEtatObjet()
	{
		Objet o = new Objet("Arbre",10);
		assertThat(o.getEtat()).isEqualTo(Objet.INTACT);
		o.setEtat(Objet.COUPE);
		assertThat(o.getEtat()).isEqualTo(Objet.COUPE);
	}
	
	@Test
	public void testActionObjet()
	{
		Objet hache = new Objet("Hache",15);
		Objet arbre = new Objet("Arbre",10);
		
		boolean succes = hache.doAction(arbre);
		
		assertThat(succes).isEqualTo(true);
		assertThat(arbre.getEtat()).isEqualTo(Objet.COUPE);
	
		Objet tap = new Objet("Tarte au pomme",15);
		Objet arbre2 = new Objet("Arbre",10);
		succes = tap.doAction(arbre2);
		
		assertThat(succes).isEqualTo(false);
		assertThat(arbre2.getEtat()).isEqualTo(Objet.INTACT);
	}
	
	@Test
	public void testPotion()
	{
		ArrayList<Effet> effets = new ArrayList<Effet>();
		effets.add(new Effet(Effet.SOIN, 10, 0));
		Potion p = new Potion("Potion de soin",10, effets);
		Personne perso = new Personne("Test",2,100);
		perso.updatePV(-20); //On fait perdre 20 pv au personnage.
		
		p.doAction(perso);
		assertThat(perso.getPV()).isEqualTo(90);
		
		
	}


    @Test
	public void TailleInventaire(){
		Inventaire i = new Inventaire(20);
		assertThat(i.getTailleInventaire()).isEqualTo(20);
    }
    @Test
	public void NomObjetInventaire(){
		Inventaire i= new Inventaire(20);
		i.addObjet("objet",200,true,0);
		assertThat(i.getObjetInventaire(0).getNom()).isEqualTo("objet");
    }
    
    @Test
	public void NomObjetInventaire2(){
		Inventaire i= new Inventaire(20);
		i.addObjet("objet2",200,true,1);
		assertThat(i.getObjetInventaire(1).getNom()).isEqualTo("objet2");
    }
    @Test
	public void AjouterObjetInventaire(){
		Inventaire i= new Inventaire(20);
		i.addObjet("objet3",200,true,2);
		assertThat(i.getObjetInventaire(2).getNom()).isEqualTo("objet3");
    }
   
    @Test
	public void SupprimerObjetInventaire(){
		Inventaire i= new Inventaire(20);
		i.addObjet("objet3",200,true,2);
		i.delObjet(2);
		assertThat(i.getObjetInventaire(2)).isNull();
    }
    
    @Test
	public void DeplacerObjetInventaire(){
		Inventaire i= new Inventaire(20);
		i.addObjet("objet1",200,true,0);
		i.addObjet("objet2",400,false,1);
		i.addObjet("objet3",230,true,2);
		i.deplacerObjet(2,1);

		assertThat(i.getObjetInventaire(1).getNom()).isEqualTo("objet3");
    }

	
   @Test
    public void personne(){
		Personne p = new Personne("Bob", 10);
		assertThat(p.getNom()).isEqualTo("Bob");
		assertThat(p.getInventaire().getTailleInventaire()).isEqualTo(10);
	}	

	@Before
	public void deleteAll()
	{
		//Fixtures.deleteAll();
	}
	
	@Test
	public void testPersistanceObjet()
	{
		Objet o = new Personne("Tarte au citron",10);
		o.save();
		System.out.println(Ebean.getServer("default").toString());
		assertThat(((Objet)Ebean.getServer("default").find(Objet.class).where("nom LIKE \"Tarte au citron\"")).getNom()).isEqualTo("Tarte au citron");
	}

}
