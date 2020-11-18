import javax.swing.*;
import java.awt.*;

/**
 * 
 * DO NOT MODIFY THIS CLASS!
 * Provides a GUI for playing the game Twenty Questions 
 * @author cmb79981
 *
 */
public class Q20GUI {

	private JFrame frame;
	private JTextField qText;
	private boolean gameOver;
	private boolean improve;
	private boolean keepPlaying;
	private final QuestionTree tree;
	private JTextField itemText;
	private JTextField ansText;
	private JLabel question;
	private JPanel improvePanel;
	private static String openFileName;
	private static String saveFileName;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		if(args.length > 1) {
			openFileName = args[0];
			saveFileName = args[1];
		}
		else if(args.length == 1) {
			openFileName = args[0];
			saveFileName = "";
		}
		else {
			openFileName = "";
			saveFileName = "";
		}
		EventQueue.invokeLater(() -> {
			try {
				Q20GUI window = new Q20GUI();
				window.frame.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	
	public Q20GUI() {
		gameOver = false;
		keepPlaying = true;
		improve = false;
		if(openFileName.equals("")) {
			tree = new QuestionTree();
		}
		else {
			tree = new QuestionTree(openFileName);
		}
		initialize();
		startGame();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		question = new JLabel("New label");
		question.setBounds(0, 6, 450, 96);
		question.setHorizontalAlignment(SwingConstants.CENTER);
		frame.getContentPane().add(question);

		JButton btnYes = new JButton("Yes");
		btnYes.addActionListener(e -> yesButtonAction());
		btnYes.setBounds(37, 102, 117, 29);
		frame.getContentPane().add(btnYes);

		JButton btnNo = new JButton("No");
		btnNo.addActionListener(e -> noButtonAction());
		btnNo.setBounds(285, 102, 117, 29);
		frame.getContentPane().add(btnNo);

		improvePanel = new JPanel();
		improvePanel.setBounds(6, 163, 438, 109);
		frame.getContentPane().add(improvePanel);
		improvePanel.setLayout(null);

		JLabel lblQ = new JLabel("Q:");
		lblQ.setBounds(129, 10, 14, 16);
		improvePanel.add(lblQ);
		lblQ.setHorizontalAlignment(SwingConstants.TRAILING);

		qText = new JTextField();
		qText.setBounds(155, 6, 277, 26);
		improvePanel.add(qText);
		qText.setColumns(10);

		JLabel lblItem = new JLabel("Item:");
		lblItem.setBounds(111, 38, 32, 16);
		improvePanel.add(lblItem);
		lblItem.setHorizontalAlignment(SwingConstants.TRAILING);

		itemText = new JTextField();
		itemText.setBounds(155, 34, 277, 26);
		improvePanel.add(itemText);
		itemText.setColumns(10);

		JLabel lblAnswerForYour = new JLabel("Answer for your item:");
		lblAnswerForYour.setBounds(6, 67, 137, 16);
		improvePanel.add(lblAnswerForYour);
		lblAnswerForYour.setHorizontalAlignment(SwingConstants.TRAILING);

		ansText = new JTextField();
		ansText.setBounds(151, 67, 281, 29);
		improvePanel.add(ansText);
		ansText.setColumns(10);
	}

	public void startGame() {
		gameOver = false;
		keepPlaying = true;
		improve = false;
		tree.restartGame();
		improvePanel.setVisible(false);
		update();
	}

	public void update() {
		if(!gameOver) {
			if(tree.isGuess()) {
				question.setText("Is your item "+tree.getCurrentText());
			}
			else {
				question.setText(tree.getCurrentText());
			}
		}
		else if(improve){
			String text = "<html> <p align='center'>"+question.getText() +"</p>";
			text += "<p align='center'>Do you want to improve the game? </p>";
			text += "<p align='center'>If so, enter a new yes/no question below that would have distinguished your item from the guessed item, "+
			"the item you were thinking of and the answer to your question for that item</p></html>";
			improvePanel.setVisible(true);
			question.setText(text);
		}
		else if(!keepPlaying){
			question.setText("Do you want to save the current tree?");
		}
		else {
			question.setText("Do you want to play again?");
		}
	}

	public void improveTree() {
		if(!qText.equals("") && !ansText.equals("") && !itemText.equals("")) {
			tree.addQuestion(qText.getText(), itemText.getText(), ansText.getText());
		}
		improve = false;
		improvePanel.setVisible(false);
	}

	public void yesButtonAction() {
		if(!gameOver) {
			if(tree.isGuess()) {
				gameOver = true;
				question.setText("The computer was correct!");
			}
			else {
				tree.nextQuestion("yes");
			}
		}
		else if(improve){
			improveTree();
		}
		else if(!keepPlaying){
			if(saveFileName.equals("")) {
				tree.save("output.txt");
			}
			else {
				tree.save(saveFileName);
			}
			frame.setVisible(false);
		}
		else {
			startGame();
		}
		update();
	}

	public void noButtonAction() {
		if(!gameOver) {
			if(tree.isGuess()) {
				gameOver = true;
				improve = true;
				question.setText("The computer was wrong :(");
			}
			else {
				tree.nextQuestion("no");
			}
		}
		else if(improve){
			improve = false;
		}
		else if(!keepPlaying) {
			frame.setVisible(false);
		}
		else {
			keepPlaying = false;
		}
		this.update();
	}
}
