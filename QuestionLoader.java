import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class QuestionLoader {

    public static List<Question> loadQuestions(String filePath, String category) {
        List<Question> questions = new ArrayList<>();
        JSONParser parser = new JSONParser();

        try (FileReader reader = new FileReader(filePath)) {
            JSONObject jsonObject = (JSONObject) parser.parse(reader);
            JSONArray arr = (JSONArray) jsonObject.get(category);

            for (Object obj : arr) {
                JSONObject q = (JSONObject) obj;
                String text = (String) q.get("question");
                JSONArray choicesArray = (JSONArray) q.get("choices");
                String[] choices = new String[choicesArray.size()];
                String type = (String) q.get("type");

                for (int i = 0; i < choicesArray.size(); i++) {
                    choices[i] = (String) choicesArray.get(i);
                }

                long ans = (long) q.get("answer");
                questions.add(new Question(text, choices, (int) ans, type));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return questions;
    }
}