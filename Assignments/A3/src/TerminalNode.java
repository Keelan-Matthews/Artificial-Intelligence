public class TerminalNode extends Node {
    private final int index; // This is the index of the attribute in the training set
    private final double value; // This is the value of the attribute that the node represents

    public TerminalNode(int index, double value) {
        this.index = index;
        this.value = value;
    }

    @Override
    public boolean evaluate(double[] inputs) {
        return inputs[index] == value;
    }
}
