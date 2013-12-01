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
public class Pion extends PieceAbstraite
{
	public Pion()
	{
		super();
		valeur = 1;
	}
	public Pion(boolean couleur_ini, int aBouge_ini, Position position_ini)
	{
		super(1, couleur_ini, aBouge_ini, position_ini);
	}
	/**
	 * Voir description dans PieceAbstraite
	 * @see PieceAbstraite
	 */
	public LinkedList<Position> positionAtteignable(Echequier echequier)
	{
		LinkedList<Position> retour = new LinkedList<Position>();
		PieceAbstraite piece;
		if(couleur == false)
		{
			retour.add(new Position(position.getX(), position.getY()+1));
			if(aBouge == 0)
			{
				retour.add(new Position(position.getX(), position.getY()+2));
			}
			retour.add(new Position(position.getX()+1, position.getY()+1));
			retour.add(new Position(position.getX()-1, position.getY()+1));
		}
		else
		{
			retour.add(new Position(position.getX(), position.getY()-1));
			if(aBouge == 0)
			{
				retour.add(new Position(position.getX(), position.getY()-2));
			}
			retour.add(new Position(position.getX()+1, position.getY()-1));
			retour.add(new Position(position.getX()-1, position.getY()-1));
		}
		for(int i = 0; i < 2; i ++)
		{
			piece = echequier.getPiecePosition(retour.getLast());
			if(piece == null || piece.getCouleur() == this.couleur)
			{
				retour.removeLast();
			}
		}
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
		for(int i = 0; i < retour.size(); i++)
		{
			piece = echequier.getPiecePosition(retour.get(i));
			if(piece != null && piece.getPosition().getX() == position.getX()) //il y a une piece, il faut la supprimer
			{
				if(couleur == false)
				{
					supprime_position(retour, new Position(position.getX(), piece.getPosition().getY()+1));//suprimmer de la liste les position (position.getX(), retour.get(i).getY()+1)
					// Si et seulement si elle existe
				}
				else
				{
					supprime_position(retour, new Position(position.getX(), piece.getPosition().getY()-1));//suprimmer de la liste les position (position.getX(), retour.get(i).getY()-1)
					// Si et seulement si elle existe
				}
				
				supprime_position(retour, new Position(position.getX(), piece.getPosition().getY()));//on retire la position trouv�
				i--;
			}
		}
		return retour;
	}
	
	public PieceAbstraite clonage()
	{
		return new Pion(couleur, aBouge, position);
	}
	public boolean equals(PieceAbstraite piece)
	{
		if( super.equals(piece) && piece instanceof Pion)
		{
			return true;
		}
		return false;
	}
	public String toString()
	{
		return "P";
	}
}