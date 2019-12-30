package autocomplete;

import java.util.Arrays;
import java.util.NoSuchElementException;

public class BinaryRangeSearch implements Autocomplete {

    Term[] terms;

    /**
     * Validates and stores the given array of terms.
     * Assumes that the given array will not be used externally afterwards (and thus may directly
     * store and mutate it).
     * @throws IllegalArgumentException if terms is null or contains null
     */
    public BinaryRangeSearch(autocomplete.Term[] terms) {
        if(terms == null ) throw new IllegalArgumentException();

        for(Term t : terms){
            if (t == null) throw new IllegalArgumentException();
        }

        this.terms = terms;

        Arrays.sort(this.terms);

    }

    /**
     * Returns all terms that start with the given prefix, in descending order of weight.
     * @throws IllegalArgumentException if prefix is null
     */

    private int getMid(int L, int H){
        return (L + (int) (H-L)/2);
    }

    private int findLow(int L, int H,String prefix){
        prefix = prefix.toLowerCase();
        int mid = getMid(L,H);
        if(H>=L) {

                if(mid == 0 ) {
                    if(prefix.compareTo(terms[mid].queryPrefix(prefix.length())) == 0 ) return mid;
                    if(prefix.compareTo(terms[mid+1].queryPrefix(prefix.length())) == 0 ) return mid+1;
                    return -1;
                }
                if( mid == terms.length -1){
                    if(prefix.compareTo(terms[mid].queryPrefix(prefix.length())) == 0 ) return mid;
                    if(prefix.compareTo(terms[mid-1].queryPrefix(prefix.length())) == 0 ) return mid-1;
                    return -1;
                }
                if (prefix.compareTo(terms[mid].queryPrefix(prefix.length())) > 0) {
                    return findLow(mid + 1, H, prefix);
                }

                if( (prefix.compareTo(terms[mid].queryPrefix(prefix.length())) == 0) &&
                    prefix.compareTo(terms[mid - 1].queryPrefix(prefix.length())) > 0 ) {
                    return mid;
                }

                else if (prefix.compareTo(terms[mid-1].queryPrefix(prefix.length())) <= 0) {
                    return findLow(L,mid - 1, prefix);
                }

              else {
                return -1;
            }

        }
        return -1;
    }

    private int findHi(int L, int H,String prefix){
        prefix = prefix.toLowerCase();
        int mid = getMid(L,H);
        if(H>=L) {

            if(mid == 0 ) {
                if(prefix.compareTo(terms[mid].queryPrefix(prefix.length())) == 0 ) return mid;
                if(prefix.compareTo(terms[mid+1].queryPrefix(prefix.length())) == 0 ) return mid+1;
                return -1;
            }
            if( mid == terms.length -1){
                if(prefix.compareTo(terms[mid].queryPrefix(prefix.length())) == 0 ) return mid;
                if(prefix.compareTo(terms[mid-1].queryPrefix(prefix.length())) == 0 ) return mid-1;
                return -1;
            }
            if (prefix.compareTo(terms[mid].queryPrefix(prefix.length())) > 0 || prefix.compareTo(terms[mid+1].queryPrefix(prefix.length())) == 0 ) {
                return findHi(mid + 1, H, prefix);
            }

            if( (prefix.compareTo(terms[mid].queryPrefix(prefix.length())) == 0) &&
                    prefix.compareTo(terms[mid + 1].queryPrefix(prefix.length())) < 0 ) {
                return mid;
            }

            else if (prefix.compareTo(terms[mid].queryPrefix(prefix.length())) < 0) {
                return findHi(L,mid -1, prefix);
            }

            else {
                return -1;
            }

        }
        return -1;
    }


    public Term[] allMatches(String prefix) {
       if (prefix == null) throw new IllegalArgumentException();

       //Find first
        int low = findLow(0,terms.length-1,prefix);
        int high = findHi(0,terms.length-1,prefix);

        if( low == -1 || high == -1) return new Term[0];

        Term[] temp = Arrays.copyOfRange(terms,low,high+1);
        Arrays.sort(temp,TermComparators.byReverseWeightOrder());
        return temp;
    }
}
