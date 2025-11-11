public class Question {
    private String question;
    private String[] choices;
    private int answer;
    private String type; // "theoretical" or "programming"

    public Question(String question, String[] choices, int answer, String type) {
        this.question = question;
        this.choices = choices;
        this.answer = answer;
        this.type = type;
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

    public String getType(){
        return type;
    }
}