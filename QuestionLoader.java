

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class QuestionLoader {
    public static List<Question> loadQuestions(String filename, String category) {
        List<Question> questions = new ArrayList<>();
        JSONParser parser = new JSONParser();
        
        try {
            JSONObject root = (JSONObject) parser.parse(new FileReader(filename));
            JSONArray arr = (JSONArray) root.get(category);
            
            if (arr == null) {
                System.err.println("Category not found: " + category);
                return questions;
            }
            
            for (Object obj : arr) {
                JSONObject q = (JSONObject) obj;
                String type = (String) q.get("type");
                String text = (String) q.get("question");
                JSONArray choicesArr = (JSONArray) q.get("choices");
                String[] choices = new String[choicesArr.size()];
                for (int i = 0; i < choicesArr.size(); i++) {
                    choices[i] = (String) choicesArr.get(i);
                }
                long ans = (long) q.get("answer");
                // Correct order: type, question, choices, answer
                questions.add(new Question(type, text, choices, (int) ans));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return questions;
    }
}