import java.io.FileWriter;
import java.io.IOException;

/**
 * This class writes the Player's name and accuracy to the file. File is created
 * in the project's source folder. File name is leaderboard.txt.
 * 
 * @author Ram Reddy and Eric Feng
 * @version 5.24.20
 */
public class WriteToFile {

	/**
	 * This method writes the players firstName, lastName and accuracyRate in a file
	 * for later viewing.
	 *
	 * @param firstName    Player's First Name
	 * @param lastName     Player's Last Name
	 * @param accuracyRate Player's accuracy rate.
	 */
	public static void writeFile(String firstName, String lastName, double accuracyRate) {

		try {
			FileWriter myFileWriter = new FileWriter("Leaderboard.txt", true);
			myFileWriter.write(firstName + " " + lastName + " : " + accuracyRate + "\n");
			myFileWriter.close();
			System.out.println("Wrote to the file.");

		} catch (IOException e) {
			System.out.println("File did not open. Please check");
			e.printStackTrace();
		}

	}

}
