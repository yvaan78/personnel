package personnel;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Employé d'une ligue hébergée par la M2L. Certains peuvent
 * être administrateurs des employés de leur ligue.
 * Un seul employé, rattaché à aucune ligue, est le root.
 * Il est impossible d'instancier directement un employé,
 * il faut passer la méthode {@link Ligue#addEmploye addEmploye}.
 */

public class Employe implements Serializable, Comparable<Employe>
{
    private static final long serialVersionUID = 4795721718037994734L;
    private int id;
    private String nom, prenom, password, mail;
    private LocalDate dateArrivee, dateDepart;
    private Ligue ligue;
    private GestionPersonnel gestionPersonnel;
   
    Employe(GestionPersonnel gestionPersonnel, Ligue ligue, String nom, String prenom, String mail, String password) throws SauvegardeImpossible
    {
        this.gestionPersonnel = gestionPersonnel;
        this.nom = nom;
        this.prenom = prenom;
        this.password = password;
        this.mail = mail;
        this.ligue = ligue;
        this.dateArrivee = null;
        this.dateDepart = null;
       
        // Insertion dans la base de données
        if (gestionPersonnel != null)
        {
            this.id = gestionPersonnel.insert(this);
        }
    }
   
    Employe(GestionPersonnel gestionPersonnel, Ligue ligue, int id, String nom, String prenom, String mail, String password)
    {
        this.gestionPersonnel = gestionPersonnel;
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.password = password;
        this.mail = mail;
        this.ligue = ligue;
        this.dateArrivee = null;
        this.dateDepart = null;
    }
   
    public int getId()
    {
        return id;
    }
   
    public void setId(int id)
    {
        this.id = id;
    }
   
    /**
     * Retourne vrai ssi l'employé est administrateur de la ligue
     * passée en paramètre.
     * @return vrai ssi l'employé est administrateur de la ligue
     * passée en paramètre.
     * @param ligue la ligue pour laquelle on souhaite vérifier si this
     * est l'admininstrateur.
     */
   
    public boolean estAdmin(Ligue ligue)
    {
        return ligue.getAdministrateur() == this;
    }
   
    /**
     * Retourne vrai ssi l'employé est le root.
     * @return vrai ssi l'employé est le root.
     */
   
    public boolean estRoot()
    {
        return gestionPersonnel.getRoot() == this;
    }
   
    /**
     * Retourne le nom de l'employé.
     * @return le nom de l'employé.
     */
   
    public String getNom()
    {
        return nom;
    }

    /**
     * Change le nom de l'employé.
     * @param nom le nouveau nom.
     */
   
    public void setNom(String nom)
    {
        this.nom = nom;
    }

    /**
     * Retourne le prénom de l'employé.
     * @return le prénom de l'employé.
     */
   
    public String getPrenom()
    {
        return prenom;
    }
   
    /**
     * Change le prénom de l'employé.
     * @param prenom le nouveau prénom de l'employé.
     */

    public void setPrenom(String prenom)
    {
        this.prenom = prenom;
    }

    /**
     * Retourne le mail de l'employé.
     * @return le mail de l'employé.
     */
   
    public String getMail()
    {
        return mail;
    }
   
    /**
     * Change le mail de l'employé.
     * @param mail le nouveau mail de l'employé.
     */

    public void setMail(String mail)
    {
        this.mail = mail;
    }

    /**
     * Retourne vrai ssi le password passé en paramètre est bien celui
     * de l'employé.
     * @return vrai ssi le password passé en paramètre est bien celui
     * de l'employé.
     * @param password le password auquel comparer celui de l'employé.
     */
   
    public boolean checkPassword(String password)
    {
        return this.password.equals(password);
    }

    /**
     * Change le password de l'employé.
     * @param password le nouveau password de l'employé.
     */
   
    public void setPassword(String password)
    {
        this.password = password;
    }

    /**
     * Retourne la date d'arrivée de l'employé.
     * @return la date d'arrivée de l'employé.
     */
   
    public LocalDate getDateArrivee()
    {
        return dateArrivee;
    }
   
    /**
     * Change la date d'arrivée de l'employé.
     * @param dateArrivee la nouvelle date d'arrivée.
     * @throws IllegalArgumentException si la date est postérieure à la date de départ
     */
   
    public void setDateArrivee(LocalDate dateArrivee)
    {
        if (dateDepart != null && dateArrivee != null && dateArrivee.isAfter(dateDepart))
        {
            throw new IllegalArgumentException("La date d'arrivée ne peut pas être postérieure à la date de départ");
        }
        this.dateArrivee = dateArrivee;
    }
   
    /**
     * Retourne la date de départ de l'employé.
     * @return la date de départ de l'employé.
     */
   
    public LocalDate getDateDepart()
    {
        return dateDepart;
    }
   
    /**
     * Change la date de départ de l'employé.
     * @param dateDepart la nouvelle date de départ.
     * @throws IllegalArgumentException si la date est antérieure à la date d'arrivée
     */
   
    public void setDateDepart(LocalDate dateDepart)
    {
        if (dateArrivee != null && dateDepart != null && dateDepart.isBefore(dateArrivee))
        {
            throw new IllegalArgumentException("La date de départ ne peut pas être antérieure à la date d'arrivée");
        }
        this.dateDepart = dateDepart;
    }
   
    /**
     * Vérifie si l'employé est actuellement en fonction.
     * @return true si l'employé est en fonction, false sinon
     */
   
    public boolean estEnFonction()
    {
        LocalDate maintenant = LocalDate.now();
        if (dateArrivee == null) return true; // Pas de date d'arrivée = toujours en fonction
        if (dateArrivee.isAfter(maintenant)) return false; // Arrivée future
        if (dateDepart != null && dateDepart.isBefore(maintenant)) return false; // Départ passé
        return true;
    }

    /**
     * Retourne la ligue à laquelle l'employé est affecté.
     * @return la ligue à laquelle l'employé est affecté.
     */
   
    public Ligue getLigue()
    {
        return ligue;
    }

    /**
     * Supprime l'employé. Si celui-ci est un administrateur, le root
     * récupère les droits d'administration sur sa ligue.
     */
   
    public void remove()
    {
        Employe root = gestionPersonnel.getRoot();
        if (this != root)
        {
            if (estAdmin(getLigue()))
                getLigue().setAdministrateur(root);
            getLigue().remove(this);
        }
        else
            throw new ImpossibleDeSupprimerRoot();
    }
   
    @Override
    public int compareTo(Employe autre)
    {
        int cmp = getNom().compareTo(autre.getNom());
        if (cmp != 0)
            return cmp;
        return getPrenom().compareTo(autre.getPrenom());
    }
   
    @Override
    public String toString()
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        StringBuilder res = new StringBuilder();
        res.append(nom).append(" ").append(prenom).append(" ").append(mail);
       
        if (dateArrivee != null)
        {
            res.append(" (arrivée: ").append(dateArrivee.format(formatter));
            if (dateDepart != null)
            {
                res.append(", départ: ").append(dateDepart.format(formatter));
            }
            res.append(")");
        }
       
        res.append(" (");
        if (estRoot())
            res.append("super-utilisateur");
        else
            res.append(ligue.toString());
        res.append(")");
       
        if (!estEnFonction() && dateArrivee != null)
        {
            res.append(" [Hors fonction]");
        }
       
        return res.toString();
    }
}
