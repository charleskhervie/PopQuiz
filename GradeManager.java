

import java.util.*;

public class GradeManager {
    private int grade = 1;
    private int lives = 5;
    private List<Question> currentQuestions = new ArrayList<>();
    private Set<Question> usedQuestionsGlobal = new HashSet<>(); // Track used questions across entire playthrough
    private Set<Question> usedQuestionsThisGrade = new HashSet<>(); // Track used questions in current grade
    private int currentIndex = 0;
    private int correctAnswers = 0;
    private List<Question> theoreticalPool;
    private List<Question> programmingPool;
    
    public GradeManager(List<Question> theoretical, List<Question> programming) {
        this.theoreticalPool = new ArrayList<>(theoretical);
        this.programmingPool = new ArrayList<>(programming);
    }
    
    private static final int[] TOTAL_PER_GRADE = {5, 10, 15, 20, 25};
    
    private static final int[][] DISTRIBUTION = {
        {3, 2}, 
        {5, 5},  
        {8, 7}, 
        {10, 10},
        {13, 12} 
    };
    
    public void startGrade() {
        currentQuestions.clear();
        usedQuestionsThisGrade.clear(); 
        currentIndex = 0;
        correctAnswers = 0;
        
        loadQuestionsForGrade();
    }
    
    private void loadQuestionsForGrade() {
        int[] dist = DISTRIBUTION[grade - 1];
        int theoryCount = dist[0];
        int progCount = dist[1];
        
        List<Question> availableTheoretical = new ArrayList<>();
        for (Question q : theoreticalPool) {
            if (!usedQuestionsGlobal.contains(q) && !usedQuestionsThisGrade.contains(q)) {
                availableTheoretical.add(q);
            }
        }
        
       
        List<Question> availableProgramming = new ArrayList<>();
        for (Question q : programmingPool) {
            if (!usedQuestionsGlobal.contains(q) && !usedQuestionsThisGrade.contains(q)) {
                availableProgramming.add(q);
            }
        }
        
        Collections.shuffle(availableTheoretical);
        Collections.shuffle(availableProgramming);
        
        int theoreticalAdded = 0;
        for (int i = 0; i < theoryCount && i < availableTheoretical.size(); i++) {
            currentQuestions.add(availableTheoretical.get(i));
            theoreticalAdded++;
        }
        
        int programmingAdded = 0;
        for (int i = 0; i < progCount && i < availableProgramming.size(); i++) {
            currentQuestions.add(availableProgramming.get(i));
            programmingAdded++;
        }
        
        if (theoreticalAdded < theoryCount || programmingAdded < progCount) {
            System.err.println("Warning: Grade " + grade + " - Expected " + theoryCount + " theoretical and " + progCount + " programming");
            System.err.println("Got " + theoreticalAdded + " theoretical and " + programmingAdded + " programming");
            System.err.println("Theoretical pool size: " + theoreticalPool.size());
            System.err.println("Programming pool size: " + programmingPool.size());
            System.err.println("Used globally: " + usedQuestionsGlobal.size());
        }
        
        Collections.shuffle(currentQuestions);
    }
    
    public Question getCurrentQuestion() {
        if (currentIndex >= currentQuestions.size()) {
            loadQuestionsForGrade();
            currentIndex = 0;
        }
        
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
            Question q = currentQuestions.get(currentIndex);
            usedQuestionsThisGrade.add(q);
            usedQuestionsGlobal.add(q); 
        }
    }
    
    public boolean hasNext() {
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