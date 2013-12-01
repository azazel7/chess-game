package JoueurEchec;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import General.Coup;
import General.Position;
import Partie.PartieAbstraite;
import PieceEchec.Cavalier;
import PieceEchec.Dame;
import PieceEchec.Fou;
import PieceEchec.PieceAbstraite;
import PieceEchec.Pion;
import PieceEchec.Roi;
import PieceEchec.Tour;

/*
 * 	O-O -> petit roque
 * 	O-O-O -> grand roque
 * algebrique complete
 * 			[A-Z][a-h][1-8][\-x][a-h][1-8][A-Z][e.p]
 * 			[piece qui joue (facultatif)][depart][action][fin][promotion (s'il y a) (facultatif)][indique prise en passant]
 * 			^([RDFCTP])?([a-h])([1-8])[\\-x]([a-h])([1-8])([RDFCT])?[e.p!\\?+#x=\\-,[0-9] /]*$
 * algebrique abrïegee
 * 			[A-Z][[a-h][1-8]][a-h][1-8][=][A-Z][e.p]
 * 			[la piece][colone ou rangï¿½ si necessaire][fin][promotion][prise en passant]
 * 			^([RDFCTP])?([a-h1-8])?([a-h])([1-8])=?([RDFCT])?[e.p!\\?+#x=\\-,[0-9] /]*$
 * 
 * 
 * 	[e.p-!-\\?-+-#-x-=-\\--,-[0-9]- -/]*
 */

/**
 * @author Tzeentch
 */

public class Humain extends Joueur
{
	/**
	 * Constructeur initialisant le nom
	 * @param nom le nom initial
	 */
	public Humain(String nom)
	{
		super(nom);
	}
	/**
	 * Cette methode traite une chaine de caractére, puis retourne un coup. La chaine peut être en notation algébrique compléte ou abrégée.
	 * Les coup spéciaux tel que partie nulle ou partie abandon sont aussi traités par cette methode
	 * @param chaine la chaine à etudier
	 * @param partie la partie sur laquel le coup sera joué
	 * @return le coup
	 */
	public static Coup traitement(String chaine, PartieAbstraite partie)
	{
		Pattern abrege, complete;
		Matcher mabrege, mcomplete;
		abrege = Pattern.compile("^([RDFCTP])?([a-h1-8])?([a-h])([1-8])=?([RDFCT])?[e.p!\\?+#x=\\-,[0-9] /]*$");
		mabrege = abrege.matcher(chaine);
		complete = Pattern.compile("^([RDFCTP])?([a-h])([1-8])[\\-x]([a-h])([1-8])=?([RDFCT])?[e.p!\\?+#x=\\-,[0-9] /]*$");
		mcomplete = complete.matcher(chaine);
		byte promotion = 0;
		Coup retour = null;
		if(mcomplete.matches() == true)
		{
			Position depart = new Position((int)mcomplete.group(2).charAt(0) - (int)'a', (int)mcomplete.group(3).charAt(0) - (int)'1'), 
					 fin    = new Position((int)mcomplete.group(4).charAt(0) - (int)'a', (int)mcomplete.group(5).charAt(0) - (int)'1');
			PieceAbstraite piece = partie.getPlateau().getPiecePosition(depart);
			
			if(mcomplete.group(6) != null)
			{
				promotion = (byte)mcomplete.group(6).charAt(0);
			}
			if(mcomplete.group(1) == null && ! (piece instanceof Pion))
			{
				return new Coup(null, null, null, (byte)0, null, (byte)0);
			}
			else if(mcomplete.group(1) != null && piece != null && ! mcomplete.group(1).equals(piece.toString()))
			{
				return new Coup(null, null, null, (byte)0, null, (byte)0);
			}
			retour = new Coup(depart, fin, piece.clonage(), (byte)'-',  partie.getPlateau().getPiecePosition(fin), promotion);
			if(retour.getPrise() != null)
			{
				retour.setAction((byte)'x');
				retour.setPrise(retour.getPrise().clonage());
			}
		}
		else if(mabrege.matches() == true)
		{
			Position fin = new Position((int)mabrege.group(3).charAt(0) - (int)'a', (int)mabrege.group(4).charAt(0) - (int)'1');
			LinkedList<PieceAbstraite> piece = new LinkedList<PieceAbstraite>();
			LinkedList<Position> listeFin;
			boolean trouve;
			for(int i = 0; i < partie.getPlateau().getPiece().size(); i++)//on recopie la liste de piece uniquement de la bonne couleur
			{
				if(partie.getListeCoup().size()%2 == 1 && partie.getPlateau().getPiece().get(i).getCouleur() == true)
				{
					piece.add(partie.getPlateau().getPiece().get(i));
				}
				else if(partie.getListeCoup().size()%2 == 0 && partie.getPlateau().getPiece().get(i).getCouleur() == false)
				{
					piece.add(partie.getPlateau().getPiece().get(i));
				}
			}
			for(int i = 0; i < piece.size(); i++)//on supprime tout ceux qui ne sont pas du meme type
			{
				if(mabrege.group(1) == null && !(piece.get(i) instanceof Pion))
				{
					piece.remove(i);
					i--;
				}
				else if(mabrege.group(1) != null && mabrege.group(1).charAt(0) == 'R' && !(piece.get(i) instanceof Roi))
				{
					piece.remove(i);
					i--;
				}
				else if(mabrege.group(1) != null && mabrege.group(1).charAt(0) == 'D' && !(piece.get(i) instanceof Dame))
				{
					piece.remove(i);
					i--;
				}
				else if(mabrege.group(1) != null && mabrege.group(1).charAt(0) == 'C' && !(piece.get(i) instanceof Cavalier))
				{
					piece.remove(i);
					i--;
				}
				else if(mabrege.group(1) != null && mabrege.group(1).charAt(0) == 'F' && !(piece.get(i) instanceof Fou))
				{
					piece.remove(i);
					i--;
				}
				else if(mabrege.group(1) != null && mabrege.group(1).charAt(0) == 'T' && !(piece.get(i) instanceof Tour))
				{
					piece.remove(i);
					i--;
				}
				else if(mabrege.group(1) != null && mabrege.group(1).charAt(0) == 'P' && !(piece.get(i) instanceof Pion))
				{
					piece.remove(i);
					i--;
				}
			}
			//on verifi les information bonus
			if(mabrege.group(2) != null && mabrege.group(2).charAt(0) >= 'a' && mabrege.group(2).charAt(0) <= 'h')//on balaye les x
			{
				for(int i = 0; i < piece.size(); i++)
				{
					if(piece.get(i).getPosition().getX() + 'a' != mabrege.group(2).charAt(0))
					{
						piece.remove(i);
						i--;
					}
				}
			}
			else if(mabrege.group(2) != null && mabrege.group(2).charAt(0) >= '1' && mabrege.group(2).charAt(0) <= '8')//on balaye les y
			{
				for(int i = 0; i < piece.size(); i++)
				{
					if(piece.get(i).getPosition().getY() + '1' != mabrege.group(2).charAt(0))
					{
						piece.remove(i);
						i--;
					}
				}
			}
			//on verifi la position d'arrivee
			for(int i = 0; i < piece.size(); i++)
			{
				trouve = false;
				listeFin = piece.get(i).positionAtteignableEchequier(partie.getPlateau());
				for(int j = 0; j < listeFin.size(); j++)
				{
					if(listeFin.get(j).equals(fin))
					{
						trouve = true;
					}
				}
				if(trouve == false)
				{
					piece.remove(i);
					i--;
				}
			}
			if(mabrege.group(5) != null)
			{
				promotion = (byte)mabrege.group(5).charAt(0);
			}
			if(piece.size() != 1)//on teste la prise en passant
			{
				LinkedList<Coup> listeCoup = null;
				if(partie.getListeCoup().size()%2 == 0)
				{
					listeCoup = partie.listeCoupPrimaire(false);
				}
				else
				{
					listeCoup = partie.listeCoupPrimaire(true);
				}
				for(int i = 0; i < listeCoup.size(); i++)
				{
					if(listeCoup.get(i).getAction() != 'p' || ! listeCoup.get(i).getFin().equals(fin))
					{
						listeCoup.remove(i);
						i--;
					}
				}
				//on verifi les information bonus
				if(mabrege.group(2) != null && mabrege.group(2).charAt(0) >= 'a' && mabrege.group(2).charAt(0) <= 'h')//on balaye les x
				{
					for(int i = 0; i < listeCoup.size(); i++)
					{
						if(listeCoup.get(i).getDepart().getX() + 'a' != mabrege.group(2).charAt(0))
						{
							listeCoup.remove(i);
							i--;
						}
					}
				}
				else if(mabrege.group(2) != null && mabrege.group(2).charAt(0) >= '1' && mabrege.group(2).charAt(0) <= '8')//on balaye les y
				{
					for(int i = 0; i < listeCoup.size(); i++)
					{
						if(listeCoup.get(i).getDepart().getY() + '1' != mabrege.group(2).charAt(0))
						{
							listeCoup.remove(i);
							i--;
						}
					}
				}
				if(listeCoup.size() != 1)
				{
					return new Coup(null, null, null, (byte)0, null, (byte)0);
				}
				retour = listeCoup.get(0);
				retour.setPrise(null);
				
			}
			if(retour == null)
			{
				retour = new Coup(piece.get(0).getPosition(), fin, piece.get(0).clonage(), (byte)'-',  partie.getPlateau().getPiecePosition(fin), promotion);
				if(retour.getPrise() != null)
				{
					retour.setAction((byte)'x');
					retour.setPrise(retour.getPrise().clonage());
				}
				
			}
		}
		else if(chaine.equals("O-O") || chaine.equals("o-o"))
		{
			if(partie.getListeCoup().size()%2 == 1)
			{
				retour = new Coup(null, null, new Roi(true, 0, null), (byte)'o', null, (byte)0);
			}
			else
			{
				retour = new Coup(null, null, new Roi(false, 0, null), (byte)'o', null, (byte)0);
			}
		}
		else if(chaine.equals("O-O-O") || chaine.equals("o-o-o"))
		{
			if(partie.getListeCoup().size()%2 == 1)
			{
				retour = new Coup(null, null, new Roi(true, 0, null), (byte)'O', null, (byte)0);
			}
			else
			{
				retour = new Coup(null, null, new Roi(false, 0, null), (byte)'O', null, (byte)0);
			}
		}
		else if(chaine.equals("r"))//rejouer -> pas de reseau
		{
			retour = new Coup(null, null, null, (byte)'r', null, (byte)0);
		}
		else if(chaine.equals("a"))//annuler -> pas de reseau
		{
			retour = new Coup(null, null, null, (byte)'a', null, (byte)0);
		}
		else if(chaine.equals("s"))//sauvegarder
		{
			retour = new Coup(null, null, null, (byte)'s', null, (byte)0);
		}
		else if(chaine.equals("partie nulle"))
		{
			retour = new Coup(null, null, null, (byte)'n', null, (byte)0);
		}
		else if(chaine.equals("partie abandon"))
		{
			retour = new Coup(null, null, null, (byte)'b', null, (byte)0);
		}
		else
		{
			retour = new Coup(null, null, null, (byte)0, null, (byte)0);
		}
		return retour;
	}
	/**
	 * Interface, permet la saisie d'un coup syntaxiquement valide
	 */
	public Coup jouerCoup(PartieAbstraite partie)
	{
		Coup retour = new Coup();
		String commande;
		boolean coup_correct;
		do
		{
			coup_correct = true;
			partie.getAffichage().afficherChaine(nom + ": ");
			commande = partie.getAffichage().SaisirChaine();
			retour = traitement(commande, partie);
			if(retour.getAction() == 0)
			{
				 partie.getAffichage().afficherErreur("Coup invalide");
				 coup_correct = false;
			}
		}while(!coup_correct);//on saisie une commande correcte et on la traite puis on l'envoi pour la valider a la partie
		return retour;
	}
	public Joueur clonage()
	{
		return new Humain(nom);
	}
}