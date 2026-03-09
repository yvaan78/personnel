package commandLine;

import static commandLineMenus.rendering.examples.util.InOut.getString;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import commandLineMenus.ListOption;
import commandLineMenus.Menu;
import commandLineMenus.Option;
import personnel.Employe;

public class EmployeConsole 
{
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    
    private Option afficher(final Employe employe)
    {
        return new Option("Afficher l'employé", "l", () -> {System.out.println(employe);});
    }

    ListOption<Employe> editerEmploye()
    {
        return (employe) -> editerEmploye(employe);        
    }

    Option editerEmploye(Employe employe)
    {
            Menu menu = new Menu("Gérer le compte " + employe.getNom(), "c");
            menu.add(afficher(employe));
            menu.add(changerNom(employe));
            menu.add(changerPrenom(employe));
            menu.add(changerMail(employe));
            menu.add(changerPassword(employe));
            menu.add(changerDateArrivee(employe));
            menu.add(changerDateDepart(employe));
            menu.add(afficherStatut(employe));
            menu.addBack("q");
            return menu;
    }

    private Option changerNom(final Employe employe)
    {
        return new Option("Changer le nom", "n", 
                () -> {employe.setNom(getString("Nouveau nom : "));}
            );
    }
    
    private Option changerPrenom(final Employe employe)
    {
        return new Option("Changer le prénom", "p", () -> {employe.setPrenom(getString("Nouveau prénom : "));});
    }
    
    private Option changerMail(final Employe employe)
    {
        return new Option("Changer le mail", "e", () -> {employe.setMail(getString("Nouveau mail : "));});
    }
    
    private Option changerPassword(final Employe employe)
    {
        return new Option("Changer le password", "x", () -> {employe.setPassword(getString("Nouveau password : "));});
    }
    
    private Option changerDateArrivee(final Employe employe)
    {
        return new Option("Changer la date d'arrivée", "a", () -> 
        {
            LocalDate date = saisirDate("Nouvelle date d'arrivée");
            if (date != null)
            {
                try
                {
                    employe.setDateArrivee(date);
                    System.out.println("Date d'arrivée modifiée avec succès.");
                }
                catch (IllegalArgumentException e)
                {
                    System.out.println("Erreur : " + e.getMessage());
                }
            }
        });
    }
    
    private Option changerDateDepart(final Employe employe)
    {
        return new Option("Changer la date de départ", "d", () -> 
        {
            LocalDate date = saisirDate("Nouvelle date de départ");
            if (date != null)
            {
                try
                {
                    employe.setDateDepart(date);
                    System.out.println("Date de départ modifiée avec succès.");
                }
                catch (IllegalArgumentException e)
                {
                    System.out.println("Erreur : " + e.getMessage());
                }
            }
        });
    }
    
    private Option afficherStatut(final Employe employe)
    {
        return new Option("Afficher le statut", "s", () -> 
        {
            System.out.println("Statut de l'employé :");
            if (employe.getDateArrivee() != null)
            {
                System.out.println("  Date d'arrivée : " + employe.getDateArrivee().format(DATE_FORMATTER));
            }
            else
            {
                System.out.println("  Date d'arrivée : non définie");
            }
            
            if (employe.getDateDepart() != null)
            {
                System.out.println("  Date de départ : " + employe.getDateDepart().format(DATE_FORMATTER));
            }
            else
            {
                System.out.println("  Date de départ : non définie");
            }
            
            if (employe.estEnFonction())
            {
                System.out.println("  Statut : EN FONCTION");
            }
            else
            {
                System.out.println("  Statut : HORS FONCTION");
                if (employe.getDateArrivee() != null && employe.getDateArrivee().isAfter(LocalDate.now()))
                {
                    System.out.println("    (arrivée future)");
                }
                if (employe.getDateDepart() != null && employe.getDateDepart().isBefore(LocalDate.now()))
                {
                    System.out.println("    (départ passé)");
                }
            }
        });
    }
    
    /**
     * Méthode utilitaire pour saisir une date avec gestion d'erreurs
     * @param message le message à afficher à l'utilisateur
     * @return la date saisie ou null si l'utilisateur a annulé
     */
    private LocalDate saisirDate(String message)
    {
        while (true)
        {
            String saisie = getString(message + " (jj/mm/aaaa) ou 'q' pour annuler : ");
            if (saisie.equalsIgnoreCase("q"))
            {
                return null;
            }
            
            try
            {
                return LocalDate.parse(saisie, DATE_FORMATTER);
            }
            catch (DateTimeParseException e)
            {
                System.out.println("Format de date invalide. Veuillez utiliser le format jj/mm/aaaa (ex: 25/12/2023)");
            }
        }
    }
}
