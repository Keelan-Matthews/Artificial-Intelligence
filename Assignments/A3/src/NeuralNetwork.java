import java.util.ArrayList;
import java.util.Random;

public class NeuralNetwork {
    private int inputSize; // The number of input neurons
    private int hiddenSize; // The number of hidden neurons
    private double learningRate; // The rate at which the weights are updated during training
    private double[][] weightsInputHidden; // The weights between the input and hidden layers
    private double[] weightsHiddenOutput; // The weights between the hidden and output layers
    private long seed; // The seed for the random number generator
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
        this.random = GenerateRandomWithSeed(seed);

        initializeWeights();
    }

    /**
     * Function to generate a random number generator with a seed
     * 
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

    /**
     * This function predicts the output of the neural network by multiplying the
     * input by the weights,
     * and applying the activation function to the result
     * 
     * @param input
     * @return
     */
    public double[] predict(double[] inputList) {
        double[] hidden = new double[hiddenSize];

        double[] input;
        // If the input size is 9, then the class attribute is still in and needs to be removed (the first element to remove)
        if (inputList.length == 9) {
            input = new double[8];
            for (int j = 1; j < inputList.length; j++) {
                input[j - 1] = inputList[j];
            }
        }
        else {
            input = inputList;
        }

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

        return new double[] { output };
    }

    // backpropagation function
    public void train(ArrayList<double[]> inputsList, int epochs) {

        // Convert the array list into a 2D array
        double[][] inputs = new double[inputsList.size()][];
        for (int i = 0; i < inputsList.size(); i++) {
            inputs[i] = inputsList.get(i);
        }

        // Train the neural network for the specified number of epochs
        for (int epoch = 0; epoch < epochs; epoch++) {
            double error = 0.0; // The total error for the epoch

            // use backpropagation to update the weights
            for (int i = 0; i < inputs.length; i++) {
                // The label is input[0], extract that and remove it from the input array
                double label = inputs[i][0];
                double[] input = new double[inputs[i].length - 1];
                for (int j = 1; j < inputs[i].length; j++) {
                    input[j - 1] = inputs[i][j];
                }

                // Calculate the output of the hidden layer neurons
                double[] hidden = new double[hiddenSize];
                for (int j = 0; j < hiddenSize; j++) {
                    double sum = 0;

                    // Multiply the input by its corresponding weight and add it to the sum,
                    // do this for all the hidden neurons' inputs
                    for (int k = 0; k < inputSize; k++) {
                        sum += input[k] * weightsInputHidden[k][j];
                    }

                    // Apply the activation function to each hidden neuron's sum,
                    // to simulate the neuron firing or not based on the input
                    hidden[j] = relu(sum);
                }

                // Multiply the hidden layer outputs by the weights between the hidden and
                // output layers,
                // and sum them, resulting in a single output value
                double output = 0;
                for (int j = 0; j < hiddenSize; j++) {
                    output += hidden[j] * weightsHiddenOutput[j];
                }

                // Apply the sigmoid activation function to the output to map it to a
                // probability between 0 and 1
                output = sigmoid(output);

                // Calculate the error of the neural network
                double delta = output - label;
                error += Math.pow(delta, 2);

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
            }

            System.out.println("Epoch " + (epoch+1) + " error: " + error);
        }
    }
}
