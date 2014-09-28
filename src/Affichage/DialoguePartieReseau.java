package Affichage;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
/**
 * Cette classe affiche la boite de dialogue necessaire a la recuperation d'information pour creer une partie en reseau
 * @author Gladiat
 *
 */
public class DialoguePartieReseau extends JDialog
{
	/**
	 * Tous les attributs
	 */
	private static final long serialVersionUID = 1L;
	private JButton envoyer = new JButton("Demarrer la Partie !");
	private JTextField saisieNom1 = new JTextField("zone de saisie");
	private JTextField saisie_ip = new JTextField("127.0.0.1");
	private JTextField saisie_port = new JTextField("65535");
	private JCheckBox joueur1 = new JCheckBox("Ordinateur");
    private JCheckBox serveur_check = new JCheckBox("Serveur");
    
    private JLabel label1 = new JLabel("Nom");
    private JLabel label_ip = new JLabel("Adresse IP: ");
    private JLabel label_port = new JLabel("Port: ");
    
    private Object[] retour = new Object[5];
    
    private boolean ordi1 = false;
    private boolean serveur = false;
    /**
     * constructeur de base
     * @param parent
     * @param title
     * @param modal
     */
	public DialoguePartieReseau(JFrame parent, String title, boolean modal)
	{
		super(parent, title, modal);
		//saisir le type de joueur avec case a cocher
		//zone de saisi pour les nom des joueur ou leur difficulte
		JPanel pan = new JPanel(), j1 = new JPanel(), j2 = new JPanel(), bouton = new JPanel();
		
		
		envoyer.addMouseListener(new MouseAdapter(){
         	public void mouseReleased(MouseEvent event){
                
       		 retour[0] = ordi1;
       		 retour[1] = saisieNom1.getText();
       		 retour[2] = serveur;
       		 retour[3] = saisie_ip.getText();
       		 retour[4] = saisie_port.getText();
       		 setVisible(false);
       	 }
        });
		envoyer.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent e) {}
			public void keyReleased(KeyEvent e) {}
			public void keyPressed(KeyEvent e)
			{
				if(e.getKeyChar() == '\n')
				{
					 retour[0] = ordi1;
		       		 retour[1] = saisieNom1.getText();
		       		 retour[2] = serveur;
		       		 retour[3] = saisie_ip.getText();
		       		 retour[4] = saisie_port.getText();
		       		 setVisible(false);
				}
			}
		});
		joueur1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0)
			{
				ordi1 = ((JCheckBox)arg0.getSource()).isSelected();
				if(ordi1 == true)
				{
					label1.setText("Difficulte");
				}
				else
				{
					label1.setText("Nom");
				}
			}
		});
		serveur_check.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0)
			{
				serveur = ((JCheckBox)arg0.getSource()).isSelected();
			}
		});
		j1.add(joueur1);
		j1.add(label1);
		j1.add(saisieNom1);
		j2.add(serveur_check);
		j2.add(label_ip);
		j2.add(saisie_ip);
		j2.add(label_port);
		j2.add(saisie_port);
		bouton.add(envoyer);
		
		pan.add(j1);
		pan.add(j2);
		pan.add(bouton);
		this.setContentPane(pan);
		this.setLocationRelativeTo(null);
		this.setTitle("Initialiser");
		this.setSize(350, 185);
	}
	/**
	 * Active la boite de dialogue pour la rendre visible
	 * @return un tableau d'objet contenant les informations recuperees
	 */
	public Object[] activer()
	{
		this.setVisible(true);
		return retour;
	}
}
