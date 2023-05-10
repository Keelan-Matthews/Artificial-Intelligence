public class App {
    public static void main(String[] args) throws Exception {
        NeuralNetwork neuralNetwork = new NeuralNetwork(2, 3, 0.1);

        // Train the neural network
        neuralNetwork.train(new double[][] { { 0, 0 }, { 0, 1 }, { 1, 0 }, { 1, 1 } },
                new double[][] { { 0 }, { 1 }, { 1 }, { 0 } }, 10000);

        // Test the neural network
        System.out.println("0, 0: " + neuralNetwork.predict(new double[] { 0, 0 })[0]); 
        System.out.println("0, 1: " + neuralNetwork.predict(new double[] { 0, 1 })[0]);
        System.out.println("1, 0: " + neuralNetwork.predict(new double[] { 1, 0 })[0]);
        System.out.println("1, 1: " + neuralNetwork.predict(new double[] { 1, 1 })[0]);
    }
}
