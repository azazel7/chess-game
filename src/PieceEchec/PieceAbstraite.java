package PieceEchec;

import java.util.LinkedList;
import java.util.ListIterator;

import General.Echequier;
import General.Position;

/**
 * Classe de laquelle decoule toutes les pieces des echecs.
 * @author Zeus
 *
 */
public abstract class PieceAbstraite
{
	/**
	 * Communement, un pion = 1, un fou = un cavalier = 3, une tour = 5 et une reine = 10
	 */
	protected int valeur;
	/**
	 * Indique s'il est blanc ou noir
	 * true = noir
	 * false = blanc
	 */
	protected boolean couleur;
	/**
	 * Indique si la piece s'est deplace
	 * 0 = elle ne s'est pas deplacee
	 */
	protected int aBouge;
	/**
	 * Indique la position actuel de la piece
	 */
	protected Position position;
	
	//constructeur
	/**
	 * Constructeur de base
	 */
	public PieceAbstraite()
	{
		couleur = false;
		aBouge = 0;
		valeur = 0;
		position = null;
	}
	/**
	 * 
	 * @param valeur_ini initialise la valeur de la piece
	 * @param couleur_ini initialise la couleur
	 * @param aBouge_ini initialise aBouge
	 * @param position_ini initialise la position de la piece
	 */
	public PieceAbstraite(int valeur_ini, boolean couleur_ini, int aBouge_ini, Position position_ini)
	{
		valeur = valeur_ini;
		couleur = couleur_ini;
		aBouge = aBouge_ini;
		position = null;
		if(position_ini != null)
		{
			position = position_ini.clonage();
		}
		
	}
	
	//methode defini
	/**
	 * 
	 * @param liste Une liste de Position
	 * @param cible La position a supprimer de la liste
	 */
	public void supprime_position(LinkedList<Position> liste, Position cible)
	{
		ListIterator<Position> iterator = liste.listIterator();
		while(iterator.hasNext())
		{
			if(iterator.next().equals(cible))
			{
				iterator.remove();
				return;
			}
		}
	}
	//methode abstraite
	/**
	 * 
	 * @param echequier echequier sur lequel se trouve la piece
	 * @return	liste des positions que la piece peut theoriquement atteindre
	 */
	public abstract LinkedList<Position> positionAtteignable(Echequier echequier);
	/**
	 * 
	 * @param echequier echequier sur lequel se trouve la piece
	 * @return liste des positions que la piece peut atteindre en tenant compte des pieces
	 */
	public abstract LinkedList<Position> positionAtteignableEchequier(Echequier echequier);
	/**
	 * 
	 * @param echequier echequier sur lequel se trouve la piece
	 * @return La liste des pieces que la piece peut prendre durant ce tour ranger par position atteignable. Si la position ne donne aucune piece, la case vaut null
	 */
	public LinkedList<Position> prisePiece(Echequier echequier)
	{
		LinkedList<Position> retour = positionAtteignableEchequier(echequier);
		ListIterator<Position> iterator = retour.listIterator();
		while(iterator.hasNext())
		{
			echequier.getPiecePosition(iterator.next());
			if(echequier.getPiecePosition(iterator.next()) == null)
			{
				iterator.remove();
			}
		}
		return retour;
	}
	/**
	 * Duplique l'objet
	 * @return retourne la piece clonnee
	 */
	public abstract PieceAbstraite clonage();
	public boolean equals(PieceAbstraite piece)
	{
		if( piece != null && valeur == piece.getValeur() && couleur == piece.getCouleur() && aBouge == piece.getaBouge())
		{
			if( (position != null && position.equals(piece.getPosition()) || (position == null && piece.getPosition() == null)))
			{
				return true;
			}
		}
		return false;
	}
	//getter et setter
	public int getValeur() {
		return valeur;
	}
	public void setValeur(int valeur) {
		this.valeur = valeur;
	}
	public boolean getCouleur() {
		return couleur;
	}
	public void setCouleur(boolean couleur) {
		this.couleur = couleur;
	}
	public int getaBouge() {
		return aBouge;
	}
	public void setaBouge(int aBouge) {
		this.aBouge = aBouge;
	}
	public Position getPosition() {
		return position;
	}
	public void setPosition(Position position) {
		this.position = position.clonage();
	}
	
}