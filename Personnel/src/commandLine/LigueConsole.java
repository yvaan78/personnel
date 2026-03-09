package commandLine;

import static commandLineMenus.rendering.examples.util.InOut.getString;

import java.util.ArrayList;

import commandLineMenus.List;
import commandLineMenus.Menu;
import commandLineMenus.Option;

import personnel.*;

public class LigueConsole 
{
    private GestionPersonnel gestionPersonnel;
    private EmployeConsole employeConsole;

    public LigueConsole(GestionPersonnel gestionPersonnel, EmployeConsole employeConsole)
    {
        this.gestionPersonnel = gestionPersonnel;
        this.employeConsole = employeConsole;
    }

    Menu menuLigues()
    {
        Menu menu = new Menu("Gérer les ligues", "l");
        menu.add(afficherLigues());
        menu.add(ajouterLigue());
        menu.add(selectionnerLigue());
        menu.addBack("q");
        return menu;    
    }

    private Option afficherLigues()
    {
        return new Option("Afficher les ligues", "l", () -> {System.out.println(gestionPersonnel.getLigues());});
    }

    private Option afficher(final Ligue ligue)
    {
        return new Option("Afficher la ligue", "l", 
                () -> 
                {
                    System.out.println(ligue);
                    System.out.println("administrée par " + ligue.getAdministrateur());
                }
        );
    }
    
    private Option afficherEmployes(final Ligue ligue)
    {
        return new Option("Afficher les employes", "f", () -> {System.out.println(ligue.getEmployes());});
    }

    private Option ajouterLigue()
    {
        return new Option("Ajouter une ligue", "a", () -> 
        {
            try
            {
                gestionPersonnel.addLigue(getString("nom : "));
            }
            catch(SauvegardeImpossible exception)
            {
                System.err.println("Impossible de sauvegarder cette ligue");
            }
        });
    }
    
    private Menu editerLigue(Ligue ligue)
    {
        Menu menu = new Menu("Editer " + ligue.getNom());
        menu.add(afficher(ligue));
        menu.add(gererEmployes(ligue));
        menu.add(changerNom(ligue));
        menu.add(changerAdministrateur(ligue));
        menu.add(supprimer(ligue));
        menu.addBack("q");
        return menu;
    }

    private Option changerNom(final Ligue ligue)
    {
        return new Option("Renommer", "r", 
                () -> {ligue.setNom(getString("Nouveau nom : "));});
    }
    
    /**
     * Nouvelle option pour changer l'administrateur d'une ligue
     * @param ligue la ligue dont on veut changer l'administrateur
     * @return l'option de menu
     */
    private Option changerAdministrateur(final Ligue ligue)
    {
        return new Option("Changer l'administrateur", "c", 
                () -> 
                {
                    // Vérifier si la ligue a des employés
                    if (ligue.getEmployes().isEmpty())
                    {
                        System.out.println("Impossible : la ligue n'a aucun employé.");
                        return;
                    }
                    
                    System.out.println("Employés de la ligue :");
                    int index = 1;
                    for (Employe employe : ligue.getEmployes())
                    {
                        System.out.println(index++ + ". " + employe.getNom() + " " + employe.getPrenom() + " (" + employe.getMail() + ")");
                    }
                    
                    int choix = -1;
                    while (choix < 1 || choix > ligue.getEmployes().size())
                    {
                        try
                        {
                            choix = Integer.parseInt(getString("Choisissez le numéro du nouvel administrateur : "));
                            if (choix < 1 || choix > ligue.getEmployes().size())
                            {
                                System.out.println("Choix invalide. Veuillez entrer un nombre entre 1 et " + ligue.getEmployes().size());
                            }
                        }
                        catch (NumberFormatException e)
                        {
                            System.out.println("Veuillez entrer un nombre valide.");
                        }
                    }
                    
                    Employe nouvelAdmin = (Employe) ligue.getEmployes().toArray()[choix - 1];
                    ligue.setAdministrateur(nouvelAdmin);
                    System.out.println("Le nouvel administrateur de la ligue est : " + nouvelAdmin.getPrenom() + " " + nouvelAdmin.getNom());
                }
        );
    }

    private List<Ligue> selectionnerLigue()
    {
        return new List<Ligue>("Sélectionner une ligue", "e", 
                () -> new ArrayList<>(gestionPersonnel.getLigues()),
                (element) -> editerLigue(element)
                );
    }
    
    private Option ajouterEmploye(final Ligue ligue)
    {
        return new Option("Ajouter un employé", "a",
                () -> 
                {
                    ligue.addEmploye(getString("nom : "), 
                        getString("prenom : "), getString("mail : "), 
                        getString("password : "));
                }
        );
    }
    
    private Menu gererEmployes(Ligue ligue)
    {
        Menu menu = new Menu("Gérer les employés de " + ligue.getNom(), "e");
        menu.add(afficherEmployes(ligue));  // Raccourci changé pour "f" (pour "fficher")
        menu.add(ajouterEmploye(ligue));    // Raccourci "a" conservé pour "ajouter"
        menu.add(selectionnerPourModifierEmploye(ligue));
        menu.add(selectionnerPourSupprimerEmploye(ligue));
        menu.add(selectionnerPourNommerAdministrateur(ligue));
        menu.addBack("q");
        return menu;
    }
    
    /**
     * Nouvelle option pour nommer un employé comme administrateur directement depuis la liste des employés
     * @param ligue la ligue concernée
     * @return l'option de menu
     */
    private List<Employe> selectionnerPourNommerAdministrateur(final Ligue ligue)
    {
        return new List<>("Nommer comme administrateur", "n", 
                () -> new ArrayList<>(ligue.getEmployes()),
                (index, element) -> 
                {
                    String confirmation = getString("Êtes-vous sûr de vouloir nommer " + 
                        element.getPrenom() + " " + element.getNom() + 
                        " comme administrateur de la ligue ? (oui/non) : ");
                    if (confirmation.equalsIgnoreCase("oui"))
                    {
                        ligue.setAdministrateur(element);
                        System.out.println(element.getPrenom() + " " + element.getNom() + 
                                " est maintenant administrateur de la ligue.");
                    }
                }
        );
    }

    // Option pour modifier un employé
    private List<Employe> selectionnerPourModifierEmploye(final Ligue ligue)
    {
        return new List<>("Modifier un employé", "m", 
                () -> new ArrayList<>(ligue.getEmployes()),
                (index, element) -> 
                {
                    employeConsole.editerEmploye(element);
                }
                );
    }
    
    // Option pour supprimer un employé
    private List<Employe> selectionnerPourSupprimerEmploye(final Ligue ligue)
    {
        return new List<>("Supprimer un employé", "s", 
                () -> new ArrayList<>(ligue.getEmployes()),
                (index, element) -> 
                {
                    String confirmation = getString("Êtes-vous sûr de vouloir supprimer " + 
                        element.getNom() + " " + element.getPrenom() + "? (oui/non): ");
                    if (confirmation.equalsIgnoreCase("oui"))
                    {
                        element.remove();
                        System.out.println("Employé supprimé avec succès");
                    }
                }
                );
    }
    
    private Option supprimer(Ligue ligue)
    {
        return new Option("Supprimer", "d", () -> {ligue.remove();});
    }
}
