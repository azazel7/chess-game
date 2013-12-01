package PieceEchec;

import java.util.LinkedList;
import java.util.ListIterator;

import General.Echequier;
import General.Position;

/**
 * 
 * @author Zeus
 *
 */
public class Roi extends PieceAbstraite
{
	public Roi()
	{
		super();
		valeur = 15;
	}
	public Roi(boolean couleur_ini, int aBouge_ini, Position position_ini)
	{
		super(15, couleur_ini, aBouge_ini, position_ini);
	}
	/**
	 * Voir description dans PieceAbstraite
	 * @see PieceAbstraite
	 */
	public LinkedList<Position> positionAtteignable(Echequier echequier)
	{
		LinkedList<Position> retour = new LinkedList<Position>();
		//on ajoute toute les position autour du roi
		for(int x = position.getX() - 1; x <= position.getX()+1; x++)
		{
			for(int y = position.getY() - 1; y <= position.getY()+1; y++)
			{
				retour.add(new Position(x, y));
			}
		}
		//on retire la position du roi
		ListIterator<Position> iterator = retour.listIterator();
		Position actuel;
		while(iterator.hasNext())
		{
			actuel = iterator.next();
			if(echequier.positionPossible(actuel) == false || position.equals(actuel))
			{
				iterator.remove();
			}
		}
		return retour;
	}
	/**
	 * Voir description dans PieceAbstraite
	 * @see PieceAbstraite
	 */
	public LinkedList<Position> positionAtteignableEchequier(Echequier echequier)
	{
		LinkedList<Position> retour = positionAtteignable(echequier);
		PieceAbstraite piece;
		ListIterator<Position> iterator = retour.listIterator();
		while(iterator.hasNext())
		{
			piece = echequier.getPiecePosition(iterator.next());
			if(piece != null && piece.getCouleur() == this.couleur)
			{
				iterator.remove();
			}
		}
		return retour;
	}
	public PieceAbstraite clonage()
	{
		return new Roi(couleur, aBouge, position);
	}
	public boolean equals(PieceAbstraite piece)
	{
		if( super.equals(piece) && piece instanceof Roi)
		{
			return true;
		}
		return false;
	}
	public String toString()
	{
		return "R";
	}
}