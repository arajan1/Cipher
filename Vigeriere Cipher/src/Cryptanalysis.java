import java.util.Scanner;

public class Cryptanalysis {
	
	public static void generateKey(int length, String message, String key, int loc){
		length++;
		char pos = Cipher.ALPHABET[loc];
		key += pos;
		
		
	}
	
	public static void checkFitness(String key, String message){
		
	}
	
	public static void main(String[] args){
		Scanner sc = new Scanner(System.in);
		String message = sc.nextLine();
	}
}
