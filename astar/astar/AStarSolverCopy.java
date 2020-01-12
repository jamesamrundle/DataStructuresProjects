package astar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

/**
 * @see ShortestPathsSolver for more method documentation
 */
public class AStarSolverCopy<Vertex> implements ShortestPathsSolver<Vertex> {
   private Vertex start;
   private Vertex end;
   private double timeout;
   private int numStates;
   private AStarGraph<Vertex> theGraph;


//    ArrayHeap<Vertex> allVertex = new ArrayHeap<>();
    TreeMapMinPQ<Vertex> allVertex = new TreeMapMinPQ<>();
    private ArrayList<Vertex> soluList = new ArrayList<>();
    private HashMap<Vertex,Vertex> nodeAndParent = new HashMap<>();
    private HashMap<Vertex,Double> nodeCostToVisit = new HashMap<>();
    private HashMap<Vertex,Double> nodeWeight = new HashMap<Vertex, Double>();
    /**
     * Immediately solves and stores the result of running memory optimized A*
     * search, computing everything necessary for all other methods to return
     * their results in constant time. The timeout is given in seconds.
     */
    public AStarSolverCopy(AStarGraph<Vertex> input, Vertex start, Vertex end, double timeout) {
        this.start = start;
        soluList.add(start);
        nodeWeight.put(start,0.0);

        this.end = end;
        this.numStates = 0;
        this.theGraph = input;
        this.timeout = timeout;
        double cat = theGraph.estimatedDistanceToGoal(start, end);
        for (WeightedEdge<Vertex> each : input.neighbors(start)) {
            allVertex.add(each.to(), each.weight());
            nodeWeight.put(each.to(), each.weight());
            nodeAndParent.put(each.to(), start);
            nodeCostToVisit.put(each.to(),each.weight());
            double min = Integer.MAX_VALUE;
            for (WeightedEdge<Vertex> inner : input.neighbors(each.to())) {
                if (inner.weight() < min) {
                    min = inner.weight();
                    allVertex.changePriority(each.to(), each.weight() + min);
                }
            }



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
                    costToTravel >= allVertex.getPriority(each.to()) ) ) continue;
            else if(allVertex.contains(each.to()))          {

                allVertex.changePriority(each.to(),costToTravel);}
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
