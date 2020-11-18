import java.util.NoSuchElementException;
import java.util.Scanner;
/**
 * DO NOT MODIFY THIS CLASS!
 * Provides a text terminal for the game Twenty Questions
 * @author Catie Baker
 * @version 10/17/2020
 */
public class TwentyQTerminal {

	public static void main(String[] args) {
		Scanner key = new Scanner(System.in);
		String play = "yes";
		String ans;
		QuestionTree t;
		if(args.length == 0) {
			t = new QuestionTree();
		}
		else {
			t = new QuestionTree(args[0]);
		}
		while(play.toLowerCase().charAt(0) == 'y') {
			System.out.println("Think of an item");
			t.restartGame();
			while(!t.isGuess()) {
				System.out.println(t.getCurrentText());
				ans = key.nextLine();
				try {
					t.nextQuestion(ans);
				}
				catch (NoSuchElementException e) {
					System.out.println("Invalid response");
				}
			}
			System.out.println("Is your item a(n) "+t.getCurrentText()+"?");
			ans = key.nextLine();
			if(ans.toLowerCase().charAt(0) == 'y') {
				System.out.println("The computer won!");
			}
			else if(ans.toLowerCase().charAt(0) == 'n') {
				System.out.println("You won. Help make the computer smarter");
				System.out.println("What was your item?");
				String item = key.nextLine().trim();
				System.out.println("What is a question that would distinguish between your item and "+t.getCurrentText());
				String q = key.nextLine().trim();
				System.out.println("Is "+item+" the answer for yes or no?");
				String corrAns = key.nextLine();
				t.addQuestion(q, item, corrAns);
			}
			System.out.println("Do you want to play again?");
			play = key.nextLine();
		}
		
		System.out.println("Do you want to save the current question tree?");
		ans = key.nextLine();
		if(ans.toLowerCase().charAt(0) == 'y') {
			System.out.println("What filename do you want to save the tree in?");
			String filename = key.nextLine().trim();
			t.save(filename);
		}

	}

}
