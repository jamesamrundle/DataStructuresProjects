package huskymaps.server.logic;

import astar.AStarSolver;
import huskymaps.Node;
import huskymaps.StreetMapGraph;
import huskymaps.params.RouteRequest;
import huskymaps.utils.Constants;
import huskymaps.utils.Spatial;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.IntSupplier;

/** Application logic for the RoutingAPIHandler. */
public class Router {

    /**
     * Overloaded method for shortestPath that has flexibility to specify a solver
     * and returns a List of longs representing the shortest path from the node
     * closest to a start location and the node closest to the destination location.
     * @param g The graph to use.
     * @param request The requested route.
     * @return A list of node id's in the order visited on the shortest path.
     */
    private static List<Long> list;
    public static List<Long> shortestPath(StreetMapGraph g, RouteRequest request) {
        long src = g.closest(request.startLat, request.startLon);
        long dest = g.closest(request.endLat, request.endLon);
        AStarSolver solu = new AStarSolver<>(g, src, dest, 20);

        //double soluWeight =  solu.solutionWeight();
        //double propoWeight  = g.weightOfRoute(huskymaps.utils.Constants.ROUTE_LIST2);
        /* // Following is for displaying all nodes hit in discovering solution
        setList( solu.soluCopy);
        */
        return solu.solution();
        //return null;
    }

    private static void setList(List<Long> l){
        list = l;
    }
    public static List<Long> getList(){return list;}
    /**
     * Create the list of directions corresponding to a route on the graph.
     * @param g The graph to use.
     * @param route The route to translate into directions. Each element
     *              corresponds to a node from the graph in the route.
     * @return A list of NavigationDirections for the route.
     */
    public static List<NavigationDirection> routeDirections(StreetMapGraph g, List<Long> route) {

        ArrayList<NavigationDirection> directions = new ArrayList<>();
        if(route.size() <= 1 )return directions;
            double[] north = {90.0000,0.0};
        Node start = StreetMapGraph.getNode(route.get(0));
        Node next =StreetMapGraph.getNode(route.get(1));
        double prevBearing = Spatial.bearing(start.lon(),start.lat(),next.lon(),next.lat());
        int prevDirect = 0;
        double totalDistance = 0;

        double grandTotal = 0;
        for(int i = 1;i<route.size();i++){


            Node node = StreetMapGraph.getNode(route.get(i));
            double nowBearing = Spatial.bearing(start.lon(), start.lat(), node.lon(), node.lat());
            int directCode = NavigationDirection.getDirection(prevBearing, nowBearing);
            if( ( directCode > 1)  )

//            if(  directCode != prevDirect || (prevDirect == 0  && directCode > 1))
                            {
                    NavigationDirection direct = new NavigationDirection();


                    direct.direction = (prevDirect == 0) ? 0: prevDirect ;
                    direct.distance = totalDistance;
                    direct.way = start.name();
                    directions.add(direct);


                    totalDistance = 0;
                    prevDirect = directCode;
                    prevBearing = nowBearing;
            }

            totalDistance += Spatial.greatCircleDistance(start.lon(),node.lon(),start.lat(),node.lat());
            grandTotal  += Spatial.greatCircleDistance(start.lon(),node.lon(),start.lat(),node.lat());
            prevBearing= nowBearing;
            start = node;
        }
        NavigationDirection direct = new NavigationDirection();
        direct.direction = prevDirect ;
        direct.distance = -totalDistance;
        direct.way = start.name();
        directions.add(direct);
        direct = new NavigationDirection();
        direct.direction = -1;
        direct.distance = grandTotal;
        direct.way = null;
        directions.add(direct);
        return directions;
    }
}
