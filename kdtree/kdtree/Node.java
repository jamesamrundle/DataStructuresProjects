package kdtree;

public class Node extends Point {

    public Node(double x, double y) {
        super(x, y);
    }
    private Node left = null;
    private Node right = null;
    private Node parent = null;
    public  Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public Node getLeft() {
        return left;
    }

    public void setLeft(Node left) {
        this.left = left;
    }

    public Node getRight() {
        return right;
    }

    public void setRight(Node right) {
        this.right = right;
    }
}
