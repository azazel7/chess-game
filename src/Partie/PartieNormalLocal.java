package Partie;

import java.util.LinkedList;
import java.util.Scanner;

import Affichage.AffichageNormal;
import Affichage.Panneau;
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
 * @author Dumbledore
 *
 */
public class PartieNormalLocal extends PartieAbstraite
{
	/**
	 * Correspond au total de temps de chaque joueur
	 */
	protected double moyenne[];
	/**
	 * constructeur de base
	 */
	public PartieNormalLocal()
	{
		super();
		listeJoueur = new Joueur[2];
		listeJoueur[0] = new Humain("Joueur 1");
		listeJoueur[1] = new Humain("Joueur 2");
		affichage = new AffichageNormal();
		initialiserPosition();
	}
	/**
	 * constructeur pour le clonage d'une partie
	 * @param echequier l'echequier sur lequel ce passera la partie
	 * @param liste la liste des coups, generalement vide
	 * @param j1 le premier joueur
	 * @param j2 le second joueur
	 * @param GUI le type d'affichage, graphique ou textuel
	 * @param annule la valeur du coup annulee, casiment tout le temps a� null
	 */
	public PartieNormalLocal(Echequier echequier, LinkedList<Coup> liste, Joueur j1, Joueur j2, Panneau GUI, Coup annule)
	{
		super(echequier, liste, j1, j2, annule);
		affichage = new AffichageNormal(GUI);
	}
	/**
	 * le constructeur le plus utilise
	 * @param j1 le premier joueur
	 * @param j2 le second joueur
	 * @param GUI le type d'affichage, graphique ou textuel
	 */
	public PartieNormalLocal(Joueur j1, Joueur j2, Panneau GUI)
	{
		super();
		coupAnnule = new Coup(null, null, null, (byte)'a', null, (byte)0);
		listeJoueur = new Joueur[2];
		listeJoueur[0] = j1;
		listeJoueur[1] = j2;
		if(j1 != null)
		{
			listeJoueur[0] = j1.clonage();
		}
		if(j2 != null)
		{
			listeJoueur[1] = j2.clonage();
		}
		affichage = new AffichageNormal(GUI);
		initialiserPosition();
		moyenne = new double[2];
		moyenne[0] = moyenne[1] = 0;
	}
	/**
	 * iitialise toutes les positions des pieces et les place sur l'echequier
	 */
	public void initialiserPosition()
	{
		plateau = new Echequier(8, 8, null);
		LinkedList<PieceAbstraite> listePiece = new LinkedList<PieceAbstraite>();
		
		listePiece.add(new Roi(false, 0, new Position(4, 0)));
		listePiece.add(new Roi(true, 0, new Position(4, 7)));
		
		listePiece.add(new Dame(false, 0, new Position(3, 0)));
		listePiece.add(new Dame(true, 0, new Position(3, 7)));
		
		listePiece.add(new Cavalier(false, 0, new Position(1, 0)));
		listePiece.add(new Cavalier(false, 0, new Position(6, 0)));
		listePiece.add(new Cavalier(true, 0, new Position(1, 7)));
		listePiece.add(new Cavalier(true, 0, new Position(6, 7)));
		
		
		for(int x = 0; x < 8; x++)//on ajoute les pion
		{
			listePiece.add(new Pion(false, 0, new Position(x, 1)));
			listePiece.add(new Pion(true, 0, new Position(x, 6)));
		}

		listePiece.add(new Tour(false, 0, new Position(0, 0)));
		listePiece.add(new Tour(false, 0, new Position(7, 0)));
		listePiece.add(new Tour(true, 0, new Position(0, 7)));
		listePiece.add(new Tour(true, 0, new Position(7, 7)));
		
		listePiece.add(new Fou(false, 0, new Position(2, 0)));
		listePiece.add(new Fou(false, 0, new Position(5, 0)));
		listePiece.add(new Fou(true, 0, new Position(2, 7)));
		listePiece.add(new Fou(true, 0, new Position(5, 7)));
		
		plateau.setPiece(listePiece);
	}
	/**
	 * Debute la partie d'echec, ou la poursuit dans certain cas
	 */
	public void commencerPartie()
	{
		Coup coup = null;
		boolean fin = false;
		int curseur_joueur = listeCoup.size()%2;
		while(!fin)//tant que la fin n'est pas atteinte
		{
			if(this.fin)//une fin de la partie ordonne de l'exterieur
			{
				break;
			}
			if(coup != null && coup.getPiece() != null && estMat(!coup.getPiece().getCouleur()))//le roi ennemi est mat
			{
				
				affichage.affichagePartie(this);//on affiche le jeu
				affichage.afficherVictoire(listeJoueur[(curseur_joueur+1)%2]);//on affiche la victoire (c'est le joueur precedant qui gagne, c'est pourquoi on fait tourner le curseur
				break;
			}
			if(estPat(true) || estPat(false))//verifie le pat
			{
				affichage.affichagePartie(this);
				affichage.afficherChaine("Un joueur est Pat. La partie est terminee.\n");
				break;
			}
			if(materielInsuffisant())//materiel insufisant
			{
				affichage.affichagePartie(this);
				affichage.afficherPop("Materiel insuffisant pour matter. La partie est terminee.\n");
				break;
			}
			if(estNulle())//on verifie si la partie est nulle
			{
				affichage.afficherChaine("Info: La partie peut etre declaree nulle.\n");
			}
			affichage.affichagePartie(this);//on affiche le jeu
			
			coup = saisieCoup(curseur_joueur);
			
			if(coup == null)
			{
				break;
			}
			else if(coup.getAction() == 'a')//annuler coup
			{
				annulerDernierCoup();
				coup = null;
				curseur_joueur = (curseur_joueur + 1)%listeJoueur.length;
				continue;
			}
			else if(coup.getAction() == 'r')//rejouer coup
			{
				listeCoup.add(coupAnnule);//ajoute le coup annule a la liste des coups.
				coup = coupAnnule.clonage();
			}
			else if(coup.getAction() == 's')//sauvegarder
			{
				do{
					affichage.afficherChaine("Entrez le chemin du fichier: ");
				}while(sauvegarderPartie(affichage.SaisirChaine()) == false);
				continue;
			}
			else//ajout du coup
			{
				listeCoup.add(coup);//ajoute le coup a la liste des coups
			}
			plateau.deplacementPiece(coup);//on execute le coup
			curseur_joueur = (curseur_joueur + 1)%listeJoueur.length;//on passe au joueur suivant
		}
		//fermer socket s'il y a ...
		String chaine = new String();
		for(int i = 0; i < 2; i++)
		{
			chaine += listeJoueur[i].getNom() + " moyenne: " + (moyenne[i]/listeCoup.size()) + " ms\n";
			if(listeJoueur[i] instanceof Reseau)
			{
				((Reseau)listeJoueur[i]).envoieCoup(listeCoup.getLast(), this);
				((Reseau)listeJoueur[i]).close(this);
			}
		}
		affichage.afficherPop(chaine);//on affiche la moyenne de chaque joueur
	}
	/**
	 * Methode servant a� la saisie d'un coup our un joueur. Elle verifie notament la validite d'un coup, s'il mes en echec
	 * les deconnection dans le cas d'une partie en reseau, les abandon et les demande de nulle. Elle s'occupe aussi de mesurer le temps de jeu de chaque joueur
	 * @param curseur le curseur correspondant a la position du joueur dans la liste
	 * @return un coup valide
	 */
	public Coup saisieCoup(int curseur)
	{
		boolean valid;
		Coup coup;
		do
		{
			valid = false;
			long startTime = System.currentTimeMillis();//recupere le temps actuel
			coup = listeJoueur[curseur].jouerCoup(this);//on saisie un coup du joueur
			moyenne[curseur] += (System.currentTimeMillis() - startTime);//on ajoute la difference au total
			
			if(!(listeJoueur[curseur] instanceof Humain))
			{
				affichage.afficherChaine(listeJoueur[curseur].getNom() + ": " + coup + "\n");
			}
			if(coup == null)//deconnection
			{
				affichage.afficherErreur("Deconnection\n");
				coup = null;
				break;
			}
			else if(coup.getAction() == 'b')//abbandon (partie abbandon)
			{
				affichage.afficherChaine("Abbandon\n");
				listeCoup.add(coup);
				coup = null;
				break;
			}
			else if(coup.getAction() == 'n')//demande de nulle
			{
				if(demandeNulle != curseur+1)
				{
					demandeNulle += curseur+1;
					
					if(!(listeJoueur[curseur] instanceof Reseau) && listeJoueur[(curseur+1)%2] instanceof Reseau)
					{
						((Reseau)listeJoueur[(curseur+1)%2]).envoieCoup(coup, this);
					}
					else if(listeJoueur[curseur] instanceof Reseau && !(listeJoueur[(curseur+1)%2] instanceof Reseau))
					{
						listeCoup.addLast(coup);
					}
					if(demandeNulle == 3)//(0+1)+(1+1) -> somme des deux curseurs augmente de 1
					{
						affichage.afficherPop("La partie est declaree nulle.\n");
						coup = null;
						break;
					}
					else
					{
						affichage.afficherPop("Demande de nulle fait ...\n");
					}
					continue;
				}
			}
			
			if(coupValid(coup) == false || rendEchec(coup) == true)//Si le coup n'est pas valide, on informe l'utilisateur
			{System.out.println(listeCoup.toString());
				affichage.afficherPop("Ce coup est invalide  " + coup + "\n");
				
				continue;
			}
			valid = true;				
		}while(!valid);//on verifi la validite du coup
		
		return coup;
	}
	/**
	 * Duplique une partie. Utile car simple à mettre en oeuvre, mais desastreusement lente pour les IA qui calcule pleinde coup
	 * Preferer jouer un coup/annuler une coup
	 * @deprecated
	 */
	public PartieAbstraite clonage()
	{
			return new PartieNormalLocal(plateau, listeCoup, listeJoueur[0], listeJoueur[1], affichage.getPaneau(), coupAnnule);
	}
}
