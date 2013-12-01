package Affichage;

import java.util.Scanner;

import javax.swing.JOptionPane;

import General.Position;
import JoueurEchec.Joueur;
import Partie.PartieAbstraite;
import PieceEchec.Cavalier;
import PieceEchec.Fou;
import PieceEchec.PieceAbstraite;
import PieceEchec.Pion;
import PieceEchec.Dame;
import PieceEchec.Roi;
import PieceEchec.Tour;


/**
 * Affichage qui affiche une partie normal
 * @author Flaubert
 *
 */
public class AffichageNormal extends AffichageAbstrait
{
	public AffichageNormal ()
	{
		super();
	}
	public AffichageNormal (Panneau panel)
	{
		super(panel);
	}
	public void affichagePartie(PartieAbstraite partie)
	{
		if(paneau == null)
		{
			try
			{
				for(int y = 7; y >= 0; y--)
				{
					System.out.print("|");
					for(int x = 0; x < 8; x++)
					{
						System.out.print((char)affichage_ascii(partie.getPlateau().getPiecePosition(new Position(x, y))) + "|");
					}
					System.out.println(" " + (y+1));
				}
				System.out.print(" ");
				for(int x = 0; x < 8; x++)
				{
					System.out.print( (char)(x+'a') + " ");
				}
				System.out.println();
				System.out.println("Nombre de coup: " + partie.getListeCoup().size());
			}
			catch(Exception e)
			{
				System.out.println(e.getMessage());
			}
			return;
		}
		//code GUI
		paneau.setPartie(partie);
		paneau.repaint();
	}
	/**
	 * Transcrit une piece en caractére ascii
	 * @param piece la piece à transposer
	 * @return la corespondance en ascii
	 */
	public static byte affichage_ascii(PieceAbstraite piece)
	{
		byte diff = 0;
		if(piece == null)
		{
			return (byte)' ';
		}
		if(piece.getCouleur() == false)
		{
			diff = 32;
		}
		if(piece instanceof Roi)
		{
			return (byte)('R'+diff);
		}
		else if(piece instanceof Dame)
		{
			return (byte)('D'+diff);
		}
		else if(piece instanceof Cavalier)
		{
			return (byte)('C'+diff);
		}
		else if(piece instanceof Fou)
		{
			return (byte)('F'+diff);
		}
		else if(piece instanceof Tour)
		{
			return (byte)('T'+diff);
		}
		else if(piece instanceof Pion)
		{
			return (byte)('P'+diff);
		}
		return (byte)' ';
	}

	public void afficherVictoire(Joueur joueur)
	{
		if(paneau == null)
		{
			System.out.println("C'est " + joueur.getNom() + " qui remporte la partie.");
			return;
		}
		JOptionPane.showMessageDialog(null, "C'est " + joueur.getNom() + " qui remporte la partie.", "Victoire", JOptionPane.INFORMATION_MESSAGE);
	}
	public void afficherErreur(String chaine)
	{
		if(paneau == null)//code console
		{
			System.out.println("erreur(" + chaine + ")");
			return;
		}
		JOptionPane.showMessageDialog(null, "erreur(" + chaine + ")", "Erreur", JOptionPane.ERROR_MESSAGE);
	}
	public void afficherChaine(String chaine)
	{
		if(paneau == null)//code console
		{
			System.out.print(chaine);
			return;
		}
	}
	public void afficherPop(String chaine)
	{
		if(paneau == null)
		{
			afficherChaine(chaine);
			return;
		}
		JOptionPane.showMessageDialog(null, chaine, "Information", JOptionPane.INFORMATION_MESSAGE);
	}
	public String SaisirChaine()
	{
		if(paneau == null)
		{
			Scanner sc = new Scanner(System.in);
			return sc.nextLine();
		}
		String retour = null;
		try
		{
			while(saisie == null)
			{
				Thread.sleep(100);
			}
			retour = saisie;
			saisie = null;
		}
		catch(Exception e)
		{
			this.afficherErreur(e.getMessage());
		}
		return retour;
	}
	public AffichageAbstrait clonage()
	{
		return new AffichageNormal(paneau);
	}
}
