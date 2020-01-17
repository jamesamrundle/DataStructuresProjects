package huskymaps;


import astar.*;
import autocomplete.BinaryRangeSearch;
import autocomplete.Term;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import huskymaps.kdtree.kdtree.KDTreePointSet;
import huskymaps.kdtree.kdtree.Point;
import huskymaps.server.logic.Router;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.*;
import java.util.stream.Collectors;

import static huskymaps.utils.Constants.BASE_DIR_PATH;
import static huskymaps.utils.Constants.ROUTE_LIST2;
import static huskymaps.utils.Spatial.greatCircleDistance;
import static huskymaps.utils.Spatial.projectToX;
import static huskymaps.utils.Spatial.projectToY;

public class StreetMapGraph implements AStarGraph<Long> {

    public static Map<Long, Node> nodes = new HashMap<>();
    private Map<Long, Set<WeightedEdge<Long>>> neighbors = new HashMap<>();

    private KDTreePointSet streetPointsKDgraph ;
    private BinaryRangeSearch autocomplete ;




    public StreetMapGraph(String filename) {
        OSMGraphHandler.initializeFromXML(this, filename);
        initDataStructures();
       try {
//           getTestRoute(); //method inserted to troubleshoot the VERY SLIGHT route difference between mine and test case....
       } catch (Exception e) {System.out.println("wompwomp"); }
    }
    private final Gson gson = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create();
    private static final String REQUEST_FORMAT = BASE_DIR_PATH + "tests/router/request%d.json";
    private static final String RESULT_FORMAT = BASE_DIR_PATH + "tests/router/result%d.json";


    public double weightOfRoute(List<Long> route){
        double weight = 0;
        int i = 0;
        Long l;
        Node n ;
        while(i<route.size()-1){
            l = route.get(i);
            n = nodes.get(l);
            List<WeightedEdge<Long>> temp =neighbors(l);
            for(WeightedEdge<Long> each : temp){
                long w = each.to();
                long next =route.get(i+1);
                if(w == next){
                    weight += each.weight();
                }
            }
            i++;
        }
        return weight;
    }

    public String getTestRoute() throws IOException {
        Node n;

        try (Reader reader = new FileReader(String.format(RESULT_FORMAT, 0))) {
           ROUTE_LIST2 = Arrays.asList(gson.fromJson(reader, Long[].class));
        }
        return "";
    }

    private class XTerm extends Term{
        //eXtending Term used in autocomplete with id field.
        public long id ;
        public XTerm(String query, long weight,long id) {
            super(query, weight);
        this.id = id;
        }
        public long getId() { return this.id; }
    }

    private void initDataStructures(){

        List<Point> streetPoints = new ArrayList<>();
        //Temp terms is ArrayList for ease of use.
        ArrayList<Term> tempTerms = new ArrayList<>();

        double x =0.;
        double y = 0.;

        for(Node each :nodes.values()){
            if(isNavigable(each)) {
                x = projectToX(each.lon(), each.lat());
                y = projectToY(each.lon(), each.lat());
                Point p = new Point(x, y);
                p.id = each.id;
                streetPoints.add(p);
            }

            if(each.name() != null) {
                Term term = new XTerm(each.name(), each.importance, each.id);
                tempTerms.add(term);
            }
        }
        //requesting this term from the search bar returns all the points hit while searching for last requested "best route"
        tempTerms.add(new Term("666",0));
        streetPointsKDgraph = new KDTreePointSet(streetPoints);

        Term[] terms = new Term[tempTerms.size()];
        //BinaryRangeSearch is initialized once. We can afford the expensive 'cast' from ArrayList to array
        autocomplete = new BinaryRangeSearch(tempTerms.toArray(terms));
    }

//    class Point extends kdtree.Point{
//        long id;
//        public Point(double x, double y) {
//            super(x, y);
//
//        }
//    }
    /**
     * Returns the vertex closest to the given longitude and latitude.
     * @param lat The target latitude.
     * @param lon The target longitude.
     * @return The id of the node in the graph closest to the target.
     */
    public long closest(double lat, double lon) {
        double x = projectToX(lon, lat);
        double y = projectToY(lon, lat);
        long ret = (streetPointsKDgraph.nearest(x,y)).id;
        return ret;

    }

    /**
     * In linear time, collect all the names of OSM locations that prefix-match the query string.
     * @param prefix Prefix string to be searched for. Could be any case, with our without
     *               punctuation.
     * @return A <code>List</code> of full names of locations matching the <code>prefix</code>.
     */
    public List<String> getLocationsByPrefix(String prefix) {
        Term[] terms = autocomplete.allMatches(prefix);

        List<String> asList = Arrays.stream(terms)
                .map(Term::query).collect(Collectors.toCollection(LinkedList::new));
        return  asList;

    }

    /**
     * Collect all locations that match a cleaned <code>locationodeame</code>, and return
     * information about each node that matches.
     * @param locationName A full name of a location searched for.
     * @return A list of locations whose name matches the <code>locationName</code>.
     */
    public List<Node> getLocations(String locationName) {
        List<Node> asList;
        Term[] terms = autocomplete.allMatches(locationName);
        if(locationName.equals("666")){
            return Router.getList().stream()
                    .map(term-> nodes.get(term))
                    .collect(Collectors.toCollection(LinkedList::new));
        }
        asList= Arrays.stream(terms)
                .map(term -> nodes.get( ((XTerm)term).id ) )
                .collect(Collectors.toCollection(LinkedList::new));

        return asList;
    }

    /** Returns a list of outgoing edges for V. Assumes V exists in this graph. */
    @Override
    public List<WeightedEdge<Long>> neighbors(Long v) {
        return new ArrayList<>(neighbors.get(v));
    }

    /**
     * Returns the great-circle distance between S and GOAL. Assumes
     * S and GOAL exist in this graph.
     */
    @Override
    public double estimatedDistanceToGoal(Long s, Long goal) {
        Node sNode = nodes.get(s);
        Node goalNode = nodes.get(goal);
        return greatCircleDistance(sNode.lon(), goalNode.lon(), sNode.lat(), goalNode.lat());
    }

    /** Returns a set of my vertices. Altering this set does not alter this graph. */
    public Set<Long> vertices() {
        return new HashSet<>(nodes.keySet());
    }

    /** Adds an edge to this graph if it doesn't already exist, using distance as the weight. */
    public void addWeightedEdge(long from, long to, String name) {
        if (nodes.containsKey(from) && nodes.containsKey(to)) {
            Node fromNode = nodes.get(from);
            Node toNode = nodes.get(to);
            double weight = greatCircleDistance(fromNode.lon(), toNode.lon(), fromNode.lat(), toNode.lat());
            neighbors.get(from).add(new WeightedEdge<>(from, to, weight, name));
        }
    }

    /** Adds an edge to this graph if it doesn't already exist. */
    public void addWeightedEdge(long from, long to, double weight, String name) {
        if (nodes.containsKey(from) && nodes.containsKey(to)) {
            neighbors.get(from).add(new WeightedEdge<>(from, to, weight, name));
        }
    }

    /** Adds an edge to this graph if it doesn't already exist. */
    public void addWeightedEdge(WeightedEdge<Long> edge) {
        if (nodes.containsKey(edge.from()) && nodes.containsKey(edge.to())) {
            neighbors.get(edge.from()).add(edge);
        }
    }

    /** Checks if a vertex has 0 out-degree from graph. */
    private boolean isNavigable(Node node) {
        return !neighbors.get(node.id()).isEmpty();
    }

    /**
     * Gets the latitude of a vertex.
     * @param v The id of the vertex.
     * @return The latitude of the vertex.
     */
    public double lat(long v) {
        if (!nodes.containsKey(v)) {
            return 0.0;
        }
        return nodes.get(v).lat();
    }

    /**
     * Gets the longitude of a vertex.
     * @param v The id of the vertex.
     * @return The longitude of the vertex.
     */
    public double lon(long v) {
        if (!nodes.containsKey(v)) {
            return 0.0;
        }
        return nodes.get(v).lon();
    }

    /** Adds a node to this graph, if it doesn't yet exist. */
    void addNode(Node node) {
        if (!nodes.containsKey(node.id())) {
            nodes.put(node.id(), node);
            neighbors.put(node.id(), new HashSet<>());
        }
    }

    public static Node getNode(long id) {
        return nodes.get(id);
    }

    Node.Builder nodeBuilder() {
        return new Node.Builder();
    }
}
