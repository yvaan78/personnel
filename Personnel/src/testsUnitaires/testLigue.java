package testsUnitaires;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;

import personnel.*;

class testLigue 
{
	GestionPersonnel gestionPersonnel = GestionPersonnel.getGestionPersonnel();
	
	@Test
	void createLigue() throws SauvegardeImpossible
	{
		Ligue ligue = gestionPersonnel.addLigue("Fléchettes");
		assertEquals("Fléchettes", ligue.getNom());
	}

	@Test
	void addEmploye() throws SauvegardeImpossible
	{
		Ligue ligue = gestionPersonnel.addLigue("Fléchettes");
		Employe employe = ligue.addEmploye("Bouchard", "Gérard", "g.bouchard@gmail.com", "azerty"); 
		assertEquals(employe, ligue.getEmployes().first());
	}

	// Tests unitaires pour les dates d'arrivée et départ
	// Pitié faites que ca marche

	@Test
	void testDatesEmployeValides() throws SauvegardeImpossible {
	    Ligue ligue = gestionPersonnel.addLigue("Tennis");
	    Employe employe = ligue.addEmploye("Aka-bile", "Daniel", "dnl78@mail.com", "j'aimetropmameuf");

	    LocalDate dateArrivee = LocalDate.of(2025, 12, 1);
	    LocalDate dateDepart = LocalDate.of(2025, 12, 10);

	    try {
	        employe.setDateArrivee(dateArrivee);
	        employe.setDateDepart(dateDepart);
	    } catch (Exception e) {
	        fail("Exception non attendue pour des dates valides");
	    }

	    assertEquals(dateArrivee, employe.getDateArrivee());
	    assertEquals(dateDepart, employe.getDateDepart());
	}

	@Test
	void testDateDepartAvantArrivee() throws SauvegardeImpossible {
	    Ligue ligue = gestionPersonnel.addLigue("football");
	    Employe employe = ligue.addEmploye("Boya", "Yvan", "yvan@mail.com", "sonicthehedgehog");

	    LocalDate dateArrivee = LocalDate.of(2025, 12, 10);
	    LocalDate dateDepart = LocalDate.of(2025, 12, 1);

	    try {
	        employe.setDateArrivee(dateArrivee);
	    } catch (Exception e) {
	        fail("Exception non attendue lors du setDateArrivee");
	    }

	    try {
	        employe.setDateDepart(dateDepart);
	        fail("Une exception aurait dû être levée : dateDepart avant dateArrivee");
	    } catch (Exception e) {
	        // si le test réussi, voilà l'exception attendue
	        assertTrue(e.getMessage().contains("La date de départ"));
	    }
	}

	@Test
	void testDateArriveeApresDepart() throws SauvegardeImpossible {
	    Ligue ligue = gestionPersonnel.addLigue("Basket");
	    Employe employe = ligue.addEmploye("Nkamen", "Yanis", "yanis@mail.com", "lebronjames");

	    LocalDate dateDepart = LocalDate.of(2025, 12, 10);
	    LocalDate dateArrivee = LocalDate.of(2025, 12, 15);

	    try {
	        employe.setDateDepart(dateDepart);
	    } catch (Exception e) {
	        fail("Exception non attendue lors du setDateDepart");
	    }

	    try {
	        employe.setDateArrivee(dateArrivee);
	        fail("Une exception aurait dû être levée : dateArrivee après dateDepart");
	    } catch (Exception e) {
	        // Test réussi, exception attendue
	        assertTrue(e.getMessage().contains("La date d'arrivée"));
	    }
	}
}
