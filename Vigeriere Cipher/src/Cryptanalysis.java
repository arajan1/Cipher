import java.util.*;

public class Cryptanalysis {
	 
	
	public static void generateKey(int length, String message, String key, int loc){
		length++;
		char pos = Cipher.ALPHABET[loc];
		key += pos;
		checkFitness(decipher(key, message));
	
		
	}
	
	public static String decipher(String key, String message) {
		String plaintext = "";
		int ki = 0;
		for (char c : message.toCharArray()) {
			// get next char from key
			char letter = key.charAt(ki);
			// consider spaces as just before 'A'
			c = c == ' ' ? Cipher.A_LESS_1 : c;
			letter = letter == ' ' ? Cipher.A_LESS_1 : letter;
			// find original character and append to deciphered plaintext
			int index = 0;
			index = (c - letter + 27) % 27;
			letter = Cipher.ALPHABET[index];
			plaintext += letter;
			// next index from key
			ki = (ki + 1) % key.length();
		}
		return plaintext;
	}
	
	public static void checkFitness(String message){
		
	}
	
	public static void main(String[] args){
		Scanner sc = new Scanner(System.in);
		String message = sc.nextLine();
	}
}
