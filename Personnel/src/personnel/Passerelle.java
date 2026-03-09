package personnel;

public interface Passerelle 
{
    GestionPersonnel getGestionPersonnel() throws SauvegardeImpossible;
    void sauvegarderGestionPersonnel(GestionPersonnel gestionPersonnel) throws SauvegardeImpossible;
    int insert(Ligue ligue) throws SauvegardeImpossible;
    int insert(Employe employe) throws SauvegardeImpossible;
}
