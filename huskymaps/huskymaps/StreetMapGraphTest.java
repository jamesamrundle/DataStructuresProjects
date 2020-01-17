package huskymaps;

import org.junit.Test;

import java.util.List;

import static huskymaps.utils.Constants.OSM_DB_PATH;
import static huskymaps.utils.Constants.SEMANTIC_STREET_GRAPH;
import static org.junit.Assert.*;

public class StreetMapGraphTest {

    @Test
    public void closest() {
        StreetMapGraph sg = new StreetMapGraph(OSM_DB_PATH);
        long close = sg.closest(47.675195,-122.2891);
        List<String> stringLocations = sg.getLocationsByPrefix("11th");
        List<Node>  nodeLocations = sg.getLocations("10th");
        System.out.println(close);
    }
}