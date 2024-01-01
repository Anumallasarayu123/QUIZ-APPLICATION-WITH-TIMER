import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class QuizApplicationGUI extends JFrame {
    private String[] questions = {
        "1. What is the capital of France?",
        "2. Which planet is known as the Red Planet?",
        "3. What is the largest mammal?",
        // Add more questions here...
    };
    private String[][] options = {
        {"A. Paris", "B. Rome", "C. Berlin", "D. Madrid"},
        {"A. Venus", "B. Mars", "C. Jupiter", "D. Saturn"},
        {"A. Elephant", "B. Blue Whale", "C. Giraffe", "D. Hippopotamus"},
        // Options for each question...
    };
    private char[] correctAnswers = {'A', 'B', 'B', /* Correct answers for each question */};

    private int currentQuestionIndex = 0;
    private int score = 0;
    private int timeLimitInSeconds = 15; // Change the time limit here (in seconds)

    private JLabel questionLabel;
    private JButton[] optionButtons;
    private Timer questionTimer;
    private int timeLeft; // Track remaining time

    public QuizApplicationGUI() {
        setTitle("Quiz Application");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setSize(400, 300);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new GridLayout(5, 1, 10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        questionLabel = new JLabel("", SwingConstants.CENTER);
        mainPanel.add(questionLabel);

        optionButtons = new JButton[4];
        for (int i = 0; i < 4; i++) {
            optionButtons[i] = new JButton();
            optionButtons[i].addActionListener(new OptionButtonListener());
            mainPanel.add(optionButtons[i]);
        }

        add(mainPanel, BorderLayout.CENTER);
        showNextQuestion();
    }

    private void showNextQuestion() {
        if (currentQuestionIndex < questions.length) {
            startTimer();
            questionLabel.setText(questions[currentQuestionIndex]);
            for (int i = 0; i < 4; i++) {
                optionButtons[i].setText(options[currentQuestionIndex][i]);
            }
        } else {
            showResult();
        }
    }

    private void startTimer() {
        timeLeft = timeLimitInSeconds;
        questionTimer = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                timeLeft--;
                if (timeLeft >= 0) {
                    updateTimerDisplay(timeLeft);
                } else {
                    questionTimer.stop();
                    checkAnswer(-1); // -1 to indicate time's up
                    currentQuestionIndex++;
                    showNextQuestion();
                }
            }
        });
        questionTimer.start();
    }

    private void updateTimerDisplay(int timeLeft) {
        questionLabel.setText(questions[currentQuestionIndex] + " - Time Left: " + timeLeft + " seconds");
    }

    private class OptionButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            JButton clickedButton = (JButton) e.getSource();
            for (int i = 0; i < optionButtons.length; i++) {
                if (clickedButton == optionButtons[i]) {
                    questionTimer.stop();
                    checkAnswer(i);
                    currentQuestionIndex++;
                    showNextQuestion();
                    break;
                }
            }
        }
    }

    private void checkAnswer(int selectedOptionIndex) {
        if (selectedOptionIndex == -1 || selectedOptionIndex == (correctAnswers[currentQuestionIndex] - 'A')) {
            score++;
        }
    }

    private void showResult() {
        StringBuilder resultText = new StringBuilder("Quiz ended!\n");
        resultText.append("Your final score is: ").append(score).append("/").append(questions.length).append("\n");
        for (int i = 0; i < questions.length; i++) {
            resultText.append(questions[i]).append(" - ").append(scoreQuestion(i)).append("\n");
        }
        JOptionPane.showMessageDialog(this, resultText.toString(), "Quiz Result", JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }

    private String scoreQuestion(int questionIndex) {
        return (questionIndex < score) ? "Correct" : "Incorrect";
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            QuizApplicationGUI quizApp = new QuizApplicationGUI();
            quizApp.setVisible(true);
        });
    }
}
