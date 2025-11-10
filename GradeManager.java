// GradeManager.java
import java.util.*;

public class GradeManager {
    private int grade = 1;
    private int lives = 5;
    private List<Question> currentQuestions = new ArrayList<>();
    private int currentIndex = 0;
    private Random rand = new Random();

    private List<Question> theoreticalPool;
    private List<Question> programmingPool;

    public GradeManager(List<Question> theoretical, List<Question> programming) {
        this.theoreticalPool = new ArrayList<>(theoretical);
        this.programmingPool = new ArrayList<>(programming);
    }

    // Number of questions per grade
    private static final int[] TOTAL_PER_GRADE = {5, 10, 15, 20, 25};

    // Theoretical/Programming distribution per grade
    private static final int[][] DISTRIBUTION = {
        {3, 2},  // G1
        {5, 5},  // G2
        {8, 7},  // G3
        {10, 10},// G4
        {13, 12} // G5
    };

    public void startGrade() {
        currentQuestions.clear();
        currentIndex = 0;

        int[] dist = DISTRIBUTION[grade - 1];
        int theoryCount = dist[0];
        int progCount = dist[1];

        Collections.shuffle(theoreticalPool);
        Collections.shuffle(programmingPool);

        for (int i = 0; i < theoryCount && i < theoreticalPool.size(); i++) {
            currentQuestions.add(theoreticalPool.get(i));
        }
        for (int i = 0; i < progCount && i < programmingPool.size(); i++) {
            currentQuestions.add(programmingPool.get(i));
        }

        Collections.shuffle(currentQuestions);
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

    public boolean hasNext() {
        return currentIndex < currentQuestions.size();
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

    // Add these methods to GradeManager class:

    public int getCurrentIndex() {
        return currentIndex;
    }

    public int getTotalQuestions() {
        return currentQuestions.size();
    }

    public int getProgress() {
        return (int) (((double) currentIndex / currentQuestions.size()) * 100);
    }
}
