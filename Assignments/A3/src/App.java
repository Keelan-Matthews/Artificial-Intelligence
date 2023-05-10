import java.util.ArrayList;

public class App {
    public static void main(String[] args) throws Exception {

        ArrayList<String> file = FileHandler.readFile("breast-cancer.data");
        ArrayList<double[]> encoded = Encoder.encodeFile(file);
        
        // Use this data to train a new neural network
        NeuralNetwork nn = new NeuralNetwork(8, 32, 0.1);

        // Train the neural network
        nn.train(encoded, 20000);

        ArrayList<String> testFile = new ArrayList<>();
        testFile.add("no-recurrence-events,60-69,ge40,30-34,0-2,no,2,left,left_low,yes");
        testFile.add("no-recurrence-events,30-39,premeno,20-24,0-2,no,3,left,central,no");
        testFile.add("recurrence-events,40-49,premeno,25-29,0-2,no,3,left,left_up,no");

        ArrayList<double[]> encodedTestFile = Encoder.encodeFile(testFile);

        // Test the neural network
        System.out.println(nn.predict(encodedTestFile.get(0))[0]);
        System.out.println(nn.predict(encodedTestFile.get(1))[0]);
        System.out.println(nn.predict(encodedTestFile.get(2))[0]);
    }
}
