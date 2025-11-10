

import java.util.*;

public class GradeManager {
    private int grade = 1;
    private int lives = 5;
    private List<Question> currentQuestions = new ArrayList<>();
    private int currentIndex = 0;
    private int correctAnswers = 0; // Track correct answers
    private Random rand = new Random();
    private List<Question> theoreticalPool;
    private List<Question> programmingPool;
    
    public GradeManager(List<Question> theoretical, List<Question> programming) {
        this.theoreticalPool = new ArrayList<>(theoretical);
        this.programmingPool = new ArrayList<>(programming);
    }
    
    // Number of CORRECT answers required per grade
    private static final int[] TOTAL_PER_GRADE = {5, 10, 15, 20, 25};
    
    // Theoretical/Programming distribution per grade
    private static final int[][] DISTRIBUTION = {
        {3, 2},  // G1: 3 theoretical, 2 programming = 5 total
        {5, 5},  // G2: 5 theoretical, 5 programming = 10 total
        {8, 7},  // G3: 8 theoretical, 7 programming = 15 total
        {10, 10},// G4: 10 theoretical, 10 programming = 20 total
        {13, 12} // G5: 13 theoretical, 12 programming = 25 total
    };
    
    public void startGrade() {
        currentQuestions.clear();
        currentIndex = 0;
        correctAnswers = 0; // Reset correct answers for new grade
        
        int[] dist = DISTRIBUTION[grade - 1];
        int theoryCount = dist[0];
        int progCount = dist[1];
        
        Collections.shuffle(theoreticalPool);
        Collections.shuffle(programmingPool);
        
        // Add theoretical questions
        for (int i = 0; i < theoryCount && i < theoreticalPool.size(); i++) {
            currentQuestions.add(theoreticalPool.get(i));
        }
        
        // Add programming questions
        for (int i = 0; i < progCount && i < programmingPool.size(); i++) {
            currentQuestions.add(programmingPool.get(i));
        }
        
        Collections.shuffle(currentQuestions);
    }
    
    public Question getCurrentQuestion() {
        if (currentIndex < currentQuestions.size()) {
            return currentQuestions.get(currentIndex);
        }
        // If we run out of questions, reshuffle and continue
        if (correctAnswers < TOTAL_PER_GRADE[grade - 1]) {
            Collections.shuffle(currentQuestions);
            currentIndex = 0;
            return currentQuestions.get(currentIndex);
        }
        return null;
    }
    
    public void nextQuestion() {
        currentIndex++;
        // Wrap around if we've gone through all questions but haven't reached the required correct answers
        if (currentIndex >= currentQuestions.size() && correctAnswers < TOTAL_PER_GRADE[grade - 1]) {
            Collections.shuffle(currentQuestions);
            currentIndex = 0;
        }
    }
    
    public boolean hasNext() {
        // Continue until we have enough correct answers
        return correctAnswers < TOTAL_PER_GRADE[grade - 1];
    }
    
    public void recordCorrectAnswer() {
        correctAnswers++;
    }
    
    public int getCorrectAnswers() {
        return correctAnswers;
    }
    
    public int getRequiredCorrect() {
        return TOTAL_PER_GRADE[grade - 1];
    }
    
    public int getLives() {
        return lives;
    }
    
    public void loseLife() {
        lives--;
    }
    
    public int getGrade() {
        return grade;
    }
    
    public void nextGrade() {
        grade++;
    }
    
    public boolean isGameOver() {
        return lives <= 0;
    }
    
    public boolean isGameFinished() {
        return grade > 5;
    }
    
    public int getCurrentIndex() {
        return currentIndex;
    }
    
    public int getTotalQuestions() {
        return TOTAL_PER_GRADE[grade - 1]; // Return required correct answers
    }
    
    public int getProgress() {
        return (int) (((double) correctAnswers / TOTAL_PER_GRADE[grade - 1]) * 100);
    }
}