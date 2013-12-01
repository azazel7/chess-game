package JoueurEchec;

import java.util.LinkedList;

import General.Coup;
import Partie.PartieAbstraite;

/**
 * 
 * @author Nurgle
 *
 */
public class IA extends Joueur
{
	/**
	 * Une instance de MinMax qui servira � calculer les coup pour des IA plus pouss�
	 */
	protected MinMax minmax = new MinMax();
	/**
	 * Le niveau de l'IA
	 */
	protected int level;
	/**
	 * Le constucteur de base avec un niveau 1
	 */
	public IA()
	{
		super(generateName());
		level = 1;
	}
	/**
	 * Constructeur qui initialise le niveau
	 * @param niveau niveau initial
	 */
	public IA(int niveau)
	{
		super(generateName());
		level = niveau;
	}
	/**
	 * Constructeur qui initialise le nom
	 * @param nom nom initial
	 */
	public IA(String nom)
	{
		super(nom);
		level = 1;
	}
	/**
	 * Constructeur pour initialiser les deux attribu
	 * @param nom
	 * @param niveau
	 */
	public IA(String nom, int niveau)
	{
		super(nom);
		level = niveau;
	}
	/**
	 * Interface, cette methode renvoie un coup pour la partie
	 */
	public Coup jouerCoup(PartieAbstraite partie)
	{
		boolean couleur = false;
		if(partie.getDemandeNulle() > 0)
		{
			return new Coup(null, null, null, (byte)'n', null, (byte)0);
		}
		if(this == partie.getListeJoueur()[1])
		{
			couleur = true;
		}
		if(level == 2)
		{
			return Facile(partie);
		}
		else if(level == 3)
		{
			return reflexionFacile(partie);
		}
		else if(level >= 4)
		{
			return minmax.jouerCoup(partie.clonage(), level-3, couleur);
		}
		return tropFacile(partie);
	}
	/**
	 * Fonction de reflexion facile
	 * @param partie partie sur laquel la methode doit refl�chir
	 * @return le coup choisie
	 */
	public Coup reflexionFacile(PartieAbstraite partie)
	{
		LinkedList<Coup> coup, prise = new LinkedList<Coup>();
		if(partie.getListeCoup().size()%2 == 0)
		{
			coup = partie.listeCoupJouable(false);
		}
		else
		{
			coup = partie.listeCoupJouable(true);
		}
		for(int i = 0; i < coup.size(); i++)//on supprimme les coup non valide
		{
			if(partie.coupValid(coup.get(i)) == false || partie.rendEchec(coup.get(i)))
			{
				coup.remove(i);
				i--;
			}
			else if(coup.get(i).getAction() == 'O' || coup.get(i).getAction() == 'o')
			{
				return coup.get(i);
			}
			else if(coup.get(i).getAction() == 'x' || coup.get(i).getAction() == 'p')
			{
				prise.add(coup.get(i));
			}
		}
		if(prise.size() > 0)
		{
			int rang_max = 0, max = prise.get(0).getPrise().getValeur();
			for(int i = 0; i < prise.size(); i++)
			{
				if(prise.get(i).getPrise().getValeur() > max)
				{
					max = prise.get(0).getPrise().getValeur();
					rang_max = i;
				}
			}
			return prise.get(rang_max);
		}
		return coup.get((int)Math.ceil(Math.random()*(coup.size() - 1)));
	}
	/**
	 * Fonction de reflexion facile
	 * @param partie partie sur laquel la methode doit refl�chir
	 * @return le coup choisie
	 */
	public Coup Facile(PartieAbstraite partie)
	{
		LinkedList<Coup> coup, prise = new LinkedList<Coup>();
		if(partie.getListeCoup().size()%2 == 0)
		{
			coup = partie.listeCoupJouable(false);
		}
		else
		{
			coup = partie.listeCoupJouable(true);
		}
		for(int i = 0; i < coup.size(); i++)//on supprimme les coup non valide
		{
			if(partie.coupValid(coup.get(i)) == false || partie.rendEchec(coup.get(i)))
			{
				coup.remove(i);
				i--;
			}
			else if(coup.get(i).getAction() == 'O' || coup.get(i).getAction() == 'o')
			{
				return coup.get(i);
			}
			else if(partie.getPlateau().getPiecePosition(coup.get(i).getFin()) != null)
			{
				prise.add(coup.get(i));
			}
		}
		if(prise.size() > 0)
		{
			return prise.get((int)Math.ceil(Math.random()*(prise.size() - 1)));
		}
		return coup.get((int)Math.ceil(Math.random()*(coup.size() - 1)));
	}
	/**
	 * Choisie au hasard un coup parmis ceux jouable
	 * @param partie partie sur laquel la methode doit refl�chir
	 * @return le coup choisie
	 */
	public Coup tropFacile(PartieAbstraite partie)
	{
		LinkedList<Coup> coup;
		if(partie.getListeCoup().size()%2 == 0)
		{
			coup = partie.listeCoupJouable(false);
		}
		else
		{
			coup = partie.listeCoupJouable(true);
		}
		for(int i = 0; i < coup.size(); i++)//on supprimme les coups non valide
		{
			if(partie.coupValid(coup.get(i)) == false || partie.rendEchec(coup.get(i)) == true)
			{
				//System.out.println("Invalide " + coup.get(i).getPiece());
				coup.remove(i);
				i--;
			}
		}
		if(coup.size() == 0)
		{
			return new Coup(null, null, null, (byte)'b', null, (byte)0);
		}
		return coup.get((int)Math.ceil(Math.random()*(coup.size() - 1)));
	}
	/**
	 * Duplique l'IA
	 */
	public Joueur clonage()
	{
		return new IA(nom, level);
	}
	/**
	 * G�n�re un nom de mani�re pseudo al�atoire. Comme cela les IA ont un nom potable et pronon�able
	 * @return une chaine qui sera le nom choisi
	 */
	public static String generateName()
	{
		String liste[] = new String[24];
		liste[0] = new String("Beaudelaire");
		liste[1] = new String("Poseidon");
		liste[2] = new String("Magoa");
		liste[3] = new String("Zeus");
		liste[4] = new String("Sauron");
		liste[5] = new String("Conan");
		liste[6] = new String("Dumbledore");
		liste[7] = new String("Azazel");
		liste[8] = new String("Akkarim");
		liste[9] = new String("Nathaniel");
		liste[10] = new String("Linus");
		liste[11] = new String("Jilano");
		liste[12] = new String("Ellana");
		liste[13] = new String("Ewilan");
		liste[14] = new String("Raistlin");
		liste[15] = new String("tasslehoff racle-pieds");
		liste[16] = new String("Tom Jedusor");
		liste[17] = new String("Flint Forgefeu");
		liste[18] = new String("Arkandia");
		liste[19] = new String("Gandalf");
		liste[20] = new String("Tzeentch");
		liste[21] = new String("Nurgle");
		liste[22] = new String("Khorne");
		liste[23] = new String("Chandra Naalar");
		int mystere = ((int)Math.ceil(Math.random()*liste.length))%liste.length;
		return liste[mystere];
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
}
