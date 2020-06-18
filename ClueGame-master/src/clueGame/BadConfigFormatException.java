package clueGame;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

/**
 * BadConfigFormatException - Custom exception for when the room/board configuration files are not correctly formatted
 * @author sethasadi, dquintana
 *
 */
public class BadConfigFormatException extends Exception {
	/**
	 * prints out an error message and writes to an error log if room or board files
	 * cannot be loaded
	 */
	public BadConfigFormatException() {
		super("The provided room or board files have some kind of discrepancy and cannot be loaded");
		try {
			PrintWriter output = new PrintWriter("error_log.txt");
			output.println("The provided room or board files have some kind of discrepancy and cannot be loaded");
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * prints out a error message based on the parameter and also writes it to an error log
	 * @param message
	 */
	public BadConfigFormatException(String message) {
		super(message);
		try {
			PrintWriter output = new PrintWriter(new FileOutputStream(new File("error_log.txt"),true));
			output.println(message);
			output.close();
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		}
	}

}
