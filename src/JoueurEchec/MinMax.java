package JoueurEchec;

import java.util.LinkedList;
import java.util.ListIterator;

import General.Coup;
import General.Position;
import Partie.PartieAbstraite;
import PieceEchec.PieceAbstraite;
import PieceEchec.Roi;
/**
 * 
 * @author Flint
 *
 */
public class MinMax
{
	//protected Coup coupsuivant = null;
	//protected int profondeur_souvenir;
	protected int poidp[][] = {{0, 0, 0, 0, 0, 0, 0, 0},
							   {0, 0, 0, 0, 0, 0, 0, 0},
							   {0, 0, 1, 1, 1, 1, 0, 0},
							   {0, 0, 1, 2, 2, 1, 0, 0},
							   {0, 0, 1, 2, 2, 1, 0, 0},
							   {0, 0, 1, 1, 1, 1, 0, 0},
							   {0, 0, 0, 0, 0, 0, 0, 0},
							   {0, 0, 0, 0, 0, 0, 0, 0}};
	/**
	 * Retourne un coup calculer par l'objet
	 * @param la partie a etudier
	 * @param la profondeur a arpanter
	 * @param la couleur du joueur
	 */
	public Coup jouerCoup(PartieAbstraite partie, int profondeur, boolean couleur)//cherche le maximum
	{
		int  valeur = 0, alpha = -100000, beta = 100000;
		Coup meilleur = null, actuel = null;
		LinkedList<Coup> listeCoup = partie.listeCoupJouable(couleur);
		ListIterator<Coup> iterator = listeCoup.listIterator();
		//profondeur_souvenir = profondeur-1;
		while(iterator.hasNext())
		{
			actuel = iterator.next();
			if(partie.rendEchec(actuel) == true)
			{
				iterator.remove();
			}
		}
		if(partie.getListeCoup().size() <= 1)
		{
			return listeCoup.get((int)Math.ceil(Math.random()*(listeCoup.size() - 1)));
		}
		
		iterator = listeCoup.listIterator();
		while(iterator.hasNext())
		{
			actuel = iterator.next();
			partie.getPlateau().deplacementPiece(actuel);//simule le coup sur partie
			partie.getListeCoup().add(actuel);
			valeur = Min(partie, profondeur-1, couleur, alpha, beta);
			if(valeur > alpha)
			{
				alpha = valeur;
				meilleur = actuel.clonage();
			}
			partie.annulerDernierCoup();
		}
		System.out.println("Meilleur: " + meilleur + "\tPoid: " + alpha);//TODO
		return meilleur;
	}
	/**
	 * Noeud min qui maximise le coup de l'adversaire
	 * @param partie la partie a explorer
	 * @param profondeur la profondeur restante
	 * @param couleur la couleur a etudier
	 * @param alpha le minimum des frere trouve
	 * @param beta le maximum des frere trouve
	 * @return le poid du coup
	 */
	public int Min(PartieAbstraite partie, int profondeur, boolean couleur, int alpha, int beta)//cherche le minimum
	{
		//on verifi si on est sur une feuille de l'arbre -> de plus on initialise d'ici le compteur de poids de la fonction d'eval, ainsi cette derniere n'a pas a refaire tout les tests donc elle est plus rapide
		if(profondeur == 0)
		{
			return Evaluation(partie, couleur, 0);
		}
		else if(partie.estMat(couleur))
		{
			return Evaluation(partie, couleur, -10000+partie.getListeCoup().size());
		}
		else if(partie.estMat(!couleur))
		{
			return Evaluation(partie, couleur, 10000-partie.getListeCoup().size());
		}
		else if(partie.estPat(couleur))
		{
			return Evaluation(partie, couleur, -1000+partie.getListeCoup().size());
		}
		else if(partie.estPat(!couleur))
		{
			return Evaluation(partie, couleur, 1000-partie.getListeCoup().size());
		}
		int valeur = 0;
		Coup actuel = null;
		LinkedList<Coup> listeCoup = partie.listeCoupJouable(!couleur);//liste coup adversaire
		ListIterator<Coup> iterator = listeCoup.listIterator();
		while(iterator.hasNext())
		{
			actuel = iterator.next();
			if(partie.rendEchec(actuel) == true)
			{
				iterator.remove();
			}
		}
		iterator = listeCoup.listIterator(); 
		while(iterator.hasNext())
		{
			actuel = iterator.next();
			partie.getPlateau().deplacementPiece(actuel);//simule
			partie.getListeCoup().add(actuel);
			valeur = Max(partie, profondeur-1, couleur, alpha, beta);
			if(valeur < beta)
			{
				beta = valeur;
			}
			partie.annulerDernierCoup();
			if(valeur <= alpha)
			{
				return valeur;
			}
		}
		return beta;
	}
	/**
	 * Noeud max qui maximise le coup pour nous
	 * @param partie la partie a explorer
	 * @param profondeur la profondeur restante
	 * @param couleur la couleur a etudier
	 * @param alpha le minimum des frere trouve
	 * @param beta le maximum des frere trouve
	 * @return le poid du coup
	 */
	public int Max(PartieAbstraite partie, int profondeur, boolean couleur, int alpha, int beta)//cherche le maximum
	{
		//on verifi si on est sur une feuille de l'arbre -> de plus on initialise d'ici le compteur de poids de la fonction d'eval, ainsi cette derniere n'a pas a refaire tout les tests donc elle est plus rapide
		if(profondeur == 0)
		{
			return Evaluation(partie, couleur, 0);
		}
		else if(partie.estMat(couleur))
		{
			return Evaluation(partie, couleur, -10000+partie.getListeCoup().size());
		}
		else if(partie.estMat(!couleur))
		{
			return Evaluation(partie, couleur, 10000-partie.getListeCoup().size());
		}
		else if(partie.estPat(couleur))
		{
			return Evaluation(partie, couleur, -1000+partie.getListeCoup().size());
		}
		else if(partie.estPat(!couleur))
		{
			return Evaluation(partie, couleur, 1000-partie.getListeCoup().size());
		}
		int valeur = 0;
		Coup actuel = null;
		LinkedList<Coup> listeCoup = partie.listeCoupJouable(couleur);//liste coup
		ListIterator<Coup> iterator = listeCoup.listIterator();
		while(iterator.hasNext())
		{
			actuel = iterator.next();
			if(partie.rendEchec(actuel) == true)
			{
				iterator.remove();
			}
		}
		iterator = listeCoup.listIterator();
		while(iterator.hasNext())
		{
			actuel = iterator.next();
			try
			{
				partie.getPlateau().deplacementPiece(actuel);//simule
				partie.getListeCoup().add(actuel);
			}
			catch(Exception e)
			{
				for(int i = 0; i < partie.getListeCoup().size(); i++)
				{
					System.out.println(partie.getListeCoup().get(i));
				}
				partie.getAffichage().affichagePartie(partie);
				System.out.println(actuel + " " + actuel.getDepart());
				System.out.println(e.getMessage() + "\n" + e.getStackTrace());
				synchronized(this)
				{
					try
					{
						this.wait();
					}
					catch(Exception b)
					{
						
					}
				}
			}
			valeur = Min(partie, profondeur-1, couleur, alpha, beta);
			if(valeur > alpha)
			{
				alpha = valeur;
			}
			partie.annulerDernierCoup();
			if(valeur >= beta)
			{
				break;
			}
		}
		return alpha;
	}
	/**
	 * Methode qui evalue une partie
	 * @param partie la partie a evaluer
	 * @param couleur la couleur de laquelle on regarde
	 * @param poid le poid initiale
	 * @return retourne le poid de la partie
	 */
	public int Evaluation(PartieAbstraite partie, boolean couleur, int poid)
	{
		//etat de la partie (verifie avant, inutile de le faire avant)
		
		PieceAbstraite actuel = null;
		LinkedList<PieceAbstraite> piece = partie.getPlateau().getPiece();
		ListIterator<PieceAbstraite> iterator = piece.listIterator();
		while(iterator.hasNext())//on verifi l'echec de nous
		{
			actuel = iterator.next();
			if(actuel instanceof Roi && actuel.getCouleur() == couleur)
			{
				if(partie.estEchec(actuel.getPosition(), couleur))
				{
					poid -= 10;
				}
			}
			else if(actuel instanceof Roi && actuel.getCouleur() == !couleur)
			{
				if(partie.estEchec(actuel.getPosition(), !couleur))
				{
					poid += 10;
				}
			}
			if(actuel.getCouleur() == couleur)
			{
				poid += poidPosition(actuel.getPosition());
			}
		}
		LinkedList<Coup> coup = partie.listeCoupJouable(couleur);
		ListIterator<Coup> i_coup = coup.listIterator();
		Coup actu = null;
		while(i_coup.hasNext())
		{
			actu = i_coup.next();
			if(actu.getPrise() != null)
			{
				poid += actu.getPrise().getValeur();
			}
		}
		//le nombre de coup
		poid += (coup.size()/2);
		
		//nombre de piece
		poid += partie.getPlateau().nombrePieceCouleur(couleur);
		poid -= partie.getPlateau().nombrePieceCouleur(!couleur);
		
		//somme total des valeur de piece
		poid += partie.getPlateau().sommeValeurCouleur(couleur);
		poid -= partie.getPlateau().sommeValeurCouleur(!couleur);
		
		return poid;
	}
	/**
	 * Renvoi la valeur d'une position
	 * @param p la position a evaluer
	 * @return son poid
	 */
	public int poidPosition(Position p)
	{
		return poidp[p.getX()][p.getY()];
	}
}
