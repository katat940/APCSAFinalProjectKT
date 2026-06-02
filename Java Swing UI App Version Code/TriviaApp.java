import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.URI;
import java.net.http.*;
import java.util.ArrayList;

public class TriviaApp extends JFrame {
    private JTextField topicField, countField;
    private JTextArea statusArea;
    private ArrayList<String> pastGames = new ArrayList<>();

    // --- CUSTOM COLOR PALETTE ---
    private final Color BACKGROUND_COLOR = new Color(19, 242, 100); 
    private final Color PANEL_COLOR = new Color(235, 9, 9);     
    private final Color TEXT_COLOR = new Color(240, 240, 240); 
    private final Color ACCENT_BLUE = new Color(75, 110, 175);     
    private final Color INPUT_BG = new Color(9, 40, 235);           
    // ----------------------------

    //UI Created with AI
    public TriviaApp() {
        setTitle("AI Trivia Master - Swing Edition");
        setSize(450, 350); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        
        getContentPane().setBackground(BACKGROUND_COLOR);

        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 8, 8));
        inputPanel.setBackground(PANEL_COLOR);
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel topicLabel = new JLabel(" Topic:");
        topicLabel.setForeground(TEXT_COLOR);
        inputPanel.add(topicLabel);
        
        topicField = new JTextField("General");
        styleTextField(topicField);
        inputPanel.add(topicField);
        
        JLabel countLabel = new JLabel(" Questions (2-10):");
        countLabel.setForeground(TEXT_COLOR);
        inputPanel.add(countLabel);
        
        countField = new JTextField("5");
        styleTextField(countField);
        inputPanel.add(countField);

        JButton runButton = new JButton("Start Game");
        styleButton(runButton);
        runButton.addActionListener(e -> startProcess());
        inputPanel.add(runButton);

        JButton reportButton = new JButton("Session Report");
        styleButton(reportButton);
        reportButton.addActionListener(e -> showReport());
        inputPanel.add(reportButton);

        add(inputPanel, BorderLayout.NORTH);

        statusArea = new JTextArea("Enter a topic and click Start!");
        statusArea.setEditable(false);
        statusArea.setBackground(INPUT_BG);
        statusArea.setForeground(TEXT_COLOR);
        statusArea.setCaretColor(TEXT_COLOR);
        statusArea.setFont(new Font("Consolas", Font.PLAIN, 13));
        statusArea.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JScrollPane scrollPane = new JScrollPane(statusArea);
        scrollPane.setBorder(BorderFactory.createLineBorder(PANEL_COLOR, 2));
        add(scrollPane, BorderLayout.CENTER);

        setLocationRelativeTo(null);
    }

    private void styleButton(JButton button) {
        button.setBackground(ACCENT_BLUE);
        button.setForeground(TEXT_COLOR);
        button.setFocusPainted(false); 
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setBorder(BorderFactory.createLineBorder(BACKGROUND_COLOR, 1));
    }

    private void styleTextField(JTextField field) {
        field.setBackground(INPUT_BG);
        field.setForeground(TEXT_COLOR);
        field.setCaretColor(TEXT_COLOR); 
        field.setFont(new Font("Arial", Font.PLAIN, 13));
        field.setBorder(BorderFactory.createLineBorder(BACKGROUND_COLOR, 1));
    }

    private void startProcess() {
        String topic = topicField.getText();
        int count;
        try {
            count = Integer.parseInt(countField.getText().trim());
        } catch (NumberFormatException nfe) {
            statusArea.setText("Error: Please enter a valid integer for question count.");
            return;
        }
        
        //if (count < 2 || count > 10){
            //statusArea.setText("Error: Enter valid question amount!");
            //return;
        //}

        statusArea.setText("Asking AI for questions...");

        //AI USED FOR EXCEPTIONS AND GAME CREATION
        new Thread(() -> {
            try {
                String aiData = fetchAIQuestions(topic, count);
                saveCSV(aiData);

                Game game = new Game(count, topic);
                game.setQuestionListFromFile("AP CSA Final Project Question Object Creation - KT - Sheet1.csv");

                // Only start the quiz if data was parsed successfully
                runQuiz(game);
            } catch (Exception ex) {
                // Displays our clean message directly to the application background panel text
                SwingUtilities.invokeLater(() -> statusArea.setText(ex.getMessage()));
            }
        }).start();
    }

    private void runQuiz(Game game) {
        int totalScore = 0;

        for (int i = 0; i < game.getNumQuestions(); i++) {
            Question q = game.getQuestionAt(i);
            Object[] options = {q.getA(), q.getB(), q.getC(), q.getD()};

            int choice = JOptionPane.showOptionDialog(this, 
                q.getQuestion(), "Question " + (i+1),
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, options, options[0]);

            String selected = switch(choice) { case 0->"A"; case 1->"B"; case 2->"C"; case 3->"D"; default->""; };

            if (selected.equals(q.getAnswer())) {
                totalScore += q.getPoints();
                JOptionPane.showMessageDialog(this, "Correct! Current Score: " + totalScore);
            } else {
                JOptionPane.showMessageDialog(this, "Wrong! It was " + q.getAnswer());
            }
        }

        String result = "Topic: " + game.getTopic() + " | Score: " + totalScore;
        pastGames.add(result);
        SwingUtilities.invokeLater(() -> statusArea.setText("Last Game: " + result));
    }

    //API CONNECTION CREATED USING AI
    private String fetchAIQuestions(String topic, int num) throws Exception {
        String apiKey = "AIzaSyDjcF3-9Fc3tXsDwGE9sZc9xr8n6lssKpI"; 
        
        // FIXED URL: Using the correct stable endpoint mapping for the active flash model
        String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=" + apiKey;
        
        String prompt = "Generate exactly " + num + " trivia questions about " + topic + ". Output ONLY raw CSV format rows. " + 
                        "Do not include markdown tags, backticks, or any markdown wrappers. Do not include headers. Do not use commas inside content fields. " + 
                        "Format template exactly: Question,A,B,C,D,CorrectLetter,Points";        
        String json = "{ \"contents\": [{ \"parts\":[{ \"text\": \"" + prompt + "\" }] }] }";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String body = response.body();
        
        // Safety check for backend issues or invalid keys
        if (body.contains("\"error\"") || !body.contains("\"text\": \"")) {
            throw new Exception("The AI-backend is overloaded. Please try again in 60 seconds.");
        }
        
        // FIXED INDEX LOOKUP: Sweeps backward from the end of the JSON body to find the true closing quote
        int start = body.indexOf("\"text\": \"") + 9;
        int end = body.lastIndexOf("}"); 
        end = body.lastIndexOf("\"", end); 
        
        if (start >= end || start < 9) {
            throw new Exception("Error: AI returned a malformed response text.");
        }
        
        String cleanData = body.substring(start, end);
        return cleanData.replace("\\n", "\n")
                         .replace("\\\"", "\"")
                         .replace("```csv", "")
                         .replace("```", "")
                         .trim();
    }

    private void saveCSV(String data) throws IOException {
        PrintWriter pw = new PrintWriter(new File("AP CSA Final Project Question Object Creation - KT - Sheet1.csv"));
        pw.println("Question,A,B,C,D,Answer,Points");
        pw.print(data);
        pw.close();
    }

    private void showReport() {
        StringBuilder sb = new StringBuilder("SESSION REPORT:\n");
        for (String s : pastGames) sb.append(s).append("\n");
        statusArea.setText(sb.toString());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TriviaApp().setVisible(true));
    }
}
