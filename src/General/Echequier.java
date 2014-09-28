package General;
import java.util.LinkedList;
import java.util.ListIterator;

import Partie.PartieAbstraite;
import PieceEchec.Cavalier;
import PieceEchec.Dame;
import PieceEchec.Fou;
import PieceEchec.PieceAbstraite;
import PieceEchec.Roi;
import PieceEchec.Tour;

/**
 * classe represantant un echequier
 * @author Ewilan
 *
 */

public class Echequier
{
	//attribut
	/**
	 * represente la longueur de l'absicce
	 */
	protected int longueur;
	/**
	 * represente la hauteur de l'ordonnee
	 */
	protected int hauteur;
	/**
	 * Repertorie toute les pieces presentes sur l'echequier
	 */
	protected LinkedList<PieceAbstraite> piece;
	
	//constructeur
	/**
	 * constructeur de base
	 */
	public Echequier()
	{
		longueur = hauteur = 1;
		piece = new LinkedList<PieceAbstraite>();
	}
	/**
	 * constructeur
	 * @param longueur_ini longueur initial
	 * @param hauteur_ini hauteur initial
	 * @param piece_ini la liste  des piece initialment presente sur l'echequier
	 */
	public Echequier(int longueur_ini, int hauteur_ini, LinkedList<PieceAbstraite> piece_ini)
	{
		if(longueur_ini <= 0)
		{
			longueur_ini = 1;
		}
		if(hauteur_ini <= 0)
		{
			hauteur_ini = 1;
		}
		longueur = longueur_ini;
		hauteur = hauteur_ini;
		piece = piece_ini;
	}
	
	//methode defini
	/**
	 * Renvoie la piece correspondant a la position donnee
	 * @param position
	 * 			la position ou peut se trouver une piece
	 * @return retourne la piece correspondante ou null s'il n'y a pas de piece ou que la position est impossible
	 */
	public PieceAbstraite getPiecePosition(Position position)
	{
		
		try
		{
			if(positionPossible(position) == false)
			{
				return null;
			}
			ListIterator<PieceAbstraite> iterator = piece.listIterator();
			PieceAbstraite actuel;
			
			while(iterator.hasNext())
			{
				actuel = iterator.next();
				if(position.equals(actuel.getPosition()) == true)
				{
					return actuel;
				}
			}
		}
		catch(Exception e)
		{
			
		}
		return null;
	}
	/**
	 * supprime la piece qui est a cette position
	 * @param position
	 * 			la position ou doit se trouver la piece
	 */
	public void removePiece(Position position)
	{
		if(positionPossible(position) == false)//inutile de faire des boucle pour rien
		{
			return;
		}
		ListIterator<PieceAbstraite> iterator = piece.listIterator();
		PieceAbstraite actuel;
		while(iterator.hasNext())
		{
			actuel = iterator.next();
			if(position.equals(actuel.getPosition()) == true)
			{
				iterator.remove();
				return;
			}
		}
	}
	/**
	 * Ajoute ou remplace une piece par celle fourni en argument
	 * @param newPiece la piece a ajouter
	 * @return si la piece a bien etait ajoutee
	 */
	public boolean setPiecePosition(PieceAbstraite newPiece)
	{
		if(newPiece == null)
		{
			return false;
		}
		if(positionPossible(newPiece.getPosition()) == false)
		{
			return false;
		}
		
		ListIterator<PieceAbstraite> iterator = piece.listIterator();
		PieceAbstraite actuel;
		while(iterator.hasNext())
		{
			actuel = iterator.next();
			if(newPiece.getPosition().equals(actuel.getPosition()) == true)
			{
				iterator.remove();
				break;
			}
		}
		if(newPiece instanceof Roi || newPiece instanceof Dame)
		{
			piece.addFirst(newPiece.clonage());
		}
		else
		{
			piece.add(newPiece.clonage());
		}
		return true;
	}
	/**
	 * Verifi si une position peut exister sur l'echequier
	 * @param position position a verifier
	 * @return true si la position est possible, false sinon
	 */
	public boolean positionPossible(Position position)
	{
		if(position == null)
		{
			return false;
		}
		if(position.getX() < 0 || position.getX() >= longueur || position.getY() < 0 || position.getY() >= hauteur)
		{
			return false;
		}
		return true;
	}
	/**
	 * Realise la roque du roi avec la tour
	 * @param type true = grand roque, false = petit roque
	 * @param couleur true = noir
	 * @return retourne true si le roque s'est bien effectue.
	 */
	public boolean realiserRoque(boolean type, boolean couleur)
	{
		PieceAbstraite roi = null, tour = null;
		//boolean found = false;
		//on cherche le roi de la bonne couleur n'ayant pas bouge
		if(couleur)
		{
			roi = getPiecePosition(new Position(4, 7));
		}
		else
		{
			roi = getPiecePosition(new Position(4, 0));
		}
		if(couleur && type)
		{
			tour = getPiecePosition(new Position(0, 7));
		}
		else if(couleur && !type)
		{
			tour = getPiecePosition(new Position(7, 7));
		}
		else if(!couleur && type)
		{
			tour = getPiecePosition(new Position(0, 0));
		}
		else
		{
			tour = getPiecePosition(new Position(7, 0));
		}
		
		//on effectue le changement
		if(type == true)
		{
			roi.setPosition(new Position(roi.getPosition().getX()-2, roi.getPosition().getY()));
			tour.setPosition(new Position(tour.getPosition().getX()+3, tour.getPosition().getY()));
		}
		else if(type == false)
		{
			roi.setPosition(new Position(roi.getPosition().getX()+2, roi.getPosition().getY()));
			tour.setPosition(new Position(tour.getPosition().getX()-2, tour.getPosition().getY()));
		}
		roi.setaBouge(roi.getaBouge()+1);
		tour.setaBouge(tour.getaBouge()+1);
		return true;
	}
	/**
	 * 
	 * @param position position de la promotion
	 * @param type la nouvelle piece
	 * @return true si cela c'est bien passe
	 */
	public boolean promotionPiece(Position position, PieceAbstraite newpiece)
	{
		ListIterator<PieceAbstraite> iterator = piece.listIterator();
		PieceAbstraite actuel;
		while(iterator.hasNext())
		{
			actuel = iterator.next();
			if(actuel.getPosition().equals(position))
			{
				iterator.remove();
				piece.add(newpiece);
				return true;
			}
		}
		return false;
	}
	/**
	 * Verifie la validite du coup puis l'effectue
	 * @param mouvement le coup a effectuer
	 * @return true si le coup a etait joue.
	 * @throws Exception 
	 */
	public boolean deplacementPiece(Coup mouvement)
	{
		//try
		//{
		PieceAbstraite piece;
		if(mouvement == null)
		{
			return true;
		}
		else if(mouvement.getAction() == 'x')//on supprime la piece a position de fin si il y a prise
		{
			removePiece(mouvement.getFin());
			piece = getPiecePosition(mouvement.getDepart());
			piece.setaBouge(piece.getaBouge()+1);
			piece.setPosition(mouvement.getFin());//les verifs sont theoriquement effectuees			
		}
		else if(mouvement.getAction() == '-')//deplacement simple
		{	
				piece = getPiecePosition(mouvement.getDepart());
				piece.setaBouge(piece.getaBouge()+1);
				piece.setPosition(mouvement.getFin());//les verifs sont theoriquement effectuees
		}
		else if(mouvement.getAction() == 'p')//prise en passant
		{
			piece = getPiecePosition(mouvement.getDepart());
			piece.setaBouge(piece.getaBouge()+1);
			piece.setPosition(mouvement.getFin());//les verifs sont th�oriquement effectuees
			removePiece(mouvement.getPrise().getPosition());
		}
		else if(mouvement.getAction() == 'o')//petit roque
		{
			realiserRoque(false, mouvement.getPiece().getCouleur());
			
		}
		else if(mouvement.getAction() == 'O')//grand roque
		{
			realiserRoque(true, mouvement.getPiece().getCouleur());
		}
		switch(mouvement.getPromotion())//tout est verifie normalement
		{
			case 'F':
				setPiecePosition(new Fou(mouvement.getPiece().getCouleur(), mouvement.getPiece().getaBouge(), mouvement.getFin()));
				break;
			case 'C':
				setPiecePosition(new Cavalier(mouvement.getPiece().getCouleur(), mouvement.getPiece().getaBouge(), mouvement.getFin()));
				break;
			case 'T':
				setPiecePosition(new Tour(mouvement.getPiece().getCouleur(), mouvement.getPiece().getaBouge(), mouvement.getFin()));
				break;
			case 'D':
				setPiecePosition(new Dame(mouvement.getPiece().getCouleur(), mouvement.getPiece().getaBouge(), mouvement.getFin()));
				break;
		}
		/*}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
			System.out.println(mouvement + " " + mouvement.getPiece());
			int a = 5/(5-5);
		}*/
		return true;
	}
	/**
	 * 
	 * @param type true = grand roque, false = petit roque
	 * @param couleur true = grand roque, false = petit roque
	 * @return si le roque est possible
	 */
	public boolean verifierRoque(boolean type, boolean couleur, PartieAbstraite partie)
	{
		PieceAbstraite roi = null, tour = null;
		//on cherche le roi de la bonne couleur n'ayant pas bouge
		if(couleur)
		{
			roi = getPiecePosition(new Position(4, 7));
		}
		else
		{
			roi = getPiecePosition(new Position(4, 0));
		}
		if(roi == null || roi.getaBouge() != 0)
		{
			return false;
		}
		if(couleur && type)
		{
			tour = getPiecePosition(new Position(0, 7));
		}
		else if(couleur && !type)
		{
			tour = getPiecePosition(new Position(7, 7));
		}
		else if(!couleur && type)
		{
			tour = getPiecePosition(new Position(0, 0));
		}
		else
		{
			tour = getPiecePosition(new Position(7, 0));
		}
		if(tour == null || tour.getaBouge() != 0)
		{
			return false;
		}
		if(partie.estEchec(roi.getPosition(), couleur))
		{
			return false;
		}
		//on a trouve les 2 pieces correspondante au roque
		
		//verifier l'existance de piece entre les deux
		if(type == true)
		{
			for(int x = tour.getPosition().getX() + 1; x < roi.getPosition().getX(); x++)
			{
				if(getPiecePosition(new Position(x, roi.getPosition().getY())) != null)
				{
					return false;
				}
				else if(Math.abs(x - roi.getPosition().getX()) <= 2 && partie.estEchec(new Position(x, roi.getPosition().getY()), couleur))
				{
					return false;
				}
			}
		}
		else if(type == false)
		{
			for(int x = roi.getPosition().getX() + 1; x < tour.getPosition().getX(); x++)
			{
				if(getPiecePosition(new Position(x, roi.getPosition().getY())) != null)
				{
					return false;
				}
				else if(partie.estEchec(new Position(x, roi.getPosition().getY()), couleur))
				{
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * renvoie le nombre de piece d'une couleur
	 * @param couleur la couleur dont on compte les piece
	 * @return le nombre de piece
	 */
	public int nombrePieceCouleur(boolean couleur)
	{
		int retour = 0;
		ListIterator<PieceAbstraite> iterator = piece.listIterator();
		while(iterator.hasNext())
		{
			if(iterator.next().getCouleur() == couleur)
			{
				retour++;
			}
		}
		return retour;
	}
	/**
	 * Renvoie la somme des valeur des pieces d'une couleur
	 * @param couleur la couleur dont on fait la somme
	 * @return la somme des valeurs des piece d'une couleur
	 */
	public int sommeValeurCouleur(boolean couleur)
	{
		int retour = 0;
		ListIterator<PieceAbstraite> iterator = piece.listIterator();
		PieceAbstraite actuel;
		while(iterator.hasNext())
		{
			actuel = iterator.next();
			if(actuel.getCouleur() == couleur)
			{
				retour += actuel.getValeur();
			}
		}
		return retour;
	}
	/**
	 * Duplique l'echequier dans une autre case memoire
	 * @return le nouvel echequier
	 */
	public Echequier clonage()
	{
		LinkedList<PieceAbstraite> tmp = new LinkedList<PieceAbstraite>();
		ListIterator<PieceAbstraite> iterator = piece.listIterator();
		while(iterator.hasNext())
		{
			tmp.add(iterator.next().clonage());
		}
		return new Echequier(longueur, hauteur, tmp);
	}
	
	//Getter et setter
	public int getLongueur() {
		return longueur;
	}
	public void setLongueur(int longueur) {
		this.longueur = longueur;
	}
	public int getHauteur() {
		return hauteur;
	}
	public void setHauteur(int hauteur) {
		this.hauteur = hauteur;
	}
	public LinkedList<PieceAbstraite> getPiece() {
		return piece;
	}
	public void setPiece(LinkedList<PieceAbstraite> piece) {
		this.piece = new LinkedList<PieceAbstraite>();
		for(int i = 0; i < piece.size(); i++)
		{
			this.piece.add(piece.get(i));
		}
	}
}
