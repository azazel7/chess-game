package PieceEchec;

import java.util.LinkedList;

import General.Echequier;
import General.Position;
/**
 * 
 * @author Zeus
 *
 */

public class Tour extends PieceAbstraite
{
	//constructeur
	public Tour()
	{
		super();
		valeur = 5;
	}
	public Tour(boolean couleur_ini, int aBouge_ini, Position position_ini)
	{
		super(5, couleur_ini, aBouge_ini, position_ini);
	}
	/**
	 * Voir description dans PieceAbstraite
	 * @see PieceAbstraite
	 */
	public LinkedList<Position> positionAtteignable(Echequier echequier)
	{
		LinkedList<Position> retour = new LinkedList<Position>();
		for(int x = 0; x < echequier.getLongueur(); x++)
		{
			if(x != position.getX())
			{
				retour.add(new Position(x, position.getY()));
			}
		}
		for(int y = 0; y < echequier.getHauteur(); y++)
		{
			if(y != position.getY())
			{
				retour.add(new Position(position.getX(), y));
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
		LinkedList<Position> retour = this.positionAtteignable(echequier);
		PieceAbstraite piece;
		boolean detruire = false;
		//on parcour les 4 directions en partant de la tour
		for(int x = position.getX()+1; x < echequier.getLongueur(); x++) //ligne droite de la tour
		{
			piece =  echequier.getPiecePosition(new Position(x, position.getY()));
			if(piece != null)//une piece sur la ligne droite
			{
				if(piece.getCouleur() != couleur && detruire == false)//on supprime la position des piece derri�re celle ci
				{
					x++;
				}
				detruire = true;
			}
			if(detruire)
			{
				supprime_position(retour, new Position(x, position.getY()));
			}
		}
		detruire = false;
		for(int x = position.getX()-1; x >= 0; x--) //ligne gauche de la tour
		{
			piece =  echequier.getPiecePosition(new Position(x, position.getY()));
			if(piece != null)//une piece sur la ligne gauche
			{
				if(piece.getCouleur() != couleur && detruire == false)//on supprime la position des piece derri�re celle ci
				{
					x--;
				}
				detruire = true;
			}
			if(detruire)
			{
				supprime_position(retour, new Position(x, position.getY()));
			}
		}
		detruire = false;
		for(int y = position.getY()-1; y >= 0; y--) //colone haute de la tour
		{
			piece =  echequier.getPiecePosition(new Position(position.getX(), y));
			if(piece != null)//une piece sur la colone haute
			{
				if(piece.getCouleur() != couleur && detruire == false)//on supprime la position des piece derri�re celle ci
				{
					y--;
				}
				detruire = true;
			}
			if(detruire)
			{
				supprime_position(retour, new Position(position.getX(), y));
			}
		}
		detruire = false;
		for(int y = position.getY()+1; y < echequier.getHauteur(); y++) //colone basse de la tour
		{
			piece =  echequier.getPiecePosition(new Position(position.getX(), y));
			if(piece != null)//une piece sur la colone basse
			{
				if(piece.getCouleur() != couleur && detruire == false)//on supprime la position des piece derri�re celle ci
				{
					y++;
				}
				detruire = true;
			}
			if(detruire)
			{
				supprime_position(retour, new Position(position.getX(), y));
			}
		}
		return retour;
	}
	
	public PieceAbstraite clonage()
	{
		return new Tour(couleur, aBouge, position);
	}
	public boolean equals(PieceAbstraite piece)
	{
		if( super.equals(piece) && piece instanceof Tour)
		{
			return true;
		}
		return false;
	}
	public String toString()
	{
		return "T";
	}
}