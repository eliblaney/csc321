import java.io.*;
import java.util.Scanner;

/**
 * A model for a binary tree consisting of questions and answers.
 *
 * @author Eli Blaney
 * @version 1.0
 */
public class QuestionTree {

	private static final String QUESTION_PREFIX = "Q:";
	private static final String ANSWER_PREFIX = "A:";

	private final QuestionNode root;
	private QuestionNode curr;

	/**
	 * Create a default QuestionTree that only knows about cats.
	 */
	public QuestionTree() {
		this(null);
	}

	/**
	 * Create a new QuestionTree from a pre-existing file
	 *
	 * @param filename The file containing the tree to import
	 */
	public QuestionTree(String filename) {
		this.root = this.curr = new QuestionNode(ANSWER_PREFIX + "cat");
		if(filename != null) {
			try(Scanner sc = new Scanner(new File(filename))) {
				read(sc, this.root.setText(null));
			} catch(FileNotFoundException e) {
				// Default root node will be cat
			}
		}
	}

	/**
	 * Read from a Scanner object to build a tree starting at the provided node.
	 *
	 * @param sc The Scanner reading the question tree file
	 * @param node The node to build the tree from, which will be overwritten with text data.
	 */
	private void read(Scanner sc, QuestionNode node) {
		if(!sc.hasNextLine()) {
			return;
		}

		String text = sc.nextLine();
		node.setText(text);

		if(text.startsWith(QUESTION_PREFIX)) {
			QuestionNode left = new QuestionNode(null);
			QuestionNode right = new QuestionNode(null);

			read(sc, node.setLeft(left));
			read(sc, node.setRight(right));
		}
	}

	/**
	 * Restart the Twenty Questions game by resetting the QuestionTree object to point at the root node.
	 */
	public void restartGame() {
		this.curr = this.root;
	}

	/**
	 * Determine if the QuestionNode at the current position is a guess (answer) as opposed to a question.
	 *
	 * @return Whether the current QuestionNode is a guess.
	 */
	public boolean isGuess() {
		return this.curr.getText().startsWith(ANSWER_PREFIX);
	}

	/**
	 * Retrieve the text of the QuestionNode at the current position, without the question/answer prefix.
	 *
	 * @return The text of the question or answer as it can be displayed to the user.
	 */
	public String getCurrentText() {
		return this.curr.getText().substring(2);
	}

	/**
	 * Submit a response to the Twenty Questions game and move to the next correct node in the tree.
	 *
	 * @param ans The answer to the Twenty Questions game's previous question
	 */
	public void nextQuestion(String ans) {
		if(ans.equalsIgnoreCase("yes") || ans.equalsIgnoreCase("y")) {
			this.curr = this.curr.getLeft();
		} else {
			this.curr = this.curr.getRight();
		}
	}

	/**
	 * Add a new question to the QuestionTree at the current node's position.
	 *
	 * @param q The question to ask the user, e.g. "Is it red?"
	 * @param item The item to add to the tree, e.g. "Apple"
	 * @param corrAns Whether the item is the affirmative response to the question or not, i.e. "yes", "y", "no", "n"
	 */
	public void addQuestion(String q, String item, String corrAns) {
		QuestionNode oldAns = new QuestionNode(this.curr.getText());
		QuestionNode newAns = new QuestionNode(ANSWER_PREFIX + item);
		if(corrAns.equalsIgnoreCase("yes") || corrAns.equalsIgnoreCase("y")) {
			this.curr.setLeft(newAns);
			this.curr.setRight(oldAns);
		} else {
			this.curr.setLeft(oldAns);
			this.curr.setRight(newAns);
		}
		this.curr.setText(QUESTION_PREFIX + q);
	}

	/**
	 * Save the current QuestionTree model to a file.
	 *
	 * @param filename The name of the file to save the QuestionTree into
	 */
	public void save(String filename) {
		try(BufferedWriter writer = new BufferedWriter(new FileWriter(new File(filename)))) {
			writer.write(toString());
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create a String representation of the current QuestionTree model
	 *
	 * @return A String of all the QuestionNodes in this QuestionTree.
	 */
	@Override
	public String toString() {
		return this.root.toString();
	}

	/**
	 * A model for a single node in the QuestionTree. Implemented as a normal binary tree node for strings.
	 *
	 * @author Eli Blaney
	 * @version 1.0
	 */
	private static class QuestionNode {

		private String text;
		private QuestionNode left, right;

		/**
		 * Instantiate a new QuestionNode object, holding text corresponding to the question or answer.
		 *
		 * @param text The content of the node, i.e. a question or answer beginning
		 *             with a question/answer prefix, respectively
		 */
		public QuestionNode(String text) {
			this.text = text;
		}

		/**
		 * Obtain the text that the node is currently holding.
		 *
		 * @return A String of the node's text content, beginning with the question or answer prefix
		 */
		public String getText() {
			return this.text;
		}

		/**
		 * Set the node's current text content.
		 *
		 * @param text The new content of the node, i.e. a question or answer beginning
		 *             with a question/answer prefix, respectively
		 * @return The QuestionNode object (for convenience).
		 */
		public QuestionNode setText(String text) {
			this.text = text;
			return this;
		}

		/**
		 * Obtain the node at the left child position relative to this node.
		 *
		 * @return The QuestionNode at the left child position.
		 */
		public QuestionNode getLeft() {
			return left;
		}

		/**
		 * Obtain the node at the right child position relative to this node.
		 *
		 * @return The QuestionNode at the right child position.
		 */
		public QuestionNode getRight() {
			return right;
		}

		/**
		 * Set the node at the left child position relative to this node.
		 *
		 * @param node The new QuestionNode to place at the left child position.
		 * @return The newly positioned QuestionNode child object (for convenience).
		 */
		public QuestionNode setLeft(QuestionNode node) {
			return this.left = node;
		}

		/**
		 * Set the node at the right child position relative to this node.
		 *
		 * @param node The new QuestionNode to place at the right child position.
		 * @return The newly positioned QuestionNode child object (for convenience).
		 */
		public QuestionNode setRight(QuestionNode node) {
			return this.right = node;
		}

		/**
		 * Create a String representation of the current QuestionNode object, containing each question/answer
		 * prefix and node text separated by newlines.
		 *
		 * @return A String of all the QuestionNode objects that are children of this node.
		 */
		@Override
		public String toString() {
			return toString(this).substring(1);
		}

		/**
		 * Create a String representation of a QuestionNode object, containing each question/answer
		 * prefix and node text separated by newlines.
		 *
		 * @return A String of all the QuestionNode objects that are children of the given node.
		 */
		private static String toString(QuestionNode node) {
			if(node == null) {
				return "";
			}
			return "\n" +  node.getText() + toString(node.getLeft()) + toString(node.getRight());
		}

	}

}
