package astar;
import org.junit.Test;

public class AStarSolverTest {

    @Test
    public void initTest(){
        astar.example.WeightedDirectedGraph wdg = new astar.example.WeightedDirectedGraph(7);
        /* Edges from vertex 0. */
        wdg.addEdge(0, 1, 2);
        wdg.addEdge(0, 2, 1);

        wdg.addEdge(1, 2, 5);
        wdg.addEdge(1, 3, 11);
        wdg.addEdge(1, 4, 3);

        wdg.addEdge(2, 5, 15);

        wdg.addEdge(3, 4, 2);

        wdg.addEdge(4, 2, 1);
        wdg.addEdge(4, 5, 4);
        wdg.addEdge(4, 6, 5);

        wdg.addEdge(6, 3, 1);
        wdg.addEdge(6, 5, 1);

        int start = 0;
        int goal = 6;

        AStarSolver<Integer> solver = new AStarSolver<>(wdg, start, goal, 10);

    }

}