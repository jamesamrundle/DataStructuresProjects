package huskymaps.server;

import huskymaps.StreetMapGraph;
import huskymaps.server.handler.APIRouteHandler;
import huskymaps.server.handler.impl.ClearRouteAPIHandler;
import huskymaps.server.handler.impl.RasterAPIHandler;
import huskymaps.server.handler.impl.RedirectAPIHandler;
import huskymaps.server.handler.impl.RoutingAPIHandler;
import huskymaps.server.handler.impl.SearchAPIHandler;

import java.util.Map;

import static huskymaps.utils.Constants.HEROKU_DEPLOYMENT;
import static huskymaps.utils.Constants.OSM_DB_PATH;
import static huskymaps.utils.Constants.LARGE_OSM_DB_PATH;
import static huskymaps.utils.Constants.PORT;
import static huskymaps.utils.Constants.SEMANTIC_STREET_GRAPH;
import static spark.Spark.before;
import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.staticFileLocation;

public class MapServer {

    private static final Map<String, APIRouteHandler> HANDLERS = Map.of(
            "/raster", new RasterAPIHandler(),
            "/route", new RoutingAPIHandler(),
            "/clear_route", new ClearRouteAPIHandler(),
            "/search", new SearchAPIHandler(),
            "/", new RedirectAPIHandler()
            );

    /** Entry point for the MapServer. Everything starts here. */
    public static void main(String[] args) {
        port(getPort());

        System.out.println("Working Directory = " +
                System.getProperty("user.dir")); //Could need to change file references. Will comment points with 'FILE-REFERENCE-HERE'

        SEMANTIC_STREET_GRAPH = new StreetMapGraph(LARGE_OSM_DB_PATH);
        staticFileLocation("./static/page"); //FILE-REFERENCE-HERE
        /* Allow for all origin requests (since this is not an authenticated server, we do not
         * care about CSRF).  */
        before((request, response) -> {
            response.header("Access-Control-Allow-Origin", "*");
            response.header("Access-Control-Request-Method", "*");
            response.header("Access-Control-Allow-Headers", "*");
        });

        for (Map.Entry<String, APIRouteHandler> apiRoute : HANDLERS.entrySet()) {
            get(apiRoute.getKey(), apiRoute.getValue());
        }
    }

    private static int getPort() {
        if (HEROKU_DEPLOYMENT) {
            ProcessBuilder processBuilder = new ProcessBuilder();
            if (processBuilder.environment().get("PORT") != null) {
                return Integer.parseInt(processBuilder.environment().get("PORT"));
            }
        }
        return PORT;
    }
}
