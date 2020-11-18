import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * File parser for DNA information regarding STR counts
 *
 * @author Eli Blaney
 * @version 1.0
 */
public class DNAFileParser {

	private String[] repeats;
	private Person[] people;

	public DNAFileParser(String file) throws FileNotFoundException {
		String[][] data = readDataFromCSV(file);
		parseDNA(data);
	}

	/**
	 * Returns the different sequences of STRs given by the data file
	 *
	 * @return A string array containing each STR sequence
	 */
	public String[] getRepeats() {
		return this.repeats;
	}

	/**
	 * Returns the people described by the file with information about their STR counts
	 *
	 * @return A Person array containing the person's name and STR counts
	 */
	public Person[] getPeople() {
		return this.people;
	}

	/**
	 * Processes the CSV sheet to identify the STR sequences and each person's STR information
	 *
	 * @param data A two-dimensional string array of the CSV contents
	 */
	private void parseDNA(String[][] data) {
		// First row is headers
		String[] headerRow = data[0];
		repeats = new String[headerRow.length - 1];
		for(int i = 0; i < headerRow.length; i++) {
			if(i == 0) {
				// First entry is column header for names (unneeded)
				continue;
			}
			repeats[i - 1] = headerRow[i].toUpperCase();
		}

		// Rows after the first are entries for people
		people = new Person[data.length - 1];
		for(int i = 1; i < data.length; i++) {
			String[] row = data[i];
			String personName = row[0];
			int[] repeatData = new int[row.length - 1];
			for(int j = 1; j < row.length; j++) {
				// TODO: handle bad parsing?
				repeatData[j - 1] = Integer.parseInt(row[j]);
			}
			people[i - 1] = new Person(personName, repeatData);
		}
	}

	/**
	 * Reads a data file and processes it as a CSV file
	 *
	 * @param file The path to the data file
	 * @return A two-dimensional array containing the contents of the CSV file
	 */
	private String[][] readDataFromCSV(String file) throws FileNotFoundException {
		// TODO: handle no file exists?
		Scanner scanner = new Scanner(new File(file));
		List<String[]> lines = new ArrayList<>();
		int columns = -1;

		while(scanner.hasNextLine()) {
			String line = scanner.nextLine();
			if(line.isBlank()) {
				continue;
			}
			String[] lineData = line.split(",");

			lines.add(lineData);

			if(columns == -1) {
				columns = lineData.length;
			}
		}
		scanner.close();

		int rows = lines.size();
		return lines.toArray(new String[rows][columns]);
	}

}
