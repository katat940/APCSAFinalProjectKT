import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Game{
    private int numQuestions = 0;
    private int score = 0;
    private String topic;
    private Question[] questionList;
    private String playerName = "";
    
    public Game(int numberOfQuestions, String gameTopic){
        numQuestions = numberOfQuestions;
        topic = gameTopic;
        questionList = new Question[numQuestions];
        
        for(int i = 0; i < numQuestions; i++){
            questionList[i] = new Question();
        }
    }
    
    public Game(){
        numQuestions = 2;
        topic = "General";
    }
    
    public void setQuestionListFromFile(String pathName) throws FileNotFoundException{
        File file = new File(pathName);
        Scanner scanner = new Scanner(file);
        if (scanner.hasNextLine()){
            scanner.nextLine();
        }
        int j = 0;
        while (scanner.hasNextLine() && j < numQuestions){
            String row = scanner.nextLine();
            String[] values = row.split(",");
            if (values.length >= 7){
                questionList[j].setQuestion(values[0]);
                questionList[j].setA(values[1]);
                questionList[j].setB(values[2]);
                questionList[j].setC(values[3]);
                questionList[j].setD(values[4]);
                questionList[j].setAnswer(values[5]);
                int pointValue = Integer.parseInt(values[6].trim());
                questionList[j].setPoints(pointValue);
            }
            j++;
        }
        scanner.close();
    }
    
    public void playGame(ArrayList<String> pastGameList){
        Scanner input = new Scanner(System.in);
        for (Question question : questionList){
            System.out.println("Question: " + question.getQuestion());
            System.out.println("A) " + question.getA());
            System.out.println("B) " + question.getB());
            System.out.println("C) " + question.getC());
            System.out.println("D) " + question.getD());
            System.out.print("Answer (only respond with the capital letter): ");
            String answer = input.next();
            System.out.println("");
            
            if (answer.equals(question.getAnswer())){
                System.out.println("Correct!");
                score += question.getPoints();
                System.out.println("Score: " + score);
            }
            
            else {
                System.out.println("Incorrect!");
                System.out.println("Score: " + score);
            }
        }
        System.out.println("Final Score: " + score);
        pastGameList.add("Topic: " + topic + "; Number of Questions: " + numQuestions + "; Score: " + score + ";");
    }
}