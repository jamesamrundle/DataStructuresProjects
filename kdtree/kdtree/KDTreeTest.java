package kdtree;

import org.junit.*;

import java.util.*;

import static org.junit.Assert.*;

public class KDTreeTest {



@Test public void findDistanceToLine(){


    Point a = new Point(0,0);
        Point b = new Point(4,0);

        Point A = new Point(2,3);
        Point A1 = new Point(2,-3);
        Point A2 = new Point(0,7);

        Point a1 = new Point(Integer.MIN_VALUE,0);
        Point b1 = new Point(Integer.MAX_VALUE ,0);
        Point c = new Point(1,1);
        Point d = new Point(1.4563,1);
        KDTreePointSet temp = new KDTreePointSet(new ArrayList<>());
    System.out.println(temp.shortestDistance(a,b,c));
    System.out.println(temp.shortestDistance(a,b,d));
    System.out.println(temp.shortestDistance(a1,b1,c));
    System.out.println(temp.shortestDistance(a1,b1,d));
    System.out.println(temp.shortestDistance(A1,A,A2));

}

    @Test public void lilUn() {
        List<Point> temp = new ArrayList<>();
        temp.add(new Point(.9,3));
        temp.add(new Point(0,4.01));
        temp.add(new Point(1,2));
        temp.add(new Point(1,1));
        temp.add(new Point(1,4));
        temp.add(new Point(1,3));

        KDTreePointSet kTest = new KDTreePointSet(temp);
        NaivePointSet nTest = new NaivePointSet(temp);
        kTest.distancesPrint(new Node(0,3));
       double dx = 0;
        double dy = 3;
        Point p1 = kTest.nearest(dx,dy);
        Point p2 = nTest.nearest(dx, dy);

        assertEquals(p1.x(),p2.x(),0);
        assertEquals(p1.y(),p2.y(),0);
    }

@Test public void bigUn() {
    KDTreePointSet kTest;
    ArrayList<Point> kArr = new ArrayList<Point>();

    NaivePointSet nTest;
    ArrayList<Point> nArr = new ArrayList<Point>();
    Random random = new Random(369);
    int x;
    int y;
    for (int i = 0; i < 4000; i++) {
        x = random.nextInt(1100);
        y = random.nextInt(1100);
        kArr.add(new Point(x, y));
        nArr.add(new Point(x, y));
    }

    kTest = new KDTreePointSet(kArr);
    nTest = new NaivePointSet(nArr);
//    kTest.nodeDistancePrint(new Point(420.391369,659.81259));
//    kTest.distancesPrint(new Point(420.391369,659.81259));
//    nTest.nearest(420.391369,659.81259);
//    boolean is = kTest.findIt(new Point(417.0,668.0));
    double dx;
    double dy;
    for (int i = 0; i < 10000; i++) {
        dx = random.nextDouble()+random.nextInt() % 1100;
        dy = random.nextDouble()+random.nextInt() % 1100;
         Point p1 = kTest.nearest(dx, dy);
         Point p2 = nTest.nearest(dx, dy);



         assertEquals(p1.x(),p2.x(),0);
         assertEquals(p1.y(),p2.y(),0);


    }
    System.out.println("hi");
}
}
