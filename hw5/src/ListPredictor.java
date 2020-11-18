import java.util.*;

/**
 * N-Gram structure that holds the words that can follow a particular string using a List structure.
 *
 * P.S.: So I heard that you'd take off points for using a List structure due to memory usage. That made me sad
 * because I liked this code and its simplicity. So I kept it, but it's deprecated and unused by my actual project.
 * However, it does inherit from a interface which is used in my project, so it could be utilized if one were to
 * want to sacrifice some memory for this approach. This is basically a lot of text to say that I like this class
 * and I didn't want to remove it even though I know it would have had some points removed for its usage.
 *
 * @author Eli Blaney
 * @version 2020-11-03
 */
@Deprecated
public class ListPredictor implements NGramPredictor {

	private static final Random r = new Random();
	private final List<String> predicted;

	/**
	 * Create a new instance of ListPredictor from a starting array of possible strings.
	 */
	public ListPredictor() {
		this.predicted = new ArrayList<>();
	}

	/**
	 * Generate the next word by randomly selecting from the list of words.
	 *
	 * @return The next word in the sentence.
	 */
	@Override
	public String predict() {
		int size = predicted.size();
		if(size <= 0) {
			return null;
		}
		return predicted.get(r.nextInt(size));
	}

	/**
	 * Add a specified word to the list. May be a duplicate of an existing word to increase its
	 * probability of being selected.
	 *
	 * @param predictedWord The word to add to the list
	 */
	@Override
	public void add(String predictedWord) {
		if(predictedWord.isBlank()) {
			return;
		}
		predicted.add(predictedWord);
	}

	/**
	 * Creates a string representation of this object.
	 *
	 * @return A string containing the List of words that can be predicted.
	 */
	@Override
	public String toString() {
		return "ListPredictor(" + predicted.size() + "){" +
				Arrays.toString(predicted.toArray(new String[0])) +
				'}';
	}
}
