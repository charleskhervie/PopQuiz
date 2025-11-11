import java.awt.SplashScreen;
import java.awt.event.ActionListener;

import javax.swing.*;

public class PopQuiz {
    private static GradeManager globalManager = null;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Access the splash screen (shown by JVM)
            SplashScreen splash = SplashScreen.getSplashScreen();

            // Load resources in the background
            new SwingWorker<GradeManager, Void>() {
                @Override
                protected GradeManager doInBackground() throws Exception {
                    // Load your questions here
                    var theoretical = QuestionLoader.loadQuestions("questions.json", "theoretical");
                    var programming = QuestionLoader.loadQuestions("questions.json", "programming");
                    return new GradeManager(theoretical, programming);
                }

                @Override
                protected void done() {
                    try {
                        globalManager = get();
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.exit(1);
                    }

                    // Close splash manually
                    if (splash != null) {
                        splash.close();
                    }

                    // Start your main game frame
                    GameFrame frame = new GameFrame();

                    final ActionListener[] onStart = new ActionListener[1];
                    final ActionListener[] onInstructions = new ActionListener[1];
                    final ActionListener[] onExit = new ActionListener[1];

                    onExit[0] = e -> System.exit(0);

                    onStart[0] = e -> loadGrade(1, frame, onStart, onInstructions, onExit);

                    onInstructions[0] = e -> {
                        InstructionsScreen instructions = new InstructionsScreen(evt -> {
                            frame.showScreen(new TitleScreen(onStart[0], onInstructions[0], onExit[0]));
                        });
                        frame.showScreen(instructions);
                    };

                    TitleScreen titleScreen = new TitleScreen(onStart[0], onInstructions[0], onExit[0]);
                    frame.showScreen(titleScreen);
                    frame.setVisible(true);
                }
            }.execute();
        });
    }

    private static void loadGrade(int grade, GameFrame frame,
                                  ActionListener[] onStart, ActionListener[] onInstructions, ActionListener[] onExit) {
        StageIntroScreen intro = new StageIntroScreen(grade, evt -> {
            globalManager.startGrade();

            Runnable onStageComplete = () -> {
                if (grade == 5) {
                    frame.showScreen(new GameVictoryScreen(evt2 -> {
                        globalManager = null;
                        frame.showScreen(new TitleScreen(onStart[0], onInstructions[0], onExit[0]));
                    }));
                } else {
                    globalManager.nextGrade();
                    loadGrade(grade + 1, frame, onStart, onInstructions, onExit);
                }
            };

            Runnable onGameOver = () -> {
                frame.showScreen(new GameOverScreen(evt2 -> {
                    globalManager = null;
                    frame.showScreen(new TitleScreen(onStart[0], onInstructions[0], onExit[0]));
                }));
            };

            QuestionScreen quiz = new QuestionScreen(globalManager, onStageComplete, onGameOver);
            frame.showScreen(quiz);
        });

        frame.showScreen(intro);
    }
}
