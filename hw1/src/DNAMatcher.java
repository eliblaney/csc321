import java.util.*;

/**
 * Query processor for a database of STRs and people
 *
 * @author Eli Blaney
 * @version 1.0
 */
public class DNAMatcher {
	
	private final String[] repeats;
	private final Person[] people;
	
	public DNAMatcher(String[] repeats, Person[] people) {
		this.repeats = repeats;
		this.people = people;
	}

	/**
	 * Performs a search query on the database for a given sequence of DNA
	 *
	 * @param pattern The DNA sequence which will be searched for STRs and matched in the database
	 * @return A MatchResult object describing the success or failure of the query and its associated data, if any
	 */
	public MatchResult match(String pattern) {
		// Get count of each kind of STR
		int numRepeats = this.repeats.length;
		int[] repeatCounts = new int[numRepeats];
		for(int i = 0; i < numRepeats; i++) {
			repeatCounts[i] = numSubstrings(pattern, this.repeats[i]);
		}

		// Compare counts with person data
		Map<Person, Integer> sums = new HashMap<>();
		for(Person p : this.people) {
			int numSTRsInCommon = 0;
			for(int i = 0; i < numRepeats; i++) {
				if(repeatCounts[i] == p.getDnaRepeats()[i]) {
					numSTRsInCommon++;
				}
			}
			sums.put(p, numSTRsInCommon);
		}

		// Get closest (hopefully exact) match by comparing to benchmark
		List<Person> matches = new ArrayList<>();
		int benchmark = numRepeats;
		while(matches.isEmpty() && benchmark > 0) {
			for(Map.Entry<Person, Integer> e : sums.entrySet()) {
				if(e.getValue() == benchmark) {
					matches.add(e.getKey());
				}
			}
			benchmark--;
		}

		// Build result
		if(!matches.isEmpty()) {
			Person[] matchesArray = matches.toArray(new Person[0]);
			MatchResult.ResultType type = MatchResult.ResultType.CLOSE_MATCH;
			if(++benchmark == numRepeats) {
				type = MatchResult.ResultType.EXACT_MATCH;
			}
			return new MatchResult(type, matchesArray, benchmark);
		} else {
			return new MatchResult(MatchResult.ResultType.NO_MATCH, null, 0);
		}
	}

	/**
	 * Determines the number of a certain substring present in a bigger string
	 *
	 * @param string The string that contains the substring pattern to be searched
	 * @param substring The substring to be searched within the string
	 * @return The number of substrings within the string
	 */
	private int numSubstrings(String string, String substring) {
		/*
		 * split will make array from substrings, so we can
		 * count the number of substrings by taking the length - 1
		 */
		// append "$" to string because split will ignore trailing split pattern
		return (string + "$").split(substring).length - 1;
	}

}
