public class InternalNode extends Node {
    private final Node left;
    private final Node right;
    private final String operator;

    public InternalNode(Node left, Node right, String operator) {
        this.left = left;
        this.right = right;
        this.operator = operator;
    }

    @Override
    public boolean evaluate(double[] inputs) {
        boolean leftResult = left.evaluate(inputs);
        boolean rightResult = right.evaluate(inputs);

        switch (operator) {
            case "and":
                return leftResult && rightResult;
            case "or":
                return leftResult || rightResult;
            case "not":
                return !leftResult;
            default:
                throw new IllegalArgumentException("Invalid operator: " + operator);
        }

    }
    
}
