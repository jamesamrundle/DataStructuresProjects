package heap;

import org.junit.Before;
import org.junit.Test;

import javax.swing.plaf.nimbus.NimbusLookAndFeel;

import java.util.*;

import static org.junit.Assert.*;


public class ArrayHeapMinPQTest {
    int seed = 373; // or your favorite number
    Random random = new Random(seed);

    @Test
    public void firstTest() {
        assertEquals(1, 1);
    }

    @Test
    public void getOutSorted() {
        ArrayHeapMinPQ test = new ArrayHeapMinPQ();
//        test.add(0,0);
        test.add("c and half", 3.5);
        test.add('a', 1);
        test.add('d', 4);
        test.add('c', 3);
        test.add('b', 2);

        Object[] TestArr = new Object[5];
        Object[] arr = {'a', 'b', 'c', "c and half", 'd'};
        int initSize = test.size();
        for (int i = 0; i < initSize; i++) {
            TestArr[i] = test.removeSmallest();
        }

        assertArrayEquals(TestArr, arr);
    }

    @Test
    public void getCurrentState() {
        ArrayHeapMinPQ test = new ArrayHeapMinPQ();
//        test.add(0,0);
        test.add("c and half", 3.5);
        test.add('a', 1);
        test.add('d', 4);
        test.add('c', 3);
        test.add('b', 2);

        for (Object each : test.getArray()) {
            if (each.getClass() == "".getClass()) System.out.print(each + ",");

            else System.out.print(each + ",");
        }
        assertTrue("Hi", true);
    }

    @Test
    public void addRemoveadd() {
        ArrayHeapMinPQ test = new ArrayHeapMinPQ();
//        test.add(0,0);
        test.add("c and half", 3.5);
        test.add('a', 1);
        test.add('d', 4);
        test.add('c', 3);
        test.add('b', 2);
        printArray(test.getArray());
        ArrayList<Object> TestArr = new ArrayList<Object>();
        Object[] arr = {'a', 'b', 'c', "c and half", 'd'};
        int initSize = test.size();
        for (int i = 2; i < initSize; i++) {
            TestArr.add(test.removeSmallest());
            printArray(test.getArray());
        }
        assertArrayEquals(test.getArray(), Arrays.copyOfRange(arr, 3, arr.length));

        test.add("z", 3);
        printArray(test.getArray());
        test.add("k", 2);
        printArray(test.getArray());
        test.add("a", 7);

        printArray(test.getArray());

    }

    private void printArray(Object[] arr) {
        for (Object each : arr) {
            if (each.getClass() == "".getClass()) System.out.print(each + ",");

            else System.out.print(each + ",");
        }
        System.out.println();
    }

    @Test
    public void testFind() {
        ArrayHeapMinPQ test = new ArrayHeapMinPQ();

        test.add("c and half", 3.5);
        test.add('a', 1);
        test.add('d', 4);
        test.add('c', 3);
        test.add('b', 2);
        test.add("z", 3);
        test.add("k", 2);

        try {
            test.add('a', 7);
        } catch (IllegalArgumentException e) {
            System.out.println("Threw expected exception, " + e);
        }
        printArray(test.getArray());
        assertEquals(test.findItem("k", 0).getItem(), "k");
        assertEquals(test.findItem("z", 0).getItem(), "z");
        assertEquals(test.findItem('a', 0).getItem(), 'a');
        assertNotEquals(test.findItem('a', 0).getItem(), "a");
        assertNull(test.findItem("a", 0));
        test.removeSmallest();
        assertNull(test.findItem('a', 0));
        printArray(test.getArray());

    }

    @Test
    public void testAllSameElem() {
        int i = 0;
        ArrayHeapMinPQ test = new ArrayHeapMinPQ();

        while (i++ < 10) {
            test.add((char) ((int) 'a' + i), 1 + i);
        }
        printArray(test.getArray());
        test.add("A", 0);
        printArray(test.getArray());
        test.add("z", 0);
        printArray(test.getArray());
        test.add("Z", 100);
        printArray(test.getArray());
        i = 0;

        while (i++ < 10) {
            assertEquals(test.findItem((char) ((int) 'a' + i), 0).getItem(), (char) ((int) 'a' + i));
        }

    }

    @Test
    public void testSelfIndexAwareness() {
        int i = 0;
        ArrayHeapMinPQ test = new ArrayHeapMinPQ();

        while (i++ < 10) {
            test.add((char) ((int) 'a' + i), 1 + i);
        }

        i = 0;
        while (i++ < 10) {
            Object[] current = test.getArray();
            int rando = random.nextInt(current.length);

            ArrayHeapMinPQ.PriorityNode pn = test.findItem(current[rando], 0);
            assertEquals(pn.getPos(), rando);
            test.removeSmallest();
        }

    }


    @Test
    public void testSelfChange() {
        int i = 0;
        ArrayHeapMinPQ test = new ArrayHeapMinPQ();

        while (i++ < 10) {
            test.add((char) ((int) 'a' + i), 1 + i);
        }
        printArray(test.getArray());
        test.changePriority('f', 0);
        printArray(test.getArray());

    }

    private String makeString() {
        String temp = "";
        for (int i = 0; i < 4; i++) {
            temp += (char) random.nextInt(97 - 65) + 65;
        }
        return temp;
    }

    @Test
    public void bigTest() {
        HashSet<String> ns = new HashSet<>();
        HashSet<Double> ds = new HashSet<>();
        ArrayHeapMinPQ arrTest = new ArrayHeapMinPQ();
        NaiveMinPQ naiveTest = new NaiveMinPQ();


        for (int opNum = 0; opNum < 10000; opNum++) {
            int opType = random.nextInt(3);


            if (opType == 1 && ds.size() > 0) {

                    String naiveI = (String) naiveTest.removeSmallest();
                    ns.remove(naiveI);

                    ds.remove( arrTest.findItem(naiveI,0).getPriority() );
                    String arrI = (String) arrTest.removeSmallest();

                    assertEquals(arrI, naiveI);

                 }else if (opType == 2 && ds.size() > 0) { //change priority
                    int randNode = random.nextInt(ns.size());

                    Double tempD = random.nextDouble();

                    while (ds.contains(tempD)) {
                        tempD = random.nextDouble();
                    }
                    ds.add(tempD);
                    String temp = (String) ns.toArray()[randNode];
                    ds.remove(arrTest.findItem(temp, 0).getPriority());
                    naiveTest.changePriority(ns.toArray()[randNode], tempD);
                    arrTest.changePriority(ns.toArray()[randNode], tempD);


                } else {
                    String tempS = makeString();
                    Double tempD = random.nextDouble();

                    while (ns.contains(tempS)) {
                        tempS = makeString();
                    }
                    ns.add(tempS);

                    while (ds.contains(tempD)) {
                        tempD = random.nextDouble();
                    }
                    ds.add(tempD);

                    arrTest.add(tempS, tempD);
                    naiveTest.add(tempS, tempD);

                }
                System.out.print(opNum + ",");
                assertArrayEquals(arrTest.getOrderedArray(), naiveTest.getArray());
            }
        }
    }




