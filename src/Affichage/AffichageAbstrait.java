package Affichage;
import JoueurEchec.Joueur;
import Partie.PartieAbstraite;
/**
 * Classe permettant l'affichage. Une partie en contient une de maniére à savoir comment s'afficher
 * @author Onyx
 *
 */
public abstract class AffichageAbstrait
{
	/**
	 * Le plateau sur lequel afficher dans le cas d'un interface graphique. Null dans le cas contraire
	 */
	Panneau paneau;
	/**
	 * Une chaine servant d'intermediaire dans le cas d'une saisie en mode graphique
	 */
	String saisie = null;
	/**
	 * constructeur de base
	 */
	public AffichageAbstrait ()
	{
		paneau = null;
	}
	/**
	 * Constructeur pour initialiser le panneau
	 * @param panel panneau initial
	 */
	public AffichageAbstrait ( Panneau panel)
	{
		paneau = panel;
	}
	/**
	 * Affiche une partie en fonction du graphique ou du textuel
	 * @param partie la partie à afficher
	 */
	public abstract void affichagePartie(PartieAbstraite partie);
	/**
	 * Affiche la victoire d'un joueur (la defaite ne s'affiche pas car on considére que le perdant ne veut pas necessairement une surcouche de rappel.)
	 * @param joueur le joueur victorieu
	 */
	public abstract void afficherVictoire(Joueur joueur);
	/**
	 * Affiche une erreur
	 * @param erreur le message d'erreur
	 */
	public abstract void afficherErreur(String erreur);
	/**
	 * Afficher une chaine en console( mais pas en graphique)
	 * Les chaines affichée ainsi sont destinées à la console. En effet, sinon, on verrai bien trop de pop up en graphique
	 * @param chaine la chaine à afficher
	 */
	public abstract void afficherChaine(String chaine);
	/**
	 * Permet la saisie d'une chaine de caractére
	 * @return la chaine saisie
	 */
	public abstract String SaisirChaine();
	/**
	 * Duplique un afichage
	 * @return le clone de l'affichage
	 */
	public abstract AffichageAbstrait clonage();
	/**
	 * Affiche un pop up en graphique avec les informations. En mode textuel, il se contente d'afficher une chaine
	 * @param chaine la chaine à afficher
	 */
	public abstract void afficherPop(String chaine);
	public Panneau getPaneau() {
		return paneau;
	}
	public void setPaneau(Panneau paneau) {
		this.paneau = paneau;
	}
	public String getSaisie() {
		return saisie;
	}
	public void setSaisie(String saisie) {
		this.saisie = saisie;
	}
}