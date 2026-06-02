import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class Game {
    private int numQuestions;
    private String topic;
    private Question[] questionList;

    public Game(int numberOfQuestions, String gameTopic) {
        this.numQuestions = numberOfQuestions;
        this.topic = gameTopic;
        this.questionList = new Question[numQuestions];
        for (int i = 0; i < numQuestions; i++) {
            questionList[i] = new Question();
        }
    }

    public void setQuestionListFromFile(String pathName) throws FileNotFoundException {
        File file = new File(pathName);
        Scanner scanner = new Scanner(file);
        if (scanner.hasNextLine()) scanner.nextLine(); // Skip header

        int j = 0;
        while (scanner.hasNextLine() && j < numQuestions) {
            String row = scanner.nextLine();
            
            if (row.startsWith("Question") || row.trim().isEmpty()){
                continue;
            }
            
            String[] values = row.split(",");
            if (values.length >= 7) {
                questionList[j].setQuestion(values[0]);
                questionList[j].setA(values[1]);
                questionList[j].setB(values[2]);
                questionList[j].setC(values[3]);
                questionList[j].setD(values[4]);
                questionList[j].setAnswer(values[5]);
                questionList[j].setPoints(Integer.parseInt(values[6].trim()));
            }
            j++;
        }
        scanner.close();
    }

    public int getNumQuestions() { return numQuestions; }
    public String getTopic() { return topic; }
    public Question getQuestionAt(int index) { return questionList[index]; }
}