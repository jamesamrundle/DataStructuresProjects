package kdtree;
import kdtree.PointSet;
import java.util.List;
import java.util.function.Function;
import java.util.function.BiFunction;

public class KDTreePointSet implements PointSet {
    Class x = new XPoint(0, 0).getClass();
    Class y = new YPoint(0, 0).getClass();

    public XPoint root;


    private Point findLeftBound(Point here, Point query, boolean isX){
        if(here == root  ){
            if(root.x() <=query.x()) return here;
            else return new Point(Integer.MIN_VALUE,0);
        }
        if ( isX ){
            if(here.x() >= query.x() ) return findLeftBound(((XPoint)here).getParent(),query, ! isX);
            else return  here;
        }
        return findLeftBound(((YPoint)here).getParent(),query, ! isX);
    }
    private Point findRightBound(Point here, Point query, boolean isX){
        if(here == root  ){
            if(root.x() >=query.x()) return here;
            else return new Point(Integer.MAX_VALUE,0);
        }
        if ( isX ){
            if(here.x() <= query.x() ) return findRightBound(((XPoint)here).getParent(),query, ! isX);
            else return  here;
        }
        return findRightBound(((YPoint)here).getParent(),query, ! isX);
    }
    private Point findUpperBound(Point here, Point query, boolean isX){
        if(here == root) return new Point(0,Integer.MAX_VALUE);
        if ( !isX ){
            if(here.y() <= query.y() ) return findUpperBound(((YPoint)here).getParent(),query, ! isX);
            else return  here;
        }
        return findUpperBound(((XPoint)here).getParent(),query, ! isX);
    }
    private Point findLowerBound(Point here, Point query, boolean isX){
        if(here == root) return new Point(0,Integer.MIN_VALUE);
        if ( !isX ){
            if(here.y() >= query.y() ) return findLowerBound(((YPoint)here).getParent(),query, ! isX);
            else return  here;
        }
        return findLowerBound(((XPoint)here).getParent(),query, ! isX);
    }

    public boolean findPoint(Point here, Point query,boolean isX ){
        if( here.x()== query.x() && here.y() == query.y() ) {
            System.out.println("*Point :(" + here.x() + "," + here.y() + ") FOUND *");
            return true;}
        if( here == null) return false;

        System.out.println("");
        System.out.print("Point :(" + here.x() + "," + here.y() + "),");



        Point[] choice = {null,null}; //sets ordering of which child node is traversed first

        if (isX){
            XPoint h = (XPoint)here;

            if( query.x() < here.x() ){
                choice[0] = (YPoint)h.getLeft();
                choice[1] = (YPoint)h.getRight();
            }
            else{
                choice[1] = (YPoint)h.getLeft();
                choice[0] = (YPoint)h.getRight();
            }
        }
        else{
            YPoint h = (YPoint)here;

            if( query.y() < here.y() ){
                choice[0] = (XPoint)h.getLeft();
                choice[1] = (XPoint)h.getRight();
            }
            else{
                choice[1] = (XPoint)h.getLeft();
                choice[0] = (XPoint)h.getRight();
            }
        }



        return (findPoint(choice[0],query,!isX)||findPoint(choice[1],query,!isX));
    }

    public boolean findIt(Point goal){
        return findPoint(root,goal,true);
    }

    private Point nearestPoint(Point here,Point minNode, Point query,boolean isX  ){
        if( here == null ) return minNode;
        else if(here.distanceSquaredTo(query) < minNode.distanceSquaredTo(query)){
            minNode = here;
        }

        Point[] choice = {null,null}; //sets ordering of which child node is traversed first

        if (isX){
            XPoint h = (XPoint)here;

            if( query.x() < here.x() ){
                choice[0] = (YPoint)h.getLeft();
                choice[1] = (YPoint)h.getRight();
            }
            else{
                choice[1] = (YPoint)h.getLeft();
                choice[0] = (YPoint)h.getRight();
            }
        }
        else{
            YPoint h = (YPoint)here;

            if( query.y() < here.y() ){
                choice[0] = (XPoint)h.getLeft();
                choice[1] = (XPoint)h.getRight();
            }
            else{
                choice[1] = (XPoint)h.getLeft();
                choice[0] = (XPoint)h.getRight();
            }
        }

        Point A = minNode;
        Point B = minNode;
        double minNodeDistance = Math.sqrt(minNode.distanceSquaredTo(query));
        double AnodeDistance = minNodeDistance;
        double BnodeDistance = minNodeDistance;
        
        if(choice[0]!= null){
            A = nearestPoint(choice[0],minNode,query,!isX);
            AnodeDistance = Math.sqrt(A.distanceSquaredTo(query));
        }

        if(bestPossibleDistance(here,query,isX) < minNodeDistance){
            if(choice[1] != null) {
                B = nearestPoint(choice[1], minNode, query, !isX);
                BnodeDistance = Math.sqrt(B.distanceSquaredTo(query));
            }
        }
        if ((BnodeDistance < minNodeDistance &&
                (BnodeDistance < AnodeDistance))) return B;
        if ((AnodeDistance < minNodeDistance &&
                (AnodeDistance < BnodeDistance))) return A;
//        if( B != null && BnodeDistance < AnodeDistance) return B;

        return minNode;
    }
    public double bestPossibleDistance(Point p,Point goal,boolean isX){
        if( p == null) return Integer.MAX_VALUE;
        Point bound1;
        Point bound2;
        if(isX){
            bound1 = new Point(p.x(),findUpperBound(p,p,isX).y());
            bound2 = new Point(p.x(),findLowerBound(p,p,isX).y());
            if(bound1.x() == bound2.x() && bound1.y() == bound2.y()  ){
                if(goal.x() > p.x()){
                    bound1 = new Point(p.x(),findLeftBound(p,p,isX).y());
                }
                else bound1 = new Point(p.x(),findRightBound(p,p,isX).y());
            }
        }
        else{
            bound1 = new Point(findRightBound(p,p,isX).x(),p.y());
            bound2 = new Point(findLeftBound(p,p,isX).x(),p.y());
            if(bound1.x() == bound2.x() && bound1.y() == bound2.y()  ){
                if(goal.y() > p.y()){
                    bound1 = new Point(p.x(),findLowerBound(p,p,isX).y());
                }
                else bound1 = new Point(p.x(),findUpperBound(p,p,isX).y());
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
        double dist = Math.sqrt(dx*dx + dy*dy);
        return dist;

    }

    public BiFunction<Point, Point, Point> pointReturner =
        (p,c) -> { if(p.getClass() == x ){
            return new YPoint(c.x(),c.y());
        } else{
            return new XPoint(c.x(),c.y());
        }
    };

    public void nodeAdder(Point parent,Point newNode){
        int compare;
        if (parent.getClass() == x) {
            XPoint here = (XPoint) parent;
            Point hl = here.getLeft();
            Point hr = here.getRight();
            compare = Double.compare(newNode.x(), here.x());

            if (compare < 0) {
                if (hl == null) {
                    YPoint newY = (YPoint)pointReturner.apply(parent,newNode);
                    newY.setParent(here);
                    here.setLeft(newY);
                    return;
                } else {
                    nodeAdder(hl, newNode);
                }
            } else {
                if (hr == null) {
                    YPoint newY = (YPoint)pointReturner.apply(parent,newNode);
                    newY.setParent(here);
                    here.setRight(newY);
                    return;
                } else {
                    nodeAdder(hr, newNode);
                }
            }


        } else {
            YPoint here = (YPoint) parent;
            Point hl = here.getLeft();
            Point hr = here.getRight();
            compare = Double.compare(newNode.y(), here.y());

            if (compare < 0) {
                if (hl == null) {
                    XPoint newX = (XPoint)pointReturner.apply(parent,newNode);
                    newX.setParent(here);
                    here.setLeft(newX);

                    return;
                } else {
                    nodeAdder(hl, newNode);
                }
            } else {
                if (hr == null) {
                    XPoint newX =  (XPoint)pointReturner.apply(parent,newNode);
                    newX.setParent(here);
                    here.setRight(newX);
                    return;
                } else {
                    nodeAdder(hr, newNode);
                }
            }
        }
    }

    private void buildTree(Point parent,Point newNode){

        Point nextNode ;
        int compare ;
        if(parent.getClass() == new XPoint(0,0).getClass()){
            XPoint here = (XPoint)parent;
            compare = Double.compare(newNode.x(),here.x());

            if ( compare < 0){
                if ( here.getLeft() == null ) {
                    YPoint newY = getYPoint(newNode);
                    newY.setParent(here);
                    here.setLeft(newY);
                    return;
                }
                else{ buildTree(here.getLeft(),newNode);}
            }
            else{
                if ( here.getRight() == null ) {
                    YPoint newY = getYPoint(newNode);
                    newY.setParent(here);
                    here.setRight(newY);
                    return;
                }
                else{ buildTree(here.getRight(),newNode);}
            }


        }else{
            YPoint here = (YPoint)parent;
            compare = Double.compare(newNode.y(),here.y());

            if ( compare < 0){
                if ( here.getLeft() == null ) {
                    XPoint newX = getXPoint(newNode);
                    newX.setParent(here);
                    here.setLeft(newX);

                    return;
                }
                else{ buildTree(here.getLeft(),newNode);}
            }
            else{
                if ( here.getRight() == null ) {
                    XPoint newX = getXPoint(newNode);
                    newX.setParent(here);
                    here.setRight(newX);
                    return;
                }
                else{ buildTree(here.getRight(),newNode);}
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

    private void printTreeWBounds(Point p,boolean isX){
        if(p == root) printTree(p);




        System.out.println("");
        System.out.print("Point :(" + p.x() + "," + p.y() + "),");
        System.out.printf("With bounds Left:%.1f Right:%.1f Up:%.1f,Down:%.1f \n", findLeftBound(p, p,isX).x(), findRightBound(p, p,isX).x(), findUpperBound(p, p,isX).y(), findLowerBound(p, p,isX).y());

        if( p.getClass() == new YPoint(0,0).getClass()) {
            YPoint t = (YPoint)p;
            if (t.getLeft() != null) printTreeWBounds((XPoint) t.getLeft(),!isX);
            if (t.getRight() != null) printTreeWBounds((XPoint) t.getRight(),! isX);
        }
        else{
            XPoint t = (XPoint)p;
            if (t.getLeft() != null) printTreeWBounds((YPoint) t.getLeft(),!isX);
            if (t.getRight() != null) printTreeWBounds((YPoint) t.getRight(),!isX);
        }
    }

    private void printTreeWDistances(Point p,Point goal, boolean isX){
        if(p == root) printTree(p);




        System.out.println("");
        System.out.print("Point :(" + p.x() + "," + p.y() + "),");
        System.out.printf("With shortest possible distance of : "+bestPossibleDistance(p,goal,isX));

        if( p.getClass() == new YPoint(0,0).getClass()) {
            YPoint t = (YPoint)p;
            if (t.getLeft() != null) printTreeWDistances((XPoint) t.getLeft(),goal,!isX);
            if (t.getRight() != null) printTreeWDistances((XPoint) t.getRight(),goal,!isX);
        }
        else{
            XPoint t = (XPoint)p;
            if (t.getLeft() != null) printTreeWDistances((YPoint) t.getLeft(),goal,!isX);
            if (t.getRight() != null) printTreeWDistances((YPoint) t.getRight(),goal,!isX);
        }
    }
    public void distancesPrint(Point goal){
        printTreeWDistances(root,goal,true);
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
        System.out.printf("With shortest possible distance of : "+Math.sqrt(p.distanceSquaredTo(goal)));

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

    public KDTreePointSet(List<Point> points) {
        for(int i = 0;i<points.size();i++) {
            if (i == 0)   root = getXPoint(points.get(0));
            else{
                nodeAdder(root,points.get(i));
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
        Point t = nearestPoint(root,root,new Point(x,y),true);
//        System.out.println("*("+t.x()+","+t.y()+")*");
        return t;
    }
}
