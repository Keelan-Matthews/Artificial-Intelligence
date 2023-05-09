import java.util.Random;

public class NeuralNetwork {
    private int inputSize; // The number of input neurons
    private int hiddenSize; // The number of hidden neurons
    private int outputSize; // The number of output neurons
    private double learningRate; // The rate at which the weights are updated during training
    private double[][] weightsInputHidden; // The weights between the input and hidden layers
    private double[] weightsHiddenOutput; // The weights between the hidden and output layers
    private long seed; // The seed for the random number generator
    private Random random; // The random number generator

    /**
     * Constructor for the NeuralNetwork class
     * @param inputSize
     * @param hiddenSize
     * @param outputSize
     * @param learningRate
     */
    public NeuralNetwork(int inputSize, int hiddenSize, int outputSize, double learningRate) {
        this.inputSize = inputSize;
        this.hiddenSize = hiddenSize;
        this.outputSize = outputSize;
        this.learningRate = learningRate;

        this.weightsInputHidden = new double[inputSize][hiddenSize];
        this.weightsHiddenOutput = new double[hiddenSize];
        this.random = GenerateRandomWithSeed(seed);

        initializeWeights();
    }

    /**
     * Function to generate a random number generator with a seed
     * @param seed
     * @return
     */
    private Random GenerateRandomWithSeed(long seed) {
        if (seed == 0) {
            return new Random();
        } else {
            return new Random(seed);
        }
    }

    /**
     * Function to initialize the weights of the neural network
     */
    private void initializeWeights() {
        // Initialize the weights between the input and hidden layers
        for (int i = 0; i < inputSize; i++) {
            for (int j = 0; j < hiddenSize; j++) {
                // Generate a random number between 0 and 0.1 because the weights need to be small
                weightsInputHidden[i][j] = random.nextDouble() * 0.1;
            }
        }

        // Initialize the weights between the hidden and output layers
        for (int i = 0; i < hiddenSize; i++) {
            weightsHiddenOutput[i] = random.nextDouble() * 0.1;
        }
    }

    /**
     * Function to train the neural network,
     * maps the output to a probability between 0 and 1
     * @param input
     * @param target
     */
    private double sigmoid(double x) {
        return 1 / (1 + Math.exp(-x));
    }

    /**
     * The activation function for the hidden layer
     * @param x
     * @return
     */
    private double relu(double x) {
        return Math.max(0, x);
    }

    public double[] predict(double[] input) {
        double[] hidden = new double[hiddenSize];

        // Calculate the output of the hidden layer neurons
        for (int i = 0; i < hiddenSize; i++) {
            double sum = 0;

            // Multiply the input by its corresponding weight and add it to the sum,
            // do this for all the hidden neurons' inputs
            for (int j = 0; j < inputSize; j++) {
                sum += input[j] * weightsInputHidden[j][i];
            }

            // Apply the activation function to each hidden neuron's sum
            hidden[i] = relu(sum);
        }

        double output = 0;

        for (int i = 0; i < hiddenSize; i++) {
            output += hidden[i] * weightsHiddenOutput[i];
        }

        output = sigmoid(output);

        return new double[]{output};
    }
}
