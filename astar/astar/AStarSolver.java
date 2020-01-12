package astar;


import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @see ShortestPathsSolver for more method documentation
 */
public class AStarSolver<Vertex> implements ShortestPathsSolver<Vertex> {
   private Vertex start;
   private Vertex end;
   private Vertex watchMe = end;
   private double timeout;
   private int numStates;
   private boolean NoMoreVertex = false;
   private AStarGraph<Vertex> theGraph;


    ArrayHeap<Vertex> allVertex = new ArrayHeap<>();
//    TreeMapMinPQ<Vertex> allVertex = new TreeMapMinPQ<>();
    private ArrayList<Vertex> soluList = new ArrayList<>();
    private HashSet<Vertex> visited = new HashSet<>();
    private HashMap<Vertex,Double> nodeWeight = new HashMap<Vertex, Double>();
    private HashMap<Vertex,Vertex> nodeAndParent = new HashMap<>();


    public AStarSolver(AStarGraph<Vertex> input, Vertex start, Vertex end, double timeout) {
        this.start = start;
//        soluList.add(start);
//        visited.add(start);
        nodeWeight.put(start,0.0);

        this.end = end;
        this.watchMe = end;
        this.numStates = 0;
        this.theGraph = input;
        this.timeout = timeout;

//        relax(start);
        allVertex.add(start,0.0);
        do {
            solveIt();
        }while(!NoMoreVertex && !soluList.get(soluList.size()-1).equals(end));
        cleanUp();
    }


    private void relax(Vertex v ){

        for(WeightedEdge e : theGraph.neighbors(v)){
            Vertex to = (Vertex)e.to();
            Vertex temp = null ;

            double weight = e.weight();
            double costToVisit = weight + nodeWeight.get(v);
            double nextPriority = theGraph.estimatedDistanceToGoal(to,end) + costToVisit;
            double pastPriority = -1.;
            double nWeight = -1;

//            if( soluList.contains(to)) continue;
            if(visited.contains(to)){
                visited.size();
                continue;
            }
            if (nodeWeight.containsKey(to) ){
                nWeight = nodeWeight.get(to);
                if(Double.compare(nWeight ,nextPriority) < 0 ) continue;
                temp = (Vertex) allVertex.findItem(to,nWeight);
            }
            if(temp != null) allVertex.changePriority(to,nWeight,nextPriority);
            else allVertex.add(to,nextPriority);
//            if ( allVertex.contains(to)){
//                pastPriority = allVertex.findItem(to,0,).getPriority();
//                if(pastPriority <= costToVisit ) continue;
//            }
//            if (Double.compare(pastPriority,-1) > 0) allVertex.changePriority(to,costToVisit);
//            else allVertex.add(to,costToVisit);

            nodeWeight.put(to,costToVisit);
            nodeAndParent.put(to, v);
        }

    }


    private void solveIt(){

        Vertex thisPoint = allVertex.removeSmallest();

        numStates += 1;
        visited.add(thisPoint);
        soluList.add(thisPoint);

//        visited.add(thisPoint);
        double costToTravel;
        if (thisPoint == null ){
            NoMoreVertex = true;
            return;
        }
        if(thisPoint.equals(end)) return;
       relax(thisPoint);


        return;
    }
    public List<Vertex> soluCopy ;
    private void cleanUp(){
        //TODO erroring here due to "pointer exception" probably something to do with init solucopy
        soluCopy = (ArrayList<Vertex>)soluList.clone();
        Collections.copy(soluCopy,soluList);
        if( outcome() == SolverOutcome.SOLVED){
            int arrayItem = soluList.size()-1;
            java.util.function.IntSupplier tr = () -> soluList.size()-1;
            Function<ArrayList<Vertex>,Integer> t = (soluList) -> soluList.size()-1;
            int inner = 0;
            while (arrayItem > 0){

                while(arrayItem > 0 && ! soluList.get(arrayItem-1).equals( nodeAndParent.get(soluList.get(arrayItem)) )){
                    soluList.remove(arrayItem-1);
                    arrayItem = tr.getAsInt();
                }
//                while(arrayItem > 0 && ! soluList.get(arrayItem-1).equals( nodeAndParent.get(soluList.get(arrayItem)) )){
//                    soluList.remove(arrayItem-1);
//                    arrayItem = tr.getAsInt();
//                }
                arrayItem -= 1;
            }

        }

    }
    @Override
    public SolverOutcome outcome() {
       if (NoMoreVertex || !soluList.get(soluList.size()-1).equals(end))return SolverOutcome.UNSOLVABLE;
       else return SolverOutcome.SOLVED;
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
