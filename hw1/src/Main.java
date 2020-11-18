import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * Driver class for DNA queries within a CSV database
 *
 * @author Jack Bilsland
 * @version 1.0
 */
public class Main {

	/**
	 * Runs an implementation of the project which asks for a database file and a DNA sequence pattern from
	 * standard input, then runs a search query of that DNA pattern through the database
	 *
	 * @param args Command line arguments (ignored)
	 */
	public static void main(String[] args) throws FileNotFoundException {
		Scanner scanner = new Scanner(System.in);
		System.out.print("What file has the DNA database? ");
		String fileName = scanner.nextLine();

		DNAFileParser fileParser = new DNAFileParser(fileName);
		Person[] people = fileParser.getPeople();
		String[] repeats = fileParser.getRepeats();

		requestSearch(scanner, repeats, people);
		scanner.close();
	}

	/**
	 * Retrieves user input and searches for DNA matches
	 *
	 * @param scanner retrieves user input of the DNA String
	 * @param repeats The list of STR DNA sequences that will be used to compare
	 * @param people The people in the database that will be compared against the DNA sequence
	 */
	private static void requestSearch(Scanner scanner, String[] repeats, Person[] people){
		String dna = "";

		while (!dna.equalsIgnoreCase("Q")){
			System.out.print("What is the DNA String or type Q to quit? ");
			dna = scanner.nextLine();

			if(dna.equalsIgnoreCase("Q")){
				System.out.println("Program terminated");
				return;
			}

			MatchResult result = getDNAMatch(dna, repeats, people);
			switch(result.getResultType()) {
				case EXACT_MATCH -> {
					System.out.println("This sequence matches:");
					System.out.println(listNames(result.getPeople()));
				}
				case CLOSE_MATCH -> {
					System.out.println("Closest matches are:");
					System.out.println(listNames(result.getPeople()));
					System.out.println("With " + result.getNumSTRs() + " STRs in common");
				}
				case NO_MATCH -> System.out.println("This sequence matches NO MATCH.");
			}
		}
	}

	/**
	 * Performs a DNA match query on the given DNA sequence, given a database of STRs and people in the database
	 *
	 * @param dnaSequence The DNA sequence to be compared with the database
	 * @param repeats The list of STR DNA sequences that will be used to compare
	 * @param people The people in the database that will be compared against the DNA sequence
	 * @return A MatchResult corresponding to the success or failure of the query, with associated data if available
	 */
	private static MatchResult getDNAMatch(String dnaSequence, String[] repeats, Person[] people){
		DNAMatcher dnaMatcher = new DNAMatcher(repeats, people);
		return dnaMatcher.match(dnaSequence);
	}

	/**
	 * Generates a list of people's names, separated by a specified delimiter
	 *
	 * @param people The array of Person objects which will be converted into a String of their names
	 * @return Each person's name, separated by the delimiter
	 */
	private static String listNames(Person... people) {
		if(people == null) {
			return "";
		}
		return Arrays.stream(people).map(Person::getName).collect(Collectors.joining("\n"));
	}
}
