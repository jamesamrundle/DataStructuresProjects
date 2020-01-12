package astar;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @see ShortestPathsSolver for more method documentation
 */
public class AStarSolverBAD<Vertex> implements ShortestPathsSolver<Vertex> {
   private Vertex start;
   private Vertex end;
   private double timeout;
   private int numStates;
   private AStarGraph<Vertex> theGraph;


    ArrayHeap<Vertex> allVertex = new ArrayHeap<>();
//    TreeMapMinPQ<Vertex> allVertex = new TreeMapMinPQ<>();
    private ArrayList<Vertex> soluList = new ArrayList<>();
    private HashMap<Vertex,LinkedHashMap<Double,Vertex>> nodeToNeibs = new HashMap<>();
    private HashMap<Vertex,Vertex> nodeAndParent = new HashMap<>();
    private HashMap<Vertex,Double> nodeCostToVisit = new HashMap<>();
    private HashMap<Vertex,Double> nodeWeight = new HashMap<Vertex, Double>();
    /**
     * Immediately solves and stores the result of running memory optimized A*
     * search, computing everything necessary for all other methods to return
     * their results in constant time. The timeout is given in seconds.
     */
    public AStarSolverBAD(AStarGraph<Vertex> input, Vertex start, Vertex end, double timeout) {
        this.start = start;
        soluList.add(start);
        nodeWeight.put(start,0.0);

        this.end = end;
        this.numStates = 0;
        this.theGraph = input;
        this.timeout = timeout;
        double cat = theGraph.estimatedDistanceToGoal(start, end);
        for (WeightedEdge<Vertex> each : input.neighbors(start)) {
           Vertex to = each.to();
           double weight = each.weight();
           nodeToNeibs.put(each.to(),new LinkedHashMap<Double,Vertex>());
           for (WeightedEdge<Vertex> inner : input.neighbors(each.to())) {
               nodeToNeibs.get(each.to()).put(inner.weight(),inner.to());
            }
            Map<Double,Vertex> result = nodeToNeibs.get(each.to()).entrySet()
                    .stream()
                    .sorted(Map.Entry.comparingByKey())
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            Map.Entry::getValue,
                            (oldValue, newValue) -> oldValue, LinkedHashMap::new));
            nodeToNeibs.put(each.to(), (LinkedHashMap<Double, Vertex>) result);

        }
        while(! soluList.get(soluList.size()-1).equals(end)) {
            solveIt();
        }
        cleanUp();
        return;
    }
    private void solveIt(){
        Vertex thisPoint = allVertex.removeSmallest();

        numStates += 1;
        soluList.add(thisPoint);
        double costToTravel;

        for(WeightedEdge<Vertex> each : theGraph.neighbors(thisPoint)){
            costToTravel = (each.weight()+nodeWeight.get(thisPoint));
            if(  (nodeCostToVisit.containsKey(each.to()) && costToTravel >= nodeCostToVisit.get(each.to()) ) ||
                    ( soluList.contains(each.to()) || allVertex.contains(each.to()) &&
                    costToTravel >= allVertex.findItem(each.to(),0).getPriority() ) ) continue;
            else if(allVertex.contains(each.to()))          {

                allVertex.changePriority(each.to(),costToTravel,666.6);}
            else                                            allVertex.add(each.to(),costToTravel);
            nodeCostToVisit.put(each.to(),costToTravel);
            nodeWeight.put(each.to(),costToTravel);
            nodeAndParent.put(each.to(), thisPoint);


        }


        return;
    }

    private void cleanUp(){

        if( outcome() == SolverOutcome.SOLVED){
            int arrayItem = soluList.size()-1;
            java.util.function.IntSupplier tr = () -> soluList.size()-1;
            Function<ArrayList<Vertex>,Integer> t = (soluList) -> soluList.size()-1;
            int inner = 0;
            while (arrayItem > 0){

                while(soluList.get(arrayItem-1) != nodeAndParent.get(soluList.get(arrayItem)) ){
                    soluList.remove(arrayItem-1);
                    arrayItem = tr.getAsInt();
                }
                arrayItem -= 1;
            }

        }

    }
    @Override
    public SolverOutcome outcome() {
       if (soluList.get(soluList.size()-1).equals(end))return SolverOutcome.SOLVED;
       else return SolverOutcome.UNSOLVABLE;
    }

    @Override
    public List<Vertex> solution() {

        return soluList;
    }

    @Override
    public double solutionWeight() {

       return nodeWeight.get(soluList.get(soluList.size()-1));
    }

    /** The total number of priority queue removeSmallest operations. */
    @Override
    public int numStatesExplored() {
      return numStates;
    }

    @Override
    public double explorationTime() {
     return 1.1;
    }
}
