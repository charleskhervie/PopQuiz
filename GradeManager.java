

import java.util.*;

public class GradeManager {
    private int grade = 1;
    private int lives = 5;
    private List<Question> currentQuestions = new ArrayList<>();
    private Set<Question> usedQuestions = new HashSet<>(); // Track used questions
    private int currentIndex = 0;
    private int correctAnswers = 0;
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
        correctAnswers = 0;
        
        int[] dist = DISTRIBUTION[grade - 1];
        int theoryCount = dist[0];
        int progCount = dist[1];
        
        // Get unused theoretical questions
        List<Question> availableTheoretical = new ArrayList<>();
        for (Question q : theoreticalPool) {
            if (!usedQuestions.contains(q)) {
                availableTheoretical.add(q);
            }
        }
        
        // Get unused programming questions
        List<Question> availableProgramming = new ArrayList<>();
        for (Question q : programmingPool) {
            if (!usedQuestions.contains(q)) {
                availableProgramming.add(q);
            }
        }
        
        Collections.shuffle(availableTheoretical);
        Collections.shuffle(availableProgramming);
        
        // Add theoretical questions
        for (int i = 0; i < theoryCount && i < availableTheoretical.size(); i++) {
            currentQuestions.add(availableTheoretical.get(i));
        }
        
        // Add programming questions
        for (int i = 0; i < progCount && i < availableProgramming.size(); i++) {
            currentQuestions.add(availableProgramming.get(i));
        }
        
        Collections.shuffle(currentQuestions);
        
        if (currentQuestions.isEmpty()) {
            System.err.println("Warning: No unused questions available for grade " + grade);
        }
    }
    
    public Question getCurrentQuestion() {
        if (currentIndex < currentQuestions.size()) {
            return currentQuestions.get(currentIndex);
        }
        return null;
    }
    
    public void nextQuestion() {
        currentIndex++;
    }
    
    public void markQuestionAsUsed() {
        if (currentIndex < currentQuestions.size()) {
            usedQuestions.add(currentQuestions.get(currentIndex));
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
        return TOTAL_PER_GRADE[grade - 1];
    }
    
    public int getProgress() {
        return (int) (((double) correctAnswers / TOTAL_PER_GRADE[grade - 1]) * 100);
    }
}