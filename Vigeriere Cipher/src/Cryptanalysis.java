import java.util.*;

public class Cryptanalysis {
	 
	
	public static void generateKey(int length, String message, String key, int loc){
		length++;
		char pos = Vigenere.ALPHABET[loc];
		key += pos;
		checkFitness(Vigenere.decipher(key, message));
	
		
	}
	
	public static void checkFitness(String message){
		
	}
	
	public static void main(String[] args){
		Scanner sc = new Scanner(System.in);
		String message = sc.nextLine();
		sc.close();
	}
}
