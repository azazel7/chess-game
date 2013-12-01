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
public class Dame extends PieceAbstraite
{
	public Dame()
	{
		super();
		valeur = 10;
	}
	public Dame(boolean couleur_ini, int aBouge_ini, Position position_ini)
	{
		super(10, couleur_ini, aBouge_ini, position_ini);
	}
	
	public LinkedList<Position> positionAtteignable(Echequier echequier)
	{
		Fou mouvement_fou = new Fou(couleur, aBouge, position);
		Tour mouvement_tour = new Tour(couleur, aBouge, position);
		
		LinkedList<Position> retour = mouvement_fou.positionAtteignable(echequier), tour = mouvement_tour.positionAtteignable(echequier);
		ListIterator<Position> iterator = tour.listIterator();
		while(iterator.hasNext())
		{
			retour.add(iterator.next());
		}
		return retour;
	}
	public LinkedList<Position> positionAtteignableEchequier(Echequier echequier)
	{
		Fou mouvement_fou = new Fou(couleur, aBouge, position);
		Tour mouvement_tour = new Tour(couleur, aBouge, position);
		
		LinkedList<Position> retour = mouvement_fou.positionAtteignableEchequier(echequier), tour = mouvement_tour.positionAtteignableEchequier(echequier);
		ListIterator<Position> iterator = tour.listIterator();
		while(iterator.hasNext())
		{
			retour.add(iterator.next());
		}
		return retour;
	}
	
	public PieceAbstraite clonage()
	{
		return new Dame(couleur, aBouge, position);
	}
	public boolean equals(PieceAbstraite piece)
	{
		if( super.equals(piece) && piece instanceof Dame)
		{
			return true;
		}
		return false;
	}
	public String toString()
	{
		return "D";
	}
}