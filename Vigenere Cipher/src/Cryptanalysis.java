import java.util.*;

public class Cryptanalysis {
	
	static Verifier monograms = new Verifier("english_monograms.txt");
	static Verifier bigrams = new Verifier("english_bigrams.txt");
	static Verifier trigrams = new Verifier("english_trigrams.txt");
	static Verifier quadgrams = new Verifier("english_quadgrams.txt");
	static ArrayList<String> permutations = new ArrayList<String>();
	static final int MAX_LENGTH = 2048;
	static TreeSet<Entry> initial, candidates;
	
	static class Entry implements Comparable<Entry> {
		
		double score;
		String key;
		
		Entry(double s, String k) {
			score = s;
			key = k;
		}

		@Override
		public int compareTo(Entry o) {
			if (score < o.score) return -1;
			else if (score > o.score) return 1;
			return 0;
		}
		
	}

	private static double getScore(String seg) {
		double score = monograms.score(seg);
		score = monograms.score(seg) + bigrams.score(seg) + trigrams.score(seg) + quadgrams.score(seg);
		return score;
	}

	@SuppressWarnings("unchecked")
	public static Entry solve(String message){
		Entry best = new Entry(Double.NEGATIVE_INFINITY, "");
		
		for(int len = 2; len <= Math.min(16, message.length()); len++) {
			System.out.println("Key length: " + len);
			initial = new TreeSet<Entry>();
			initial.add(new Entry(0, ""));

			candidates = new TreeSet<Entry>();
			for(int i = 0; i < len; i++){
				if (initial.isEmpty()) break;
				Entry e = initial.last();
				for(int c = 0; c < Vigenere.ALPHABET.length; c++) {
					String key = e.key + Vigenere.ALPHABET[c];
					String padded = key + String.join("", Collections.nCopies(len - key.length(), " "));
					String clear = Vigenere.decipher(message, padded);
					double score = 0;
					boolean mark = false;
					for(int j = 0; j < Math.min(clear.length(), MAX_LENGTH); j += len) {
						String seg = clear.substring(j, Math.min(j+key.length(), message.length()));
						// If not English text (two consecutive spaces) skip.
						if (seg.contains("  ")) {
							mark = true;
							break;
						}
						score += getScore(seg);
					}
					if (mark) continue;
					if (score < best.score) continue;
					candidates.add(new Entry(score, key));
				}
				initial = (TreeSet<Entry>) candidates.clone();
				candidates.clear();
			}
			
			if (!initial.isEmpty()) {
				Entry curBest = initial.last();
				System.out.println(best.key + ":" + best.score);
				if (curBest.score > best.score) {
					best = curBest;
				}
			}
		}
		return best;
	}
	
	public static void main(String[] args){
		String message = "";
		Scanner sc = new Scanner(System.in);
		while (sc.hasNextLine()) {
			message += sc.nextLine();
		}
		message = message.toUpperCase().replaceAll("[^A-Z ]", "").replaceAll("  ", " ");
		message = Vigenere.encipher(message, "MOOSE BREEDER");
		Entry sol = solve(message);
		System.out.println("Key guess: " + sol.key);
		System.out.println(Vigenere.decipher(message, sol.key));
		sc.close();
	}
}
