public class Question {
    private String question = "";
    private String answerA = "";
    private String answerB = "";
    private String answerC = "";
    private String answerD = "";
    private String answer = "";
    private int points = 0;

    public Question() {}

    // Getters
    public String getQuestion() { return question; }
    public String getA() { return answerA; }
    public String getB() { return answerB; }
    public String getC() { return answerC; }
    public String getD() { return answerD; }
    public String getAnswer() { return answer; }
    public int getPoints() { return points; }

    // Setters
    public void setQuestion(String q) { question = q; }
    public void setA(String a) { answerA = a; }
    public void setB(String b) { answerB = b; }
    public void setC(String c) { answerC = c; }
    public void setD(String d) { answerD = d; }
    public void setAnswer(String ans) { answer = ans; }
    public void setPoints(int p) { points = p; }
}