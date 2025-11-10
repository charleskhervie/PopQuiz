// QuestionScreen.java
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class QuestionScreen implements Screen {
    private JPanel panel;
    private JLabel questionLabel;
    private JButton[] choiceButtons;
    private JLabel livesLabel;
    private JProgressBar progressBar;
    private JLabel questionCounter;

    private GradeManager gradeManager;
    private Runnable onStageComplete;
    private Runnable onGameOver;
    
    private int totalQuestions;

    public QuestionScreen(GradeManager gradeManager, Runnable onStageComplete, Runnable onGameOver) {
        this.gradeManager = gradeManager;
        this.onStageComplete = onStageComplete;
        this.onGameOver = onGameOver;
        this.totalQuestions = gradeManager.getTotalQuestions();

        panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        // Progress Bar
        progressBar = new JProgressBar(0, 100);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        panel.add(progressBar, BorderLayout.NORTH);

        // Question Area
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));
        centerPanel.setBackground(Color.WHITE);

        questionLabel = new JLabel("Question text here");
        questionLabel.setFont(new Font("Arial", Font.BOLD, 20));
        questionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        questionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        questionLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        centerPanel.add(questionLabel);
        
        // Question Counter
        questionCounter = new JLabel("Question 1 of " + totalQuestions);
        questionCounter.setFont(new Font("Arial", Font.PLAIN, 16));
        questionCounter.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(questionCounter);
        centerPanel.add(Box.createVerticalStrut(20));

        choiceButtons = new JButton[4];

        JPanel choicesPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        choicesPanel.setBackground(Color.WHITE);
        choicesPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        for (int i = 0; i < 4; i++) {
            JButton btn = new JButton("Choice " + (i + 1));
            btn.setFont(new Font("Arial", Font.PLAIN, 16));
            int index = i;
            btn.addActionListener(e -> checkAnswer(index));
            
            // Keyboard shortcuts (1-4 keys)
            int keyCode = KeyEvent.VK_1 + i;
            btn.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
               .put(KeyStroke.getKeyStroke(keyCode, 0), "choice" + i);
            btn.getActionMap().put("choice" + i, new AbstractAction() {
                public void actionPerformed(ActionEvent e) {
                    if (btn.isEnabled()) checkAnswer(index);
                }
            });
            
            choiceButtons[i] = btn;
            choicesPanel.add(btn);
        }

        centerPanel.add(choicesPanel);

        panel.add(centerPanel, BorderLayout.CENTER);

        // Lives display
        livesLabel = new JLabel();
        livesLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        livesLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 20));
        updateLivesLabel();

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.add(livesLabel, BorderLayout.EAST);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        // Start first question
        showNextQuestion();
    }

    private void showNextQuestion() {
        if (!gradeManager.hasNext()) {
            onStageComplete.run();
            return;
        }

        Question q = gradeManager.getCurrentQuestion();
        
        // Update question counter
        int currentQuestionNum = gradeManager.getCurrentIndex() + 1;
        questionCounter.setText("Question " + currentQuestionNum + " of " + totalQuestions);
        
        questionLabel.setText("<html><div style='text-align:center;'>" + q.getQuestion() + "</div></html>");
        String[] choices = q.getChoices();
        for (int i = 0; i < choiceButtons.length; i++) {
            if (i < choices.length) {
                choiceButtons[i].setText(choices[i]);
                choiceButtons[i].setEnabled(true);
                choiceButtons[i].setBackground(null);
                choiceButtons[i].setVisible(true);
            } else {
                choiceButtons[i].setVisible(false);
            }
        }

        // Update progress bar
        int progress = (currentQuestionNum * 100) / totalQuestions;
        progressBar.setValue(progress);
    }

    private void checkAnswer(int index) {
        Question q = gradeManager.getCurrentQuestion();
        int correct = q.getAnswer();

        // Highlight colors
        for (int i = 0; i < choiceButtons.length; i++) {
            if (i == correct) {
                choiceButtons[i].setBackground(Color.GREEN);
            } else if (i == index) {
                choiceButtons[i].setBackground(Color.RED);
            } else {
                choiceButtons[i].setBackground(null);
            }
            choiceButtons[i].setEnabled(false);
        }

        // Update lives if wrong
        if (index != correct) {
            gradeManager.loseLife();
            updateLivesLabel();

            if (gradeManager.getLives() <= 0) {
                JOptionPane.showMessageDialog(panel, "All lives lost!");
                onGameOver.run();
                return;
            }
        }

        // Move to next question after short delay
        Timer timer = new Timer(1000, e -> {
            gradeManager.nextQuestion();
            showNextQuestion();
        });
        timer.setRepeats(false);
        timer.start();
    }

    private void updateLivesLabel() {
        int lives = gradeManager.getLives();
        StringBuilder sb = new StringBuilder("Lives: ");
        for (int i = 0; i < lives; i++) sb.append("❤️");
        livesLabel.setText(sb.toString());
    }

    @Override
    public JPanel getPanel() {
        return panel;
    }
}