package Affichage;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import General.Position;
import Partie.PartieAbstraite;
import PieceEchec.Cavalier;
import PieceEchec.Dame;
import PieceEchec.Fou;
import PieceEchec.PieceAbstraite;
import PieceEchec.Roi;
import PieceEchec.Tour;
/**
 * Classe affichant le contenu d'une partie (le plateau, les pieces, les noms et le nombre de coup)
 * @author Dwayna
 *
 */
public class Panneau extends JPanel
{  
	
	private static final long serialVersionUID = 1L;
	/**
	 * La partie à afficher
	 */
	private PartieAbstraite partie;
	/**
	 * Ayant échoué dans ma quéte de la transparence du fond, j'ai du me rabaisser à créer un second jeu d'image avec le fond de couleur marron pour ce genre de case
	 * C'est pourquoi il y a deux tableau
	 */
	private Image piece_img[][] = new Image[6][2];
	private Image piece_imgm[][] = new Image[6][2];
	/**
	 * Constructeur qui charge les images et affiche la partie s'il en a une en paramétre
	 * @param partie_ini la partie initial
	 */
    public Panneau(PartieAbstraite partie_ini)
    {
    	super();
    	partie = partie_ini;//on stock les adresses
    	try
    	{
    		//chargement image fond blanc
            piece_img[0][0] = ImageIO.read(new File("pion.PNG"));
    		piece_img[1][0] = ImageIO.read(new File("tour.PNG"));
    		piece_img[2][0] = ImageIO.read(new File("cavalier.PNG"));
    		piece_img[3][0] = ImageIO.read(new File("fou.PNG"));
    		piece_img[4][0] = ImageIO.read(new File("dame.PNG"));
    		piece_img[5][0] = ImageIO.read(new File("roi.PNG"));
    		
    		piece_img[0][1] = ImageIO.read(new File("pion2.PNG"));
    		piece_img[1][1] = ImageIO.read(new File("tour2.PNG"));
    		piece_img[2][1] = ImageIO.read(new File("cavalier2.PNG"));
    		piece_img[3][1] = ImageIO.read(new File("fou2.PNG"));
    		piece_img[4][1] = ImageIO.read(new File("dame2.PNG"));
    		piece_img[5][1] = ImageIO.read(new File("roi2.PNG"));
    		
    		//chargement image fond marron
    		piece_imgm[0][0] = ImageIO.read(new File("pionm.PNG"));
    		piece_imgm[1][0] = ImageIO.read(new File("tourm.PNG"));
    		piece_imgm[2][0] = ImageIO.read(new File("cavalierm.PNG"));
    		piece_imgm[3][0] = ImageIO.read(new File("foum.PNG"));
    		piece_imgm[4][0] = ImageIO.read(new File("damem.PNG"));
    		piece_imgm[5][0] = ImageIO.read(new File("roim.PNG"));
    		
    		piece_imgm[0][1] = ImageIO.read(new File("pion2m.PNG"));
    		piece_imgm[1][1] = ImageIO.read(new File("tour2m.PNG"));
    		piece_imgm[2][1] = ImageIO.read(new File("cavalier2m.PNG"));
    		piece_imgm[3][1] = ImageIO.read(new File("fou2m.PNG"));
    		piece_imgm[4][1] = ImageIO.read(new File("dame2m.PNG"));
    		piece_imgm[5][1] = ImageIO.read(new File("roi2m.PNG"));
    	}
    	catch(Exception e)
    	{
    		JOptionPane.showMessageDialog(null, "Erreur lors de la recuperation des fichiers image.", "Erreur", JOptionPane.ERROR_MESSAGE);
    	}
    }
    /**
     * A chaque appel de cette fonction, l'objet doit repaindre la partie courante si elle n'est pas null
     */
    public void paintComponent(Graphics g)//repaint partie or partie est celle passe en argument, donc des qu'elle se
    {//modifie, elle modifie la partie en interne. Et des que repaint est appele, il repaint la partie
    	Image tmp;
    	g.setColor(Color.gray);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		
    	if(partie != null)
    	{
    		String chaine;
    		
    		g.setFont(new Font("Comics Sans MS", Font.BOLD, 25));
    		g.setColor(Color.white);
    		
    		if(partie.getListeCoup().size()%2 == 0)
    		{
    			g.setColor(new Color(207, 148, 85));
    			g.drawString(partie.getListeJoueur()[0].getNom(), 50+8*50, 50+8*50+75);
    			g.setColor(new Color(212, 212, 212));
    			g.drawString(partie.getListeJoueur()[1].getNom(), 50+8*50, 25);
    		}
    		else
    		{
    			g.setColor(new Color(207, 148, 85));
    			g.drawString(partie.getListeJoueur()[1].getNom(), 50+8*50, 25);
    			g.setColor(new Color(212, 212, 212));
    			g.drawString(partie.getListeJoueur()[0].getNom(), 50+8*50, 50+8*50+75);
    		}
    		for(int y = 7; y >= 0; y--)
			{
				for(int x = 0; x < 8; x++)
				{
					if(x%2 == y%2)
					{
						g.setColor(new Color(187, 128, 65));
						g.fillRect(50+x*50, 50+7*50-y*50, 50, 50);
						g.setColor(Color.black);
					}
					else
					{
						g.setColor(Color.white);
						g.fillRect(50+x*50, 50+7*50-y*50, 50, 50);
					}
					tmp = recupererImagePiece(partie.getPlateau().getPiecePosition(new Position(x, y)), x, y);
					if(tmp != null)
					{
						g.drawImage(tmp, 50+x*50, 50+7*50-y*50, Color.BLACK,this);
					}
				}
				chaine = new String();
				chaine += (char)(7-y+'1');
				g.drawString(chaine, 75+8*50, 75+50*y);//on paint l'indice de coordonne
			}
			for(int x = 0; x < 8; x++)
			{
				chaine = new String();
				chaine += (char)(x+'a');
				if(x%2 == 0)
				{
					g.setColor(Color.black);
				}
				else
				{
					g.setColor(Color.white);
				}
				g.drawString(chaine, 75+x*50, 75+50*8);//on paint l'indice de coordonnee
			}
			chaine = new String("Coup " + partie.getListeCoup().size());//on indique le nombre de coup
			g.setColor(Color.white);
			g.drawString(chaine, 200, 50+8*50+75);
    	}
    }
    /**
     * Cette méthode renvoie une image des tableaux de piece en fonction de la case et de la piece s'y trouvant
     * @param piece la piece
     * @param x abscisse de la piece
     * @param y ordonné de la piece
     * @return l'image à afficher
     */
    public Image recupererImagePiece(PieceAbstraite piece, int x, int y)
    {
    	int couleur = 0, type = 0;
    	if(piece == null)
    	{
    		return null;
    	}
    	if(piece.getCouleur() == true)//noir
    	{
    		couleur = 1;
    	}
    	if(piece instanceof Tour)
    	{
    		type = 1;
    	}
    	else if(piece instanceof Cavalier)
    	{
    		type = 2;
    	}
    	else if( piece instanceof Fou)
    	{
    		type = 3;
    	}
    	else if(piece instanceof Dame)
    	{
    		type = 4;
    	}
    	else if(piece instanceof Roi)
    	{
    		type = 5;
    	}
    	if(x%2 == y%2)
    	{
    		return piece_imgm[type][couleur];
    	}
    	return piece_img[type][couleur];
    }
	public PartieAbstraite getPartie() {
		return partie;
	}
	public void setPartie(PartieAbstraite partie) {
		this.partie = partie;
	}
}