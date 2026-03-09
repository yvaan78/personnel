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
    
    public void setNom(String nom)
    {
        this.nom = nom;
    }
    
    public SortedSet<Employe> getEmployes()
    {
        return Collections.unmodifiableSortedSet(employes);
    }
    
    public Employe getAdministrateur()
    {
        return administrateur;
    }
    
    public void setAdministrateur(Employe administrateur)
    {
        this.administrateur = administrateur;
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
