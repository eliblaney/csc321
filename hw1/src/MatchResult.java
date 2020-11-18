/**
 * Result container describing the success of a DNA search query and
 * the relevant data associated with that result.
 *
 * @author Eli Blaney
 * @version 1.0
 */
public class MatchResult {

	private final ResultType resultType;
	private final Person[] people;
	private final int numSTRs;

	public MatchResult(ResultType resultType, Person[] people, int numSTRs) {
		this.resultType = resultType;
		this.people = people;
		this.numSTRs = numSTRs;
	}

	/**
	 * Returns the type of result given by the query
	 *
	 * @return A ResultType that may describe an exact match, close match, or no match
	 */
	public ResultType getResultType() {
		return resultType;
	}

	/**
	 * Returns the people returned by the search query, if applicable
	 *
	 * @return An array of Person objects corresponding to people that match the query
	 */
	public Person[] getPeople() {
		return people;
	}

	/**
	 * Returns the number of STRs that match between the search pattern and data
	 *
	 * @return A whole number of STR sequences
	 */
	public int getNumSTRs() {
		return numSTRs;
	}

	/**
	 * Representation of the status returned by a query
	 *
	 * @author Eli Blaney
	 * @version 1.0
	 */
	public enum ResultType {
		EXACT_MATCH, CLOSE_MATCH, NO_MATCH
	}
}
