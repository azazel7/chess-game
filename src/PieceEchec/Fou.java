package PieceEchec;

import java.util.LinkedList;

import General.Echequier;
import General.Position;

/**
 * 
 * @author Zeus
 *
 */
public class Fou extends PieceAbstraite
{
	public Fou()
	{
		super();
		valeur = 3;
	}
	public Fou(boolean couleur_ini, int aBouge_ini, Position position_ini)
	{
		super(3, couleur_ini, aBouge_ini, position_ini);
	}
	public LinkedList<Position> positionAtteignable(Echequier echequier)
	{
		LinkedList<Position> retour = new LinkedList<Position>();
		int x, y;
		x = position.getX()+1;//bas droite
		y = position.getY()+1;
		while(echequier.positionPossible(new Position(x, y)))
		{
			retour.add(new Position(x, y));
			x++;
			y++;
		}
		x = position.getX()-1;//bas gauche
		y = position.getY()+1;
		while(echequier.positionPossible(new Position(x, y)))
		{
			retour.add(new Position(x, y));
			x--;
			y++;
		}
		x = position.getX()-1;//haut gauche
		y = position.getY()-1;
		while(echequier.positionPossible(new Position(x, y)))
		{
			retour.add(new Position(x, y));
			x--;
			y--;
		}
		x = position.getX()+1;//haut droit
		y = position.getY()-1;
		while(echequier.positionPossible(new Position(x, y)))
		{
			retour.add(new Position(x, y));
			x++;
			y--;
		}
		return retour;
	}
	public LinkedList<Position> positionAtteignableEchequier(Echequier echequier)
	{
		LinkedList<Position> retour = positionAtteignable(echequier);
		int x, y;
		PieceAbstraite piece;
		boolean detruire = false;
		x = position.getX()+1;//bas droite
		y = position.getY()+1;
		while(echequier.positionPossible(new Position(x, y)))
		{
			piece = echequier.getPiecePosition(new Position(x, y));
			if(piece != null)//une piece sur la diagonal
			{
				if(piece.getCouleur() != couleur && detruire == false)//de celui de l'ennement
				{
					x++;
					y++;
				}
				detruire = true;
			}
			if(detruire)
			{
				supprime_position(retour, new Position(x, y));
			}
			x++;
			y++;
		}
		detruire = false;
		x = position.getX()-1;//bas gauche
		y = position.getY()+1;
		while(echequier.positionPossible(new Position(x, y)))
		{
			piece = echequier.getPiecePosition(new Position(x, y));
			if(piece != null)//une piece sur la diagonal
			{
				if(piece.getCouleur() != couleur && detruire == false)//de celui de l'ennement
				{
					x--;
					y++;
				}
				detruire = true;
			}
			if(detruire)
			{
				supprime_position(retour, new Position(x, y));
			}
			x--;
			y++;
		}
		detruire = false;
		x = position.getX()-1;//bas gauche
		y = position.getY()-1;
		while(echequier.positionPossible(new Position(x, y)))
		{
			piece = echequier.getPiecePosition(new Position(x, y));
			if(piece != null)//une piece sur la diagonal
			{
				if(piece.getCouleur() != couleur && detruire == false)//de celui de l'ennement
				{
					x--;
					y--;
				}
				detruire = true;
			}
			if(detruire)
			{
				supprime_position(retour, new Position(x, y));
			}
			x--;
			y--;
		}
		detruire = false;
		x = position.getX()+1;//bas gauche
		y = position.getY()-1;
		while(echequier.positionPossible(new Position(x, y)))
		{
			piece = echequier.getPiecePosition(new Position(x, y));
			if(piece != null)//une piece sur la diagonal
			{
				if(piece.getCouleur() != couleur && detruire == false)//de celui de l'ennement
				{
					x++;
					y--;
				}
				detruire = true;
			}
			if(detruire)
			{
				supprime_position(retour, new Position(x, y));
			}
			x++;
			y--;
		}
		return retour;
	}
	
	public PieceAbstraite clonage()
	{
		return new Fou(couleur, aBouge, position);
	}
	public boolean equals(PieceAbstraite piece)
	{
		if( super.equals(piece) && piece instanceof Fou)
		{
			return true;
		}
		return false;
	}
	public String toString()
	{
		return "F";
	}
}