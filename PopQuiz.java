// PopQuiz.java
import javax.swing.*;
import java.awt.event.*;

public class PopQuiz {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GameFrame frame = new GameFrame();

            // Step 1: Declare all listeners first (arrays so they can reference each other)
            final ActionListener[] onStart = new ActionListener[1];
            final ActionListener[] onInstructions = new ActionListener[1];
            final ActionListener[] onExit = new ActionListener[1];

            // Step 2: Define onExit
            onExit[0] = e -> System.exit(0);

            // Step 3: Define onStart (Start button from TitleScreen)
            onStart[0] = e -> {
                // Start Grade 1 intro
                loadGrade(1, frame, onStart, onInstructions, onExit);
            };

            // Step 4: Define onInstructions (Instructions button from TitleScreen)
            onInstructions[0] = e -> {
                InstructionsScreen instructions = new InstructionsScreen(evt -> {
                    // Go back to title
                    frame.showScreen(new TitleScreen(onStart[0], onInstructions[0], onExit[0]));
                });
                frame.showScreen(instructions);
            };

            // Step 5: Show the initial title screen
            TitleScreen titleScreen = new TitleScreen(onStart[0], onInstructions[0], onExit[0]);
            frame.showScreen(titleScreen);

            // Step 6: Show window
            frame.setVisible(true);
        });
    }

    // Handles grade flow, stage victory, and game over
    private static void loadGrade(int grade, GameFrame frame,
                                  ActionListener[] onStart, ActionListener[] onInstructions, ActionListener[] onExit) {

        StageIntroScreen intro = new StageIntroScreen(grade, evt -> {
            // Load questions
            var theoretical = QuestionLoader.loadQuestions("questions.json", "theoretical");
            var programming = QuestionLoader.loadQuestions("questions.json", "programming");

            GradeManager manager = new GradeManager(theoretical, programming);
            manager.startGrade();

            // Define what happens when you finish a grade
            Runnable onStageComplete = () -> {
                if (grade == 5) {
                    // Game victory after Grade 5
                    frame.showScreen(new GameVictoryScreen(evt2 -> {
                        frame.showScreen(new TitleScreen(onStart[0], onInstructions[0], onExit[0]));
                    }));
                } else {
                    // Move to next grade
                    frame.showScreen(new StageVictoryScreen(grade, evt2 -> {
                        loadGrade(grade + 1, frame, onStart, onInstructions, onExit);
                    }, onExit[0]));
                }
            };

            // Define what happens on Game Over
            Runnable onGameOver = () -> {
                frame.showScreen(new GameOverScreen(evt2 -> {
                    frame.showScreen(new TitleScreen(onStart[0], onInstructions[0], onExit[0]));
                }));
            };

            // Show Question Screen
            QuestionScreen quiz = new QuestionScreen(manager, onStageComplete, onGameOver);
            frame.showScreen(quiz);
        });

        // Show the intro first
        frame.showScreen(intro);
    }
}
