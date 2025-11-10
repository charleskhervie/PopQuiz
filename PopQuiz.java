

import javax.swing.*;
import java.awt.event.*;

public class PopQuiz {
    private static GradeManager globalManager = null;
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GameFrame frame = new GameFrame();
            final ActionListener[] onStart = new ActionListener[1];
            final ActionListener[] onInstructions = new ActionListener[1];
            final ActionListener[] onExit = new ActionListener[1];
            
            onExit[0] = e -> System.exit(0);
            
            onStart[0] = e -> {
                var theoretical = QuestionLoader.loadQuestions("questions.json", "theoretical");
                var programming = QuestionLoader.loadQuestions("questions.json", "programming");
                globalManager = new GradeManager(theoretical, programming);
                
                loadGrade(1, frame, onStart, onInstructions, onExit);
            };
            
            onInstructions[0] = e -> {
                InstructionsScreen instructions = new InstructionsScreen(evt -> {
                    frame.showScreen(new TitleScreen(onStart[0], onInstructions[0], onExit[0]));
                });
                frame.showScreen(instructions);
            };
            
            TitleScreen titleScreen = new TitleScreen(onStart[0], onInstructions[0], onExit[0]);
            frame.showScreen(titleScreen);
            
            frame.setVisible(true);
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