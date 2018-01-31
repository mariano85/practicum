package practicum.lucene.es;

import java.util.HashSet;
import java.util.Set;

public class PracticumBean implements Comparable<PracticumBean> {
	
	private String word;
	
	private Integer totalOcurrences = 0;
	
	private Set<Integer> sentenceIndexes = new HashSet<Integer>();

	public PracticumBean(String word) {
		this.setWord(word);
	}
	
	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public Integer getTotalOcurrences() {
		return totalOcurrences;
	}

	public void setTotalOcurrences(Integer totalOcurrences) {
		this.totalOcurrences = totalOcurrences;
	}

	public Set<Integer> getSentenceIndexes() {
		return sentenceIndexes;
	}

	public void setSentenceIndexes(Set<Integer> sentenceIndexes) {
		this.sentenceIndexes = sentenceIndexes;
	}

	public int compareTo(PracticumBean arg0) {
		return this.getWord().compareTo(arg0.getWord());
	}
	
	public String toString() {
		return String.format("word: %s; totalOcurrences: %d; sentenceIndexes: %s", this.getWord(), this.getTotalOcurrences(), this.getSentenceIndexes());
	}
	
	public void addOcurrence(Integer index) {
		this.getSentenceIndexes().add(index);
		this.setTotalOcurrences(this.getTotalOcurrences() + 1);
	}

}
