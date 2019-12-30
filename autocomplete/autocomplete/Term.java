package autocomplete;

public class Term implements Comparable<Term> {
    private String query ;
    private long weight;

    /**
     * Initializes a term with the given query string and weight.
     * @throws IllegalArgumentException if query is null or weight is negative
     */
    public Term(String query, long weight) {
       if(query == null | weight < 0){
           throw new IllegalArgumentException();
       }
       this.query = query;
       this.weight = weight;

    }

    /**
     * Compares the two terms in lexicographic order by query.
     * @throws NullPointerException if the specified object is null
     */
    public int compareTo(Term that) {
        return this.query.toLowerCase().compareTo(that.query.toLowerCase());
    }

    /** Compares to another term, in descending order by weight. */
    public int compareToByReverseWeightOrder(Term that) {

        if (this.weight < that.weight()) {
            return 1;
        }
        else if (this.weight > that.weight()) {
            return -1;
        }
        else return 0;
    }

    /**
     * Compares to another term in lexicographic order, but using only the first r characters
     * of each query. If r is greater than the length of any term's query, compares using the
     * term's full query.
     * @throws IllegalArgumentException if r < 0
     */
    public int compareToByPrefixOrder(Term that, int r) {
        if( r < 0 ){
            throw new IllegalArgumentException();
        }



        return this.queryPrefix(r).toLowerCase().compareTo(that.queryPrefix(r).toLowerCase());

    }

    /** Returns this term's query. */
    public String query() {
        return this.query;

    }

    /**
     * Returns the first r characters of this query.
     * If r is greater than the length of the query, returns the entire query.
     * @throws IllegalArgumentException if r < 0
     */
    public String queryPrefix(int r) {
        if (r < 0){
            throw new IllegalArgumentException();
        }
        if ( this.query.length() > r){
            return this.query.substring(0,r).toLowerCase();
        }
        return this.query.toLowerCase();
    }

    /** Returns this term's weight. */
    public long weight() {
       return this.weight;
    }

    @Override
    public String toString() {
        return this.query;
    }
}
