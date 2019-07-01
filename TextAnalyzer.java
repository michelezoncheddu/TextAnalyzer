import java.util.Scanner;
import java.util.Vector;
import java.util.Iterator;
import java.io.File;
import java.io.FileNotFoundException;

public class TextAnalyzer {
	private final DataCounter<String> dc;
	private int rows; // numero di righe del documento
	private int length; // numero di caratteri totali
	private int spaces; // numero di spazi
	private final long time; // tempo di elaborazione
	private final Iterator<String> iterator;
	
	public TextAnalyzer(File source, String mode) {
		Vector<String> words = new Vector<>();
		long start = 0;
		rows = 0;
		length = 0;
		spaces = 0;
		try {
			Scanner in = new Scanner(source);
			start = System.currentTimeMillis(); // avvio il timer
			while (in.hasNextLine()) {
				rows++;
				String phrase = in.nextLine(); // leggo una linea
				length += phrase.length(); // conto il numero di caratteri
				for (int i = 0; i < phrase.length(); i++) // conto gli spazi
					if (phrase.charAt(i) == ' ')
						spaces++;
				phrase = phrase.replaceAll("['’‘<>()\\[\\]{}]", " ");
				phrase = phrase.replaceAll("[\\p{Punct}…“”«»—]", ""); // cancella la punteggiatura
				String[] tmp = phrase.split(" +"); // separa le parole tra un qualunque numero di spazi
				for (String elem : tmp)
					if (! elem.equals("")) // la rimozione di caratteri può generare stringhe vuote
						words.add(elem.toLowerCase());
			}
			in.close();
		} catch (FileNotFoundException e) {
			System.err.println("File error!");
		}
		if (mode.equals("hash")) // scelgo la struttura da utilizzare
			dc = new HashDataCounter<>(words);
		else
			dc = new TreeDataCounter<>(words); // l'unica alternativa è TreeDataCounter
		iterator = dc.getIterator();
		time = System.currentTimeMillis() - start; // fermo il tempo dopo aver ordinato con l'iteratore
	}
	
	public Stats getStats() {
		int size = dc.getSizeWithoutDuplicates();
		String[][] words = new String[size][4]; // dati per la tabella
		for (int i = 0; i < size; i++) {
			String tmp = iterator.next();
			words[i] = new String[4];
			words[i][0] = Integer.toString(i+1); // numero di parola
			words[i][1] = tmp; // parola
			int occ = dc.getCount(tmp);
			words[i][2] = Integer.toString(occ); // numero di occorrenze della parola
			float percentage = (float)occ / dc.getSize() * 100; // percentuale di occorrenze sul totale
			words[i][3] = String.format("%.3f", percentage); // limito la percuentuale a 3 cifre decimali
		}
		// utilizzo un oggetto Stats di supporto per inviare i dati alla GUI
		Stats stats = new Stats(words, dc.getSize(), rows, length, spaces, time);
		return stats;
	}
}
