import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Question {
    private String question = "";
    private String answerA = "";
    private String answerB = "";
    private String answerC = "";
    private String answerD = "";
    private String answer = "";
    private int points = 0;
    
    public Question(String q, String A, String B, String C, String D, String questionAnswer, int numPoints){
        super();
        question = q;
        answerA = A;
        answerB = B;
        answerC = C;
        answerD = D;
        answer = questionAnswer;
        points = numPoints;
    }
    
    public Question(){
        question = "";
        answerA = "";
        answerB = "";
        answerC = "";
        answerD = "";
        answer = "";
        points = 0;
    }
    
    public String getQuestion(){
        return question;
    }
    
    public String getA(){
        return answerA;
    }
    
    public String getB(){
        return answerB;
    }
    
    public String getC(){
        return answerC;
    }
    
    public String getD(){
        return answerD;
    }
    
    public String getAnswer(){
        return answer;
    }
    
    public int getPoints(){
        return points;
    }
    
    public void setQuestion(String quest){
        question = quest;
    }
    
    public void setA(String choiceA){
        answerA = choiceA;
    }
    
    public void setB(String choiceB){
        answerB = choiceB;
    }
    
    public void setC(String choiceC){
        answerC = choiceC;
    }
    
    public void setD(String choiceD){
        answerD = choiceD;
    }
    
    public void setAnswer(String ans){
        answer = ans;
    }
    
    public void setPoints(int numPoints){
        points = numPoints;
    }
    
}