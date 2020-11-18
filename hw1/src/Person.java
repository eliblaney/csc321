/**
 * A container for people in a DNA dataset
 *
 * @author Eli Blaney
 * @version 1.0
 */
public class Person {

	private final String name;
	private final int[] dnaRepeats;

	public Person(String name, int[] dnaRepeats) {
		this.name = name;
		this.dnaRepeats = dnaRepeats;
	}

	/**
	 * Returns the name of the person
	 *
	 * @return A String corresponding to the name of the person
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the number of STRs for each particular sequence in a given order
	 *
	 * @return An integer array holding the count of each STR described
	 */
	public int[] getDnaRepeats() {
		return dnaRepeats;
	}
}
