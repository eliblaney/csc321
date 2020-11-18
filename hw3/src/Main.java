import java.util.*;
import java.util.function.Consumer;

/**
 * Driver class for function evaluations.
 *
 * @author Eli Blaney
 * @version 1.0
 */
@SuppressWarnings({"UnusedReturnValue", "ForLoopReplaceableByForEach", "ResultOfMethodCallIgnored"})
public class Main {

	private static final Random random = new Random();
	private static final int NUM_MULTIPLES = 5; // Number of times to double the initial list size
	private static final Map<String, Consumer<List<Integer>>> functions = Map.of(
			"getCounts", Main::getCounts,
			"getCountsV2", Main::getCountsV2,
			"addRemove", Main::addRemove
	);

	public static void main(String... args) {
		// Optional: Uncomment line below to simulate providing size arguments
		// args = new String[]{"10000", "20000", "40000", "80000", "160000"};

		FunctionEvaluator<List<Integer>, Integer> evaluator = new FunctionEvaluator<List<Integer>, Integer>(
				functions,          // Map of functions to test
				Main::addNums,      // Preprocessor function to populate lists
				new ArrayList<>(),  // Lists to measure
				new LinkedList<>()
		);

		if(args.length > 0) {
			// Evaluate functions for list sizes provided by arguments
			for(String arg : args) {
				evaluator.evaluate(Integer.parseInt(arg));
			}
		} else {
			// Get user input for initial list size to evaluate
			Scanner scanner = new Scanner(System.in);
			System.out.print("Initial list size: ");
			int size = scanner.nextInt();
			for(int i = 0; i < NUM_MULTIPLES; i++) {
				// Doubles the initial size NUM_MULTIPLES times
				evaluator.evaluate((int) (size * Math.pow(2, i)));
			}
			scanner.close();
		}
    }

	/**
	 * Add random numbers between 0 and 9, inclusive, to a List of Integers
	 *
	 * @param numbers A List<Integers> to add new numbers
	 * @param n Number of random integers to add
	 */
	public static void addNums(List<Integer> numbers, int n) {
		numbers.clear();
		while(n-- > 0) {
			numbers.add(random.nextInt(10));
		}
	}

	/**
	 * Generates an array of frequencies for each integer in the List.
	 *
	 * @param numbers A List<Integer> containing only numbers between 0 and 9, inclusive
	 * @return An array of ints that correspond to the frequency of each number at its corresponding index
	 */
	public static int[] getCounts(List<Integer> numbers) {
		int[] counts = new int[10];
		for(int i = 0; i<numbers.size(); i++) {
			counts[numbers.get(i)]++;
		}
		return counts;
	}

	/**
	 * Generates an array of frequencies for each integer in the List.
	 * An optimized version of {@link Main#getCounts(List)}.
	 *
	 * @param numbers A List<Integer> containing only numbers between 0 and 9, inclusive
	 * @return An array of ints that correspond to the frequency of each number at its corresponding index
	 */
	public static int[] getCountsV2(List<Integer> numbers) {
		int[] counts = new int[10];
		// Uses Iterator<Integer> under the hood
		for(Integer number : numbers) {
			counts[number]++;
		}
    	return counts;
	}

	/**
	 * Continuously removes the first element and adds a random number to the List, maintaining constant size.
	 *
	 * @param numbers The List<Integer>> to be modified by this function
	 * @return An int[] representation of the modified List
	 */
	public static int[] addRemove(List<Integer> numbers) {
		int len = numbers.size();
		int[] result = new int[len];
		while(len-- > 0) {
			numbers.remove(0);
			numbers.add(0, random.nextInt(10));
		}
		// Convert to array
		int i = 0;
		for(int n : numbers) {
			result[i++] = n;
		}
		return result;
	}
}
