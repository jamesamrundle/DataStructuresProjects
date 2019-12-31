package kdtree;

import java.util.List;

public class NaivePointSet implements PointSet {
    List<Point> points;

    public NaivePointSet(List<Point> points) {
        this.points = List.copyOf(points);

    }

    @Override
    public Point nearest(double x, double y) {
        double min = -1;
        Point nearest = points.get(0);
        for (Point each : points) {
            double xDist = Math.pow(each.x() - x, 2);
            double yDist = Math.pow(each.y() - y, 2);

            if (min < 0) {
                min = Math.sqrt(xDist + yDist);
                nearest = each;
            } else {
                double temp = Math.sqrt(xDist + yDist);
//                System.out.print("*Point :(" + each.x() + "," + each.y() + "),");
//                System.out.printf("With shortest possible distance of : "+temp);
                if(temp < min){
                    min = temp;
                    nearest = each;
                }

            }
        }
        return nearest;

    }
}
