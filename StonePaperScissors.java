import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class StonePaperScissors extends JFrame implements ActionListener {
    private JButton stoneButton, paperButton, scissorsButton;
    private JLabel userChoiceLabel, computerChoiceLabel, resultLabel;
    private String[] options = {"Stone", "Paper", "Scissors"};

    public StonePaperScissors() {
        // Frame setup
        setTitle("Stone Paper Scissors Game");
        setSize(800, 800);
        setLayout(new GridLayout(3, 1));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Top panel for buttons
        JPanel buttonPanel = new JPanel();
        ImageIcon stoneIcon = new ImageIcon("C:\\Users\\harsh\\Desktop\\DivyaJava\\Projects\\Stone-Paper-Scissor\\stone.png");
        ImageIcon paperIcon = new ImageIcon("C:\\Users\\harsh\\Desktop\\DivyaJava\\Projects\\Stone-Paper-Scissor\\paper.png");
        ImageIcon scissorsIcon = new ImageIcon("C:\\Users\\harsh\\Desktop\\DivyaJava\\Projects\\Stone-Paper-Scissor\\scissors.png");

        // Create buttons with images
        stoneButton = createImageButton(stoneIcon);
        paperButton = createImageButton(paperIcon);
        scissorsButton = createImageButton(scissorsIcon);
        

        stoneButton.addActionListener(this);
        paperButton.addActionListener(this);
        scissorsButton.addActionListener(this);

        buttonPanel.add(stoneButton);
        buttonPanel.add(paperButton);
        buttonPanel.add(scissorsButton);

        // Middle panel for choices
        JPanel choicePanel = new JPanel(new GridLayout(2, 1));
        userChoiceLabel = new JLabel("Your Choice: ");
        userChoiceLabel.setFont(new Font("Arial", Font.BOLD, 25));

        computerChoiceLabel = new JLabel("Computer's Choice: ");
        computerChoiceLabel.setFont(new Font("Arial", Font.BOLD , 25));

        choicePanel.add(userChoiceLabel);
        choicePanel.add(computerChoiceLabel);

        // Bottom panel for result
        JPanel resultPanel = new JPanel();
        resultLabel = new JLabel("Result: ");
        resultLabel.setFont(new Font("Arial", Font.BOLD , 25));

        resultPanel.add(resultLabel);

        // Add panels to frame
        add(buttonPanel);
        add(choicePanel);
        add(resultPanel);

        setVisible(true);
    }

    private JButton createImageButton(ImageIcon icon) {
        // Resize image (optional) to ensure consistency
        Image img = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(img);

        // Create button and set icon
        JButton button = new JButton(scaledIcon);

        // Adjust button size to match image size
        button.setPreferredSize(new Dimension(140,140));

        //button.addActionListener(this); // Add action listener for interactivity
        return button;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        // Get user's choice
        String userChoice = "";
        if (e.getSource() == stoneButton) userChoice = "Stone";
        if (e.getSource() == paperButton) userChoice = "Paper";
        if (e.getSource() == scissorsButton) userChoice = "Scissors";

        // Get computer's random choice
        Random random = new Random();
        String computerChoice = options[random.nextInt(3)];

        // Update labels
        userChoiceLabel.setText("Your Choice: " + userChoice);
        computerChoiceLabel.setText("Computer's Choice: " + computerChoice);

        // Determine the result
        String result = determineWinner(userChoice, computerChoice);
        resultLabel.setText("Result: " + result);
        resultLabel.setFont(new Font("Arial", Font.BOLD , 25));
    }

    private String determineWinner(String userChoice, String computerChoice) {
        if (userChoice.equals(computerChoice)) {
            return "It's a Draw!";
        }
        if ((userChoice.equals("Stone") && computerChoice.equals("Scissors")) ||
            (userChoice.equals("Paper") && computerChoice.equals("Stone")) ||
            (userChoice.equals("Scissors") && computerChoice.equals("Paper"))) {
            return "You Win!";
        }
        return "Computer Wins!";
    }

    public static void main(String[] args) {
        new StonePaperScissors();
    }
}
