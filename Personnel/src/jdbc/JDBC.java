package jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import personnel.*;

public class JDBC implements Passerelle
{
    private Connection connection;

    public JDBC()
    {
        try
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/m2l", "root", "");
        }
        catch (ClassNotFoundException | SQLException e)
        {
            e.printStackTrace();
        }
    }
   
    @Override
    public GestionPersonnel getGestionPersonnel()
    {
        GestionPersonnel gestionPersonnel = new GestionPersonnel();
        try
        {
            // Charger les ligues
            String requete = "select * from ligue";
            Statement instruction = connection.createStatement();
            ResultSet ligues = instruction.executeQuery(requete);
            while (ligues.next())
                gestionPersonnel.addLigue(ligues.getInt(1), ligues.getString(2));
           
            // Charger les employés (pour le root notamment)
            requete = "select * from employe";
            ResultSet employes = instruction.executeQuery(requete);
            while (employes.next())
            {
                int id = employes.getInt("id");
                String nom = employes.getString("nom");
                String prenom = employes.getString("prenom");
                String mail = employes.getString("mail");
                String password = employes.getString("password");
                int ligueId = employes.getInt("ligue_id");
               
                Ligue ligue = null;
                if (!employes.wasNull()) {
                    // Trouver la ligue correspondante
                    for (Ligue l : gestionPersonnel.getLigues()) {
                        if (l.getId() == ligueId) {
                            ligue = l;
                            break;
                        }
                    }
                }
               
                // Créer l'employé
                Employe employe = new Employe(gestionPersonnel, ligue, id, nom, prenom, mail, password);
               
                // Si c'est le root (ligue_id NULL, mail root@localhost)
                if (mail.equals("root@localhost")) {
                    // Utiliser la réflexion pour modifier le root (car root est private)
                    java.lang.reflect.Field rootField = GestionPersonnel.class.getDeclaredField("root");
                    rootField.setAccessible(true);
                    rootField.set(gestionPersonnel, employe);
                } else if (ligue != null) {
                    // Ajouter l'employé à sa ligue
                    java.lang.reflect.Field employesField = Ligue.class.getDeclaredField("employes");
                    employesField.setAccessible(true);
                    java.util.SortedSet<Employe> employesSet = (java.util.SortedSet<Employe>) employesField.get(ligue);
                    employesSet.add(employe);
                }
            }
        }
        catch (SQLException | NoSuchFieldException | IllegalAccessException e)
        {
            System.out.println(e);
        }
        return gestionPersonnel;
    }
   
    @Override
    public void sauvegarderGestionPersonnel(GestionPersonnel gestionPersonnel) throws SauvegardeImpossible
    {
        // Méthode à implémenter si nécessaire
    }
   
    @Override
    public int insert(Ligue ligue) throws SauvegardeImpossible
    {
        try
        {
            PreparedStatement instruction;
            instruction = connection.prepareStatement("insert into ligue (nom) values (?)", Statement.RETURN_GENERATED_KEYS);
            instruction.setString(1, ligue.getNom());
            instruction.executeUpdate();
            ResultSet id = instruction.getGeneratedKeys();
            id.next();
            return id.getInt(1);
        }
        catch (SQLException exception)
        {
            exception.printStackTrace();
            throw new SauvegardeImpossible(exception);
        }
    }
   
    @Override
    public int insert(Employe employe) throws SauvegardeImpossible
    {
        try
        {
            String sql;
            PreparedStatement instruction;
           
            // Si c'est le root (pas de ligue), ligue_id sera NULL
            if (employe.getLigue() == null)
            {
                sql = "INSERT INTO employe (nom, prenom, mail, password, ligue_id) VALUES (?, ?, ?, ?, NULL)";
                instruction = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                instruction.setString(1, employe.getNom());
                instruction.setString(2, employe.getPrenom());
                instruction.setString(3, employe.getMail());
                instruction.setString(4, employe.getPassword());
            }
            else
            {
                sql = "INSERT INTO employe (nom, prenom, mail, password, ligue_id) VALUES (?, ?, ?, ?, ?)";
                instruction = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                instruction.setString(1, employe.getNom());
                instruction.setString(2, employe.getPrenom());
                instruction.setString(3, employe.getMail());
                instruction.setString(4, employe.getPassword());
                instruction.setInt(5, employe.getLigue().getId());
            }
           
            instruction.executeUpdate();
            ResultSet rs = instruction.getGeneratedKeys();
            if (rs.next())
            {
                return rs.getInt(1);
            }
            return -1;
        }
        catch (SQLException exception)
        {
            exception.printStackTrace();
            throw new SauvegardeImpossible(exception);
        }
    }
}
