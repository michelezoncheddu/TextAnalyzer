import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;

public class TreeDataCounter<K extends Comparable<K>> implements DataCounter<K> {
	// AF: f: K -> V tale che f(x_i) = tree.get(x_i), con x = tree.key(i) e 0 <= i < tree.size(),
	//     non definita altrimenti
	// IR: tree != null && size > 0 &&
	//     forall x: tree.get(x) != null ==> tree.get(x) > 0
	//     && size = Σ{ x: tree.get(x) != null . tree.get(x) }
	private final TreeMap<K, Integer> tree;
	private int size;
	
	public TreeDataCounter(Vector<K> keys) throws NullPointerException, IllegalArgumentException {
		// REQUIRES: keys != null && keys.size() > 0 &&
		//           forall i: 0 <= i < keys.size() ==> keys.get(i) != null
		// THROWS: se keys = null, o un qualunque elemento di keys è null, 
		//         lancia NullPointerException (disponibile in Java, unchecked);
		//         se keys.length = 0 lancia IllegalArgumentException (disponibile in Java, unchecked)
		// MODIFIES: this
		// EFFECTS: inizializza tree utilizzando gli elementi di keys come chiavi,
		//          e il numero di occorrenze di ogni elemento come valori associati;
		//          inizializza size come keys.length
		if (keys == null)
			throw new NullPointerException();
		if (keys.size() == 0)
			throw new IllegalArgumentException();
		tree = new TreeMap<>();
		for (K elem : keys) {
			if (elem == null)
				throw new NullPointerException();
			if (! tree.containsKey(elem))
				tree.put(elem, 1);
			else
				incCount(elem);
		}
		size = keys.size();
	}

	public void incCount(K key) throws NullPointerException, NoSuchElementException {
		// REQUIRES: key != null && tree.contains(key)
		// THROWS: se key = null lancia NullPointerException (disponibile in Java, unchecked)
		//         se !tree.contains(key) lancia NoSuchElementException (disponibile in Java, unchecked)
		// MODIFIES: this
		// EFFECTS: incrementa size e il valore associato alla chiave key
		if (key == null)
			throw new NullPointerException();
		if (! tree.containsKey(key))
			throw new NoSuchElementException();
		tree.put(key, tree.get(key) + 1);
		size++;
	}

	public int getSize() {
		// EFFECTS: restituisce size (vedi IR)
		return size;
	}

	public int getSizeWithoutDuplicates() {
		// EFFECTS: restituisce la dimensione dell'albero,
		//          ovvero il numero di chiavi presenti
		return tree.size();
	}

	public int getCount(K key) throws NullPointerException {
		// REQUIRES: key != null
		// THROWS: se key = null lancia NullPointerException (disponibile in Java, unchecked)
		// EFFECTS: restituisce il valore associato alla chiave key se questa è presente,
		//          0 altrimenti
		if (key == null)
			throw new NullPointerException();
		if (tree.containsKey(key))
			return tree.get(key);
		return 0;
	}

	// disattivo i messaggi di warning per questo metodo
	// perché sono sicuro che il cast non causerà problemi
	@SuppressWarnings("unchecked")
	public Iterator<K> getIterator() {
		// EFFECTS: restituisce un iteratore per scorrere le chiavi di tree,
		//          ordinate in modo non crescente di chiave, e a parità di chiave
		//          in modo lessicografico crescente
		Set<K> set = tree.keySet();
		// salvo chiavi e valori in due array perché sono più rapidi da ordinare
		// (avrei potuto lasciare il compito all'iteratore, ma così risparmio
		// una copia inutile in una struttura intermedia che non verrà mai sfruttata)
		K[] keys = (K[]) new Comparable[tree.size()];
		Integer[] values = new Integer[tree.size()];
		int i = 0;
		for (K elem : set) {
			keys[i] = elem;
			values[i++] = tree.get(elem);
		}
		Iterator<K> it = new DataIterator<>(keys, values);
		return it;
	}
}
