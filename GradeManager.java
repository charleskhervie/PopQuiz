

import java.util.*;

public class GradeManager {
    private int grade = 1;
    private int lives = 5;
    private List<Question> currentQuestions = new ArrayList<>();
    private List<Integer> usedIndicesGlobal = new ArrayList<>();
    private List<Integer> usedIndicesThisGrade = new ArrayList<>();
    private int currentIndex = 0;
    private int correctAnswers = 0;
    private Random rand = new Random();
    private List<Question> theoreticalPool;
    private List<Question> programmingPool;
    private List<Question> allQuestions;
    
    public GradeManager(List<Question> theoretical, List<Question> programming) {
        this.theoreticalPool = new ArrayList<>(theoretical);
        this.programmingPool = new ArrayList<>(programming);
        
        this.allQuestions = new ArrayList<>();
        this.allQuestions.addAll(theoretical);
        this.allQuestions.addAll(programming);
        
        System.out.println("Total questions loaded: " + allQuestions.size());
        System.out.println("Theoretical: " + theoretical.size());
        System.out.println("Programming: " + programming.size());
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
        usedIndicesThisGrade.clear();
        currentIndex = 0;
        correctAnswers = 0;
        
        System.out.println("\n=== Starting Grade " + grade + " ===");
        System.out.println("Questions used so far in playthrough: " + usedIndicesGlobal.size());
        
        loadQuestionsForGrade();
    }
    
    private void loadQuestionsForGrade() {
        currentQuestions.clear();
        
        int[] dist = DISTRIBUTION[grade - 1];
        int theoryCount = dist[0];
        int progCount = dist[1];
        
        List<Integer> availableTheoryIndices = new ArrayList<>();
        for (int i = 0; i < theoreticalPool.size(); i++) {
            if (!usedIndicesGlobal.contains(i) && !usedIndicesThisGrade.contains(i)) {
                availableTheoryIndices.add(i);
            }
        }
        
        List<Integer> availableProgIndices = new ArrayList<>();
        for (int i = 0; i < programmingPool.size(); i++) {
            int globalIndex = theoreticalPool.size() + i;
            if (!usedIndicesGlobal.contains(globalIndex) && !usedIndicesThisGrade.contains(globalIndex)) {
                availableProgIndices.add(globalIndex);
            }
        }
        
        System.out.println("Available theoretical questions: " + availableTheoryIndices.size());
        System.out.println("Available programming questions: " + availableProgIndices.size());
        System.out.println("Need to load: " + theoryCount + " theoretical, " + progCount + " programming");
        
        Collections.shuffle(availableTheoryIndices);
        Collections.shuffle(availableProgIndices);
        
        int theoryAdded = 0;
        for (int i = 0; i < theoryCount && i < availableTheoryIndices.size(); i++) {
            int index = availableTheoryIndices.get(i);
            currentQuestions.add(theoreticalPool.get(index));
            System.out.println("Added theoretical question at index: " + index);
            theoryAdded++;
        }
        
        int progAdded = 0;
        for (int i = 0; i < progCount && i < availableProgIndices.size(); i++) {
            int globalIndex = availableProgIndices.get(i);
            int progIndex = globalIndex - theoreticalPool.size();
            currentQuestions.add(programmingPool.get(progIndex));
            System.out.println("Added programming question at global index: " + globalIndex);
            progAdded++;
        }
        
        Collections.shuffle(currentQuestions);
        System.out.println("Loaded " + currentQuestions.size() + " questions for this batch (" + theoryAdded + " theory + " + progAdded + " prog)");
    }
    
    public Question getCurrentQuestion() {
        if (currentIndex >= currentQuestions.size()) {
            loadQuestionsForGrade();
            currentIndex = 0;
        }
        
        if (currentIndex < currentQuestions.size()) {
            Question q = currentQuestions.get(currentIndex);
            System.out.println("Showing question " + (currentIndex + 1) + " of " + currentQuestions.size() + " in current batch");
            return q;
        }
        
        return null;
    }
    
    public void nextQuestion() {
        currentIndex++;
    }
    
    public void markQuestionAsUsed() {
        if (currentIndex < currentQuestions.size()) {
            Question q = currentQuestions.get(currentIndex);
            
            int globalIndex = allQuestions.indexOf(q);
            
            if (globalIndex != -1) {
                if (!usedIndicesGlobal.contains(globalIndex)) {
                    usedIndicesGlobal.add(globalIndex);
                    System.out.println(" Marked question " + globalIndex + " as used globally. Total used: " + usedIndicesGlobal.size());
                } else {
                    System.out.println("!!!!!WARNING this lalalla Question " + globalIndex + " was already used globally");
                }
                
                if (!usedIndicesThisGrade.contains(globalIndex)) {
                    usedIndicesThisGrade.add(globalIndex);
                }
            } else {
                System.err.println("ERROR: Could not find question in allQuestions pool!");
            }
        }
    }
    
    public boolean hasNext() {
        boolean shouldContinue = correctAnswers < TOTAL_PER_GRADE[grade - 1];
        if (!shouldContinue) {
            System.out.println("Grade " + grade + " complete! Got " + correctAnswers + " correct answers.");
        }
        return shouldContinue;
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