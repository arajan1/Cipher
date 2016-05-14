import java.util.*;

public class Cryptanalysis {
	
	static Verifier monograms = new Verifier("english_monograms.txt");
	static Verifier bigrams = new Verifier("english_bigrams.txt");
	static Verifier trigrams = new Verifier("english_trigrams.txt");
	static Verifier quadgrams = new Verifier("english_quadgrams.txt");
	static ArrayList<String> permutations = new ArrayList<String>();
	static final int MAX_LENGTH = 2048;
	static PriorityQueue<Entry> initial;
	
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
		/*
		if (seg.length() >= 4)
			score = quadgrams.score(seg);
		else if (seg.length() == 3)
			score = trigrams.score(seg);
		if (seg.length() >= 2)
			score = bigrams.score(seg);
		else
			score = monograms.score(seg);
		*/
		return score;
	}

	public static Entry solve(String message){
		Entry best = new Entry(Double.NEGATIVE_INFINITY, "");
		initial = new PriorityQueue<Entry>(10, Collections.reverseOrder());
		
		for(int len = 2; len <= Math.min(16, message.length()); len++) {
			System.out.println("Trying key length: " + len);
			initial.add(new Entry(0, ""));

			for(int i = 0; i < len; i++){
				if (initial.isEmpty()) break;
				Entry e = initial.poll();
				initial.clear();
				
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
					if (i == len - 1 && clear.contains("  ")) mark = true;
					if (mark) continue;
					//System.out.println(key + ":" + score);
					if (score < best.score) continue;
					initial.add(new Entry(score, key));
				}
			}
			
			if (!initial.isEmpty()) {
				Entry curBest = initial.poll();
				if (curBest.score > best.score) {
					System.out.println(curBest.key + ":" + curBest.score);
					best = curBest;
				}
				initial.clear();
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
		//message = Vigenere.encipher(message, "NICKY");
		//System.out.println(message);
		Entry sol = solve(message);
		System.out.println("Key guess: " + sol.key);
		System.out.println(Vigenere.decipher(message, sol.key));
		sc.close();
	}
}
