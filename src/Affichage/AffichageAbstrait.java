package Affichage;
import JoueurEchec.Joueur;
import Partie.PartieAbstraite;

public abstract class AffichageAbstrait
{
	Panneau paneau;
	String saisie = null;
	public AffichageAbstrait()
	{
		paneau = null;
	}
	public AffichageAbstrait(Panneau panel)
	{
		paneau = panel;
	}
	public abstract void affichagePartie(PartieAbstraite partie);
	public abstract void afficherVictoire(Joueur joueur);
	public abstract void afficherErreur(String erreur);
	public abstract void afficherChaine(String chaine);
	public abstract String SaisirChaine();    
	public abstract AffichageAbstrait clonage();
	public abstract void afficherPop(String chaine);

	public Panneau getPaneau()
	{return paneau;}
	public void setPaneau(Panneau paneau)
	{this.paneau = paneau;}
	public String getSaisie()
	{return saisie;}
	public void setSaisie(String saisie)
	{this.saisie = saisie;}
}
