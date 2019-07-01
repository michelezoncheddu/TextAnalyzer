import java.util.Hashtable;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.Vector;

public class HashDataCounter<K extends Comparable<K>> implements DataCounter<K> {
	// AF: f: K -> V tale che f(x_i) = table.get(x_i), con x = table.key(i) e 0 <= i < table.size(),
	//     non definita altrimenti
	// IR: table != null && size > 0 &&
	//     forall x: table.get(x) != null ==> table.get(x) > 0 &&
	//     && size = Σ{ x: table.get(x) != null . table.get(x) }
	private final Hashtable<K, Integer> table;
	private int size;

	public HashDataCounter(Vector<K> keys) throws NullPointerException, IllegalArgumentException {
		// REQUIRES: keys != null && keys.size() > 0 &&
		//           forall i: 0 <= i < keys.size() ==> keys.get(i) != null
		// THROWS: se keys = null, o un qualunque elemento di keys è null, 
		//         lancia NullPointerException (disponibile in Java, unchecked);
		//         se keys.length = 0 lancia IllegalArgumentException (disponibile in Java, unchecked)
		// MODIFIES: this
		// EFFECTS: inizializza table utilizzando gli elementi di keys come chiavi,
		//          e il numero di occorrenze di ogni elemento come valori associati;
		//          inizializza size come keys.length
		if (keys == null)
			throw new NullPointerException();
		if (keys.size() == 0)
			throw new IllegalArgumentException();
		// dimensione della tabella sovrastimata nel caso di molte chiavi ripetute,
		// load factor col miglior compromesso prestazioni-spazio occupato
		table = new Hashtable<>(keys.size(), 0.5f);
		for (K elem : keys) {
			if (elem == null)
				throw new NullPointerException();
			if (! table.containsKey(elem))
				table.put(elem, 1);
			else
				incCount(elem);
		}
		size = keys.size();
	}

	public void incCount(K key) throws NullPointerException, NoSuchElementException {
		// REQUIRES: key != null && table.contains(key)
		// THROWS: se key = null lancia NullPointerException (disponibile in Java, unchecked)
		//         se !table.contains(key) lancia NoSuchElementException (disponibile in Java, unchecked)
		// MODIFIES: this
		// EFFECTS: incrementa size e il valore associato alla chiave key
		if (key == null)
			throw new NullPointerException();
		if (! table.containsKey(key))
			throw new NoSuchElementException();
		table.put(key, table.get(key) + 1);
		size++;
	}

	public int getSize() {
		// EFFECTS: restituisce size (vedi IR)
		return size;
	}

	public int getSizeWithoutDuplicates() {
		// EFFECTS: restituisce la dimensione della tabella,
		//          ovvero il numero di chiavi senza contare i duplicati
		return table.size();
	}

	public int getCount(K key) throws NullPointerException {
		// REQUIRES: key != null
		// THROWS: se key = null lancia NullPointerException (disponibile in Java, unchecked)
		// EFFECTS: restituisce il valore associato alla chiave key se questa è presente,
		//          0 altrimenti
		if (key == null)
			throw new NullPointerException();
		if (table.containsKey(key))
			return table.get(key);
		return 0;
	}

	// disattivo i messaggi di warning per questo metodo
	// perché sono sicuro che il cast non causerà problemi
	@SuppressWarnings("unchecked")
	public Iterator<K> getIterator() {
		// EFFECTS: restituisce un iteratore per scorrere le chiavi di table,
		//          ordinate in modo non crescente di chiave, e a parità di chiave
		//          in modo lessicografico crescente
		Set<K> set = table.keySet();
		// salvo chiavi e valori in due array perché sono più rapidi da ordinare
		// (avrei potuto lasciare il compito all'iteratore, ma così risparmio
		// una copia inutile in una struttura intermedia che non verrà mai sfruttata)
		K[] keys = (K[]) new Comparable[table.size()];
		Integer[] values = new Integer[table.size()];
		int i = 0;
		for (K elem : set) {
			keys[i] = elem;
			values[i++] = table.get(elem);
		}
		Iterator<K> it = new DataIterator<>(keys, values);
		return it;
	}
}
