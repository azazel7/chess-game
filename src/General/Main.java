package General;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



import Affichage.Fenetre;
import JoueurEchec.Humain;
import JoueurEchec.IA;
import JoueurEchec.Joueur;
import JoueurEchec.Reseau;
import Partie.PartieNormalLocal;
/**
 * 
 * @author Ellana
 *
 */
public class Main 
{
	/**
	 * @param args -h pour lancer l'aide
	 */
	public static void main(String[] args) 
	{
		boolean gui = true;
		for(String arg : args)
		{
			if(arg.equalsIgnoreCase("-c") || arg.equalsIgnoreCase("--console"))
			{
				gui = false;
			}
			else if(arg.equalsIgnoreCase("-h") || arg.equalsIgnoreCase("--help"))
			{
				System.out.println("-h --help  -> lancer cet aide");
				System.out.println("-c --console  -> lancer la demande de console");
				System.out.println("partie nulle: demande une partie nulle");
				System.out.println("partie abandon: envoie un abandon de partie");
			}
		}
		if(gui)
		{
			Fenetre fenetre = new Fenetre();
			if(fenetre != null)//pu de warning :-)
			{
				
			}
		}
		else
		{
			Scanner sc = new Scanner(System.in);
			String choix;
			System.out.print("1 - Local\n" +
							 "2 - Reseau\n" +
							 "3 - Charger une partie\n" +
							 "Votre choix: ");
			choix = sc.nextLine();
			if(choix.equals("1"))
			{
				Joueur joueur[] = new Joueur[2];
				joueur[0] = saisieJoueur();
				if(joueur[0] != null)
				{
					joueur[1] = saisieJoueur();
					if(joueur[1] != null)
					{
						PartieNormalLocal partie = new PartieNormalLocal(joueur[0], joueur[1], null);
						partie.commencerPartie();
					}
				}
			}
			else if(choix.equals("2"))
			{
				String ip = null;
				boolean serveur = true;
				int port = 33775;
				Joueur joueur = null;
				do
				{
					System.out.print("\n1 - Herberger la partie\n2 - Rejoindre une partie\nAutre - Quiter\nVotre choix: ");
					choix = sc.nextLine();
					if(choix.equals("1"))
					{}
					else if(choix.equals("2"))
					{
						serveur = false;
					}
					else
					{
						break;
					}
					
					System.out.print("Adresse ip v4: ");
					ip = sc.nextLine();
					if(isIP(ip) == false)
					{
						System.out.println("Adresse non conforme.");
						break;
					}
					System.out.print("Numero de port (defaut 33775): ");
					choix = sc.nextLine();
					if(isInteger(choix) == false && ! choix.equals(""))
					{
						System.out.println("Numero de port non conforme.");
						break;
					}
					if(! choix.equals(""))
					{
						port = Integer.parseInt(choix);
					}
					if(port <= 0 || port >= 65536)
					{
						System.out.println("Le numero de port doit etre superieur a 0 et inferieur a 65536.");
						break;
					}
					joueur = saisieJoueur();
					Joueur reseau = new Reseau(serveur, port, ip, joueur.getNom(), null);
					if(joueur != null)
					{
						PartieNormalLocal partie = null;
						if(!serveur)//le serveur commence a jouer
						{
							partie = new PartieNormalLocal(reseau, joueur, null);
						}
						else
						{
							partie = new PartieNormalLocal(joueur, reseau, null);
						}
						
						partie.commencerPartie();
					}
					
				}while(false);
			}
			else if(choix.equals("3"))
			{
				PartieNormalLocal partie = new PartieNormalLocal(null, null, null);
				partie.getAffichage().afficherChaine("Entrez le chemin de la sauvegarde: ");
				partie.chargerPartie(partie.getAffichage().SaisirChaine());
				partie.commencerPartie();
			}
		}
	}
	
	/**
	 * 
	 * @return un joueur qui a  etait saisie (en console exclusivement)
	 */
	public static Joueur saisieJoueur()
	{
		String choix = null, nom = null;
		boolean humain = true;
		int difficulte = 1;

		Scanner sc = new Scanner(System.in);
		System.out.print("\n1 - Humain\n2 - Intelligence Artificiel\nAutre - Quiter\nVotre choix: ");
		choix = sc.nextLine();
		if(choix.equals("1"))
		{
			humain = true;
		}
		else if(choix.equals("2"))
		{
			humain = false;
		}
		else
		{
			return null;
		}
		if(humain == true)
		{
			do
			{
				System.out.print("Pseudo: ");
				nom = sc.nextLine();
			}while(nom.equals(""));
			return new Humain(nom);
		}	
		else
		{
			System.out.print("Difficulte IA (1,2,3, 4 ...): ");
			choix = sc.nextLine();
			if(choix.equals("1"))
			{
				difficulte = 1;
			}
			else if(choix.equals("2"))
			{
				difficulte = 2;
			}
			else if(choix.equals("3"))
			{
				difficulte = 3;
			}
			else
			{
				if(isInteger(choix))
				{
					difficulte = Integer.parseInt(choix);
					difficulte = Math.abs(difficulte);
				}
				else
				{
					return null;
				}
				
			}
			nom = IA.generateName();
			System.out.println("Nom IA: " + nom);
			return new IA(nom, difficulte);
		}
	}
	
	/**
	 * Verifie si une chaine est un nombre positif
	 * @param chaine la chaine a verifier
	 * @return true si la chaine correspond Ã  un nombre positif
	 */
	
	public static boolean isInteger(String chaine)
	{
		for(int i = 0; i < chaine.length(); i++ )
		{
			if(chaine.charAt(i) < '0' || chaine.charAt(i) > '9')
			{
				return false;
			}
		}
	
		return true;
	}
	/**
	 * Verifie si une chaine correspond a une adresse ip
	 * @param chaine la chaie a verifier
	 * @return true si cela correspond a une adresse ip
	 */
	public static boolean isIP(String chaine)
	{
		Pattern format = Pattern.compile("([0-9]{1,3})\\.([0-9]{1,3})\\.([0-9]{1,3})\\.([0-9]{1,3})");
		Matcher matcher = format.matcher(chaine);
		if(matcher.matches() == true)
		{
			for(int i = 1; i <= 4; i++)
			{
				if(Integer.parseInt(matcher.group(i)) < 0 || Integer.parseInt(matcher.group(i)) > 255)
				{
					return false;
				}
			}
		}
		else
		{
			return false;
		}
		return true;
	}
}