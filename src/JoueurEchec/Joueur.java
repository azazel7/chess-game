package JoueurEchec;
/**
 * 
 * @author Jedusor
 *
 */
public abstract class Joueur implements Jouer
{
	/**
	 * Le nom du joueur
	 */
	protected String nom;
	/**
	 * Constructeur de base
	 */
	public Joueur()
	{
		nom = "";
	}
	/**
	 * Constructeur pour initialiser le nom
	 * @param nom_ini le nom initiale
	 */
	public Joueur(String nom_ini)
	{
		nom = nom_ini;
	}
	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}
	public abstract Joueur clonage();

	
}
