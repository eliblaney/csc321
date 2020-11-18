/**
 * N-Gram structure that holds the words that can follow a particular string and predict them.
 *
 * @author Eli Blaney
 * @version 2020-11-16
 */
public interface NGramPredictor {

	/**
	 * Predict the word that would come next in a sentence according to the N-Gram structure.
	 *
	 * @return The word that comes next.
	 */
	String predict();

	/**
	 * Add a word to the N-Gram structure.
	 *
	 * @param predictedWord The word to add
	 */
	void add(String predictedWord);
}
