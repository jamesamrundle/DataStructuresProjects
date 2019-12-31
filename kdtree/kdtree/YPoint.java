package kdtree;

public class YPoint extends Point {

    private Point left = null;
    private Point right = null;
    private Point parent= null;

    public YPoint(double x, double y) {
        super(x, y);
    }

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
