package Affichage;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.Thread.State;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import General.Main;
import JoueurEchec.Joueur;
import Partie.PartieAbstraite;
import Partie.PartieNormalLocal;
import JoueurEchec.Humain;
import JoueurEchec.IA;
import JoueurEchec.Reseau;

/**
 * Cette classe s'occupe de la gestion de l'affichage et de l'interface graphique
 * @author Arkkandia
 *
 */
public class Fenetre extends JFrame{
 /**
  * Tout les attribu de la classe et lrd composant graphique
  */
	private static final long serialVersionUID = 1L;
	private JTextField saisieCoup = new JTextField();
	 private JButton envoyer = new JButton("Envoyer");
	 
	 private JMenuBar barMenu = new JMenuBar();
	 private JMenu partieMenu = new JMenu("Partie");
	 private JMenu aide = new JMenu("Aide");
			
	private JMenuItem nouvellePartieNormale = new JMenuItem("Nouvelle partie");
	private JMenuItem nouvellePartieReseau = new JMenuItem("Nouvelle partie en reseau");
	private JMenuItem sauvegarderPartie = new JMenuItem("Sauvegarder une Partie");
	private JMenuItem chargerPartie = new JMenuItem("Charger une partie");
	private JMenuItem listeDesCoup = new JMenuItem("Afficher la liste des coups");
	private JMenuItem stopPartie = new JMenuItem("Stopper la partie en cours");
	private JMenuItem verifNulle = new JMenuItem("Partie nulle ?");
	private JMenuItem aideItem = new JMenuItem("Aide");
	
	
	private JPanel principal = new JPanel(null);
	private JPanel sud = new JPanel();
	private Panneau  centre = new Panneau(null);
	
	private PartieAbstraite partie;
	private Thread partie_en_cour;
	/**
	 * Constructeur mettant en place la disposition des composant
	 */
	
     public Fenetre()
     {
             this.setTitle("Echec");
             this.setSize(700, 650);
             this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
             this.setLocationRelativeTo(null);
             
             sud.setLayout(new GridLayout(1, 10));
             envoyer.addActionListener(new EnvoyerCoup());
             envoyer.addKeyListener(new EnvoyerCoup());
             saisieCoup.addKeyListener(new EnvoyerCoup());
             envoyer.setEnabled(false);
             saisieCoup.setEnabled(false);
             sud.add(saisieCoup);
             sud.add(envoyer);
            
             principal.setLayout(new BorderLayout());
             principal.add(sud, BorderLayout.SOUTH);
             principal.add(centre, BorderLayout.CENTER);
             
             
             aideItem.addMouseListener(new MouseAdapter(){
             	public void mouseReleased(MouseEvent event){
        
             		JOptionPane.showMessageDialog(null, "-h --help  -> lancer l'aide en console\n-c --console  -> lancer le mode textuel\npartie nulle: demande une partie nulle.\npartie abandon: envoie un abandon de partie.\n"
             		+ "Les Notations Algebriques:\n- Complete Le nom de la piece, Sa position de depart, se quelle fait puis sa case d'arrive et eventuellement sa promotion ou sa prise en passant\n"
             		+ "- Abrege Le nom de la piece, eventuellement sa colone ou sa ligne de depart puis sa case d'arrive, et ensuite sa promotion\n"
             		+ "Exemple: h2-h3 = h3 = pion par de h2 pour aller en h3\n"
             		+ "Ch1xg3 = Cg3 = cavalier par de h1 pour aller manger en g3\n"
             		+ "h7xg8=D = g8D = un pion va en g8 en mangeant et est promue en dame\n"
             		+ "R = roi, D = dame, F = fou, C = cavalier, T = tour,  = pion\nx = mange une piece, - = mouvement simple, e.p a la fin = prise en passant", "Information", JOptionPane.INFORMATION_MESSAGE);
            	 }
             });
             nouvellePartieNormale.addMouseListener(new NouvellePartieLocale());
             nouvellePartieReseau.addMouseListener(new NouvellePartieReseau());
             sauvegarderPartie.addMouseListener(new SauvegarderPartie());
             chargerPartie.addMouseListener(new ChargerPartie());
             listeDesCoup.addMouseListener(new AfficherListeDeCoup());
             verifNulle.addMouseListener(new VerifNulle());
             stopPartie.addMouseListener(new StopperPartie());
            		
             this.partieMenu.add(nouvellePartieNormale);
             this.partieMenu.add(nouvellePartieReseau);
 			 this.partieMenu.add(sauvegarderPartie);
     		 this.partieMenu.add(chargerPartie);
     		 this.partieMenu.add(listeDesCoup);
     		 this.partieMenu.add(stopPartie);
     		 this.partieMenu.add(verifNulle);
     		 this.aide.add(aideItem);
     		 
   			 this.barMenu.add(partieMenu);
     		 this.barMenu.add(aide);
     		 
             this.setJMenuBar(barMenu);
             this.setContentPane(principal);
             this.setVisible(true);
     }
     
     /**
      * Classe interne qui gere l'appuie de la touche entr� pour envoyer des coup. Elle gere aussi le clique pour le bouton
      * @author Ellana
      *
      */
     class EnvoyerCoup implements ActionListener, KeyListener
     {
    	 public void actionPerformed(ActionEvent arg0)
         {
    		if(partie_en_cour != null)
    		{
    			if(partie_en_cour.getState() == State.TIMED_WAITING || partie_en_cour.getState() == State.WAITING)
        		{
        			if(saisieCoup.getText() != null && !saisieCoup.getText().equals(""))
        			{
        				partie.getAffichage().setSaisie(saisieCoup.getText());
        				saisieCoup.setText("");
        			}
        		}
    		}
         }

		@Override
		public void keyPressed(KeyEvent arg0) {
			if(arg0.getKeyChar() == '\n')
			{
				if(partie_en_cour != null)
	    		{
	    			if(partie_en_cour.getState() == State.TIMED_WAITING || partie_en_cour.getState() == State.WAITING)
	        		{
	        			if(saisieCoup.getText() != null && !saisieCoup.getText().equals(""))
	        			{
	        				partie.getAffichage().setSaisie(saisieCoup.getText());
	        				saisieCoup.setText("");
	        			}
	        		}
	    		}
			}
		}

		@Override
		public void keyReleased(KeyEvent arg0) 
		{}

		@Override
		public void keyTyped(KeyEvent arg0)
		{}
     }

    /**
     * classe interne gerant le clique sur le menu pour lancer le chargement d'une partie
     * @author Rafi
     *
     */
     class ChargerPartie extends MouseAdapter
     {
    	 public void mouseReleased(MouseEvent event)
     	{
     		String nom_svg = JOptionPane.showInputDialog(null, "Entre le nom du fichier de sauvegarde", "Sauvegarder Partie", JOptionPane.QUESTION_MESSAGE);
     		if(nom_svg == null)
     		{
     			return;
     		}
     		if(partie != null && partie_en_cour != null)
    		{
    			partie.setFin(true);
    			partie.getAffichage().setSaisie("abbandon");
    			partie = null;
    			partie_en_cour = null;
    			centre.setPartie(null);
    			centre.repaint();
    		}
     		partie = new PartieNormalLocal(null, null, centre);
     		partie.chargerPartie(nom_svg);
     		//faire un set sur une JFrame dans affichage
     		if(partie_en_cour != null && partie_en_cour.getState() != State.TERMINATED)
     		{
     			partie_en_cour.interrupt();
     		}
 			partie_en_cour = new Thread(new Runnable()
 			{
 				public void run()
					{
						partie.commencerPartie();
						envoyer.setEnabled(false);
			 			saisieCoup.setEnabled(false);
			 			saisieCoup.setBorder(BorderFactory.createLineBorder(Color.white));
					}
 			}	
 			);
 			envoyer.setEnabled(true);
 			saisieCoup.setEnabled(true);
 			saisieCoup.setBorder(BorderFactory.createLineBorder(new Color(210, 105, 30)));
 			saisieCoup.requestFocus();
 			partie_en_cour.start();
 			
    	 	}
     }
     
    /**
     * Classe interne gerant le clique dans le menu pour lancer la procedure de sauvegarde d'une partie
     * @author Francis Cabrel
     *
     */
     class SauvegarderPartie extends MouseAdapter
     {
    	 public void mouseReleased(MouseEvent event)
     	{
     		String nom_svg = JOptionPane.showInputDialog(null, "Entre le nom du fichier de sauvegarde", "Sauvegarder Partie", JOptionPane.QUESTION_MESSAGE);
     		if(nom_svg == null)
     		{
     			return;
     		}
     		if(partie != null)
     		{
     			partie.sauvegarderPartie(nom_svg);
     		}
     	}
     }

     /**
      * Classe interne gerant le lancement d'une partie en reseau a partie de l'interface graphique ainsi que tout ce qui est relatif a la connexion
      * @author Savara
      *
      */
     class NouvellePartieReseau extends MouseAdapter
     {
    		public void mouseReleased(MouseEvent event){
    			
    			partie_en_cour = new Thread(new Runnable()
    			{
    				public void run()
					{
    					DialoguePartieReseau pop = new DialoguePartieReseau(null, null, true);
    	        		Object info[] = pop.activer();
    	        		
    	        		for(int i = 0; i < 5; i++)
    	        		{
    	        			if(info[i] == null)
    	        			{
    	        				return;
    	        			}
    	        		}
    	        		Joueur joueur[] = new Joueur[2];
    	        		boolean serveur = false;
    	        		int port = 0;
    	        		if(info[0].toString().equals("true"))
    	        		{
    	        			if(Main.isInteger((String)info[1]))
    	        			{
    	        				joueur[0] = new IA(Integer.parseInt((String)info[1]));
    	        			}
    	        			else
    	        			{
    	        				JOptionPane.showMessageDialog(null, "La difficultee de l'ordinateur doit etre un nombre superieur a 0.Auquel cas il sera ramenee a 1", "Erreur", JOptionPane.ERROR_MESSAGE);
    	        				 joueur[0] = new IA(1);
    	        			}
    	        		}
    	        		else
    	        		{
    	        			joueur[0] = new Humain((String)info[1]);
    	        		}
    	        		
    	        		if(!Main.isIP((String)info[3]))
    	        		{
    	        			JOptionPane.showMessageDialog(null, "Adresse IP incorrecte", "Erreur", JOptionPane.ERROR_MESSAGE);
    	        			return;
    	        		}
    	        		if(!Main.isInteger((String)info[4]))
    	        		{
    	        			JOptionPane.showMessageDialog(null, "Numero de port incorrecte. Il doit etre comprit entre 1 et 65535.", "Erreur", JOptionPane.ERROR_MESSAGE);
    	        			return;
    	        		}
    	        		else
    	        		{
    	        			port = Integer.parseInt((String)info[4]);
    	        		}
    	        		if(info[2].toString().equals("true"))
    	        		{
    	        			serveur = true;
    	        		}
    	        		if(partie != null && partie_en_cour != null)
    	        		{
    	        			partie.setFin(true);
    	        			partie.getAffichage().setSaisie("abbandon");
    	        			partie = null;
    	        			partie_en_cour = null;
    	        			centre.setPartie(null);
    	        			centre.repaint();
    	        		}
    	        		
    	        		joueur[1] = new Reseau(serveur, port, (String)info[3], joueur[0].getNom(), centre);
    	    			if(joueur[1].getNom() != null)
    	    			{
    	    				if(serveur == false)//inverser les deux joueurs dans le cas d'un client
    	    				{
    	    					Joueur tmp = joueur[1];
    	    					joueur[1] = joueur[0];
    	    					joueur[0] = tmp;
    	    				}
    	    				//lancer partie
    	    				partie = new PartieNormalLocal(joueur[0], joueur[1], centre);
    	    				envoyer.setEnabled(true);
    	     				saisieCoup.setEnabled(true);
    	     				saisieCoup.requestFocus();
    	     				saisieCoup.setBorder(BorderFactory.createLineBorder(new Color(210, 105, 30)));
    	     				partie.commencerPartie();
							envoyer.setEnabled(false);
    	     				saisieCoup.setEnabled(false);
    	     				saisieCoup.setBorder(BorderFactory.createLineBorder(Color.white));
    	    			}
					}
    			}	
    			);
    			partie_en_cour.start();
          	 }
     }
     /**
      * Classe interne gerant le lancement d'une partie local a partie de l'interface graphique
      * @author Gwen
      *
      */
     class NouvellePartieLocale extends MouseAdapter
     {
    		public void mouseReleased(MouseEvent event){
           		DialoguePartieLocal pop = new DialoguePartieLocal(null, null, true);
        		Object info[] = pop.activer();
        		
        		for(int i = 0; i < 4; i++)
        		{
        			if(info[i] == null)
        			{
        				return;
        			}
        		}
        		Joueur joueur[] = new Joueur[2];
        		for(int i = 0, cur = 0; i <= 2; i += 2, cur++)
        		{
        			if(info[i].toString().equals("true"))
            		{
            			if(Main.isInteger((String)info[i+1]))
            			{
            				joueur[cur] = new IA(Integer.parseInt((String)info[i+1]));
            			}
            			else
            			{
            				JOptionPane.showMessageDialog(null, "La difficulte de l'ordinateur doit etre un nombre superieur a 0.Auquel cas il sera ramene a 1", "Erreur", JOptionPane.ERROR_MESSAGE);
            				 joueur[cur] = new IA(1);
            			}
            		}
            		else
            		{
            			joueur[cur] = new Humain((String)info[i+1]);
            		}
        		}
        		if(partie != null && partie_en_cour != null)
        		{
        			partie.setFin(true);
        			partie.getAffichage().setSaisie("abbandon");
        			partie = null;
        			partie_en_cour = null;
        			centre.setPartie(null);
        			centre.repaint();
        		}
    			partie = new PartieNormalLocal(joueur[0], joueur[1], centre);
    			partie_en_cour = new Thread(new Runnable()
    			{
    				public void run()
					{
    					envoyer.setEnabled(true);
    	     			saisieCoup.setEnabled(true);
    	     			saisieCoup.requestFocus();
    	     			saisieCoup.setBorder(BorderFactory.createLineBorder(new Color(210, 105, 30)));
						partie.commencerPartie();
						envoyer.setEnabled(false);
    	     			saisieCoup.setEnabled(false);
    	     			saisieCoup.setBorder(BorderFactory.createLineBorder(Color.white));
					}
    			}	
    			);
    			partie_en_cour.start();
          	 }
     }
     /**
      * Classe interne gerant le lancement de l'affichage des coup a un instant T
      * @author Vagabonde
      *
      */
     class AfficherListeDeCoup extends MouseAdapter
     {
    	 public void mouseReleased(MouseEvent event)
    	 {
    		 if(partie != null)
    		 {
    			 (new DialogueAfficherCoup(null, null, true, partie.getListeCoup())).activer();
    		 }
    	 }
     }
     /**
      * Classe interne permettant de stopper une partie en cour via l'interface graphique
      * @author Grenth
      *
      */
     class StopperPartie extends MouseAdapter
     {
    	 public void mouseReleased(MouseEvent event)
    	 {
    		 if(partie_en_cour != null)
    		 {
    			 if(partie != null)
    			 {
    				 partie.setFin(true);
    				 partie.getAffichage().setSaisie("abbandon");
    				 partie = null;
    			 }
    			 partie_en_cour = null;
    			 centre.setPartie(null);
    			 centre.repaint();
    			 envoyer.setEnabled(false);
    			 saisieCoup.setEnabled(false);
    		 }
    		 
    	 }
     }
     /**
      * Classe interne qui informe si une partie peut-etre declaree nulle selon certaine regle officiel qui sont les 50 coup et la triple repetition
      * @author Balthazar
      *
      */
     class VerifNulle extends MouseAdapter
     {
         public void mouseReleased(MouseEvent event)
         {
        	 if(partie != null)
        	 {
        		 if(partie.estNulle())
        		 {
        			 JOptionPane.showMessageDialog(null, "La partie peut etre declaree nulle\nUtiliser \"partie nulle\" pour envoyer une demande.\n", "Information", JOptionPane.INFORMATION_MESSAGE);
                	 
        		 }
        		 else
        		 {
        			 JOptionPane.showMessageDialog(null, "La partie n'est pas nulle de maniere officiel.\n", "Information", JOptionPane.INFORMATION_MESSAGE);
                	 
        		 }
        	 }
        }
     }
}