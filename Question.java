

import java.util.Arrays;
import java.util.Objects;

public class Question {
    private String type;
    private String question;
    private String[] choices;
    private int answer;

    public Question(String type, String question, String[] choices, int answer) {
        this.type = type;
        this.question = question;
        this.choices = choices;
        this.answer = answer;
    }

    public String getType() {
        return type;
    }

    public String getQuestion() {
        return question;
    }

    public String[] getChoices() {
        return choices;
    }

    public int getAnswer() {
        return answer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Question question1 = (Question) o;
        return answer == question1.answer &&
               Objects.equals(type, question1.type) &&
               Objects.equals(question, question1.question) &&
               Arrays.equals(choices, question1.choices);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(type, question, answer);
        result = 31 * result + Arrays.hashCode(choices);
        return result;
    }
}