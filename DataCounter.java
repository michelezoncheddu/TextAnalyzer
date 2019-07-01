import java.util.Iterator;
import java.util.NoSuchElementException;

public interface DataCounter<E extends Comparable<E>> {
	// OVERVIEW: tipo modificabile che rappresenta una collezione finita di
	//           elementi di tipo E, che associa un valore numerico ad ogni elemento
	// TYPICAL ELEMENT: una funzione parziale f: E -> V, con V insieme di valori numerici,
	//                  e { x | f(x) definita } con x appartenente ad E, è un insieme finito

	// incrementa il valore associato all'elemento data di tipo E
	public void incCount(E data) throws NullPointerException, NoSuchElementException;
	// REQUIRES: data != null && f(data) definito
	// THROWS: se data = null lancia NullPointerException (disponibile in Java, unchecked),
	//         se f(data) non è definito lancia NoSuchElementException (disponibile in Java, unchecked)
	// MODIFIES: f
	// EFFECTS: incrementa il valore associato a f(data) 

	// restituisce il numero degli elementi presenti nella collezione
	public int getSize();
	// EFFECTS: restituisce la somma di f(x) tale che f(x) è definita

	// restituisce il valore corrente associato al parametro data,
	// e 0 se data non appartiene alla collezione
	public int getCount(E data) throws NullPointerException;
	// REQUIRES: data != null
	// THROWS: se data = null lancia NullPointerException
	// EFFECTS: restituisce f(data) se è definito, 0 altrimenti

	// resituisce un iteratore (senza remove) per la collezione
	public Iterator<E> getIterator();
	// EFFECTS: restituisce un iteratore per scorrere gli elementi della collezione

	public int getSizeWithoutDuplicates();
	// EFFECTS: restituisce #{ x | f(x) definita }
}
