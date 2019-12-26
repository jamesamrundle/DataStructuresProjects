package deques.palindrome;

import deques.Deque;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PalindromeTest {
    // You must use this palindrome, and not instantiate
    // new Palindromes, or the autograder might be upset.
    static Palindrome palindrome = new Palindrome();

    @Test
    public void testWordToDeque() {
        Deque<Character> d = palindrome.wordToDeque("persiflage");
        String actual = "";
        for (int i = 0; i < "persiflage".length(); i++) {
            actual += d.removeFirst();
        }
        assertEquals("persiflage", actual);
    }
    @Test
    public void testOnlyAddFront() {
        Deque<Character> d = palindrome.wordToDeque("persiflage");

        assertEquals('p', (char) d.removeFirst());
        assertEquals('e', (char) d.removeLast());
        assertEquals('e', (char) d.get(0));
        assertEquals('g', (char) d.get(d.size()-1));
    }

    @Test
    public void checkPalindrome(){

        assertEquals(false, palindrome.isPalindrome("persiflage"));
        assertEquals(true, palindrome.isPalindrome("tacocat"));
        assertEquals(true, palindrome.isPalindrome("coc"));
        assertEquals(true, palindrome.isPalindrome("t"));
        assertEquals(true, palindrome.isPalindrome("tt"));
        assertEquals(true, palindrome.isPalindrome("tacoocat"));
        assertEquals(false, palindrome.isPalindrome("ta"));
    }

}
