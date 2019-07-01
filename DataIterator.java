import java.util.Iterator;
import java.util.NoSuchElementException;

public class DataIterator<E extends Comparable<E>> implements Iterator<E> {
	private final E[] data;
	private int index;
	// NOTE: per ordinare avrei potuto usare un Comparator che confronta i campi di una classe wrapper

	public DataIterator(E[] keys, Integer[] values) throws NullPointerException, IllegalArgumentException {
		if (keys == null || values == null) // defensive programming, in realtà gli array non saranno mai null
			throw new NullPointerException();
		if (keys.length != values.length) // defensive programming, i vettori saranno sempre della stessa dimensione
			throw new IllegalArgumentException();
		quickSort(keys, values, 0, keys.length - 1);
		data = keys;
		index = -1; // perché sarà la prima chiamata di next() a restituire il primo valore
	}

	public boolean hasNext() {
		return index < data.length - 1;
	}

	public E next() throws NoSuchElementException {
		// incremento l'indice e restitiusco l'elemento
		if (! hasNext()) throw new NoSuchElementException();
		return data[++index];
	}

	public void remove() throws UnsupportedOperationException {
		// operazione proibita
		throw new UnsupportedOperationException();
	}

	private void quickSort(E[] keys, Integer[] values, int start, int end) {
		if (start < end) {
			// pivot calcolato in modo casuale per evitare configurazioni
			// che renderebbero l'algoritmo di costo quadratico
			int pivot = start + (int)(Math.random() * (end - start + 1));
			int p = partition(keys, values, start, pivot, end);
			quickSort(keys, values, start, p - 1);
			quickSort(keys, values, p + 1, end);
		}
	}

	private int partition(E[] keys, Integer[] values, int start, int p, int end) {
		swap(keys, values, p, end);
		int pivot = values[end];
		int i = start - 1;
		for (int j = start; j < end; j++)
			if (values[j] > pivot)
				swap(keys, values, ++i, j); // ordino per occorrenze
			else if ((values[j] == pivot) && (keys[j].compareTo(keys[end]) < 0))
				swap(keys, values, ++i, j); // ordino lessicograficamente
		swap(keys, values, i + 1, end);
		return i + 1;
	}

	private void swap(E[] keys, Integer[] values, int i, int j) {
		// swap di chiavi e valori
		E tmpKey = keys[i];
		Integer tmpValue = values[i];
		keys[i] = keys[j];
		values[i] = values[j];
		keys[j] = tmpKey;
		values[j] = tmpValue;
	}
}
