
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class QuestionScreen implements Screen {
    private JPanel panel;
    private JLabel questionLabel;
    private JButton[] choiceButtons;
    private JPanel livesPanel;
    private JProgressBar progressBar;
    private JLabel questionCounter;

    private GradeManager gradeManager;
    private Runnable onStageComplete;
    private Runnable onGameOver;
    
    private int requiredCorrect;
    private ImageIcon heartIcon;

    public QuestionScreen(GradeManager gradeManager, Runnable onStageComplete, Runnable onGameOver) {
        this.gradeManager = gradeManager;
        this.onStageComplete = onStageComplete;
        this.onGameOver = onGameOver;
        this.requiredCorrect = gradeManager.getRequiredCorrect();

        // Load heart icon
        try {
            ImageIcon originalIcon = new ImageIcon("./assets/lives.png");
            Image scaledImage = originalIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
            heartIcon = new ImageIcon(scaledImage);
        } catch (Exception e) {
            System.err.println("Could not load lives.png: " + e.getMessage());
            heartIcon = null;
        }

        Image bgImage = new ImageIcon("./assets/question-bg.jpg").getImage();

        panel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (bgImage != null) {
                    g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };

        progressBar = new JProgressBar(0, 100);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        progressBar.setOpaque(false); 
        panel.add(progressBar, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));
        centerPanel.setOpaque(false); 

        questionLabel = new JLabel("Question text here");
        questionLabel.setFont(new Font("Arial", Font.BOLD, 20));
        questionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        questionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        questionLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        questionLabel.setForeground(Color.WHITE); 
        centerPanel.add(questionLabel);

        questionCounter = new JLabel("Correct: 0 / " + requiredCorrect);
        questionCounter.setFont(new Font("Arial", Font.PLAIN, 16));
        questionCounter.setAlignmentX(Component.CENTER_ALIGNMENT);
        questionCounter.setForeground(Color.WHITE);
        centerPanel.add(questionCounter);
        centerPanel.add(Box.createVerticalStrut(20));

        choiceButtons = new JButton[4];
        JPanel choicesPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        choicesPanel.setOpaque(false);
        choicesPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        for (int i = 0; i < 4; i++) {
            JButton btn = new JButton("Choice " + (i + 1));
            btn.setFont(new Font("Arial", Font.PLAIN, 16));
            btn.setBackground(Color.WHITE);
            btn.setForeground(Color.BLACK);
            btn.setOpaque(true);
            btn.setBorderPainted(true);
            btn.setFocusPainted(false);
            btn.setContentAreaFilled(true);

            int index = i;
            btn.addActionListener(e -> checkAnswer(index));

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

        livesPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        livesPanel.setOpaque(false);
        livesPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 20));
        updateLivesDisplay();

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);
        bottomPanel.add(livesPanel, BorderLayout.EAST);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        showNextQuestion();
    }

    private void showNextQuestion() {
        if (!gradeManager.hasNext()) {
            onStageComplete.run();
            return;
        }

        Question q = gradeManager.getCurrentQuestion();
        int correctAnswers = gradeManager.getCorrectAnswers();

        questionCounter.setText("Correct: " + correctAnswers + " / " + requiredCorrect);

        questionLabel.setText("<html><div style='text-align:center;'>" + q.getQuestion() + "</div></html>");

        String[] choices = q.getChoices();
        for (int i = 0; i < choiceButtons.length; i++) {
            if (i < choices.length) {
                choiceButtons[i].setText(choices[i]);
                choiceButtons[i].setEnabled(true);
                choiceButtons[i].setBackground(Color.WHITE);
                choiceButtons[i].setForeground(Color.BLACK);
                choiceButtons[i].setVisible(true);
            } else {
                choiceButtons[i].setVisible(false);
            }
        }

        // Update progress bar based on correct answers
        int progress = gradeManager.getProgress();
        progressBar.setValue(progress);
    }

    private void checkAnswer(int index) {
        Question q = gradeManager.getCurrentQuestion();
        int correct = q.getAnswer();
        boolean isCorrect = (index == correct);

        for (int i = 0; i < choiceButtons.length; i++) {
            if (i == correct) {
                choiceButtons[i].setBackground(new Color(46, 204, 113)); // Green
                choiceButtons[i].setForeground(Color.WHITE);
            } else if (i == index) {
                choiceButtons[i].setBackground(new Color(231, 76, 60)); // Red
                choiceButtons[i].setForeground(Color.WHITE);
            } else {
                choiceButtons[i].setBackground(Color.LIGHT_GRAY);
                choiceButtons[i].setForeground(Color.DARK_GRAY);
            }
            choiceButtons[i].setEnabled(false);
        }

        if (isCorrect) {
            // Record the correct answer
            gradeManager.recordCorrectAnswer();
        } else {
            // Lost a life
            gradeManager.loseLife();
            updateLivesDisplay();

            if (gradeManager.getLives() <= 0) {
                Timer timer = new Timer(500, e -> {
                    onGameOver.run();
                });
                timer.setRepeats(false);
                timer.start();
                return;
            }
        }

        Timer timer = new Timer(1000, e -> {
            gradeManager.nextQuestion();
            showNextQuestion();
        });
        timer.setRepeats(false);
        timer.start();
    }

    private void updateLivesDisplay() {
        livesPanel.removeAll();
        
        int lives = gradeManager.getLives();
        
        if (heartIcon != null) {
            for (int i = 0; i < lives; i++) {
                JLabel heartLabel = new JLabel(heartIcon);
                livesPanel.add(heartLabel);
            }
        } else {
            // Fallback to text if image can't be loaded
            JLabel livesLabel = new JLabel("Lives: ");
            livesLabel.setFont(new Font("Arial", Font.PLAIN, 18));
            livesLabel.setForeground(Color.WHITE);
            livesPanel.add(livesLabel);
            
            for (int i = 0; i < lives; i++) {
                JLabel heart = new JLabel("❤️");
                livesPanel.add(heart);
            }
        }
        
        livesPanel.revalidate();
        livesPanel.repaint();
    }

    @Override
    public JPanel getPanel() {
        return panel;
    }
}