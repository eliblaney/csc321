import java.util.Scanner;

/**
 * Driver class for N-Gram modeling.
 *
 * @author Eli Blaney
 * @version 2020-11-03
 */
public class Main {

    public static void main(String... args) {
    	Scanner sc = new Scanner(System.in);
    	System.out.println("What file should we use for the model?");
    	String filename = sc.nextLine();
    	System.out.println("What order model should I create?");
    	int N = sc.nextInt();

    	// Build the model of order N and feed it our starting text
    	NGramController model = new NGramController(N);
	    model.feedFile(filename);

	    System.out.print("""
			    Please type the start of the phrase that you would like
			    the model to complete. Every time you hit enter, it will
			    add whatever you have typed to the text and then predict
			    the next word, printing the message in its entirety with the
			    new prediction. It will continue to make predictions until
			    you type Q""");
    	String input;
    	String sentence = "";
    	do {
		    System.out.println(sentence);
    		input = sc.nextLine();
    		if(!input.isEmpty() || !sentence.isEmpty()) {
			    sentence += " " + input;
			    sentence = model.predictNext(sentence.trim());
		    }
	    } while(!input.equalsIgnoreCase("q"));
    	sc.close();
    }
}
