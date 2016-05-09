import java.util.*;

public class Cryptanalysis {
	
	static Verifier monograms = new Verifier("english_monograms.txt");
	static Verifier bigrams = new Verifier("english_bigrams.txt");
	static Verifier quadgrams = new Verifier("english_quadgrams.txt");
	static ArrayList<String> perm = new ArrayList<String>();
	static TreeSet<Entry> ts = new TreeSet<Entry>();
	static final int N = 100;
	
	static class Entry implements Comparable<Entry> {
		
		double score;
		String key;
		
		Entry(double score2, String k) {
			score = score2;
			key = k;
		}

		@Override
		public int compareTo(Entry o) {
			// TODO Auto-generated method stub
			return (int) (score - o.score);
		}
		
	}

	private static void permutation(String prefix, String str, int len) {
	    int n = prefix.length();
	    if (n == len) {
	    	perm.add(prefix);
	    }
	    else {
	        for (int i = 0; i < n; i++)
	            permutation(prefix + str.charAt(i), str.substring(0, i) + str.substring(i+1, n), len);
	    }
	}

	public static String solve(String message){
		String best = "";
		double bscore = Double.MIN_VALUE;
		permutation("", " ABCDEFGHIJKLMNOPQRSTUVWXYZ", 2);
		for(int len = 2; len <= Math.min(16, message.length()); len++) {
			for(String k : perm) {
				String key = k + String.join("", Collections.nCopies(len - 2, " "));
				String clear = Vigenere.decipher(message, key);
				double score = 0;
				for (int i=0;i<message.length();i+=len) {
					score += bigrams.score(clear.substring(i, i+2));
				}
				ts.add(new Entry(score, k));
			}
			String cur = "";
			for(int i = 0; i < len - 2; i++){
				double ms = Double.MIN_VALUE;
				char mc = ' ';
				for(int j=0;j<27;j++) {
					String s = cur;
					s += (char)Vigenere.ALPHABET[j];
					for(int k=i+1;k<len;k++) s += " ";
					String clear = Vigenere.decipher(message, s);
					double v = 0;
					for(int k = 0; k < message.length(); k += len) {
						System.out.println("Clear: " +clear.substring(k, Math.min(k+i+1, message.length())));
						//v += monograms.score(clear.substring(k, Math.min(k+i+1, message.length())));
						v += quadgrams.score(clear.substring(k, Math.min(k+i, message.length())));
					}
					System.out.println(s + ":" + v);
					if (v > ms) {
						ms = v;
						mc = Vigenere.ALPHABET[j];
					}
				}
				cur += mc;
			}
			double sc = 0;
			//sc += monograms.score(Vigenere.decipher(message, cur));
			sc += quadgrams.score(Vigenere.decipher(message, cur));
			System.out.print(cur + " ");
			System.out.print(Vigenere.decipher(message, cur) + " ");
			System.out.println(sc);
			if (sc > bscore) {
				bscore = sc;
				best = cur;
			}
		}
		return best;
	}
	public static void main(String[] args){
		Scanner sc = new Scanner(System.in);
		//String message = sc.nextLine();
		String message = Vigenere.encipher("ATTACK", "MOO");
		//System.out.println("cor" +Vigenere.decipher(message, "MOOSE B"));
		System.out.println(message);
		System.out.println(solve(message));
		sc.close();
	}
}
