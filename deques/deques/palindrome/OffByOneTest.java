package deques.palindrome;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class OffByOneTest {
    // You must use this CharacterComparator and not instantiate
    // new ones, or the autograder might be upset.
    static CharacterComparator offByOne = new OffByOne();

    @Test
    public void testAll(){

        assertEquals(true, offByOne.equalChars(';', ':'));
        assertEquals(true, offByOne.equalChars('b', 'a'));
        assertEquals(true , offByOne.equalChars((char) ((int) 'a'-1), 'a'));
        assertEquals(true , offByOne.equalChars('A', 'B'));
        assertEquals(false , offByOne.equalChars('a', 'c'));
        assertEquals(false , offByOne.equalChars('a', 'K'));

    }
}
