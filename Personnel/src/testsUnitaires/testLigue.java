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
    
    @Test
    void testDatesEmploye() throws SauvegardeImpossible
    {
        Ligue ligue = gestionPersonnel.addLigue("Fléchettes");
        Employe employe = ligue.addEmploye("Bouchard", "Gérard", "g.bouchard@gmail.com", "azerty");
        
        LocalDate arrivee = LocalDate.of(2020, 1, 1);
        LocalDate depart = LocalDate.of(2023, 12, 31);
        
        employe.setDateArrivee(arrivee);
        employe.setDateDepart(depart);
        
        assertEquals(arrivee, employe.getDateArrivee());
        assertEquals(depart, employe.getDateDepart());
    }
    
    @Test
    void testDateArriveeApresDepart() throws SauvegardeImpossible
    {
        Ligue ligue = gestionPersonnel.addLigue("Fléchettes");
        Employe employe = ligue.addEmploye("Bouchard", "Gérard", "g.bouchard@gmail.com", "azerty");
        
        LocalDate arrivee = LocalDate.of(2023, 12, 31);
        LocalDate depart = LocalDate.of(2020, 1, 1);
        
        employe.setDateArrivee(arrivee);
        
        assertThrows(IllegalArgumentException.class, () -> {
            employe.setDateDepart(depart);
        });
    }
    
    @Test
    void testEstEnFonction() throws SauvegardeImpossible
    {
        Ligue ligue = gestionPersonnel.addLigue("Fléchettes");
        Employe employe = ligue.addEmploye("Bouchard", "Gérard", "g.bouchard@gmail.com", "azerty");
        
        // Sans dates, devrait être en fonction
        assertTrue(employe.estEnFonction());
        
        // Avec date d'arrivée future
        LocalDate future = LocalDate.now().plusYears(1);
        employe.setDateArrivee(future);
        assertFalse(employe.estEnFonction());
        
        // Avec date de départ passée
        LocalDate passee = LocalDate.now().minusYears(1);
        employe.setDateArrivee(LocalDate.now().minusYears(2));
        employe.setDateDepart(passee);
        assertFalse(employe.estEnFonction());
    }
}
