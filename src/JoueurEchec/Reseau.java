package JoueurEchec;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

import Affichage.AffichageNormal;
import Affichage.Panneau;
import General.Coup;
import Partie.PartieAbstraite;
/**
 * 
 * @author Harry Potter
 *
 */
public class Reseau extends Joueur
{
	/**
	 * Indique si le joueur est un serveur ou un client
	 */
	protected boolean serveur;
	/**
	 * Le socket necessaire au serveur
	 */
	protected ServerSocket socketServeur;
	/**
	 * Le socket necessaire au reseau, par lequel les données seront echangé
	 */
	protected Socket socket;

	/**
	 * Constructeur de base qui permet de récupérer le nom du l'adversaire
	 * @param server indique si l'on est le serveur ou le client
	 * @param numero_port le port d'écoute
	 * @param ip l'adresse ip où l'on souhaite se connecter
	 * @param nom_ le nom du joueur en local
	 * @param paneau l'affichage utilisé
	 */
	public Reseau(final boolean server, final int numero_port, final String ip, final String nom_, final Panneau paneau)
	{
		serveur = server;
		if(serveur == true)
		{
			try
			{
				socketServeur = new ServerSocket();
				socketServeur.bind(new InetSocketAddress(ip, numero_port));
				(new AffichageNormal(paneau)).afficherPop("En attente d'une connexion ...\n");
				socket = socketServeur.accept();
				//recevoir un nom
				BufferedReader in = new BufferedReader (new InputStreamReader (socket.getInputStream()));
				nom = in.readLine().substring(4);
				(new Thread(new Runnable(){
					public void run()
					{
						(new AffichageNormal(paneau)).afficherPop(nom + " vient de se connecter.");
					}
				})).start();
				//envoyer nom
				PrintWriter out = new PrintWriter(socket.getOutputStream());
						out.println("nom " + nom_);
				        	out.flush();
			}
			catch(Exception e)
			{
				(new AffichageNormal(paneau)).afficherErreur("Une erreur s'est produite. Le reseau peut ne pas fonctionner.\nIl est probable que vous ayez tente de lancer plusieur partie en reseau sur le meme port.\nOu encore que votre politique de securite n'autorise par ce jeu en reseau.\n");
				nom = null;
			}
		}
		else
		{
			boolean connexion = true;
			do
			{
				connexion = true;
				try
				{
					socket = new Socket(ip, numero_port);
					//envoyer nom
					PrintWriter out = new PrintWriter(socket.getOutputStream());
							out.println("nom " + nom_);
					        	out.flush();
					//recevoir un nom
					BufferedReader in = new BufferedReader (new InputStreamReader (socket.getInputStream()));
					nom = in.readLine().substring(4);
					(new AffichageNormal(paneau)).afficherChaine(nom + " vient de se connecter.\n");
				}
				catch(Exception e)
				{
					(new AffichageNormal(paneau)).afficherChaine("Connexion echouee ...\n");
					try
					{
						Thread.sleep(1000);//on fait roupiller le client en attendant de recommencer
					}
					catch(Exception a){}
					connexion = false;
				}
			}while(connexion == false);
	}
}
	/**
	 * Constructeur servant au clonage
	 * @param serveur_ini
	 * @param socketServeur_ini
	 * @param socket_ini
	 * @param nom_ini
	 */
	public Reseau(boolean serveur_ini, ServerSocket socketServeur_ini, Socket socket_ini, String nom_ini)
	{
		serveur= serveur_ini;
		socketServeur = socketServeur_ini;
		socket = socket_ini;
		nom = nom_ini;
	}
	/**
	 * Interface envoyant le coup précédant (sauf les partie nulle) et récupérant ensuite les coup de l'adversaire
	 */
	public Coup jouerCoup(PartieAbstraite partie)
	{
		Coup retour = null;
		//envoie du dernier coup joue par le joueur local sauf si c'est une demande de partie nulle
		if((! (partie.getListeCoup().size() == 0 && serveur == false)) && !(partie.getListeCoup().size() >= 1 && partie.getListeCoup().getLast().getAction() == 'n'))//condition fait pour le demmarage, de facon a ce que le client n'envoi rien
		{
			envoieCoup(partie.getListeCoup().getLast(), partie);
		}
		else if(partie.getListeCoup().size() >= 1 && partie.getListeCoup().getLast().getAction() == 'n')
		{
			partie.getListeCoup().removeLast();
		}
		retour = receptionCoup(partie);
		
		return retour;
	}
	
	
	/**
	 * Receptionne un coup
	 * @param partie partie pour laquelle le coup sera joué
	 * @return le coup reçu
	 */
	public Coup receptionCoup(PartieAbstraite partie)
	{
		String chaine = null;
		Coup coup = null;
		try
		{
			BufferedReader in = new BufferedReader (new InputStreamReader (socket.getInputStream()));
			chaine = in.readLine();
			System.out.println("[Client]: " + chaine);//TODO
			//traitement de la ligne
			if(chaine.substring(0, 4).equals("coup"))
			{
				chaine = chaine.substring(5);
			}
			coup = Humain.traitement(chaine, partie);
		}
		catch(Exception e)
		{
			partie.getAffichage().afficherErreur(e.getMessage());
		}
		return coup;
	}
	/**
	 * Envoie un coup
	 * @param coup le à envoyer
	 * @param partie la partie sert à récupérer l'affichage
	 */
	public void envoieCoup(Coup coup, PartieAbstraite partie)
	{
		try
		{
			if(coup.getAction() != 'n' && coup.getAction() != 'b')
			{
				System.out.println("[Serveur]: coup " + coup);//TODO
				PrintWriter out = new PrintWriter(socket.getOutputStream());
				out.println("coup " + coup);
				out.flush();
			}
			else
			{
				System.out.println("[Serveur]: " + coup);//TODO
				PrintWriter out = new PrintWriter(socket.getOutputStream());
				out.println(coup);
				out.flush();
			}
			
			
		}
		catch(Exception e)
		{
			partie.getAffichage().afficherErreur(e.getMessage());
		}
	}

	public Joueur clonage()
	{
		return new Reseau(serveur, socketServeur, socket, nom); 
	}
	/**
	 * Ferme les different socket
	 * @param partie utilise l'affichage de la partie pour afficher les erreurs
	 */
	public void close(PartieAbstraite partie)
	{
		try
		{
			socket.close();
			if(serveur == true)
			{
				socketServeur.close();
			}
		}
		catch(Exception e)
		{
			partie.getAffichage().afficherErreur(e.getMessage());
		}
	}
}
	
