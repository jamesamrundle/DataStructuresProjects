package kdtree;

public interface PointSet {
    /** Returns the point in this set closest to (x, y). */
    Point nearest(double x, double y);


//
//   double dist2 (Point b, Point goal) {
//        return Math.pow(b.x() - goal.x(),2) + Math.pow(b.y() - goal.y(),2);
//    }
//
//    // p - point
//// v - start point of segment
//// w - end point of segment
//    public Point bestPoint (Point goal, Point b, Point a) {
//        double l2 = a.distanceSquaredTo(b);
//        if (Double.compare(l2,0) == 0) return null;
//         double t = ((goal.x() - b.x()) * (a.x() - b.x()) + (goal.y() - b.y()) * (a.y() - b.y())) / l2;
//        t = Math.max(0, Math.min(1, t));
//        return new Point(  (b.x() + t * (a.x() - b.x()) ), (b.y() + t * (a.y() - b.y()) ) );
//    }
//
//    // p - point
//// v - start point of segment
//// w - end point of segment
//    aublic double distToSegment (a, v, goal) {
//        return Math.sqrt(distToSegmentSquared(a, v, goal));
//    }

}
