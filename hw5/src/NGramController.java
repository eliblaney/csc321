import java.io.*;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Controller class for N-Gram predictions.
 *
 * @author Eli Blaney
 * @version 2020-11-03
 */
public class NGramController {

	// You can choose between ListPredictor and CountPredictor by editing this constant.
	private static final Supplier<NGramPredictor> predictor = CountPredictor::new;

	private final Random r = new Random();
	private final Map<String, NGramPredictor> ngrams = new HashMap<>();
	private final int N;

	/**
	 * Create a new NGramController.
	 *
	 * @param N The order of this model. N=2 means there is a 1:1 relationship between the words in the keys and
	 *          predicted values (e.g. "I" => "need"; N=3 would symbolize "I need" => "to").
	 */
	public NGramController(int N) {
		if(N <= 1) {
			throw new IllegalArgumentException("N must be greater than or equal to 2.");
		}
		this.N = N;
	}

	/**
	 * Feed the model a set of new word pairings to add to the model.
	 *
	 * @param s The string of words to incorporate into the model
	 */
	public void feed(String s) {
		// Extract rolling N-length substrings containing N words
		String[] words = s.split(" ");
		int size = words.length;
		for(int i = 0; i <= size - N; i++) {
			String key = stripWord(String.join(" ", Arrays.copyOfRange(words, i, i + N - 1)));
			String next = stripWord(words[i + N - 1]);
			if(ngrams.containsKey(key)) {
				ngrams.get(key).add(next);
			} else {
				NGramPredictor p = predictor.get();
				ngrams.put(key, p);
				p.add(next);
			}
		}
	}

	/**
	 * Feed the model all word pairings found in a particular file.
	 *
	 * @param filename The name of the file to read and extract word pairings
	 */
	public void feedFile(String filename) {
		try(BufferedReader r = new BufferedReader(new FileReader(new File(filename)))) {
			String contents = r.lines().collect(Collectors.joining(" "));
			feed(contents);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Predict the next word in a given string based on the existing words. The model will randomly guess based on
	 * the words that tend to follow the trailing words, and if there are no matching words, will give a totally
	 * random guess from the words it does know.
	 *
	 * @param sentence The string that the model will read and act on
	 * @return The original string with the model's predicted word at the end.
	 */
	public String predictNext(String sentence) {
		// Extract the final N-1 words of a sentence as a substring
		String[] words = stripWord(sentence).split(" ");
		int len = words.length;
		String substring = sentence;
		if(len >= N - 1) {
			substring = String.join(" ", Arrays.copyOfRange(words, len - N + 1, len));
		}
		substring = stripWord(substring);
		// Find predictor corresponding with the substring
		NGramPredictor ngram = ngrams.get(substring);
		// Attempt to use the predictor if it exists
		if(ngram != null) {
			String prediction = ngram.predict();
			if(prediction != null) {
				return sentence + " " + prediction;
			}
		}
		// If the predictor does not exist (or is empty), then predict a random word from a random existing predictor
		int size = ngrams.size();
		String[] keys = ngrams.keySet().toArray(String[]::new);
		String prediction = null;
		while(prediction == null) {
			String randomKey = keys[r.nextInt(size)];
			prediction = ngrams.get(randomKey).predict();
		}
		return sentence + " " + prediction;
	}

	/**
	 * Retrieve the order of this model.
	 *
	 * @return The order specified when creating the model.
	 */
	public int getN() {
		return N;
	}

	/**
	 * Strips the provided word of non-letter characters at the beginning
	 * and the end of the word.
	 * @param word the word to strip
	 * @return a lowercase version of the word with leading and
	 *     trailing non-letter characters removed
	 */
	public static String stripWord(String word) {
		while(word.length() > 0 && !Character.isLetter(word.charAt(0))) {
			word = word.substring(1);
		}
		while(word.length() > 0 && !Character.isLetter(word.charAt(word.length()-1))) {
			word = word.substring(0, word.length()-1);
		}
		return word.toLowerCase();
	}
}
