package General;

import PieceEchec.PieceAbstraite;
import PieceEchec.Pion;

/**
 * Repertorie tout les types de coup que peut effectuer un joueur
 * @author Magoa
 *
 */

public class Coup
{
	/**
	 * La position initiale de la piece
	 */
	protected Position depart;
	/**
	 * La position finale de la piece
	 */
	protected Position fin;
	/**
	 * Indique quel piece a effectuee le coup. '' = pion, 'K' = roi, 'R' = reine, 'F' = fou, 'C' = cavalier, 'T' = tour
	 */
	protected PieceAbstraite piece;
	/**
	 * Quel action a t-elle effectuee ? '-' = mouvement simple, 'x' = prise de piece, 'o' = petit roque, 'O' = grand roque, 'p' = prise en passant, 'a' = annuler dernier coup, 'r' = rejouer dernier coup
	 * 'b' = abandon, 'n' = demande de nulle
	 */
	protected byte action;
	/**
	 * si prise il  a, qu'elle piece a etait prise.
	 */
	protected PieceAbstraite prise;

	/**
	 * indique dans le cas d'une promotion quelle est la future piece
	 */
	protected byte promotion;
	/**
	 * Correspond a la valeur du coup attribuee par l'algorithme min-max
	 */
	public Coup()
	{
		depart = fin = new Position();
		piece = null;
		action = 0;
		prise = null;
		promotion = 0;
	}
	/**
	 * 
	 * @param d
	 * 		la position de depart
	 * @param f
	 * 		la position de fin
	 * @param p
	 * 		la piece qui a joue
	 * @param a
	 * 		type d'action
	 * @param pr
	 * 		quelle piece a etait prise
	 */
	public Coup(Position d, Position f, PieceAbstraite p, byte a, PieceAbstraite pr, byte pro)
	{
		depart = fin = null;
		piece = prise = null;	
		action = a;
		promotion = pro;
		if(d != null)
		{
			depart = d.clonage();
		}
		if(f != null)
		{
			fin = f.clonage();
		}
		if(p != null)
		{
			piece = p.clonage();
		}
		if(pr != null)
		{
			prise = pr.clonage();
		}
		
	}
	/**
	 * Duplique le coup dans une autre case memoire
	 * @return un coup
	 */
	public Coup clonage()
	{
		return new Coup(depart, fin, piece, action, prise, promotion);
	}
	/**
	 * 
	 * @param coup le coup a tester
	 * @return true si le coup et le parametre son egaux
	 */
	public boolean equals(Coup coup)
	{
		if(action == coup.getAction())//la representation en cascade de if me semblait plus lisible
		{
			if((depart != null && depart.equals(coup.getDepart())) || (depart == null &&  coup.getDepart() == null))
			{
				if((fin != null && fin.equals(coup.getFin())) || (fin == null &&  coup.getFin() == null))
				{
					if((piece != null && piece.equals(coup.getPiece())) || (piece == null &&  coup.getPiece() == null) )
					{
						if((prise != null && prise.equals(coup.getPrise())) || (prise == null &&  coup.getPrise() == null) )
						{
							return true;
						}
					}
				}
			}
		}
		return false;
	}
	/**
	 * Affiche le coup en notation complete ou en fonction des code definie tel que partie nulle ou partie abandon
	 */
	public String toString()
	{
		String retour = new String();
		if(action == 'o')
		{
			return "O-O";
		}
		else if(action == 'O')
		{
			return "O-O-O";
		}
		else if(action == 'n')
		{
			return "partie nulle";
		}
		else if(action == 'b')
		{
			return "abbandon";
		}
		if(piece != null && !(piece instanceof Pion))
		{
			retour += piece.toString();
		}
		if(depart != null)
		{
			retour += (char)(depart.getX()+'a');
			retour += (char)(depart.getY()+'1');
		}
		if(action == 'p' || action == 'x')
		{
			retour += "x";
		}
		else
		{
			retour += (char)action;
		}
		
		if(fin != null)
		{
			retour += (char)(fin.getX()+'a');
			retour += (char)(fin.getY()+'1');
		}
		if(promotion != 0)
		{
			retour += '=';
			retour += (char)promotion;
		}
		if(action == 'p')
		{
			retour += " e.p";
		}
		return retour;
	}
	
	//getter et setter
	public Position getDepart() {
		return depart;
	}
	public void setDepart(Position depart) {
		this.depart = depart.clonage();
	}
	public Position getFin() {
		return fin;
	}
	public void setFin(Position fin) {
		this.fin = fin.clonage();
	}
	public PieceAbstraite getPiece() {
		return piece;
	}
	public void setPiece(PieceAbstraite piece) {
		if(piece != null)
		{
			this.piece = piece.clonage();
		}
		else
		{
			this.piece = null;
		}
	}
	public byte getAction() {
		return action;
	}
	public void setAction(byte action) {
		this.action = action;
	}
	public PieceAbstraite getPrise() {
		return prise;
	}
	public void setPrise(PieceAbstraite prise) {
		if(prise != null)
		{
			this.prise = prise.clonage();
		}
		else
		{
			this.prise = null;
		}
	}
	public byte getPromotion() {
		return promotion;
	}
	public void setPromotion(byte promotion) {
		this.promotion = promotion;
	}

}
