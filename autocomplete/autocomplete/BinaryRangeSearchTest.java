package autocomplete;

import edu.princeton.cs.algs4.In;
import java.util.Random;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class BinaryRangeSearchTest {
    Random random = new Random(369);
    private static Autocomplete linearAuto;
    private static Autocomplete binaryAuto;
    private static Term[] moreTerms;
    private static int N = 0;
    private static Term[] terms = null;

    private static final String INPUT_FILENAME = "./cities.txt";

    /**
     * Creates LinearRangeSearch and BinaryRangeSearch instances based on the data from cities.txt
     * so that they can easily be used in tests.
     */
    @Before
    public void setUp() {
        if (terms != null) {
            return;
        }

        In in = new In(INPUT_FILENAME);
        N = in.readInt();
        terms = new Term[N];
        for (int i = 0; i < N; i += 1) {
            long weight = in.readLong();
            in.readChar();
            String query = in.readLine();
            terms[i] = new Term(query, weight);
        }

         moreTerms = new Term[] {
                new Term("hello", 0),
                new Term("world", 0),
                new Term("welcome", 0),
                new Term("to", 0),
                new Term("aubocomplete", 0),
                new Term("me", 0),
                new Term("catatonic", 3),
                new Term("catamaran", 4),
                new Term("catalan", 2),
                new Term("cathole", 10),
                new Term("catholic", 0)

        };

        linearAuto = new LinearRangeSearch(terms);
        binaryAuto = new BinaryRangeSearch(terms);
    }

    /**
     * Checks that the terms in the expected array are equivalent to the ones in the actual array.
     */
    private void assertTermsEqual(Term[] expected, Term[] actual) {
        assertEquals(expected.length, actual.length);
        for (int i = 0; i < expected.length; i++) {
            Term e = expected[i];
            Term a = actual[i];
            assertEquals(e.query(), a.query());
            assertEquals(e.weight(), a.weight());
        }
    }

    @Test
    public void testSimpleExample() {

        LinearRangeSearch lrs = new LinearRangeSearch(moreTerms);
        BinaryRangeSearch brs = new BinaryRangeSearch(moreTerms);
        Term[] expected = new Term[]{new Term("autocomplete", 0)};
        Term[] expectedCat = new Term[]{
                new Term("catatonic", 3),
                new Term("catamaran", 4),
                new Term("catalan", 2),
                new Term("cathole", 10),
                new Term("catholic", 0)
        };
        Arrays.sort(expectedCat,TermComparators.byReverseWeightOrder());
        Term[] actual = brs.allMatches("auto");
        Term[] actualCat = brs.allMatches("cat");
        assertNull(actual);
        assertTermsEqual(expectedCat, actualCat);
    }

    @Test
    public void randomTests(){
        Term temp;
        int prefLen = 0;
        Term[] linResults;
        Term[] binResults;
        for(int i = 0;i < 1000;i++){
            temp = terms[random.nextInt(terms.length)];
            prefLen = random.nextInt(6);
            linResults = linearAuto.allMatches(temp.queryPrefix(prefLen));
            binResults = binaryAuto.allMatches(temp.queryPrefix(prefLen));
            Arrays.sort(linResults,TermComparators.byReverseWeightOrder());
            Arrays.sort(binResults,TermComparators.byReverseWeightOrder());
            assertTermsEqual(linResults,binResults);
        }
    }

    // Write more unit tests below.
}
