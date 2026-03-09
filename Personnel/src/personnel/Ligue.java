package personnel;

import java.io.Serializable;
import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Ligue hébergée par la M2L. Une ligue a un administrateur et contient des employés.
 */

public class Ligue implements Serializable, Comparable<Ligue>
{
    private static final long serialVersionUID = 1L;
    private int id;
    private String nom;
    private SortedSet<Employe> employes;
    private Employe administrateur;
    private GestionPersonnel gestionPersonnel;
    
    // Constructeur pour insertion (avec auto-incrément)
    public Ligue(GestionPersonnel gestionPersonnel, String nom) throws SauvegardeImpossible
    {
        this.gestionPersonnel = gestionPersonnel;
        this.nom = nom;
        employes = new TreeSet<>();
        this.id = gestionPersonnel.insert(this);
    }
    
    // Constructeur pour reconstruction depuis la base de données
    public Ligue(GestionPersonnel gestionPersonnel, int id, String nom)
    {
        this.gestionPersonnel = gestionPersonnel;
        this.id = id;
        this.nom = nom;
        employes = new TreeSet<>();
    }
    
    public int getId()
    {
        return id;
    }
    
    public String getNom()
    {
        return nom;
    }
    
    /**
     * Change le nom de la ligue et met à jour la base de données.
     * @param nom le nouveau nom de la ligue
     * @throws SauvegardeImpossible si la mise à jour dans la base échoue
     */
    public void setNom(String nom) throws SauvegardeImpossible
    {
        this.nom = nom;
        gestionPersonnel.update(this);
    }
    
    public SortedSet<Employe> getEmployes()
    {
        return Collections.unmodifiableSortedSet(employes);
    }
    
    public Employe getAdministrateur()
    {
        return administrateur;
    }
    
    /**
     * Change l'administrateur de la ligue.
     * @param administrateur le nouvel administrateur
     * @throws SauvegardeImpossible si la mise à jour dans la base échoue
     */
    public void setAdministrateur(Employe administrateur) throws SauvegardeImpossible
    {
        this.administrateur = administrateur;
        // Note: Si l'administrateur est stocké dans la table ligue,
        // il faudrait aussi mettre à jour la base ici
        // Pour l'instant, on ne fait que la mise à jour en mémoire
        // Si nécessaire, décommentez la ligne suivante :
        // gestionPersonnel.update(this);
    }
    
    public Employe addEmploye(String nom, String prenom, String mail, String password) throws SauvegardeImpossible
    {
        Employe employe = new Employe(gestionPersonnel, this, nom, prenom, mail, password);
        employes.add(employe);
        return employe;
    }
    
    void remove(Employe employe)
    {
        employes.remove(employe);
    }
    
    @Override
    public int compareTo(Ligue autre)
    {
        return getNom().compareTo(autre.getNom());
    }
    
    @Override
    public String toString()
    {
        return nom;
    }
}
