import java.util.ArrayList;
import java.util.Random;

public class NeuralNetwork {
    private int inputSize; // The number of input neurons
    private int hiddenSize; // The number of hidden neurons
    private double learningRate; // The rate at which the weights are updated during training
    private double[][] weightsInputHidden; // The weights between the input and hidden layers
    private double[] weightsHiddenOutput; // The weights between the hidden and output layers
    private long seed = 0; // The seed for the random number generator
    private Random random; // The random number generator

    /**
     * Constructor for the NeuralNetwork class
     * 
     * @param inputSize
     * @param hiddenSize
     * @param learningRate
     */
    public NeuralNetwork(int inputSize, int hiddenSize, double learningRate) {
        this.inputSize = inputSize;
        this.hiddenSize = hiddenSize;
        this.learningRate = learningRate;

        this.weightsInputHidden = new double[inputSize][hiddenSize];
        this.weightsHiddenOutput = new double[hiddenSize];
        this.random = new Random(seed);

        initializeWeights();
    }

    /**
     * Function to initialize the weights of the neural network
     */
    private void initializeWeights() {
        // Initialize the weights between the input and hidden layers
        for (int i = 0; i < inputSize; i++) {
            for (int j = 0; j < hiddenSize; j++) {
                // Generate a random number between 0 and 0.1 because the weights need to be
                // small
                weightsInputHidden[i][j] = random.nextDouble() * 0.1;
            }
        }

        // Initialize the weights between the hidden and output layers
        for (int i = 0; i < hiddenSize; i++) {
            weightsHiddenOutput[i] = random.nextDouble() * 0.1;
        }
    }

    /**
     * This function predicts the output of the neural network by multiplying the
     * input by the weights,
     * and applying the activation function to the result
     * 
     * @param input
     * @return
     */
    public double predict(double[] inputList) {
        // Remove the class attribute from the input if it exists
        double[] input = trimClassAttribtue(inputList);

        // Calculate the output of the hidden layer neurons
        double[] hidden = CalcHiddenLayerOutput(input);
        // Calculate the output of the neural network
        double output = CalcOutput(hidden);

        return output;
    }

    // Function to train the neural network
    public void train(ArrayList<double[]> inputsList, int epochs) {
        int epochsWithoutImprovement = 0;
        double bestValidationError = Double.MAX_VALUE;
        double tolerance = 0.01;

        // Convert the array list into a 2D array
        double[][] inputs = ListToArray(inputsList);

        // Train the neural network for the specified number of epochs
        for (int epoch = 0; epoch < epochs; epoch++) {
            double error = 0.0; // The total error for the epoch

            for (int i = 0; i < inputs.length; i++) {
                // The label is input[0], extract that and remove it from the input array
                double label = inputs[i][0];
                double[] input = trimClassAttribtue(inputs[i]);

                // Backpropagation
                double delta = backpropagation(input, label);

                // Add the error to the total error for the epoch and square it
                // The error is squared to make sure it is positive
                error += Math.pow(delta, 2);
            }

            // Determine if the error is within the tolerance
            if (error + tolerance < bestValidationError) {
                bestValidationError = error;
                epochsWithoutImprovement = 0;
            } else {
                epochsWithoutImprovement++;
            }

            // Stop training if validation error has not improved for 10 epochs
            if (epochsWithoutImprovement == 50) {
                break;
            }

            System.out.println("Epoch " + (epoch + 1) + " error: " + error);
        }
    }

    /**
     * Function to train the neural network using backpropagation
     * 
     * @param input
     * @param target
     * @return
     */
    public double backpropagation(double[] input, double label) {
        // Calculate the output of the hidden layer neurons
        double[] hidden = CalcHiddenLayerOutput(input);
        // Calculate the output of the neural network
        double output = CalcOutput(hidden);

        // Calculate the error of the neural network by subtracting the expected
        // output from the actual output
        double delta = output - label;

        // Calculate the error of the output layer
        double outputError = delta * sigmoidDerivative(output);

        // Calculate the error of the hidden layer
        double[] hiddenError = new double[hiddenSize];
        for (int j = 0; j < hiddenSize; j++) {
            hiddenError[j] = outputError * weightsHiddenOutput[j] * sigmoidDerivative(hidden[j]);
        }

        // Update the weights between the hidden and output layers
        for (int j = 0; j < hiddenSize; j++) {
            weightsHiddenOutput[j] -= hidden[j] * outputError * learningRate;
        }

        // Update the weights between the input and hidden layers
        for (int j = 0; j < inputSize; j++) {
            for (int k = 0; k < hiddenSize; k++) {
                weightsInputHidden[j][k] -= input[j] * hiddenError[k] * learningRate;
            }
        }

        return delta;
    }

    /**
     * Function to multiply the input by the weights and apply the activation
     * for the hidden layer
     * 
     * @param input
     * @return
     */
    public double[] CalcHiddenLayerOutput(double[] input) {
        double[] hidden = new double[hiddenSize];

        // Calculate the output of the hidden layer neurons
        for (int i = 0; i < hiddenSize; i++) {
            double sum = 0;

            // Multiply the input by its corresponding weight and add it to the sum,
            // do this for all the hidden neurons' inputs
            for (int j = 0; j < inputSize; j++) {
                sum += input[j] * weightsInputHidden[j][i];
            }

            // Apply the activation function to each hidden neuron's sum,
            // to simulate the neuron firing or not based on the input
            hidden[i] = relu(sum);
        }

        return hidden;
    }

    /**
     * Function to calculate the output of the neural network by multiplying the
     * hidden layer outputs by the weights between the hidden and output layers,
     * and applying the sigmoid activation function to the result
     * 
     * @param hidden
     * @return
     */
    public double CalcOutput(double[] hidden) {
        double output = 0;

        // Multiply the hidden layer outputs by the weights between the hidden and
        // output layers,
        // and sum them, resulting in a single output value
        for (int i = 0; i < hiddenSize; i++) {
            output += hidden[i] * weightsHiddenOutput[i];
        }

        // Apply the sigmoid activation function to the output to map it to a
        // probability between 0 and 1
        output = sigmoid(output);

        return output;
    }

    // Function to convert an array list to a 2D array
    public double[][] ListToArray(ArrayList<double[]> list) {
        double[][] array = new double[list.size()][];
        for (int i = 0; i < list.size(); i++) {
            array[i] = list.get(i);
        }
        return array;
    }

    // Function to remove the class attribute from the input
    public double[] trimClassAttribtue(double[] input) {
        if (input.length == 8) {
            return input;
        }

        double[] trimmed = new double[input.length - 1];
        for (int i = 1; i < input.length; i++) {
            trimmed[i - 1] = input[i];
        }
        return trimmed;
    }

    /**
     * Function to train the neural network,
     * maps the output to a probability between 0 and 1
     * 
     * @param input
     * @param target
     */
    private double sigmoid(double x) {
        return 1 / (1 + Math.exp(-x));
    }

    /**
     * The derivative of the sigmoid function,
     * used to calculate the error of the neural network in the backpropagation
     * 
     * @param x
     * @return
     */
    private double sigmoidDerivative(double x) {
        double sigmoid = sigmoid(x);
        return sigmoid * (1 - sigmoid);
    }

    /**
     * The activation function for the hidden layer
     * 
     * @param x
     * @return
     */
    private double relu(double x) {         
        return Math.max(0, x);
    }

}
