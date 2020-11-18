import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * N-Gram structure that holds the words that can follow a particular
 * string and their frequencies using a Map structure.
 *
 * @author Eli Blaney
 * @version 2020-11-16
 */
public class CountPredictor implements NGramPredictor {

	private static final Random r = new Random();
	private final Map<String, Integer> predicted;
	private Set<Map.Entry<String, Integer>> cumulativeEntries = null;

	/**
	 * Create a new instance of NGramPredictions.
	 */
	public CountPredictor() {
		this.predicted = new HashMap<>();
	}

	/**
	 * Safely convert the frequencies into an ordered set of cumulative weights.
	 *
	 * In essence, changes:
	 * [("text", 1), ("word", 2), ("example", 1)]
	 * into:
	 * [("text", 1), ("word", 3), ("example", 4)]
	 *
	 * This method is called in a lazy fashion (i.e. only when the underlying map changes).
	 */
	private void generateWeights() {
		if(this.cumulativeEntries != null) {
			// Avoid unnecessary work
			return;
		}
		// Copy the map (to prevent making changes to the original)
		this.cumulativeEntries = new HashMap<>(predicted).entrySet();
		// Create an array of the cumulative weights (i.e. [1, 2, 1] => [1, 3, 4])
		Integer[] cumulativeWeights = cumulativeEntries.stream().map(Map.Entry::getValue).toArray(Integer[]::new);
		Arrays.parallelPrefix(cumulativeWeights, Integer::sum);
		// Merge the Map with the newly generated cumulative weights
		AtomicInteger i = new AtomicInteger();
		cumulativeEntries.forEach(e -> e.setValue(cumulativeWeights[i.getAndIncrement()]));
	}

	/**
	 * Generate the next word by randomly selecting from the map of words. Uses fitness proportionate selection
	 * to take the frequencies of each word into account by weighting them.
	 *
	 * @return The next word in the sentence.
	 */
	@Override
	public String predict() {
		if(predicted.size() == 0) {
			return null;
		}

		generateWeights();
		// Add the frequencies to get a total
		int totalCounts = this.predicted.values().parallelStream().reduce(Integer::sum).orElse(0);
		// Select a random weight based on the frequencies
		int randomWeight = r.nextInt(totalCounts) + 1;
		// Select the cumulatively-weighted entry corresponding to the random weight
		return cumulativeEntries.parallelStream()
				.filter(e -> e.getValue() <= randomWeight)  // Removes weights above randomWeight
				.max(Map.Entry.comparingByValue())          // Gets the highest weight less than randomWeight
				.map(Map.Entry::getKey)
				.orElse(null);
	}

	/**
	 * Add a specified word to the structure. May be a duplicate of an existing word to increase its
	 * probability of being selected.
	 *
	 * @param predictedWord The word to add to the structure
	 */
	@Override
	public void add(String predictedWord) {
		if(predictedWord.isBlank()) {
			return;
		}
		int count = predicted.getOrDefault(predictedWord, 0) + 1;
		predicted.put(predictedWord, count);
		// Mark map as dirty to regenerate weights
		this.cumulativeEntries = null;
	}

	/**
	 * Creates a string representation of this object.
	 *
	 * @return A string containing the Map of words that can be predicted with their frequencies.
	 */
	@Override
	public String toString() {
		return "CountPredictor(" + predicted.size() + ")" + predicted.toString();
	}
}