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

public class Cavalier extends PieceAbstraite
{
	public Cavalier()
	{
		super();
		valeur = 3;
	}
	public Cavalier(boolean couleur_ini, int aBouge_ini, Position position_ini)
	{
		super(3, couleur_ini, aBouge_ini, position_ini);
	}
	/**
	 * Voir description dans PieceAbstraite
	 * @see PieceAbstraite
	 */
	public LinkedList<Position> positionAtteignable(Echequier echequier)
	{
		LinkedList<Position> retour = new LinkedList<Position>();
		//on boucle sur l'axe des x pour parcourire les cercles des positions disponible
		//faire un dessin du mouvement du cavalier pour voir
		for(int x = position.getX() - 2; x <= position.getX() + 2; x++)
		{
			if(Math.abs(x-position.getX()) == 2)
			{
				retour.add(new Position(x, position.getY()+1));
				retour.add(new Position(x, position.getY()-1));
			}
			else if(Math.abs(x-position.getX()) == 1)
			{
				retour.add(new Position(x, position.getY()+2));
				retour.add(new Position(x, position.getY()-2));
			}
		}
		
		//On verifie si les positions sont valides sinon, on les retire
		ListIterator<Position> iterator = retour.listIterator();
		while(iterator.hasNext())
		{
			if(echequier.positionPossible(iterator.next()) == false)
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
		return new Cavalier(couleur, aBouge, position);
	}
	public boolean equals(PieceAbstraite piece)
	{
		if( super.equals(piece) && piece instanceof Cavalier)
		{
			return true;
		}
		return false;
	}
	public String toString()
	{
		return "C";
	}
}