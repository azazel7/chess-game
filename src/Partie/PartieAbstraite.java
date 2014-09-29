/**
 * 
 */
package Partie;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.LinkedList;
import java.util.ListIterator;

import Affichage.AffichageAbstrait;
import General.Coup;
import General.Echequier;
import General.Position;
import JoueurEchec.Humain;
import JoueurEchec.Joueur;
import JoueurEchec.Reseau;
import PieceEchec.Cavalier;
import PieceEchec.Dame;
import PieceEchec.Fou;
import PieceEchec.PieceAbstraite;
import PieceEchec.Pion;
import PieceEchec.Roi;
import PieceEchec.Tour;

/**
 * 
 * @author Sauron
 *
 */
public abstract class PartieAbstraite
{
	//attribu
	/**
	 * le plateau de jeu
	 */
	protected Echequier plateau;
	/**
	 * La liste des coup joué
	 */
	protected LinkedList<Coup> listeCoup;
	/**
	 * Un tableau des joueurs
	 */
	protected Joueur[] listeJoueur;
	/**
	 * UN affichage qui sert à afficher des donnée plus ou moins importante
	 */
	protected AffichageAbstrait affichage;
	/**
	 * Le dernier coup annulé
	 */
	protected Coup coupAnnule;
	/**
	 * Stocke l'evolution de la demande de nulle
	 */
	protected int demandeNulle;
	/**
	 * Variable qui si elle est true stop la partie au prochain coup
	 */
	protected volatile boolean fin = false;
	//constructeur
	/**
	 * constructeur de base
	 */
	public PartieAbstraite()
	{
		listeCoup = new LinkedList<Coup>();
		coupAnnule = null;
		demandeNulle = 0;
	}
	/**
	 * 
	 * @param echequier le plateau de jeu
	 * @param liste la liste des coup à utiliser (généralement vide)
	 * @param j1 le premier joueru
	 * @param j2 le second
	 * @param annule le dernier coup annulé (généralement null sauf dans le cas d'un clonage)
	 */
	public PartieAbstraite(Echequier echequier, LinkedList<Coup> liste, Joueur j1, Joueur j2, Coup annule)
	{
		demandeNulle = 0;
		plateau = echequier.clonage();
		coupAnnule = null;
		listeJoueur = new Joueur[2];
		if(annule != null)
		{
			coupAnnule = annule.clonage();
		}
		listeCoup = new LinkedList<Coup>();
		for(int i = 0; i < liste.size(); i++)
		{
			listeCoup.add(liste.get(i).clonage());
		}
		listeJoueur[0] = null;
		if(j1 != null)
		{
			listeJoueur[0] = j1.clonage();
		}
		
		listeJoueur[1] = null;
		if(j2 != null)
		{
			listeJoueur[1] = j2.clonage();
		}
		
	}
	
	
	//methode defini
	/**
	 * Annule le dernier coup joué
	 *
	 */
	public boolean annulerDernierCoup()//a faire la roque inverse et la prise en passant inverse
	{
		coupAnnule = listeCoup.removeLast();//on supprime le dernier coup
		if(coupAnnule.getAction() == 'p' || coupAnnule.getAction() == '-' || coupAnnule.getAction() == 'x')
		{
			plateau.setPiecePosition(coupAnnule.getPiece());
			plateau.setPiecePosition(coupAnnule.getPrise());
			if(coupAnnule.getAction() == 'p' || coupAnnule.getAction() == '-')
			{
				plateau.removePiece(coupAnnule.getFin());
			}
		}
		else if(coupAnnule.getAction() == 'O')
		{
			//refaire la roque inverse
			//chercher le roi
			//recuperer la tour sur son cote
			//mettre x tour a 0
			//augmenter x roi de 2
			LinkedList<PieceAbstraite> piece = plateau.getPiece();
			Position position = null;
			for(int i = 0; i < piece.size(); i++)
			{
				if(piece.get(i) instanceof Roi && piece.get(i).getCouleur() == coupAnnule.getPiece().getCouleur())
				{
					position = piece.get(i).getPosition().clonage();
					position.setX(position.getX()+1);
					piece.get(i).setPosition(new Position(piece.get(i).getPosition().getX()+2, piece.get(i).getPosition().getY()));
					piece.get(i).setaBouge(piece.get(i).getaBouge()-1);
					break;
				}
			}
			for(int i = 0; i < piece.size(); i++)
			{
				if(piece.get(i).getPosition().equals(position) && piece.get(i) instanceof Tour)
				{
					piece.get(i).setPosition(new Position(0, piece.get(i).getPosition().getY()));
					piece.get(i).setaBouge(piece.get(i).getaBouge()-1);
					break;
				}
			}
		}
		else if(coupAnnule.getAction() == 'o')
		{
			//refaire la roque inverse
			//refaire la roque inverse
			//chercher le roi
			//recuperer la tour sur son cote
			//mettre x tour a 7
			//diminuer x roi de 2
			LinkedList<PieceAbstraite> piece = plateau.getPiece();
			Position position = null;
			for(int i = 0; i < piece.size(); i++)
			{
				if(piece.get(i) instanceof Roi && piece.get(i).getCouleur() == coupAnnule.getPiece().getCouleur())
				{
					position = piece.get(i).getPosition().clonage();
					position.setX(position.getX()-1);
					piece.get(i).setPosition(new Position(piece.get(i).getPosition().getX()-2, piece.get(i).getPosition().getY()));
					piece.get(i).setaBouge(piece.get(i).getaBouge()-1);
					break;
				}
			}
			for(int i = 0; i < piece.size(); i++)
			{
				if(piece.get(i).getPosition().equals(position) && piece.get(i) instanceof Tour)
				{
					piece.get(i).setPosition(new Position(7, piece.get(i).getPosition().getY()));
					piece.get(i).setaBouge(piece.get(i).getaBouge()-1);
					break;
				}
			}
		}
		return true;
	}
	/**
	 * Rejoue le dernier coup annulé et met coup Annule à null
	 */
	public void rejouerCoup()
	{
		if(coupAnnule == null)
		{
			return;
		}
		else if(coupAnnule.getAction() == 'o')
		{
			if((listeCoup.size()%2) == 0)
			{
				plateau.realiserRoque(false, false);
			}
			else
			{
				plateau.realiserRoque(false, true);
			}
			listeCoup.add(coupAnnule);
		}
		else if(coupAnnule.getAction() == 'O')
		{
			if((listeCoup.size()%2) == 0)
			{
				plateau.realiserRoque(true, false);
			}
			else
			{
				plateau.realiserRoque(true, true);
			}
			listeCoup.add(coupAnnule);
		}
		else if(coupAnnule.getAction() == 'x' || coupAnnule.getAction() == '-')
		{
			plateau.deplacementPiece(coupAnnule);
			listeCoup.add(coupAnnule);
		}
		coupAnnule = null;
	}
	/**
	 * Verifie si un coup est valide en fonction de la partie (prise en passant, roque, et coup simple)
	 * Elle ne verifie pas la mise en echec.
	 * @param coup le coup à verifier
	 * @return true si le coup est valide
	 */
	public boolean coupValid(Coup coup)
	{
		if(coup.getAction() == 'a' && !this.estReseau())
		{
			return true;
		}
		else if(coup.getAction() == 'r' && !this.estReseau() && listeCoup.size() >= 1)
		{
			return true;
		}
		else if(coup.getAction() == 's')
		{
			return true;
		}
		else if(coup.getAction() == 'o')
		{
			if(listeCoup.size()%2 == 0)
			{
				return plateau.verifierRoque(false, false, this); //petit roque blanc
			}
			return plateau.verifierRoque(false, true, this); //petit roque noir
		}
		else if(coup.getAction() == 'O')
		{
			if(listeCoup.size()%2 == 0)
			{
				return plateau.verifierRoque(true, false, this); //grand roque blanc
			}
			return plateau.verifierRoque(true, true, this); //grand roque noir
		}
		if(coup.getPiece() == null)
		{
			return false;
		}
		if((coup.getPiece().getCouleur() == true && listeCoup.size()%2 == 0) || (coup.getPiece().getCouleur() == false && listeCoup.size()%2 == 1))
		{//on ne joue pas les pieces de l'adversaire !
			return false;
		}
		if(coup.getPiece() instanceof Roi && estEchec(coup.getFin(), coup.getPiece().getCouleur()))//c'est un roi et la position de fin est en echec
		{
			return false;
		}
		if(coup.getPromotion() != (byte)0 && ! (coup.getPiece() instanceof Pion))//verifie la promotion, une promo sans pion
		{
			return false;
		}
		if(coup.getPromotion() != (byte)0 && coup.getPiece() instanceof Pion && (coup.getFin().getY()%7) != 0)//verifie la promotion, une promo sans etre au fond
		{
			return false;
		}
		if(coup.getPromotion() == (byte)0 && coup.getPiece() instanceof Pion && (coup.getFin().getY()%7) == 0)//il manque une promotion
		{
			return false;
		}
		LinkedList<Position> position = coup.getPiece().positionAtteignableEchequier(plateau);
		ListIterator<Position> iterator = position.listIterator();
		while(iterator.hasNext())
		{
			if(coup.getFin().equals(iterator.next()))
			{
				return true;
			}
		}
		
		if(coup.getPiece() instanceof Pion && coup.getPrise() == null && coup.getDepart().getX() != coup.getFin().getX())//un pion qui se deplace en diago son prise ... prise en passant ?
		{
			if(listeCoup.size() >= 1 && listeCoup.getLast().getPiece() instanceof Pion && Math.abs(listeCoup.getLast().getDepart().getY() - listeCoup.getLast().getFin().getY()) == 2)//on verifie la demande
			{
				Position placement = new Position(coup.getFin().getX(), coup.getDepart().getY());
				if(placement.equals(listeCoup.getLast().getFin()))//si c'est bien le pion du coup precedant
				{
					PieceAbstraite pion = plateau.getPiecePosition(placement);
					if(pion != null && pion.getCouleur() != coup.getPiece().getCouleur())
					{
						coup.setPrise(pion);
						coup.setAction((byte)'p');
						return true;
					}
				}
			}
		}
		
		if(coup.getAction() == (byte)'p' && coup.getPiece() instanceof Pion && coup.getPrise() != null && coup.getPrise() instanceof Pion)
		{
			if(listeCoup.size() >= 1 && listeCoup.getLast().getPiece() instanceof Pion && Math.abs(listeCoup.getLast().getDepart().getY() - listeCoup.getLast().getFin().getY()) == 2)//on verifie la demande
			{
				Position placement = new Position(coup.getFin().getX(), coup.getDepart().getY());
				if(placement.equals(listeCoup.getLast().getFin()))//si c'est bien le pion du coup precedant
				{
					PieceAbstraite pion = plateau.getPiecePosition(placement);
					if(pion != null && pion.getCouleur() != coup.getPiece().getCouleur())
					{
						return true;
					}
				}
			}
		}
		return false;		
	}
	/**
	 * Verifie si une position est echec dans le cas ou le roi se placerai dessus
	 * @param position la position à verifier
	 * @param couleur la couleur qui joue à cette position
	 * @return true s'il y a echec
	 */
	public boolean estEchec(Position position, boolean couleur)
	{
		boolean ajout = false;
		if(plateau.getPiecePosition(position) == null)//si a cette position, il n'y a pas de piece, on en ajoute une (pour les coup des pions)
		{
			ajout = true;
			plateau.setPiecePosition(new Roi(couleur, 0, position));
		}
		ListIterator<Coup> iterator = listeCoupPrimaire(!couleur).listIterator();
		Coup actuel;
		while(iterator.hasNext())
		{
			actuel = iterator.next();
			if(actuel.getFin().equals(position) && actuel.getAction() == (byte)'x')//c'est une prise qui fini sur la mï¿½me position que la position donnï¿½e ...
			{
				if(ajout == true)//on supprime la piece que l'on avait ajoutï¿½
				{
					plateau.removePiece(position);
				}
				return true;
			}
		}
		if(ajout == true)//on supprime la piece que l'on avait ajoutï¿½
		{
			plateau.removePiece(position);
		}
		return false;
	}
	/**
	 * Verifie si une couleur est echec et mat
	 * @param couleur la couleur à verifier
	 * @return true s'il y a echec et mat
	 */
	public boolean estMat(boolean couleur)
	{
		Position position = null;
		LinkedList<PieceAbstraite> listePiece = plateau.getPiece();//on cherche le roi
		ListIterator<PieceAbstraite> piece_it = listePiece.listIterator();
		PieceAbstraite actuel_p;
		while(piece_it.hasNext())
		{
			actuel_p = piece_it.next();
			if(actuel_p instanceof Roi && actuel_p.getCouleur() == couleur)//c'est une piece adverse et c'est un roi
			{
				position = actuel_p.getPosition();
				break;
			}
		}
		if(estEchec(position, couleur) == false)
		{
			return false;
		}
		LinkedList<Coup> coup = listeCoupJouable(couleur);//liste des coups du joueur
		ListIterator<Coup> coup_it = coup.listIterator();
		Coup actuel_c;
		while(coup_it.hasNext())//on verifi si pour chaque coup, il reste echec
		{
			actuel_c = coup_it.next();
			//joue le coup sur le plateau
			plateau.deplacementPiece(actuel_c);
			listeCoup.add(actuel_c);
			if(!(actuel_c.getPiece() instanceof Roi) || actuel_c.getAction() == (byte)'o' || actuel_c.getAction() == (byte)'O')
			{
				if(estEchec(position, couleur) == false)
				{
					annulerDernierCoup();
					return false;
				}
			}
			else
			{
				if(estEchec(actuel_c.getFin(), couleur) == false)
				{
					annulerDernierCoup();
					return false;
				}
			}
			
			//on rï¿½initialise plateau avec svg
			annulerDernierCoup();
		}//aucun coup possible du joueur n'a pu lui ï¿½ter l'echec, donc il est mat.
		//plateau = svg.clonage();
		return true;
	}
	/**
	 * Verifie si une couleur est pat
	 * @param couleur la couleur à verifier
	 * @return true si la couleur est pat
	 */
	public boolean estPat(boolean couleur)
	{
		Position position = null;
		LinkedList<PieceAbstraite> listePiece = plateau.getPiece();//on cherche le roi
		ListIterator<PieceAbstraite> piece_it = listePiece.listIterator();
		PieceAbstraite actuel_p;
		while(piece_it.hasNext())
		{
			actuel_p = piece_it.next();
			if(actuel_p instanceof Roi && actuel_p.getCouleur() == couleur)//c'est une piece adverse et c'est un roi
			{
				position = actuel_p.getPosition();
				break;
			}
		}
		if(estEchec(position, couleur) == true)
		{
			return false;
		}
		LinkedList<Coup> coup = listeCoupJouable(couleur);//liste des coups du joueur
		ListIterator<Coup> coup_it = coup.listIterator();
		Coup actuel_c;
		while(coup_it.hasNext())//on verifi si pour chaque coup, il reste echec
		{
			actuel_c = coup_it.next();
			//joue le coup sur le plateau
			plateau.deplacementPiece(actuel_c);
			listeCoup.add(actuel_c);
			if(!(actuel_c.getPiece() instanceof Roi) || actuel_c.getAction() == (byte)'o' || actuel_c.getAction() == (byte)'O')
			{
				if(estEchec(position, couleur) == false)
				{
					annulerDernierCoup();
					return false;
				}
			}
			else
			{
				if(estEchec(actuel_c.getFin(), couleur) == false)
				{
					annulerDernierCoup();
					return false;
				}
			}
			
			//on reinitialise plateau avec svg
			annulerDernierCoup();
		}//aucun coup possible du joueur n'a pu lui oter l'echec, donc il est mat.
		return true;
	}
	/**
	 * Teste les 50 coups sans prise ni sans mouvement de pion, ainsi que la triple repététion
	 * @return true si la partie peut être consideré comme nulle ?
	 */
	public boolean estNulle()
	{
		if(listeCoup.size() >= 50)//verification des 50 coups sans prise ni deplacement de pion
		{
			for(int i = listeCoup.size()-1; i >= listeCoup.size()-50; i--)
			{
				if(listeCoup.get(i).getAction() == (byte)'x' || listeCoup.get(i).getAction() == (byte)'p' || listeCoup.get(i).getPiece() instanceof Pion)
				{
					break;
				}
				if(i == listeCoup.size()-50)
				{
					return true;
				}
			}
		}
		//triple repetition
		if(listeCoup.size() >= 5)
		{
			if(tripleRepetition() == true)
			{
				return true;
			}
		}
		return false;
	}
	/**
	 * Compare deux plateau pour verifier leur similitude
	 * @param plateau1 le plateau 1
	 * @param plateau2 le plateau 2
	 * @return true si les deux plateau sont identique
	 */
	public boolean plateauIdentique(Echequier plateau1, Echequier plateau2)
	{
		PieceAbstraite piece1, piece2;
		if((plateau1 == null && plateau2 != null) || (plateau1 != null && plateau2 == null))
		{
			return false;
		}
		if(plateau1 == null && plateau1 == plateau2)
		{
			return true;
		}
		if(plateau1.getPiece().size() != plateau2.getPiece().size())
		{
			return false;
		}
		for(int x = 0; x < 8; x++)
		{
			for(int y = 0; y < 8; y++)
			{
				piece1 = plateau1.getPiecePosition(new Position(x, y));
				piece2 = plateau2.getPiecePosition(new Position(x, y));
				if((piece1 == null || piece2 == null) && piece1 != piece2)
				{
					return false;
				}//les 2 pieces sont identiques
				if(piece1 != null && ! piece1.equals(piece1))//elles sont differentes de nulle et ne sont pas egale
				{
					return false;
				}		
			}
		}
		return true;
	}
	/**
	 * Compare deux liste de coup
	 * @param liste1 la liste 1
	 * @param liste2 la seconde liste
	 * @return true si les deux liste de coup sont identique
	 */
	public boolean coupIdentique(LinkedList<Coup> liste1, LinkedList<Coup> liste2)
	{
		if(liste1.size() != liste2.size())
		{
			return false;
		}
		ListIterator<Coup> i1 = liste1.listIterator(), i2 = liste2.listIterator();
		while(i1.hasNext())
		{
			if( ! i1.next().equals(i2.next()))
			{
				return false;
			}
		}
		return true;
	}
	/**
	 * verifie si la triple repétition affecte la partie
	 * @return true si la triple repétition est declaré
	 */
	public boolean tripleRepetition()
	{
		/*on svg le plateau
		on cree un tableau de plateau de la taille du nombre de coup
		on creer un 2 tableau de linkedlist de coup jouable pour un joueur
		(chaque plateau aura donc 2 linkedlist de coup jouable)
		2 partie son identique quand les plateaux sont identique et que leurs listes de coup jouable est identique pour chaque joueur
		On rejoue tous les coups
		Puis on compare l'etat de la partie par rapport au autre coup.
		Si l'on trouve 3 occurences identiques alors il peut y avoir un nul.
		 */
		int compteur = 0;
		Echequier svg = plateau.clonage();//svg
		Echequier listePlateauBlanc[] = new Echequier[listeCoup.size()];//creation des differents coup
		Echequier listePlateauNoir[] = new Echequier[listeCoup.size()];
		LinkedList<LinkedList<Coup>> coupBlanc = new LinkedList<LinkedList<Coup>>();
		LinkedList<LinkedList<Coup>> coupNoir = new LinkedList<LinkedList<Coup>>();
		initialiserPosition();//initialise les positions
		//affichage.affichagePartie(this);
		
		int a;// = 5/(5-5);
		for(int i = 0; i < listeCoup.size(); i += 2)//rejoue la partie et stock tout ce dont on a besoin
		{
			coupBlanc.add(this.listeCoupJouable(false));
			listePlateauBlanc[i] = plateau.clonage();
			plateau.deplacementPiece(listeCoup.get(i));
			if(i+1 < listeCoup.size())
			{
				coupNoir.add(this.listeCoupJouable(true));
				listePlateauNoir[i] = plateau.clonage();
				if(plateau.getPiecePosition(listeCoup.get(i+1).getDepart()) == null)
				{
					affichage.affichagePartie(this);
					System.out.println(listeCoup.get(i+1) + " " + (i+1) + " " + listeCoup.size());
					a = 5/(5-5);
				}
				plateau.deplacementPiece(listeCoup.get(i+1));
			}
		}
		//verifie chez les blancs
		for(int i = 0; i < coupBlanc.size(); i++)
		{
			
			for(int j = 0; j < coupBlanc.size(); j++)
			{
				if(j != i && plateauIdentique(listePlateauBlanc[i], listePlateauBlanc[j]) == true && coupIdentique(coupBlanc.get(i), coupBlanc.get(j)))
				{
					compteur++;
					if(compteur >= 3)
					{
						return true;
					}
				}
			}
		}
		
		//verifie chez les noirs
		compteur = 0;
		for(int i = 0; i < coupNoir.size(); i++)
		{
			
			for(int j = 0; j < coupNoir.size(); j++)
			{
				if(j != i && plateauIdentique(listePlateauNoir[i], listePlateauNoir[j]) == true && coupIdentique(coupNoir.get(i), coupNoir.get(j)))
				{
					compteur++;
					if(compteur >= 3)
					{
						return true;
					}
				}
			}
		}
		plateau = svg.clonage();
		return false;
	}
	/**
	 * Verifie quelques cas flagrant de materiel insuffisant pour matter
	 * @return true si il n'y a pas suffisament de matériel pour matter
	 */
	public boolean materielInsuffisant()
	{
		LinkedList<PieceAbstraite> blanc = new LinkedList<PieceAbstraite>(), noir = new LinkedList<PieceAbstraite>();
		if(plateau.getPiece().size() > 4)
		{
			return false;
		}
		if(plateau.getPiece().size() <= 2)
		{
			return true;
		}
		for(int i = 0; i < plateau.getPiece().size(); i++)
		{
			if(plateau.getPiece().get(i).getCouleur() == true)
			{
				blanc.add(plateau.getPiece().get(i));
			}
			else
			{
				noir.add(plateau.getPiece().get(i));
			}
		}
		if(plateau.getPiece().size() == 3)
		{
			for(int i = 0; i < 3; i++)
			{
				if(plateau.getPiece().get(i) instanceof Fou || plateau.getPiece().get(i) instanceof Cavalier)
				{
					return true;
				}
			}
		}
		if(blanc.size() == 2 && noir.size() == 2)
		{
			boolean fou = false;
			for(int i = 0; i < 2; i++)
			{
				if(blanc.get(i) instanceof Fou)
				{
					if(fou == true)
					{
						return true;
					}
					fou = true;
				}
				if(noir.get(i) instanceof Fou)
				{
					if(fou == true)
					{
						return true;
					}
					fou = true;
				}
			}
		}
		if(blanc.size() == 2 && noir.size() == 2)
		{
			boolean cavalier = false;
			for(int i = 0; i < 2; i++)
			{
				if(blanc.get(i) instanceof Cavalier)
				{
					if(cavalier == true)
					{
						return true;
					}
					cavalier = true;
				}
				if(noir.get(i) instanceof Cavalier)
				{
					if(cavalier == true)
					{
						return true;
					}
					cavalier = true;
				}
			}
		}
		return true;
	}
	
	/**
	 * Calcule tout les coup qu'un joueur peut effectuer pour un tour
	 * @param couleur indique pour quel joueur cette liste doit etre constitue
	 * @return la liste des coup jouable pour un joueur
	 */
	public LinkedList<Coup> listeCoupPrimaire(boolean couleur)
	{
		LinkedList<Coup> retour = new LinkedList<Coup>();
		ListIterator<Position> tmp_it; 
		Position actuel_pos;
		ListIterator<PieceAbstraite> piece_it = plateau.getPiece().listIterator();
		PieceAbstraite actuel_piece;
		int curseur = 0;
		while(piece_it.hasNext())//on rï¿½cupï¿½re toutes les position atteignable des pieces
		{
			actuel_piece = piece_it.next();
			if(actuel_piece.getCouleur() == couleur)//qui sont de la mï¿½me couleur
			{
				tmp_it = actuel_piece.positionAtteignableEchequier(plateau).listIterator();
				
				while(tmp_it.hasNext())
				{
					actuel_pos = tmp_it.next();
					if(actuel_piece instanceof Pion && actuel_pos.getY()%7 == 0)//si c'est un pion pouvant etre promu
					{
						retour.add(	new Coup(actuel_piece.getPosition(), actuel_pos, actuel_piece, (byte)'-', plateau.getPiecePosition(actuel_pos), (byte)'D'));//ce sont automatiquement des mvt simple
						retour.add(	new Coup(actuel_piece.getPosition(), actuel_pos, actuel_piece, (byte)'-', plateau.getPiecePosition(actuel_pos), (byte)'C'));//ce sont automatiquement des mvt simple
						retour.add(	new Coup(actuel_piece.getPosition(), actuel_pos, actuel_piece, (byte)'-', plateau.getPiecePosition(actuel_pos), (byte)'F'));//ce sont automatiquement des mvt simple
						retour.add(	new Coup(actuel_piece.getPosition(), actuel_pos, actuel_piece, (byte)'-', plateau.getPiecePosition(actuel_pos), (byte)'T'));//ce sont automatiquement des mvt simple
						//TODO error
					}
					else
					{
						retour.add(
								new Coup(
										actuel_piece.getPosition(), 
										actuel_pos, 
										actuel_piece, 
										(byte)'-', 
										plateau.getPiecePosition(actuel_pos), 
										(byte)0));//ce sont automatiquement des mvt simple						
					}
					if(retour.get(curseur).getPrise() != null)
					{
						retour.get(curseur).setAction((byte)'x');
					}
					curseur++;
				}
			}
		}
		//si le coup precedant jouait un pion, et que son deplacement fut de 2 -> prise en passant a verifier
		if(listeCoup.size() >= 3 && listeCoup.getLast().getPiece() instanceof Pion && Math.abs(listeCoup.getLast().getDepart().getY() - listeCoup.getLast().getFin().getY()) == 2)
		{
			Position arrive;
			//verifie a gauche
			PieceAbstraite prise_p = plateau.getPiecePosition(new Position(listeCoup.getLast().getFin().getX()-1, listeCoup.getLast().getFin().getY()));
			if( prise_p != null && prise_p.getCouleur() == couleur && prise_p instanceof Pion)//il y a une piece qui est un pion, et de la couleur du joueur
			{
				int y = listeCoup.getLast().getFin().getY() - ((listeCoup.getLast().getFin().getY() - listeCoup.getLast().getDepart().getY())/2);//calcule du y quelque soi la couleur
				arrive = new Position(listeCoup.getLast().getFin().getX(), y);
				if(plateau.getPiecePosition(arrive) == null)//en thï¿½orie, il n'y a aucune piece, mais la mï¿½thode echec ajoute une piece si necessaire pour faire son travail, donc si par mï¿½garde cela se produit sur une prise en passant, ce pourrait avoir de grave consï¿½quence
				{
					retour.add(new Coup(prise_p.getPosition(), arrive, prise_p, (byte)'p', listeCoup.getLast().getPiece(), (byte)0));
				}
			}
			//verifie a droite
			prise_p = plateau.getPiecePosition(new Position(listeCoup.getLast().getFin().getX()+1, listeCoup.getLast().getFin().getY()));
			if( prise_p != null && prise_p.getCouleur() == couleur && prise_p instanceof Pion)
			{
				int y = listeCoup.getLast().getFin().getY() - ((listeCoup.getLast().getFin().getY() - listeCoup.getLast().getDepart().getY())/2);//calcule du y quelque soi la couleur
				arrive = new Position(listeCoup.getLast().getFin().getX(), y);
				if(plateau.getPiecePosition(arrive) == null)//idem
				{
					retour.add(new Coup(prise_p.getPosition(), arrive, prise_p, (byte)'p', listeCoup.getLast().getPiece(), (byte)0));
				}
			}
		}
		return retour;
	}
	/**
	 * Liste les coups jouable, c'est à dire les coup primaire plus les roques
	 * @param couleur
	 * @return
	 */
	public LinkedList<Coup> listeCoupJouable(boolean couleur)
	{
				LinkedList<Coup> retour = listeCoupPrimaire(couleur);
				//on verifie les roques (sans prendre en compte la situation d'echec
				if(plateau.verifierRoque(true, couleur, this))
				{
					retour.add(new Coup(null, null, new Roi(couleur, 0, null), (byte)'O', null, (byte)0));
				}
				if(plateau.verifierRoque(false, couleur, this))
				{
					retour.add(new Coup(null, null, new Roi(couleur, 0, null), (byte)'o', null, (byte)0));
				}
				return retour;
	}
	/**
	 * Verifie si un coup rend echec le joueur qui le joue
	 * @param coup le coup qui sera joué
	 * @return true si le coup rend echec
	 */
	public boolean rendEchec(Coup coup)
	{
		if(coup == null)
		{
			return false;
		}
		else if(coup.getAction() == 'a' || coup.getAction() == 'r' || coup.getAction() == 's')
		{
			return false;
		}
		PieceAbstraite roi = null;
		plateau.deplacementPiece(coup);//on joue le coup
		listeCoup.add(coup);
		ListIterator<PieceAbstraite> iterator = plateau.getPiece().listIterator();
		while(iterator.hasNext())
		{
			roi = iterator.next();
			if(roi instanceof Roi && roi.getCouleur() == coup.getPiece().getCouleur())
			{
				break;
			}
		}
		if(estEchec(roi.getPosition(), roi.getCouleur()) == true)//si il est echec, alors ce coup rend echec
		{
			annulerDernierCoup();
			return true;
		}
		annulerDernierCoup();
		return false;
	}
	/**
	 * Effectue une promotion à une position.
	 * Cette derniére est normalement verifiée avant
	 * @param position la position à promouvoir
	 * @param type EN quoi on doit promouvoir 'T' = tour, 'D' = dame, 'C' = cavalier, 'F' = fou
	 */
	public void promotion(Position position, byte type)
	{
		PieceAbstraite piece = null;
		boolean couleur = false;
		if((listeCoup.size()%2) == 0)
		{
			couleur = true;
		}
		switch(type)
		{
			case 'T':
				piece = new Tour(couleur, 0, position);
				break;
			case 'F':
				piece = new Fou(couleur, 0, position);
				break;
			case 'D':
				piece = new Dame(couleur, 0, position);
				break;
			case 'C':
				piece = new Cavalier(couleur, 0, position);
				break;
		}
		plateau.setPiecePosition(piece);
	}
	
	//methode abstraite
	public abstract void initialiserPosition();
	public abstract void commencerPartie();
	public abstract PartieAbstraite clonage();
	
	/**
	 * Sauvegarde la partie dans un fichier de maniere textuel
	 * @param nom_fichier le nom ou le chemin du fichier où stocker la partie
	 * @return true si la partie à pu être sauvegarde
	 */
	public boolean sauvegarderPartie(String nom_fichier)
	{
		String chaine = null;
		BufferedOutputStream bos = null;
		//une erreur sur le fichier sera recuperee, puis affcihee.
		try
		{
					bos = new BufferedOutputStream(new FileOutputStream(new File(nom_fichier)));
					chaine = new String("[White \"" + listeJoueur[0].getNom() + "\"]");
					writeLine(chaine, bos);
					chaine = new String("[Black \"" + listeJoueur[1].getNom() + "\"]");
					writeLine(chaine, bos);
		}
		catch(Exception e)
		{
			affichage.afficherErreur(e.getMessage());
		}
		//          *liste de coup au format complet ou abrege.*
		for(int i = 0; i < listeCoup.size(); i++)
		{
			try
			{
				writeLine(listeCoup.get(i).toString(), bos);
			}
			catch(Exception e)
			{
				affichage.afficherErreur(e.getMessage());
			}
		}
		try
		{
			bos.close();
		}
		catch(Exception e)
		{
			affichage.afficherErreur(e.getMessage());
		}
		//resultat
		return true;
	}
	/**
	 * Charge une partie dans la partie courante depuis un fichier
	 * @param nom_fichier le nom contenant la sauvegarde
	 */
	public void chargerPartie(String nom_fichier)
	{
		
		boolean fin = false;
		String lecture = null, nom1 = null, nom2 = null;
		Coup coup = null;
		BufferedInputStream bis = null;
		//verifier si le fichier existe -> si on souhaite rï¿½ï¿½crire par dessus
		//si le fichier est impossible a creer, return false
		//format -> Reseau|local
		//			IA|humain (difficulte) nom		
		//          *liste de coup au format complet ou abrege.*
		try
		{
			bis = new BufferedInputStream(new FileInputStream(new File(nom_fichier)));
			nom1 = readLine(bis).substring(8);
			nom1 = nom1.substring(0, nom1.indexOf("\""));
			nom2 = readLine(bis).substring(8);
			nom2 = nom2.substring(0, nom2.indexOf("\""));
			listeJoueur[0] = new Humain(nom1);
			listeJoueur[1] = new Humain(nom2);
		}
		catch(Exception e)
		{
			affichage.afficherErreur(e.getMessage());
			fin = true;
		}
		while(fin == false)
		{
			try
			{
				lecture = readLine( bis);
				if(lecture != null)
				{
					coup = Humain.traitement(lecture, this);
				}
				else
				{
					break;
				}
				if(this.coupValid(coup) == false || this.rendEchec(coup) == true)
				{
					throw new Exception("Un coup est errone.\n");
				}
				plateau.deplacementPiece(coup);
				listeCoup.add(coup);
			}
			catch(Exception e)
			{
				fin = true;
				if(e.getMessage().equals(""))
				{
					affichage.afficherErreur(e.getMessage());
				}
				
			}
		}
		try
		{
			bis.close();
		}
		catch(Exception e)
		{
			affichage.afficherErreur(e.getMessage());
		}
	}
	/**
	 * Ecrit une ligne dans un buffer
	 * @param ligne la ligne à ecrire
	 * @param bos la buffer d'ecriture
	 * @throws Exception l'exception est lancé dans le cas d'un probléme d'ecriture
	 */
	public static void writeLine(String ligne, BufferedOutputStream bos) throws Exception
	{
		byte buffer[] = new byte[ligne.length()+1];
		for(int i = 0; i < ligne.length(); i++)
		{
			buffer[i] = (byte) ligne.charAt(i);
		}
		buffer[ligne.length()] = '\n';
		bos.write(buffer);
	}
	/**
	 * Lit une ligne depuis un buffer
	 * @param bis buffer de lecture
	 * @return retourne la ligne lut
	 * @throws Exception lance une exception si la lecture à échouée
	 */
	public static String readLine( BufferedInputStream bis) throws Exception
	{
		byte buffer[] = new byte[1];
		int val = 0;
		buffer[0] = 0;
		String retour = new String();
		do
		{
			val = bis.read(buffer);
			if(buffer[0] != '\n' && val != -1)
			{
				retour += (char)buffer[0];
			}
			if(val == -1)
			{
				return null;
			}
		}while(buffer[0] != '\n');
		return retour;
	}
	/**
	 * Verifie si la partie est en reseau
	 * @return true si la partie se joue en reseau
	 */
	public boolean estReseau()
	{
		for(Joueur j : listeJoueur)
		{
			if(j instanceof Reseau)
			{
				return true;
			}
		}
		return false;
	}
	//getter et setter
	public Echequier getPlateau() {
		return plateau;
	}
	public void setPlateau(Echequier plateau) {
		this.plateau = plateau;
	}
	public LinkedList<Coup> getListeCoup() {
		return listeCoup;
	}
	public void setListeCoup(LinkedList<Coup> listeCoup) {
		this.listeCoup = listeCoup;
	}
	public Joueur[] getListeJoueur() {
		return listeJoueur;
	}
	public void setListeJoueur(Joueur[] listeJoueur) {
		this.listeJoueur = listeJoueur;
	}
	public AffichageAbstrait getAffichage() {
		return affichage;
	}
	public void setAffichage(AffichageAbstrait affichage) {
		this.affichage = affichage;
	}
	public Coup getCoupAnnule() {
		return coupAnnule;
	}
	public void setCoupAnnule(Coup coupAnnule) {
		this.coupAnnule = coupAnnule;
	}
	public boolean isFin() {
		return fin;
	}
	public void setFin(boolean fin) {
		this.fin = fin;
	}
	public int getDemandeNulle() {
		return demandeNulle;
	}
	public void setDemandeNulle(int demandeNulle) {
		this.demandeNulle = demandeNulle;
	}
}
