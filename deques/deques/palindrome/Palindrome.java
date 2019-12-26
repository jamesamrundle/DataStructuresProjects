package deques.palindrome;

import deques.Deque;
import deques.LinkedDeque;

public class Palindrome {
    public Deque<Character> wordToDeque(String word) {

        if (word == null) {return null; }

        Deque<Character> wordDeque = new LinkedDeque<>();
        for (char each : word.toCharArray()){
            wordDeque.addLast(each);
        }
        return wordDeque;
    }

    public boolean isPalindrome(String word) {
        Deque<Character> wordDeque = wordToDeque(word);
        if ( word.length() == 1){
            return true;
        }
            if (wordDeque.removeFirst() == wordDeque.removeLast()){
                String newWord = "";
                if (wordDeque.size() == 0){
                    return true;
                }
                for (int i = 0; i < wordDeque.size(); i++){
                    newWord += wordDeque.get(i);
                }
                return isPalindrome(newWord);
            }
            else {
                return false;
            }


    }

    public boolean isPalindrome(String word, CharacterComparator cc) {
        // TODO: your code here
        throw new UnsupportedOperationException("Not implemented yet.");
    }
}
