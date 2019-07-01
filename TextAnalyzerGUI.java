import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;

public class TextAnalyzerGUI extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L; // default serial id
	private JButton hashB, treeB; // globali perché devono essere attivati e disattivati
	
	public TextAnalyzerGUI() {
		super("Simple text analyzer"); // creo il frame
		setBounds(450, 200, 300, 140);
		setLayout(new GridLayout(2, 1));

		JPanel top = new JPanel(), center = new JPanel();
		top.setLayout(new BorderLayout());
		JLabel label = new JLabel("Which structure do you want to use?", SwingConstants.CENTER);
		top.add(label, BorderLayout.CENTER);

		hashB = new JButton("Hash Table");
		treeB = new JButton("Tree Map");
		hashB.addActionListener(this);
		treeB.addActionListener(this);

		center.add(hashB);
		center.add(treeB);

		add(top);
		add(center);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
	}

	public void actionPerformed(ActionEvent event) {
		JFileChooser file = new JFileChooser();
		file.showOpenDialog(null);
		disableButtons(); // disattivo temporaneamente i pulsanti per evitare analisi concorrenti
		if (file.getSelectedFile() != null) {
			TextAnalyzer ta = null;
			if (event.getActionCommand().equals("Hash Table")) // scelgo la struttura da utilizzare
				ta = new TextAnalyzer(file.getSelectedFile(), "hash");
			else
				ta = new TextAnalyzer(file.getSelectedFile(), "tree"); // l'unica alternativa è Tree Map
			Stats stats = ta.getStats(); // ottengo i dati dell'elaborazione del documento
			
			String[] columns = {"Order", "Word", "Occurrences", "%"}; // nomi delle colonne
			JTable table = new JTable(stats.getWords(), columns);
			int nWords = stats.getNWords();
			int wSpaces = stats.getLength();
			int rows = stats.getRows();
			int noSpaces = wSpaces - stats.getSpaces();
			long time = stats.getTime();
			table.setEnabled(false); // impedisco la modifica
			table.getColumnModel().getColumn(0).setPreferredWidth(5);
			table.getColumnModel().getColumn(2).setPreferredWidth(35);
			table.getColumnModel().getColumn(3).setPreferredWidth(5);

			// creo il frame delle statistiche col nome del file analizzato
			JFrame statsFrame = new JFrame("Statistics of: " + file.getName(file.getSelectedFile()));
			statsFrame.setBounds(300, 180, 670, 415);
			statsFrame.setLayout(new GridLayout(1, 2, 10, 0));
			JScrollPane left = new JScrollPane(table);
			JPanel right = new JPanel(new GridLayout(5, 1));
			JLabel wSpacesLab = new JLabel("Number of characters (including spaces): " + wSpaces);
			JLabel noSpacesLab = new JLabel("Number of characters (without spaces): " + noSpaces);
			JLabel nWordsLab = new JLabel("Number of words: " + nWords);
			JLabel rowsLab = new JLabel("Number of rows: " + rows);
			JLabel timeLab = new JLabel("Processing time: " + time + " milliseconds");
			right.add(wSpacesLab);
			right.add(noSpacesLab);
			right.add(nWordsLab);
			right.add(rowsLab);
			right.add(timeLab);
			statsFrame.add(left);
			statsFrame.add(right);
			statsFrame.setVisible(true);
			enableButtons();
		}
		else // TODO: da implementare graficamente
			System.out.println("Please, select a valid text file.");
	}

	private void enableButtons() {
		hashB.setEnabled(true);
		treeB.setEnabled(true);
	}
	
	private void disableButtons() {
		hashB.setEnabled(false);
		treeB.setEnabled(false);
	}

	public static void main(String[] args) {
		TextAnalyzerGUI gui = new TextAnalyzerGUI(); // creo la GUI
		gui.setVisible(true); // rendo il frame visibile
	}
}
