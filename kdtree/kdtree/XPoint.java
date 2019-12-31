package kdtree;

public class XPoint extends Point {


    public XPoint(double x, double y) {
        super(x, y);
    }
    private Point left = null;
    private Point right = null;
    private Point parent = null;
    public Point getParent() {
        return parent;
    }

    public void setParent(Point parent) {
        this.parent = parent;
    }

    public Point getLeft() {
        return left;
    }

    public void setLeft(Point left) {
        this.left = left;
    }

    public Point getRight() {
        return right;
    }

    public void setRight(Point right) {
        this.right = right;
    }
}
