package kdtree;

import java.util.Comparator;

public class PointComparator {
//    public static Comparator<Term> byReverseWeightOrder() {
//        return Term::compareToByReverseWeightOrder;
//    }
public static boolean isX = true;
public static Comparator<Point> orderPoints() {

    Class x = new Point(0, 0).getClass();
    Class y = new Point(0, 0).getClass();
    return (p1, p2) -> {

        if (isX) {
            if (p1.x() < p2.x()){
                isX = !isX;
                return -1;
            }
            if (p1.x() == p2.x()){
                isX = !isX;
                return -0;
            }
        } else {
            if (p1.y() < p2.y()){
                isX = !isX;
                return -1;
            }
            if (p1.y() == p2.y()){
                isX = !isX;
                return -0;
            }
        }
        isX = !isX;
        return 1;

    };
}

    public static Comparator<Point> compByDistToGoal(Point goal){ return (p1,p2) -> Double.compare(p1.distanceSquaredTo(goal), p2.distanceSquaredTo(goal));}
    public static Comparator<Point> compByX(){ return Comparator.comparing(Point::x);}
    //?    public static Comparator<Term> byPrefixOrder(int r) {
//        return (t1, t2) -> t1.compareToByPrefixOrder(t2, r);
//    }

}


