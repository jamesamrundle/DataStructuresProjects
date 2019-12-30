package autocomplete;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class TermTest {
    @Test
    public void testSimpleCompareTo() {
        Term a = new Term("autocomplete", 0);
        Term b = new Term("me", 0);
        assertTrue(a.compareTo(b) < 0); // "autocomplete" < "me"
    }

    @Test
    public void testReverseCompareTo() {
        Term a = new Term("autocomplete", 0);
        Term b = new Term("me", 1);
        Term c = new Term("you", 1);
        assertTrue(a.compareToByReverseWeightOrder(b) <= 1); // "autocomplete" < "me"
        assertTrue(b.compareToByReverseWeightOrder(a) < 1); // "autocomplete" < "me"
        assertTrue(b.compareToByReverseWeightOrder(c) == 0); // "autocomplete" < "me"

    }

    @Test
    public void testPrefixCompareTo(){
        Term a = new Term("cathole",1);
        Term aa = new Term("catho",1);
        Term b = new Term("catholic",100000);
        Term bb = new Term("cakhole",20);
        assertTrue(a.compareToByPrefixOrder(bb,2) == 0);
        assertTrue(aa.compareToByPrefixOrder(bb,10) > 0);
        assertTrue(a.compareToByPrefixOrder(bb,3) > 0);
        assertTrue(a.compareToByPrefixOrder(b,4) == 0);
        assertTrue(a.compareToByPrefixOrder(aa,4) == 0);
        assertTrue(a.compareToByPrefixOrder(b,4) == 0);
        assertTrue(a.compareToByPrefixOrder(b,7) < 0);
        assertTrue(b.compareToByPrefixOrder(a,7) > 0);
        assertTrue(a.compareToByPrefixOrder(b,10) < 0);
    }
    // Write more unit tests below.
}
