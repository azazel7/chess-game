package Affichage;

import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import General.Coup;
/**
 * Cette classe gerer une boite de dialogue affichant les coup d'une partie
 * @author Anakin
 *
 */
public class DialogueAfficherCoup extends JDialog
{
	private static final long serialVersionUID = 1L;
	/**
	 * Constructeur de base
	 * @param parent
	 * @param title
	 * @param modal
	 * @param liste liste des coup a afficher
	 */
	public DialogueAfficherCoup(JFrame parent, String title, boolean modal, LinkedList<Coup> liste)
	{
		super(parent, title, modal);
		
		String titre[] = { "Blanc", "Noir"};
		Object data[][] = new String[(int) Math.ceil(liste.size()/2) + 1][2];
		Iterator<Coup> iterator = liste.listIterator();
		String tmp;
		int i = 0, rang_b = 0, rang_n = 0;
		while(iterator.hasNext())
		{
			tmp = iterator.next().toString();
			if(i%2 == 0)
			{
				data[rang_b][0] = tmp;
				rang_b++;
			}
			else
			{
				data[rang_n][1] = tmp;
				rang_n++;
			}
			i++;
		}
		JTable tableau = new JTable(data, titre);
		tableau.getColumnModel().getColumn(0).setMaxWidth(75);
		tableau.getColumnModel().getColumn(1).setMaxWidth(75);
		this.getContentPane().add(new JScrollPane(tableau));
		this.setLocationRelativeTo(null);
		this.setTitle("Liste des coups");
		this.setSize(150, 450);
	}
	/**
	 * Active la boite de dialogue pour la rendre visible
	 */
	public void activer()
	{
		this.setVisible(true);
	}
}
