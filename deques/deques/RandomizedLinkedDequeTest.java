package deques;

import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertEquals;

public class RandomizedLinkedDequeTest {
    @Test
    public void testAddLastThenRemoveLast() {
        // Random seed ensures that each test run is reproducible
        int seed = 373; // or your favorite number
        Random random = new Random(seed);

        // Create a new ArrayDeque (reference implementation)
        ArrayDeque<Integer> expectedDeque = new ArrayDeque<>();
        // Create a new LinkedDeque (testing implementation)
        LinkedDeque<Integer> testingDeque = new LinkedDeque<>();

        // Add 1000000 random integers to both the expectedDeque and testingDeque implementations
        for (int i = 0; i < 1000000; i += 1) {
            int n = random.nextInt();
            expectedDeque.addLast(n);
            testingDeque.addLast(n);
        }
        // Check that testingDeque matches expectedDeque on all 1000000 integers
        for (int i = 0; i < 1000000; i += 1) {
            int expectedValue = expectedDeque.removeLast();
            int testingValue = testingDeque.removeLast();
            assertEquals("Failed on iteration " + i, expectedValue, testingValue);
            // To debug a particular iteration, set a conditional breakpoint
            // Add a regular breakpoint but right click and set a condition like i == 57893
        }
    }

    @Test
    public void testRandomOperations() {
        // Random seed ensures that each test run is reproducible
        int seed = 373; // or your favorite number
        Random random = new Random(seed);

        // Create a new ArrayDeque (reference implementation)
        ArrayDeque<Integer> expectedDeque = new ArrayDeque<>();
        // Create a new LinkedDeque (testing implementation)
        LinkedDeque<Integer> testingDeque = new LinkedDeque<>();

        // Try writing testing code here to add a few thousands items to both deques
        for (int i = 0; i < 4000; i += 1) {
            int randInt = random.nextInt(100);
            expectedDeque.addFirst(randInt);
            testingDeque.addFirst(randInt);
        }
        // For 1000000 iterations...
        for (int i = 0; i < 1000000; i += 1) {
            // Roll a six-sided dice returning a value 0, 1, 2, 3, 4, or 5
            int choice = random.nextInt(6);

            // Choose to perform one of six different actions
            if (choice == 0) {
                int n = random.nextInt();
                expectedDeque.addFirst(n);
                testingDeque.addFirst(n);
            } else if (choice == 1) {
                int n = random.nextInt();
                expectedDeque.addLast(n);
                testingDeque.addLast(n);
            } else if (choice == 2) {
                int expectedSize = expectedDeque.size();
                int testingSize = testingDeque.size();
                assertEquals("size() failed on iteration " + i, expectedSize, testingSize);
            } else if (choice == 3) {
                if (expectedDeque.isEmpty()) {
                    i -= 1;
                    continue;
                }
                int expectedValue = expectedDeque.removeFirst();
                int testingValue = testingDeque.removeFirst();
                assertEquals("removeFirst() failed on iteration " + i, expectedValue, testingValue);
            } else if (choice == 4) {
                if (expectedDeque.isEmpty()) {
                    i -= 1;
                    continue;
                }
                int expectedValue = expectedDeque.removeLast();
                int testingValue = testingDeque.removeLast();
                assertEquals("removeLast() failed on iteration " + i, expectedValue, testingValue);
            } else {
                if (expectedDeque.isEmpty()) {
                    i -= 1;
                    continue;
                }
                int maxSize = expectedDeque.size();
                int index = random.nextInt(maxSize);
                int expectedValue = expectedDeque.get(index);
                int testingValue = testingDeque.get(index);
                assertEquals("get(" + index + ") failed on iteration " + i, expectedValue, testingValue);
            }
        }
    }

    @Test
    public void testTricky() {
        Deque<Integer> deque = new LinkedDeque<>();
        deque.addFirst(0);
        assertEquals(0, (int) deque.get(0));

        deque.addLast(1);
        assertEquals(1, (int) deque.get(1));

        deque.addFirst(-1);
        deque.addLast(2);
        assertEquals(2, (int) deque.get(3));

        deque.addLast(3);
        deque.addLast(4);

        // Test that removing and adding back is okay
        assertEquals(-1, (int) deque.removeFirst());
        deque.addFirst(-1);
        assertEquals(-1, (int) deque.get(0));

        deque.addLast(5);
        deque.addFirst(-2);
        deque.addFirst(-3);

        // Test a tricky sequence of removes
        assertEquals(-3, (int) deque.removeFirst());
        assertEquals(5, (int) deque.removeLast());
        assertEquals(4, (int) deque.removeLast());
        assertEquals(3, (int) deque.removeLast());
        assertEquals(2, (int) deque.removeLast());
        // Failing test
        assertEquals(1, (int) deque.removeLast());
    }
}


