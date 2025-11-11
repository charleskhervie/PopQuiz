import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.*;
import java.util.List;
import javax.swing.Timer;

public class QuestionScreen implements Screen {
    private JPanel panel;
    private JLabel questionLabel;
    private JTextArea questionTextArea; 
    private JScrollPane questionScrollPane;
    private JButton[] choiceButtons;
    private JPanel livesPanel;
    private JProgressBar progressBar;
    private JLabel questionCounter;

    private GradeManager gradeManager;
    private Runnable onStageComplete;
    private Runnable onGameOver;

    private int requiredCorrect;
    private ImageIcon heartIcon;

    private List<Integer> shuffledIndices;
    private int correctAnswerPosition;

    public QuestionScreen(GradeManager gradeManager, Runnable onStageComplete, Runnable onGameOver) {
        this.gradeManager = gradeManager;
        this.onStageComplete = onStageComplete;
        this.onGameOver = onGameOver;
        this.requiredCorrect = gradeManager.getRequiredCorrect();

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

        questionTextArea = new JTextArea(6, 50);
        questionTextArea.setFont(new Font("Courier New", Font.PLAIN, 14));
        questionTextArea.setEditable(false);
        questionTextArea.setLineWrap(false);
        questionTextArea.setWrapStyleWord(false);
        questionTextArea.setBackground(new Color(40, 44, 52));
        questionTextArea.setForeground(new Color(171, 178, 191));
        questionTextArea.setCaretColor(Color.WHITE);
        questionTextArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(60, 64, 72), 2),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        questionScrollPane = new JScrollPane(questionTextArea);
        questionScrollPane.setOpaque(false);
        questionScrollPane.getViewport().setOpaque(false);
        questionScrollPane.setMaximumSize(new Dimension(700, 150));
        questionScrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);

        centerPanel.add(questionLabel);
        centerPanel.add(questionScrollPane);

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

        questionScrollPane.setVisible(false);
        questionTextArea.setVisible(true); 
        showNextQuestion();
    }

    private boolean isProgrammingQuestion(Question q) {
        String type = q.getType();
        return type != null && (type.equals("output") || type.equals("code-snippet") || type.equals("missing"));
    }

    private String formatProgrammingText(String text) {
        // Unescape common escape sequences
        text = text.replace("\\n", "\n");
        text = text.replace("\\t", "    ");
        text = text.replace("\\\"", "\"");

        return text;
    }

    private void showNextQuestion() {
        if (!gradeManager.hasNext()) {
            onStageComplete.run();
            return;
        }

        Question q = gradeManager.getCurrentQuestion();
        int correctAnswers = gradeManager.getCorrectAnswers();

        questionCounter.setText("Correct: " + correctAnswers + " / " + requiredCorrect);

        boolean isProgramming = isProgrammingQuestion(q);

        if (isProgramming) {
            questionLabel.setVisible(false);
            questionScrollPane.setVisible(true);
            questionTextArea.setText(formatProgrammingText(q.getQuestion()));
            questionTextArea.setCaretPosition(0);
        } else {
            questionLabel.setVisible(true);
            questionScrollPane.setVisible(false);
            questionLabel.setText("<html><div style='text-align:center;'>" + q.getQuestion() + "</div></html>");
        }

        panel.revalidate();
        panel.repaint();

        String[] choices = q.getChoices();
        int correctAnswerIndex = q.getAnswer();

        shuffledIndices = new ArrayList<>();
        for (int i = 0; i < choices.length; i++) {
            shuffledIndices.add(i);
        }
        Collections.shuffle(shuffledIndices);

        correctAnswerPosition = -1;
        for (int i = 0; i < shuffledIndices.size(); i++) {
            if (shuffledIndices.get(i) == correctAnswerIndex) {
                correctAnswerPosition = i;
                break;
            }
        }

        for (int i = 0; i < choiceButtons.length; i++) {
            if (i < shuffledIndices.size()) {
                int originalIndex = shuffledIndices.get(i);
                String choiceText = choices[originalIndex];

                if (isProgramming) {
                    choiceText = formatProgrammingText(choiceText);
                    choiceButtons[i].setFont(new Font("Courier New", Font.PLAIN, 12));
                    choiceText = "<html><pre style='font-family: Courier New; font-size: 10px;'>" +
                                choiceText.replace("<", "&lt;").replace(">", "&gt;") +
                                "</pre></html>";
                } else {
                    choiceButtons[i].setFont(new Font("Arial", Font.PLAIN, 16));
                }

                choiceButtons[i].setText(choiceText);
                choiceButtons[i].setEnabled(true);
                choiceButtons[i].setBackground(Color.WHITE);
                choiceButtons[i].setForeground(Color.BLACK);
                choiceButtons[i].setVisible(true);
            } else {
                choiceButtons[i].setVisible(false);
            }
        }

        int progress = gradeManager.getProgress();
        progressBar.setValue(progress);
    }

    private void checkAnswer(int buttonIndex) {
        boolean isCorrect = (buttonIndex == correctAnswerPosition);

        for (int i = 0; i < choiceButtons.length; i++) {
            if (i == correctAnswerPosition) {
                choiceButtons[i].setBackground(new Color(46, 204, 113)); // Green
                choiceButtons[i].setForeground(Color.WHITE);
            } else if (i == buttonIndex) {
                choiceButtons[i].setBackground(new Color(231, 76, 60)); // Red
                choiceButtons[i].setForeground(Color.WHITE);
            } else {
                choiceButtons[i].setBackground(Color.LIGHT_GRAY);
                choiceButtons[i].setForeground(Color.DARK_GRAY);
            }
            choiceButtons[i].setEnabled(false);
        }

        gradeManager.markQuestionAsUsed();

        if (isCorrect) {
            gradeManager.recordCorrectAnswer();
        } else {
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
