package JoueurEchec;
import General.Coup;
import Partie.PartieAbstraite;

/**
 * 
 * @author Drago
 *
 */
public interface Jouer
{
	public Coup jouerCoup(PartieAbstraite partie);
}
