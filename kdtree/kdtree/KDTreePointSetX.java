package kdtree;

import java.util.List;
import java.util.function.Function;
import java.util.function.BiFunction;
import kdtree.PointSet;
import kdtree.Node;
import kdtree.Point;
import kdtree.XPoint;
import kdtree.YPoint;
public class KDTreePointSetX implements PointSet {
    
    public Node root;
    public Node goal;

    private Node findLeftBound(Node here, Node query, boolean isX){
        if(here == root  ){
            if(root.x() <=query.x()) return here;
            else return new Node(Integer.MIN_VALUE,0);
        }
        if ( isX ){
            if(here.x() >= query.x() ) return findLeftBound(here.getParent(),query, ! isX);
            else return  here;
        }
        return findLeftBound(here.getParent(),query, ! isX);
    }
    private Node findRightBound(Node here, Node query, boolean isX){
        if(here == root  ){
            if(root.x() >=query.x()) return here;
            else return new Node(Integer.MAX_VALUE,0);
        }
        if ( isX ){
            if(here.x() <= query.x() ) return findRightBound(here.getParent(),query, ! isX);
            else return  here;
        }
        return findRightBound(here.getParent(),query, ! isX);
    }
    private Node findUpperBound(Node here, Node query, boolean isX){
        if(here == root) return new Node(0,Integer.MAX_VALUE);
        if ( !isX ){
            if(here.y() <= query.y() ) return findUpperBound(here.getParent(),query, ! isX);
            else return  here;
        }
        return findUpperBound(here.getParent(),query, ! isX);
    }
    private Node findLowerBound(Node here, Node query, boolean isX){
        if(here == root) return new Node(0,Integer.MIN_VALUE);
        if ( !isX ){
            if(here.y() >= query.y() ) return findLowerBound(here.getParent(),query, ! isX);
            else return  here;
        }
        return findLowerBound(here.getParent(),query, ! isX);
    }

   
    private Node nearestPoint(Node here,Node minNode,boolean isX  ){
        if( here == null ) return minNode;
        else if(here.distanceSquaredTo(goal) < minNode.distanceSquaredTo(goal)){
            minNode = here;
        }

        Node[] choice = {null,null}; //sets ordering of which child node is traversed first
        int compare;
        if (isX) {
            compare = Double.compare(goal.x(), here.x());
        } else compare = Double.compare(goal.y(), here.y() );

            if( compare < 0){
                choice[0] = here.getLeft();
                choice[1] = here.getRight();
            }
            else{
                choice[1] = here.getLeft();
                choice[0] = here.getRight();
            }



        Node A = minNode;
        Node B = minNode;
        double minNodeDistance = (minNode.distanceSquaredTo(goal));
        double AnodeDistance = minNodeDistance;
        double BnodeDistance = minNodeDistance;

        if(choice[0]!= null){
            A = nearestPoint(choice[0],minNode,!isX);
            AnodeDistance = (A.distanceSquaredTo(goal));
        }

        if(bestPossibleDistance(here,isX) < minNodeDistance){
            if(choice[1] != null) {
                B = nearestPoint(choice[1], minNode, !isX);
                BnodeDistance = (B.distanceSquaredTo(goal));
            }
        }
        if ((BnodeDistance < minNodeDistance &&
                (BnodeDistance < AnodeDistance))) return B;
        if ((AnodeDistance < minNodeDistance &&
                (AnodeDistance < BnodeDistance))) return A;
//        if( B != null && BnodeDistance < AnodeDistance) return B;

        return minNode;
    }
    public double bestPossibleDistance(Node p,boolean isX){
        if( p == null) return Integer.MAX_VALUE;
        Node bound1;
        Node bound2;
        if(isX){
            bound1 = new Node(p.x(),findUpperBound(p,p,isX).y());
            bound2 = new Node(p.x(),findLowerBound(p,p,isX).y());
            if(bound1.x() == bound2.x() && bound1.y() == bound2.y()  ){
                if(goal.x() > p.x()){
                    bound1 = new Node(p.x(),findLeftBound(p,p,isX).y());
                }
                else bound1 = new Node(p.x(),findRightBound(p,p,isX).y());
            }
        }
        else{
            bound1 = new Node(findRightBound(p,p,isX).x(),p.y());
            bound2 = new Node(findLeftBound(p,p,isX).x(),p.y());
            if(bound1.x() == bound2.x() && bound1.y() == bound2.y()  ){
                if(goal.y() > p.y()){
                    bound1 = new Node(p.x(),findLowerBound(p,p,isX).y());
                }
                else bound1 = new Node(p.x(),findUpperBound(p,p,isX).y());
            }
        }

        return shortestDistance(bound1,bound2,goal);
    }


    public double shortestDistance(Point p1,Point p2, Point goal)
    {
        double px=p2.x()-p1.x();
        double py=p2.y()-p1.y();
        double temp=(px*px)+(py*py);
        double u=((goal.x() - p1.x()) * px + (goal.y() - p1.y()) * py) / (temp);
        if(u>1){
            u=1;
        }
        else if(u<0){
            u=0;
        }
        double x = p1.x() + u * px;
        double y = p1.y() + u * py;
//        System.out.println(">>"+x+":"+y);
        double dx = x - goal.x();
        double dy = y - goal.y();
        double dist = (dx*dx + dy*dy);
        return dist;

    }



    public void nodeAdder(Node parent,Node newNode, boolean isX){
        int compare;
        Node here = parent;
        Node hl = here.getLeft();
        Node hr = here.getRight();
        if (isX) {
            compare = Double.compare(newNode.x(), here.x());
        }
        else compare = Double.compare(newNode.y(), here.y());

            if (compare < 0) {
                if (hl == null) {
                    newNode.setParent(here);
                    here.setLeft(newNode);
                    return;
                } else {
                    nodeAdder(hl, newNode,!isX);
                }
            } else {
                if (hr == null) {
                    newNode.setParent(here);
                    here.setRight(newNode);
                    return;
                } else {
                    nodeAdder(hr, newNode,!isX);
                }
            }



            }

   

    private void printTree(Point p){
        System.out.print("("+p.x()+","+p.y()+"),");

        if( p.getClass() == new YPoint(0,0).getClass()) {
            YPoint t = (YPoint)p;
            if (t.getLeft() != null) printTree((XPoint) t.getLeft());
            if (t.getRight() != null) printTree((XPoint) t.getRight());
        }
        else{
            XPoint t = (XPoint)p;
            if (t.getLeft() != null) printTree((YPoint) t.getLeft());
            if (t.getRight() != null) printTree((YPoint) t.getRight());
        }
    }

    private void printTreeWBounds(Node p,boolean isX){
        if(p == root) printTree(p);




        System.out.println("");
        System.out.print("Point :(" + p.x() + "," + p.y() + "),");
        System.out.printf("With bounds Left:%.1f Right:%.1f Up:%.1f,Down:%.1f \n", findLeftBound(p, p,isX).x(), findRightBound(p, p,isX).x(), findUpperBound(p, p,isX).y(), findLowerBound(p, p,isX).y());

        if( p.getClass() == new Node(0,0).getClass()) {
            Node t = p;
            if (t.getLeft() != null) printTreeWBounds( t.getLeft(),!isX);
            if (t.getRight() != null) printTreeWBounds( t.getRight(),! isX);
        }
        else{
            Node t = p;
            if (t.getLeft() != null) printTreeWBounds( t.getLeft(),!isX);
            if (t.getRight() != null) printTreeWBounds( t.getRight(),!isX);
        }
    }

    private void printTreeWDistances(Node p, boolean isX){
        if(p == root) printTree(p);




        System.out.println("");
        System.out.print("Point :(" + p.x() + "," + p.y() + "),");
        System.out.printf("With shortest possible distance of : "+bestPossibleDistance(p,isX));
        Node t = p;
        if( !isX) {

            if (t.getLeft() != null) printTreeWDistances( t.getLeft(),!isX);
            if (t.getRight() != null) printTreeWDistances( t.getRight(),!isX);
        }
        else{

            if (t.getLeft() != null) printTreeWDistances( t.getLeft(),!isX);
            if (t.getRight() != null) printTreeWDistances( t.getRight(),!isX);
        }
    }
    public void distancesPrint(Node goal){
        this.goal = goal;
        printTreeWDistances(root,true);
    }
    public void boundsPrint(){
        printTreeWBounds(root,true);
    }
    public void print(){
        printTree(root);
    }
    private XPoint getXPoint(Point point){
        return new XPoint(point.x(),point.y());
    }
    private YPoint getYPoint(Point point){
        return new YPoint(point.x(),point.y());
    }


    private void printNodeDistances(Point p,Point goal){
        if(p == root) printTree(p);




        System.out.println("");
        System.out.print("Point :(" + p.x() + "," + p.y() + "),");
        System.out.printf("With shortest possible distance of : "+(p.distanceSquaredTo(goal)));

        if( p.getClass() == new YPoint(0,0).getClass()) {
            YPoint t = (YPoint)p;
            if (t.getLeft() != null) printNodeDistances((XPoint) t.getLeft(),goal);
            if (t.getRight() != null) printNodeDistances((XPoint) t.getRight(),goal);
        }
        else{
            XPoint t = (XPoint)p;
            if (t.getLeft() != null) printNodeDistances((YPoint) t.getLeft(),goal);
            if (t.getRight() != null) printNodeDistances((YPoint) t.getRight(),goal);
        }
    }
    public void nodeDistancePrint(Point goal){
        printNodeDistances(root,goal);
    }

    public KDTreePointSetX(List<Point> points) {
        Point p ;
        for(int i = 0;i<points.size();i++) {
            p = points.get(i);
            if (i == 0)   root = new Node(p.x(),p.y());
            else{
                nodeAdder(root,new Node(p.x(),p.y()),true);
//                buildTree(root,points.get(i));
            }
        }


    }

    /**
     * Returns the point in this set closest to (x, y) in (usually) O(log N) time,
     * where N is the number of points in this set.
     */
    @Override
    public Point nearest(double x, double y) {
        goal = new Node(x,y);
        Point t = nearestPoint(root,root,true);
//        Point t = nearestPoint(root,root,new Node(x,y),true);
//        System.out.println("*("+t.x()+","+t.y()+")*");
        return t;
    }
}
