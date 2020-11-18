import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Performs a timed evaluation of a given set of functions
 *
 * @param <T> Function argument
 * @param <E> Preprocessor supplemental argument
 *
 * @author Eli Blaney
 * @version 1.0
 */
public class FunctionEvaluator<T, E> {

	private final Map<String, Consumer<T>> functions;
	private final T[] arguments;
	private final BiConsumer<T, E> preprocessor;

	private long time;

	@SafeVarargs
	public FunctionEvaluator(Map<String, Consumer<T>> functions, T... arguments) {
		this(functions, (t, e) -> {}, arguments);
	}

	@SafeVarargs
	public FunctionEvaluator(Map<String, Consumer<T>> functions, BiConsumer<T, E> preprocessor, T... arguments) {
		this.functions = functions;
		this.arguments = arguments;
		this.preprocessor = preprocessor;
	}

	/**
	 * Evaluate the time in milliseconds it takes for a function to run.
	 * Displays the resulting information to System.out.
	 */
	public void evaluate() {
		this.evaluate(null);
	}

	/**
	 * Evaluate the time in milliseconds it takes for a function to run.
	 * Displays the resulting information to System.out.
	 *
	 * @param preArg The argument to be provided to the preprocessor function.
	 */
	public void evaluate(E preArg) {
		for(Map.Entry<String, Consumer<T>> e : functions.entrySet()) {
			String name = e.getKey();
			Consumer<T> func = e.getValue();

			System.out.println("Evaluating: " + name + " [len: " + preArg + "]");
			System.out.println("---------------------------------------------");
			for(T arg : this.arguments) {
				// Allow T to be preprocessed if necessary
				this.preprocessor.accept(arg, preArg);

				// Perform function evaluation
				markTime();
				func.accept(arg);
				long time = checkTime();

				System.out.println("[" + arg.getClass().getSimpleName() + "] Finished in "  + time + "ms");
			}
			System.out.println("---------------------------------------------");
			System.out.println();
		}
	}

	/**
	 * Set a marker for the current time in milliseconds.
	 */
	private void markTime() {
		this.time = System.currentTimeMillis();
	}

	/**
	 * Determine the amount of time that has passed since the last time marker has been set.
	 *
	 * @return The number of milliseconds since the last time marker.
	 */
	private long checkTime() {
		return System.currentTimeMillis() - this.time;
	}

}
