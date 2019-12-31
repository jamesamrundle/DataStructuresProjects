package kdtree;

import org.junit.*;

import java.util.*;

import static org.junit.Assert.*;

public class KDTreeTest {



    @Test
    public void firstTest() {
        Point xp = new XPoint(0, 0);
        Point yp = new YPoint(0, 0);
        Point tp = new Point(0, 0);

        System.out.println(xp.getClass() + ":" + yp.getClass());
        assertEquals(xp.getClass(), new XPoint(0, 0).getClass());
    }



@Test
    public void testRoot(){
        ArrayList<Point> temp = new ArrayList<>();
        temp.add(new Point(5,5));
        temp.add(new Point(1,2));
        temp.add(new Point(-1,0));
        temp.add(new Point(0,0));
        temp.add(new Point(4,4));
        temp.add(new Point(1,6));
        temp.add(new Point(3,6));
        temp.add(new Point(0,5));
        temp.add(new Point(0,6));
        temp.add(new Point(2,2));
        PointComparator.isX = false;

    List<Point> temp2 = new ArrayList<>();
    temp2.add(new Point(2,3));
    temp2.add(new Point(1,5));
    temp2.add(new Point(4,2));
    temp2.add(new Point(4,5));
    temp2.add(new Point(4,4));
    temp2.add(new Point(3,3));

        KDTreePointSetX test = new KDTreePointSetX(temp);
        KDTreePointSetX test2 = new KDTreePointSetX(temp2);

        assertEquals(new XPoint(0,0).getClass(),test.root.getClass());
//        test2.distancesPrint(new Point(0,7));
            test2.nearest(5,4);
            test2.nearest(5,2.5);
            test2.nearest(5,5);
            test.nearest(2,3);
    }

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
        kTest.distancesPrint(new Point(0,3));
       double dx = 0;
        double dy = 3;
        Point p1 = kTest.nearest(dx,dy);
        Point p2 = nTest.nearest(dx, dy);

        assertEquals(p1.x(),p2.x(),0);
        assertEquals(p1.y(),p2.y(),0);
    }

@Test public void bigUn() {
    KDTreePointSetX kTest;
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

    kTest = new KDTreePointSetX(kArr);
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
