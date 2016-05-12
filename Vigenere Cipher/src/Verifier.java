import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class Verifier {
	
	private HashMap<String, Double> ngrams;
	private int L;
	private double floor;

	public Verifier (String ngramfile) {
        // load a file containing n-grams and counts, calculate log probabilities
        ngrams = new HashMap<String, Double>();
        try (BufferedReader br = new BufferedReader(new FileReader(ngramfile))) {
            String line;
            while ((line = br.readLine()) != null) {
            	String[] all = line.split(" ");
            	String key = all[0];
            	int count = Integer.parseInt(all[1]);
                ngrams.put(key, (double) count);
                //System.out.println((double) count);
            }
        } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        long N = 0;
        for (double v: ngrams.values()) {
        	N += v;
            //System.out.println(v);
        }
        for (String k: ngrams.keySet()) {
        	L = k.length();
        	ngrams.put(k, Math.log10(ngrams.get(k) / N));
        }
        // calculate log probabilities
        floor = Math.log10(0.01/N);
	}

    double score(String text) {
        // compute the score of text
        double score = 0;
        //System.out.println("scoring raw " + text);
        text = text.replace(" ", "");
        for (int i = 0; i < text.length() - L + 1; i++) {
        	if (ngrams.containsKey(text.substring(i, i+L))) {
        		score += ngrams.get(text.substring(i, i+L));
        		//System.out.println(text.substring(i, i+L) + ":" + ngrams.get(text.substring(i, i+L)));
        	}
        	else {
        		score += floor;
        	}
        }
        return score;
    }
}
