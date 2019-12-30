package autocomplete;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class LinearRangeSearch implements Autocomplete {
    Term[] terms;

    /**
     * Validates and stores the given array of terms.
     * Assumes that the given array will not be used externally afterwards (and thus may directly
     * store and mutate it).
     * @throws IllegalArgumentException if terms is null or contains null
     */
    public LinearRangeSearch(Term[] terms) {
        if(terms == null ) throw new IllegalArgumentException();

        for(Term t : terms){
            if (t == null) throw new IllegalArgumentException();
        }

        this.terms = terms;
    }

    /**
     * Returns all terms that start with the given prefix, in descending order of weight.
     * @throws IllegalArgumentException if prefix is null
     */
    public Term[] allMatches(String prefix) {
        ArrayList<Term> temp = new ArrayList<>();
        for(Term term : terms){
            if(term.queryPrefix(prefix.length()).compareTo(prefix) == 0){
                temp.add(term);
            }
        }
        Term[] returnArray = new Term[temp.size()];
        temp.toArray(returnArray);
        Arrays.sort( returnArray, TermComparators.byReverseWeightOrder()  );
        return returnArray;
    }
}

