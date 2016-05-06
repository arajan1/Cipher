public class Vigenere
{
	public static final char[] ALPHABET = {' ', 'A','B','C','D','E','F','G',
		'H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X',
		'Y','Z'};
	public static final char A_LESS_1 = 'A' - 1;
	
	public static String encipher(String msg, String key)
	{
		String ciphertext = "";
		int ki = 0;
		//locate the index in alpha for all chars in the message
		//assemble the ciphertext
		for (char c : msg.toCharArray())
		{
			//get next char from key
			char letter = key.charAt(ki);
			
			//consider spaces as char just before 'A'
			letter = letter == ' ' ? A_LESS_1 : letter;
			
			//determine index and letter, append to cipher
			int index = (c + letter - 2*A_LESS_1) % 27;
			letter = ALPHABET[index];
			ciphertext += letter;
			
			//next value from the key
			ki = (ki + 1) % key.length();
		}
		return ciphertext;
	}
	
	public static String decipher(String ciphertext, String key)
	{
		String plaintext = "";
		
		int ki = 0;
		for (char c : ciphertext.toCharArray())
		{
			//get next char from key
			char letter = key.charAt(ki);
			
			//consider spaces as just before 'A'
			c = c == ' ' ? A_LESS_1 : c;
			letter = letter == ' ' ? A_LESS_1 : letter;
			
			//find original character and append to deciphered plaintext
			int index = 0;
			index = (c - letter + 27) % 27;
			letter = ALPHABET[index];
			plaintext += letter;
			
			//next index from key
			ki = (ki + 1) % key.length();
		}
		
		return plaintext;
	}
}