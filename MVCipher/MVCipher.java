/* imports */
import java.util.Scanner;
import java.io.File;
import java.io.PrintWriter;
import java.io.FileNotFoundException;

/**
 *	MVCipher - Deciphers or Encrypts a text file. You, the user, would 
 *  enter a keyword, and your text file would be encrypted based on your
 *  keyword. A would be a shift of 1 letter, B would be a shift of 2, C
 *  would be a shift of 3, and so forth. The keyword shift is repeated 
 *  over and over again for every word in the encoding. Everything 
 *  except spaces, symbols, and numbers are encoded.
 *	Requires Prompt and FileUtils classes.
 *	
 *	@author	Jason He
 *	@since	9/17/25
 */
public class MVCipher 
{
	// scanner for user input
	private Scanner keyboard;
		
	/** Constructor */
	public MVCipher() 
	{
		keyboard = new Scanner(System.in);
	}
	
	public static void main(String[] args) 
	{
		MVCipher mvc = new MVCipher();
		mvc.run();
	}
	
	/**
	 *	run() welcomes the user, keeps on prompting the user for a encoding 
	 *  key until the user enters a proper value (checks with the 
	 *  isValidKeyword() method). It would then prompt the user to encrypt
	 *  or decrypt, and keep on asking until the user enters a proper 
	 *  value. The user would then be prompted the name of the file to 
	 *  encrypt/decrypt. Then, you would ask the user to name an output
	 *  file that would save their encrypted/decrypted content into. After
	 *  that, we would read the file, and send the information to an 
	 *  encrypter/decrypter method, which would send back the completed 
	 *  version. 
	 * 
	 * 	Keep in mind that this is all the operations that we need. Below
	 *  this method are helper methods only.
	 */
	public void run() 
	{
		System.out.println("Welcome to the MV Cipher machine!");
		String keyword = "";
		
		// prompt for the keyword 
		do 
		{
			System.out.print("Please input a word to use as key " +
				"(letters only) -> ");
			keyword = keyboard.nextLine();
			
			if (!isValidKeyword(keyword)) // check if valid
			{
				System.out.println("Error: Keyword must contain only " +
					"alphabetic characters.");
			}
			
		} while ( !isValidKeyword(keyword) ); // keep doing until valid
		
		// change to uppercase
		keyword = toUpperCase(keyword);
		
		// prompt for encrypt or decrypt
		int choice = 0;
		do 
		{
			System.out.print("Encrypt or decrypt? (1 - 2) -> ");
			choice = keyboard.nextInt();
			keyboard.nextLine(); // get rid of new line
			
			if (choice != 1 && choice != 2) 
			{
				System.out.println("Error: Please enter 1 for encrypt " +
					"or 2 for decrypt.");
			}
			
		} while (choice != 1 && choice != 2);
		
		// 1 = encrypt, 2 = decrypt
		boolean encrypt = false;
		
		if(choice == 1)
			encrypt = true;
		else
			encrypt = false;
		
		// prompt for an input file name
		if (encrypt == true) 
		{
			System.out.print("Name of file to encrypt -> ");
		} 
		else 
		{
			System.out.print("Name of file to decrypt -> ");
		}
		String inputFile = keyboard.nextLine();
		
		// prompt for an output file name
		System.out.print("Name of output file -> ");
		String outputFile = keyboard.nextLine();
		
		// read input file, encrypt/decrypt, print to output file
		String content = readFile(inputFile);
		if (content != null) // aka if user has a blank file
		{
			String processedContent = processText(content, keyword, encrypt);
			if (writeFile(outputFile, processedContent)) 
			{
				if (encrypt == true) 
				{
					System.out.println("The encrypted file " + outputFile + 
						" has been created using the keyword -> " + keyword);
				} 
				else 
				{
					System.out.println("The decrypted file " + outputFile + 
						" has been created using the keyword -> " + keyword);
				}
			}
		}
	}
	
	///
	/// helper methods.
	///
	
	/**
	 * makes sure that the keyword contains only alphabetic characters
	 * - you dont want a symbol or number keyword
	 * @param keyword (the keyword to make sure)
	 * @return true if valid, false otherwise and ask again
	 */
	public boolean isValidKeyword(String keyword) 
	{
		if (keyword == null || keyword.length() == 0) // check length
		{
			return false;
		}
		
		// check if all letters of the keyword are valid
		for (int i = 0; i < keyword.length(); i++) 
		{
			char c = keyword.charAt(i);
			if (!((c >= 'A' && c <= 'Z') || 
				(c >= 'a' && c <= 'z'))) 
			{
				return false;
			}
		}
		return true;
	}
	
	/**
	 * converts keyword to uppercase
	 * @param keyword (the keyword to convert)
	 * @return uppercase version of the keyword
	 */
	public String toUpperCase(String keyword) 
	{
		String result = "";
		
		// run through every character in the keyword
		for (int i = 0; i < keyword.length(); i++) 
		{
			char c = keyword.charAt(i);
			if (c >= 'a' && c <= 'z') // if lowercase
			{
				result += (char)(c - 32); // add 32 = uppercase version:
				// not a magic number, 26 letters plus the six symbols
				// in between uppercase and lowercase (ASCII 91 to 96)
			} 
			else 
			{
				result += c; // otherwise already uppercase, just add
			}
		}
		return result;
	}
	
	/**
	 * reads a file line by line and returns the content as a string
	 * @param filename (the name of the file to read - user provided)
	 * @return the content of the file as a string
	 */
	public String readFile(String filename) 
	{
		String content = "";
		try 
		{
			Scanner fileScanner = new Scanner(new File(filename));
			// read in every line
			while (fileScanner.hasNextLine()) 
			{
				content += fileScanner.nextLine();
				if (fileScanner.hasNextLine()) 
				{
					content += "\n"; // formatting
				}
			}
			fileScanner.close();
			
			// buggy if file doesnt end with a new line.
			// add newline at end if file doesnt end with one
			if (content.length() > 0 && !content.endsWith("\n")) 
			{
				content += "\n"; 
				
			}
		} 
		catch (FileNotFoundException e) 
		{
			System.out.println("Error: File not found - " + filename);
			return null; // if no file at all
		}
		return content;
	}
	
	/**
	 * encrypts/ decrypts a string using the cipher
	 * @param text (text to encrypt/decrypt)
	 * @param keyword (keyword for encryption)
	 * @param encrypt (true = encrypt, false = decrypt)
	 * @return the encrypted/decrypted string
	 */
	public String processText(String text, String keyword, boolean encrypt) 
	{
		String result = "";
		int keywordIndex = 0;
		
		for (int i = 0; i < text.length(); i++) 
		{
			char c = text.charAt(i); // character to encrypt/decrypt
			
			// deal with uppercase LETTERS
			if (c >= 'A' && c <= 'Z') 
			{
				// always always always repeat through keyword!!
				char keyChar = keyword.charAt(keywordIndex % 
					keyword.length());
				
				// call the other helper method, gets back the encrypted
				// or decrypted single character, and add it to result	
				result += processUppercase(c, keyChar, encrypt);
				keywordIndex++;
			}
			// deal with lowercase LETTERS 
			else if (c >= 'a' && c <= 'z') 
			{
				char keyChar = keyword.charAt(keywordIndex % 
					keyword.length());
				
				// call the other method for lowercase letters, gets back
				// the en/decrypted character, add to result	
				result += processLowercase(c, keyChar, encrypt);
				keywordIndex++;
			}
			// symbols, numbers, spaces, dont need to do anything. just add
			else 
			{
				result += c;
			}
		}
		
		return result; // return the total, encrypted text
	}
	
	/**
	 * processes a lowercase character for encryption/decryption
	 * @param c (the character to process)
	 * @param keyChar (the key character)
	 * @param encrypt (true for encryption, false for decryption)
	 * @return the processed character
	 */
	public char processLowercase(char c, char keyChar, boolean encrypt) 
	{
		// this is the shift value from the key. 
		int shift = keyChar - 'A' + 1;
		
		// if decryption 
		if (!encrypt) 
		{
			shift = -shift; 
		}
		
		// otherwise, for encryption, this is simply adding shift value
		int newChar = c + shift; 
		
		// handle alphabet wrap (if shift value makes character go over
		// or under)
		while (newChar < 'a') // under a = over wrap
		{
			newChar += 26; // not a magic number: wrap 26 as in 26 letters
			// in the alphabet
		}
		while (newChar > 'z') // over z = down wrap
		{
			newChar -= 26; // not a magic number: wrap 26 as in 26 letters
			// in the alphabet
		}
		
		return (char)(newChar); 
	}
	
	/**
	 * processes an uppercase character for encryption/decryption
	 * basically a copy of the lowercase version
	 * @param c (the character to process)
	 * @param keyChar (the key character)
	 * @param encrypt (true = encryption, false = decryption)
	 * @return the processed character
	 */
	public char processUppercase(char c, char keyChar, boolean encrypt) 
	{
		int shift = keyChar - 'A' + 1;
		if (!encrypt) // again, decryption
		{
			shift = -shift;
		}
		
		int newChar = c + shift;
		
		// handle alphabet wrap (if shift value makes character go over
		// or under)
		while (newChar < 'A') 
		{
			newChar += 26; // not a magic number: wrap 26 as in 26 letters
			// in the alphabet
		}
		while (newChar > 'Z') 
		{
			newChar -= 26; // not a magic number: wrap 26 as in 26 letters
			// in the alphabet
		}
		
		return (char)(newChar);
	}
	
	/**
	 * writes the new content to a file
	 * @param filename (name of file to write to)
	 * @param content (content to write)
	 * @return true (if successful, false otherwise)
	 */
	public boolean writeFile(String filename, String content) 
	{
		try 
		{
			PrintWriter writer = new PrintWriter(filename);
			writer.print(content);
			writer.close();
			return true;
		} 
		catch (FileNotFoundException e) 
		{
			System.out.println("Error: Could not write to file - " + 
				filename);
			return false;
		}
	}
	
}
