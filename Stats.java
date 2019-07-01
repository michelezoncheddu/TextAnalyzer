
public class Stats {
	private String[][] words;
	private int nWords, rows, length, spaces;
	private long time;
	
	public Stats(String[][] wordsPar, int nWordsPar, int rowsPar, int lengthPar, int spacesPar, long timePar) {
		words = wordsPar;
		nWords = nWordsPar;
		rows = rowsPar;
		length = lengthPar;
		spaces = spacesPar;
		time = timePar;
	}
	
	public String[][] getWords() {
		return words;
	}
	
	public int getNWords() {
		return nWords;
	}
	
	public int getRows() {
		return rows;
	}
	
	public int getLength() {
		return length;
	}
	
	public int getSpaces() {
		return spaces;
	}
	
	public long getTime() {
		return time;
	}
}
