import java.util.Scanner;
import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.io.IOException;
import java.io.PrintWriter;


public class Main
{
    public static void AIquestionsToFile(String fileName, String AIString) throws FileNotFoundException{
        File file = new File(fileName);
        PrintWriter writer = new PrintWriter(file);
        writer.println("Question,Answer Choice A,Answer Choice B,Answer Choice C,Answer Choice D,Answer,Points");
        writer.print(AIString);
        writer.close();
    }
    //API Connection Created Using AI
    public static String getAIQuestions(String topic, int numberOfQuestions) throws IOException, InterruptedException {
        String apiKey = "AIzaSyDjcF3-9Fc3tXsDwGE9sZc9xr8n6lssKpI";
        String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=" + apiKey;
        String AIPrompt = "Generate " + numberOfQuestions + " trivia questions about " + topic + ". Output ONLY raw CSV data with 7 columns: Question, A, B, C, D, CorrectLetter, Points. " +
                    "Do not include a header row or markdown. Ensure that the question itself does not contain commas.";
        String jsonBody = "{ \"contents\": [{ \"parts\":[{ \"text\": \"" + AIPrompt + "\" }] }] }";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).header("Content-Type", "application/json").POST(BodyPublishers.ofString(jsonBody)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String body = response.body();
        
        if (body.contains("\"error\"")){
            return "Error: AI Overloaded! Try again in 1 minute";
        }
        
        String target = "\"text\": \"";
        
        int start = body.indexOf(target);
        if (start == -1){
            return "";
        }
        start += target.length();
        
        int end = body.indexOf("\"", start);
        if (end == -1){
            return "";
        }
        String result = body.substring(start, end);
        return result.replace("\\n", "\n")
             .replace("\\\"", "\"")
             .replace("```csv", "")
             .replace("```", "")
             .replace("`", "") 
             .trim();
    }
    
	public static void main(String[] args) throws FileNotFoundException, IOException, InterruptedException {
		ArrayList<String> pastGames = new ArrayList<String>();
		
		Scanner userInput = new Scanner(System.in);
		System.out.print("Do you want to play a game? Y/N: ");
		String userAnswer = userInput.nextLine();
		System.out.println("");
		
		while (userAnswer.equalsIgnoreCase("Y")){
		    System.out.print("How many questions? (Enter as integer): ");
		    int questionAmount = userInput.nextInt();
		    userInput.nextLine();
		    while (questionAmount < 2 || questionAmount > 10){
		        if (questionAmount > 10){
    		        System.out.println("You cannot have more than 10 questions!");
    		        System.out.print("How many questions? (Enter as integer): ");
    		        questionAmount = userInput.nextInt();
    		        userInput.nextLine();
		        }
    		    else if (questionAmount < 2){
    		        System.out.println("You must have at least 2 questions!");
    		        System.out.print("How many questions? (Enter as integer): ");
    		        questionAmount = userInput.nextInt();
    		        userInput.nextLine();
    		    }
		    }

			//AI Response filtering created with AI
		    System.out.print("Topic: ");
		    String topic = userInput.nextLine();
		    String aiResponse = getAIQuestions(topic, questionAmount);
		    if (!aiResponse.isEmpty() && !aiResponse.startsWith("Error")){
    		    AIquestionsToFile("AP CSA Final Project Question Object Creation - KT - Sheet1.csv", aiResponse);
    		    Game game = new Game(questionAmount, topic);
    		    game.setQuestionListFromFile("AP CSA Final Project Question Object Creation - KT - Sheet1.csv");
    		    game.playGame(pastGames);
		    }
		    
		    else {
		        System.out.println("Error: AI response failed!");
		    }
		    
		    System.out.print("Do you want to play again? Y/N: ");
		    userAnswer = userInput.nextLine();
		}
		userInput.close();
		System.out.println("-----SESSION REPORT-----");
		for (String gameReport : pastGames){
		    System.out.println(gameReport);
		}
	}
}
