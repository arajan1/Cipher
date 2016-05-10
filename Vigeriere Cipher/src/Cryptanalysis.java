import java.util.*;

public class Cryptanalysis {
	
	static Verifier monograms = new Verifier("english_monograms.txt");
	static Verifier bigrams = new Verifier("english_bigrams.txt");
	static Verifier trigrams = new Verifier("english_trigrams.txt");
	static Verifier quadgrams = new Verifier("english_quadgrams.txt");
	static ArrayList<String> permutations = new ArrayList<String>();
	static TreeSet<Entry> initial, candidates;
	static final int N = 1;
	
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
		//double score = monograms.score(seg) + bigrams.score(seg) + trigrams.score(seg) + quadgrams.score(seg);
		return score;
	}

	@SuppressWarnings("unchecked")
	public static String solve(String message){
		String bestKey = "";
		double bestScore = Double.NEGATIVE_INFINITY;
		for(int len = 2; len <= Math.min(16, message.length()); len++) {
			System.out.println("key length: " + len);
			initial = new TreeSet<Entry>();
			initial.add(new Entry(0, ""));

			candidates = new TreeSet<Entry>();
			for(int i = 0; i < len; i++){
				System.out.println("Length: " + i);
				Iterator<Entry> iterator = initial.descendingIterator();
				System.out.println("initial size: " + initial.size());
				for(int k = 0; k < N && iterator.hasNext(); k++){
					Entry e = iterator.next();
					//System.out.println("examining " + e.key);
					for(int c = 0; c < Vigenere.ALPHABET.length; c++) {
						String key = e.key + (char)Vigenere.ALPHABET[c];
						String padded = key + String.join("", Collections.nCopies(len - key.length(), " "));
						//System.out.println("Key: " + key);
						String clear = Vigenere.decipher(message, padded);
						double score = 0;
						boolean mark = false;
						for(int j = 0; j < clear.length(); j += len) {
							String seg = clear.substring(j, Math.min(j+key.length(), message.length()));
							// If not English text (two consecutive spaces) skip.
							if (seg.contains("  ")) {
								mark = true;
								break;
							}
							//System.out.println("Cleartext: " +clear.substring(j, Math.min(j+key.length(), message.length())));
							score += getScore(seg);
						}
						if (mark) continue;
						if (score < bestScore) continue;
						candidates.add(new Entry(score, key));
						//System.out.println(key + ":" + score);
					}
				}
				initial = (TreeSet<Entry>) candidates.clone();
				candidates.clear();
			}
			
			Iterator<Entry> iterator = initial.descendingIterator();
			for(int k = 0; k < N && iterator.hasNext(); k++){
				Entry cur = iterator.next();
				System.out.println(cur.key + ":" + cur.score);
				if (cur.score > bestScore) {
					bestKey = cur.key;
					bestScore = cur.score;
				}
			}
		}
		return bestKey;
	}
	
	public static void main(String[] args){
		String message = "";
		Scanner sc = new Scanner(System.in);
		while (sc.hasNextLine()) {
			message += sc.nextLine();
		}
		message = message.toUpperCase().replaceAll("[^A-Z ]", "").replaceAll("  ", " ");
		//System.out.println(message);
		message = Vigenere.encipher(message, "MOOSE BREEDER");
		String sol = solve(message);
		System.out.println("Key guess: " + sol);
		System.out.println(Vigenere.decipher(message, sol));
		sc.close();
	}
}
